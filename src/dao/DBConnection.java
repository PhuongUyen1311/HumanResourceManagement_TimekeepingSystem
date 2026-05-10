package com.qlns.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBConnection {

    // ✅ CẤU HÌNH SERVER
    private static final String SERVER_NAME = "127.0.0.1";
    private static final String PORT = "1435";
    private static final String DB_NAME = "QuanLyNhanSu";

    // ✅ TÀI KHOẢN SQL SERVER (Vui lòng kiểm tra lại sa/password)
    private static final String USER = "sa";
    private static final String PASSWORD = "12345678";

    // ✅ Connection URL
    private static final String CONNECTION_URL = "jdbc:sqlserver://" + SERVER_NAME + ":" + PORT +
            ";databaseName=" + DB_NAME +
            ";encrypt=true;trustServerCertificate=true;";

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
                System.out.println("Kết nối CSDL thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Thiếu JDBC Driver");
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối: " + e.getMessage());
            String msg = "KHÔNG THỂ KẾT NỐI DATABASE!\n\n" +
                    "Instance: " + SERVER_NAME + "\n" +
                    "Lỗi: " + e.getMessage();
            JOptionPane.showMessageDialog(null, msg, "Lỗi Kết Nối", JOptionPane.ERROR_MESSAGE);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đã đóng kết nối.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            closeConnection();
        }
    }
}