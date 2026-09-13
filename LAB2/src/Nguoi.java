public class Nguoi {
    private String hoTen;
    private int namSinh;
    private String diaChi;
    public Nguoi(String hoTen, int namSinh, String diaChi){
        this.hoTen = hoTen;
        this.namSinh = namSinh;
        this.diaChi = diaChi;
    }

    public int getNamSinh() {
        return namSinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setNamSinh(int namSinh) {
        this.namSinh = namSinh;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    public void hienThiThongTin(){
        System.out.println("Ho ten: " +hoTen+ "\n" + "Nam sinh: " +namSinh + "\n" + "Dia chi: " +diaChi);
    }
}
