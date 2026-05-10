package com.qlns.ui;

import com.qlns.dao.ChiNhanhDAO;
import com.qlns.model.ChiNhanh;
import com.qlns.util.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BranchPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public BranchPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Header
        JLabel lblTitle = new JLabel("Quản Lý Chi Nhánh");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Tên Chi Nhánh", "Hotline", "Địa Chỉ"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        UIUtils.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(Color.WHITE);
        
        JButton btnAdd = new JButton("Thêm Chi Nhánh");
        JButton btnEdit = new JButton("Sửa Chi Nhánh");
        JButton btnRefresh = new JButton("Làm mới");

        styleButton(btnAdd, new Color(46, 204, 113));
        styleButton(btnEdit, new Color(241, 196, 15));
        styleButton(btnRefresh, new Color(52, 152, 219));

        btnRefresh.addActionListener(e -> loadData());

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnRefresh);

        add(actionPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void styleButton(JButton btn, Color color) {
        UIUtils.styleButton(btn, color);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        ChiNhanhDAO dao = new ChiNhanhDAO();
        List<ChiNhanh> list = dao.getAllChiNhanh();
        for (ChiNhanh cn : list) {
            tableModel.addRow(new Object[]{cn.getIdcn(), cn.getTenChiNhanh(), cn.getHotline(), cn.getDiaChi()});
        }
    }
}
