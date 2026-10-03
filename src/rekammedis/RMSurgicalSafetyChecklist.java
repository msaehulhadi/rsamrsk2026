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
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;


/**
 *
 * @author perpustakaan
 */
public final class RMSurgicalSafetyChecklist extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0,pilihan=0;    
    private DlgCariPetugas petugas=new DlgCariPetugas(null,false);
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private String finger="",finger2="";
    private StringBuilder htmlContent;
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMSurgicalSafetyChecklist(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.RM","Nama Pasien","Tgl.Lahir","J.K.","Tanggal","SN/CN","Tindakan","Kode Dokter Bedah","Nama Dokter Bedah",
            "Kode Dokter Anest","Nama Dokter Anestesi","Identitas","Alergi","Area Operasi","Resiko Aspirasi","Antisipasi Resiko Aspirasi",
            "Kehilangan Darah","Jalur IV Line","Antisipasi Resiko Kehilangan Darah","Alat & Obat","Rencana Antisipasi Ketidaklengkapan Alat & Obat",
            "NIP OK","Petugas Ruang OK"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 24; i++) {
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
            }else if(i==6){
                column.setPreferredWidth(90);
            }else if(i==7){
                column.setPreferredWidth(160);
            }else if(i==8){
                column.setPreferredWidth(100);
            }else if(i==9){
                column.setPreferredWidth(160);
            }else if(i==10){
                column.setPreferredWidth(100);
            }else if(i==11){
                column.setPreferredWidth(150);
            }else if(i==12){
                column.setPreferredWidth(53);
            }else if(i==13){
                column.setPreferredWidth(130);
            }else if(i==14){
                column.setPreferredWidth(88);
            }else if(i==15){
                column.setPreferredWidth(83);
            }else if(i==16){
                column.setPreferredWidth(145);
            }else if(i==17){
                column.setPreferredWidth(95);
            }else if(i==18){
                column.setPreferredWidth(110);
            }else if(i==19){
                column.setPreferredWidth(180);
            }else if(i==20){
                column.setPreferredWidth(77);
            }else if(i==21){
                column.setPreferredWidth(250);
            }else if(i==22){
                column.setPreferredWidth(90);
            }else if(i==23){
                column.setPreferredWidth(150);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        SNCN.setDocument(new batasInput((byte)25).getKata(SNCN));
        Tindakan.setDocument(new batasInput((byte)50).getKata(Tindakan));
        Alergi.setDocument(new batasInput((byte)30).getKata(Alergi));
        AntisipasiResikoAspirasi.setDocument(new batasInput((byte)50).getKata(AntisipasiResikoAspirasi));
        JalurIVLine.setDocument(new batasInput((byte)30).getKata(JalurIVLine));
        RencanaAntisipasiKehilanganDarah.setDocument(new batasInput((byte)50).getKata(RencanaAntisipasiKehilanganDarah));
        RencanaAntisipasiKesiapanAlat.setDocument(new batasInput((byte)50).getKata(RencanaAntisipasiKesiapanAlat));
        
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
        
        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(petugas.getTable().getSelectedRow()!= -1){     
                    KdPetugasOK.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                    NmPetugasOK.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                    btnPetugasOK.requestFocus();
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
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){      
                    if(pilihan==1){
                        KodeDokterBedah.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                        NamaDokterBedah.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                        btnDokterBedah.requestFocus();
                    }else if(pilihan==2){
                        KodeDokterAnestesi.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                        NamaDokterAnestesi.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                        btnDokterAnestesi.requestFocus();
                    }   
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
        MnSignInSebelumAnestesi = new javax.swing.JMenuItem();
        LoadHTML = new widget.editorpane();
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
        jLabel20 = new widget.Label();
        SNCN = new widget.TextBox();
        jLabel22 = new widget.Label();
        jLabel23 = new widget.Label();
        KodeDokterBedah = new widget.TextBox();
        NamaDokterBedah = new widget.TextBox();
        btnDokterBedah = new widget.Button();
        btnDokterAnestesi = new widget.Button();
        NamaDokterAnestesi = new widget.TextBox();
        KodeDokterAnestesi = new widget.TextBox();
        jLabel24 = new widget.Label();
        jLabel25 = new widget.Label();
        jLabel5 = new widget.Label();
        Tindakan = new widget.TextBox();
        jSeparator1 = new javax.swing.JSeparator();
        Scroll1 = new widget.ScrollPane();
        internalFrame2 = new widget.InternalFrame();
        PanelInput1 = new javax.swing.JPanel();
        scrollInput1 = new widget.ScrollPane();
        FormInput1 = new widget.PanelBiasa();
        LapanganOperasi = new widget.ComboBox();
        jLabel51 = new widget.Label();
        AreaOperasi = new widget.ComboBox();
        ResikoKehilanganDarah = new widget.ComboBox();
        jLabel57 = new widget.Label();
        jLabel58 = new widget.Label();
        jLabel62 = new widget.Label();
        JalurIVLine = new widget.TextBox();
        jLabel26 = new widget.Label();
        KdPetugasOK = new widget.TextBox();
        NmPetugasOK = new widget.TextBox();
        btnPetugasOK = new widget.Button();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel28 = new widget.Label();
        Alergi = new widget.TextBox();
        jLabel29 = new widget.Label();
        AntisipasiResikoAspirasi = new widget.TextBox();
        ResikoAspirasi = new widget.ComboBox();
        RencanaAntisipasiKehilanganDarah = new widget.TextBox();
        jLabel59 = new widget.Label();
        KesiapanAlatAnes = new widget.ComboBox();
        jLabel31 = new widget.Label();
        RencanaAntisipasiKesiapanAlat = new widget.TextBox();
        jLabel52 = new widget.Label();
        jLabel32 = new widget.Label();
        jLabel27 = new widget.Label();
        jLabel53 = new widget.Label();
        BtnSimpan1 = new widget.Button();
        PanelInput2 = new javax.swing.JPanel();
        scrollInput2 = new widget.ScrollPane();
        FormInput2 = new widget.PanelBiasa();
        jLabel34 = new widget.Label();
        KdPetugasOK2 = new widget.TextBox();
        NmPetugasOK2 = new widget.TextBox();
        btnPetugasOK2 = new widget.Button();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jLabel37 = new widget.Label();
        jLabel38 = new widget.Label();
        jLabel39 = new widget.Label();
        VerbalKasa = new widget.ComboBox();
        jLabel40 = new widget.Label();
        jLabel41 = new widget.Label();
        VerbalAlatTajam = new widget.ComboBox();
        jLabel42 = new widget.Label();
        KelengkapanSpesimenLabel = new widget.ComboBox();
        KelengkapanSpesimenFormulir = new widget.ComboBox();
        jLabel46 = new widget.Label();
        jLabel69 = new widget.Label();
        PerhatianUtamaFasePemulihan = new widget.TextBox();
        PerhatianUtamaFasePemulihan1 = new widget.TextBox();
        jLabel65 = new widget.Label();
        PanelInput3 = new javax.swing.JPanel();
        scrollInput3 = new widget.ScrollPane();
        FormInput3 = new widget.PanelBiasa();
        jLabel54 = new widget.Label();
        PengenalanPetugas = new widget.ComboBox();
        jLabel30 = new widget.Label();
        PemberianAntibiotik = new widget.ComboBox();
        KdPetugasOK1 = new widget.TextBox();
        NmPetugasOK1 = new widget.TextBox();
        btnPetugasOK1 = new widget.Button();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel33 = new widget.Label();
        AntisipisasiKehialnganDarah = new widget.TextBox();
        jLabel55 = new widget.Label();
        VerbalTindakan = new widget.ComboBox();
        jLabel35 = new widget.Label();
        jLabel36 = new widget.Label();
        VerbalArea = new widget.ComboBox();
        PenayanganRadiologi = new widget.ComboBox();
        jLabel60 = new widget.Label();
        Masalahpadaperalatan = new widget.ComboBox();
        jLabel61 = new widget.Label();
        PemakaianImplant = new widget.ComboBox();
        jLabel63 = new widget.Label();
        VerbalIdentitas = new widget.ComboBox();
        AdaHalKhusus = new widget.ComboBox();
        jLabel64 = new widget.Label();
        NamaAntibiotikDIberikan = new widget.TextBox();
        jLabel43 = new widget.Label();
        jLabel44 = new widget.Label();
        jLabel45 = new widget.Label();
        jLabel56 = new widget.Label();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnSignInSebelumAnestesi.setBackground(new java.awt.Color(255, 255, 254));
        MnSignInSebelumAnestesi.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnSignInSebelumAnestesi.setForeground(new java.awt.Color(50, 50, 50));
        MnSignInSebelumAnestesi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnSignInSebelumAnestesi.setText("Formulir Sign-In Sebelum Tindakan Anestesi");
        MnSignInSebelumAnestesi.setName("MnSignInSebelumAnestesi"); // NOI18N
        MnSignInSebelumAnestesi.setPreferredSize(new java.awt.Dimension(270, 26));
        MnSignInSebelumAnestesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnSignInSebelumAnestesiActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnSignInSebelumAnestesi);

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ SURGICAL SAFETY CHECKLIST ]::", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(50, 50, 50))); // NOI18N
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
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-06-2025" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-06-2025" }));
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
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 500));
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
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 100));

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 100));
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
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-06-2025 12:10:27" }));
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

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText("SIGN IN");
        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(350, 120, 120, 23);

        SNCN.setHighlighter(null);
        SNCN.setName("SNCN"); // NOI18N
        SNCN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SNCNKeyPressed(evt);
            }
        });
        FormInput.add(SNCN);
        SNCN.setBounds(264, 40, 120, 23);

        jLabel22.setText("SN/CN :");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(210, 40, 50, 23);

        jLabel23.setText("Dokter Bedah :");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(390, 40, 91, 23);

        KodeDokterBedah.setEditable(false);
        KodeDokterBedah.setHighlighter(null);
        KodeDokterBedah.setName("KodeDokterBedah"); // NOI18N
        FormInput.add(KodeDokterBedah);
        KodeDokterBedah.setBounds(485, 40, 97, 23);

        NamaDokterBedah.setEditable(false);
        NamaDokterBedah.setName("NamaDokterBedah"); // NOI18N
        FormInput.add(NamaDokterBedah);
        NamaDokterBedah.setBounds(584, 40, 175, 23);

        btnDokterBedah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokterBedah.setMnemonic('2');
        btnDokterBedah.setToolTipText("ALt+2");
        btnDokterBedah.setName("btnDokterBedah"); // NOI18N
        btnDokterBedah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokterBedahActionPerformed(evt);
            }
        });
        btnDokterBedah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDokterBedahKeyPressed(evt);
            }
        });
        FormInput.add(btnDokterBedah);
        btnDokterBedah.setBounds(761, 40, 28, 23);

        btnDokterAnestesi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokterAnestesi.setMnemonic('2');
        btnDokterAnestesi.setToolTipText("ALt+2");
        btnDokterAnestesi.setName("btnDokterAnestesi"); // NOI18N
        btnDokterAnestesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokterAnestesiActionPerformed(evt);
            }
        });
        btnDokterAnestesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDokterAnestesiKeyPressed(evt);
            }
        });
        FormInput.add(btnDokterAnestesi);
        btnDokterAnestesi.setBounds(761, 70, 28, 23);

        NamaDokterAnestesi.setEditable(false);
        NamaDokterAnestesi.setName("NamaDokterAnestesi"); // NOI18N
        FormInput.add(NamaDokterAnestesi);
        NamaDokterAnestesi.setBounds(584, 70, 175, 23);

        KodeDokterAnestesi.setEditable(false);
        KodeDokterAnestesi.setHighlighter(null);
        KodeDokterAnestesi.setName("KodeDokterAnestesi"); // NOI18N
        FormInput.add(KodeDokterAnestesi);
        KodeDokterAnestesi.setBounds(485, 70, 97, 23);

        jLabel24.setText("Dokter Anestesi :");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(390, 70, 91, 23);

        jLabel25.setText("Tindakan :");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(0, 70, 75, 23);

        jLabel5.setText(":");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(0, 10, 75, 23);

        Tindakan.setHighlighter(null);
        Tindakan.setName("Tindakan"); // NOI18N
        Tindakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TindakanKeyPressed(evt);
            }
        });
        FormInput.add(Tindakan);
        Tindakan.setBounds(79, 70, 305, 23);

        jSeparator1.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator1.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator1.setName("jSeparator1"); // NOI18N
        FormInput.add(jSeparator1);
        jSeparator1.setBounds(0, 100, 810, 1);

        scrollInput.setViewportView(FormInput);

        PanelInput.add(scrollInput, java.awt.BorderLayout.PAGE_START);

        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);
        Scroll1.setPreferredSize(new java.awt.Dimension(452, 200));

        internalFrame2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data MONITORING ]::", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame2.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        PanelInput1.setName("PanelInput1"); // NOI18N
        PanelInput1.setOpaque(false);
        PanelInput1.setPreferredSize(new java.awt.Dimension(600, 100));
        PanelInput1.setLayout(new java.awt.BorderLayout(1, 1));

        scrollInput1.setName("scrollInput1"); // NOI18N
        scrollInput1.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput1.setBackground(new java.awt.Color(250, 255, 245));
        FormInput1.setBorder(null);
        FormInput1.setName("FormInput1"); // NOI18N
        FormInput1.setPreferredSize(new java.awt.Dimension(100, 500));
        FormInput1.setLayout(null);

        LapanganOperasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        LapanganOperasi.setName("LapanganOperasi"); // NOI18N
        LapanganOperasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LapanganOperasiKeyPressed(evt);
            }
        });
        FormInput1.add(LapanganOperasi);
        LapanganOperasi.setBounds(440, 40, 120, 23);

        jLabel51.setText("Apakah lapangan operasi sudah diberi tanda?");
        jLabel51.setName("jLabel51"); // NOI18N
        FormInput1.add(jLabel51);
        jLabel51.setBounds(20, 70, 410, 23);

        AreaOperasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Tidak Ada", "Tidak Diperlukan" }));
        AreaOperasi.setName("AreaOperasi"); // NOI18N
        AreaOperasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AreaOperasiKeyPressed(evt);
            }
        });
        FormInput1.add(AreaOperasi);
        AreaOperasi.setBounds(440, 70, 120, 23);

        ResikoKehilanganDarah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ada" }));
        ResikoKehilanganDarah.setName("ResikoKehilanganDarah"); // NOI18N
        ResikoKehilanganDarah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ResikoKehilanganDarahKeyPressed(evt);
            }
        });
        FormInput1.add(ResikoKehilanganDarah);
        ResikoKehilanganDarah.setBounds(440, 220, 100, 23);

        jLabel57.setText("Resiko Kehilangan Darah > 500 ml (7 ml/Kg Berat Badan Untuk Anak) :");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput1.add(jLabel57);
        jLabel57.setBounds(80, 220, 350, 23);

        jLabel58.setText("Apakah alat pulse oximeter yang terpasang pada pasein berfungsi dengan baik?");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput1.add(jLabel58);
        jLabel58.setBounds(20, 130, 410, 23);

        jLabel62.setText("Jika Ada, Jalur IV Line :");
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput1.add(jLabel62);
        jLabel62.setBounds(310, 250, 120, 23);

        JalurIVLine.setHighlighter(null);
        JalurIVLine.setName("JalurIVLine"); // NOI18N
        JalurIVLine.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JalurIVLineKeyPressed(evt);
            }
        });
        FormInput1.add(JalurIVLine);
        JalurIVLine.setBounds(440, 250, 130, 23);

        jLabel26.setText("Penata Anestesi :");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput1.add(jLabel26);
        jLabel26.setBounds(20, 350, 110, 23);

        KdPetugasOK.setEditable(false);
        KdPetugasOK.setHighlighter(null);
        KdPetugasOK.setName("KdPetugasOK"); // NOI18N
        FormInput1.add(KdPetugasOK);
        KdPetugasOK.setBounds(140, 350, 110, 23);

        NmPetugasOK.setEditable(false);
        NmPetugasOK.setName("NmPetugasOK"); // NOI18N
        FormInput1.add(NmPetugasOK);
        NmPetugasOK.setBounds(250, 350, 300, 23);

        btnPetugasOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPetugasOK.setMnemonic('2');
        btnPetugasOK.setToolTipText("ALt+2");
        btnPetugasOK.setName("btnPetugasOK"); // NOI18N
        btnPetugasOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPetugasOKActionPerformed(evt);
            }
        });
        btnPetugasOK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPetugasOKKeyPressed(evt);
            }
        });
        FormInput1.add(btnPetugasOK);
        btnPetugasOK.setBounds(560, 350, 28, 23);

        jSeparator3.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator3.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator3.setName("jSeparator3"); // NOI18N
        FormInput1.add(jSeparator3);
        jSeparator3.setBounds(0, 340, 810, 1);

        jSeparator4.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator4.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator4.setName("jSeparator4"); // NOI18N
        FormInput1.add(jSeparator4);
        jSeparator4.setBounds(0, 340, 810, 1);

        jLabel28.setText("Apakah pasien memiliki riwayat penyakit Alergi/HIV/Infeksi/Hepatitis/TB?");
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput1.add(jLabel28);
        jLabel28.setBounds(10, 160, 420, 23);

        Alergi.setHighlighter(null);
        Alergi.setName("Alergi"); // NOI18N
        Alergi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlergiKeyPressed(evt);
            }
        });
        FormInput1.add(Alergi);
        Alergi.setBounds(440, 160, 130, 23);

        jLabel29.setText("Kesulitan menjaga Nafas atau Resiko Aspirasi :");
        jLabel29.setName("jLabel29"); // NOI18N
        FormInput1.add(jLabel29);
        jLabel29.setBounds(160, 190, 270, 23);

        AntisipasiResikoAspirasi.setHighlighter(null);
        AntisipasiResikoAspirasi.setName("AntisipasiResikoAspirasi"); // NOI18N
        AntisipasiResikoAspirasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AntisipasiResikoAspirasiKeyPressed(evt);
            }
        });
        FormInput1.add(AntisipasiResikoAspirasi);
        AntisipasiResikoAspirasi.setBounds(440, 190, 130, 23);

        ResikoAspirasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Tidak Ada" }));
        ResikoAspirasi.setName("ResikoAspirasi"); // NOI18N
        ResikoAspirasi.setPreferredSize(new java.awt.Dimension(90, 20));
        ResikoAspirasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ResikoAspirasiKeyPressed(evt);
            }
        });
        FormInput1.add(ResikoAspirasi);
        ResikoAspirasi.setBounds(440, 130, 120, 23);

        RencanaAntisipasiKehilanganDarah.setHighlighter(null);
        RencanaAntisipasiKehilanganDarah.setName("RencanaAntisipasiKehilanganDarah"); // NOI18N
        RencanaAntisipasiKehilanganDarah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RencanaAntisipasiKehilanganDarahKeyPressed(evt);
            }
        });
        FormInput1.add(RencanaAntisipasiKehilanganDarah);
        RencanaAntisipasiKehilanganDarah.setBounds(440, 280, 130, 23);

        jLabel59.setText("Apakah mesin anestesi dan pramedikasi sudah diperiksa?");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput1.add(jLabel59);
        jLabel59.setBounds(150, 100, 280, 23);

        KesiapanAlatAnes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lengkap", "Pulsa Oximetri", "Tidak Lengkap" }));
        KesiapanAlatAnes.setName("KesiapanAlatAnes"); // NOI18N
        KesiapanAlatAnes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KesiapanAlatAnesKeyPressed(evt);
            }
        });
        FormInput1.add(KesiapanAlatAnes);
        KesiapanAlatAnes.setBounds(440, 100, 120, 23);

        jLabel31.setText("Bila Tidak Lengkap, Rencana Antisipasi :");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput1.add(jLabel31);
        jLabel31.setBounds(220, 310, 210, 23);

        RencanaAntisipasiKesiapanAlat.setHighlighter(null);
        RencanaAntisipasiKesiapanAlat.setName("RencanaAntisipasiKesiapanAlat"); // NOI18N
        RencanaAntisipasiKesiapanAlat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RencanaAntisipasiKesiapanAlatKeyPressed(evt);
            }
        });
        FormInput1.add(RencanaAntisipasiKesiapanAlat);
        RencanaAntisipasiKesiapanAlat.setBounds(440, 310, 130, 23);

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel52.setText("Apakah pasien telah dikonfirmasi nama lapangan operasi, prosedur dan inform consent?");
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput1.add(jLabel52);
        jLabel52.setBounds(10, 40, 450, 23);

        jLabel32.setText("Jika Ada Resiko Kehilangan Darah, Rencana Antisipasi :");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput1.add(jLabel32);
        jLabel32.setBounds(50, 280, 380, 23);

        jLabel27.setText(":");
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput1.add(jLabel27);
        jLabel27.setBounds(0, 350, 143, 23);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setText("SEBELUM INDUKASI ANESTESI (SIGN IN)");
        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput1.add(jLabel53);
        jLabel53.setBounds(10, 3, 570, 30);

        BtnSimpan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan1.setMnemonic('S');
        BtnSimpan1.setText("Simpan");
        BtnSimpan1.setToolTipText("Alt+S");
        BtnSimpan1.setGlassColor(new java.awt.Color(0, 204, 51));
        BtnSimpan1.setName("BtnSimpan1"); // NOI18N
        BtnSimpan1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpan1ActionPerformed(evt);
            }
        });
        BtnSimpan1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpan1KeyPressed(evt);
            }
        });
        FormInput1.add(BtnSimpan1);
        BtnSimpan1.setBounds(460, 390, 100, 30);

        scrollInput1.setViewportView(FormInput1);

        PanelInput1.add(scrollInput1, java.awt.BorderLayout.CENTER);

        internalFrame2.add(PanelInput1, java.awt.BorderLayout.LINE_START);

        PanelInput2.setName("PanelInput2"); // NOI18N
        PanelInput2.setOpaque(false);
        PanelInput2.setPreferredSize(new java.awt.Dimension(500, 100));
        PanelInput2.setLayout(new java.awt.BorderLayout(1, 1));

        scrollInput2.setName("scrollInput2"); // NOI18N
        scrollInput2.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput2.setBackground(new java.awt.Color(250, 255, 245));
        FormInput2.setBorder(null);
        FormInput2.setName("FormInput2"); // NOI18N
        FormInput2.setPreferredSize(new java.awt.Dimension(100, 500));
        FormInput2.setLayout(null);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText("Perawat Kamar Operasi");
        jLabel34.setName("jLabel34"); // NOI18N
        FormInput2.add(jLabel34);
        jLabel34.setBounds(0, 240, 130, 23);

        KdPetugasOK2.setEditable(false);
        KdPetugasOK2.setHighlighter(null);
        KdPetugasOK2.setName("KdPetugasOK2"); // NOI18N
        FormInput2.add(KdPetugasOK2);
        KdPetugasOK2.setBounds(130, 240, 110, 23);

        NmPetugasOK2.setEditable(false);
        NmPetugasOK2.setName("NmPetugasOK2"); // NOI18N
        FormInput2.add(NmPetugasOK2);
        NmPetugasOK2.setBounds(240, 240, 300, 23);

        btnPetugasOK2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPetugasOK2.setMnemonic('2');
        btnPetugasOK2.setToolTipText("ALt+2");
        btnPetugasOK2.setName("btnPetugasOK2"); // NOI18N
        btnPetugasOK2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPetugasOK2ActionPerformed(evt);
            }
        });
        btnPetugasOK2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPetugasOK2KeyPressed(evt);
            }
        });
        FormInput2.add(btnPetugasOK2);
        btnPetugasOK2.setBounds(540, 240, 28, 23);

        jSeparator8.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator8.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator8.setName("jSeparator8"); // NOI18N
        FormInput2.add(jSeparator8);
        jSeparator8.setBounds(0, 50, 810, 1);

        jSeparator9.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator9.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator9.setName("jSeparator9"); // NOI18N
        FormInput2.add(jSeparator9);
        jSeparator9.setBounds(0, 50, 810, 1);

        jSeparator10.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator10.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator10.setName("jSeparator10"); // NOI18N
        FormInput2.add(jSeparator10);
        jSeparator10.setBounds(0, 275, 810, 1);

        jSeparator11.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator11.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator11.setName("jSeparator11"); // NOI18N
        FormInput2.add(jSeparator11);
        jSeparator11.setBounds(0, 275, 810, 1);

        jLabel37.setText(":");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput2.add(jLabel37);
        jLabel37.setBounds(0, 310, 143, 23);

        jLabel38.setText("Perawat secara lisan manyampaikan Nama dari prosedur :");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput2.add(jLabel38);
        jLabel38.setBounds(0, 60, 420, 23);

        jLabel39.setText("Apakah instrumen habis pakai (kasa) dan jarum telah dihitung dan sesuai :");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput2.add(jLabel39);
        jLabel39.setBounds(0, 90, 420, 23);

        VerbalKasa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        VerbalKasa.setName("VerbalKasa"); // NOI18N
        VerbalKasa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                VerbalKasaKeyPressed(evt);
            }
        });
        FormInput2.add(VerbalKasa);
        VerbalKasa.setBounds(420, 90, 100, 23);

        jLabel40.setText("Jumlah kasa yang terpakai :");
        jLabel40.setName("jLabel40"); // NOI18N
        FormInput2.add(jLabel40);
        jLabel40.setBounds(0, 120, 420, 23);

        jLabel41.setText("Labeling dari spesimen (baca label) spesimen dengan kertas termasuk nama pasien :");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput2.add(jLabel41);
        jLabel41.setBounds(0, 150, 420, 23);

        VerbalAlatTajam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        VerbalAlatTajam.setName("VerbalAlatTajam"); // NOI18N
        VerbalAlatTajam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                VerbalAlatTajamKeyPressed(evt);
            }
        });
        FormInput2.add(VerbalAlatTajam);
        VerbalAlatTajam.setBounds(420, 150, 100, 23);

        jLabel42.setText("Adakah masalah terhadap peralatan yang dipakai :");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput2.add(jLabel42);
        jLabel42.setBounds(0, 180, 420, 23);

        KelengkapanSpesimenLabel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lengkap", "Tidak Lengkap", "Tidak Ada Pemeriksaan Spesimen" }));
        KelengkapanSpesimenLabel.setName("KelengkapanSpesimenLabel"); // NOI18N
        KelengkapanSpesimenLabel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KelengkapanSpesimenLabelKeyPressed(evt);
            }
        });
        FormInput2.add(KelengkapanSpesimenLabel);
        KelengkapanSpesimenLabel.setBounds(420, 180, 100, 23);

        KelengkapanSpesimenFormulir.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lengkap", "Tidak Lengkap", "Tidak Ada Pemeriksaan Spesimen" }));
        KelengkapanSpesimenFormulir.setName("KelengkapanSpesimenFormulir"); // NOI18N
        KelengkapanSpesimenFormulir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KelengkapanSpesimenFormulirKeyPressed(evt);
            }
        });
        FormInput2.add(KelengkapanSpesimenFormulir);
        KelengkapanSpesimenFormulir.setBounds(420, 210, 100, 23);

        jLabel46.setText("Adakah hal yang penting untuk pulih, sadar dan perawatan pasien telah diperhatikan :");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput2.add(jLabel46);
        jLabel46.setBounds(0, 210, 420, 23);

        jLabel69.setText(":");
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput2.add(jLabel69);
        jLabel69.setBounds(0, 270, 209, 23);

        PerhatianUtamaFasePemulihan.setHighlighter(null);
        PerhatianUtamaFasePemulihan.setName("PerhatianUtamaFasePemulihan"); // NOI18N
        PerhatianUtamaFasePemulihan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerhatianUtamaFasePemulihanKeyPressed(evt);
            }
        });
        FormInput2.add(PerhatianUtamaFasePemulihan);
        PerhatianUtamaFasePemulihan.setBounds(420, 60, 100, 23);

        PerhatianUtamaFasePemulihan1.setHighlighter(null);
        PerhatianUtamaFasePemulihan1.setName("PerhatianUtamaFasePemulihan1"); // NOI18N
        PerhatianUtamaFasePemulihan1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerhatianUtamaFasePemulihan1KeyPressed(evt);
            }
        });
        FormInput2.add(PerhatianUtamaFasePemulihan1);
        PerhatianUtamaFasePemulihan1.setBounds(420, 120, 100, 23);

        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setText("SEBELUM MENINGGALKAN KAMAR OPERASI (SIGN OUT)");
        jLabel65.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput2.add(jLabel65);
        jLabel65.setBounds(0, 3, 500, 30);

        scrollInput2.setViewportView(FormInput2);

        PanelInput2.add(scrollInput2, java.awt.BorderLayout.CENTER);

        internalFrame2.add(PanelInput2, java.awt.BorderLayout.LINE_END);

        PanelInput3.setName("PanelInput3"); // NOI18N
        PanelInput3.setOpaque(false);
        PanelInput3.setPreferredSize(new java.awt.Dimension(500, 100));
        PanelInput3.setLayout(new java.awt.BorderLayout(1, 1));

        scrollInput3.setName("scrollInput3"); // NOI18N
        scrollInput3.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput3.setBackground(new java.awt.Color(250, 255, 245));
        FormInput3.setBorder(null);
        FormInput3.setName("FormInput3"); // NOI18N
        FormInput3.setPreferredSize(new java.awt.Dimension(100, 500));
        FormInput3.setLayout(null);

        jLabel54.setText("Apakah Seluruh Anggota atau TIM sudah memperkenalkan Nama :");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput3.add(jLabel54);
        jLabel54.setBounds(0, 50, 340, 23);

        PengenalanPetugas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sudah", "Belum" }));
        PengenalanPetugas.setName("PengenalanPetugas"); // NOI18N
        PengenalanPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PengenalanPetugasKeyPressed(evt);
            }
        });
        FormInput3.add(PengenalanPetugas);
        PengenalanPetugas.setBounds(340, 50, 130, 23);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel30.setText("Perawat Kamar Operasi");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput3.add(jLabel30);
        jLabel30.setBounds(20, 380, 130, 23);

        PemberianAntibiotik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        PemberianAntibiotik.setName("PemberianAntibiotik"); // NOI18N
        PemberianAntibiotik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemberianAntibiotikKeyPressed(evt);
            }
        });
        FormInput3.add(PemberianAntibiotik);
        PemberianAntibiotik.setBounds(340, 260, 130, 23);

        KdPetugasOK1.setEditable(false);
        KdPetugasOK1.setHighlighter(null);
        KdPetugasOK1.setName("KdPetugasOK1"); // NOI18N
        FormInput3.add(KdPetugasOK1);
        KdPetugasOK1.setBounds(150, 380, 110, 23);

        NmPetugasOK1.setEditable(false);
        NmPetugasOK1.setName("NmPetugasOK1"); // NOI18N
        FormInput3.add(NmPetugasOK1);
        NmPetugasOK1.setBounds(260, 380, 190, 23);

        btnPetugasOK1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPetugasOK1.setMnemonic('2');
        btnPetugasOK1.setToolTipText("ALt+2");
        btnPetugasOK1.setName("btnPetugasOK1"); // NOI18N
        btnPetugasOK1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPetugasOK1ActionPerformed(evt);
            }
        });
        btnPetugasOK1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPetugasOK1KeyPressed(evt);
            }
        });
        FormInput3.add(btnPetugasOK1);
        btnPetugasOK1.setBounds(448, 380, 20, 23);

        jSeparator2.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator2.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator2.setName("jSeparator2"); // NOI18N
        FormInput3.add(jSeparator2);
        jSeparator2.setBounds(0, 40, 600, 1);

        jSeparator5.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator5.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator5.setName("jSeparator5"); // NOI18N
        FormInput3.add(jSeparator5);
        jSeparator5.setBounds(0, 40, 600, 1);

        jSeparator6.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator6.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator6.setName("jSeparator6"); // NOI18N
        FormInput3.add(jSeparator6);
        jSeparator6.setBounds(0, 410, 810, 1);

        jSeparator7.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator7.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator7.setName("jSeparator7"); // NOI18N
        FormInput3.add(jSeparator7);
        jSeparator7.setBounds(0, 410, 810, 1);

        jLabel33.setText("Konfirmasi Nama Paien, Prosedur dan Dimana Insisi Akan Dilakukan :");
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput3.add(jLabel33);
        jLabel33.setBounds(0, 80, 340, 23);

        AntisipisasiKehialnganDarah.setHighlighter(null);
        AntisipisasiKehialnganDarah.setName("AntisipisasiKehialnganDarah"); // NOI18N
        AntisipisasiKehialnganDarah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AntisipisasiKehialnganDarahKeyPressed(evt);
            }
        });
        FormInput3.add(AntisipisasiKehialnganDarah);
        AntisipisasiKehialnganDarah.setBounds(340, 350, 130, 23);

        jLabel55.setText("Adakah antisipasi kehilangan darah :");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput3.add(jLabel55);
        jLabel55.setBounds(0, 170, 340, 23);

        VerbalTindakan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        VerbalTindakan.setName("VerbalTindakan"); // NOI18N
        VerbalTindakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                VerbalTindakanKeyPressed(evt);
            }
        });
        FormInput3.add(VerbalTindakan);
        VerbalTindakan.setBounds(340, 110, 130, 23);

        jLabel35.setText("Apakah antibiotik profilaksis telah diberikan dalam 60 menit terakhir :");
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput3.add(jLabel35);
        jLabel35.setBounds(0, 110, 340, 23);

        jLabel36.setText("Adakah keadaan kritis atau langkah yang tidak rutin :");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput3.add(jLabel36);
        jLabel36.setBounds(0, 140, 340, 23);

        VerbalArea.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        VerbalArea.setName("VerbalArea"); // NOI18N
        VerbalArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                VerbalAreaKeyPressed(evt);
            }
        });
        FormInput3.add(VerbalArea);
        VerbalArea.setBounds(340, 140, 130, 23);

        PenayanganRadiologi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ditampilkan", "Tidak Diperlukan" }));
        PenayanganRadiologi.setName("PenayanganRadiologi"); // NOI18N
        PenayanganRadiologi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PenayanganRadiologiKeyPressed(evt);
            }
        });
        FormInput3.add(PenayanganRadiologi);
        PenayanganRadiologi.setBounds(340, 170, 130, 23);

        jLabel60.setText("Adakah kondisi khusus pada pasien :");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput3.add(jLabel60);
        jLabel60.setBounds(0, 200, 340, 23);

        Masalahpadaperalatan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Iya", "Tidak" }));
        Masalahpadaperalatan.setName("Masalahpadaperalatan"); // NOI18N
        Masalahpadaperalatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MasalahpadaperalatanKeyPressed(evt);
            }
        });
        FormInput3.add(Masalahpadaperalatan);
        Masalahpadaperalatan.setBounds(340, 200, 130, 23);

        jLabel61.setText("Apakah peralatan semua steril sesuai dengan indikator :");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput3.add(jLabel61);
        jLabel61.setBounds(0, 230, 340, 23);

        PemakaianImplant.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Iya", "Tidak" }));
        PemakaianImplant.setName("PemakaianImplant"); // NOI18N
        PemakaianImplant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemakaianImplantKeyPressed(evt);
            }
        });
        FormInput3.add(PemakaianImplant);
        PemakaianImplant.setBounds(340, 230, 130, 23);

        jLabel63.setText("Adakah masalah pada peralatan :");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput3.add(jLabel63);
        jLabel63.setBounds(0, 260, 340, 23);

        VerbalIdentitas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        VerbalIdentitas.setName("VerbalIdentitas"); // NOI18N
        VerbalIdentitas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                VerbalIdentitasKeyPressed(evt);
            }
        });
        FormInput3.add(VerbalIdentitas);
        VerbalIdentitas.setBounds(340, 80, 130, 23);

        AdaHalKhusus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Tidak Ada" }));
        AdaHalKhusus.setName("AdaHalKhusus"); // NOI18N
        AdaHalKhusus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AdaHalKhususKeyPressed(evt);
            }
        });
        FormInput3.add(AdaHalKhusus);
        AdaHalKhusus.setBounds(340, 290, 130, 23);

        jLabel64.setText("Adakah Pemakaian Implan :");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput3.add(jLabel64);
        jLabel64.setBounds(0, 350, 340, 23);

        NamaAntibiotikDIberikan.setHighlighter(null);
        NamaAntibiotikDIberikan.setName("NamaAntibiotikDIberikan"); // NOI18N
        NamaAntibiotikDIberikan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NamaAntibiotikDIberikanKeyPressed(evt);
            }
        });
        FormInput3.add(NamaAntibiotikDIberikan);
        NamaAntibiotikDIberikan.setBounds(340, 320, 130, 23);

        jLabel43.setText("Berapa Jumlah kasa yang disiapkan :");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput3.add(jLabel43);
        jLabel43.setBounds(0, 320, 340, 23);

        jLabel44.setText("Apakah foto-foto pasien yang penting telah ditampilkan :");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput3.add(jLabel44);
        jLabel44.setBounds(0, 290, 340, 23);

        jLabel45.setText(":");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput3.add(jLabel45);
        jLabel45.setBounds(0, 460, 143, 23);

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("SEBELUM INSISI KULIT (TIME OUT)");
        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput3.add(jLabel56);
        jLabel56.setBounds(0, 3, 470, 30);

        scrollInput3.setViewportView(FormInput3);

        PanelInput3.add(scrollInput3, java.awt.BorderLayout.CENTER);

        internalFrame2.add(PanelInput3, java.awt.BorderLayout.CENTER);

        Scroll1.setViewportView(internalFrame2);

        PanelInput.add(Scroll1, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
            isPsien();
        }else{            
            //Valid.pindah(evt,TCari,Tanggal);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt,TCari,BtnSimpan);
}//GEN-LAST:event_TPasienKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        }else if(KodeDokterBedah.getText().trim().equals("")||NamaDokterBedah.getText().trim().equals("")){
            Valid.textKosong(btnDokterBedah,"Dokter Bedah");
        }else if(KodeDokterAnestesi.getText().trim().equals("")||NamaDokterAnestesi.getText().trim().equals("")){
            Valid.textKosong(KodeDokterAnestesi,"Dokter Anestesi");
        }else if(Tindakan.getText().trim().equals("")){
            Valid.textKosong(Tindakan,"Tindakan");
        }else if(SNCN.getText().trim().equals("")){
            Valid.textKosong(SNCN,"SN/CN");
        }else{
            if(Sequel.menyimpantf("signin_sebelum_anestesi","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",17,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),SNCN.getText(),Tindakan.getText(),
                KodeDokterBedah.getText(),KodeDokterAnestesi.getText(),LapanganOperasi.getSelectedItem().toString(),AreaOperasi.getSelectedItem().toString(),Alergi.getText(), 
                ResikoAspirasi.getSelectedItem().toString(),AntisipasiResikoAspirasi.getText(),ResikoKehilanganDarah.getSelectedItem().toString(),JalurIVLine.getText(), 
                RencanaAntisipasiKehilanganDarah.getText(),KesiapanAlatAnes.getSelectedItem().toString(),RencanaAntisipasiKesiapanAlat.getText(),KdPetugasOK.getText()
            })==true){
                tampil();
                emptTeks();
            } 
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,btnPetugasOK,BtnBatal);
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
                if(akses.getkode().equals(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString())||akses.getkode().equals(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString())){
                    hapus();
                }else{
                    JOptionPane.showMessageDialog(null,"User Login harus Petugas OK/Dokter Anestesi..!!");
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
        }else if(KodeDokterBedah.getText().trim().equals("")||NamaDokterBedah.getText().trim().equals("")){
            Valid.textKosong(btnDokterBedah,"Dokter Bedah");
        }else if(KodeDokterAnestesi.getText().trim().equals("")||NamaDokterAnestesi.getText().trim().equals("")){
            Valid.textKosong(KodeDokterAnestesi,"Dokter Anestesi");
        }else if(Tindakan.getText().trim().equals("")){
            Valid.textKosong(Tindakan,"Tindakan");
        }else if(SNCN.getText().trim().equals("")){
            Valid.textKosong(SNCN,"SN/CN");
        }else{  
            if(tbObat.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else {
                    if(akses.getkode().equals(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString())||akses.getkode().equals(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString())){
                        ganti();
                    }else{
                        JOptionPane.showMessageDialog(null,"User Login harus Petugas OK/Dokter Anestesi..!!");
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
        petugas.dispose();
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
                if(TCari.getText().trim().equals("")){
                    ps=koneksi.prepareStatement(
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,signin_sebelum_anestesi.tanggal,"+
                        "signin_sebelum_anestesi.sncn,signin_sebelum_anestesi.tindakan,signin_sebelum_anestesi.kd_dokter_bedah,dokterbedah.nm_dokter as dokterbedah,"+
                        "signin_sebelum_anestesi.kd_dokter_anestesi,dokteranestesi.nm_dokter as dokteranestesi,signin_sebelum_anestesi.identitas,signin_sebelum_anestesi.penandaan_area_operasi,"+
                        "signin_sebelum_anestesi.alergi,signin_sebelum_anestesi.resiko_aspirasi,signin_sebelum_anestesi.resiko_aspirasi_rencana_antisipasi,"+
                        "signin_sebelum_anestesi.resiko_kehilangan_darah,signin_sebelum_anestesi.resiko_kehilangan_darah_line,signin_sebelum_anestesi.resiko_kehilangan_darah_rencana_antisipasi,"+
                        "signin_sebelum_anestesi.kesiapan_alat_obat_anestesi,signin_sebelum_anestesi.kesiapan_alat_obat_anestesi_rencana_antisipasi,signin_sebelum_anestesi.nip_perawat_ok,"+
                        "petugas.nama from signin_sebelum_anestesi inner join reg_periksa on signin_sebelum_anestesi.no_rawat=reg_periksa.no_rawat "+
                        "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join dokter as dokterbedah on dokterbedah.kd_dokter=signin_sebelum_anestesi.kd_dokter_bedah "+
                        "inner join dokter as dokteranestesi on dokteranestesi.kd_dokter=signin_sebelum_anestesi.kd_dokter_anestesi "+
                        "inner join petugas on petugas.nip=signin_sebelum_anestesi.nip_perawat_ok "+
                        "where signin_sebelum_anestesi.tanggal between ? and ? order by signin_sebelum_anestesi.tanggal ");
                }else{
                    ps=koneksi.prepareStatement(
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,signin_sebelum_anestesi.tanggal,"+
                        "signin_sebelum_anestesi.sncn,signin_sebelum_anestesi.tindakan,signin_sebelum_anestesi.kd_dokter_bedah,dokterbedah.nm_dokter as dokterbedah,"+
                        "signin_sebelum_anestesi.kd_dokter_anestesi,dokteranestesi.nm_dokter as dokteranestesi,signin_sebelum_anestesi.identitas,signin_sebelum_anestesi.penandaan_area_operasi,"+
                        "signin_sebelum_anestesi.alergi,signin_sebelum_anestesi.resiko_aspirasi,signin_sebelum_anestesi.resiko_aspirasi_rencana_antisipasi,"+
                        "signin_sebelum_anestesi.resiko_kehilangan_darah,signin_sebelum_anestesi.resiko_kehilangan_darah_line,signin_sebelum_anestesi.resiko_kehilangan_darah_rencana_antisipasi,"+
                        "signin_sebelum_anestesi.kesiapan_alat_obat_anestesi,signin_sebelum_anestesi.kesiapan_alat_obat_anestesi_rencana_antisipasi,signin_sebelum_anestesi.nip_perawat_ok,"+
                        "petugas.nama from signin_sebelum_anestesi inner join reg_periksa on signin_sebelum_anestesi.no_rawat=reg_periksa.no_rawat "+
                        "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join dokter as dokterbedah on dokterbedah.kd_dokter=signin_sebelum_anestesi.kd_dokter_bedah "+
                        "inner join dokter as dokteranestesi on dokteranestesi.kd_dokter=signin_sebelum_anestesi.kd_dokter_anestesi "+
                        "inner join petugas on petugas.nip=signin_sebelum_anestesi.nip_perawat_ok "+
                        "where signin_sebelum_anestesi.tanggal between ? and ? and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or "+
                        "pasien.nm_pasien like ? or dokterbedah.nm_dokter like ? or dokteranestesi.nm_dokter like ? or petugas.nama like ?) "+
                        "order by signin_sebelum_anestesi.tanggal ");
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
                        ps.setString(8,"%"+TCari.getText()+"%");
                    }
                    rs=ps.executeQuery();
                    htmlContent = new StringBuilder();
                    htmlContent.append(                             
                        "<tr class='isi'>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>No.Rawat</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>No.RM</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Nama Pasien</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Tgl.Lahir</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>J.K.</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Tanggal</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>SN/CN</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Tindakan</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Kode Dokter Bedah</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Nama Dokter Bedah</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Kode Dokter Anest</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Nama Dokter Anestesi</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Identitas</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Alergi</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Area Operasi</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Resiko Aspirasi</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Antisipasi Resiko Aspirasi</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Kehilangan Darah</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Jalur IV Line</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Antisipasi Resiko Kehilangan Darah</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Alat & Obat</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Rencana Antisipasi Ketidaklengkapan Alat & Obat</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>NIP OK</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Petugas Ruang OK</b></td>"+
                        "</tr>"
                    );
                    while(rs.next()){
                        htmlContent.append(
                            "<tr class='isi'>"+
                               "<td valign='top'>"+rs.getString("no_rawat")+"</td>"+
                               "<td valign='top'>"+rs.getString("no_rkm_medis")+"</td>"+
                               "<td valign='top'>"+rs.getString("nm_pasien")+"</td>"+
                               "<td valign='top'>"+rs.getString("tgl_lahir")+"</td>"+
                               "<td valign='top'>"+rs.getString("jk")+"</td>"+
                               "<td valign='top'>"+rs.getString("tanggal")+"</td>"+
                               "<td valign='top'>"+rs.getString("sncn")+"</td>"+
                               "<td valign='top'>"+rs.getString("tindakan")+"</td>"+
                               "<td valign='top'>"+rs.getString("kd_dokter_bedah")+"</td>"+
                               "<td valign='top'>"+rs.getString("dokterbedah")+"</td>"+
                               "<td valign='top'>"+rs.getString("kd_dokter_anestesi")+"</td>"+
                               "<td valign='top'>"+rs.getString("dokteranestesi")+"</td>"+
                               "<td valign='top'>"+rs.getString("identitas")+"</td>"+
                               "<td valign='top'>"+rs.getString("alergi")+"</td>"+
                               "<td valign='top'>"+rs.getString("penandaan_area_operasi")+"</td>"+
                               "<td valign='top'>"+rs.getString("resiko_aspirasi")+"</td>"+
                               "<td valign='top'>"+rs.getString("resiko_aspirasi_rencana_antisipasi")+"</td>"+
                               "<td valign='top'>"+rs.getString("resiko_kehilangan_darah")+"</td>"+
                               "<td valign='top'>"+rs.getString("resiko_kehilangan_darah_line")+"</td>"+
                               "<td valign='top'>"+rs.getString("resiko_kehilangan_darah_rencana_antisipasi")+"</td>"+
                               "<td valign='top'>"+rs.getString("kesiapan_alat_obat_anestesi")+"</td>"+
                               "<td valign='top'>"+rs.getString("kesiapan_alat_obat_anestesi_rencana_antisipasi")+"</td>"+
                               "<td valign='top'>"+rs.getString("nip_perawat_ok")+"</td>"+
                               "<td valign='top'>"+rs.getString("nama")+"</td>"+
                            "</tr>");
                    }
                    LoadHTML.setText(
                        "<html>"+
                          "<table width='2100px' border='0' align='center' cellpadding='1px' cellspacing='0' class='tbl_form'>"+
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

                    File f = new File("DataSignInSebelumAnestesi.html");            
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));            
                    bw.write(LoadHTML.getText().replaceAll("<head>","<head>"+
                                "<link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" />"+
                                "<table width='2100px' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                    "<tr class='isi2'>"+
                                        "<td valign='top' align='center'>"+
                                            "<font size='4' face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                                            akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br>"+
                                            akses.getkontakrs()+", E-mail : "+akses.getemailrs()+"<br><br>"+
                                            "<font size='2' face='Tahoma'>DATA SIGN-IN SEBELUM ANESTESI<br><br></font>"+        
                                        "</td>"+
                                   "</tr>"+
                                "</table>")
                    );
                    bw.close();                         
                    Desktop.getDesktop().browse(f.toURI());
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

    private void MnSignInSebelumAnestesiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnSignInSebelumAnestesiActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),22).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbObat.getValueAt(tbObat.getSelectedRow(),23).toString()+"\nID "+(finger.equals("")?tbObat.getValueAt(tbObat.getSelectedRow(),22).toString():finger)+"\n"+Tanggal.getSelectedItem()); 
            finger2=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            param.put("finger2","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbObat.getValueAt(tbObat.getSelectedRow(),11).toString()+"\nID "+(finger2.equals("")?tbObat.getValueAt(tbObat.getSelectedRow(),10).toString():finger2)+"\n"+Tanggal.getSelectedItem()); 
            Valid.MyReportqry("rptFormulirSIgnInSebelumAnestesi.jasper","report","::[ Formulir Sign-In Sebelum Tindakan Anestesi ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,signin_sebelum_anestesi.tanggal,"+
                    "signin_sebelum_anestesi.sncn,signin_sebelum_anestesi.tindakan,signin_sebelum_anestesi.kd_dokter_bedah,dokterbedah.nm_dokter as dokterbedah,"+
                    "signin_sebelum_anestesi.kd_dokter_anestesi,dokteranestesi.nm_dokter as dokteranestesi,signin_sebelum_anestesi.identitas,signin_sebelum_anestesi.penandaan_area_operasi,"+
                    "signin_sebelum_anestesi.alergi,signin_sebelum_anestesi.resiko_aspirasi,signin_sebelum_anestesi.resiko_aspirasi_rencana_antisipasi,"+
                    "signin_sebelum_anestesi.resiko_kehilangan_darah,signin_sebelum_anestesi.resiko_kehilangan_darah_line,signin_sebelum_anestesi.resiko_kehilangan_darah_rencana_antisipasi,"+
                    "signin_sebelum_anestesi.kesiapan_alat_obat_anestesi,signin_sebelum_anestesi.kesiapan_alat_obat_anestesi_rencana_antisipasi,signin_sebelum_anestesi.nip_perawat_ok,"+
                    "petugas.nama from signin_sebelum_anestesi inner join reg_periksa on signin_sebelum_anestesi.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter as dokterbedah on dokterbedah.kd_dokter=signin_sebelum_anestesi.kd_dokter_bedah "+
                    "inner join dokter as dokteranestesi on dokteranestesi.kd_dokter=signin_sebelum_anestesi.kd_dokter_anestesi "+
                    "inner join petugas on petugas.nip=signin_sebelum_anestesi.nip_perawat_ok "+
                    "where signin_sebelum_anestesi.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"' and signin_sebelum_anestesi.tanggal='"+tbObat.getValueAt(tbObat.getSelectedRow(),5).toString()+"' ",param);
        }
    }//GEN-LAST:event_MnSignInSebelumAnestesiActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
       // Valid.pindah(evt,Rencana,Informasi);
    }//GEN-LAST:event_TanggalKeyPressed

    private void btnDokterBedahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokterBedahActionPerformed
        pilihan=1;
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_btnDokterBedahActionPerformed

    private void btnDokterBedahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDokterBedahKeyPressed
        Valid.pindah(evt,Tindakan,btnDokterAnestesi);
    }//GEN-LAST:event_btnDokterBedahKeyPressed

    private void btnDokterAnestesiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokterAnestesiActionPerformed
        pilihan=2;
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_btnDokterAnestesiActionPerformed

    private void btnDokterAnestesiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDokterAnestesiKeyPressed
        Valid.pindah(evt,btnDokterBedah,LapanganOperasi);
    }//GEN-LAST:event_btnDokterAnestesiKeyPressed

    private void LapanganOperasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LapanganOperasiKeyPressed
        Valid.pindah(evt,btnDokterAnestesi,Alergi);
    }//GEN-LAST:event_LapanganOperasiKeyPressed

    private void AreaOperasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AreaOperasiKeyPressed
        Valid.pindah(evt,Alergi,ResikoAspirasi);
    }//GEN-LAST:event_AreaOperasiKeyPressed

    private void ResikoKehilanganDarahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ResikoKehilanganDarahKeyPressed
       Valid.pindah(evt,AntisipasiResikoAspirasi,JalurIVLine);
    }//GEN-LAST:event_ResikoKehilanganDarahKeyPressed

    private void btnPetugasOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPetugasOKActionPerformed
        pilihan=2;
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_btnPetugasOKActionPerformed

    private void btnPetugasOKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPetugasOKKeyPressed
        Valid.pindah(evt,RencanaAntisipasiKesiapanAlat,BtnSimpan);
    }//GEN-LAST:event_btnPetugasOKKeyPressed

    private void SNCNKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SNCNKeyPressed
        Valid.pindah(evt,Tanggal,Tindakan);
    }//GEN-LAST:event_SNCNKeyPressed

    private void TindakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TindakanKeyPressed
        Valid.pindah(evt,SNCN,btnDokterBedah);
    }//GEN-LAST:event_TindakanKeyPressed

    private void AlergiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlergiKeyPressed
        Valid.pindah(evt,LapanganOperasi,AreaOperasi);
    }//GEN-LAST:event_AlergiKeyPressed

    private void AntisipasiResikoAspirasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AntisipasiResikoAspirasiKeyPressed
        Valid.pindah(evt,ResikoAspirasi,ResikoKehilanganDarah);
    }//GEN-LAST:event_AntisipasiResikoAspirasiKeyPressed

    private void ResikoAspirasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ResikoAspirasiKeyPressed
        Valid.pindah(evt,AreaOperasi,AntisipasiResikoAspirasi);
    }//GEN-LAST:event_ResikoAspirasiKeyPressed

    private void KesiapanAlatAnesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KesiapanAlatAnesKeyPressed
        Valid.pindah(evt,RencanaAntisipasiKehilanganDarah,RencanaAntisipasiKesiapanAlat);
    }//GEN-LAST:event_KesiapanAlatAnesKeyPressed

    private void RencanaAntisipasiKesiapanAlatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RencanaAntisipasiKesiapanAlatKeyPressed
        Valid.pindah(evt,KesiapanAlatAnes,btnPetugasOK);
    }//GEN-LAST:event_RencanaAntisipasiKesiapanAlatKeyPressed

    private void RencanaAntisipasiKehilanganDarahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RencanaAntisipasiKehilanganDarahKeyPressed
        Valid.pindah(evt,JalurIVLine,KesiapanAlatAnes);
    }//GEN-LAST:event_RencanaAntisipasiKehilanganDarahKeyPressed

    private void JalurIVLineKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JalurIVLineKeyPressed
        Valid.pindah(evt,ResikoKehilanganDarah,RencanaAntisipasiKehilanganDarah);
    }//GEN-LAST:event_JalurIVLineKeyPressed

    private void PengenalanPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PengenalanPetugasKeyPressed
