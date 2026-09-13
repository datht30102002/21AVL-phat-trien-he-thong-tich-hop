public class SinhVien extends Nguoi {
    private String maSinhVien;
    private String nganhHoc;
    private double diemTrungBinh;

    public SinhVien(Nguoi nguoi, String maSinhVien, String nganhHoc, double diemTrungBinh) {
        super(nguoi.getHoTen(), nguoi.getNamSinh(), nguoi.getDiaChi());
        this.maSinhVien = maSinhVien;
        this.nganhHoc = nganhHoc;
        this.diemTrungBinh = diemTrungBinh;
    }

    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }

    public String getNganhHoc() {
        return nganhHoc;
    }

    public void setNganhHoc(String nganhHoc) {
        this.nganhHoc = nganhHoc;
    }

    public double getDiemTrungBinh() {
        return diemTrungBinh;
    }

    public void setDiemTrungBinh(double diemTrungBinh) {
        this.diemTrungBinh = diemTrungBinh;
    }

    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.println("Ma sinh vien: " + maSinhVien);
        System.out.println("Ngan hoc: " + nganhHoc);
        System.out.println("Diem trung binh: " + diemTrungBinh);
        System.out.println("Xep loai: " +xepLoai());
    }

    public String xepLoai() {
        if (diemTrungBinh >= 8.5) {
            return ("Gioi");
        } else if (diemTrungBinh < 8.5) {
            return ("Kha");
        } else if (diemTrungBinh >= 5 || diemTrungBinh < 7) {
            return ("Trung Binh");
        } else {
            return ("Yeu");
        }
    }
}

