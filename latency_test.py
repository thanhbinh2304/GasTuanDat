import requests
import time
import concurrent.futures
import statistics

# Cấu hình test
BASE_URL = "http://localhost:8080/api/v1/purchase-orders" # Thay đổi cổng và API nếu cần
TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyMyIsImlhdCI6MTc4MTcxMzk1NSwiZXhwIjoxNzgxNzE3NTU1LCJyb2xlIjoiQURNSU4ifQ.1ULlMBDwBsvWHCgmajmnTky9bjucKu_exUne21Pq19o" # TODO: Copy JWT token của tài khoản đã đăng nhập vào đây
HEADERS = {
    "Authorization": f"Bearer {TOKEN}"
}
CONCURRENT_USERS = 20 # Số lượng người dùng gửi request đồng thời
TOTAL_REQUESTS = 20 # Tổng số lượng request

from requests.adapters import HTTPAdapter

session = requests.Session()
adapter = HTTPAdapter(pool_connections=1000, pool_maxsize=1000)
session.mount('http://', adapter)
session.mount('https://', adapter)
session.headers.update(HEADERS)

def make_request():
    start_time = time.time()
    try:
        response = session.get(BASE_URL, timeout=10)
        latency = time.time() - start_time
        return response.status_code, latency
    except Exception as e:
        latency = time.time() - start_time
        return 0, latency

def run_load_test():
    print(f"Đang bắt đầu test trên {BASE_URL}")
    print(f"Số lượng Concurrent Users: {CONCURRENT_USERS}")
    print(f"Tổng số Requests: {TOTAL_REQUESTS}")
    
    latencies = []
    status_codes = {}
    
    start_total_time = time.time()
    
    # Sử dụng ThreadPoolExecutor để gửi request đồng thời
    with concurrent.futures.ThreadPoolExecutor(max_workers=CONCURRENT_USERS) as executor:
        futures = [executor.submit(make_request) for _ in range(TOTAL_REQUESTS)]
        
        for future in concurrent.futures.as_completed(futures):
            status_code, latency = future.result()
            latencies.append(latency)
            status_codes[status_code] = status_codes.get(status_code, 0) + 1

    total_time = time.time() - start_total_time
    
    # Tính toán các chỉ số
    latencies.sort()
    avg_latency = statistics.mean(latencies)
    median_latency = statistics.median(latencies)
    p95_latency = latencies[int(len(latencies) * 0.95) - 1]
    p99_latency = latencies[int(len(latencies) * 0.99) - 1]
    requests_per_second = TOTAL_REQUESTS / total_time
    
    print("\n--- KẾT QUẢ TEST (TEST RESULTS) ---")
    print(f"Tổng thời gian chạy (Total Time): {total_time:.2f} giây")
    print(f"Số request/giây (RPS - Requests per Second): {requests_per_second:.2f} req/s")
    print(f"Độ trễ trung bình (Average Latency): {avg_latency * 1000:.2f} ms")
    print(f"Độ trễ trung vị (Median Latency): {median_latency * 1000:.2f} ms")
    print(f"Độ trễ bách phân vị 95 (p95): {p95_latency * 1000:.2f} ms")
    print(f"Độ trễ bách phân vị 99 (p99): {p99_latency * 1000:.2f} ms")
    print("\nChi tiết Status Codes:")
    for code, count in status_codes.items():
        if code == 0:
            print(f"  Failed/Timeout: {count}")
        else:
            print(f"  {code}: {count}")

if __name__ == "__main__":
    run_load_test()
