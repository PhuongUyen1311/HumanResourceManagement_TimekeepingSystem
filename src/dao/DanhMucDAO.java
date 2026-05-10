package com.qlns.dao;

import com.qlns.model.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DanhMucDAO {

    public List<Item> getListChiNhanh() {
        List<Item> list = new ArrayList<>();
        String query = "SELECT IDCN, TENCNHANH FROM CHINHANH";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        
        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new Item(rs.getString("IDCN"), rs.getString("TENCNHANH")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Item> getListPhongBan(String idChiNhanh) {
        List<Item> list = new ArrayList<>();
        String query = "SELECT IDPB, TENPB FROM PHONGBAN";
        if (idChiNhanh != null && !idChiNhanh.equals("0")) {
            query += " WHERE IDCN = ?";
        }
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            if (idChiNhanh != null && !idChiNhanh.equals("0")) {
                pst.setString(1, idChiNhanh);
            }
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new Item(rs.getString("IDPB"), rs.getString("TENPB")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Item> getListChucVu() {
        List<Item> list = new ArrayList<>();
        String query = "SELECT IDCV, TENCV FROM CHUCVU";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new Item(rs.getString("IDCV"), rs.getString("TENCV")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Item> getListTrinhDo() {
        List<Item> list = new ArrayList<>();
        String query = "SELECT IDTD, TENTD, CHUYENNGANH FROM TRINHDO";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String label = rs.getString("TENTD");
                String cn = rs.getString("CHUYENNGANH");
                if (cn != null && !cn.isEmpty()) {
                    label += " (" + cn + ")";
                }
                list.add(new Item(rs.getString("IDTD"), label));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int addTrinhDo(String hocVi, String chuyenNganh) {
        String query = "INSERT INTO TRINHDO (TENTD, CHUYENNGANH) VALUES (?, ?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return -1;
        
        try (PreparedStatement pst = conn.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, hocVi);
            pst.setString(2, chuyenNganh);
            int affected = pst.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<String> getListHocVi() {
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT TENTD FROM TRINHDO";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) list.add(rs.getString("TENTD"));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<String> getListChuyenNganh(String hocVi) {
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT CHUYENNGANH FROM TRINHDO WHERE TENTD = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, hocVi);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String cn = rs.getString("CHUYENNGANH");
                    if (cn != null && !cn.isEmpty()) list.add(cn);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
