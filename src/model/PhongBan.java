package com.qlns.model;

import java.sql.Date;

public class PhongBan {
    private String idpb;
    private String idcn;
    private String tenpb;
    private String diachi;
    private Date ngaythanhlap;

    private String id_truongphong;
    private String tenTruongPhong;

    // Phục vụ UI (Join table)
    private String tenChiNhanh;

    public PhongBan() {
    }

    public PhongBan(String idpb, String idcn, String tenpb, String diachi, Date ngaythanhlap, String tenChiNhanh, String id_truongphong, String tenTruongPhong) {
        this.idpb = idpb;
        this.idcn = idcn;
        this.tenpb = tenpb;
        this.diachi = diachi;
        this.ngaythanhlap = ngaythanhlap;
        this.tenChiNhanh = tenChiNhanh;
        this.id_truongphong = id_truongphong;
        this.tenTruongPhong = tenTruongPhong;
    }

    public String getIdpb() { return idpb; }
    public void setIdpb(String idpb) { this.idpb = idpb; }

    public String getIdcn() { return idcn; }
    public void setIdcn(String idcn) { this.idcn = idcn; }

    public String getTenpb() { return tenpb; }
    public void setTenpb(String tenpb) { this.tenpb = tenpb; }

    public String getDiachi() { return diachi; }
    public void setDiachi(String diachi) { this.diachi = diachi; }

    public Date getNgaythanhlap() { return ngaythanhlap; }
    public void setNgaythanhlap(Date ngaythanhlap) { this.ngaythanhlap = ngaythanhlap; }

    public String getTenChiNhanh() { return tenChiNhanh; }
    public void setTenChiNhanh(String tenChiNhanh) { this.tenChiNhanh = tenChiNhanh; }

    public String getId_truongphong() { return id_truongphong; }
    public void setId_truongphong(String id_truongphong) { this.id_truongphong = id_truongphong; }

    public String getTenTruongPhong() { return tenTruongPhong; }
    public void setTenTruongPhong(String tenTruongPhong) { this.tenTruongPhong = tenTruongPhong; }
}
