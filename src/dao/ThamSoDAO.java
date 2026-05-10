package com.qlns.dao;

import com.qlns.model.ThamSo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ThamSoDAO {
    public List<ThamSo> getAllThamSo() {
        List<ThamSo> list = new ArrayList<>();
        String sql = "SELECT * FROM THAMSO_HETHONG";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                ThamSo ts = new ThamSo();
                ts.setMaTs(rs.getString("MA_TS"));
                ts.setTenTs(rs.getString("TEN_TS"));
                ts.setGiaTri(rs.getDouble("GIA_TRI"));
                list.add(ts);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
