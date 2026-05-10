package com.qlns.model;

import java.sql.Date;

public class NhanVienPhuCap {
    private String idnv;
    private int idpc;
    private String tenNv;
    private String tenPhuCap;
    private double soTien;
    private Date ngayApdung;

    public NhanVienPhuCap() {}

    public String getIdnv() { return idnv; }
    public void setIdnv(String idnv) { this.idnv = idnv; }

    public int getIdpc() { return idpc; }
    public void setIdpc(int idpc) { this.idpc = idpc; }

    public String getTenNv() { return tenNv; }
    public void setTenNv(String tenNv) { this.tenNv = tenNv; }

    public String getTenPhuCap() { return tenPhuCap; }
    public void setTenPhuCap(String tenPhuCap) { this.tenPhuCap = tenPhuCap; }

    public double getSoTien() { return soTien; }
    public void setSoTien(double soTien) { this.soTien = soTien; }

    public Date getNgayApdung() { return ngayApdung; }
    public void setNgayApdung(Date ngayApdung) { this.ngayApdung = ngayApdung; }
}
