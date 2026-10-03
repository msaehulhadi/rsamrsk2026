/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgPasienMati.java
 *
 * Created on Aug 30, 2010, 7:46:01 AM
 */

package bridging;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import fungsi.CustomTableResponse;
import fungsi.WarnaDxDRG;
import fungsi.WarnaDxInacbg;
import fungsi.WarnaPxInacbg;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import fungsi.formatAngka;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import kepegawaian.DlgCariDokter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import simrskhanza.DlgPasien;
//import widget.TextBox;
import widget.Toast;
import widget.ToastType;

/**
 *
 * @author dosen3
 */
public class INACBGIdrgData extends javax.swing.JDialog {
    private final DefaultTableModel tabCombo, tabCombo1, tabCombo2, tabCombo22,tabMode; // tabModeDiagnosa,tabModeProsedur;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private DlgPasien pasien=new DlgPasien(null,false);
    private PreparedStatement ps,pskoders,pspenyakit,psprosedur;
    private ResultSet rs,rskode,rs2,rs3,rsdiagnosa, rsprosedur,rsgrouper;
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private String tgl, finger="", lama="",payor_id="",payor_cd="",coder="",dokterkonsul="",tgl_masuk_ranap="",tgl_keluar_ranap="",dpjp_ranap="",inacbg_diagnosa="",prosedur_inacbg="",no_rawat_bayi="";
    private String[] part;
    private String[] kode,nama,ciripny,keterangan,kategori,cirium,kode2,panjang,pendek,multi;
    private boolean[] pilih;
    private final String KOMPILASIBERKASGUNAKANRIWAYATPASIEN = koneksiDB.KOMPILASIBERKASGUNAKANRIWAYATPASIEN(),
                         KODEPPKBPJS = Sequel.cariIsiSmc("select setting.kode_ppk from setting limit 1") + "%";
    private final boolean[] updatingDx = {false}, updatingPx = {false};
    private boolean vl=false,vb=false, updateCombo=false;
    public  ApiINACBG inacbg = new ApiINACBG();
    private int total_biaya = 0,i=0,j=0,jml=0,index=0,verticalPos = 0;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter_tanggal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    
    /** Creates new form DlgPasienMati
     * @param parent
     * @param modal */
    public INACBGIdrgData(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
//        initTambahComboDiTable()
//        addCellEditorCombo();       
        tabMode = new DefaultTableModel(null, new Object[] {
            "No. Rawat", "No. SEP", "No. RM", "Nama Pasien", "Status",
            "Tgl. SEP", "Tgl. Pulang", "Stts. Pulang", "Ruangan", "DPJP",
            "Diagnosa", "Status INACBG", "KirimINACBG","No Kartu"
        }) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return java.lang.String.class;
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        
        tbKompilasi.setModel(tabMode);
        tbKompilasi.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbKompilasi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        tbKompilasi.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tbKompilasi.getColumnModel().getColumn(0).setPreferredWidth(10);
        tbKompilasi.getColumnModel().getColumn(1).setPreferredWidth(126);
        tbKompilasi.getColumnModel().getColumn(2).setPreferredWidth(46);
        tbKompilasi.getColumnModel().getColumn(3).setPreferredWidth(200);
        tbKompilasi.getColumnModel().getColumn(4).setPreferredWidth(50);
        tbKompilasi.getColumnModel().getColumn(5).setPreferredWidth(80);
        tbKompilasi.getColumnModel().getColumn(6).setPreferredWidth(70);
        tbKompilasi.getColumnModel().getColumn(7).setPreferredWidth(90);
        tbKompilasi.getColumnModel().getColumn(8).setPreferredWidth(180);
        tbKompilasi.getColumnModel().getColumn(9).setPreferredWidth(140);
        tbKompilasi.getColumnModel().getColumn(10).setPreferredWidth(70);
        tbKompilasi.getColumnModel().getColumn(11).setPreferredWidth(100);
        tbKompilasi.getColumnModel().getColumn(12).setMinWidth(0);
        tbKompilasi.getColumnModel().getColumn(12).setMaxWidth(0);
        
        tbKompilasi.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row % 2 == 1) {
                    component.setBackground(new Color(255, 244, 244));
                    component.setForeground(new Color(50, 50, 50));
                } else {
                    component.setBackground(new Color(255, 255, 255));
                    component.setForeground(new Color(50, 50, 50));
                }
                if (table.getValueAt(row, 12).toString().equals("1")) {
                    component.setBackground(new Color(50, 50, 50));
                    component.setForeground(new Color(255, 255, 255));
                }
                return component;
            }
        });

//01 --------------------------------------------------------------------------------------
        tabCombo=new DefaultTableModel(null,new Object[]{
//            CODE, CODE2, DESCRIPTION, SYSTEM, VALIDCODE, ACCPDX, ASTERISK, IM
            "P","Kode","Nama Penyakit","VALID", "DX UTAMA", "ASTERISK","IM","."}){
            @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbCombo.setModel(tabCombo);
        tbCombo.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbCombo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i= 0; i < 8; i++) {
            TableColumn column = tbCombo.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(40);
            }else if(i==2){
                column.setPreferredWidth(400);
            }else if(i==3){
                column.setPreferredWidth(70);
//                column.setMinWidth(0);
//                column.setMaxWidth(0);
            }else if(i==4){
                column.setPreferredWidth(70);
//                column.setMinWidth(0);
//                column.setMaxWidth(0);
            }else if(i==5){
//                column.setPreferredWidth(50);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==6){
//                column.setPreferredWidth(50);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==7){
                column.setPreferredWidth(150);
            }
        }
        tbCombo.setDefaultRenderer(Object.class, new WarnaDxDRG());

        // pasang editor khusus ke kolom 0
        TableColumn col0 = tbCombo.getColumnModel().getColumn(0);
//        col0.setCellEditor(new ValidatingBooleanEditor(tabCombo));
        
//        DefaultCellEditor checkBoxEditor = new DefaultCellEditor(new JCheckBox()) {
        col0.setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public boolean stopCellEditing() {

                String noVal = tbCombo.getValueAt(tbCombo.getSelectedRow(), 3).toString();
                String dxutama = tbCombo.getValueAt(tbCombo.getSelectedRow(), 4).toString();
                int rowCount = tabCombo.getRowCount();
//                List<Integer> trueRows = new ArrayList<>();
                boolean adaTrue = false;
                
                for (int i=0; i < rowCount; i++) {
                    Boolean checked = (Boolean) tabCombo.getValueAt(i, 0);
//                    System.out.println("checked : " + checked );
                    if( checked != null && checked ) {
                        adaTrue = true;
//                        trueRows.add(i);
                        break;
                    }
                }
//                System.out.println("adaTrue : " + adaTrue );
                
                if("0".equals(noVal)) {
//                    tabCombo.setValueAt(false, tbCombo.getSelectedRow(), 0); // kembalikan
                    Toast.show("Diagnosa Tidak bisa digunakan, Coba yang lain", ToastType.ERROR);
                     cancelCellEditing();
                    return false; // hentikan
                }
                
                if( adaTrue ) {
                } else {
                    if(dxutama.equals("N")) {
//                        tabCombo.setValueAt(false, tbCombo.getSelectedRow(), 0); // kembalikan
                        Toast.show("Tidak Dapat digunakan sebagai Diagnosa Utama", ToastType.ERROR);
                         cancelCellEditing();
                        return false;
                    }
                }
                
                
                return super.stopCellEditing();
            }
        });
        
//01 --------------------------------------------------------------------------------------
        tabCombo1=new DefaultTableModel(null,new Object[]{
//            CODE, CODE2, DESCRIPTION, SYSTEM, VALIDCODE, ACCPDX, ASTERISK, IM
            "P","Kode","Nama Penyakit","VALID", "DX UTAMA", "ASTERISK","IM","."}){
            @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbCombo1.setModel(tabCombo1);
        tbCombo1.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbCombo1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i= 0; i < 8; i++) {
            TableColumn column = tbCombo1.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(40);
            }else if(i==2){
                column.setPreferredWidth(400);
            }else if(i==3){
                column.setPreferredWidth(70);
//                column.setMinWidth(0);
//                column.setMaxWidth(0);
            }else if(i==4){
                column.setPreferredWidth(70);
//                column.setMinWidth(0);
//                column.setMaxWidth(0);
            }else if(i==5){
//                column.setPreferredWidth(50);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==6){
//                column.setPreferredWidth(50);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==7){
                column.setPreferredWidth(150);
            }
        }
        tbCombo1.setDefaultRenderer(Object.class, new WarnaDxInacbg());

        // pasang editor khusus ke kolom 0
        TableColumn col01 = tbCombo1.getColumnModel().getColumn(0);
//        col0.setCellEditor(new ValidatingBooleanEditor(tabCombo));
        
//        DefaultCellEditor checkBoxEditor = new DefaultCellEditor(new JCheckBox()) {
        col01.setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public boolean stopCellEditing() {

                String noVal = tbCombo1.getValueAt(tbCombo1.getSelectedRow(), 3).toString();
                String dxutama = tbCombo1.getValueAt(tbCombo1.getSelectedRow(), 4).toString();
                int rowCount = tabCombo1.getRowCount();
                boolean adaTrue = false;
                
                for (int i=0; i < rowCount; i++) {
                    Boolean checked = (Boolean) tabCombo1.getValueAt(i, 0);
//                    System.out.println("checked : " + checked );
                    if( checked != null && checked ) {
                        adaTrue = true;
                        break;
                    }
                }
                System.out.println("adaTrue : " + adaTrue );
                
                if("0".equals(noVal)) {
//                    tabCombo.setValueAt(false, tbCombo.getSelectedRow(), 0); // kembalikan
                    Toast.show("Diagnosa Tidak bisa digunakan, Coba yang lain", ToastType.ERROR);
                     cancelCellEditing();
                    return false; // hentikan
                }
                
                if( adaTrue ) {
                } else {
                    if(dxutama.equals("N")) {
//                        tabCombo.setValueAt(false, tbCombo.getSelectedRow(), 0); // kembalikan
                        Toast.show("Tidak Dapat digunakan sebagai Diagnosa Utama", ToastType.ERROR);
                         cancelCellEditing();
                        return false;
                    }
                }
                
                return super.stopCellEditing();
            }
        });
        
        tabCombo1.addTableModelListener(e -> {
          if (e.getColumn() == 0 && e.getFirstRow() >= 0) {
            int mr = e.getFirstRow();
            int vr = tbCombo1.convertRowIndexToView(mr);
            if (vr >= 0) {
              Rectangle r = tbCombo1.getCellRect(vr, 0, true);
              r.x = 0; r.width = tbCombo1.getWidth(); // seluruh baris
              tbCombo1.repaint(r);
            }
          }
        });
        
        
//01 --------------------------------------------------------------------------------------
        tabCombo2=new DefaultTableModel(null,new Object[]{
//            CODE, CODE2, DESCRIPTION, SYSTEM, VALIDCODE, ACCPDX, ASTERISK, IM
            "P","Kode","Nama Penyakit","VALID", "DX UTAMA", "ASTERISK","IM","Multiplicity"}){
            @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0 || colIndex==7 ) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbCombo2.setModel(tabCombo2);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbCombo2.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbCombo2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i= 0; i < 8; i++) {
            TableColumn column = tbCombo2.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(40);
            }else if(i==2){
                column.setPreferredWidth(550);
            }else if(i==3){
//                column.setPreferredWidth(70);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==4){
//                column.setPreferredWidth(70);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==5){
//                column.setPreferredWidth(50);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==6){
//                column.setPreferredWidth(50);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==7){
                column.setPreferredWidth(80);
            }
        }
        tbCombo2.setDefaultRenderer(Object.class, new WarnaDxDRG());
        
//        tbCombo2.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                SwingUtilities.invokeLater(() -> {
//                    if (!textProc.isFocusOwner()) {
//                        scrollCombo2.setVisible(false);
//                    }
//                });
//            }
//        });
        
        // listener untuk menangkap perubahan nilai
        tabCombo2.addTableModelListener(e -> {
            if (!updatingPx[0] && e.getColumn() == 0 && e.getFirstRow() >= 0) {
                int row = e.getFirstRow();
                Boolean newValue = (Boolean) tabCombo2.getValueAt(row, 0);
                String status = (String) tabCombo2.getValueAt(row, 3);
                String noVal = tbCombo2.getValueAt(tbCombo2.getSelectedRow(), 3).toString();

                if (noVal.equals("0")) {
                    updatingPx[0] = true;
                    tabCombo2.setValueAt(false, row, 0); // kembalikan
                    updatingPx[0] = false;
                    Toast.show("Prosedur Tidak bisa digunakan, Coba yang lain", ToastType.ERROR);
                    return; // hentikan
                }
                
//                if ("no".equalsIgnoreCase(status)) {
//                    // kembalikan ke nilai sebelumnya (false)
//                    tabCombo.setValueAt(false, row, 0);
//
//                    // tampilkan notifikasi
//                    JOptionPane.showMessageDialog(null,
//                            "Tidak boleh memilih row ini, Status = NO",
//                            "Peringatan",
//                            JOptionPane.WARNING_MESSAGE);
//                }
            }
        });

//01 --------------------------------------------------------------------------------------
        tabCombo22=new DefaultTableModel(null,new Object[]{
//            CODE, CODE2, DESCRIPTION, SYSTEM, VALIDCODE, ACCPDX, ASTERISK, IM
            "P","Kode","Nama Penyakit","VALID", "DX UTAMA", "ASTERISK","IM","."}){
            @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0 ) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbCombo22.setModel(tabCombo22);
        tbCombo22.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbCombo22.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i= 0; i < 8; i++) {
            TableColumn column = tbCombo22.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(40);
            }else if(i==2){
                column.setPreferredWidth(550);
            }else if(i==3){
//                column.setPreferredWidth(70);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==4){
//                column.setPreferredWidth(70);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==5){
//                column.setPreferredWidth(50);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==6){
//                column.setPreferredWidth(50);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbCombo22.setDefaultRenderer(Object.class, new WarnaPxInacbg());
                
        // listener untuk menangkap perubahan nilai
        tabCombo22.addTableModelListener(e -> {
            if (!updatingPx[0] && e.getColumn() == 0 && e.getFirstRow() >= 0) {
                int row = e.getFirstRow();
                Boolean newValue = (Boolean) tabCombo22.getValueAt(row, 0);
                String status = (String) tabCombo22.getValueAt(row, 3);
                String noVal = tbCombo22.getValueAt(tbCombo22.getSelectedRow(), 3).toString();

                if (noVal.equals("0")) {
                    updatingPx[0] = true;
                    tabCombo22.setValueAt(false, row, 0); // kembalikan
                    updatingPx[0] = false;
                    Toast.show("Prosedur Tidak bisa digunakan, Coba yang lain", ToastType.ERROR);
                    return; // hentikan
                }
                
            }
        });
               
        cmbProc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                if(updateCombo==false) {
//                    Object selected = cmbProc.getSelectedItem().toString().split(":")[0].trim();                    
                    setCMG();
                }
            }
        });

        cmbPros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Object selected = cmbPros.getSelectedItem().toString().split(":")[0].trim();
                if(updateCombo==false) {
                    setCMG();
                }
            }
        });

        cmbInv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Object selected = cmbInv.getSelectedItem().toString().split(":")[0].trim();
                if(updateCombo==false) {
                    setCMG();
                }
            }
        });

        cmbDrugs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Object selected = cmbDrugs.getSelectedItem().toString().split(":")[0].trim();
//                if (selected != null && !selected.equals("none")) {
                if(updateCombo==false) {
                    setCMG();
                }
            }
        });
        
//        scrollInput.getViewport().addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                int posisiVertikal = scrollPane.getViewport().getViewPosition().y;
//                System.out.println("Posisi scroll vertikal: " + posisiVertikal);
//            }
//        });
        
        scrollInput.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                int posisiVertikal = e.getValue();
                verticalPos = posisiVertikal;
