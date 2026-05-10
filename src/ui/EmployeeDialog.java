package com.qlns.ui;

import com.qlns.dao.DanhMucDAO;
import com.qlns.model.Item;
import com.qlns.model.NhanVien;
import com.qlns.service.NhanVienService;
import com.qlns.util.UserSession;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class EmployeeDialog extends JDialog {
    private JTextField txtTenNV, txtEmail, txtCCCD, txtPhone, txtSalary;
    private JSpinner spNgayKy, spNgayBD, spNgayKT, spNgaySinh;
    private JComboBox<Item> cbChiNhanh, cbPhongBan, cbChucVu, cbLoaiHD;
    private JComboBox<String> cbHocVi, cbChuyenNganh;
    private JRadioButton rbNam, rbNu;
    private JButton btnSave, btnCancel;
    
    private NhanVienService nhanVienService;
    private DanhMucDAO danhMucDAO;
    private boolean isSuccess = false;

    public EmployeeDialog(Frame owner, String title, NhanVien nv) {
        super(owner, title, true);
        nhanVienService = new NhanVienService();
        danhMucDAO = new DanhMucDAO();
        
        setSize(550, 750);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        initComponents(nv == null);
        loadData();
        
        if (nv != null) fillData(nv);

        add(createFooter(), BorderLayout.SOUTH);
    }

    private void initComponents(boolean isAdd) {
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;
        // Tên
        gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1; txtTenNV = new JTextField(20); pnlForm.add(txtTenNV, gbc);

        // Giới tính
        row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Giới tính:"), gbc);
        JPanel pnlGT = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rbNam = new JRadioButton("Nam", true); rbNu = new JRadioButton("Nữ");
        ButtonGroup bg = new ButtonGroup(); bg.add(rbNam); bg.add(rbNu);
        pnlGT.add(rbNam); pnlGT.add(rbNu);
        gbc.gridx = 1; pnlForm.add(pnlGT, gbc);

        // Ngày sinh
        row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Ngày sinh:"), gbc);
        spNgaySinh = createDateSpinner(1990);
        gbc.gridx = 1; pnlForm.add(spNgaySinh, gbc);

        // Email
        row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; txtEmail = new JTextField(); pnlForm.add(txtEmail, gbc);

        // CCCD
        row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("CCCD:"), gbc);
        gbc.gridx = 1; txtCCCD = new JTextField(); pnlForm.add(txtCCCD, gbc);

        // SĐT
        row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 1; txtPhone = new JTextField(); pnlForm.add(txtPhone, gbc);

        // Chi nhánh
        row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Chi nhánh:"), gbc);
        cbChiNhanh = new JComboBox<>(); gbc.gridx = 1; pnlForm.add(cbChiNhanh, gbc);

        // Phòng ban
        row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Phòng ban:"), gbc);
        cbPhongBan = new JComboBox<>(); gbc.gridx = 1; pnlForm.add(cbPhongBan, gbc);

        // Chức vụ
        row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Chức vụ:"), gbc);
        cbChucVu = new JComboBox<>(); gbc.gridx = 1; pnlForm.add(cbChucVu, gbc);

        // Học vị
        row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Học vị:"), gbc);
        cbHocVi = new JComboBox<>(); gbc.gridx = 1; pnlForm.add(cbHocVi, gbc);

        // Chuyên ngành
        row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Chuyên ngành:"), gbc);
        cbChuyenNganh = new JComboBox<>();
        cbChuyenNganh.setEditable(true);
        gbc.gridx = 1; pnlForm.add(cbChuyenNganh, gbc);

        if (isAdd) {
            row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Lương cơ bản:"), gbc);
            txtSalary = new JTextField(); gbc.gridx = 1; pnlForm.add(txtSalary, gbc);
            
            row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Loại Hợp đồng:"), gbc);
            cbLoaiHD = new JComboBox<>();
            cbLoaiHD.addItem(new Item("1", "Hợp đồng thử việc"));
            cbLoaiHD.addItem(new Item("2", "Hợp đồng xác định thời hạn"));
            cbLoaiHD.addItem(new Item("3", "Hợp đồng không xác định thời hạn"));
            gbc.gridx = 1; pnlForm.add(cbLoaiHD, gbc);

            row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Ngày ký:"), gbc);
            spNgayKy = createDateSpinner(2024); gbc.gridx = 1; pnlForm.add(spNgayKy, gbc);

            row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Ngày bắt đầu:"), gbc);
            spNgayBD = createDateSpinner(2024); gbc.gridx = 1; pnlForm.add(spNgayBD, gbc);

            row++; gbc.gridx = 0; gbc.gridy = row; pnlForm.add(new JLabel("Ngày kết thúc:"), gbc);
            spNgayKT = createDateSpinner(2025); gbc.gridx = 1; pnlForm.add(spNgayKT, gbc);

            cbLoaiHD.addActionListener(e -> {
                Item sel = (Item) cbLoaiHD.getSelectedItem();
                if (sel != null && sel.getId().equals("3")) {
                    spNgayKT.setEnabled(false);
                } else {
                    spNgayKT.setEnabled(true);
                }
            });
        }

        add(new JScrollPane(pnlForm), BorderLayout.CENTER);
        
        cbChiNhanh.addActionListener(e -> {
            Item sel = (Item) cbChiNhanh.getSelectedItem();
            if(sel != null) loadPhongBan(sel.getId());
        });

        cbHocVi.addActionListener(e -> {
            String hv = (String) cbHocVi.getSelectedItem();
            if (hv != null) loadChuyenNganh(hv);
        });
    }

    private JSpinner createDateSpinner(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        SpinnerDateModel model = new SpinnerDateModel(cal.getTime(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
        spinner.setEditor(editor);
        return spinner;
    }

    private JPanel createFooter() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnSave = new JButton("Xác nhận");
        btnCancel = new JButton("Hủy");
        btnSave.addActionListener(e -> handleSave());
        btnCancel.addActionListener(e -> dispose());
        pnl.add(btnSave); pnl.add(btnCancel);
        return pnl;
    }

    private void loadData() {
        for (Item cn : danhMucDAO.getListChiNhanh()) cbChiNhanh.addItem(cn);
        for (Item cv : danhMucDAO.getListChucVu()) cbChucVu.addItem(cv);
        for (String hv : danhMucDAO.getListHocVi()) cbHocVi.addItem(hv);
        
        if (!UserSession.getIdQuyen().equals("1") && !UserSession.getIdQuyen().equals("2")) {
            cbChiNhanh.setEnabled(false);
            setComboboxSelectedItem(cbChiNhanh, UserSession.getIdChiNhanh());
            loadPhongBan(UserSession.getIdChiNhanh());
        }
    }

    private void loadPhongBan(String idCN) {
        cbPhongBan.removeAllItems();
        for(Item pb : danhMucDAO.getListPhongBan(idCN)) cbPhongBan.addItem(pb);
    }

    private void loadChuyenNganh(String hocVi) {
        cbChuyenNganh.removeAllItems();
        for (String cn : danhMucDAO.getListChuyenNganh(hocVi)) cbChuyenNganh.addItem(cn);
    }

    private void fillData(NhanVien nv) {
        txtTenNV.setText(nv.getTenNv());
        txtEmail.setText(nv.getEmail());
        txtCCCD.setText(nv.getCccd());
        txtPhone.setText(nv.getDienThoai());
        if ("Nữ".equals(nv.getGioiTinh())) rbNu.setSelected(true);
        spNgaySinh.setValue(nv.getNgaySinh());
        
        setComboboxSelectedItem(cbChiNhanh, nv.getIdChiNhanh());
        setComboboxSelectedItem(cbPhongBan, nv.getIdPhongBan());
        setComboboxSelectedItem(cbChucVu, nv.getIdChucVu());
        
        // Cần lấy IDTD để mapping về Học vị / Chuyên ngành
        // Tạm thời để trống hoặc bổ sung DAO lấy theo IDTD
    }

    private void handleSave() {
        if (txtTenNV.getText().trim().isEmpty() || txtCCCD.getText().length() != 12) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và CCCD (12 số)!");
            return;
        }

        // Xử lý trình độ: học vị + chuyên ngành
        String hocVi = (String) cbHocVi.getSelectedItem();
        String chuyenNganh = (String) cbChuyenNganh.getSelectedItem();
        
        // Tìm IDTD hiện có hoặc tạo mới
        int idtd = -1;
        List<Item> trinhDos = danhMucDAO.getListTrinhDo();
        String targetLabel = hocVi + (chuyenNganh != null && !chuyenNganh.isEmpty() ? " (" + chuyenNganh + ")" : "");
        for (Item it : trinhDos) {
            if (it.getName().equals(targetLabel)) {
                idtd = Integer.parseInt(it.getId());
                break;
            }
        }
        
        if (idtd == -1) {
            idtd = danhMucDAO.addTrinhDo(hocVi, chuyenNganh);
        }

        NhanVien nv = new NhanVien();
        nv.setTenNv(txtTenNV.getText());
        nv.setGioiTinh(rbNam.isSelected() ? "Nam" : "Nữ");
        nv.setEmail(txtEmail.getText());
        nv.setCccd(txtCCCD.getText());
        nv.setDienThoai(txtPhone.getText());
        nv.setIdChiNhanh(((Item)cbChiNhanh.getSelectedItem()).getId());
        nv.setIdPhongBan(((Item)cbPhongBan.getSelectedItem()).getId());
        nv.setIdChucVu(((Item)cbChucVu.getSelectedItem()).getId());
        nv.setIdTrinhDo(String.valueOf(idtd));
        nv.setNgaySinh(new java.sql.Date(((java.util.Date)spNgaySinh.getValue()).getTime()));

        if (txtSalary != null) { // Mode Add
            try {
                double luong = Double.parseDouble(txtSalary.getText().isEmpty() ? "0" : txtSalary.getText());
                int loaiHD = Integer.parseInt(((Item)cbLoaiHD.getSelectedItem()).getId());
                Date nKy = new java.sql.Date(((java.util.Date)spNgayKy.getValue()).getTime());
                Date nBD = new java.sql.Date(((java.util.Date)spNgayBD.getValue()).getTime());
                Date nKT = (loaiHD == 3) ? null : new java.sql.Date(((java.util.Date)spNgayKT.getValue()).getTime());

                if (nhanVienService.onboardingNhanVien(nv, luong, loaiHD, 4, nKy, nBD, nKT)) {
                    JOptionPane.showMessageDialog(this, "Tiếp nhận nhân viên thành công!\nTài khoản và Hợp đồng đã được tự động khởi tạo.");
                    isSuccess = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        } else {
            isSuccess = true;
            dispose();
        }
    }

    private void setComboboxSelectedItem(JComboBox<Item> cb, String id) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (cb.getItemAt(i).getId().equals(id)) {
                cb.setSelectedIndex(i);
                break;
            }
        }
    }

    public boolean isSuccess() { return isSuccess; }
}
