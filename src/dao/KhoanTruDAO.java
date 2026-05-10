package com.qlns.dao;

import com.qlns.model.KhoanTru;
import com.qlns.model.NhanVienKhoanTru;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhoanTruDAO {
    public List<KhoanTru> getAllKhoanTru() {
        List<KhoanTru> list = new ArrayList<>();
        String sql = "SELECT * FROM KHOANTRU";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new KhoanTru(rs.getInt("IDKT"), rs.getString("TENKHOANTRU"), 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addKhoanTru(KhoanTru kt) {
        String sql = "INSERT INTO KHOANTRU (TENKHOANTRU) VALUES (?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, kt.getTenKhoanTru());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateKhoanTru(KhoanTru kt) {
        String sql = "UPDATE KHOANTRU SET TENKHOANTRU = ? WHERE IDKT = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, kt.getTenKhoanTru());
            pst.setInt(2, kt.getIdkt());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteKhoanTru(int idkt) {
        String sql = "DELETE FROM KHOANTRU WHERE IDKT = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idkt);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<NhanVienKhoanTru> getListNhanVienKhoanTru() {
        List<NhanVienKhoanTru> list = new ArrayList<>();
        String sql = "SELECT nvk.*, nv.TENNV, kt.TENKHOANTRU " +
                     "FROM NHANVIEN_KHOANTRU nvk " +
                     "JOIN NHANVIEN nv ON nvk.IDNV = nv.IDNV " +
                     "JOIN KHOANTRU kt ON nvk.IDKT = kt.IDKT";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                NhanVienKhoanTru nvk = new NhanVienKhoanTru();
                nvk.setIdnv(rs.getString("IDNV"));
                nvk.setIdkt(rs.getInt("IDKT"));
                nvk.setTenNv(rs.getString("TENNV"));
                nvk.setTenKhoanTru(rs.getString("TENKHOANTRU"));
                nvk.setSoTien(rs.getDouble("SOTIEN"));
                nvk.setNgayApdung(rs.getDate("NGAYAPDUNG"));
                list.add(nvk);
            }
            System.out.println("DEBUG: getListNhanVienKhoanTru found " + list.size() + " records");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
