package com.qlns.model;

public class BangChamCong {
    private String idbc;
    private String idnv;
    private int thang;
    private int nam;
    private double soGioTangCa;
    private int soNgayNghi;
    private int soNgayDiTre;
    private int tongNgayLam;
    private String trangThai;

    // Phục vụ UI
    private String tenNhanVien;

    public BangChamCong() {
    }

    public String getIdbc() { return idbc; }
    public void setIdbc(String idbc) { this.idbc = idbc; }

    public String getIdnv() { return idnv; }
    public void setIdnv(String idnv) { this.idnv = idnv; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public double getSoGioTangCa() { return soGioTangCa; }
    public void setSoGioTangCa(double soGioTangCa) { this.soGioTangCa = soGioTangCa; }

    public int getSoNgayNghi() { return soNgayNghi; }
    public void setSoNgayNghi(int soNgayNghi) { this.soNgayNghi = soNgayNghi; }

    public int getSoNgayDiTre() { return soNgayDiTre; }
    public void setSoNgayDiTre(int soNgayDiTre) { this.soNgayDiTre = soNgayDiTre; }

    public int getTongNgayLam() { return tongNgayLam; }
    public void setTongNgayLam(int tongNgayLam) { this.tongNgayLam = tongNgayLam; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }
}
