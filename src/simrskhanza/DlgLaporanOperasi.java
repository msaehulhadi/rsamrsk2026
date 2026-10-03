package simrskhanza;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;
import keuangan.DlgJnsPerawatanOperasi;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import keuangan.Jurnal;
import rekammedis.MasterCariTemplateLaporanOperasi;

public class DlgLaporanOperasi extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Jurnal jur=new Jurnal();
    private Connection koneksi=koneksiDB.condb();
    private PreparedStatement ps;
    private ResultSet rs;
    private DlgCariPetugas petugas=new DlgCariPetugas( null,false);
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private MasterCariTemplateLaporanOperasi template=new MasterCariTemplateLaporanOperasi(null,false);
    private String kelas_operasi="Yes",kelas="",cara_bayar_operasi="Yes",kd_pj="",status="";
    private double ttljmdokter=0,ttljmpetugas=0,ttlpendapatan=0,ttlbhp=0;
    private String finger="",norawatibu="";
    private int pilihan=0,i=0;
    private boolean[] pilih; 
    private boolean sukses=true;


    /** Creates new form DlgProgramStudi
     * @param parent
     * @param modal*/
    public DlgLaporanOperasi(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        Object[] row={"No. Rawat","No. RM","Nama Pasien","Tanggal Operasi","Operator","Asisten 1","Asisten 2",
                      "Instrumen","Dokter Anastesi","Penata Anastesi","Sirkuler","Jenis Operasi","Jenis Anastesi","Komplikasi",
                      "No. Implant","Diagnosa Pre Operasi","Diagnosa Post Operasi","Tindakan Operasi","Selesai Operasi","Permintaan PA","Asal Spesimen","Kultur",
                      "Perdarahan","Transfusi","Ket. Transfusi","Laporan Operasi"};
        tabMode=new DefaultTableModel(null,row){
            @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbtindakan.setModel(tabMode);

        tbtindakan.setPreferredScrollableViewportSize(new Dimension(800,800));
        tbtindakan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 26; i++) {
            TableColumn column = tbtindakan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(170);
            }else if(i==1){
                column.setPreferredWidth(100);
            }else if(i==2){
                column.setPreferredWidth(250);
            }else if(i==3){
                column.setPreferredWidth(100);
            }else{
                column.setPreferredWidth(85);
            }
        }
        tbtindakan.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        kdoperator1.setDocument(new batasInput((byte)20).getKata(kdoperator1));
        kdasistoperator1.setDocument(new batasInput((byte)20).getKata(kdasistoperator1));
        kdasistoperator2.setDocument(new batasInput((byte)20).getKata(kdasistoperator2));
        kdInstrumen.setDocument(new batasInput((byte)20).getKata(kdInstrumen));
        kdanestesi.setDocument(new batasInput((byte)20).getKata(kdanestesi));
        kdasistanestesi.setDocument(new batasInput((byte)20).getKata(kdasistanestesi));
        kdonloop1.setDocument(new batasInput((byte)20).getKata(kdonloop1));    
        PreOp.setDocument(new batasInput((int)100).getKata(PreOp));      
        PostOp.setDocument(new batasInput((int)100).getKata(PostOp));      
        Jaringan.setDocument(new batasInput((int)100).getKata(Jaringan));
        Laporan.setDocument(new batasInput((int)8000).getKata(Laporan));
        
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
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
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){                    
                    if(pilihan==1){
                        kdoperator1.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                        nmoperator1.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                        kdoperator1.requestFocus();
                    }else if(pilihan==2){
                        kdanestesi.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                        nmanestesi.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                        kdanestesi.requestFocus();
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
        
        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(petugas.getTable().getSelectedRow()!= -1){    
                    if(pilihan==1){
                        kdasistoperator1.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                        nmasistoperator1.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                        kdasistoperator1.requestFocus();
                    }else if(pilihan==2){
                        kdasistoperator2.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                        nmasistoperator2.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                        kdasistoperator2.requestFocus();
                    }else if(pilihan==3){
                        kdInstrumen.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                        nminstrumen.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                        kdInstrumen.requestFocus();
                    }else if(pilihan==4){
                        kdasistanestesi.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                        nmasistanestesi.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                        kdasistanestesi.requestFocus();
                    }else if(pilihan==5){
                        kdonloop1.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                        nmonloop1.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                        kdonloop1.requestFocus();
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
        
        template.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(template.getTable().getSelectedRow()!= -1){  
                    PreOp.setText(template.getTable().getValueAt(template.getTable().getSelectedRow(),2).toString());
                    PostOp.setText(template.getTable().getValueAt(template.getTable().getSelectedRow(),3).toString());
                    Jaringan.setText(template.getTable().getValueAt(template.getTable().getSelectedRow(),4).toString());
                    DikirimPA.setSelectedItem(template.getTable().getValueAt(template.getTable().getSelectedRow(),5).toString());
                    Laporan.setText(template.getTable().getValueAt(template.getTable().getSelectedRow(),6).toString());
                    Laporan.requestFocus();
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
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Kd2 = new widget.TextBox();
        Popup = new javax.swing.JPopupMenu();
        ppLaporanOperasi = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panelisi5 = new widget.panelisi();
        label10 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari2 = new widget.Button();
        BtnAll1 = new widget.Button();
        Scroll1 = new widget.ScrollPane();
        tbtindakan = new widget.Table();
        panelisi1 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnSimpan1 = new widget.Button();
        BtnKeluar = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        scrollPane1 = new widget.ScrollPane();
        FormInput = new widget.panelisi();
        label14 = new widget.Label();
        kdoperator1 = new widget.TextBox();
        nmoperator1 = new widget.TextBox();
        BtnOperator1 = new widget.Button();
        label11 = new widget.Label();
        tgl = new widget.Tanggal();
        jLabel3 = new widget.Label();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        jLabel4 = new widget.Label();
        label17 = new widget.Label();
        kdasistoperator1 = new widget.TextBox();
        nmasistoperator1 = new widget.TextBox();
        btnAsis1 = new widget.Button();
        label21 = new widget.Label();
        kdanestesi = new widget.TextBox();
        nmanestesi = new widget.TextBox();
        BtnAnastesi = new widget.Button();
        btnAsis2 = new widget.Button();
        nmasistoperator2 = new widget.TextBox();
        kdasistoperator2 = new widget.TextBox();
        label18 = new widget.Label();
        btnAsis3 = new widget.Button();
        nminstrumen = new widget.TextBox();
        kdInstrumen = new widget.TextBox();
        label23 = new widget.Label();
        label26 = new widget.Label();
        kdasistanestesi = new widget.TextBox();
        nmasistanestesi = new widget.TextBox();
        BtnAsnes = new widget.Button();
        jLabel5 = new widget.Label();
        Kategori = new widget.ComboBox();
        label25 = new widget.Label();
        kdonloop1 = new widget.TextBox();
        nmonloop1 = new widget.TextBox();
        btnOnloop1 = new widget.Button();
        label12 = new widget.Label();
        tgl2 = new widget.Tanggal();
        PreOp = new widget.TextBox();
        jLabel6 = new widget.Label();
        jLabel7 = new widget.Label();
        PostOp = new widget.TextBox();
        jLabel8 = new widget.Label();
        Jaringan = new widget.TextBox();
        jLabel9 = new widget.Label();
        DikirimPA = new widget.ComboBox();
        scrollPane2 = new widget.ScrollPane();
        Laporan = new widget.TextArea();
        jLabel10 = new widget.Label();
        jenis_anastesi = new widget.ComboBox();
        jLabel11 = new widget.Label();
        komplikasi = new widget.ComboBox();
        jLabel12 = new widget.Label();
        asal_spesimen = new widget.TextBox();
        kultur = new widget.ComboBox();
        jLabel13 = new widget.Label();
        jLabel14 = new widget.Label();
        perdarahan = new widget.ComboBox();
        jLabel15 = new widget.Label();
        transfusi = new widget.ComboBox();
        jLabel16 = new widget.Label();
        ket_transfusi = new widget.TextBox();
        jLabel17 = new widget.Label();
        no_implant = new widget.TextBox();

        Kd2.setName("Kd2"); // NOI18N
        Kd2.setPreferredSize(new java.awt.Dimension(207, 23));

        Popup.setName("Popup"); // NOI18N

        ppLaporanOperasi.setBackground(new java.awt.Color(255, 255, 254));
        ppLaporanOperasi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ppLaporanOperasi.setForeground(new java.awt.Color(50, 50, 50));
        ppLaporanOperasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/PrinterSettings.png"))); // NOI18N
        ppLaporanOperasi.setText("Laporan Operasi");
        ppLaporanOperasi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppLaporanOperasi.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppLaporanOperasi.setName("ppLaporanOperasi"); // NOI18N
        ppLaporanOperasi.setPreferredSize(new java.awt.Dimension(200, 25));
        ppLaporanOperasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppLaporanOperasiActionPerformed(evt);
            }
        });
        Popup.add(ppLaporanOperasi);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Laporan Operasi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(816, 102));
        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)), " Tindakan ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(300, 102));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelisi5.setBorder(null);
        panelisi5.setName("panelisi5"); // NOI18N
        panelisi5.setPreferredSize(new java.awt.Dimension(100, 43));
        panelisi5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        label10.setText("Key Word :");
        label10.setName("label10"); // NOI18N
        label10.setPreferredSize(new java.awt.Dimension(68, 23));
        panelisi5.add(label10);

        TCari.setToolTipText("Alt+C");
        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(215, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelisi5.add(TCari);

        BtnCari2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari2.setMnemonic('1');
        BtnCari2.setToolTipText("Alt+1");
        BtnCari2.setName("BtnCari2"); // NOI18N
        BtnCari2.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari2ActionPerformed(evt);
            }
        });
        BtnCari2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCari2KeyPressed(evt);
            }
        });
        panelisi5.add(BtnCari2);

        BtnAll1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll1.setMnemonic('2');
        BtnAll1.setToolTipText("Alt+2");
        BtnAll1.setName("BtnAll1"); // NOI18N
        BtnAll1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAll1ActionPerformed(evt);
            }
        });
        BtnAll1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAll1KeyPressed(evt);
            }
        });
        panelisi5.add(BtnAll1);

        jPanel3.add(panelisi5, java.awt.BorderLayout.PAGE_END);

        Scroll1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 253)));
        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);

        tbtindakan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbtindakan.setComponentPopupMenu(Popup);
        tbtindakan.setName("tbtindakan"); // NOI18N
        tbtindakan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbtindakanMouseClicked(evt);
            }
        });
        tbtindakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbtindakanKeyPressed(evt);
            }
        });
        Scroll1.setViewportView(tbtindakan);

        jPanel3.add(Scroll1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3);

        internalFrame1.add(jPanel1, java.awt.BorderLayout.CENTER);

        panelisi1.setName("panelisi1"); // NOI18N
        panelisi1.setPreferredSize(new java.awt.Dimension(100, 56));
        panelisi1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

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
        panelisi1.add(BtnSimpan);

        BtnSimpan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/PrinterSettings.png"))); // NOI18N
        BtnSimpan1.setMnemonic('S');
        BtnSimpan1.setText("Cetak");
        BtnSimpan1.setToolTipText("Alt+S");
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
        panelisi1.add(BtnSimpan1);

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
        panelisi1.add(BtnKeluar);

        internalFrame1.add(panelisi1, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(560, 444));
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

        scrollPane1.setName("scrollPane1"); // NOI18N

        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(89, 553));
        FormInput.setLayout(null);

        label14.setText("Operator 1 :");
        label14.setName("label14"); // NOI18N
        label14.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label14);
        label14.setBounds(0, 70, 81, 23);

        kdoperator1.setEditable(false);
        kdoperator1.setName("kdoperator1"); // NOI18N
        kdoperator1.setPreferredSize(new java.awt.Dimension(80, 23));
        kdoperator1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kdoperator1ActionPerformed(evt);
            }
        });
        kdoperator1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdoperator1KeyPressed(evt);
            }
        });
        FormInput.add(kdoperator1);
        kdoperator1.setBounds(84, 70, 100, 23);

        nmoperator1.setEditable(false);
        nmoperator1.setName("nmoperator1"); // NOI18N
        nmoperator1.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(nmoperator1);
        nmoperator1.setBounds(185, 70, 190, 23);

        BtnOperator1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnOperator1.setMnemonic('2');
        BtnOperator1.setToolTipText("Alt+2");
        BtnOperator1.setName("BtnOperator1"); // NOI18N
        BtnOperator1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnOperator1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnOperator1ActionPerformed(evt);
            }
        });
        BtnOperator1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnOperator1KeyPressed(evt);
            }
        });
        FormInput.add(BtnOperator1);
        BtnOperator1.setBounds(376, 70, 28, 23);

        label11.setText("Tanggal :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label11);
        label11.setBounds(0, 40, 101, 23);

        tgl.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        tgl.setName("tgl"); // NOI18N
        tgl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tglKeyPressed(evt);
            }
        });
        FormInput.add(tgl);
        tgl.setBounds(110, 40, 150, 23);

        jLabel3.setText("No.Rawat :");
        jLabel3.setName("jLabel3"); // NOI18N
        FormInput.add(jLabel3);
        jLabel3.setBounds(0, 10, 81, 23);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(84, 10, 180, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        FormInput.add(TPasien);
        TPasien.setBounds(265, 10, 535, 23);

        jLabel4.setText("Jenis Anasthesi :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(400, 190, 100, 23);

        label17.setText("Asisten 1 :");
        label17.setName("label17"); // NOI18N
        label17.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label17);
        label17.setBounds(0, 100, 80, 23);

        kdasistoperator1.setEditable(false);
        kdasistoperator1.setName("kdasistoperator1"); // NOI18N
        kdasistoperator1.setPreferredSize(new java.awt.Dimension(80, 23));
        kdasistoperator1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdasistoperator1KeyPressed(evt);
            }
        });
        FormInput.add(kdasistoperator1);
        kdasistoperator1.setBounds(80, 100, 100, 23);

        nmasistoperator1.setEditable(false);
        nmasistoperator1.setName("nmasistoperator1"); // NOI18N
        nmasistoperator1.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(nmasistoperator1);
        nmasistoperator1.setBounds(180, 100, 190, 23);

        btnAsis1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnAsis1.setMnemonic('2');
        btnAsis1.setToolTipText("Alt+2");
        btnAsis1.setName("btnAsis1"); // NOI18N
        btnAsis1.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAsis1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsis1ActionPerformed(evt);
            }
        });
        FormInput.add(btnAsis1);
        btnAsis1.setBounds(370, 100, 28, 23);

        label21.setText("dr Anestesi :");
        label21.setName("label21"); // NOI18N
        label21.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label21);
        label21.setBounds(410, 70, 81, 23);

        kdanestesi.setEditable(false);
        kdanestesi.setName("kdanestesi"); // NOI18N
        kdanestesi.setPreferredSize(new java.awt.Dimension(80, 23));
        kdanestesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdanestesiKeyPressed(evt);
            }
        });
        FormInput.add(kdanestesi);
        kdanestesi.setBounds(500, 70, 100, 23);

        nmanestesi.setEditable(false);
        nmanestesi.setName("nmanestesi"); // NOI18N
        nmanestesi.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(nmanestesi);
        nmanestesi.setBounds(600, 70, 190, 23);

        BtnAnastesi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnAnastesi.setMnemonic('2');
        BtnAnastesi.setToolTipText("Alt+2");
        BtnAnastesi.setName("BtnAnastesi"); // NOI18N
        BtnAnastesi.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAnastesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAnastesiActionPerformed(evt);
            }
        });
        FormInput.add(BtnAnastesi);
        BtnAnastesi.setBounds(790, 70, 28, 23);

        btnAsis2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnAsis2.setMnemonic('2');
        btnAsis2.setToolTipText("Alt+2");
        btnAsis2.setName("btnAsis2"); // NOI18N
        btnAsis2.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAsis2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsis2ActionPerformed(evt);
            }
        });
        FormInput.add(btnAsis2);
        btnAsis2.setBounds(370, 130, 28, 23);

        nmasistoperator2.setEditable(false);
        nmasistoperator2.setName("nmasistoperator2"); // NOI18N
        nmasistoperator2.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(nmasistoperator2);
        nmasistoperator2.setBounds(180, 130, 190, 23);

        kdasistoperator2.setEditable(false);
        kdasistoperator2.setName("kdasistoperator2"); // NOI18N
        kdasistoperator2.setPreferredSize(new java.awt.Dimension(80, 23));
        kdasistoperator2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdasistoperator2KeyPressed(evt);
            }
        });
        FormInput.add(kdasistoperator2);
        kdasistoperator2.setBounds(80, 130, 100, 23);

        label18.setText("Asisten 2 :");
        label18.setName("label18"); // NOI18N
        label18.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label18);
        label18.setBounds(0, 130, 80, 23);

        btnAsis3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnAsis3.setMnemonic('2');
        btnAsis3.setToolTipText("Alt+2");
        btnAsis3.setName("btnAsis3"); // NOI18N
        btnAsis3.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAsis3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsis3ActionPerformed(evt);
            }
        });
        FormInput.add(btnAsis3);
        btnAsis3.setBounds(370, 160, 28, 23);

        nminstrumen.setEditable(false);
        nminstrumen.setName("nminstrumen"); // NOI18N
        nminstrumen.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(nminstrumen);
        nminstrumen.setBounds(180, 160, 190, 23);

        kdInstrumen.setEditable(false);
        kdInstrumen.setName("kdInstrumen"); // NOI18N
        kdInstrumen.setPreferredSize(new java.awt.Dimension(80, 23));
        kdInstrumen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdInstrumenKeyPressed(evt);
            }
        });
        FormInput.add(kdInstrumen);
        kdInstrumen.setBounds(80, 160, 100, 23);

        label23.setText("Instrumen :");
        label23.setName("label23"); // NOI18N
        label23.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label23);
        label23.setBounds(0, 160, 81, 23);

        label26.setText("Penata Anestesi 1 :");
        label26.setName("label26"); // NOI18N
        label26.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label26);
        label26.setBounds(400, 100, 101, 23);

        kdasistanestesi.setEditable(false);
        kdasistanestesi.setName("kdasistanestesi"); // NOI18N
        kdasistanestesi.setPreferredSize(new java.awt.Dimension(80, 23));
        kdasistanestesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdasistanestesiKeyPressed(evt);
            }
        });
        FormInput.add(kdasistanestesi);
        kdasistanestesi.setBounds(500, 100, 100, 23);

        nmasistanestesi.setEditable(false);
        nmasistanestesi.setName("nmasistanestesi"); // NOI18N
        nmasistanestesi.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(nmasistanestesi);
        nmasistanestesi.setBounds(600, 100, 190, 23);

        BtnAsnes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnAsnes.setMnemonic('2');
        BtnAsnes.setToolTipText("Alt+2");
        BtnAsnes.setName("BtnAsnes"); // NOI18N
        BtnAsnes.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAsnes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAsnesActionPerformed(evt);
            }
        });
        FormInput.add(BtnAsnes);
        BtnAsnes.setBounds(790, 100, 28, 23);

        jLabel5.setText("Jenis Operasi :");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(10, 190, 81, 23);

        Kategori.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Elektif", "Cito", "ODC", "Khusus", "Besar", "Sedangkecil", "Bersih", "Bersih Terkontaminasi", "Terkontaminasi", "Kotor" }));
        Kategori.setName("Kategori"); // NOI18N
        Kategori.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KategoriKeyPressed(evt);
            }
        });
        FormInput.add(Kategori);
        Kategori.setBounds(90, 190, 290, 23);

        label25.setText("Sirkuler :");
        label25.setName("label25"); // NOI18N
        label25.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label25);
        label25.setBounds(400, 130, 101, 23);

        kdonloop1.setEditable(false);
        kdonloop1.setName("kdonloop1"); // NOI18N
        kdonloop1.setPreferredSize(new java.awt.Dimension(80, 23));
        kdonloop1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdonloop1KeyPressed(evt);
            }
        });
        FormInput.add(kdonloop1);
        kdonloop1.setBounds(500, 130, 100, 23);

        nmonloop1.setEditable(false);
        nmonloop1.setName("nmonloop1"); // NOI18N
        nmonloop1.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(nmonloop1);
        nmonloop1.setBounds(600, 130, 190, 23);

        btnOnloop1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnOnloop1.setMnemonic('2');
        btnOnloop1.setToolTipText("Alt+2");
        btnOnloop1.setName("btnOnloop1"); // NOI18N
        btnOnloop1.setPreferredSize(new java.awt.Dimension(28, 23));
        btnOnloop1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOnloop1ActionPerformed(evt);
            }
        });
        FormInput.add(btnOnloop1);
        btnOnloop1.setBounds(790, 130, 28, 23);

        label12.setText("Selesai :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label12);
        label12.setBounds(530, 40, 101, 23);

        tgl2.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        tgl2.setName("tgl2"); // NOI18N
        tgl2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tgl2KeyPressed(evt);
            }
        });
        FormInput.add(tgl2);
        tgl2.setBounds(630, 40, 150, 23);

        PreOp.setHighlighter(null);
        PreOp.setName("PreOp"); // NOI18N
        PreOp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PreOpKeyPressed(evt);
            }
        });
        FormInput.add(PreOp);
        PreOp.setBounds(150, 250, 256, 23);

        jLabel6.setText("Diagnosis Pra-Operasi :");
        jLabel6.setName("jLabel6"); // NOI18N
        FormInput.add(jLabel6);
        jLabel6.setBounds(0, 250, 145, 23);

        jLabel7.setText("Diagnosis Pasca Operasi :");
        jLabel7.setName("jLabel7"); // NOI18N
        FormInput.add(jLabel7);
        jLabel7.setBounds(410, 250, 145, 23);

        PostOp.setHighlighter(null);
        PostOp.setName("PostOp"); // NOI18N
        PostOp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PostOpKeyPressed(evt);
            }
        });
        FormInput.add(PostOp);
        PostOp.setBounds(560, 250, 256, 23);

        jLabel8.setText("Tindakan Operasi :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(0, 280, 145, 23);

        Jaringan.setHighlighter(null);
        Jaringan.setName("Jaringan"); // NOI18N
        Jaringan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JaringanKeyPressed(evt);
            }
        });
        FormInput.add(Jaringan);
        Jaringan.setBounds(150, 280, 660, 23);

        jLabel9.setText("Pengiriman Spesimen PA :");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput.add(jLabel9);
        jLabel9.setBounds(0, 440, 145, 23);

        DikirimPA.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        DikirimPA.setName("DikirimPA"); // NOI18N
        DikirimPA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DikirimPAKeyPressed(evt);
            }
        });
        FormInput.add(DikirimPA);
        DikirimPA.setBounds(140, 440, 80, 23);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setName("scrollPane2"); // NOI18N

        Laporan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Laporan.setColumns(20);
        Laporan.setRows(5);
        Laporan.setName("Laporan"); // NOI18N
        scrollPane2.setViewportView(Laporan);

        FormInput.add(scrollPane2);
        scrollPane2.setBounds(20, 340, 790, 90);

        jLabel10.setText("Prosedur Operasi dan Penemuan Intra Operasi :");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput.add(jLabel10);
        jLabel10.setBounds(1, 310, 250, 23);

        jenis_anastesi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "General", "Spinal", "Epidural", "Lokal" }));
        jenis_anastesi.setName("jenis_anastesi"); // NOI18N
        jenis_anastesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jenis_anastesiKeyPressed(evt);
            }
        });
        FormInput.add(jenis_anastesi);
        jenis_anastesi.setBounds(510, 190, 280, 23);

        jLabel11.setText("Komplikasi :");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(10, 220, 81, 23);

        komplikasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        komplikasi.setName("komplikasi"); // NOI18N
        komplikasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                komplikasiKeyPressed(evt);
            }
        });
        FormInput.add(komplikasi);
        komplikasi.setBounds(90, 220, 290, 23);

        jLabel12.setText("Asal Spesimen :");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(230, 440, 100, 23);

        asal_spesimen.setHighlighter(null);
        asal_spesimen.setName("asal_spesimen"); // NOI18N
        asal_spesimen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                asal_spesimenKeyPressed(evt);
            }
        });
        FormInput.add(asal_spesimen);
        asal_spesimen.setBounds(340, 440, 240, 23);

        kultur.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        kultur.setName("kultur"); // NOI18N
        kultur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kulturKeyPressed(evt);
            }
        });
        FormInput.add(kultur);
        kultur.setBounds(720, 440, 80, 23);

        jLabel13.setText("Kultur :");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(660, 440, 60, 23);

        jLabel14.setText("Perdarahan :");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(0, 470, 145, 23);

        perdarahan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        perdarahan.setName("perdarahan"); // NOI18N
        perdarahan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                perdarahanKeyPressed(evt);
            }
        });
        FormInput.add(perdarahan);
        perdarahan.setBounds(140, 470, 80, 23);

        jLabel15.setText("Ket. Transfusi :");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput.add(jLabel15);
        jLabel15.setBounds(440, 470, 90, 23);

        transfusi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        transfusi.setName("transfusi"); // NOI18N
        transfusi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                transfusiKeyPressed(evt);
            }
        });
        FormInput.add(transfusi);
        transfusi.setBounds(340, 470, 70, 23);

        jLabel16.setText("Transfusi :");
        jLabel16.setName("jLabel16"); // NOI18N
        FormInput.add(jLabel16);
        jLabel16.setBounds(250, 470, 80, 23);

        ket_transfusi.setHighlighter(null);
        ket_transfusi.setName("ket_transfusi"); // NOI18N
        ket_transfusi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ket_transfusiKeyPressed(evt);
            }
        });
        FormInput.add(ket_transfusi);
        ket_transfusi.setBounds(530, 470, 270, 23);

        jLabel17.setText("No. Pendaftaran Implant :");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput.add(jLabel17);
        jLabel17.setBounds(410, 223, 145, 23);

        no_implant.setHighlighter(null);
        no_implant.setName("no_implant"); // NOI18N
        no_implant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                no_implantKeyPressed(evt);
            }
        });
        FormInput.add(no_implant);
        no_implant.setBounds(560, 223, 256, 23);

        scrollPane1.setViewportView(FormInput);

        PanelInput.add(scrollPane1, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
            dispose();  
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){            
            dispose();              
        }else{Valid.pindah(evt,TCari,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed
/*
private void KdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKdKeyPressed
    Valid.pindah(evt,BtnCari,Nm);
}//GEN-LAST:event_TKdKeyPressed
*/

private void kdoperator1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdoperator1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            nmoperator1.setText(dokter.tampil3(kdoperator1.getText()));
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnOperator1ActionPerformed(null);
        }else{
            Valid.pindah(evt,tgl,kdoperator1);
        }
}//GEN-LAST:event_kdoperator1KeyPressed

