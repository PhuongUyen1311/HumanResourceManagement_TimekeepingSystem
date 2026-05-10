package com.qlns.dao;

import com.qlns.model.TaiKhoan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {
    
    public List<TaiKhoan> getAll() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT tk.*, nv.TENNV, nq.TENNHOMQUYEN " +
                     "FROM TAIKHOAN tk " +
                     "LEFT JOIN NHANVIEN nv ON tk.IDNV = nv.IDNV " +
                     "LEFT JOIN NHOMQUYEN nq ON tk.IDNQ = nq.IDNQ";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setIdTk(rs.getInt("IDTK"));
                tk.setTenTk(rs.getString("TENTK"));
                tk.setPassword(rs.getString("PASSWORD"));
                tk.setTrangThai(rs.getBoolean("TRANGTHAI"));
                tk.setIdNq(rs.getInt("IDNQ"));
                tk.setIdNv(rs.getInt("IDNV"));
                tk.setTenNv(rs.getString("TENNV"));
                tk.setTenNhomQuyen(rs.getString("TENNHOMQUYEN"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean update(TaiKhoan tk) {
        String sql = "UPDATE TAIKHOAN SET IDNQ = ?, TRANGTHAI = ? WHERE IDTK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, tk.getIdNq());
            pst.setBoolean(2, tk.isTrangThai());
            pst.setInt(3, tk.getIdTk());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int idTk) {
        String sql = "DELETE FROM TAIKHOAN WHERE IDTK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idTk);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean resetPassword(int idTk, String newPass) {
        String sql = "UPDATE TAIKHOAN SET PASSWORD = ? WHERE IDTK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, newPass);
            pst.setInt(2, idTk);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createDefault(int idNv, String username) {
        String sql = "INSERT INTO TAIKHOAN (TENTK, PASSWORD, IDNQ, IDNV) VALUES (?, '123456', 4, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setInt(2, idNv);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            // Có thể username đã tồn tại
            e.printStackTrace();
        }
        return false;
    }
}
