/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fungsi;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import fungsi.sekuel;

/**
 *
 * @author Owner
 */
public class WarnaTableWA extends DefaultTableCellRenderer {
    private sekuel Sequel=new sekuel();
//    private String kodeTemplateKunjungan = Sequel.cariIsi("select template_pesan_wa_default.no_template from template_pesan_wa_default where template_pesan_wa_default.jenis=?","kunjungan");
//    private String kodeTemplateKontrol = Sequel.cariIsi("select template_pesan_wa_default.no_template from template_pesan_wa_default where template_pesan_wa_default.jenis=?","kontrol");
//    private String kodeTemplateRadiologi = Sequel.cariIsi("select template_pesan_wa_default.no_template from template_pesan_wa_default where template_pesan_wa_default.jenis=?","radiologi");
//    private String kodeTemplateLab = Sequel.cariIsi("select template_pesan_wa_default.no_template from template_pesan_wa_default where template_pesan_wa_default.jenis=?","lab");
        
    
    @Override
    //private int adaalergi=0;
    
    //adaalergi =Sequel.cariInteger("select count(rangkuman_alergi.no_rkm_medis) from rangkuman_alergi where rangkuman_alergi.no_rkm_medis=?",table.getValueAt(row,2).toString()>0);
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//        System.out.println(kodeTemplateKunjungan);
//        System.out.println(kodeTemplateKontrol);
        if (row % 2 == 1){
            component.setBackground(new Color(255,244,244));
            component.setForeground(new Color(50,50,50));
        }else{
            component.setBackground(new Color(255,255,255));
            component.setForeground(new Color(50,50,50));
        } 
        
//        if(table.getValueAt(row,0).toString().equals(kodeTemplateKunjungan)){
//            component.setBackground(new Color(245,66,66));
//            component.setForeground(new Color(245,255,245));
//        }else if(table.getValueAt(row,0).toString().equals(kodeTemplateKontrol)){
//            component.setBackground(new Color(255,243,109));
//            component.setForeground(new Color(120,110,50));
//        }else if(table.getValueAt(row,0).toString().equals(kodeTemplateRadiologi)){
//            component.setBackground(new Color(0,153,255));
//            component.setForeground(new Color(245,255,245));
//        }else if(table.getValueAt(row,0).toString().equals(kodeTemplateLab)){
//            component.setBackground(new Color(0,153,0));
//            component.setForeground(new Color(245,255,245));
//        }
        return component;
    }

}
