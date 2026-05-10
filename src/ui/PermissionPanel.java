package com.qlns.ui;

import com.qlns.dao.DanhMucDAO;
import com.qlns.dao.PhanQuyenDAO;
import com.qlns.model.Item;
import com.qlns.util.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PermissionPanel extends JPanel {
    private JComboBox<Item> cbNhomQuyen;
    private JTable table;
    private DefaultTableModel tableModel;
    private PhanQuyenDAO phanQuyenDAO;
    private DanhMucDAO danhMucDAO;

    public PermissionPanel() {
        phanQuyenDAO = new PhanQuyenDAO();
        danhMucDAO = new DanhMucDAO();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        initComponents();
        loadNhomQuyen();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("CẤU HÌNH PHÂN QUYỀN HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.add(new JLabel("Chọn Nhóm Quyền: "));
        cbNhomQuyen = new JComboBox<>();
        cbNhomQuyen.setPreferredSize(new Dimension(250, 35));
        cbNhomQuyen.addActionListener(e -> loadPermissions());
        filterPanel.add(cbNhomQuyen);
        
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Table with Checkboxes
        tableModel = new DefaultTableModel(new Object[]{"Mã Quyền", "Tên Quyền Chi Tiết", "Cho Phép"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(Color.WHITE);
        JButton btnSave = new JButton("Lưu Thay Đổi");
        styleButton(btnSave, new Color(46, 204, 113));
        btnSave.addActionListener(e -> handleSave());
        bottomPanel.add(btnSave);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn, Color color) {
        UIUtils.styleButton(btn, color);
        btn.setPreferredSize(new Dimension(150, 40));
    }

    private void loadNhomQuyen() {
        cbNhomQuyen.removeAllItems();
        // Lấy danh sách nhóm quyền (có thể bổ sung method vào DanhMucDAO hoặc hardcode tạm)
        // Dựa vào schema: 1: Admin, 2: HR, 3: Kế toán, 4: Nhân viên, 5: Giám đốc chi nhánh
        cbNhomQuyen.addItem(new Item("1", "Quản trị viên (Admin)"));
        cbNhomQuyen.addItem(new Item("2", "Quản lý nhân sự (HR)"));
        cbNhomQuyen.addItem(new Item("3", "Kế toán (Accountant)"));
        cbNhomQuyen.addItem(new Item("5", "Giám đốc chi nhánh (Branch Manager)"));
        cbNhomQuyen.addItem(new Item("4", "Nhân viên (Employee)"));
    }

    private void loadPermissions() {
        tableModel.setRowCount(0);
        Item sel = (Item) cbNhomQuyen.getSelectedItem();
        if (sel == null) return;

        List<String[]> perms = phanQuyenDAO.getPermissionsByGroup(sel.getId());
        for (String[] p : perms) {
            tableModel.addRow(new Object[]{p[0], p[1], Boolean.parseBoolean(p[2])});
        }
    }

    private void handleSave() {
        Item sel = (Item) cbNhomQuyen.getSelectedItem();
        if (sel == null) return;

        List<Integer> selectedIds = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((Boolean) tableModel.getValueAt(i, 2)) {
                selectedIds.add(Integer.parseInt(tableModel.getValueAt(i, 0).toString()));
            }
        }

        if (phanQuyenDAO.updatePermissions(sel.getId(), selectedIds)) {
            JOptionPane.showMessageDialog(this, "Cập nhật quyền thành công! Quyền sẽ có hiệu lực sau khi người dùng đăng nhập lại.");
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu phân quyền!");
        }
    }
}
