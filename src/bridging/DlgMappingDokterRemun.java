/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgJnsPerawatanRalan.java
 *
 * Created on May 22, 2010, 11:58:21 PM
 */

package bridging;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariDokterRemun;

/**
 *
 * @author dosen
 */
public final class DlgMappingDokterRemun extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private PreparedStatement ps;
    private ResultSet rs;    
    private int i=0;
    private double tindakandokter,tindakandokternondpjp,tindakanbedah,tindakanbedahnondpjp,tindakanbedahpendamping,tindakanbedahpendampingnondpjp,
            tindakanbedahlokal,tindakanbedahlokalnondpjp,tindakandokterrj,tindakandokternondpjprj,tindakanbedahrj,tindakanbedahnondpjprj,tindakanbedahlokalrj,
            tindakanbedahlokalnondpjprj,jtl,total;
    private DlgCariDokter poli=new DlgCariDokter(null,false);
    private DlgCariDokterRemun dokterremun=new DlgCariDokterRemun(null,false);
//    private BPJSCekReferensiDokterDPJP polipcare=new BPJSCekReferensiDokterDPJP(null,false);
    

    /** Creates new form DlgJnsPerawatanRalan
     * @param parent
     * @param modal */
    public DlgMappingDokterRemun(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(8,1);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new Object[]{
            "Kode Dokter RS","Nama Dokter RS","Kode Dokter Remun","Nama Dokter Remun","NIP","Jabatan"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbJnsPerawatan.setModel(tabMode);

        tbJnsPerawatan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbJnsPerawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 6; i++) {
            TableColumn column = tbJnsPerawatan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(100);
            }else if(i==1){
                column.setPreferredWidth(260);
            }else if(i==2){
                column.setPreferredWidth(100);
            }else if(i==3){
                column.setPreferredWidth(260);
            }else if(i==4){
                column.setPreferredWidth(260);
            }else if(i==5){
                column.setPreferredWidth(260);
            }
        }
        tbJnsPerawatan.setDefaultRenderer(Object.class, new WarnaTable());

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
        
        poli.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(poli.getTable().getSelectedRow()!= -1){                    
                    KdDokterRs.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),0).toString());
                    nmDokterRS.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),1).toString());
                }
                KdDokterRs.requestFocus();
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
        
        poli.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    poli.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        dokterremun.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokterremun.getTable().getSelectedRow()!= -1){                   
                    KdDokterRemun.setText(dokterremun.getTable().getValueAt(dokterremun.getTable().getSelectedRow(),0).toString());
                    nmDokterRemun.setText(dokterremun.getTable().getValueAt(dokterremun.getTable().getSelectedRow(),1).toString());
                    KdDokterRemun.requestFocus();
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
        
        dokterremun.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    dokterremun.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });  
    
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Popup = new javax.swing.JPopupMenu();
        btnCetakKitir = new javax.swing.JMenuItem();
        WindowCetakKitir = new javax.swing.JDialog();
        internalFrame2 = new widget.InternalFrame();
        jLabel15 = new widget.Label();
        BtnCloseIn = new widget.Button();
        jLabel21 = new widget.Label();
        BtnSimpan1 = new widget.Button();
        jLabel41 = new widget.Label();
        label11 = new widget.Label();
        Tgl1 = new widget.Tanggal();
        label18 = new widget.Label();
        TglPeriode = new widget.Tanggal();
        label12 = new widget.Label();
        cmbCaraBayar = new widget.ComboBox();
        label13 = new widget.Label();
        cmbKategori = new widget.ComboBox();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbJnsPerawatan = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        BtnAll = new widget.Button();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        FormInput = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        KdDokterRs = new widget.TextBox();
        nmDokterRS = new widget.TextBox();
        btnPoliRS = new widget.Button();
        jLabel19 = new widget.Label();
        KdDokterRemun = new widget.TextBox();
        nmDokterRemun = new widget.TextBox();
        btnPoliBPJS = new widget.Button();
        nip = new widget.TextBox();
        jLabel5 = new widget.Label();
        jLabel20 = new widget.Label();
        nmJabatan = new widget.TextBox();

        Popup.setName("Popup"); // NOI18N

        btnCetakKitir.setBackground(new java.awt.Color(255, 255, 254));
        btnCetakKitir.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        btnCetakKitir.setForeground(new java.awt.Color(50, 50, 50));
        btnCetakKitir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        btnCetakKitir.setText("Cetak Kitir Remun");
        btnCetakKitir.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCetakKitir.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnCetakKitir.setName("btnCetakKitir"); // NOI18N
        btnCetakKitir.setPreferredSize(new java.awt.Dimension(180, 30));
        btnCetakKitir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakKitirActionPerformed(evt);
            }
        });
        Popup.add(btnCetakKitir);

        WindowCetakKitir.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowCetakKitir.setName("WindowCetakKitir"); // NOI18N
        WindowCetakKitir.setUndecorated(true);
        WindowCetakKitir.setResizable(false);
        WindowCetakKitir.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                WindowCetakKitirWindowActivated(evt);
            }
        });

        internalFrame2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(215, 225, 205)), "::[ Cetak Kitir Remunerasi Per Dokter ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Fira Sans", 0, 13), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(null);

        jLabel15.setText("=");
        jLabel15.setName("jLabel15"); // NOI18N
        internalFrame2.add(jLabel15);
        jLabel15.setBounds(342, 145, 20, 23);

        BtnCloseIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnCloseIn.setMnemonic('U');
        BtnCloseIn.setText("Tutup");
        BtnCloseIn.setToolTipText("Alt+U");
        BtnCloseIn.setGlassColor(new java.awt.Color(255, 204, 51));
        BtnCloseIn.setName("BtnCloseIn"); // NOI18N
        BtnCloseIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseInActionPerformed(evt);
            }
        });
        BtnCloseIn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCloseInKeyPressed(evt);
            }
        });
        internalFrame2.add(BtnCloseIn);
        BtnCloseIn.setBounds(560, 85, 100, 30);

        jLabel21.setText("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        jLabel21.setName("jLabel21"); // NOI18N
        internalFrame2.add(jLabel21);
        jLabel21.setBounds(0, 75, 820, 10);

        BtnSimpan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconrsi/printer.png"))); // NOI18N
        BtnSimpan1.setMnemonic('S');
        BtnSimpan1.setText("Cetak");
        BtnSimpan1.setToolTipText("Alt+S");
        BtnSimpan1.setGlassColor(new java.awt.Color(255, 204, 51));
        BtnSimpan1.setName("BtnSimpan1"); // NOI18N
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
        internalFrame2.add(BtnSimpan1);
        BtnSimpan1.setBounds(20, 85, 100, 30);

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setToolTipText("");
        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel41.setName("jLabel41"); // NOI18N
        internalFrame2.add(jLabel41);
        jLabel41.setBounds(670, 260, 140, 30);

        label11.setText("Tanggal :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(70, 23));
        internalFrame2.add(label11);
        label11.setBounds(0, 20, 80, 23);

        Tgl1.setDisplayFormat("dd-MM-yyyy");
        Tgl1.setName("Tgl1"); // NOI18N
        Tgl1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl1KeyPressed(evt);
            }
        });
        internalFrame2.add(Tgl1);
        Tgl1.setBounds(90, 20, 100, 23);

        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText("Periode Pembagian :");
        label18.setName("label18"); // NOI18N
        label18.setPreferredSize(new java.awt.Dimension(70, 23));
        internalFrame2.add(label18);
        label18.setBounds(200, 20, 110, 23);

        TglPeriode.setDisplayFormat("MMMM-yyyy");
        TglPeriode.setName("TglPeriode"); // NOI18N
        TglPeriode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglPeriodeKeyPressed(evt);
            }
        });
        internalFrame2.add(TglPeriode);
        TglPeriode.setBounds(310, 20, 120, 23);

        label12.setText("Cara Bayar :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(70, 23));
        internalFrame2.add(label12);
        label12.setBounds(0, 50, 80, 23);

        cmbCaraBayar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BPJS", "UMUM", "BPJS TK", "ASURANSI", "-" }));
        cmbCaraBayar.setName("cmbCaraBayar"); // NOI18N
        internalFrame2.add(cmbCaraBayar);
        cmbCaraBayar.setBounds(90, 50, 160, 20);

        label13.setText("Kategori :");
        label13.setName("label13"); // NOI18N
        label13.setPreferredSize(new java.awt.Dimension(70, 23));
        internalFrame2.add(label13);
        label13.setBounds(300, 50, 80, 23);

        cmbKategori.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Reguler", "Pending 1", "Pending 2", "Pending 3" }));
        cmbKategori.setName("cmbKategori"); // NOI18N
        cmbKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKategoriActionPerformed(evt);
            }
        });
        internalFrame2.add(cmbKategori);
        cmbKategori.setBounds(390, 50, 180, 20);

        WindowCetakKitir.getContentPane().add(internalFrame2, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Mapping Dokter DPJP REMUNERASI ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbJnsPerawatan.setAutoCreateRowSorter(true);
        tbJnsPerawatan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbJnsPerawatan.setComponentPopupMenu(Popup);
        tbJnsPerawatan.setName("tbJnsPerawatan"); // NOI18N
        tbJnsPerawatan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbJnsPerawatanMouseClicked(evt);
            }
        });
        tbJnsPerawatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbJnsPerawatanKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbJnsPerawatan);

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

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
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
        panelGlass8.add(BtnAll);

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

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(450, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('2');
        BtnCari.setToolTipText("Alt+2");
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

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass9.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass9.add(LCount);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 70));
        FormInput.setLayout(null);

        jLabel4.setText("Dokter RS :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(0, 10, 66, 23);

        KdDokterRs.setEditable(false);
        KdDokterRs.setHighlighter(null);
        KdDokterRs.setName("KdDokterRs"); // NOI18N
        FormInput.add(KdDokterRs);
        KdDokterRs.setBounds(69, 10, 80, 23);

        nmDokterRS.setEditable(false);
        nmDokterRS.setHighlighter(null);
        nmDokterRS.setName("nmDokterRS"); // NOI18N
        FormInput.add(nmDokterRS);
        nmDokterRS.setBounds(151, 10, 170, 23);

        btnPoliRS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPoliRS.setMnemonic('1');
        btnPoliRS.setToolTipText("Alt+1");
        btnPoliRS.setName("btnPoliRS"); // NOI18N
        btnPoliRS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPoliRSActionPerformed(evt);
            }
        });
        btnPoliRS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPoliRSKeyPressed(evt);
            }
        });
        FormInput.add(btnPoliRS);
        btnPoliRS.setBounds(323, 10, 28, 23);

        jLabel19.setText("Dokter Remun :");
        jLabel19.setName("jLabel19"); // NOI18N
        FormInput.add(jLabel19);
        jLabel19.setBounds(353, 10, 90, 23);

        KdDokterRemun.setEditable(false);
        KdDokterRemun.setHighlighter(null);
        KdDokterRemun.setName("KdDokterRemun"); // NOI18N
        FormInput.add(KdDokterRemun);
        KdDokterRemun.setBounds(446, 10, 70, 23);

        nmDokterRemun.setEditable(false);
        nmDokterRemun.setName("nmDokterRemun"); // NOI18N
        FormInput.add(nmDokterRemun);
        nmDokterRemun.setBounds(518, 10, 170, 23);

        btnPoliBPJS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPoliBPJS.setMnemonic('3');
        btnPoliBPJS.setToolTipText("ALt+3");
        btnPoliBPJS.setName("btnPoliBPJS"); // NOI18N
        btnPoliBPJS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPoliBPJSActionPerformed(evt);
            }
        });
        FormInput.add(btnPoliBPJS);
        btnPoliBPJS.setBounds(690, 10, 28, 23);

        nip.setName("nip"); // NOI18N
        FormInput.add(nip);
        nip.setBounds(70, 40, 250, 23);

        jLabel5.setText("NIP :");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(0, 40, 66, 23);

        jLabel20.setText("Jabatan :");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(353, 40, 90, 23);

        nmJabatan.setName("nmJabatan"); // NOI18N
        FormInput.add(nmJabatan);
        nmJabatan.setBounds(448, 40, 240, 23);

        internalFrame1.add(FormInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPoliRSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPoliRSActionPerformed
        poli.isCek();        
        poli.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        poli.setLocationRelativeTo(internalFrame1);
        poli.setVisible(true);
}//GEN-LAST:event_btnPoliRSActionPerformed

    private void btnPoliRSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPoliRSKeyPressed
        
}//GEN-LAST:event_btnPoliRSKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(KdDokterRs.getText().trim().equals("")||nmDokterRS.getText().trim().equals("")){
            Valid.textKosong(KdDokterRs,"Dokter RS");
        }else if(KdDokterRemun.getText().trim().equals("")||nmDokterRemun.getText().trim().equals("")){
            Valid.textKosong(KdDokterRemun,"Dokter Remun");
        }else{
            if(Sequel.menyimpantf("maping_dokter_dpjpremun","?,?,?,?,?","Mapping Dokter Remun",5,new String[]{
                KdDokterRs.getText(),KdDokterRemun.getText(),nmDokterRemun.getText(),nip.getText(),nmJabatan.getText()
            })==true){
                tampil();
                emptTeks();
            }                
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{Valid.pindah(evt,btnPoliBPJS, BtnBatal);}
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
        Valid.hapusTable(tabMode,KdDokterRs,"maping_dokter_dpjpremun","kd_dokter");
        tampil();
        emptTeks();
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(KdDokterRs.getText().trim().equals("")||nmDokterRS.getText().trim().equals("")){
            Valid.textKosong(KdDokterRs,"Dokter RS");
        }else if(KdDokterRemun.getText().trim().equals("")||nmDokterRemun.getText().trim().equals("")){
            Valid.textKosong(KdDokterRemun,"Dokter Remun");
        }else{
            if(tbJnsPerawatan.getSelectedRow()>-1){
                if(Sequel.mengedittf("maping_dokter_dpjpremun","kd_dokter=?","kd_dokter=?,kd_dokter_remun=?,nm_dokter_remun=?,nip=?,jabatan=?",6,new String[]{
                        KdDokterRs.getText(),KdDokterRemun.getText(),nmDokterRemun.getText(),nip.getText(),nmJabatan.getText(),tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),0).toString()
                    })==true){
                    emptTeks();
                    tampil();
                }
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
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            Locale locale = new Locale ("id", "ID"); 
            Locale.setDefault(locale);
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());
            param.put("logo",Sequel.cariGambar("select setting.logo from setting"));
            param.put("tindakandokter", Valid.SetAngka(Sequel.cariIsi("select sum("+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'")));
            param.put("tindakandokternondpjp", Valid.SetAngka(Sequel.cariIsi("select sum("+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'")));
            param.put("tindakanbedah", Valid.SetAngka(Sequel.cariIsi("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'")));
            param.put("tindakanbedahnondpjp", Valid.SetAngka(Sequel.cariIsi("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'")));
            param.put("tindakanbedahpendamping", Valid.SetAngka(Sequel.cariIsi("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'")));
            param.put("tindakanbedahpendampingnondpjp", Valid.SetAngka(Sequel.cariIsi("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'")));
            param.put("tindakanbedahlokal", Valid.SetAngka(Sequel.cariIsi("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'")));
            param.put("tindakanbedahlokalnondpjp", Valid.SetAngka(Sequel.cariIsi("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'")));
            param.put("jtl", Valid.SetAngka(Sequel.cariIsi("select sum(jtl) from import_perbup_remun where nip='"+nip.getText()+"'")));
            param.put("ri", Sequel.cariIsi("select sum(sep) from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'"));
            param.put("rj", Sequel.cariIsi("select sum(sep) from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"'"));
//            param.put("logo2",Sequel.cariGambar("select setting.logo from setting"));
//            param.put("watermark",Sequel.cariGambar("select setting_watermark.logo from setting_watermark"));
            Valid.MyReportqry("rptKitirJaspel.jasper","report","::[ Kitir Remunerasi ]::",
                "select * from import_perbup_remun ",param);
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
            Valid.pindah(evt, BtnPrint, BtnKeluar);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbJnsPerawatanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbJnsPerawatanMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbJnsPerawatanMouseClicked

    private void tbJnsPerawatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbJnsPerawatanKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbJnsPerawatanKeyPressed

private void btnPoliBPJSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPoliBPJSActionPerformed
    dokterremun.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
    dokterremun.setLocationRelativeTo(internalFrame1);
    dokterremun.setVisible(true);
}//GEN-LAST:event_btnPoliBPJSActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tampil();
        emptTeks();
    }//GEN-LAST:event_formWindowOpened

    private void btnCetakKitirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakKitirActionPerformed
        WindowCetakKitir.setSize(675,128);
        WindowCetakKitir.setLocationRelativeTo(null);
        WindowCetakKitir.setLocationRelativeTo(internalFrame1);
        WindowCetakKitir.setVisible(true);
//        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//        if(tabMode.getRowCount()==0){
//            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
//            BtnBatal.requestFocus();
//        }else if(tabMode.getRowCount()!=0){
//            Locale locale = new Locale ("id", "ID"); 
//            Locale.setDefault(locale);
//            Map<String, Object> param = new HashMap<>();
//            param.put("namars",akses.getnamars());
//            param.put("alamatrs",akses.getalamatrs());
//            param.put("kotars",akses.getkabupatenrs());
//            param.put("propinsirs",akses.getpropinsirs());
//            param.put("kontakrs",akses.getkontakrs());
//            param.put("emailrs",akses.getemailrs());
//            param.put("nama",nmDokterRemun.getText());
//            param.put("kd_dokter",KdDokterRemun.getText());
//            param.put("nip",nip.getText());
//            param.put("logo",Sequel.cariGambar("select setting.logo from setting"));
//            /*param.put("tindakandokter", Valid.SetAngka4(Sequel.cariIsiAngka("select sum("+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'")));
//            param.put("tindakandokternondpjp", Valid.SetAngka4(Sequel.cariIsiAngka("select sum("+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'")));
//            param.put("tindakanbedah", Valid.SetAngka4(Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'")));
//            param.put("tindakanbedahnondpjp", Valid.SetAngka4(Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'")));
//            param.put("tindakanbedahpendamping", Valid.SetAngka4(Sequel.cariIsiAngka("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'")));
//            param.put("tindakanbedahpendampingnondpjp", Valid.SetAngka4(Sequel.cariIsiAngka("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'")));
//            param.put("tindakanbedahlokal", Valid.SetAngka4(Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'")));
//            param.put("tindakanbedahlokalnondpjp", Valid.SetAngka4(Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'")));*/
//            //rawat inap
//            tindakandokter=Sequel.cariIsiAngka("select sum("+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'");
//            tindakandokternondpjp=Sequel.cariIsiAngka("select sum("+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'");
//            tindakanbedah=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'");
//            tindakanbedahnondpjp=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'");
//            tindakanbedahpendamping=Sequel.cariIsiAngka("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'");
//            tindakanbedahpendampingnondpjp=Sequel.cariIsiAngka("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'");
//            tindakanbedahlokal=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'");
//            tindakanbedahlokalnondpjp=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'");
//            param.put("tindakandokter", Valid.SetAngka4(tindakandokter));
//            param.put("tindakandokternondpjp", Valid.SetAngka4(tindakandokternondpjp));
//            param.put("tindakanbedah", Valid.SetAngka4(tindakanbedah));
//            param.put("tindakanbedahnondpjp", Valid.SetAngka4(tindakanbedahnondpjp));
//            param.put("tindakanbedahpendamping", Valid.SetAngka4(tindakanbedahpendamping));
//            param.put("tindakanbedahpendampingnondpjp", Valid.SetAngka4(tindakanbedahpendampingnondpjp));
//            param.put("tindakanbedahlokal", Valid.SetAngka4(tindakanbedahlokal));
//            param.put("tindakanbedahlokalnondpjp", Valid.SetAngka4(tindakanbedahlokalnondpjp));
//            
//            tindakandokterrj=Sequel.cariIsiAngka("select sum("+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"'");
//            tindakandokternondpjprj=Sequel.cariIsiAngka("select sum("+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter<>'"+KdDokterRemun.getText()+"'");
//            tindakanbedahrj=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"'");
//            tindakanbedahnondpjprj=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter<>'"+KdDokterRemun.getText()+"'");
//            tindakanbedahlokalrj=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"'");
//            tindakanbedahlokalnondpjprj=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter<>'"+KdDokterRemun.getText()+"'");
//            jtl=Sequel.cariIsiAngka("select sum(jtl) from import_perbup_remun where nip='"+nip.getText()+"'");
//            //rawat jalan
//            param.put("tindakandokterrj", Valid.SetAngka4(tindakandokterrj));
//            param.put("tindakandokternondpjprj", Valid.SetAngka4(tindakandokternondpjprj));
//            param.put("tindakanbedahrj", Valid.SetAngka4(tindakanbedahrj));
//            param.put("tindakanbedahnondpjprj", Valid.SetAngka4(tindakanbedahnondpjprj));
//            param.put("tindakanbedahlokalrj", Valid.SetAngka4(tindakanbedahlokalrj));
//            param.put("tindakanbedahlokalnondpjprj", Valid.SetAngka4(tindakanbedahlokalnondpjprj));
//            
//            param.put("jtl", Valid.SetAngka4(jtl));
//            
//            total=tindakandokter+tindakandokternondpjp+tindakanbedah+tindakanbedahnondpjp+tindakanbedahpendamping+tindakanbedahpendampingnondpjp+tindakanbedahlokal+tindakanbedahlokalnondpjp+
//                    tindakandokterrj+tindakandokternondpjprj+tindakanbedahrj+tindakanbedahnondpjprj+tindakanbedahlokalrj+tindakanbedahlokalnondpjprj+jtl;
//            param.put("total",Valid.SetAngka4(total));
//            
//            param.put("ri", Sequel.cariIsi("select count(sep) from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'"));
//            param.put("rj", Sequel.cariIsi("select count(sep) from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"'"));
////            param.put("logo2",Sequel.cariGambar("select setting.logo from setting"));
////            param.put("watermark",Sequel.cariGambar("select setting_watermark.logo from setting_watermark"));
//            Valid.MyReportqry("rptKitirJaspel.jasper","report","::[ Kitir Remunerasi ]::",
//                "select * from import_perbup_remun ",param);
//        }
//        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnCetakKitirActionPerformed

    private void BtnCloseInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseInActionPerformed
        WindowCetakKitir.dispose();
    }//GEN-LAST:event_BtnCloseInActionPerformed

    private void BtnCloseInKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCloseInKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            WindowCetakKitir.dispose();
        }else{Valid.pindah(evt, BtnBatal, BtnBatal);}
    }//GEN-LAST:event_BtnCloseInKeyPressed

    private void BtnSimpan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpan1ActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            Locale locale = new Locale ("id", "ID"); 
            Locale.setDefault(locale);
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());
            param.put("nama",nmDokterRemun.getText());
            param.put("kd_dokter",KdDokterRemun.getText());
            param.put("nip",nip.getText());
            param.put("logo",Sequel.cariGambar("select setting.logo from setting"));
            
            //rawat inap
            tindakandokter=Sequel.cariIsiAngka("select sum("+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
            tindakandokternondpjp=Sequel.cariIsiAngka("select sum("+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
            //tindakan Bedah
            if(nmJabatan.getText().equals("Dokter Spesialis Anestesi")){
                tindakanbedah=Sequel.cariIsiAngka("select sum(dr_anestesi) from import_hitung_jp_ri where dpjpanestesi like '%"+nmDokterRS.getText()+"%' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
            }else if(nmJabatan.getText().equals("Dokter Spesialis Bedah")){
                tindakanbedah=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahnondpjp=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahpendamping=Sequel.cariIsiAngka("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahpendampingnondpjp=Sequel.cariIsiAngka("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahlokal=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahlokalnondpjp=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
            }else if(nmJabatan.getText().equals("Dokter Spesialis Mata")){
                tindakanbedah=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahnondpjp=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahpendamping=Sequel.cariIsiAngka("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahpendampingnondpjp=Sequel.cariIsiAngka("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahlokal=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahlokalnondpjp=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
            }else if(nmJabatan.getText().equals("Dokter Spesialis Kandungan")){
                tindakanbedah=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahnondpjp=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahpendamping=Sequel.cariIsiAngka("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahpendampingnondpjp=Sequel.cariIsiAngka("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahlokal=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahlokalnondpjp=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
            }else{
                tindakanbedah=0;
                tindakanbedahnondpjp=0;
                tindakanbedahpendamping=0;
                tindakanbedahpendampingnondpjp=0;
                tindakanbedahlokal=0;
                tindakanbedahlokalnondpjp=0;
            }
//            tindakanbedah=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'");
//            tindakanbedahnondpjp=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'");
//            tindakanbedahpendamping=Sequel.cariIsiAngka("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'");
//            tindakanbedahpendampingnondpjp=Sequel.cariIsiAngka("select sum(bp"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'");
//            tindakanbedahlokal=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"'");
//            tindakanbedahlokalnondpjp=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_ri where id_dokter<>'"+KdDokterRemun.getText()+"'");
            param.put("tindakandokter", Valid.SetAngka4(tindakandokter));
            param.put("tindakandokternondpjp", Valid.SetAngka4(tindakandokternondpjp));
            param.put("tindakanbedah", Valid.SetAngka4(tindakanbedah));
            param.put("tindakanbedahnondpjp", Valid.SetAngka4(tindakanbedahnondpjp));
            param.put("tindakanbedahpendamping", Valid.SetAngka4(tindakanbedahpendamping));
            param.put("tindakanbedahpendampingnondpjp", Valid.SetAngka4(tindakanbedahpendampingnondpjp));
            param.put("tindakanbedahlokal", Valid.SetAngka4(tindakanbedahlokal));
            param.put("tindakanbedahlokalnondpjp", Valid.SetAngka4(tindakanbedahlokalnondpjp));
            
            //Rawat Jalan
            if(nmJabatan.getText().equals("Dokter Spesialis Anestesi")){
//                tindakanbedahrj=Sequel.cariIsiAngka("select sum(dr_anestesi) from import_hitung_jp_ri where dpjpanestesi like '%"+nmDokterRS.getText()+"%'");
                tindakanbedahrj=0;
            }else if(nmJabatan.getText().equals("Dokter Spesialis Bedah")){
                tindakanbedahrj=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahnondpjprj=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahlokalrj=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahlokalnondpjprj=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
            }else if(nmJabatan.getText().equals("Dokter Spesialis Mata")){
                tindakanbedahrj=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahnondpjprj=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahlokalrj=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahlokalnondpjprj=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
            }else if(nmJabatan.getText().equals("Dokter Spesialis Kandungan")){
                tindakanbedahrj=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahnondpjprj=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahlokalrj=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
                tindakanbedahlokalnondpjprj=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
            }else{
                tindakanbedahrj=0;
                tindakanbedahnondpjprj=0;
                tindakanbedahlokalrj=0;
                tindakanbedahlokalnondpjprj=0;
            }
            
            tindakandokterrj=Sequel.cariIsiAngka("select sum("+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
            tindakandokternondpjprj=Sequel.cariIsiAngka("select sum("+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter<>'"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
//            tindakanbedahrj=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"'");
//            tindakanbedahnondpjprj=Sequel.cariIsiAngka("select sum(b"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter<>'"+KdDokterRemun.getText()+"'");
//            tindakanbedahlokalrj=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"'");
//            tindakanbedahlokalnondpjprj=Sequel.cariIsiAngka("select sum(bl"+KdDokterRemun.getText()+") from import_hitung_jp_rj where id_dokter<>'"+KdDokterRemun.getText()+"'");
            jtl=Sequel.cariIsiAngka("select sum(jtl) from import_perbup_remun where nip='"+nip.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'");
            //rawat jalan
            param.put("tindakandokterrj", Valid.SetAngka4(tindakandokterrj));
            param.put("tindakandokternondpjprj", Valid.SetAngka4(tindakandokternondpjprj));
            param.put("tindakanbedahrj", Valid.SetAngka4(tindakanbedahrj));
            param.put("tindakanbedahnondpjprj", Valid.SetAngka4(tindakanbedahnondpjprj));
            param.put("tindakanbedahlokalrj", Valid.SetAngka4(tindakanbedahlokalrj));
            param.put("tindakanbedahlokalnondpjprj", Valid.SetAngka4(tindakanbedahlokalnondpjprj));
            
            param.put("jtl", Valid.SetAngka4(jtl));
            
            total=tindakandokter+tindakandokternondpjp+tindakanbedah+tindakanbedahnondpjp+tindakanbedahpendamping+tindakanbedahpendampingnondpjp+tindakanbedahlokal+tindakanbedahlokalnondpjp+
                    tindakandokterrj+tindakandokternondpjprj+tindakanbedahrj+tindakanbedahnondpjprj+tindakanbedahlokalrj+tindakanbedahlokalnondpjprj+jtl;
            param.put("total",Valid.SetAngka4(total));
            
            param.put("ri", Sequel.cariIsi("select count(sep) from import_hitung_jp_ri where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'"));
            param.put("rj", Sequel.cariIsi("select count(sep) from import_hitung_jp_rj where id_dokter='"+KdDokterRemun.getText()+"' and tanggal_dibagi='"+Valid.SetTgl(Tgl1.getSelectedItem().toString().replaceAll("/", "-"))+"' and periode_layanan='"+TglPeriode.getSelectedItem().toString()+"' and cara_bayar='"+cmbCaraBayar.getSelectedItem().toString()+"' and kategori_layanan='"+cmbKategori.getSelectedItem().toString()+"'"));
            param.put("periode",TglPeriode.getSelectedItem().toString());
            param.put("tanggaldibagi", Tgl1.getSelectedItem().toString());
//            param.put("logo2",Sequel.cariGambar("select setting.logo from setting"));
//            param.put("watermark",Sequel.cariGambar("select setting_watermark.logo from setting_watermark"));
            Valid.MyReportqry("rptKitirJaspel.jasper","report","::[ Kitir Remunerasi ]::",
                "select * from import_perbup_remun ",param);
        }
        this.setCursor(Cursor.getDefaultCursor());
        WindowCetakKitir.dispose();
    }//GEN-LAST:event_BtnSimpan1ActionPerformed

    private void BtnSimpan1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpan1KeyPressed
        
    }//GEN-LAST:event_BtnSimpan1KeyPressed

    private void WindowCetakKitirWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowCetakKitirWindowActivated
        akses.setstatus(false);
    }//GEN-LAST:event_WindowCetakKitirWindowActivated

    private void Tgl1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl1KeyPressed
        //        Valid.pindah(evt,kdmem,Tgl2);
    }//GEN-LAST:event_Tgl1KeyPressed

    private void TglPeriodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglPeriodeKeyPressed
        //        Valid.pindah(evt, Tgl1,kdptg);
    }//GEN-LAST:event_TglPeriodeKeyPressed

    private void cmbKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbKategoriActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgMappingDokterRemun dialog = new DlgMappingDokterRemun(new javax.swing.JFrame(), true);
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
    private widget.Button BtnCloseIn;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.Button BtnSimpan1;
    private widget.PanelBiasa FormInput;
    private widget.TextBox KdDokterRemun;
    private widget.TextBox KdDokterRs;
    private widget.Label LCount;
    private javax.swing.JPopupMenu Popup;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.Tanggal Tgl1;
    private widget.Tanggal TglPeriode;
    private javax.swing.JDialog WindowCetakKitir;
    private javax.swing.JMenuItem btnCetakKitir;
    private widget.Button btnPoliBPJS;
    private widget.Button btnPoliRS;
    private widget.ComboBox cmbCaraBayar;
    private widget.ComboBox cmbKategori;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.Label jLabel15;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel4;
    private widget.Label jLabel41;
    private widget.Label jLabel5;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private javax.swing.JPanel jPanel3;
    private widget.Label label11;
    private widget.Label label12;
    private widget.Label label13;
    private widget.Label label18;
    private widget.TextBox nip;
    private widget.TextBox nmDokterRS;
    private widget.TextBox nmDokterRemun;
    private widget.TextBox nmJabatan;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.Table tbJnsPerawatan;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
        Valid.tabelKosong(tabMode);
        try{
           ps=koneksi.prepareStatement(
                   "select maping_dokter_dpjpremun.kd_dokter,dokter.nm_dokter,maping_dokter_dpjpremun.kd_dokter_remun,maping_dokter_dpjpremun.nm_dokter_remun,maping_dokter_dpjpremun.nip,maping_dokter_dpjpremun.jabatan "+
                   "from maping_dokter_dpjpremun inner join dokter on maping_dokter_dpjpremun.kd_dokter=dokter.kd_dokter where "+
                   "maping_dokter_dpjpremun.kd_dokter like ? or dokter.nm_dokter like ? or maping_dokter_dpjpremun.kd_dokter_remun like ? or maping_dokter_dpjpremun.nm_dokter_remun like ? order by dokter.nm_dokter");
            try {
                ps.setString(1,"%"+TCari.getText()+"%");
                ps.setString(2,"%"+TCari.getText()+"%");
                ps.setString(3,"%"+TCari.getText()+"%");
                ps.setString(4,"%"+TCari.getText()+"%");
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new Object[]{
                        rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("kd_dokter_remun"),rs.getString("nm_dokter_remun"),rs.getString("nip"),rs.getString("jabatan")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif Ketersediaan : "+e);
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
        KdDokterRs.setText("");
        nmDokterRS.setText("");
        KdDokterRemun.setText("");
        nmDokterRemun.setText("");
        KdDokterRs.requestFocus();
    }

    private void getData() {
       if(tbJnsPerawatan.getSelectedRow()!= -1){
           KdDokterRs.setText(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),0).toString());
           nmDokterRS.setText(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),1).toString());
           KdDokterRemun.setText(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),2).toString());
           nmDokterRemun.setText(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),3).toString());
           nip.setText(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),4).toString());
           nmJabatan.setText(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),5).toString());
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getbpjs_mapping_dokterdpjp());
        BtnHapus.setEnabled(akses.getbpjs_mapping_dokterdpjp());
        BtnEdit.setEnabled(akses.getbpjs_mapping_dokterdpjp());
        BtnPrint.setEnabled(akses.getbpjs_mapping_dokterdpjp());
    }
    
    public JTable getTable(){
        return tbJnsPerawatan;
    }
}
