package bai9;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;
public class MulticastSender {
	private static final String MULTICAST_IP = "239.255.0.1";
    private static final int PORT = 8888;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress groupAddr = InetAddress.getByName(MULTICAST_IP);

            // Tìm NetworkInterface phù hợp
            NetworkInterface netIf = getAvailableNetworkInterface();
            if (netIf == null) {
                netIf = NetworkInterface.getByInetAddress(InetAddress.getByName("127.0.0.1"));
            }

            System.out.println("Sender dang gui tin qua Card mang: " + netIf.getDisplayName());
            System.out.println("Gui thong bao toi " + MULTICAST_IP + ":" + PORT);
            System.out.println("Nhap noi dung thong bao (go 'EXIT' de dung):");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();

                if ("EXIT".equalsIgnoreCase(message.trim())) {
                    break;
                }

                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, groupAddr, PORT);
                
                socket.send(packet);
                System.out.println("-> Da phat tin nhan Multicast thanh cong!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
