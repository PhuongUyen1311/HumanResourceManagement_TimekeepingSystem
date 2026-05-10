package com.qlns.model;

public class TaiKhoan {
    private int idTk;
    private String tenTk;
    private String password;
    private boolean trangThai;
    private int idNq;
    private int idNv;
    
    // Bổ sung các trường hiển thị
    private String tenNv;
    private String tenNhomQuyen;

    public TaiKhoan() {}

    public TaiKhoan(int idTk, String tenTk, String password, boolean trangThai, int idNq, int idNv) {
        this.idTk = idTk;
        this.tenTk = tenTk;
        this.password = password;
        this.trangThai = trangThai;
        this.idNq = idNq;
        this.idNv = idNv;
    }

    public int getIdTk() { return idTk; }
    public void setIdTk(int idTk) { this.idTk = idTk; }

    public String getTenTk() { return tenTk; }
    public void setTenTk(String tenTk) { this.tenTk = tenTk; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }

    public int getIdNq() { return idNq; }
    public void setIdNq(int idNq) { this.idNq = idNq; }

    public int getIdNv() { return idNv; }
    public void setIdNv(int idNv) { this.idNv = idNv; }

    public String getTenNv() { return tenNv; }
    public void setTenNv(String tenNv) { this.tenNv = tenNv; }

    public String getTenNhomQuyen() { return tenNhomQuyen; }
    public void setTenNhomQuyen(String tenNhomQuyen) { this.tenNhomQuyen = tenNhomQuyen; }
}
