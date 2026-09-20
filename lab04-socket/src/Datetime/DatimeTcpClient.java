package Datetime;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DatimeTcpClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 6000;

    public static void main(String[] args) {
        System.out.println("--- TCP DATE/TIME CLIENT ---");
        try (
                Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Kết nối TCP thành công!");
            System.out.println("Nhập các lệnh: DATE, TIME, DATETIME, QUIT");

            while (true) {
                System.out.print("TCP Client > ");
                String command = scanner.nextLine();

                // Gửi lệnh lên Server
                writer.write(command);
                writer.newLine();
                writer.flush();

                // Nếu gửi QUIT thì chủ động ngắt kết nối
                if ("QUIT".equalsIgnoreCase(command.trim())) {
                    System.out.println("Đã ngắt kết nối TCP.");
                    break;
                }

                // Nhận và hiển thị phản hồi từ Server
                String response = reader.readLine();
                if (response == null) {
                    System.err.println("Lỗi: Server đã đóng kết nối đột ngột!");
                    break;
                }
                System.out.println("Server trả về: " + response);
            }
        } catch (IOException e) {
            System.err.println("Lỗi kết nối TCP: " + e.getMessage());
        }
    }
}