//                System.out.println("Posisi scroll vertikal: " + posisiVertikal);
            }
        });

        
        scrollRespon.setWheelScrollingEnabled(false);
        scrollCombo.setWheelScrollingEnabled(false);
        scrollCombo2.setWheelScrollingEnabled(false);
        scrollCombo4.setWheelScrollingEnabled(false);
        scrollCombo5.setWheelScrollingEnabled(false);
        scrollBilling.setWheelScrollingEnabled(false);
        
        Scroll2.setWheelScrollingEnabled(false);
        Scroll3.setWheelScrollingEnabled(false);
        // opsional: arahkan event scroll anak ke parent
        java.awt.event.MouseWheelListener redirector = e -> scrollInput.dispatchEvent(e);
        scrollRespon.addMouseWheelListener(redirector);
        scrollCombo.addMouseWheelListener(redirector);
        scrollCombo2.addMouseWheelListener(redirector);
        scrollCombo4.addMouseWheelListener(redirector);
        scrollCombo5.addMouseWheelListener(redirector);
        scrollBilling.addMouseWheelListener(redirector);
        
        Scroll2.addMouseWheelListener(redirector);
        Scroll3.addMouseWheelListener(redirector);
        
        pnb.setDocument(new batasInput((byte)10).getOnlyAngka(pnb));
        ahli.setDocument(new batasInput((byte)10).getOnlyAngka(ahli));
        rad.setDocument(new batasInput((byte)10).getOnlyAngka(rad));
        rehab.setDocument(new batasInput((byte)10).getOnlyAngka(rehab));
        obat.setDocument(new batasInput((byte)10).getOnlyAngka(obat));
        alkes.setDocument(new batasInput((byte)10).getOnlyAngka(alkes));
        pb.setDocument(new batasInput((byte)10).getOnlyAngka(pb));
        kpr.setDocument(new batasInput((byte)10).getOnlyAngka(kpr));
        lab.setDocument(new batasInput((byte)10).getOnlyAngka(lab));
        akomodasi.setDocument(new batasInput((byte)10).getOnlyAngka(akomodasi));
        kronis.setDocument(new batasInput((byte)10).getOnlyAngka(kronis));
        bmhp.setDocument(new batasInput((byte)10).getOnlyAngka(bmhp));
        ksl.setDocument(new batasInput((byte)10).getOnlyAngka(ksl));
        pnj.setDocument(new batasInput((byte)10).getOnlyAngka(pnj));
        pl_darah.setDocument(new batasInput((byte)10).getOnlyAngka(pl_darah));
        ri.setDocument(new batasInput((byte)10).getOnlyAngka(ri));
        obat_kemo.setDocument(new batasInput((byte)10).getOnlyAngka(obat_kemo));
        sewa_alat.setDocument(new batasInput((byte)10).getOnlyAngka(sewa_alat));
        multiple1.setDocument(new batasInput((byte)1).getOnlyAngka(multiple1));
        multiple2.setDocument(new batasInput((byte)1).getOnlyAngka(multiple2));
        multiple3.setDocument(new batasInput((byte)1).getOnlyAngka(multiple3));
        multiple4.setDocument(new batasInput((byte)1).getOnlyAngka(multiple4));
        multiple5.setDocument(new batasInput((byte)1).getOnlyAngka(multiple5));
        multiple6.setDocument(new batasInput((byte)1).getOnlyAngka(multiple6));
        multiple7.setDocument(new batasInput((byte)1).getOnlyAngka(multiple7));
        multiple8.setDocument(new batasInput((byte)1).getOnlyAngka(multiple8));
        multiple9.setDocument(new batasInput((byte)1).getOnlyAngka(multiple9));
        multiple10.setDocument(new batasInput((byte)1).getOnlyAngka(multiple10));


        
    }
    
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        no_rawat = new widget.TextBoxW11UI();
        icd10 = new widget.TextBoxW11UI();
        icd9 = new widget.TextBoxW11UI();
        Cmgs = new widget.TextBoxW11UI();
        tabPane1 = new widget.TabPane();
        internalFrame1 = new widget.InternalFrame();
        FormInput1 = new widget.PanelBiasa();
        jLabel3 = new widget.Label();
        jLabel17 = new widget.Label();
        Status = new widget.ComboBox();
        TNoRw = new widget.TextBoxW11UI();
        TNoSEP = new widget.TextBoxW11UI();
        TPasien = new widget.TextBoxW11UI();
        TNoRM = new widget.TextBoxW11UI();
        Tnoka = new widget.TextBoxW11UI();
        jLabel4 = new widget.Label();
        jLabel5 = new widget.Label();
        jLabel9 = new widget.Label();
        jLabel10 = new widget.Label();
        jLabel24 = new widget.Label();
        jaminan = new widget.ComboBox();
        panelGlass8 = new widget.panelisi();
        BtnGrouperIDRG = new widget.Button();
        finalklaimIDRG = new widget.Button();
        editKlaimIDRG = new widget.Button();
        hapusKlaim = new widget.Button();
        BtnGrouper = new widget.Button();
        finalklaim1 = new widget.Button();
        editKlaim1 = new widget.Button();
        finalklaim = new widget.Button();
        editKlaim = new widget.Button();
        kirimonline = new widget.Button();
        jLabel25 = new widget.Label();
        BtnKeluar = new widget.Button();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.panelisi();
        tgl_masuk = new widget.Tanggal();
        jLabel12 = new widget.Label();
        jLabel15 = new widget.Label();
        cara_masuk = new widget.ComboBox();
        sistole = new widget.TextBoxW11UI();
        jLabel22 = new widget.Label();
        jenis_rawat = new widget.ComboBox();
        cob = new widget.ComboBox();
        jLabel19 = new widget.Label();
        jLabel20 = new widget.Label();
        jLabel23 = new widget.Label();
        hak_kelas = new widget.ComboBox();
        jLabel26 = new widget.Label();
        jLabel43 = new widget.Label();
        jam_masuk = new widget.ComboBox();
        menit_masuk = new widget.ComboBox();
        tgl_keluar = new widget.Tanggal();
        jLabel44 = new widget.Label();
        jam_keluar = new widget.ComboBox();
        menit_keluar = new widget.ComboBox();
        rawat_intensif = new javax.swing.JCheckBox();
        jLabel45 = new widget.Label();
        ventilator = new javax.swing.JCheckBox();
        naik_turun_kelas = new javax.swing.JCheckBox();
        naik_kelas_pelayanan = new widget.ComboBox();
        jLabel47 = new widget.Label();
        lama_naik_kelas = new widget.TextBoxW11UI();
        jLabel48 = new widget.Label();
        jLabel51 = new widget.Label();
        jLabel52 = new widget.Label();
        chronic = new widget.TextBoxW11UI();
        sub_acute = new widget.TextBoxW11UI();
        jLabel54 = new widget.Label();
        berat_bayi = new widget.TextBoxW11UI();
        jLabel55 = new widget.Label();
        dpjp = new widget.TextBoxW11UI();
        jLabel57 = new widget.Label();
        validasi_tb = new widget.Button();
        status_pulang = new widget.ComboBox();
        jLabel59 = new widget.Label();
        kelas_eksekutif = new javax.swing.JCheckBox();
        tgl_intubasi = new widget.Tanggal();
        jam_intubasi = new widget.ComboBox();
        menit_intubasi = new widget.ComboBox();
        total_rupiah = new widget.Label();
        menit_ekstubasi = new widget.ComboBox();
        jam_ekstubasi = new widget.ComboBox();
        tgl_ekstubasi = new widget.Tanggal();
        jLabel62 = new widget.Label();
        hari_intensif = new widget.TextBoxW11UI();
        jLabel63 = new widget.Label();
        jLabel66 = new widget.Label();
        jLabel67 = new widget.Label();
        distole = new widget.TextBoxW11UI();
        tarif_eksekutif = new widget.TextBoxW11UI();
        jLabel13 = new widget.Label();
        tgl_lahir = new widget.TextBoxW11UI();
        jLabel60 = new widget.Label();
        jenis_kelamin = new widget.TextBoxW11UI();
        jLabel64 = new widget.Label();
        jam_ventilator = new widget.TextBoxW11UI();
        jLabel65 = new widget.Label();
        jLabel68 = new widget.Label();
        jLabel69 = new widget.Label();
        nm_diag_1 = new widget.TextBoxW11UI();
        jLabel71 = new widget.Label();
        kd_diag_1 = new widget.TextBoxW11UI();
        nm_diag_2 = new widget.TextBoxW11UI();
        kd_diag_2 = new widget.TextBoxW11UI();
        nm_diag_3 = new widget.TextBoxW11UI();
        kd_diag_3 = new widget.TextBoxW11UI();
        nm_diag_4 = new widget.TextBoxW11UI();
        kd_diag_4 = new widget.TextBoxW11UI();
        nm_diag_5 = new widget.TextBoxW11UI();
        kd_diag_5 = new widget.TextBoxW11UI();
        nm_psdr_1 = new widget.TextBoxW11UI();
        kd_psdr_1 = new widget.TextBoxW11UI();
        nm_psdr_2 = new widget.TextBoxW11UI();
        kd_psdr_2 = new widget.TextBoxW11UI();
        nm_psdr_3 = new widget.TextBoxW11UI();
        kd_psdr_3 = new widget.TextBoxW11UI();
        nm_psdr_4 = new widget.TextBoxW11UI();
        kd_psdr_4 = new widget.TextBoxW11UI();
        nm_psdr_5 = new widget.TextBoxW11UI();
        kd_psdr_5 = new widget.TextBoxW11UI();
        chk_diag_5 = new javax.swing.JCheckBox();
        chk_diag_1 = new javax.swing.JCheckBox();
        chk_diag_2 = new javax.swing.JCheckBox();
        chk_diag_3 = new javax.swing.JCheckBox();
        chk_diag_4 = new javax.swing.JCheckBox();
        total_rupiah1 = new widget.Label();
        jLabel75 = new widget.Label();
        jLabel76 = new widget.Label();
        stage_2 = new widget.ComboBox();
        jLabel49 = new widget.Label();
        jLabel53 = new widget.Label();
        pasien_tb = new widget.TextBoxW11UI();
        batal_validasi_tb = new widget.Button();
        pasien_hd = new widget.ComboBox();
        jLabel56 = new widget.Label();
        kt_darah = new widget.TextBoxW11UI();
        jLabel79 = new widget.Label();
        check_hd = new javax.swing.JCheckBox();
        jLabel77 = new widget.Label();
        respon = new widget.Label();
        respon2 = new widget.Label();
        nm_diag_6 = new widget.TextBoxW11UI();
        kd_diag_6 = new widget.TextBoxW11UI();
        nm_diag_7 = new widget.TextBoxW11UI();
        kd_diag_7 = new widget.TextBoxW11UI();
        nm_diag_8 = new widget.TextBoxW11UI();
        kd_diag_8 = new widget.TextBoxW11UI();
        nm_diag_9 = new widget.TextBoxW11UI();
        kd_diag_9 = new widget.TextBoxW11UI();
        nm_diag_10 = new widget.TextBoxW11UI();
        kd_diag_10 = new widget.TextBoxW11UI();
        chk_diag_6 = new javax.swing.JCheckBox();
        chk_diag_7 = new javax.swing.JCheckBox();
        chk_diag_8 = new javax.swing.JCheckBox();
        chk_diag_9 = new javax.swing.JCheckBox();
        chk_diag_10 = new javax.swing.JCheckBox();
        nm_psdr_6 = new widget.TextBoxW11UI();
        kd_psdr_6 = new widget.TextBoxW11UI();
        nm_psdr_7 = new widget.TextBoxW11UI();
        kd_psdr_7 = new widget.TextBoxW11UI();
        nm_psdr_8 = new widget.TextBoxW11UI();
        kd_psdr_8 = new widget.TextBoxW11UI();
        nm_psdr_9 = new widget.TextBoxW11UI();
        kd_psdr_9 = new widget.TextBoxW11UI();
        nm_psdr_10 = new widget.TextBoxW11UI();
        kd_psdr_10 = new widget.TextBoxW11UI();
        multiple1 = new widget.TextBoxW11UI();
        multiple2 = new widget.TextBoxW11UI();
        multiple3 = new widget.TextBoxW11UI();
        multiple4 = new widget.TextBoxW11UI();
        multiple5 = new widget.TextBoxW11UI();
        multiple6 = new widget.TextBoxW11UI();
        multiple7 = new widget.TextBoxW11UI();
        multiple8 = new widget.TextBoxW11UI();
        multiple9 = new widget.TextBoxW11UI();
        multiple10 = new widget.TextBoxW11UI();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel58 = new widget.Label();
        jLabel61 = new widget.Label();
        jLabel72 = new widget.Label();
        jLabel73 = new widget.Label();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        scrollBilling = new widget.ScrollPane();
        panelGlass19 = new widget.panelisi();
        jLabel28 = new widget.Label();
        jLabel29 = new widget.Label();
        pnb = new widget.TextBoxW11UI();
        ahli = new widget.TextBoxW11UI();
        jLabel30 = new widget.Label();
        rad = new widget.TextBoxW11UI();
        jLabel31 = new widget.Label();
        rehab = new widget.TextBoxW11UI();
        jLabel32 = new widget.Label();
        obat = new widget.TextBoxW11UI();
        jLabel70 = new widget.Label();
        alkes = new widget.TextBoxW11UI();
        jLabel33 = new widget.Label();
        pb = new widget.TextBoxW11UI();
        jLabel34 = new widget.Label();
        kpr = new widget.TextBoxW11UI();
        jLabel35 = new widget.Label();
        lab = new widget.TextBoxW11UI();
        jLabel36 = new widget.Label();
        akomodasi = new widget.TextBoxW11UI();
        jLabel37 = new widget.Label();
        kronis = new widget.TextBoxW11UI();
        jLabel27 = new widget.Label();
        bmhp = new widget.TextBoxW11UI();
        jLabel42 = new widget.Label();
        ksl = new widget.TextBoxW11UI();
        pnj = new widget.TextBoxW11UI();
        sewa_alat = new widget.TextBoxW11UI();
        jLabel40 = new widget.Label();
        jLabel38 = new widget.Label();
        ri = new widget.TextBoxW11UI();
        jLabel50 = new widget.Label();
        jLabel41 = new widget.Label();
        pl_darah = new widget.TextBoxW11UI();
        obat_kemo = new widget.TextBoxW11UI();
        jLabel39 = new widget.Label();
        pnb1 = new widget.TextBoxW11UI();
        TabRawat = new javax.swing.JTabbedPane();
        Scroll2 = new widget.ScrollPane();
        panelGlass11 = new widget.panelisi();
        panelGlass13 = new widget.panelisi();
        scrollCombo = new widget.ScrollPane();
        tbCombo = new widget.Table();
        textDiag = new widget.TextBoxW11UI();
        jLabel7 = new widget.Label();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new widget.Label();
        textProc = new widget.TextBoxW11UI();
        scrollCombo2 = new widget.ScrollPane();
        tbCombo2 = new widget.Table();
        simpanDxPxIDRG = new javax.swing.JButton();
        panelGlass14 = new widget.panelisi();
        scrollRespon = new widget.ScrollPane();
        panelGlass10 = new widget.panelisi();
        jLabel111 = new widget.Label();
        txStatus = new widget.Label();
        jLabel120 = new widget.Label();
        txDrg = new widget.Label();
        jSeparator30 = new javax.swing.JSeparator();
        grupCode1 = new widget.Label();
        jSeparator39 = new javax.swing.JSeparator();
        jLabel128 = new widget.Label();
        txMdc = new widget.Label();
        jLabel130 = new widget.Label();
        jSeparator40 = new javax.swing.JSeparator();
        jSeparator41 = new javax.swing.JSeparator();
        jLabel112 = new widget.Label();
        txInfo = new widget.Label();
        jSeparator33 = new javax.swing.JSeparator();
        txJenis = new widget.Label();
        txDrgCode = new widget.Label();
        txMdcCode = new widget.Label();
        scrollPane6 = new widget.ScrollPane();
        tLog = new widget.TextArea();
        Scroll3 = new widget.ScrollPane();
        panelGlass18 = new widget.panelisi();
        panelGlass20 = new widget.panelisi();
        scrollCombo4 = new widget.ScrollPane();
        tbCombo1 = new widget.Table();
        textDiag2 = new widget.TextBoxW11UI();
        jLabel16 = new widget.Label();
        jLabel18 = new widget.Label();
        textProc2 = new widget.TextBoxW11UI();
        scrollCombo5 = new widget.ScrollPane();
        tbCombo22 = new widget.Table();
        simpanDxPxInacbg = new javax.swing.JButton();
        simpanDxPxInacbg1 = new javax.swing.JButton();
        panelGlass21 = new widget.panelisi();
        scrollPane7 = new widget.ScrollPane();
        tLog1 = new widget.TextArea();
        scrollRespon1 = new widget.ScrollPane();
        panelGlass9 = new widget.panelisi();
        cmbDrugs = new widget.ComboBox();
        jLabel86 = new widget.Label();
        jLabel87 = new widget.Label();
        jLabel88 = new widget.Label();
        jLabel89 = new widget.Label();
        jLabel90 = new widget.Label();
        jLabel91 = new widget.Label();
        jLabel92 = new widget.Label();
        jLabel93 = new widget.Label();
        jLabel94 = new widget.Label();
        lStatus = new widget.Label();
        lChronic = new widget.Label();
        jLabel97 = new widget.Label();
        jLabel99 = new widget.Label();
        cmbProc = new widget.ComboBox();
        cmbPros = new widget.ComboBox();
        cmbInv = new widget.ComboBox();
        lJenis = new widget.Label();
        lSubacute = new widget.Label();
        jLabel103 = new widget.Label();
        jLabel104 = new widget.Label();
        jLabel105 = new widget.Label();
        jLabel106 = new widget.Label();
        jLabel107 = new widget.Label();
        jLabel108 = new widget.Label();
        jLabel110 = new widget.Label();
        lblTarifKronis = new widget.Label();
        lblTarifGrup = new widget.Label();
        lblTarifAcute = new widget.Label();
        lbltarifProc = new widget.Label();
        lblTarifPros = new widget.Label();
        lblTarifDrugs = new widget.Label();
        lblTarifInv = new widget.Label();
        lblTarifTotal = new widget.Label();
        jSeparator16 = new javax.swing.JSeparator();
        jSeparator17 = new javax.swing.JSeparator();
        jSeparator18 = new javax.swing.JSeparator();
        jSeparator19 = new javax.swing.JSeparator();
        jSeparator20 = new javax.swing.JSeparator();
        jSeparator21 = new javax.swing.JSeparator();
        jSeparator22 = new javax.swing.JSeparator();
        jSeparator23 = new javax.swing.JSeparator();
        jSeparator24 = new javax.swing.JSeparator();
        jSeparator25 = new javax.swing.JSeparator();
        grupCode = new widget.Label();
        jSeparator26 = new javax.swing.JSeparator();
        jLabel95 = new widget.Label();
        lJenis1 = new widget.Label();
        jLabel98 = new widget.Label();
        lblTarifRS = new widget.Label();
        jLabel96 = new widget.Label();
        jLabel100 = new widget.Label();
        lblBaseTariff = new widget.Label();
        jSeparator27 = new javax.swing.JSeparator();
        scrollPane1 = new widget.ScrollPane();
        Txtgroup = new widget.TextArea();
        jLabel101 = new widget.Label();
        jLabel109 = new widget.Label();
        lblSelisih = new widget.Label();
        jSeparator28 = new javax.swing.JSeparator();
        internalFrame2 = new widget.InternalFrame();
        jPanel3 = new javax.swing.JPanel();
        panelGlass15 = new widget.panelisi();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        jLabel11 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar1 = new widget.Button();
        panelGlass16 = new widget.panelisi();
        jLabel21 = new widget.Label();
        CmbTanggalSEP = new widget.ComboBox();
        DTPCari1 = new widget.Tanggal();
        jLabel46 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel14 = new widget.Label();
        CmbStatusRawat = new widget.ComboBox();
        jLabel74 = new widget.Label();
        CmbStatusKirim = new widget.ComboBox();
        lblCoderNIK = new widget.Label();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        panelGlass17 = new widget.panelisi();
        Scroll = new widget.ScrollPane();
        tbKompilasi = new widget.Table();

        no_rawat.setHighlighter(null);
        no_rawat.setName("no_rawat"); // NOI18N

        icd10.setHighlighter(null);
        icd10.setName("icd10"); // NOI18N

        icd9.setHighlighter(null);
        icd9.setName("icd9"); // NOI18N

        Cmgs.setHighlighter(null);
        Cmgs.setName("Cmgs"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        tabPane1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        tabPane1.setName("tabPane1"); // NOI18N

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Indonesian DRG  ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Fira Sans", 0, 13), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setPreferredSize(new java.awt.Dimension(1300, 900));
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput1.setName("FormInput1"); // NOI18N
        FormInput1.setPreferredSize(new java.awt.Dimension(865, 44));
        FormInput1.setLayout(null);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Nama Pasien");
        jLabel3.setName("jLabel3"); // NOI18N
        FormInput1.add(jLabel3);
        jLabel3.setBounds(600, 0, 330, 20);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Status :");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput1.add(jLabel17);
        jLabel17.setBounds(1160, 0, 110, 23);

        Status.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ralan", "Ranap" }));
        Status.setName("Status"); // NOI18N
        Status.setPreferredSize(new java.awt.Dimension(308, 23));
        FormInput1.add(Status);
        Status.setBounds(1160, 20, 108, 23);

        TNoRw.setEditable(false);
        TNoRw.setName("TNoRw"); // NOI18N
        FormInput1.add(TNoRw);
        TNoRw.setBounds(10, 20, 160, 23);

        TNoSEP.setEditable(false);
        TNoSEP.setName("TNoSEP"); // NOI18N
        FormInput1.add(TNoSEP);
        TNoSEP.setBounds(430, 20, 150, 23);

        TPasien.setEditable(false);
        TPasien.setName("TPasien"); // NOI18N
        FormInput1.add(TPasien);
        TPasien.setBounds(600, 20, 330, 23);

        TNoRM.setEditable(false);
        TNoRM.setName("TNoRM"); // NOI18N
        FormInput1.add(TNoRM);
        TNoRM.setBounds(180, 20, 80, 23);

        Tnoka.setEditable(false);
        Tnoka.setName("Tnoka"); // NOI18N
        FormInput1.add(Tnoka);
        Tnoka.setBounds(270, 20, 150, 23);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("No.Rawat");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput1.add(jLabel4);
        jLabel4.setBounds(10, 0, 160, 20);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("No.RM");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput1.add(jLabel5);
        jLabel5.setBounds(180, 0, 80, 20);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Noka BPJS");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput1.add(jLabel9);
        jLabel9.setBounds(270, 0, 160, 20);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("No.SEP");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput1.add(jLabel10);
        jLabel10.setBounds(430, 0, 150, 20);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Jaminan : ");
        jLabel24.setToolTipText("");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput1.add(jLabel24);
        jLabel24.setBounds(940, 0, 200, 23);

        jaminan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3 : JKN ", "71 : JAMINAN COVID-19", "72 : JAMINAN KIPI", "73 : JAMINAN BAYI BARU LAHIR", "74 : JAMINAN PERPANJANGAN MASA RAWAT", "75 : JAMINAN CO-INSIDENSE", "76 : JAMPERSAL", "77 : JAMINAN PEMULIHAN KESEHATAN PRIORITAS" }));
        jaminan.setName("jaminan"); // NOI18N
        FormInput1.add(jaminan);
        jaminan.setBounds(940, 20, 200, 23);

        internalFrame1.add(FormInput1, java.awt.BorderLayout.PAGE_START);

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 54));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        BtnGrouperIDRG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/users.png"))); // NOI18N
        BtnGrouperIDRG.setMnemonic('B');
        BtnGrouperIDRG.setText("Grouping IDRG");
        BtnGrouperIDRG.setToolTipText("Alt+B");
        BtnGrouperIDRG.setName("BtnGrouperIDRG"); // NOI18N
        BtnGrouperIDRG.setPreferredSize(new java.awt.Dimension(150, 30));
        BtnGrouperIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGrouperIDRGActionPerformed(evt);
            }
        });
        BtnGrouperIDRG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnGrouperIDRGKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnGrouperIDRG);

        finalklaimIDRG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        finalklaimIDRG.setText("Final IDRG");
        finalklaimIDRG.setToolTipText("Alt+S");
        finalklaimIDRG.setName("finalklaimIDRG"); // NOI18N
        finalklaimIDRG.setPreferredSize(new java.awt.Dimension(120, 30));
        finalklaimIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalklaimIDRGActionPerformed(evt);
            }
        });
        panelGlass8.add(finalklaimIDRG);

        editKlaimIDRG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/EDIT2.png"))); // NOI18N
        editKlaimIDRG.setText("Edit IDRG");
        editKlaimIDRG.setToolTipText("Alt+S");
        editKlaimIDRG.setName("editKlaimIDRG"); // NOI18N
        editKlaimIDRG.setPreferredSize(new java.awt.Dimension(120, 30));
        editKlaimIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editKlaimIDRGActionPerformed(evt);
            }
        });
        panelGlass8.add(editKlaimIDRG);

        hapusKlaim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/delete_folder.png"))); // NOI18N
        hapusKlaim.setMnemonic('K');
        hapusKlaim.setText("Hapus Claim");
        hapusKlaim.setToolTipText("Alt+K");
        hapusKlaim.setName("hapusKlaim"); // NOI18N
        hapusKlaim.setPreferredSize(new java.awt.Dimension(130, 30));
        hapusKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusKlaimActionPerformed(evt);
            }
        });
        hapusKlaim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hapusKlaimKeyPressed(evt);
            }
        });
        panelGlass8.add(hapusKlaim);

        BtnGrouper.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/users.png"))); // NOI18N
        BtnGrouper.setMnemonic('B');
        BtnGrouper.setText("Grouping");
        BtnGrouper.setToolTipText("Alt+B");
        BtnGrouper.setName("BtnGrouper"); // NOI18N
        BtnGrouper.setPreferredSize(new java.awt.Dimension(110, 30));
        BtnGrouper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGrouperActionPerformed(evt);
            }
        });
        BtnGrouper.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnGrouperKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnGrouper);

        finalklaim1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        finalklaim1.setText("Final INACBG");
        finalklaim1.setToolTipText("Alt+S");
        finalklaim1.setName("finalklaim1"); // NOI18N
        finalklaim1.setPreferredSize(new java.awt.Dimension(120, 30));
        finalklaim1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalklaim1ActionPerformed(evt);
            }
        });
        panelGlass8.add(finalklaim1);

        editKlaim1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/EDIT2.png"))); // NOI18N
        editKlaim1.setText("Edit INACBG");
        editKlaim1.setToolTipText("Alt+S");
        editKlaim1.setName("editKlaim1"); // NOI18N
        editKlaim1.setPreferredSize(new java.awt.Dimension(120, 30));
        editKlaim1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editKlaim1ActionPerformed(evt);
            }
        });
        panelGlass8.add(editKlaim1);

        finalklaim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        finalklaim.setText("Final Klaim");
        finalklaim.setToolTipText("Alt+S");
        finalklaim.setName("finalklaim"); // NOI18N
        finalklaim.setPreferredSize(new java.awt.Dimension(120, 30));
        finalklaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalklaimActionPerformed(evt);
            }
        });
        panelGlass8.add(finalklaim);

        editKlaim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/EDIT2.png"))); // NOI18N
        editKlaim.setText("Edit Klaim");
        editKlaim.setToolTipText("Alt+S");
        editKlaim.setName("editKlaim"); // NOI18N
        editKlaim.setPreferredSize(new java.awt.Dimension(120, 30));
        editKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editKlaimActionPerformed(evt);
            }
        });
        panelGlass8.add(editKlaim);

        kirimonline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/2.png"))); // NOI18N
        kirimonline.setText("Kirim Online");
        kirimonline.setToolTipText("Alt+S");
        kirimonline.setName("kirimonline"); // NOI18N
        kirimonline.setPreferredSize(new java.awt.Dimension(120, 30));
        kirimonline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kirimonlineActionPerformed(evt);
            }
        });
        panelGlass8.add(kirimonline);

        jLabel25.setName("jLabel25"); // NOI18N
        jLabel25.setPreferredSize(new java.awt.Dimension(150, 14));
        panelGlass8.add(jLabel25);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        internalFrame1.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(1300, 900));
        scrollInput.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                scrollInputPropertyChange(evt);
            }
        });

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(1300, 900));
        FormInput.setLayout(null);

        tgl_masuk.setEditable(false);
        tgl_masuk.setForeground(new java.awt.Color(50, 70, 50));
        tgl_masuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2025-10-07" }));
        tgl_masuk.setDisplayFormat("yyyy-MM-dd");
        tgl_masuk.setName("tgl_masuk"); // NOI18N
        tgl_masuk.setOpaque(false);
        FormInput.add(tgl_masuk);
        tgl_masuk.setBounds(90, 40, 110, 23);

        jLabel12.setText("Distole : ");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(640, 130, 50, 23);

        jLabel15.setText("Intubasi :");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput.add(jLabel15);
        jLabel15.setBounds(80, 280, 70, 23);

        cara_masuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "gp : Rujukan FKTP", "hosp-trans : Rujukan FKRTL", "mp : Rujukan Spesialis", "outp : Dari Rawat Jalan", "inp : Dari Rawat Inap", "emd : Dari Rawat Darurat", "born : Lahir di RS", "nursing : Rujukan Panti Jompo", "psych : Rujukan dari RS Jiwa", "rehab : Rujukan Fasilitas Rehab", "other : Lain-lain" }));
        cara_masuk.setName("cara_masuk"); // NOI18N
        FormInput.add(cara_masuk);
        cara_masuk.setBounds(90, 70, 250, 23);

        sistole.setHighlighter(null);
        sistole.setName("sistole"); // NOI18N
        FormInput.add(sistole);
        sistole.setBounds(540, 130, 60, 23);

        jLabel22.setText("Pasien TB : ");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(20, 160, 70, 23);

        jenis_rawat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1 : RAWAT INAP", "2 : RAWAT JALAN", "3 : IGD" }));
        jenis_rawat.setSelectedIndex(1);
        jenis_rawat.setName("jenis_rawat"); // NOI18N
        jenis_rawat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jenis_rawatItemStateChanged(evt);
            }
        });
        FormInput.add(jenis_rawat);
        jenis_rawat.setBounds(90, 10, 130, 23);

        cob.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "# : TIDAK COB", "0001 : MANDIRI INHEALTH", "0005 : ASURANSI SINAR MAS", "0006 : ASURANSI TUGU MANDIRI", "0007 : ASURANSI MITRA MAPARYA", "0008 : ASURANSI AXA MANDIRI FINANSIAL SERVICE", "0009 : ASURANSI AXA FINANSIAL INDONESIA", "0010 : LIPPO GENERAL INSURANCE", "0011 : ARTHAGRAHA GENERAL INSURANSE", "0012 : TUGU PRATAMA INDONESIA", "0013 : ASURANSI BINA DANA ARTA", "0014 : ASURANSI JIWA SINAR MAS MSIG", "0015 : AVRIST ASSURANCE", "0016 : ASURANSI JIWA SRAYA", "0017 : ASURANSI JIWA CENTRAL ASIA RAYA", "0018 : ASURANSI TAKAFUL KELUARGA", "0019 : ASURANSI JIWA GENERALI INDONESIA", "0020 : ASURANSI ASTRA BUANA", "0021 : ASURANSI UMUM MEGA", "0022 : ASURANSI MULTI ARTHA GUNA", "0023 : ASURANSI AIA INDONESIA", "0024 : ASURANSI JIWA EQUITY LIFE INDONESIA", "0025 : ASURANSI JIWA RECAPITAL", "0026 : GREAT EASTERN LIFE INDONESIA", "0027 : ASURANSI ADISARANA WANAARTHA", "0028 : ASURANSI JIWA BRINGIN JIWA SEJAHTERA", "0029 : BOSOWA ASURANSI", "0030 : MNC LIFE ASSURANCE", "0031 : ASURANSI AVIVA INDONESIA", "0032 : ASURANSI CENTRAL ASIA RAYA", "0033 : ASURANSI ALLIANZ LIFE INDONESIA", "0034 : ASURANSI BINTANG", "0035 : TOKIO MARINE LIFE INSURANCE INDONESIA", "0036 : MALACCA TRUST WUWUNGAN", "0037 : ASURANSI JASA INDONESIA", "0038 : ASURANSI JIWA MANULIFE INDONESIA", "0039 : ASURANSI BANGUN ASKRIDA", "0040 : ASURANSI JIWA SEQUIS FINANCIAL", "0041 : ASURANSI AXA INDONESIA", "0042 : BNI LIFE", "0043 : ACE LIFE INSURANCE", "0044 : CITRA INTERNATIONAL UNDERWRITERS", "0045 : ASURANSI RELIANCE INDONESIA", "0046 : HANWHA LIFE INSURANCE INDONESIA", "0047 : ASURANSI DAYIN MITRA", "0048 : ASURANSI ADIRA DINAMIKA", "0049 : PAN PASIFIC INSURANCE", "0050 : ASURANSI SAMSUNG TUGU", "0051 : ASURANSI UMUM BUMI PUTERA MUDA 1967", "0052 : ASURANSI JIWA KRESNA", "0053 : ASURANSI RAMAYANA", "0054 : VICTORIA INSURANCE", "0055 : ASURANSI JIWA BERSAMA BUMIPUTERA 1912", "0056 : FWD LIFE INDONESIA", "0057 : ASURANSI TAKAFUL KELUARGA", "0058 : ASURANSI TUGU KRESNA PRATAMA", "0059 : SOMPO INSURANCE" }));
        cob.setName("cob"); // NOI18N
        FormInput.add(cob);
        cob.setBounds(90, 190, 270, 23);

        jLabel19.setText("Tgl Lahir : ");
        jLabel19.setName("jLabel19"); // NOI18N
        FormInput.add(jLabel19);
        jLabel19.setBounds(410, 160, 80, 23);

        jLabel20.setText("COB : ");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(50, 190, 40, 23);

        jLabel23.setText("Tgl Masuk : ");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(10, 40, 80, 23);

        hak_kelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kelas 1", "Kelas 2", "Kelas 3" }));
        hak_kelas.setSelectedIndex(2);
        hak_kelas.setName("hak_kelas"); // NOI18N
        FormInput.add(hak_kelas);
        hak_kelas.setBounds(300, 10, 100, 23);

        jLabel26.setText("Hemodialisa : ");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(410, 70, 80, 23);

        jLabel43.setText("Jenis Rawat : ");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput.add(jLabel43);
        jLabel43.setBounds(10, 10, 80, 23);

        jam_masuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jam_masuk.setName("jam_masuk"); // NOI18N
        FormInput.add(jam_masuk);
        jam_masuk.setBounds(210, 40, 62, 23);

        menit_masuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        menit_masuk.setName("menit_masuk"); // NOI18N
        FormInput.add(menit_masuk);
        menit_masuk.setBounds(280, 40, 62, 23);

        tgl_keluar.setEditable(false);
        tgl_keluar.setForeground(new java.awt.Color(50, 70, 50));
        tgl_keluar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2025-10-07" }));
        tgl_keluar.setDisplayFormat("yyyy-MM-dd");
        tgl_keluar.setName("tgl_keluar"); // NOI18N
        tgl_keluar.setOpaque(false);
        FormInput.add(tgl_keluar);
        tgl_keluar.setBounds(490, 40, 100, 23);

        jLabel44.setText("Poli Eksekutif : ");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(410, 190, 80, 23);

        jam_keluar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jam_keluar.setName("jam_keluar"); // NOI18N
        FormInput.add(jam_keluar);
        jam_keluar.setBounds(600, 40, 62, 23);

        menit_keluar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        menit_keluar.setName("menit_keluar"); // NOI18N
        FormInput.add(menit_keluar);
        menit_keluar.setBounds(670, 40, 62, 23);

        rawat_intensif.setBackground(new java.awt.Color(255, 255, 255));
        rawat_intensif.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        rawat_intensif.setText("Ada Rawat Intensif");
        rawat_intensif.setName("rawat_intensif"); // NOI18N
        rawat_intensif.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rawat_intensifItemStateChanged(evt);
            }
        });
        FormInput.add(rawat_intensif);
        rawat_intensif.setBounds(90, 220, 140, 22);

        jLabel45.setText("ICU : ");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput.add(jLabel45);
        jLabel45.setBounds(10, 220, 80, 23);

        ventilator.setBackground(new java.awt.Color(255, 255, 255));
        ventilator.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        ventilator.setText("Ventilator");
        ventilator.setName("ventilator"); // NOI18N
        ventilator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ventilatorItemStateChanged(evt);
            }
        });
        ventilator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ventilatorActionPerformed(evt);
            }
        });
        FormInput.add(ventilator);
        ventilator.setBounds(90, 250, 90, 22);

        naik_turun_kelas.setBackground(new java.awt.Color(255, 255, 255));
        naik_turun_kelas.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        naik_turun_kelas.setText("Naik / Turun Kelas");
        naik_turun_kelas.setName("naik_turun_kelas"); // NOI18N
        naik_turun_kelas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                naik_turun_kelasItemStateChanged(evt);
            }
        });
        FormInput.add(naik_turun_kelas);
        naik_turun_kelas.setBounds(410, 10, 130, 22);

        naik_kelas_pelayanan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "kelas_3 : Kelas 3", "kelas_2 : Kelas 2", "kelas_1 : Kelas 1", "vip : Diatas Kelas 1" }));
        naik_kelas_pelayanan.setName("naik_kelas_pelayanan"); // NOI18N
        FormInput.add(naik_kelas_pelayanan);
        naik_kelas_pelayanan.setBounds(710, 10, 123, 23);

        jLabel47.setText("Kelas : ");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(660, 10, 50, 23);

        lama_naik_kelas.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        lama_naik_kelas.setHighlighter(null);
        lama_naik_kelas.setName("lama_naik_kelas"); // NOI18N
        FormInput.add(lama_naik_kelas);
        lama_naik_kelas.setBounds(610, 10, 50, 23);

        jLabel48.setText("Lama (hari) : ");
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput.add(jLabel48);
        jLabel48.setBounds(540, 10, 70, 23);

        jLabel51.setText("Chronic : ");
        jLabel51.setName("jLabel51"); // NOI18N
        FormInput.add(jLabel51);
        jLabel51.setBounds(190, 100, 90, 23);

        jLabel52.setText("Gram");
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput.add(jLabel52);
        jLabel52.setBounds(770, 160, 30, 23);

        chronic.setText("-");
        chronic.setHighlighter(null);
        chronic.setName("chronic"); // NOI18N
        FormInput.add(chronic);
        chronic.setBounds(280, 100, 60, 23);

        sub_acute.setText("-");
        sub_acute.setHighlighter(null);
        sub_acute.setName("sub_acute"); // NOI18N
        FormInput.add(sub_acute);
        sub_acute.setBounds(150, 100, 60, 23);

        jLabel54.setText("Sub Acute : ");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(60, 100, 90, 23);

        berat_bayi.setHighlighter(null);
        berat_bayi.setName("berat_bayi"); // NOI18N
        FormInput.add(berat_bayi);
        berat_bayi.setBounds(690, 160, 80, 23);

        jLabel55.setText("Berat Lahir Bayi : ");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(600, 160, 90, 23);

        dpjp.setHighlighter(null);
        dpjp.setName("dpjp"); // NOI18N
        FormInput.add(dpjp);
        dpjp.setBounds(90, 130, 290, 23);

        jLabel57.setText("DPJP : ");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(20, 130, 70, 23);

        validasi_tb.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        validasi_tb.setText("Validasi");
        validasi_tb.setToolTipText("");
        validasi_tb.setName("validasi_tb"); // NOI18N
        validasi_tb.setPreferredSize(new java.awt.Dimension(100, 30));
        validasi_tb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validasi_tbActionPerformed(evt);
            }
        });
        FormInput.add(validasi_tb);
        validasi_tb.setBounds(280, 160, 70, 22);

        status_pulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1 : Atas persetujuan dokter", "2 : Dirujuk", "3 : Atas permintaan sendiri", "4 : Meninggal", "5 : Lain-lain" }));
        status_pulang.setName("status_pulang"); // NOI18N
        FormInput.add(status_pulang);
        status_pulang.setBounds(490, 100, 170, 23);

        jLabel59.setText("T/ Darah : ");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput.add(jLabel59);
        jLabel59.setBounds(410, 130, 80, 23);

        kelas_eksekutif.setBackground(new java.awt.Color(255, 255, 255));
        kelas_eksekutif.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        kelas_eksekutif.setText("Kelas Eksekutif");
        kelas_eksekutif.setName("kelas_eksekutif"); // NOI18N
        kelas_eksekutif.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                kelas_eksekutifItemStateChanged(evt);
            }
        });
        FormInput.add(kelas_eksekutif);
        kelas_eksekutif.setBounds(490, 190, 120, 22);

        tgl_intubasi.setEditable(false);
        tgl_intubasi.setForeground(new java.awt.Color(50, 70, 50));
        tgl_intubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2025-10-07" }));
        tgl_intubasi.setDisplayFormat("yyyy-MM-dd");
        tgl_intubasi.setName("tgl_intubasi"); // NOI18N
        tgl_intubasi.setOpaque(false);
        FormInput.add(tgl_intubasi);
        tgl_intubasi.setBounds(160, 280, 100, 23);

        jam_intubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jam_intubasi.setName("jam_intubasi"); // NOI18N
        FormInput.add(jam_intubasi);
        jam_intubasi.setBounds(270, 280, 62, 23);

        menit_intubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        menit_intubasi.setName("menit_intubasi"); // NOI18N
        FormInput.add(menit_intubasi);
        menit_intubasi.setBounds(330, 280, 62, 23);

        total_rupiah.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        total_rupiah.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        total_rupiah.setName("total_rupiah"); // NOI18N
        FormInput.add(total_rupiah);
        total_rupiah.setBounds(2220, 300, 360, 50);

        menit_ekstubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        menit_ekstubasi.setName("menit_ekstubasi"); // NOI18N
        FormInput.add(menit_ekstubasi);
        menit_ekstubasi.setBounds(330, 310, 62, 23);

        jam_ekstubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jam_ekstubasi.setName("jam_ekstubasi"); // NOI18N
        FormInput.add(jam_ekstubasi);
        jam_ekstubasi.setBounds(270, 310, 62, 23);

        tgl_ekstubasi.setEditable(false);
        tgl_ekstubasi.setForeground(new java.awt.Color(50, 70, 50));
        tgl_ekstubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2025-10-07" }));
        tgl_ekstubasi.setDisplayFormat("yyyy-MM-dd");
        tgl_ekstubasi.setName("tgl_ekstubasi"); // NOI18N
        tgl_ekstubasi.setOpaque(false);
        FormInput.add(tgl_ekstubasi);
        tgl_ekstubasi.setBounds(160, 310, 100, 23);

        jLabel62.setText("Ekstubasi :");
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput.add(jLabel62);
        jLabel62.setBounds(80, 310, 70, 23);

        hari_intensif.setEditable(false);
        hari_intensif.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        hari_intensif.setHighlighter(null);
        hari_intensif.setName("hari_intensif"); // NOI18N
        FormInput.add(hari_intensif);
        hari_intensif.setBounds(350, 220, 50, 23);

        jLabel63.setText(" Rawat Intensif (hari) : ");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput.add(jLabel63);
        jLabel63.setBounds(230, 220, 120, 23);

        jLabel66.setText("Check Diagnosa Utama");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(260, 990, 140, 23);

        jLabel67.setText("Sistole : ");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput.add(jLabel67);
        jLabel67.setBounds(490, 130, 50, 23);

        distole.setHighlighter(null);
        distole.setName("distole"); // NOI18N
        FormInput.add(distole);
        distole.setBounds(690, 130, 60, 23);

        tarif_eksekutif.setHighlighter(null);
        tarif_eksekutif.setName("tarif_eksekutif"); // NOI18N
        FormInput.add(tarif_eksekutif);
        tarif_eksekutif.setBounds(690, 190, 100, 23);

        jLabel13.setText("Tarif Eksekutif : ");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(600, 190, 90, 23);

        tgl_lahir.setEditable(false);
        tgl_lahir.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        tgl_lahir.setEnabled(false);
        tgl_lahir.setHighlighter(null);
        tgl_lahir.setName("tgl_lahir"); // NOI18N
        FormInput.add(tgl_lahir);
        tgl_lahir.setBounds(490, 160, 80, 23);

        jLabel60.setText("Gender : ");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(410, 220, 80, 23);

        jenis_kelamin.setEditable(false);
        jenis_kelamin.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jenis_kelamin.setEnabled(false);
        jenis_kelamin.setHighlighter(null);
        jenis_kelamin.setName("jenis_kelamin"); // NOI18N
        FormInput.add(jenis_kelamin);
        jenis_kelamin.setBounds(490, 220, 50, 23);

        jLabel64.setText("Cara Masuk : ");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput.add(jLabel64);
        jLabel64.setBounds(10, 70, 80, 23);

        jam_ventilator.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jam_ventilator.setHighlighter(null);
        jam_ventilator.setName("jam_ventilator"); // NOI18N
        FormInput.add(jam_ventilator);
        jam_ventilator.setBounds(270, 250, 60, 23);

        jLabel65.setText("Berapa Jam  : ");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput.add(jLabel65);
        jLabel65.setBounds(190, 250, 80, 23);

        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel68.setText(" TARIF RUMAH SAKIT : ");
        jLabel68.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput.add(jLabel68);
        jLabel68.setBounds(2170, 290, 130, 23);

        jLabel69.setText("RP .");
        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput.add(jLabel69);
        jLabel69.setBounds(2130, 300, 80, 50);

        nm_diag_1.setEditable(false);
        nm_diag_1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_1.setEnabled(false);
        nm_diag_1.setHighlighter(null);
        nm_diag_1.setName("nm_diag_1"); // NOI18N
        FormInput.add(nm_diag_1);
        nm_diag_1.setBounds(130, 1010, 240, 23);

        jLabel71.setText("DIAGNOSA PASIEN");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput.add(jLabel71);
        jLabel71.setBounds(30, 990, 115, 23);

        kd_diag_1.setEditable(false);
        kd_diag_1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_1.setEnabled(false);
        kd_diag_1.setHighlighter(null);
        kd_diag_1.setName("kd_diag_1"); // NOI18N
        FormInput.add(kd_diag_1);
        kd_diag_1.setBounds(50, 1010, 70, 23);

        nm_diag_2.setEditable(false);
        nm_diag_2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_2.setEnabled(false);
        nm_diag_2.setHighlighter(null);
        nm_diag_2.setName("nm_diag_2"); // NOI18N
        FormInput.add(nm_diag_2);
        nm_diag_2.setBounds(130, 1040, 240, 23);

        kd_diag_2.setEditable(false);
        kd_diag_2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_2.setEnabled(false);
        kd_diag_2.setHighlighter(null);
        kd_diag_2.setName("kd_diag_2"); // NOI18N
        FormInput.add(kd_diag_2);
        kd_diag_2.setBounds(50, 1040, 70, 23);

        nm_diag_3.setEditable(false);
        nm_diag_3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_3.setEnabled(false);
        nm_diag_3.setHighlighter(null);
        nm_diag_3.setName("nm_diag_3"); // NOI18N
        FormInput.add(nm_diag_3);
        nm_diag_3.setBounds(130, 1070, 240, 23);

        kd_diag_3.setEditable(false);
        kd_diag_3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_3.setEnabled(false);
        kd_diag_3.setHighlighter(null);
        kd_diag_3.setName("kd_diag_3"); // NOI18N
        FormInput.add(kd_diag_3);
        kd_diag_3.setBounds(50, 1070, 70, 23);

        nm_diag_4.setEditable(false);
        nm_diag_4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_4.setEnabled(false);
        nm_diag_4.setHighlighter(null);
        nm_diag_4.setName("nm_diag_4"); // NOI18N
        FormInput.add(nm_diag_4);
        nm_diag_4.setBounds(130, 1100, 240, 23);

        kd_diag_4.setEditable(false);
        kd_diag_4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_4.setEnabled(false);
        kd_diag_4.setHighlighter(null);
        kd_diag_4.setName("kd_diag_4"); // NOI18N
        FormInput.add(kd_diag_4);
        kd_diag_4.setBounds(50, 1100, 70, 23);

        nm_diag_5.setEditable(false);
        nm_diag_5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_5.setEnabled(false);
        nm_diag_5.setHighlighter(null);
        nm_diag_5.setName("nm_diag_5"); // NOI18N
        FormInput.add(nm_diag_5);
        nm_diag_5.setBounds(130, 1130, 240, 23);

        kd_diag_5.setEditable(false);
        kd_diag_5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_5.setEnabled(false);
        kd_diag_5.setHighlighter(null);
        kd_diag_5.setName("kd_diag_5"); // NOI18N
        FormInput.add(kd_diag_5);
        kd_diag_5.setBounds(50, 1130, 70, 23);

        nm_psdr_1.setEditable(false);
        nm_psdr_1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_1.setEnabled(false);
        nm_psdr_1.setHighlighter(null);
        nm_psdr_1.setName("nm_psdr_1"); // NOI18N
        FormInput.add(nm_psdr_1);
        nm_psdr_1.setBounds(490, 1010, 250, 23);

        kd_psdr_1.setEditable(false);
        kd_psdr_1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_1.setEnabled(false);
        kd_psdr_1.setHighlighter(null);
        kd_psdr_1.setName("kd_psdr_1"); // NOI18N
        FormInput.add(kd_psdr_1);
        kd_psdr_1.setBounds(410, 1010, 70, 23);

        nm_psdr_2.setEditable(false);
        nm_psdr_2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_2.setEnabled(false);
        nm_psdr_2.setHighlighter(null);
        nm_psdr_2.setName("nm_psdr_2"); // NOI18N
        FormInput.add(nm_psdr_2);
        nm_psdr_2.setBounds(490, 1040, 250, 23);

        kd_psdr_2.setEditable(false);
        kd_psdr_2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_2.setEnabled(false);
        kd_psdr_2.setHighlighter(null);
        kd_psdr_2.setName("kd_psdr_2"); // NOI18N
        FormInput.add(kd_psdr_2);
        kd_psdr_2.setBounds(410, 1040, 70, 23);

        nm_psdr_3.setEditable(false);
        nm_psdr_3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_3.setEnabled(false);
        nm_psdr_3.setHighlighter(null);
        nm_psdr_3.setName("nm_psdr_3"); // NOI18N
        FormInput.add(nm_psdr_3);
        nm_psdr_3.setBounds(490, 1070, 250, 23);

        kd_psdr_3.setEditable(false);
        kd_psdr_3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_3.setEnabled(false);
        kd_psdr_3.setHighlighter(null);
        kd_psdr_3.setName("kd_psdr_3"); // NOI18N
        FormInput.add(kd_psdr_3);
        kd_psdr_3.setBounds(410, 1070, 70, 23);

        nm_psdr_4.setEditable(false);
        nm_psdr_4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_4.setEnabled(false);
        nm_psdr_4.setHighlighter(null);
        nm_psdr_4.setName("nm_psdr_4"); // NOI18N
        FormInput.add(nm_psdr_4);
        nm_psdr_4.setBounds(490, 1100, 250, 23);

        kd_psdr_4.setEditable(false);
        kd_psdr_4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_4.setEnabled(false);
        kd_psdr_4.setHighlighter(null);
        kd_psdr_4.setName("kd_psdr_4"); // NOI18N
        FormInput.add(kd_psdr_4);
        kd_psdr_4.setBounds(410, 1100, 70, 23);

        nm_psdr_5.setEditable(false);
        nm_psdr_5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_5.setEnabled(false);
        nm_psdr_5.setHighlighter(null);
        nm_psdr_5.setName("nm_psdr_5"); // NOI18N
        FormInput.add(nm_psdr_5);
        nm_psdr_5.setBounds(490, 1130, 250, 23);

        kd_psdr_5.setEditable(false);
        kd_psdr_5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_5.setEnabled(false);
        kd_psdr_5.setHighlighter(null);
        kd_psdr_5.setName("kd_psdr_5"); // NOI18N
        FormInput.add(kd_psdr_5);
        kd_psdr_5.setBounds(410, 1130, 70, 23);

        chk_diag_5.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_5.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_5.setName("chk_diag_5"); // NOI18N
        chk_diag_5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_5ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_5);
        chk_diag_5.setBounds(370, 1130, 30, 22);

        chk_diag_1.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_1.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_1.setName("chk_diag_1"); // NOI18N
        chk_diag_1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_1ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_1);
        chk_diag_1.setBounds(370, 1010, 30, 22);

        chk_diag_2.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_2.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_2.setName("chk_diag_2"); // NOI18N
        chk_diag_2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_2ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_2);
        chk_diag_2.setBounds(370, 1040, 30, 22);

        chk_diag_3.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_3.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_3.setName("chk_diag_3"); // NOI18N
        chk_diag_3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_3ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_3);
        chk_diag_3.setBounds(370, 1070, 30, 22);

        chk_diag_4.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_4.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_4.setName("chk_diag_4"); // NOI18N
        chk_diag_4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_4ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_4);
        chk_diag_4.setBounds(370, 1100, 30, 22);

        total_rupiah1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        total_rupiah1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        total_rupiah1.setName("total_rupiah1"); // NOI18N
        FormInput.add(total_rupiah1);
        total_rupiah1.setBounds(2220, 370, 360, 50);

        jLabel75.setText("RP .");
        jLabel75.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel75.setName("jLabel75"); // NOI18N
        FormInput.add(jLabel75);
        jLabel75.setBounds(2130, 370, 80, 50);

        jLabel76.setText("PROSEDUR PASIEN");
        jLabel76.setName("jLabel76"); // NOI18N
        FormInput.add(jLabel76);
        jLabel76.setBounds(400, 990, 115, 23);

        stage_2.setBackground(new java.awt.Color(204, 204, 255));
        stage_2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        stage_2.setName("stage_2"); // NOI18N
        FormInput.add(stage_2);
        stage_2.setBounds(1420, 270, 250, 30);

        jLabel49.setForeground(new java.awt.Color(255, 0, 0));
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("JANGAN LUPA KLIK GROUPER LAGI");
        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput.add(jLabel49);
        jLabel49.setBounds(1420, 300, 250, 23);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel53.setText("GROUPER STAGE 2");
        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(1420, 240, 250, 23);

        pasien_tb.setHighlighter(null);
        pasien_tb.setName("pasien_tb"); // NOI18N
        FormInput.add(pasien_tb);
        pasien_tb.setBounds(90, 160, 190, 23);

        batal_validasi_tb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        batal_validasi_tb.setToolTipText("");
        batal_validasi_tb.setName("batal_validasi_tb"); // NOI18N
        batal_validasi_tb.setPreferredSize(new java.awt.Dimension(100, 30));
        batal_validasi_tb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batal_validasi_tbActionPerformed(evt);
            }
        });
        FormInput.add(batal_validasi_tb);
        batal_validasi_tb.setBounds(350, 160, 40, 22);

        pasien_hd.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MULTIPLE USE", "SINGLE USE" }));
        pasien_hd.setSelectedIndex(1);
        pasien_hd.setName("pasien_hd"); // NOI18N
        FormInput.add(pasien_hd);
        pasien_hd.setBounds(620, 70, 100, 23);

        jLabel56.setText("Dializer : ");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput.add(jLabel56);
        jLabel56.setBounds(570, 70, 50, 23);

        kt_darah.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kt_darah.setHighlighter(null);
        kt_darah.setName("kt_darah"); // NOI18N
        FormInput.add(kt_darah);
        kt_darah.setBounds(810, 70, 50, 23);

        jLabel79.setText("Kantong Darah : ");
        jLabel79.setName("jLabel79"); // NOI18N
        FormInput.add(jLabel79);
        jLabel79.setBounds(720, 70, 90, 23);

        check_hd.setBackground(new java.awt.Color(255, 255, 255));
        check_hd.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        check_hd.setText("Pasien HD");
        check_hd.setName("check_hd"); // NOI18N
        check_hd.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                check_hdItemStateChanged(evt);
            }
        });
        FormInput.add(check_hd);
        check_hd.setBounds(490, 70, 90, 22);

        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel77.setText(" TARIF INACBG : ");
        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput.add(jLabel77);
        jLabel77.setBounds(2170, 360, 130, 23);

        respon.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        respon.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        respon.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        respon.setName("respon"); // NOI18N
        FormInput.add(respon);
        respon.setBounds(2180, 430, 650, 30);

        respon2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        respon2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        respon2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        respon2.setName("respon2"); // NOI18N
        FormInput.add(respon2);
        respon2.setBounds(2180, 450, 650, 30);

        nm_diag_6.setEditable(false);
        nm_diag_6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_6.setEnabled(false);
        nm_diag_6.setHighlighter(null);
        nm_diag_6.setName("nm_diag_6"); // NOI18N
        FormInput.add(nm_diag_6);
        nm_diag_6.setBounds(130, 1160, 240, 23);

        kd_diag_6.setEditable(false);
        kd_diag_6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_6.setEnabled(false);
        kd_diag_6.setHighlighter(null);
        kd_diag_6.setName("kd_diag_6"); // NOI18N
        FormInput.add(kd_diag_6);
        kd_diag_6.setBounds(50, 1160, 70, 23);

        nm_diag_7.setEditable(false);
        nm_diag_7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_7.setEnabled(false);
        nm_diag_7.setHighlighter(null);
        nm_diag_7.setName("nm_diag_7"); // NOI18N
        FormInput.add(nm_diag_7);
        nm_diag_7.setBounds(130, 1190, 240, 23);

        kd_diag_7.setEditable(false);
        kd_diag_7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_7.setEnabled(false);
        kd_diag_7.setHighlighter(null);
        kd_diag_7.setName("kd_diag_7"); // NOI18N
        FormInput.add(kd_diag_7);
        kd_diag_7.setBounds(50, 1190, 70, 23);

        nm_diag_8.setEditable(false);
        nm_diag_8.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_8.setEnabled(false);
        nm_diag_8.setHighlighter(null);
        nm_diag_8.setName("nm_diag_8"); // NOI18N
        FormInput.add(nm_diag_8);
        nm_diag_8.setBounds(130, 1220, 240, 23);

        kd_diag_8.setEditable(false);
        kd_diag_8.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_8.setEnabled(false);
        kd_diag_8.setHighlighter(null);
        kd_diag_8.setName("kd_diag_8"); // NOI18N
        FormInput.add(kd_diag_8);
        kd_diag_8.setBounds(50, 1220, 70, 23);

        nm_diag_9.setEditable(false);
        nm_diag_9.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_9.setEnabled(false);
        nm_diag_9.setHighlighter(null);
        nm_diag_9.setName("nm_diag_9"); // NOI18N
        FormInput.add(nm_diag_9);
        nm_diag_9.setBounds(130, 1250, 240, 23);

        kd_diag_9.setEditable(false);
        kd_diag_9.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_9.setEnabled(false);
        kd_diag_9.setHighlighter(null);
        kd_diag_9.setName("kd_diag_9"); // NOI18N
        FormInput.add(kd_diag_9);
        kd_diag_9.setBounds(50, 1250, 70, 23);

        nm_diag_10.setEditable(false);
        nm_diag_10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_10.setEnabled(false);
        nm_diag_10.setHighlighter(null);
        nm_diag_10.setName("nm_diag_10"); // NOI18N
        FormInput.add(nm_diag_10);
        nm_diag_10.setBounds(130, 1280, 240, 23);

        kd_diag_10.setEditable(false);
        kd_diag_10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_10.setEnabled(false);
        kd_diag_10.setHighlighter(null);
        kd_diag_10.setName("kd_diag_10"); // NOI18N
        FormInput.add(kd_diag_10);
        kd_diag_10.setBounds(50, 1280, 70, 23);

        chk_diag_6.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_6.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_6.setName("chk_diag_6"); // NOI18N
        chk_diag_6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_6ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_6);
        chk_diag_6.setBounds(370, 1160, 30, 22);

        chk_diag_7.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_7.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_7.setName("chk_diag_7"); // NOI18N
        chk_diag_7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_7ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_7);
        chk_diag_7.setBounds(370, 1190, 30, 22);

        chk_diag_8.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_8.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_8.setName("chk_diag_8"); // NOI18N
        chk_diag_8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_8ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_8);
        chk_diag_8.setBounds(370, 1220, 30, 22);

        chk_diag_9.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_9.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_9.setName("chk_diag_9"); // NOI18N
        chk_diag_9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_9ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_9);
        chk_diag_9.setBounds(370, 1250, 30, 22);

        chk_diag_10.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_10.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_10.setName("chk_diag_10"); // NOI18N
        chk_diag_10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_10ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_10);
        chk_diag_10.setBounds(370, 1280, 30, 22);

        nm_psdr_6.setEditable(false);
        nm_psdr_6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_6.setEnabled(false);
        nm_psdr_6.setHighlighter(null);
        nm_psdr_6.setName("nm_psdr_6"); // NOI18N
        FormInput.add(nm_psdr_6);
        nm_psdr_6.setBounds(490, 1160, 250, 23);

        kd_psdr_6.setEditable(false);
        kd_psdr_6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_6.setEnabled(false);
        kd_psdr_6.setHighlighter(null);
        kd_psdr_6.setName("kd_psdr_6"); // NOI18N
        FormInput.add(kd_psdr_6);
        kd_psdr_6.setBounds(410, 1160, 70, 23);

        nm_psdr_7.setEditable(false);
        nm_psdr_7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_7.setEnabled(false);
        nm_psdr_7.setHighlighter(null);
        nm_psdr_7.setName("nm_psdr_7"); // NOI18N
        FormInput.add(nm_psdr_7);
        nm_psdr_7.setBounds(490, 1190, 250, 23);

        kd_psdr_7.setEditable(false);
        kd_psdr_7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_7.setEnabled(false);
        kd_psdr_7.setHighlighter(null);
        kd_psdr_7.setName("kd_psdr_7"); // NOI18N
        FormInput.add(kd_psdr_7);
        kd_psdr_7.setBounds(410, 1190, 70, 23);

        nm_psdr_8.setEditable(false);
        nm_psdr_8.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_8.setEnabled(false);
        nm_psdr_8.setHighlighter(null);
        nm_psdr_8.setName("nm_psdr_8"); // NOI18N
        FormInput.add(nm_psdr_8);
        nm_psdr_8.setBounds(490, 1220, 250, 23);

        kd_psdr_8.setEditable(false);
        kd_psdr_8.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_8.setEnabled(false);
        kd_psdr_8.setHighlighter(null);
        kd_psdr_8.setName("kd_psdr_8"); // NOI18N
        FormInput.add(kd_psdr_8);
        kd_psdr_8.setBounds(410, 1220, 70, 23);

        nm_psdr_9.setEditable(false);
        nm_psdr_9.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_9.setEnabled(false);
        nm_psdr_9.setHighlighter(null);
        nm_psdr_9.setName("nm_psdr_9"); // NOI18N
        FormInput.add(nm_psdr_9);
        nm_psdr_9.setBounds(490, 1250, 250, 23);

        kd_psdr_9.setEditable(false);
        kd_psdr_9.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_9.setEnabled(false);
        kd_psdr_9.setHighlighter(null);
        kd_psdr_9.setName("kd_psdr_9"); // NOI18N
        FormInput.add(kd_psdr_9);
        kd_psdr_9.setBounds(410, 1250, 70, 23);

        nm_psdr_10.setEditable(false);
        nm_psdr_10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_10.setEnabled(false);
        nm_psdr_10.setHighlighter(null);
        nm_psdr_10.setName("nm_psdr_10"); // NOI18N
        FormInput.add(nm_psdr_10);
        nm_psdr_10.setBounds(490, 1280, 250, 23);

        kd_psdr_10.setEditable(false);
        kd_psdr_10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_10.setEnabled(false);
        kd_psdr_10.setHighlighter(null);
        kd_psdr_10.setName("kd_psdr_10"); // NOI18N
        FormInput.add(kd_psdr_10);
        kd_psdr_10.setBounds(410, 1280, 70, 23);

        multiple1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple1.setHighlighter(null);
        multiple1.setName("multiple1"); // NOI18N
        FormInput.add(multiple1);
        multiple1.setBounds(750, 1010, 50, 23);

        multiple2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple2.setHighlighter(null);
        multiple2.setName("multiple2"); // NOI18N
        FormInput.add(multiple2);
        multiple2.setBounds(750, 1040, 50, 23);

        multiple3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple3.setHighlighter(null);
        multiple3.setName("multiple3"); // NOI18N
        FormInput.add(multiple3);
        multiple3.setBounds(750, 1070, 50, 23);

        multiple4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple4.setHighlighter(null);
        multiple4.setName("multiple4"); // NOI18N
        FormInput.add(multiple4);
        multiple4.setBounds(750, 1100, 50, 23);

        multiple5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple5.setHighlighter(null);
        multiple5.setName("multiple5"); // NOI18N
        FormInput.add(multiple5);
        multiple5.setBounds(750, 1130, 50, 23);

        multiple6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple6.setHighlighter(null);
        multiple6.setName("multiple6"); // NOI18N
        FormInput.add(multiple6);
        multiple6.setBounds(750, 1160, 50, 23);

        multiple7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple7.setHighlighter(null);
        multiple7.setName("multiple7"); // NOI18N
        FormInput.add(multiple7);
        multiple7.setBounds(750, 1190, 50, 23);

        multiple8.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple8.setHighlighter(null);
        multiple8.setName("multiple8"); // NOI18N
        FormInput.add(multiple8);
        multiple8.setBounds(750, 1220, 50, 23);

        multiple9.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple9.setHighlighter(null);
        multiple9.setName("multiple9"); // NOI18N
        FormInput.add(multiple9);
        multiple9.setBounds(750, 1250, 50, 23);

        multiple10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        multiple10.setHighlighter(null);
        multiple10.setName("multiple10"); // NOI18N
        FormInput.add(multiple10);
        multiple10.setBounds(750, 1280, 50, 23);

        jSeparator2.setBackground(new java.awt.Color(102, 102, 102));
        jSeparator2.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setName("jSeparator2"); // NOI18N
        FormInput.add(jSeparator2);
        jSeparator2.setBounds(407, 35, 1, 325);

        jSeparator3.setName("jSeparator3"); // NOI18N
        FormInput.add(jSeparator3);
        jSeparator3.setBounds(20, 1320, 1000, 10);

        jLabel58.setText("ADL Score : ");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(10, 100, 80, 23);

        jLabel61.setText("Tgl Keluar : ");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput.add(jLabel61);
        jLabel61.setBounds(410, 40, 80, 23);

        jLabel72.setText("Hak Kelas : ");
        jLabel72.setName("jLabel72"); // NOI18N
        FormInput.add(jLabel72);
        jLabel72.setBounds(240, 10, 60, 23);

        jLabel73.setText("Pulang : ");
        jLabel73.setName("jLabel73"); // NOI18N
        FormInput.add(jLabel73);
        jLabel73.setBounds(410, 100, 80, 23);

        jSeparator4.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator4.setName("jSeparator4"); // NOI18N
        FormInput.add(jSeparator4);
        jSeparator4.setBounds(0, 305, 860, 10);

        jSeparator6.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator6.setName("jSeparator6"); // NOI18N
        FormInput.add(jSeparator6);
        jSeparator6.setBounds(0, 35, 860, 10);

        jSeparator7.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator7.setName("jSeparator7"); // NOI18N
        FormInput.add(jSeparator7);
        jSeparator7.setBounds(0, 65, 860, 10);

        jSeparator8.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator8.setName("jSeparator8"); // NOI18N
        FormInput.add(jSeparator8);
        jSeparator8.setBounds(0, 95, 860, 10);

        jSeparator9.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator9.setName("jSeparator9"); // NOI18N
        FormInput.add(jSeparator9);
        jSeparator9.setBounds(0, 125, 860, 10);

        jSeparator10.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator10.setName("jSeparator10"); // NOI18N
        FormInput.add(jSeparator10);
        jSeparator10.setBounds(0, 155, 860, 10);

        jSeparator11.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator11.setName("jSeparator11"); // NOI18N
        FormInput.add(jSeparator11);
        jSeparator11.setBounds(0, 185, 860, 10);

        jSeparator12.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator12.setName("jSeparator12"); // NOI18N
        FormInput.add(jSeparator12);
        jSeparator12.setBounds(0, 215, 860, 10);

        jSeparator13.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator13.setName("jSeparator13"); // NOI18N
        FormInput.add(jSeparator13);
        jSeparator13.setBounds(0, 245, 860, 10);

        jSeparator14.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator14.setName("jSeparator14"); // NOI18N
        FormInput.add(jSeparator14);
        jSeparator14.setBounds(0, 275, 860, 10);

        jSeparator15.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator15.setName("jSeparator15"); // NOI18N
        FormInput.add(jSeparator15);
        jSeparator15.setBounds(0, 335, 860, 10);

        scrollBilling.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        scrollBilling.setName("scrollBilling"); // NOI18N

        panelGlass19.setName("panelGlass19"); // NOI18N
        panelGlass19.setPreferredSize(new java.awt.Dimension(44, 300));
        panelGlass19.setLayout(null);

        jLabel28.setText("Pros. Non Bedah :");
        jLabel28.setName("jLabel28"); // NOI18N
        panelGlass19.add(jLabel28);
        jLabel28.setBounds(10, 10, 90, 23);

        jLabel29.setText("Tenaga Ahli :");
        jLabel29.setName("jLabel29"); // NOI18N
        panelGlass19.add(jLabel29);
        jLabel29.setBounds(10, 40, 90, 23);

        pnb.setHighlighter(null);
        pnb.setName("pnb"); // NOI18N
        pnb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pnbKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pnbKeyTyped(evt);
            }
        });
        panelGlass19.add(pnb);
        pnb.setBounds(110, 10, 95, 23);

        ahli.setHighlighter(null);
        ahli.setName("ahli"); // NOI18N
        ahli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ahliKeyReleased(evt);
            }
        });
        panelGlass19.add(ahli);
        ahli.setBounds(110, 40, 95, 23);

        jLabel30.setText("Radiologi :");
        jLabel30.setName("jLabel30"); // NOI18N
        panelGlass19.add(jLabel30);
        jLabel30.setBounds(10, 70, 90, 23);

        rad.setHighlighter(null);
        rad.setName("rad"); // NOI18N
        rad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                radKeyReleased(evt);
            }
        });
        panelGlass19.add(rad);
        rad.setBounds(110, 70, 95, 23);

        jLabel31.setText("Rehabilitasi :");
        jLabel31.setName("jLabel31"); // NOI18N
        panelGlass19.add(jLabel31);
        jLabel31.setBounds(10, 100, 90, 23);

        rehab.setHighlighter(null);
        rehab.setName("rehab"); // NOI18N
        rehab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rehabKeyReleased(evt);
            }
        });
        panelGlass19.add(rehab);
        rehab.setBounds(110, 100, 95, 23);

        jLabel32.setText("Obat :");
        jLabel32.setName("jLabel32"); // NOI18N
        panelGlass19.add(jLabel32);
        jLabel32.setBounds(10, 130, 90, 23);

        obat.setHighlighter(null);
        obat.setName("obat"); // NOI18N
        obat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                obatKeyReleased(evt);
            }
        });
        panelGlass19.add(obat);
        obat.setBounds(110, 130, 95, 23);

        jLabel70.setText("Alkes :");
        jLabel70.setName("jLabel70"); // NOI18N
        panelGlass19.add(jLabel70);
        jLabel70.setBounds(10, 160, 90, 23);

        alkes.setHighlighter(null);
        alkes.setName("alkes"); // NOI18N
        alkes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                alkesKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alkesKeyTyped(evt);
            }
        });
        panelGlass19.add(alkes);
        alkes.setBounds(110, 160, 95, 23);

        jLabel33.setText("Pros. Bedah :");
        jLabel33.setName("jLabel33"); // NOI18N
        panelGlass19.add(jLabel33);
        jLabel33.setBounds(10, 190, 90, 23);

        pb.setHighlighter(null);
        pb.setName("pb"); // NOI18N
        pb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pbKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pbKeyReleased(evt);
            }
        });
        panelGlass19.add(pb);
        pb.setBounds(110, 190, 95, 23);

        jLabel34.setText("Keperawatan :");
        jLabel34.setName("jLabel34"); // NOI18N
        panelGlass19.add(jLabel34);
        jLabel34.setBounds(10, 220, 90, 23);

        kpr.setHighlighter(null);
        kpr.setName("kpr"); // NOI18N
        kpr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kprKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                kprKeyReleased(evt);
            }
        });
        panelGlass19.add(kpr);
        kpr.setBounds(110, 220, 95, 23);

        jLabel35.setText("Labotatorium :");
        jLabel35.setName("jLabel35"); // NOI18N
        panelGlass19.add(jLabel35);
        jLabel35.setBounds(10, 250, 90, 23);

        lab.setHighlighter(null);
        lab.setName("lab"); // NOI18N
        lab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                labKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                labKeyReleased(evt);
            }
        });
        panelGlass19.add(lab);
        lab.setBounds(110, 250, 95, 23);

        jLabel36.setText("Akomodasi :");
        jLabel36.setName("jLabel36"); // NOI18N
        panelGlass19.add(jLabel36);
        jLabel36.setBounds(220, 10, 90, 23);

        akomodasi.setHighlighter(null);
        akomodasi.setName("akomodasi"); // NOI18N
        akomodasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                akomodasiKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                akomodasiKeyReleased(evt);
            }
        });
        panelGlass19.add(akomodasi);
        akomodasi.setBounds(320, 10, 95, 23);

        jLabel37.setText("Obat Kronis :");
        jLabel37.setName("jLabel37"); // NOI18N
        panelGlass19.add(jLabel37);
        jLabel37.setBounds(220, 40, 90, 23);

        kronis.setHighlighter(null);
        kronis.setName("kronis"); // NOI18N
        kronis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kronisKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                kronisKeyReleased(evt);
            }
        });
        panelGlass19.add(kronis);
        kronis.setBounds(320, 40, 95, 23);

        jLabel27.setText("BMHP :");
        jLabel27.setName("jLabel27"); // NOI18N
        panelGlass19.add(jLabel27);
        jLabel27.setBounds(220, 70, 90, 23);

        bmhp.setHighlighter(null);
        bmhp.setName("bmhp"); // NOI18N
        bmhp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bmhpKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bmhpKeyReleased(evt);
            }
        });
        panelGlass19.add(bmhp);
        bmhp.setBounds(320, 70, 95, 23);

        jLabel42.setText("Obat Kemo :");
        jLabel42.setName("jLabel42"); // NOI18N
        panelGlass19.add(jLabel42);
        jLabel42.setBounds(220, 220, 90, 23);

        ksl.setHighlighter(null);
        ksl.setName("ksl"); // NOI18N
        ksl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kslKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                kslKeyReleased(evt);
            }
        });
        panelGlass19.add(ksl);
        ksl.setBounds(320, 100, 95, 23);

        pnj.setHighlighter(null);
        pnj.setName("pnj"); // NOI18N
        pnj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pnjKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pnjKeyReleased(evt);
            }
        });
        panelGlass19.add(pnj);
        pnj.setBounds(320, 130, 95, 23);

        sewa_alat.setHighlighter(null);
        sewa_alat.setName("sewa_alat"); // NOI18N
        sewa_alat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sewa_alatKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sewa_alatKeyReleased(evt);
            }
        });
        panelGlass19.add(sewa_alat);
        sewa_alat.setBounds(320, 250, 95, 23);

        jLabel40.setText("Pelay. Darah :");
        jLabel40.setName("jLabel40"); // NOI18N
        panelGlass19.add(jLabel40);
        jLabel40.setBounds(220, 160, 90, 23);

        jLabel38.setText("Konsultasi :");
        jLabel38.setName("jLabel38"); // NOI18N
        panelGlass19.add(jLabel38);
        jLabel38.setBounds(220, 100, 90, 23);

        ri.setHighlighter(null);
        ri.setName("ri"); // NOI18N
        ri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                riKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                riKeyReleased(evt);
            }
        });
        panelGlass19.add(ri);
        ri.setBounds(320, 190, 95, 23);

        jLabel50.setText("Sewa Alat :");
        jLabel50.setName("jLabel50"); // NOI18N
        panelGlass19.add(jLabel50);
        jLabel50.setBounds(220, 250, 90, 23);

        jLabel41.setText("Rwt Intensif :");
        jLabel41.setName("jLabel41"); // NOI18N
        panelGlass19.add(jLabel41);
        jLabel41.setBounds(220, 190, 90, 23);

        pl_darah.setHighlighter(null);
        pl_darah.setName("pl_darah"); // NOI18N
        pl_darah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pl_darahKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pl_darahKeyReleased(evt);
            }
        });
        panelGlass19.add(pl_darah);
        pl_darah.setBounds(320, 160, 95, 23);

        obat_kemo.setHighlighter(null);
        obat_kemo.setName("obat_kemo"); // NOI18N
        obat_kemo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                obat_kemoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                obat_kemoKeyReleased(evt);
            }
        });
        panelGlass19.add(obat_kemo);
        obat_kemo.setBounds(320, 220, 95, 23);

        jLabel39.setText("Penunjang :");
        jLabel39.setName("jLabel39"); // NOI18N
        panelGlass19.add(jLabel39);
        jLabel39.setBounds(220, 130, 90, 23);

        pnb1.setHighlighter(null);
        pnb1.setName("pnb1"); // NOI18N
        panelGlass19.add(pnb1);
        pnb1.setBounds(110, 10, 95, 23);

        scrollBilling.setViewportView(panelGlass19);

        FormInput.add(scrollBilling);
        scrollBilling.setBounds(870, 10, 430, 330);

        TabRawat.setBackground(new java.awt.Color(255, 255, 254));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFocusCycleRoot(true);
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabRawatStateChanged(evt);
            }
        });
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);

        panelGlass11.setName("panelGlass11"); // NOI18N
        panelGlass11.setPreferredSize(new java.awt.Dimension(44, 54));
        panelGlass11.setLayout(new java.awt.GridBagLayout());

        panelGlass13.setName("panelGlass13"); // NOI18N
        panelGlass13.setPreferredSize(new java.awt.Dimension(44, 54));
        panelGlass13.setLayout(null);

        scrollCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 153, 255)));
        scrollCombo.setName("scrollCombo"); // NOI18N
        scrollCombo.setOpaque(true);

        tbCombo.setAutoCreateRowSorter(true);
        tbCombo.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbCombo.setName("tbCombo"); // NOI18N
        tbCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbComboMouseClicked(evt);
            }
        });
        tbCombo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbComboKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbComboKeyReleased(evt);
            }
        });
        scrollCombo.setViewportView(tbCombo);

        panelGlass13.add(scrollCombo);
        scrollCombo.setBounds(10, 40, 750, 170);

        textDiag.setName("textDiag"); // NOI18N
        textDiag.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textDiagFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textDiagFocusLost(evt);
            }
        });
        textDiag.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                textDiagInputMethodTextChanged(evt);
            }
        });
        textDiag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textDiagActionPerformed(evt);
            }
        });
        textDiag.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textDiagKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textDiagKeyReleased(evt);
            }
        });
        panelGlass13.add(textDiag);
        textDiag.setBounds(80, 10, 310, 25);

        jLabel7.setText("Diagnosa :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass13.add(jLabel7);
        jLabel7.setBounds(10, 10, 60, 23);

        jButton1.setText("...");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panelGlass13.add(jButton1);
        jButton1.setBounds(730, 430, 30, 23);

        jLabel8.setText("Prosedur :");
        jLabel8.setName("jLabel8"); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass13.add(jLabel8);
        jLabel8.setBounds(10, 220, 60, 23);

        textProc.setName("textProc"); // NOI18N
        textProc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textProcFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textProcFocusLost(evt);
            }
        });
        textProc.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                textProcInputMethodTextChanged(evt);
            }
        });
        textProc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textProcActionPerformed(evt);
            }
        });
        textProc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textProcKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textProcKeyReleased(evt);
            }
        });
        panelGlass13.add(textProc);
        textProc.setBounds(80, 220, 310, 25);

        scrollCombo2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 153, 255)));
        scrollCombo2.setName("scrollCombo2"); // NOI18N
        scrollCombo2.setOpaque(true);

        tbCombo2.setAutoCreateRowSorter(true);
        tbCombo2.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbCombo2.setName("tbCombo2"); // NOI18N
        tbCombo2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbCombo2MouseClicked(evt);
            }
        });
        tbCombo2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbCombo2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbCombo2KeyReleased(evt);
            }
        });
        scrollCombo2.setViewportView(tbCombo2);

        panelGlass13.add(scrollCombo2);
        scrollCombo2.setBounds(10, 250, 750, 170);

        simpanDxPxIDRG.setText("Simpan Diagnose & Procedures");
        simpanDxPxIDRG.setName("simpanDxPxIDRG"); // NOI18N
        simpanDxPxIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanDxPxIDRGActionPerformed(evt);
            }
        });
        panelGlass13.add(simpanDxPxIDRG);
        simpanDxPxIDRG.setBounds(560, 3, 200, 27);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.52;
        gridBagConstraints.weighty = 1.0;
        panelGlass11.add(panelGlass13, gridBagConstraints);

        panelGlass14.setName("panelGlass14"); // NOI18N
        panelGlass14.setPreferredSize(new java.awt.Dimension(44, 54));
        panelGlass14.setLayout(null);

        scrollRespon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        scrollRespon.setName("scrollRespon"); // NOI18N
        scrollRespon.setPreferredSize(new java.awt.Dimension(50, 50));

        panelGlass10.setBackground(new java.awt.Color(187, 187, 187));
        panelGlass10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        panelGlass10.setName("panelGlass10"); // NOI18N
        panelGlass10.setPreferredSize(new java.awt.Dimension(520, 500));
        panelGlass10.setLayout(null);

        jLabel111.setText("MDC :");
        jLabel111.setName("jLabel111"); // NOI18N
        panelGlass10.add(jLabel111);
        jLabel111.setBounds(0, 70, 70, 23);

        txStatus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txStatus.setName("txStatus"); // NOI18N
        panelGlass10.add(txStatus);
        txStatus.setBounds(80, 130, 430, 23);

        jLabel120.setText("Status :");
        jLabel120.setName("jLabel120"); // NOI18N
        panelGlass10.add(jLabel120);
        jLabel120.setBounds(0, 130, 70, 23);

        txDrg.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txDrg.setName("txDrg"); // NOI18N
        panelGlass10.add(txDrg);
        txDrg.setBounds(80, 100, 330, 23);

        jSeparator30.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator30.setName("jSeparator30"); // NOI18N
        panelGlass10.add(jSeparator30);
        jSeparator30.setBounds(10, 125, 500, 10);

        grupCode1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        grupCode1.setName("grupCode1"); // NOI18N
        panelGlass10.add(grupCode1);
        grupCode1.setBounds(300, 450, 60, 23);

        jSeparator39.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator39.setName("jSeparator39"); // NOI18N
        panelGlass10.add(jSeparator39);
        jSeparator39.setBounds(10, 95, 500, 10);

        jLabel128.setText("Jns Rawat :");
        jLabel128.setName("jLabel128"); // NOI18N
        panelGlass10.add(jLabel128);
        jLabel128.setBounds(0, 40, 70, 23);

        txMdc.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txMdc.setName("txMdc"); // NOI18N
        panelGlass10.add(txMdc);
        txMdc.setBounds(80, 70, 330, 23);

        jLabel130.setText("DRG :");
        jLabel130.setName("jLabel130"); // NOI18N
        panelGlass10.add(jLabel130);
        jLabel130.setBounds(0, 100, 70, 23);

        jSeparator40.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator40.setName("jSeparator40"); // NOI18N
        panelGlass10.add(jSeparator40);
        jSeparator40.setBounds(10, 65, 500, 10);

        jSeparator41.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator41.setName("jSeparator41"); // NOI18N
        panelGlass10.add(jSeparator41);
        jSeparator41.setBounds(10, 160, 500, 10);

        jLabel112.setText("Info :");
        jLabel112.setName("jLabel112"); // NOI18N
        panelGlass10.add(jLabel112);
        jLabel112.setBounds(0, 10, 70, 23);

        txInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txInfo.setName("txInfo"); // NOI18N
        panelGlass10.add(txInfo);
        txInfo.setBounds(80, 10, 430, 23);

        jSeparator33.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator33.setName("jSeparator33"); // NOI18N
        panelGlass10.add(jSeparator33);
        jSeparator33.setBounds(10, 35, 500, 10);

        txJenis.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txJenis.setName("txJenis"); // NOI18N
        panelGlass10.add(txJenis);
        txJenis.setBounds(80, 40, 430, 23);

        txDrgCode.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txDrgCode.setName("txDrgCode"); // NOI18N
        panelGlass10.add(txDrgCode);
        txDrgCode.setBounds(420, 100, 90, 23);

        txMdcCode.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txMdcCode.setName("txMdcCode"); // NOI18N
        panelGlass10.add(txMdcCode);
        txMdcCode.setBounds(420, 70, 90, 23);

        scrollRespon.setViewportView(panelGlass10);

        panelGlass14.add(scrollRespon);
        scrollRespon.setBounds(10, 10, 510, 200);

        scrollPane6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane6.setName("scrollPane6"); // NOI18N
        scrollPane6.setPreferredSize(new java.awt.Dimension(400, 300));

        tLog.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tLog.setColumns(20);
        tLog.setRows(5);
        tLog.setName("tLog"); // NOI18N
        tLog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tLogKeyPressed(evt);
            }
        });
        scrollPane6.setViewportView(tLog);

        panelGlass14.add(scrollPane6);
        scrollPane6.setBounds(6, 250, 510, 200);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.34;
        gridBagConstraints.weighty = 1.0;
        panelGlass11.add(panelGlass14, gridBagConstraints);

        Scroll2.setViewportView(panelGlass11);

        TabRawat.addTab("iDRG", Scroll2);

        Scroll3.setName("Scroll3"); // NOI18N
        Scroll3.setOpaque(true);

        panelGlass18.setName("panelGlass18"); // NOI18N
        panelGlass18.setPreferredSize(new java.awt.Dimension(44, 54));
        panelGlass18.setLayout(new java.awt.GridBagLayout());

        panelGlass20.setName("panelGlass20"); // NOI18N
        panelGlass20.setPreferredSize(new java.awt.Dimension(44, 54));
        panelGlass20.setLayout(null);

        scrollCombo4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 153, 255)));
        scrollCombo4.setName("scrollCombo4"); // NOI18N
        scrollCombo4.setOpaque(true);

        tbCombo1.setAutoCreateRowSorter(true);
        tbCombo1.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbCombo1.setName("tbCombo1"); // NOI18N
        tbCombo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbCombo1MouseClicked(evt);
            }
        });
        tbCombo1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbCombo1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbCombo1KeyReleased(evt);
            }
        });
        scrollCombo4.setViewportView(tbCombo1);

        panelGlass20.add(scrollCombo4);
        scrollCombo4.setBounds(10, 40, 750, 170);

        textDiag2.setName("textDiag2"); // NOI18N
        textDiag2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textDiag2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textDiag2FocusLost(evt);
            }
        });
        textDiag2.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                textDiag2InputMethodTextChanged(evt);
            }
        });
        textDiag2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textDiag2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textDiag2KeyReleased(evt);
            }
        });
        panelGlass20.add(textDiag2);
        textDiag2.setBounds(80, 10, 310, 25);

        jLabel16.setText("Diagnosa :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass20.add(jLabel16);
        jLabel16.setBounds(10, 10, 60, 23);

        jLabel18.setText("Prosedur :");
        jLabel18.setName("jLabel18"); // NOI18N
        jLabel18.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass20.add(jLabel18);
        jLabel18.setBounds(10, 220, 60, 23);

        textProc2.setName("textProc2"); // NOI18N
        textProc2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textProc2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textProc2FocusLost(evt);
            }
        });
        textProc2.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                textProc2InputMethodTextChanged(evt);
            }
        });
        textProc2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textProc2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textProc2KeyReleased(evt);
            }
        });
        panelGlass20.add(textProc2);
        textProc2.setBounds(80, 220, 310, 25);

        scrollCombo5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 153, 255)));
        scrollCombo5.setName("scrollCombo5"); // NOI18N
        scrollCombo5.setOpaque(true);

        tbCombo22.setAutoCreateRowSorter(true);
        tbCombo22.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbCombo22.setName("tbCombo22"); // NOI18N
        tbCombo22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbCombo22MouseClicked(evt);
            }
        });
        tbCombo22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbCombo22KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbCombo22KeyReleased(evt);
            }
        });
        scrollCombo5.setViewportView(tbCombo22);

        panelGlass20.add(scrollCombo5);
        scrollCombo5.setBounds(10, 250, 750, 170);

        simpanDxPxInacbg.setText("Simpan Diagnosa & Procedure");
        simpanDxPxInacbg.setName("simpanDxPxInacbg"); // NOI18N
        simpanDxPxInacbg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanDxPxInacbgActionPerformed(evt);
            }
        });
        panelGlass20.add(simpanDxPxInacbg);
        simpanDxPxInacbg.setBounds(550, 6, 210, 27);

        simpanDxPxInacbg1.setText("Import iDRG");
        simpanDxPxInacbg1.setName("simpanDxPxInacbg1"); // NOI18N
        simpanDxPxInacbg1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanDxPxInacbg1ActionPerformed(evt);
            }
        });
        panelGlass20.add(simpanDxPxInacbg1);
        simpanDxPxInacbg1.setBounds(420, 6, 120, 27);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.52;
        gridBagConstraints.weighty = 1.0;
        panelGlass18.add(panelGlass20, gridBagConstraints);

        panelGlass21.setName("panelGlass21"); // NOI18N
        panelGlass21.setPreferredSize(new java.awt.Dimension(44, 54));
        panelGlass21.setLayout(null);

        scrollPane7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane7.setName("scrollPane7"); // NOI18N
        scrollPane7.setPreferredSize(new java.awt.Dimension(400, 300));

        tLog1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tLog1.setColumns(20);
        tLog1.setRows(5);
        tLog1.setName("tLog1"); // NOI18N
        tLog1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tLog1KeyPressed(evt);
            }
        });
        scrollPane7.setViewportView(tLog1);

        panelGlass21.add(scrollPane7);
        scrollPane7.setBounds(540, 380, 200, 40);

        scrollRespon1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        scrollRespon1.setName("scrollRespon1"); // NOI18N
        scrollRespon1.setPreferredSize(new java.awt.Dimension(50, 50));

        panelGlass9.setBackground(new java.awt.Color(187, 187, 187));
        panelGlass9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(520, 500));
        panelGlass9.setLayout(null);

        cmbDrugs.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none : None" }));
        cmbDrugs.setEnabled(false);
        cmbDrugs.setName("cmbDrugs"); // NOI18N
        panelGlass9.add(cmbDrugs);
        cmbDrugs.setBounds(120, 260, 240, 23);

        jLabel86.setText("Sub Acute :");
        jLabel86.setName("jLabel86"); // NOI18N
        panelGlass9.add(jLabel86);
        jLabel86.setBounds(10, 110, 100, 23);

        jLabel87.setText("BILLING RS :");
        jLabel87.setName("jLabel87"); // NOI18N
        panelGlass9.add(jLabel87);
        jLabel87.setBounds(10, 50, 100, 23);

        jLabel88.setText("Group :");
        jLabel88.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel88.setName("jLabel88"); // NOI18N
        panelGlass9.add(jLabel88);
        jLabel88.setBounds(10, 290, 100, 23);

        jLabel89.setText("Chronic");
        jLabel89.setName("jLabel89"); // NOI18N
        panelGlass9.add(jLabel89);
        jLabel89.setBounds(10, 140, 100, 23);

        jLabel90.setText("Spec. Procedure :");
        jLabel90.setName("jLabel90"); // NOI18N
        panelGlass9.add(jLabel90);
        jLabel90.setBounds(10, 170, 100, 23);

        jLabel91.setText("Spec. Prosthesis :");
        jLabel91.setName("jLabel91"); // NOI18N
        panelGlass9.add(jLabel91);
        jLabel91.setBounds(10, 200, 100, 23);

        jLabel92.setText("TOTAL KLAIM :");
        jLabel92.setName("jLabel92"); // NOI18N
        panelGlass9.add(jLabel92);
        jLabel92.setBounds(10, 350, 100, 23);

        jLabel93.setText("Spec. Investigation :");
        jLabel93.setName("jLabel93"); // NOI18N
        panelGlass9.add(jLabel93);
        jLabel93.setBounds(10, 230, 100, 23);

        jLabel94.setText("Spec.Drugs :");
        jLabel94.setName("jLabel94"); // NOI18N
        panelGlass9.add(jLabel94);
        jLabel94.setBounds(10, 260, 100, 23);

        lStatus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lStatus.setName("lStatus"); // NOI18N
        panelGlass9.add(lStatus);
        lStatus.setBounds(120, 410, 150, 23);

        lChronic.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lChronic.setName("lChronic"); // NOI18N
        panelGlass9.add(lChronic);
        lChronic.setBounds(120, 140, 150, 23);

        jLabel97.setText("Rp. ");
        jLabel97.setName("jLabel97"); // NOI18N
        panelGlass9.add(jLabel97);
        jLabel97.setBounds(375, 290, 30, 23);

        jLabel99.setText("Status :");
        jLabel99.setName("jLabel99"); // NOI18N
        panelGlass9.add(jLabel99);
        jLabel99.setBounds(10, 410, 100, 23);

        cmbProc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none : None" }));
        cmbProc.setEnabled(false);
        cmbProc.setName("cmbProc"); // NOI18N
        panelGlass9.add(cmbProc);
        cmbProc.setBounds(120, 170, 240, 23);

        cmbPros.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none : None" }));
        cmbPros.setEnabled(false);
        cmbPros.setName("cmbPros"); // NOI18N
        panelGlass9.add(cmbPros);
        cmbPros.setBounds(120, 200, 240, 23);

        cmbInv.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none : None" }));
        cmbInv.setEnabled(false);
        cmbInv.setName("cmbInv"); // NOI18N
        panelGlass9.add(cmbInv);
        cmbInv.setBounds(120, 230, 240, 23);

        lJenis.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lJenis.setName("lJenis"); // NOI18N
        panelGlass9.add(lJenis);
        lJenis.setBounds(120, 80, 220, 23);

        lSubacute.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lSubacute.setName("lSubacute"); // NOI18N
        panelGlass9.add(lSubacute);
        lSubacute.setBounds(120, 110, 170, 23);

        jLabel103.setText("Rp. ");
        jLabel103.setName("jLabel103"); // NOI18N
        panelGlass9.add(jLabel103);
        jLabel103.setBounds(375, 140, 30, 23);

        jLabel104.setText("Rp. ");
        jLabel104.setName("jLabel104"); // NOI18N
        panelGlass9.add(jLabel104);
        jLabel104.setBounds(375, 110, 30, 23);

        jLabel105.setText("Rp. ");
        jLabel105.setName("jLabel105"); // NOI18N
        panelGlass9.add(jLabel105);
        jLabel105.setBounds(375, 200, 30, 23);

        jLabel106.setText("Rp. ");
        jLabel106.setName("jLabel106"); // NOI18N
        panelGlass9.add(jLabel106);
        jLabel106.setBounds(375, 170, 30, 23);

        jLabel107.setText("Rp. ");
        jLabel107.setName("jLabel107"); // NOI18N
        panelGlass9.add(jLabel107);
        jLabel107.setBounds(375, 260, 30, 23);

        jLabel108.setText("Rp. ");
        jLabel108.setName("jLabel108"); // NOI18N
        panelGlass9.add(jLabel108);
        jLabel108.setBounds(375, 230, 30, 23);

        jLabel110.setText("Rp. ");
        jLabel110.setName("jLabel110"); // NOI18N
        panelGlass9.add(jLabel110);
        jLabel110.setBounds(375, 350, 30, 23);

        lblTarifKronis.setText("0");
        lblTarifKronis.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTarifKronis.setName("lblTarifKronis"); // NOI18N
        panelGlass9.add(lblTarifKronis);
        lblTarifKronis.setBounds(400, 140, 95, 23);

        lblTarifGrup.setText("0");
        lblTarifGrup.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTarifGrup.setName("lblTarifGrup"); // NOI18N
        panelGlass9.add(lblTarifGrup);
        lblTarifGrup.setBounds(400, 290, 95, 23);

        lblTarifAcute.setText("0");
        lblTarifAcute.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTarifAcute.setName("lblTarifAcute"); // NOI18N
        panelGlass9.add(lblTarifAcute);
        lblTarifAcute.setBounds(400, 110, 95, 23);

        lbltarifProc.setText("0");
        lbltarifProc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbltarifProc.setName("lbltarifProc"); // NOI18N
        panelGlass9.add(lbltarifProc);
        lbltarifProc.setBounds(400, 170, 95, 23);

        lblTarifPros.setText("0");
        lblTarifPros.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTarifPros.setName("lblTarifPros"); // NOI18N
        panelGlass9.add(lblTarifPros);
        lblTarifPros.setBounds(400, 200, 95, 23);

        lblTarifDrugs.setText("0");
        lblTarifDrugs.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTarifDrugs.setName("lblTarifDrugs"); // NOI18N
        panelGlass9.add(lblTarifDrugs);
        lblTarifDrugs.setBounds(400, 260, 95, 23);

        lblTarifInv.setText("0");
        lblTarifInv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTarifInv.setName("lblTarifInv"); // NOI18N
        panelGlass9.add(lblTarifInv);
        lblTarifInv.setBounds(400, 230, 95, 23);

        lblTarifTotal.setText("0");
        lblTarifTotal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTarifTotal.setName("lblTarifTotal"); // NOI18N
        panelGlass9.add(lblTarifTotal);
        lblTarifTotal.setBounds(400, 350, 95, 23);

        jSeparator16.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator16.setName("jSeparator16"); // NOI18N
        panelGlass9.add(jSeparator16);
        jSeparator16.setBounds(10, 405, 500, 10);

        jSeparator17.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator17.setName("jSeparator17"); // NOI18N
        panelGlass9.add(jSeparator17);
        jSeparator17.setBounds(10, 105, 500, 10);

        jSeparator18.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator18.setName("jSeparator18"); // NOI18N
        panelGlass9.add(jSeparator18);
        jSeparator18.setBounds(10, 375, 500, 10);

        jSeparator19.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator19.setName("jSeparator19"); // NOI18N
        panelGlass9.add(jSeparator19);
        jSeparator19.setBounds(10, 135, 500, 10);

        jSeparator20.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator20.setName("jSeparator20"); // NOI18N
        panelGlass9.add(jSeparator20);
        jSeparator20.setBounds(10, 165, 500, 10);

        jSeparator21.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator21.setName("jSeparator21"); // NOI18N
        panelGlass9.add(jSeparator21);
        jSeparator21.setBounds(10, 195, 500, 10);

        jSeparator22.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator22.setName("jSeparator22"); // NOI18N
        panelGlass9.add(jSeparator22);
        jSeparator22.setBounds(10, 225, 500, 10);

        jSeparator23.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator23.setName("jSeparator23"); // NOI18N
        panelGlass9.add(jSeparator23);
        jSeparator23.setBounds(10, 255, 500, 10);

        jSeparator24.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator24.setName("jSeparator24"); // NOI18N
        panelGlass9.add(jSeparator24);
        jSeparator24.setBounds(10, 285, 500, 10);

        jSeparator25.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator25.setName("jSeparator25"); // NOI18N
        panelGlass9.add(jSeparator25);
        jSeparator25.setBounds(10, 340, 500, 10);

        grupCode.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        grupCode.setName("grupCode"); // NOI18N
        panelGlass9.add(grupCode);
        grupCode.setBounds(300, 450, 60, 23);

        jSeparator26.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator26.setName("jSeparator26"); // NOI18N
        panelGlass9.add(jSeparator26);
        jSeparator26.setBounds(10, 75, 500, 10);

        jLabel95.setText("Jns Rawat :");
        jLabel95.setName("jLabel95"); // NOI18N
        panelGlass9.add(jLabel95);
        jLabel95.setBounds(10, 20, 100, 23);

        lJenis1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lJenis1.setName("lJenis1"); // NOI18N
        panelGlass9.add(lJenis1);
        lJenis1.setBounds(120, 20, 220, 23);

        jLabel98.setText("Rp. ");
        jLabel98.setName("jLabel98"); // NOI18N
        panelGlass9.add(jLabel98);
        jLabel98.setBounds(375, 50, 30, 23);

        lblTarifRS.setText("0");
        lblTarifRS.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTarifRS.setName("lblTarifRS"); // NOI18N
        panelGlass9.add(lblTarifRS);
        lblTarifRS.setBounds(400, 50, 95, 23);

        jLabel96.setText("Base Tariff INAcbg :");
        jLabel96.setName("jLabel96"); // NOI18N
        panelGlass9.add(jLabel96);
        jLabel96.setBounds(10, 80, 100, 23);

        jLabel100.setText("Rp. ");
        jLabel100.setName("jLabel100"); // NOI18N
        panelGlass9.add(jLabel100);
        jLabel100.setBounds(375, 80, 30, 23);

        lblBaseTariff.setText("0");
        lblBaseTariff.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBaseTariff.setName("lblBaseTariff"); // NOI18N
        panelGlass9.add(lblBaseTariff);
        lblBaseTariff.setBounds(400, 80, 95, 23);

        jSeparator27.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator27.setName("jSeparator27"); // NOI18N
        panelGlass9.add(jSeparator27);
        jSeparator27.setBounds(10, 45, 500, 10);

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane1.setName("scrollPane1"); // NOI18N

        Txtgroup.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Txtgroup.setColumns(20);
        Txtgroup.setRows(5);
        Txtgroup.setName("Txtgroup"); // NOI18N
        Txtgroup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtgroupKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(Txtgroup);

        panelGlass9.add(scrollPane1);
        scrollPane1.setBounds(120, 290, 240, 50);

        jLabel101.setText("Selisih :");
        jLabel101.setName("jLabel101"); // NOI18N
        panelGlass9.add(jLabel101);
        jLabel101.setBounds(10, 380, 100, 23);

        jLabel109.setText("Rp. ");
        jLabel109.setName("jLabel109"); // NOI18N
        panelGlass9.add(jLabel109);
        jLabel109.setBounds(375, 380, 30, 23);

        lblSelisih.setText("0");
        lblSelisih.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblSelisih.setName("lblSelisih"); // NOI18N
        panelGlass9.add(lblSelisih);
        lblSelisih.setBounds(400, 380, 95, 23);

        jSeparator28.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator28.setName("jSeparator28"); // NOI18N
        panelGlass9.add(jSeparator28);
        jSeparator28.setBounds(10, 440, 500, 10);

        scrollRespon1.setViewportView(panelGlass9);

        panelGlass21.add(scrollRespon1);
        scrollRespon1.setBounds(10, 10, 520, 450);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.34;
        gridBagConstraints.weighty = 1.0;
        panelGlass18.add(panelGlass21, gridBagConstraints);

        Scroll3.setViewportView(panelGlass18);

        TabRawat.addTab("InaCBGs", Scroll3);

        FormInput.add(TabRawat);
        TabRawat.setBounds(0, 340, 1310, 500);

        scrollInput.setViewportView(FormInput);

        internalFrame1.add(scrollInput, java.awt.BorderLayout.CENTER);

        tabPane1.addTab("INACBG IDRG", internalFrame1);

        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass15.setName("panelGlass15"); // NOI18N
        panelGlass15.setPreferredSize(new java.awt.Dimension(55, 55));
        panelGlass15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(62, 23));
        panelGlass15.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(180, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass15.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('2');
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass15.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass15.add(BtnAll);

        jLabel11.setText("Record :");
        jLabel11.setName("jLabel11"); // NOI18N
        jLabel11.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass15.add(jLabel11);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(50, 23));
        panelGlass15.add(LCount);

        BtnKeluar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar1.setMnemonic('K');
        BtnKeluar1.setText("Keluar");
        BtnKeluar1.setToolTipText("Alt+K");
        BtnKeluar1.setName("BtnKeluar1"); // NOI18N
        BtnKeluar1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluar1ActionPerformed(evt);
            }
        });
        BtnKeluar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluar1KeyPressed(evt);
            }
        });
        panelGlass15.add(BtnKeluar1);

        jPanel3.add(panelGlass15, java.awt.BorderLayout.PAGE_END);

        panelGlass16.setName("panelGlass16"); // NOI18N
        panelGlass16.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        jLabel21.setText("Tgl. SEP Berdasarkan :");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(120, 23));
        panelGlass16.add(jLabel21);

        CmbTanggalSEP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tanggal Masuk", "Tanggal Pulang" }));
        CmbTanggalSEP.setLightWeightPopupEnabled(false);
        CmbTanggalSEP.setMinimumSize(new java.awt.Dimension(75, 21));
        CmbTanggalSEP.setName("CmbTanggalSEP"); // NOI18N
        CmbTanggalSEP.setPreferredSize(new java.awt.Dimension(110, 23));
        panelGlass16.add(CmbTanggalSEP);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "07-10-2025" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass16.add(DTPCari1);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("s.d.");
        jLabel46.setName("jLabel46"); // NOI18N
        jLabel46.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass16.add(jLabel46);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "07-10-2025" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass16.add(DTPCari2);

        jLabel14.setText("Status Rawat :");
        jLabel14.setName("jLabel14"); // NOI18N
        jLabel14.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass16.add(jLabel14);

        CmbStatusRawat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Ralan", "Ranap" }));
        CmbStatusRawat.setLightWeightPopupEnabled(false);
        CmbStatusRawat.setMinimumSize(new java.awt.Dimension(75, 21));
        CmbStatusRawat.setName("CmbStatusRawat"); // NOI18N
        CmbStatusRawat.setPreferredSize(new java.awt.Dimension(76, 23));
        panelGlass16.add(CmbStatusRawat);

        jLabel74.setText("Status Kirim :");
        jLabel74.setName("jLabel74"); // NOI18N
        jLabel74.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass16.add(jLabel74);

        CmbStatusKirim.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Terkirim", "Belum Terkirim" }));
        CmbStatusKirim.setLightWeightPopupEnabled(false);
        CmbStatusKirim.setMinimumSize(new java.awt.Dimension(75, 21));
        CmbStatusKirim.setName("CmbStatusKirim"); // NOI18N
        CmbStatusKirim.setPreferredSize(new java.awt.Dimension(113, 23));
        panelGlass16.add(CmbStatusKirim);

        lblCoderNIK.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCoderNIK.setName("lblCoderNIK"); // NOI18N
        lblCoderNIK.setPreferredSize(new java.awt.Dimension(105, 23));
        panelGlass16.add(lblCoderNIK);

        jPanel3.add(panelGlass16, java.awt.BorderLayout.CENTER);

        internalFrame2.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(816, 102));
        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setOpaque(false);

        panelGlass17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        panelGlass17.setName("panelGlass17"); // NOI18N
        panelGlass17.setLayout(new java.awt.BorderLayout());

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbKompilasi.setMaximumSize(new java.awt.Dimension(32767, 32767));
        tbKompilasi.setName("tbKompilasi"); // NOI18N
        tbKompilasi.setPreferredScrollableViewportSize(null);
        tbKompilasi.setPreferredSize(null);
        tbKompilasi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbKompilasiMouseClicked(evt);
            }
        });
        tbKompilasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbKompilasiKeyReleased(evt);
            }
        });
        Scroll.setViewportView(tbKompilasi);

        panelGlass17.add(Scroll, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGlass17, javax.swing.GroupLayout.DEFAULT_SIZE, 1573, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGlass17, javax.swing.GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4);

        internalFrame2.add(jPanel1, java.awt.BorderLayout.CENTER);

        tabPane1.addTab("PASIEN KLAIM", internalFrame2);

        getContentPane().add(tabPane1, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnGrouperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGrouperActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        if (stage_2.getSelectedItem() == null) {
            grouper1();
        } else {
            grouper2();
        }
        
//        simpanDxPxInacbg.setEnabled(false);
//        disableDxInacbg(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_BtnGrouperActionPerformed

    private void BtnGrouperKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnGrouperKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            emptTeks();
        }
}//GEN-LAST:event_BtnGrouperKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
       
    }//GEN-LAST:event_formWindowOpened

    private void klaimbaru(){
        if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson = "{\n"
                                   + " \"metadata\": {\n"
                                   + " \"method\": \"new_claim\"\n"
                                   + "},\n"
                                   + " \"data\": {\n"+
                                                    "\"nomor_kartu\": \""+Tnoka.getText().trim()+"\",\n" +
                                                    "\"nomor_sep\": \""+TNoSEP.getText().trim()+"\",\n" +
                                                    "\"nomor_rm\": \""+TNoRM.getText().trim()+"\",\n" +
                                                    "\"nama_pasien\": \""+TPasien.getText().trim()+"\",\n" +
                                                    "\"tgl_lahir\": \""+tgl_lahir.getText().trim()+" 00:00:00\",\n" +
                                                    "\"gender\": \""+(jenis_kelamin.getText().trim().equals("L") ? 1 : 2)+"\""
                                            + " }\n"
                                   + "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);

                JSONObject response = inacbg.request(request);
                update_respon(TNoRw.getText(),TNoSEP.getText(),"klaim_baru","klaim_baru_res",requestJson,response.toString() );
                tLog.append(response+"\n------------ \n");
                System.out.println("respon klaimbaru : "+response);

                if(response.has("metadata")){
                    JSONObject code = response.getJSONObject("metadata");
                    if(code.getInt("code")==200){
                        JSONObject data = response.getJSONObject("response");
                        if (data.has("patient_id")) {
                            if(Sequel.menyimpantf2("inacbg_klaim_baru","?,?,?,?","EKLAIM",4,new String[]{
                               TNoSEP.getText(),data.getInt("patient_id")+"",data.getInt("admission_id")+"",data.getInt("hospital_admission_id")+""
                            })==false){
                                Sequel.mengedit("inacbg_klaim_baru", "no_sep='"+TNoSEP.getText()+"'", "patient_id='"+data.getInt("patient_id")+"',admission_id='"+data.getInt("admission_id")+"',hospital_admission_id='"+data.getInt("hospital_admission_id")+"'");
                            }
                        }
                        System.out.println("RESPON : "+code.getString("message"));
                        
                    }else{
                        if (!code.getString("message").contains("Duplikasi")) {
//                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                        }
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void grouper1(){
        respon.setText("");
        respon2.setText("");
                            
        if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"grouper\",\n" +
                                            "\"stage\":\"1\",\n" +
                                            "\"grouper\":\"inacbg\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                
                update_respon(TNoRw.getText(),TNoSEP.getText(),"ina_grouper1","ina_grouper1_res",requestJson,response.toString() );
                tLog1.append(response+"\n------------ \n");
                System.out.println("response grouper1 : "+response);
                
                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        JSONObject data = response.getJSONObject("response_inacbg");
                        JSONObject trf = data.getJSONObject("cbg");
                        if (data.has("tariff")) {
                            Sequel.meghapus("inacbg_grouping_stage1", "no_sep", TNoSEP.getText());
                            Sequel.menyimpantf2("inacbg_grouping_stage1","?,?,?,?","GROUPER EKLAIM",4,new String[]{
                               TNoSEP.getText(),trf.getString("code"),trf.getString("description"),data.getDouble("tariff")+""
                            });
                            total_rupiah1.setText(Valid.SetAngka(data.getDouble("tariff")));
//                            tbResponse.setValueAt(Valid.SetAngka(data.getDouble("tariff")), 2, 2);
//                            tarifIna.setText(Valid.SetAngka(data.getDouble("tariff")));
                            lblBaseTariff.setText(Valid.SetAngka(data.getDouble("tariff")));
                        }
                        
                        respon.setText("CODE INACBG : "+trf.getString("code"));
                        respon2.setText("DESKRIPSI : "+trf.getString("description"));
                        lStatus.setText("Normal");
                        
//                        tbResponse.setValueAt(trf.getString("code"), 6, 2);
//                        tbResponse.setValueAt(trf.getString("description"), 7, 2);
                        
                        if(response.has("special_cmg_option")){
                            //kirim ke table response1
//                            addCellEditorCombo(response);
                              viewHasilGrouper1(response);
//                              hitungKlaim();
                            
                            System.out.println("spesial : "+response.getJSONArray("special_cmg_option"));
                            JSONArray spesial = response.getJSONArray("special_cmg_option");
//                            jLabel49.setVisible(true);
//                            stage_2.setVisible(true);
//                            jLabel53.setVisible(true);
                            
                            for (int i = 0; i < spesial.length(); i++) {
                                JSONObject cmg = spesial.getJSONObject(i);
                                stage_2.addItem(cmg.getString("code")+" : "+cmg.getString("description"));
                            }
                        }
                        hitungKlaim();
                    }else{
                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void grouper2(){
        if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"grouper\",\n" +
                                            "\"stage\":\"2\",\n" +
                                            "\"grouper\":\"inacbg\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\",\n" +
                                            "\"special_cmg\": \""+Cmgs.getText()+"\"\n" +
//                                            "\"special_cmg\": \""+stage_2.getSelectedItem().toString().split(":")[0].trim()+"\"\n" +
                                        "}\n" +
                                    "}";


                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                
                update_respon(TNoRw.getText(),TNoSEP.getText(),"ina_grouper2","ina_grouper2_res",requestJson,response.toString() );
                tLog1.append(response+"\n------------ \n");
                System.out.println("response grouper2 : "+response);
                
                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        JSONObject data = response.getJSONObject("response_inacbg");
                        JSONObject trf = data.getJSONObject("cbg");
//                        System.out.println("resdata : "+trf);
                        if (data.has("tariff")) {
                            HasilGrouper2(response);
                            hitungKlaim();
                            Sequel.meghapus("inacbg_grouping_stage2", "no_sep", TNoSEP.getText());
                            Sequel.menyimpantf2("inacbg_grouping_stage2","?,?,?,?","GROUPER EKLAIM",4,new String[]{
                               TNoSEP.getText(),trf.getString("code"),trf.getString("description"),data.getDouble("tariff")+""
                            });
                            total_rupiah1.setText(Valid.SetAngka(data.getDouble("tariff")));
//                            tbResponse.setValueAt(Valid.SetAngka(data.getDouble("tariff")), 2, 2);
                            
                            respon.setText("CODE INACBG : "+trf.getString("code"));
                            respon2.setText("DESKRIPSI : "+trf.getString("description"));
                            
//                            tbResponse.setValueAt(trf.getString("code"), 8, 2);
//                            tbResponse.setValueAt(trf.getString("description"), 9, 2);
                        }
                    }else{
                        System.out.println("response : "+response);
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void setdataklaim(){
        if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {            
            switch (jaminan.getSelectedIndex()) {
                case 0:
                    payor_id = 3+"";
                    payor_cd = "JKN";
                    break;
                case 1:
                    payor_id = 71+"";
                    payor_cd = "COVID-19";
                    break;
                case 2:
                    payor_id = 72+"";
                    payor_cd = "KIPI";
                    break;
                case 3:
                    payor_id = 73+"";
                    payor_cd = "BBL";
                    break;
                case 4:
                    payor_id = 74+"";
                    payor_cd = "PMR";
                    break;
                case 5:
                    payor_id = 75+"";
                    payor_cd = "CO-INS";
                    break;
                case 6:
                    payor_id = 76+"";
                    payor_cd = "JPS";
                    break;
                case 7:
                    payor_id = 77+"";
                    payor_cd = "JPKP";
                    break;
                default:
                    payor_id = 3+"";
                    payor_cd = "JKN";
                    break;
            }
            try {
//                
                String requestJson =   "{\n"+
                                        "  \"metadata\": {\n" +
                                        "        \"method\": \"set_claim_data\",\n" +
                                        "        \"nomor_sep\": \""+TNoSEP.getText()+"\"\n" +
                                        "    },\n" +
                                        "    \"data\": {\n" +
                                        "        \"nomor_sep\": \""+TNoSEP.getText()+"\",\n" +
                                        "        \"nomor_kartu\": \""+Tnoka.getText()+"\",\n" +
                                        "        \"tgl_masuk\": \""+tgl_masuk.getSelectedItem()+" "+jam_masuk.getSelectedItem()+":"+menit_masuk.getSelectedItem()+":00\",\n" +
                                        "        \"tgl_pulang\": \""+tgl_keluar.getSelectedItem()+" "+jam_keluar.getSelectedItem()+":"+menit_keluar.getSelectedItem()+":00\",\n" +
                                        "        \"cara_masuk\": \""+cara_masuk.getSelectedItem().toString().split(":")[0].trim()+"\",\n" +
                                        "        \"jenis_rawat\": \""+jenis_rawat.getSelectedItem().toString().split(":")[0].trim()+"\",\n" +
                                        "        \"kelas_rawat\": \""+hak_kelas.getSelectedItem().toString().split(" ")[1].trim()+"\",\n" +
                                        "        \"adl_sub_acute\": \""+sub_acute.getText()+"\",\n" +
                                        "        \"adl_chronic\": \""+chronic.getText()+"\",\n" +
                                        "        \"icu_indikator\": \""+(rawat_intensif.isSelected() == true ? 1 : 0)+"\",\n" +
                                                 
                                        (rawat_intensif.isSelected() == true ? 
                                        "        \"icu_los\": \""+hari_intensif.getText()+"\",\n" +
                                        "        \"ventilator_hour\": \""+jam_ventilator.getText()+"\",\n" +
                                        "        \"ventilator\": {\n" +
                                        "            \"use_ind\": \""+(ventilator.isSelected()==true ? 1 : 0)+"\",\n" +
                                        "            \"start_dttm\": \""+tgl_intubasi.getSelectedItem()+" "+jam_intubasi.getSelectedItem()+":"+menit_intubasi.getSelectedItem()+":00\",\n" +
                                        "            \"stop_dttm\": \""+tgl_ekstubasi.getSelectedItem()+" "+jam_ekstubasi.getSelectedItem()+":"+menit_ekstubasi.getSelectedItem()+":00\"\n" +
                                        "        },\n"  
                                        : "") + 
                        
                        
                                        "        \"upgrade_class_ind\": \""+(naik_turun_kelas.isSelected()==true ? 1 : 0)+"\",\n" +
                        
                                        (naik_turun_kelas.isSelected()==true ? 
                                        "        \"upgrade_class_class\": \""+naik_kelas_pelayanan.getSelectedItem().toString().split(":")[0].trim()+"\",\n" + 
                                        "        \"upgrade_class_los\": \""+lama_naik_kelas.getText().trim()+"\",\n" +
                                        "        \"upgrade_class_payor\": \"peserta\",\n" +
                                        "        \"add_payment_pct\": \"35\",\n" 
                                        : "") +
                                        
                                        "        \"sistole\": \""+sistole.getText()+"\",\n" +
                                        "        \"diastole\": \""+distole.getText()+"\",\n" +
                                        "        \"discharge_status\": \""+status_pulang.getSelectedItem().toString().split(":")[0].trim()+"\",\n" +
                                        
                                        (check_hd.isSelected()==true ? 
                                        "        \"dializer_single_use\": \""+pasien_hd.getSelectedIndex()+"\",\n" +
                                        "        \"kantong_darah\": \""+kt_darah.getText().trim()+"\",\n" 
                                        : "") +
                        
                                        "        \"birth_weight\": \""+berat_bayi.getText()+"\",\n" +
//                                        "        \"diagnosa\": \""+icd10.getText()+"\",\n" +
//                                        "        \"procedure\": \""+icd9.getText()+"\",\n" +
//                                        "        \"diagnosa_inagrouper\": \""+icd10.getText()+"\",\n" +
//                                        "        \"procedure_inagrouper\": \""+icd9.getText()+"\",\n" +
                                        "        \"tarif_rs\": {\n" +
                                        "            \"prosedur_non_bedah\": \""+pnb.getText()+"\",\n" +
                                        "            \"prosedur_bedah\": \""+pb.getText()+"\",\n" +
                                        "            \"konsultasi\": \""+ksl.getText()+"\",\n" +
                                        "            \"tenaga_ahli\": \""+ahli.getText()+"\",\n" +
                                        "            \"keperawatan\": \""+kpr.getText()+"\",\n" +
                                        "            \"penunjang\": \""+pnj.getText()+"\",\n" +
                                        "            \"radiologi\": \""+rad.getText()+"\",\n" +
                                        "            \"laboratorium\": \""+lab.getText()+"\",\n" +
                                        "            \"pelayanan_darah\": \""+pl_darah.getText()+"\",\n" +
                                        "            \"rehabilitasi\": \""+rehab.getText()+"\",\n" +
                                        "            \"kamar\": \""+akomodasi.getText()+"\",\n" +
                                        "            \"rawat_intensif\": \""+ri.getText()+"\",\n" +
                                        "            \"obat\": \""+obat.getText()+"\",\n" +
                                        "            \"obat_kronis\": \""+kronis.getText()+"\",\n" +
                                        "            \"obat_kemoterapi\": \""+obat_kemo.getText()+"\",\n" +
                                        "            \"alkes\": \""+alkes.getText()+"\",\n" +
                                        "            \"bmhp\": \""+bmhp.getText()+"\",\n" +
                                        "            \"sewa_alat\": \""+sewa_alat.getText()+"\"\n" +
                                        "        },\n" +
//                                        "        \"pemulasaraan_jenazah\": \"1\",\n" +
//                                        "        \"kantong_jenazah\": \"1\",\n" +
//                                        "        \"peti_jenazah\": \"1\",\n" +
//                                        "        \"plastik_erat\": \"1\",\n" +
//                                        "        \"desinfektan_jenazah\": \"1\",\n" +
//                                        "        \"mobil_jenazah\": \"0\",\n" +
//                                        "        \"desinfektan_mobil_jenazah\": \"0\",\n" +
//                                        "        \"covid19_status_cd\": \"1\",\n" +
//                                        "        \"nomor_kartu_t\": \"nik\",\n" +
//                                        "        \"episodes\": \"1;12#2;3#6;5\",\n" +
//                                        "        \"covid19_cc_ind\": \"1\",\n" +
//                                        "        \"covid19_rs_darurat_ind\": \"1\",\n" +
//                                        "        \"covid19_co_insidense_ind\": \"1\",\n" +
//                                        "        \"covid19_penunjang_pengurang\": {\n" +
//                                        "            \"lab_asam_laktat\": \"1\",\n" +
//                                        "            \"lab_procalcitonin\": \"1\",\n" +
//                                        "            \"lab_crp\": \"1\",\n" +
//                                        "            \"lab_kultur\": \"1\",\n" +
//                                        "            \"lab_d_dimer\": \"1\",\n" +
//                                        "            \"lab_pt\": \"1\",\n" +
//                                        "            \"lab_aptt\": \"1\",\n" +
//                                        "            \"lab_waktu_pendarahan\": \"1\",\n" +
//                                        "            \"lab_anti_hiv\": \"1\",\n" +
//                                        "            \"lab_analisa_gas\": \"1\",\n" +
//                                        "            \"lab_albumin\": \"1\",\n" +
//                                        "            \"rad_thorax_ap_pa\": \"0\"\n" +
//                                        "        },\n" +
//                                        "        \"terapi_konvalesen\": \"1000000\",\n" +
//                                        "        \"akses_naat\": \"C\",\n" +
//                                        "        \"isoman_ind\": \"0\",\n" +
//                                        "        \"bayi_lahir_status_cd\": 1,\n" +
//                                        "        \"alteplase_ind\": 0,\n" +
//                                        "        \"apgar\": {\n" +
//                                        "            \"menit_1\": {\n" +
//                                        "                \"appearance\": "+ap_1.getText()+",\n" +
//                                        "                \"pulse\": "+ap_2.getText()+",\n" +
//                                        "                \"grimace\": "+ap_3.getText()+",\n" +
//                                        "                \"activity\": "+ap_4.getText()+",\n" +
//                                        "                \"respiration\": "+ap_5.getText()+"\n" +
//                                        "            },\n" +
//                                        "            \"menit_5\": {\n" +
//                                        "                \"appearance\": "+ap_6.getText()+",\n" +
//                                        "                \"pulse\": "+ap_7.getText()+",\n" +
//                                        "                \"grimace\": "+ap_8.getText()+",\n" +
//                                        "                \"activity\": "+ap_9.getText()+",\n" +
//                                        "                \"respiration\": "+ap_10.getText()+"\n" +
//                                        "            }\n" +
//                                        "        },\n" +
//                                        "        \"persalinan\": {\n" +
//                                        "            \"usia_kehamilan\": \"22\",\n" +
//                                        "            \"gravida\": \"2\",\n" +
//                                        "            \"partus\": \"4\",\n" +
//                                        "            \"abortus\": \"2\",\n" +
//                                        "            \"onset_kontraksi\": \"induksi\",\n" +
//                                        "            \"delivery\": [\n" +
//                                        "                {\n" +
//                                        "                    \"delivery_sequence\": \"1\",\n" +
//                                        "                    \"delivery_method\": \"vaginal\",\n" +
//                                        "                    \"delivery_dttm\": \"2023-01-21 17:01:33\",\n" +
//                                        "                    \"letak_janin\": \"kepala\",\n" +
//                                        "                    \"kondisi\": \"livebirth\",\n" +
//                                        "                    \"use_manual\": \"1\",\n" +
//                                        "                    \"use_forcep\": \"0\",\n" +
//                                        "                    \"use_vacuum\": \"1\",\n" +
//                                        "                    \"shk_spesimen_ambil\": \"ya\",\n" +
//                                        "                    \"shk_lokasi\": \"tumit\",\n" +
//                                        "                    \"shk_spesimen_dttm\": \"2023-01-21 18:11:33\"\n" +
//                                        "                },\n" +
//                                        "                {\n" +
//                                        "                    \"delivery_sequence\": \"2\",\n" +
//                                        "                    \"delivery_method\": \"vaginal\",\n" +
//                                        "                    \"delivery_dttm\": \"2023-01-21 17:03:49\",\n" +
//                                        "                    \"letak_janin\": \"lintang\",\n" +
//                                        "                    \"kondisi\": \"livebirth\",\n" +
//                                        "                    \"use_manual\": \"1\",\n" +
//                                        "                    \"use_forcep\": \"0\",\n" +
//                                        "                    \"use_vacuum\": \"0\",\n" +
//                                        "                    \"shk_spesimen_ambil\": \"tidak\",\n" +
//                                        "                    \"shk_alasan\": \"akses-sulit\"\n" +
//                                        "                }\n" +
//                                        "            ]\n" +
//                                        "        },\n" +
                                        "        \"tarif_poli_eks\": \""+(tarif_eksekutif.getText().equals(0) ? 0 : tarif_eksekutif.getText())+"\",\n" +
                                        "        \"nama_dokter\": \""+dpjp.getText()+"\",\n" +
                                        "        \"kode_tarif\": \""+koneksiDB.KELASRSEKLAIM()+"\",\n" +
                                        "        \"payor_id\": \""+payor_id+"\",\n" +
                                        "        \"payor_cd\": \""+payor_cd+"\",\n" +
                                        "        \"cob_cd\": \""+cob.getSelectedItem().toString().split(":")[0].trim()+"\",\n" +
                                        "        \"coder_nik\": \""+coder+"\"\n" +
                                        "    \n}"
                                    + " }\n";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);

                JSONObject response = inacbg.request(request);
                update_respon(TNoRw.getText(),TNoSEP.getText(),"data_klaim","data_klaim_res",requestJson,response.toString() );
                tLog.append(response+"\n------------ \n");
                System.out.println("response data klaim : "+response); 
                
                
                if(response.has("metadata")){
                    JSONObject code = response.getJSONObject("metadata");
                    if(code.getInt("code")==200){
//                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        Sequel.meghapus("inacbg_data_terkirim", "no_sep", TNoSEP.getText());
                        Sequel.menyimpantf2("inacbg_data_terkirim","?,?","DATA EKLAIM",2,new String[]{
                               TNoSEP.getText(),coder
                        });                        
                    }else{
//                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }
                
            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    
    private void validasi_tbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validasi_tbActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"YAKIN VALIDASI SITB PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (pasien_tb.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Silahkan isi nomor sitb pasien terlebih dahulu..!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson = "{\n"
                                       + " \"metadata\": {\n"
                                       + " \"method\": \"sitb_validate\"\n"
                                       + "},\n"
                                       + " \"data\": {\n"
                                       + " \"nomor_sep\": \""+TNoSEP.getText()+"\",\n"
                                       + " \"nomor_register_sitb\": \"" + pasien_tb.getText().trim() + "\"\n"
                                       + " }\n"
                                       + "}";

                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);

                    JSONObject response = inacbg.request(request);
                    System.out.println("response data klaim : "+response); 

                    if(response.has("metadata")){
                        JSONObject code = response.getJSONObject("metadata");
                        if (code.getInt("code") == 200) {
                            JSONObject res = response.getJSONObject("response");
                            if(res.getString("status").equals("VALID")){
                                JOptionPane.showMessageDialog(null,"RESPON : STATUS PASIEN "+TPasien.getText().trim()+" ( "+res.getString("status")+" )");
                                Sequel.menyimpan("inacbg_validasi_tb","'"+TNoRM.getText()+"','"+pasien_tb.getText().trim()+"'");
                            }else{
                                JOptionPane.showMessageDialog(null,"RESPON : "+res.getString("detail"));
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_validasi_tbActionPerformed

    private void jenis_rawatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jenis_rawatItemStateChanged
        // TODO add your handling code here:
        if (jenis_rawat.getSelectedIndex() == 0) {
            naik_turun_kelas.setVisible(true);
            rawat_intensif.setVisible(true);
            kelas_eksekutif.setVisible(false);
            jLabel22.setVisible(true);
            validasi_tb.setVisible(true);
            pasien_tb.setVisible(true);
            kelas_eksekutif.setSelected(false);
            jLabel47.setVisible(false);
            naik_kelas_pelayanan.setVisible(false);
        } else {
            naik_turun_kelas.setVisible(false);
            rawat_intensif.setVisible(false);
            kelas_eksekutif.setVisible(true);
            jLabel22.setVisible(true);
            validasi_tb.setVisible(true);
            pasien_tb.setVisible(true);
            kelas_eksekutif.setSelected(false);
            jLabel47.setVisible(true);
            naik_kelas_pelayanan.setVisible(true);
        }
    }//GEN-LAST:event_jenis_rawatItemStateChanged

    private void rawat_intensifItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rawat_intensifItemStateChanged
        // TODO add your handling code here:
        if (rawat_intensif.isSelected() == true) {
//            jLabel45.setVisible(true);
            ventilator.setVisible(true);
            jLabel63.setVisible(true);
            hari_intensif.setVisible(true);
            ventilatorItemStateChanged(null);
        } else {
//            jLabel45.setVisible(false);
            ventilator.setVisible(false);
            jLabel63.setVisible(false);
            hari_intensif.setVisible(false);
            ventilatorItemStateChanged(null);
        }
    }//GEN-LAST:event_rawat_intensifItemStateChanged

    private void naik_turun_kelasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_naik_turun_kelasItemStateChanged
        // TODO add your handling code here:
        if (naik_turun_kelas.isSelected() == true) {
           jLabel47.setVisible(true);
           naik_kelas_pelayanan.setVisible(true);
           jLabel48.setVisible(true);
           lama_naik_kelas.setVisible(true);
        } else {
           jLabel47.setVisible(false);
           naik_kelas_pelayanan.setVisible(false);
           jLabel48.setVisible(false);
           lama_naik_kelas.setVisible(false); 
        }
    }//GEN-LAST:event_naik_turun_kelasItemStateChanged

    private void kelas_eksekutifItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_kelas_eksekutifItemStateChanged
        // TODO add your handling code here:
        if (kelas_eksekutif.isSelected() == true) {
           jLabel13.setVisible(true);
           tarif_eksekutif.setVisible(true);
        } else {
           jLabel13.setVisible(false);
           tarif_eksekutif.setVisible(false);
        }
    }//GEN-LAST:event_kelas_eksekutifItemStateChanged

    private void ventilatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ventilatorItemStateChanged
        // TODO add your handling code here:
        if (ventilator.isSelected() == true) {
            tgl_intubasi.setVisible(true);
            jam_intubasi.setVisible(true);
            menit_intubasi.setVisible(true);
            tgl_ekstubasi.setVisible(true);
            jam_ekstubasi.setVisible(true);
            menit_ekstubasi.setVisible(true);
            jLabel15.setVisible(true);
            jLabel62.setVisible(true);
            jLabel65.setVisible(true);
            jam_ventilator.setVisible(true);
        } else {
            tgl_intubasi.setVisible(false);
            jam_intubasi.setVisible(false);
            menit_intubasi.setVisible(false);
            tgl_ekstubasi.setVisible(false);
            jam_ekstubasi.setVisible(false);
            menit_ekstubasi.setVisible(false);
            jLabel15.setVisible(false);
            jLabel62.setVisible(false);
            jLabel65.setVisible(false);
            jam_ventilator.setVisible(false);
        }
    }//GEN-LAST:event_ventilatorItemStateChanged

    private void pbKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pbKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_pbKeyPressed

    private void kprKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kprKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_kprKeyPressed

    private void labKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_labKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_labKeyPressed

    private void akomodasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_akomodasiKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_akomodasiKeyPressed

    private void kronisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kronisKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_kronisKeyPressed

    private void bmhpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bmhpKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_bmhpKeyPressed

    private void kslKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kslKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_kslKeyPressed

    private void pnjKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnjKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_pnjKeyPressed

    private void pl_darahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pl_darahKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_pl_darahKeyPressed

    private void riKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_riKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_riKeyPressed

    private void obat_kemoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_obat_kemoKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_obat_kemoKeyPressed

    private void sewa_alatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sewa_alatKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_sewa_alatKeyPressed

    private void pnbKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnbKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_pnbKeyReleased

    private void ahliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ahliKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_ahliKeyReleased

    private void radKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_radKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_radKeyReleased

    private void rehabKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rehabKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_rehabKeyReleased

    private void obatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_obatKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_obatKeyReleased

    private void alkesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alkesKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_alkesKeyReleased

    private void pnbKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnbKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_pnbKeyTyped

    private void kirimonlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kirimonlineActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU KIRIM ONLINE PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            try {
                if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
                } else {
                    try {
                        TabRawat.setSelectedIndex(1);
                        // Create a human-readable JSON string
                        String requestJson ="{\n" +
                                                "\"metadata\": {\n" +
                                                    "\"method\":\"send_claim_individual\"\n" +
                                                "},\n" +
                                                "\"data\": {\n" +
                                                    "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\"\n" +
                                                "}\n" +
                                            "}";


                        // Use the JSON string in your request
                        String request = requestJson;
                        System.out.println("request JSON : "+requestJson);
                        JSONObject response = inacbg.request(request);
                        JSONObject code = response.getJSONObject("metadata");
                        
//                        update_respon(TNoRw.getText(),TNoSEP.getText(),"ina_final","ina_final_res",requestJson,response.toString() );
                        tLog1.append(response+"\n------------ \n");
                        System.out.println("response kirim online : "+response);

                        if(response.has("metadata")){
                            if(code.getInt("code")==200){
//                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                                Toast.show("RESPON : "+code.getString("message"), ToastType.SUCCESS);
                            }else{
//                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                                Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                            }
                        } else {
                            System.out.println("response : "+response); 
                            System.err.println("\n The 'data' field is missing in the response.");
                        }

                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }catch (Exception ex) {
                System.out.println("Notifikasi Bridging Hapus : "+ex);
            }
        }
    }//GEN-LAST:event_kirimonlineActionPerformed

    private void chk_diag_5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_5ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_5.isSelected()==true && !kd_diag_5.getText().trim().isEmpty()) {
            chk_diag_4.setSelected(false);
            chk_diag_3.setSelected(false);
            chk_diag_2.setSelected(false);
            chk_diag_1.setSelected(false);
            chk_diag_6.setSelected(false);
            chk_diag_7.setSelected(false);
            chk_diag_8.setSelected(false);
            chk_diag_9.setSelected(false);
            chk_diag_10.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_5.getText().trim()+(kd_diag_1.getText().trim().equals("") ? "" : "#"+kd_diag_1.getText().trim())+
                    (kd_diag_2.getText().trim().equals("") ? "" : "#"+kd_diag_2.getText().trim())+(kd_diag_3.getText().trim().equals("") ? "" : "#"+kd_diag_3.getText().trim())+
                    (kd_diag_4.getText().trim().equals("") ? "" : "#"+kd_diag_4.getText().trim())+(kd_diag_6.getText().trim().equals("") ? "" : "#"+kd_diag_6.getText().trim())+
                    (kd_diag_7.getText().trim().equals("") ? "" : "#"+kd_diag_7.getText().trim())+(kd_diag_8.getText().trim().equals("") ? "" : "#"+kd_diag_8.getText().trim())+
                    (kd_diag_9.getText().trim().equals("") ? "" : "#"+kd_diag_9.getText().trim())+(kd_diag_10.getText().trim().equals("") ? "" : "#"+kd_diag_10.getText().trim()));
        }else{
            chk_diag_5.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_5ItemStateChanged

    private void chk_diag_1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_1ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_1.isSelected()==true && !kd_diag_1.getText().trim().isEmpty()) {
            chk_diag_2.setSelected(false);
            chk_diag_3.setSelected(false);
            chk_diag_4.setSelected(false);
            chk_diag_5.setSelected(false);
            chk_diag_6.setSelected(false);
            chk_diag_7.setSelected(false);
            chk_diag_8.setSelected(false);
            chk_diag_9.setSelected(false);
            chk_diag_10.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_1.getText().trim()+(kd_diag_2.getText().trim().equals("") ? "" : "#"+kd_diag_2.getText().trim())+
                    (kd_diag_3.getText().trim().equals("") ? "" : "#"+kd_diag_3.getText().trim())+(kd_diag_4.getText().trim().equals("") ? "" : "#"+kd_diag_4.getText().trim())+
                    (kd_diag_5.getText().trim().equals("") ? "" : "#"+kd_diag_5.getText().trim())+(kd_diag_6.getText().trim().equals("") ? "" : "#"+kd_diag_6.getText().trim())+
                    (kd_diag_7.getText().trim().equals("") ? "" : "#"+kd_diag_7.getText().trim())+(kd_diag_8.getText().trim().equals("") ? "" : "#"+kd_diag_8.getText().trim())+
                    (kd_diag_9.getText().trim().equals("") ? "" : "#"+kd_diag_9.getText().trim())+(kd_diag_10.getText().trim().equals("") ? "" : "#"+kd_diag_10.getText().trim()));
        }else{
            chk_diag_1.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_1ItemStateChanged

    private void chk_diag_2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_2ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_2.isSelected()==true && !kd_diag_2.getText().trim().isEmpty()) {
            chk_diag_1.setSelected(false);
            chk_diag_3.setSelected(false);
            chk_diag_4.setSelected(false);
            chk_diag_5.setSelected(false);
            chk_diag_6.setSelected(false);
            chk_diag_7.setSelected(false);
            chk_diag_8.setSelected(false);
            chk_diag_9.setSelected(false);
            chk_diag_10.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_2.getText().trim()+(kd_diag_1.getText().trim().equals("") ? "" : "#"+kd_diag_1.getText().trim())+
                    (kd_diag_3.getText().trim().equals("") ? "" : "#"+kd_diag_3.getText().trim())+(kd_diag_4.getText().trim().equals("") ? "" : "#"+kd_diag_4.getText().trim())+
                    (kd_diag_5.getText().trim().equals("") ? "" : "#"+kd_diag_5.getText().trim())+(kd_diag_6.getText().trim().equals("") ? "" : "#"+kd_diag_6.getText().trim())+
                    (kd_diag_7.getText().trim().equals("") ? "" : "#"+kd_diag_7.getText().trim())+(kd_diag_8.getText().trim().equals("") ? "" : "#"+kd_diag_8.getText().trim())+
                    (kd_diag_9.getText().trim().equals("") ? "" : "#"+kd_diag_9.getText().trim())+(kd_diag_10.getText().trim().equals("") ? "" : "#"+kd_diag_10.getText().trim()));
        }else{
            chk_diag_2.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_2ItemStateChanged

    private void chk_diag_3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_3ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_3.isSelected()==true && !kd_diag_3.getText().trim().isEmpty()) {
            chk_diag_1.setSelected(false);
            chk_diag_2.setSelected(false);
            chk_diag_4.setSelected(false);
            chk_diag_5.setSelected(false);
            chk_diag_6.setSelected(false);
            chk_diag_7.setSelected(false);
            chk_diag_8.setSelected(false);
            chk_diag_9.setSelected(false);
            chk_diag_10.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_3.getText().trim()+(kd_diag_1.getText().trim().equals("") ? "" : "#"+kd_diag_1.getText().trim())+
                    (kd_diag_2.getText().trim().equals("") ? "" : "#"+kd_diag_2.getText().trim())+(kd_diag_4.getText().trim().equals("") ? "" : "#"+kd_diag_4.getText().trim())+
                    (kd_diag_5.getText().trim().equals("") ? "" : "#"+kd_diag_5.getText().trim())+(kd_diag_6.getText().trim().equals("") ? "" : "#"+kd_diag_6.getText().trim())+
                    (kd_diag_7.getText().trim().equals("") ? "" : "#"+kd_diag_7.getText().trim())+(kd_diag_8.getText().trim().equals("") ? "" : "#"+kd_diag_8.getText().trim())+
                    (kd_diag_9.getText().trim().equals("") ? "" : "#"+kd_diag_9.getText().trim())+(kd_diag_10.getText().trim().equals("") ? "" : "#"+kd_diag_10.getText().trim()));
        }else{
            chk_diag_3.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_3ItemStateChanged

    private void chk_diag_4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_4ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_4.isSelected()==true && !kd_diag_4.getText().trim().isEmpty()) {
            chk_diag_2.setSelected(false);
            chk_diag_3.setSelected(false);
            chk_diag_1.setSelected(false);
            chk_diag_5.setSelected(false);
            chk_diag_6.setSelected(false);
            chk_diag_7.setSelected(false);
            chk_diag_8.setSelected(false);
            chk_diag_9.setSelected(false);
            chk_diag_10.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_4.getText().trim()+(kd_diag_1.getText().trim().equals("") ? "" : "#"+kd_diag_1.getText().trim())+
                    (kd_diag_2.getText().trim().equals("") ? "" : "#"+kd_diag_2.getText().trim())+(kd_diag_3.getText().trim().equals("") ? "" : "#"+kd_diag_3.getText().trim())+
                    (kd_diag_5.getText().trim().equals("") ? "" : "#"+kd_diag_5.getText().trim())+(kd_diag_6.getText().trim().equals("") ? "" : "#"+kd_diag_6.getText().trim())+
                    (kd_diag_7.getText().trim().equals("") ? "" : "#"+kd_diag_7.getText().trim())+(kd_diag_8.getText().trim().equals("") ? "" : "#"+kd_diag_8.getText().trim())+
                    (kd_diag_9.getText().trim().equals("") ? "" : "#"+kd_diag_9.getText().trim())+(kd_diag_10.getText().trim().equals("") ? "" : "#"+kd_diag_10.getText().trim()));
        }else{
            chk_diag_4.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_4ItemStateChanged

    private void finalklaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalklaimActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU FINAL KLAIM PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
            } else {
                try {
                    TabRawat.setSelectedIndex(1);
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                                            "\"metadata\": {\n" +
                                                "\"method\":\"claim_final\"\n" +
                                            "},\n" +
                                            "\"data\": {\n" +
                                                "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\",\n" +
                                                "\"coder_nik\": \""+coder+"\"\n" +
                                            "}\n" +
                                        "}";


                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);
                    JSONObject response = inacbg.request(request);
                    JSONObject code = response.getJSONObject("metadata");
                    
                    update_respon(TNoRw.getText(),TNoSEP.getText(),"ina_final","ina_final_res",requestJson,response.toString() );
                    tLog1.append(response+"\n------------ \n");
                    System.out.println("response final inacbg : "+response);

                    if(response.has("metadata")){
                        if(code.getInt("code")==200){
                            if(code.getInt("code")==200){
//                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                                Toast.show("RESPON : "+code.getString("message"), ToastType.SUCCESS);
                                Sequel.menyimpan("inacbg_grouping_final", "'"+TNoSEP.getText().trim()+"'");
                            }else{
                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            }
                        }else{
//                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_finalklaimActionPerformed

    private void hapusKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusKlaimActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU HAPUS KLAIM PASIEN "+TPasien.getText()+" (KLAIM INI AKAN TERHAPUS !!!!!!) ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                                            "\"metadata\": {\n" +
                                                "\"method\":\"delete_claim\"\n" +
                                            "},\n" +
                                            "\"data\": {\n" +
                                                "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\",\n" +
                                                "\"coder_nik\": \""+coder+"\"\n" +
                                            "}\n" +
                                        "}";


                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);
                    JSONObject response = inacbg.request(request);
                    JSONObject code = response.getJSONObject("metadata");
                    
                    update_respon(TNoRw.getText(),TNoSEP.getText(),"delete_klaim","delete_klaim_res",requestJson,response.toString() );
                    System.out.println("response hapus : "+response);

                    if(response.has("metadata")){
                        if(code.getInt("code")==200){
//                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            Toast.show("RESPON : "+code.getString("message"), ToastType.SUCCESS);
                            
                            Sequel.meghapus("inacbg_data_terkirim", "no_sep", TNoSEP.getText());
                            Sequel.meghapus("inacbg_grouping_stage1", "no_sep", TNoSEP.getText());
                            Sequel.meghapus("inacbg_grouping_stage2", "no_sep", TNoSEP.getText());
                            Sequel.meghapus("inacbg_grouping_final", "no_sep", TNoSEP.getText());
                        }else{
//                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_hapusKlaimActionPerformed

    private void hapusKlaimKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hapusKlaimKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_hapusKlaimKeyPressed

    private void editKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editKlaimActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU EDIT KLAIM PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
            } else {
                try {
                    TabRawat.setSelectedIndex(1);
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                                            "\"metadata\": {\n" +
                                                "\"method\":\"reedit_claim\"\n" +
                                            "},\n" +
                                            "\"data\": {\n" +
                                                "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\"\n" +
                                            "}\n" +
                                        "}";


                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);
                    JSONObject response = inacbg.request(request);
                    JSONObject code = response.getJSONObject("metadata");
                    
                    update_respon(TNoRw.getText(),TNoSEP.getText(),"ina_edit","ina_edit_res",requestJson,response.toString() );
                    tLog1.append(response+"\n------------ \n");
                    System.out.println("response edit : "+response);

                    if(response.has("metadata")){
                        if(code.getInt("code")==200){
                            if(code.getInt("code")==200){
//                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                                Toast.show("RESPON : "+code.getString("message"), ToastType.SUCCESS);
                            }else{
                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            }
//                            simpanDxPxInacbg.setEnabled(true);
                            disableDxInacbg(true);
                        }else{
//                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_editKlaimActionPerformed

    private void batal_validasi_tbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batal_validasi_tbActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"YAKIN MAU BATAL REGISTER SITB PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (pasien_tb.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Silahkan isi nomor sitb pasien terlebih dahulu..!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson = "{\n"
                                       + " \"metadata\": {\n"
                                       + " \"method\": \"sitb_invalidate\"\n"
                                       + "},\n"
                                       + " \"data\": {\n"
                                       + " \"nomor_sep\": \""+TNoSEP.getText()+"\"\n"
                                       + " }\n"
                                       + "}";

                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);

                    JSONObject response = inacbg.request(request);
                    System.out.println("response data klaim : "+response); 

                    if(response.has("metadata")){
                        JSONObject code = response.getJSONObject("metadata");
                        JSONObject res = response.getJSONObject("metadata");
                        if(code.getInt("code")==200){
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            Sequel.meghapus("inacbg_validasi_tb","no_rkm_medis", TNoRM.getText());
                        } else {
                           JOptionPane.showMessageDialog(null,"RESPON : "+res.getString("message")); 
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        
    }//GEN-LAST:event_batal_validasi_tbActionPerformed

    private void check_hdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_check_hdItemStateChanged
        // TODO add your handling code here:
        if (check_hd.isSelected() == true) {
            jLabel56.setVisible(true);                    
            jLabel79.setVisible(true);
            pasien_hd.setVisible(true);
            kt_darah.setVisible(true);
        }else{
            jLabel56.setVisible(false);                    
            jLabel79.setVisible(false);
            pasien_hd.setVisible(false);
            kt_darah.setVisible(false); 
        }
    }//GEN-LAST:event_check_hdItemStateChanged

    private void alkesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alkesKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_alkesKeyTyped

    private void pbKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pbKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_pbKeyReleased

    private void kprKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kprKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_kprKeyReleased

    private void labKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_labKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_labKeyReleased

    private void akomodasiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_akomodasiKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_akomodasiKeyReleased

    private void kronisKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kronisKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_kronisKeyReleased

    private void bmhpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bmhpKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_bmhpKeyReleased

    private void kslKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kslKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_kslKeyReleased

    private void pnjKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnjKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_pnjKeyReleased

    private void pl_darahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pl_darahKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_pl_darahKeyReleased

    private void riKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_riKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_riKeyReleased

    private void obat_kemoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_obat_kemoKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_obat_kemoKeyReleased

    private void sewa_alatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sewa_alatKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_sewa_alatKeyReleased

    private void chk_diag_6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_6ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_6.isSelected()==true && !kd_diag_6.getText().trim().isEmpty()) {
            chk_diag_1.setSelected(false);
            chk_diag_2.setSelected(false);
            chk_diag_3.setSelected(false);
            chk_diag_4.setSelected(false);
            chk_diag_5.setSelected(false);
            chk_diag_7.setSelected(false);
            chk_diag_8.setSelected(false);
            chk_diag_9.setSelected(false);
            chk_diag_10.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_6.getText().trim()+(kd_diag_1.getText().trim().equals("") ? "" : "#"+kd_diag_1.getText().trim())+
                    (kd_diag_2.getText().trim().equals("") ? "" : "#"+kd_diag_2.getText().trim())+(kd_diag_3.getText().trim().equals("") ? "" : "#"+kd_diag_3.getText().trim())+
                    (kd_diag_4.getText().trim().equals("") ? "" : "#"+kd_diag_4.getText().trim())+(kd_diag_5.getText().trim().equals("") ? "" : "#"+kd_diag_5.getText().trim())+
                    (kd_diag_7.getText().trim().equals("") ? "" : "#"+kd_diag_7.getText().trim())+(kd_diag_8.getText().trim().equals("") ? "" : "#"+kd_diag_8.getText().trim())+
                    (kd_diag_9.getText().trim().equals("") ? "" : "#"+kd_diag_9.getText().trim())+(kd_diag_10.getText().trim().equals("") ? "" : "#"+kd_diag_10.getText().trim()));
        }else{
            chk_diag_6.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_6ItemStateChanged

    private void chk_diag_7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_7ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_7.isSelected()==true && !kd_diag_7.getText().trim().isEmpty()) {
            chk_diag_1.setSelected(false);
            chk_diag_2.setSelected(false);
            chk_diag_3.setSelected(false);
            chk_diag_4.setSelected(false);
            chk_diag_5.setSelected(false);
            chk_diag_6.setSelected(false);
            chk_diag_8.setSelected(false);
            chk_diag_9.setSelected(false);
            chk_diag_10.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_7.getText().trim()+(kd_diag_1.getText().trim().equals("") ? "" : "#"+kd_diag_1.getText().trim())+
                    (kd_diag_2.getText().trim().equals("") ? "" : "#"+kd_diag_2.getText().trim())+(kd_diag_3.getText().trim().equals("") ? "" : "#"+kd_diag_3.getText().trim())+
                    (kd_diag_4.getText().trim().equals("") ? "" : "#"+kd_diag_4.getText().trim())+(kd_diag_5.getText().trim().equals("") ? "" : "#"+kd_diag_5.getText().trim())+
                    (kd_diag_6.getText().trim().equals("") ? "" : "#"+kd_diag_6.getText().trim())+(kd_diag_8.getText().trim().equals("") ? "" : "#"+kd_diag_8.getText().trim())+
                    (kd_diag_9.getText().trim().equals("") ? "" : "#"+kd_diag_9.getText().trim())+(kd_diag_10.getText().trim().equals("") ? "" : "#"+kd_diag_10.getText().trim()));
        }else{
            chk_diag_7.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_7ItemStateChanged

    private void chk_diag_8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_8ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_8.isSelected()==true && !kd_diag_8.getText().trim().isEmpty()) {
            chk_diag_1.setSelected(false);
            chk_diag_2.setSelected(false);
            chk_diag_3.setSelected(false);
            chk_diag_4.setSelected(false);
            chk_diag_5.setSelected(false);
            chk_diag_6.setSelected(false);
            chk_diag_7.setSelected(false);
            chk_diag_9.setSelected(false);
            chk_diag_10.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_8.getText().trim()+(kd_diag_1.getText().trim().equals("") ? "" : "#"+kd_diag_1.getText().trim())+
                    (kd_diag_2.getText().trim().equals("") ? "" : "#"+kd_diag_2.getText().trim())+(kd_diag_3.getText().trim().equals("") ? "" : "#"+kd_diag_3.getText().trim())+
                    (kd_diag_4.getText().trim().equals("") ? "" : "#"+kd_diag_4.getText().trim())+(kd_diag_5.getText().trim().equals("") ? "" : "#"+kd_diag_5.getText().trim())+
                    (kd_diag_6.getText().trim().equals("") ? "" : "#"+kd_diag_6.getText().trim())+(kd_diag_7.getText().trim().equals("") ? "" : "#"+kd_diag_7.getText().trim())+
                    (kd_diag_9.getText().trim().equals("") ? "" : "#"+kd_diag_9.getText().trim())+(kd_diag_10.getText().trim().equals("") ? "" : "#"+kd_diag_10.getText().trim()));
        }else{
            chk_diag_8.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_8ItemStateChanged

    private void chk_diag_9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_9ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_9.isSelected()==true && !kd_diag_9.getText().trim().isEmpty()) {
            chk_diag_1.setSelected(false);
            chk_diag_2.setSelected(false);
            chk_diag_3.setSelected(false);
            chk_diag_4.setSelected(false);
            chk_diag_5.setSelected(false);
            chk_diag_6.setSelected(false);
            chk_diag_7.setSelected(false);
            chk_diag_8.setSelected(false);
            chk_diag_10.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_9.getText().trim()+(kd_diag_1.getText().trim().equals("") ? "" : "#"+kd_diag_1.getText().trim())+
                    (kd_diag_2.getText().trim().equals("") ? "" : "#"+kd_diag_2.getText().trim())+(kd_diag_3.getText().trim().equals("") ? "" : "#"+kd_diag_3.getText().trim())+
                    (kd_diag_4.getText().trim().equals("") ? "" : "#"+kd_diag_4.getText().trim())+(kd_diag_5.getText().trim().equals("") ? "" : "#"+kd_diag_5.getText().trim())+
                    (kd_diag_6.getText().trim().equals("") ? "" : "#"+kd_diag_6.getText().trim())+(kd_diag_7.getText().trim().equals("") ? "" : "#"+kd_diag_7.getText().trim())+
                    (kd_diag_8.getText().trim().equals("") ? "" : "#"+kd_diag_8.getText().trim())+(kd_diag_10.getText().trim().equals("") ? "" : "#"+kd_diag_10.getText().trim()));
        }else{
            chk_diag_9.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_9ItemStateChanged

    private void chk_diag_10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_10ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_10.isSelected()==true && !kd_diag_10.getText().trim().isEmpty()) {
            chk_diag_1.setSelected(false);
            chk_diag_2.setSelected(false);
            chk_diag_3.setSelected(false);
            chk_diag_4.setSelected(false);
            chk_diag_5.setSelected(false);
            chk_diag_6.setSelected(false);
            chk_diag_7.setSelected(false);
            chk_diag_8.setSelected(false);
            chk_diag_9.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_10.getText().trim()+(kd_diag_1.getText().trim().equals("") ? "" : "#"+kd_diag_1.getText().trim())+
                    (kd_diag_2.getText().trim().equals("") ? "" : "#"+kd_diag_2.getText().trim())+(kd_diag_3.getText().trim().equals("") ? "" : "#"+kd_diag_3.getText().trim())+
                    (kd_diag_4.getText().trim().equals("") ? "" : "#"+kd_diag_4.getText().trim())+(kd_diag_5.getText().trim().equals("") ? "" : "#"+kd_diag_5.getText().trim())+
                    (kd_diag_6.getText().trim().equals("") ? "" : "#"+kd_diag_6.getText().trim())+(kd_diag_7.getText().trim().equals("") ? "" : "#"+kd_diag_7.getText().trim())+
                    (kd_diag_8.getText().trim().equals("") ? "" : "#"+kd_diag_8.getText().trim())+(kd_diag_9.getText().trim().equals("") ? "" : "#"+kd_diag_9.getText().trim()));
        }else{
            chk_diag_10.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_10ItemStateChanged

    private void BtnGrouperIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGrouperIDRGActionPerformed
        // TODO add your handling code here:
        tombolinacbg();
        updateicd9(TNoRw.getText());
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       
        if (Sequel.cariInteger("select count(no_sep) from inacbg_klaim_baru where no_sep='"+TNoSEP.getText().trim()+"' ") == 0) {
            klaimbaru();   
        }

        setdataklaim();
        setDx("idrg"); setPx("idrg");
        
        idrg_diagnosa();
        idrg_prosedur();        
                
        grouperidrg();
        
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_BtnGrouperIDRGActionPerformed

    private void BtnGrouperIDRGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnGrouperIDRGKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnGrouperIDRGKeyPressed

    private void finalklaimIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalklaimIDRGActionPerformed
        // TODO add your handling code here: 
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU FINAL IDRG PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                                            "\"metadata\": {\n" +
                                                "\"method\":\"idrg_grouper_final\"\n" +    
                                            "},\n" +
                                            "\"data\": {\n" +
                                                "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\"\n" +
                                            "}\n" +
                                        "}";

                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);
                    JSONObject response = inacbg.request(request);
                    JSONObject code = response.getJSONObject("metadata");
                    
                    update_respon(TNoRw.getText(),TNoSEP.getText(),"idrg_final","idrg_final_res",requestJson,response.toString() );
                    tLog.append(response+"\n------------ \n");
                    System.out.println("response grouper idrg : "+response);

                    if(response.has("metadata")){
                        if(code.getInt("code")==200){
                            System.out.println("response final idrg : "+code.getString("message"));
                            Sequel.meghapus("inacbg_idrg_final", "no_sep", TNoSEP.getText());
                            Sequel.menyimpantf2("inacbg_idrg_final","?,?","DATA IDRG",2,new String[]{
                                   TNoSEP.getText(),coder
                            });
                            respon.setText("");
                            respon2.setText("");
                            tombolinacbg();
//                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
//                        }else{
//                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));

                            if(code.getString("message").equals("Ok")) {
                                txStatus.setText("Final");
                                TabRawat.setSelectedIndex(1);       
                                Toast.show("Status iDRG sudah Final", ToastType.SUCCESS);
                            } else {
//                                Toast.show(code.getString("message") , ToastType.WARNING);
                                Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                            }
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_finalklaimIDRGActionPerformed

    private void editKlaimIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editKlaimIDRGActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU EDIT IDRG PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            TabRawat.setSelectedIndex(0);
            if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                                            "\"metadata\": {\n" +
                                                "\"method\":\"idrg_grouper_reedit\"\n" +    
                                            "},\n" +
                                            "\"data\": {\n" +
                                                "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\"\n" +
                                            "}\n" +
                                        "}";

                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);
                    JSONObject response = inacbg.request(request);
                    JSONObject code = response.getJSONObject("metadata");
                    
                    update_respon(TNoRw.getText(),TNoSEP.getText(),"idrg_edit","idrg_edit_res",requestJson,response.toString() );
                    
                    update_respon(TNoRw.getText(),TNoSEP.getText(),"idrg_grouper","idrg_grouper_res","","" );
                    update_respon(TNoRw.getText(),TNoSEP.getText(),"ina_grouper1","ina_grouper1_res","","" );
                    update_respon(TNoRw.getText(),TNoSEP.getText(),"ina_grouper2","ina_grouper2_res","","" );
                    tLog.append(response+"\n------------ \n");
                    System.out.println("response grouper idrg : "+response);

                    if(response.has("metadata")){
                        if(code.getInt("code")==200){
                            System.out.println("response final idrg : "+code.getString("message"));
                            Sequel.meghapus("inacbg_idrg_final", "no_sep", TNoSEP.getText());
                            Sequel.meghapus("inacbg_final", "no_sep", TNoSEP.getText());
                            Sequel.meghapus("inacbg_grouping_stage1", "no_sep", TNoSEP.getText());
                            Sequel.meghapus("inacbg_grouping_stage2", "no_sep", TNoSEP.getText());
                            respon.setText("");
                            respon2.setText("");
                            total_rupiah1.setText("");
//                            tbResponse.setValueAt("", 2, 2);
//                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            if(code.getString("message").equals("Ok")) {
                                hapusDxPxInacbg();
                                Toast.show("iDRG Klaim Pasien Sudah bisa di Edit", ToastType.SUCCESS);
                            }
//                            setResponseAwal();
                            simpanDxPxInacbg1.setEnabled(true);
                            simpanDxPxInacbg.setEnabled(true);
                            prepareEditiDRG();
                            tombolinacbg();

                        }else{
//                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                        }
                    } else {
                        System.out.println("response : "+response); 
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_editKlaimIDRGActionPerformed

    private void finalklaim1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalklaim1ActionPerformed
        // TODO add your handling code here:
        if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                TabRawat.setSelectedIndex(1);
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"inacbg_grouper_final\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";


                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                
                update_respon(TNoRw.getText(),TNoSEP.getText(),"ina_final","ina_final_res",requestJson,response.toString() );
                tLog1.append(response+"\n------------ \n");
                System.out.println("response final inacbg : "+response);

                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        if(code.getInt("code")==200){
//                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            lStatus.setText("Final");
                            Toast.show("RESPON : "+code.getString("message"), ToastType.SUCCESS);
                            Sequel.menyimpan("inacbg_final", "'"+TNoSEP.getText().trim()+"'");
                        }else{
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        }
                        tombolinacbg();                        
                    }else{
//                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_finalklaim1ActionPerformed

    private void editKlaim1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editKlaim1ActionPerformed
        // TODO add your handling code here:
        if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                TabRawat.setSelectedIndex(1);
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"inacbg_grouper_reedit\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";


                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");

                update_respon(TNoRw.getText(),TNoSEP.getText(),"ina_edit","ina_edit_res",requestJson,response.toString() );
                tLog1.append(response+"\n------------ \n");
                System.out.println("response edit : "+response);

                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        if(code.getInt("code")==200){
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            Sequel.meghapus("inacbg_final", "no_sep", TNoSEP.getText());
//                            simpanDxPxInacbg.setEnabled(true);
                            disableDxInacbg(true);
                            tombolinacbg();
                        }else{
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_editKlaim1ActionPerformed

    private void tbComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbComboMouseClicked

    }//GEN-LAST:event_tbComboMouseClicked

    private void tbComboKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbComboKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbComboKeyPressed

    private void tbComboKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbComboKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbComboKeyReleased

    private void textDiagFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textDiagFocusGained
        //        scrollCombo.setVisible(true);
    }//GEN-LAST:event_textDiagFocusGained

    private void textDiagFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textDiagFocusLost
        //        scrollCombo.setVisible(false);
    }//GEN-LAST:event_textDiagFocusLost

    private void textDiagInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_textDiagInputMethodTextChanged

    }//GEN-LAST:event_textDiagInputMethodTextChanged

    private void textDiagKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textDiagKeyReleased
        if(textDiag.getText().length()>=1) {
            tampildiagnosa("idrg");
            cekDiagUtama();
        }
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            tampildiagnosa("idrg");
        }
    }//GEN-LAST:event_textDiagKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//                Diagnosa.setText("n18");
