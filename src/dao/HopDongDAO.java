package com.qlns.dao;

import com.qlns.model.HopDong;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HopDongDAO {
    public List<HopDong> getAllHopDong() {
        List<HopDong> list = new ArrayList<>();
        String sql = "SELECT hd.*, nv.TENNV, cv.TENCV, pb.TENPB, cn.TENCNHANH " +
                     "FROM HOPDONG hd " +
                     "JOIN NHANVIEN nv ON hd.IDNV = nv.IDNV " +
                     "JOIN CHUCVU cv ON nv.IDCV = cv.IDCV " +
                     "JOIN PHONGBAN pb ON nv.IDPB = pb.IDPB " +
                     "JOIN CHINHANH cn ON nv.IDCN = cn.IDCN";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                HopDong hd = new HopDong();
                hd.setSodh(rs.getInt("SODH"));
                hd.setNgayKy(rs.getDate("NGAYKY"));
                hd.setNgayBatDau(rs.getDate("NGAYBATDAU"));
                hd.setNgayKetThuc(rs.getDate("NGAYKETTHUC"));
                hd.setLuongCoBan(rs.getDouble("LUONGCOBAN"));
                hd.setTrangThai(rs.getString("TRANGTHAI"));
                hd.setIdnv(rs.getInt("IDNV"));
                hd.setIdloaihd(rs.getInt("IDLOAIHD"));
                hd.setTenNhanVien(rs.getString("TENNV"));
                hd.setTenChucVu(rs.getString("TENCV"));
                hd.setTenPhongBan(rs.getString("TENPB"));
                hd.setTenChiNhanh(rs.getString("TENCNHANH"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
