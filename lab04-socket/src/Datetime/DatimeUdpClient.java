package Datetime;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DatimeUdpClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 6001;
    private static final int TIMEOUT_MS = 3000; // Cấu hình Timeout 3s để tránh bị treo khi Server ngắt

    public static void main(String[] args) {
        System.out.println("--- UDP DATE/TIME CLIENT ---");
        try (
                DatagramSocket socket = new DatagramSocket();
                Scanner scanner = new Scanner(System.in)
        ) {
            InetAddress serverAddress = InetAddress.getByName(SERVER_HOST);
            socket.setSoTimeout(TIMEOUT_MS); // Thiết lập thời gian chờ phản hồi từ UDP Server

            System.out.println("Sẵn sàng gửi UDP Packet!");
            System.out.println("Nhập các lệnh: DATE, TIME, DATETIME (Gõ 'EXIT' để dừng client)");

            while (true) {
                System.out.print("UDP Client > ");
                String command = scanner.nextLine().trim();

                if ("EXIT".equalsIgnoreCase(command)) {
                    System.out.println("Đã thoát UDP Client.");
                    break;
                }

                if (command.isEmpty()) {
                    continue;
                }

                // Gửi DatagramPacket đến Server
                byte[] sendData = command.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendPacket = new DatagramPacket(
                        sendData, sendData.length, serverAddress, SERVER_PORT
                );
                socket.send(sendPacket);

                // Lắng nghe phản hồi từ Server
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                try {
                    socket.receive(receivePacket);
                    String response = new String(
                            receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8
                    );
                    System.out.println("Server trả về: " + response);
                } catch (SocketTimeoutException e) {
                    System.err.println("Lỗi Timeout: Không nhận được phản hồi từ UDP Server (Server có thể đã dừng).");
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi UDP Client: " + e.getMessage());
        }
    }
}
