-- ======================================================
-- DATABASE: QUẢN LÝ NHÂN SỰ (QLNS) - FINAL VERSION
-- ======================================================

USE master
GO
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'QuanLyNhanSu')
BEGIN
    ALTER DATABASE QuanLyNhanSu SET SINGLE_USER WITH ROLLBACK IMMEDIATE
    DROP DATABASE QuanLyNhanSu
END
GO
CREATE DATABASE QuanLyNhanSu
GO
USE QuanLyNhanSu
GO

-- ==============================================
-- 1. TẠO CÁC BẢNG CƠ SỞ (CORE TABLES)
-- ==============================================

-- 1. Bảng Chi nhánh
CREATE TABLE CHINHANH (
    IDCN INT IDENTITY(1,1) PRIMARY KEY,
    TENCNHANH NVARCHAR(255) NOT NULL,
    HOTLINE VARCHAR(20) CHECK (HOTLINE NOT LIKE '%[^0-9]%'),
    DIACHI NVARCHAR(255)
);

-- 2. Bảng Chức vụ
CREATE TABLE CHUCVU (
    IDCV INT IDENTITY(1,1) PRIMARY KEY,
    TENCV NVARCHAR(255) NOT NULL
);

-- 3. Bảng Trình độ
CREATE TABLE TRINHDO (
    IDTD INT IDENTITY(1,1) PRIMARY KEY,
    TENTD NVARCHAR(255) NOT NULL,
    CHUYENNGANH NVARCHAR(255)
);

-- 4. Bảng Phòng ban
CREATE TABLE PHONGBAN (
    IDPB INT IDENTITY(1,1) PRIMARY KEY,
    TENPB NVARCHAR(255) NOT NULL,
    DIACHI NVARCHAR(255),
    NGAYTHANHLAP DATE,
    IDCN INT NOT NULL,
    ID_TRUONGPHONG INT,
    FOREIGN KEY (IDCN) REFERENCES CHINHANH(IDCN)
);

-- 5. Bảng Nhân viên
CREATE TABLE NHANVIEN (
    IDNV INT IDENTITY(1,1) PRIMARY KEY,
    TENNV NVARCHAR(255) NOT NULL,
    GIOITINH NVARCHAR(10) CHECK (GIOITINH IN (N'Nam', N'Nữ', N'Khác')),
    NGAYSINH DATE CHECK (DATEDIFF(YEAR, NGAYSINH, GETDATE()) >= 18),
    CCCD CHAR(12) UNIQUE,
    EMAIL VARCHAR(255) CHECK (EMAIL LIKE '%_@__%.__%'),
    DIENTHOAI VARCHAR(15) CHECK (DIENTHOAI NOT LIKE '%[^0-9]%'),
    DIACHI NVARCHAR(255),
    DANTOC NVARCHAR(50),
    TONGIAO NVARCHAR(50),
    HONNHAN NVARCHAR(50),
    IDTD INT NOT NULL,
    IDCV INT NOT NULL,
    IDPB INT NOT NULL,
    IDCN INT NOT NULL,
    TRANGTHAI NVARCHAR(50) DEFAULT N'Đang làm việc',
    FOREIGN KEY (IDCN) REFERENCES CHINHANH(IDCN),
    FOREIGN KEY (IDCV) REFERENCES CHUCVU(IDCV),
    FOREIGN KEY (IDTD) REFERENCES TRINHDO(IDTD),
    FOREIGN KEY (IDPB) REFERENCES PHONGBAN(IDPB)
);

-- Thêm ràng buộc Trưởng phòng cho bảng Phòng ban
ALTER TABLE PHONGBAN ADD CONSTRAINT FK_PB_TRUONGPHONG FOREIGN KEY (ID_TRUONGPHONG) REFERENCES NHANVIEN(IDNV);

-- 6. Bảng Loại hợp đồng
CREATE TABLE LOAIHD (
    IDLOAI INT IDENTITY(1,1) PRIMARY KEY,
    TENLOAI NVARCHAR(255) NOT NULL,
    THOIHAN INT,
    BHYT NVARCHAR(11),
    BHXH NVARCHAR(11)
);

-- 7. Bảng Hợp đồng
CREATE TABLE HOPDONG (
    SODH INT IDENTITY(1,1) PRIMARY KEY,
    NGAYKY DATE,
    NGAYBATDAU DATE NOT NULL,
    NGAYKETTHUC DATE,
    LUONGCOBAN DECIMAL(15, 2),
    TRANGTHAI NVARCHAR(50),
    IDNV INT NOT NULL,
    IDLOAIHD INT NOT NULL,
    CONSTRAINT CK_HieuLucHopDong CHECK (NGAYKETTHUC IS NULL OR NGAYKETTHUC >= NGAYBATDAU),
    FOREIGN KEY (IDNV) REFERENCES NHANVIEN(IDNV),
    FOREIGN KEY (IDLOAIHD) REFERENCES LOAIHD(IDLOAI)
);

-- 8. Bảng Chấm công
CREATE TABLE BANGCHAMCONG (
    IDBC INT IDENTITY(1,1) PRIMARY KEY,
    IDNV INT NOT NULL,
    THANG TINYINT,
    NAM SMALLINT,
    TONGNGAYLAM INT DEFAULT 0,
    CONGCHUAN INT DEFAULT 26,
    SOGIOTANGCA DECIMAL(5,2) DEFAULT 0,
    SONGAYNGHI INT DEFAULT 0,
    SONGAYDITRE INT DEFAULT 0,
    TRANGTHAI NVARCHAR(50),
    FOREIGN KEY (IDNV) REFERENCES NHANVIEN(IDNV)
);

-- 9. Bảng Loại nghỉ phép
CREATE TABLE LOAINGHIPHEP (
    IDLNP INT IDENTITY(1,1) PRIMARY KEY,
    TENLOAI NVARCHAR(255) NOT NULL,
    HUONGLUONG BIT DEFAULT 0 -- 1 là có lương, 0 là không lương
);

-- 10. Bảng Chi tiết bảng công
CREATE TABLE CHITIET_BANGCONG (
    IDCT INT IDENTITY(1,1) PRIMARY KEY,
    IDBC INT NOT NULL,
    NGAYLAM DATE,
    GIOVAO TIME,
    GIORA TIME,
    SOGIOTANGCA DECIMAL(5,2) DEFAULT 0,
    SOPHUTDITRE INT DEFAULT 0,
    IDLNP INT,
    FOREIGN KEY (IDBC) REFERENCES BANGCHAMCONG(IDBC),
    FOREIGN KEY (IDLNP) REFERENCES LOAINGHIPHEP(IDLNP)
);

-- 11. Bảng Phụ cấp
CREATE TABLE PHUCAP (
    IDPC INT IDENTITY(1,1) PRIMARY KEY,
    TENPHUCAP NVARCHAR(255) NOT NULL,
);

-- 12. Bảng Nhân viên - Phụ cấp
CREATE TABLE NHANVIEN_PHUCAP (
    IDNV INT,
    IDPC INT,
    SOTIEN DECIMAL(15,2),
    NGAYAPDUNG DATE,
    PRIMARY KEY (IDNV, IDPC),
    FOREIGN KEY (IDNV) REFERENCES NHANVIEN(IDNV),
    FOREIGN KEY (IDPC) REFERENCES PHUCAP(IDPC)
);