private void BtnOperator1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnOperator1ActionPerformed
        pilihan=1;
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
}//GEN-LAST:event_BtnOperator1ActionPerformed

private void tglKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tglKeyPressed
        Valid.pindah(evt,jenis_anastesi,BtnOperator1);
}//GEN-LAST:event_tglKeyPressed

private void ppLaporanOperasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppLaporanOperasiActionPerformed
    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Map<String, Object> param = new HashMap<>();                 
        param.put("namars",akses.getnamars());
        param.put("alamatrs",akses.getalamatrs());
        param.put("kotars",akses.getkabupatenrs());
        param.put("propinsirs",akses.getpropinsirs());
        param.put("kontakrs",akses.getkontakrs());
        param.put("emailrs",akses.getemailrs());   
        param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
        param.put("norawat",TNoRw.getText());  
        if(Sequel.cariInteger("select count(kamar_inap.no_rawat) from kamar_inap where kamar_inap.no_rawat='"+TNoRw.getText()+"'")>0){
            param.put("ruang", Sequel.cariIsi("select bangsal.nm_bangsal from bangsal inner join kamar on kamar.kd_bangsal=bangsal.kd_bangsal inner join kamar_inap on kamar_inap.kd_kamar=kamar.kd_kamar where kamar_inap.no_rawat='"+TNoRw.getText()+"'"));
            param.put("kelas", Sequel.cariIsi("select kamar.kelas from kamar inner join kamar_inap on kamar_inap.kd_kamar=kamar.kd_kamar where kamar_inap.no_rawat='"+TNoRw.getText()+"'"));
        }else{
            param.put("ruang", Sequel.cariIsi("select poliklinik.nm_poli from poliklinik inner join reg_periksa on reg_periksa.kd_poli=poliklinik.kd_poli where reg_periksa.no_rawat='"+TNoRw.getText()+"'"));
            param.put("kelas", "-");
        }
