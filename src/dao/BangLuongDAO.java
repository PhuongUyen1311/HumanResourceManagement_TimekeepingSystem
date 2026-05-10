package com.qlns.dao;

import com.qlns.model.BangLuong;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BangLuongDAO {

    public List<BangLuong> getDanhSachBangLuong(int thang, int nam, String idChiNhanh) {
        List<BangLuong> list = new ArrayList<>();
        String sql = "SELECT bl.*, nv.TENNV, bc.THANG, bc.NAM FROM BANGLUONG bl " +
                     "INNER JOIN BANGCHAMCONG bc ON bl.IDBC = bc.IDBC " +
                     "INNER JOIN NHANVIEN nv ON bc.IDNV = nv.IDNV " +
                     "WHERE bc.THANG = ? AND bc.NAM = ?";
        
        if (idChiNhanh != null && !idChiNhanh.equals("0")) {
            sql += " AND nv.IDCN = ? ";
        }

        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, thang);
            pst.setInt(2, nam);
            if (idChiNhanh != null && !idChiNhanh.equals("0")) {
                pst.setInt(3, Integer.parseInt(idChiNhanh));
            }
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    BangLuong bl = new BangLuong();
                    bl.setIdbl(rs.getString("IDBL"));
                    bl.setIdbc(rs.getString("IDBC"));
                    bl.setLuongCoBan(rs.getDouble("LUONGCOBAN"));
                    bl.setLuongThucTe(rs.getDouble("LUONGTHUCTE"));
                    bl.setThucNhan(rs.getDouble("THUCNHAN"));
                    bl.setTenNhanVien(rs.getString("TENNV"));
                    bl.setTrangThai(rs.getString("TRANGTHAI"));
                    bl.setThang(rs.getInt("THANG"));
                    bl.setNam(rs.getInt("NAM"));
                    
                    list.add(bl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BangLuong> getDanhSachBangLuongByNhanVien(String idNv) {
        List<BangLuong> list = new ArrayList<>();
        String sql = "SELECT bl.*, nv.TENNV, bc.THANG, bc.NAM FROM BANGLUONG bl " +
                     "INNER JOIN BANGCHAMCONG bc ON bl.IDBC = bc.IDBC " +
                     "INNER JOIN NHANVIEN nv ON bc.IDNV = nv.IDNV " +
                     "WHERE nv.IDNV = ? ORDER BY bc.NAM DESC, bc.THANG DESC";
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, idNv);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    BangLuong bl = new BangLuong();
                    bl.setIdbl(rs.getString("IDBL"));
                    bl.setIdbc(rs.getString("IDBC"));
                    bl.setLuongCoBan(rs.getDouble("LUONGCOBAN"));
                    bl.setLuongThucTe(rs.getDouble("LUONGTHUCTE"));
                    bl.setThucNhan(rs.getDouble("THUCNHAN"));
                    bl.setTenNhanVien(rs.getString("TENNV"));
                    bl.setTrangThai(rs.getString("TRANGTHAI"));
                    bl.setThang(rs.getInt("THANG"));
                    bl.setNam(rs.getInt("NAM"));
                    list.add(bl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
