/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgRujuk.java
 *
 * Created on 31 Mei 10, 20:19:56
 */

package rekammedis;

import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariPetugas;


/**
 *
 * @author perpustakaan
 */
public final class RMCatatanADIMEGizi extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0;    
    private DlgCariPetugas petugas=new DlgCariPetugas(null,false);
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMCatatanADIMEGizi(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.R.M.","Nama Pasien","Umur","JK","Tanggal","Asesmen","Diagnosis","Intervensi","Monitoring","Evaluasi","Instruksi","NIP","Petugas"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbadime.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbadime.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbadime.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 14; i++) {
            TableColumn column = tbadime.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(65);
            }else if(i==2){
                column.setPreferredWidth(160);
            }else if(i==3){
                column.setPreferredWidth(35);
            }else if(i==4){
                column.setPreferredWidth(20);
            }else if(i==5){
                column.setPreferredWidth(120);
            }else if(i==6){
                column.setPreferredWidth(230);
            }else if(i==7){
                column.setPreferredWidth(230);
            }else if(i==8){
                column.setPreferredWidth(230);
            }else if(i==9){
                column.setPreferredWidth(230);
            }else if(i==10){
                column.setPreferredWidth(230);
            }else if(i==11){
                column.setPreferredWidth(230);
            }else if(i==12){
                column.setPreferredWidth(80);
            }else if(i==13){
                column.setPreferredWidth(150);
            }
        }
        tbadime.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRwadime.setDocument(new batasInput((byte)17).getKata(TNoRwadime));
        KdPetugasadime.setDocument(new batasInput((byte)20).getKata(KdPetugasadime));
        Asesmenadime.setDocument(new batasInput((int)1000).getKata(Asesmenadime));
        Diagnosisadime.setDocument(new batasInput((int)1000).getKata(Diagnosisadime));
        Intervensiadime.setDocument(new batasInput((int)1000).getKata(Intervensiadime));
        Monitoringadime.setDocument(new batasInput((int)1000).getKata(Monitoringadime));
        Evaluasiadime.setDocument(new batasInput((int)1000).getKata(Evaluasiadime));
        Instruksiadime.setDocument(new batasInput((int)1000).getKata(Instruksiadime));
        TCariadime.setDocument(new batasInput((int)100).getKata(TCariadime));
        
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCariadime.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCariadime.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCariadime.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCariadime.getText().length()>2){
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
                    KdPetugasadime.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                    NmPetugasadime.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                }  
                KdPetugasadime.requestFocus();
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
        
        
        ChkInputadime.setSelected(false);
        isForm();
        
        jam();
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
        MnCatatanADIME = new javax.swing.JMenuItem();
        JK = new widget.TextBox();
        Umur = new widget.TextBox();
        internalFrame1adime = new widget.InternalFrame();
        Scrolladime = new widget.ScrollPane();
        tbadime = new widget.Table();
        jPanel3adime = new javax.swing.JPanel();
        panelGlass8adime = new widget.panelisi();
        BtnSimpanadime = new widget.Button();
        BtnBataladime = new widget.Button();
        BtnHapusadime = new widget.Button();
        BtnEditadime = new widget.Button();
        BtnPrintadime = new widget.Button();
        jLabel7adime = new widget.Label();
        LCountadime = new widget.Label();
        BtnKeluaradime = new widget.Button();
        panelGlass9adime = new widget.panelisi();
        jLabel19adime = new widget.Label();
        DTPCari1adime = new widget.Tanggal();
        jLabel21adime = new widget.Label();
        DTPCari2adime = new widget.Tanggal();
        jLabel6adime = new widget.Label();
        TCariadime = new widget.TextBox();
        BtnCariadime = new widget.Button();
        BtnAlladime = new widget.Button();
        PanelInputadime = new javax.swing.JPanel();
        ChkInputadime = new widget.CekBox();
        scrollInput = new widget.ScrollPane();
        FormInputadime = new widget.PanelBiasa();
        jLabel4adime = new widget.Label();
        TNoRwadime = new widget.TextBox();
        TPasienadime = new widget.TextBox();
        Tanggaladime = new widget.Tanggal();
        TNoRMadime = new widget.TextBox();
        jLabel16adime = new widget.Label();
        Jamadime = new widget.ComboBox();
        Menitadime = new widget.ComboBox();
        Detikadime = new widget.ComboBox();
        ChkKejadianadime = new widget.CekBox();
        jLabel18adime = new widget.Label();
        KdPetugasadime = new widget.TextBox();
        NmPetugasadime = new widget.TextBox();
        btnPetugasadime = new widget.Button();
        jLabel24adime = new widget.Label();
        jLabel29adime = new widget.Label();
        scrollPane1adime = new widget.ScrollPane();
        Asesmenadime = new widget.TextArea();
        scrollPane2adime = new widget.ScrollPane();
        Monitoringadime = new widget.TextArea();
        jLabel25adime = new widget.Label();
        scrollPane3adime = new widget.ScrollPane();
        Diagnosisadime = new widget.TextArea();
        jLabel30adime = new widget.Label();
        scrollPane4adime = new widget.ScrollPane();
        Evaluasiadime = new widget.TextArea();
        jLabel26adime = new widget.Label();
        scrollPane5adime = new widget.ScrollPane();
        Intervensiadime = new widget.TextArea();
        jLabel31adime = new widget.Label();
        scrollPane6adime = new widget.ScrollPane();
        Instruksiadime = new widget.TextArea();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnCatatanADIME.setBackground(new java.awt.Color(255, 255, 254));
        MnCatatanADIME.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnCatatanADIME.setForeground(new java.awt.Color(50, 50, 50));
        MnCatatanADIME.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnCatatanADIME.setText("Formulir Catatan ADIME Gizi");
        MnCatatanADIME.setName("MnCatatanADIME"); // NOI18N
        MnCatatanADIME.setPreferredSize(new java.awt.Dimension(240, 26));
        MnCatatanADIME.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnCatatanADIMEActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnCatatanADIME);

        JK.setHighlighter(null);
        JK.setName("JK"); // NOI18N

        Umur.setHighlighter(null);
        Umur.setName("Umur"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1adime.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Catatan ADIME Gizi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1adime.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1adime.setName("internalFrame1adime"); // NOI18N
        internalFrame1adime.setLayout(new java.awt.BorderLayout(1, 1));

        Scrolladime.setName("Scrolladime"); // NOI18N
        Scrolladime.setOpaque(true);
        Scrolladime.setPreferredSize(new java.awt.Dimension(452, 200));

        tbadime.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbadime.setComponentPopupMenu(jPopupMenu1);
        tbadime.setName("tbadime"); // NOI18N
        tbadime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbadimeMouseClicked(evt);
            }
        });
        tbadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbadimeKeyPressed(evt);
            }
        });
        Scrolladime.setViewportView(tbadime);

        internalFrame1adime.add(Scrolladime, java.awt.BorderLayout.CENTER);

        jPanel3adime.setName("jPanel3adime"); // NOI18N
        jPanel3adime.setOpaque(false);
        jPanel3adime.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3adime.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8adime.setName("panelGlass8adime"); // NOI18N
        panelGlass8adime.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8adime.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpanadime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpanadime.setMnemonic('S');
        BtnSimpanadime.setText("Simpan");
        BtnSimpanadime.setToolTipText("Alt+S");
        BtnSimpanadime.setName("BtnSimpanadime"); // NOI18N
        BtnSimpanadime.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpanadime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanadimeActionPerformed(evt);
            }
        });
        BtnSimpanadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanadimeKeyPressed(evt);
            }
        });
        panelGlass8adime.add(BtnSimpanadime);

        BtnBataladime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBataladime.setMnemonic('B');
        BtnBataladime.setText("Baru");
        BtnBataladime.setToolTipText("Alt+B");
        BtnBataladime.setName("BtnBataladime"); // NOI18N
        BtnBataladime.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBataladime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBataladimeActionPerformed(evt);
            }
        });
        BtnBataladime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBataladimeKeyPressed(evt);
            }
        });
        panelGlass8adime.add(BtnBataladime);

        BtnHapusadime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapusadime.setMnemonic('H');
        BtnHapusadime.setText("Hapus");
        BtnHapusadime.setToolTipText("Alt+H");
        BtnHapusadime.setName("BtnHapusadime"); // NOI18N
        BtnHapusadime.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapusadime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusadimeActionPerformed(evt);
            }
        });
        BtnHapusadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusadimeKeyPressed(evt);
            }
        });
        panelGlass8adime.add(BtnHapusadime);

        BtnEditadime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEditadime.setMnemonic('G');
        BtnEditadime.setText("Ganti");
        BtnEditadime.setToolTipText("Alt+G");
        BtnEditadime.setName("BtnEditadime"); // NOI18N
        BtnEditadime.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEditadime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditadimeActionPerformed(evt);
            }
        });
        BtnEditadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditadimeKeyPressed(evt);
            }
        });
        panelGlass8adime.add(BtnEditadime);

        BtnPrintadime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrintadime.setMnemonic('T');
        BtnPrintadime.setText("Cetak");
        BtnPrintadime.setToolTipText("Alt+T");
        BtnPrintadime.setName("BtnPrintadime"); // NOI18N
        BtnPrintadime.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrintadime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintadimeActionPerformed(evt);
            }
        });
        BtnPrintadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintadimeKeyPressed(evt);
            }
        });
        panelGlass8adime.add(BtnPrintadime);

        jLabel7adime.setText("Record :");
        jLabel7adime.setName("jLabel7adime"); // NOI18N
        jLabel7adime.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass8adime.add(jLabel7adime);

        LCountadime.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCountadime.setText("0");
        LCountadime.setName("LCountadime"); // NOI18N
        LCountadime.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8adime.add(LCountadime);

        BtnKeluaradime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluaradime.setMnemonic('K');
        BtnKeluaradime.setText("Keluar");
        BtnKeluaradime.setToolTipText("Alt+K");
        BtnKeluaradime.setName("BtnKeluaradime"); // NOI18N
        BtnKeluaradime.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluaradime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluaradimeActionPerformed(evt);
            }
        });
        BtnKeluaradime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluaradimeKeyPressed(evt);
            }
        });
        panelGlass8adime.add(BtnKeluaradime);

        jPanel3adime.add(panelGlass8adime, java.awt.BorderLayout.CENTER);

        panelGlass9adime.setName("panelGlass9adime"); // NOI18N
        panelGlass9adime.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9adime.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19adime.setText("Tanggal :");
        jLabel19adime.setName("jLabel19adime"); // NOI18N
        jLabel19adime.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9adime.add(jLabel19adime);

        DTPCari1adime.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1adime.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-08-2023" }));
        DTPCari1adime.setDisplayFormat("dd-MM-yyyy");
        DTPCari1adime.setName("DTPCari1adime"); // NOI18N
        DTPCari1adime.setOpaque(false);
        DTPCari1adime.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9adime.add(DTPCari1adime);

        jLabel21adime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21adime.setText("s.d.");
        jLabel21adime.setName("jLabel21adime"); // NOI18N
        jLabel21adime.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9adime.add(jLabel21adime);

        DTPCari2adime.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2adime.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-08-2023" }));
        DTPCari2adime.setDisplayFormat("dd-MM-yyyy");
        DTPCari2adime.setName("DTPCari2adime"); // NOI18N
        DTPCari2adime.setOpaque(false);
        DTPCari2adime.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9adime.add(DTPCari2adime);

        jLabel6adime.setText("Key Word :");
        jLabel6adime.setName("jLabel6adime"); // NOI18N
        jLabel6adime.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9adime.add(jLabel6adime);

        TCariadime.setName("TCariadime"); // NOI18N
        TCariadime.setPreferredSize(new java.awt.Dimension(310, 23));
        TCariadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariadimeKeyPressed(evt);
            }
        });
        panelGlass9adime.add(TCariadime);

        BtnCariadime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariadime.setMnemonic('3');
        BtnCariadime.setToolTipText("Alt+3");
        BtnCariadime.setName("BtnCariadime"); // NOI18N
        BtnCariadime.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariadime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariadimeActionPerformed(evt);
            }
        });
        BtnCariadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariadimeKeyPressed(evt);
            }
        });
        panelGlass9adime.add(BtnCariadime);

        BtnAlladime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAlladime.setMnemonic('M');
        BtnAlladime.setToolTipText("Alt+M");
        BtnAlladime.setName("BtnAlladime"); // NOI18N
        BtnAlladime.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAlladime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAlladimeActionPerformed(evt);
            }
        });
        BtnAlladime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAlladimeKeyPressed(evt);
            }
        });
        panelGlass9adime.add(BtnAlladime);

        jPanel3adime.add(panelGlass9adime, java.awt.BorderLayout.PAGE_START);

        internalFrame1adime.add(jPanel3adime, java.awt.BorderLayout.PAGE_END);

        PanelInputadime.setToolTipText("");
        PanelInputadime.setName("PanelInputadime"); // NOI18N
        PanelInputadime.setOpaque(false);
        PanelInputadime.setPreferredSize(new java.awt.Dimension(192, 306));
        PanelInputadime.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInputadime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInputadime.setMnemonic('I');
        ChkInputadime.setText(".: Input Data");
        ChkInputadime.setToolTipText("Alt+I");
        ChkInputadime.setBorderPainted(true);
        ChkInputadime.setBorderPaintedFlat(true);
        ChkInputadime.setFocusable(false);
        ChkInputadime.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInputadime.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInputadime.setName("ChkInputadime"); // NOI18N
        ChkInputadime.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInputadime.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInputadime.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInputadime.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInputadime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputadimeActionPerformed(evt);
            }
        });
        PanelInputadime.add(ChkInputadime, java.awt.BorderLayout.PAGE_END);

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInputadime.setBackground(new java.awt.Color(250, 255, 245));
        FormInputadime.setBorder(null);
        FormInputadime.setName("FormInputadime"); // NOI18N
        FormInputadime.setPreferredSize(new java.awt.Dimension(100, 225));
        FormInputadime.setLayout(null);

        jLabel4adime.setText("No.Rawat :");
        jLabel4adime.setName("jLabel4adime"); // NOI18N
        FormInputadime.add(jLabel4adime);
        jLabel4adime.setBounds(0, 10, 75, 23);

        TNoRwadime.setHighlighter(null);
        TNoRwadime.setName("TNoRwadime"); // NOI18N
        TNoRwadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwadimeKeyPressed(evt);
            }
        });
        FormInputadime.add(TNoRwadime);
        TNoRwadime.setBounds(79, 10, 141, 23);

        TPasienadime.setEditable(false);
        TPasienadime.setHighlighter(null);
        TPasienadime.setName("TPasienadime"); // NOI18N
        TPasienadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPasienadimeKeyPressed(evt);
            }
        });
        FormInputadime.add(TPasienadime);
        TPasienadime.setBounds(336, 10, 450, 23);

        Tanggaladime.setForeground(new java.awt.Color(50, 70, 50));
        Tanggaladime.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-08-2023" }));
        Tanggaladime.setDisplayFormat("dd-MM-yyyy");
        Tanggaladime.setName("Tanggaladime"); // NOI18N
        Tanggaladime.setOpaque(false);
        Tanggaladime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggaladimeKeyPressed(evt);
            }
        });
        FormInputadime.add(Tanggaladime);
        Tanggaladime.setBounds(79, 40, 90, 23);

        TNoRMadime.setEditable(false);
        TNoRMadime.setHighlighter(null);
        TNoRMadime.setName("TNoRMadime"); // NOI18N
        TNoRMadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMadimeKeyPressed(evt);
            }
        });
        FormInputadime.add(TNoRMadime);
        TNoRMadime.setBounds(222, 10, 112, 23);

        jLabel16adime.setText("Tanggal :");
        jLabel16adime.setName("jLabel16adime"); // NOI18N
        jLabel16adime.setVerifyInputWhenFocusTarget(false);
        FormInputadime.add(jLabel16adime);
        jLabel16adime.setBounds(0, 40, 75, 23);

        Jamadime.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jamadime.setName("Jamadime"); // NOI18N
        Jamadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JamadimeKeyPressed(evt);
            }
        });
        FormInputadime.add(Jamadime);
        Jamadime.setBounds(173, 40, 62, 23);

        Menitadime.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menitadime.setName("Menitadime"); // NOI18N
        Menitadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenitadimeKeyPressed(evt);
            }
        });
        FormInputadime.add(Menitadime);
        Menitadime.setBounds(238, 40, 62, 23);

        Detikadime.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detikadime.setName("Detikadime"); // NOI18N
        Detikadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetikadimeKeyPressed(evt);
            }
        });
        FormInputadime.add(Detikadime);
        Detikadime.setBounds(303, 40, 62, 23);

        ChkKejadianadime.setBorder(null);
        ChkKejadianadime.setSelected(true);
        ChkKejadianadime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkKejadianadime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkKejadianadime.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkKejadianadime.setName("ChkKejadianadime"); // NOI18N
        FormInputadime.add(ChkKejadianadime);
        ChkKejadianadime.setBounds(368, 40, 23, 23);

        jLabel18adime.setText("Petugas :");
        jLabel18adime.setName("jLabel18adime"); // NOI18N
        FormInputadime.add(jLabel18adime);
        jLabel18adime.setBounds(400, 40, 70, 23);

        KdPetugasadime.setEditable(false);
        KdPetugasadime.setHighlighter(null);
        KdPetugasadime.setName("KdPetugasadime"); // NOI18N
        KdPetugasadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPetugasadimeKeyPressed(evt);
            }
        });
        FormInputadime.add(KdPetugasadime);
        KdPetugasadime.setBounds(474, 40, 94, 23);

        NmPetugasadime.setEditable(false);
        NmPetugasadime.setName("NmPetugasadime"); // NOI18N
        FormInputadime.add(NmPetugasadime);
        NmPetugasadime.setBounds(570, 40, 184, 23);

        btnPetugasadime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPetugasadime.setMnemonic('2');
        btnPetugasadime.setToolTipText("ALt+2");
        btnPetugasadime.setName("btnPetugasadime"); // NOI18N
        btnPetugasadime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPetugasadimeActionPerformed(evt);
            }
        });
        btnPetugasadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPetugasadimeKeyPressed(evt);
            }
        });
        FormInputadime.add(btnPetugasadime);
        btnPetugasadime.setBounds(758, 40, 28, 23);

        jLabel24adime.setText("Asesmen :");
        jLabel24adime.setName("jLabel24adime"); // NOI18N
        FormInputadime.add(jLabel24adime);
        jLabel24adime.setBounds(0, 70, 75, 23);

        jLabel29adime.setText("Monitoring :");
        jLabel29adime.setName("jLabel29adime"); // NOI18N
        FormInputadime.add(jLabel29adime);
        jLabel29adime.setBounds(400, 70, 70, 23);

        scrollPane1adime.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane1adime.setName("scrollPane1adime"); // NOI18N

        Asesmenadime.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Asesmenadime.setColumns(20);
        Asesmenadime.setRows(5);
        Asesmenadime.setName("Asesmenadime"); // NOI18N
        Asesmenadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AsesmenadimeKeyPressed(evt);
            }
        });
        scrollPane1adime.setViewportView(Asesmenadime);

        FormInputadime.add(scrollPane1adime);
        scrollPane1adime.setBounds(79, 70, 312, 63);

        scrollPane2adime.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2adime.setName("scrollPane2adime"); // NOI18N

        Monitoringadime.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Monitoringadime.setColumns(20);
        Monitoringadime.setRows(5);
        Monitoringadime.setName("Monitoringadime"); // NOI18N
        Monitoringadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MonitoringadimeKeyPressed(evt);
            }
        });
        scrollPane2adime.setViewportView(Monitoringadime);

        FormInputadime.add(scrollPane2adime);
        scrollPane2adime.setBounds(474, 70, 312, 63);

        jLabel25adime.setText("Diagnosis :");
        jLabel25adime.setName("jLabel25adime"); // NOI18N
        FormInputadime.add(jLabel25adime);
        jLabel25adime.setBounds(0, 140, 75, 23);

        scrollPane3adime.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane3adime.setName("scrollPane3adime"); // NOI18N

        Diagnosisadime.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Diagnosisadime.setColumns(20);
        Diagnosisadime.setRows(5);
        Diagnosisadime.setName("Diagnosisadime"); // NOI18N
        Diagnosisadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosisadimeKeyPressed(evt);
            }
        });
        scrollPane3adime.setViewportView(Diagnosisadime);

        FormInputadime.add(scrollPane3adime);
        scrollPane3adime.setBounds(79, 140, 312, 63);

        jLabel30adime.setText("Evaluasi :");
        jLabel30adime.setName("jLabel30adime"); // NOI18N
        FormInputadime.add(jLabel30adime);
        jLabel30adime.setBounds(400, 140, 70, 23);

        scrollPane4adime.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane4adime.setName("scrollPane4adime"); // NOI18N

        Evaluasiadime.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Evaluasiadime.setColumns(20);
        Evaluasiadime.setRows(5);
        Evaluasiadime.setName("Evaluasiadime"); // NOI18N
        Evaluasiadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EvaluasiadimeKeyPressed(evt);
            }
        });
        scrollPane4adime.setViewportView(Evaluasiadime);

        FormInputadime.add(scrollPane4adime);
        scrollPane4adime.setBounds(474, 140, 312, 63);

        jLabel26adime.setText("Intervensi :");
        jLabel26adime.setName("jLabel26adime"); // NOI18N
        FormInputadime.add(jLabel26adime);
        jLabel26adime.setBounds(0, 210, 75, 23);

        scrollPane5adime.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane5adime.setName("scrollPane5adime"); // NOI18N

        Intervensiadime.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Intervensiadime.setColumns(20);
        Intervensiadime.setRows(5);
        Intervensiadime.setName("Intervensiadime"); // NOI18N
        Intervensiadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntervensiadimeKeyPressed(evt);
            }
        });
        scrollPane5adime.setViewportView(Intervensiadime);

        FormInputadime.add(scrollPane5adime);
        scrollPane5adime.setBounds(79, 210, 312, 63);

        jLabel31adime.setText("Instruksi :");
        jLabel31adime.setName("jLabel31adime"); // NOI18N
        FormInputadime.add(jLabel31adime);
        jLabel31adime.setBounds(400, 210, 70, 23);

        scrollPane6adime.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane6adime.setName("scrollPane6adime"); // NOI18N

        Instruksiadime.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Instruksiadime.setColumns(20);
        Instruksiadime.setRows(5);
        Instruksiadime.setName("Instruksiadime"); // NOI18N
        Instruksiadime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                InstruksiadimeKeyPressed(evt);
            }
        });
        scrollPane6adime.setViewportView(Instruksiadime);

        FormInputadime.add(scrollPane6adime);
        scrollPane6adime.setBounds(474, 210, 312, 63);

        scrollInput.setViewportView(FormInputadime);

        PanelInputadime.add(scrollInput, java.awt.BorderLayout.CENTER);

        internalFrame1adime.add(PanelInputadime, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1adime, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwadimeKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
        }else{            
            Valid.pindah(evt,TCariadime,Tanggaladime);
        }
}//GEN-LAST:event_TNoRwadimeKeyPressed

    private void TPasienadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienadimeKeyPressed
        Valid.pindah(evt,TCariadime,BtnSimpanadime);
}//GEN-LAST:event_TPasienadimeKeyPressed

    private void BtnSimpanadimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanadimeActionPerformed
        if(TNoRwadime.getText().trim().equals("")||TPasienadime.getText().trim().equals("")){
            Valid.textKosong(TNoRwadime,"pasien");
        }else if(KdPetugasadime.getText().trim().equals("")||NmPetugasadime.getText().trim().equals("")){
            Valid.textKosong(KdPetugasadime,"Petugas");
        }else if(Asesmenadime.getText().trim().equals("")){
            Valid.textKosong(Asesmenadime,"Monitoring");
        }else if(Monitoringadime.getText().trim().equals("")){
            Valid.textKosong(Monitoringadime,"Evaluasi");
        }else{
            if(Sequel.menyimpantf("catatan_adime_gizi","?,?,?,?,?,?,?,?,?","Data",9,new String[]{
                TNoRwadime.getText(),Valid.SetTgl(Tanggaladime.getSelectedItem()+"")+" "+Jamadime.getSelectedItem()+":"+Menitadime.getSelectedItem()+":"+Detikadime.getSelectedItem(),Asesmenadime.getText(),Diagnosisadime.getText(),Intervensiadime.getText(),Monitoringadime.getText(),Evaluasiadime.getText(),Instruksiadime.getText(),KdPetugasadime.getText()
            })==true){
                tabMode.addRow(new String[]{
                    TNoRwadime.getText(),TNoRMadime.getText(),TPasienadime.getText(),Umur.getText(),JK.getText(),Valid.SetTgl(Tanggaladime.getSelectedItem()+"")+" "+Jamadime.getSelectedItem()+":"+Menitadime.getSelectedItem()+":"+Detikadime.getSelectedItem(),
                    Asesmenadime.getText(),Diagnosisadime.getText(),Intervensiadime.getText(),Monitoringadime.getText(),Evaluasiadime.getText(),Instruksiadime.getText(),KdPetugasadime.getText(),NmPetugasadime.getText()
                });
                LCountadime.setText(""+tabMode.getRowCount());
                emptTeks();
            }   
        }
}//GEN-LAST:event_BtnSimpanadimeActionPerformed

    private void BtnSimpanadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanadimeKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanadimeActionPerformed(null);
        }else{
            Valid.pindah(evt,Instruksiadime,BtnBataladime);
        }
}//GEN-LAST:event_BtnSimpanadimeKeyPressed

    private void BtnBataladimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBataladimeActionPerformed
        emptTeks();
        ChkInputadime.setSelected(true);
        isForm(); 
}//GEN-LAST:event_BtnBataladimeActionPerformed

    private void BtnBataladimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBataladimeKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpanadime, BtnHapusadime);}
}//GEN-LAST:event_BtnBataladimeKeyPressed

    private void BtnHapusadimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusadimeActionPerformed
        if(tbadime.getSelectedRow()>-1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else{
                if(KdPetugasadime.getText().equals(tbadime.getValueAt(tbadime.getSelectedRow(),12).toString())){
                    hapus();
                }else{
                    JOptionPane.showMessageDialog(null,"Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
                }
            }
        }else{
            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        }   
}//GEN-LAST:event_BtnHapusadimeActionPerformed

    private void BtnHapusadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusadimeKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusadimeActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBataladime, BtnEditadime);
        }
}//GEN-LAST:event_BtnHapusadimeKeyPressed

    private void BtnEditadimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditadimeActionPerformed
        if(TNoRwadime.getText().trim().equals("")||TPasienadime.getText().trim().equals("")){
            Valid.textKosong(TNoRwadime,"pasien");
        }else if(KdPetugasadime.getText().trim().equals("")||NmPetugasadime.getText().trim().equals("")){
            Valid.textKosong(KdPetugasadime,"Petugas");
        }else if(Asesmenadime.getText().trim().equals("")){
            Valid.textKosong(Asesmenadime,"Monitoring");
        }else if(Monitoringadime.getText().trim().equals("")){
            Valid.textKosong(Monitoringadime,"Evaluasi");
        }else{ 
            if(tbadime.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else{
                    if(KdPetugasadime.getText().equals(tbadime.getValueAt(tbadime.getSelectedRow(),12).toString())){
                        ganti();
                    }else{
                        JOptionPane.showMessageDialog(null,"Hanya bisa diganti oleh petugas yang bersangkutan..!!");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
            } 
        }
}//GEN-LAST:event_BtnEditadimeActionPerformed

    private void BtnEditadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditadimeKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditadimeActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapusadime, BtnPrintadime);
        }
}//GEN-LAST:event_BtnEditadimeKeyPressed

    private void BtnKeluaradimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluaradimeActionPerformed
        petugas.dispose();
        dispose();
}//GEN-LAST:event_BtnKeluaradimeActionPerformed

    private void BtnKeluaradimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluaradimeKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluaradimeActionPerformed(null);
        }else{Valid.pindah(evt,BtnEditadime,TCariadime);}
}//GEN-LAST:event_BtnKeluaradimeKeyPressed

    private void BtnPrintadimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintadimeActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBataladime.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            Map<String, Object> param = new HashMap<>(); 
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            if(TCariadime.getText().equals("")){
                Valid.MyReportqry("rptDataCatatanADIMEGizi.jasper","report","::[ Data Catatan ADIME Gizi Pasien ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,catatan_adime_gizi.tanggal,catatan_adime_gizi.asesmen,catatan_adime_gizi.diagnosis,"+
                    "catatan_adime_gizi.intervensi,catatan_adime_gizi.monitoring,catatan_adime_gizi.evaluasi,catatan_adime_gizi.instruksi,"+
                    "catatan_adime_gizi.nip,petugas.nama "+
                    "from catatan_adime_gizi inner join reg_periksa on catatan_adime_gizi.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on catatan_adime_gizi.nip=petugas.nip where "+
                    "catatan_adime_gizi.tanggal between '"+Valid.SetTgl(DTPCari1adime.getSelectedItem()+"")+" 00:00:00' and '"+Valid.SetTgl(DTPCari2adime.getSelectedItem()+"")+" 23:59:59' order by catatan_adime_gizi.tanggal ",param);
            }else{
                Valid.MyReportqry("rptDataCatatanADIMEGizi.jasper","report","::[ Data Catatan ADIME Gizi Pasien ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,catatan_adime_gizi.tanggal,catatan_adime_gizi.asesmen,catatan_adime_gizi.diagnosis,"+
                    "catatan_adime_gizi.intervensi,catatan_adime_gizi.monitoring,catatan_adime_gizi.evaluasi,catatan_adime_gizi.instruksi,"+
                    "catatan_adime_gizi.nip,petugas.nama "+
                    "from catatan_adime_gizi inner join reg_periksa on catatan_adime_gizi.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on catatan_adime_gizi.nip=petugas.nip where "+
                    "catatan_adime_gizi.tanggal between '"+Valid.SetTgl(DTPCari1adime.getSelectedItem()+"")+" 00:00:00' and '"+Valid.SetTgl(DTPCari2adime.getSelectedItem()+"")+" 23:59:59' and "+
                    "(reg_periksa.no_rawat like '%"+TCariadime.getText().trim()+"%' or pasien.no_rkm_medis like '%"+TCariadime.getText().trim()+"%' or pasien.nm_pasien like '%"+TCariadime.getText().trim()+"%' or "+
                    "catatan_adime_gizi.asesmen like '%"+TCariadime.getText().trim()+"%' or catatan_adime_gizi.diagnosis like '%"+TCariadime.getText().trim()+"%' or catatan_adime_gizi.intervensi like '%"+TCariadime.getText().trim()+"%' or "+
                    "catatan_adime_gizi.monitoring like '%"+TCariadime.getText().trim()+"%' or catatan_adime_gizi.evaluasi like '%"+TCariadime.getText().trim()+"%' or catatan_adime_gizi.instruksi like '%"+TCariadime.getText().trim()+"%' or "+
                    "catatan_adime_gizi.nip like '%"+TCariadime.getText().trim()+"%' or petugas.nama like '%"+TCariadime.getText().trim()+"%') order by catatan_adime_gizi.tanggal ",param);
            }  
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintadimeActionPerformed

    private void BtnPrintadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintadimeKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintadimeActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEditadime, BtnKeluaradime);
        }
}//GEN-LAST:event_BtnPrintadimeKeyPressed

    private void TCariadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariadimeKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariadimeActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCariadime.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluaradime.requestFocus();
        }
}//GEN-LAST:event_TCariadimeKeyPressed

    private void BtnCariadimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariadimeActionPerformed
        tampil();
}//GEN-LAST:event_BtnCariadimeActionPerformed

    private void BtnCariadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariadimeKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariadimeActionPerformed(null);
        }else{
            Valid.pindah(evt, TCariadime, BtnAlladime);
        }
}//GEN-LAST:event_BtnCariadimeKeyPressed

    private void BtnAlladimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAlladimeActionPerformed
        TCariadime.setText("");
        tampil();
}//GEN-LAST:event_BtnAlladimeActionPerformed

    private void BtnAlladimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAlladimeKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
            TCariadime.setText("");
        }else{
            Valid.pindah(evt, BtnCariadime, TPasienadime);
        }
}//GEN-LAST:event_BtnAlladimeKeyPressed

    private void TanggaladimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggaladimeKeyPressed
        Valid.pindah2(evt,TCariadime,Jamadime);
}//GEN-LAST:event_TanggaladimeKeyPressed

    private void TNoRMadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMadimeKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
}//GEN-LAST:event_TNoRMadimeKeyPressed

    private void tbadimeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbadimeMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbadimeMouseClicked

    private void tbadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbadimeKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbadimeKeyPressed

    private void ChkInputadimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputadimeActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputadimeActionPerformed

    private void JamadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JamadimeKeyPressed
        Valid.pindah(evt,Tanggaladime,Menitadime);
    }//GEN-LAST:event_JamadimeKeyPressed

    private void MenitadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenitadimeKeyPressed
        Valid.pindah(evt,Jamadime,Detikadime);
    }//GEN-LAST:event_MenitadimeKeyPressed

    private void DetikadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetikadimeKeyPressed
        Valid.pindah(evt,Menitadime,btnPetugasadime);
    }//GEN-LAST:event_DetikadimeKeyPressed

    private void KdPetugasadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPetugasadimeKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            NmPetugasadime.setText(petugas.tampil3(KdPetugasadime.getText()));
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            Detikadime.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            Asesmenadime.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            btnPetugasadimeActionPerformed(null);
        }
    }//GEN-LAST:event_KdPetugasadimeKeyPressed

    private void btnPetugasadimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPetugasadimeActionPerformed
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1adime.getWidth()-20,internalFrame1adime.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1adime);
        petugas.setVisible(true);
    }//GEN-LAST:event_btnPetugasadimeActionPerformed

    private void btnPetugasadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPetugasadimeKeyPressed
        Valid.pindah(evt,Detikadime,Asesmenadime);
    }//GEN-LAST:event_btnPetugasadimeKeyPressed

    private void MnCatatanADIMEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnCatatanADIMEActionPerformed
        if(tbadime.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();
            param.put("diagnosa",Sequel.cariIsi("select kamar_inap.diagnosa_awal from kamar_inap where kamar_inap.diagnosa_awal<>'' and kamar_inap.no_rawat=? ",TNoRwadime.getText()));
            Valid.MyReportqry("rptFormulirCatatanADIMEGizi.jasper","report","::[ Formulir Catatan ADIME Gizi Pasien ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,catatan_adime_gizi.tanggal,catatan_adime_gizi.asesmen,catatan_adime_gizi.diagnosis,"+
                    "catatan_adime_gizi.intervensi,catatan_adime_gizi.monitoring,catatan_adime_gizi.evaluasi,catatan_adime_gizi.instruksi,"+
                    "catatan_adime_gizi.nip,petugas.nama "+
                    "from catatan_adime_gizi inner join reg_periksa on catatan_adime_gizi.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on catatan_adime_gizi.nip=petugas.nip where reg_periksa.no_rawat='"+tbadime.getValueAt(tbadime.getSelectedRow(),0).toString()+"'",param);
        }
    }//GEN-LAST:event_MnCatatanADIMEActionPerformed

    private void AsesmenadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AsesmenadimeKeyPressed
        Valid.pindah2(evt,KdPetugasadime,Diagnosisadime);
    }//GEN-LAST:event_AsesmenadimeKeyPressed

    private void MonitoringadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MonitoringadimeKeyPressed
        Valid.pindah2(evt,Intervensiadime,Evaluasiadime);
    }//GEN-LAST:event_MonitoringadimeKeyPressed

    private void DiagnosisadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosisadimeKeyPressed
       Valid.pindah2(evt,Asesmenadime,Intervensiadime);
    }//GEN-LAST:event_DiagnosisadimeKeyPressed

    private void EvaluasiadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EvaluasiadimeKeyPressed
        Valid.pindah2(evt,Monitoringadime,Instruksiadime);
    }//GEN-LAST:event_EvaluasiadimeKeyPressed

    private void IntervensiadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntervensiadimeKeyPressed
        Valid.pindah2(evt,Diagnosisadime,Monitoringadime);
    }//GEN-LAST:event_IntervensiadimeKeyPressed

    private void InstruksiadimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_InstruksiadimeKeyPressed
        Valid.pindah2(evt,Evaluasiadime,BtnSimpanadime);
    }//GEN-LAST:event_InstruksiadimeKeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMCatatanADIMEGizi dialog = new RMCatatanADIMEGizi(new javax.swing.JFrame(), true);
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
    private widget.TextArea Asesmenadime;
    private widget.Button BtnAlladime;
    private widget.Button BtnBataladime;
    private widget.Button BtnCariadime;
    private widget.Button BtnEditadime;
    private widget.Button BtnHapusadime;
    private widget.Button BtnKeluaradime;
    private widget.Button BtnPrintadime;
    private widget.Button BtnSimpanadime;
    private widget.CekBox ChkInputadime;
    private widget.CekBox ChkKejadianadime;
    private widget.Tanggal DTPCari1adime;
    private widget.Tanggal DTPCari2adime;
    private widget.ComboBox Detikadime;
    private widget.TextArea Diagnosisadime;
    private widget.TextArea Evaluasiadime;
    private widget.PanelBiasa FormInputadime;
    private widget.TextArea Instruksiadime;
    private widget.TextArea Intervensiadime;
    private widget.TextBox JK;
    private widget.ComboBox Jamadime;
    private widget.TextBox KdPetugasadime;
    private widget.Label LCountadime;
    private widget.ComboBox Menitadime;
    private javax.swing.JMenuItem MnCatatanADIME;
    private widget.TextArea Monitoringadime;
    private widget.TextBox NmPetugasadime;
    private javax.swing.JPanel PanelInputadime;
    private widget.ScrollPane Scrolladime;
    private widget.TextBox TCariadime;
    private widget.TextBox TNoRMadime;
    private widget.TextBox TNoRwadime;
    private widget.TextBox TPasienadime;
    private widget.Tanggal Tanggaladime;
    private widget.TextBox Umur;
    private widget.Button btnPetugasadime;
    private widget.InternalFrame internalFrame1adime;
    private widget.Label jLabel16adime;
    private widget.Label jLabel18adime;
    private widget.Label jLabel19adime;
    private widget.Label jLabel21adime;
    private widget.Label jLabel24adime;
    private widget.Label jLabel25adime;
    private widget.Label jLabel26adime;
    private widget.Label jLabel29adime;
    private widget.Label jLabel30adime;
    private widget.Label jLabel31adime;
    private widget.Label jLabel4adime;
    private widget.Label jLabel6adime;
    private widget.Label jLabel7adime;
    private javax.swing.JPanel jPanel3adime;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.panelisi panelGlass8adime;
    private widget.panelisi panelGlass9adime;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane1adime;
    private widget.ScrollPane scrollPane2adime;
    private widget.ScrollPane scrollPane3adime;
    private widget.ScrollPane scrollPane4adime;
    private widget.ScrollPane scrollPane5adime;
    private widget.ScrollPane scrollPane6adime;
    private widget.Table tbadime;
    // End of variables declaration//GEN-END:variables

    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            if(TCariadime.getText().toString().trim().equals("")){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,catatan_adime_gizi.tanggal,catatan_adime_gizi.asesmen,catatan_adime_gizi.diagnosis,"+
                    "catatan_adime_gizi.intervensi,catatan_adime_gizi.monitoring,catatan_adime_gizi.evaluasi,catatan_adime_gizi.instruksi,"+
                    "catatan_adime_gizi.nip,petugas.nama "+
                    "from catatan_adime_gizi inner join reg_periksa on catatan_adime_gizi.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on catatan_adime_gizi.nip=petugas.nip where "+
                    "catatan_adime_gizi.tanggal between ? and ? order by catatan_adime_gizi.tanggal ");
            }else{
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,catatan_adime_gizi.tanggal,catatan_adime_gizi.asesmen,catatan_adime_gizi.diagnosis,"+
                    "catatan_adime_gizi.intervensi,catatan_adime_gizi.monitoring,catatan_adime_gizi.evaluasi,catatan_adime_gizi.instruksi,"+
                    "catatan_adime_gizi.nip,petugas.nama "+
                    "from catatan_adime_gizi inner join reg_periksa on catatan_adime_gizi.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on catatan_adime_gizi.nip=petugas.nip where "+
                    "catatan_adime_gizi.tanggal between ? and ? and "+
                    "(reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or "+
                    "catatan_adime_gizi.asesmen like ? or catatan_adime_gizi.diagnosis like ? or "+
                    "catatan_adime_gizi.intervensi like ? or catatan_adime_gizi.monitoring like ? or "+
                    "catatan_adime_gizi.evaluasi like ? or catatan_adime_gizi.instruksi like ? or "+
                    "catatan_adime_gizi.nip like ? or petugas.nama like ?) "+
                    "order by catatan_adime_gizi.tanggal ");
            }
                
            try {
                if(TCariadime.getText().toString().trim().equals("")){
                    ps.setString(1,Valid.SetTgl(DTPCari1adime.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2adime.getSelectedItem()+"")+" 23:59:59");
                }else{
                    ps.setString(1,Valid.SetTgl(DTPCari1adime.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2adime.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(3,"%"+TCariadime.getText()+"%");
                    ps.setString(4,"%"+TCariadime.getText()+"%");
                    ps.setString(5,"%"+TCariadime.getText()+"%");
                    ps.setString(6,"%"+TCariadime.getText()+"%");
                    ps.setString(7,"%"+TCariadime.getText()+"%");
                    ps.setString(8,"%"+TCariadime.getText()+"%");
                    ps.setString(9,"%"+TCariadime.getText()+"%");
                    ps.setString(10,"%"+TCariadime.getText()+"%");
                    ps.setString(11,"%"+TCariadime.getText()+"%");
                    ps.setString(12,"%"+TCariadime.getText()+"%");
                    ps.setString(13,"%"+TCariadime.getText()+"%");
                }
                    
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("umurdaftar")+" "+rs.getString("sttsumur"),rs.getString("jk"),
                        rs.getString("tanggal"),rs.getString("asesmen"),rs.getString("diagnosis"),
                        rs.getString("intervensi"),rs.getString("monitoring"),rs.getString("evaluasi"),
                        rs.getString("instruksi"),rs.getString("nip"),rs.getString("nama")
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
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }
        LCountadime.setText(""+tabMode.getRowCount());
    }

    public void emptTeks() {
        Asesmenadime.setText("");
        Diagnosisadime.setText("");
        Intervensiadime.setText("");
        Monitoringadime.setText("");
        Evaluasiadime.setText("");
        Instruksiadime.setText("");
        Tanggaladime.setDate(new Date());
        Asesmenadime.requestFocus();
    } 

    private void getData() {
        if(tbadime.getSelectedRow()!= -1){
            TNoRwadime.setText(tbadime.getValueAt(tbadime.getSelectedRow(),0).toString());
            TNoRMadime.setText(tbadime.getValueAt(tbadime.getSelectedRow(),1).toString());
            TPasienadime.setText(tbadime.getValueAt(tbadime.getSelectedRow(),2).toString());
            Umur.setText(tbadime.getValueAt(tbadime.getSelectedRow(),3).toString());
            JK.setText(tbadime.getValueAt(tbadime.getSelectedRow(),4).toString());
            Valid.SetTgl(Tanggaladime,tbadime.getValueAt(tbadime.getSelectedRow(),5).toString());  
            Jamadime.setSelectedItem(tbadime.getValueAt(tbadime.getSelectedRow(),5).toString().substring(11,13));
            Menitadime.setSelectedItem(tbadime.getValueAt(tbadime.getSelectedRow(),5).toString().substring(14,15));
            Detikadime.setSelectedItem(tbadime.getValueAt(tbadime.getSelectedRow(),5).toString().substring(17,19));
            Asesmenadime.setText(tbadime.getValueAt(tbadime.getSelectedRow(),6).toString());
            Diagnosisadime.setText(tbadime.getValueAt(tbadime.getSelectedRow(),7).toString());
            Intervensiadime.setText(tbadime.getValueAt(tbadime.getSelectedRow(),8).toString());
            Monitoringadime.setText(tbadime.getValueAt(tbadime.getSelectedRow(),9).toString());
            Evaluasiadime.setText(tbadime.getValueAt(tbadime.getSelectedRow(),10).toString());
            Instruksiadime.setText(tbadime.getValueAt(tbadime.getSelectedRow(),11).toString());
        }
    }

    private void isRawat() {
        try {
            ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.jk,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "reg_periksa.tgl_registrasi from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "where reg_periksa.no_rawat=?");
            try {
                ps.setString(1,TNoRwadime.getText());
                rs=ps.executeQuery();
                if(rs.next()){
                    TNoRMadime.setText(rs.getString("no_rkm_medis"));
                    DTPCari1adime.setDate(rs.getDate("tgl_registrasi"));
                    TPasienadime.setText(rs.getString("nm_pasien"));
                    JK.setText(rs.getString("jk"));
                    Umur.setText(rs.getString("umurdaftar")+" "+rs.getString("sttsumur"));
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
        TNoRwadime.setText(norwt);
        TCariadime.setText(norwt);
        DTPCari2adime.setDate(tgl2);
        isRawat();              
        ChkInputadime.setSelected(true);
        isForm();
    }
    
    private void isForm(){
        if(ChkInputadime.isSelected()==true){
            if(internalFrame1adime.getHeight()>478){
                ChkInputadime.setVisible(false);
                PanelInputadime.setPreferredSize(new Dimension(WIDTH,306));
                FormInputadime.setVisible(true);      
                ChkInputadime.setVisible(true);
            }else{
                ChkInputadime.setVisible(false);
                PanelInputadime.setPreferredSize(new Dimension(WIDTH,internalFrame1adime.getHeight()-172));
                FormInputadime.setVisible(true);      
                ChkInputadime.setVisible(true);
            }
        }else if(ChkInputadime.isSelected()==false){           
            ChkInputadime.setVisible(false);            
            PanelInputadime.setPreferredSize(new Dimension(WIDTH,20));
            FormInputadime.setVisible(false);      
            ChkInputadime.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpanadime.setEnabled(akses.getcatatan_adime_gizi());
        BtnHapusadime.setEnabled(akses.getcatatan_adime_gizi());
        BtnEditadime.setEnabled(akses.getcatatan_adime_gizi());
        BtnPrintadime.setEnabled(akses.getcatatan_adime_gizi()); 
        if(akses.getjml2()>=1){
            KdPetugasadime.setEditable(false);
            btnPetugasadime.setEnabled(false);
            KdPetugasadime.setText(akses.getkode());
            NmPetugasadime.setText(petugas.tampil3(KdPetugasadime.getText()));
            if(NmPetugasadime.getText().equals("")){
                KdPetugasadime.setText("");
                JOptionPane.showMessageDialog(null,"User login bukan petugas...!!");
            }
        }            
    }

    private void jam(){
        ActionListener taskPerformer = new ActionListener(){
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;
            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                
                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if(ChkKejadianadime.isSelected()==true){
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                }else if(ChkKejadianadime.isSelected()==false){
                    nilai_jam =Jamadime.getSelectedIndex();
                    nilai_menit =Menitadime.getSelectedIndex();
                    nilai_detik =Detikadime.getSelectedIndex();
                }

                // Jika nilai JAM lebih kecil dari 10 (hanya 1 digit)
                if (nilai_jam <= 9) {
                    // Tambahkan "0" didepannya
                    nol_jam = "0";
                }
                // Jika nilai MENIT lebih kecil dari 10 (hanya 1 digit)
                if (nilai_menit <= 9) {
                    // Tambahkan "0" didepannya
                    nol_menit = "0";
                }
                // Jika nilai DETIK lebih kecil dari 10 (hanya 1 digit)
                if (nilai_detik <= 9) {
                    // Tambahkan "0" didepannya
                    nol_detik = "0";
                }
                // Membuat String JAM, MENIT, DETIK
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                // Menampilkan pada Layar
                //tampil_jam.setText("  " + jam + " : " + menit + " : " + detik + "  ");
                Jamadime.setSelectedItem(jam);
                Menitadime.setSelectedItem(menit);
                Detikadime.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }

    private void ganti() {
        if(Sequel.mengedittf("catatan_adime_gizi","tanggal=? and no_rawat=?","no_rawat=?,tanggal=?,asesmen=?,diagnosis=?,intervensi=?,monitoring=?,evaluasi=?,instruksi=?,nip=?",11,new String[]{
            TNoRwadime.getText(),Valid.SetTgl(Tanggaladime.getSelectedItem()+"")+" "+Jamadime.getSelectedItem()+":"+Menitadime.getSelectedItem()+":"+Detikadime.getSelectedItem(),Asesmenadime.getText(),Diagnosisadime.getText(),
            Intervensiadime.getText(),Monitoringadime.getText(),Evaluasiadime.getText(),Instruksiadime.getText(),KdPetugasadime.getText(),tbadime.getValueAt(tbadime.getSelectedRow(),5).toString(),
            tbadime.getValueAt(tbadime.getSelectedRow(),0).toString()
        })==true){
            tbadime.setValueAt(TNoRwadime.getText(),tbadime.getSelectedRow(),0);
            tbadime.setValueAt(TNoRMadime.getText(),tbadime.getSelectedRow(),1);
            tbadime.setValueAt(TPasienadime.getText(),tbadime.getSelectedRow(),2);
            tbadime.setValueAt(Umur.getText(),tbadime.getSelectedRow(),3);
            tbadime.setValueAt(JK.getText(),tbadime.getSelectedRow(),4);
            tbadime.setValueAt(Valid.SetTgl(Tanggaladime.getSelectedItem()+"")+" "+Jamadime.getSelectedItem()+":"+Menitadime.getSelectedItem()+":"+Detikadime.getSelectedItem(),tbadime.getSelectedRow(),5);
            tbadime.setValueAt(Asesmenadime.getText(),tbadime.getSelectedRow(),6);
            tbadime.setValueAt(Diagnosisadime.getText(),tbadime.getSelectedRow(),7);
            tbadime.setValueAt(Intervensiadime.getText(),tbadime.getSelectedRow(),8);
            tbadime.setValueAt(Monitoringadime.getText(),tbadime.getSelectedRow(),9);
            tbadime.setValueAt(Evaluasiadime.getText(),tbadime.getSelectedRow(),10);
            tbadime.setValueAt(Instruksiadime.getText(),tbadime.getSelectedRow(),11);
            tbadime.setValueAt(KdPetugasadime.getText(),tbadime.getSelectedRow(),12);
            tbadime.setValueAt(NmPetugasadime.getText(),tbadime.getSelectedRow(),13);
            emptTeks();
        }
    }

    private void hapus() {
        if(Sequel.queryu2tf("delete from catatan_adime_gizi where tanggal=? and no_rawat=?",2,new String[]{
            tbadime.getValueAt(tbadime.getSelectedRow(),5).toString(),tbadime.getValueAt(tbadime.getSelectedRow(),0).toString()
        })==true){
            tabMode.removeRow(tbadime.getSelectedRow());
            LCountadime.setText(""+tabMode.getRowCount());
            emptTeks();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }
    
}
