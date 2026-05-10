package com.qlns.ui;

import com.qlns.dao.ChamCongDAO;
import com.qlns.model.BangChamCong;
import com.qlns.util.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ChamCongPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<Integer> cbThang;
    private JComboBox<Integer> cbNam;
    private JTextField txtSearch;
    private JButton btnLoc;
    private ChamCongDAO chamCongDAO;

    public ChamCongPanel() {
        chamCongDAO = new ChamCongDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();

        cbThang.setSelectedItem(4);
        cbNam.setSelectedItem(2024);
        loadDataToTable();
    }

    private void initComponents() {
        // Top Panel: Title & Filter
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ BẢNG CHẤM CÔNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(230, 126, 34)); // Orange
        topPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Tháng: "));
        cbThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cbThang.addItem(i);
        }
        filterPanel.add(cbThang);

        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filterPanel.add(new JLabel("Năm: "));
        cbNam = new JComboBox<>();
        for (int i = 2020; i <= 2030; i++) {
            cbNam.addItem(i);
        }
        filterPanel.add(cbNam);

        filterPanel.add(new JLabel("Tìm tên: "));
        txtSearch = new JTextField(12);
        txtSearch.setPreferredSize(new Dimension(150, 30));
        filterPanel.add(txtSearch);

        btnLoc = new JButton("Lọc Dữ Liệu");
        UIUtils.styleButton(btnLoc, new Color(52, 152, 219));
        btnLoc.addActionListener(e -> loadDataToTable());
        filterPanel.add(btnLoc);

        topPanel.add(filterPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Table
        String[] columns = { "ID", "Tên Nhân Viên", "Tháng", "Năm", "Công chuẩn", "Ngày làm", "Đi trễ", "Nghỉ",
                "Tăng ca", "Trạng thái" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);

        // Hide filter for Employee
        if (com.qlns.util.UserSession.getIdQuyen().equals("4")) {
            topPanel.getComponent(1).setVisible(false);
        }

        // Add double click listener to open details
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    showDetailDialog();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(new JLabel("Mẹo: Click đúp vào một dòng để xem chi tiết chấm công."));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        String roleId = com.qlns.util.UserSession.getIdQuyen();
        List<BangChamCong> list;

        if (roleId.equals("4")) {
            list = chamCongDAO.getDanhSachChamCongByNhanVien(com.qlns.util.UserSession.getIdNv());
        } else {
            int thang = (int) cbThang.getSelectedItem();
            int nam = (int) cbNam.getSelectedItem();
            list = chamCongDAO.getDanhSachChamCong(thang, nam);
        }

        String searchText = txtSearch.getText().toLowerCase().trim();
        for (BangChamCong bc : list) {
            if (!searchText.isEmpty() && !bc.getTenNhanVien().toLowerCase().contains(searchText)) {
                continue;
            }
            tableModel.addRow(new Object[] {
                    bc.getIdbc(),
                    bc.getTenNhanVien(),
                    bc.getThang(),
                    bc.getNam(),
                    26, // Công chuẩn mặc định
                    bc.getTongNgayLam(),
                    bc.getSoNgayDiTre() + " ngày",
                    bc.getSoNgayNghi() + " ngày",
                    bc.getSoGioTangCa() + "h",
                    bc.getTrangThai()
            });
        }
    }

    private void showDetailDialog() {
        int selectedRow = table.getSelectedRow();
        String idbc = table.getValueAt(selectedRow, 0).toString();
        String tenNhanVien = table.getValueAt(selectedRow, 1).toString();
        int thang = Integer.parseInt(table.getValueAt(selectedRow, 2).toString());
        int nam = Integer.parseInt(table.getValueAt(selectedRow, 3).toString());

        Window window = SwingUtilities.getWindowAncestor(this);
        Frame frame = (window instanceof Frame) ? (Frame) window : null;

        TimesheetDetailDialog dialog = new TimesheetDetailDialog(frame, idbc, tenNhanVien, thang, nam);
        dialog.setVisible(true);
    }
}
