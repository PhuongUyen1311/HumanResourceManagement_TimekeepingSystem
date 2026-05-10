package com.qlns.model;
import java.util.Date;

public class HopDong {
    private int sodh;
    private Date ngayKy;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private double luongCoBan;
    private String trangThai;
    private int idnv;
    private int idloaihd;
    private String tenNhanVien;
    private String tenChucVu;
    private String tenPhongBan;
    private String tenChiNhanh;

    public HopDong() {}

    public int getSodh() { return sodh; }
    public void setSodh(int sodh) { this.sodh = sodh; }
    public Date getNgayKy() { return ngayKy; }
    public void setNgayKy(Date ngayKy) { this.ngayKy = ngayKy; }
    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public double getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(double luongCoBan) { this.luongCoBan = luongCoBan; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public int getIdnv() { return idnv; }
    public void setIdnv(int idnv) { this.idnv = idnv; }
    public int getIdloaihd() { return idloaihd; }
    public void setIdloaihd(int idloaihd) { this.idloaihd = idloaihd; }
    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }
    public String getTenChucVu() { return tenChucVu; }
    public void setTenChucVu(String tenChucVu) { this.tenChucVu = tenChucVu; }
    public String getTenPhongBan() { return tenPhongBan; }
    public void setTenPhongBan(String tenPhongBan) { this.tenPhongBan = tenPhongBan; }
    public String getTenChiNhanh() { return tenChiNhanh; }
    public void setTenChiNhanh(String tenChiNhanh) { this.tenChiNhanh = tenChiNhanh; }
}