//        Valid.pindah(evt,VerbalArea,PerkiraanLama);
    }//GEN-LAST:event_PengenalanPetugasKeyPressed

    private void PemberianAntibiotikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemberianAntibiotikKeyPressed
        Valid.pindah(evt,PemakaianImplant,NamaAntibiotikDIberikan);
    }//GEN-LAST:event_PemberianAntibiotikKeyPressed

    private void btnPetugasOK1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPetugasOK1ActionPerformed
        pilihan=2;
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_btnPetugasOK1ActionPerformed

    private void btnPetugasOK1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPetugasOK1KeyPressed
//        Valid.pindah(evt,JmlKasa,BtnSimpan);
    }//GEN-LAST:event_btnPetugasOK1KeyPressed

    private void AntisipisasiKehialnganDarahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AntisipisasiKehialnganDarahKeyPressed
//        Valid.pindah(evt,JamPemberianAntibiotik,AdaHalKhusus);
    }//GEN-LAST:event_AntisipisasiKehialnganDarahKeyPressed

    private void VerbalTindakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_VerbalTindakanKeyPressed
        Valid.pindah(evt,VerbalIdentitas,VerbalArea);
    }//GEN-LAST:event_VerbalTindakanKeyPressed

    private void VerbalAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_VerbalAreaKeyPressed
        Valid.pindah(evt,VerbalTindakan,PengenalanPetugas);
    }//GEN-LAST:event_VerbalAreaKeyPressed

    private void PenayanganRadiologiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PenayanganRadiologiKeyPressed
