/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package rekammedis;

import fungsi.WarnaTable;
import fungsi.akses;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import kepegawaian.DlgCariPegawai;


/**
 *
 * @author perpustakaan
 */
public final class RMMonitoringPasienNeonatus2 extends javax.swing.JDialog {
//    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0;    
    private DlgCariPegawai pegawai=new DlgCariPegawai(null,false);
    private String finger="";
    private StringBuilder htmlContent;
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMMonitoringPasienNeonatus2(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        TCari.setDocument(new batasInput((int)100).getKata(TCari));
        
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
            });
        }
        
        pegawai.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(pegawai.getTable().getSelectedRow()!= -1){  
                    KodePetugas.setText(pegawai.getTable().getValueAt(pegawai.getTable().getSelectedRow(),0).toString());
                    NamaPetugas.setText(pegawai.getTable().getValueAt(pegawai.getTable().getSelectedRow(),1).toString());
                    btnPetugas.requestFocus();
                }  
                    
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        }); 
        
        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTML.setEditable(true);
        LoadHTML.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(
                ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"+
                ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"+
                ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"+
                ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"+
                ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"+
                ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
        );
        Document doc = kit.createDefaultDocument();
        LoadHTML.setDocument(doc);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnKriteriaKeluarHCU = new javax.swing.JMenuItem();
        LoadHTML = new widget.editorpane();
        JK = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        internalFrame2 = new widget.InternalFrame();
        PanelInput1 = new javax.swing.JPanel();
        scrollInput1 = new widget.ScrollPane();
        FormInput1 = new widget.PanelBiasa();
        FormInput3 = new widget.PanelBiasa();
        jLabel9 = new widget.Label();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel65 = new widget.Label();
        jLabel66 = new widget.Label();
        jLabel68 = new widget.Label();
        jLabel72 = new widget.Label();
        jLabel78 = new widget.Label();
        jLabel79 = new widget.Label();
        jLabel80 = new widget.Label();
        jLabel82 = new widget.Label();
        VenaPariver1 = new widget.TextBox();
        VenaPariver2 = new widget.TextBox();
        VenaPariver3 = new widget.TextBox();
        AlteriLine = new widget.TextBox();
        jLabel67 = new widget.Label();
        jLabel69 = new widget.Label();
        jLabel70 = new widget.Label();
        jLabel73 = new widget.Label();
        jLabel81 = new widget.Label();
        jLabel83 = new widget.Label();
        jLabel84 = new widget.Label();
        jLabel85 = new widget.Label();
        Drain1 = new widget.TextBox();
        Drain2 = new widget.TextBox();
        Drain3 = new widget.TextBox();
        CentralLine = new widget.TextBox();
        jSeparator5 = new javax.swing.JSeparator();
        BtnSimpanInvaive = new widget.Button();
        FormInput4 = new widget.PanelBiasa();
        jLabel10 = new widget.Label();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel71 = new widget.Label();
        jLabel74 = new widget.Label();
        jLabel75 = new widget.Label();
        jLabel76 = new widget.Label();
        jLabel86 = new widget.Label();
        jLabel87 = new widget.Label();
        jLabel88 = new widget.Label();
        jLabel89 = new widget.Label();
        SuhuInkubator = new widget.TextBox();
        SuhuBayi = new widget.TextBox();
        Pernapasan = new widget.TextBox();
        Saturasi = new widget.TextBox();
        jLabel77 = new widget.Label();
        jLabel90 = new widget.Label();
        jLabel91 = new widget.Label();
        jLabel92 = new widget.Label();
        jLabel93 = new widget.Label();
        jLabel94 = new widget.Label();
        jLabel95 = new widget.Label();
        jLabel96 = new widget.Label();
        Kesadaran = new widget.TextBox();
        Grunting = new widget.TextBox();
        Nadi = new widget.TextBox();
        jSeparator6 = new javax.swing.JSeparator();
        SimpanHemodinamik = new widget.Button();
        jLabel97 = new widget.Label();
        jLabel98 = new widget.Label();
        jLabel99 = new widget.Label();
        jLabel100 = new widget.Label();
        NEWS = new widget.ComboBox();
        SNPKM = new widget.ComboBox();
        WarnaKulit = new widget.TextBox();
        jLabel101 = new widget.Label();
        jLabel102 = new widget.Label();
        jLabel103 = new widget.Label();
        jLabel104 = new widget.Label();
        jLabel105 = new widget.Label();
        jLabel106 = new widget.Label();
        jLabel107 = new widget.Label();
        jSeparator7 = new javax.swing.JSeparator();
        Saturasi1 = new widget.TextBox();
        jLabel108 = new widget.Label();
        SuhuInkubator1 = new widget.TextBox();
        Grunting1 = new widget.TextBox();
        jLabel109 = new widget.Label();
        SNPKM1 = new widget.ComboBox();
        jLabel110 = new widget.Label();
        NEWS1 = new widget.ComboBox();
        jLabel111 = new widget.Label();
        Pernapasan1 = new widget.TextBox();
        jLabel112 = new widget.Label();
        jLabel113 = new widget.Label();
        jLabel114 = new widget.Label();
        jLabel115 = new widget.Label();
        jLabel116 = new widget.Label();
        Nadi1 = new widget.TextBox();
        Kesadaran1 = new widget.TextBox();
        SuhuBayi1 = new widget.TextBox();
        jLabel117 = new widget.Label();
        jLabel118 = new widget.Label();
        jLabel119 = new widget.Label();
        WarnaKulit1 = new widget.TextBox();
        jLabel120 = new widget.Label();
        PanelInput2 = new javax.swing.JPanel();
        scrollInput2 = new widget.ScrollPane();
        FormInput2 = new widget.PanelBiasa();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        FormInput = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        TNoRM = new widget.TextBox();
        jLabel16 = new widget.Label();
        jLabel8 = new widget.Label();
        TglLahir = new widget.TextBox();
        Tanggal = new widget.Tanggal();
        jLabel23 = new widget.Label();
        KodePetugas = new widget.TextBox();
        NamaPetugas = new widget.TextBox();
        btnPetugas = new widget.Button();
        jLabel5 = new widget.Label();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnKriteriaKeluarHCU.setBackground(new java.awt.Color(255, 255, 254));
        MnKriteriaKeluarHCU.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnKriteriaKeluarHCU.setForeground(new java.awt.Color(50, 50, 50));
        MnKriteriaKeluarHCU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnKriteriaKeluarHCU.setText("Formulir Checklist Kriteria Keluar HCU");
        MnKriteriaKeluarHCU.setName("MnKriteriaKeluarHCU"); // NOI18N
        MnKriteriaKeluarHCU.setPreferredSize(new java.awt.Dimension(260, 26));
        MnKriteriaKeluarHCU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnKriteriaKeluarHCUActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnKriteriaKeluarHCU);

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N

        JK.setHighlighter(null);
        JK.setName("JK"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ MONITORING PASIEN NEONATUS ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        internalFrame2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data MONITORING ]::", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame2.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        PanelInput1.setName("PanelInput1"); // NOI18N
        PanelInput1.setOpaque(false);
        PanelInput1.setPreferredSize(new java.awt.Dimension(500, 100));
        PanelInput1.setLayout(new java.awt.BorderLayout(1, 1));

        scrollInput1.setName("scrollInput1"); // NOI18N
        scrollInput1.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput1.setBackground(new java.awt.Color(250, 255, 245));
        FormInput1.setBorder(null);
        FormInput1.setName("FormInput1"); // NOI18N
        FormInput1.setPreferredSize(new java.awt.Dimension(100, 1000));
        FormInput1.setLayout(null);

        FormInput3.setBackground(new java.awt.Color(250, 255, 245));
        FormInput3.setBorder(null);
        FormInput3.setMinimumSize(new java.awt.Dimension(900, 190));
        FormInput3.setName("FormInput3"); // NOI18N
        FormInput3.setPreferredSize(new java.awt.Dimension(100, 203));
        FormInput3.setLayout(null);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("ALAT INVASIVE YANG TERPASANG (diisi Oleh Perawat)");
        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput3.add(jLabel9);
        jLabel9.setBounds(0, 0, 900, 23);

        jSeparator3.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator3.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator3.setName("jSeparator3"); // NOI18N
        FormInput3.add(jSeparator3);
        jSeparator3.setBounds(0, 20, 900, 3);

        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel65.setText("Vena Pariver 1");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput3.add(jLabel65);
        jLabel65.setBounds(10, 30, 100, 23);

        jLabel66.setText(":");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput3.add(jLabel66);
        jLabel66.setBounds(110, 60, 10, 23);

        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel68.setText("Vena Pariver 3");
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput3.add(jLabel68);
        jLabel68.setBounds(10, 90, 100, 23);

        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel72.setText("Alteri Line");
        jLabel72.setName("jLabel72"); // NOI18N
        FormInput3.add(jLabel72);
        jLabel72.setBounds(10, 120, 100, 23);

        jLabel78.setText(":");
        jLabel78.setName("jLabel78"); // NOI18N
        FormInput3.add(jLabel78);
        jLabel78.setBounds(110, 30, 10, 23);

        jLabel79.setText(":");
        jLabel79.setName("jLabel79"); // NOI18N
        FormInput3.add(jLabel79);
        jLabel79.setBounds(110, 90, 10, 23);

        jLabel80.setText(":");
        jLabel80.setName("jLabel80"); // NOI18N
        FormInput3.add(jLabel80);
        jLabel80.setBounds(110, 120, 10, 23);

        jLabel82.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel82.setText("Vena Pariver 2");
        jLabel82.setName("jLabel82"); // NOI18N
        FormInput3.add(jLabel82);
        jLabel82.setBounds(10, 60, 100, 23);

        VenaPariver1.setHighlighter(null);
        VenaPariver1.setName("VenaPariver1"); // NOI18N
        FormInput3.add(VenaPariver1);
        VenaPariver1.setBounds(120, 30, 300, 23);

        VenaPariver2.setHighlighter(null);
        VenaPariver2.setName("VenaPariver2"); // NOI18N
        FormInput3.add(VenaPariver2);
        VenaPariver2.setBounds(120, 60, 300, 23);

        VenaPariver3.setHighlighter(null);
        VenaPariver3.setName("VenaPariver3"); // NOI18N
        FormInput3.add(VenaPariver3);
        VenaPariver3.setBounds(120, 90, 300, 23);

        AlteriLine.setHighlighter(null);
        AlteriLine.setName("AlteriLine"); // NOI18N
        FormInput3.add(AlteriLine);
        AlteriLine.setBounds(120, 120, 300, 23);

        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel67.setText("Drain 1");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput3.add(jLabel67);
        jLabel67.setBounds(470, 30, 70, 23);

        jLabel69.setText(":");
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput3.add(jLabel69);
        jLabel69.setBounds(540, 60, 10, 23);

        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel70.setText("Drain 3");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput3.add(jLabel70);
        jLabel70.setBounds(470, 90, 70, 23);

        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel73.setText("Central Line");
        jLabel73.setName("jLabel73"); // NOI18N
        FormInput3.add(jLabel73);
        jLabel73.setBounds(470, 120, 70, 23);

        jLabel81.setText(":");
        jLabel81.setName("jLabel81"); // NOI18N
        FormInput3.add(jLabel81);
        jLabel81.setBounds(540, 30, 10, 23);

        jLabel83.setText(":");
        jLabel83.setName("jLabel83"); // NOI18N
        FormInput3.add(jLabel83);
        jLabel83.setBounds(540, 90, 10, 23);

        jLabel84.setText(":");
        jLabel84.setName("jLabel84"); // NOI18N
        FormInput3.add(jLabel84);
        jLabel84.setBounds(540, 120, 10, 23);

        jLabel85.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel85.setText("Drain 2");
        jLabel85.setName("jLabel85"); // NOI18N
        FormInput3.add(jLabel85);
        jLabel85.setBounds(470, 60, 70, 23);

        Drain1.setHighlighter(null);
        Drain1.setName("Drain1"); // NOI18N
        FormInput3.add(Drain1);
        Drain1.setBounds(550, 30, 300, 23);

        Drain2.setHighlighter(null);
        Drain2.setName("Drain2"); // NOI18N
        FormInput3.add(Drain2);
        Drain2.setBounds(550, 60, 300, 23);

        Drain3.setHighlighter(null);
        Drain3.setName("Drain3"); // NOI18N
        FormInput3.add(Drain3);
        Drain3.setBounds(550, 90, 300, 23);

        CentralLine.setToolTipText("Tuliskan tanggal dan lokasinya");
        CentralLine.setHighlighter(null);
        CentralLine.setName("CentralLine"); // NOI18N
        FormInput3.add(CentralLine);
        CentralLine.setBounds(550, 120, 300, 23);

        jSeparator5.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator5.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator5.setName("jSeparator5"); // NOI18N
        FormInput3.add(jSeparator5);
        jSeparator5.setBounds(0, 180, 900, 3);

        BtnSimpanInvaive.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpanInvaive.setMnemonic('S');
        BtnSimpanInvaive.setText("Simpan");
        BtnSimpanInvaive.setToolTipText("Alt+S");
        BtnSimpanInvaive.setGlassColor(new java.awt.Color(0, 204, 0));
        BtnSimpanInvaive.setName("BtnSimpanInvaive"); // NOI18N
        BtnSimpanInvaive.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpanInvaive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanInvaiveActionPerformed(evt);
            }
        });
        BtnSimpanInvaive.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanInvaiveKeyPressed(evt);
            }
        });
        FormInput3.add(BtnSimpanInvaive);
        BtnSimpanInvaive.setBounds(750, 150, 100, 30);

        FormInput1.add(FormInput3);
        FormInput3.setBounds(0, 0, 900, 190);

        FormInput4.setBackground(new java.awt.Color(250, 255, 245));
        FormInput4.setBorder(null);
        FormInput4.setMinimumSize(new java.awt.Dimension(900, 190));
        FormInput4.setName("FormInput4"); // NOI18N
        FormInput4.setPreferredSize(new java.awt.Dimension(100, 203));
        FormInput4.setLayout(null);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("STATUS HEMODINAMIK");
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput4.add(jLabel10);
        jLabel10.setBounds(0, 0, 900, 23);

        jSeparator4.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator4.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator4.setName("jSeparator4"); // NOI18N
        FormInput4.add(jSeparator4);
        jSeparator4.setBounds(0, 20, 900, 3);

        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel71.setText("Suhu Inkubator");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput4.add(jLabel71);
        jLabel71.setBounds(10, 30, 80, 23);

        jLabel74.setText(":");
        jLabel74.setName("jLabel74"); // NOI18N
        FormInput4.add(jLabel74);
        jLabel74.setBounds(290, 30, 10, 23);

        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel75.setText("Pernapasan");
        jLabel75.setName("jLabel75"); // NOI18N
        FormInput4.add(jLabel75);
        jLabel75.setBounds(10, 60, 80, 23);

        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel76.setText("Saturasi O2");
        jLabel76.setName("jLabel76"); // NOI18N
        FormInput4.add(jLabel76);
        jLabel76.setBounds(230, 60, 60, 23);

        jLabel86.setText(":");
        jLabel86.setName("jLabel86"); // NOI18N
        FormInput4.add(jLabel86);
        jLabel86.setBounds(90, 30, 10, 23);

        jLabel87.setText(":");
        jLabel87.setName("jLabel87"); // NOI18N
        FormInput4.add(jLabel87);
        jLabel87.setBounds(90, 60, 10, 23);

        jLabel88.setText(":");
        jLabel88.setName("jLabel88"); // NOI18N
        FormInput4.add(jLabel88);
        jLabel88.setBounds(290, 60, 10, 23);

        jLabel89.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel89.setText("Suhu Bayi");
        jLabel89.setName("jLabel89"); // NOI18N
        FormInput4.add(jLabel89);
        jLabel89.setBounds(240, 30, 50, 23);

        SuhuInkubator.setHighlighter(null);
        SuhuInkubator.setName("SuhuInkubator"); // NOI18N
        FormInput4.add(SuhuInkubator);
        SuhuInkubator.setBounds(100, 30, 90, 23);

        SuhuBayi.setHighlighter(null);
        SuhuBayi.setName("SuhuBayi"); // NOI18N
        FormInput4.add(SuhuBayi);
        SuhuBayi.setBounds(300, 30, 90, 23);

        Pernapasan.setHighlighter(null);
        Pernapasan.setName("Pernapasan"); // NOI18N
        FormInput4.add(Pernapasan);
        Pernapasan.setBounds(100, 60, 90, 23);

        Saturasi.setHighlighter(null);
        Saturasi.setName("Saturasi"); // NOI18N
        FormInput4.add(Saturasi);
        Saturasi.setBounds(300, 60, 90, 23);

        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel77.setText("Status Kesadaran");
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput4.add(jLabel77);
        jLabel77.setBounds(450, 30, 90, 23);

        jLabel90.setText(":");
        jLabel90.setName("jLabel90"); // NOI18N
        FormInput4.add(jLabel90);
        jLabel90.setBounds(540, 60, 10, 23);

        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel91.setText("Nadi");
        jLabel91.setName("jLabel91"); // NOI18N
        FormInput4.add(jLabel91);
        jLabel91.setBounds(690, 30, 60, 23);

        jLabel92.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel92.setText("Nilae NEWS");
        jLabel92.setName("jLabel92"); // NOI18N
        FormInput4.add(jLabel92);
        jLabel92.setBounds(690, 60, 60, 23);

        jLabel93.setText(":");
        jLabel93.setName("jLabel93"); // NOI18N
        FormInput4.add(jLabel93);
        jLabel93.setBounds(540, 30, 10, 23);

        jLabel94.setText(":");
        jLabel94.setName("jLabel94"); // NOI18N
        FormInput4.add(jLabel94);
        jLabel94.setBounds(750, 30, 10, 23);

        jLabel95.setText(":");
        jLabel95.setName("jLabel95"); // NOI18N
        FormInput4.add(jLabel95);
        jLabel95.setBounds(750, 60, 10, 23);

        jLabel96.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel96.setText("Grunting");
        jLabel96.setName("jLabel96"); // NOI18N
        FormInput4.add(jLabel96);
        jLabel96.setBounds(450, 60, 90, 23);

        Kesadaran.setHighlighter(null);
        Kesadaran.setName("Kesadaran"); // NOI18N
        FormInput4.add(Kesadaran);
        Kesadaran.setBounds(550, 30, 90, 23);

        Grunting.setHighlighter(null);
        Grunting.setName("Grunting"); // NOI18N
        FormInput4.add(Grunting);
        Grunting.setBounds(550, 60, 90, 23);

        Nadi.setHighlighter(null);
        Nadi.setName("Nadi"); // NOI18N
        FormInput4.add(Nadi);
        Nadi.setBounds(760, 30, 90, 23);

        jSeparator6.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator6.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator6.setName("jSeparator6"); // NOI18N
        FormInput4.add(jSeparator6);
        jSeparator6.setBounds(0, 120, 900, 3);

        SimpanHemodinamik.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        SimpanHemodinamik.setMnemonic('S');
        SimpanHemodinamik.setText("Simpan");
        SimpanHemodinamik.setToolTipText("Alt+S");
        SimpanHemodinamik.setGlassColor(new java.awt.Color(0, 204, 0));
        SimpanHemodinamik.setName("SimpanHemodinamik"); // NOI18N
        SimpanHemodinamik.setPreferredSize(new java.awt.Dimension(100, 30));
        SimpanHemodinamik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SimpanHemodinamikActionPerformed(evt);
            }
        });
        SimpanHemodinamik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SimpanHemodinamikKeyPressed(evt);
            }
        });
        FormInput4.add(SimpanHemodinamik);
        SimpanHemodinamik.setBounds(760, 240, 100, 30);

        jLabel97.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel97.setText("Warna Kulit");
        jLabel97.setName("jLabel97"); // NOI18N
        FormInput4.add(jLabel97);
        jLabel97.setBounds(10, 90, 80, 23);

        jLabel98.setText(":");
        jLabel98.setName("jLabel98"); // NOI18N
        FormInput4.add(jLabel98);
        jLabel98.setBounds(90, 90, 10, 23);

        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel99.setText("S / N / P / K / M");
        jLabel99.setName("jLabel99"); // NOI18N
        FormInput4.add(jLabel99);
        jLabel99.setBounds(230, 90, 80, 23);

        jLabel100.setText(":");
        jLabel100.setName("jLabel100"); // NOI18N
        FormInput4.add(jLabel100);
        jLabel100.setBounds(310, 90, 10, 23);

        NEWS.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hijau", "Kuning", "Merah" }));
        NEWS.setName("NEWS"); // NOI18N
        NEWS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NEWSKeyPressed(evt);
            }
        });
        FormInput4.add(NEWS);
        NEWS.setBounds(760, 60, 90, 23);

        SNPKM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sianosis", "Normal", "Pucat", "Kuning", "Motiled", " " }));
        SNPKM.setName("SNPKM"); // NOI18N
        SNPKM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SNPKMKeyPressed(evt);
            }
        });
        FormInput4.add(SNPKM);
        SNPKM.setBounds(320, 90, 110, 23);

        WarnaKulit.setHighlighter(null);
        WarnaKulit.setName("WarnaKulit"); // NOI18N
        FormInput4.add(WarnaKulit);
        WarnaKulit.setBounds(100, 90, 90, 23);

        jLabel101.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel101.setText("Tekanan Darah");
        jLabel101.setName("jLabel101"); // NOI18N
        FormInput4.add(jLabel101);
        jLabel101.setBounds(10, 190, 80, 20);

        jLabel102.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel102.setText("MAP");
        jLabel102.setName("jLabel102"); // NOI18N
        FormInput4.add(jLabel102);
        jLabel102.setBounds(240, 130, 50, 20);

        jLabel103.setText(":");
        jLabel103.setName("jLabel103"); // NOI18N
        FormInput4.add(jLabel103);
        jLabel103.setBounds(90, 160, 10, 20);

        jLabel104.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel104.setText("Down Score");
        jLabel104.setName("jLabel104"); // NOI18N
        FormInput4.add(jLabel104);
        jLabel104.setBounds(10, 160, 80, 20);

        jLabel105.setText(":");
        jLabel105.setName("jLabel105"); // NOI18N
        FormInput4.add(jLabel105);
        jLabel105.setBounds(290, 130, 10, 20);

        jLabel106.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel106.setText("Grunting");
        jLabel106.setName("jLabel106"); // NOI18N
        FormInput4.add(jLabel106);
        jLabel106.setBounds(450, 160, 90, 20);

        jLabel107.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel107.setText("Nadi");
        jLabel107.setName("jLabel107"); // NOI18N
        FormInput4.add(jLabel107);
        jLabel107.setBounds(690, 130, 60, 20);

        jSeparator7.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator7.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator7.setName("jSeparator7"); // NOI18N
        FormInput4.add(jSeparator7);
        jSeparator7.setBounds(0, 220, 900, 10);

        Saturasi1.setHighlighter(null);
        Saturasi1.setName("Saturasi1"); // NOI18N
        FormInput4.add(Saturasi1);
        Saturasi1.setBounds(300, 160, 90, 24);

        jLabel108.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel108.setText("Status Kesadaran");
        jLabel108.setName("jLabel108"); // NOI18N
        FormInput4.add(jLabel108);
        jLabel108.setBounds(450, 130, 90, 20);

        SuhuInkubator1.setHighlighter(null);
        SuhuInkubator1.setName("SuhuInkubator1"); // NOI18N
        FormInput4.add(SuhuInkubator1);
        SuhuInkubator1.setBounds(100, 130, 90, 24);

        Grunting1.setHighlighter(null);
        Grunting1.setName("Grunting1"); // NOI18N
        FormInput4.add(Grunting1);
        Grunting1.setBounds(550, 160, 90, 24);

        jLabel109.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel109.setText("Nilae NEWS");
        jLabel109.setName("jLabel109"); // NOI18N
        FormInput4.add(jLabel109);
        jLabel109.setBounds(690, 160, 60, 20);

        SNPKM1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sianosis", "Normal", "Pucat", "Kuning", "Motiled", " " }));
        SNPKM1.setName("SNPKM1"); // NOI18N
        SNPKM1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SNPKM1KeyPressed(evt);
            }
        });
        FormInput4.add(SNPKM1);
        SNPKM1.setBounds(320, 190, 110, 20);

        jLabel110.setText(":");
        jLabel110.setName("jLabel110"); // NOI18N
        FormInput4.add(jLabel110);
        jLabel110.setBounds(90, 130, 10, 20);

        NEWS1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hijau", "Kuning", "Merah" }));
        NEWS1.setName("NEWS1"); // NOI18N
        NEWS1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NEWS1KeyPressed(evt);
            }
        });
        FormInput4.add(NEWS1);
        NEWS1.setBounds(760, 160, 90, 20);

        jLabel111.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel111.setText("S / N / P / K / M");
        jLabel111.setName("jLabel111"); // NOI18N
        FormInput4.add(jLabel111);
        jLabel111.setBounds(230, 190, 80, 20);

        Pernapasan1.setHighlighter(null);
        Pernapasan1.setName("Pernapasan1"); // NOI18N
        FormInput4.add(Pernapasan1);
        Pernapasan1.setBounds(100, 160, 90, 24);

        jLabel112.setText(":");
        jLabel112.setName("jLabel112"); // NOI18N
        FormInput4.add(jLabel112);
        jLabel112.setBounds(750, 160, 10, 20);

        jLabel113.setText(":");
        jLabel113.setName("jLabel113"); // NOI18N
        FormInput4.add(jLabel113);
        jLabel113.setBounds(90, 190, 10, 20);

        jLabel114.setText(":");
        jLabel114.setName("jLabel114"); // NOI18N
        FormInput4.add(jLabel114);
        jLabel114.setBounds(290, 160, 10, 20);

        jLabel115.setText(":");
        jLabel115.setName("jLabel115"); // NOI18N
        FormInput4.add(jLabel115);
        jLabel115.setBounds(540, 160, 10, 20);

        jLabel116.setText(":");
        jLabel116.setName("jLabel116"); // NOI18N
        FormInput4.add(jLabel116);
        jLabel116.setBounds(750, 130, 10, 20);

        Nadi1.setHighlighter(null);
        Nadi1.setName("Nadi1"); // NOI18N
        FormInput4.add(Nadi1);
        Nadi1.setBounds(760, 130, 90, 24);

        Kesadaran1.setHighlighter(null);
        Kesadaran1.setName("Kesadaran1"); // NOI18N
        FormInput4.add(Kesadaran1);
        Kesadaran1.setBounds(550, 130, 90, 24);

        SuhuBayi1.setHighlighter(null);
        SuhuBayi1.setName("SuhuBayi1"); // NOI18N
        FormInput4.add(SuhuBayi1);
        SuhuBayi1.setBounds(300, 130, 90, 24);

        jLabel117.setText(":");
        jLabel117.setName("jLabel117"); // NOI18N
        FormInput4.add(jLabel117);
        jLabel117.setBounds(310, 190, 10, 20);

        jLabel118.setText(":");
        jLabel118.setName("jLabel118"); // NOI18N
        FormInput4.add(jLabel118);
        jLabel118.setBounds(540, 130, 10, 20);

        jLabel119.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel119.setText("Skala Nyeri");
        jLabel119.setName("jLabel119"); // NOI18N
        FormInput4.add(jLabel119);
        jLabel119.setBounds(10, 130, 80, 20);

        WarnaKulit1.setHighlighter(null);
        WarnaKulit1.setName("WarnaKulit1"); // NOI18N
        FormInput4.add(WarnaKulit1);
        WarnaKulit1.setBounds(100, 190, 90, 24);

        jLabel120.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel120.setText("CRT");
        jLabel120.setName("jLabel120"); // NOI18N
        FormInput4.add(jLabel120);
        jLabel120.setBounds(230, 160, 60, 20);

        FormInput1.add(FormInput4);
        FormInput4.setBounds(0, 190, 900, 280);

        scrollInput1.setViewportView(FormInput1);

        PanelInput1.add(scrollInput1, java.awt.BorderLayout.CENTER);

        internalFrame2.add(PanelInput1, java.awt.BorderLayout.LINE_START);

        PanelInput2.setName("PanelInput2"); // NOI18N
        PanelInput2.setOpaque(false);
        PanelInput2.setPreferredSize(new java.awt.Dimension(900, 100));
        PanelInput2.setLayout(new java.awt.BorderLayout(1, 1));

        scrollInput2.setName("scrollInput2"); // NOI18N
        scrollInput2.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput2.setBackground(new java.awt.Color(250, 255, 245));
        FormInput2.setBorder(null);
        FormInput2.setName("FormInput2"); // NOI18N
        FormInput2.setPreferredSize(new java.awt.Dimension(100, 203));
        FormInput2.setLayout(null);
        scrollInput2.setViewportView(FormInput2);

        PanelInput2.add(scrollInput2, java.awt.BorderLayout.CENTER);

        internalFrame2.add(PanelInput2, java.awt.BorderLayout.LINE_END);

        Scroll.setViewportView(internalFrame2);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
        BtnBatal.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnBatal);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnPrint);

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(LCount);

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

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tanggal :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "03-07-2025" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "03-07-2025" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(310, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('3');
        BtnCari.setToolTipText("Alt+3");
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
        panelGlass9.add(BtnCari);

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
        panelGlass9.add(BtnAll);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(100, 70));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 203));
        FormInput.setLayout(null);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("No.Rawat");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(21, 10, 75, 23);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(79, 10, 141, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPasienKeyPressed(evt);
            }
        });
        FormInput.add(TPasien);
        TPasien.setBounds(336, 10, 285, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        FormInput.add(TNoRM);
        TNoRM.setBounds(222, 10, 112, 23);

        jLabel16.setText("Tanggal :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setVerifyInputWhenFocusTarget(false);
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 40, 75, 23);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(625, 10, 60, 23);

        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(689, 10, 100, 23);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "03-07-2025 19:28:19" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        FormInput.add(Tanggal);
        Tanggal.setBounds(79, 40, 130, 23);

        jLabel23.setText("DPJP / Dokter Jaga / NICU :");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(221, 40, 160, 23);

        KodePetugas.setEditable(false);
        KodePetugas.setHighlighter(null);
        KodePetugas.setName("KodePetugas"); // NOI18N
        FormInput.add(KodePetugas);
        KodePetugas.setBounds(385, 40, 127, 23);

        NamaPetugas.setEditable(false);
        NamaPetugas.setName("NamaPetugas"); // NOI18N
        FormInput.add(NamaPetugas);
        NamaPetugas.setBounds(514, 40, 245, 23);

        btnPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPetugas.setMnemonic('2');
        btnPetugas.setToolTipText("ALt+2");
        btnPetugas.setName("btnPetugas"); // NOI18N
        btnPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPetugasActionPerformed(evt);
            }
        });
        btnPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPetugasKeyPressed(evt);
            }
        });
        FormInput.add(btnPetugas);
        btnPetugas.setBounds(761, 40, 28, 23);

        jLabel5.setText(":");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(0, 10, 75, 23);

        jSeparator1.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator1.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator1.setName("jSeparator1"); // NOI18N
        FormInput.add(jSeparator1);
        jSeparator1.setBounds(0, 70, 810, 1);

        jSeparator2.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator2.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator2.setName("jSeparator2"); // NOI18N
        FormInput.add(jSeparator2);
        jSeparator2.setBounds(0, 70, 810, 1);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
        }else{            
            Valid.pindah(evt,TCari,Tanggal);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt,TCari,BtnSimpan);
}//GEN-LAST:event_TPasienKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        }else if(KodePetugas.getText().trim().equals("")||NamaPetugas.getText().trim().equals("")){
            Valid.textKosong(btnPetugas,"DPJP/Dokter Jaga/HCU");
        }else{
            if(Sequel.menyimpantf("checklist_kriteria_keluar_nicu","?,?,?,?,?,?,?","Data",7,new String[]{
//                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),bb.getSelectedItem().toString(),
//                ttv.getSelectedItem().toString(),rr.getSelectedItem().toString(),obat.getSelectedItem().toString(),KodePetugas.getText()
            })==true){
                tampil();
//                LCount.setText(""+tabMode.getRowCount());
                emptTeks();
            } 
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
//            Valid.pindah(evt,ttv,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
//        if(tbObat.getSelectedRow()>-1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else {
//                if(akses.getkode().equals(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString())){
                    hapus();
//                }else{
//                    JOptionPane.showMessageDialog(null,"Harus salah satu petugas sesuai user login..!!");
//                }
            }
