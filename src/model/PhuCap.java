package com.qlns.model;

public class PhuCap {
    private int idpc;
    private String tenPhuCap;
    private double mucTien;

    public PhuCap() {}

    public PhuCap(int idpc, String tenPhuCap, double mucTien) {
        this.idpc = idpc;
        this.tenPhuCap = tenPhuCap;
        this.mucTien = mucTien;
    }

    public int getIdpc() { return idpc; }
    public void setIdpc(int idpc) { this.idpc = idpc; }
    public String getTenPhuCap() { return tenPhuCap; }
    public void setTenPhuCap(String tenPhuCap) { this.tenPhuCap = tenPhuCap; }
    public double getMucTien() { return mucTien; }
    public void setMucTien(double mucTien) { this.mucTien = mucTien; }
}
