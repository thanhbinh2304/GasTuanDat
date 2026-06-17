package com.example.GasTuanDat.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(404, "User not found"),
    ROLE_NOT_FOUND(404, "Role not found"),
    POSITION_NOT_FOUND(404, "Position not found"),
    EMPLOYEE_NOT_FOUND(404, "Employee not found"),
    SUPPLIER_NOT_FOUND(404, "Supplier not found"),
    CUSTOMER_NOT_FOUND(404, "Customer not found"),
    CUSTOMER_CODE_ALREADY_EXISTS(409, "Customer code already exists"),
    CUSTOMER_GROUP_NOT_FOUND(404, "Customer group not found"),
    GAS_BOOK_NOT_FOUND(404, "GasBook not found"),
    PROMOTION_CODE_ALREADY_EXISTS(409, "Promotion code already exists"),
    PROMOTION_NOT_FOUND(404, "Promotion not found"),
    REWARD_MILESTONE_NOT_FOUND(404, "Reward milestone not found"),
    PURCHASE_ORDER_NOT_FOUND(404, "Purchase order not found"),
    PURCHASE_DETAIL_NOT_FOUND(404, "Purchase detail not found"),
    PRODUCT_NOT_FOUND(404, "Product not found"),
    STOCK_NOT_FOUND(404, "Stock not found"),
    PRODUCT_ALREADY_EXISTS(409, "Product already exists"),
    PRODUCT_CODE_ALREADY_EXISTS(409, "Product code already exists"),
    ATTRIBUTE_NOT_FOUND(404, "Attribute not found"),
    ATTRIBUTE_ALREADY_EXISTS(409, "Attribute already exists"),
    PRODUCT_ATTRIBUTE_NOT_FOUND(404, "Product attribute not found"),
    PRODUCT_ATTRIBUTE_ALREADY_EXISTS(409, "Product attribute already exists"),
    WARD_NOT_FOUND(404, "Ward not found"),
    ACCOUNT_ALREADY_EXISTS(409, "Account already exists"),
    EMPLOYEE_ALREADY_HAS_ACCOUNT(409, "Employee already has an account"),
    INVALID_INPUT(400, "Invalid input"),
    CURRENT_PASSWORD_INCORRECT(400, "Current password is incorrect"),
    VERIFICATION_CODE_INVALID(400, "Verification code is invalid"),
    VERIFICATION_CODE_EXPIRED(400, "Verification code has expired"),
    PRICE_LIST_NOT_FOUND(404, "Price list not found"),
    PRICE_LIST_ALREADY_EXISTS(409, "Price list already exists"),
    PRODUCT_PRICE_NOT_FOUND(404, "Product price not found"),
    PRODUCT_PRICE_ALREADY_EXISTS(409, "Product price already exists"),
    PRODUCT_CATEGORY_NOT_FOUND(404, "Product category not found"),
    PRODUCT_CATEGORY_ALREADY_EXISTS(409, "Product category already exists"),
    MAIL_SEND_FAILED(503, "Unable to send email"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Don't have permission"),
    TRANSFER_NOT_FOUND(404, "Stock transfer not found"),
    TRANSFER_CODE_ALREADY_EXISTS(409, "Transfer code already exists"),
    STOCK_TAKE_NOT_FOUND(404, "Stock take not found"),
    SALE_INVOICE_NOT_FOUND(404, "Sale invoice not found"),
    INTERNAL_ERROR(500, "Internal server error");

    private final int status;
    private final String message;

}