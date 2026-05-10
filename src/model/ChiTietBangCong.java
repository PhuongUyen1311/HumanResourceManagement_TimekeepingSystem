package com.qlns.model;

import java.sql.Date;
import java.sql.Time;

public class ChiTietBangCong {
    private String idct;
    private String idbc;
    private Date ngayLam;
    private Time gioVao;
    private Time gioRa;
    private double soGioTangCa;
    private int soPhutDiTre;
    private String idLnp; // ID Loại nghỉ phép

    // Phục vụ UI
    private String tenLoaiNghiPhep;

    public ChiTietBangCong() {
    }

    public String getIdct() { return idct; }
    public void setIdct(String idct) { this.idct = idct; }

    public String getIdbc() { return idbc; }
    public void setIdbc(String idbc) { this.idbc = idbc; }

    public Date getNgayLam() { return ngayLam; }
    public void setNgayLam(Date ngayLam) { this.ngayLam = ngayLam; }

    public Time getGioVao() { return gioVao; }
    public void setGioVao(Time gioVao) { this.gioVao = gioVao; }

    public Time getGioRa() { return gioRa; }
    public void setGioRa(Time gioRa) { this.gioRa = gioRa; }

    public double getSoGioTangCa() { return soGioTangCa; }
    public void setSoGioTangCa(double soGioTangCa) { this.soGioTangCa = soGioTangCa; }

    public int getSoPhutDiTre() { return soPhutDiTre; }
    public void setSoPhutDiTre(int soPhutDiTre) { this.soPhutDiTre = soPhutDiTre; }

    public String getIdLnp() { return idLnp; }
    public void setIdLnp(String idLnp) { this.idLnp = idLnp; }

    public String getTenLoaiNghiPhep() { return tenLoaiNghiPhep; }
    public void setTenLoaiNghiPhep(String tenLoaiNghiPhep) { this.tenLoaiNghiPhep = tenLoaiNghiPhep; }
}
