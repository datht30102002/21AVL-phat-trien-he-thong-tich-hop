package bai9;
import java.io.IOException;
import java.net.*;
public class MulticastReceiver {
	private static final String MULTICAST_IP = "239.255.0.1";
    private static final int PORT = 8888;

    public static void main(String[] args) {
        MulticastSocket socket = null;
        InetSocketAddress group = null;
        NetworkInterface netIf = null;

        try {
            InetAddress groupAddr = InetAddress.getByName(MULTICAST_IP);
            group = new InetSocketAddress(groupAddr, PORT);

            // 1. Khởi tạo MulticastSocket binding tới PORT
            socket = new MulticastSocket(PORT);

            // 2. Tự động tìm NetworkInterface phù hợp (Ưu tiên card mạng active, hỗ trợ multicast)
            netIf = getAvailableNetworkInterface();
            if (netIf == null) {
                System.err.println("Khong tim thay NetworkInterface phu hop! Dung Loopback interface...");
                netIf = NetworkInterface.getByInetAddress(InetAddress.getByName("127.0.0.1"));
            }

            System.out.println("Sử dụng Card mạng: " + netIf.getDisplayName());

            // 3. Gia nhập nhóm Multicast (joinGroup)
            socket.joinGroup(group, netIf);
            System.out.println("-> Receiver da tham gia nhom " + MULTICAST_IP + ":" + PORT);
            System.out.println("Dang cho nhan thong bao (An Ctrl+C de dung)...");

            byte[] buffer = new byte[1024];

            // 4. Vòng lặp nhận thông điệp
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.printf("[%s] Nhan tu %s: %s\n", 
                        netIf.getName(), packet.getSocketAddress(), message);

                // Nếu nhận được tín hiệu STOP thì thoát vòng lặp
                if ("STOP".equalsIgnoreCase(message.trim())) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Loi Receiver: " + e.getMessage());
        } finally {
            // 5. Rời nhóm (leaveGroup) và đóng Socket an toàn
            if (socket != null && group != null && netIf != null) {
                try {
                    System.out.println("-> Dang roi khoi nhom Multicast...");
                    socket.leaveGroup(group, netIf);
                } catch (IOException e) {
                    System.err.println("Loi khi roi nhom: " + e.getMessage());
                }
                socket.close();
                System.out.println("-> Da dong Socket an toan.");
            }
        }
    }

    // Hàm chọn NetworkInterface hỗ trợ Multicast và đang hoạt động
    private static NetworkInterface getAvailableNetworkInterface() throws SocketException {
        var interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            if (ni.isUp() && ni.supportsMulticast() && !ni.isLoopback()) {
                return ni;
            }
        }
        return null;
    }
}
