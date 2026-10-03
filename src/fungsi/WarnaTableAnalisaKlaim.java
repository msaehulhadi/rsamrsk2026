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
public class WarnaTableAnalisaKlaim extends DefaultTableCellRenderer {
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
            component.setForeground(Color.BLACK); // teks putih
            }else{
                component.setBackground(new Color(255,255,255));
            component.setForeground(Color.BLACK); // teks putih
            } 
            
            if(table.getValueAt(row,8).toString().equals("Pending BPJS")){
                component.setBackground(new Color(200,0,0));
                component.setForeground(new Color(255,230,230));
            }
        }
        return component;
    }

}
