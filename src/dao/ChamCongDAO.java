package com.qlns.dao;

import com.qlns.model.BangChamCong;
import com.qlns.model.ChiTietBangCong;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChamCongDAO {

    public List<BangChamCong> getDanhSachChamCong(int thang, int nam) {
        List<BangChamCong> list = new ArrayList<>();
        String sql = "SELECT bc.*, nv.TENNV FROM BANGCHAMCONG bc " +
                     "INNER JOIN NHANVIEN nv ON bc.IDNV = nv.IDNV " +
                     "WHERE bc.THANG = ? AND bc.NAM = ?";
                     
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
             
            pst.setInt(1, thang);
            pst.setInt(2, nam);
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    BangChamCong bc = new BangChamCong();
                    bc.setIdbc(rs.getString("IDBC"));
                    bc.setIdnv(rs.getString("IDNV"));
                    bc.setThang(rs.getInt("THANG"));
                    bc.setNam(rs.getInt("NAM"));
                    bc.setSoGioTangCa(rs.getDouble("SOGIOTANGCA"));
                    bc.setSoNgayNghi(rs.getInt("SONGAYNGHI"));
                    bc.setSoNgayDiTre(rs.getInt("SONGAYDITRE"));
                    bc.setTongNgayLam(rs.getInt("TONGNGAYLAM"));
                    bc.setTrangThai(rs.getString("TRANGTHAI"));
                    bc.setTenNhanVien(rs.getString("TENNV"));
                    
                    list.add(bc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ChiTietBangCong> getChiTietChamCong(String idbc) {
        List<ChiTietBangCong> list = new ArrayList<>();
        String sql = "SELECT ct.*, lp.TENLOAI AS TENLOAINGHIPHEP FROM CHITIET_BANGCONG ct " +
                     "LEFT JOIN LOAINGHIPHEP lp ON ct.IDLNP = lp.IDLNP " +
                     "WHERE ct.IDBC = ? ORDER BY ct.NGAYLAM ASC";
                     
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
             
            pst.setString(1, idbc);
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    ChiTietBangCong ct = new ChiTietBangCong();
                    ct.setIdct(rs.getString("IDCT"));
                    ct.setIdbc(rs.getString("IDBC"));
                    ct.setNgayLam(rs.getDate("NGAYLAM"));
                    ct.setGioVao(rs.getTime("GIOVAO"));
                    ct.setGioRa(rs.getTime("GIORA"));
                    ct.setSoGioTangCa(rs.getDouble("SOGIOTANGCA"));
                    ct.setSoPhutDiTre(rs.getInt("SOPHUTDITRE"));
                    ct.setIdLnp(rs.getString("IDLNP"));
                    ct.setTenLoaiNghiPhep(rs.getString("TENLOAINGHIPHEP"));
                    
                    list.add(ct);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<BangChamCong> getDanhSachChamCongByNhanVien(String idNv) {
        List<BangChamCong> list = new ArrayList<>();
        String sql = "SELECT bc.*, nv.TENNV FROM BANGCHAMCONG bc " +
                     "INNER JOIN NHANVIEN nv ON bc.IDNV = nv.IDNV " +
                     "WHERE nv.IDNV = ? ORDER BY bc.NAM DESC, bc.THANG DESC";
                     
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, idNv);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    BangChamCong bc = new BangChamCong();
                    bc.setIdbc(rs.getString("IDBC"));
                    bc.setIdnv(rs.getString("IDNV"));
                    bc.setThang(rs.getInt("THANG"));
                    bc.setNam(rs.getInt("NAM"));
                    bc.setSoGioTangCa(rs.getDouble("SOGIOTANGCA"));
                    bc.setSoNgayNghi(rs.getInt("SONGAYNGHI"));
                    bc.setSoNgayDiTre(rs.getInt("SONGAYDITRE"));
                    bc.setTongNgayLam(rs.getInt("TONGNGAYLAM"));
                    bc.setTrangThai(rs.getString("TRANGTHAI"));
                    bc.setTenNhanVien(rs.getString("TENNV"));
                    list.add(bc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