//        }else{
//            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
//        }   
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
//        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
//            Valid.textKosong(TNoRw,"pasien");
//        }else if(KodePetugas.getText().trim().equals("")||NamaPetugas.getText().trim().equals("")){
//            Valid.textKosong(btnPetugas,"DPJP/Dokter Jaga/HCU");
//        }else{  
//            if(tbObat.getSelectedRow()>-1){
//                if(akses.getkode().equals("Admin Utama")){
//                    ganti();
//                }else {
//                    if(akses.getkode().equals(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString())){
//                        ganti();
//                    }else{
//                        JOptionPane.showMessageDialog(null,"Harus salah satu petugas sesuai user login..!!");
//                    }
//                }
//            }else{
//                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
//            }
//        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        pegawai.dispose();
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//        if(tabMode.getRowCount()==0){
//            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
//            BtnBatal.requestFocus();
//        }else if(tabMode.getRowCount()!=0){
//            try{
//                htmlContent = new StringBuilder();
//                htmlContent.append(                             
//                    "<tr class='isi'>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>No.Rawat</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>No.RM</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Nama Pasien</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Tgl.Lahir</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>J.K.</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Tanggal</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Pasien Yang Sudah Tidak Memerlukan Perawatan Di Ruang HCU</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Hypertensi Emergency Teratasi / Normal</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>HR 60 - 100 x/menit (Hasil EKG Menunjukkan Perbaikan / Sinus Rithme)</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Kesadaran Dengan GCS >= 7</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Gangguan Inotropic / Vasoaktif Gent Teratasi / Normal</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>MAP >= 60 mmHg</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>RR 12 - 20x/menit</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Gangguan Elektrolit (Na, Ca, CI, Mg, Cal) & Asam Basa Normal</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Perdarahan Saluran Pencernaan Teratasi</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Temperature 35 - 38°C</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Gagal Jantung Akut Teratasi</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Gangguan Sirkulasi Teratasi</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>NIP/Kode Dokter</b></td>"+
//                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>DPJP/Dokter Jaga/HCU</b></td>"+
//                    "</tr>"
//                );
//                
//                for (i = 0; i < tabMode.getRowCount(); i++) {
//                    htmlContent.append(
//                        "<tr class='isi'>"+
//                           "<td valign='top'>"+tbObat.getValueAt(i,0).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,1).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,2).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,3).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,4).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,5).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,6).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,7).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,8).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,9).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,10).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,11).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,12).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,13).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,14).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,15).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,16).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,17).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,18).toString()+"</td>"+
//                            "<td valign='top'>"+tbObat.getValueAt(i,19).toString()+"</td>"+
//                        "</tr>");
//                }
//                
//                LoadHTML.setText(
//                    "<html>"+
//                      "<table width='2500px' border='0' align='center' cellpadding='1px' cellspacing='0' class='tbl_form'>"+
//                       htmlContent.toString()+
//                      "</table>"+
//                    "</html>"
//                );
//
//                File g = new File("file2.css");            
//                BufferedWriter bg = new BufferedWriter(new FileWriter(g));
//                bg.write(
//                    ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
//                    ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"+
//                    ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
//                    ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
//                    ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"+
//                    ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"+
//                    ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"+
//                    ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"+
//                    ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
//                );
//                bg.close();
//
//                File f = new File("DataChecklistKriteriaKeluarHCU.html");            
//                BufferedWriter bw = new BufferedWriter(new FileWriter(f));            
//                bw.write(LoadHTML.getText().replaceAll("<head>","<head>"+
//                            "<link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" />"+
//                            "<table width='2500px' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
//                                "<tr class='isi2'>"+
//                                    "<td valign='top' align='center'>"+
//                                        "<font size='4' face='Tahoma'>"+akses.getnamars()+"</font><br>"+
//                                        akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br>"+
//                                        akses.getkontakrs()+", E-mail : "+akses.getemailrs()+"<br><br>"+
//                                        "<font size='2' face='Tahoma'>DATA CHECK LIST KRITERIA KELUAR HCU<br><br></font>"+        
//                                    "</td>"+
//                               "</tr>"+
//                            "</table>")
//                );
//                bw.close();                         
//                Desktop.getDesktop().browse(f.toURI());
//
//            }catch(Exception e){
//                System.out.println("Notifikasi : "+e);
//            }
//        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEdit, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
            TCari.setText("");
        }else{
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
}//GEN-LAST:event_TNoRMKeyPressed

    private void MnKriteriaKeluarHCUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnKriteriaKeluarHCUActionPerformed
