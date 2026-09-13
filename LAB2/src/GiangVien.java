public class GiangVien extends Nguoi {
    private String maGiangVien;
    private String chuyenMon;
    private double luongCoBan;
    private double heSoLuong;
    public GiangVien(Nguoi nguoi, String maGiangVien, String chuyenMon, double luongCoBan, double heSoLuong) {
        super(nguoi.getHoTen(), nguoi.getNamSinh(), nguoi.getDiaChi());
        this.maGiangVien = maGiangVien;
        this.chuyenMon = chuyenMon;
        this.luongCoBan = luongCoBan;
        this.heSoLuong = heSoLuong;
    }
    public String getMaGiangVien() {
        return maGiangVien;
    }
    public void setMaGiangVien(String maGiangVien) {
        this.maGiangVien = maGiangVien;
    }
    public String getChuyenMon() {
        return chuyenMon;
    }
    public void setChuyenMon(String chuyenMon) {
        this.chuyenMon = chuyenMon;
    }
    public double getLuongCoBan() {
        return luongCoBan;
    }
    public void setLuongCoBan(double luongCoBan) {
        this.luongCoBan = luongCoBan;
    }
    public double getHeSoLuong() {
        return heSoLuong;
    }
    public void setHeSoLuong(double heSoLuong) {
        this.heSoLuong = heSoLuong;
    }
    public double tinhLuong() {
        return luongCoBan * heSoLuong;
    }

    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.println("Ma giang vien: " + maGiangVien);
        System.out.println("Chuyen Mon: " + chuyenMon);
        System.out.println("Luong Co Ban: " + luongCoBan);
        System.out.println("Luong : " +tinhLuong());
        System.out.println("He so luong: " + heSoLuong);
    }
}
