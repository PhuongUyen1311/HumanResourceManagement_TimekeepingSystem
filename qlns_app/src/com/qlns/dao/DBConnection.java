package com.qlns.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // ✅ DÙNG INSTANCE NAME (QUAN TRỌNG)
    private static final String SERVER_NAME = "localhost\\SQL2025_CLEAN";

    private static final String DB_NAME = "QuanLyNhanSu";

    // ❗ Nếu dùng SQL Server Authentication thì điền vào
    private static final String USER = "";
    private static final String PASSWORD = ""; // sửa lại password của bạn

    // ✅ Connection URL chuẩn
    private static final String CONNECTION_URL = "jdbc:sqlserver://" + SERVER_NAME +
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
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối: " + e.getMessage());
            e.printStackTrace();
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