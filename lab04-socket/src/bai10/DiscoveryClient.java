package bai10;
import java.io.*;
import java.net.*;
import java.util.*;
public class DiscoveryClient {
	private static final int DISCOVERY_PORT = 9888;
    private static final int TIMEOUT_MS = 3000; // Chờ phản hồi trong 3 giây

    // Lớp lưu trữ thông tin Server tìm thấy
    public static class DiscoveredServer {
        public InetAddress ipAddress;
        public String serviceName;
        public int tcpPort;
        public String version;

        public DiscoveredServer(InetAddress ipAddress, String serviceName, int tcpPort, String version) {
            this.ipAddress = ipAddress;
            this.serviceName = serviceName;
            this.tcpPort = tcpPort;
            this.version = version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DiscoveredServer that = (DiscoveredServer) o;
            return tcpPort == that.tcpPort && Objects.equals(ipAddress, that.ipAddress);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ipAddress, tcpPort);
        }

        @Override
        public String toString() {
            return String.format("[%s] IP: %s | TCP Port: %d | Version: %s", 
                    serviceName, ipAddress.getHostAddress(), tcpPort, version);
        }
    }

    public static void main(String[] args) {
        List<DiscoveredServer> serverList = discoverServers();

        if (serverList.isEmpty()) {
            System.out.println("Khong tim thay Server nao trong mang!");
            return;
        }

        // Hiển thị danh sách các Server tìm thấy
        System.out.println("\n=== DANH SACH SERVER TIM THAY ===");
        for (int i = 0; i < serverList.size(); i++) {
            System.out.println((i + 1) + ". " + serverList.get(i));
        }

        // Chọn kết nối tới Server đầu tiên tìm được
        DiscoveredServer targetServer = serverList.get(0);
        connectTcpService(targetServer);
    }

    // 1. Quá trình phát UDP Broadcast & Gom thông tin Server
    public static List<DiscoveredServer> discoverServers() {
        Set<DiscoveredServer> uniqueServers = new LinkedHashSet<>(); // Dùng Set để tự động LỌC TRÙNG

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true); // Bật cờ Broadcast
            socket.setSoTimeout(TIMEOUT_MS); // Thiết lập Timeout 3 giây

            // Gửi gói DISCOVER_SERVICE tới địa chỉ Broadcast 255.255.255.255
            byte[] sendData = "DISCOVER_SERVICE".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(
                    sendData, sendData.length,
                    InetAddress.getByName("255.255.255.255"), DISCOVERY_PORT
            );
            socket.send(sendPacket);
            System.out.println("Da phat tin UDP Broadcast 'DISCOVER_SERVICE'... Dang cho phan hoi (" + (TIMEOUT_MS/1000) + "s)");

            byte[] buffer = new byte[1024];

            // Vòng lặp nhận các phản hồi từ nhiều Server trong mạng cho đến khi Timeout
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < TIMEOUT_MS) {
                try {
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(receivePacket); // Bị chặn (block) cho tới khi nhận gói hoặc Timeout

                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                    // Phân tích cú pháp: SERVICE <tên_dịch_vụ> <tcp_port> <phiên_bản>
                    String[] parts = response.split(" ");
                    if (parts.length >= 4 && "SERVICE".equals(parts[0])) {
                        String serviceName = parts[1];
                        int tcpPort = Integer.parseInt(parts[2]);
                        String version = parts[3];
                        InetAddress serverIp = receivePacket.getAddress(); // Lấy IP nguồn từ Datagram Header

                        DiscoveredServer server = new DiscoveredServer(serverIp, serviceName, tcpPort, version);
                        uniqueServers.add(server); // Loại bỏ phản hồi trùng dựa trên IP + TCP Port
                    }
                } catch (SocketTimeoutException e) {
                    // Hết thời gian chờ thu thập -> Kết thúc vòng lặp
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Loi khi thuc hien Discovery: " + e.getMessage());
        }

        return new ArrayList<>(uniqueServers);
    }

    // 2. Kết nối tới TCP Service của Server đã chọn
    public static void connectTcpService(DiscoveredServer server) {
        System.out.println("\n-> Dang ket noi TCP toi " + server.ipAddress.getHostAddress() + ":" + server.tcpPort + "...");
        try (
            Socket socket = new Socket(server.ipAddress, server.tcpPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String welcome = in.readLine();
            System.out.println("Server phan hoi TCP: " + welcome);

            out.println("HELLO_SERVER_REQUEST");
            String echo = in.readLine();
            System.out.println("Server phan hoi TCP: " + echo);

        } catch (IOException e) {
            System.err.println("Loi ket noi TCP Service: " + e.getMessage());
        }
    }
}
