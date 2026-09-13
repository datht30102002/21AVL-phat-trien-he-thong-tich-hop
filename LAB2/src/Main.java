//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Nguoi n = new Nguoi("Huynh Tan Dat", 2002, "Quan 7");
        Nguoi n2 = new Nguoi("Duong Quang Phuc", 2001, "Quan 7");
        SinhVien sv1 = new SinhVien( n,"001", "CNTT", 7.5);
        SinhVien sv2 = new SinhVien(n2, "002", "CNTT", 8.5);
        GiangVien gv1 = new GiangVien(n,"gv001", "CNTT", 560000,2.5);
        GiangVien gv2 = new GiangVien(n2,"gv002", "Sinh Hoc", 640000,1.4);
        sv1.hienThiThongTin();
        sv2.hienThiThongTin();
        gv1.hienThiThongTin();
        gv2.hienThiThongTin();
    }
}