//                tampildiagnosa();
//                dlgCombodx.setSize(580,325);
//                dlgCombodx.setLocationRelativeTo(internalFrame1);
//                dlgCombodx.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void textProcFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textProcFocusGained
        //        scrollCombo2.setVisible(true);
    }//GEN-LAST:event_textProcFocusGained

    private void textProcFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textProcFocusLost
        //        scrollCombo2.setVisible(false);
    }//GEN-LAST:event_textProcFocusLost

    private void textProcInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_textProcInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_textProcInputMethodTextChanged

    private void textProcKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textProcKeyReleased
        if(textProc.getText().length()>=1) {
            tampilTindakan("idrg");

        }
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            tampilTindakan("idrg");
        }
    }//GEN-LAST:event_textProcKeyReleased

    private void tbCombo2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCombo2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbCombo2MouseClicked

    private void tbCombo2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbCombo2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbCombo2KeyPressed

    private void tbCombo2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbCombo2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbCombo2KeyReleased

    private void simpanDxPxIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanDxPxIDRGActionPerformed
        simpanIdrg();
        setDx("idrg"); setPx("idrg");
    }//GEN-LAST:event_simpanDxPxIDRGActionPerformed

    private void ventilatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ventilatorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ventilatorActionPerformed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        
        scrollInput.getVerticalScrollBar().setValue(337);
