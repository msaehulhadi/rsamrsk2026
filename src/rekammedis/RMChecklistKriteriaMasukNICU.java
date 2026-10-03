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
public final class RMChecklistKriteriaMasukNICU extends javax.swing.JDialog {
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
    public RMChecklistKriteriaMasukNICU(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.RM","Nama Pasien","Tgl.Lahir","J.K.","Tanggal","Nadi > 160",
            "CRT > 2 Detik","Akral Dingin","Sianosis (Biru) / Motled","Retraksi","Merintih",
            "RR > 60x/Menit","SPO2 < 90%","Butuh Oksigen","Kesadaran Menurun","Akivias Menurun",
            "Tonus Menurun","Refleks Menurun","Kejang",
            "Ada Muntah","Tidak Mau Minum","Diare > 10x",
            "Belum BAB > 24 Jam","Ada Distensi","Ada Kelainan Anatomi","Hipotermi < 36.5 Derajat Celcius",
            "Hipertermi >= 37.5 Derajat Celcius","Kuning / Ikterik","BBLR < 2500gr","Laboratorium Abnormal","BAK MWarna Merah",
            "BAB Darah","Tali Pusat Merah/Nanah/Tidak Normal","Tanda Infeksi Pada Mata/Hidung/Telinga/Mulut/Vagina/Penis/Kulit/Lainnya","Rawat NICU",
            "NIP/Kode Dokter","DPJP/Dokter Jaga/IGD"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 38; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(70);
            }else if(i==2){
                column.setPreferredWidth(150);
            }else if(i==3){
                column.setPreferredWidth(65);
            }else if(i==4){
                column.setPreferredWidth(25);
            }else if(i==5){
                column.setPreferredWidth(115);
            }else if(i==28){
                column.setPreferredWidth(90);
            }else if(i==29){
                column.setPreferredWidth(150);
            }else{
                column.setPreferredWidth(100);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

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
        
        ChkInput.setSelected(false);
        isForm();
        
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
        MnKriteriaMasukHCU = new javax.swing.JMenuItem();
        LoadHTML = new widget.editorpane();
        JK = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
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
        ChkInput = new widget.CekBox();
        scrollInput = new widget.ScrollPane();
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
        jLabel53 = new widget.Label();
        jLabel61 = new widget.Label();
        akraldingin = new widget.ComboBox();
        jLabel62 = new widget.Label();
        crt = new widget.ComboBox();
        jLabel63 = new widget.Label();
        jLabel64 = new widget.Label();
        jLabel65 = new widget.Label();
        nadi = new widget.ComboBox();
        jLabel66 = new widget.Label();
        sianosis = new widget.ComboBox();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel54 = new widget.Label();
        jLabel67 = new widget.Label();
        jLabel68 = new widget.Label();
        retraksi = new widget.ComboBox();
        merintih = new widget.ComboBox();
        jLabel69 = new widget.Label();
        jLabel70 = new widget.Label();
        jLabel71 = new widget.Label();
        rr = new widget.ComboBox();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel55 = new widget.Label();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel56 = new widget.Label();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel57 = new widget.Label();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel88 = new widget.Label();
        jSeparator13 = new javax.swing.JSeparator();
        jSeparator14 = new javax.swing.JSeparator();
        jLabel92 = new widget.Label();
        jLabel93 = new widget.Label();
        jLabel94 = new widget.Label();
        nicu = new widget.ComboBox();
        jLabel95 = new widget.Label();
        jLabel96 = new widget.Label();
        jLabel97 = new widget.Label();
        spo2 = new widget.ComboBox();
        jLabel98 = new widget.Label();
        jLabel99 = new widget.Label();
        o2 = new widget.ComboBox();
        jLabel100 = new widget.Label();
        jLabel101 = new widget.Label();
        kesadaran = new widget.ComboBox();
        jLabel102 = new widget.Label();
        jLabel103 = new widget.Label();
        akivias = new widget.ComboBox();
        jLabel104 = new widget.Label();
        jLabel105 = new widget.Label();
        jLabel106 = new widget.Label();
        jLabel107 = new widget.Label();
        tonus = new widget.ComboBox();
        refleksi = new widget.ComboBox();
        jLabel108 = new widget.Label();
        jLabel109 = new widget.Label();
        kejang = new widget.ComboBox();
        jLabel110 = new widget.Label();
        jLabel111 = new widget.Label();
        distensi = new widget.ComboBox();
        diare = new widget.ComboBox();
        belumbab = new widget.ComboBox();
        jLabel112 = new widget.Label();
        jLabel113 = new widget.Label();
        jLabel114 = new widget.Label();
        jLabel115 = new widget.Label();
        tidakminum = new widget.ComboBox();
        muntah = new widget.ComboBox();
        jLabel116 = new widget.Label();
        jLabel117 = new widget.Label();
        jLabel118 = new widget.Label();
        jLabel119 = new widget.Label();
        jLabel120 = new widget.Label();
        jLabel121 = new widget.Label();
        kelainananatomi = new widget.ComboBox();
        jLabel122 = new widget.Label();
        jLabel123 = new widget.Label();
        jLabel124 = new widget.Label();
        jLabel125 = new widget.Label();
        hipertermi = new widget.ComboBox();
        hipotermi = new widget.ComboBox();
        jLabel126 = new widget.Label();
        jLabel127 = new widget.Label();
        jLabel128 = new widget.Label();
        jLabel129 = new widget.Label();
        kuning = new widget.ComboBox();
        bblr = new widget.ComboBox();
        jLabel130 = new widget.Label();
        jLabel131 = new widget.Label();
        laboratorium = new widget.ComboBox();
        jLabel132 = new widget.Label();
        jLabel133 = new widget.Label();
        bak = new widget.ComboBox();
        jLabel134 = new widget.Label();
        babdarah = new widget.ComboBox();
        jLabel135 = new widget.Label();
        jLabel136 = new widget.Label();
        jLabel137 = new widget.Label();
        talipusat = new widget.ComboBox();
        jLabel138 = new widget.Label();
        jLabel139 = new widget.Label();
        tandainfeksi = new widget.ComboBox();
        jLabel140 = new widget.Label();
        jLabel141 = new widget.Label();
        jLabel142 = new widget.Label();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnKriteriaMasukHCU.setBackground(new java.awt.Color(255, 255, 254));
        MnKriteriaMasukHCU.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnKriteriaMasukHCU.setForeground(new java.awt.Color(50, 50, 50));
        MnKriteriaMasukHCU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnKriteriaMasukHCU.setText("Formulir Checklist Kriteria Masuk HCU");
        MnKriteriaMasukHCU.setName("MnKriteriaMasukHCU"); // NOI18N
        MnKriteriaMasukHCU.setPreferredSize(new java.awt.Dimension(260, 26));
        MnKriteriaMasukHCU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnKriteriaMasukHCUActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnKriteriaMasukHCU);

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N

