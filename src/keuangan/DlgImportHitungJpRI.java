/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgJadwal.java
 *
 * Created on May 22, 2010, 10:25:16 PM
 */

package keuangan;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author dosen
 */
public class DlgImportHitungJpRI extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps,pssimpan;
    private ResultSet rs;
    private DlgRekeningTahun rekening=new DlgRekeningTahun(null,false);
    File pilihfile;

    /** Creates new form DlgJadwal
     * @param parent
     * @param modal */
    public DlgImportHitungJpRI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(8,1);
        setSize(628,674);

        Object[] row={"P","terklaim","terbagi","admission_date","discharge_date","inacbgs","deskripsi_inacbg","total_tarif","tarif_rs","nama_pasien","mrn",
            "dpjp","id dokter","sep","d000001","d000002","d000003","d000004","d000005","d000006","d000007","d000008","d000017","d000018","d000067","d000030",
            "d000033","d000034","d000035","d000036","d000037","d000039","d000044","d000027","d000062","d000026","radd000017","bdrsd000026","d000009","d000010",
            "d000011","d000012","d000049","d000014","d000015","d000016","d000019","d000020","d000021","d000022","d000023","d000048","d000025","d000038","d000028",
            "d000031","d000032","d000068","d000069","d000070","d000071","d000072","d000073","d000065","d000066","d000051","d000052","d000053","d000054","d000055",
            "d000056","d000057","bd000005","bd000006","bd000007","bd000030","bd000039","bd000035","bd000018","bd000034","d000002","dr_anestesi","bpd000005","bpd000006",
            "bpd000007","bpd000030","bpd000039","bpd000035","bpd000018","bpd000034","dr_pendamping","bld000018","bld000006","bld000002","bld000030","bld000039","bld000035",
            "bld000034","bld000036","ibs operator","ibs anestesi","ibs pendamping","ibs","poli","ugd","vk ugd","irna i","irna ii","irna iii","irna anak","irna paru",
            "icu","nicu","iidb","iidb vk","isolasi","isolasi tk. negatif","ugd isolasi","gizi","radiologi","laboratorium","bdrs","farmasi",
            "kel. apoteker","kel. admin","direksi","manajemen","manajemen teknis","post remun","cek konversi","dpjpanestesi","Tanggal Dibagi","Periode Layanan","Cara Bayar","Kategori Layanan"};
        tabMode=new DefaultTableModel(null,row){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbJadwal.setModel(tabMode);

        tbJadwal.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbJadwal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 135; i++) {
            TableColumn column = tbJadwal.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(80);
            }else if(i==2){
                column.setPreferredWidth(80);
            }else{
                column.setPreferredWidth(100);
            }
        }
        
        tbJadwal.setDefaultRenderer(Object.class, new WarnaTable());
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
//        nama.setDocument(new batasInput((byte)50).getKata(nama));
//        ppn.setDocument(new batasInput((byte)5).getKata(ppn));
//        kdrek.setDocument(new batasInput((byte)15).getKata(kdrek));
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
        