-- 13. Bảng Khoản trừ
CREATE TABLE KHOANTRU (
    IDKT INT IDENTITY(1,1) PRIMARY KEY,
    TENKHOANTRU NVARCHAR(255) NOT NULL,
);

-- 14. Bảng Nhân viên - Khoản trừ
CREATE TABLE NHANVIEN_KHOANTRU (
    IDNV INT,
    IDKT INT,
    SOTIEN DECIMAL(15,2),
    NGAYAPDUNG DATE,
    PRIMARY KEY (IDNV, IDKT),
    FOREIGN KEY (IDNV) REFERENCES NHANVIEN(IDNV),
    FOREIGN KEY (IDKT) REFERENCES KHOANTRU(IDKT)
);

-- 15. Bảng Tham số hệ thống (Nâng cấp)
CREATE TABLE THAMSO_HETHONG (
    MA_TS VARCHAR(50) PRIMARY KEY,
    TEN_TS NVARCHAR(255),
    GIA_TRI DECIMAL(15,2)
);

-- 16. Bảng Log biến động lương (Nâng cấp)
CREATE TABLE LOG_LUONG (
    IDLOG INT IDENTITY(1,1) PRIMARY KEY,
    IDNV INT,
    LUONG_CU DECIMAL(15,2),
    LUONG_MOI DECIMAL(15,2),
    NGAY_THAY_DOI DATETIME DEFAULT GETDATE(),
    GHI_CHU NVARCHAR(255),
    FOREIGN KEY (IDNV) REFERENCES NHANVIEN(IDNV)
);

-- 17. Bảng Nhóm quyền
CREATE TABLE NHOMQUYEN (
    IDNQ INT IDENTITY(1,1) PRIMARY KEY,
    TENNHOMQUYEN VARCHAR(50),
    MOTA NVARCHAR(255)
);

-- 18. Bảng Quyền
CREATE TABLE QUYEN (
    IDQUYEN INT IDENTITY(1,1) PRIMARY KEY,
    MAQUYEN VARCHAR(50) UNIQUE NOT NULL,
    TENQUYEN NVARCHAR(255) NOT NULL
);

-- 19. Bảng Phân quyền
CREATE TABLE PHANQUYEN (
    IDNQ INT,
    IDQUYEN INT,
    PRIMARY KEY (IDNQ, IDQUYEN),
    FOREIGN KEY (IDNQ) REFERENCES NHOMQUYEN(IDNQ),
    FOREIGN KEY (IDQUYEN) REFERENCES QUYEN(IDQUYEN)
);

-- 20. Bảng Tài khoản
CREATE TABLE TAIKHOAN (
    IDTK INT IDENTITY(1,1) PRIMARY KEY,
    TENTK VARCHAR(50) UNIQUE,
    PASSWORD VARCHAR(255) NOT NULL,
    TRANGTHAI BIT DEFAULT 1,
    IDNQ INT,
    IDNV INT UNIQUE,
    FOREIGN KEY (IDNV) REFERENCES NHANVIEN(IDNV),
    FOREIGN KEY (IDNQ) REFERENCES NHOMQUYEN(IDNQ)
);

-- 21. Bảng Lương (Tính toán tự động)
CREATE TABLE BANGLUONG (
    IDBL INT IDENTITY(1,1) PRIMARY KEY,
    IDBC INT NOT NULL,
    LUONGCOBAN DECIMAL(15,2),
    TIEN_TANGCA DECIMAL(15,2) DEFAULT 0,
    TONG_PHUCAP DECIMAL(15,2) DEFAULT 0,
    TONG_KHOANTRU DECIMAL(15,2) DEFAULT 0,
    LUONG_THUCTE DECIMAL(15,2) DEFAULT 0,
    THUC_NHAN DECIMAL(15,2) DEFAULT 0,
    FOREIGN KEY (IDBC) REFERENCES BANGCHAMCONG(IDBC)
);
GO

-- ==============================================
-- 2. INSERT DỮ LIỆU MẪU (INITIAL DATA)
-- ==============================================

-- Tham số hệ thống
INSERT INTO THAMSO_HETHONG (MA_TS, TEN_TS, GIA_TRI) VALUES 
('HESO_TANGCA', N'Hệ số lương tăng ca', 1.5),
('GIO_BATDAU', N'Giờ bắt đầu làm việc (8h)', 8);

-- Chi nhánh
INSERT INTO CHINHANH (TENCNHANH, HOTLINE, DIACHI) VALUES 
(N'Chi nhánh Miền Bắc', '0243555666', N'Số 1 Đào Duy Anh, Đống Đa, Hà Nội'),
(N'Chi nhánh Miền Trung', '0236444555', N'150 Duy Tân, Hải Châu, Đà Nẵng'),
(N'Chi nhánh Miền Nam', '0282223334', N'Tầng 10, Bitexco Financial Tower, Quận 1, TP. HCM');

-- Chức vụ
INSERT INTO CHUCVU (TENCV) VALUES 
(N'Tổng Giám đốc'), (N'Giám đốc Tài chính'), (N'Giám đốc Nhân sự'), 
(N'Trưởng phòng'), (N'Phó phòng'), (N'Chuyên viên cao cấp'), 
(N'Chuyên viên'), (N'Nhân viên kỹ thuật'), (N'Nhân viên kinh doanh'), 
(N'Kế toán viên'), (N'Lập trình viên'), (N'Nhân viên hành chính');

-- Trình độ
INSERT INTO TRINHDO (TENTD, CHUYENNGANH) VALUES 
(N'Tiến sĩ', N'Quản trị Kinh doanh'), (N'Thạc sĩ', N'Khoa học Máy tính'), 
(N'Thạc sĩ', N'Tài chính Ngân hàng'), (N'Đại học', N'Công nghệ thông tin'), 
(N'Đại học', N'Kế toán doanh nghiệp'), (N'Đại học', N'Marketing'), 
(N'Đại học', N'Kỹ thuật Điện'), (N'Đại học', N'Ngôn ngữ Anh'), 
(N'Cao đẳng', N'Quản trị văn phòng'), (N'Cao đẳng', N'Thiết kế đồ họa'), 
(N'Trung cấp', N'Điện dân dụng'), (N'Chứng chỉ', N'Nghiệp vụ nhân sự');

-- Phòng ban
INSERT INTO PHONGBAN (TENPB, DIACHI, NGAYTHANHLAP, IDCN) VALUES 
(N'Hội đồng quản trị', N'Tầng 25 - Hà Nội', '2010-01-01', 1),
(N'Phòng Tài chính', N'Tầng 20 - Hà Nội', '2010-05-15', 1), 
(N'Phòng Nhân sự', N'Tầng 19 - Hà Nội', '2010-06-20', 1), 
(N'Phòng Công nghệ (IT)', N'Tầng 18 - Hà Nội', '2011-01-10', 1), 
(N'Phòng Kinh doanh', N'Tầng 15 - HCM', '2012-03-05', 3), 
(N'Phòng Marketing', N'Tầng 14 - HCM', '2013-08-20', 3), 
(N'Phòng Kỹ thuật', N'Tầng 2 - Đà Nẵng', '2014-04-18', 2), 
(N'Phòng Chăm sóc khách hàng', N'Tầng 3 - Đà Nẵng', '2015-11-12', 2), 
(N'Phòng Truyền thông', N'Tầng 17 - Hà Nội', '2018-09-30', 1), 
(N'Phòng Logistics', N'Kho 01 - HCM', '2019-12-01', 3), 
(N'Phòng Pháp chế', N'Tầng 21 - Hà Nội', '2010-05-15', 1);

