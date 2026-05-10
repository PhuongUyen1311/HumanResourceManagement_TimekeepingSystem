package com.qlns.ui;

import com.qlns.dao.DashboardDAO;
import com.qlns.util.UserSession;
import com.qlns.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class DashboardPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private DashboardDAO dashboardDAO;

    public DashboardPanel() {
        dashboardDAO = new DashboardDAO();
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel("BẢNG ĐIỀU KHIỂN HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(44, 62, 80));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JButton btnExport = new JButton("Xuất Excel Báo Cáo");
        styleButton(btnExport, new Color(39, 174, 96));
        headerPanel.add(btnExport, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        String roleId = UserSession.getIdQuyen();
        
        if (roleId.equals("1") || roleId.equals("2")) { // Admin or HR
            tabbedPane.addTab("Tổng Công Ty", createBranchDashboard("TOAN_QUOC"));
            tabbedPane.addTab("Chi Nhánh Miền Bắc", createBranchDashboard("1"));
            tabbedPane.addTab("Chi Nhánh Miền Trung", createBranchDashboard("2"));
            tabbedPane.addTab("Chi Nhánh Miền Nam", createBranchDashboard("3"));
        } else if (roleId.equals("5")) { // Branch Manager
            tabbedPane.addTab(UserSession.getTenChiNhanh(), createBranchDashboard(UserSession.getIdChiNhanh()));
        } else {
            tabbedPane.addTab("Đơn vị của tôi", createBranchDashboard(UserSession.getIdChiNhanh()));
        }

        add(tabbedPane, BorderLayout.CENTER);
        
        btnExport.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đang khởi tạo dữ liệu và xuất file Excel...");
        });
    }

    private JPanel createBranchDashboard(String branchId) {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        Map<String, Object> stats = dashboardDAO.getStats(branchId);

        // Stats Row
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.add(createStatCard("Tổng Nhân Viên", String.valueOf(stats.getOrDefault("totalEmp", 0)), new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Đang Làm Việc", String.valueOf(stats.getOrDefault("activeEmp", 0)), new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Vắng Mặt", String.valueOf(stats.getOrDefault("absentEmp", 0)), new Color(231, 76, 60)));
        statsPanel.add(createStatCard("KPI Hoàn Thành", (String)stats.getOrDefault("kpi", "0%"), new Color(155, 89, 182)));
        panel.add(statsPanel, BorderLayout.NORTH);

        // Charts & Table Row
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.add(createPremiumChart("Biến động nhân sự", "LINE"));
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(223, 228, 234)), "Thống kê chi tiết phòng ban"));
        
        String[] cols = {"Phòng Ban", "Nhân sự", "Vắng", "Lương TB"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        List<Object[]> deptStats = dashboardDAO.getDepartmentStats(branchId);
        for (Object[] row : deptStats) model.addRow(row);
        
        JTable table = new JTable(model);
        UIUtils.styleTable(table);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        bottomPanel.add(tablePanel);
        panel.add(bottomPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(223, 228, 234), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(new Color(127, 140, 141));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValue.setForeground(color);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(lblValue);
        return card;
    }

    private void styleButton(JButton btn, Color color) {
        UIUtils.styleButton(btn, color);
        btn.setPreferredSize(new Dimension(180, 40));
    }

    private JPanel createPremiumChart(String title, String type) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(223, 228, 234), 1, true));
        
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        card.add(lblTitle, BorderLayout.NORTH);
        
        JPanel chartContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight(), padding = 40;
                if ("LINE".equals(type)) {
                    g2.setColor(new Color(240, 240, 240));
                    for(int i=0; i<5; i++) {
                        int y = padding + i * (h - 2*padding) / 4;
                        g2.drawLine(padding, y, w - padding, y);
                    }
                    int[] data = {30, 45, 35, 60, 50, 75, 65};
                    int xStep = (w - 2*padding) / (data.length - 1);
                    g2.setStroke(new BasicStroke(3f));
                    g2.setColor(new Color(52, 152, 219));
                    int lastX = -1, lastY = -1;
                    for(int i=0; i<data.length; i++) {
                        int x = padding + i * xStep;
                        int y = h - padding - (data[i] * (h - 2*padding) / 100);
                        if (lastX != -1) g2.drawLine(lastX, lastY, x, y);
                        lastX = x; lastY = y;
                        g2.fillOval(x-4, y-4, 8, 8);
                    }
                }
            }
        };
        chartContainer.setOpaque(false);
        card.add(chartContainer, BorderLayout.CENTER);
        return card;
    }
}
