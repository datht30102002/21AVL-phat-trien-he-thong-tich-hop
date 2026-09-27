package socketdatetime;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UdpDateTimeServer {
	private static final int PORT = 9091;

    public static void main(String[] args) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MM yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH mm ss");
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd MM yyyy HH mm ss");

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("[UDP Server] Dang lang nghe tai port " + PORT);
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(requestPacket);

                String command = new String(requestPacket.getData(), 0, requestPacket.getLength()).trim().toUpperCase();
                LocalDateTime now = LocalDateTime.now();
                String response;

                if ("DATE".equals(command)) {
                    response = now.format(dateFormat);
                } else if ("TIME".equals(command)) {
                    response = now.format(timeFormat);
                } else if ("DATETIME".equals(command)) {
                    response = now.format(dateTimeFormat);
                } else {
                    response = "Loi: Lenh khong hop le (Dung DATE, TIME, DATETIME)";
                }

                byte[] sendData = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(
                        sendData,
                        sendData.length,
                        requestPacket.getAddress(),
                        requestPacket.getPort()
                );
                socket.send(responsePacket);
            }
        } catch (Exception e) {
            System.err.println("Loi UDP Server: " + e.getMessage());
        }
    }
}