//        if(tbObat.getSelectedRow()>-1){
//            Map<String, Object> param = new HashMap<>();
//            param.put("namars",akses.getnamars());
//            param.put("alamatrs",akses.getalamatrs());
//            param.put("kotars",akses.getkabupatenrs());
//            param.put("propinsirs",akses.getpropinsirs());
//            param.put("kontakrs",akses.getkontakrs());
//            param.put("emailrs",akses.getemailrs());   
//            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
//            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());
//            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbObat.getValueAt(tbObat.getSelectedRow(),19).toString()+"\nID "+(finger.equals("")?tbObat.getValueAt(tbObat.getSelectedRow(),18).toString():finger)+"\n"+Tanggal.getSelectedItem()); 
//            Valid.MyReportqry("rptFormulirChecklistKriteriaKeluarHCU.jasper","report","::[ Formulir Check List Kriteria Keluar HCU ]::",
//                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,checklist_kriteria_keluar_nicu.tanggal,"+
//                    "checklist_kriteria_keluar_nicu.kriteria1,checklist_kriteria_keluar_nicu.kriteria2,checklist_kriteria_keluar_nicu.kriteria3,"+
//                    "checklist_kriteria_keluar_nicu.kriteria4,checklist_kriteria_keluar_nicu.kriteria5,checklist_kriteria_keluar_nicu.kriteria6,"+
//                    "checklist_kriteria_keluar_nicu.kriteria7,checklist_kriteria_keluar_nicu.kriteria8,checklist_kriteria_keluar_nicu.kriteria9,"+
//                    "checklist_kriteria_keluar_nicu.kriteria10,checklist_kriteria_keluar_nicu.kriteria11,checklist_kriteria_keluar_nicu.kriteria12,"+
//                    "checklist_kriteria_keluar_nicu.nik,pegawai.nama "+
//                    "from checklist_kriteria_keluar_nicu inner join reg_periksa on checklist_kriteria_keluar_nicu.no_rawat=reg_periksa.no_rawat "+
//                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
//                    "inner join pegawai on pegawai.nik=checklist_kriteria_keluar_nicu.nik "+
//                    "where checklist_kriteria_keluar_nicu.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"' and checklist_kriteria_keluar_nicu.tanggal='"+tbObat.getValueAt(tbObat.getSelectedRow(),5).toString()+"' ",param);
//        }
    }//GEN-LAST:event_MnKriteriaKeluarHCUActionPerformed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
       Valid.pindah(evt,TCari,btnPetugas);
    }//GEN-LAST:event_TanggalKeyPressed

    private void btnPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPetugasActionPerformed
        pegawai.emptTeks();
        pegawai.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        pegawai.setLocationRelativeTo(internalFrame1);
        pegawai.setVisible(true);
    }//GEN-LAST:event_btnPetugasActionPerformed

    private void btnPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPetugasKeyPressed
