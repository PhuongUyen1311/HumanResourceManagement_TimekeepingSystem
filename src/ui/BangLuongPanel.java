package com.qlns.ui;

import com.qlns.dao.BangLuongDAO;
import com.qlns.model.BangLuong;
import com.qlns.util.UserSession;
import com.qlns.util.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BangLuongPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<com.qlns.model.Item> cbChiNhanh;
    private JComboBox<Integer> cbThang;
    private JComboBox<Integer> cbNam;
    private JTextField txtSearch;
    private JButton btnLoc;
    private BangLuongDAO bangLuongDAO;

    public BangLuongPanel() {
        bangLuongDAO = new BangLuongDAO();
        
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
        JLabel lblTitle = new JLabel("QUẢN LÝ BẢNG LƯƠNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(46, 204, 113));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.add(new JLabel("Chi nhánh: "));
        cbChiNhanh = new JComboBox<>();
        filterPanel.add(cbChiNhanh);

        filterPanel.add(new JLabel("Tháng: "));
        cbThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbThang.addItem(i);
        filterPanel.add(cbThang);
        
        filterPanel.add(new JLabel("Năm: "));
        cbNam = new JComboBox<>();
        for (int i = 2020; i <= 2030; i++) cbNam.addItem(i);
        filterPanel.add(cbNam);
        
        filterPanel.add(new JLabel("Tìm tên: "));
        txtSearch = new JTextField(12);
        txtSearch.setPreferredSize(new Dimension(150, 30));
        filterPanel.add(txtSearch);

        btnLoc = new JButton("Lọc Dữ Liệu");
        UIUtils.styleButton(btnLoc, new Color(52, 152, 219));
        btnLoc.addActionListener(e -> loadDataToTable());
        filterPanel.add(btnLoc);

        // Load Chi nhánh data
        com.qlns.dao.DanhMucDAO danhMucDAO = new com.qlns.dao.DanhMucDAO();
        cbChiNhanh.addItem(new com.qlns.model.Item("0", "-- Tất cả --"));
        for (com.qlns.model.Item cn : danhMucDAO.getListChiNhanh()) cbChiNhanh.addItem(cn);

        topPanel.add(filterPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Table
        String[] columnNames;
        if (UserSession.getIdQuyen().equals("4")) {
            columnNames = new String[]{"Mã BL", "Tháng", "Năm", "Lương CB", "Lương Thực", "Thực Nhận", "Trạng Thái"};
            filterPanel.setVisible(false); // Hide filter for Employee
        } else {
            columnNames = new String[]{"Mã BL", "Tên Nhân Viên", "Lương CB", "Lương Thực", "Thực Nhận", "Trạng Thái"};
        }

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String roleId = UserSession.getIdQuyen();

        if (roleId.equals("4")) {
            List<BangLuong> list = bangLuongDAO.getDanhSachBangLuongByNhanVien(UserSession.getIdNv());
            for (BangLuong bl : list) {
                tableModel.addRow(new Object[]{
                        bl.getIdbl(),
                        bl.getThang(),
                        bl.getNam(),
                        currencyFormat.format(bl.getLuongCoBan()),
                        currencyFormat.format(bl.getLuongThucTe()),
                        currencyFormat.format(bl.getThucNhan()),
                        bl.getTrangThai()
                });
            }
        } else {
            int thang = (int) cbThang.getSelectedItem();
            int nam = (int) cbNam.getSelectedItem();
            com.qlns.model.Item selCN = (com.qlns.model.Item) cbChiNhanh.getSelectedItem();
            String idCN = (selCN != null) ? selCN.getId() : "0";
            
            String searchText = txtSearch.getText().toLowerCase().trim();
            List<BangLuong> list = bangLuongDAO.getDanhSachBangLuong(thang, nam, idCN);
            for (BangLuong bl : list) {
                if (!searchText.isEmpty() && !bl.getTenNhanVien().toLowerCase().contains(searchText)) {
                    continue;
                }
                tableModel.addRow(new Object[]{
                        bl.getIdbl(),
                        bl.getTenNhanVien(),
                        currencyFormat.format(bl.getLuongCoBan()),
                        currencyFormat.format(bl.getLuongThucTe()),
                        currencyFormat.format(bl.getThucNhan()),
                        bl.getTrangThai()
                });
            }
        }
    }
}