//        emptTeks();
    }
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbJadwal = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        PanelInput = new javax.swing.JPanel();
        FormInput = new widget.PanelBiasa();
        jLabel10 = new widget.Label();
        nmrek = new widget.TextBox();
        BtnPoli = new widget.Button();
        label11 = new widget.Label();
        Tgl1 = new widget.Tanggal();
        label18 = new widget.Label();
        TglPeriode = new widget.Tanggal();
        cmbCaraBayar = new widget.ComboBox();
        label13 = new widget.Label();
        cmbKategori = new widget.ComboBox();
        label12 = new widget.Label();
        ChkInput = new widget.CekBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ IMPORT HITUNG JP RI ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbJadwal.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbJadwal.setName("tbJadwal"); // NOI18N
        tbJadwal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbJadwalMouseClicked(evt);
            }
        });
        tbJadwal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbJadwalKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbJadwal);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(null);

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
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
        BtnSimpan.setBounds(6, 10, 100, 30);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
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
        BtnKeluar.setBounds(520, 10, 100, 30);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(340, 23));
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
        BtnAll.setMnemonic('4');
        BtnAll.setToolTipText("Alt+4");
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

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass9.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(50, 23));
        panelGlass9.add(LCount);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(865, 120));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(865, 137));
        FormInput.setLayout(null);

        jLabel10.setText("BROWSE :");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput.add(jLabel10);
        jLabel10.setBounds(0, 10, 80, 23);

        nmrek.setEditable(false);
        nmrek.setHighlighter(null);
        nmrek.setName("nmrek"); // NOI18N
        nmrek.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nmrekKeyPressed(evt);
            }
        });
        FormInput.add(nmrek);
        nmrek.setBounds(90, 10, 480, 23);

        BtnPoli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPoli.setMnemonic('2');
        BtnPoli.setToolTipText("ALt+2");
        BtnPoli.setName("BtnPoli"); // NOI18N
        BtnPoli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPoliActionPerformed(evt);
            }
        });
        FormInput.add(BtnPoli);
        BtnPoli.setBounds(580, 10, 28, 23);

        label11.setText("Tanggal :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label11);
        label11.setBounds(0, 40, 80, 23);

        Tgl1.setDisplayFormat("dd-MM-yyyy");
        Tgl1.setName("Tgl1"); // NOI18N
        Tgl1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl1KeyPressed(evt);
            }
        });
        FormInput.add(Tgl1);
        Tgl1.setBounds(90, 40, 100, 23);

        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText("Periode Pembagian :");
        label18.setName("label18"); // NOI18N
        label18.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label18);
        label18.setBounds(200, 40, 110, 23);

        TglPeriode.setDisplayFormat("MMMM-yyyy");
        TglPeriode.setName("TglPeriode"); // NOI18N
        TglPeriode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglPeriodeKeyPressed(evt);
            }
        });
        FormInput.add(TglPeriode);
        TglPeriode.setBounds(310, 40, 120, 23);

        cmbCaraBayar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BPJS", "UMUM", "BPJS TK", "ASURANSI", "-" }));
        cmbCaraBayar.setName("cmbCaraBayar"); // NOI18N
        FormInput.add(cmbCaraBayar);
        cmbCaraBayar.setBounds(90, 70, 160, 20);

        label13.setText("Kategori :");
        label13.setName("label13"); // NOI18N
        label13.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label13);
        label13.setBounds(300, 70, 80, 23);

        cmbKategori.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Reguler", "Pending 1", "Pending 2", "Pending 3" }));
        cmbKategori.setName("cmbKategori"); // NOI18N
        FormInput.add(cmbKategori);
        cmbKategori.setBounds(390, 70, 180, 20);

        label12.setText("Cara Bayar :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label12);
        label12.setBounds(0, 70, 80, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

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

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nmrekKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nmrekKeyPressed
        //Valid.pindah(evt,TJns,BtnSimpan);
}//GEN-LAST:event_nmrekKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if (pilihfile != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(pilihfile))) {

                String line;
                br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] data = line.split("\t");
                    String sql = "INSERT INTO import_hitung_jp_ri VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pssimpan = koneksi.prepareStatement(sql);
                    pssimpan.setString(1, data[0]);
                    pssimpan.setString(2, data[1]);
                    pssimpan.setString(3, Valid.SetTgl(data[2].replaceAll("/", "-")+""));
                    pssimpan.setString(4, Valid.SetTgl(data[3].replaceAll("/", "-")+""));