//        param.put("norawat",tbtindakan.getValueAt(tbtindakan.getSelectedRow(),0).toString());  
        finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",Sequel.cariIsi("select operator from laporan_operasi_rsi where laporan_operasi_rsi.no_rawat='"+TNoRw.getText()+"'"));
        param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 3).toString()+"\nID "+Valid.SetTgl3(tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 3).toString()));
//        param.put("finger","Tes");
        param.put("operator",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 4).toString());
        param.put("asisten1",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 5).toString());
        param.put("asisten2",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 6).toString());
        param.put("instrumen",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 7).toString());
        param.put("anastesi",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 8).toString());
        param.put("penata_anastesi",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 9).toString());
        param.put("sirkuler",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 10).toString());
//        Valid.MyReportqry("rptLaporanOperasi2.jasper","report","::[ Laporan Operasi Pasien ]::","select pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.no_rawat,laporan_operasi_rsi.operator,laporan_operasi_rsi.asisten1,"
//                + "laporan_operasi_rsi.asisten2,laporan_operasi_rsi.instrumen,laporan_operasi_rsi.anastesi,laporan_operasi_rsi.penata_anastesi,laporan_operasi_rsi.sirkuler,"
//                + "laporan_operasi_rsi.jenis_operasi,laporan_operasi_rsi.jenis_anestesi,laporan_operasi_rsi.komplikasi,laporan_operasi_rsi.no_pendaftaran_implant,"
//                + "laporan_operasi_rsi.diagnosa_preop,laporan_operasi_rsi.diagnosa_postop,laporan_operasi_rsi.jaringan_dieksekusi,laporan_operasi_rsi.selesaioperasi,"
//                + "laporan_operasi_rsi.permintaan_pa,laporan_operasi_rsi.asal_spesimen,laporan_operasi_rsi.kultur,laporan_operasi_rsi.perdarahan,laporan_operasi_rsi.transfusi,"
//                + "laporan_operasi_rsi.ket_transfusi,laporan_operasi_rsi.laporan_operasi,reg_periksa.umurdaftar,reg_periksa.sttsumur,penjab.png_jawab as penjab,"
//                + "pasien.tgl_lahir,pasien.jk,laporan_operasi_rsi.tanggal,kamar.kelas,bangsal.nm_bangsal from laporan_operasi_rsi "
//                + "inner join reg_periksa on reg_periksa.no_rawat=laporan_operasi_rsi.no_rawat "
//                + "inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis "
//                + "inner join kamar_inap on reg_periksa.no_rawat=reg_periksa.no_rawat "
//                + "inner join kamar on kamar.kd_kamar=kamar_inap.kd_kamar "
//                + "inner join bangsal on bangsal.kd_bangsal=kamar.kd_bangsal "
//                + "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj where laporan_operasi_rsi.no_rawat='"+TNoRw.getText()+"'",param);
        Valid.MyReport("rptLaporanOperasi2.jasper","report","::[ Laporan Operasi ]::",param);
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_ppLaporanOperasiActionPerformed

