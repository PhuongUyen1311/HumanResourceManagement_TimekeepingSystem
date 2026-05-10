package com.qlns.dao;

import com.qlns.model.PhongBan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhongBanDAO {

    public List<PhongBan> getDanhSachPhongBan(String idChiNhanh) {
        List<PhongBan> list = new ArrayList<>();
        String sql = "SELECT p.IDPB, p.IDCN, p.TENPB, p.DIACHI, p.NGAYTHANHLAP, p.ID_TRUONGPHONG, c.TENCNHANH, nv.TENNV AS TENTRUONGPHONG " +
                     "FROM PHONGBAN p " +
                     "INNER JOIN CHINHANH c ON p.IDCN = c.IDCN " +
                     "LEFT JOIN NHANVIEN nv ON p.ID_TRUONGPHONG = nv.IDNV ";
        
        if (idChiNhanh != null && !idChiNhanh.equals("ALL") && !idChiNhanh.equals("0")) {
            sql += " WHERE p.IDCN = ?";
        }
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
             
            if (idChiNhanh != null && !idChiNhanh.equals("ALL") && !idChiNhanh.equals("0")) {
                pst.setString(1, idChiNhanh);
            }
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    PhongBan pb = new PhongBan();
                    pb.setIdpb(rs.getString("IDPB"));
                    pb.setIdcn(rs.getString("IDCN"));
                    pb.setTenpb(rs.getString("TENPB"));
                    pb.setDiachi(rs.getString("DIACHI"));
                    pb.setNgaythanhlap(rs.getDate("NGAYTHANHLAP"));
                    pb.setTenChiNhanh(rs.getString("TENCNHANH"));
                    pb.setId_truongphong(rs.getString("ID_TRUONGPHONG"));
                    pb.setTenTruongPhong(rs.getString("TENTRUONGPHONG"));
                    list.add(pb);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
