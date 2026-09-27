package bai6;

public class BenchMarkRunner {
	public static void main(String[] args) throws Exception {
        int packetSize = 1024; // 1 KB
        int packetCount = 1000;
        int runs = 5;

        System.out.println("=== BAT DAU THUC NGHIEM SO SANH TCP VS UDP ===");
        System.out.println("So luong goi: " + packetCount + " | Kich thuoc: " + packetSize + " bytes\n");

        // 1. Chạy thực nghiệm TCP
        System.out.println("--- KET QUA TCP ---");
        long totalTcpTime = 0;
        for (int i = 1; i <= runs; i++) {
            long time = TCPClient.runTest(packetSize, packetCount);
            totalTcpTime += time;
            System.out.printf("Lan %d: Thoi gian = %d ms | So phan hoi = %d/1000\n", i, time, packetCount);
            Thread.sleep(500); // Nghi giua cac lan chay
        }
        System.out.printf("-> Thoi gian trung binh TCP: %.2f ms\n\n", (double) totalTcpTime / runs);

        // 2. Chạy thực nghiệm UDP
        System.out.println("--- KET QUA UDP ---");
        long totalUdpTime = 0;
        int totalUdpReceived = 0;
        for (int i = 1; i <= runs; i++) {
            UDPClient.UDPResult result = UDPClient.runTest(packetSize, packetCount, 2000);
            totalUdpTime += result.duration;
            totalUdpReceived += result.receivedCount;
            System.out.printf("Lan %d: Thoi gian = %d ms | So phan hoi = %d/1000\n", i, result.duration, result.receivedCount);
            Thread.sleep(500);
        }
        System.out.printf("-> Thoi gian trung binh UDP: %.2f ms\n", (double) totalUdpTime / runs);
        System.out.printf("-> Ty le nhan thanh cong UDP trung binh: %.2f%%\n", ((double) totalUdpReceived / (runs * packetCount)) * 100);
    }
}