private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select concat(pasien.no_rkm_medis,', ',pasien.nm_pasien) from reg_periksa inner join pasien "+
                        " on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where reg_periksa.no_rawat=? ",TPasien,TNoRw.getText());
        }else{            
            Valid.pindah(evt,TCari,kdoperator1);
        }
}//GEN-LAST:event_TNoRwKeyPressed

private void kdasistoperator1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdasistoperator1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_UP){
            btnAsis1ActionPerformed(null);
        }else{
            Valid.pindah(evt,kdInstrumen,kdasistoperator2);
        }
          
}//GEN-LAST:event_kdasistoperator1KeyPressed

private void btnAsis1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsis1ActionPerformed
    pilihan=1;
    petugas.emptTeks();
    petugas.isCek();
    petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
    petugas.setLocationRelativeTo(internalFrame1);
    petugas.setVisible(true);
}//GEN-LAST:event_btnAsis1ActionPerformed

private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
    if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCari2ActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari2.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

private void BtnCari2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari2ActionPerformed
   tampil();
}//GEN-LAST:event_BtnCari2ActionPerformed

private void BtnCari2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCari2KeyPressed
// TODO add your handling code here:
}//GEN-LAST:event_BtnCari2KeyPressed

private void BtnAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAll1ActionPerformed
  TCari.setText("");
  tampil();
}//GEN-LAST:event_BtnAll1ActionPerformed

