package socketcaculate;
import java.io.*;
import java.net.*;
public class CalcServer {
	private static final int PORT = 9092;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Calc Server] Dang lang nghe tai port " + PORT + "...");

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    System.out.println("[Calc Server] Client da ket noi: " + socket.getInetAddress());
                    String line;

                    while ((line = in.readLine()) != null) {
                        String response = processCommand(line.trim());
                        out.println(response);
                    }
                } catch (IOException e) {
                    System.err.println("Loi ket noi voi Client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Loi khoi tao Server: " + e.getMessage());
        }
    }

    private static String processCommand(String input) {
        if (input.isEmpty()) {
            return "ERR INVALID_FORMAT";
        }

        // Tách các thành phần dựa vào khoảng trắng
        String[] parts = input.split("\\s+");

        // Kiểm tra xem cú pháp có bắt đầu bằng CALC và có đúng 4 thành phần không
        if (parts.length != 4 || !"CALC".equalsIgnoreCase(parts[0])) {
            return "ERR INVALID_FORMAT";
        }

        String operator = parts[1];
        double operand1, operand2;

        // Ép kiểu toán hạng sang số
        try {
            operand1 = Double.parseDouble(parts[2]);
            operand2 = Double.parseDouble(parts[3]);
        } catch (NumberFormatException e) {
            return "ERR INVALID_NUMBER";
        }

        // Xử lý phép tính
        switch (operator) {
            case "+":
                return formatResult(operand1 + operand2);
            case "-":
                return formatResult(operand1 - operand2);
            case "*":
                return formatResult(operand1 * operand2);
            case "/":
                if (operand2 == 0) {
                    return "ERR DIVIDE_BY_ZERO";
                }
                return formatResult(operand1 / operand2);
            default:
                return "ERR UNSUPPORTED_OPERATOR";
        }
    }

    // Định dạng kết quả (bỏ .0 nếu là số nguyên)
    private static String formatResult(double result) {
        if (result == (long) result) {
            return String.format("OK %d", (long) result);
        } else {
            return String.format("OK %s", result);
        }
    }
}
