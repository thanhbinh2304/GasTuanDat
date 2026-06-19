# Gas Tuấn Đạt - Backend API Server

## 📖 Mô tả dự án
Hệ thống Backend (RESTful API) phục vụ phần mềm Quản lý Phân phối và Bán lẻ **Gas Tuấn Đạt**. Dự án cung cấp giải pháp số hóa toàn diện quy trình kinh doanh, từ khâu quản lý kho bãi, xuất/nhập hàng, theo dõi vỏ bình gas đến kiểm soát dòng tiền và công nợ.

## ✨ Tính năng nổi bật (Nghiệp vụ cốt lõi)
- **Quản lý Bán hàng & Sổ Gas:** Tạo và quản lý hóa đơn bán hàng, theo dõi chi tiết vỏ bình gas khách hàng đang mượn/trả.
- **Quản lý Kho bãi:** Theo dõi lượng tồn kho thực tế, lập phiếu nhập kho từ nhà cung cấp, xử lý phiếu xuất kho và luân chuyển hàng hóa giữa các kho nội bộ.
- **Quản lý Sổ quỹ & Công nợ:** Ghi nhận phiếu thu, phiếu chi, tự động hạch toán dòng tiền. Quản lý chi tiết công nợ của từng khách hàng và nhà cung cấp.
- **Thống kê & Báo cáo (Dashboard):** Tổng hợp doanh thu, đếm lượng bình gas bán ra, liệt kê Top khách hàng & Top sản phẩm bán chạy theo thời gian thực.
- **Quản lý Đối tác & Nhân sự:** Lưu trữ và quản lý thông tin khách hàng, nhóm khách hàng, nhà cung cấp, và nhân viên giao hàng.
- **Bảo mật & Phân quyền:** Đăng nhập bảo mật qua JWT, phân quyền truy cập, tính năng gửi Email cấp lại mật khẩu (Forgot Password).

## 🚀 Tech Stack
- **Ngôn ngữ:** Java 21 
- **Framework:** Spring Boot 3.2.x
- **Bảo mật:** Spring Security & JWT (JSON Web Token)
- **Cơ sở dữ liệu:** PostgreSQL (Spring Data JPA / Hibernate)
- **Caching:** Caffeine Cache
- **Build Tool:** Gradle
- **Containerization:** Docker & Docker Compose

## 🏛️ Kiến trúc và Cấu trúc dự án

### Kiến trúc phần mềm
Hệ thống được xây dựng theo mô hình **Monolithic Architecture (Kiến trúc nguyên khối)** áp dụng mẫu thiết kế **Layered Architecture (Kiến trúc phân tầng)**:
1. **Controller Layer:** Tiếp nhận HTTP Request, gọi tầng Service và trả về `ApiResponse` chuẩn mực.
2. **Service Layer:** Chứa toàn bộ logic nghiệp vụ (Business Logic).
3. **Repository Layer:** Kế thừa `JpaRepository` để tương tác với Database (Sử dụng cả Native SQL Query và JPQL).
4. **Data Layer (Entity & DTO):** Áp dụng mô hình DTO (Data Transfer Object) và MapStruct để tách biệt cấu trúc DB (`Entity`) khỏi dữ liệu trả về cho Client (`Response DTO`).

### Cấu trúc thư mục (Directory Structure)
```text
src/main/java/com/example/GasTuanDat/
├── auth/           # Đăng nhập, JWT, Quên mật khẩu
├── cashReceipt/    # Quản lý Phiếu Thu
├── payment/        # Quản lý Phiếu Chi
├── common/         # Cấu hình chung (Cache, CORS, Exception Handler, Security)
├── customer/       # Quản lý khách hàng, nhóm khách hàng
├── employee/       # Quản lý nhân viên
├── product/        # Quản lý sản phẩm, danh mục, thuộc tính
├── purchase/       # Nhập hàng (Purchase Orders)
├── sale/           # Bán hàng (Sale Invoices), Sổ Gas
├── stock/          # Quản lý kho, tồn kho
├── stockTransfer/  # Chuyển kho nội bộ
├── supplier/       # Quản lý nhà cung cấp
└── report/         # Xử lý truy vấn tổng hợp dữ liệu cho Dashboard
```

## 📚 API Documentation (Swagger)
Toàn bộ tài liệu API được tự động hóa bằng **Swagger UI**.
Sau khi chạy dự án, truy cập tài liệu API tại:
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`

## 🛠 Cách chạy project

### Cách 1: Chạy trực tiếp (Local Development)
**Yêu cầu:** JDK 21, PostgreSQL 14+, Gradle.
1. Clone dự án:
   ```bash
   git clone <repo_url>
   cd GasTuanDat
   ```
2. Cấu hình Database trong file `application.yml` hoặc truyền biến môi trường.
3. Khởi chạy:
   ```bash
   ./gradlew bootRun
   ```

### Cách 2: Chạy bằng Docker Compose
```bash
docker-compose up -d --build
```
API sẽ hoạt động tại cổng `8080`.

## ⚙️ Environment Variables
Các biến môi trường cần thiết (tạo file `.env`):

```env
DB_URL=jdbc:postgresql://localhost:5432/gastuandat
DB_USERNAME=postgres
DB_PASSWORD=your_password
APP_SECRET=your_super_secret_jwt_key
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
MAIL_FROM_ADDRESS=noreply@gastuandat.com
APP_BACKEND_URL=http://localhost:8080/api/v1
```
## 🌐 Demo URL
- **Frontend Dashboard:** `https://nppgastuandat.vercel.app`