private void BtnAll1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAll1KeyPressed
// TODO add your handling code here:
}//GEN-LAST:event_BtnAll1KeyPressed

private void tbtindakanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbtindakanMouseClicked
       if(tbtindakan.getRowCount()!=0){
            try {
                getData2();
            } catch (java.lang.NullPointerException e) {
                System.out.println(e);
            }
        }
}//GEN-LAST:event_tbtindakanMouseClicked

private void tbtindakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbtindakanKeyPressed
    if(tbtindakan.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                try {
                    int row=tbtindakan.getSelectedColumn();
                    if((row!=0)||(row!=28)){
                        if(tbtindakan.getSelectedRow()>-1){
                          tbtindakan.setValueAt(true,tbtindakan.getSelectedRow(),0);   
                        }                               
                        TCari.setText("");
                        TCari.requestFocus();
                    }                    
                    getData2();
                } catch (java.lang.NullPointerException e) {
                }
            }else if((evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData2();
                } catch (java.lang.NullPointerException e) {
                }
            }
    }
}//GEN-LAST:event_tbtindakanKeyPressed

private void kdanestesiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdanestesiKeyPressed
   if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            nmanestesi.setText(dokter.tampil3(kdanestesi.getText()));
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnAnastesiActionPerformed(null);
        }else{
            Valid.pindah(evt,kdoperator1,kdanestesi);
        }
}//GEN-LAST:event_kdanestesiKeyPressed