//        if (TabRawat.getSelectedIndex() == 0) {
//            tampilDxPx("idrg", Status.getSelectedItem().toString());
////            TCariKunjungan.setText("");
////            tampilRalan();
//        } else if (TabRawat.getSelectedIndex() == 1) {
//            tampilDxPx("inacbg", Status.getSelectedItem().toString());
////            TCariKunjungan.setText("");
////            tampilRanap();
//        }
        scrollRespon1.setPreferredSize(new java.awt.Dimension(518, 258));
        panelGlass9.setPreferredSize(new java.awt.Dimension(518, 750));
    }//GEN-LAST:event_TabRawatMouseClicked

    private void tbCombo1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCombo1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbCombo1MouseClicked

    private void tbCombo1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbCombo1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbCombo1KeyPressed

    private void tbCombo1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbCombo1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbCombo1KeyReleased

    private void textDiag2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textDiag2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_textDiag2FocusGained

    private void textDiag2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textDiag2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_textDiag2FocusLost

    private void textDiag2InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_textDiag2InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_textDiag2InputMethodTextChanged

    private void textDiag2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textDiag2KeyReleased
        if(textDiag2.getText().length()>=1) {
            tampildiagnosa("inacbg");
        }
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            tampildiagnosa("inacbg");
        }        
    }//GEN-LAST:event_textDiag2KeyReleased

    private void textProc2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textProc2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_textProc2FocusGained

    private void textProc2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textProc2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_textProc2FocusLost

    private void textProc2InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_textProc2InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_textProc2InputMethodTextChanged

    private void textProc2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textProc2KeyReleased
        if(textProc2.getText().length()>=1) {
            tampilTindakan("inacbg");
        }
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            tampilTindakan("inacbg");
        }
    }//GEN-LAST:event_textProc2KeyReleased

    private void tbCombo22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCombo22MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbCombo22MouseClicked

    private void tbCombo22KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbCombo22KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbCombo22KeyPressed

    private void tbCombo22KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbCombo22KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbCombo22KeyReleased

    private void simpanDxPxInacbgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanDxPxInacbgActionPerformed
        simpan();
        tampildiagnosa("inacbg");
        tampilTindakan("inacbg");
        if ( tbCombo1.getRowCount() != 0 ) {
            setDx("inacbg"); 
            inacbgSetDx();
        }
        if (tbCombo22.getRowCount() != 0 ) {
            setPx("inacbg");
            inacbgSetPx();
        }
    }//GEN-LAST:event_simpanDxPxInacbgActionPerformed

    private void textProcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textProcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textProcActionPerformed

    private void textDiagKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textDiagKeyPressed

    }//GEN-LAST:event_textDiagKeyPressed

    private void textProcKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textProcKeyPressed

    }//GEN-LAST:event_textProcKeyPressed

    private void textDiag2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textDiag2KeyPressed

    }//GEN-LAST:event_textDiag2KeyPressed

    private void textProc2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textProc2KeyPressed

    }//GEN-LAST:event_textProc2KeyPressed

    private void tLogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tLogKeyPressed
