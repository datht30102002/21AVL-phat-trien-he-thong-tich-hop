package Bai8;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.MessageDigest;
public class SecureTCPServer {
	private static final int PORT = 9888;
    private static final String UPLOAD_DIR = "uploads";

    public static void main(String[] args) {
        // Tạo thư mục uploads nếu chưa tồn tại
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        System.out.println("Secure File Server dang chay tren port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new FileReceiverHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class FileReceiverHandler implements Runnable {
        private Socket socket;

        public FileReceiverHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream())
            ) {
                // 1. Đọc Metadata (Header)
                String rawFileName = dis.readUTF();
                long fileSize = dis.readLong();
                String clientHash = dis.readUTF();

                // Chống Path Traversal: Loại bỏ tất cả đường dẫn thư mục, chỉ lấy tên file đơn lẻ
                String safeFileName = Paths.get(rawFileName).getFileName().toString();
                
                // Trường hợp tên file trống/không hợp lệ
                if (safeFileName.trim().isEmpty()) {
                    safeFileName = "unnamed_file_" + System.currentTimeMillis();
                }

                File destinationFile = new File(UPLOAD_DIR, safeFileName);
                System.out.printf("Dang nhan file: '%s' (Kich thuoc: %d bytes)\n", safeFileName, fileSize);

                // 2. Khởi tạo SHA-256 Digest
                MessageDigest digest = MessageDigest.getInstance("SHA-256");

                // 3. Đọc ĐÚNG fileSize bytes
                try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
                    byte[] buffer = new byte[8192];
                    long remaining = fileSize;
                    int read;

                    while (remaining > 0 && (read = dis.read(buffer, 0, (int) Math.min(buffer.length, remaining))) != -1) {
                        fos.write(buffer, 0, read);
                        digest.update(buffer, 0, read); // Cập nhật SHA-256
                        remaining -= read;
                    }
                }

                // 4. Kiểm tra tính toàn vẹn (SHA-256)
                String serverHash = HashUtils.bytesToHex(digest.digest());

                if (serverHash.equalsIgnoreCase(clientHash)) {
                    System.out.println("-> Truyen file thanh cong! SHA-256 khop.");
                    dos.writeUTF("OK");
                } else {
                    System.err.println("-> LOI: SHA-256 KHONG KHOP!");
                    System.err.println("  Client Hash: " + clientHash);
                    System.err.println("  Server Hash: " + serverHash);
                    dos.writeUTF("ERR HASH_MISMATCH");
                }
                dos.flush();

            } catch (Exception e) {
                System.err.println("Loi khi nhan file: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }
    }
}
