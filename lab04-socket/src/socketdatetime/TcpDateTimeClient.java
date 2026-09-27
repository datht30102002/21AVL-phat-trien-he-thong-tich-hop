package socketdatetime;
import java.io.*;
import java.net.*;
public class TcpDateTimeClient {
	private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9090;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Da ket noi den TCP Server. Nhap lenh (DATE, TIME, DATETIME, QUIT):");
            String command;

            while ((command = userInput.readLine()) != null) {
                out.println(command);
                String response = in.readLine();

                if (response == null) {
                    System.out.println("Server da ngat ket noi!");
                    break;
                }

                System.out.println("Server phan hoi: " + response);

                if ("QUIT".equalsIgnoreCase(command.trim())) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Loi ket noi TCP: " + e.getMessage());
        }
    }
}
