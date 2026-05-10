package com.qlns.ui;

import com.qlns.dao.KhoanTruDAO;
import com.qlns.model.KhoanTru;
import com.qlns.model.NhanVienKhoanTru;
import com.qlns.util.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KhoanTruPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public KhoanTruPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("QUẢN LÝ KHOẢN TRỪ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(41, 128, 185));
        add(lblTitle, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.add(new JLabel("Tìm tên NV: "));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));
        filterPanel.add(txtSearch);
        JButton btnLoc = new JButton("Lọc");
        UIUtils.styleButton(btnLoc, new Color(52, 152, 219));
        btnLoc.setPreferredSize(new Dimension(80, 30));
        btnLoc.addActionListener(e -> loadData());
        filterPanel.add(btnLoc);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(lblTitle, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Mã NV", "Tên Nhân Viên", "Tên Khoản Trừ", "Số Tiền", "Ngày Áp Dụng"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        JButton btnThem = new JButton("Gán Khoản Trừ");
        JButton btnXoa = new JButton("Gỡ Khoản Trừ");
        JButton btnRefresh = new JButton("Làm mới");
        
        styleButton(btnThem, new Color(46, 204, 113));
        styleButton(btnXoa, new Color(231, 76, 60));
        styleButton(btnRefresh, new Color(149, 165, 166));
        
        btnPanel.add(btnThem);
        btnPanel.add(btnXoa);
        btnPanel.add(btnRefresh);
        add(btnPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadData());
        loadData();
    }

    private void styleButton(JButton btn, Color color) {
        UIUtils.styleButton(btn, color);
        btn.setPreferredSize(new Dimension(130, 35));
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        KhoanTruDAO dao = new KhoanTruDAO();
        List<NhanVienKhoanTru> list = dao.getListNhanVienKhoanTru();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
        String searchText = txtSearch.getText().toLowerCase().trim();
        
        for (NhanVienKhoanTru nvk : list) {
            if (!searchText.isEmpty() && !nvk.getTenNv().toLowerCase().contains(searchText)) {
                continue;
            }
            tableModel.addRow(new Object[]{
                nvk.getIdnv(),
                nvk.getTenNv(),
                nvk.getTenKhoanTru(),
                df.format(nvk.getSoTien()),
                nvk.getNgayApdung()
            });
        }
    }
}
