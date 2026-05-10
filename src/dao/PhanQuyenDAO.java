package com.qlns.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhanQuyenDAO {
    
    public List<String[]> getPermissionsByGroup(String idNQ) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT q.IDQUYEN, q.TENQUYEN, CASE WHEN pq.IDNQ IS NOT NULL THEN 'TRUE' ELSE 'FALSE' END AS HAS_PERM " +
                     "FROM QUYEN q " +
                     "LEFT JOIN PHANQUYEN pq ON q.IDQUYEN = pq.IDQUYEN AND pq.IDNQ = ?";
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, idNQ);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new String[]{rs.getString("IDQUYEN"), rs.getString("TENQUYEN"), rs.getString("HAS_PERM")});
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean updatePermissions(String idNQ, List<Integer> selectedQuyenIds) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        
        try {
            conn.setAutoCommit(false);
            // 1. Delete old perms
            String delSql = "DELETE FROM PHANQUYEN WHERE IDNQ = ?";
            try (PreparedStatement pst = conn.prepareStatement(delSql)) {
                pst.setString(1, idNQ);
                pst.executeUpdate();
            }
            
            // 2. Insert new perms
            String insSql = "INSERT INTO PHANQUYEN (IDNQ, IDQUYEN) VALUES (?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(insSql)) {
                for (Integer idQ : selectedQuyenIds) {
                    pst.setString(1, idNQ);
                    pst.setInt(2, idQ);
                    pst.addBatch();
                }
                pst.executeBatch();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) {}
        }
        return false;
    }
}
