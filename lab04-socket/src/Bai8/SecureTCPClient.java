package Bai8;
import java.io.*;
import java.net.*;
public class SecureTCPClient {
	public static boolean sendFile(String serverHost, int serverPort, File file, String customSendName) {
        if (!file.exists()) {
            System.err.println("File khong ton tai: " + file.getAbsolutePath());
            return false;
        }

        try (
            Socket socket = new Socket(serverHost, serverPort);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            FileInputStream fis = new FileInputStream(file)
        ) {
            // 1. Tính toán mã SHA-256 của file trước khi gửi
            String sha256 = HashUtils.calculateSHA256(file);
            long fileSize = file.length();
            String sendFileName = (customSendName != null) ? customSendName : file.getName();

            // 2. Gửi Header: Tên file -> Kích thước (long) -> SHA-256 (String)
            dos.writeUTF(sendFileName);
            dos.writeLong(fileSize);
            dos.writeUTF(sha256);
            dos.flush();

            // 3. Gửi dữ liệu file
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, read);
            }
            dos.flush();

            // 4. Nhận phản hồi từ Server
            String response = dis.readUTF();
            System.out.println("Server phan hoi: " + response);

            return "OK".equals(response);

        } catch (Exception e) {
            System.err.println("Loi khi gui file: " + e.getMessage());
            return false;
        }
    }
}
