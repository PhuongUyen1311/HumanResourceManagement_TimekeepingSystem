package com.qlns.service;

import com.qlns.dao.NhanVienDAO;
import com.qlns.model.NhanVien;

import java.util.List;

public class NhanVienService {
    private NhanVienDAO nhanVienDAO;

    public NhanVienService() {
        nhanVienDAO = new NhanVienDAO();
    }

    public List<NhanVien> getAllNhanVien() {
        return nhanVienDAO.getAll();
    }

    public List<NhanVien> searchNhanVienByName(String name) {
        return nhanVienDAO.searchByName(name);
    }

    public boolean addNhanVien(NhanVien nv) {
        // Có thể thêm các logic validate dữ liệu ở đây (VD: Check email hợp lệ, sđt hợp lệ...)
        if (nv.getIdNv() == null || nv.getIdNv().isEmpty()) {
            return false;
        }
        return nhanVienDAO.insert(nv);
    }

    public boolean updateNhanVien(NhanVien nv) {
        if (nv.getIdNv() == null || nv.getIdNv().isEmpty()) {
            return false;
        }
        return nhanVienDAO.update(nv);
    }

    public boolean deleteNhanVien(String idNv) {
        if (idNv == null || idNv.isEmpty()) {
            return false;
        }
        return nhanVienDAO.delete(idNv);
    }

    public boolean onboardingNhanVien(NhanVien nv, double luongCoBan, int idLoaiHD, int idNhomQuyen,
                                     java.sql.Date ngayKy, java.sql.Date ngayBD, java.sql.Date ngayKT) {
        // Business validation logic could go here
        if (nv.getTenNv() == null || nv.getTenNv().isEmpty()) return false;
        if (nv.getCccd() == null || nv.getCccd().length() != 12) return false;
        
        return nhanVienDAO.onboardingNhanVien(nv, luongCoBan, idLoaiHD, idNhomQuyen, ngayKy, ngayBD, ngayKT);
    }
}