//                    pssimpan.setString(3, data[2]);
//                    pssimpan.setString(4, data[3]);
                    pssimpan.setString(5,data[4]);
                    pssimpan.setString(6,data[5]);
                    pssimpan.setString(7,data[6]);
                    pssimpan.setString(8,data[7]);
                    pssimpan.setString(9,data[8]);
                    pssimpan.setString(10,data[9]);
                    pssimpan.setString(11,data[10]);
                    pssimpan.setString(12,data[11]);
                    pssimpan.setString(13,data[12]);
                    pssimpan.setString(14,data[13]);
                    pssimpan.setString(15,data[14]);
                    pssimpan.setString(16,data[15]);
                    pssimpan.setString(17,data[16]);
                    pssimpan.setString(18,data[17]);
                    pssimpan.setString(19,data[18]);
                    pssimpan.setString(20,data[19]);
                    pssimpan.setString(21,data[20]);
                    pssimpan.setString(22,data[21]);
                    pssimpan.setString(23,data[22]);
                    pssimpan.setString(24,data[23]);
                    pssimpan.setString(25,data[24]);
                    pssimpan.setString(26,data[25]);
                    pssimpan.setString(27,data[26]);
                    pssimpan.setString(28,data[27]);
                    pssimpan.setString(29,data[28]);
                    pssimpan.setString(30,data[29]);
                    pssimpan.setString(31,data[30]);
                    pssimpan.setString(32,data[31]);
                    pssimpan.setString(33,data[32]);
                    pssimpan.setString(34,data[33]);
                    pssimpan.setString(35,data[34]);
                    pssimpan.setString(36,data[35]);
                    pssimpan.setString(37,data[36]);
                    pssimpan.setString(38,data[37]);
                    pssimpan.setString(39,data[38]);
                    pssimpan.setString(40,data[39]);
                    pssimpan.setString(41,data[40]);
                    pssimpan.setString(42,data[41]);
                    pssimpan.setString(43,data[42]);
                    pssimpan.setString(44,data[43]);
                    pssimpan.setString(45,data[44]);
                    pssimpan.setString(46,data[45]);
                    pssimpan.setString(47,data[46]);
                    pssimpan.setString(48,data[47]);
                    pssimpan.setString(49,data[48]);
                    pssimpan.setString(50,data[49]);
                    pssimpan.setString(51,data[50]);
                    pssimpan.setString(52,data[51]);
                    pssimpan.setString(53,data[52]);
                    pssimpan.setString(54,data[53]);
                    pssimpan.setString(55,data[54]);
                    pssimpan.setString(56,data[55]);
                    pssimpan.setString(57,data[56]);
                    pssimpan.setString(58,data[57]);
                    pssimpan.setString(59,data[58]);
                    pssimpan.setString(60,data[59]);
                    pssimpan.setString(61,data[60]);
                    pssimpan.setString(62,data[61]);
                    pssimpan.setString(63,data[62]);
                    pssimpan.setString(64,data[63]);
                    pssimpan.setString(65,data[64]);
                    pssimpan.setString(66,data[65]);
                    pssimpan.setString(67,data[66]);
                    pssimpan.setString(68,data[67]);
                    pssimpan.setString(69,data[68]);
                    pssimpan.setString(70,data[69]);
                    pssimpan.setString(71,data[70]);
                    pssimpan.setString(72,data[71]);
                    pssimpan.setString(73,data[72]);
                    pssimpan.setString(74,data[73]);
                    pssimpan.setString(75,data[74]);
                    pssimpan.setString(76,data[75]);
                    pssimpan.setString(77,data[76]);
                    pssimpan.setString(78,data[77]);
                    pssimpan.setString(79,data[78]);
                    pssimpan.setString(80,data[79]);
                    pssimpan.setString(81,data[80]);
                    pssimpan.setString(82,data[81]);
                    pssimpan.setString(83,data[82]);
                    pssimpan.setString(84,data[83]);
                    pssimpan.setString(85,data[84]);
                    pssimpan.setString(86,data[85]);
                    pssimpan.setString(87,data[86]);
                    pssimpan.setString(88,data[87]);
                    pssimpan.setString(89,data[88]);
                    pssimpan.setString(90,data[89]);
                    pssimpan.setString(91,data[90]);
                    pssimpan.setString(92,data[91]);
                    pssimpan.setString(93,data[92]);
                    pssimpan.setString(94,data[93]);
                    pssimpan.setString(95,data[94]);
                    pssimpan.setString(96,data[95]);
                    pssimpan.setString(97,data[96]);
                    pssimpan.setString(98,data[97]);
                    pssimpan.setString(99,data[98]);
                    pssimpan.setString(100,data[99]);
                    pssimpan.setString(101,data[100]);
                    pssimpan.setString(102,data[101]);
                    pssimpan.setString(103,data[102]);
                    pssimpan.setString(104,data[103]);
                    pssimpan.setString(105,data[104]);
                    pssimpan.setString(106,data[105]);
                    pssimpan.setString(107,data[106]);
                    pssimpan.setString(108,data[107]);
                    pssimpan.setString(109,data[108]);
                    pssimpan.setString(110,data[109]);
                    pssimpan.setString(111,data[110]);
                    pssimpan.setString(112,data[111]);
                    pssimpan.setString(113,data[112]);
                    pssimpan.setString(114,data[113]);
                    pssimpan.setString(115,data[114]);
                    pssimpan.setString(116,data[115]);
                    pssimpan.setString(117,data[116]);
                    pssimpan.setString(118,data[117]);
                    pssimpan.setString(119,data[118]);
                    pssimpan.setString(120,data[119]);
                    pssimpan.setString(121,data[120]);
                    pssimpan.setString(122,data[121]);
                    pssimpan.setString(123,data[122]);
                    pssimpan.setString(124,data[123]);
                    pssimpan.setString(125,data[124]);
                    pssimpan.setString(126,data[125]);
                    pssimpan.setString(127,data[126]);
                    pssimpan.setString(128,data[127]);
                    pssimpan.setString(129,data[128]);
                    pssimpan.setString(130,data[129]);
                    pssimpan.setString(131,Valid.SetTgl(Tgl1.getSelectedItem().toString()));
                    pssimpan.setString(132,TglPeriode.getSelectedItem().toString());
                    pssimpan.setString(133,cmbCaraBayar.getSelectedItem().toString());
                    pssimpan.setString(134,cmbKategori.getSelectedItem().toString());
                    
                    pssimpan.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Data berhasil diimport!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih file terlebih dahulu!");
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
//        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
//            BtnSimpanActionPerformed(null);
//        }else{
//            Valid.pindah(evt,kdrek,BtnBatal);
//        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
//        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
//            dispose();
//        }else{Valid.pindah(evt,BtnPrint,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

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
           // Valid.pindah(evt, BtnCari,kddokter);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbJadwalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbJadwalMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