private void BtnAnastesiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAnastesiActionPerformed
    pilihan=2;
    dokter.emptTeks();
    dokter.isCek();
    dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
    dokter.setLocationRelativeTo(internalFrame1);
    dokter.setVisible(true);
}//GEN-LAST:event_BtnAnastesiActionPerformed

private void btnAsis2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsis2ActionPerformed
    pilihan=2;
    petugas.emptTeks();
    petugas.isCek();
    petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
    petugas.setLocationRelativeTo(internalFrame1);
    petugas.setVisible(true);
}//GEN-LAST:event_btnAsis2ActionPerformed

private void kdasistoperator2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdasistoperator2KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_UP){
            btnAsis2ActionPerformed(null);
        }else{
            Valid.pindah(evt,kdasistoperator1,kdasistoperator2);
        }
}//GEN-LAST:event_kdasistoperator2KeyPressed

private void btnAsis3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsis3ActionPerformed
    pilihan=3;
    petugas.emptTeks();
    petugas.isCek();
    petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
    petugas.setLocationRelativeTo(internalFrame1);
    petugas.setVisible(true);
}//GEN-LAST:event_btnAsis3ActionPerformed

private void kdInstrumenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdInstrumenKeyPressed
   if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nama from petugas where nip='"+kdInstrumen.getText()+"'",nminstrumen);            
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            btnAsis3ActionPerformed(null);
        }else{
            Valid.pindah(evt,kdInstrumen,kdasistoperator1);
        }         
}//GEN-LAST:event_kdInstrumenKeyPressed

private void kdasistanestesiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdasistanestesiKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnAsnesActionPerformed(null);
        }else{
//            Valid.pindah(evt,kdasistoperator3,kdasistanestesi2);
        }
}//GEN-LAST:event_kdasistanestesiKeyPressed

private void BtnAsnesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAsnesActionPerformed
    pilihan=4;
    petugas.emptTeks();
    petugas.isCek();
    petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
    petugas.setLocationRelativeTo(internalFrame1);
    petugas.setVisible(true);
}//GEN-LAST:event_BtnAsnesActionPerformed

private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
   isForm();  
}//GEN-LAST:event_ChkInputActionPerformed

    private void KategoriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KategoriKeyPressed
//        Valid.pindah(evt,TCariPaket,jenis);
    }//GEN-LAST:event_KategoriKeyPressed

    private void kdonloop1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdonloop1KeyPressed
//        Valid.pindah(evt,kdprwresust,kdonloop2);
    }//GEN-LAST:event_kdonloop1KeyPressed

    private void btnOnloop1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOnloop1ActionPerformed
        pilihan=5;
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_btnOnloop1ActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
//            Valid.pindah(evt,kddrumum,BtnKeluar);
        }
    }//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(kdanestesi.getText().trim().equals("")||nmanestesi.getText().trim().equals("")){
            kdanestesi.setText("-");
            nmanestesi.setText("-");
        }

        if(kdonloop1.getText().trim().equals("")||nmonloop1.getText().trim().equals("")){
            kdonloop1.setText("-");
            nmonloop1.setText("-");
        }

        if(kdasistoperator1.getText().trim().equals("")||nmasistoperator1.getText().trim().equals("")){
            kdasistoperator1.setText("-");
            nmasistoperator1.setText("-");
        }

        if(kdasistoperator2.getText().trim().equals("")||nmasistoperator2.getText().trim().equals("")){
            kdasistoperator2.setText("-");
            nmasistoperator2.setText("-");
        }
        
        if(kdInstrumen.getText().trim().equals("")||nminstrumen.getText().trim().equals("")){
            kdInstrumen.setText("-");
            nminstrumen.setText("-");
        }

        if(kdasistanestesi.getText().trim().equals("")||nmasistanestesi.getText().trim().equals("")){
            kdasistanestesi.setText("-");
            nmasistanestesi.setText("-");
        }
        
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Pasien");
        }else if(kdoperator1.getText().trim().equals("")||nmoperator1.getText().trim().equals("")){
            Valid.textKosong(kdoperator1,"Operator 1");
        }else if(kdanestesi.getText().trim().equals("")||nmanestesi.getText().trim().equals("")){
            Valid.textKosong(kdanestesi,"dr Anestesi");
        }else if(kdonloop1.getText().trim().equals("")||nmonloop1.getText().trim().equals("")){
            Valid.textKosong(kdonloop1,"Onloop 1");
        }else if(kdasistoperator1.getText().trim().equals("")||nmasistoperator1.getText().trim().equals("")){
            Valid.textKosong(kdasistoperator1,"Asisten Operator 1");
        }else if(kdasistoperator2.getText().trim().equals("")||nmasistoperator2.getText().trim().equals("")){
            Valid.textKosong(kdasistoperator2,"Asisten Operator 2");
        }else if(kdInstrumen.getText().trim().equals("")||nminstrumen.getText().trim().equals("")){
            Valid.textKosong(kdInstrumen,"Instrumen");
        }else if(kdasistanestesi.getText().trim().equals("")||nmasistanestesi.getText().trim().equals("")){
            Valid.textKosong(kdasistanestesi,"Asisten Anastesi 1");
        }else{            
            if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                TCari.requestFocus();
            }else{
                Sequel.AutoComitFalse();
                sukses=true;
                if(Sequel.menyimpantf2("laporan_operasi_rsi","'"+TNoRw.getText()+"','"+Valid.SetTgl(tgl.getSelectedItem()+"")+" "+tgl.getSelectedItem().toString().substring(11,19)
                    +"','"+kdoperator1.getText()+"','"+kdasistoperator1.getText()+"','"+kdasistoperator2.getText()+"','"+kdInstrumen.getText()
                    +"','"+kdanestesi.getText()+"','"+kdasistanestesi.getText() +"','"+kdonloop1.getText()
                    +"','"+Kategori.getSelectedItem().toString()+"','"+jenis_anastesi.getSelectedItem().toString()
                    +"','"+komplikasi.getSelectedItem().toString()+"','"+no_implant.getText()+"','"+PreOp.getText()
                    +"','"+PostOp.getText()+"','"+Jaringan.getText()
                    +"','"+Valid.SetTgl(tgl2.getSelectedItem()+"")+" "+tgl2.getSelectedItem().toString().substring(11,19)
                    +"','"+DikirimPA.getSelectedItem().toString()+"','"+asal_spesimen.getText()+"','"+kultur.getSelectedItem().toString()
                    +"','"+perdarahan.getSelectedItem().toString()+"','"+transfusi.getSelectedItem().toString()
                    +"','"+ket_transfusi.getText()+"','"+Laporan.getText()+"'","data")==true){
                }else{
                    sukses=false;
                }
                Sequel.AutoComitTrue();
            }
        }
    }//GEN-LAST:event_BtnSimpanActionPerformed

    private void tgl2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tgl2KeyPressed