-- Loại nghỉ phép
INSERT INTO LOAINGHIPHEP (TENLOAI, HUONGLUONG) VALUES
(N'Nghỉ ốm (Có giấy tờ)', 1), (N'Nghỉ phép năm', 1), 
(N'Nghỉ thai sản', 1), (N'Nghỉ không lương', 0), (N'Nghỉ lễ/tết', 1);

-- Phụ cấp
INSERT INTO PHUCAP (TENPHUCAP) VALUES 
(N'Phụ cấp Xăng xe'), (N'Phụ cấp Ăn trưa'), 
(N'Phụ cấp Điện thoại'), (N'Phụ cấp Thâm niên'),
(N'Phụ cấp Trách nhiệm'), (N'Phụ cấp Độc hại');

-- Khoản trừ
INSERT INTO KHOANTRU (TENKHOANTRU) VALUES 
(N'Bảo hiểm xã hội (8%)'), (N'Bảo hiểm y tế (1.5%)'), 
(N'Bảo hiểm thất nghiệp (1%)'), (N'Công đoàn phí'),
(N'Phạt vi phạm nội quy'), (N'Tạm ứng lương');

-- Nhóm quyền
INSERT INTO NHOMQUYEN (TENNHOMQUYEN, MOTA) VALUES 
('System Admin', N'Quản trị tối cao hệ thống'), 
('HR Manager', N'Quản lý nhân sự và hợp đồng'), 
('Accountant', N'Kế toán lương và chấm công'), 
('Employee', N'Nhân viên xem thông tin cá nhân'),
('Branch Manager', N'Giám đốc quản lý toàn bộ nhân sự tại 1 chi nhánh');

-- Quyền
INSERT INTO QUYEN (MAQUYEN, TENQUYEN) VALUES 
('EMP_VIEW', N'Xem danh sách nhân viên'), ('EMP_EDIT', N'Sửa thông tin nhân viên'), ('EMP_ADD', N'Thêm nhân viên mới'), ('EMP_DELETE', N'Xóa nhân viên'),
('SAL_VIEW', N'Xem bảng lương'), ('SAL_CHOT', N'Chốt bảng lương tháng'), ('SAL_EDIT', N'Sửa bảng lương'), ('SAL_ADD', N'Thêm bảng lương'), ('SAL_DELETE', N'Xóa bảng lương'),
('TIME_VIEW', N'Xem dữ liệu chấm công'), ('TIME_EDIT', N'Chỉnh sửa giờ công'), ('TIME_ADD', N'Thêm dữ liệu chấm công'), ('TIME_DELETE', N'Xóa dữ liệu chấm công'),
('PC_VIEW', N'Xem phụ cấp'), ('PC_ADD', N'Thêm phụ cấp'), ('PC_EDIT', N'Sửa phụ cấp'), ('PC_DELETE', N'Xóa phụ cấp'),
('KT_VIEW', N'Xem khoản trừ'), ('KT_ADD', N'Thêm khoản trừ'), ('KT_EDIT', N'Sửa khoản trừ'), ('KT_DELETE', N'Xóa khoản trừ'),
('HD_VIEW', N'Xem hợp đồng'), ('HD_ADD', N'Thêm hợp đồng'), ('HD_EDIT', N'Sửa hợp đồng'), ('HD_DELETE', N'Xóa hợp đồng'),
('TK_VIEW', N'Xem tài khoản'), ('TK_ADD', N'Thêm tài khoản'), ('TK_EDIT', N'Sửa tài khoản'), ('TK_DELETE', N'Xóa tài khoản');

-- Phân quyền
INSERT INTO PHANQUYEN (IDNQ, IDQUYEN) SELECT 1, IDQUYEN FROM QUYEN;
INSERT INTO PHANQUYEN (IDNQ, IDQUYEN) SELECT 2, IDQUYEN FROM QUYEN WHERE MAQUYEN LIKE 'EMP_%' OR MAQUYEN LIKE 'HD_%' OR MAQUYEN LIKE 'PC_%' OR MAQUYEN LIKE 'KT_%';
INSERT INTO PHANQUYEN (IDNQ, IDQUYEN) SELECT 3, IDQUYEN FROM QUYEN WHERE MAQUYEN LIKE 'SAL_%' OR MAQUYEN LIKE 'TIME_%' OR MAQUYEN IN ('PC_VIEW', 'KT_VIEW');
INSERT INTO PHANQUYEN (IDNQ, IDQUYEN) SELECT 5, IDQUYEN FROM QUYEN;

-- Loại hợp đồng
INSERT INTO LOAIHD (TENLOAI, THOIHAN, BHYT, BHXH) VALUES 
(N'Vô thời hạn', NULL, N'Có', N'Có'), 
(N'Xác định thời hạn 3 năm', 36, N'Có', N'Có'),
(N'Xác định thời hạn 1 năm', 12, N'Có', N'Có'), 
(N'Thử việc 2 tháng', 2, N'Không', N'Không');

-- 20 Nhân viên mẫu (INSERT trực tiếp để phân tán nhanh)
INSERT INTO NHANVIEN (TENNV, GIOITINH, NGAYSINH, CCCD, EMAIL, DIENTHOAI, IDTD, IDCV, IDPB, IDCN, TRANGTHAI) VALUES
(N'Phạm Minh Chính', N'Nam', '1975-02-12', '001075000001', 'chinh.pm@qlns.vn', '0912345678', 1, 1, 1, 1, N'Đang làm việc'),
(N'Nguyễn Thị Kim Ngân', N'Nữ', '1980-05-20', '001080000002', 'ngan.ntk@qlns.vn', '0988776655', 3, 3, 3, 1, N'Đang làm việc'),
(N'Trần Quốc Vượng', N'Nam', '1982-11-10', '001082000003', 'vuong.tq@qlns.vn', '0901234567', 5, 4, 2, 1, N'Đang làm việc'),
(N'Võ Văn Thưởng', N'Nam', '1988-06-15', '001088000004', 'thuong.vv@qlns.vn', '0944556677', 4, 4, 4, 3, N'Đang làm việc'),
(N'Trương Thị Mai', N'Nữ', '1985-03-22', '001085000005', 'mai.tt@qlns.vn', '0977889900', 8, 4, 8, 2, N'Đang làm việc'),
(N'Phan Văn Giang', N'Nam', '1983-09-30', '001083000006', 'giang.pv@qlns.vn', '0911223344', 2, 4, 11, 1, N'Đang làm việc'),
(N'Nguyễn Xuân Thắng', N'Nam', '1986-12-05', '001086000007', 'thang.nx@qlns.vn', '0922334455', 1, 6, 5, 3, N'Đang làm việc'),
(N'Đinh Tiến Dũng', N'Nam', '1989-08-28', '001089000008', 'dung.dt@qlns.vn', '0933445566', 5, 7, 2, 1, N'Đang làm việc'),
(N'Trần Thanh Mẫn', N'Nam', '1990-01-15', '001090000009', 'man.tt@qlns.vn', '0944332211', 6, 9, 6, 3, N'Đang làm việc'),
(N'Trần Tuấn Anh', N'Nam', '1987-04-18', '001087000010', 'anh.tt@qlns.vn', '0955667788', 4, 8, 7, 2, N'Đang làm việc'),
(N'Nguyễn Văn Thắng', N'Nam', '1992-10-10', '001092000011', 'thang.nv@qlns.vn', '0966778899', 7, 11, 4, 1, N'Đang làm việc'),
(N'Lê Minh Khái', N'Nam', '1984-07-07', '001084000012', 'khai.lm@qlns.vn', '0977665544', 5, 10, 2, 1, N'Đang làm việc'),
(N'Nguyễn Trọng Nghĩa', N'Nam', '1995-05-05', '001095000013', 'nghia.nt@qlns.vn', '0988554433', 8, 12, 9, 1, N'Đang làm việc'),
(N'Đỗ Văn Chiến', N'Nam', '1993-02-02', '001093000014', 'chien.dv@qlns.vn', '0999887766', 11, 8, 10, 3, N'Đang làm việc'),
(N'Lê Minh Hoan', N'Nam', '1988-03-03', '001088000015', 'hoan.lm@qlns.vn', '0900112233', 2, 7, 5, 3, N'Đang làm việc'),
(N'Đào Ngọc Dung', N'Nam', '1991-01-01', '001091000016', 'dung.dn@qlns.vn', '0911002233', 3, 4, 3, 1, N'Đang làm việc'),
(N'Hồ Đức Phớc', N'Nam', '1989-11-11', '001089000017', 'phoc.hd@qlns.vn', '0922003344', 5, 2, 2, 1, N'Đang làm việc'),
(N'Nguyễn Kim Sơn', N'Nam', '1994-06-06', '001094000018', 'son.nk@qlns.vn', '0933004455', 1, 7, 9, 1, N'Đang làm việc'),
(N'Nguyễn Hồng Diên', N'Nam', '1985-08-08', '001085000019', 'dien.nh@qlns.vn', '0944005566', 4, 5, 6, 3, N'Đang làm việc'),
(N'Đặng Quốc Khánh', N'Nam', '1996-09-09', '001096000020', 'khanh.dq@qlns.vn', '0955006677', 10, 11, 4, 1, N'Đang làm việc');

