package com.qlns.ui;

import com.qlns.dao.DanhMucDAO;
import com.qlns.model.Item;
import com.qlns.model.NhanVien;
import com.qlns.service.NhanVienService;
import com.qlns.util.UserSession;
import com.qlns.util.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeePanel extends JPanel {
    private JTable tbNhanVien;
    private DefaultTableModel tableModel;
    private NhanVienService nhanVienService;
    private DanhMucDAO danhMucDAO;

    private JComboBox<Item> cbChiNhanh, cbPhongBan, cbChucVu;
    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnView, btnDelete, btnRefresh;

    private List<NhanVien> currentList;

    public EmployeePanel() {
        nhanVienService = new NhanVienService();
        danhMucDAO = new DanhMucDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        loadComboboxData();
        loadDataToTable();
    }

    private void initComponents() {
        // --- Filter Panel (NORTH) ---
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlFilter.setBackground(new Color(248, 249, 250));
        pnlFilter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        pnlFilter.add(new JLabel("Chi nhánh:"));
        cbChiNhanh = new JComboBox<>();
        cbChiNhanh.setPreferredSize(new Dimension(180, 30));
        pnlFilter.add(cbChiNhanh);

        pnlFilter.add(new JLabel("Phòng ban:"));
        cbPhongBan = new JComboBox<>();
        cbPhongBan.setPreferredSize(new Dimension(180, 30));
        pnlFilter.add(cbPhongBan);

        pnlFilter.add(new JLabel("Chức vụ:"));
        cbChucVu = new JComboBox<>();
        cbChucVu.setPreferredSize(new Dimension(180, 30));
        pnlFilter.add(cbChucVu);

        pnlFilter.add(new JLabel("Tìm tên:"));
        txtSearch = new JTextField(12);
        txtSearch.setPreferredSize(new Dimension(150, 30));
        pnlFilter.add(txtSearch);

        btnSearch = new JButton("Lọc");
        btnSearch.setPreferredSize(new Dimension(80, 30));
        UIUtils.styleButton(btnSearch, new Color(52, 152, 219));
        pnlFilter.add(btnSearch);

        add(pnlFilter, BorderLayout.NORTH);

        // --- Table & Action Buttons (CENTER) ---
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setOpaque(false);

        String[] columns = { "Mã NV", "Tên NV", "Giới tính", "Phòng ban", "Chức vụ", "Chi nhánh", "Email" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Chỉ cho phép sửa trực tiếp Tên, Giới tính, Email trên bảng
                return column == 1 || column == 2 || column == 6;
            }
        };
        tbNhanVien = new JTable(tableModel);
        UIUtils.styleTable(tbNhanVien);

        // Inline Edit Confirmation
        tableModel.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (row >= 0 && row < currentList.size()) {
                    String newValue = tableModel.getValueAt(row, col).toString();
                    NhanVien nv = currentList.get(row);

                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Xác nhận cập nhật thông tin nhân viên " + nv.getIdNv() + "?",
                            "Xác nhận sửa", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // Update local object and DB
                        if (col == 1)
                            nv.setTenNv(newValue);
                        if (col == 2)
                            nv.setGioiTinh(newValue);
                        if (col == 6)
                            nv.setEmail(newValue);
                        nhanVienService.updateNhanVien(nv);
                    } else {
                        // Revert change
                        loadDataToTable();
                    }
                }
            }
        });

        pnlCenter.add(new JScrollPane(tbNhanVien), BorderLayout.CENTER);

        // Buttons Panel
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);

        btnAdd = new JButton("Tiếp nhận NV");
        btnView = new JButton("Xem chi tiết");
        btnDelete = new JButton("Xóa");
        btnRefresh = new JButton("Làm mới");

        UIUtils.styleButton(btnAdd, new Color(46, 204, 113));
        UIUtils.styleButton(btnView, new Color(52, 152, 219));
        UIUtils.styleButton(btnDelete, new Color(231, 76, 60));
        UIUtils.styleButton(btnRefresh, new Color(149, 165, 166));

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnView);
        pnlButtons.add(btnDelete);
        pnlButtons.add(btnRefresh);

        pnlCenter.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlCenter, BorderLayout.CENTER);

        // --- Listeners ---
        btnSearch.addActionListener(e -> loadDataToTable());
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            cbChiNhanh.setSelectedIndex(0);
            loadDataToTable();
        });

        btnDelete.addActionListener(e -> {
            int row = tbNhanVien.getSelectedRow();
            if (row != -1) {
                String id = tableModel.getValueAt(row, 0).toString();
                if (JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + id + "?") == JOptionPane.YES_OPTION) {
                    nhanVienService.deleteNhanVien(id);
                    loadDataToTable();
                }
            }
        });

        btnAdd.addActionListener(e -> {
            EmployeeDialog dialog = new EmployeeDialog((Frame) SwingUtilities.getWindowAncestor(this),
                    "Tiếp nhận nhân viên mới", null);
            dialog.setVisible(true);
            if (dialog.isSuccess())
                loadDataToTable();
        });

        btnView.addActionListener(e -> {
            int row = tbNhanVien.getSelectedRow();
            if (row != -1) {
                EmployeeDialog dialog = new EmployeeDialog((Frame) SwingUtilities.getWindowAncestor(this),
                        "Chi tiết nhân viên", currentList.get(row));
                dialog.setVisible(true);
            }
        });

        cbChiNhanh.addActionListener(e -> {
            Item sel = (Item) cbChiNhanh.getSelectedItem();
            if (sel != null)
                loadPhongBan(sel.getId());
        });
    }

    private void styleButton(JButton btn, Color color) {
        UIUtils.styleButton(btn, color);
    }

    private void loadComboboxData() {
        // Load Chi nhánh
        cbChiNhanh.addItem(new Item("0", "-- Tất cả chi nhánh --"));
        for (Item cn : danhMucDAO.getListChiNhanh())
            cbChiNhanh.addItem(cn);

        // Load Chức vụ
        cbChucVu.addItem(new Item("0", "-- Tất cả chức vụ --"));
        for (Item cv : danhMucDAO.getListChucVu())
            cbChucVu.addItem(cv);

        // RBAC: Nếu không phải Admin (1) hoặc HR (2) thì khóa chi nhánh
        if (!UserSession.getIdQuyen().equals("1") && !UserSession.getIdQuyen().equals("2")) {
            cbChiNhanh.setEnabled(false);
            for (int i = 0; i < cbChiNhanh.getItemCount(); i++) {
                if (cbChiNhanh.getItemAt(i).getId().equals(UserSession.getIdChiNhanh())) {
                    cbChiNhanh.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void loadPhongBan(String idCN) {
        cbPhongBan.removeAllItems();
        cbPhongBan.addItem(new Item("0", "-- Tất cả phòng ban --"));
        List<Item> pbs = (idCN == null || idCN.equals("0")) ? danhMucDAO.getListPhongBan(null)
                : danhMucDAO.getListPhongBan(idCN);
        for (Item pb : pbs)
            cbPhongBan.addItem(pb);
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        String name = txtSearch.getText();

        Item selCN = (Item) cbChiNhanh.getSelectedItem();
        String idCN = (selCN != null) ? selCN.getId() : "0";

        Item selPB = (Item) cbPhongBan.getSelectedItem();
        String idPB = (selPB != null) ? selPB.getId() : "0";

        Item selCV = (Item) cbChucVu.getSelectedItem();
        String idCV = (selCV != null) ? selCV.getId() : "0";

        // Lọc dữ liệu Local để demo nhanh (Thực tế nên lọc qua SQL)
        currentList = nhanVienService.getAllNhanVien();

        for (NhanVien nv : currentList) {
            boolean match = true;
            if (!name.isEmpty() && !nv.getTenNv().toLowerCase().contains(name.toLowerCase()))
                match = false;
            if (!idCN.equals("0") && !nv.getIdChiNhanh().equals(idCN))
                match = false;
            if (!idPB.equals("0") && !nv.getIdPhongBan().equals(idPB))
                match = false;
            if (!idCV.equals("0") && !nv.getIdChucVu().equals(idCV))
                match = false;

            if (match) {
                tableModel.addRow(new Object[] {
                        nv.getIdNv(), nv.getTenNv(), nv.getGioiTinh(),
                        nv.getTenPhongBan(), nv.getTenChucVu(), nv.getTenChiNhanh(), nv.getEmail()
                });
            }
        }
    }
}
