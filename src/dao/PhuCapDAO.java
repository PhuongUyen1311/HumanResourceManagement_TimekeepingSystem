package com.qlns.dao;

import com.qlns.model.PhuCap;
import com.qlns.model.NhanVienPhuCap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhuCapDAO {
    public List<PhuCap> getAllPhuCap() {
        List<PhuCap> list = new ArrayList<>();
        String sql = "SELECT * FROM PHUCAP";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new PhuCap(rs.getInt("IDPC"), rs.getString("TENPHUCAP"), 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addPhuCap(PhuCap pc) {
        String sql = "INSERT INTO PHUCAP (TENPHUCAP) VALUES (?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, pc.getTenPhuCap());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePhuCap(PhuCap pc) {
        String sql = "UPDATE PHUCAP SET TENPHUCAP = ? WHERE IDPC = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, pc.getTenPhuCap());
            pst.setInt(2, pc.getIdpc());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePhuCap(int idpc) {
        String sql = "DELETE FROM PHUCAP WHERE IDPC = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idpc);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<NhanVienPhuCap> getListNhanVienPhuCap() {
        List<NhanVienPhuCap> list = new ArrayList<>();
        String sql = "SELECT nvp.*, nv.TENNV, pc.TENPHUCAP " +
                     "FROM NHANVIEN_PHUCAP nvp " +
                     "JOIN NHANVIEN nv ON nvp.IDNV = nv.IDNV " +
                     "JOIN PHUCAP pc ON nvp.IDPC = pc.IDPC";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                NhanVienPhuCap nvp = new NhanVienPhuCap();
                nvp.setIdnv(rs.getString("IDNV"));
                nvp.setIdpc(rs.getInt("IDPC"));
                nvp.setTenNv(rs.getString("TENNV"));
                nvp.setTenPhuCap(rs.getString("TENPHUCAP"));
                nvp.setSoTien(rs.getDouble("SOTIEN"));
                nvp.setNgayApdung(rs.getDate("NGAYAPDUNG"));
                list.add(nvp);
            }
            System.out.println("DEBUG: getListNhanVienPhuCap found " + list.size() + " records");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
