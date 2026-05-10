package com.qlns.util;

import java.util.HashSet;
import java.util.Set;

public class UserSession {
    // Biến static lưu session (chỉ có 1 user đang hoạt động ở 1 thời điểm)
    private static String idTk;       // Mã tài khoản (VD: TK001)
    private static String idNv;       // Mã nhân viên (VD: NV001)
    private static String tenNv;      // Tên nhân viên hiển thị (VD: Nguyễn Văn A)
    private static String idQuyen;    // Mã nhóm quyền (từ bảng NHOMQUYEN) (VD: 1)
    private static String tenQuyen;   // Tên nhóm quyền (VD: Admin)
    private static String idChiNhanh; // Mã chi nhánh tương ứng của nhân viên đó (VD: CN1)
    private static String tenChiNhanh; // Tên chi nhánh (VD: Chi nhánh Miền Bắc)
    private static Set<String> permissions = new HashSet<>(); // Danh sách mã quyền chi tiết (MAQUYEN)

    // Phương thức thiết lập session khi đăng nhập thành công
    public static void setSession(String idTk, String idNv, String tenNv, String idQuyen, String tenQuyen, String idChiNhanh, String tenChiNhanh) {
        UserSession.idTk = idTk;
        UserSession.idNv = idNv;
        UserSession.tenNv = tenNv;
        UserSession.idQuyen = idQuyen;
        UserSession.tenQuyen = tenQuyen;
        UserSession.idChiNhanh = idChiNhanh;
        UserSession.tenChiNhanh = tenChiNhanh;
    }

    // Thiết lập danh sách quyền
    public static void setPermissions(Set<String> perms) {
        UserSession.permissions = perms;
    }

    // Kiểm tra xem user có mã quyền cụ thể không
    public static boolean hasPermission(String maQuyen) {
        return permissions.contains(maQuyen);
    }

    // Huỷ session khi đăng xuất
    public static void clearSession() {
        idTk = null;
        idNv = null;
        tenNv = null;
        idQuyen = null;
        tenQuyen = null;
        idChiNhanh = null;
        tenChiNhanh = null;
        permissions.clear();
    }

    public static boolean isLoggedClient() {
        return idTk != null;
    }

    // Các phương thức Getters
    public static String getIdTk() { return idTk; }
    public static String getIdNv() { return idNv; }
    public static String getTenNv() { return tenNv; }
    public static String getIdQuyen() { return idQuyen; }
    public static String getTenQuyen() { return tenQuyen; }
    public static String getIdChiNhanh() { return idChiNhanh; }
    public static String getTenChiNhanh() { return tenChiNhanh; }
    public static Set<String> getPermissions() { return permissions; }
}
