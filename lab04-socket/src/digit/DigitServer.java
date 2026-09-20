package digit;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class DigitServer {
    private static final int PORT = 5000;
    private static final String[] DIGIT_WORDS = {
            "không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"
    };

    public static void main(String[] args) {
        System.out.println("Server TCP Bài 2 đang chạy tại port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client mới kết nối: " + clientSocket.getRemoteSocketAddress());

                // Xử lý client trong luồng riêng
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Kiểm tra lệnh QUIT
                if ("QUIT".equals(line)) {
                    System.out.println("Client gửi QUIT. Đóng kết nối.");
                    break;
                }

                // Xử lý và kiểm tra dữ liệu đầu vào
                String response = processInput(line);

                // Gửi phản hồi kèm ký tự xuống dòng (\n)
                writer.write(response);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Lỗi xử lý client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String processInput(String input) {
        // Kiểm tra điều kiện: đúng 1 ký tự và là chữ số '0' -> '9'
        if (input != null && input.length() == 1 && Character.isDigit(input.charAt(0))) {
            int digit = input.charAt(0) - '0';
            return DIGIT_WORDS[digit];
        }
        return "ERR INVALID_DIGIT";
    }
}
