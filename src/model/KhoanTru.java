package com.qlns.model;

public class KhoanTru {
    private int idkt;
    private String tenKhoanTru;
    private double mucTien;

    public KhoanTru() {}

    public KhoanTru(int idkt, String tenKhoanTru, double mucTien) {
        this.idkt = idkt;
        this.tenKhoanTru = tenKhoanTru;
        this.mucTien = mucTien;
    }

    public int getIdkt() { return idkt; }
    public void setIdkt(int idkt) { this.idkt = idkt; }
    public String getTenKhoanTru() { return tenKhoanTru; }
    public void setTenKhoanTru(String tenKhoanTru) { this.tenKhoanTru = tenKhoanTru; }
    public double getMucTien() { return mucTien; }
    public void setMucTien(double mucTien) { this.mucTien = mucTien; }
}
