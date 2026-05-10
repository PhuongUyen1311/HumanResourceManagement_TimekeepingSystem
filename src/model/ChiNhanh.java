package com.qlns.model;

public class ChiNhanh {
    private int idcn;
    private String tenChiNhanh;
    private String hotline;
    private String diaChi;

    public ChiNhanh() {}

    public ChiNhanh(int idcn, String tenChiNhanh, String hotline, String diaChi) {
        this.idcn = idcn;
        this.tenChiNhanh = tenChiNhanh;
        this.hotline = hotline;
        this.diaChi = diaChi;
    }

    public int getIdcn() { return idcn; }
    public void setIdcn(int idcn) { this.idcn = idcn; }
    public String getTenChiNhanh() { return tenChiNhanh; }
    public void setTenChiNhanh(String tenChiNhanh) { this.tenChiNhanh = tenChiNhanh; }
    public String getHotline() { return hotline; }
    public void setHotline(String hotline) { this.hotline = hotline; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
}
