package com.qlns.model;

import java.sql.Date;

public class NhanVienKhoanTru {
    private String idnv;
    private int idkt;
    private String tenNv;
    private String tenKhoanTru;
    private double soTien;
    private Date ngayApdung;

    public NhanVienKhoanTru() {}

    public String getIdnv() { return idnv; }
    public void setIdnv(String idnv) { this.idnv = idnv; }

    public int getIdkt() { return idkt; }
    public void setIdkt(int idkt) { this.idkt = idkt; }

    public String getTenNv() { return tenNv; }
    public void setTenNv(String tenNv) { this.tenNv = tenNv; }

    public String getTenKhoanTru() { return tenKhoanTru; }
    public void setTenKhoanTru(String tenKhoanTru) { this.tenKhoanTru = tenKhoanTru; }

    public double getSoTien() { return soTien; }
    public void setSoTien(double soTien) { this.soTien = soTien; }

    public Date getNgayApdung() { return ngayApdung; }
    public void setNgayApdung(Date ngayApdung) { this.ngayApdung = ngayApdung; }
}