//        Valid.pindah2(evt,Evaluasi,BtnSimpan);
    }//GEN-LAST:event_tLogKeyPressed

    private void tLog1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tLog1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tLog1KeyPressed

    private void TabRawatStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabRawatStateChanged
//        if (TabRawat.getSelectedIndex() == 0) {
//            tampilDxPx("idrg", Status.getSelectedItem().toString());
//        } else if (TabRawat.getSelectedIndex() == 1) {
//            tampilDxPx("inacbg", Status.getSelectedItem().toString());
//        }
    }//GEN-LAST:event_TabRawatStateChanged

    private void simpanDxPxInacbg1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanDxPxInacbg1ActionPerformed
        // copy diagnosa dari table idrg ke table inacbg di database
        copyIdrgKeInaCbg(TNoRw.getText());
        
        tampilDxPx("inacbg", Status.getSelectedItem().toString());
        
        idrgtoinacbg();
    }//GEN-LAST:event_simpanDxPxInacbg1ActionPerformed

    private void TxtgroupKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtgroupKeyPressed
//        Valid.pindah2(evt,KdPetugas,Diagnosis);
    }//GEN-LAST:event_TxtgroupKeyPressed

    private void scrollInputPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_scrollInputPropertyChange
//        verticalPos = scrollInput.getVerticalScrollBar().getValue();
//        System.out.println("posY : " + verticalPos);
    }//GEN-LAST:event_scrollInputPropertyChange

    private void textDiagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textDiagActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textDiagActionPerformed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            BtnCariActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            BtnCari.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            BtnKeluar.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        if(CmbTanggalSEP.getSelectedIndex()==0){
            tampil();
        }else{
            tampil2();
        }
        //        tampil();
    }//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnCariActionPerformed(null);
        } else {
            Valid.pindah(evt, TCari, BtnAll);
        }
    }//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        CmbStatusRawat.setSelectedIndex(0);
        CmbStatusKirim.setSelectedIndex(0);
        emptTeks();
        tampil();
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            TCari.setText("");
            tampil();
        }
    }//GEN-LAST:event_BtnAllKeyPressed

    private void BtnKeluar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluar1ActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluar1ActionPerformed

    private void BtnKeluar1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluar1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            dispose();
        }
    }//GEN-LAST:event_BtnKeluar1KeyPressed

    private void tbKompilasiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbKompilasiMouseClicked
        if (evt.getClickCount() >= 2) {
            evt.consume();
        } else {
            if (tabMode.getRowCount() != 0) {
                try {
                    getData();
                    tabPane1.setSelectedIndex(0);
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbKompilasiMouseClicked

    private void tbKompilasiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbKompilasiKeyReleased
        if (tabMode.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    getData();
                    tabPane1.setSelectedIndex(0);
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbKompilasiKeyReleased

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            INACBGIdrgData dialog = new INACBGIdrgData(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                } 
           });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnAll;
    private widget.Button BtnCari;
    private widget.Button BtnGrouper;
    private widget.Button BtnGrouperIDRG;
    private widget.Button BtnKeluar;
    private widget.Button BtnKeluar1;
    private widget.ComboBox CmbStatusKirim;
    private widget.ComboBox CmbStatusRawat;
    private widget.ComboBox CmbTanggalSEP;
    private widget.TextBoxW11UI Cmgs;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.panelisi FormInput;
    private widget.PanelBiasa FormInput1;
    private widget.Label LCount;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll2;
    private widget.ScrollPane Scroll3;
    private widget.ComboBox Status;
    private widget.TextBox TCari;
    private widget.TextBoxW11UI TNoRM;
    private widget.TextBoxW11UI TNoRw;
    private widget.TextBoxW11UI TNoSEP;
    private widget.TextBoxW11UI TPasien;
    private javax.swing.JTabbedPane TabRawat;
    private widget.TextBoxW11UI Tnoka;
    private widget.TextArea Txtgroup;
    private widget.TextBoxW11UI ahli;
    private widget.TextBoxW11UI akomodasi;
    private widget.TextBoxW11UI alkes;
    private widget.Button batal_validasi_tb;
    private widget.TextBoxW11UI berat_bayi;
    private widget.TextBoxW11UI bmhp;
    private widget.ComboBox cara_masuk;
    private javax.swing.JCheckBox check_hd;
    private javax.swing.JCheckBox chk_diag_1;
    private javax.swing.JCheckBox chk_diag_10;
    private javax.swing.JCheckBox chk_diag_2;
    private javax.swing.JCheckBox chk_diag_3;
    private javax.swing.JCheckBox chk_diag_4;
    private javax.swing.JCheckBox chk_diag_5;
    private javax.swing.JCheckBox chk_diag_6;
    private javax.swing.JCheckBox chk_diag_7;
    private javax.swing.JCheckBox chk_diag_8;
    private javax.swing.JCheckBox chk_diag_9;
    private widget.TextBoxW11UI chronic;
    private widget.ComboBox cmbDrugs;
    private widget.ComboBox cmbInv;
    private widget.ComboBox cmbProc;
    private widget.ComboBox cmbPros;
    private widget.ComboBox cob;
    private widget.TextBoxW11UI distole;
    private widget.TextBoxW11UI dpjp;
    private widget.Button editKlaim;
    private widget.Button editKlaim1;
    private widget.Button editKlaimIDRG;
    private widget.Button finalklaim;
    private widget.Button finalklaim1;
    private widget.Button finalklaimIDRG;
    private widget.Label grupCode;
    private widget.Label grupCode1;
    private widget.ComboBox hak_kelas;
    private widget.Button hapusKlaim;
    private widget.TextBoxW11UI hari_intensif;
    private widget.TextBoxW11UI icd10;
    private widget.TextBoxW11UI icd9;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private javax.swing.JButton jButton1;
    private widget.Label jLabel10;
    private widget.Label jLabel100;
    private widget.Label jLabel101;
    private widget.Label jLabel103;
    private widget.Label jLabel104;
    private widget.Label jLabel105;
    private widget.Label jLabel106;
    private widget.Label jLabel107;
    private widget.Label jLabel108;
    private widget.Label jLabel109;
    private widget.Label jLabel11;
    private widget.Label jLabel110;
    private widget.Label jLabel111;
    private widget.Label jLabel112;
    private widget.Label jLabel12;
    private widget.Label jLabel120;
    private widget.Label jLabel128;
    private widget.Label jLabel13;
    private widget.Label jLabel130;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel28;
    private widget.Label jLabel29;
    private widget.Label jLabel3;
    private widget.Label jLabel30;
    private widget.Label jLabel31;
    private widget.Label jLabel32;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel39;
    private widget.Label jLabel4;
    private widget.Label jLabel40;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel48;
    private widget.Label jLabel49;
    private widget.Label jLabel5;
    private widget.Label jLabel50;
    private widget.Label jLabel51;
    private widget.Label jLabel52;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel57;
    private widget.Label jLabel58;
    private widget.Label jLabel59;
    private widget.Label jLabel6;
    private widget.Label jLabel60;
    private widget.Label jLabel61;
    private widget.Label jLabel62;
    private widget.Label jLabel63;
    private widget.Label jLabel64;
    private widget.Label jLabel65;
    private widget.Label jLabel66;
    private widget.Label jLabel67;
    private widget.Label jLabel68;
    private widget.Label jLabel69;
    private widget.Label jLabel7;
    private widget.Label jLabel70;
    private widget.Label jLabel71;
    private widget.Label jLabel72;
    private widget.Label jLabel73;
    private widget.Label jLabel74;
    private widget.Label jLabel75;
    private widget.Label jLabel76;
    private widget.Label jLabel77;
    private widget.Label jLabel79;
    private widget.Label jLabel8;
    private widget.Label jLabel86;
    private widget.Label jLabel87;
    private widget.Label jLabel88;
    private widget.Label jLabel89;
    private widget.Label jLabel9;
    private widget.Label jLabel90;
    private widget.Label jLabel91;
    private widget.Label jLabel92;
    private widget.Label jLabel93;
    private widget.Label jLabel94;
    private widget.Label jLabel95;
    private widget.Label jLabel96;
    private widget.Label jLabel97;
    private widget.Label jLabel98;
    private widget.Label jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator30;
    private javax.swing.JSeparator jSeparator33;
    private javax.swing.JSeparator jSeparator39;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator40;
    private javax.swing.JSeparator jSeparator41;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private widget.ComboBox jam_ekstubasi;
    private widget.ComboBox jam_intubasi;
    private widget.ComboBox jam_keluar;
    private widget.ComboBox jam_masuk;
    private widget.TextBoxW11UI jam_ventilator;
    private widget.ComboBox jaminan;
    private widget.TextBoxW11UI jenis_kelamin;
    private widget.ComboBox jenis_rawat;
    private widget.TextBoxW11UI kd_diag_1;
    private widget.TextBoxW11UI kd_diag_10;
    private widget.TextBoxW11UI kd_diag_2;
    private widget.TextBoxW11UI kd_diag_3;
    private widget.TextBoxW11UI kd_diag_4;
    private widget.TextBoxW11UI kd_diag_5;
    private widget.TextBoxW11UI kd_diag_6;
    private widget.TextBoxW11UI kd_diag_7;
    private widget.TextBoxW11UI kd_diag_8;
    private widget.TextBoxW11UI kd_diag_9;
    private widget.TextBoxW11UI kd_psdr_1;
    private widget.TextBoxW11UI kd_psdr_10;
    private widget.TextBoxW11UI kd_psdr_2;
    private widget.TextBoxW11UI kd_psdr_3;
    private widget.TextBoxW11UI kd_psdr_4;
    private widget.TextBoxW11UI kd_psdr_5;
    private widget.TextBoxW11UI kd_psdr_6;
    private widget.TextBoxW11UI kd_psdr_7;
    private widget.TextBoxW11UI kd_psdr_8;
    private widget.TextBoxW11UI kd_psdr_9;
    private javax.swing.JCheckBox kelas_eksekutif;
    private widget.Button kirimonline;
    private widget.TextBoxW11UI kpr;
    private widget.TextBoxW11UI kronis;
    private widget.TextBoxW11UI ksl;
    private widget.TextBoxW11UI kt_darah;
    private widget.Label lChronic;
    private widget.Label lJenis;
    private widget.Label lJenis1;
    private widget.Label lStatus;
    private widget.Label lSubacute;
    private widget.TextBoxW11UI lab;
    private widget.TextBoxW11UI lama_naik_kelas;
    private widget.Label lblBaseTariff;
    private widget.Label lblCoderNIK;
    private widget.Label lblSelisih;
    private widget.Label lblTarifAcute;
    private widget.Label lblTarifDrugs;
    private widget.Label lblTarifGrup;
    private widget.Label lblTarifInv;
    private widget.Label lblTarifKronis;
    private widget.Label lblTarifPros;
    private widget.Label lblTarifRS;
    private widget.Label lblTarifTotal;
    private widget.Label lbltarifProc;
    private widget.ComboBox menit_ekstubasi;
    private widget.ComboBox menit_intubasi;
    private widget.ComboBox menit_keluar;
    private widget.ComboBox menit_masuk;
    private widget.TextBoxW11UI multiple1;
    private widget.TextBoxW11UI multiple10;
    private widget.TextBoxW11UI multiple2;
    private widget.TextBoxW11UI multiple3;
    private widget.TextBoxW11UI multiple4;
    private widget.TextBoxW11UI multiple5;
    private widget.TextBoxW11UI multiple6;
    private widget.TextBoxW11UI multiple7;
    private widget.TextBoxW11UI multiple8;
    private widget.TextBoxW11UI multiple9;
    private widget.ComboBox naik_kelas_pelayanan;
    private javax.swing.JCheckBox naik_turun_kelas;
    private widget.TextBoxW11UI nm_diag_1;
    private widget.TextBoxW11UI nm_diag_10;
    private widget.TextBoxW11UI nm_diag_2;
    private widget.TextBoxW11UI nm_diag_3;
    private widget.TextBoxW11UI nm_diag_4;
    private widget.TextBoxW11UI nm_diag_5;
    private widget.TextBoxW11UI nm_diag_6;
    private widget.TextBoxW11UI nm_diag_7;
    private widget.TextBoxW11UI nm_diag_8;
    private widget.TextBoxW11UI nm_diag_9;
    private widget.TextBoxW11UI nm_psdr_1;
    private widget.TextBoxW11UI nm_psdr_10;
    private widget.TextBoxW11UI nm_psdr_2;
    private widget.TextBoxW11UI nm_psdr_3;
    private widget.TextBoxW11UI nm_psdr_4;
    private widget.TextBoxW11UI nm_psdr_5;
    private widget.TextBoxW11UI nm_psdr_6;
    private widget.TextBoxW11UI nm_psdr_7;
    private widget.TextBoxW11UI nm_psdr_8;
    private widget.TextBoxW11UI nm_psdr_9;
    private widget.TextBoxW11UI no_rawat;
    private widget.TextBoxW11UI obat;
    private widget.TextBoxW11UI obat_kemo;
    private widget.panelisi panelGlass10;
    private widget.panelisi panelGlass11;
    private widget.panelisi panelGlass13;
    private widget.panelisi panelGlass14;
    private widget.panelisi panelGlass15;
    private widget.panelisi panelGlass16;
    private widget.panelisi panelGlass17;
    private widget.panelisi panelGlass18;
    private widget.panelisi panelGlass19;
    private widget.panelisi panelGlass20;
    private widget.panelisi panelGlass21;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ComboBox pasien_hd;
    private widget.TextBoxW11UI pasien_tb;
    private widget.TextBoxW11UI pb;
    private widget.TextBoxW11UI pl_darah;
    private widget.TextBoxW11UI pnb;
    private widget.TextBoxW11UI pnb1;
    private widget.TextBoxW11UI pnj;
    private widget.TextBoxW11UI rad;
    private javax.swing.JCheckBox rawat_intensif;
    private widget.TextBoxW11UI rehab;
    private widget.Label respon;
    private widget.Label respon2;
    private widget.TextBoxW11UI ri;
    private widget.ScrollPane scrollBilling;
    private widget.ScrollPane scrollCombo;
    private widget.ScrollPane scrollCombo2;
    private widget.ScrollPane scrollCombo4;
    private widget.ScrollPane scrollCombo5;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane6;
    private widget.ScrollPane scrollPane7;
    private widget.ScrollPane scrollRespon;
    private widget.ScrollPane scrollRespon1;
    private widget.TextBoxW11UI sewa_alat;
    private javax.swing.JButton simpanDxPxIDRG;
    private javax.swing.JButton simpanDxPxInacbg;
    private javax.swing.JButton simpanDxPxInacbg1;
    private widget.TextBoxW11UI sistole;
    private widget.ComboBox stage_2;
    private widget.ComboBox status_pulang;
    private widget.TextBoxW11UI sub_acute;
    private widget.TextArea tLog;
    private widget.TextArea tLog1;
    private widget.TabPane tabPane1;
    private widget.TextBoxW11UI tarif_eksekutif;
    private widget.Table tbCombo;
    private widget.Table tbCombo1;
    private widget.Table tbCombo2;
    private widget.Table tbCombo22;
    private widget.Table tbKompilasi;
    private widget.TextBoxW11UI textDiag;
    private widget.TextBoxW11UI textDiag2;
    private widget.TextBoxW11UI textProc;
    private widget.TextBoxW11UI textProc2;
    private widget.Tanggal tgl_ekstubasi;
    private widget.Tanggal tgl_intubasi;
    private widget.Tanggal tgl_keluar;
    private widget.TextBoxW11UI tgl_lahir;
    private widget.Tanggal tgl_masuk;
    private widget.Label total_rupiah;
    private widget.Label total_rupiah1;
    private widget.Label txDrg;
    private widget.Label txDrgCode;
    private widget.Label txInfo;
    private widget.Label txJenis;
    private widget.Label txMdc;
    private widget.Label txMdcCode;
    private widget.Label txStatus;
    private widget.Button validasi_tb;
    private javax.swing.JCheckBox ventilator;
    // End of variables declaration//GEN-END:variables
    
    // field anggota kelas
    private JComboBox<String> combo1;
    private JComboBox<String> combo2;
    private JComboBox<String> combo3;
    private JComboBox<String> combo4;

    private Map<Integer,JComboBox<String>> comboPerBaris;
    
    public void emptTeks() {
        TNoRM.setText("");
        TPasien.setText("");
        TNoSEP.setText("");
        tgl_masuk.setDate(new Date());
        tgl_masuk.requestFocus();
    }
    
    public void setDiagnosa(String norw, String norm, String pasien, String jenpel) {
        //cek koder
        try {

//            rs = koneksi.prepareStatement("select * from bridging_sep where no_rawat='"+norw+"'").executeQuery();
//            System.out.println("Error: " +jenpel);
            Status.setSelectedItem(jenpel);
            if( jenpel.equals("Ranap") ) {
                rs = koneksi.prepareStatement("select * from bridging_sep where no_rawat='"+norw+"' AND jnspelayanan = '1' ").executeQuery();
            } else if( jenpel.equals("Ralan") ) {
                rs = koneksi.prepareStatement("select * from bridging_sep where no_rawat='"+norw+"' AND jnspelayanan = '2' ").executeQuery();
            }
            while (rs.next()) {
                coder = Sequel.cariIsi("select no_ik from inacbg_coder_nik where nik='"+akses.getkode()+"'");
                if (akses.getkode().equals("Admin Utama")){
                   coder=Sequel.cariIsi("select no_ik from inacbg_coder_nik LIMIT 1");
                } else if (!coder.equals("")){
                   coder=coder;
                } else {
                    JOptionPane.showMessageDialog(null,"ANDA BUKAN CODER KLAIM, JADINYA TIDAK BISA !!!!!");
                    BtnKeluarActionPerformed(null);
                }
                
                //pindahkan kemari
                TNoRw.setText(norw);
                TNoRM.setText(norm);
                TPasien.setText(pasien);
                Tnoka.setText(rs.getString("no_kartu"));
                TNoSEP.setText(rs.getString("no_sep"));
                
                no_rawat.setText(norw);
                Tnoka.setText(rs.getString("no_kartu"));  
                TNoSEP.setText(rs.getString("no_sep"));  
                
                TPasien.setText(pasien);   
                jenis_kelamin.setText(rs.getString("jkel"));
                tgl_lahir.setText(rs.getString("tanggal_lahir"));
                TNoRM.setText(norm);
                
                if (rs.getString("jnspelayanan").equals("2")) {
                    hak_kelas.setSelectedIndex(2);
                } else {
                    switch (rs.getString("klsrawat")) {
                        case "1":
                            hak_kelas.setSelectedIndex(0);
                            break;
                        case "2":
                            hak_kelas.setSelectedIndex(1);
                            break;
                        case "3":
                            hak_kelas.setSelectedIndex(2);
                            break;
                        default:
                            throw new AssertionError();
                    }
                }
                
                if (rs.getString("kdpolitujuan").equals("HDL")) {
                    check_hd.setSelected(true);
                } else {
                    check_hd.setSelected(false);
                }
                
                
                pnb.setText(Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya),0) AS total FROM billing " +
                                             "WHERE no_rawat='"+TNoRw.getText()+"' " +
                                             "AND status IN ('Ralan Dokter Paramedis','Ranap Dokter Paramedis') " +
                                             "AND nm_perawatan NOT LIKE '%terapi%'"));
                
//                pnb.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(PNB)%' and no_rawat='"+no_rawat.getText()+"'"));
                pb.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Operasi'"));
                ksl.setText(Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya),0) AS total FROM billing " +
                                             "WHERE no_rawat='"+TNoRw.getText()+"' " +
                                             "AND status IN ('Ralan Dokter','Ranap Dokter') "));
                
