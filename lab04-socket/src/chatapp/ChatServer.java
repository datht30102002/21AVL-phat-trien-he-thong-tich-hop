package chatapp;
import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.*;
public class ChatServer {
	private static final int PORT = 9093;
    // Thread pool để quản lý các kết nối client
    private static final ExecutorService pool = Executors.newCachedThreadPool();
    // Cấu trúc Thread-safe lưu danh sách các client online (Key: Nickname, Value: ClientHandler)
    private static final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("[Chat Server] Dang khoi tao va lang nghe tai port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                // Giao việc xử lý client cho một thread trong Pool
                pool.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            System.err.println("Loi Chat Server: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    // Class xử lý luồng riêng cho từng Client
    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private String nickname;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // 1. Bước đăng ký Nickname duy nhất
                while (true) {
                    out.println("NICK_REQ: Nhap nickname cua ban:");
                    String inputNick = in.readLine();
                    if (inputNick == null) return;

                    inputNick = inputNick.trim();
                    if (inputNick.isEmpty()) {
                        out.println("ERR Nickname khong duoc de rong!");
                        continue;
                    }

                    // Kiểm tra tính duy nhất (Thread-safe với ConcurrentHashMap)
                    synchronized (clients) {
                        if (clients.containsKey(inputNick)) {
                            out.println("ERR Nickname da ton tai. Vui long chon nickname khac!");
                        } else {
                            this.nickname = inputNick;
                            clients.put(nickname, this);
                            out.println("OK Welcome " + nickname + "! Gui MSG <noi_dung>, USERS, hoac QUIT.");
                            broadcast("[Hethong] " + nickname + " da tham gia phong chat!", nickname);
                            break;
                        }
                    }
                }

                // 2. Vòng lặp lắng nghe lệnh từ Client
                String message;
                while ((message = in.readLine()) != null) {
                    message = message.trim();
                    if (message.equalsIgnoreCase("QUIT")) {
                        out.println("Goodbye!");
                        break;
                    } else if (message.equalsIgnoreCase("USERS")) {
                        // Trả về danh sách người dùng đang online
                        out.println("ONLINE USERS: " + String.join(", ", clients.keySet()));
                    } else if (message.toUpperCase().startsWith("MSG ")) {
                        // Broadcast tin nhắn cho các client còn lại
                        String content = message.substring(4).trim();
                        broadcast("[" + nickname + "]: " + content, nickname);
                    } else {
                        out.println("ERR Lenh khong hop le. Dung: MSG <noi_dung>, USERS, hoac QUIT.");
                    }
                }

            } catch (IOException e) {
                System.out.println("[Loi/Ngat ket noi] Client " + (nickname != null ? nickname : socket.getInetAddress()) + " ngat dot ngot.");
            } finally {
                // 3. Dọn dẹp khi Client thoát hoặc bị ngắt kết nối bất thường
                closeConnection();
            }
        }

        // Gửi tin nhắn cho client hiện tại
        public void sendMessage(String msg) {
            if (out != null) {
                out.println(msg);
            }
        }

        // Broadcast tin nhắn tới tất cả client khác
        private void broadcast(String msg, String excludeUser) {
            for (Map.Entry<String, ClientHandler> entry : clients.entrySet()) {
                if (!entry.getKey().equalsIgnoreCase(excludeUser)) {
                    entry.getValue().sendMessage(msg);
                }
            }
        }

        // Đóng socket và loại bỏ client khỏi danh sách
        private void closeConnection() {
            if (nickname != null && clients.containsKey(nickname)) {
                clients.remove(nickname);
                broadcast("[Hethong] " + nickname + " da roi phong chat.", nickname);
                System.out.println("[Info] Da loai bo client: " + nickname);
            }
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
