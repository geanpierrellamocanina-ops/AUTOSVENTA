/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author JE
 */
public class ImageRenderer extends DefaultTableCellRenderer {
    public ImageRenderer() {
        setHorizontalAlignment(JLabel.CENTER); // Centrar horizontalmente
        setVerticalAlignment(JLabel.CENTER);   // Centrar verticalmente
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof ImageIcon) {
            setIcon((ImageIcon) value);
            setText("");
        } else {
            setIcon(null);
            setText((value == null) ? "" : value.toString());
        }
        return this;
    }
}