//        Valid.pindah(evt,kdonloop5,PreOp);
    }//GEN-LAST:event_tgl2KeyPressed

    private void PreOpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PreOpKeyPressed
        Valid.pindah(evt,tgl2,PostOp);
    }//GEN-LAST:event_PreOpKeyPressed

    private void PostOpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PostOpKeyPressed
        Valid.pindah(evt,PreOp,Jaringan);
    }//GEN-LAST:event_PostOpKeyPressed

    private void JaringanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JaringanKeyPressed
        Valid.pindah(evt,PostOp,DikirimPA);
    }//GEN-LAST:event_JaringanKeyPressed

    private void DikirimPAKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DikirimPAKeyPressed
        Valid.pindah(evt,Jaringan,Laporan);
    }//GEN-LAST:event_DikirimPAKeyPressed

    private void BtnOperator1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnOperator1KeyPressed
//        Valid.pindah(evt,tgl,BtnOperator2);
    }//GEN-LAST:event_BtnOperator1KeyPressed

    private void jenis_anastesiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jenis_anastesiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jenis_anastesiKeyPressed

    private void komplikasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_komplikasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_komplikasiKeyPressed

    private void asal_spesimenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_asal_spesimenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_asal_spesimenKeyPressed

    private void kulturKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kulturKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kulturKeyPressed

    private void perdarahanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_perdarahanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_perdarahanKeyPressed

    private void transfusiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_transfusiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_transfusiKeyPressed

    private void ket_transfusiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ket_transfusiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ket_transfusiKeyPressed

    private void no_implantKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_no_implantKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_no_implantKeyPressed

    private void BtnSimpan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpan1ActionPerformed
        Map<String, Object> param = new HashMap<>();                 
        param.put("namars",akses.getnamars());
        param.put("alamatrs",akses.getalamatrs());
        param.put("kotars",akses.getkabupatenrs());
        param.put("propinsirs",akses.getpropinsirs());
        param.put("kontakrs",akses.getkontakrs());
        param.put("emailrs",akses.getemailrs());   
        param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
