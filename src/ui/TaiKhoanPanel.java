package com.qlns.ui;

import com.qlns.dao.DanhMucDAO;
import com.qlns.dao.TaiKhoanDAO;
import com.qlns.model.Item;
import com.qlns.model.TaiKhoan;
import com.qlns.util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TaiKhoanPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TaiKhoanDAO taiKhoanDAO;
    private DanhMucDAO danhMucDAO;
    private List<TaiKhoan> currentList;
    private JTextField txtSearch;

    public TaiKhoanPanel() {
        taiKhoanDAO = new TaiKhoanDAO();
        danhMucDAO = new DanhMucDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        loadData();
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("QUẢN LÝ TÀI KHOẢN HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(41, 128, 185));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.add(new JLabel("Tìm tài khoản/NV: "));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));
        filterPanel.add(txtSearch);
        JButton btnLoc = new JButton("Lọc");
        styleButton(btnLoc, new Color(52, 152, 219));
        btnLoc.addActionListener(e -> loadData());
        filterPanel.add(btnLoc);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(lblTitle, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Tên tài khoản", "Nhân viên", "Nhóm quyền", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setOpaque(false);

        JButton btnEdit = new JButton("Đổi Nhóm Quyền");
        JButton btnLock = new JButton("Khóa/Mở Khóa");
        JButton btnReset = new JButton("Reset Mật Khẩu");
        JButton btnRefresh = new JButton("Làm Mới");

        UIUtils.styleButton(btnEdit, new Color(52, 152, 219));
        UIUtils.styleButton(btnLock, new Color(231, 76, 60));
        UIUtils.styleButton(btnReset, new Color(241, 196, 15));
        UIUtils.styleButton(btnRefresh, new Color(149, 165, 166));

        btnPanel.add(btnEdit);
        btnPanel.add(btnLock);
        btnPanel.add(btnReset);
        btnPanel.add(btnRefresh);
        add(btnPanel, BorderLayout.SOUTH);

        // Listeners
        btnRefresh.addActionListener(e -> loadData());

        btnLock.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                TaiKhoan tk = currentList.get(row);
                tk.setTrangThai(!tk.isTrangThai());
                if (taiKhoanDAO.update(tk)) {
                    JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái tài khoản!");
                    loadData();
                }
            }
        });

        btnReset.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                TaiKhoan tk = currentList.get(row);
                int confirm = JOptionPane.showConfirmDialog(this, "Reset mật khẩu tài khoản " + tk.getTenTk() + " về '123456'?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (taiKhoanDAO.resetPassword(tk.getIdTk(), "123456")) {
                        JOptionPane.showMessageDialog(this, "Đã reset mật khẩu thành công!");
                    }
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                TaiKhoan tk = currentList.get(row);
                showEditRoleDialog(tk);
            }
        });
    }

    private void styleButton(JButton btn, Color color) {
        UIUtils.styleButton(btn, color);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        currentList = taiKhoanDAO.getAll();
        String searchText = txtSearch.getText().toLowerCase().trim();
        for (TaiKhoan tk : currentList) {
            if (!searchText.isEmpty() && 
                !tk.getTenTk().toLowerCase().contains(searchText) && 
                !tk.getTenNv().toLowerCase().contains(searchText)) {
                continue;
            }
            tableModel.addRow(new Object[]{
                tk.getIdTk(),
                tk.getTenTk(),
                tk.getTenNv(),
                tk.getTenNhomQuyen(),
                tk.isTrangThai() ? "Đang hoạt động" : "Đã khóa"
            });
        }
    }

    private void showEditRoleDialog(TaiKhoan tk) {
        // Mocking roles for simplicity, should fetch from DB
        String[] roles = {"System Admin", "HR Manager", "Accountant", "Employee", "Branch Manager"};
        String selected = (String) JOptionPane.showInputDialog(this, "Chọn nhóm quyền mới:", "Phân quyền", 
                JOptionPane.QUESTION_MESSAGE, null, roles, tk.getTenNhomQuyen());
        
        if (selected != null) {
            int newRoleId = 4; // Default to Employee
            if (selected.equals("System Admin")) newRoleId = 1;
            else if (selected.equals("HR Manager")) newRoleId = 2;
            else if (selected.equals("Accountant")) newRoleId = 3;
            else if (selected.equals("Branch Manager")) newRoleId = 5;
            
            tk.setIdNq(newRoleId);
            if (taiKhoanDAO.update(tk)) {
                JOptionPane.showMessageDialog(this, "Đã cập nhật nhóm quyền!");
                loadData();
            }
        }
    }
}
