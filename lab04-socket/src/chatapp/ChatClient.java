package chatapp;
import java.io.*;
import java.net.*;
public class ChatClient {
	private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9093;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            // Thread riêng chuyên đọc tin nhắn từ Server và in ra màn hình
            Thread receiverThread = new Thread(() -> {
                try {
                    String serverMsg;
                    while ((serverMsg = in.readLine()) != null) {
                        System.out.println("\n" + serverMsg);
                        System.out.print("> ");
                    }
                } catch (IOException e) {
                    System.out.println("\n[Loi] Mat ket noi toi Server.");
                }
            });
            receiverThread.setDaemon(true);
            receiverThread.start();

            // Luồng chính đọc input người dùng và gửi đi
            String userInput;
            while ((userInput = console.readLine()) != null) {
                out.println(userInput);
                if ("QUIT".equalsIgnoreCase(userInput.trim())) {
                    break;
                }
            }

            socket.close();
            System.out.println("Da thoat phong chat.");

        } catch (IOException e) {
            System.err.println("Không the ket noi toi Chat Server: " + e.getMessage());
        }
    }
}
