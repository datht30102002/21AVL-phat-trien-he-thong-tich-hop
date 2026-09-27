package socketdatetime;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class TcpDateTimeServer {
	private static final int PORT = 9090;
	public static void main (String[] args) {
	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MM yyyy");
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH mm ss");
    DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd MM yyyy HH mm ss");
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
        System.out.println("[TCP Server] Dang lang nghe tai port " + PORT);

        while (true) {
            try (Socket socket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                System.out.println("[TCP Server] Client ket noi: " + socket.getInetAddress());
                String line;

                while ((line = in.readLine()) != null) {
                    String command = line.trim().toUpperCase();
                    LocalDateTime now = LocalDateTime.now();

                    if ("DATE".equals(command)) {
                        out.println(now.format(dateFormat));
                    } else if ("TIME".equals(command)) {
                        out.println(now.format(timeFormat));
                    } else if ("DATETIME".equals(command)) {
                        out.println(now.format(dateTimeFormat));
                    } else if ("QUIT".equals(command)) {
                        out.println("Goodbye!");
                        break; // Ngắt phiên kết nối TCP với client hiện tại
                    } else {
                        out.println("Loi: Lenh khong hop le (Dung DATE, TIME, DATETIME, QUIT)");
                    }
                }
            } catch (IOException e) {
                System.err.println("Loi phiên client: " + e.getMessage());
            }
        }
    } catch (IOException e) {
        System.err.println("Loi khoi tao server: " + e.getMessage());
    }
}
}
