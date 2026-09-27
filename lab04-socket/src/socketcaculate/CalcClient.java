package socketcaculate;
import java.io.*;
import java.net.*;
public class CalcClient {
	private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9092;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Da ket noi den Máy tính từ xa!");
            System.out.println("Nhap lenh theo dang: CALC <toán_tử> <toán_hạng_1> <toán_hạng_2>");
            System.out.println("Vi du: CALC + 100 200 (Gõ 'exit' de thoát)");

            String command;
            while (true) {
                System.out.print("\n> ");
                command = userInput.readLine();

                if (command == null || "exit".equalsIgnoreCase(command.trim())) {
                    System.out.println("Thoat chuong trinh.");
                    break;
                }

                if (command.trim().isEmpty()) {
                    continue;
                }

                // Gửi yêu cầu tới Server
                out.println(command);

                // Nhận phản hồi từ Server
                String response = in.readLine();
                if (response == null) {
                    System.out.println("Server da ngat ket noi!");
                    break;
                }

                System.out.println("Phan hoi tu Server: " + response);
            }
        } catch (IOException e) {
            System.err.println("Loi ket noi: " + e.getMessage());
        }
    }
}