//        Valid.pindah(evt,PerkiraanLama,Masalahpadaperalatan);
    }//GEN-LAST:event_PenayanganRadiologiKeyPressed

    private void MasalahpadaperalatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MasalahpadaperalatanKeyPressed
        Valid.pindah(evt,PenayanganRadiologi,PemakaianImplant);
    }//GEN-LAST:event_MasalahpadaperalatanKeyPressed

    private void PemakaianImplantKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemakaianImplantKeyPressed
        Valid.pindah(evt,Masalahpadaperalatan,PemberianAntibiotik);
    }//GEN-LAST:event_PemakaianImplantKeyPressed

    private void VerbalIdentitasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_VerbalIdentitasKeyPressed
        Valid.pindah(evt,btnDokterAnestesi,VerbalTindakan);
    }//GEN-LAST:event_VerbalIdentitasKeyPressed

    private void AdaHalKhususKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AdaHalKhususKeyPressed
//        Valid.pindah(evt,AntisipisasiKehialnganDarah,HalKhususDiperhatikan);
    }//GEN-LAST:event_AdaHalKhususKeyPressed

    private void NamaAntibiotikDIberikanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NamaAntibiotikDIberikanKeyPressed
//        Valid.pindah(evt,PemberianAntibiotik,JamPemberianAntibiotik);
    }//GEN-LAST:event_NamaAntibiotikDIberikanKeyPressed

    private void btnPetugasOK2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPetugasOK2ActionPerformed
        pilihan=2;
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_btnPetugasOK2ActionPerformed

    private void btnPetugasOK2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPetugasOK2KeyPressed
        Valid.pindah(evt,PerhatianUtamaFasePemulihan,BtnSimpan);
    }//GEN-LAST:event_btnPetugasOK2KeyPressed

    private void VerbalKasaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_VerbalKasaKeyPressed
