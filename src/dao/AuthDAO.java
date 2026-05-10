package com.qlns.dao;

import com.qlns.util.UserSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AuthDAO {
    
    public boolean login(String username, String password) {
        String query = "SELECT tk.IDTK, tk.IDNV, nv.TENNV, tk.IDNQ, nq.TENNHOMQUYEN, nv.IDCN, cn.TENCNHANH " +
                       "FROM TAIKHOAN tk " +
                       "JOIN NHANVIEN nv ON tk.IDNV = nv.IDNV " +
                       "JOIN NHOMQUYEN nq ON tk.IDNQ = nq.IDNQ " +
                       "LEFT JOIN CHINHANH cn ON nv.IDCN = cn.IDCN " +
                       "WHERE tk.TENTK = ? AND tk.PASSWORD = ? AND tk.TRANGTHAI = 1";
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, password);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String idTk = rs.getString("IDTK");
                    String idNv = rs.getString("IDNV");
                    String tenNv = rs.getString("TENNV");
                    String idNq = rs.getString("IDNQ");
                    String tenNq = rs.getString("TENNHOMQUYEN");
                    String idCn = rs.getString("IDCN");
                    String tenCn = rs.getString("TENCNHANH");

                    // Set session cơ bản
                    UserSession.setSession(idTk, idNv, tenNv, idNq, tenNq, idCn, tenCn != null ? tenCn : "Chưa xác định");

                    // Lấy danh sách quyền chi tiết (RBAC)
                    UserSession.setPermissions(getPermissions(idNq));
                    
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Set<String> getPermissions(String idNq) {
        Set<String> permissions = new HashSet<>();
        String sql = "SELECT q.MAQUYEN FROM PHANQUYEN pq " +
                     "JOIN QUYEN q ON pq.IDQUYEN = q.IDQUYEN " +
                     "WHERE pq.IDNQ = ?";
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) return permissions;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, idNq);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    permissions.add(rs.getString("MAQUYEN"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissions;
    }
}
