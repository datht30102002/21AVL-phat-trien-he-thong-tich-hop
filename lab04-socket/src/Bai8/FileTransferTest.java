package Bai8;
import java.io.*;
import java.nio.charset.StandardCharsets;
public class FileTransferTest {
	public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 9888;

        System.out.println("=== BAT DAU KIEM THU TRUYEN FILE TCP AN TOAN ===\n");

        // 1. Test 1: File rỗng (0 bytes)
        File emptyFile = new File("test_empty.txt");
        emptyFile.createNewFile();
        System.out.println("[TEST 1] Gui file rong (0 bytes)...");
        SecureTCPClient.sendFile(host, port, emptyFile, "test_empty.txt");
        System.out.println();

        // 2. Test 2: File văn bản tiếng Việt
        File utf8File = new File("test_tieng_viet.txt");
        try (FileOutputStream fos = new FileOutputStream(utf8File)) {
            String content = "Cộng hòa Xã hội Chủ nghĩa Việt Nam\nĐộc lập - Tự do - Hạnh phúc\nChào mừng bạn đến với Mạng Máy Tính!";
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("[TEST 2] Gui file van ban Tieng Viet...");
        SecureTCPClient.sendFile(host, port, utf8File, "test_tieng_viet.txt");
        System.out.println();

        // 3. Test 3: File ảnh nhị phân (Binary Image)
        File imageFile = new File("test_image.png");
        // Bơm dữ liệu nhị phân giả lập nếu chưa có file thực tế
        if (!imageFile.exists()) {
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                byte[] dummyImageData = new byte[100 * 1024]; // 100 KB
                for (int i = 0; i < dummyImageData.length; i++) {
                    dummyImageData[i] = (byte) (i % 256);
                }
                fos.write(dummyImageData);
            }
        }
        System.out.println("[TEST 3] Gui file anh nhi pan (Binary PNG)...");
        SecureTCPClient.sendFile(host, port, imageFile, "test_image.png");
        System.out.println();

        // 4. Test 4: Tên file chứa chuỗi Path Traversal (../)
        System.out.println("[TEST 4] Gui file voi ten doc hai '../../hack_system.txt'...");
        SecureTCPClient.sendFile(host, port, utf8File, "../../hack_system.txt");
        System.out.println();

        System.out.println("=== HOAN THANH KIEM THU ===");
    }
}