//       Valid.pindah(evt,Tanggal,Kriteria1);
    }//GEN-LAST:event_btnPetugasKeyPressed

    private void BtnSimpanInvaiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanInvaiveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSimpanInvaiveActionPerformed

    private void BtnSimpanInvaiveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanInvaiveKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSimpanInvaiveKeyPressed

    private void SimpanHemodinamikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SimpanHemodinamikActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SimpanHemodinamikActionPerformed

    private void SimpanHemodinamikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SimpanHemodinamikKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SimpanHemodinamikKeyPressed

    private void NEWSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NEWSKeyPressed
        //        Valid.pindah(evt,Kriteria8,Kriteria10);
    }//GEN-LAST:event_NEWSKeyPressed

    private void SNPKMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SNPKMKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SNPKMKeyPressed

    private void SNPKM1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SNPKM1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SNPKM1KeyPressed

    private void NEWS1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NEWS1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NEWS1KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMMonitoringPasienNeonatus2 dialog = new RMMonitoringPasienNeonatus2(new javax.swing.JFrame(), true);
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
    private widget.TextBox AlteriLine;
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.Button BtnSimpanInvaive;
    private widget.TextBox CentralLine;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextBox Drain1;
    private widget.TextBox Drain2;
    private widget.TextBox Drain3;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormInput1;
    private widget.PanelBiasa FormInput2;
    private widget.PanelBiasa FormInput3;
    private widget.PanelBiasa FormInput4;
    private widget.TextBox Grunting;
    private widget.TextBox Grunting1;
    private widget.TextBox JK;
    private widget.TextBox Kesadaran;
    private widget.TextBox Kesadaran1;
    private widget.TextBox KodePetugas;
    private widget.Label LCount;
    private widget.editorpane LoadHTML;
    private javax.swing.JMenuItem MnKriteriaKeluarHCU;
    private widget.ComboBox NEWS;
    private widget.ComboBox NEWS1;
    private widget.TextBox Nadi;
    private widget.TextBox Nadi1;
    private widget.TextBox NamaPetugas;
    private javax.swing.JPanel PanelInput;
    private javax.swing.JPanel PanelInput1;
    private javax.swing.JPanel PanelInput2;
    private widget.TextBox Pernapasan;
    private widget.TextBox Pernapasan1;
    private widget.ComboBox SNPKM;
    private widget.ComboBox SNPKM1;
    private widget.TextBox Saturasi;
    private widget.TextBox Saturasi1;
    private widget.ScrollPane Scroll;
    private widget.Button SimpanHemodinamik;
    private widget.TextBox SuhuBayi;
    private widget.TextBox SuhuBayi1;
    private widget.TextBox SuhuInkubator;
    private widget.TextBox SuhuInkubator1;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Tanggal Tanggal;
    private widget.TextBox TglLahir;
    private widget.TextBox VenaPariver1;
    private widget.TextBox VenaPariver2;
    private widget.TextBox VenaPariver3;
    private widget.TextBox WarnaKulit;
    private widget.TextBox WarnaKulit1;
    private widget.Button btnPetugas;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.Label jLabel10;
    private widget.Label jLabel100;
    private widget.Label jLabel101;
    private widget.Label jLabel102;
    private widget.Label jLabel103;
    private widget.Label jLabel104;
    private widget.Label jLabel105;
    private widget.Label jLabel106;
    private widget.Label jLabel107;
    private widget.Label jLabel108;
    private widget.Label jLabel109;
    private widget.Label jLabel110;
    private widget.Label jLabel111;
    private widget.Label jLabel112;
    private widget.Label jLabel113;
    private widget.Label jLabel114;
    private widget.Label jLabel115;
    private widget.Label jLabel116;
    private widget.Label jLabel117;
    private widget.Label jLabel118;
    private widget.Label jLabel119;
    private widget.Label jLabel120;
    private widget.Label jLabel16;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel23;
    private widget.Label jLabel4;
    private widget.Label jLabel5;
    private widget.Label jLabel6;
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
    private widget.Label jLabel78;
    private widget.Label jLabel79;
    private widget.Label jLabel8;
    private widget.Label jLabel80;
    private widget.Label jLabel81;
    private widget.Label jLabel82;
    private widget.Label jLabel83;
    private widget.Label jLabel84;
    private widget.Label jLabel85;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput1;
    private widget.ScrollPane scrollInput2;
    // End of variables declaration//GEN-END:variables
    
    public void tampil() {
//        Valid.tabelKosong(tabMode);
        try{
            if(TCari.getText().trim().equals("")){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,checklist_kriteria_keluar_nicu.tanggal,"+
                    "checklist_kriteria_keluar_nicu.berat_badan_naik_stabil,checklist_kriteria_keluar_nicu.menunjukan_tanda_vital_stabil,checklist_kriteria_keluar_nicu.tidak_memerlukan_tunjangan_respirasi,"+
                    "checklist_kriteria_keluar_nicu.tidak_memerlukan_obat_kardiotonik,"+
                    "checklist_kriteria_keluar_nicu.nik,pegawai.nama "+
                    "from checklist_kriteria_keluar_nicu inner join reg_periksa on checklist_kriteria_keluar_nicu.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join pegawai on pegawai.nik=checklist_kriteria_keluar_nicu.nik "+
                    "where checklist_kriteria_keluar_nicu.tanggal between ? and ? order by checklist_kriteria_keluar_nicu.tanggal ");
            }else{
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,checklist_kriteria_keluar_nicu.tanggal,"+
                    "checklist_kriteria_keluar_nicu.berat_badan_naik_stabil,checklist_kriteria_keluar_nicu.menunjukan_tanda_vital_stabil,checklist_kriteria_keluar_nicu.tidak_memerlukan_tunjangan_respirasi,"+
                    "checklist_kriteria_keluar_nicu.tidak_memerlukan_obat_kardiotonik,"+
                    "checklist_kriteria_keluar_nicu.nik,pegawai.nama "+
                    "from checklist_kriteria_keluar_nicu inner join reg_periksa on checklist_kriteria_keluar_nicu.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join pegawai on pegawai.nik=checklist_kriteria_keluar_nicu.nik "+
                    "where checklist_kriteria_keluar_nicu.tanggal between ? and ? and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or "+
                    "pasien.nm_pasien like ? or pegawai.nama like ? or checklist_kriteria_keluar_nicu.nik like ?) order by checklist_kriteria_keluar_nicu.tanggal ");
            }
                
            try {
                if(TCari.getText().trim().equals("")){
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                }else{
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                }
                    
                rs=ps.executeQuery();
//                while(rs.next()){
//                    tabMode.addRow(new String[]{
//                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("tgl_lahir"),rs.getString("jk"),
//                        rs.getString("tanggal"),rs.getString("berat_badan_naik_stabil"),rs.getString("menunjukan_tanda_vital_stabil"),rs.getString("tidak_memerlukan_tunjangan_respirasi"),rs.getString("tidak_memerlukan_obat_kardiotonik"),
//                        rs.getString("nik"),rs.getString("nama")
//                    });
//                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
//        LCount.setText(""+tabMode.getRowCount());
    }
    
    public void emptTeks() {
        Tanggal.setDate(new Date());
    } 

    private void getData() {
//        if(tbObat.getSelectedRow()!= -1){
//            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
//            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
//            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
//            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
//            JK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
//            Valid.SetTgl2(Tanggal,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
//        }
    }
    
    private void isRawat() {
        try {
            ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,reg_periksa.tgl_registrasi "+
                    "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "where reg_periksa.no_rawat=?");
            try {
                ps.setString(1,TNoRw.getText());
                rs=ps.executeQuery();
                if(rs.next()){
                    TNoRM.setText(rs.getString("no_rkm_medis"));
                    DTPCari1.setDate(rs.getDate("tgl_registrasi"));
                    TPasien.setText(rs.getString("nm_pasien"));
                    JK.setText(rs.getString("jk"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }
    
    public void setNoRm(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        DTPCari2.setDate(tgl2);
        isRawat();
    }
    
    public void isCek(){
//        BtnSimpan.setEnabled(akses.getchecklist_kriteria_keluar_nicu());
//        BtnHapus.setEnabled(akses.getchecklist_kriteria_keluar_nicu());
//        BtnEdit.setEnabled(akses.getchecklist_kriteria_keluar_nicu());
//        BtnPrint.setEnabled(akses.getchecklist_kriteria_keluar_nicu()); 
//        if(akses.getjml2()>=1){
//            btnPetugas.setEnabled(false);
//            KodePetugas.setText(akses.getkode());
//            NamaPetugas.setText(pegawai.tampil3(akses.getkode()));
//        } 
    }

    private void ganti() {
        if(Sequel.mengedittf("checklist_kriteria_keluar_nicu","no_rawat=? and tanggal=?","no_rawat=?,tanggal=?,kriteria1=?,kriteria2=?,kriteria3=?,kriteria4=?,kriteria5=?,"+
                "kriteria6=?,kriteria7=?,kriteria8=?,kriteria9=?,kriteria10=?,kriteria11=?,kriteria12=?,nik=?",17,new String[]{
//                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),Kriteria1.getSelectedItem().toString(),
//                Kriteria2.getSelectedItem().toString(),Kriteria3.getSelectedItem().toString(),Kriteria4.getSelectedItem().toString(),Kriteria5.getSelectedItem().toString(),
//                Kriteria6.getSelectedItem().toString(),Kriteria7.getSelectedItem().toString(),Kriteria8.getSelectedItem().toString(),Kriteria9.getSelectedItem().toString(),
//                Kriteria10.getSelectedItem().toString(),Kriteria11.getSelectedItem().toString(),ttv.getSelectedItem().toString(),KodePetugas.getText(),
//                tbObat.getValueAt(tbObat.getSelectedRow(),0).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),5).toString()
        })==true){
//            tbObat.setValueAt(TNoRw.getText(),tbObat.getSelectedRow(),0);
//            tbObat.setValueAt(TNoRM.getText(),tbObat.getSelectedRow(),1);
//            tbObat.setValueAt(TPasien.getText(),tbObat.getSelectedRow(),2);
//            tbObat.setValueAt(TglLahir.getText(),tbObat.getSelectedRow(),3);
//            tbObat.setValueAt(JK.getText(),tbObat.getSelectedRow(),4);
//            tbObat.setValueAt(Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),tbObat.getSelectedRow(),5);
//            tbObat.setValueAt(KodePetugas.getText(),tbObat.getSelectedRow(),18);
//            tbObat.setValueAt(NamaPetugas.getText(),tbObat.getSelectedRow(),19);
            emptTeks();
        }
    }

    private void hapus() {
//        if(Sequel.queryu2tf("delete from checklist_kriteria_keluar_nicu where no_rawat=? and tanggal=?",2,new String[]{
//            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),5).toString()
//        })==true){
//            tabMode.removeRow(tbObat.getSelectedRow());
//            LCount.setText(""+tabMode.getRowCount());
//            emptTeks();
//        }else{
//            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
//        }
    }
}