//                ksl.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(KSL)%' and no_rawat='"+no_rawat.getText()+"'"));
                ahli.setText("0");
                kpr.setText(Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya),0) AS total FROM billing " +
                                             "WHERE no_rawat='"+TNoRw.getText()+"' " +
                                             "AND status IN ('Ralan Paramedis','Ranap Paramedis') "));
                
//                kpr.setText(Sequel.cariIsi("select (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(TDP)%' and no_rawat='"+no_rawat.getText()+"') + (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(PSB)%' and no_rawat='"+no_rawat.getText()+"')"));
                rad.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Radiologi'"));
                lab.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Laborat'"));
                alkes.setText((Sequel.cariIsi("select (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(ALKES)%' and no_rawat='"+no_rawat.getText()+"') + (select ifnull(sum(totalbiaya),0) from billing where (nm_perawatan like '%Lensa%' OR nm_perawatan like '%LENSA%' OR nm_perawatan like '%lensa%') and no_rawat='"+no_rawat.getText()+"')")));
                kronis.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(kronis)%' and no_rawat='"+no_rawat.getText()+"' and status='Obat'"));
                obat_kemo.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(kemo)%' and no_rawat='"+no_rawat.getText()+"' and status='Obat'"));
                obat.setText(Sequel.cariIsi("select (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan not like '%(ALKES)%' and no_rawat='"+no_rawat.getText()+"' and status='Obat') + (select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Retur Obat') + (select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Resep Pulang') - (select (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%kronis%' and no_rawat='"+no_rawat.getText()+"' and status='Obat') - (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%kemo%' and no_rawat='"+no_rawat.getText()+"' and status='Obat'))"));
                pnj.setText(Sequel.cariIsi("select (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan not like '%selisih%' and no_rawat='"+no_rawat.getText()+"' and status='Tambahan') + (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(PNG)%' and no_rawat='"+no_rawat.getText()+"')"));
                pl_darah.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(DRH)%' and no_rawat='"+no_rawat.getText()+"'"));
                bmhp.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(BMHP)%' and no_rawat='"+no_rawat.getText()+"'"));
                sewa_alat.setText(Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya),0) AS total FROM billing " +
                                                   "WHERE no_rawat='"+TNoRw.getText()+"' " +
                                                   "AND status IN ('Harian','Service') "));
//                sewa_alat.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(ALM)%' and no_rawat='"+no_rawat.getText()+"'"));
                akomodasi.setText(Sequel.cariIsi("SELECT (select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Kamar')+(select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Harian')+(select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Service')+(select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Registrasi')+(select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(HRP)%' and no_rawat='"+no_rawat.getText()+"')"));
                rehab.setText(Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya),0) AS total FROM billing " +
                                               "WHERE no_rawat='"+TNoRw.getText()+"' " +
                                               "AND status IN ('Ralan Dokter Paramedis','Ranap Dokter Paramedis') " +
                                               "AND nm_perawatan LIKE '%terapi%'"));
//                rehab.setText("0");
                ri.setText("0");
                
//              ICU
                if(Sequel.cariInteger("select count(*) from kamar_inap where kamar_inap.no_rawat = '"+no_rawat.getText()+"' AND kamar_inap.kd_kamar like '%ICU%' ") > 0 ) {
                    rawat_intensif.setSelected(true);
                    hari_intensif.setText( Sequel.cariIsi("select kamar_inap.lama from kamar_inap where kamar_inap.no_rawat = '"+no_rawat.getText()+"' AND kamar_inap.kd_kamar like '%ICU%'"));
                }

//              HEMODIALISIS
                if(Sequel.cariInteger("SELECT COUNT(*) AS total_baris " +
                " FROM ( " +
                    "select no_rawat, kd_jenis_prw from rawat_jl_dr where no_rawat = '"+TNoRw.getText()+"' AND kd_jenis_prw in ('RI20067','HD00001','HD00002') " +
                    "union select no_rawat, kd_jenis_prw from rawat_inap_drpr where no_rawat = '"+TNoRw.getText()+"' AND kd_jenis_prw in ('RI20067','HD00001','HD00002') " +
                    "union select no_rawat, kd_jenis_prw from rawat_inap_dr where no_rawat = '"+TNoRw.getText()+"' AND kd_jenis_prw in ('RI20067','HD00001','HD00002') " +
                    "union select no_rawat, kd_jenis_prw from rawat_inap_drpr where no_rawat = '"+TNoRw.getText()+"' AND kd_jenis_prw in ('RI20067','HD00001','HD00002') " +
                " ) as gabungan") > 0 ) {
                    check_hd.setSelected(true);                    
                }
                
//                set jumlah darah hd
                kt_darah.setText(Sequel.cariIsi("select jumlah from billing where nm_perawatan like '%(DRH)%' and no_rawat='"+no_rawat.getText()+"'"));
                pasien_tb.setText(Sequel.cariIsi("select no_sitb from inacbg_validasi_tb where no_rkm_medis='"+TNoRM.getText().trim()+"' "));
                hitungtotalbiaya();
                
                no_rawat_bayi = Sequel.cariIsi("SELECT ranap_gabung.no_rawat2 from ranap_gabung WHERE ranap_gabung.no_rawat='"+no_rawat.getText().trim()+"' ");
                berat_bayi.setText(Sequel.cariIsi("SELECT pasien_bayi.berat_badan FROM pasien_bayi INNER JOIN reg_periksa ON reg_periksa.no_rkm_medis=pasien_bayi.no_rkm_medis WHERE reg_periksa.no_rawat='"+no_rawat_bayi+"'"));

                
                try {
                    rs = koneksi.prepareStatement("select reg_periksa.tgl_registrasi, reg_periksa.jam_reg, reg_periksa.status_lanjut, reg_periksa.status_bayar, dokter.nm_dokter from reg_periksa inner join "
                            + "dokter on reg_periksa.kd_dokter=dokter.kd_dokter where reg_periksa.no_rawat='"+no_rawat.getText()+"'").executeQuery();
                    if (rs.next()) {
                        if (rs.getString("status_lanjut").equals("Ralan")) {
                            dokterkonsul = Sequel.cariIsi("select concat(', ',dokter.nm_dokter) from reg_periksa inner "
                                    + "join konsultasi_medik on konsultasi_medik.no_rawat=reg_periksa.no_rawat inner join dokter on konsultasi_medik.kd_dokter_dikonsuli=dokter.kd_dokter where "
                                    + "reg_periksa.no_rawat='"+no_rawat.getText()+"'");
                             
                            tgl_masuk.setDate(rs.getDate("tgl_registrasi"));
                            tgl_keluar.setDate(rs.getDate("tgl_registrasi"));
                            jam_masuk.setSelectedItem(rs.getString("jam_reg").substring(0,2));
                            jam_keluar.setSelectedItem(rs.getString("jam_reg").substring(0,2));
                            menit_masuk.setSelectedItem(rs.getString("jam_reg").substring(3,5));
                            menit_keluar.setSelectedItem(rs.getString("jam_reg").substring(3,5));
                            dpjp.setText(rs.getString("nm_dokter")); //+dokterkonsul);
                        } else {
                            jenis_rawat.setSelectedIndex(0);

                            tgl_masuk_ranap = Sequel.cariIsi("SELECT DISTINCT(concat(tgl_masuk,' ',jam_masuk)) as jam_masuk FROM kamar_inap WHERE no_rawat='"+no_rawat.getText()+"' ORDER BY tgl_masuk ASC LIMIT 1");
                            tgl_keluar_ranap = Sequel.cariIsi("SELECT DISTINCT(concat(tgl_keluar,' ',jam_keluar)) as jam_keluar FROM kamar_inap WHERE no_rawat='"+no_rawat.getText()+"' AND tgl_keluar<>'0000-00-00' ORDER BY tgl_masuk DESC LIMIT 1");
                            
                            if (tgl_keluar_ranap.equals("")) {
                                tgl_keluar_ranap = tgl_masuk_ranap;
                                JOptionPane.showMessageDialog(null,"PASIEN INI BELUM DIPULANGKAN !!!!!!");
                            }
                            
                            LocalDateTime tanggalWaktu1 = LocalDateTime.parse(tgl_masuk_ranap, formatter);
                            LocalDateTime tanggalWaktu2 = LocalDateTime.parse(tgl_keluar_ranap, formatter);

                            // Menghitung selisih hari
                            long selisihHari = ChronoUnit.DAYS.between(tanggalWaktu1, tanggalWaktu2);
                            long selisihJam = ChronoUnit.HOURS.between(tanggalWaktu1, tanggalWaktu2);

                            try{
                                rs3=koneksi.prepareStatement(
                                    "select dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat='"+no_rawat.getText()+"' AND prioritas='1' ").executeQuery();

                                while (rs3.next()) {
                                    dpjp_ranap = rs3.getString("nm_dokter"); //+", ";
                                }
                            } catch (Exception e) {
                                System.out.println("Status Lanjut : "+e);
                            }

                            Valid.SetTgl(tgl_masuk,tgl_masuk_ranap.substring(0,10));
                            Valid.SetTgl(tgl_keluar,tgl_keluar_ranap.substring(0,10));
                            jam_masuk.setSelectedItem(tgl_masuk_ranap.substring(11,13));
                            jam_keluar.setSelectedItem(tgl_keluar_ranap.substring(11,13));
                            menit_masuk.setSelectedItem(tgl_masuk_ranap.substring(14,16));
                            menit_keluar.setSelectedItem(tgl_keluar_ranap.substring(14,16));
                            dpjp.setText(dpjp_ranap);                            
                        }                        
                    }
                    
                    //buat wadah untuk respon bridging
                    if( Sequel.cariInteger("select count(*) from idrg_json where no_rawat ='"+TNoRw.getText()+"' and no_sep ='"+TNoSEP.getText()+"' ") < 1 ) {
                        simpan_awal_respon(TNoRw.getText(),TNoSEP.getText());
                    }                    
                } catch (Exception e) {
                    System.out.println("error eklaim : "+e);
                }
                
////                diagnosa
//                try {
//                    rsdiagnosa = koneksi.prepareStatement("select penyakit.kd_penyakit, penyakit.nm_penyakit from diagnosa_pasien inner join penyakit on diagnosa_pasien.kd_penyakit=penyakit.kd_penyakit where diagnosa_pasien.no_rawat='"+norw+"' "
//                            + "order by diagnosa_pasien.prioritas asc").executeQuery();
//                    i=1;
//                    while (rsdiagnosa.next()) {
////                        System.out.println("data "+rsdiagnosa.getString("kd_penyakit"));
//                        
//                        if (i == 1) {
//                            kd_diag_1.setText(rsdiagnosa.getString("kd_penyakit"));
//                            nm_diag_1.setText(rsdiagnosa.getString("nm_penyakit"));
//                        }
//                        if (i == 2) {
//                            kd_diag_2.setText(rsdiagnosa.getString("kd_penyakit"));
//                            nm_diag_2.setText(rsdiagnosa.getString("nm_penyakit"));
//                        }
//                        if (i == 3) {
//                            kd_diag_3.setText(rsdiagnosa.getString("kd_penyakit"));
//                            nm_diag_3.setText(rsdiagnosa.getString("nm_penyakit"));
//                        }
//                        if (i == 4) {
//                            kd_diag_4.setText(rsdiagnosa.getString("kd_penyakit"));
//                            nm_diag_4.setText(rsdiagnosa.getString("nm_penyakit"));
//                        }
//                        if (i == 5) {
//                            kd_diag_5.setText(rsdiagnosa.getString("kd_penyakit"));
//                            nm_diag_5.setText(rsdiagnosa.getString("nm_penyakit"));
//                        }
//                        if (i == 6) {
//                            kd_diag_6.setText(rsdiagnosa.getString("kd_penyakit"));
//                            nm_diag_6.setText(rsdiagnosa.getString("nm_penyakit"));
//                        }
//                        if (i == 7) {
//                            kd_diag_7.setText(rsdiagnosa.getString("kd_penyakit"));
//                            nm_diag_7.setText(rsdiagnosa.getString("nm_penyakit"));
//                        }
//                        if (i == 8) {
//                            kd_diag_8.setText(rsdiagnosa.getString("kd_penyakit"));
//                            nm_diag_8.setText(rsdiagnosa.getString("nm_penyakit"));
//                        }
//                        if (i == 9) {
//                            kd_diag_9.setText(rsdiagnosa.getString("kd_penyakit"));
//                            nm_diag_9.setText(rsdiagnosa.getString("nm_penyakit"));
//                        }
//                        if (i == 10) {
//                            kd_diag_10.setText(rsdiagnosa.getString("kd_penyakit"));
//                            nm_diag_10.setText(rsdiagnosa.getString("nm_penyakit"));
//                        }
//                        
//                        
//                        i++;
//                    }
//                } catch (Exception e) {
//                    System.out.println("ICD 10 : "+e);
//                }
                
                
////                prosedur
//                try {
//                    rsprosedur = koneksi.prepareStatement("select icd9.kode, icd9.deskripsi_panjang from prosedur_pasien inner join icd9 on prosedur_pasien.kode=icd9.kode where prosedur_pasien.no_rawat='"+norw+"' "
//                            + "order by prosedur_pasien.prioritas asc").executeQuery();
//                    j=1;
//                    while (rsprosedur.next()) {
////                        System.out.println("data "+rsprosedur.getString("kode"));
//
//                        if (j == 1) {
//                            kd_psdr_1.setText(rsprosedur.getString("kode"));
//                            nm_psdr_1.setText(rsprosedur.getString("deskripsi_panjang"));
//                        }
//                        if (j == 2) {
//                            kd_psdr_2.setText(rsprosedur.getString("kode"));
//                            nm_psdr_2.setText(rsprosedur.getString("deskripsi_panjang"));
//                        }
//                        if (j == 3) {
//                            kd_psdr_3.setText(rsprosedur.getString("kode"));
//                            nm_psdr_3.setText(rsprosedur.getString("deskripsi_panjang"));
//                        }
//                        if (j == 4) {
//                            kd_psdr_4.setText(rsprosedur.getString("kode"));
//                            nm_psdr_4.setText(rsprosedur.getString("deskripsi_panjang"));
//                        }
//                        if (j == 5) {
//                            kd_psdr_5.setText(rsprosedur.getString("kode"));
//                            nm_psdr_5.setText(rsprosedur.getString("deskripsi_panjang"));
//                        }
//                        if (j == 6) {
//                            kd_psdr_6.setText(rsprosedur.getString("kode"));
//                            nm_psdr_6.setText(rsprosedur.getString("deskripsi_panjang"));
//                        }
//                        if (j == 7) {
//                            kd_psdr_7.setText(rsprosedur.getString("kode"));
//                            nm_psdr_7.setText(rsprosedur.getString("deskripsi_panjang"));
//                        }
//                        if (j == 8) {
//                            kd_psdr_8.setText(rsprosedur.getString("kode"));
//                            nm_psdr_8.setText(rsprosedur.getString("deskripsi_panjang"));
//                        }
//                        if (j == 9) {
//                            kd_psdr_9.setText(rsprosedur.getString("kode"));
//                            nm_psdr_9.setText(rsprosedur.getString("deskripsi_panjang"));
//                        }
//                        if (j == 10) {
//                            kd_psdr_10.setText(rsprosedur.getString("kode"));
//                            nm_psdr_10.setText(rsprosedur.getString("deskripsi_panjang"));
//                        }
//
//                        j++;
//                    }
//                    icd9.setText("");
//                    icd9.setText(kd_psdr_1.getText().trim() + (multiple1.getText().trim().equals("") ? "" : ("+" + multiple1.getText())) + 
//                        (kd_psdr_2.getText().trim().equals("") ? "" : ("#"+kd_psdr_2.getText() + (multiple2.getText().trim().equals("") ? "" : "+" + multiple2.getText())) )
//                       +(kd_psdr_3.getText().trim().equals("") ? "" : "#"+kd_psdr_3.getText() + (multiple3.getText().trim().equals("") ? "" : "+" + multiple3.getText()) )
//                       +(kd_psdr_4.getText().trim().equals("") ? "" : "#"+kd_psdr_4.getText() + (multiple4.getText().trim().equals("") ? "" : "+" + multiple4.getText()) )
//                       +(kd_psdr_5.getText().trim().equals("") ? "" : "#"+kd_psdr_5.getText() + (multiple5.getText().trim().equals("") ? "" : "+" + multiple5.getText()) )
//                       +(kd_psdr_6.getText().trim().equals("") ? "" : "#"+kd_psdr_6.getText() + (multiple6.getText().trim().equals("") ? "" : "+" + multiple6.getText()) )
//                       +(kd_psdr_7.getText().trim().equals("") ? "" : "#"+kd_psdr_7.getText() + (multiple7.getText().trim().equals("") ? "" : "+" + multiple7.getText()) )
//                       +(kd_psdr_8.getText().trim().equals("") ? "" : "#"+kd_psdr_8.getText() + (multiple8.getText().trim().equals("") ? "" : "+" + multiple8.getText()) )
//                       +(kd_psdr_9.getText().trim().equals("") ? "" : "#"+kd_psdr_9.getText() + (multiple9.getText().trim().equals("") ? "" : "+" + multiple9.getText()) )
//                       +(kd_psdr_10.getText().trim().equals("") ? "" : "#"+kd_psdr_10.getText() + (multiple10.getText().trim().equals("") ? "" : "+" + multiple10.getText()) )
//                    );
//
//
//                } catch (Exception e) {
//                    System.out.println("ICD 9 : "+e);
//                }
//                
//               
//                if (!kd_diag_1.getText().trim().equals("")) {
//                   chk_diag_1.setSelected(true);
//                }
                
                jLabel49.setVisible(false);                
                jLabel53.setVisible(false);
                stage_2.setVisible(false);
                
                check_hdItemStateChanged(null);
                jenis_rawatItemStateChanged(null);
                rawat_intensifItemStateChanged(null);
                kelas_eksekutifItemStateChanged(null);
                naik_turun_kelasItemStateChanged(null);
                
//                cek sudah di grouper 
                cariLog(TNoRw.getText());
                try {
                    rsgrouper = koneksi.prepareStatement("select * from inacbg_grouping_stage1 where no_sep='"+TNoSEP.getText().trim()+"'").executeQuery();
                    while (rsgrouper.next()) {
                        total_rupiah1.setText(Valid.SetAngka(rsgrouper.getDouble("tarif")));
                        respon.setText("CODE INACBG : "+rsgrouper.getString("code_cbg"));
                        respon2.setText("DESKRIPSI : "+rsgrouper.getString("deskripsi"));
                        
                        //pindahkan response ke dalam table tbResponse
//                        tbResponse.setValueAt(Valid.SetAngka(rsgrouper.getDouble("tarif")), 2, 2);
//                        tbResponse.setValueAt(rsgrouper.getString("code_cbg"), 6, 2);
//                        tbResponse.setValueAt(rsgrouper.getString("deskripsi"), 7, 2);
                        
//                        txMdcCode.setText(rsgrouper.getString("code_cbg"));
//                        txMdc.setText(rsgrouper.getString("deskripsi"));
                        
                        
                    }
                } catch (Exception e) {
                    System.out.println("error tampil : "+e);
                }
                
                try {
                    rsgrouper = koneksi.prepareStatement("select * from inacbg_grouping_stage2 where no_sep='"+TNoSEP.getText().trim()+"'").executeQuery();
                    while (rsgrouper.next()) {
                        total_rupiah1.setText(Valid.SetAngka(rsgrouper.getDouble("tarif")));
                        respon.setText("CODE INACBG : "+rsgrouper.getString("code_cbg"));
                        respon2.setText("DESKRIPSI : "+rsgrouper.getString("deskripsi"));
                        
                        //pindahkan response ke dalam table tbResponse
//                        tbResponse.setValueAt(Valid.SetAngka(rsgrouper.getDouble("tarif")), 2, 2);
//                        tbResponse.setValueAt(rsgrouper.getString("code_cbg"), 8, 2);
//                        tbResponse.setValueAt(rsgrouper.getString("deskripsi"), 9, 2);
                        
                        
                        
                    }
                } catch (Exception e) {
                    System.out.println("error tampil : "+e);
                }
                
