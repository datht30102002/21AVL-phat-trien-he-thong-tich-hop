package bai7;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class TCPLogClient {
	public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = 9876;

        try (
            Socket socket = new Socket(serverHost, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.print("Nhap clientId cua ban: ");
            String clientId = scanner.nextLine();

            // Gửi HELLO clientId
            out.println("HELLO " + clientId);
            String response = in.readLine();
            System.out.println("Server: " + response);

            if (response == null || !response.startsWith("OK")) {
                System.out.println("Ket noi bi tu choi hoac clientId khong hop le.");
                return;
            }

            System.out.println("Nhap tin nhan (go 'QUIT' de ket thuc):");
            while (true) {
                System.out.print("> ");
                String msg = scanner.nextLine();
                out.println(msg);

                String ack = in.readLine();
                System.out.println("Server: " + ack);

                if ("QUIT".equalsIgnoreCase(msg.trim())) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
