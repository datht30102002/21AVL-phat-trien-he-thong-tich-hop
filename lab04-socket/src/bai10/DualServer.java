package bai10;
import java.io.*;
import java.net.*;
public class DualServer {
	private static final int UDP_DISCOVERY_PORT = 9888;
    private static final String SERVICE_NAME = "MyJavaService";
    private static final String SERVICE_VERSION = "v1.0.26";

    public static void main(String[] args) {
        // Tự động chọn một cổng TCP trống (pass 0 để OS cấp cổng ngẫu nhiên)
        try (ServerSocket tcpServer = new ServerSocket(0)) {
            int tcpPort = tcpServer.getLocalPort();
            System.out.println("=== SERVER DANG CHAY ===");
            System.out.println("TCP Service dang lang nghe tren port: " + tcpPort);

            // 1. Chạy Luồng UDP Discovery Listener
            Thread discoveryThread = new Thread(() -> startUdpDiscoveryListener(tcpPort));
            discoveryThread.setDaemon(true);
            discoveryThread.start();

            // 2. Luồng chính phục vụ các kết nối TCP từ Client
            while (true) {
                Socket clientSocket = tcpServer.accept();
                new Thread(new TcpClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Xử lý lắng nghe và phản hồi gói UDP Broadcast
    private static void startUdpDiscoveryListener(int tcpPort) {
        try (DatagramSocket udpSocket = new DatagramSocket(UDP_DISCOVERY_PORT)) {
            System.out.println("UDP Discovery Listener dang chay tren port: " + UDP_DISCOVERY_PORT);
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                udpSocket.receive(packet);

                String request = new String(packet.getData(), 0, packet.getLength()).trim();
                if ("DISCOVER_SERVICE".equals(request)) {
                    // Cấu trúc phản hồi: SERVICE <tên_dịch_vụ> <tcp_port> <phiên_bản>
                    String responseMsg = String.format("SERVICE %s %d %s", SERVICE_NAME, tcpPort, SERVICE_VERSION);
                    byte[] sendData = responseMsg.getBytes();

                    DatagramPacket responsePacket = new DatagramPacket(
                            sendData, sendData.length,
                            packet.getAddress(), packet.getPort() // Trả về đúng IP và Port của Client vừa hỏi
                    );
                    udpSocket.send(responsePacket);
                    System.out.println("-> Da phan hoi Discovery toi " + packet.getSocketAddress());
                }
            }
        } catch (IOException e) {
            System.err.println("Loi UDP Discovery: " + e.getMessage());
        }
    }

    // Xử lý luồng kết nối TCP Service
    private static class TcpClientHandler implements Runnable {
        private Socket socket;

        public TcpClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                out.println("WELCOME_TO_TCP_SERVICE (" + SERVICE_NAME + ")");
                String request = in.readLine();
                System.out.println("Nhan tin nhan qua TCP tu client [" + socket.getRemoteSocketAddress() + "]: " + request);
                out.println("ECHO_ACK: " + request);
            } catch (IOException e) {
                System.err.println("Loi TCP Client Handler: " + e.getMessage());
            }
        }
    }
}
