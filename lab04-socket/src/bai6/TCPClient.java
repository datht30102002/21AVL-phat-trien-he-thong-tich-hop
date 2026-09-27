package bai6;
import java.io.*;
import java.net.*;
public class TCPClient {
	public static long runTest(int packetSize, int packetCount) throws IOException {
        Socket socket = new Socket("localhost", 9876);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        byte[] data = new byte[packetSize];
        byte[] response = new byte[packetSize];

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < packetCount; i++) {
            out.writeInt(data.length);
            out.write(data);
            out.flush();

            int len = in.readInt();
            in.readFully(response);
        }

        long endTime = System.currentTimeMillis();
        socket.close();

        return (endTime - startTime);
    }
}
