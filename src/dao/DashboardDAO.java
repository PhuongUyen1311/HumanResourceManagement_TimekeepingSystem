package com.qlns.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardDAO {
    
    public Map<String, Object> getStats(String branchId) {
        Map<String, Object> stats = new HashMap<>();
        String where = (branchId != null && !branchId.equals("TOAN_QUOC") && !branchId.equals("0")) ? " WHERE IDCN = ?" : "";
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) return stats;

        try {
            // 1. Tổng nhân viên
            String sqlTotal = "SELECT COUNT(*) FROM NHANVIEN" + where;
            try (PreparedStatement pst = conn.prepareStatement(sqlTotal)) {
                if (!where.isEmpty()) pst.setString(1, branchId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) stats.put("totalEmp", rs.getInt(1));
                }
            }

            // 2. Nhân viên đang làm việc
            String sqlActive = "SELECT COUNT(*) FROM NHANVIEN WHERE TRANGTHAI = N'Đang làm việc'" + (where.isEmpty() ? "" : " AND IDCN = ?");
            try (PreparedStatement pst = conn.prepareStatement(sqlActive)) {
                if (!where.isEmpty()) pst.setString(1, branchId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) stats.put("activeEmp", rs.getInt(1));
                }
            }

            // 3. Vắng mặt (Mock - thực tế dựa trên chấm công ngày hôm nay)
            stats.put("absentEmp", 3); 
            
            // 4. KPI Hoàn thành (Mock)
            stats.put("kpi", "92%");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }

    public List<Object[]> getDepartmentStats(String branchId) {
        List<Object[]> list = new ArrayList<>();
        String where = (branchId != null && !branchId.equals("TOAN_QUOC") && !branchId.equals("0")) ? " WHERE pb.IDCN = ?" : "";
        
        String sql = "SELECT pb.TENPB, COUNT(nv.IDNV) AS SL, 0 AS VANG, '15.0M' AS LUONG_TB " +
                     "FROM PHONGBAN pb " +
                     "LEFT JOIN NHANVIEN nv ON pb.IDPB = nv.IDPB " +
                     where + " GROUP BY pb.TENPB";
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            if (!where.isEmpty()) pst.setString(1, branchId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getString("TENPB"),
                        rs.getInt("SL"),
                        rs.getInt("VANG"),
                        rs.getString("LUONG_TB")
                    });
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