//        Valid.pindah(evt,VerbalTindakan,VerbalInstrumen);
    }//GEN-LAST:event_VerbalKasaKeyPressed

    private void VerbalAlatTajamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_VerbalAlatTajamKeyPressed
//        Valid.pindah(evt,VerbalInstrumen,KelengkapanSpesimenLabel);
    }//GEN-LAST:event_VerbalAlatTajamKeyPressed

    private void KelengkapanSpesimenLabelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KelengkapanSpesimenLabelKeyPressed
        Valid.pindah(evt,VerbalAlatTajam,KelengkapanSpesimenFormulir);
    }//GEN-LAST:event_KelengkapanSpesimenLabelKeyPressed

    private void KelengkapanSpesimenFormulirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KelengkapanSpesimenFormulirKeyPressed
//        Valid.pindah(evt,KelengkapanSpesimenLabel,PeninjauanKembaliDokterBedah);
    }//GEN-LAST:event_KelengkapanSpesimenFormulirKeyPressed

    private void PerhatianUtamaFasePemulihanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PerhatianUtamaFasePemulihanKeyPressed
//        Valid.pindah(evt,PeninjauanKembaliPerawatKamarOK,btnPetugasOK);
    }//GEN-LAST:event_PerhatianUtamaFasePemulihanKeyPressed

    private void PerhatianUtamaFasePemulihan1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PerhatianUtamaFasePemulihan1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PerhatianUtamaFasePemulihan1KeyPressed

    private void BtnSimpan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSimpan1ActionPerformed

    private void BtnSimpan1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpan1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSimpan1KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMSurgicalSafetyChecklist dialog = new RMSurgicalSafetyChecklist(new javax.swing.JFrame(), true);
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
    private widget.ComboBox AdaHalKhusus;
    private widget.TextBox Alergi;
    private widget.TextBox AntisipasiResikoAspirasi;
    private widget.TextBox AntisipisasiKehialnganDarah;
    private widget.ComboBox AreaOperasi;
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.Button BtnSimpan1;
    private widget.CekBox ChkInput;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormInput1;
    private widget.PanelBiasa FormInput2;
    private widget.PanelBiasa FormInput3;
    private widget.TextBox JalurIVLine;
    private widget.TextBox KdPetugasOK;
    private widget.TextBox KdPetugasOK1;
    private widget.TextBox KdPetugasOK2;
    private widget.ComboBox KelengkapanSpesimenFormulir;
    private widget.ComboBox KelengkapanSpesimenLabel;
    private widget.ComboBox KesiapanAlatAnes;
    private widget.TextBox KodeDokterAnestesi;
    private widget.TextBox KodeDokterBedah;
    private widget.Label LCount;
    private widget.ComboBox LapanganOperasi;
    private widget.editorpane LoadHTML;
    private widget.ComboBox Masalahpadaperalatan;
    private javax.swing.JMenuItem MnSignInSebelumAnestesi;
    private widget.TextBox NamaAntibiotikDIberikan;
    private widget.TextBox NamaDokterAnestesi;
    private widget.TextBox NamaDokterBedah;
    private widget.TextBox NmPetugasOK;
    private widget.TextBox NmPetugasOK1;
    private widget.TextBox NmPetugasOK2;
    private javax.swing.JPanel PanelInput;
    private javax.swing.JPanel PanelInput1;
    private javax.swing.JPanel PanelInput2;
    private javax.swing.JPanel PanelInput3;
    private widget.ComboBox PemakaianImplant;
    private widget.ComboBox PemberianAntibiotik;
    private widget.ComboBox PenayanganRadiologi;
    private widget.ComboBox PengenalanPetugas;
    private widget.TextBox PerhatianUtamaFasePemulihan;
    private widget.TextBox PerhatianUtamaFasePemulihan1;
    private widget.TextBox RencanaAntisipasiKehilanganDarah;
    private widget.TextBox RencanaAntisipasiKesiapanAlat;
    private widget.ComboBox ResikoAspirasi;
    private widget.ComboBox ResikoKehilanganDarah;
    private widget.TextBox SNCN;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Tanggal Tanggal;
    private widget.TextBox TglLahir;
    private widget.TextBox Tindakan;
    private widget.ComboBox VerbalAlatTajam;
    private widget.ComboBox VerbalArea;
    private widget.ComboBox VerbalIdentitas;
    private widget.ComboBox VerbalKasa;
    private widget.ComboBox VerbalTindakan;
    private widget.Button btnDokterAnestesi;
    private widget.Button btnDokterBedah;
    private widget.Button btnPetugasOK;
    private widget.Button btnPetugasOK1;
    private widget.Button btnPetugasOK2;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.Label jLabel16;
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
    private widget.Label jLabel5;
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
    private widget.Label jLabel69;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollInput1;
    private widget.ScrollPane scrollInput2;
    private widget.ScrollPane scrollInput3;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables
    
    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            if(TCari.getText().trim().equals("")){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,signin_sebelum_anestesi.tanggal,"+
                    "signin_sebelum_anestesi.sncn,signin_sebelum_anestesi.tindakan,signin_sebelum_anestesi.kd_dokter_bedah,dokterbedah.nm_dokter as dokterbedah,"+
                    "signin_sebelum_anestesi.kd_dokter_anestesi,dokteranestesi.nm_dokter as dokteranestesi,signin_sebelum_anestesi.identitas,signin_sebelum_anestesi.penandaan_area_operasi,"+
                    "signin_sebelum_anestesi.alergi,signin_sebelum_anestesi.resiko_aspirasi,signin_sebelum_anestesi.resiko_aspirasi_rencana_antisipasi,"+
                    "signin_sebelum_anestesi.resiko_kehilangan_darah,signin_sebelum_anestesi.resiko_kehilangan_darah_line,signin_sebelum_anestesi.resiko_kehilangan_darah_rencana_antisipasi,"+
                    "signin_sebelum_anestesi.kesiapan_alat_obat_anestesi,signin_sebelum_anestesi.kesiapan_alat_obat_anestesi_rencana_antisipasi,signin_sebelum_anestesi.nip_perawat_ok,"+
                    "petugas.nama from signin_sebelum_anestesi inner join reg_periksa on signin_sebelum_anestesi.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter as dokterbedah on dokterbedah.kd_dokter=signin_sebelum_anestesi.kd_dokter_bedah "+
                    "inner join dokter as dokteranestesi on dokteranestesi.kd_dokter=signin_sebelum_anestesi.kd_dokter_anestesi "+
                    "inner join petugas on petugas.nip=signin_sebelum_anestesi.nip_perawat_ok "+
                    "where signin_sebelum_anestesi.tanggal between ? and ? order by signin_sebelum_anestesi.tanggal ");
            }else{
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,signin_sebelum_anestesi.tanggal,"+
                    "signin_sebelum_anestesi.sncn,signin_sebelum_anestesi.tindakan,signin_sebelum_anestesi.kd_dokter_bedah,dokterbedah.nm_dokter as dokterbedah,"+
                    "signin_sebelum_anestesi.kd_dokter_anestesi,dokteranestesi.nm_dokter as dokteranestesi,signin_sebelum_anestesi.identitas,signin_sebelum_anestesi.penandaan_area_operasi,"+
                    "signin_sebelum_anestesi.alergi,signin_sebelum_anestesi.resiko_aspirasi,signin_sebelum_anestesi.resiko_aspirasi_rencana_antisipasi,"+
                    "signin_sebelum_anestesi.resiko_kehilangan_darah,signin_sebelum_anestesi.resiko_kehilangan_darah_line,signin_sebelum_anestesi.resiko_kehilangan_darah_rencana_antisipasi,"+
                    "signin_sebelum_anestesi.kesiapan_alat_obat_anestesi,signin_sebelum_anestesi.kesiapan_alat_obat_anestesi_rencana_antisipasi,signin_sebelum_anestesi.nip_perawat_ok,"+
                    "petugas.nama from signin_sebelum_anestesi inner join reg_periksa on signin_sebelum_anestesi.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter as dokterbedah on dokterbedah.kd_dokter=signin_sebelum_anestesi.kd_dokter_bedah "+
                    "inner join dokter as dokteranestesi on dokteranestesi.kd_dokter=signin_sebelum_anestesi.kd_dokter_anestesi "+
                    "inner join petugas on petugas.nip=signin_sebelum_anestesi.nip_perawat_ok "+
                    "where signin_sebelum_anestesi.tanggal between ? and ? and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or "+
                    "pasien.nm_pasien like ? or dokterbedah.nm_dokter like ? or dokteranestesi.nm_dokter like ? or petugas.nama like ?) "+
                    "order by signin_sebelum_anestesi.tanggal ");
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
                    ps.setString(8,"%"+TCari.getText()+"%");
                }
                    
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("tgl_lahir"),rs.getString("jk"),
                        rs.getString("tanggal"),rs.getString("sncn"),rs.getString("tindakan"),rs.getString("kd_dokter_bedah"),rs.getString("dokterbedah"),
                        rs.getString("kd_dokter_anestesi"),rs.getString("dokteranestesi"),rs.getString("identitas"),rs.getString("alergi"),rs.getString("penandaan_area_operasi"),
                        rs.getString("resiko_aspirasi"),rs.getString("resiko_aspirasi_rencana_antisipasi"),rs.getString("resiko_kehilangan_darah"),rs.getString("resiko_kehilangan_darah_line"),
                        rs.getString("resiko_kehilangan_darah_rencana_antisipasi"),rs.getString("kesiapan_alat_obat_anestesi"),rs.getString("kesiapan_alat_obat_anestesi_rencana_antisipasi"),
                        rs.getString("nip_perawat_ok"),rs.getString("nama")
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
        SNCN.setText("");
        Tindakan.setText("");
        KodeDokterBedah.setText("");
        NamaDokterBedah.setText("");
        KodeDokterAnestesi.setText("");
        NamaDokterAnestesi.setText("");
        LapanganOperasi.setSelectedIndex(0);
        Alergi.setText("");
        AreaOperasi.setSelectedIndex(0);
        ResikoAspirasi.setSelectedIndex(0);
        AntisipasiResikoAspirasi.setText("");
        ResikoKehilanganDarah.setSelectedIndex(0);
        JalurIVLine.setText("");
        RencanaAntisipasiKehilanganDarah.setText("");
        KesiapanAlatAnes.setSelectedIndex(0);
        RencanaAntisipasiKesiapanAlat.setText("");
        Tanggal.setDate(new Date());
        SNCN.requestFocus();
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            SNCN.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            Tindakan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            KodeDokterBedah.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            NamaDokterBedah.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            KodeDokterAnestesi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            NamaDokterAnestesi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            LapanganOperasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            Alergi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            AreaOperasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            ResikoAspirasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
            AntisipasiResikoAspirasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
            ResikoKehilanganDarah.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());
            JalurIVLine.setText(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());
            RencanaAntisipasiKehilanganDarah.setText(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString());
            KesiapanAlatAnes.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());
            RencanaAntisipasiKesiapanAlat.setText(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString());
            KdPetugasOK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString());
            NmPetugasOK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),23).toString());
            Valid.SetTgl2(Tanggal,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
        }
    }
    private void isRawat() {
         Sequel.cariIsi("select reg_periksa.no_rkm_medis from reg_periksa where reg_periksa.no_rawat='"+TNoRw.getText()+"' ",TNoRM);
    }

    private void isPsien() {
        Sequel.cariIsi("select pasien.nm_pasien from pasien where pasien.no_rkm_medis='"+TNoRM.getText()+"' ",TPasien);
        Sequel.cariIsi("select DATE_FORMAT(pasien.tgl_lahir,'%d-%m-%Y') from pasien where pasien.no_rkm_medis=? ",TglLahir,TNoRM.getText());
    }
    
    public void setNoRm(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        Sequel.cariIsi("select reg_periksa.tgl_registrasi from reg_periksa where reg_periksa.no_rawat='"+norwt+"'", DTPCari1);
        DTPCari2.setDate(tgl2);
        isRawat();
        isPsien();
        ChkInput.setSelected(true);
        isForm();
    }
    
    public void setNoRm(String norwt, Date tgl2,String KodeDokter,String NamaDokter,String Operasi) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        Sequel.cariIsi("select reg_periksa.tgl_registrasi from reg_periksa where reg_periksa.no_rawat='"+norwt+"'", DTPCari1);
        DTPCari2.setDate(tgl2);
        isRawat();
        isPsien();
        ChkInput.setSelected(true);
        isForm();
        KodeDokterBedah.setText(KodeDokter);
        NamaDokterBedah.setText(NamaDokter);
        Tindakan.setText(Operasi);
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            if(internalFrame1.getHeight()>508){
                ChkInput.setVisible(false);
                PanelInput.setPreferredSize(new Dimension(WIDTH,600));
                FormInput.setVisible(true);      
                ChkInput.setVisible(true);
            }else{
                ChkInput.setVisible(false);
                PanelInput.setPreferredSize(new Dimension(WIDTH,internalFrame1.getHeight()-172));
                FormInput.setVisible(true);      
                ChkInput.setVisible(true);
            }
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getsignin_sebelum_anestesi());
        BtnHapus.setEnabled(akses.getsignin_sebelum_anestesi());
        BtnEdit.setEnabled(akses.getsignin_sebelum_anestesi());
        BtnPrint.setEnabled(akses.getsignin_sebelum_anestesi()); 
    }

    private void ganti() {
        Sequel.mengedit("signin_sebelum_anestesi","no_rawat=? and tanggal=?","no_rawat=?,tanggal=?,sncn=?,tindakan=?,kd_dokter_bedah=?,kd_dokter_anestesi=?,identitas=?,"+
            "penandaan_area_operasi=?,alergi=?,resiko_aspirasi=?,resiko_aspirasi_rencana_antisipasi=?,resiko_kehilangan_darah=?,resiko_kehilangan_darah_line=?,"+
            "resiko_kehilangan_darah_rencana_antisipasi=?,kesiapan_alat_obat_anestesi=?,kesiapan_alat_obat_anestesi_rencana_antisipasi=?,nip_perawat_ok=?",19,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),SNCN.getText(),Tindakan.getText(),
                KodeDokterBedah.getText(),KodeDokterAnestesi.getText(),LapanganOperasi.getSelectedItem().toString(),AreaOperasi.getSelectedItem().toString(),Alergi.getText(), 
                ResikoAspirasi.getSelectedItem().toString(),AntisipasiResikoAspirasi.getText(),ResikoKehilanganDarah.getSelectedItem().toString(),JalurIVLine.getText(), 
                RencanaAntisipasiKehilanganDarah.getText(),KesiapanAlatAnes.getSelectedItem().toString(),RencanaAntisipasiKesiapanAlat.getText(),KdPetugasOK.getText(),
                tbObat.getValueAt(tbObat.getSelectedRow(),0).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),5).toString()
        });
            
        if(tabMode.getRowCount()!=0){tampil();}
        emptTeks();
    }

    private void hapus() {
        if(Sequel.queryu2tf("delete from signin_sebelum_anestesi where no_rawat=? and tanggal=?",2,new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),5).toString()
        })==true){
            tabMode.removeRow(tbObat.getSelectedRow());
            LCount.setText(""+tabMode.getRowCount());
            emptTeks();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }
    
    private void simpanSignin2(){
        Sequel.menyimpantf("signin_sebelum_anestesi2","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",17,new String[]{
            TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),Tindakan.getText(),
            KodeDokterAnestesi.getText(),LapanganOperasi.getSelectedItem().toString(),AreaOperasi.getSelectedItem().toString(),KesiapanAlatAnes.getSelectedItem().toString(),
            ResikoAspirasi.getSelectedItem().toString(),AntisipasiResikoAspirasi.getText(),ResikoKehilanganDarah.getSelectedItem().toString(),JalurIVLine.getText(), 
            RencanaAntisipasiKehilanganDarah.getText(),KesiapanAlatAnes.getSelectedItem().toString(),RencanaAntisipasiKesiapanAlat.getText(),KdPetugasOK.getText()
        });
    }
}
