# Hệ thống quản lý bán hàng

Đây là Hệ thống quản lý bán hàng được xây dựng dưới dạng ứng dụng GUI dành cho máy tính để bàn được phát triển bằng ***Java*** sử dụng ***MySQL*** làm cơ sở dữ liệu.
GUI được thiết kế bằng **Swing** và kết nối cơ sở dữ liệu được quản lý bằng **JDBC API**.

Hệ thống bao gồm những thực thể sau:
- Products (sẩn phẩm)
- Customers  (khách hàng)
- Suppliers (nhà cung cấp)
- Users (người dùng)
- Transactions (giao dịch)

  ## Tính năng của ứng dụng

- Người dùng có thể quản lý kho hàng và tồn kho của tất cả các sản phẩm có sẵn trong cửa hàng của mình.
- Người dùng có thể quản lý mọi giao dịch mua bán do cửa hàng thực hiện.
- Hỗ trợ 2 loại người dùng:
  1. Quản trị viên
  2. Nhân viên
  
  [Quản trị viên có khả năng quản lý tất cả các nhân sự khác.]
- Bất kỳ giao dịch nào được thực hiện đều tự động xử lý tình trạng còn hàng trong kho.
- Mỗi phần đều có tính năng tìm kiếm giúp người dùng dễ dàng xem những dữ liệu mình muốn xem hơn.
- Người dùng chỉ cần nhập mã sản phẩm khi bán hàng và mọi thông tin chi tiết liên quan sẽ tự động được truy xuất từ ​​cơ sở dữ liệu.
- Duy trì nhật ký thời gian của tất cả người dùng sử dụng ứng dụng.

##Cách tải và chạy phần mềm

#### Yêu cầu tối thiểu: JDK hoặc JRE phiên bản 16.
- tải MySQL , Mysql workbrench, tạo table inventory, import sql file ở SQL/ vào và chạy
- lib đã cài sẵn ờ file lib nên không cần import thêm
- sau đó vào config file database/. Thay đổi phần mkcuaban bằng mật khẩu mysql của bạn

