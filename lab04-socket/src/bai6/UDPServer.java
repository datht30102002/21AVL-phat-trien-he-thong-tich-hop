package bai6;
import java.net.*;
public class UDPServer {
	public static void main(String[] args) {
        int port = 9877;
        byte[] buffer = new byte[2048];
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server dang chay tren port " + port);
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);

                // Echo lại cho Client
                DatagramPacket sendPacket = new DatagramPacket(
                    receivePacket.getData(), receivePacket.getLength(),
                    receivePacket.getAddress(), receivePacket.getPort()
                );
                socket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
