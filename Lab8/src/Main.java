import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    private static final String URL = "jdbc:sqlite:app.db";

    public static void main(String[] args) {
        initTables();

        System.out.println("=== 1. THÊM SẢN PHẨM (CREATE) ===");
        insertProduct("SP01", "Laptop Dell", 15000000, 10);
        insertProduct("SP02", "Chuột Razer", 1200000, 25);
        insertProduct("SP03", "Bàn phím cơ", 2000000, 15);

        System.out.println("\n=== 2. LIỆT KÊ TẤT CẢ SẢN PHẨM (READ) ===");
        listAllProducts();

        System.out.println("\n=== 3. TÌM KIẾM SẢN PHẨM THEO TÊN ===");
        searchProductByName("Razer");

        System.out.println("\n=== 4. CẬP NHẬT SẢN PHẨM (UPDATE) ===");
        updateProduct("SP01", 14500000, 8);
        listAllProducts();

        System.out.println("\n=== 5. XÓA SẢN PHẨM (DELETE) ===");
        deleteProduct("SP02");
        listAllProducts();

        System.out.println("\n=== 6. DEMO GIAO DỊCH (TRANSACTION) ===");
        demoTransactionUpdate();

        System.out.println("\n=== 7. TRUY VẤN NÂNG CAO TRÊN BẢNG NHANVIEN ===");
        setupNhanVienData();
        executeAggregationQueries();
    }

   
    private static void initTables() {
        String sqlSanPham = "CREATE TABLE IF NOT EXISTS SanPham ("
                + " maSP TEXT PRIMARY KEY,"
                + " tenSP TEXT NOT NULL,"
                + " gia REAL NOT NULL,"
                + " soluong INTEGER NOT NULL"
                + ");";

        String sqlNhanVien = "CREATE TABLE IF NOT EXISTS NhanVien ("
                + " maNV TEXT PRIMARY KEY,"
                + " tenNV TEXT NOT NULL,"
                + " chucVu TEXT NOT NULL,"
                + " luong REAL NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlSanPham);
            stmt.execute(sqlNhanVien);
        } catch (SQLException e) {
            System.err.println("Lỗi khởi tạo bảng: " + e.getMessage());
        }
    }

    public static void insertProduct(String maSP, String tenSP, double gia, int soLuong) {
        String sql = "INSERT INTO SanPham(maSP, tenSP, gia, soluong) VALUES(?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maSP);
            pstmt.setString(2, tenSP);
            pstmt.setDouble(3, gia);
            pstmt.setInt(4, soLuong);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("-> Thêm thành công sản phẩm: " + tenSP);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thêm sản phẩm (" + maSP + "): " + e.getMessage());
        }
    }

    public static void listAllProducts() {
        String sql = "SELECT maSP, tenSP, gia, soluong FROM SanPham";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.printf("%-10s %-20s %-15s %-10s%n", "Mã SP", "Tên SP", "Giá (VNĐ)", "Số lượng");
            System.out.println("-------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-10s %-20s %-15.2f %-10d%n",
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getDouble("gia"),
                        rs.getInt("soluong"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn sản phẩm: " + e.getMessage());
        }
    }

    public static void searchProductByName(String keyword) {
        String sql = "SELECT maSP, tenSP, gia, soluong FROM SanPham WHERE tenSP LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.printf("Kết quả tìm kiếm cho từ khóa '%s':%n", keyword);
                while (rs.next()) {
                    System.out.printf(" - [%s] %s | Giá: %.2f | SL: %d%n",
                            rs.getString("maSP"),
                            rs.getString("tenSP"),
                            rs.getDouble("gia"),
                            rs.getInt("soluong"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm sản phẩm: " + e.getMessage());
        }
    }

    public static void updateProduct(String maSP, double giaMoi, int soLuongMoi) {
        String sql = "UPDATE SanPham SET gia = ?, soluong = ? WHERE maSP = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, giaMoi);
            pstmt.setInt(2, soLuongMoi);
            pstmt.setString(3, maSP);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("-> Cập nhật thành công cho mã sản phẩm: " + maSP);
            } else {
                System.out.println("-> Không tìm thấy sản phẩm mã: " + maSP);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật sản phẩm: " + e.getMessage());
        }
    }

    public static void deleteProduct(String maSP) {
        String sql = "DELETE FROM SanPham WHERE maSP = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maSP);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("-> Xóa thành công sản phẩm mã: " + maSP);
            } else {
                System.out.println("-> Không tìm thấy sản phẩm mã: " + maSP);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi xóa sản phẩm: " + e.getMessage());
        }
    }


    public static void demoTransactionUpdate() {
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;

        String sqlUpdate1 = "UPDATE SanPham SET soluong = soluong - 1 WHERE maSP = ?";
        String sqlUpdate2 = "UPDATE SanPham SET soluong = soluong + 1 WHERE maSP = ?";

        try {
            conn = DriverManager.getConnection(URL);

            conn.setAutoCommit(false);

            pstmt1 = conn.prepareStatement(sqlUpdate1);
            pstmt1.setString(1, "SP01");
            pstmt1.executeUpdate();

            pstmt2 = conn.prepareStatement(sqlUpdate2);
            pstmt2.setString(1, "SP03");
            pstmt2.executeUpdate();

            conn.commit();
            System.out.println("-> Thực hiện Giao dịch (Transaction) thành công!");

        } catch (SQLException e) {
            System.err.println("-> Lỗi trong Transaction, tiến hành Rollback! Lỗi: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("Lỗi Rollback: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (pstmt1 != null) pstmt1.close();
                if (pstmt2 != null) pstmt2.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Lỗi đóng tài nguyên JDBC: " + e.getMessage());
            }
        }
    }

    private static void setupNhanVienData() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM NhanVien");
        } catch (SQLException e) {
            // Ignore
        }

        insertNhanVien("NV01", "Nguyễn Văn A", "Lập trình viên", 15000000);
        insertNhanVien("NV02", "Trần Thị B", "Lập trình viên", 18000000);
        insertNhanVien("NV03", "Lê Văn C", "Kiểm thử viên", 12000000);
        insertNhanVien("NV04", "Phạm Văn D", "Quản lý dự án", 30000000);
        insertNhanVien("NV05", "Hoàng Thị E", "Kiểm thử viên", 11000000);
    }

    private static void insertNhanVien(String maNV, String tenNV, String chucVu, double luong) {
        String sql = "INSERT INTO NhanVien(maNV, tenNV, chucVu, luong) VALUES(?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            pstmt.setString(2, tenNV);
            pstmt.setString(3, chucVu);
            pstmt.setDouble(4, luong);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi thêm nhân viên: " + e.getMessage());
        }
    }

    public static void executeAggregationQueries() {
        String sqlAgg = "SELECT SUM(luong) AS tongLuong, AVG(luong) AS luongTB, "
                + "MIN(luong) AS luongThapNhat, MAX(luong) AS luongCaoNhat FROM NhanVien";

        String sqlGroupBy = "SELECT chucVu, COUNT(*) AS soLuongNV FROM NhanVien GROUP BY chucVu";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmtAgg = conn.prepareStatement(sqlAgg);
             ResultSet rsAgg = pstmtAgg.executeQuery()) {

            if (rsAgg.next()) {
                System.out.printf("1. Tổng lương SUM(luong): %,.2f VNĐ%n", rsAgg.getDouble("tongLuong"));
                System.out.printf("2. Lương trung bình AVG(luong): %,.2f VNĐ%n", rsAgg.getDouble("luongTB"));
                System.out.printf("3. Lương thấp nhất MIN(luong): %,.2f VNĐ%n", rsAgg.getDouble("luongThapNhat"));
                System.out.printf("4. Lương cao nhất MAX(luong): %,.2f VNĐ%n", rsAgg.getDouble("luongCaoNhat"));
            }

            System.out.println("\n5. Số lượng nhân viên theo chức vụ (COUNT & GROUP BY):");
            try (PreparedStatement pstmtGroup = conn.prepareStatement(sqlGroupBy);
                 ResultSet rsGroup = pstmtGroup.executeQuery()) {
                while (rsGroup.next()) {
                    System.out.printf(" - Chức vụ: %-20s | Số lượng: %d người%n",
                            rsGroup.getString("chucVu"),
                            rsGroup.getInt("soLuongNV"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn nâng cao: " + e.getMessage());
        }
    }
}