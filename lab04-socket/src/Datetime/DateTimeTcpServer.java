package Datetime;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeTcpServer {
    private static final int PORT = 6000;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MM yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH mm ss");

    public static void main(String[] args) {
        System.out.println("TCP Date/Time Server đang chạy tại port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket client = serverSocket.accept();
                new Thread(() -> handleClient(client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))
        ) {
            String command;
            while ((command = reader.readLine()) != null) {
                if ("QUIT".equalsIgnoreCase(command.trim())) {
                    break;
                }
                String response = processCommand(command.trim());
                writer.write(response);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Client ngắt kết nối: " + e.getMessage());
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
