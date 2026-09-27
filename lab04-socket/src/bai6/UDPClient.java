package bai6;
import java.net.*;
public class UDPClient {
	public static class UDPResult {
        public long duration;
        public int receivedCount;

        public UDPResult(long duration, int receivedCount) {
            this.duration = duration;
            this.receivedCount = receivedCount;
        }
    }

    public static UDPResult runTest(int packetSize, int packetCount, int timeoutMs) {
        int received = 0;
        long duration = 0;

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(timeoutMs);
            InetAddress address = InetAddress.getByName("localhost");
            byte[] data = new byte[packetSize];
            byte[] buffer = new byte[packetSize + 128];

            long startTime = System.currentTimeMillis();

            for (int i = 0; i < packetCount; i++) {
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, 9877);
                socket.send(sendPacket);

                try {
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(receivePacket);
                    received++;
                } catch (SocketTimeoutException e) {
                    // Bo qua khi packet bi drop/timeout
                }
            }

            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new UDPResult(duration, received);
    }
}