-- Hợp đồng
INSERT INTO HOPDONG (NGAYKY, NGAYBATDAU, LUONGCOBAN, TRANGTHAI, IDNV, IDLOAIHD) VALUES 
('2025-01-01', '2025-01-01', 150000000, N'Đang hiệu lực', 1, 1),
('2025-01-01', '2025-01-01', 80000000, N'Đang hiệu lực', 2, 1),
('2025-02-01', '2025-02-01', 60000000, N'Đang hiệu lực', 3, 1),
('2025-03-01', '2025-03-01', 45000000, N'Đang hiệu lực', 4, 2),
('2025-03-01', '2025-03-01', 35000000, N'Đang hiệu lực', 5, 2),
('2025-04-01', '2025-04-01', 30000000, N'Đang hiệu lực', 6, 2),
('2025-05-01', '2025-05-01', 50000000, N'Đang hiệu lực', 7, 1),
('2025-06-01', '2025-06-01', 25000000, N'Đang hiệu lực', 8, 3),
('2025-06-01', '2025-06-01', 20000000, N'Đang hiệu lực', 9, 3),
('2025-07-01', '2025-07-01', 28000000, N'Đang hiệu lực', 10, 2),
('2025-08-01', '2025-08-01', 22000000, N'Đang hiệu lực', 11, 3),
('2025-08-01', '2025-08-01', 32000000, N'Đang hiệu lực', 12, 2),
('2025-09-01', '2025-09-01', 15000000, N'Đang hiệu lực', 13, 3),
('2026-01-01', '2026-01-01', 24000000, N'Đang hiệu lực', 14, 3),
('2026-01-01', '2026-01-01', 26000000, N'Đang hiệu lực', 15, 2),
('2026-02-01', '2026-02-01', 38000000, N'Đang hiệu lực', 16, 2),
('2026-02-01', '2026-02-01', 42000000, N'Đang hiệu lực', 17, 1),
('2026-03-01', '2026-03-01', 21000000, N'Đang hiệu lực', 18, 3),
('2026-03-01', '2026-03-01', 33000000, N'Đang hiệu lực', 19, 2),
('2026-04-01', '2026-04-01', 18000000, N'Thử việc', 20, 4);

-- Tài khoản
INSERT INTO TAIKHOAN (TENTK, PASSWORD, IDNQ, IDNV) VALUES 
('chinh.pm', '123456', 1, 1), ('ngan.ntk', '123456', 2, 2), ('vuong.tq', '123456', 3, 3), ('thuong.vv', '123456', 4, 4), ('mai.tt', '123456', 4, 5),
('giang.pv', '123456', 4, 6), ('thang.nx', '123456', 4, 7), ('dung.dt', '123456', 4, 8), ('man.tt', '123456', 4, 9), ('anh.tt', '123456', 4, 10),
('thang.nv', '123456', 4, 11), ('khai.lm', '123456', 4, 12), ('nghia.nt', '123456', 4, 13), ('chien.dv', '123456', 4, 14), ('hoan.lm', '123456', 4, 15),
('dung.dn', '123456', 4, 16), ('phoc.hd', '123456', 4, 17), ('son.nk', '123456', 4, 18), ('dien.nh', '123456', 4, 19), ('khanh.dq', '123456', 4, 20);

-- Cập nhật Trưởng phòng
UPDATE PHONGBAN SET ID_TRUONGPHONG = 1 WHERE IDPB = 1;
UPDATE PHONGBAN SET ID_TRUONGPHONG = 2 WHERE IDPB = 3;
UPDATE PHONGBAN SET ID_TRUONGPHONG = 3 WHERE IDPB = 2;
UPDATE PHONGBAN SET ID_TRUONGPHONG = 4 WHERE IDPB = 4;
UPDATE PHONGBAN SET ID_TRUONGPHONG = 7 WHERE IDPB = 5;

-- Gán Phụ cấp & Khoản trừ
INSERT INTO NHANVIEN_PHUCAP (IDNV, IDPC, SOTIEN, NGAYAPDUNG) 
SELECT IDNV, 2, 770000, '2026-04-01' FROM NHANVIEN WHERE IDCV >= 4;

INSERT INTO NHANVIEN_PHUCAP (IDNV, IDPC, SOTIEN, NGAYAPDUNG) VALUES
(1, 5, 10000000, '2025-01-01'), (4, 1, 500000, '2025-03-01'), (4, 3, 300000, '2025-03-01'), (7, 4, 2000000, '2025-05-01');

INSERT INTO NHANVIEN_KHOANTRU (IDNV, IDKT, SOTIEN, NGAYAPDUNG) 
SELECT IDNV, 4, 50000, '2026-04-01' FROM NHANVIEN;

INSERT INTO NHANVIEN_KHOANTRU (IDNV, IDKT, SOTIEN, NGAYAPDUNG) VALUES (11, 5, 200000, '2026-04-02');

-- 1. Khởi tạo Bảng Chấm Công rỗng cho tháng 4/2026
INSERT INTO BANGCHAMCONG (IDNV, THANG, NAM, CONGCHUAN, TRANGTHAI)
SELECT IDNV, 4, 2026, 22, N'Đã chốt' FROM NHANVIEN;