//                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbJadwalMouseClicked

    private void tbJadwalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbJadwalKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
//                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbJadwalKeyPressed

private void BtnPoliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPoliActionPerformed
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            pilihfile = chooser.getSelectedFile();
            nmrek.setText(pilihfile.getName());
        }
}//GEN-LAST:event_BtnPoliActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tampil();
    }//GEN-LAST:event_formWindowOpened

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void Tgl1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl1KeyPressed
        //        Valid.pindah(evt,kdmem,Tgl2);
    }//GEN-LAST:event_Tgl1KeyPressed

    private void TglPeriodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglPeriodeKeyPressed
        //        Valid.pindah(evt, Tgl1,kdptg);
    }//GEN-LAST:event_TglPeriodeKeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgImportHitungJpRI dialog = new DlgImportHitungJpRI(new javax.swing.JFrame(), true);
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
    private widget.Button BtnKeluar;
    private widget.Button BtnPoli;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.PanelBiasa FormInput;
    private widget.Label LCount;
    private javax.swing.JPanel PanelInput;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.Tanggal Tgl1;
    private widget.Tanggal TglPeriode;
    private widget.ComboBox cmbCaraBayar;
    private widget.ComboBox cmbKategori;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private javax.swing.JPanel jPanel3;
    private widget.Label label11;
    private widget.Label label12;
    private widget.Label label13;
    private widget.Label label18;
    private widget.TextBox nmrek;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.Table tbJadwal;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            ps=koneksi.prepareStatement(
                    "select * from import_hitung_jp_ri "+
                    "where import_hitung_jp_ri.sep  like ? order by import_hitung_jp_ri.sep");
            try {
                ps.setString(1,"%"+TCari.getText().trim()+"%");
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new Object[]{
                        false,rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getString(11),
                        rs.getString(12),
                        rs.getString(13),
                        rs.getString(14),
                        rs.getString(15),
                        rs.getString(16),
                        rs.getString(17),
                        rs.getString(18),
                        rs.getString(19),
                        rs.getString(20),
                        rs.getString(21),
                        rs.getString(22),
                        rs.getString(23),
                        rs.getString(24),
                        rs.getString(25),
                        rs.getString(26),
                        rs.getString(27),
                        rs.getString(28),
                        rs.getString(29),
                        rs.getString(30),
                        rs.getString(31),
                        rs.getString(32),
                        rs.getString(33),
                        rs.getString(34),
                        rs.getString(35),
                        rs.getString(36),
                        rs.getString(37),
                        rs.getString(38),
                        rs.getString(39),
                        rs.getString(40),
                        rs.getString(41),
                        rs.getString(42),
                        rs.getString(43),
                        rs.getString(44),
                        rs.getString(45),
                        rs.getString(46),
                        rs.getString(47),
                        rs.getString(48),
                        rs.getString(49),
                        rs.getString(50),
                        rs.getString(51),
                        rs.getString(52),
                        rs.getString(53),
                        rs.getString(54),
                        rs.getString(55),
                        rs.getString(56),
                        rs.getString(57),
                        rs.getString(58),
                        rs.getString(59),
                        rs.getString(60),
                        rs.getString(61),
                        rs.getString(62),
                        rs.getString(63),
                        rs.getString(64),
                        rs.getString(65),
                        rs.getString(66),
                        rs.getString(67),
                        rs.getString(68),
                        rs.getString(69),
                        rs.getString(70),
                        rs.getString(71),
                        rs.getString(72),
                        rs.getString(73),
                        rs.getString(74),
                        rs.getString(75),
                        rs.getString(76),
                        rs.getString(77),
                        rs.getString(78),
                        rs.getString(79),
                        rs.getString(80),
                        rs.getString(81),
                        rs.getString(82),
                        rs.getString(83),
                        rs.getString(84),
                        rs.getString(85),
                        rs.getString(86),
                        rs.getString(87),
                        rs.getString(88),
                        rs.getString(89),
                        rs.getString(90),
                        rs.getString(91),
                        rs.getString(92),
                        rs.getString(93),
                        rs.getString(94),
                        rs.getString(95),
                        rs.getString(96),
                        rs.getString(97),
                        rs.getString(98),
                        rs.getString(99),
                        rs.getString(100),
                        rs.getString(101),
                        rs.getString(102),
                        rs.getString(103),
                        rs.getString(104),
                        rs.getString(105),
                        rs.getString(106),
                        rs.getString(107),
                        rs.getString(108),
                        rs.getString(109),
                        rs.getString(110),
                        rs.getString(111),
                        rs.getString(112),
                        rs.getString(113),
                        rs.getString(114),
                        rs.getString(115),
                        rs.getString(116),
                        rs.getString(117),
                        rs.getString(118),
                        rs.getString(119),
                        rs.getString(120),
                        rs.getString(121),
                        rs.getString(122),
                        rs.getString(123),
                        rs.getString(124),
                        rs.getString(125),
                        rs.getString(126),
                        rs.getString(127),
                        rs.getString(128),
                        rs.getString(129),
                        rs.getString(130),
                        rs.getString(131),
                        rs.getString(132),
                        rs.getString(133),
                        rs.getString(134)

                    });
                }
            } catch (Exception e) {
                System.out.println(e);
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
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,90));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
}
