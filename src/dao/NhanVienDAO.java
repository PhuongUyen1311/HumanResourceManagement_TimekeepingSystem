package com.qlns.dao;

import com.qlns.model.NhanVien;
import com.qlns.util.UserSession;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    public List<NhanVien> getAll() {
        return searchByName(null);
    }

    public List<NhanVien> searchByName(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        String query = "SELECT nv.*, pb.TENPB, cv.TENCV, cn.TENCNHANH " +
                       "FROM NHANVIEN nv " +
                       "LEFT JOIN PHONGBAN pb ON nv.IDPB = pb.IDPB " +
                       "LEFT JOIN CHUCVU cv ON nv.IDCV = cv.IDCV " +
                       "LEFT JOIN CHINHANH cn ON nv.IDCN = cn.IDCN " +
                       "WHERE 1=1 ";
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            query += " AND nv.TENNV LIKE ? ";
        }
        
        // RBAC: Nếu không phải Admin thì chỉ thấy nhân viên cùng chi nhánh
        boolean isAdmin = UserSession.hasPermission("EMP_VIEW_ALL") || UserSession.getIdQuyen().equals("1");
        if (!isAdmin) {
            query += " AND nv.IDCN = ? ";
        }

        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        try (PreparedStatement pst = conn.prepareStatement(query)) {
             
            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                pst.setString(paramIndex++, "%" + keyword + "%");
            }
            if (!isAdmin) {
                pst.setInt(paramIndex++, Integer.parseInt(UserSession.getIdChiNhanh()));
            }

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    NhanVien nv = new NhanVien(
                        rs.getString("IDNV"), rs.getString("TENNV"), rs.getString("GIOITINH"),
                        rs.getDate("NGAYSINH"), rs.getString("CCCD"), rs.getString("EMAIL"),
                        rs.getString("DIENTHOAI"), rs.getString("DIACHI"), rs.getString("DANTOC"),
                        rs.getString("TONGIAO"), rs.getString("HONNHAN"), rs.getString("IDTD"),
                        rs.getString("IDCV"), rs.getString("IDPB"), rs.getString("IDCN")
                    );
                    
                    nv.setTenPhongBan(rs.getString("TENPB") != null ? rs.getString("TENPB") : "");
                    nv.setTenChucVu(rs.getString("TENCV") != null ? rs.getString("TENCV") : "");
                    nv.setTenChiNhanh(rs.getString("TENCNHANH") != null ? rs.getString("TENCNHANH") : "");
                    
                    list.add(nv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(NhanVien nv) {
        String query = "INSERT INTO NHANVIEN (IDNV, TENNV, GIOITINH, NGAYSINH, CCCD, EMAIL, DIENTHOAI, DIACHI, DANTOC, TONGIAO, HONNHAN, IDTD, IDCV, IDPB, IDCN) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(query, nv, true);
    }

    public boolean update(NhanVien nv) {
        String query = "UPDATE NHANVIEN SET TENNV=?, GIOITINH=?, NGAYSINH=?, CCCD=?, EMAIL=?, DIENTHOAI=?, DIACHI=?, DANTOC=?, TONGIAO=?, HONNHAN=?, IDTD=?, IDCV=?, IDPB=?, IDCN=? WHERE IDNV=?";
        return executeUpdate(query, nv, false);
    }

    public boolean delete(String idNv) {
        String query = "DELETE FROM NHANVIEN WHERE IDNV = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, idNv);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean executeUpdate(String query, NhanVien nv, boolean isInsert) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try (PreparedStatement pst = conn.prepareStatement(query)) {

            if (isInsert) {
                pst.setString(1, nv.getIdNv());
                pst.setString(2, nv.getTenNv());
                pst.setString(3, nv.getGioiTinh());
                pst.setDate(4, nv.getNgaySinh());
                pst.setString(5, nv.getCccd());
                pst.setString(6, nv.getEmail());
                pst.setString(7, nv.getDienThoai());
                pst.setString(8, nv.getDiaChi());
                pst.setString(9, nv.getDanToc());
                pst.setString(10, nv.getTonGiao());
                pst.setString(11, nv.getHonNhan());
                pst.setInt(12, Integer.parseInt(nv.getIdTrinhDo()));
                pst.setInt(13, Integer.parseInt(nv.getIdChucVu()));
                pst.setInt(14, Integer.parseInt(nv.getIdPhongBan()));
                pst.setInt(15, Integer.parseInt(nv.getIdChiNhanh()));
            } else {
                pst.setString(1, nv.getTenNv());
                pst.setString(2, nv.getGioiTinh());
                pst.setDate(3, nv.getNgaySinh());
                pst.setString(4, nv.getCccd());
                pst.setString(5, nv.getEmail());
                pst.setString(6, nv.getDienThoai());
                pst.setString(7, nv.getDiaChi());
                pst.setString(8, nv.getDanToc());
                pst.setString(9, nv.getTonGiao());
                pst.setString(10, nv.getHonNhan());
                pst.setInt(11, Integer.parseInt(nv.getIdTrinhDo()));
                pst.setInt(12, Integer.parseInt(nv.getIdChucVu()));
                pst.setInt(13, Integer.parseInt(nv.getIdPhongBan()));
                pst.setInt(14, Integer.parseInt(nv.getIdChiNhanh()));
                pst.setString(15, nv.getIdNv());
            }

            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean onboardingNhanVien(NhanVien nv, double luongCoBan, int idLoaiHD, int idNhomQuyen, 
                                     java.sql.Date ngayKy, java.sql.Date ngayBD, java.sql.Date ngayKT) {
        String sql = "{CALL sp_OnboardingNhanVien(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, nv.getTenNv());
            cstmt.setString(2, nv.getGioiTinh());
            cstmt.setDate(3, nv.getNgaySinh());
            cstmt.setString(4, nv.getCccd());
            cstmt.setString(5, nv.getEmail());
            cstmt.setString(6, nv.getDienThoai());
            cstmt.setInt(7, Integer.parseInt(nv.getIdTrinhDo()));
            cstmt.setInt(8, Integer.parseInt(nv.getIdChucVu()));
            cstmt.setInt(9, Integer.parseInt(nv.getIdPhongBan()));
            cstmt.setInt(10, Integer.parseInt(nv.getIdChiNhanh()));
            cstmt.setDouble(11, luongCoBan);
            cstmt.setInt(12, idLoaiHD);
            cstmt.setInt(13, idNhomQuyen);
            cstmt.setDate(14, ngayKy);
            cstmt.setDate(15, ngayBD);
            cstmt.setDate(16, ngayKT);

            cstmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