-- 2. Nạp Chi tiết chấm công (Dữ liệu gốc)
-- Nhân viên 11 (Nguyễn Văn Thắng) - 20 ngày đi làm, 1 ngày tăng ca, 1 ngày đi trễ
INSERT INTO CHITIET_BANGCONG (IDBC, NGAYLAM, GIOVAO, GIORA, SOGIOTANGCA, SOPHUTDITRE)
SELECT IDBC, DATEFROMPARTS(2026, 4, n), '08:00', '17:00', 0, 0
FROM BANGCHAMCONG bc
CROSS JOIN (SELECT TOP 20 ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) as n FROM sys.objects) nums
WHERE bc.IDNV = 11 AND bc.THANG = 4;

-- Cập nhật trường hợp đặc biệt cho ID 11
UPDATE CHITIET_BANGCONG SET SOPHUTDITRE = 30 WHERE IDBC = (SELECT IDBC FROM BANGCHAMCONG WHERE IDNV = 11 AND THANG = 4) AND DAY(NGAYLAM) = 5;
UPDATE CHITIET_BANGCONG SET SOGIOTANGCA = 2 WHERE IDBC = (SELECT IDBC FROM BANGCHAMCONG WHERE IDNV = 11 AND THANG = 4) AND DAY(NGAYLAM) = 10;

-- Nhân viên 12 (Lê Minh Khái) - Có 1 ngày nghỉ phép có lương (IDLNP = 2: Nghỉ phép năm)
INSERT INTO CHITIET_BANGCONG (IDBC, NGAYLAM, GIOVAO, GIORA, IDLNP)
SELECT IDBC, '2026-04-15', NULL, NULL, 2 FROM BANGCHAMCONG WHERE IDNV = 12 AND THANG = 4;

-- Thêm công mặc định cho các NV khác (mỗi người 1 ngày mẫu để test)
INSERT INTO CHITIET_BANGCONG (IDBC, NGAYLAM, GIOVAO, GIORA)
SELECT IDBC, '2026-04-01', '08:00', '17:00' FROM BANGCHAMCONG WHERE THANG = 4 AND IDNV NOT IN (11, 12);

-- 3. Cập nhật Bảng Chấm Công dựa trên Chi tiết (Sử dụng logic GIOVAO và HUONGLUONG)
UPDATE B
SET 
    TONGNGAYLAM = (
        SELECT COUNT(*) FROM CHITIET_BANGCONG C 
        LEFT JOIN LOAINGHIPHEP LNP ON C.IDLNP = LNP.IDLNP
        WHERE C.IDBC = B.IDBC 
        AND (C.GIOVAO IS NOT NULL OR (C.IDLNP IS NOT NULL AND ISNULL(LNP.HUONGLUONG, 0) = 1))
    ),
    SONGAYNGHI = (SELECT COUNT(*) FROM CHITIET_BANGCONG C WHERE C.IDBC = B.IDBC AND C.IDLNP IS NOT NULL),
    SONGAYDITRE = (SELECT COUNT(*) FROM CHITIET_BANGCONG C WHERE C.IDBC = B.IDBC AND C.SOPHUTDITRE > 0),
    SOGIOTANGCA = (SELECT ISNULL(SUM(SOGIOTANGCA), 0) FROM CHITIET_BANGCONG C WHERE C.IDBC = B.IDBC)
FROM BANGCHAMCONG B WHERE THANG = 4 AND NAM = 2026;

-- 4. Chốt Bảng Lương (Tính toán logic vào cột vật lý)
INSERT INTO BANGLUONG (IDBC, LUONGCOBAN, TIEN_TANGCA, TONG_PHUCAP, TONG_KHOANTRU, LUONG_THUCTE, THUC_NHAN)
SELECT 
    bc.IDBC, 
    hd.LUONGCOBAN,
    CAST((hd.LUONGCOBAN / bc.CONGCHUAN / 8) * bc.SOGIOTANGCA * 1.5 AS DECIMAL(15,2)),
    ISNULL((SELECT SUM(SOTIEN) FROM NHANVIEN_PHUCAP pc WHERE pc.IDNV = bc.IDNV), 0),
    ISNULL((SELECT SUM(SOTIEN) FROM NHANVIEN_KHOANTRU kt WHERE kt.IDNV = bc.IDNV), 0),
    CAST((hd.LUONGCOBAN / bc.CONGCHUAN) * bc.TONGNGAYLAM + ((hd.LUONGCOBAN / bc.CONGCHUAN / 8) * bc.SOGIOTANGCA * 1.5) AS DECIMAL(15,2)),
    CAST(((hd.LUONGCOBAN / bc.CONGCHUAN) * bc.TONGNGAYLAM + ((hd.LUONGCOBAN / bc.CONGCHUAN / 8) * bc.SOGIOTANGCA * 1.5)) 
         + ISNULL((SELECT SUM(SOTIEN) FROM NHANVIEN_PHUCAP pc WHERE pc.IDNV = bc.IDNV), 0) 
         - ISNULL((SELECT SUM(SOTIEN) FROM NHANVIEN_KHOANTRU kt WHERE kt.IDNV = bc.IDNV), 0) AS DECIMAL(15,2))
FROM BANGCHAMCONG bc
JOIN HOPDONG hd ON bc.IDNV = hd.IDNV AND hd.TRANGTHAI = N'Đang hiệu lực'
WHERE bc.THANG = 4 AND bc.NAM = 2026;

-- 5. Khởi tạo tháng 5 (Đang chấm)
INSERT INTO BANGCHAMCONG (IDNV, THANG, NAM, CONGCHUAN, TRANGTHAI)
SELECT IDNV, 5, 2026, 22, N'Đang chấm' FROM NHANVIEN;

INSERT INTO CHITIET_BANGCONG (IDBC, NGAYLAM, GIOVAO, GIORA, SOPHUTDITRE) VALUES 
((SELECT IDBC FROM BANGCHAMCONG WHERE IDNV = 11 AND THANG = 5), '2026-05-01', '07:59', '17:10', 0), 
((SELECT IDBC FROM BANGCHAMCONG WHERE IDNV = 11 AND THANG = 5), '2026-05-04', '08:10', '18:00', 10), 
((SELECT IDBC FROM BANGCHAMCONG WHERE IDNV = 11 AND THANG = 5), '2026-05-05', '08:00', '17:00', 0);

GO
PRINT N'Dữ liệu hệ thống QLNS đã được khởi tạo thành công!'
GO


-- ======================================================
-- SCRIPT: QLNS_LOGIC.SQL
-- MỤC ĐÍCH: Bổ sung Logic (Function, Procedure, Trigger) sau khi phân tán
-- ======================================================

USE QuanLyNhanSu
GO

-- 1. CÁC FUNCTION TÍNH TOÁN
-- ==============================================

-- Function tính lương thực tế
IF OBJECT_ID('fn_TinhLuongThucTe', 'FN') IS NOT NULL DROP FUNCTION fn_TinhLuongThucTe;
GO
CREATE FUNCTION fn_TinhLuongThucTe(@IDBC INT, @LuongCoBan DECIMAL(15,2))
RETURNS DECIMAL(15,2)
AS
BEGIN
    DECLARE @LuongThucTe DECIMAL(15,2);
    DECLARE @HeSoTangCa DECIMAL(15,2);
    
    SELECT @HeSoTangCa = GIA_TRI FROM THAMSO_HETHONG WHERE MA_TS = 'HESO_TANGCA';
    SET @HeSoTangCa = ISNULL(@HeSoTangCa, 1.5);
    
    SELECT @LuongThucTe = 
        (@LuongCoBan / NULLIF(CONGCHUAN, 0)) * TONGNGAYLAM + 
        ((@LuongCoBan / NULLIF(CONGCHUAN, 0) / 8) * SOGIOTANGCA * @HeSoTangCa)
    FROM BANGCHAMCONG 
    WHERE IDBC = @IDBC;
    
    RETURN ISNULL(@LuongThucTe, 0);
