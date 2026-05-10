package com.qlns.model;

public class BangLuong {
    private String idbl;
    private String idbc;
    private double luongCoBan;
    private double luongThucTe;
    private double thucNhan;

    // Phục vụ UI
    private String tenNhanVien;

    public BangLuong() {
    }

    public String getIdbl() { return idbl; }
    public void setIdbl(String idbl) { this.idbl = idbl; }

    public String getIdbc() { return idbc; }
    public void setIdbc(String idbc) { this.idbc = idbc; }

    public double getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(double luongCoBan) { this.luongCoBan = luongCoBan; }

    public double getLuongThucTe() { return luongThucTe; }
    public void setLuongThucTe(double luongThucTe) { this.luongThucTe = luongThucTe; }

    public double getThucNhan() { return thucNhan; }
    public void setThucNhan(double thucNhan) { this.thucNhan = thucNhan; }

    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }

    private String trangThai;
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    private int thang;
    private int nam;
    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }
    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }
}
