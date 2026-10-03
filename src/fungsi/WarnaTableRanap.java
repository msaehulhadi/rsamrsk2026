/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fungsi;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Owner
 */
public class WarnaTableRanap extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            component.setBackground(new Color(255,255,153)); // warna select
            component.setForeground(Color.BLACK); // teks putih
            component.setFont(component.getFont().deriveFont(java.awt.Font.BOLD)); // teks tebal
        } else {
            if (row % 2 == 1){
                component.setBackground(new Color(204,255,204));
            }else{
                component.setBackground(new Color(255,255,255));
            } 

            if(table.getValueAt(row,23).toString().equals("SEP Belum Dibuat")){
                component.setBackground(new Color(200,0,0));
                component.setForeground(new Color(255,230,230));
            }else if(table.getValueAt(row,23).toString().equals("SEP Sudah Ada")){
                component.setBackground(new Color(152,152,156));
                component.setForeground(new Color(245,245,255));
            }else if(table.getValueAt(row,23).toString().equals("SEP Double")){
                component.setBackground(new Color(119,221,119));
                component.setForeground(new Color(245,255,245));
            }else if(table.getValueAt(row,23).toString().equals("SEP Rajal Masih ada")){
                component.setBackground(new Color(200,0,0));
                component.setForeground(new Color(255,230,230));
            }
        }
        return component;
    }

}