END;
GO

-- Function tính thực nhận
IF OBJECT_ID('fn_TinhThucNhan', 'FN') IS NOT NULL DROP FUNCTION fn_TinhThucNhan;
GO
CREATE FUNCTION fn_TinhThucNhan(@IDBC INT, @LuongCoBan DECIMAL(15,2))
RETURNS DECIMAL(15,2)
AS
BEGIN
    DECLARE @IDNV INT;
    DECLARE @LuongThucTe DECIMAL(15,2);
    DECLARE @TongPhuCap DECIMAL(15,2) = 0;
    DECLARE @TongKhoanTru DECIMAL(15,2) = 0;
    DECLARE @ThucNhan DECIMAL(15,2);

    SELECT @IDNV = IDNV FROM BANGCHAMCONG WHERE IDBC = @IDBC;
    SET @LuongThucTe = dbo.fn_TinhLuongThucTe(@IDBC, @LuongCoBan);

    SELECT @TongPhuCap = ISNULL(SUM(SOTIEN), 0) FROM NHANVIEN_PHUCAP WHERE IDNV = @IDNV;
    SELECT @TongKhoanTru = ISNULL(SUM(SOTIEN), 0) FROM NHANVIEN_KHOANTRU WHERE IDNV = @IDNV;
    
    SET @ThucNhan = @LuongThucTe + @TongPhuCap - @TongKhoanTru;
    RETURN @ThucNhan;
END;
GO

-- 2. CẬP NHẬT LOGIC BẢNG LƯƠNG
-- ==============================================
-- (Bỏ các cột computed cũ, sử dụng các cột vật lý đã thêm ở QLNS_database.sql)
GO


-- Procedure Tính Lương Tháng
IF OBJECT_ID('sp_TinhLuongThang', 'P') IS NOT NULL DROP PROCEDURE sp_TinhLuongThang;
GO
CREATE PROCEDURE sp_TinhLuongThang
    @Thang INT,
    @Nam INT
AS
BEGIN
    SET NOCOUNT ON;
    
    -- 1. Cập nhật các khoản phụ cấp và khoản trừ mới nhất vào bảng lương
    UPDATE BL
    SET 
        TONG_PHUCAP = ISNULL((SELECT SUM(SOTIEN) FROM NHANVIEN_PHUCAP NPC WHERE NPC.IDNV = BC.IDNV), 0),
        TONG_KHOANTRU = ISNULL((SELECT SUM(SOTIEN) FROM NHANVIEN_KHOANTRU NKT WHERE NKT.IDNV = BC.IDNV), 0)
    FROM BANGLUONG BL
    JOIN BANGCHAMCONG BC ON BL.IDBC = BC.IDBC
    WHERE BC.THANG = @Thang AND BC.NAM = @Nam;

    -- 2. Tính tiền tăng ca
    DECLARE @HeSoTangCa DECIMAL(5,2);
    SELECT @HeSoTangCa = GIA_TRI FROM THAMSO_HETHONG WHERE MA_TS = 'HESO_TANGCA';
    SET @HeSoTangCa = ISNULL(@HeSoTangCa, 1.5);

    UPDATE BL
    SET 
        TIEN_TANGCA = (LUONGCOBAN / NULLIF(BC.CONGCHUAN, 0) / 8) * BC.SOGIOTANGCA * @HeSoTangCa
    FROM BANGLUONG BL
    JOIN BANGCHAMCONG BC ON BL.IDBC = BC.IDBC
    WHERE BC.THANG = @Thang AND BC.NAM = @Nam;

    -- 3. Tính lương thực tế và thực nhận
    UPDATE BL
    SET 
        LUONG_THUCTE = (LUONGCOBAN / NULLIF(BC.CONGCHUAN, 0)) * BC.TONGNGAYLAM + TIEN_TANGCA,
        THUC_NHAN = ((LUONGCOBAN / NULLIF(BC.CONGCHUAN, 0)) * BC.TONGNGAYLAM + TIEN_TANGCA) + TONG_PHUCAP - TONG_KHOANTRU
    FROM BANGLUONG BL
    JOIN BANGCHAMCONG BC ON BL.IDBC = BC.IDBC
    WHERE BC.THANG = @Thang AND BC.NAM = @Nam;

    PRINT N'Đã tính xong lương cho tháng ' + CAST(@Thang AS VARCHAR) + '/' + CAST(@Nam AS VARCHAR);
END;
GO

-- Procedure Onboarding
IF OBJECT_ID('sp_OnboardingNhanVien', 'P') IS NOT NULL DROP PROCEDURE sp_OnboardingNhanVien;
GO
CREATE PROCEDURE sp_OnboardingNhanVien
    @TenNV NVARCHAR(255), @GioiTinh NVARCHAR(10), @NgaySinh DATE, @CCCD CHAR(12),
    @Email VARCHAR(255), @DienThoai VARCHAR(15), @IDTD INT, @IDCV INT, @IDPB INT, @IDCN INT,
    @LuongCoBan DECIMAL(15,2), @IDLoaiHD INT, @NhomQuyen INT,
    @NgayKy DATE, @NgayBD DATE, @NgayKT DATE
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;
        DECLARE @NewEmpID INT;
        INSERT INTO NHANVIEN (TENNV, GIOITINH, NGAYSINH, CCCD, EMAIL, DIENTHOAI, IDTD, IDCV, IDPB, IDCN, TRANGTHAI)
        VALUES (@TenNV, @GioiTinh, @NgaySinh, @CCCD, @Email, @DienThoai, @IDTD, @IDCV, @IDPB, @IDCN, N'Đang làm việc');
        SET @NewEmpID = SCOPE_IDENTITY();

        INSERT INTO HOPDONG (NGAYKY, NGAYBATDAU, NGAYKETTHUC, LUONGCOBAN, TRANGTHAI, IDNV, IDLOAIHD)
        VALUES (@NgayKy, @NgayBD, @NgayKT, @LuongCoBan, N'Đang hiệu lực', @NewEmpID, @IDLoaiHD);

        DECLARE @Username VARCHAR(50);
        IF @Email IS NOT NULL AND CHARINDEX('@', @Email) > 1
            SET @Username = LEFT(@Email, CHARINDEX('@', @Email) - 1);
        ELSE
            SET @Username = @CCCD;

        IF EXISTS (SELECT 1 FROM TAIKHOAN WHERE TENTK = @Username)
            SET @Username = @Username + CAST(@NewEmpID AS VARCHAR(10));

        INSERT INTO TAIKHOAN (TENTK, PASSWORD, IDNQ, IDNV)
        VALUES (@Username, '123456', @NhomQuyen, @NewEmpID);

        COMMIT TRANSACTION;
        SELECT @NewEmpID AS NewID, @Username AS CreatedUser;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END;
GO



-- 3. CÁC PROCEDURE QUẢN LÝ NHÂN SỰ & HỢP ĐỒNG
-- ==============================================

