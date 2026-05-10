package com.qlns.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class UIUtils {

    public static void styleTable(JTable table) {
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(212, 230, 241)); // Light Blue
        header.setForeground(new Color(44, 62, 80));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 40));

        // Zebra stripes
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 243, 244));
                }
                c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                return c;
            }
        });

        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(52, 152, 219, 50));
        table.setSelectionForeground(Color.BLACK);
    }

    public static void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        
        // Custom UI for rounded corners
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, c.getWidth(), c.getHeight(), 15, 15));
                g2.dispose();
                super.paint(g, c);
            }
        });
    }
}
