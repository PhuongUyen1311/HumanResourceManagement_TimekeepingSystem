package com.qlns.dao;

import com.qlns.model.ChiNhanh;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiNhanhDAO {
    public List<ChiNhanh> getAllChiNhanh() {
        List<ChiNhanh> list = new ArrayList<>();
        String sql = "SELECT * FROM CHINHANH";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new ChiNhanh(rs.getInt("IDCN"), rs.getString("TENCNHANH"), rs.getString("HOTLINE"), rs.getString("DIACHI")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
