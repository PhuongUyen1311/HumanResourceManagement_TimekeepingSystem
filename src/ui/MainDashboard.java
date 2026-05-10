package com.qlns.ui;

import com.qlns.util.UserSession;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainDashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel centerPanel;

    public MainDashboard() {
        setTitle("Hệ thống Quản lý Nhân sự - " + UserSession.getTenNv() + " (" + UserSession.getTenQuyen() + ")");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // --- Sidebar (Menu bên trái) ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(30, 41, 59)); // Slate 800 - SaaS look
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblMenu = new JLabel("MENU");
        lblMenu.setForeground(Color.WHITE);
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblMenu);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton btnDashboard = createMenuButton("Tổng Quan");
        JButton btnNhanVien = createMenuButton("Nhân viên");
        JButton btnChiNhanh = createMenuButton("Chi Nhánh");
        JButton btnPhongBan = createMenuButton("Phòng Ban");
        JButton btnBangLuong = createMenuButton("Bảng Lương");
        JButton btnChamCong = createMenuButton("Bảng Công");
        JButton btnPhuCap = createMenuButton("Phụ Cấp");
        JButton btnKhoanTru = createMenuButton("Khoản Trừ");
        JButton btnHopDong = createMenuButton("Hợp Đồng");
        JButton btnTaiKhoan = createMenuButton("Tài Khoản");
        JButton btnPhanQuyen = createMenuButton("Phân Quyền");
        JButton btnLogout = createMenuButton("Đăng xuất");
        btnLogout.setBackground(new Color(220, 53, 69)); // Red for logout
        btnLogout.setOpaque(true);
        btnLogout.setBorderPainted(false);

        sidebar.add(btnDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnNhanVien);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnChiNhanh);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnPhongBan);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnBangLuong);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnChamCong);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnPhuCap);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnKhoanTru);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnHopDong);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnTaiKhoan);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnPhanQuyen);

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // --- Center Panel (Nội dung chính với CardLayout) ---
        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);

        // Thêm các panel chức năng vào CardLayout
        DashboardPanel pnlDashboard = new DashboardPanel();
        EmployeePanel pnlNhanVien = new EmployeePanel();
        BranchPanel pnlChiNhanh = new BranchPanel();
        PhongBanPanel pnlPhongBan = new PhongBanPanel();
        BangLuongPanel pnlBangLuong = new BangLuongPanel();
        ChamCongPanel pnlChamCong = new ChamCongPanel();
        PhuCapPanel pnlPhuCap = new PhuCapPanel();
        KhoanTruPanel pnlKhoanTru = new KhoanTruPanel();
        HopDongPanel pnlHopDong = new HopDongPanel();
        TaiKhoanPanel pnlTaiKhoan = new TaiKhoanPanel();
        PermissionPanel pnlPhanQuyen = new PermissionPanel();

        centerPanel.add(pnlDashboard, "Dashboard");
        centerPanel.add(pnlNhanVien, "NhanVien");
        centerPanel.add(pnlChiNhanh, "ChiNhanh");
        centerPanel.add(pnlPhongBan, "PhongBan");
        centerPanel.add(pnlBangLuong, "BangLuong");
        centerPanel.add(pnlChamCong, "ChamCong");
        centerPanel.add(pnlPhuCap, "PhuCap");
        centerPanel.add(pnlKhoanTru, "KhoanTru");
        centerPanel.add(pnlHopDong, "HopDong");
        centerPanel.add(pnlTaiKhoan, "TaiKhoan");
        centerPanel.add(pnlPhanQuyen, "PhanQuyen");

        add(centerPanel, BorderLayout.CENTER);

        // --- Action Listeners ---
        btnDashboard.addActionListener(e -> cardLayout.show(centerPanel, "Dashboard"));
        btnNhanVien.addActionListener(e -> cardLayout.show(centerPanel, "NhanVien"));
        btnChiNhanh.addActionListener(e -> cardLayout.show(centerPanel, "ChiNhanh"));
        btnPhongBan.addActionListener(e -> cardLayout.show(centerPanel, "PhongBan"));
        btnBangLuong.addActionListener(e -> cardLayout.show(centerPanel, "BangLuong"));
        btnChamCong.addActionListener(e -> cardLayout.show(centerPanel, "ChamCong"));
        btnPhuCap.addActionListener(e -> cardLayout.show(centerPanel, "PhuCap"));
        btnKhoanTru.addActionListener(e -> cardLayout.show(centerPanel, "KhoanTru"));
        btnHopDong.addActionListener(e -> cardLayout.show(centerPanel, "HopDong"));
        btnTaiKhoan.addActionListener(e -> cardLayout.show(centerPanel, "TaiKhoan"));
        btnPhanQuyen.addActionListener(e -> cardLayout.show(centerPanel, "PhanQuyen"));

        btnLogout.addActionListener(e -> {
            UserSession.clearSession();
            new LoginFrame().setVisible(true);
            this.dispose();
        });

        // --- Phân quyền UI linh động (RBAC) ---
        String roleId = UserSession.getIdQuyen();

        btnNhanVien.setVisible(UserSession.hasPermission("EMP_VIEW"));
        btnChiNhanh.setVisible(!roleId.equals("2") && !roleId.equals("3") && !roleId.equals("4")); // HR(2),
                                                                                                   // Accountant(3),
                                                                                                   // Emp(4) hide
        btnPhongBan.setVisible(UserSession.hasPermission("DEPT_VIEW"));
        btnBangLuong.setVisible(UserSession.hasPermission("SAL_VIEW") || roleId.equals("2") || roleId.equals("4"));
        btnChamCong.setVisible(UserSession.hasPermission("TIME_VIEW") || roleId.equals("4"));
        btnPhuCap.setVisible(UserSession.hasPermission("PC_VIEW"));
        btnKhoanTru.setVisible(UserSession.hasPermission("KT_VIEW"));
        btnHopDong.setVisible(UserSession.hasPermission("HD_VIEW"));
        btnTaiKhoan.setVisible(UserSession.hasPermission("TK_VIEW"));
        btnPhanQuyen.setVisible(UserSession.hasPermission("SYS_ADMIN") || roleId.equals("1"));

        // Nếu là Admin tối cao (SYS_ADMIN) thì bật tất cả
        if (UserSession.hasPermission("SYS_ADMIN") || roleId.equals("1")) {
            btnNhanVien.setVisible(true);
            btnChiNhanh.setVisible(true);
            btnPhongBan.setVisible(true);
            btnBangLuong.setVisible(true);
            btnChamCong.setVisible(true);
            btnPhuCap.setVisible(true);
            btnKhoanTru.setVisible(true);
            btnHopDong.setVisible(true);
            btnTaiKhoan.setVisible(true);
            btnPhanQuyen.setVisible(true);
        }
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(51, 65, 85)); // Slate 700
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return btn;
    }
}
