# Gas Tuấn Đạt - Backend API Server

Hệ thống Backend (RESTful API) cho dự án Quản lý phân phối Gas Tuấn Đạt. Được xây dựng dựa trên kiến trúc Microservices tinh gọn sử dụng Spring Boot 3 và Java 21, tối ưu hóa cho môi trường triển khai có tài nguyên hạn chế (Render 512MB RAM).

## 🚀 Công nghệ sử dụng
- **Ngôn ngữ:** Java 21 (hỗ trợ Virtual Threads)
- **Framework:** Spring Boot 3.2.x, Spring Security, Spring Data JPA
- **Database:** PostgreSQL
- **Caching:** Caffeine Cache (JSON String Caching)
- **Bảo mật:** JWT (JSON Web Tokens)
- **Containerization:** Docker & Docker Compose
- **Build Tool:** Gradle

## ⚙️ Tính năng nổi bật
- **Hiệu năng cao:** Tích hợp `Caffeine Cache` để lưu trữ dữ liệu dạng JSON String, giảm tải cho Garbage Collector và Jackson Serialization.
- **Tối ưu RAM:** Cấu hình Docker với `-Xmx300m` để ngăn chặn lỗi OOM (Out Of Memory) trên các máy chủ có RAM nhỏ.
- **Virtual Threads:** Khai thác sức mạnh của Java 21 để xử lý đồng thời hàng nghìn request mà không tốn nhiều bộ nhớ luồng (Thread Starvation).
- **Dashboard Aggregation:** Thực thi tính toán và tổng hợp dữ liệu siêu tốc bằng `Native SQL Query` trực tiếp trong Database thay vì xử lý trên memory.

## 🛠 Hướng dẫn cài đặt và chạy (Local)

### Yêu cầu hệ thống
- JDK 21
- PostgreSQL 14+
- Gradle

### Các bước cài đặt
1. **Clone repository và di chuyển vào thư mục:**
   ```bash
   cd GasTuanDat
   ```
2. **Cấu hình Database:**
   Tạo cơ sở dữ liệu trên PostgreSQL. Mở file `src/main/resources/application.yml` (hoặc cấu hình biến môi trường) để thay đổi chuỗi kết nối (url, username, password).
3. **Chạy ứng dụng:**
   ```bash
   ./gradlew bootRun
   ```
   *Ứng dụng sẽ chạy mặc định ở cổng `8080`.*

### Build và Chạy bằng Docker
```bash
docker build -t gastuandat-api .
docker run -d -p 8080:8080 gastuandat-api
```

## 📂 Cấu trúc thư mục (Packages)
- `common/`: Chứa cấu hình bảo mật, cache, xử lý exception chung.
- `auth/`: Xử lý đăng nhập, cấp phát JWT, Reset mật khẩu.
- `sale/`: Quản lý hóa đơn bán hàng (Sale Invoices).
- `stock/` & `stockTransfer/`: Quản lý kho bãi, chuyển kho.
- `cashReceipt/` & `payment/`: Thu chi, sổ quỹ.
- `report/`: Controller và Service chuyên dụng xử lý logic thống kê cho Dashboard.