        JK.setHighlighter(null);
        JK.setName("JK"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Check List Kriteria Masuk NICU ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setComponentPopupMenu(jPopupMenu1);
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

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
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "12-06-2025" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "12-06-2025" }));
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
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 386));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('I');
        ChkInput.setText(".: Input Data");
        ChkInput.setToolTipText("Alt+I");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 603));
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
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "12-06-2025 17:51:58" }));
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

        jLabel23.setText("DPJP / Dokter Jaga / IGD :");
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

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel53.setText("I. SISTEM KARDIOVASKULER");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(10, 70, 180, 23);

        jLabel61.setText("Akral Dingin :");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput.add(jLabel61);
        jLabel61.setBounds(470, 90, 150, 23);

        akraldingin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        akraldingin.setSelectedIndex(1);
        akraldingin.setName("akraldingin"); // NOI18N
        akraldingin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                akraldinginKeyPressed(evt);
            }
        });
        FormInput.add(akraldingin);
        akraldingin.setBounds(630, 90, 90, 23);

        jLabel62.setText(":");
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput.add(jLabel62);
        jLabel62.setBounds(140, 120, 10, 23);

        crt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        crt.setSelectedIndex(1);
        crt.setName("crt"); // NOI18N
        crt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                crtKeyPressed(evt);
            }
        });
        FormInput.add(crt);
        crt.setBounds(150, 120, 90, 23);

        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel63.setText("CRT > 2 Detik");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput.add(jLabel63);
        jLabel63.setBounds(40, 120, 100, 23);

        jLabel64.setText(":");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput.add(jLabel64);
        jLabel64.setBounds(140, 90, 10, 23);

        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel65.setText("Nadi > 160");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput.add(jLabel65);
        jLabel65.setBounds(40, 90, 100, 23);

        nadi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        nadi.setSelectedIndex(1);
        nadi.setName("nadi"); // NOI18N
        nadi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nadiKeyPressed(evt);
            }
        });
        FormInput.add(nadi);
        nadi.setBounds(150, 90, 90, 23);

        jLabel66.setText("Sianosis (biru)/ Motled :");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(470, 120, 150, 23);

        sianosis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        sianosis.setSelectedIndex(1);
        sianosis.setName("sianosis"); // NOI18N
        sianosis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sianosisKeyPressed(evt);
            }
        });
        FormInput.add(sianosis);
        sianosis.setBounds(630, 120, 90, 23);

        jSeparator3.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator3.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator3.setName("jSeparator3"); // NOI18N
        FormInput.add(jSeparator3);
        jSeparator3.setBounds(0, 180, 810, 1);

        jSeparator4.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator4.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator4.setName("jSeparator4"); // NOI18N
        FormInput.add(jSeparator4);
        jSeparator4.setBounds(0, 180, 810, 1);

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel54.setText("II. SISTEM RESPIRASI");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(10, 180, 180, 23);

        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel67.setText("Retraksi");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput.add(jLabel67);
        jLabel67.setBounds(40, 200, 70, 23);

        jLabel68.setText(":");
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput.add(jLabel68);
        jLabel68.setBounds(110, 200, 10, 23);

        retraksi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        retraksi.setSelectedIndex(1);
        retraksi.setName("retraksi"); // NOI18N
        retraksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                retraksiKeyPressed(evt);
            }
        });
        FormInput.add(retraksi);
        retraksi.setBounds(120, 200, 90, 23);

        merintih.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        merintih.setSelectedIndex(1);
        merintih.setName("merintih"); // NOI18N
        merintih.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                merintihKeyPressed(evt);
            }
        });
        FormInput.add(merintih);
        merintih.setBounds(120, 230, 90, 23);

        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel69.setText("Merintih");
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput.add(jLabel69);
        jLabel69.setBounds(40, 230, 70, 23);

        jLabel70.setText(":");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput.add(jLabel70);
        jLabel70.setBounds(110, 230, 10, 23);

        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel71.setText("RR > 60 X / Menit");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput.add(jLabel71);
        jLabel71.setBounds(280, 200, 100, 23);

        rr.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        rr.setSelectedIndex(1);
        rr.setName("rr"); // NOI18N
        rr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rrKeyPressed(evt);
            }
        });
        FormInput.add(rr);
        rr.setBounds(390, 200, 100, 23);

        jSeparator5.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator5.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator5.setName("jSeparator5"); // NOI18N
        FormInput.add(jSeparator5);
        jSeparator5.setBounds(0, 260, 810, 1);

        jSeparator6.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator6.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator6.setName("jSeparator6"); // NOI18N
        FormInput.add(jSeparator6);
        jSeparator6.setBounds(0, 260, 810, 1);

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel55.setText("III. SISTEM NEUROLOGIS");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(10, 260, 180, 23);

        jSeparator7.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator7.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator7.setName("jSeparator7"); // NOI18N
        FormInput.add(jSeparator7);
        jSeparator7.setBounds(0, 340, 810, 1);

        jSeparator8.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator8.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator8.setName("jSeparator8"); // NOI18N
        FormInput.add(jSeparator8);
        jSeparator8.setBounds(0, 340, 810, 1);

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel56.setText("IV. SISTEM GASTROINTESTINAL");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput.add(jLabel56);
        jLabel56.setBounds(10, 340, 240, 23);

        jSeparator9.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator9.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator9.setName("jSeparator9"); // NOI18N
        FormInput.add(jSeparator9);
        jSeparator9.setBounds(0, 420, 810, 1);

        jSeparator10.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator10.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator10.setName("jSeparator10"); // NOI18N
        FormInput.add(jSeparator10);
        jSeparator10.setBounds(0, 420, 810, 1);

        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel57.setText("V. SISTEM BEDAH");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(10, 420, 240, 23);

        jSeparator11.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator11.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator11.setName("jSeparator11"); // NOI18N
        FormInput.add(jSeparator11);
        jSeparator11.setBounds(0, 470, 810, 1);

        jSeparator12.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator12.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator12.setName("jSeparator12"); // NOI18N
        FormInput.add(jSeparator12);
        jSeparator12.setBounds(0, 470, 810, 1);

        jLabel88.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel88.setText("VI. LAIN-LAIN");
        jLabel88.setName("jLabel88"); // NOI18N
        FormInput.add(jLabel88);
        jLabel88.setBounds(10, 470, 240, 23);

        jSeparator13.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator13.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator13.setName("jSeparator13"); // NOI18N
        FormInput.add(jSeparator13);
        jSeparator13.setBounds(0, 600, 810, 1);

        jSeparator14.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator14.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator14.setName("jSeparator14"); // NOI18N
        FormInput.add(jSeparator14);
        jSeparator14.setBounds(0, 600, 810, 1);

        jLabel92.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel92.setText("VII. TINDAK LANJUT");
        jLabel92.setName("jLabel92"); // NOI18N
        FormInput.add(jLabel92);
        jLabel92.setBounds(10, 600, 240, 23);

        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel93.setText("RAWAT NICU");
        jLabel93.setName("jLabel93"); // NOI18N
        FormInput.add(jLabel93);
        jLabel93.setBounds(40, 620, 70, 23);

        jLabel94.setText(":");
        jLabel94.setName("jLabel94"); // NOI18N
        FormInput.add(jLabel94);
        jLabel94.setBounds(115, 620, 10, 23);

        nicu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        nicu.setSelectedIndex(1);
        nicu.setName("nicu"); // NOI18N
        nicu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nicuKeyPressed(evt);
            }
        });
        FormInput.add(nicu);
        nicu.setBounds(130, 620, 80, 23);

        jLabel95.setText(":");
        jLabel95.setName("jLabel95"); // NOI18N
        FormInput.add(jLabel95);
        jLabel95.setBounds(380, 200, 10, 23);

        jLabel96.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel96.setText("SPO2 < 90%");
        jLabel96.setName("jLabel96"); // NOI18N
        FormInput.add(jLabel96);
        jLabel96.setBounds(280, 230, 100, 23);

        jLabel97.setText(":");
        jLabel97.setName("jLabel97"); // NOI18N
        FormInput.add(jLabel97);
        jLabel97.setBounds(380, 230, 10, 23);

        spo2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        spo2.setSelectedIndex(1);
        spo2.setName("spo2"); // NOI18N
        spo2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                spo2KeyPressed(evt);
            }
        });
        FormInput.add(spo2);
        spo2.setBounds(390, 230, 100, 23);

        jLabel98.setText("Butuh Oksigen");
        jLabel98.setName("jLabel98"); // NOI18N
        FormInput.add(jLabel98);
        jLabel98.setBounds(530, 200, 80, 23);

        jLabel99.setText(":");
        jLabel99.setName("jLabel99"); // NOI18N
        FormInput.add(jLabel99);
        jLabel99.setBounds(610, 200, 10, 23);

        o2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        o2.setSelectedIndex(1);
        o2.setName("o2"); // NOI18N
        o2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                o2KeyPressed(evt);
            }
        });
        FormInput.add(o2);
        o2.setBounds(620, 200, 100, 23);

        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel100.setText("Kesadaran Menurun");
        jLabel100.setName("jLabel100"); // NOI18N
        FormInput.add(jLabel100);
        jLabel100.setBounds(40, 280, 100, 23);

        jLabel101.setText(":");
        jLabel101.setName("jLabel101"); // NOI18N
        FormInput.add(jLabel101);
        jLabel101.setBounds(140, 280, 10, 23);

        kesadaran.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        kesadaran.setSelectedIndex(1);
        kesadaran.setName("kesadaran"); // NOI18N
        kesadaran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kesadaranKeyPressed(evt);
            }
        });
        FormInput.add(kesadaran);
        kesadaran.setBounds(150, 280, 90, 23);

        jLabel102.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel102.setText("Akivias Menurun");
        jLabel102.setName("jLabel102"); // NOI18N
        FormInput.add(jLabel102);
        jLabel102.setBounds(40, 310, 100, 23);

        jLabel103.setText(":");
        jLabel103.setName("jLabel103"); // NOI18N
        FormInput.add(jLabel103);
        jLabel103.setBounds(140, 310, 10, 23);

        akivias.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        akivias.setSelectedIndex(1);
        akivias.setName("akivias"); // NOI18N
        akivias.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                akiviasKeyPressed(evt);
            }
        });
        FormInput.add(akivias);
        akivias.setBounds(150, 310, 90, 23);

        jLabel104.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel104.setText("Tonus Menurun");
        jLabel104.setName("jLabel104"); // NOI18N
        FormInput.add(jLabel104);
        jLabel104.setBounds(280, 280, 100, 23);

        jLabel105.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel105.setText("Refleks Menurun");
        jLabel105.setName("jLabel105"); // NOI18N
        FormInput.add(jLabel105);
        jLabel105.setBounds(280, 310, 100, 23);

        jLabel106.setText(":");
        jLabel106.setName("jLabel106"); // NOI18N
        FormInput.add(jLabel106);
        jLabel106.setBounds(380, 310, 10, 23);

        jLabel107.setText(":");
        jLabel107.setName("jLabel107"); // NOI18N
        FormInput.add(jLabel107);
        jLabel107.setBounds(380, 280, 10, 23);

        tonus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        tonus.setSelectedIndex(1);
        tonus.setName("tonus"); // NOI18N
        tonus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tonusKeyPressed(evt);
            }
        });
        FormInput.add(tonus);
        tonus.setBounds(390, 280, 100, 23);

        refleksi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        refleksi.setSelectedIndex(1);
        refleksi.setName("refleksi"); // NOI18N
        refleksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                refleksiKeyPressed(evt);
            }
        });
        FormInput.add(refleksi);
        refleksi.setBounds(390, 310, 100, 23);

        jLabel108.setText("Kejang");
        jLabel108.setName("jLabel108"); // NOI18N
        FormInput.add(jLabel108);
        jLabel108.setBounds(570, 280, 40, 23);

        jLabel109.setText(":");
        jLabel109.setName("jLabel109"); // NOI18N
        FormInput.add(jLabel109);
        jLabel109.setBounds(610, 280, 10, 23);

        kejang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        kejang.setSelectedIndex(1);
        kejang.setName("kejang"); // NOI18N
        kejang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kejangKeyPressed(evt);
            }
        });
        FormInput.add(kejang);
        kejang.setBounds(620, 280, 100, 23);

        jLabel110.setText("Ada Distensi");
        jLabel110.setName("jLabel110"); // NOI18N
        FormInput.add(jLabel110);
        jLabel110.setBounds(540, 360, 70, 23);

        jLabel111.setText(":");
        jLabel111.setName("jLabel111"); // NOI18N
        FormInput.add(jLabel111);
        jLabel111.setBounds(610, 360, 10, 23);

        distensi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        distensi.setSelectedIndex(1);
        distensi.setName("distensi"); // NOI18N
        distensi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                distensiKeyPressed(evt);
            }
        });
        FormInput.add(distensi);
        distensi.setBounds(620, 360, 100, 23);

        diare.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        diare.setSelectedIndex(1);
        diare.setName("diare"); // NOI18N
        diare.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                diareKeyPressed(evt);
            }
        });
        FormInput.add(diare);
        diare.setBounds(390, 360, 100, 23);

        belumbab.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        belumbab.setSelectedIndex(1);
        belumbab.setName("belumbab"); // NOI18N
        belumbab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                belumbabKeyPressed(evt);
            }
        });
        FormInput.add(belumbab);
        belumbab.setBounds(390, 390, 100, 23);

        jLabel112.setText(":");
        jLabel112.setName("jLabel112"); // NOI18N
        FormInput.add(jLabel112);
        jLabel112.setBounds(380, 390, 10, 23);

        jLabel113.setText(":");
        jLabel113.setName("jLabel113"); // NOI18N
        FormInput.add(jLabel113);
        jLabel113.setBounds(380, 360, 10, 23);

        jLabel114.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel114.setText("Diare > 10 X");
        jLabel114.setName("jLabel114"); // NOI18N
        FormInput.add(jLabel114);
        jLabel114.setBounds(280, 360, 100, 23);

        jLabel115.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel115.setText("Belum BAB > 24 Jam");
        jLabel115.setName("jLabel115"); // NOI18N
        FormInput.add(jLabel115);
        jLabel115.setBounds(280, 390, 100, 23);

        tidakminum.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        tidakminum.setSelectedIndex(1);
        tidakminum.setName("tidakminum"); // NOI18N
        tidakminum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tidakminumKeyPressed(evt);
            }
        });
        FormInput.add(tidakminum);
        tidakminum.setBounds(150, 390, 90, 23);

        muntah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        muntah.setSelectedIndex(1);
        muntah.setName("muntah"); // NOI18N
        muntah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                muntahKeyPressed(evt);
            }
        });
        FormInput.add(muntah);
        muntah.setBounds(150, 360, 90, 23);

        jLabel116.setText(":");
        jLabel116.setName("jLabel116"); // NOI18N
        FormInput.add(jLabel116);
        jLabel116.setBounds(140, 360, 10, 23);

        jLabel117.setText(":");
        jLabel117.setName("jLabel117"); // NOI18N
        FormInput.add(jLabel117);
        jLabel117.setBounds(140, 390, 10, 23);

        jLabel118.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel118.setText("Tidak Mau Minum");
        jLabel118.setName("jLabel118"); // NOI18N
        FormInput.add(jLabel118);
        jLabel118.setBounds(40, 390, 100, 23);

        jLabel119.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel119.setText("Ada Muntah");
        jLabel119.setName("jLabel119"); // NOI18N
        FormInput.add(jLabel119);
        jLabel119.setBounds(40, 360, 100, 23);

        jLabel120.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel120.setText("Ada Kelainan Anatomi");
        jLabel120.setName("jLabel120"); // NOI18N
        FormInput.add(jLabel120);
        jLabel120.setBounds(30, 440, 110, 23);

        jLabel121.setText(":");
        jLabel121.setName("jLabel121"); // NOI18N
        FormInput.add(jLabel121);
        jLabel121.setBounds(140, 440, 10, 23);

        kelainananatomi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        kelainananatomi.setSelectedIndex(1);
        kelainananatomi.setName("kelainananatomi"); // NOI18N
        kelainananatomi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kelainananatomiKeyPressed(evt);
            }
        });
        FormInput.add(kelainananatomi);
        kelainananatomi.setBounds(150, 440, 90, 23);

        jLabel122.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel122.setText("Hipotermi < 36.5 C");
        jLabel122.setName("jLabel122"); // NOI18N
        FormInput.add(jLabel122);
        jLabel122.setBounds(30, 490, 110, 23);

        jLabel123.setText(":");
        jLabel123.setName("jLabel123"); // NOI18N
        FormInput.add(jLabel123);
        jLabel123.setBounds(140, 490, 10, 23);

        jLabel124.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel124.setText("Hipertermi >= 37.5 C");
        jLabel124.setName("jLabel124"); // NOI18N
        FormInput.add(jLabel124);
        jLabel124.setBounds(30, 520, 110, 23);

        jLabel125.setText(":");
        jLabel125.setName("jLabel125"); // NOI18N
        FormInput.add(jLabel125);
        jLabel125.setBounds(140, 520, 10, 23);

        hipertermi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        hipertermi.setSelectedIndex(1);
        hipertermi.setName("hipertermi"); // NOI18N
        hipertermi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hipertermiKeyPressed(evt);
            }
        });
        FormInput.add(hipertermi);
        hipertermi.setBounds(150, 520, 90, 23);

        hipotermi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        hipotermi.setSelectedIndex(1);
        hipotermi.setName("hipotermi"); // NOI18N
        hipotermi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hipotermiKeyPressed(evt);
            }
        });
        FormInput.add(hipotermi);
        hipotermi.setBounds(150, 490, 90, 23);

        jLabel126.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel126.setText("Kuning / Ikterik");
        jLabel126.setName("jLabel126"); // NOI18N
        FormInput.add(jLabel126);
        jLabel126.setBounds(280, 490, 100, 23);

        jLabel127.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel127.setText("BBLR < 2500 gr");
        jLabel127.setName("jLabel127"); // NOI18N
        FormInput.add(jLabel127);
        jLabel127.setBounds(280, 520, 100, 23);

        jLabel128.setText(":");
        jLabel128.setName("jLabel128"); // NOI18N
        FormInput.add(jLabel128);
        jLabel128.setBounds(380, 520, 10, 23);

        jLabel129.setText(":");
        jLabel129.setName("jLabel129"); // NOI18N
        FormInput.add(jLabel129);
        jLabel129.setBounds(380, 490, 10, 23);

        kuning.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        kuning.setSelectedIndex(1);
        kuning.setName("kuning"); // NOI18N
        kuning.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kuningKeyPressed(evt);
            }
        });
        FormInput.add(kuning);
        kuning.setBounds(390, 490, 100, 23);

        bblr.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        bblr.setSelectedIndex(1);
        bblr.setName("bblr"); // NOI18N
        bblr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bblrKeyPressed(evt);
            }
        });
        FormInput.add(bblr);
        bblr.setBounds(390, 520, 100, 23);

        jLabel130.setText("Laboratorium Abnormal");
        jLabel130.setName("jLabel130"); // NOI18N
        FormInput.add(jLabel130);
        jLabel130.setBounds(510, 490, 120, 23);

        jLabel131.setText(":");
        jLabel131.setName("jLabel131"); // NOI18N
        FormInput.add(jLabel131);
        jLabel131.setBounds(630, 490, 10, 23);

        laboratorium.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        laboratorium.setSelectedIndex(1);
        laboratorium.setName("laboratorium"); // NOI18N
        laboratorium.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                laboratoriumKeyPressed(evt);
            }
        });
        FormInput.add(laboratorium);
        laboratorium.setBounds(640, 490, 80, 23);

        jLabel132.setText("BAK Warna Merah");
        jLabel132.setName("jLabel132"); // NOI18N
        FormInput.add(jLabel132);
        jLabel132.setBounds(510, 520, 120, 23);

        jLabel133.setText(":");
        jLabel133.setName("jLabel133"); // NOI18N
        FormInput.add(jLabel133);
        jLabel133.setBounds(630, 520, 10, 23);

        bak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        bak.setSelectedIndex(1);
        bak.setName("bak"); // NOI18N
        bak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bakKeyPressed(evt);
            }
        });
        FormInput.add(bak);
        bak.setBounds(640, 520, 80, 23);

        jLabel134.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel134.setText("BAB Darah");
        jLabel134.setName("jLabel134"); // NOI18N
        FormInput.add(jLabel134);
        jLabel134.setBounds(30, 552, 110, 23);

        babdarah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        babdarah.setSelectedIndex(1);
        babdarah.setName("babdarah"); // NOI18N
        babdarah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                babdarahKeyPressed(evt);
            }
        });
        FormInput.add(babdarah);
        babdarah.setBounds(150, 552, 90, 23);

        jLabel135.setText(":");
        jLabel135.setName("jLabel135"); // NOI18N
        FormInput.add(jLabel135);
        jLabel135.setBounds(140, 552, 10, 23);

        jLabel136.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel136.setText("Nanah / Tidak Normal");
        jLabel136.setName("jLabel136"); // NOI18N
        FormInput.add(jLabel136);
        jLabel136.setBounds(280, 565, 110, 23);

        jLabel137.setText(":");
        jLabel137.setName("jLabel137"); // NOI18N
        FormInput.add(jLabel137);
        jLabel137.setBounds(380, 552, 10, 23);

        talipusat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        talipusat.setSelectedIndex(1);
        talipusat.setName("talipusat"); // NOI18N
        talipusat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                talipusatKeyPressed(evt);
            }
        });
        FormInput.add(talipusat);
        talipusat.setBounds(390, 552, 100, 23);

        jLabel138.setText("Tanda Infeksi Pada Mata / ");
        jLabel138.setName("jLabel138"); // NOI18N
        FormInput.add(jLabel138);
        jLabel138.setBounds(490, 552, 140, 23);

        jLabel139.setText(":");
        jLabel139.setName("jLabel139"); // NOI18N
        FormInput.add(jLabel139);
        jLabel139.setBounds(630, 552, 10, 23);

        tandainfeksi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        tandainfeksi.setSelectedIndex(1);
        tandainfeksi.setName("tandainfeksi"); // NOI18N
        tandainfeksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tandainfeksiKeyPressed(evt);
            }
        });
        FormInput.add(tandainfeksi);
        tandainfeksi.setBounds(640, 552, 80, 23);

        jLabel140.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel140.setText("Tali Pusat Merah /");
        jLabel140.setName("jLabel140"); // NOI18N
        FormInput.add(jLabel140);
        jLabel140.setBounds(280, 552, 100, 23);

        jLabel141.setText("Hidung / Telinga / Mulut / ");
        jLabel141.setName("jLabel141"); // NOI18N
        FormInput.add(jLabel141);
        jLabel141.setBounds(490, 565, 140, 23);

        jLabel142.setText("Vagina / Penis / Kulit / Lainnya");
        jLabel142.setName("jLabel142"); // NOI18N
        FormInput.add(jLabel142);
        jLabel142.setBounds(490, 578, 160, 23);

        scrollInput.setViewportView(FormInput);

        PanelInput.add(scrollInput, java.awt.BorderLayout.CENTER);

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
            Valid.textKosong(btnPetugas,"DPJP/Dokter Jaga/IGD");
        }else{
            if(Sequel.menyimpantf("checklist_kriteria_masuk_nicu","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",33,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),nadi.getSelectedItem().toString(),
                crt.getSelectedItem().toString(),akraldingin.getSelectedItem().toString(),sianosis.getSelectedItem().toString(),retraksi.getSelectedItem().toString(),
                merintih.getSelectedItem().toString(),rr.getSelectedItem().toString(),spo2.getSelectedItem().toString(),o2.getSelectedItem().toString(),
                kesadaran.getSelectedItem().toString(),akivias.getSelectedItem().toString(),tonus.getSelectedItem().toString(),refleksi.getSelectedItem().toString(),
                kejang.getSelectedItem().toString(),muntah.getSelectedItem().toString(),tidakminum.getSelectedItem().toString(),diare.getSelectedItem().toString(),
                belumbab.getSelectedItem().toString(),distensi.getSelectedItem().toString(),kelainananatomi.getSelectedItem().toString(),hipotermi.getSelectedItem().toString(),
                hipertermi.getSelectedItem().toString(),kuning.getSelectedItem().toString(),bblr.getSelectedItem().toString(),laboratorium.getSelectedItem().toString(),
                bak.getSelectedItem().toString(),babdarah.getSelectedItem().toString(),talipusat.getSelectedItem().toString(),tandainfeksi.getSelectedItem().toString(),
                nicu.getSelectedItem().toString(),KodePetugas.getText()
            })==true){
                tampil();
                LCount.setText(""+tabMode.getRowCount());
                emptTeks();
            } 
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,nicu,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        ChkInput.setSelected(true);
        isForm(); 
        emptTeks();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()>-1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else {
                if(akses.getkode().equals(tbObat.getValueAt(tbObat.getSelectedRow(),28).toString())){
                    hapus();
                }else{
                    JOptionPane.showMessageDialog(null,"Harus salah satu petugas sesuai user login..!!");
                }
            }
        }else{
            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        }   
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
            Valid.textKosong(btnPetugas,"DPJP/Dokter Jaga/IGD");
        }else{  
            if(tbObat.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else {
                    if(akses.getkode().equals(tbObat.getValueAt(tbObat.getSelectedRow(),28).toString())){
                        ganti();
                    }else{
                        JOptionPane.showMessageDialog(null,"Harus salah satu petugas sesuai user login..!!");
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
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            try{
                htmlContent = new StringBuilder();
                htmlContent.append(                             
                    "<tr class='isi'>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>No.Rawat</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>No.RM</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Nama Pasien</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Tgl.Lahir</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>J.K.</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Tanggal</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Gangguan Sirkulasi Atau Pre Dan Pasca Operasi (Syok Hypovolemic)</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Hypertensi Emergency</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>HR 60x/menit (Tiidak Stabil Hasil EKG Gambaran Mengancam Nyawa)</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Gagal Jantung Acute</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Menggunakan Inotropik / Vasoaktif Gent</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>MAP < 60 mmHg</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>R <8 x/ menit > 25 x/menit</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Trauma Thorax / Peumothorax</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Oxigenisasi Tidak Cukup Dari Hasil AGD</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Kesadaran Dengan GCS >= 7</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Temperatur <35 C / >38 C</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Trauma Kepala Sedang - Berat</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Kejang Yang Tidak Memerlukan Ventilator / Cerebro Vasculer / Neoromusculer / Infeksi Syaraf</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Gangguan Elektrolit (Na, Ca,CI, Mg, Cal) & Asam Basa</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Hypeglikemia & Hypoglikemia, Ketoasidosis Metabolic</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Pendarahan Saluran Pencernaan Tanpa Hypotensi & Repon Dengan Cairan</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Pengobatan Keracunan</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Penyulit Pasca Pembedahan</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Pasca Pembedahan Hemodinamik Stabil Tetapi Masih Perlu Resusitasi Cairan</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Gangguan Imunologi (Reaksi Alergi, Steven Jhonson) dll</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>DIC, Anemia Berat, Reaksi Penolakan Transfusi Darah</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Semua Infeksi Yang Menyebabkan Penurunan Kesadaran & Tidak Memerlukan Ventilator</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>NIP/Kode Dokter</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>DPJP/Dokter Jaga/IGD</b></td>"+
                    "</tr>"
                );
                
                for (i = 0; i < tabMode.getRowCount(); i++) {
                    htmlContent.append(
                        "<tr class='isi'>"+
                           "<td valign='top'>"+tbObat.getValueAt(i,0).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,1).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,2).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,3).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,4).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,5).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,6).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,7).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,8).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,9).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,10).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,11).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,12).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,13).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,14).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,15).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,16).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,17).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,18).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,19).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,20).toString()+"</td>"+ 
                            "<td valign='top'>"+tbObat.getValueAt(i,21).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,22).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,23).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,24).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,25).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,26).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,27).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,28).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,29).toString()+"</td>"+
                        "</tr>");
                }
                
                LoadHTML.setText(
                    "<html>"+
                      "<table width='3500px' border='0' align='center' cellpadding='1px' cellspacing='0' class='tbl_form'>"+
                       htmlContent.toString()+
                      "</table>"+
                    "</html>"
                );

                File g = new File("file2.css");            
                BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                bg.write(
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
                bg.close();

                File f = new File("DataChecklistKriteriaMasukHCU.html");            
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));            
                bw.write(LoadHTML.getText().replaceAll("<head>","<head>"+
                            "<link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" />"+
                            "<table width='3500px' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                "<tr class='isi2'>"+
                                    "<td valign='top' align='center'>"+
                                        "<font size='4' face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                                        akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br>"+
                                        akses.getkontakrs()+", E-mail : "+akses.getemailrs()+"<br><br>"+
                                        "<font size='2' face='Tahoma'>DATA CHECK LIST KRITERIA MASUK HCU<br><br></font>"+        
                                    "</td>"+
                               "</tr>"+
                            "</table>")
                );
                bw.close();                         
                Desktop.getDesktop().browse(f.toURI());

            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }
        }
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

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void MnKriteriaMasukHCUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnKriteriaMasukHCUActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),28).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbObat.getValueAt(tbObat.getSelectedRow(),29).toString()+"\nID "+(finger.equals("")?tbObat.getValueAt(tbObat.getSelectedRow(),28).toString():finger)+"\n"+Tanggal.getSelectedItem()); 
            Valid.MyReportqry("rptFormulirChecklistKriteriaMasukHCU.jasper","report","::[ Formulir Check List Kriteria Masuk HCU ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,checklist_kriteria_masuk_nicu.tanggal,"+
                    "checklist_kriteria_masuk_nicu.kardiologi1,checklist_kriteria_masuk_nicu.kardiologi2,checklist_kriteria_masuk_nicu.kardiologi3,"+
                    "checklist_kriteria_masuk_nicu.kardiologi4,checklist_kriteria_masuk_nicu.kardiologi5,checklist_kriteria_masuk_nicu.kardiologi6,"+
                    "checklist_kriteria_masuk_nicu.pernapasan1,checklist_kriteria_masuk_nicu.pernapasan2,checklist_kriteria_masuk_nicu.pernapasan3,"+
                    "checklist_kriteria_masuk_nicu.syaraf1,checklist_kriteria_masuk_nicu.syaraf2,checklist_kriteria_masuk_nicu.syaraf3,checklist_kriteria_masuk_nicu.syaraf4,"+
                    "checklist_kriteria_masuk_nicu.pencernaan1,checklist_kriteria_masuk_nicu.pencernaan2,checklist_kriteria_masuk_nicu.pencernaan3,"+
                    "checklist_kriteria_masuk_nicu.pencernaan4,checklist_kriteria_masuk_nicu.pembedahan1,checklist_kriteria_masuk_nicu.pembedahan2,"+
                    "checklist_kriteria_masuk_nicu.hematologi1,checklist_kriteria_masuk_nicu.hematologi2,checklist_kriteria_masuk_nicu.infeksi,"+
                    "checklist_kriteria_masuk_nicu.nik,pegawai.nama "+
                    "from checklist_kriteria_masuk_nicu inner join reg_periksa on checklist_kriteria_masuk_nicu.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join pegawai on pegawai.nik=checklist_kriteria_masuk_nicu.nik "+
                    "where checklist_kriteria_masuk_nicu.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"' and checklist_kriteria_masuk_nicu.tanggal='"+tbObat.getValueAt(tbObat.getSelectedRow(),5).toString()+"' ",param);
        }
    }//GEN-LAST:event_MnKriteriaMasukHCUActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

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
       Valid.pindah(evt,Tanggal,nadi);
    }//GEN-LAST:event_btnPetugasKeyPressed

    private void akraldinginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_akraldinginKeyPressed
        Valid.pindah(evt,nadi,crt);
    }//GEN-LAST:event_akraldinginKeyPressed

    private void crtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_crtKeyPressed
        Valid.pindah(evt,akraldingin,sianosis);
    }//GEN-LAST:event_crtKeyPressed

    private void nadiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nadiKeyPressed
        Valid.pindah(evt,btnPetugas,akraldingin);
    }//GEN-LAST:event_nadiKeyPressed

    private void sianosisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sianosisKeyPressed
