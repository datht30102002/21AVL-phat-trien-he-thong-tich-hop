package socketdatetime;
import java.io.*;
import java.net.*;
public class UdpDateTimeClient {
	private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9091;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket();
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            // Dat timeout 3 giây de ngat neu Server ngung hoat dong
            socket.setSoTimeout(3000); 
            InetAddress address = InetAddress.getByName(SERVER_HOST);

            System.out.println("San sang gui request UDP. Nhap lenh (DATE, TIME, DATETIME):");
            String command;

            while ((command = userInput.readLine()) != null) {
                byte[] sendData = command.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, SERVER_PORT);
                socket.send(sendPacket);

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                try {
                    socket.receive(receivePacket);
                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Server phan hoi: " + response);
                } catch (SocketTimeoutException e) {
                    System.err.println("Khong nhan duoc phan hoi (Server co the da dung hoac nghien packet)!");
                }
            }
        } catch (Exception e) {
            System.err.println("Loi UDP Client: " + e.getMessage());
        }
    }
}
