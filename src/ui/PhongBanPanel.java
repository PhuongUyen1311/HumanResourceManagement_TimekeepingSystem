package com.qlns.ui;

import com.qlns.dao.DanhMucDAO;
import com.qlns.dao.PhongBanDAO;
import com.qlns.model.Item;
import com.qlns.model.PhongBan;
import com.qlns.util.UserSession;
import com.qlns.util.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class PhongBanPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<Item> cbChiNhanh;
    private PhongBanDAO phongBanDAO;
    private DanhMucDAO danhMucDAO;

    public PhongBanPanel() {
        phongBanDAO = new PhongBanDAO();
        danhMucDAO = new DanhMucDAO();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        loadDuLieuChiNhanh();
        loadDataToTable();
    }

    private void initComponents() {
        // Top Panel: Title & Filter
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ PHÒNG BAN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(41, 128, 185));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.add(new JLabel("Lọc theo Chi nhánh: "));
        cbChiNhanh = new JComboBox<>();
        cbChiNhanh.setPreferredSize(new Dimension(200, 30));
        cbChiNhanh.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loadDataToTable();
            }
        });
        filterPanel.add(cbChiNhanh);

        filterPanel.add(new JLabel("Tìm tên phòng: "));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 30));
        filterPanel.add(txtSearch);

        JButton btnSearch = new JButton("Lọc");
        UIUtils.styleButton(btnSearch, new Color(52, 152, 219));
        btnSearch.addActionListener(e -> loadDataToTable());
        filterPanel.add(btnSearch);

        topPanel.add(filterPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Table
        String[] columns = {"ID", "Tên Phòng Ban", "Địa Chỉ", "Ngày Lập", "Trưởng Phòng", "Chi Nhánh"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void styleButton(JButton btn, Color color) {
        UIUtils.styleButton(btn, color);
    }

    private void loadDuLieuChiNhanh() {
        cbChiNhanh.removeAllItems();
        if (UserSession.getIdQuyen() != null && (UserSession.getIdQuyen().equals("1") || UserSession.getIdQuyen().equals("2"))) {
            cbChiNhanh.addItem(new Item("0", "--- Tất cả Chi nhánh ---"));
            for (Item it : danhMucDAO.getListChiNhanh()) cbChiNhanh.addItem(it);
        } else {
            String idcnSession = UserSession.getIdChiNhanh();
            for (Item it : danhMucDAO.getListChiNhanh()) {
                if(it.getId().equals(idcnSession)) {
                    cbChiNhanh.addItem(it);
                    break;
                }
            }
            cbChiNhanh.setEnabled(false);
        }
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        Item selectedItem = (Item) cbChiNhanh.getSelectedItem();
        String idChiNhanh = (selectedItem != null) ? selectedItem.getId() : "0";
        String searchText = txtSearch.getText().toLowerCase().trim();

        List<PhongBan> list = phongBanDAO.getDanhSachPhongBan(idChiNhanh);
        for (PhongBan pb : list) {
            if (!searchText.isEmpty() && !pb.getTenpb().toLowerCase().contains(searchText)) {
                continue;
            }
            tableModel.addRow(new Object[]{
                    pb.getIdpb(),
                    pb.getTenpb(),
                    pb.getDiachi(),
                    pb.getNgaythanhlap(),
                    pb.getTenTruongPhong() != null ? pb.getTenTruongPhong() : "---",
                    pb.getTenChiNhanh()
            });
        }
    }
    private JTextField txtSearch;
}
