import interfaces.*;
import models.Product;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class RMIClient {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Kết nối RMI Registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // Lookup tất cả các Service
            HelloService helloService = (HelloService) registry.lookup("interfaces.HelloService");
            CaculatorService calculatorService = (CaculatorService) registry.lookup("interfaces.CaculatorService");
            PrimeService primeService = (PrimeService) registry.lookup("interfaces.PrimeService");
            ContactService contactService = (ContactService) registry.lookup("interfaces.ContactService");
            ChatService chatService = (ChatService) registry.lookup("interfaces.ChatService");
            BankService bankService = (BankService) registry.lookup("interfaces.BankService");
            GeometryService geometryService = (GeometryService) registry.lookup("interfaces.GeometryService");
            FlightBookingService flightService = (FlightBookingService) registry.lookup("interfaces.FlightBookingService");
            AuctionService auctionService = (AuctionService) registry.lookup("interfaces.AuctionService");

            while (true) {
                System.out.println("\n================ MENU TỔNG CÁC BÀI TẬP RMI ================");
                System.out.println("1. Bài 1: Hello World RMI");
                System.out.println("2. Bài 2: Máy tính đơn giản (Caculator)");
                System.out.println("3. Bài 3: Kiểm tra số nguyên tố (Prime)");
                System.out.println("4. Bài 4: Quản lý danh bạ (Contact)");
                System.out.println("5. Bài 5: Chức năng Chat");
                System.out.println("6. Bài 6: Quản lý tài khoản ngân hàng (Bank)");
                System.out.println("7. Bài 7: Tính diện tích hình học (Geometry)");
                System.out.println("8. Bài 8: Đặt vé máy bay (Flight Booking)");
                System.out.println("9. Bài 9: Đấu giá trực tuyến (Auction)");
                System.out.println("0. Thoát chương trình");
                System.out.print("Chọn bài tập muốn chạy (0-9): ");

                int choice = readInt(scanner);

                switch (choice) {
                    case 1:
                        handleHello(helloService);
                        break;
                    case 2:
                        handleCalculator(scanner, calculatorService);
                        break;
                    case 3:
                        handlePrime(scanner, primeService);
                        break;
                    case 4:
                        handleContact(scanner, contactService);
                        break;
                    case 5:
                        handleChat(scanner, chatService);
                        break;
                    case 6:
                        handleBank(scanner, bankService);
                        break;
                    case 7:
                        handleGeometry(scanner, geometryService);
                        break;
                    case 8:
                        handleFlight(scanner, flightService);
                        break;
                    case 9:
                        handleAuction(scanner, auctionService);
                        break;
                    case 0:
                        System.out.println("Đã thoát chương trình RMI Client!");
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= BÀI 1: HELLO SERVICE =================
    private static void handleHello(HelloService service) throws Exception {
        System.out.println("\n--- BÀI 1: HELLO WORLD RMI ---");
        String message = service.sayHello();
        System.out.println("Kết quả từ Server: " + message);
    }

    // ================= BÀI 2: CALCULATOR SERVICE =================
    private static void handleCalculator(Scanner scanner, CaculatorService service) throws Exception {
        System.out.println("\n--- BÀI 2: MÁY TÍNH ĐƠN GIẢN ---");
        System.out.print("Nhập số a: ");
        int a = readInt(scanner);
        System.out.print("Nhập số b: ");
        int b = readInt(scanner);

        int result = service.add(a, b);
        System.out.println("-> Kết quả phép cộng từ Server: " + a + " + " + b + " = " + result);
    }

    // ================= BÀI 3: PRIME SERVICE =================
    private static void handlePrime(Scanner scanner, PrimeService service) throws Exception {
        System.out.println("\n--- BÀI 3: KIỂM TRA SỐ NGUYÊN TỐ ---");
        System.out.print("Nhập số n cần kiểm tra: ");
        int n = readInt(scanner);

        boolean check = service.isPrime(n);
        if (check) {
            System.out.println("-> Kết quả từ Server: " + n + " là số nguyên tố.");
        } else {
            System.out.println("-> Kết quả từ Server: " + n + " không phải là số nguyên tố.");
        }
    }

    // ================= BÀI 4: CONTACT SERVICE =================
    private static void handleContact(Scanner scanner, ContactService service) throws Exception {
        while (true) {
            System.out.println("\n--- BÀI 4: QUẢN LÝ DANH BẠ ---");
            System.out.println("1. Thêm/Cập nhật liên hệ");
            System.out.println("2. Tìm kiếm số điện thoại");
            System.out.println("3. Xóa liên hệ");
            System.out.println("0. Quay lại Menu chính");
            System.out.print("Chọn chức năng (0-3): ");

            int choice = readInt(scanner);
            if (choice == 0) break;

            switch (choice) {
                case 1:
                    System.out.print("Nhập tên liên hệ: ");
                    String nameAdd = scanner.nextLine().trim();
                    System.out.print("Nhập số điện thoại: ");
                    String phone = scanner.nextLine().trim();

                    service.addContact(nameAdd, phone);
                    System.out.println("-> Đã thêm/cập nhật thành công!");
                    break;
                case 2:
                    System.out.print("Nhập tên cần tìm: ");
                    String nameFind = scanner.nextLine().trim();

                    String resultPhone = service.findContact(nameFind);
                    if (resultPhone != null) {
                        System.out.println("-> SĐT của [" + nameFind + "]: " + resultPhone);
                    } else {
                        System.out.println("-> Không tìm thấy [" + nameFind + "] trong danh bạ!");
                    }
                    break;
                case 3:
                    System.out.print("Nhập tên cần xóa: ");
                    String nameDelete = scanner.nextLine().trim();

                    boolean isDeleted = service.deleteContact(nameDelete);
                    if (isDeleted) {
                        System.out.println("-> Đã xóa liên hệ [" + nameDelete + "] thành công!");
                    } else {
                        System.out.println("-> Không tìm thấy liên hệ [" + nameDelete + "] để xóa!");
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    // ================= BÀI 5: CHAT SERVICE =================
    private static void handleChat(Scanner scanner, ChatService service) throws Exception {
        System.out.println("\n--- BÀI 5: PHIÊN CHAT VỚI SERVER ---");
        System.out.println("(Nhập 'exit' hoặc 'quit' để kết thúc)");

        while (true) {
            System.out.print("[Bạn (Client)]: ");
            String msg = scanner.nextLine().trim();

            if (msg.equalsIgnoreCase("exit") || msg.equalsIgnoreCase("quit")) {
                System.out.println("-> Đã kết thúc phiên chat.");
                break;
            }

            if (msg.isEmpty()) continue;

            String response = service.sendMessage(msg);
            System.out.println("[Server]: " + response);
        }
    }

    // ================= BÀI 6: BANK SERVICE =================
    private static void handleBank(Scanner scanner, BankService service) throws Exception {
        while (true) {
            System.out.println("\n--- BÀI 6: QUẢN LÝ TÀI KHOẢN NGÂN HÀNG ---");
            System.out.println("1. Kiểm tra số dư");
            System.out.println("2. Nạp tiền");
            System.out.println("3. Rút tiền");
            System.out.println("0. Quay lại Menu chính");
            System.out.print("Chọn chức năng (0-3): ");

            int choice = readInt(scanner);
            if (choice == 0) break;

            switch (choice) {
                case 1:
                    double balance = service.getBalance();
                    System.out.printf("-> Số dư hiện tại: %,.2f VNĐ\n", balance);
                    break;
                case 2:
                    System.out.print("Nhập số tiền gửi: ");
                    double deposit = readDouble(scanner);
                    if (deposit <= 0) {
                        System.out.println("-> Số tiền phải lớn hơn 0!");
                    } else {
                        service.deposit(deposit);
                        System.out.printf("-> Gửi thành công %,.2f VNĐ.\n", deposit);
                    }
                    break;
                case 3:
                    System.out.print("Nhập số tiền rút: ");
                    double withdraw = readDouble(scanner);
                    if (withdraw <= 0) {
                        System.out.println("-> Số tiền phải lớn hơn 0!");
                    } else {
                        boolean ok = service.withdraw(withdraw);
                        if (ok) {
                            System.out.printf("-> Rút thành công %,.2f VNĐ.\n", withdraw);
                        } else {
                            System.out.println("-> Rút thất bại! Số dư không đủ.");
                        }
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    // ================= BÀI 7: GEOMETRY SERVICE =================
    private static void handleGeometry(Scanner scanner, GeometryService service) throws Exception {
        while (true) {
            System.out.println("\n--- BÀI 7: TÍNH DIỆN TÍCH HÌNH HỌC ---");
            System.out.println("1. Diện tích Hình chữ nhật");
            System.out.println("2. Diện tích Hình tròn");
            System.out.println("3. Diện tích Hình tam giác");
            System.out.println("0. Quay lại Menu chính");
            System.out.print("Chọn chức năng (0-3): ");

            int choice = readInt(scanner);
            if (choice == 0) break;

            switch (choice) {
                case 1:
                    System.out.print("Nhập chiều rộng: ");
                    double w = readDouble(scanner);
                    System.out.print("Nhập chiều cao: ");
                    double h = readDouble(scanner);
                    System.out.printf("-> Diện tích HCN: %,.2f\n", service.rectangleArea(w, h));
                    break;
                case 2:
                    System.out.print("Nhập bán kính: ");
                    double r = readDouble(scanner);
                    System.out.printf("-> Diện tích Hình tròn: %,.2f\n", service.circleArea(r));
                    break;
                case 3:
                    System.out.print("Nhập chiều dài đáy: ");
                    double b = readDouble(scanner);
                    System.out.print("Nhập chiều cao: ");
                    double th = readDouble(scanner);
                    System.out.printf("-> Diện tích Tam giác: %,.2f\n", service.triangleArea(b, th));
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    // ================= BÀI 8: FLIGHT BOOKING SERVICE =================
    private static void handleFlight(Scanner scanner, FlightBookingService service) throws Exception {
        while (true) {
            System.out.println("\n--- BÀI 8: HỆ THỐNG ĐẶT VÉ MÁY BAY ---");
            System.out.println("1. Kiểm tra số ghế trống");
            System.out.println("2. Đặt vé");
            System.out.println("3. Hủy vé");
            System.out.println("0. Quay lại Menu chính");
            System.out.print("Chọn chức năng (0-3): ");

            int choice = readInt(scanner);
            if (choice == 0) break;

            System.out.print("Nhập mã chuyến bay (VD: VN123, VJ456): ");
            String flightNo = scanner.nextLine().trim();

            switch (choice) {
                case 1:
                    int seats = service.availableSeats(flightNo);
                    if (seats >= 0) {
                        System.out.printf("-> Số ghế trống của chuyến [%s]: %d ghế.\n", flightNo, seats);
                    } else {
                        System.out.println("-> Không tìm thấy chuyến bay!");
                    }
                    break;
                case 2:
                    System.out.print("Nhập số ghế muốn đặt: ");
                    int bookSeats = readInt(scanner);
                    if (service.bookTicket(flightNo, bookSeats)) {
                        System.out.println("-> Đặt vé thành công!");
                    } else {
                        System.out.println("-> Đặt vé thất bại! Chuyến bay không tồn tại hoặc không đủ ghế.");
                    }
                    break;
                case 3:
                    System.out.print("Nhập số ghế muốn hủy: ");
                    int cancelSeats = readInt(scanner);
                    if (service.cancelBooking(flightNo, cancelSeats)) {
                        System.out.println("-> Hủy vé thành công!");
                    } else {
                        System.out.println("-> Hủy vé thất bại!");
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    // ================= BÀI 9: AUCTION SERVICE =================
    private static void handleAuction(Scanner scanner, AuctionService service) throws Exception {
        while (true) {
            System.out.println("\n--- BÀI 9: HỆ THỐNG ĐẤU GIÁ TRỰC TUYẾN ---");
            System.out.println("1. [Chủ sở hữu] Đăng sản phẩm đấu giá");
            System.out.println("2. [Người đấu giá] Xem danh sách sản phẩm");
            System.out.println("3. [Người đấu giá] Đưa ra giá thầu");
            System.out.println("0. Quay lại Menu chính");
            System.out.print("Chọn chức năng (0-3): ");

            int choice = readInt(scanner);
            if (choice == 0) break;

            switch (choice) {
                case 1:
                    System.out.print("Nhập mã sản phẩm: ");
                    String id = scanner.nextLine().trim();
                    System.out.print("Nhập tên sản phẩm: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Nhập giá khởi điểm: ");
                    double initPrice = readDouble(scanner);

                    if (service.addProduct(id, name, initPrice)) {
                        System.out.println("-> Đã thêm sản phẩm thành công!");
                    } else {
                        System.out.println("-> Thêm thất bại! Mã SP đã tồn tại hoặc giá không hợp lệ.");
                    }
                    break;
                case 2:
                    List<Product> products = service.getAllProducts();
                    if (products.isEmpty()) {
                        System.out.println("-> Hiện chưa có sản phẩm nào đang đấu giá.");
                    } else {
                        System.out.println("\n--- DANH SÁCH SẢN PHẨM ---");
                        for (Product p : products) {
                            System.out.println(" + " + p);
                        }
                    }
                    break;
                case 3:
                    System.out.print("Nhập tên của bạn: ");
                    String bidder = scanner.nextLine().trim();
                    System.out.print("Nhập mã SP muốn đấu giá: ");
                    String pId = scanner.nextLine().trim();
                    System.out.print("Nhập giá thầu của bạn: ");
                    double bidAmount = readDouble(scanner);

                    if (service.placeBid(pId, bidAmount, bidder)) {
                        System.out.println("-> Đặt giá thành công! Bạn đang trả giá cao nhất.");
                    } else {
                        System.out.println("-> Đặt giá thất bại! Giá thầu phải lớn hơn giá hiện tại.");
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    // ================= UTILITY METHODS =================
    private static int readInt(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Vui lòng nhập số nguyên hợp lệ: ");
            }
        }
    }

    private static double readDouble(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Vui lòng nhập số hợp lệ: ");
            }
        }
    }
}