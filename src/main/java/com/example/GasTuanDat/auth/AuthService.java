package com.example.GasTuanDat.auth;

import com.example.GasTuanDat.account.AccountRepository;
import com.example.GasTuanDat.account.entity.AccountEntity;
import com.example.GasTuanDat.auth.dtos.ApproveForgotPasswordRequest;
import com.example.GasTuanDat.auth.dtos.ApproveForgotPasswordResponse;
import com.example.GasTuanDat.auth.dtos.ChangePasswordCodeResponse;
import com.example.GasTuanDat.auth.dtos.ChangePasswordRequest;
import com.example.GasTuanDat.auth.dtos.ChangePasswordResponse;
import com.example.GasTuanDat.auth.dtos.ForgotPasswordRequest;
import com.example.GasTuanDat.auth.dtos.ForgotPasswordResponse;
import com.example.GasTuanDat.auth.dtos.LoginRequest;
import com.example.GasTuanDat.auth.dtos.LoginResponse;
import com.example.GasTuanDat.auth.entity.PasswordChangeVerificationEntity;
import com.example.GasTuanDat.auth.entity.PasswordResetRequestEntity;
import com.example.GasTuanDat.auth.entity.PasswordResetStatus;
import com.example.GasTuanDat.auth.entity.TokenEntity;
import com.example.GasTuanDat.common.constants.RoleConstants;
import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.employee.entities.EmployeeEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthService {
        private final PasswordEncoder passwordEncoder;
        private final AccountRepository accountRepository;
        private final TokenRepository tokenRepository;
        private final PasswordResetRequestRepository passwordResetRequestRepository;
        private final PasswordChangeVerificationRepository passwordChangeVerificationRepository;
        private final JavaMailSender mailSender;

        @NonFinal
        @Value("${jwt.signerKey}")
        protected String SIGNER_KEY;

        @NonFinal
        @Value("${jwt.expiration:3600}")
        protected long accessExpirationSeconds;

        @NonFinal
        @Value("${jwt.refreshExpiration:604800}")
        protected long refreshExpirationSeconds;

        @NonFinal
        @Value("${app.ownerEmail:}")
        protected String ownerEmail;

        @NonFinal
        @Value("${app.mailFrom:}")
        protected String mailFrom;

        @NonFinal
        @Value("${app.backendBaseUrl:http://localhost:8080/api/v1}")
        protected String backendBaseUrl;

        private static final String PASSWORD_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
        private static final int DEFAULT_PASSWORD_LENGTH = 12;
        private static final int VERIFICATION_CODE_LENGTH = 6;
        private static final int VERIFICATION_CODE_EXPIRY_MINUTES = 10;
        private static final SecureRandom SECURE_RANDOM = new SecureRandom();

        public LoginResponse login(LoginRequest request) {
                AccountEntity account = accountRepository.findByUsername(request.getUsername())
                                .orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED));

                boolean passwordMatches = passwordEncoder.matches(request.getPassword(), account.getPassword());
                if (!passwordMatches) {
                        throw new ApiException(ErrorCode.UNAUTHORIZED);
                }

                long now = System.currentTimeMillis();
                long accessExpiresAt = now + accessExpirationSeconds * 1000;
                long refreshExpiresAt = now + refreshExpirationSeconds * 1000;

                SecretKey key = Keys.hmacShaKeyFor(SIGNER_KEY.getBytes(StandardCharsets.UTF_8));
                String roleClaim = resolveRoleClaim(account.getRole() != null ? account.getRole().getRoleName() : null);

                String accessToken = Jwts.builder()
                                .setSubject(account.getUsername())
                                .setIssuedAt(new Date(now))
                                .setExpiration(new Date(accessExpiresAt))
                                .claim("role", roleClaim)
                                .signWith(key, SignatureAlgorithm.HS256)
                                .compact();

                String refreshToken = Jwts.builder()
                                .setSubject(account.getUsername())
                                .setIssuedAt(new Date(now))
                                .setExpiration(new Date(refreshExpiresAt))
                                .claim("type", "refresh")
                                .signWith(key, SignatureAlgorithm.HS256)
                                .compact();

                TokenEntity tokenEntity = tokenRepository.findByAccountId(account.getAccountId())
                                .orElseGet(() -> TokenEntity.builder()
                                                .accountId(account.getAccountId())
                                                .build());

                tokenEntity.setRefreshToken(refreshToken);
                tokenEntity.setExpiresAt(new java.sql.Timestamp(refreshExpiresAt));
                tokenRepository.save(tokenEntity);

                return LoginResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .tokenType("Bearer")
                                .expiresIn(accessExpirationSeconds)
                                .build();
        }

        @Transactional
        public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
                AccountEntity account = accountRepository.findByUsername(request.getUsername())
                                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

                EmployeeEntity employee = account.getEmployee();
                if (employee == null || employee.getEmail() == null || employee.getEmail().isBlank()) {
                        throw new ApiException(ErrorCode.INVALID_INPUT);
                }

                AccountEntity adminAccount = accountRepository.findByUsername("admin123")
                                .orElseThrow(() -> new ApiException(ErrorCode.INTERNAL_ERROR));
                EmployeeEntity adminEmployee = adminAccount.getEmployee();
                if (adminEmployee == null || adminEmployee.getEmail() == null || adminEmployee.getEmail().isBlank()) {
                        throw new ApiException(ErrorCode.INTERNAL_ERROR);
                }
                String adminEmail = adminEmployee.getEmail();

                PasswordResetRequestEntity resetRequest = PasswordResetRequestEntity.builder()
                                .accountId(account.getAccountId())
                                .username(account.getUsername())
                                .employeeEmail(employee.getEmail())
                                .approvalToken(UUID.randomUUID())
                                .status(PasswordResetStatus.PENDING)
                                .build();

                PasswordResetRequestEntity savedRequest = passwordResetRequestRepository.save(resetRequest);
                try {
                        sendOwnerResetRequest(savedRequest, adminEmail);
                } catch (MailException e) {
                        log.error("Failed to send password reset approval email", e);
                        throw new ApiException(ErrorCode.MAIL_SEND_FAILED);
                }

                return ForgotPasswordResponse.builder()
                                .requestId(savedRequest.getRequestId())
                                .status(savedRequest.getStatus().name())
                                .build();
        }

        @Transactional
        public ApproveForgotPasswordResponse approveForgotPassword(ApproveForgotPasswordRequest request) {
                PasswordResetRequestEntity resetRequest = passwordResetRequestRepository
                                .findById(request.getRequestId())
                                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_INPUT));

                if (resetRequest.getStatus() != PasswordResetStatus.PENDING) {
                        throw new ApiException(ErrorCode.INVALID_INPUT);
                }

                if (!resetRequest.getApprovalToken().equals(request.getApprovalToken())) {
                        throw new ApiException(ErrorCode.INVALID_INPUT);
                }

                AccountEntity account = accountRepository.findById(resetRequest.getAccountId())
                                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

                String newPassword = generatePassword(DEFAULT_PASSWORD_LENGTH);
                account.setPassword(passwordEncoder.encode(newPassword));
                accountRepository.save(account);

                resetRequest.setStatus(PasswordResetStatus.APPROVED);
                resetRequest.setProcessedAt(new Timestamp(System.currentTimeMillis()));
                passwordResetRequestRepository.save(resetRequest);

                try {
                        sendEmployeeNewPassword(resetRequest, newPassword);
                } catch (MailException e) {
                        log.error("Failed to send new password email", e);
                        throw new ApiException(ErrorCode.MAIL_SEND_FAILED);
                }

                return ApproveForgotPasswordResponse.builder()
                                .requestId(resetRequest.getRequestId())
                                .status(resetRequest.getStatus().name())
                                .build();
        }

        @Transactional
        public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
                AccountEntity account = getCurrentEmployeeAccount();

                if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
                        throw new ApiException(ErrorCode.CURRENT_PASSWORD_INCORRECT);
                }

                account.setPassword(passwordEncoder.encode(request.getNewPassword()));
                accountRepository.save(account);
                tokenRepository.deleteByAccountId(account.getAccountId());

                return ChangePasswordResponse.builder()
                                .status("CHANGED")
                                .build();
        }

        private void sendOwnerResetRequest(PasswordResetRequestEntity request, String adminEmail) {
                String approveLink = backendBaseUrl + "/forgot-password-approval.html?requestId="
                                + request.getRequestId() + "&approvalToken=" + request.getApprovalToken();

                String html = """
                                <html>
                                <body style="font-family:Arial,sans-serif;line-height:1.5;color:#222">
                                        <h2>Yêu cầu cấp lại mật khẩu</h2>
                                        <p>Tài khoản %s yêu cầu cấp lại mật khẩu từ Hệ thống quản lý nhà phân phối Gas Tuấn Đạt.</p>

                                </body>
                                </html>
                                """
                                .formatted(request.getUsername(), approveLink);

                sendHtmlMail(adminEmail, "Password reset approval request", html);
        }

        private void sendEmployeeNewPassword(PasswordResetRequestEntity request, String newPassword) {
                String html = """
                                <html>
                                <body style="font-family:Arial,sans-serif;line-height:1.5;color:#222">
                                        <h2>Mật khẩu mới của bạn</h2>
                                        <p>Tài khoản <b>%s</b> đã được cấp lại mật khẩu.</p>
                                        <p><b>Mật khẩu mới:</b> %s</p>
                                        <p>Vui lòng đăng nhập và đổi mật khẩu ngay sau khi vào hệ thống.</p>
                                </body>
                                </html>
                                """.formatted(request.getUsername(), newPassword);

                sendHtmlMail(request.getEmployeeEmail(), "Your new password", html);
        }

        private void sendChangePasswordCodeMail(String to, String username, String verificationCode) {
                String html = """
                                <html>
                                <body style="font-family:Arial,sans-serif;line-height:1.5;color:#222">
                                        <h2>Mã xác minh đổi mật khẩu</h2>
                                        <p>Tài khoản <b>%s</b> đang yêu cầu đổi mật khẩu.</p>
                                        <p>Mã xác minh 6 số của bạn là:</p>
                                        <div style="font-size:28px;font-weight:bold;letter-spacing:6px;padding:14px 18px;background:#f3f7f8;border-radius:12px;display:inline-block">%s</div>
                                        <p>Mã này có hiệu lực trong %s phút.</p>
                                        <p>Nếu bạn không yêu cầu đổi mật khẩu, hãy bỏ qua email này.</p>
                                </body>
                                </html>
                                """
                                .formatted(username, verificationCode, VERIFICATION_CODE_EXPIRY_MINUTES);

                sendHtmlMail(to, "Your password change verification code", html);
        }

        private String resolveFromAddress() {
                if (mailFrom != null && !mailFrom.isBlank()) {
                        return mailFrom;
                }
                if (ownerEmail != null && !ownerEmail.isBlank()) {
                        return ownerEmail;
                }
                throw new ApiException(ErrorCode.INTERNAL_ERROR);
        }

        private AccountEntity getCurrentEmployeeAccount() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
                        throw new ApiException(ErrorCode.UNAUTHORIZED);
                }

                AccountEntity account = accountRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

                if (account.getEmployee() == null || account.getEmployee().getEmail() == null
                                || account.getEmployee().getEmail().isBlank()) {
                        throw new ApiException(ErrorCode.INVALID_INPUT);
                }

                return account;
        }

        private String generateVerificationCode() {
                int code = SECURE_RANDOM.nextInt(1_000_000);
                return String.format(Locale.ROOT, "%06d", code);
        }

        private String resolveRoleClaim(String roleName) {
                String canonicalRoleName = RoleConstants.canonicalize(roleName);
                if (canonicalRoleName == null) {
                        throw new ApiException(ErrorCode.INVALID_INPUT);
                }
                return canonicalRoleName;
        }

        private void sendHtmlMail(String to, String subject, String html) {
                try {
                        MimeMessage mimeMessage = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
                        helper.setTo(to);
                        helper.setFrom(resolveFromAddress());
                        helper.setSubject(subject);
                        helper.setText(html, true);
                        mailSender.send(mimeMessage);
                } catch (MessagingException e) {
                        log.error("Failed to build HTML email", e);
                        throw new ApiException(ErrorCode.MAIL_SEND_FAILED);
                }
        }

        private String generatePassword(int length) {
                StringBuilder builder = new StringBuilder(length);
                for (int i = 0; i < length; i++) {
                        int index = SECURE_RANDOM.nextInt(PASSWORD_CHARS.length());
                        builder.append(PASSWORD_CHARS.charAt(index));
                }
                return builder.toString();
        }
}
