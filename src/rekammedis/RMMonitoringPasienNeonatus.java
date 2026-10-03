/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package rekammedis;

import fungsi.WarnaTable;
import fungsi.WarnaTableRanap;
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
public final class RMMonitoringPasienNeonatus extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
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
    public RMMonitoringPasienNeonatus(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);
        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","Nomer RM","Nama Pasien","Tgl Lahir","Jenis Kelamin","Tanggal","Vena Pariver 1","Vena Pariver 2","Vena Pariver 3","Alteri Line","Drain1","Drain2",
            "Drain3","Central Line","Suhu Inkubator","Suhu Bayi","Kesadaran","Nadi","Pernapasan","Saturasi","Grunting","NEWS","Warna Kulit","SNPKM","Skala Nyeri","TD","LP / LK",
            "Down Score","MAP","Oral","NGT","Cairan","Jumlah Cairan","Produk Darah","Jumlah Darah","Total Input","Total Komulatif","BB","IWL","Urin Input","Urin Komulatif",
            "Fases","Gastrik","Drain","IWL1","Total Input1","Komulatif1","Balance Cairan","Diuresis","Nilai Gula","NIP","Nama"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbData.setModel(tabMode);

        //tbData.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbData.getBackground()));
        tbData.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 51; i++) {
            TableColumn column = tbData.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(70);
            }else if(i==2){
                column.setPreferredWidth(170);
            }else if(i==3){
                column.setPreferredWidth(150);
            }else if(i==4){
                column.setPreferredWidth(80);
            }else if(i==5){
                column.setPreferredWidth(250);
            }else{
                column.setPreferredWidth(120);
            }
        }
        tbData.setDefaultRenderer(Object.class, new WarnaTableRanap());

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
        MnMonitoringNICU = new javax.swing.JMenuItem();
        LoadHTML = new widget.editorpane();
        JK = new widget.TextBox();
        tabPane1 = new widget.TabPane();
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
        jLabel97 = new widget.Label();
        jLabel98 = new widget.Label();
        jLabel99 = new widget.Label();
        jLabel100 = new widget.Label();
        NEWS = new widget.ComboBox();
        SNPKM = new widget.ComboBox();
        WarnaKulit = new widget.TextBox();
        jLabel102 = new widget.Label();
        jLabel103 = new widget.Label();
        jLabel104 = new widget.Label();
        jLabel105 = new widget.Label();
        jSeparator7 = new javax.swing.JSeparator();
        MAP = new widget.TextBox();
        jLabel108 = new widget.Label();
        skala_nyeri = new widget.TextBox();
        jLabel110 = new widget.Label();
        DownScore = new widget.TextBox();
        jLabel114 = new widget.Label();
        lplk = new widget.TextBox();
        td = new widget.TextBox();
        jLabel118 = new widget.Label();
        jLabel119 = new widget.Label();
        jLabel120 = new widget.Label();
        jLabel11 = new widget.Label();
        jLabel106 = new widget.Label();
        jLabel107 = new widget.Label();
        Oral = new widget.TextBox();
        jLabel121 = new widget.Label();
        jLabel115 = new widget.Label();
        NGT = new widget.TextBox();
        jLabel101 = new widget.Label();
        Cairan = new widget.ComboBox();
        jLabel109 = new widget.Label();
        jLabel122 = new widget.Label();
        jLabel116 = new widget.Label();
        JumlahCairan = new widget.TextBox();
        jLabel111 = new widget.Label();
        jLabel112 = new widget.Label();
        ProdukDarah = new widget.TextBox();
        jLabel123 = new widget.Label();
        JumlahDarah = new widget.TextBox();
        jLabel117 = new widget.Label();
        TotalInput = new widget.TextBox();
        jLabel113 = new widget.Label();
        jLabel124 = new widget.Label();
        jLabel125 = new widget.Label();
        jLabel126 = new widget.Label();
        TotalKomulatif = new widget.TextBox();
        BB = new widget.TextBox();
        jLabel127 = new widget.Label();
        jLabel128 = new widget.Label();
        jLabel129 = new widget.Label();
        jLabel130 = new widget.Label();
        IWL = new widget.TextBox();
        jLabel131 = new widget.Label();
        jLabel132 = new widget.Label();
        UrinInput = new widget.TextBox();
        jLabel133 = new widget.Label();
        jLabel134 = new widget.Label();
        UrinKomulatif = new widget.TextBox();
        jLabel135 = new widget.Label();
        jLabel136 = new widget.Label();
        Fases = new widget.TextBox();
        jLabel137 = new widget.Label();
        jLabel138 = new widget.Label();
        Gastrik = new widget.TextBox();
        jLabel139 = new widget.Label();
        jLabel140 = new widget.Label();
        Drain = new widget.TextBox();
        jLabel141 = new widget.Label();
        jLabel142 = new widget.Label();
        IWL1 = new widget.TextBox();
        jLabel143 = new widget.Label();
        jLabel144 = new widget.Label();
        TotalInput1 = new widget.TextBox();
        jLabel145 = new widget.Label();
        jLabel146 = new widget.Label();
        Komulatif1 = new widget.TextBox();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel147 = new widget.Label();
        jLabel148 = new widget.Label();
        BalanceCairan = new widget.TextBox();
        jLabel149 = new widget.Label();
        jLabel150 = new widget.Label();
        Diuresis = new widget.TextBox();
        jLabel151 = new widget.Label();
        jLabel152 = new widget.Label();
        NilaiGula = new widget.TextBox();
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
        Scroll1 = new widget.ScrollPane();
        tbData = new widget.Table();
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
        BtnCari1 = new widget.Button();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnMonitoringNICU.setBackground(new java.awt.Color(255, 255, 254));
        MnMonitoringNICU.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnMonitoringNICU.setForeground(new java.awt.Color(50, 50, 50));
        MnMonitoringNICU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnMonitoringNICU.setText("Formulir Checklist Kriteria Keluar HCU");
        MnMonitoringNICU.setName("MnMonitoringNICU"); // NOI18N
        MnMonitoringNICU.setPreferredSize(new java.awt.Dimension(260, 26));
        MnMonitoringNICU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnMonitoringNICUActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnMonitoringNICU);

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N

        JK.setHighlighter(null);
        JK.setName("JK"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        tabPane1.setName("tabPane1"); // NOI18N
        tabPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabPane1MouseClicked(evt);
            }
        });

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
        PanelInput1.setPreferredSize(new java.awt.Dimension(900, 100));
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

        jLabel67.setText("Drain 1");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput3.add(jLabel67);
        jLabel67.setBounds(470, 30, 70, 23);

        jLabel69.setText(":");
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput3.add(jLabel69);
        jLabel69.setBounds(540, 60, 10, 23);

        jLabel70.setText("Drain 3");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput3.add(jLabel70);
        jLabel70.setBounds(470, 90, 70, 23);

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
        jSeparator5.setBounds(0, 150, 900, 1);

        FormInput1.add(FormInput3);
        FormInput3.setBounds(0, 0, 900, 160);

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

        jLabel77.setText("Status Kesadaran");
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput4.add(jLabel77);
        jLabel77.setBounds(450, 30, 90, 23);

        jLabel90.setText(":");
        jLabel90.setName("jLabel90"); // NOI18N
        FormInput4.add(jLabel90);
        jLabel90.setBounds(540, 60, 10, 23);

        jLabel91.setText("Nadi");
        jLabel91.setName("jLabel91"); // NOI18N
        FormInput4.add(jLabel91);
        jLabel91.setBounds(690, 30, 60, 23);

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
        jSeparator6.setBounds(0, 120, 900, 1);

        jLabel97.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel97.setText("Warna Kulit");
        jLabel97.setName("jLabel97"); // NOI18N
        FormInput4.add(jLabel97);
        jLabel97.setBounds(10, 90, 80, 23);

        jLabel98.setText(":");
        jLabel98.setName("jLabel98"); // NOI18N
        FormInput4.add(jLabel98);
        jLabel98.setBounds(90, 90, 10, 23);

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

        jLabel102.setText("Tekanan Darah");
        jLabel102.setName("jLabel102"); // NOI18N
        FormInput4.add(jLabel102);
        jLabel102.setBounds(210, 130, 80, 20);

        jLabel103.setText(":");
        jLabel103.setName("jLabel103"); // NOI18N
        FormInput4.add(jLabel103);
        jLabel103.setBounds(90, 160, 10, 24);

        jLabel104.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel104.setText("Down Score");
        jLabel104.setName("jLabel104"); // NOI18N
        FormInput4.add(jLabel104);
        jLabel104.setBounds(10, 160, 80, 24);

        jLabel105.setText(":");
        jLabel105.setName("jLabel105"); // NOI18N
        FormInput4.add(jLabel105);
        jLabel105.setBounds(290, 130, 10, 20);

        jSeparator7.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator7.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator7.setName("jSeparator7"); // NOI18N
        FormInput4.add(jSeparator7);
        jSeparator7.setBounds(0, 190, 900, 1);

        MAP.setHighlighter(null);
        MAP.setName("MAP"); // NOI18N
        FormInput4.add(MAP);
        MAP.setBounds(300, 160, 90, 24);

        jLabel108.setText("LP / LK");
        jLabel108.setName("jLabel108"); // NOI18N
        FormInput4.add(jLabel108);
        jLabel108.setBounds(450, 130, 90, 20);

        skala_nyeri.setHighlighter(null);
        skala_nyeri.setName("skala_nyeri"); // NOI18N
        FormInput4.add(skala_nyeri);
        skala_nyeri.setBounds(100, 130, 90, 24);

        jLabel110.setText(":");
        jLabel110.setName("jLabel110"); // NOI18N
        FormInput4.add(jLabel110);
        jLabel110.setBounds(90, 130, 10, 20);

        DownScore.setHighlighter(null);
        DownScore.setName("DownScore"); // NOI18N
        FormInput4.add(DownScore);
        DownScore.setBounds(100, 160, 90, 24);

        jLabel114.setText(":");
        jLabel114.setName("jLabel114"); // NOI18N
        FormInput4.add(jLabel114);
        jLabel114.setBounds(290, 160, 10, 20);

        lplk.setHighlighter(null);
        lplk.setName("lplk"); // NOI18N
        FormInput4.add(lplk);
        lplk.setBounds(550, 130, 90, 24);

        td.setHighlighter(null);
        td.setName("td"); // NOI18N
        FormInput4.add(td);
        td.setBounds(300, 130, 90, 24);

        jLabel118.setText(":");
        jLabel118.setName("jLabel118"); // NOI18N
        FormInput4.add(jLabel118);
        jLabel118.setBounds(540, 130, 10, 20);

        jLabel119.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel119.setText("Skala Nyeri");
        jLabel119.setName("jLabel119"); // NOI18N
        FormInput4.add(jLabel119);
        jLabel119.setBounds(10, 130, 80, 20);

        jLabel120.setText("MAP");
        jLabel120.setName("jLabel120"); // NOI18N
        FormInput4.add(jLabel120);
        jLabel120.setBounds(210, 160, 80, 20);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("INTAKE (Satuan ml) TULISKAN KEBUTUHAN CAIRAN 24 JAM JIKA DIBUTUHKAN");
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput4.add(jLabel11);
        jLabel11.setBounds(0, 193, 900, 23);

        jLabel106.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel106.setText("Oral");
        jLabel106.setName("jLabel106"); // NOI18N
        FormInput4.add(jLabel106);
        jLabel106.setBounds(10, 220, 80, 20);

        jLabel107.setText(":");
        jLabel107.setName("jLabel107"); // NOI18N
        FormInput4.add(jLabel107);
        jLabel107.setBounds(90, 220, 10, 20);

        Oral.setHighlighter(null);
        Oral.setName("Oral"); // NOI18N
        FormInput4.add(Oral);
        Oral.setBounds(100, 220, 90, 24);

        jLabel121.setText("NGT / OGT / CATETER GASTOMI");
        jLabel121.setName("jLabel121"); // NOI18N
        FormInput4.add(jLabel121);
        jLabel121.setBounds(210, 220, 190, 23);

        jLabel115.setText(":");
        jLabel115.setName("jLabel115"); // NOI18N
        FormInput4.add(jLabel115);
        jLabel115.setBounds(400, 220, 10, 23);

        NGT.setHighlighter(null);
        NGT.setName("NGT"); // NOI18N
        FormInput4.add(NGT);
        NGT.setBounds(410, 220, 90, 23);

        jLabel101.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel101.setText("Cairan");
        jLabel101.setName("jLabel101"); // NOI18N
        FormInput4.add(jLabel101);
        jLabel101.setBounds(10, 250, 80, 23);

        Cairan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Injeksi / Infus dll." }));
        Cairan.setName("Cairan"); // NOI18N
        Cairan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CairanKeyPressed(evt);
            }
        });
        FormInput4.add(Cairan);
        Cairan.setBounds(100, 250, 240, 23);

        jLabel109.setText(":");
        jLabel109.setName("jLabel109"); // NOI18N
        FormInput4.add(jLabel109);
        jLabel109.setBounds(90, 250, 10, 23);

        jLabel122.setText("JUMLAH");
        jLabel122.setName("jLabel122"); // NOI18N
        FormInput4.add(jLabel122);
        jLabel122.setBounds(350, 250, 50, 23);

        jLabel116.setText(":");
        jLabel116.setName("jLabel116"); // NOI18N
        FormInput4.add(jLabel116);
        jLabel116.setBounds(400, 250, 10, 23);

        JumlahCairan.setHighlighter(null);
        JumlahCairan.setName("JumlahCairan"); // NOI18N
        FormInput4.add(JumlahCairan);
        JumlahCairan.setBounds(410, 250, 90, 23);

        jLabel111.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel111.setText("Produk Darah");
        jLabel111.setName("jLabel111"); // NOI18N
        FormInput4.add(jLabel111);
        jLabel111.setBounds(10, 280, 80, 24);

        jLabel112.setText(":");
        jLabel112.setName("jLabel112"); // NOI18N
        FormInput4.add(jLabel112);
        jLabel112.setBounds(90, 280, 10, 24);

        ProdukDarah.setHighlighter(null);
        ProdukDarah.setName("ProdukDarah"); // NOI18N
        FormInput4.add(ProdukDarah);
        ProdukDarah.setBounds(100, 280, 240, 24);

        jLabel123.setText("JUMLAH");
        jLabel123.setName("jLabel123"); // NOI18N
        FormInput4.add(jLabel123);
        jLabel123.setBounds(350, 280, 50, 24);

        JumlahDarah.setHighlighter(null);
        JumlahDarah.setName("JumlahDarah"); // NOI18N
        FormInput4.add(JumlahDarah);
        JumlahDarah.setBounds(410, 280, 90, 24);

        jLabel117.setText(":");
        jLabel117.setName("jLabel117"); // NOI18N
        FormInput4.add(jLabel117);
        jLabel117.setBounds(400, 280, 10, 24);

        TotalInput.setHighlighter(null);
        TotalInput.setName("TotalInput"); // NOI18N
        FormInput4.add(TotalInput);
        TotalInput.setBounds(100, 310, 90, 24);

        jLabel113.setText(":");
        jLabel113.setName("jLabel113"); // NOI18N
        FormInput4.add(jLabel113);
        jLabel113.setBounds(90, 310, 10, 24);

        jLabel124.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel124.setText("Total Input");
        jLabel124.setName("jLabel124"); // NOI18N
        FormInput4.add(jLabel124);
        jLabel124.setBounds(10, 310, 80, 24);

        jLabel125.setText("Komulatif");
        jLabel125.setName("jLabel125"); // NOI18N
        FormInput4.add(jLabel125);
        jLabel125.setBounds(210, 310, 80, 24);

        jLabel126.setText(":");
        jLabel126.setName("jLabel126"); // NOI18N
        FormInput4.add(jLabel126);
        jLabel126.setBounds(290, 310, 10, 24);

        TotalKomulatif.setHighlighter(null);
        TotalKomulatif.setName("TotalKomulatif"); // NOI18N
        FormInput4.add(TotalKomulatif);
        TotalKomulatif.setBounds(300, 310, 90, 24);

        BB.setHighlighter(null);
        BB.setName("BB"); // NOI18N
        FormInput4.add(BB);
        BB.setBounds(100, 340, 90, 24);

        jLabel127.setText(":");
        jLabel127.setName("jLabel127"); // NOI18N
        FormInput4.add(jLabel127);
        jLabel127.setBounds(90, 340, 10, 24);

        jLabel128.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel128.setText("BB (Gram)");
        jLabel128.setName("jLabel128"); // NOI18N
        FormInput4.add(jLabel128);
        jLabel128.setBounds(10, 340, 80, 24);

        jLabel129.setText("IWL");
        jLabel129.setName("jLabel129"); // NOI18N
        FormInput4.add(jLabel129);
        jLabel129.setBounds(210, 340, 80, 24);

        jLabel130.setText(":");
        jLabel130.setName("jLabel130"); // NOI18N
        FormInput4.add(jLabel130);
        jLabel130.setBounds(290, 340, 10, 24);

        IWL.setHighlighter(null);
        IWL.setName("IWL"); // NOI18N
        FormInput4.add(IWL);
        IWL.setBounds(300, 340, 90, 24);

        jLabel131.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel131.setText("Urin Input");
        jLabel131.setName("jLabel131"); // NOI18N
        FormInput4.add(jLabel131);
        jLabel131.setBounds(10, 370, 80, 24);

        jLabel132.setText(":");
        jLabel132.setName("jLabel132"); // NOI18N
        FormInput4.add(jLabel132);
        jLabel132.setBounds(90, 370, 10, 24);

        UrinInput.setHighlighter(null);
        UrinInput.setName("UrinInput"); // NOI18N
        FormInput4.add(UrinInput);
        UrinInput.setBounds(100, 370, 90, 24);

        jLabel133.setText("Komulatif");
        jLabel133.setName("jLabel133"); // NOI18N
        FormInput4.add(jLabel133);
        jLabel133.setBounds(210, 370, 80, 24);

        jLabel134.setText(":");
        jLabel134.setName("jLabel134"); // NOI18N
        FormInput4.add(jLabel134);
        jLabel134.setBounds(290, 370, 10, 24);

        UrinKomulatif.setHighlighter(null);
        UrinKomulatif.setName("UrinKomulatif"); // NOI18N
        FormInput4.add(UrinKomulatif);
        UrinKomulatif.setBounds(300, 370, 90, 24);

        jLabel135.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel135.setText("Fases");
        jLabel135.setName("jLabel135"); // NOI18N
        FormInput4.add(jLabel135);
        jLabel135.setBounds(10, 400, 80, 24);

        jLabel136.setText(":");
        jLabel136.setName("jLabel136"); // NOI18N
        FormInput4.add(jLabel136);
        jLabel136.setBounds(90, 400, 10, 24);

        Fases.setHighlighter(null);
        Fases.setName("Fases"); // NOI18N
        FormInput4.add(Fases);
        Fases.setBounds(100, 400, 90, 24);

        jLabel137.setText("Gastrik");
        jLabel137.setName("jLabel137"); // NOI18N
        FormInput4.add(jLabel137);
        jLabel137.setBounds(210, 400, 80, 24);

        jLabel138.setText(":");
        jLabel138.setName("jLabel138"); // NOI18N
        FormInput4.add(jLabel138);
        jLabel138.setBounds(290, 400, 10, 24);

        Gastrik.setHighlighter(null);
        Gastrik.setName("Gastrik"); // NOI18N
        FormInput4.add(Gastrik);
        Gastrik.setBounds(300, 400, 90, 24);

        jLabel139.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel139.setText("Drain");
        jLabel139.setName("jLabel139"); // NOI18N
        FormInput4.add(jLabel139);
        jLabel139.setBounds(10, 430, 80, 24);

        jLabel140.setText(":");
        jLabel140.setName("jLabel140"); // NOI18N
        FormInput4.add(jLabel140);
        jLabel140.setBounds(90, 430, 10, 24);

        Drain.setHighlighter(null);
        Drain.setName("Drain"); // NOI18N
        FormInput4.add(Drain);
        Drain.setBounds(100, 430, 90, 24);

        jLabel141.setText("IWL");
        jLabel141.setName("jLabel141"); // NOI18N
        FormInput4.add(jLabel141);
        jLabel141.setBounds(210, 430, 80, 24);

        jLabel142.setText(":");
        jLabel142.setName("jLabel142"); // NOI18N
        FormInput4.add(jLabel142);
        jLabel142.setBounds(290, 430, 10, 24);

        IWL1.setHighlighter(null);
        IWL1.setName("IWL1"); // NOI18N
        FormInput4.add(IWL1);
        IWL1.setBounds(300, 430, 90, 24);

        jLabel143.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel143.setText("Total Input");
        jLabel143.setName("jLabel143"); // NOI18N
        FormInput4.add(jLabel143);
        jLabel143.setBounds(10, 460, 80, 24);

        jLabel144.setText(":");
        jLabel144.setName("jLabel144"); // NOI18N
        FormInput4.add(jLabel144);
        jLabel144.setBounds(90, 460, 10, 24);

        TotalInput1.setHighlighter(null);
        TotalInput1.setName("TotalInput1"); // NOI18N
        FormInput4.add(TotalInput1);
        TotalInput1.setBounds(100, 460, 90, 24);

        jLabel145.setText("Komulatif");
        jLabel145.setName("jLabel145"); // NOI18N
        FormInput4.add(jLabel145);
        jLabel145.setBounds(210, 460, 80, 24);

        jLabel146.setText(":");
        jLabel146.setName("jLabel146"); // NOI18N
        FormInput4.add(jLabel146);
        jLabel146.setBounds(290, 460, 10, 24);

        Komulatif1.setHighlighter(null);
        Komulatif1.setName("Komulatif1"); // NOI18N
        FormInput4.add(Komulatif1);
        Komulatif1.setBounds(300, 460, 90, 24);

        jSeparator8.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator8.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator8.setName("jSeparator8"); // NOI18N
        FormInput4.add(jSeparator8);
        jSeparator8.setBounds(0, 490, 900, 1);

        jLabel147.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel147.setText("Balance Cairan");
        jLabel147.setName("jLabel147"); // NOI18N
        FormInput4.add(jLabel147);
        jLabel147.setBounds(10, 500, 80, 20);

        jLabel148.setText(":");
        jLabel148.setName("jLabel148"); // NOI18N
        FormInput4.add(jLabel148);
        jLabel148.setBounds(90, 500, 10, 20);

        BalanceCairan.setHighlighter(null);
        BalanceCairan.setName("BalanceCairan"); // NOI18N
        FormInput4.add(BalanceCairan);
        BalanceCairan.setBounds(100, 500, 90, 24);

        jLabel149.setText("Diuresis");
        jLabel149.setName("jLabel149"); // NOI18N
        FormInput4.add(jLabel149);
        jLabel149.setBounds(210, 500, 80, 20);

        jLabel150.setText(":");
        jLabel150.setName("jLabel150"); // NOI18N
        FormInput4.add(jLabel150);
        jLabel150.setBounds(290, 500, 10, 20);

        Diuresis.setHighlighter(null);
        Diuresis.setName("Diuresis"); // NOI18N
        FormInput4.add(Diuresis);
        Diuresis.setBounds(300, 500, 90, 24);

        jLabel151.setText("Nilai Gula");
        jLabel151.setName("jLabel151"); // NOI18N
        FormInput4.add(jLabel151);
        jLabel151.setBounds(450, 500, 90, 20);

        jLabel152.setText(":");
        jLabel152.setName("jLabel152"); // NOI18N
        FormInput4.add(jLabel152);
        jLabel152.setBounds(540, 500, 10, 20);

        NilaiGula.setHighlighter(null);
        NilaiGula.setName("NilaiGula"); // NOI18N
        FormInput4.add(NilaiGula);
        NilaiGula.setBounds(550, 500, 90, 24);

        FormInput1.add(FormInput4);
        FormInput4.setBounds(0, 160, 900, 580);

        scrollInput1.setViewportView(FormInput1);

        PanelInput1.add(scrollInput1, java.awt.BorderLayout.CENTER);

        internalFrame2.add(PanelInput1, java.awt.BorderLayout.CENTER);

        Scroll.setViewportView(internalFrame2);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

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
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "02-07-2025 17:02:16" }));
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

        tabPane1.addTab("Input Data", internalFrame1);

        Scroll1.setToolTipText("Klik data di table, kemudian klik kanan untuk memilih menu yang diinginkan");
        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);

        tbData.setToolTipText("Klik data di table, kemudian klik kanan untuk memilih menu yang diinginkan");
        tbData.setComponentPopupMenu(jPopupMenu1);
        tbData.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tbData.setName("tbData"); // NOI18N
        tbData.setRowHeight(35);
        tbData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDataMouseClicked(evt);
            }
        });
        Scroll1.setViewportView(tbData);

        tabPane1.addTab("Data monitoring", Scroll1);

        getContentPane().add(tabPane1, java.awt.BorderLayout.CENTER);

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
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "02-07-2025" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "02-07-2025" }));
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

        BtnCari1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/curve.png"))); // NOI18N
        BtnCari1.setMnemonic('3');
        BtnCari1.setText("GRAFIK MONITORING");
        BtnCari1.setToolTipText("Alt+3");
        BtnCari1.setName("BtnCari1"); // NOI18N
        BtnCari1.setPreferredSize(new java.awt.Dimension(200, 32));
        BtnCari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari1ActionPerformed(evt);
            }
        });
        BtnCari1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCari1KeyPressed(evt);
            }
        });
        panelGlass9.add(BtnCari1);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_END);

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
            if(Sequel.menyimpantf("monitoring_pasien_neonatus","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",47,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),VenaPariver1.getText(),VenaPariver2.getText(),
                VenaPariver3.getText(),AlteriLine.getText(),Drain1.getText(),Drain2.getText(),Drain3.getText(),CentralLine.getText(),SuhuInkubator.getText(),SuhuBayi.getText(),
                Kesadaran.getText(),Nadi.getText(),Pernapasan.getText(),Saturasi.getText(),Grunting.getText(),NEWS.getSelectedItem().toString(),WarnaKulit.getText(),SNPKM.getSelectedItem().toString(),
                skala_nyeri.getText(),td.getText(),lplk.getText(),DownScore.getText(),MAP.getText(),Oral.getText(),NGT.getText(),Cairan.getSelectedItem().toString(),JumlahCairan.getText(),
                ProdukDarah.getText(),JumlahDarah.getText(),TotalInput.getText(),TotalKomulatif.getText(),BB.getText(),IWL.getText(),UrinInput.getText(),UrinKomulatif.getText(),
                Fases.getText(),Gastrik.getText(),Drain.getText(),IWL1.getText(),TotalInput1.getText(),Komulatif1.getText(),BalanceCairan.getText(),Diuresis.getText(),NilaiGula.getText(),
                KodePetugas.getText()
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
//        if(tbData.getSelectedRow()>-1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else {
//                if(akses.getkode().equals(tbData.getValueAt(tbData.getSelectedRow(),18).toString())){
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
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        }else if(KodePetugas.getText().trim().equals("")||NamaPetugas.getText().trim().equals("")){
            Valid.textKosong(btnPetugas,"DPJP/Dokter Jaga/HCU");
        }else{  
            if(tbData.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else {
                    if(akses.getkode().equals(tbData.getValueAt(tbData.getSelectedRow(),18).toString())){
                        ganti();
                    }else{
                        ganti();
//                        JOptionPane.showMessageDialog(null,"Harus salah satu petugas sesuai user login..!!");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
            }
        }
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
//                           "<td valign='top'>"+tbData.getValueAt(i,0).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,1).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,2).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,3).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,4).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,5).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,6).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,7).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,8).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,9).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,10).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,11).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,12).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,13).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,14).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,15).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,16).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,17).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,18).toString()+"</td>"+
//                            "<td valign='top'>"+tbData.getValueAt(i,19).toString()+"</td>"+
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

    private void MnMonitoringNICUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnMonitoringNICUActionPerformed
//        if(tbData.getSelectedRow()>-1){
//            Map<String, Object> param = new HashMap<>();
//            param.put("namars",akses.getnamars());
//            param.put("alamatrs",akses.getalamatrs());
//            param.put("kotars",akses.getkabupatenrs());
//            param.put("propinsirs",akses.getpropinsirs());
//            param.put("kontakrs",akses.getkontakrs());
//            param.put("emailrs",akses.getemailrs());   
//            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
//            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbData.getValueAt(tbData.getSelectedRow(),18).toString());
//            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbData.getValueAt(tbData.getSelectedRow(),19).toString()+"\nID "+(finger.equals("")?tbData.getValueAt(tbData.getSelectedRow(),18).toString():finger)+"\n"+Tanggal.getSelectedItem()); 
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
//                    "where checklist_kriteria_keluar_nicu.no_rawat='"+tbData.getValueAt(tbData.getSelectedRow(),0).toString()+"' and checklist_kriteria_keluar_nicu.tanggal='"+tbData.getValueAt(tbData.getSelectedRow(),5).toString()+"' ",param);
//        }
    }//GEN-LAST:event_MnMonitoringNICUActionPerformed

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

    private void NEWSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NEWSKeyPressed
        //        Valid.pindah(evt,Kriteria8,Kriteria10);
    }//GEN-LAST:event_NEWSKeyPressed

    private void SNPKMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SNPKMKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SNPKMKeyPressed

    private void CairanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CairanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CairanKeyPressed

    private void BtnCari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari1ActionPerformed
        if(tbData.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            param.put("diagnosa",Sequel.cariIsi("select kamar_inap.diagnosa_awal from kamar_inap where kamar_inap.diagnosa_awal<>'' and kamar_inap.no_rawat=? ",TNoRw.getText()));
//            param.put("atas", Sequel.cariGambar("select setting_watermark.header from setting_watermark"));
//            param.put("bawah", Sequel.cariGambar("select setting_watermark.footer from setting_watermark"));
            Valid.MyReportqry("rptMonitoringPasienNICU.jasper","report","::[ Monitoring Pasien Neonatus ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,monitoring_pasien_neonatus.tanggal,"+
                    "monitoring_pasien_neonatus.venaparifier1,monitoring_pasien_neonatus.venaparifier2,monitoring_pasien_neonatus.venaparifier3,monitoring_pasien_neonatus.alteriline,"+
                    "monitoring_pasien_neonatus.drain1,monitoring_pasien_neonatus.drain2,monitoring_pasien_neonatus.drain3,monitoring_pasien_neonatus.centralline,"+
                    "monitoring_pasien_neonatus.suhu_inkubator,monitoring_pasien_neonatus.suhu_bayi,monitoring_pasien_neonatus.status_kesadaran,monitoring_pasien_neonatus.nadi,"+
                    "monitoring_pasien_neonatus.pernapasan,monitoring_pasien_neonatus.saturasi,monitoring_pasien_neonatus.grunting,monitoring_pasien_neonatus.nilai_news,"+
                    "monitoring_pasien_neonatus.warna_kulit,monitoring_pasien_neonatus.snpkm,monitoring_pasien_neonatus.skala_nyeri,monitoring_pasien_neonatus.td,"+
                    "monitoring_pasien_neonatus.lplk,monitoring_pasien_neonatus.downscore,monitoring_pasien_neonatus.map,monitoring_pasien_neonatus.oral,monitoring_pasien_neonatus.ngt,"+
                    "monitoring_pasien_neonatus.cairan,monitoring_pasien_neonatus.jumlah_cairan,monitoring_pasien_neonatus.produk_darah,monitoring_pasien_neonatus.jumlah_darah,"+
                    "monitoring_pasien_neonatus.total_input,monitoring_pasien_neonatus.komulatif,monitoring_pasien_neonatus.bb,monitoring_pasien_neonatus.iwl,monitoring_pasien_neonatus.urin_input,"+
                    "monitoring_pasien_neonatus.urin_komulatif,monitoring_pasien_neonatus.fases,monitoring_pasien_neonatus.gastrik,monitoring_pasien_neonatus.drain,monitoring_pasien_neonatus.iwl1,"+
                    "monitoring_pasien_neonatus.total_input1,monitoring_pasien_neonatus.total_komulatif1,monitoring_pasien_neonatus.balance_cairan,monitoring_pasien_neonatus.diuresis,"+
                    "monitoring_pasien_neonatus.nilai_gula,monitoring_pasien_neonatus.nip,pegawai.nama  "+
                    "from monitoring_pasien_neonatus inner join reg_periksa on monitoring_pasien_neonatus.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join pegawai on pegawai.nik=monitoring_pasien_neonatus.nip "+
                    "where monitoring_pasien_neonatus.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00"+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59"+"' and "+
                    "reg_periksa.no_rawat like '"+TNoRw.getText()+"' order by monitoring_pasien_neonatus.tanggal ",param);
        }
    }//GEN-LAST:event_BtnCari1ActionPerformed

    private void BtnCari1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCari1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCari1KeyPressed

    private void tabPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabPane1MouseClicked
        if(tabPane1.getSelectedIndex()==1){
            tampil();
        }
    }//GEN-LAST:event_tabPane1MouseClicked

    private void tbDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataMouseClicked
        getData();
    }//GEN-LAST:event_tbDataMouseClicked

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMMonitoringPasienNeonatus dialog = new RMMonitoringPasienNeonatus(new javax.swing.JFrame(), true);
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
    private widget.TextBox BB;
    private widget.TextBox BalanceCairan;
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnCari1;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.ComboBox Cairan;
    private widget.TextBox CentralLine;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextBox Diuresis;
    private widget.TextBox DownScore;
    private widget.TextBox Drain;
    private widget.TextBox Drain1;
    private widget.TextBox Drain2;
    private widget.TextBox Drain3;
    private widget.TextBox Fases;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormInput1;
    private widget.PanelBiasa FormInput3;
    private widget.PanelBiasa FormInput4;
    private widget.TextBox Gastrik;
    private widget.TextBox Grunting;
    private widget.TextBox IWL;
    private widget.TextBox IWL1;
    private widget.TextBox JK;
    private widget.TextBox JumlahCairan;
    private widget.TextBox JumlahDarah;
    private widget.TextBox Kesadaran;
    private widget.TextBox KodePetugas;
    private widget.TextBox Komulatif1;
    private widget.Label LCount;
    private widget.editorpane LoadHTML;
    private widget.TextBox MAP;
    private javax.swing.JMenuItem MnMonitoringNICU;
    private widget.ComboBox NEWS;
    private widget.TextBox NGT;
    private widget.TextBox Nadi;
    private widget.TextBox NamaPetugas;
    private widget.TextBox NilaiGula;
    private widget.TextBox Oral;
    private javax.swing.JPanel PanelInput;
    private javax.swing.JPanel PanelInput1;
    private widget.TextBox Pernapasan;
    private widget.TextBox ProdukDarah;
    private widget.ComboBox SNPKM;
    private widget.TextBox Saturasi;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.TextBox SuhuBayi;
    private widget.TextBox SuhuInkubator;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Tanggal Tanggal;
    private widget.TextBox TglLahir;
    private widget.TextBox TotalInput;
    private widget.TextBox TotalInput1;
    private widget.TextBox TotalKomulatif;
    private widget.TextBox UrinInput;
    private widget.TextBox UrinKomulatif;
    private widget.TextBox VenaPariver1;
    private widget.TextBox VenaPariver2;
    private widget.TextBox VenaPariver3;
    private widget.TextBox WarnaKulit;
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
    private widget.Label jLabel11;
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
    private widget.Label jLabel121;
    private widget.Label jLabel122;
    private widget.Label jLabel123;
    private widget.Label jLabel124;
    private widget.Label jLabel125;
    private widget.Label jLabel126;
    private widget.Label jLabel127;
    private widget.Label jLabel128;
    private widget.Label jLabel129;
    private widget.Label jLabel130;
    private widget.Label jLabel131;
    private widget.Label jLabel132;
    private widget.Label jLabel133;
    private widget.Label jLabel134;
    private widget.Label jLabel135;
    private widget.Label jLabel136;
    private widget.Label jLabel137;
    private widget.Label jLabel138;
    private widget.Label jLabel139;
    private widget.Label jLabel140;
    private widget.Label jLabel141;
    private widget.Label jLabel142;
    private widget.Label jLabel143;
    private widget.Label jLabel144;
    private widget.Label jLabel145;
    private widget.Label jLabel146;
    private widget.Label jLabel147;
    private widget.Label jLabel148;
    private widget.Label jLabel149;
    private widget.Label jLabel150;
    private widget.Label jLabel151;
    private widget.Label jLabel152;
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
    private javax.swing.JSeparator jSeparator8;
    private widget.TextBox lplk;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput1;
    private widget.TextBox skala_nyeri;
    private widget.TabPane tabPane1;
    private widget.Table tbData;
    private widget.TextBox td;
    // End of variables declaration//GEN-END:variables
    
    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            if(TCari.getText().trim().equals("")){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,monitoring_pasien_neonatus.tanggal,"+
                    "monitoring_pasien_neonatus.venaparifier1,monitoring_pasien_neonatus.venaparifier2,monitoring_pasien_neonatus.venaparifier3,monitoring_pasien_neonatus.alteriline,"+
                    "monitoring_pasien_neonatus.drain1,monitoring_pasien_neonatus.drain2,monitoring_pasien_neonatus.drain3,monitoring_pasien_neonatus.centralline,"+
                    "monitoring_pasien_neonatus.suhu_inkubator,monitoring_pasien_neonatus.suhu_bayi,monitoring_pasien_neonatus.status_kesadaran,monitoring_pasien_neonatus.nadi,"+
                    "monitoring_pasien_neonatus.pernapasan,monitoring_pasien_neonatus.saturasi,monitoring_pasien_neonatus.grunting,monitoring_pasien_neonatus.nilai_news,"+
                    "monitoring_pasien_neonatus.warna_kulit,monitoring_pasien_neonatus.snpkm,monitoring_pasien_neonatus.skala_nyeri,monitoring_pasien_neonatus.td,"+
                    "monitoring_pasien_neonatus.lplk,monitoring_pasien_neonatus.downscore,monitoring_pasien_neonatus.map,monitoring_pasien_neonatus.oral,monitoring_pasien_neonatus.ngt,"+
                    "monitoring_pasien_neonatus.cairan,monitoring_pasien_neonatus.jumlah_cairan,monitoring_pasien_neonatus.produk_darah,monitoring_pasien_neonatus.jumlah_darah,"+
                    "monitoring_pasien_neonatus.total_input,monitoring_pasien_neonatus.komulatif,monitoring_pasien_neonatus.bb,monitoring_pasien_neonatus.iwl,monitoring_pasien_neonatus.urin_input,"+
                    "monitoring_pasien_neonatus.urin_komulatif,monitoring_pasien_neonatus.fases,monitoring_pasien_neonatus.gastrik,monitoring_pasien_neonatus.drain,monitoring_pasien_neonatus.iwl1,"+
                    "monitoring_pasien_neonatus.total_input1,monitoring_pasien_neonatus.total_komulatif1,monitoring_pasien_neonatus.balance_cairan,monitoring_pasien_neonatus.diuresis,"+
                    "monitoring_pasien_neonatus.nilai_gula,monitoring_pasien_neonatus.nip,pegawai.nama  "+
                    "from monitoring_pasien_neonatus inner join reg_periksa on monitoring_pasien_neonatus.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join pegawai on pegawai.nik=monitoring_pasien_neonatus.nip "+
                    "where monitoring_pasien_neonatus.tanggal between ? and ? order by monitoring_pasien_neonatus.tanggal ");
            }else{
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,monitoring_pasien_neonatus.tanggal,"+
                    "monitoring_pasien_neonatus.venaparifier1,monitoring_pasien_neonatus.venaparifier2,monitoring_pasien_neonatus.venaparifier3,monitoring_pasien_neonatus.alteriline,"+
                    "monitoring_pasien_neonatus.drain1,monitoring_pasien_neonatus.drain2,monitoring_pasien_neonatus.drain3,monitoring_pasien_neonatus.centralline,"+
                    "monitoring_pasien_neonatus.suhu_inkubator,monitoring_pasien_neonatus.suhu_bayi,monitoring_pasien_neonatus.status_kesadaran,monitoring_pasien_neonatus.nadi,"+
                    "monitoring_pasien_neonatus.pernapasan,monitoring_pasien_neonatus.saturasi,monitoring_pasien_neonatus.grunting,monitoring_pasien_neonatus.nilai_news,"+
                    "monitoring_pasien_neonatus.warna_kulit,monitoring_pasien_neonatus.snpkm,monitoring_pasien_neonatus.skala_nyeri,monitoring_pasien_neonatus.td,"+
                    "monitoring_pasien_neonatus.lplk,monitoring_pasien_neonatus.downscore,monitoring_pasien_neonatus.map,monitoring_pasien_neonatus.oral,monitoring_pasien_neonatus.ngt,"+
                    "monitoring_pasien_neonatus.cairan,monitoring_pasien_neonatus.jumlah_cairan,monitoring_pasien_neonatus.produk_darah,monitoring_pasien_neonatus.jumlah_darah,"+
                    "monitoring_pasien_neonatus.total_input,monitoring_pasien_neonatus.komulatif,monitoring_pasien_neonatus.bb,monitoring_pasien_neonatus.iwl,monitoring_pasien_neonatus.urin_input,"+
                    "monitoring_pasien_neonatus.urin_komulatif,monitoring_pasien_neonatus.fases,monitoring_pasien_neonatus.gastrik,monitoring_pasien_neonatus.drain,monitoring_pasien_neonatus.iwl1,"+
                    "monitoring_pasien_neonatus.total_input1,monitoring_pasien_neonatus.total_komulatif1,monitoring_pasien_neonatus.balance_cairan,monitoring_pasien_neonatus.diuresis,"+
                    "monitoring_pasien_neonatus.nilai_gula,monitoring_pasien_neonatus.nip,pegawai.nama  "+
                    "from monitoring_pasien_neonatus inner join reg_periksa on monitoring_pasien_neonatus.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join pegawai on pegawai.nik=monitoring_pasien_neonatus.nip "+
                    "where monitoring_pasien_neonatus.tanggal between ? and ? and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or "+
                    "pasien.nm_pasien like ? or pegawai.nama like ? or monitoring_pasien_neonatus.nip like ?) order by monitoring_pasien_neonatus.tanggal ");
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
                while(rs.next()){
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("tgl_lahir"),rs.getString("jk"),
                        rs.getString("tanggal"),rs.getString("venaparifier1"),rs.getString("venaparifier2"),rs.getString("venaparifier3"),rs.getString("alteriline"),
                        rs.getString("drain1"),rs.getString("drain2"),rs.getString("drain3"),rs.getString("centralline"),rs.getString("suhu_inkubator"),rs.getString("suhu_bayi"),
                        rs.getString("status_kesadaran"),rs.getString("nadi"),rs.getString("pernapasan"),rs.getString("saturasi"),rs.getString("grunting"),rs.getString("nilai_news"),
                        rs.getString("warna_kulit"),rs.getString("snpkm"),rs.getString("skala_nyeri"),rs.getString("td"),rs.getString("lplk"),rs.getString("downscore"),
                        rs.getString("map"),rs.getString("oral"),rs.getString("ngt"),rs.getString("cairan"),rs.getString("jumlah_cairan"),rs.getString("produk_darah"),
                        rs.getString("jumlah_darah"),rs.getString("total_input"),rs.getString("komulatif"),rs.getString("bb"),rs.getString("iwl"),
                        rs.getString("urin_input"),rs.getString("urin_komulatif"),rs.getString("fases"),rs.getString("gastrik"),rs.getString("drain"),rs.getString("iwl1"),
                        rs.getString("total_input1"),rs.getString("total_komulatif1"),rs.getString("balance_cairan"),rs.getString("diuresis"),rs.getString("nilai_gula"),
                        rs.getString("nip"),rs.getString("nama")
                    });
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
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabMode.getRowCount());
    }
    
    public void emptTeks() {
        Tanggal.setDate(new Date());
        VenaPariver1.setText("");
        VenaPariver2.setText("");
        VenaPariver3.setText("");
        AlteriLine.setText("");
        Drain1.setText("");
        Drain2.setText("");
        Drain3.setText("");
        CentralLine.setText("");
        SuhuInkubator.setText("");
        SuhuBayi.setText("");
        Kesadaran.setText("");
        Nadi.setText("");
        Pernapasan.setText("");
        Saturasi.setText("");
        Grunting.setText("");
        WarnaKulit.setText("");
        skala_nyeri.setText("");
        td.setText("");
        lplk.setText("");
        DownScore.setText("");
        MAP.setText("");
        Oral.setText("");
        NGT.setText("");
        JumlahCairan.setText("");
        ProdukDarah.setText("");
        JumlahDarah.setText("");
        TotalInput.setText("");
        TotalKomulatif.setText("");
        BB.setText("");
        IWL.setText("");
        UrinInput.setText("");
        UrinKomulatif.setText("");
        Fases.setText("");
        Gastrik.setText("");
        Drain.setText("");
        IWL1.setText("");
        TotalInput1.setText("");
        Komulatif1.setText("");
        BalanceCairan.setText("");
        Diuresis.setText("");
        NilaiGula.setText("");
    } 

    private void getData() {
        if(tbData.getSelectedRow()!= -1){
            TNoRw.setText(tbData.getValueAt(tbData.getSelectedRow(),0).toString());
            TNoRM.setText(tbData.getValueAt(tbData.getSelectedRow(),1).toString());
            TPasien.setText(tbData.getValueAt(tbData.getSelectedRow(),2).toString());
            TglLahir.setText(tbData.getValueAt(tbData.getSelectedRow(),3).toString());
            JK.setText(tbData.getValueAt(tbData.getSelectedRow(),4).toString());
            Valid.SetTgl2(Tanggal,tbData.getValueAt(tbData.getSelectedRow(),5).toString());
            VenaPariver1.setText(tbData.getValueAt(tbData.getSelectedRow(),6).toString());
            VenaPariver2.setText(tbData.getValueAt(tbData.getSelectedRow(),7).toString());
            VenaPariver3.setText(tbData.getValueAt(tbData.getSelectedRow(),8).toString());
            AlteriLine.setText(tbData.getValueAt(tbData.getSelectedRow(),9).toString());
            Drain1.setText(tbData.getValueAt(tbData.getSelectedRow(),10).toString());
            Drain2.setText(tbData.getValueAt(tbData.getSelectedRow(),11).toString());
            Drain3.setText(tbData.getValueAt(tbData.getSelectedRow(),12).toString());
            CentralLine.setText(tbData.getValueAt(tbData.getSelectedRow(),13).toString());
            SuhuInkubator.setText(tbData.getValueAt(tbData.getSelectedRow(),14).toString());
            SuhuBayi.setText(tbData.getValueAt(tbData.getSelectedRow(),15).toString());
            Kesadaran.setText(tbData.getValueAt(tbData.getSelectedRow(),16).toString());
            Nadi.setText(tbData.getValueAt(tbData.getSelectedRow(),17).toString());
            Pernapasan.setText(tbData.getValueAt(tbData.getSelectedRow(),18).toString());
            Saturasi.setText(tbData.getValueAt(tbData.getSelectedRow(),19).toString());
            Grunting.setText(tbData.getValueAt(tbData.getSelectedRow(),20).toString());
//            NEWS.setText(tbData.getValueAt(tbData.getSelectedRow(),21).toString());
            WarnaKulit.setText(tbData.getValueAt(tbData.getSelectedRow(),22).toString());
//            SNPKM.setText(tbData.getValueAt(tbData.getSelectedRow(),23).toString());
            skala_nyeri.setText(tbData.getValueAt(tbData.getSelectedRow(),24).toString());
            td.setText(tbData.getValueAt(tbData.getSelectedRow(),25).toString());
            lplk.setText(tbData.getValueAt(tbData.getSelectedRow(),26).toString());
            DownScore.setText(tbData.getValueAt(tbData.getSelectedRow(),27).toString());
            MAP.setText(tbData.getValueAt(tbData.getSelectedRow(),28).toString());
            Oral.setText(tbData.getValueAt(tbData.getSelectedRow(),29).toString());
            NGT.setText(tbData.getValueAt(tbData.getSelectedRow(),30).toString());
//            Cairan.setText(tbData.getValueAt(tbData.getSelectedRow(),31).toString());
            JumlahCairan.setText(tbData.getValueAt(tbData.getSelectedRow(),32).toString());
            ProdukDarah.setText(tbData.getValueAt(tbData.getSelectedRow(),33).toString());
            JumlahDarah.setText(tbData.getValueAt(tbData.getSelectedRow(),34).toString());
            TotalInput.setText(tbData.getValueAt(tbData.getSelectedRow(),35).toString());
            TotalKomulatif.setText(tbData.getValueAt(tbData.getSelectedRow(),36).toString());
            BB.setText(tbData.getValueAt(tbData.getSelectedRow(),37).toString());
            IWL.setText(tbData.getValueAt(tbData.getSelectedRow(),38).toString());
            UrinInput.setText(tbData.getValueAt(tbData.getSelectedRow(),39).toString());
            UrinKomulatif.setText(tbData.getValueAt(tbData.getSelectedRow(),40).toString());
            Fases.setText(tbData.getValueAt(tbData.getSelectedRow(),41).toString());
            Gastrik.setText(tbData.getValueAt(tbData.getSelectedRow(),42).toString());
            Drain.setText(tbData.getValueAt(tbData.getSelectedRow(),43).toString());
            IWL1.setText(tbData.getValueAt(tbData.getSelectedRow(),44).toString());
            TotalInput1.setText(tbData.getValueAt(tbData.getSelectedRow(),45).toString());
            Komulatif1.setText(tbData.getValueAt(tbData.getSelectedRow(),46).toString());
            BalanceCairan.setText(tbData.getValueAt(tbData.getSelectedRow(),47).toString());
            Diuresis.setText(tbData.getValueAt(tbData.getSelectedRow(),48).toString());
            NilaiGula.setText(tbData.getValueAt(tbData.getSelectedRow(),49).toString());
        }
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
        if(Sequel.mengedittf("monitoring_pasien_neonatus","no_rawat=? and tanggal=?","no_rawat=?,tanggal=?,venaparifier1=?,venaparifier2=?,venaparifier3=?,alteriline=?,"+
                "drain1=?,drain2=?,drain3=?,centralline=?,suhu_inkubator=?,suhu_bayi=?,status_kesadaran=?,nadi=?,pernapasan=?,saturasi=?,grunting=?,nilai_news=?,warna_kulit=?,"+
                "snpkm=?,skala_nyeri=?,td=?,lplk=?,downscore=?,map=?,oral=?,ngt=?,cairan=?,jumlah_cairan=?,produk_darah=?,jumlah_darah=?,total_input=?,komulatif=?,bb=?,"+
                "iwl=?,urin_input=?,urin_komulatif=?,fases=?,gastrik=?,drain=?,iwl1=?,total_input1=?,total_komulatif1=?,balance_cairan=?,diuresis=?,nilai_gula=?,nip=?",49,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),
                VenaPariver1.getText(),VenaPariver2.getText(),VenaPariver3.getText(),AlteriLine.getText(),Drain1.getText(),Drain2.getText(),Drain3.getText(),
                CentralLine.getText(),SuhuInkubator.getText(),SuhuBayi.getText(),Kesadaran.getText(),Nadi.getText(),Pernapasan.getText(),Saturasi.getText(),
                Grunting.getText(),NEWS.getSelectedItem().toString(),WarnaKulit.getText(),SNPKM.getSelectedItem().toString(),skala_nyeri.getText(),td.getText(),
                lplk.getText(),DownScore.getText(),MAP.getText(),Oral.getText(),NGT.getText(),Cairan.getSelectedItem().toString(),JumlahCairan.getText(),
                ProdukDarah.getText(),JumlahDarah.getText(),TotalInput.getText(),TotalKomulatif.getText(),BB.getText(),IWL.getText(),UrinInput.getText(),
                UrinKomulatif.getText(),Fases.getText(),Gastrik.getText(),Drain.getText(),IWL1.getText(),TotalInput1.getText(),Komulatif1.getText(),BalanceCairan.getText(),
                Diuresis.getText(),NilaiGula.getText(),KodePetugas.getText(),
                tbData.getValueAt(tbData.getSelectedRow(),0).toString(),tbData.getValueAt(tbData.getSelectedRow(),5).toString()
        })==true){
            tampil();
            emptTeks();
        }
    }

    private void hapus() {
        if(Sequel.queryu2tf("delete from monitoring_pasien_neonatus where no_rawat=? and tanggal=?",2,new String[]{
            tbData.getValueAt(tbData.getSelectedRow(),0).toString(),tbData.getValueAt(tbData.getSelectedRow(),5).toString()
        })==true){
            tabMode.removeRow(tbData.getSelectedRow());
            LCount.setText(""+tabMode.getRowCount());
            emptTeks();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }
}