//            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",Sequel.cariIsi("select operator from laporan_operasi_rsi where laporan_operasi_rsi.no_rawat='"+TNoRw.getText()+"'"));
//        param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 3).toString()+"\nID "+Valid.SetTgl3(tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 3).toString()));
        param.put("operator",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 3).toString());
        param.put("asisten1",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 4).toString());
        param.put("asisten2",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 5).toString());
        param.put("instrumen",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 6).toString());
        param.put("anastesi",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 7).toString());
        param.put("penata_anastesi",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 8).toString());
        param.put("sirkuler",tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 9).toString());
        Valid.MyReportqry("rptLaporanOperasi2.jasper","report","::[ Laporan Operasi Pasien ]::","select pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.no_rawat,laporan_operasi_rsi.operator,laporan_operasi_rsi.asisten1,"
                + "laporan_operasi_rsi.asisten2,laporan_operasi_rsi.instrumen,laporan_operasi_rsi.anastesi,laporan_operasi_rsi.penata_anastesi,laporan_operasi_rsi.sirkuler,"
                + "laporan_operasi_rsi.jenis_operasi,laporan_operasi_rsi.jenis_anestesi,laporan_operasi_rsi.komplikasi,laporan_operasi_rsi.no_pendaftaran_implant,"
                + "laporan_operasi_rsi.diagnosa_preop,laporan_operasi_rsi.diagnosa_postop,laporan_operasi_rsi.jaringan_dieksekusi,laporan_operasi_rsi.selesaioperasi,"
                + "laporan_operasi_rsi.permintaan_pa,laporan_operasi_rsi.asal_spesimen,laporan_operasi_rsi.kultur,laporan_operasi_rsi.perdarahan,laporan_operasi_rsi.transfusi,"
                + "laporan_operasi_rsi.ket_transfusi,laporan_operasi_rsi.laporan_operasi,reg_periksa.umurdaftar,reg_periksa.sttsumur,penjab.png_jawab as penjab,"
                + "pasien.tgl_lahir,pasien.jk,laporan_operasi_rsi.tanggal,kamar.kelas,bangsal.nm_bangsal from laporan_operasi_rsi "
                + "inner join reg_periksa on reg_periksa.no_rawat=laporan_operasi_rsi.no_rawat "
                + "inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis "
                + "inner join kamar_inap on reg_periksa.no_rawat=reg_periksa.no_rawat "
                + "inner join kamar on kamar.kd_kamar=kamar_inap.kd_kamar "
                + "inner join bangsal on bangsal.kd_bangsal=kamar.kd_bangsal "
                + "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj where laporan_operasi_rsi.no_rawat='"+TNoRw.getText()+"'",param);
    }//GEN-LAST:event_BtnSimpan1ActionPerformed

    private void BtnSimpan1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpan1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSimpan1KeyPressed

    private void kdoperator1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kdoperator1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kdoperator1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgLaporanOperasi dialog = new DlgLaporanOperasi(new javax.swing.JFrame(), true);
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
    private widget.Button BtnAll1;
    private widget.Button BtnAnastesi;
    private widget.Button BtnAsnes;
    private widget.Button BtnCari2;
    private widget.Button BtnKeluar;
    private widget.Button BtnOperator1;
    private widget.Button BtnSimpan;
    private widget.Button BtnSimpan1;
    private widget.CekBox ChkInput;
    private widget.ComboBox DikirimPA;
    private widget.panelisi FormInput;
    private widget.TextBox Jaringan;
    private widget.ComboBox Kategori;
    private widget.TextBox Kd2;
    private widget.TextArea Laporan;
    private javax.swing.JPanel PanelInput;
    private javax.swing.JPopupMenu Popup;
    private widget.TextBox PostOp;
    private widget.TextBox PreOp;
    private widget.ScrollPane Scroll1;
    private widget.TextBox TCari;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox asal_spesimen;
    private widget.Button btnAsis1;
    private widget.Button btnAsis2;
    private widget.Button btnAsis3;
    private widget.Button btnOnloop1;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel3;
    private widget.Label jLabel4;
    private widget.Label jLabel5;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private widget.ComboBox jenis_anastesi;
    private widget.TextBox kdInstrumen;
    private widget.TextBox kdanestesi;
    private widget.TextBox kdasistanestesi;
    private widget.TextBox kdasistoperator1;
    private widget.TextBox kdasistoperator2;
    private widget.TextBox kdonloop1;
    private widget.TextBox kdoperator1;
    private widget.TextBox ket_transfusi;
    private widget.ComboBox komplikasi;
    private widget.ComboBox kultur;
    private widget.Label label10;
    private widget.Label label11;
    private widget.Label label12;
    private widget.Label label14;
    private widget.Label label17;
    private widget.Label label18;
    private widget.Label label21;
    private widget.Label label23;
    private widget.Label label25;
    private widget.Label label26;
    private widget.TextBox nmanestesi;
    private widget.TextBox nmasistanestesi;
    private widget.TextBox nmasistoperator1;
    private widget.TextBox nmasistoperator2;
    private widget.TextBox nminstrumen;
    private widget.TextBox nmonloop1;
    private widget.TextBox nmoperator1;
    private widget.TextBox no_implant;
    private widget.panelisi panelisi1;
    private widget.panelisi panelisi5;
    private widget.ComboBox perdarahan;
    private javax.swing.JMenuItem ppLaporanOperasi;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.Table tbtindakan;
    private widget.Tanggal tgl;
    private widget.Tanggal tgl2;
    private widget.ComboBox transfusi;
    // End of variables declaration//GEN-END:variables

    public void tampil() {  
        Valid.tabelKosong(tabMode);
        try{
            ps=koneksi.prepareStatement("select pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.no_rawat,laporan_operasi_rsi.operator,laporan_operasi_rsi.asisten1,"
                    + "laporan_operasi_rsi.asisten2,laporan_operasi_rsi.instrumen,laporan_operasi_rsi.anastesi,laporan_operasi_rsi.penata_anastesi,laporan_operasi_rsi.sirkuler,"
                    + "laporan_operasi_rsi.jenis_operasi,laporan_operasi_rsi.jenis_anestesi,laporan_operasi_rsi.komplikasi,laporan_operasi_rsi.no_pendaftaran_implant,"
                    + "laporan_operasi_rsi.diagnosa_preop,laporan_operasi_rsi.diagnosa_postop,laporan_operasi_rsi.jaringan_dieksekusi,laporan_operasi_rsi.selesaioperasi,"
                    + "laporan_operasi_rsi.permintaan_pa,laporan_operasi_rsi.asal_spesimen,laporan_operasi_rsi.kultur,laporan_operasi_rsi.perdarahan,laporan_operasi_rsi.transfusi,"
                    + "laporan_operasi_rsi.ket_transfusi,laporan_operasi_rsi.laporan_operasi,laporan_operasi_rsi.tanggal from laporan_operasi_rsi "
                    + "inner join reg_periksa on reg_periksa.no_rawat=laporan_operasi_rsi.no_rawat "
                    + "inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis "
                    + "where laporan_operasi_rsi.no_rawat like ? or pasien.nm_pasien like ? or pasien.no_rkm_medis like ? ");
            try{
                ps.setString(1,"%"+TCari.getText().trim()+"%");
                ps.setString(2,"%"+TCari.getText().trim()+"%");
                ps.setString(3,"%"+TCari.getText().trim()+"%");
                rs=ps.executeQuery();
                while(rs.next()){
                tabMode.addRow(new String[]{
                    rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("tanggal"),
                    dokter.tampil3(rs.getString("operator")),petugas.tampil3(rs.getString("asisten1")),petugas.tampil3(rs.getString("asisten2")),
                    petugas.tampil3(rs.getString("instrumen")),dokter.tampil3(rs.getString("anastesi")),petugas.tampil3(rs.getString("penata_anastesi")),
                    petugas.tampil3(rs.getString("sirkuler")),rs.getString("jenis_operasi"),rs.getString("jenis_anestesi"),rs.getString("komplikasi"),rs.getString("no_pendaftaran_implant"),
                    rs.getString("diagnosa_preop"),rs.getString("diagnosa_postop"),rs.getString("jaringan_dieksekusi"),rs.getString("selesaioperasi"),
                    rs.getString("permintaan_pa"),rs.getString("asal_spesimen"),rs.getString("kultur"),rs.getString("perdarahan"),rs.getString("transfusi"),
                    rs.getString("ket_transfusi"),rs.getString("laporan_operasi")
                });
                }
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
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
    
    private void getData2(){
       TNoRw.setText(tbtindakan.getValueAt(tbtindakan.getSelectedRow(), 0).toString());
    }
    
    public void setNoRm(String norm,String nama,String posisi){
        TNoRw.setText(norm);
        TPasien.setText(nama);
        TCari.setText(norm);
        this.status=posisi;
        this.kd_pj=Sequel.cariIsi("select reg_periksa.kd_pj from reg_periksa where reg_periksa.no_rawat=?",norm);        
        if(status.equals("Ranap")){
            norawatibu=Sequel.cariIsi("select ranap_gabung.no_rawat from ranap_gabung where ranap_gabung.no_rawat2=?",TNoRw.getText());
        
            if(!norawatibu.equals("")){
                kelas=Sequel.cariIsi(
                    "select kamar.kelas from kamar inner join kamar_inap "+
                    "on kamar.kd_kamar=kamar_inap.kd_kamar where no_rawat=? "+
                    "and stts_pulang='-' order by STR_TO_DATE(concat(kamar_inap.tgl_masuk,' ',jam_masuk),'%Y-%m-%d %H:%i:%s') desc limit 1",norawatibu);
            }else{
                kelas=Sequel.cariIsi(
                    "select kamar.kelas from kamar inner join kamar_inap "+
                    "on kamar.kd_kamar=kamar_inap.kd_kamar where no_rawat=? "+
                    "and stts_pulang='-' order by STR_TO_DATE(concat(kamar_inap.tgl_masuk,' ',jam_masuk),'%Y-%m-%d %H:%i:%s') desc limit 1",TNoRw.getText());
            } 
        }else if(status.equals("Ralan")){
            kelas="Rawat Jalan";
        }
        tampil();
    }
    
    public void setNoRm(String norm,String nama,String posisi,String KodeOperator,String NamaOperator){
        TNoRw.setText(norm);
        TPasien.setText(nama);
        this.status=posisi;
        this.kd_pj=Sequel.cariIsi("select reg_periksa.kd_pj from reg_periksa where reg_periksa.no_rawat=?",norm);        
        if(status.equals("Ranap")){
            norawatibu=Sequel.cariIsi("select ranap_gabung.no_rawat from ranap_gabung where ranap_gabung.no_rawat2=?",TNoRw.getText());
        
            if(!norawatibu.equals("")){
                kelas=Sequel.cariIsi(
                    "select kamar.kelas from kamar inner join kamar_inap "+
                    "on kamar.kd_kamar=kamar_inap.kd_kamar where no_rawat=? "+
                    "and stts_pulang='-' order by STR_TO_DATE(concat(kamar_inap.tgl_masuk,' ',jam_masuk),'%Y-%m-%d %H:%i:%s') desc limit 1",norawatibu);
            }else{
                kelas=Sequel.cariIsi(
                    "select kamar.kelas from kamar inner join kamar_inap "+
                    "on kamar.kd_kamar=kamar_inap.kd_kamar where no_rawat=? "+
                    "and stts_pulang='-' order by STR_TO_DATE(concat(kamar_inap.tgl_masuk,' ',jam_masuk),'%Y-%m-%d %H:%i:%s') desc limit 1",TNoRw.getText());
            } 
        }else if(status.equals("Ralan")){
            kelas="Rawat Jalan";
        }
        tampil();
        kdoperator1.setText(KodeOperator);
        nmoperator1.setText(NamaOperator);
    }
    
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,internalFrame1.getHeight()-79));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void emptTeks(){
        kdasistoperator1.setText("");
        kdasistoperator2.setText("");
    }
    
    public void SetCariOperasi(String Operasi,String kodedokter,String namadokter){
        TCari.setText(Operasi);
        kdoperator1.setText(kodedokter);
        nmoperator1.setText(namadokter);
    }
 
}
