package bai7;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
public class TCPLogServer {
	private static final int PORT = 9876;
    // Regex validate clientId: chỉ gồm chữ cái, số, dấu gạch ngang (-) và gạch dưới (_)
    private static final Pattern CLIENT_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");

    public static void main(String[] args) {
        System.out.println("TCP Log Server dang chay tren port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Xử lý mỗi client trên một Thread riêng biệt
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String remoteAddr = socket.getRemoteSocketAddress().toString();
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                // 1. Nhận câu lệnh HELLO clientId ban đầu
                String firstLine = in.readLine();
                if (firstLine == null || !firstLine.startsWith("HELLO ")) {
                    out.println("ERROR Structure message must start with HELLO <clientId>");
                    socket.close();
                    return;
                }

                String clientId = firstLine.substring(6).trim();
                if (!CLIENT_ID_PATTERN.matcher(clientId).matches()) {
                    out.println("ERROR Invalid clientId format. Only alphanumeric, '-' and '_' allowed.");
                    socket.close();
                    return;
                }

                out.println("OK Welcome " + clientId);

                // 2. Mở file data_logs_<clientId>.txt để ghi (append = true)
                String fileName = "data_logs_" + clientId + ".txt";
                try (FileWriter fw = new FileWriter(fileName, true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter logWriter = new PrintWriter(bw)) {

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String line;

                    // 3. Đọc nội dung tin nhắn liên tục cho tới khi gặp QUIT
                    while ((line = in.readLine()) != null) {
                        if ("QUIT".equalsIgnoreCase(line.trim())) {
                            out.println("OK Bye");
                            break;
                        }

                        // Định dạng nhật ký: timestamp | remote_address | content
                        String timestamp = LocalDateTime.now().format(formatter);
                        String logEntry = String.format("[%s] [%s] %s", timestamp, remoteAddr, line);
                        
                        logWriter.println(logEntry);
                        logWriter.flush(); // Ghi trực tiếp xuống file

                        out.println("ACK Message logged");
                    }
                }
            } catch (IOException e) {
                System.err.println("Loi xu ly client " + remoteAddr + ": " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }
    }
}
