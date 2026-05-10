package com.qlns.ui;

import com.qlns.dao.ChamCongDAO;
import com.qlns.model.ChiTietBangCong;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TimesheetDetailDialog extends JDialog {
    private JTable table;
    private DefaultTableModel tableModel;
    private String idbc;
    private ChamCongDAO chamCongDAO;

    public TimesheetDetailDialog(Frame owner, String idbc, String tenNhanVien, int thang, int nam) {
        super(owner, "Chi tiết chấm công - " + tenNhanVien + " (" + thang + "/" + nam + ")", true);
        this.idbc = idbc;
        this.chamCongDAO = new ChamCongDAO();
        
        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        tableModel = new DefaultTableModel(new Object[]{"Ngày làm", "Giờ vào", "Giờ ra", "Giờ tăng ca", "Phút đi trễ", "Loại nghỉ phép"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnClose);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        List<ChiTietBangCong> list = chamCongDAO.getChiTietChamCong(idbc);
        for (ChiTietBangCong ct : list) {
            tableModel.addRow(new Object[]{
                ct.getNgayLam() != null ? ct.getNgayLam().toString() : "",
                ct.getGioVao() != null ? ct.getGioVao().toString() : "",
                ct.getGioRa() != null ? ct.getGioRa().toString() : "",
                ct.getSoGioTangCa(),
                ct.getSoPhutDiTre(),
                ct.getTenLoaiNghiPhep() != null ? ct.getTenLoaiNghiPhep() : ""
            });
        }
    }
}
