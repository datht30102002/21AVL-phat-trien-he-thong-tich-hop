package digit;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DigitClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Đã kết nối tới Server. Nhập dữ liệu (nhập 'QUIT' để thoát):");

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();

                // Gửi yêu cầu lên server
                writer.write(input);
                writer.newLine();
                writer.flush();

                if ("QUIT".equals(input)) {
                    break;
                }

                // Nhận phản hồi
                String response = reader.readLine();
                System.out.println("Server trả về: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
