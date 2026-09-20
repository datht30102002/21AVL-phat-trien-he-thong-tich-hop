package Datetime;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class DateTimeUdpServer {
    private static final int PORT = 6001;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MM yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH mm ss");

    public static void main(String[] args) {
        System.out.println("UDP Date/Time Server đang chạy tại port " + PORT);
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); // Chờ nhận packet từ client

                String request = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8).trim();
                String response = processCommand(request);

                byte[] sendData = response.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendPacket = new DatagramPacket(
                        sendData, sendData.length, packet.getAddress(), packet.getPort()
                );
                socket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String processCommand(String cmd) {
        LocalDateTime now = LocalDateTime.now();
        switch (cmd.toUpperCase()) {
            case "DATE":
                return now.format(DATE_FMT);
            case "TIME":
                return now.format(TIME_FMT);
            case "DATETIME":
                return now.format(DATE_FMT) + " " + now.format(TIME_FMT);
            default:
                return "ERR INVALID_COMMAND";
        }
    }
}
