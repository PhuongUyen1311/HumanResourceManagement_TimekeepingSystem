package com.qlns.model;

import java.sql.Date;

public class NhanVien {
    private String idNv;
    private String tenNv;
    private String gioiTinh;
    private Date ngaySinh;
    private String cccd;
    private String email;
    private String dienThoai;
    private String diaChi;
    private String danToc;
    private String tonGiao;
    private String honNhan;
    private String idTrinhDo;
    private String idChucVu;
    private String idPhongBan;
    private String idChiNhanh;

    // Fields mở rộng cho hiển thị UI (JOIN)
    private String tenPhongBan;
    private String tenChucVu;
    private String tenChiNhanh;

    // Constructors
    public NhanVien() {}

    public NhanVien(String idNv, String tenNv, String gioiTinh, Date ngaySinh, String cccd, String email,
                    String dienThoai, String diaChi, String danToc, String tonGiao, String honNhan,
                    String idTrinhDo, String idChucVu, String idPhongBan, String idChiNhanh) {
        this.idNv = idNv;
        this.tenNv = tenNv;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.cccd = cccd;
        this.email = email;
        this.dienThoai = dienThoai;
        this.diaChi = diaChi;
        this.danToc = danToc;
        this.tonGiao = tonGiao;
        this.honNhan = honNhan;
        this.idTrinhDo = idTrinhDo;
        this.idChucVu = idChucVu;
        this.idPhongBan = idPhongBan;
        this.idChiNhanh = idChiNhanh;
    }

    public String getTenPhongBan() { return tenPhongBan; }
    public void setTenPhongBan(String tenPhongBan) { this.tenPhongBan = tenPhongBan; }

    public String getTenChucVu() { return tenChucVu; }
    public void setTenChucVu(String tenChucVu) { this.tenChucVu = tenChucVu; }

    public String getTenChiNhanh() { return tenChiNhanh; }
    public void setTenChiNhanh(String tenChiNhanh) { this.tenChiNhanh = tenChiNhanh; }

    public String getIdNv() { return idNv; }
    public void setIdNv(String idNv) { this.idNv = idNv; }

    public String getTenNv() { return tenNv; }
    public void setTenNv(String tenNv) { this.tenNv = tenNv; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDienThoai() { return dienThoai; }
    public void setDienThoai(String dienThoai) { this.dienThoai = dienThoai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getDanToc() { return danToc; }
    public void setDanToc(String danToc) { this.danToc = danToc; }

    public String getTonGiao() { return tonGiao; }
    public void setTonGiao(String tonGiao) { this.tonGiao = tonGiao; }

    public String getHonNhan() { return honNhan; }
    public void setHonNhan(String honNhan) { this.honNhan = honNhan; }

    public String getIdTrinhDo() { return idTrinhDo; }
    public void setIdTrinhDo(String idTrinhDo) { this.idTrinhDo = idTrinhDo; }

    public String getIdChucVu() { return idChucVu; }
    public void setIdChucVu(String idChucVu) { this.idChucVu = idChucVu; }

    public String getIdPhongBan() { return idPhongBan; }
    public void setIdPhongBan(String idPhongBan) { this.idPhongBan = idPhongBan; }

    public String getIdChiNhanh() { return idChiNhanh; }
    public void setIdChiNhanh(String idChiNhanh) { this.idChiNhanh = idChiNhanh; }
}
