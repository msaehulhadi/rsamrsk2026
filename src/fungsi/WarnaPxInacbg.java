/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fungsi;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class WarnaPxInacbg extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        Component component = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        if (isSelected) {
            // pakai warna selection kalau baris dipilih
            component.setBackground(table.getSelectionBackground());
            component.setForeground(table.getSelectionForeground());
        } else {
            // Reset ke default supaya warna tidak terbawa
            if (row % 2 == 1) {
                component.setBackground(new Color(255, 246, 244));
            } else {
                component.setBackground(Color.WHITE);
            }
            component.setForeground(Color.BLACK); // default text color
        }

        // Jika kolom 3 berisi "0"
//        if (table.getValueAt(row, 3) != null &&
//            table.getValueAt(row, 3).toString().contains("0")) {
//            component.setBackground(new Color(204, 204, 204)); // abu-abu
//            component.setForeground(Color.BLACK);
//        }
       
        // === Kondisi merah kolom 0 && kolom 7 ===
        Object kol7 = table.getValueAt(row, 7);
        boolean kol0Checked = Boolean.TRUE.equals(table.getValueAt(row, 0));
        if (kol7 != null && kol7.toString().contains("IM tidak berlaku") && kol0Checked) {
            component.setBackground(new Color(178, 34, 34)); // merah tua
            component.setForeground(Color.WHITE);            // teks putih
        }

        return component;
    }
}