//        Valid.pindah(evt,Kardiologi3,Kardiologi5);
    }//GEN-LAST:event_sianosisKeyPressed

    private void retraksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_retraksiKeyPressed
//        Valid.pindah(evt,Kardiologi6,Pernapasan2);
    }//GEN-LAST:event_retraksiKeyPressed

    private void merintihKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_merintihKeyPressed
        Valid.pindah(evt,retraksi,rr);
    }//GEN-LAST:event_merintihKeyPressed

    private void rrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rrKeyPressed
//        Valid.pindah(evt,Pernapasan2,Syaraf1);
    }//GEN-LAST:event_rrKeyPressed

    private void nicuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nicuKeyPressed
//        Valid.pindah(evt,Hematologi2,BtnSimpan);
    }//GEN-LAST:event_nicuKeyPressed

    private void spo2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spo2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_spo2KeyPressed

    private void o2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_o2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_o2KeyPressed

    private void kesadaranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kesadaranKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kesadaranKeyPressed

    private void akiviasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_akiviasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_akiviasKeyPressed

    private void tonusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tonusKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tonusKeyPressed

    private void refleksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_refleksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_refleksiKeyPressed

    private void kejangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kejangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kejangKeyPressed

    private void distensiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_distensiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_distensiKeyPressed

    private void diareKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_diareKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_diareKeyPressed

    private void belumbabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_belumbabKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_belumbabKeyPressed

    private void tidakminumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tidakminumKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tidakminumKeyPressed

    private void muntahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_muntahKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_muntahKeyPressed

    private void kelainananatomiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kelainananatomiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kelainananatomiKeyPressed

    private void hipertermiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hipertermiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_hipertermiKeyPressed

    private void hipotermiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hipotermiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_hipotermiKeyPressed

    private void kuningKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kuningKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kuningKeyPressed

    private void bblrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bblrKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_bblrKeyPressed

    private void laboratoriumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_laboratoriumKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_laboratoriumKeyPressed

    private void bakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bakKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_bakKeyPressed

    private void babdarahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_babdarahKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_babdarahKeyPressed

    private void talipusatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_talipusatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_talipusatKeyPressed

    private void tandainfeksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tandainfeksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tandainfeksiKeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMChecklistKriteriaMasukNICU dialog = new RMChecklistKriteriaMasukNICU(new javax.swing.JFrame(), true);
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
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.PanelBiasa FormInput;
    private widget.TextBox JK;
    private widget.TextBox KodePetugas;
    private widget.Label LCount;
    private widget.editorpane LoadHTML;
    private javax.swing.JMenuItem MnKriteriaMasukHCU;
    private widget.TextBox NamaPetugas;
    private javax.swing.JPanel PanelInput;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Tanggal Tanggal;
    private widget.TextBox TglLahir;
    private widget.ComboBox akivias;
    private widget.ComboBox akraldingin;
    private widget.ComboBox babdarah;
    private widget.ComboBox bak;
    private widget.ComboBox bblr;
    private widget.ComboBox belumbab;
    private widget.Button btnPetugas;
    private widget.ComboBox crt;
    private widget.ComboBox diare;
    private widget.ComboBox distensi;
    private widget.ComboBox hipertermi;
    private widget.ComboBox hipotermi;
    private widget.InternalFrame internalFrame1;
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
    private widget.Label jLabel16;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel23;
    private widget.Label jLabel4;
    private widget.Label jLabel5;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel57;
    private widget.Label jLabel6;
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
    private widget.Label jLabel8;
    private widget.Label jLabel88;
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
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private widget.ComboBox kejang;
    private widget.ComboBox kelainananatomi;
    private widget.ComboBox kesadaran;
    private widget.ComboBox kuning;
    private widget.ComboBox laboratorium;
    private widget.ComboBox merintih;
    private widget.ComboBox muntah;
    private widget.ComboBox nadi;
    private widget.ComboBox nicu;
    private widget.ComboBox o2;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ComboBox refleksi;
    private widget.ComboBox retraksi;
    private widget.ComboBox rr;
    private widget.ScrollPane scrollInput;
    private widget.ComboBox sianosis;
    private widget.ComboBox spo2;
    private widget.ComboBox talipusat;
    private widget.ComboBox tandainfeksi;
    private widget.Table tbObat;
    private widget.ComboBox tidakminum;
    private widget.ComboBox tonus;
    // End of variables declaration//GEN-END:variables
    
    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            if(TCari.getText().trim().equals("")){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,checklist_kriteria_masuk_nicu.tanggal,"+
                    "checklist_kriteria_masuk_nicu.nadi,checklist_kriteria_masuk_nicu.crt,checklist_kriteria_masuk_nicu.akral_dingin,"+
                    "checklist_kriteria_masuk_nicu.sianosis,checklist_kriteria_masuk_nicu.retraksi,checklist_kriteria_masuk_nicu.merintih,"+
                    "checklist_kriteria_masuk_nicu.rr,checklist_kriteria_masuk_nicu.spo2,checklist_kriteria_masuk_nicu.butuh_oksigen,"+
                    "checklist_kriteria_masuk_nicu.kesadaran_menurun,checklist_kriteria_masuk_nicu.akivias_menurun,checklist_kriteria_masuk_nicu.tonus_menurun,"+
                    "checklist_kriteria_masuk_nicu.refleks_menurun,checklist_kriteria_masuk_nicu.kejang,checklist_kriteria_masuk_nicu.ada_muntah,"+
                    "checklist_kriteria_masuk_nicu.tidak_mau_minum,checklist_kriteria_masuk_nicu.diare,checklist_kriteria_masuk_nicu.belum_bab,"+
                    "checklist_kriteria_masuk_nicu.ada_distensi,checklist_kriteria_masuk_nicu.ada_kelainan_anatomi,checklist_kriteria_masuk_nicu.hipotermi,"+
                    "checklist_kriteria_masuk_nicu.hipertermi,checklist_kriteria_masuk_nicu.kuning,checklist_kriteria_masuk_nicu.bblr,"+
                    "checklist_kriteria_masuk_nicu.laboratorium,checklist_kriteria_masuk_nicu.bak_warna_merah,checklist_kriteria_masuk_nicu.bab_darah,"+
                    "checklist_kriteria_masuk_nicu.tali_pusat_merah,checklist_kriteria_masuk_nicu.tanda_infeksi,checklist_kriteria_masuk_nicu.tindaklanjut,"+
                    "checklist_kriteria_masuk_nicu.nik,pegawai.nama "+
                    "from checklist_kriteria_masuk_nicu inner join reg_periksa on checklist_kriteria_masuk_nicu.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join pegawai on pegawai.nik=checklist_kriteria_masuk_nicu.nik "+
                    "where checklist_kriteria_masuk_nicu.tanggal between ? and ? order by checklist_kriteria_masuk_nicu.tanggal ");
            }else{
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,checklist_kriteria_masuk_nicu.tanggal,"+
                    "checklist_kriteria_masuk_nicu.nadi,checklist_kriteria_masuk_nicu.crt,checklist_kriteria_masuk_nicu.akral_dingin,"+
                    "checklist_kriteria_masuk_nicu.sianosis,checklist_kriteria_masuk_nicu.retraksi,checklist_kriteria_masuk_nicu.merintih,"+
                    "checklist_kriteria_masuk_nicu.rr,checklist_kriteria_masuk_nicu.spo2,checklist_kriteria_masuk_nicu.butuh_oksigen,"+
                    "checklist_kriteria_masuk_nicu.kesadaran_menurun,checklist_kriteria_masuk_nicu.akivias_menurun,checklist_kriteria_masuk_nicu.tonus_menurun,"+
                    "checklist_kriteria_masuk_nicu.refleks_menurun,checklist_kriteria_masuk_nicu.kejang,checklist_kriteria_masuk_nicu.ada_muntah,"+
                    "checklist_kriteria_masuk_nicu.tidak_mau_minum,checklist_kriteria_masuk_nicu.diare,checklist_kriteria_masuk_nicu.belum_bab,"+
                    "checklist_kriteria_masuk_nicu.ada_distensi,checklist_kriteria_masuk_nicu.ada_kelainan_anatomi,checklist_kriteria_masuk_nicu.hipotermi,"+
                    "checklist_kriteria_masuk_nicu.hipertermi,checklist_kriteria_masuk_nicu.kuning,checklist_kriteria_masuk_nicu.bblr,"+
                    "checklist_kriteria_masuk_nicu.laboratorium,checklist_kriteria_masuk_nicu.bak_warna_merah,checklist_kriteria_masuk_nicu.bab_darah,"+
                    "checklist_kriteria_masuk_nicu.tali_pusat_merah,checklist_kriteria_masuk_nicu.tanda_infeksi,checklist_kriteria_masuk_nicu.tindaklanjut,"+
                    "checklist_kriteria_masuk_nicu.nik,pegawai.nama "+
                    "from checklist_kriteria_masuk_nicu inner join reg_periksa on checklist_kriteria_masuk_nicu.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join pegawai on pegawai.nik=checklist_kriteria_masuk_nicu.nik "+
                    "where checklist_kriteria_masuk_nicu.tanggal between ? and ? and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or "+
                    "pasien.nm_pasien like ? or pegawai.nama like ? or checklist_kriteria_masuk_nicu.nik like ?) order by checklist_kriteria_masuk_nicu.tanggal ");
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
                        rs.getString("tanggal"),rs.getString("nadi"),rs.getString("crt"),rs.getString("akral_dingin"),rs.getString("sianosis"),
                        rs.getString("retraksi"),rs.getString("merintih"),rs.getString("rr"),rs.getString("spo2"),rs.getString("butuh_oksigen"),
                        rs.getString("kesadaran_menurun"),rs.getString("akivias_menurun"),rs.getString("tonus_menurun"),rs.getString("refleks_menurun"),rs.getString("kejang"),
                        rs.getString("ada_muntah"),rs.getString("tidak_mau_minum"),rs.getString("diare"),rs.getString("belum_bab"),rs.getString("ada_distensi"),
                        rs.getString("ada_kelainan_anatomi"),rs.getString("hipotermi"),rs.getString("hipertermi"),
                        rs.getString("kuning"),rs.getString("bblr"),rs.getString("laboratorium"),rs.getString("bak_warna_merah"),rs.getString("bab_darah"),
                        rs.getString("tali_pusat_merah"),rs.getString("tanda_infeksi"),rs.getString("tindaklanjut"),
                        rs.getString("nik"),rs.getString("nama")
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
        nadi.setSelectedItem("Tidak");
        akraldingin.setSelectedItem("Tidak");
        crt.setSelectedItem("Tidak");
        sianosis.setSelectedItem("Tidak");
        retraksi.setSelectedItem("Tidak");
        merintih.setSelectedItem("Tidak");
        rr.setSelectedItem("Tidak");
        nicu.setSelectedItem("Tidak");
        Tanggal.setDate(new Date());
        nadi.requestFocus();
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            JK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            nadi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            akraldingin.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            crt.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            sianosis.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            retraksi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            merintih.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            rr.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            nicu.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),27).toString());
            Valid.SetTgl2(Tanggal,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
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
        ChkInput.setSelected(true);
        isForm();
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,internalFrame1.getHeight()-182));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
//        BtnSimpan.setEnabled(akses.getchecklist_kriteria_masuk_nicu());
//        BtnHapus.setEnabled(akses.getchecklist_kriteria_masuk_nicu());
//        BtnEdit.setEnabled(akses.getchecklist_kriteria_masuk_nicu());
//        BtnPrint.setEnabled(akses.getchecklist_kriteria_masuk_nicu()); 
//        if(akses.getjml2()>=1){
//            btnPetugas.setEnabled(false);
//            KodePetugas.setText(akses.getkode());
//            NamaPetugas.setText(pegawai.tampil3(akses.getkode()));
//        }
    }

    private void ganti() {
        if(Sequel.mengedittf("checklist_kriteria_masuk_nicu","no_rawat=? and tanggal=?","no_rawat=?,tanggal=?,kardiologi1=?,kardiologi2=?,kardiologi3=?,kardiologi4=?,kardiologi5=?,kardiologi6=?,pernapasan1=?,pernapasan2=?,"+
                "pernapasan3=?,syaraf1=?,syaraf2=?,syaraf3=?,syaraf4=?,pencernaan1=?,pencernaan2=?,pencernaan3=?,pencernaan4=?,pembedahan1=?,pembedahan2=?,hematologi1=?,hematologi2=?,infeksi=?,nik=?",27,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),nadi.getSelectedItem().toString(),
//                Kardiologi2.getSelectedItem().toString(),Kardiologi3.getSelectedItem().toString(),Kardiologi4.getSelectedItem().toString(),Kardiologi5.getSelectedItem().toString(),
//                Kardiologi6.getSelectedItem().toString(),Pernapasan1.getSelectedItem().toString(),Pernapasan2.getSelectedItem().toString(),Pernapasan3.getSelectedItem().toString(),
//                Syaraf1.getSelectedItem().toString(),Syaraf2.getSelectedItem().toString(),Syaraf3.getSelectedItem().toString(),Syaraf4.getSelectedItem().toString(),
//                Pencernaan1.getSelectedItem().toString(),Pencernaan2.getSelectedItem().toString(),Pencernaan3.getSelectedItem().toString(),Pencernaan4.getSelectedItem().toString(),
//                Pembedahan1.getSelectedItem().toString(),Pembedahan2.getSelectedItem().toString(),Hematologi1.getSelectedItem().toString(),Hematologi2.getSelectedItem().toString(),
                nicu.getSelectedItem().toString(),KodePetugas.getText(),tbObat.getValueAt(tbObat.getSelectedRow(),0).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),5).toString()
        })==true){
            emptTeks();
        }
    }

    private void hapus() {
        if(Sequel.queryu2tf("delete from checklist_kriteria_masuk_nicu where no_rawat=? and tanggal=?",2,new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),5).toString()
        })==true){
            tabMode.removeRow(tbObat.getSelectedRow());
            LCount.setText(""+tabMode.getRowCount());
            emptTeks();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }
}