-- Procedure Sửa thông tin nhân viên
IF OBJECT_ID('sp_SuaThongTinNhanVien', 'P') IS NOT NULL DROP PROCEDURE sp_SuaThongTinNhanVien;
GO
CREATE PROCEDURE sp_SuaThongTinNhanVien
    @IDNV INT, @TenNV NVARCHAR(255), @GioiTinh NVARCHAR(10), @NgaySinh DATE, @CCCD CHAR(12),
    @Email VARCHAR(255), @DienThoai VARCHAR(15), @DiaChi NVARCHAR(255), @DanToc NVARCHAR(50),
    @TonGiao NVARCHAR(50), @HonNhan NVARCHAR(50), @IDTD INT, @IDCV INT, @IDPB INT, @IDCN INT
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE NHANVIEN 
    SET TENNV = @TenNV, GIOITINH = @GioiTinh, NGAYSINH = @NgaySinh, CCCD = @CCCD, 
        EMAIL = @Email, DIENTHOAI = @DienThoai, DIACHI = @DiaChi, DANTOC = @DanToc, 
        TONGIAO = @TonGiao, HONNHAN = @HonNhan, IDTD = @IDTD, IDCV = @IDCV, IDPB = @IDPB, IDCN = @IDCN
    WHERE IDNV = @IDNV;
    PRINT N'Đã cập nhật thông tin nhân viên ID: ' + CAST(@IDNV AS VARCHAR);
END;
GO

-- Procedure Ký hợp đồng mới
IF OBJECT_ID('sp_KyHopDong', 'P') IS NOT NULL DROP PROCEDURE sp_KyHopDong;
GO
CREATE PROCEDURE sp_KyHopDong
    @IDNV INT, @IDLoaiHD INT, @NgayKy DATE, @NgayBD DATE, @NgayKT DATE, @LuongCoBan DECIMAL(15,2)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE HOPDONG SET TRANGTHAI = N'Hết hiệu lực' WHERE IDNV = @IDNV AND TRANGTHAI = N'Đang hiệu lực';
    INSERT INTO HOPDONG (NGAYKY, NGAYBATDAU, NGAYKETTHUC, LUONGCOBAN, TRANGTHAI, IDNV, IDLOAIHD)
    VALUES (@NgayKy, @NgayBD, @NgayKT, @LuongCoBan, N'Đang hiệu lực', @IDNV, @IDLoaiHD);
    PRINT N'Đã ký hợp đồng mới cho nhân viên ID: ' + CAST(@IDNV AS VARCHAR);
END;
GO

-- Procedure Xóa nhân viên (Soft Delete)
IF OBJECT_ID('sp_XoaNhanVien', 'P') IS NOT NULL DROP PROCEDURE sp_XoaNhanVien;
GO
CREATE PROCEDURE sp_XoaNhanVien
    @IDNV INT
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE NHANVIEN SET TRANGTHAI = N'Đã nghỉ việc' WHERE IDNV = @IDNV;
    UPDATE TAIKHOAN SET TRANGTHAI = 0 WHERE IDNV = @IDNV;
    UPDATE HOPDONG SET TRANGTHAI = N'Đã chấm dứt', NGAYKETTHUC = GETDATE() 
    WHERE IDNV = @IDNV AND TRANGTHAI = N'Đang hiệu lực';
    PRINT N'Đã xử lý cho nghỉ việc nhân viên ID: ' + CAST(@IDNV AS VARCHAR);
END;
GO

-- Procedure Cập nhật nhanh số điện thoại
IF OBJECT_ID('sp_CapNhatSoDienThoaiNhanVien', 'P') IS NOT NULL DROP PROCEDURE sp_CapNhatSoDienThoaiNhanVien;
GO
CREATE PROCEDURE sp_CapNhatSoDienThoaiNhanVien
    @IDNV INT, @DienThoai VARCHAR(15)
AS
BEGIN
    UPDATE NHANVIEN SET DIENTHOAI = @DienThoai WHERE IDNV = @IDNV;
END;
GO


-- 4. CÁC PROCEDURE CHẤM CÔNG & TIỀN LƯƠNG
-- ==============================================

-- Procedure Khởi tạo bảng công tháng cho chi nhánh
IF OBJECT_ID('sp_ThemBangChamCong', 'P') IS NOT NULL DROP PROCEDURE sp_ThemBangChamCong;
GO
CREATE PROCEDURE sp_ThemBangChamCong
    @Thang INT, @Nam INT, @IDCN INT
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO BANGCHAMCONG (IDNV, THANG, NAM, CONGCHUAN, TRANGTHAI)
    SELECT IDNV, @Thang, @Nam, 26, N'Đang chấm'
    FROM NHANVIEN 
    WHERE IDCN = @IDCN AND TRANGTHAI = N'Đang làm việc'
    AND NOT EXISTS (SELECT 1 FROM BANGCHAMCONG WHERE IDNV = NHANVIEN.IDNV AND THANG = @Thang AND NAM = @Nam);
    PRINT N'Đã khởi tạo bảng công tháng ' + CAST(@Thang AS VARCHAR) + N' cho chi nhánh ' + CAST(@IDCN AS VARCHAR);
END;
GO

-- Procedure Ghi nhận giờ vào/ra (Giao ca)
IF OBJECT_ID('sp_GhiNhanGiaoCa', 'P') IS NOT NULL DROP PROCEDURE sp_GhiNhanGiaoCa;
GO
CREATE PROCEDURE sp_GhiNhanGiaoCa
    @IDBC INT, @NgayLam DATE, @GioVao TIME, @GioRa TIME
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @GioBatDauChuẩn INT;
    SELECT @GioBatDauChuẩn = GIA_TRI FROM THAMSO_HETHONG WHERE MA_TS = 'GIO_BATDAU';
    SET @GioBatDauChuẩn = ISNULL(@GioBatDauChuẩn, 8);
    DECLARE @PhutDiTre INT = 0;
    IF @GioVao > CAST(CAST(@GioBatDauChuẩn AS VARCHAR) + ':00' AS TIME)
        SET @PhutDiTre = DATEDIFF(MINUTE, CAST(CAST(@GioBatDauChuẩn AS VARCHAR) + ':00' AS TIME), @GioVao);

    IF EXISTS (SELECT 1 FROM CHITIET_BANGCONG WHERE IDBC = @IDBC AND NGAYLAM = @NgayLam)
        UPDATE CHITIET_BANGCONG SET GIOVAO = @GioVao, GIORA = @GioRa, SOPHUTDITRE = @PhutDiTre WHERE IDBC = @IDBC AND NGAYLAM = @NgayLam;
    ELSE
        INSERT INTO CHITIET_BANGCONG (IDBC, NGAYLAM, GIOVAO, GIORA, SOPHUTDITRE) VALUES (@IDBC, @NgayLam, @GioVao, @GioRa, @PhutDiTre);
END;
GO


-- 5. LOGIC PHÂN TÁN (SITE-SPECIFIC)
-- ==============================================

-- Site 1: Chi nhánh Miền Bắc
IF OBJECT_ID('SP_CN1_ThemNhanVien', 'P') IS NOT NULL DROP PROCEDURE SP_CN1_ThemNhanVien;
GO
CREATE PROCEDURE SP_CN1_ThemNhanVien
    @TenNV NVARCHAR(255), @GioiTinh NVARCHAR(10), @NgaySinh DATE, @CCCD CHAR(12),
    @Email VARCHAR(255), @DienThoai VARCHAR(15), @IDTD INT, @IDCV INT, @IDPB INT,
    @LuongCoBan DECIMAL(15,2), @IDLoaiHD INT, @NhomQuyen INT