//                setResponseAwal();
                tampilDxPx("idrg", Status.getSelectedItem().toString());
                tampilDxPx("inacbg", Status.getSelectedItem().toString());
                tombolinacbg();
                lJenis1.setText(Status.getSelectedItem().toString());
            }
                
        } catch (Exception e) {
            System.out.println("error : "+e);
        }
    }
    
    public void hitungtotalbiaya(){
        if (pnb.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pnb.requestFocus();
        }
        else if (pb.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pb.requestFocus();
        }
        else if (ahli.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            ahli.requestFocus();
        }
        else if (rad.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            rad.requestFocus();
        }
        else if (rehab.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            rehab.requestFocus();
        }
        else if (obat.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            obat.requestFocus();
        }
        else if (alkes.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            alkes.requestFocus();
        }
        else if (kpr.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            kpr.requestFocus();
        }
        else if (lab.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            lab.requestFocus();
        }
        else if (akomodasi.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            akomodasi.requestFocus();
        }
        else if (kronis.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            kronis.requestFocus();
        }
        else if (bmhp.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            bmhp.requestFocus();
        }
        else if (ksl.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            ksl.requestFocus();
        }
        else if (pnb.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pnb.requestFocus();
        }
        else if (pnj.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pnj.requestFocus();
        }
        else if (pl_darah.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pl_darah.requestFocus();
        }
        else if (ri.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            ri.requestFocus();
        }
        else if (obat_kemo.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            obat_kemo.requestFocus();
        }
        else if (sewa_alat.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            sewa_alat.requestFocus();
        } else {
            total_biaya = (Integer.parseInt(pnb.getText()) + Integer.parseInt(pb.getText()) + Integer.parseInt(ksl.getText()) + Integer.parseInt(ahli.getText()) + Integer.parseInt(kpr.getText()) +  Integer.parseInt(rad.getText()) + Integer.parseInt(lab.getText()) + Integer.parseInt(alkes.getText()) + Integer.parseInt(kronis.getText()) + Integer.parseInt(obat_kemo.getText()) + Integer.parseInt(obat.getText()) + Integer.parseInt(pnj.getText()) + Integer.parseInt(pl_darah.getText()) + Integer.parseInt(bmhp.getText()) + Integer.parseInt(sewa_alat.getText()) + Integer.parseInt(rehab.getText()) + Integer.parseInt(ri.getText()) + Integer.parseInt(akomodasi.getText()) );
            total_rupiah.setText(String.valueOf(Valid.SetAngka(total_biaya)));
//            tbResponse.setValueAt(String.valueOf(Valid.SetAngka(total_biaya)), 1, 2);
//            totalBill.setText(String.valueOf(Valid.SetAngka(total_biaya)) );
            lblTarifRS.setText(String.valueOf(Valid.SetAngka(total_biaya)) );
        }
    }
    
    private void copyIdrgKeInaCbg(String norawat) {
        List<List<String>> dataDx = Sequel.cariData("select no_rawat, kd_penyakit, status, prioritas, status_penyakit from diagnosa_pasien_idrg where no_rawat = '"+norawat+"' ");
        List<List<String>> dataPx = Sequel.cariData("select no_rawat, kode, status, prioritas from prosedur_pasien_idrg where no_rawat = '"+norawat+"' ");
                
        if(dataDx.size() > 0 ) {
            // hapus dulu data yang sudah ada
            Sequel.meghapus("diagnosa_pasien", "no_rawat", norawat);
            
            // simpan data yg baru
            for (List<String> baris : dataDx) {
                Sequel.menyimpan("diagnosa_pasien", " '"+baris.get(0)+"', '"+baris.get(1)+"', '"+baris.get(2)+"', '"+baris.get(3)+"', '"+baris.get(4)+"' ");                     
            }
        }
        
        if(dataPx.size() > 0 ) {
            // hapus dulu data yang sudah ada
            Sequel.meghapus("prosedur_pasien", "no_rawat", norawat);
            
            // simpan data yg baru
            for (List<String> row : dataPx) {
                Sequel.menyimpan("prosedur_pasien", " '"+row.get(0)+"', '"+row.get(1)+"', '"+row.get(2)+"', '"+row.get(3)+"' ");
            }
        }
        

    }
    
    private void refreshDxIdrg(String norawat) {
        List<List<String>> dataDx = Sequel.cariData("select no_rawat, kd_penyakit, status, prioritas, status_penyakit from diagnosa_pasien_idrg where no_rawat = '"+norawat+"' ");
    }
    
    private void refreshDxInacbg(String norawat) {
        List<List<String>> dataPx = Sequel.cariData("select no_rawat, kode, status, prioritas from prosedur_pasien_idrg where no_rawat = '"+norawat+"' ");
    }
    
    
    
    private void idrg_diagnosa(){
        if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"idrg_diagnosa_set\",\n" +
                                            "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"diagnosa\":\""+icd10.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                System.out.println("response idrg1 : "+response);
                update_respon(TNoRw.getText(),TNoSEP.getText(),"set_diagnosa","set_diagnosa_res",requestJson,response.toString() );
                tLog.append(response+"\n------------ \n");
                
                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        System.out.println("set "+code.getString("method")+" : "+code.getString("message"));
                    }else{
//                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void idrg_prosedur(){
        if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"idrg_procedure_set\",\n" +
                                            "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"procedure\":\""+icd9.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                update_respon(TNoRw.getText(),TNoSEP.getText(),"set_prosedur","set_prosedur_res",requestJson,response.toString() );
                tLog.append(response+"\n------------ \n");
                System.out.println("response idrg prosedur 1 : "+response);
                
                if(response.has("metadata")){
                    JSONObject code = response.getJSONObject("metadata");
                    if(code.getInt("code")==200){
                        System.out.println("set "+code.getString("method")+" : "+code.getString("message"));
                    }else{
//                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void tombolinacbg(){
        if (Sequel.cariIsi("select no_sep from inacbg_idrg_final where no_sep='"+TNoSEP.getText().trim()+"'").equals("")) {
            BtnGrouper.setVisible(false);
            finalklaim.setVisible(false);
            editKlaim.setVisible(false);
            kirimonline.setVisible(false);
            editKlaimIDRG.setVisible(false);
            BtnGrouperIDRG.setVisible(true);
            finalklaimIDRG.setVisible(false);
            hapusKlaim.setVisible(false);
            BtnGrouper.setVisible(false);
            finalklaim1.setVisible(false);
            editKlaim1.setVisible(false);
            simpanDxPxIDRG.setEnabled(true);
            disableDxiDRG(true);
        } else {
            BtnGrouper.setVisible(true);
            finalklaim.setVisible(true);
            editKlaim.setVisible(true);
            kirimonline.setVisible(true);
            editKlaimIDRG.setVisible(true);
            BtnGrouperIDRG.setVisible(false);
            finalklaimIDRG.setVisible(false);
            simpanDxPxIDRG.setEnabled(false);
            disableDxiDRG(false);
            
            if (Sequel.cariIsi("select no_sep from inacbg_final where no_sep='"+TNoSEP.getText().trim()+"'").equals("")) {
                hapusKlaim.setVisible(true);
                BtnGrouper.setVisible(true);
                finalklaim1.setVisible(true);
                finalklaim.setVisible(false);
                editKlaim.setVisible(false);
                editKlaim1.setVisible(false);
            } else {
                hapusKlaim.setVisible(false);
                BtnGrouper.setVisible(false);
                finalklaim1.setVisible(false);
                finalklaim.setVisible(true);
                editKlaim.setVisible(true);
                editKlaim1.setVisible(true);
            }
        }
    }
    
    private void grouperidrg(){
        respon.setText("");
        respon2.setText("");
        
        if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"grouper\",\n" +
                                            "\"stage\":\"1\",\n" +
                                            "\"grouper\":\"idrg\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                update_respon(TNoRw.getText(),TNoSEP.getText(),"idrg_grouper","idrg_grouper_res",requestJson,response.toString() );
                tLog.append(response+"\n------------ \n");
                System.out.println("response grouper idrg : "+response);
                
                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        JSONObject idrg = response.getJSONObject("response_idrg");
                        respon.setText("KODE : "+idrg.getString("mdc_number")+", DESC : "+idrg.getString("mdc_description"));
                        respon2.setText("KODE : "+idrg.getString("drg_code")+", DESC : "+idrg.getString("drg_description"));
                        
//                        tbResponse.setValueAt("KODE : "+idrg.getString("mdc_number")+", DESC : "+idrg.getString("mdc_description"), 4, 2);
//                        tbResponse.setValueAt("KODE : "+idrg.getString("drg_code")+", DESC : "+idrg.getString("drg_description"), 5, 2);
                        
                        txMdcCode.setText(idrg.getString("mdc_number"));
                        txMdc.setText(idrg.getString("mdc_description"));                
                        
                        txDrgCode.setText(idrg.getString("drg_code"));
                        txDrg.setText(idrg.getString("drg_description"));
                        
                        System.out.println("response grouper idrg : "+code.getString("message"));
                        
//                    }else{
//                        JOptionPane.showMessageDialog(null,"RESPONe : "+code.getString("message"));
                        if(code.getString("message").equals("Ok")) {
                            if(idrg.getString("drg_description").contains("cannot be used for coding")) {
                                Toast.show(idrg.getString("drg_description")+", gunakan Diagnosa lain", ToastType.ERROR);
                            } else {
                                finalklaimIDRG.setVisible(true);
                                Toast.show("Grouper iDRG berhasil", ToastType.SUCCESS);
                            }
                        } else {
                            Toast.show(idrg.getString("drg_description")+", "+idrg.getString("mdc_description") , ToastType.ERROR);
                        }
                    } else if(code.getInt("code")==400){
                        Toast.show(code.getString("message")+", "+code.getString("error_no") , ToastType.WARNING);
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error grouper_idrg: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void idrgtoinacbg() {
        if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"idrg_to_inacbg_import\"\n" +    
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                
                update_respon(TNoRw.getText(),TNoSEP.getText(),"idrg_to_ina","idrg_to_ina_res",requestJson,response.toString() );
                tLog1.append(response+"\n------------ \n");
                System.out.println("response import idrg to inacbg : "+response);

                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        Toast.show("RESPON : "+code.getString("message"), ToastType.SUCCESS);
                        System.out.println("response final idrg : "+code.getString("message"));
                        diagnosaTakBerlaku(response);
                    }else{
                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in idrg_to_inacbg: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void inacbgSetDx(){
        if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"inacbg_diagnosa_set\",\n" +
                                            "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"diagnosa\":\""+icd10.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                System.out.println("response inacbg : "+response);
//                update_respon(TNoRw.getText(),TNoSEP.getText(),"set_diagnosa","set_diagnosa_res",requestJson,response.toString() );
                tLog.append(response+"\n------------ \n");
                
                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        System.out.println("set "+code.getString("method")+" : "+code.getString("message"));
                    }else{
//                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void inacbgSetPx(){
        if (TNoRM.getText().trim().equals("") || TNoSEP.getText().trim().equals("") || Tnoka.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"inacbg_procedure_set\",\n" +
                                            "\"nomor_sep\":\""+TNoSEP.getText().trim()+"\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"procedure\":\""+icd9.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                System.out.println("response inacbg : "+response);
//                update_respon(TNoRw.getText(),TNoSEP.getText(),"set_diagnosa","set_diagnosa_res",requestJson,response.toString() );
                tLog.append(response+"\n------------ \n");
                
                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        System.out.println("set "+code.getString("method")+" : "+code.getString("message"));
                    }else{
//                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        Toast.show("RESPON : "+code.getString("message"), ToastType.ERROR);
                    }
                } else {
                    System.out.println("response : "+response); 
                    System.err.println("\n The 'data' field is missing in the response.");
                }

            } catch (Exception e) {
                System.err.println("Error in sitb: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
        
    private void updateicd9(String norw){

//                prosedur
        try {
            rsprosedur = koneksi.prepareStatement("select icd9.kode, icd9.deskripsi_panjang from prosedur_pasien inner join icd9 on prosedur_pasien.kode=icd9.kode where prosedur_pasien.no_rawat='"+norw+"' "
                    + "order by prosedur_pasien.prioritas asc").executeQuery();
            j=1;
            while (rsprosedur.next()) {
//                        System.out.println("data "+rsprosedur.getString("kode"));

                if (j == 1) {
                    kd_psdr_1.setText(rsprosedur.getString("kode"));
                    nm_psdr_1.setText(rsprosedur.getString("deskripsi_panjang"));
                }
                if (j == 2) {
                    kd_psdr_2.setText(rsprosedur.getString("kode"));
                    nm_psdr_2.setText(rsprosedur.getString("deskripsi_panjang"));
                }
                if (j == 3) {
                    kd_psdr_3.setText(rsprosedur.getString("kode"));
                    nm_psdr_3.setText(rsprosedur.getString("deskripsi_panjang"));
                }
                if (j == 4) {
                    kd_psdr_4.setText(rsprosedur.getString("kode"));
                    nm_psdr_4.setText(rsprosedur.getString("deskripsi_panjang"));
                }
                if (j == 5) {
                    kd_psdr_5.setText(rsprosedur.getString("kode"));
                    nm_psdr_5.setText(rsprosedur.getString("deskripsi_panjang"));
                }
                if (j == 6) {
                    kd_psdr_6.setText(rsprosedur.getString("kode"));
                    nm_psdr_6.setText(rsprosedur.getString("deskripsi_panjang"));
                }
                if (j == 7) {
                    kd_psdr_7.setText(rsprosedur.getString("kode"));
                    nm_psdr_7.setText(rsprosedur.getString("deskripsi_panjang"));
                }
                if (j == 8) {
                    kd_psdr_8.setText(rsprosedur.getString("kode"));
                    nm_psdr_8.setText(rsprosedur.getString("deskripsi_panjang"));
                }
                if (j == 9) {
                    kd_psdr_9.setText(rsprosedur.getString("kode"));
                    nm_psdr_9.setText(rsprosedur.getString("deskripsi_panjang"));
                }
                if (j == 10) {
                    kd_psdr_10.setText(rsprosedur.getString("kode"));
                    nm_psdr_10.setText(rsprosedur.getString("deskripsi_panjang"));
                }

                j++;
            }
            icd9.setText("");
            icd9.setText(kd_psdr_1.getText().trim() + (multiple1.getText().trim().equals("") ? "" : ("+" + multiple1.getText())) + 
                (kd_psdr_2.getText().trim().equals("") ? "" : ("#"+kd_psdr_2.getText() + (multiple2.getText().trim().equals("") ? "" : "+" + multiple2.getText())) )
               +(kd_psdr_3.getText().trim().equals("") ? "" : "#"+kd_psdr_3.getText() + (multiple3.getText().trim().equals("") ? "" : "+" + multiple3.getText()) )
               +(kd_psdr_4.getText().trim().equals("") ? "" : "#"+kd_psdr_4.getText() + (multiple4.getText().trim().equals("") ? "" : "+" + multiple4.getText()) )
               +(kd_psdr_5.getText().trim().equals("") ? "" : "#"+kd_psdr_5.getText() + (multiple5.getText().trim().equals("") ? "" : "+" + multiple5.getText()) )
               +(kd_psdr_6.getText().trim().equals("") ? "" : "#"+kd_psdr_6.getText() + (multiple6.getText().trim().equals("") ? "" : "+" + multiple6.getText()) )
               +(kd_psdr_7.getText().trim().equals("") ? "" : "#"+kd_psdr_7.getText() + (multiple7.getText().trim().equals("") ? "" : "+" + multiple7.getText()) )
               +(kd_psdr_8.getText().trim().equals("") ? "" : "#"+kd_psdr_8.getText() + (multiple8.getText().trim().equals("") ? "" : "+" + multiple8.getText()) )
               +(kd_psdr_9.getText().trim().equals("") ? "" : "#"+kd_psdr_9.getText() + (multiple9.getText().trim().equals("") ? "" : "+" + multiple9.getText()) )
               +(kd_psdr_10.getText().trim().equals("") ? "" : "#"+kd_psdr_10.getText() + (multiple10.getText().trim().equals("") ? "" : "+" + multiple10.getText()) )
            );


        } catch (Exception e) {
            System.out.println("ICD 9 : "+e);
        }
    }
    
    private void prepareEditiDRG() {
//        for (int i = 2; i < 11; i++) {
//                tbResponse.setValueAt("", i, 2);
//        }
        
        // hasil iDRG
        txInfo.setText("");
        txMdc.setText("");
        txMdcCode.setText("");
        txDrg.setText("");
        txDrgCode.setText("");
        txStatus.setText("");
        
        // hasil inacbg
        lblBaseTariff.setText("");
        lblTarifAcute.setText("");
        lblTarifKronis.setText("");
        lbltarifProc.setText("");
        lblTarifPros.setText("");
        lblTarifInv.setText("");
        lblTarifDrugs.setText("");
        lblTarifGrup.setText("");
        lblTarifTotal.setText("");
        lblSelisih.setText("");
        lStatus.setText("");
        
        updateCombo=true;
        cmbProc.removeAllItems();  cmbProc.addItem("none : None");
        cmbPros.removeAllItems();  cmbPros.addItem("none : None");
        cmbInv.removeAllItems();   cmbInv.addItem("none : None");
        cmbDrugs.removeAllItems(); cmbDrugs.addItem("none : None");
        updateCombo=false;
        
        Txtgroup.setText("");
    }
    
    private void diagnosaTakBerlaku(JSONObject response) {
        try {
            JSONArray expanded = response.getJSONObject("data")
                    .getJSONObject("diagnosa")
                    .getJSONArray("expanded");
            
            JSONArray expanded2 = response.getJSONObject("data")
                    .getJSONObject("procedure")
                    .getJSONArray("expanded");

            for (int i = 0; i < expanded.length(); i++) {

                JSONObject item = expanded.getJSONObject(i);
                JSONObject meta = item.getJSONObject("metadata");
                String code = meta.getString("code");
                String message = meta.optString("message");
                String errorNo = meta.optString("error_no");
                
                if ("400".equals(code) && "IM tidak berlaku".equals(message)) {
                    // ketemu                    
                    tbCombo1.setValueAt(message, i, 7);                    
                } 
            }
            
            for (int i = 0; i < expanded2.length(); i++) {

                JSONObject itempx = expanded2.getJSONObject(i);
                JSONObject metapx = itempx.getJSONObject("metadata");
                String codepx = metapx.getString("code");
                String messagepx = metapx.optString("message");
                String errorNo = metapx.optString("error_no");
                
                if ("400".equals(codepx) && "IM tidak berlaku".equals(messagepx)) {
                    // ketemu
                    tbCombo22.setValueAt(messagepx, i, 7);
                }
            }
            disableDxInacbg(true);
            
        } catch (JSONException ex) {
            System.out.println( "log : "+ex);
            ex.printStackTrace();
        }
    }
    
    
private void tampildiagnosa(String jenis) {
        JTable tabelAktif = null;
        DefaultTableModel modelAktif = null;
        widget.TextBoxW11UI txt = null;
        if(jenis.equals("idrg")) {
            tabelAktif = tbCombo;
            modelAktif = tabCombo;
            txt = textDiag;
        } else if(jenis.equals("inacbg")) {
            tabelAktif = tbCombo1;
            modelAktif = tabCombo1;
            txt = textDiag2;
        }
        try{
            jml=0;
            for(i=0;i<tabelAktif.getRowCount();i++){
                if(tabelAktif.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml];
            kode=null;
            kode=new String[jml];
            nama=null;
            nama=new String[jml];
            ciripny=null;
            ciripny=new String[jml];
            keterangan=null;
            keterangan=new String[jml];
            kategori=null;
            kategori=new String[jml];
            cirium=null;
            cirium=new String[jml];

            index=0; 
            for(i=0;i<tabelAktif.getRowCount();i++){
                if(tabelAktif.getValueAt(i,0).toString().equals("true")){
                    pilih[index]=true;
                    kode[index]=tabelAktif.getValueAt(i,1).toString();
                    nama[index]=tabelAktif.getValueAt(i,2).toString();
                    ciripny[index]=tabelAktif.getValueAt(i,3).toString();
                    keterangan[index]=tabelAktif.getValueAt(i,4).toString();
                    kategori[index]=tabelAktif.getValueAt(i,5).toString();
                    cirium[index]=tabelAktif.getValueAt(i,6).toString();
                    index++;
                }
            }

            Valid.tabelKosong(modelAktif);
            for(i=0;i<jml;i++){
                modelAktif.addRow(new Object[] {pilih[i],kode[i],nama[i],ciripny[i],keterangan[i],kategori[i],cirium[i]});
            }       

//            pspenyakit=koneksi.prepareStatement("select CODE, CODE2, DESCRIPTION, SYSTEM, VALIDCODE, ACCPDX, ASTERISK, IM " +
//                        "FROM idrg WHERE SYSTEM LIKE 'ICD_10_2010_IM' AND ( CODE like ? OR CODE2 LIKE ? OR DESCRIPTION LIKE ? )" +
//                        "ORDER BY CODE LIMIT 100");
            try {
//                pspenyakit.setString(1,"%"+txt.getText().trim()+"%");
//                pspenyakit.setString(2,"%"+txt.getText().trim()+"%");
//                pspenyakit.setString(3,"%"+txt.getText().trim()+"%"); 

                String input = txt.getText().trim();
                String[] keywords = input.split("\\s+"); // pecah berdasarkan spasi

                // Bangun query dinamis
                StringBuilder sql = new StringBuilder(
                    "SELECT CODE, CODE2, DESCRIPTION, SYSTEM, VALIDCODE, ACCPDX, ASTERISK, IM " +
                    "FROM idrg WHERE SYSTEM LIKE 'ICD_10_2010_IM' "
                );

                if (keywords.length > 0) {
                    sql.append("AND (");
                    for (int i = 0; i < keywords.length; i++) {
                        if (i > 0) sql.append(" AND "); // supaya semua kata HARUS ada
                        sql.append("(CODE LIKE ? OR CODE2 LIKE ? OR DESCRIPTION LIKE ?)");
                    }
                    sql.append(") ");
                }
                sql.append("ORDER BY CODE LIMIT 100");
                pspenyakit = koneksi.prepareStatement(sql.toString());

                // Isi parameter dinamis
                int paramIndex = 1;
                for (String kw : keywords) {
                    String like = "%" + kw + "%";
                    pspenyakit.setString(paramIndex++, like); // CODE
                    pspenyakit.setString(paramIndex++, like); // CODE2
                    pspenyakit.setString(paramIndex++, like); // DESCRIPTION
                }
                
                rs=pspenyakit.executeQuery();
                while(rs.next()){
//            CODE, CODE2, DESCRIPTION, SYSTEM, VALIDCODE, ACCPDX, ASTERISK, IM
//            "P","Kode","Nama Penyakit","Valid Code", "ACCPDX", "ASTERISK","IM"}){                    
                    modelAktif.addRow(new Object[]{false,rs.getString("CODE"),
                                   rs.getString("DESCRIPTION"),
                                   rs.getString("VALIDCODE"),
                                   rs.getString("ACCPDX"),
                                   rs.getString("ASTERISK"),
                                   rs.getString("IM"),
                                   ""});
                } 
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(pspenyakit!=null){
                    pspenyakit.close();
                }
            }           
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }
    
    private void tampilTindakan(String jenis) {
        JTable tabelAktif = null;
        DefaultTableModel modelAktif = null;
        widget.TextBoxW11UI txt = null;
        if(jenis.equals("idrg")) {
            tabelAktif = tbCombo2;
            modelAktif = tabCombo2;
            txt = textProc;
        } else if(jenis.equals("inacbg")) {
            tabelAktif = tbCombo22;
            modelAktif = tabCombo22;
            txt = textProc2;
        }
        try{
            jml=0;
            for(i=0;i<tabelAktif.getRowCount();i++){
                if(tabelAktif.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml];
            kode=null;
            kode=new String[jml];
            nama=null;
            nama=new String[jml];
            ciripny=null;
            ciripny=new String[jml];
            keterangan=null;
            keterangan=new String[jml];
            kategori=null;
            kategori=new String[jml];
            cirium=null;
            cirium=new String[jml];
            multi=null;
            multi=new String[jml];

            index=0; 
            for(i=0;i<tabelAktif.getRowCount();i++){
                if(tabelAktif.getValueAt(i,0).toString().equals("true")){
                    pilih[index]=true;
                    kode[index]=tabelAktif.getValueAt(i,1).toString();
                    nama[index]=tabelAktif.getValueAt(i,2).toString();
                    ciripny[index]=tabelAktif.getValueAt(i,3).toString();
                    keterangan[index]=tabelAktif.getValueAt(i,4).toString();
                    kategori[index]=tabelAktif.getValueAt(i,5).toString();
                    cirium[index]=tabelAktif.getValueAt(i,6).toString();
                    
                    if(jenis.equals("idrg")) {
                        multi[index]=tabelAktif.getValueAt(i,7).toString();
                    }
                    
                    index++;
                }
            }

            Valid.tabelKosong(modelAktif);
            for(i=0;i<jml;i++){
                modelAktif.addRow(new Object[] {pilih[i],kode[i],nama[i],ciripny[i],keterangan[i],kategori[i],cirium[i],multi[i]});
            }       

//            pspenyakit=koneksi.prepareStatement("select CODE, CODE2, DESCRIPTION, SYSTEM, VALIDCODE, ACCPDX, ASTERISK, IM " +
//                        "FROM idrg WHERE SYSTEM LIKE 'ICD_9CM_2010_IM' AND ( CODE like ? OR CODE2 LIKE ? OR DESCRIPTION LIKE ? )" +
//                        "ORDER BY CODE LIMIT 100");
            try {
//                pspenyakit.setString(1,"%"+txt.getText().trim()+"%");
//                pspenyakit.setString(2,"%"+txt.getText().trim()+"%");
//                pspenyakit.setString(3,"%"+txt.getText().trim()+"%");

                String input = txt.getText().trim();
                String[] keywords = input.split("\\s+"); // pecah berdasarkan spasi

                // Bangun query dinamis
                StringBuilder sql = new StringBuilder(
                    "SELECT CODE, CODE2, DESCRIPTION, SYSTEM, VALIDCODE, ACCPDX, ASTERISK, IM " +
                    "FROM idrg WHERE SYSTEM LIKE 'ICD_9CM_2010_IM' "
                );

                if (keywords.length > 0) {
                    sql.append("AND (");
                    for (int i = 0; i < keywords.length; i++) {
                        if (i > 0) sql.append(" AND "); // supaya semua kata HARUS ada
                        sql.append("(CODE LIKE ? OR CODE2 LIKE ? OR DESCRIPTION LIKE ?)");
                    }
                    sql.append(") ");
                }
                sql.append("ORDER BY CODE LIMIT 100");
                pspenyakit = koneksi.prepareStatement(sql.toString());

                // Isi parameter dinamis
                int paramIndex = 1;
                for (String kw : keywords) {
                    String like = "%" + kw + "%";
                    pspenyakit.setString(paramIndex++, like); // CODE
                    pspenyakit.setString(paramIndex++, like); // CODE2
                    pspenyakit.setString(paramIndex++, like); // DESCRIPTION
                }


                rs=pspenyakit.executeQuery();
                while(rs.next()){
//            CODE, CODE2, DESCRIPTION, SYSTEM, VALIDCODE, ACCPDX, ASTERISK, IM
//            "P","Kode","Nama Penyakit","Valid Code", "ACCPDX", "ASTERISK","IM"}){                    
                    modelAktif.addRow(new Object[]{false,rs.getString("CODE"),
                                   rs.getString("DESCRIPTION"),
                                   rs.getString("VALIDCODE"),
                                   rs.getString("ACCPDX"),
                                   rs.getString("ASTERISK"),
                                   rs.getString("IM"),"1"});
                } 
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
                e.printStackTrace();
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(pspenyakit!=null){
                    pspenyakit.close();
                }
            }           
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
    }

    private void simpan_awal_respon(String norawat, String nosep) {
        String tgl =formatAngka.getDateNow() , jam=formatAngka.getJamSekarang();
        String coder="2020.90";
        String kolom = "no_rawat,no_sep,tanggal,jam,klaim_baru,klaim_baru_res,data_klaim,data_klaim_res,set_diagnosa,set_diagnosa_res,set_prosedur,set_prosedur_res,idrg_grouper,idrg_grouper_res,idrg_final,idrg_final_res,idrg_to_ina,idrg_to_ina_res,ina_grouper1,ina_grouper1_res,ina_grouper2,ina_grouper2_res,ina_final,ina_final_res,idrg_edit,idrg_edit_res,ina_edit,ina_edit_res,delete_klaim,delete_klaim_res,coder";
        String nilai = "'"+norawat+"', '"+nosep+"', '"+tgl+"','"+jam+"' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'' ,'"+coder+"' ";
        
        Sequel.menyimpans("idrg_json", kolom, nilai);

    }    
    
    private void update_respon(String norawat, String nosep, String jsonField, String responField, String json, String response) {
        String kriteria = "no_rawat='"+norawat+"' and no_sep='"+nosep+"' ";
        String setfield = jsonField+"='"+json+"', "+responField+"='"+response+"' ";
        
        Sequel.mengedittf( "idrg_json", kriteria, setfield );        
    }
    
    private void tampilDxPx(String jenis, String layan) {
        JTable tabelDxAktif = null;
        JTable tabelPxAktif = null;
        DefaultTableModel modelDxAktif = null;
        DefaultTableModel modelPxAktif = null;
        String queryDx = "",queryPx = "", m="";
        String dx1 = "select idrg.CODE, idrg.CODE2, idrg.DESCRIPTION, idrg.SYSTEM, idrg.VALIDCODE, idrg.ACCPDX, idrg.ASTERISK, idrg.IM " +
                            "from diagnosa_pasien inner join idrg on diagnosa_pasien.kd_penyakit=idrg.CODE AND idrg.SYSTEM LIKE 'ICD_10_2010_IM' " +
                            "where diagnosa_pasien.no_rawat='"+TNoRw.getText()+"' and status = '"+layan+"' " +
                            "order by diagnosa_pasien.prioritas asc";
        String dx2 = "select idrg.CODE, idrg.CODE2, idrg.DESCRIPTION, idrg.SYSTEM, idrg.VALIDCODE, idrg.ACCPDX, idrg.ASTERISK, idrg.IM " +
                            "from diagnosa_pasien_idrg inner join idrg on diagnosa_pasien_idrg.kd_penyakit=idrg.CODE AND idrg.SYSTEM LIKE 'ICD_10_2010_IM' " +
                            "where diagnosa_pasien_idrg.no_rawat='"+TNoRw.getText()+"' and status = '"+layan+"' " +
                            "order by diagnosa_pasien_idrg.prioritas asc";
        
        String px1 = "select idrg.CODE, idrg.CODE2, idrg.DESCRIPTION, idrg.SYSTEM, idrg.VALIDCODE, idrg.ACCPDX, idrg.ASTERISK, idrg.IM " +
                            "from prosedur_pasien inner join idrg on prosedur_pasien.kode=idrg.CODE AND idrg.SYSTEM LIKE 'ICD_9CM_2010_IM' " +
                            "where prosedur_pasien.no_rawat='"+TNoRw.getText()+"' and status = '"+layan+"' " +
                            "order by prosedur_pasien.prioritas asc;";
        String px2 = "select idrg.CODE, idrg.CODE2, idrg.DESCRIPTION, idrg.SYSTEM, idrg.VALIDCODE, idrg.ACCPDX, idrg.ASTERISK, idrg.IM " +
                            "from prosedur_pasien_idrg inner join idrg on prosedur_pasien_idrg.kode=idrg.CODE AND idrg.SYSTEM LIKE 'ICD_9CM_2010_IM' " +
                            "where prosedur_pasien_idrg.no_rawat='"+TNoRw.getText()+"' and status = '"+layan+"' " +
                            "order by prosedur_pasien_idrg.prioritas asc;";

        if(jenis.equals("idrg")) {
            modelDxAktif = tabCombo;
            modelPxAktif = tabCombo2;
            queryDx = dx2; queryPx = px2;
            m="1";

        } else if(jenis.equals("inacbg")) {
            modelDxAktif = tabCombo1;
            modelPxAktif = tabCombo22;
            queryDx = dx1; queryPx = px1;
        }

        try{
            rsdiagnosa = koneksi.prepareStatement( queryDx ).executeQuery();
            Valid.tabelKosong(modelDxAktif);
            
            i=1;
            while (rsdiagnosa.next()) {                        
                modelDxAktif.addRow(new Object[]{
                      true,rsdiagnosa.getString("CODE"),
                           rsdiagnosa.getString("DESCRIPTION"),
                           rsdiagnosa.getString("VALIDCODE"),
                           rsdiagnosa.getString("ACCPDX"),
                           rsdiagnosa.getString("ASTERISK"),
                           rsdiagnosa.getString("IM"),
                           "" });
            }
            
            rsprosedur = koneksi.prepareStatement( queryPx ).executeQuery();
            Valid.tabelKosong(modelPxAktif);
            
            i=1;
            while (rsprosedur.next()) {                        
                modelPxAktif.addRow(new Object[]{true,rsprosedur.getString("CODE"),
                           rsprosedur.getString("DESCRIPTION"),
                           rsprosedur.getString("VALIDCODE"),
                           rsprosedur.getString("ACCPDX"),
                           rsprosedur.getString("ASTERISK"),
                           rsprosedur.getString("IM"),
                           m});
            }
            
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
        
    }
    
    private void simpanIdrg() {
        String norm = TNoRM.getText();
        String norawat = TNoRw.getText();
        String status = Status.getSelectedItem().toString();
        try {
            koneksi.setAutoCommit(false);

            Sequel.meghapus("diagnosa_pasien_idrg", "no_rawat", TNoRw.getText());
            
            index=1;
            for(i=0;i<tbCombo.getRowCount();i++){ 
                if(tbCombo.getValueAt(i,0).toString().equals("true")){
                    if(Sequel.cariInteger(
                            "select count(diagnosa_pasien_idrg.kd_penyakit) from diagnosa_pasien_idrg "+
                            "inner join reg_periksa inner join pasien on "+
                            "diagnosa_pasien_idrg.no_rawat=reg_periksa.no_rawat and "+
                            "reg_periksa.no_rkm_medis=pasien.no_rkm_medis where "+
                            "pasien.no_rkm_medis='"+norm+"' and diagnosa_pasien_idrg.kd_penyakit='"+tbCombo.getValueAt(i,1).toString()+"'")>0){
                        Sequel.menyimpan("diagnosa_pasien_idrg","?,?,?,?,?","Penyakit",5,new String[]{
                            norawat,tbCombo.getValueAt(i,1).toString(),status,
                            Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from diagnosa_pasien_idrg where no_rawat=? and status='"+status+"'",norawat),"Lama"
                        });
                    }else{
                        Sequel.menyimpan("diagnosa_pasien_idrg","?,?,?,?,?","Penyakit",5,new String[]{
                            norawat,tbCombo.getValueAt(i,1).toString(),status,
                            Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from diagnosa_pasien_idrg where no_rawat=? and status='"+status+"'",norawat),"Baru"
                        });
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Kemungkinan ada data diagnosa yang sama dimasukkan sebelumnya...!");
        }
        
        try {
            Sequel.meghapus("prosedur_pasien_idrg", "no_rawat", TNoRw.getText());
            koneksi.setAutoCommit(false);
            index=1;
            for(i=0;i<tbCombo2.getRowCount();i++){ 
                if(tbCombo2.getValueAt(i,0).toString().equals("true")){
                    Sequel.menyimpan("prosedur_pasien_idrg","?,?,?,?,?","ICD 9",5,new String[]{
                        norawat,
                        tbCombo2.getValueAt(i,1).toString(),
                        status,
                        Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from prosedur_pasien_idrg where no_rawat=? and status='"+status+"'",TNoRw.getText()),
                        tbCombo2.getValueAt(i,7).toString()
                    });
                }
            }
            koneksi.setAutoCommit(true);
        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Kemungkinan ada data diagnosa yang sama dimasukkan sebelumnya...!");
            ex.printStackTrace();
        }
    }
    
    private void simpan(){
        String norm = TNoRM.getText();
        String norawat = TNoRw.getText();
        String status = Status.getSelectedItem().toString();
        try {
            
            Sequel.meghapus("diagnosa_pasien", "no_rawat", TNoRw.getText());
            koneksi.setAutoCommit(false);

            
            index=1;
            for(i=0;i<tbCombo1.getRowCount();i++){ 
                if(tbCombo1.getValueAt(i,0).toString().equals("true")){
//                    if(Sequel.cariInteger(
//                            "select count(diagnosa_pasien.kd_penyakit) from diagnosa_pasien "+
//                            "inner join reg_periksa inner join pasien on "+
//                            "diagnosa_pasien.no_rawat=reg_periksa.no_rawat and "+
//                            "reg_periksa.no_rkm_medis=pasien.no_rkm_medis where "+
//                            "pasien.no_rkm_medis='"+norm+"' and diagnosa_pasien.kd_penyakit='"+tbCombo1.getValueAt(i,1).toString()+"'")>0){
//                        Sequel.menyimpan("diagnosa_pasien","?,?,?,?,?","Penyakit",5,new String[]{
//                            norawat,tbCombo1.getValueAt(i,1).toString(),status,
//                            Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from diagnosa_pasien where no_rawat=? and status='"+status+"'",norawat),"Lama"
//                        });
//                    }else{
                    Sequel.menyimpan("diagnosa_pasien","?,?,?,?,?","Penyakit",5,new String[]{
                        norawat,tbCombo1.getValueAt(i,1).toString(),status,
                        Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from diagnosa_pasien where no_rawat=? and status='"+status+"'",norawat),"Baru"
                    });
//                    }  
                    
                    if(index==1){
                        if(status.equals("Ralan")){
                            Sequel.mengedit("resume_pasien","no_rawat=?","kd_diagnosa_utama=?",2,new String[]{
                                tbCombo1.getValueAt(i,1).toString(),norawat
                            });
                        }else if(status.equals("Ranap")){
                            Sequel.mengedit("resume_pasien_ranap","no_rawat=?","kd_diagnosa_utama=?",2,new String[]{
                                tbCombo1.getValueAt(i,1).toString(),norawat
                            });
                        }   
                    }else if(index==2){
                        if(status.equals("Ralan")){
                            Sequel.mengedit("resume_pasien","no_rawat=?","kd_diagnosa_sekunder=?",2,new String[]{
                                tbCombo1.getValueAt(i,1).toString(),norawat
                            });
                        }else if(status.equals("Ranap")){
                            Sequel.mengedit("resume_pasien_ranap","no_rawat=?","kd_diagnosa_sekunder=?",2,new String[]{
                                tbCombo1.getValueAt(i,1).toString(),norawat
                            });
                        }
                    }else if(index==3){
                        if(status.equals("Ralan")){
                            Sequel.mengedit("resume_pasien","no_rawat=?","kd_diagnosa_sekunder2=?",2,new String[]{
                                tbCombo1.getValueAt(i,1).toString(),norawat
                            });
                        }else if(status.equals("Ranap")){
                            Sequel.mengedit("resume_pasien_ranap","no_rawat=?","kd_diagnosa_sekunder2=?",2,new String[]{
                                tbCombo1.getValueAt(i,1).toString(),norawat
                            });
                        }
                    }else if(index==4){
                        if(status.equals("Ralan")){
                            Sequel.mengedit("resume_pasien","no_rawat=?","kd_diagnosa_sekunder3=?",2,new String[]{
                                tbCombo1.getValueAt(i,1).toString(),norawat
                            });
                        }else if(status.equals("Ranap")){
                            Sequel.mengedit("resume_pasien_ranap","no_rawat=?","kd_diagnosa_sekunder3=?",2,new String[]{
                                tbCombo1.getValueAt(i,1).toString(),norawat
                            });
                        }   
                    }else if(index==5){
                        if(status.equals("Ralan")){
                            Sequel.mengedit("resume_pasien","no_rawat=?","kd_diagnosa_sekunder4=?",2,new String[]{
                                tbCombo1.getValueAt(i,1).toString(),norawat
                            });
                        }else if(status.equals("Ranap")){
                            Sequel.mengedit("resume_pasien_ranap","no_rawat=?","kd_diagnosa_sekunder4=?",2,new String[]{
                                tbCombo1.getValueAt(i,1).toString(),norawat
                            });
                        }
                    }
                        
                    index++;
                }                    
            }
            koneksi.setAutoCommit(true);  
//            for(i=0;i<tbCombo1.getRowCount();i++){ 
//               tbCombo1.setValueAt(false,i,0);
//            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Kemungkinan ada data diagnosa yang sama dimasukkan sebelumnya...!");
        }

        try {
            Sequel.meghapus("prosedur_pasien", "no_rawat", TNoRw.getText());
            koneksi.setAutoCommit(false);
            index=1;
            for(i=0;i<tbCombo22.getRowCount();i++){ 
                if(tbCombo22.getValueAt(i,0).toString().equals("true")){
                    Sequel.menyimpan("prosedur_pasien","?,?,?,?","ICD 9",4,new String[]{
                        norawat,tbCombo2.getValueAt(i,1).toString(),status,Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from prosedur_pasien where no_rawat=? and status='"+status+"'",norawat)
                    });
                    
                    if(index==1){
                        if(status.equals("Ralan")){
                            Sequel.mengedit("resume_pasien","no_rawat=?","kd_prosedur_utama=?",2,new String[]{
                                tbCombo22.getValueAt(i,1).toString(),norawat
                            });
                        }else if(status.equals("Ranap")){
                            Sequel.mengedit("resume_pasien_ranap","no_rawat=?","kd_prosedur_utama=?",2,new String[]{
                                tbCombo22.getValueAt(i,1).toString(),norawat
                            });
                        }
                    }else if(index==2){
                        if(status.equals("Ralan")){
                            Sequel.mengedit("resume_pasien","no_rawat=?","kd_prosedur_sekunder=?",2,new String[]{
                                tbCombo22.getValueAt(i,1).toString(),norawat
                            });
                        }else if(status.equals("Ranap")){
                            Sequel.mengedit("resume_pasien_ranap","no_rawat=?","kd_prosedur_sekunder=?",2,new String[]{
                                tbCombo22.getValueAt(i,1).toString(),norawat
                            });
                        }
                    }else if(index==3){
                        if(status.equals("Ralan")){
                            Sequel.mengedit("resume_pasien","no_rawat=?","kd_prosedur_sekunder2=?",2,new String[]{
                                tbCombo22.getValueAt(i,1).toString(),norawat
                            });
                        }else if(status.equals("Ranap")){
                            Sequel.mengedit("resume_pasien_ranap","no_rawat=?","kd_prosedur_sekunder2=?",2,new String[]{
                                tbCombo22.getValueAt(i,1).toString(),norawat
                            });
                        }
                    }else if(index==4){
                        if(status.equals("Ralan")){
                            Sequel.mengedit("resume_pasien","no_rawat=?","kd_prosedur_sekunder3=?",2,new String[]{
                                tbCombo22.getValueAt(i,1).toString(),norawat
                            });
                        }else if(status.equals("Ranap")){
                            Sequel.mengedit("resume_pasien_ranap","no_rawat=?","kd_prosedur_sekunder3=?",2,new String[]{
                                tbCombo22.getValueAt(i,1).toString(),norawat
                            });
                        }
                    }
                        
                    index++;
                }                    
            }
            koneksi.setAutoCommit(true);  
//            for(i=0;i<tbCombo22.getRowCount();i++){ 
//               tbCombo22.setValueAt(false,i,0);
//            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Kemungkinan ada data prosedur/ICD9 yang sama dimasukkan sebelumnya...!");
        }
//        pilihTab();
    }
    
    private void hitungKlaim() {
//        int bill[] = {0,0,0,0,0,0,0,0};
        int bill2[] = {0,0,0,0,0,0,0,0};
//        bill[0] = totalBill.getText().equals("") ? 0 : Integer.parseInt( totalBill.getText().replace(".", "").replace(",", "") );
//         bill[1] = tarifIna.getText().equals("") ? 0 : Integer.parseInt( tarifIna.getText().replace(".", "").replace(",", "") );
//          bill[2] = tarifSpeProc.getText().equals("") ? 0 : Integer.parseInt( tarifSpeProc.getText().replace(".", "").replace(",", "") );
//           bill[3] = tarifSpePros.getText().equals("") ? 0 : Integer.parseInt( tarifSpePros.getText().replace(".", "").replace(",", "") );
//            bill[4] = tarifSpeInvest.getText().equals("") ? 0 : Integer.parseInt( tarifSpeInvest.getText().replace(".", "").replace(",", "") );
//             bill[5] = tarifSpeDrugs.getText().equals("") ? 0 : Integer.parseInt( tarifSpeDrugs.getText().replace(".", "").replace(",", "") );

        bill2[0] = lblTarifRS.getText().equals("") ? 0 : Integer.parseInt( lblTarifRS.getText().replace(".", "").replace(",", "") );
         bill2[1] = lblBaseTariff.getText().equals("") ? 0 : Integer.parseInt( lblBaseTariff.getText().replace(".", "").replace(",", "") );
          bill2[2] = lbltarifProc.getText().equals("") ? 0 : Integer.parseInt( lbltarifProc.getText().replace(".", "").replace(",", "") );
           bill2[3] = lblTarifPros.getText().equals("") ? 0 : Integer.parseInt( lblTarifPros.getText().replace(".", "").replace(",", "") );
            bill2[4] = lblTarifInv.getText().equals("") ? 0 : Integer.parseInt( lblTarifInv.getText().replace(".", "").replace(",", "") );
             bill2[5] = lblTarifDrugs.getText().equals("") ? 0 : Integer.parseInt( lblTarifDrugs.getText().replace(".", "").replace(",", "") );
         
//        bill[6] = bill[1] + bill[2] + bill[3] + bill[4] + bill[5];
//        bill[7] = bill[6] - bill[0];
//        totalTarif.setText( Valid.SetAngka(bill[6]) );
//        selisihTarif.setText( Valid.SetAngka(bill[7]) );

        bill2[6] = bill2[1] + bill2[2] + bill2[3] + bill2[4] + bill2[5];
        bill2[7] = bill2[6] - bill2[0];
        lblTarifTotal.setText( Valid.SetAngka(bill2[6]) );
        lblSelisih.setText( Valid.SetAngka(bill2[7]) );        
    }
    
    private void cek2ndRow(List<Integer> listIndex) {
//        if( tbCombo.getValueAt(listIndex.get(1), 4).toString().equals("N") ) {
            System.out.println("index : "+tbCombo.getValueAt(listIndex.get(0), 4).toString());
            System.out.println("index : "+listIndex.get(1));
            Toast.show("Diagnosa kedua, Tidak Dapat digunakan sebagai Diagnosa Utama", ToastType.ERROR);
            for (int i = 1; i < listIndex.size(); i++) {
                tabCombo.setValueAt(Boolean.FALSE, listIndex.get(i), 0);
//                Boolean value = (Boolean) tbCombo.getValueAt(i, 0);
//                if (value != null && value) {
//                    trueRows.add(i);
//                }
            }

//        }
    }
    
    private void cekDiagUtama() {
//        System.out.println("jalan..");
        java.util.List<Integer> trueRows = new java.util.ArrayList<>();
        for (int i = 0; i < tbCombo.getRowCount(); i++) {
            Boolean b = (Boolean) tbCombo.getValueAt(i, 0);
            if (Boolean.TRUE.equals(b)) {
                trueRows.add(i);
            }
        }
//        System.out.println("trueRows"+trueRows.toString());
        
        if (trueRows.size() >= 1) {
            int rowTrue2 = trueRows.get(0);
            Object valCol4 = tbCombo.getValueAt(rowTrue2, 4);
//            System.out.println("dx nya : "+tbCombo.getValueAt(rowTrue2, 2));
            if ("N".equals(valCol4)) {
                Toast.showWDurasi("Diagnosa Utama tidak valid, semua Diagnosa akan di hapus", ToastType.ERROR,6000);
                // set false mulai baris true kedua
                for (int i = 0; i < trueRows.size(); i++) {
                    tbCombo.setValueAt(false, trueRows.get(i), 0);
                }
            }
        }
    
    }
    
    private void setDx( String jns ) {
        String dx = "";
        JTable tabelDxAktif = null;
        DefaultTableModel modelDxAktif = null;
        DefaultTableModel modelPxAktif = null;
        
        if("idrg".equals(jns)) {
            modelDxAktif = tabCombo;
            tabelDxAktif = tbCombo;
        } else if("inacbg".equals(jns)) {
            modelDxAktif = tabCombo1;
            tabelDxAktif = tbCombo1;
        }
        for (int i = 0; i < modelDxAktif.getRowCount(); i++) {
            if ((Boolean) tabelDxAktif.getValueAt(i, 0)) {
                dx = dx + tabelDxAktif.getValueAt(i, 1).toString() +"#" ;
            }
        }
        if (dx.endsWith("#")) {
            dx = dx.substring(0, dx.length() - 1);
            icd10.setText(dx);
        } else {
            icd10.setText(dx);
        }
        
        System.out.println("diagnosa : "+dx);
    }
    
    private void setPx( String jns ) {
        String px = "", m="";
        JTable tabelPxAktif = null;
        DefaultTableModel modelDxAktif = null;
        DefaultTableModel modelPxAktif = null;
        
        if("idrg".equals(jns)) {
            modelPxAktif = tabCombo2;
            tabelPxAktif = tbCombo2;
        } else if("inacbg".equals(jns)) {
            modelPxAktif = tabCombo22;
            tabelPxAktif = tbCombo22;
        }
        
        for (int i = 0; i < modelPxAktif.getRowCount(); i++) {
            if ((Boolean) tabelPxAktif.getValueAt(i, 0)) {
                if("idrg".equals(jns)) {
                    m = tabelPxAktif.getValueAt(i, 7).toString();
//                    System.out.println("m : "+m);
                    if(Integer.parseInt(m) == 1 ) { 
                        m = "";
                    }
                    px = px + tabelPxAktif.getValueAt(i, 1).toString() + "+" + m +"#" ;
                } else {
                    px = px + tabelPxAktif.getValueAt(i, 1).toString() +"#" ;
                }
            }
        }
        
        if (px.endsWith("#")) {
            px = px.substring(0, px.length() - 1);
            icd9.setText(px);
        } else {
            icd9.setText(px);
        }
        if (px.endsWith("+")) {
            px = px.substring(0, px.length() - 1);
        }
        System.out.println("procedures : "+px);
    }
    
    private void hapusDxPxInacbg() {
        Sequel.meghapus("diagnosa_pasien", "no_rawat", TNoRw.getText());
        Sequel.meghapus("prosedur_pasien", "no_rawat", TNoRw.getText());
        tampilDxPx("inacbg", Status.getSelectedItem().toString());
//        tampildxpx("inacbg");
    }
    
    private void disableDxiDRG( Boolean bole) {
        textDiag.setEnabled(bole);
        textProc.setEnabled(bole);
        simpanDxPxIDRG.setEnabled(bole);
    }
    
    private void disableDxInacbg( Boolean bole) {
        textDiag2.setEnabled(bole);
        textProc2.setEnabled(bole);
        simpanDxPxInacbg.setEnabled(bole);
    }
    
    private void viewHasilGrouper1(JSONObject json) {
        try {
            updateCombo = true;
            JSONObject data = json.getJSONObject("response_inacbg");
            JSONObject trf = data.getJSONObject("cbg");
            if (data.has("tariff")) {
                total_rupiah1.setText(Valid.SetAngka(data.getDouble("tariff")));
                lblBaseTariff.setText(Valid.SetAngka(data.getDouble("tariff")));
            }

            respon.setText("CODE INACBG : "+trf.getString("code"));
            respon2.setText("DESKRIPSI : "+trf.getString("description"));
            lStatus.setText("Normal");
                        
            if(json.has("special_cmg_option")) {
                JSONArray arr = json.getJSONArray("special_cmg_option");
                String[] jenis = {"Special Procedure","Special Prosthesis","Special Investigation","Special Drug"};
                JComboBox<String>[] combos = new JComboBox[] { cmbProc, cmbPros, cmbInv, cmbDrugs };



                    for (int i = 0; i < jenis.length; i++) {
                        combos[i].removeAllItems();
                        combos[i].addItem("none : None");         

                        for (int j = 0; j < arr.length(); j++) {
                            JSONObject cmg      = arr.getJSONObject(j);
                            String type         = cmg.getString("type");
                            String description  = cmg.getString("description");
                            String code         = cmg.getString("code");

                            if(type.equalsIgnoreCase(jenis[i])) {
                                combos[i].setEnabled(true);
                                combos[i].addItem(code + " : " + description);
                            }
                        }
                    }
            }
            updateCombo = false;
        } catch (Exception ex) {
            System.out.println("notif : "+ex);
//            ex.printStackTrace();
        }
        
    }
    
    private void HasilGrouper2(JSONObject json) {
        JLabel[] lbl = new JLabel[]{lbltarifProc, lblTarifPros, lblTarifInv, lblTarifDrugs};
        String[] jenis = {"Special Procedure","Special Prosthesis","Special Investigation","Special Drug"};
        JComboBox<String>[] combos = new JComboBox[] { cmbProc, cmbPros, cmbInv, cmbDrugs };
        
        try {
            JSONObject data = json.getJSONObject("response_inacbg");            
            JSONObject trf  = data.getJSONObject("cbg");
            JSONArray  arr  = data.getJSONArray("special_cmg");
            String type         = "";
            String description  = "";
            String code = "";
            String[] codes = {"","","",""};
            
            Txtgroup.setText(trf.getString("description") + " - " + trf.getString("code"));
            
            for (int i = 0; i < jenis.length; i++) {
                for (int j = 0; j < arr.length(); j++) {
                    JSONObject cmg      = arr.getJSONObject(j);
                    type         = cmg.getString("type");
                    description  = cmg.getString("description");
                    code         = cmg.getString("code");
                    
                    if(type.equalsIgnoreCase(jenis[i])) {
                        lbl[i].setText(String.valueOf( Valid.SetAngka(cmg.getInt("tariff"))) );
                        codes[i] = code;
                        combos[i].setEnabled(true);
                    }
                }
                    String potong = "";
                    if (codes[i] != null && !codes[i].toString().isEmpty()) {
                        potong = codes[i].toString().substring(0,5).replace("-", ""); //substring(0, codes[i].lastIndexOf("-"));
//                        System.out.println("potong : "+potong);
                        setSelectedItemContains(combos[i],potong );
                    }                     
            }                        
            
        } catch (Exception ex) {
            System.out.println("notif : "+ex);
            ex.printStackTrace();
        }
          
    }
    
    private void setCMG() {
       JComboBox<String>[] combo = new JComboBox[] {cmbProc,cmbPros,cmbInv,cmbDrugs};
       String cmgs = "";
       
       for (JComboBox<String> c : combo) {
           cmgs = cmgs + c.getSelectedItem().toString().split(":")[0].trim() + "#";
       }
       
       Cmgs.setText( cmgs.substring(0, cmgs.length() - 1) );       
    }
    
    private void cariLog(String norawat) {
        List<List<String>> dataLog = Sequel.cariData("select  idrg_grouper_res, ina_grouper1_res, ina_grouper2_res from idrg_json where no_rawat = '"+norawat+"' order by id desc limit 1");
        String idrgRes = "", inagruper1="",inagruper2="";
        // simpan data yg baru
        for (List<String> baris : dataLog) {
            idrgRes = baris.get(0);
            inagruper1 = baris.get(1);
            inagruper2 = baris.get(2);
//            Sequel.menyimpan("diagnosa_pasien", " '"+baris.get(0)+"', '"+baris.get(1)+"', '"+baris.get(2)+"', '"+baris.get(3)+"', '"+baris.get(4)+"' ");                     
        }
        try {
            //load iDRG responses
            if( !idrgRes.equals("") ){
                JSONObject root = new JSONObject(idrgRes);
                JSONObject idrg = root.getJSONObject("response_idrg");
                
                txMdcCode.setText(idrg.getString("mdc_number"));
                txMdc.setText(idrg.getString("mdc_description"));                

                txDrgCode.setText(idrg.getString("drg_code"));
                txDrg.setText(idrg.getString("drg_description"));
            }
            
            //load inaGgrouper1 responses
            if( !inagruper1.equals("") ){
                JSONObject root = new JSONObject(inagruper1);
                viewHasilGrouper1(root);
                hitungKlaim();
            }
            
            //load inaGgrouper2 responses
            if( !inagruper2.equals("") ){
                JSONObject root = new JSONObject(inagruper2);
                simpanDxPxInacbg1.setEnabled(false);
                simpanDxPxInacbg.setEnabled(false);
                HasilGrouper2(root);
                hitungKlaim();
            }
            
        } catch (Exception ex) {
            System.out.println("notif : "+ex);
            ex.printStackTrace();
        }
    }
    
    public static void setSelectedItemContains(JComboBox<String> combo, String text) {
        if (text == null || text.trim().isEmpty()) {
            return; // kalau pencarian kosong, jangan lakukan apa-apa
        }
//        System.out.println("comb : "+combo.toString());
//        System.out.println("text : "+text);
        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i);
            if ( item != null && containsIgnoreCase(item,text) ) {
                combo.setSelectedItem(item);
                return; // berhenti setelah menemukan yang cocok
            }
        }
    }
    
    public static boolean containsIgnoreCase(String src, String what) {
        if (src == null || what == null) {
            return false;
        }
        return src.toLowerCase().contains(what.toLowerCase());
    }
    
//end of class    
    
    public void tampil() {
        Valid.tabelKosong(tabMode);
        String statusKirim = "";
        
        try (PreparedStatement ps = koneksi.prepareStatement(
            "select bridging_sep.no_rawat, bridging_sep.no_sep, reg_periksa.no_rkm_medis, pasien.nm_pasien, reg_periksa.status_lanjut, bridging_sep.tglsep, date(bridging_sep.tglpulang) " +
            "as tglpulang, kamar_inap.stts_pulang, case when reg_periksa.status_lanjut = 'Ranap' then concat(kamar_inap.kd_kamar, ' ', bangsal.nm_bangsal) when reg_periksa.status_lanjut " +
            "= 'Ralan' then poliklinik.nm_poli end as ruangan, dokter.nm_dokter, diagnosa_pasien.kd_penyakit, (inacbg_cetak_klaim.no_sep is not null) as inacbg_terkirim,bridging_sep.no_kartu from bridging_sep " +
            "join reg_periksa on bridging_sep.no_rawat = reg_periksa.no_rawat and (if(bridging_sep.jnspelayanan = '1', 'Ranap', 'Ralan')) = reg_periksa.status_lanjut join pasien on " +
            "reg_periksa.no_rkm_medis = pasien.no_rkm_medis join poliklinik on reg_periksa.kd_poli = poliklinik.kd_poli left join maping_dokter_dpjpvclaim on bridging_sep.kddpjp = " +
            "maping_dokter_dpjpvclaim.kd_dokter_bpjs left join dokter on maping_dokter_dpjpvclaim.kd_dokter = dokter.kd_dokter left join diagnosa_pasien on reg_periksa.no_rawat = " +
            "diagnosa_pasien.no_rawat and reg_periksa.status_lanjut = diagnosa_pasien.status and diagnosa_pasien.prioritas = '1' left join inacbg_cetak_klaim on bridging_sep.no_sep " +
            "= inacbg_cetak_klaim.no_sep left join kamar_inap on reg_periksa.no_rawat = kamar_inap.no_rawat and kamar_inap.stts_pulang != 'Pindah Kamar' left join kamar on kamar_inap.kd_kamar " +
            "= kamar.kd_kamar left join bangsal on kamar.kd_bangsal = bangsal.kd_bangsal where bridging_sep.no_sep like ? and bridging_sep.jnspelayanan like ? and bridging_sep.tglsep between " +
            "? and ? and length(bridging_sep.no_sep) = 19  and reg_periksa.status_bayar = 'Sudah Bayar' and reg_periksa.kd_pj like ? " + (TCari.getText().equals("") ? "" : "and ( " +
            "reg_periksa.no_rawat like ? or bridging_sep.no_sep like ? or reg_periksa.no_rkm_medis like ? or pasien.nm_pasien like ? or poliklinik.nm_poli like ? or concat( " +
            "kamar_inap.kd_kamar, ' ', bangsal.nm_bangsal) like ? or dokter.nm_dokter like ?) ") + "group by bridging_sep.no_sep, bridging_sep.no_rawat, reg_periksa.no_rkm_medis, " +
            "reg_periksa.status_lanjut order by bridging_sep.jnspelayanan desc, bridging_sep.no_sep"
        )) {
            ps.setString(1, KODEPPKBPJS);
            if (CmbStatusRawat.getSelectedIndex() == 0) {
                ps.setString(2, "%");
            } else if (CmbStatusRawat.getSelectedIndex() == 1) {
                ps.setString(2, "2");
            } else if (CmbStatusRawat.getSelectedIndex() == 2) {
                ps.setString(2, "1");
            }
            ps.setString(3, Valid.getTglSmc(DTPCari1));
            ps.setString(4, Valid.getTglSmc(DTPCari2));
            ps.setString(5, "%");
            if (!TCari.getText().equals("")) {
                ps.setString(6, "%" + TCari.getText() + "%");
                ps.setString(7, "%" + TCari.getText() + "%");
                ps.setString(8, "%" + TCari.getText() + "%");
                ps.setString(9, "%" + TCari.getText() + "%");
                ps.setString(10, "%" + TCari.getText() + "%");
                ps.setString(11, "%" + TCari.getText() + "%");
                ps.setString(12, "%" + TCari.getText() + "%");
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tabMode.addRow(new Object[] {
                        rs.getString("no_rawat"),
                        rs.getString("no_sep"),
                        rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),
                        rs.getString("status_lanjut"),
                        rs.getString("tglsep"),
                        rs.getString("tglpulang"),
                        rs.getString("stts_pulang"),
                        rs.getString("ruangan"),
                        rs.getString("nm_dokter"),
                        rs.getString("kd_penyakit"),
                        (rs.getBoolean("inacbg_terkirim") ? "Terkirim" : "Belum Terkirim"),
                        rs.getString("inacbg_terkirim"),
                        rs.getString("no_kartu")
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        LCount.setText(String.valueOf(tabMode.getRowCount()));
    }
    
    public void tampil2() {
        Valid.tabelKosong(tabMode);
        String statusKirim = "";
        
        try (PreparedStatement ps = koneksi.prepareStatement(
            "select bridging_sep.no_rawat, bridging_sep.no_sep, reg_periksa.no_rkm_medis, pasien.nm_pasien, reg_periksa.status_lanjut, bridging_sep.tglsep, date(bridging_sep.tglpulang) " +
            "as tglpulang, kamar_inap.stts_pulang, case when reg_periksa.status_lanjut = 'Ranap' then concat(kamar_inap.kd_kamar, ' ', bangsal.nm_bangsal) when reg_periksa.status_lanjut " +
            "= 'Ralan' then poliklinik.nm_poli end as ruangan, dokter.nm_dokter, diagnosa_pasien.kd_penyakit, (inacbg_cetak_klaim.no_sep is not null) as inacbg_terkirim,bridging_sep.no_kartu from bridging_sep " +
            "join reg_periksa on bridging_sep.no_rawat = reg_periksa.no_rawat and (if(bridging_sep.jnspelayanan = '1', 'Ranap', 'Ralan')) = reg_periksa.status_lanjut join pasien on " +
            "reg_periksa.no_rkm_medis = pasien.no_rkm_medis join poliklinik on reg_periksa.kd_poli = poliklinik.kd_poli left join maping_dokter_dpjpvclaim on bridging_sep.kddpjp = " +
            "maping_dokter_dpjpvclaim.kd_dokter_bpjs left join dokter on maping_dokter_dpjpvclaim.kd_dokter = dokter.kd_dokter left join diagnosa_pasien on reg_periksa.no_rawat = " +
            "diagnosa_pasien.no_rawat and reg_periksa.status_lanjut = diagnosa_pasien.status and diagnosa_pasien.prioritas = '1' left join inacbg_cetak_klaim on bridging_sep.no_sep " +
            "= inacbg_cetak_klaim.no_sep left join kamar_inap on reg_periksa.no_rawat = kamar_inap.no_rawat and kamar_inap.stts_pulang != 'Pindah Kamar' left join kamar on kamar_inap.kd_kamar " +
            "= kamar.kd_kamar left join bangsal on kamar.kd_bangsal = bangsal.kd_bangsal where bridging_sep.no_sep like ? and bridging_sep.jnspelayanan like ? and date(kamar_inap.tgl_keluar) between " +
            "? and ? and length(bridging_sep.no_sep) = 19 and reg_periksa.status_bayar = 'Sudah Bayar' and reg_periksa.kd_pj like ? " + (TCari.getText().equals("") ? "" : "and ( " +
            "reg_periksa.no_rawat like ? or bridging_sep.no_sep like ? or reg_periksa.no_rkm_medis like ? or pasien.nm_pasien like ? or poliklinik.nm_poli like ? or concat( " +
            "kamar_inap.kd_kamar, ' ', bangsal.nm_bangsal) like ? or dokter.nm_dokter like ?) ") + "group by bridging_sep.no_sep, bridging_sep.no_rawat, reg_periksa.no_rkm_medis, " +
            "reg_periksa.status_lanjut order by bridging_sep.jnspelayanan desc, bridging_sep.no_sep"
        )) {
            ps.setString(1, KODEPPKBPJS);
            if (CmbStatusRawat.getSelectedIndex() == 0) {
                ps.setString(2, "%");
            } else if (CmbStatusRawat.getSelectedIndex() == 1) {
                ps.setString(2, "2");
            } else if (CmbStatusRawat.getSelectedIndex() == 2) {
                ps.setString(2, "1");
            }
            ps.setString(3, Valid.getTglSmc(DTPCari1));
            ps.setString(4, Valid.getTglSmc(DTPCari2));
            ps.setString(5, "%");
            if (!TCari.getText().equals("")) {
                ps.setString(6, "%" + TCari.getText() + "%");
                ps.setString(7, "%" + TCari.getText() + "%");
                ps.setString(8, "%" + TCari.getText() + "%");
                ps.setString(9, "%" + TCari.getText() + "%");
                ps.setString(10, "%" + TCari.getText() + "%");
                ps.setString(11, "%" + TCari.getText() + "%");
                ps.setString(12, "%" + TCari.getText() + "%");
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tabMode.addRow(new Object[] {
                        rs.getString("no_rawat"),
                        rs.getString("no_sep"),
                        rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),
                        rs.getString("status_lanjut"),
                        rs.getString("tglsep"),
                        rs.getString("tglpulang"),
                        rs.getString("stts_pulang"),
                        rs.getString("ruangan"),
                        rs.getString("nm_dokter"),
                        rs.getString("kd_penyakit"),
                        (rs.getBoolean("inacbg_terkirim") ? "Terkirim" : "Belum Terkirim"),
                        rs.getString("inacbg_terkirim"),
                        rs.getString("no_kartu")
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        LCount.setText(String.valueOf(tabMode.getRowCount()));
    }

    private void getData() {
        if (tbKompilasi.getSelectedRow() != -1) {
            TPasien.setText(tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 3).toString());
            TNoRw.setText(tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 0).toString());
            TNoRM.setText(tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 2).toString());
            Tnoka.setText(tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 13).toString());
            if(tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 4).toString().equals("Ralan")){
                Status.setSelectedIndex(0);
                jenis_rawat.setSelectedIndex(1);
            }else{
                Status.setSelectedIndex(1);
                jenis_rawat.setSelectedIndex(0);
            }
            setDiagnosa(tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 0).toString(), tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 2).toString(), tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 3).toString(), tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 4).toString());
//            lblStatusRawat.setText(tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 4).toString());
            TNoSEP.setText(tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 1).toString());
            dpjp.setText(tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 9).toString());
//            lblNoRawatGabung.setText(Sequel.cariIsi("select no_rawat2 from ranap_gabung where no_rawat like ?",lblNoRawat.getText()));
//            if(lblStatusRawat.getText().equals("Ralan")){
//                lblTglSEP.setText(tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 5).toString());
//            }else{
//                lblTglSEP.setText(tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 6).toString());
////                lblTglSEP.setText(Sequel.cariIsi("select bridging_sep.tglpulang from bridging_sep where bridging_sep.no_rawat='"+lblNoRawat.getText()+"'").toString().substring(0, 10));
//            }
////            lblTglSEP.setText(tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 5).toString());
////            lblTglSEP.setText(Sequel.cariIsi("select bridging_sep.tglpulang from bridging_sep where bridging_sep.no_rawat='"+lblNoRawat.getText()+"'"));
//            String noSuratKontrol = Sequel.cariIsiSmc("select noskdp from bridging_sep where no_sep = ?", lblNoSEP.getText());
//            if (noSuratKontrol.equals("")) {
//                noSuratKontrol = Sequel.cariIsiSmc("select noskdp from bridging_sep where no_rawat = ? and noskdp != ''", lblNoRawat.getText());
//            }
//            btnSEP.setText("Ada");
//            btnSEP.setEnabled(true);
//            try (PreparedStatement ps = koneksi.prepareStatement(
//                "select exists(select * from data_triase_igd where data_triase_igd.no_rawat = bridging_sep.no_rawat) as ada_triase, " +
//                "exists(select * from resume_pasien_ranap where resume_pasien_ranap.no_rawat = bridging_sep.no_rawat) as ada_resume_ranap, " +
//                "exists(select * from inacbg_cetak_klaim where inacbg_cetak_klaim.no_sep = bridging_sep.no_sep) as ada_cetak_klaim, " +
//                "exists(select * from penilaian_medis_igd where penilaian_medis_igd.no_rawat = bridging_sep.no_rawat) as ada_awal_medis_igd, " +
//                "exists(select * from periksa_lab where periksa_lab.no_rawat = bridging_sep.no_rawat) as ada_periksa_lab, " +
//                "exists(select * from periksa_radiologi where periksa_radiologi.no_rawat = bridging_sep.no_rawat) as ada_periksa_rad, " +
//                "exists(select * from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat = ?) as ada_skdp, " +
//                "exists(select * from bridging_surat_pri_bpjs where bridging_surat_pri_bpjs.no_rawat = bridging_sep.no_rawat) as ada_spri, " +
//                "exists(select * from billing where billing.no_rawat = bridging_sep.no_rawat) as ada_billing from bridging_sep where bridging_sep.no_sep = ?"
//            )) {
//                ps.setString(1, noSuratKontrol);
//                ps.setString(2, lblNoSEP.getText());
//                try (ResultSet rs = ps.executeQuery()) {
//                    if (rs.next()) {
//                        flipStatus(btnPDFKlaimINACBG, rs.getBoolean("ada_cetak_klaim"));
//                        flipStatus(btnInvoice, rs.getBoolean("ada_billing"));
//                        flipStatus(btnTriaseIGD, rs.getBoolean("ada_triase"));
//                        flipStatus(btnAwalMedisIGD, rs.getBoolean("ada_awal_medis_igd"));
//                        flipStatus(btnResumeRanap, rs.getBoolean("ada_resume_ranap"));
//                        flipStatus(btnHasilLab, rs.getBoolean("ada_periksa_lab"));
//                        flipStatus(btnHasilRad, rs.getBoolean("ada_periksa_rad"));
//                        flipStatus(btnSPRI, rs.getBoolean("ada_spri"));
//                        flipStatus(btnSurkon, rs.getBoolean("ada_skdp"));
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("Notif : " + e);
//                flipStatus(btnPDFKlaimINACBG, false);
//                flipStatus(btnInvoice, false);
//                flipStatus(btnTriaseIGD, false);
//                flipStatus(btnAwalMedisIGD, false);
//                flipStatus(btnResumeRanap, false);
//                flipStatus(btnHasilLab, false);
//                flipStatus(btnHasilRad, false);
//                flipStatus(btnSPRI, false);
//                flipStatus(btnSurkon, false);
//            }
//            panelDiagnosaSmc.setRM(lblNoRawat.getText(), lblNoRM.getText(), Valid.getTglSmc(DTPCari1), Valid.getTglSmc(DTPCari2), lblStatusRawat.getText());
//            panelDiagnosaSmc.batal();
//            panelDiagnosaSmc.pilihTab(0);
//            lblTglSEP1.setText(lblTglSEP.getText().substring(5, 7));
//            if(Sequel.cariInteger("select count(no_skl) from pasien_bayi inner join reg_periksa on reg_periksa.no_rkm_medis=pasien_bayi.no_rkm_medis where reg_periksa.no_rawat='"+lblNoRawatGabung.getText()+"'")>0){
//                btnSKL.setText("Ada");
//                btnSKL.setEnabled(true);
//            }else{
//                btnSKL.setText("Tidak Ada");
//                btnSKL.setEnabled(false);
//            }
//            if(Sequel.cariInteger("select count(no_rawat) from pemeriksaan_ranap where pemeriksaan_ranap.no_rawat='"+lblNoRawat.getText()+"'")>0){
//                btnSOAPRanap.setText("Ada");
//                btnSOAPRanap.setEnabled(true);
//            }else{
//                btnSOAPRanap.setText("Tidak Ada");
//                btnSOAPRanap.setEnabled(false);
//            }
//            tampilINACBG();
//            tampilBilling();
        }
    }
    
}
