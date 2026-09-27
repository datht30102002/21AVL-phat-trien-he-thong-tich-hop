package bai6;
import java.io.*;
import java.net.*;
public class TCPServer {
	public static void main(String[] args) {
        int port = 9876;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("TCP Server dang chay tren port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                for (int i = 0; i < 1000; i++) {
                    int length = in.readInt();
                    byte[] buffer = new byte[length];
                    in.readFully(buffer);
                    
                    // Phản hồi lại client
                    out.writeInt(length);
                    out.write(buffer);
                    out.flush();
                }
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