AS
BEGIN
    EXEC sp_OnboardingNhanVien @TenNV, @GioiTinh, @NgaySinh, @CCCD, @Email, @DienThoai, @IDTD, @IDCV, @IDPB, 1, @LuongCoBan, @IDLoaiHD, @NhomQuyen, NULL, NULL, NULL;
END;
GO

-- Site 2: Chi nhánh Miền Trung
IF OBJECT_ID('SP_CN2_ThemNhanVien', 'P') IS NOT NULL DROP PROCEDURE SP_CN2_ThemNhanVien;
GO
CREATE PROCEDURE SP_CN2_ThemNhanVien
    @TenNV NVARCHAR(255), @GioiTinh NVARCHAR(10), @NgaySinh DATE, @CCCD CHAR(12),
    @Email VARCHAR(255), @DienThoai VARCHAR(15), @IDTD INT, @IDCV INT, @IDPB INT,
    @LuongCoBan DECIMAL(15,2), @IDLoaiHD INT, @NhomQuyen INT
AS
BEGIN
    EXEC sp_OnboardingNhanVien @TenNV, @GioiTinh, @NgaySinh, @CCCD, @Email, @DienThoai, @IDTD, @IDCV, @IDPB, 2, @LuongCoBan, @IDLoaiHD, @NhomQuyen, NULL, NULL, NULL;
END;
GO

-- Site 3: Chi nhánh Miền Nam
IF OBJECT_ID('SP_CN3_ThemNhanVien', 'P') IS NOT NULL DROP PROCEDURE SP_CN3_ThemNhanVien;
GO
CREATE PROCEDURE SP_CN3_ThemNhanVien
    @TenNV NVARCHAR(255), @GioiTinh NVARCHAR(10), @NgaySinh DATE, @CCCD CHAR(12),
    @Email VARCHAR(255), @DienThoai VARCHAR(15), @IDTD INT, @IDCV INT, @IDPB INT,
    @LuongCoBan DECIMAL(15,2), @IDLoaiHD INT, @NhomQuyen INT
AS
BEGIN
    EXEC sp_OnboardingNhanVien @TenNV, @GioiTinh, @NgaySinh, @CCCD, @Email, @DienThoai, @IDTD, @IDCV, @IDPB, 3, @LuongCoBan, @IDLoaiHD, @NhomQuyen, NULL, NULL, NULL;
END;
GO

-- Chuyển chi nhánh
IF OBJECT_ID('sp_ChuyenChiNhanh', 'P') IS NOT NULL DROP PROCEDURE sp_ChuyenChiNhanh;
GO
CREATE PROCEDURE sp_ChuyenChiNhanh
    @IDNV INT, @IDCN_Moi INT, @IDPB_Moi INT
AS
BEGIN
    UPDATE NHANVIEN SET IDCN = @IDCN_Moi, IDPB = @IDPB_Moi WHERE IDNV = @IDNV;
    PRINT N'Đã chuyển nhân viên ' + CAST(@IDNV AS VARCHAR) + N' sang chi nhánh ' + CAST(@IDCN_Moi AS VARCHAR);
END;
GO


-- 6. TRIGGERS & FUNCTIONS BỔ SUNG
-- ==============================================

-- Trigger đồng bộ bảng chấm công
IF OBJECT_ID('trg_Sync_BangChamCong', 'TR') IS NOT NULL DROP TRIGGER trg_Sync_BangChamCong;
GO
CREATE TRIGGER trg_Sync_BangChamCong
ON CHITIET_BANGCONG
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    DECLARE @AffectedIDBC TABLE (IDBC INT);
    INSERT INTO @AffectedIDBC SELECT DISTINCT IDBC FROM inserted UNION SELECT DISTINCT IDBC FROM deleted;
    UPDATE B SET 
        TONGNGAYLAM = (SELECT COUNT(*) FROM CHITIET_BANGCONG C LEFT JOIN LOAINGHIPHEP LNP ON C.IDLNP = LNP.IDLNP WHERE C.IDBC = B.IDBC AND (C.GIOVAO IS NOT NULL OR (C.IDLNP IS NOT NULL AND ISNULL(LNP.HUONGLUONG, 0) = 1))),
        SONGAYNGHI = (SELECT COUNT(*) FROM CHITIET_BANGCONG C WHERE C.IDBC = B.IDBC AND C.IDLNP IS NOT NULL),
        SONGAYDITRE = (SELECT COUNT(*) FROM CHITIET_BANGCONG C WHERE C.IDBC = B.IDBC AND C.SOPHUTDITRE > 0),
        SOGIOTANGCA = (SELECT ISNULL(SUM(SOGIOTANGCA),0) FROM CHITIET_BANGCONG C WHERE C.IDBC = B.IDBC)
    FROM BANGCHAMCONG B INNER JOIN @AffectedIDBC A ON B.IDBC = A.IDBC;
END;
GO

-- Trigger Log biến động lương
IF OBJECT_ID('trg_Log_BienDongLuong', 'TR') IS NOT NULL DROP TRIGGER trg_Log_BienDongLuong;
GO
CREATE TRIGGER trg_Log_BienDongLuong
ON HOPDONG
AFTER UPDATE
AS
BEGIN
    IF UPDATE(LUONGCOBAN)
    BEGIN
        INSERT INTO LOG_LUONG (IDNV, LUONG_CU, LUONG_MOI, GHI_CHU)
        SELECT i.IDNV, d.LUONGCOBAN, i.LUONGCOBAN, N'Cập nhật lương hợp đồng'
        FROM inserted i JOIN deleted d ON i.SODH = d.SODH WHERE i.LUONGCOBAN <> d.LUONGCOBAN;
    END
END;
GO

-- Trigger kiểm tra Trưởng phòng (Phân mảnh theo Site)
IF OBJECT_ID('trg_Check_TruongPhong', 'TR') IS NOT NULL DROP TRIGGER trg_Check_TruongPhong;
GO
CREATE TRIGGER trg_Check_TruongPhong
ON PHONGBAN
AFTER INSERT, UPDATE
AS
BEGIN
    IF EXISTS (SELECT 1 FROM inserted i INNER JOIN NHANVIEN nv ON i.ID_TRUONGPHONG = nv.IDNV WHERE nv.IDPB <> i.IDPB)
    BEGIN
        RAISERROR (N'Lỗi: Trưởng phòng phải là nhân viên thuộc chính phòng ban đó!', 16, 1);
        ROLLBACK TRANSACTION;
    END
END;
GO

-- Function lấy tổng lương thực nhận của phòng ban
IF OBJECT_ID('fn_LayTongLuongPhongBan', 'FN') IS NOT NULL DROP FUNCTION fn_LayTongLuongPhongBan;
GO
CREATE FUNCTION fn_LayTongLuongPhongBan(@IDPB INT, @Thang INT, @Nam INT)
RETURNS DECIMAL(15,2)
AS
BEGIN
    DECLARE @TongLuong DECIMAL(15,2);
    SELECT @TongLuong = SUM(BL.THUC_NHAN) FROM BANGLUONG BL JOIN BANGCHAMCONG BC ON BL.IDBC = BC.IDBC JOIN NHANVIEN NV ON BC.IDNV = NV.IDNV WHERE NV.IDPB = @IDPB AND BC.THANG = @Thang AND BC.NAM = @Nam;
    RETURN ISNULL(@TongLuong, 0);
END;
GO

PRINT N'Toàn bộ Logic (Function, Procedure, Trigger) đã được cài đặt thành công!'
GO

