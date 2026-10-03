

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
import static java.awt.image.ImageObserver.WIDTH;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;
import simrskhanza.DlgCariPoli;
import simrskhanza.DlgCariCaraBayar;

/**
 *
 * @author perpustakaan
 */
public final class DlgDetailTindakanRemun extends javax.swing.JDialog {
    private final Connection koneksi=koneksiDB.condb();
    private final sekuel Sequel=new sekuel();
    private DefaultTableModel tabModeRalanDokter;
    private validasi Valid=new validasi();
    private ResultSet rs,rs2,rssetjam;
    private PreparedStatement ps,psanak,pssetjam;
    private int i=0;
    private String nosep="",jenistindakanok="",rawatgabung="",ruang="",kodedokter="",jenis="",kodepetugas="";
    private DlgCariPoli poli=new DlgCariPoli(null,false);
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private DlgCariPetugas petugas=new DlgCariPetugas(null,false);
    private DlgCariCaraBayar penjab=new DlgCariCaraBayar(null,false);
    private double material=0,bhp=0,jmdokter=0,jmpetugas=0,jmperujuk=0,kso=0,
            menejemen=0,total=0,biayaoperator1=0,biayaoperator2=0, 
            biayaoperator3=0,biayaasisten_operator1=0,biayaasisten_operator2=0,
            biayaasisten_operator3=0,biayainstrumen=0,biayadokter_anak=0,
            biayaperawaat_resusitas=0,biayadokter_anestesi=0,biayaasisten_anestesi=0,
            biayaasisten_anestesi2=0,biayabidan=0,biayabidan2=0,biayabidan3=0,
            biayaperawat_luar=0,biayaalat=0,biayasewaok=0,akomodasi=0,
            bagian_rs=0,biaya_omloop=0,biaya_omloop2=0,biaya_omloop3=0,
            biaya_omloop4=0,biaya_omloop5=0,biayasarpras=0,biaya_dokter_pjanak=0,
            biaya_dokter_umum=0,persenbayi=0;
    private DateFormat format=new SimpleDateFormat("yyyy-MM-dd");

    /** Creates new form DlgLhtBiaya
     * @param parent
     * @param modal */
    public DlgDetailTindakanRemun(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(885,674);
        
        tabModeRalanDokter=new DefaultTableModel(null,new Object[]{
            "No.","No.R.M.","No.Rawat","No. SEP","Nama Pasien","Jenis Tindakan","Kd.Tnd","Perawatan/Tindakan","Kode Dokter",
            "Dokter Yg Menangani","Kode Petugas","Petugas yang menangani","Tanggal","Jam","Cara Bayar","Ruangan",
            "Jasa Sarana","Paket BHP","JM Dokter","KSO","Menejemen","Total","Ruang","Input","Jenis","Jumlah","Tarif","Keterangan","Dokter","Petugas"}){
//                "No.","No.R.M.","No.Rawat","No. SEP","Nama Pasien","Jenis Tindakan","Kd.Tnd","Perawatan/Tindakan","Kode Dokter",
//            "Dokter Yg Menangani","Kode Petugas","Petugas yang menangani","Tanggal","Jam","Cara Bayar","Ruangan",
//            "Jasa Sarana","Paket BHP","JM Dokter","KSO","Menejemen","Total"
             @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
             Class[] types = new Class[] {
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Double.class,
                 java.lang.Double.class,java.lang.Double.class,java.lang.Double.class,
                 java.lang.Double.class,java.lang.Double.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRalanDokter.setModel(tabModeRalanDokter);
        tbRalanDokter.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRalanDokter.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 30; i++) {
            TableColumn column = tbRalanDokter.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(30);
            }else if(i==1){
                column.setPreferredWidth(70);
            }else if(i==2){
                column.setPreferredWidth(105);
            }else if(i==3){
                column.setPreferredWidth(150);
            }else if(i==4){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setPreferredWidth(75);
            }else if(i==6){
                column.setPreferredWidth(75);
            }else if(i==7){
                column.setPreferredWidth(200);
            }else if(i==8){
                column.setPreferredWidth(75);
            }else if(i==9){
                column.setPreferredWidth(150);
            }else if(i==10){
                column.setPreferredWidth(70);
            }else if(i==11){
                column.setPreferredWidth(55);
            }else if(i==12){
                column.setPreferredWidth(70);
            }else if(i==13){
                column.setPreferredWidth(55);
            }else if(i==14){
                column.setPreferredWidth(110);
            }else if(i==15){
                column.setPreferredWidth(130);
            }else if(i==16){
//                column.setPreferredWidth(75);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==17){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==18){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==19){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==20){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==21){
                column.setPreferredWidth(80);
            }else{
                column.setPreferredWidth(100);
            }
        }
        tbRalanDokter.setDefaultRenderer(Object.class, new WarnaTable());
        
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){
                    KdDokterRalanDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                    NmDokterRalanDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                    BtnDokterRalanDokter.requestFocus();
                }      
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {dokter.emptTeks();}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });   
        
        dokter.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    dokter.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        petugas.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    petugas.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        poli.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(poli.getTable().getSelectedRow()!= -1){
                    KdPoliRalanDokter.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),0).toString());
                    NmPoliRalanDokter.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),1).toString());
                    BtnPoliRalanDokter.requestFocus();
                }      
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {poli.emptTeks();}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        penjab.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(penjab.getTable().getSelectedRow()!= -1){
                    KdCaraBayarRalanDokter.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(),1).toString());
                    NmCaraBayarRalanDokter.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(),2).toString());
                    BtnCaraBayarRalanDokter.requestFocus();
                }      
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {penjab.emptTeks();}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });   
        
        penjab.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    penjab.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
            
        ChkInput.setSelected(false);
        isForm();
        
        Valid.SetTgl2(Tgl1,format.format(new Date())+" 00:00:00");
        Valid.SetTgl2(Tgl2,format.format(new Date())+" 23:59:59");
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
        internalFrame2 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbRalanDokter = new widget.Table();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        FormInput = new widget.panelisi();
        label17 = new widget.Label();
        KdDokterRalanDokter = new widget.TextBox();
        NmDokterRalanDokter = new widget.TextBox();
        BtnDokterRalanDokter = new widget.Button();
        label19 = new widget.Label();
        KdCaraBayarRalanDokter = new widget.TextBox();
        NmCaraBayarRalanDokter = new widget.TextBox();
        BtnCaraBayarRalanDokter = new widget.Button();
        label20 = new widget.Label();
        KdPoliRalanDokter = new widget.TextBox();
        NmPoliRalanDokter = new widget.TextBox();
        BtnPoliRalanDokter = new widget.Button();
        label24 = new widget.Label();
        KdPetugasRalanDokterParamedis = new widget.TextBox();
        NmPetugasRalanDokterParamedis = new widget.TextBox();
        BtnPetugasRalanDokterParamedis = new widget.Button();
        panelGlass5 = new widget.panelisi();
        label9 = new widget.Label();
        Tgl1 = new widget.Tanggal();
        label18 = new widget.Label();
        Tgl2 = new widget.Tanggal();
        jLabel12 = new widget.Label();
        cmbStatus = new widget.ComboBox();
        label10 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        BtnPrint = new widget.Button();
        BtnKeluar = new widget.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Detail Tindakan Untuk Remunerasi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        internalFrame2.setBackground(new java.awt.Color(235, 255, 235));
        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbRalanDokter.setName("tbRalanDokter"); // NOI18N
        Scroll.setViewportView(tbRalanDokter);

        internalFrame2.add(Scroll, java.awt.BorderLayout.CENTER);

        PanelInput.setBackground(new java.awt.Color(255, 255, 255));
        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 100));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('M');
        ChkInput.setText(".: Filter Data");
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

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 74));
        FormInput.setLayout(null);

        label17.setText("Dokter :");
        label17.setName("label17"); // NOI18N
        label17.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput.add(label17);
        label17.setBounds(0, 10, 55, 23);

        KdDokterRalanDokter.setEditable(false);
        KdDokterRalanDokter.setName("KdDokterRalanDokter"); // NOI18N
        KdDokterRalanDokter.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput.add(KdDokterRalanDokter);
        KdDokterRalanDokter.setBounds(58, 10, 65, 23);

        NmDokterRalanDokter.setEditable(false);
        NmDokterRalanDokter.setName("NmDokterRalanDokter"); // NOI18N
        NmDokterRalanDokter.setPreferredSize(new java.awt.Dimension(150, 23));
        FormInput.add(NmDokterRalanDokter);
        NmDokterRalanDokter.setBounds(125, 10, 150, 23);

        BtnDokterRalanDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokterRalanDokter.setMnemonic('3');
        BtnDokterRalanDokter.setToolTipText("Alt+3");
        BtnDokterRalanDokter.setName("BtnDokterRalanDokter"); // NOI18N
        BtnDokterRalanDokter.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDokterRalanDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDokterRalanDokterActionPerformed(evt);
            }
        });
        FormInput.add(BtnDokterRalanDokter);
        BtnDokterRalanDokter.setBounds(278, 10, 28, 23);

        label19.setText("Cara Bayar :");
        label19.setName("label19"); // NOI18N
        label19.setPreferredSize(new java.awt.Dimension(75, 23));
        FormInput.add(label19);
        label19.setBounds(640, 10, 70, 23);

        KdCaraBayarRalanDokter.setEditable(false);
        KdCaraBayarRalanDokter.setName("KdCaraBayarRalanDokter"); // NOI18N
        KdCaraBayarRalanDokter.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput.add(KdCaraBayarRalanDokter);
        KdCaraBayarRalanDokter.setBounds(713, 10, 65, 23);

        NmCaraBayarRalanDokter.setEditable(false);
        NmCaraBayarRalanDokter.setName("NmCaraBayarRalanDokter"); // NOI18N
        NmCaraBayarRalanDokter.setPreferredSize(new java.awt.Dimension(150, 23));
        FormInput.add(NmCaraBayarRalanDokter);
        NmCaraBayarRalanDokter.setBounds(780, 10, 150, 23);

        BtnCaraBayarRalanDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnCaraBayarRalanDokter.setMnemonic('3');
        BtnCaraBayarRalanDokter.setToolTipText("Alt+3");
        BtnCaraBayarRalanDokter.setName("BtnCaraBayarRalanDokter"); // NOI18N
        BtnCaraBayarRalanDokter.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCaraBayarRalanDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCaraBayarRalanDokterActionPerformed(evt);
            }
        });
        FormInput.add(BtnCaraBayarRalanDokter);
        BtnCaraBayarRalanDokter.setBounds(933, 10, 28, 23);

        label20.setText("Unit/Poli :");
        label20.setName("label20"); // NOI18N
        label20.setPreferredSize(new java.awt.Dimension(45, 23));
        FormInput.add(label20);
        label20.setBounds(310, 10, 62, 23);

        KdPoliRalanDokter.setEditable(false);
        KdPoliRalanDokter.setName("KdPoliRalanDokter"); // NOI18N
        KdPoliRalanDokter.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput.add(KdPoliRalanDokter);
        KdPoliRalanDokter.setBounds(375, 10, 65, 23);

        NmPoliRalanDokter.setEditable(false);
        NmPoliRalanDokter.setName("NmPoliRalanDokter"); // NOI18N
        NmPoliRalanDokter.setPreferredSize(new java.awt.Dimension(150, 23));
        FormInput.add(NmPoliRalanDokter);
        NmPoliRalanDokter.setBounds(442, 10, 150, 23);

        BtnPoliRalanDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPoliRalanDokter.setMnemonic('3');
        BtnPoliRalanDokter.setToolTipText("Alt+3");
        BtnPoliRalanDokter.setName("BtnPoliRalanDokter"); // NOI18N
        BtnPoliRalanDokter.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPoliRalanDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPoliRalanDokterActionPerformed(evt);
            }
        });
        FormInput.add(BtnPoliRalanDokter);
        BtnPoliRalanDokter.setBounds(595, 10, 28, 23);

        label24.setText("Petugas :");
        label24.setName("label24"); // NOI18N
        label24.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput.add(label24);
        label24.setBounds(0, 40, 55, 23);

        KdPetugasRalanDokterParamedis.setEditable(false);
        KdPetugasRalanDokterParamedis.setName("KdPetugasRalanDokterParamedis"); // NOI18N
        KdPetugasRalanDokterParamedis.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput.add(KdPetugasRalanDokterParamedis);
        KdPetugasRalanDokterParamedis.setBounds(58, 40, 65, 23);

        NmPetugasRalanDokterParamedis.setEditable(false);
        NmPetugasRalanDokterParamedis.setName("NmPetugasRalanDokterParamedis"); // NOI18N
        NmPetugasRalanDokterParamedis.setPreferredSize(new java.awt.Dimension(150, 23));
        FormInput.add(NmPetugasRalanDokterParamedis);
        NmPetugasRalanDokterParamedis.setBounds(125, 40, 150, 23);

        BtnPetugasRalanDokterParamedis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPetugasRalanDokterParamedis.setMnemonic('3');
        BtnPetugasRalanDokterParamedis.setToolTipText("Alt+3");
        BtnPetugasRalanDokterParamedis.setName("BtnPetugasRalanDokterParamedis"); // NOI18N
        BtnPetugasRalanDokterParamedis.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPetugasRalanDokterParamedis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPetugasRalanDokterParamedisActionPerformed(evt);
            }
        });
        FormInput.add(BtnPetugasRalanDokterParamedis);
        BtnPetugasRalanDokterParamedis.setBounds(278, 40, 28, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        internalFrame2.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(internalFrame2, java.awt.BorderLayout.CENTER);

        panelGlass5.setName("panelGlass5"); // NOI18N
        panelGlass5.setPreferredSize(new java.awt.Dimension(55, 44));
        panelGlass5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 9));

        label9.setText("Tanggal :");
        label9.setName("label9"); // NOI18N
        label9.setPreferredSize(new java.awt.Dimension(52, 23));
        panelGlass5.add(label9);

        Tgl1.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        Tgl1.setName("Tgl1"); // NOI18N
        Tgl1.setPreferredSize(new java.awt.Dimension(132, 23));
        Tgl1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl1KeyPressed(evt);
            }
        });
        panelGlass5.add(Tgl1);

        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText("s.d.");
        label18.setName("label18"); // NOI18N
        label18.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass5.add(label18);

        Tgl2.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        Tgl2.setName("Tgl2"); // NOI18N
        Tgl2.setPreferredSize(new java.awt.Dimension(132, 23));
        Tgl2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl2KeyPressed(evt);
            }
        });
        panelGlass5.add(Tgl2);

        jLabel12.setText("Status :");
        jLabel12.setName("jLabel12"); // NOI18N
        jLabel12.setPreferredSize(new java.awt.Dimension(53, 23));
        panelGlass5.add(jLabel12);

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Piutang Belum Lunas", "Piutang Sudah Lunas", "Sudah Bayar Non Piutang", "Belum Terclosing Kasir" }));
        cmbStatus.setName("cmbStatus"); // NOI18N
        cmbStatus.setPreferredSize(new java.awt.Dimension(180, 23));
        panelGlass5.add(cmbStatus);

        label10.setText("Key Word :");
        label10.setName("label10"); // NOI18N
        label10.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass5.add(label10);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(187, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass5.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('1');
        BtnCari.setText("Tampilkan");
        BtnCari.setToolTipText("Alt+1");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(100, 23));
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
        panelGlass5.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('2');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("2Alt+2");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 23));
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
        panelGlass5.add(BtnAll);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Print");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 23));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnPrint);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 23));
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
        panelGlass5.add(BtnKeluar);

        internalFrame1.add(panelGlass5, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,Tgl1,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            CetakRalanDokter();
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void Tgl1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl1KeyPressed
        Valid.pindah(evt, BtnKeluar, Tgl2);
    }//GEN-LAST:event_Tgl1KeyPressed

    private void Tgl2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl2KeyPressed
        
    }//GEN-LAST:event_Tgl2KeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
//        TabRawatMouseClicked(null);
        Valid.tabelKosong(tabModeRalanDokter);
        tampildrralan();
        tampilralandokterpetugas();
        tampiltindakanok();
        tampildokterranap();
        tampildokterpetugasranap();
        tampilperiksaradiologi();
        tampilperiksalaboratorium();
        tampildetailperiksalaboratorium();
        tampiltindakanbdrs();
        tampilKamarInap();
    }//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnKeluar);
        }
    }//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        cmbStatus.setSelectedIndex(0);
        KdDokterRalanDokter.setText("");
        NmDokterRalanDokter.setText("");
        KdPoliRalanDokter.setText("");
        NmPoliRalanDokter.setText("");
        KdCaraBayarRalanDokter.setText("");
        NmCaraBayarRalanDokter.setText("");
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCari, TCari);
        }
    }//GEN-LAST:event_BtnAllKeyPressed

    private void BtnDokterRalanDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterRalanDokterActionPerformed
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setAlwaysOnTop(false);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDokterRalanDokterActionPerformed

    private void BtnCaraBayarRalanDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCaraBayarRalanDokterActionPerformed
        penjab.isCek();
        penjab.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        penjab.setLocationRelativeTo(internalFrame1);
        penjab.setAlwaysOnTop(false);
        penjab.setVisible(true);
    }//GEN-LAST:event_BtnCaraBayarRalanDokterActionPerformed

    private void BtnPoliRalanDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPoliRalanDokterActionPerformed
        poli.isCek();
        poli.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        poli.setLocationRelativeTo(internalFrame1);
        poli.setAlwaysOnTop(false);
        poli.setVisible(true);
    }//GEN-LAST:event_BtnPoliRalanDokterActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void BtnPetugasRalanDokterParamedisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPetugasRalanDokterParamedisActionPerformed
//        BtnPetugasRalanParamedisActionPerformed(null);
    }//GEN-LAST:event_BtnPetugasRalanDokterParamedisActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgDetailTindakanRemun dialog = new DlgDetailTindakanRemun(new javax.swing.JFrame(), true);
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
    private widget.Button BtnCaraBayarRalanDokter;
    private widget.Button BtnCari;
    private widget.Button BtnDokterRalanDokter;
    private widget.Button BtnKeluar;
    private widget.Button BtnPetugasRalanDokterParamedis;
    private widget.Button BtnPoliRalanDokter;
    private widget.Button BtnPrint;
    private widget.CekBox ChkInput;
    private widget.panelisi FormInput;
    private widget.TextBox KdCaraBayarRalanDokter;
    private widget.TextBox KdDokterRalanDokter;
    private widget.TextBox KdPetugasRalanDokterParamedis;
    private widget.TextBox KdPoliRalanDokter;
    private widget.TextBox NmCaraBayarRalanDokter;
    private widget.TextBox NmDokterRalanDokter;
    private widget.TextBox NmPetugasRalanDokterParamedis;
    private widget.TextBox NmPoliRalanDokter;
    private javax.swing.JPanel PanelInput;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private widget.ComboBox cmbStatus;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.Label jLabel12;
    private widget.Label label10;
    private widget.Label label17;
    private widget.Label label18;
    private widget.Label label19;
    private widget.Label label20;
    private widget.Label label24;
    private widget.Label label9;
    private widget.panelisi panelGlass5;
    private widget.Table tbRalanDokter;
    // End of variables declaration//GEN-END:variables

    public void tampildrralan(){     
//        Valid.tabelKosong(tabModeRalanDokter);
        try{
            if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPoliRalanDokter.getText().equals("")&&NmPoliRalanDokter.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                ps=koneksi.prepareStatement(
                   "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                   "pasien.nm_pasien,rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                   "rawat_jl_dr.kd_dokter,dokter.nm_dokter,rawat_jl_dr.tgl_perawatan,"+
                   "rawat_jl_dr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                   "rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.tarif_tindakandr,"+
                   "rawat_jl_dr.kso,rawat_jl_dr.menejemen,rawat_jl_dr.biaya_rawat,penjab.kd_pj  "+
                   "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                   "inner join rawat_jl_dr on reg_periksa.no_rawat=rawat_jl_dr.no_rawat "+
                   "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                   "inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                   "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                   "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                   "where concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? order by rawat_jl_dr.no_rawat desc");
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    ps=koneksi.prepareStatement(
                        "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                        "pasien.nm_pasien,rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                        "rawat_jl_dr.kd_dokter,dokter.nm_dokter,rawat_jl_dr.tgl_perawatan,"+
                        "rawat_jl_dr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                        "rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.tarif_tindakandr,"+
                        "rawat_jl_dr.kso,rawat_jl_dr.menejemen,rawat_jl_dr.biaya_rawat,penjab.kd_pj  "+
                        "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join rawat_jl_dr on reg_periksa.no_rawat=rawat_jl_dr.no_rawat "+
                        "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                        "inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                        "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                        "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                        "where concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_dr.no_rawat like ? or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan.nm_perawatan like ? or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_dr.kd_dokter like ? or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and poliklinik.nm_poli like ? "+
                       " order by rawat_jl_dr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
                    ps=koneksi.prepareStatement(
                        "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                        "pasien.nm_pasien,rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                        "rawat_jl_dr.kd_dokter,dokter.nm_dokter,rawat_jl_dr.tgl_perawatan,"+
                        "rawat_jl_dr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                        "rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.tarif_tindakandr,"+
                        "rawat_jl_dr.kso,rawat_jl_dr.menejemen,rawat_jl_dr.biaya_rawat,penjab.kd_pj  "+
                        "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join rawat_jl_dr on reg_periksa.no_rawat=rawat_jl_dr.no_rawat "+
                        "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                        "inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                        "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                        "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                        "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                        "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_dr.no_rawat like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan.nm_perawatan like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_dr.kd_dokter like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and poliklinik.nm_poli like ? "+
                       " order by rawat_jl_dr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
                    ps=koneksi.prepareStatement(
                        "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                        "pasien.nm_pasien,rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                        "rawat_jl_dr.kd_dokter,dokter.nm_dokter,rawat_jl_dr.tgl_perawatan,"+
                        "rawat_jl_dr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                        "rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.tarif_tindakandr,"+
                        "rawat_jl_dr.kso,rawat_jl_dr.menejemen,rawat_jl_dr.biaya_rawat,penjab.kd_pj  "+
                        "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join rawat_jl_dr on reg_periksa.no_rawat=rawat_jl_dr.no_rawat "+
                        "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                        "inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                        "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                        "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                        "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                        "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_dr.no_rawat like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan.nm_perawatan like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_dr.kd_dokter like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and poliklinik.nm_poli like ? "+
                       " order by rawat_jl_dr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
                    ps=koneksi.prepareStatement(
                        "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                        "pasien.nm_pasien,rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                        "rawat_jl_dr.kd_dokter,dokter.nm_dokter,rawat_jl_dr.tgl_perawatan,"+
                        "rawat_jl_dr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                        "rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.tarif_tindakandr,"+
                        "rawat_jl_dr.kso,rawat_jl_dr.menejemen,rawat_jl_dr.biaya_rawat,penjab.kd_pj  "+
                        "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join rawat_jl_dr on reg_periksa.no_rawat=rawat_jl_dr.no_rawat "+
                        "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                        "inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                        "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                        "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                        "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_dr.no_rawat like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan.nm_perawatan like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_dr.kd_dokter like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and poliklinik.nm_poli like ? "+
                       " order by rawat_jl_dr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
                    ps=koneksi.prepareStatement(
                        "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                        "pasien.nm_pasien,rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                        "rawat_jl_dr.kd_dokter,dokter.nm_dokter,rawat_jl_dr.tgl_perawatan,"+
                        "rawat_jl_dr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                        "rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.tarif_tindakandr,"+
                        "rawat_jl_dr.kso,rawat_jl_dr.menejemen,rawat_jl_dr.biaya_rawat,penjab.kd_pj "+
                        "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join rawat_jl_dr on reg_periksa.no_rawat=rawat_jl_dr.no_rawat "+
                        "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                        "inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                        "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                        "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                        "where reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_dr.no_rawat like ? or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan.nm_perawatan like ? or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_dr.kd_dokter like ? or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between ? and ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and poliklinik.nm_poli like ? "+
                       " order by rawat_jl_dr.no_rawat desc");
                }
            }
            
            try {
                if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPoliRalanDokter.getText().equals("")&&NmPoliRalanDokter.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                }else{
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(3,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(4,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(5,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(8,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(9,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(10,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(11,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(12,"%"+TCari.getText().trim()+"%");
                    ps.setString(13,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(14,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(15,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(16,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(17,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(18,"%"+TCari.getText().trim()+"%");
                    ps.setString(19,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(20,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(21,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(22,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(23,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(24,"%"+TCari.getText().trim()+"%");
                    ps.setString(25,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(26,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(27,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(28,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(29,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(30,"%"+TCari.getText().trim()+"%");
                    ps.setString(31,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(32,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(33,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(34,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(35,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(36,"%"+TCari.getText().trim()+"%");
                    ps.setString(37,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(38,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(39,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(40,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(41,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(42,"%"+TCari.getText().trim()+"%");
                    ps.setString(43,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(44,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(45,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(46,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(47,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(48,"%"+TCari.getText().trim()+"%");
                }
                    
                rs=ps.executeQuery();
                i=1;
                material=0;bhp=0;jmdokter=0;kso=0;menejemen=0;total=0;
                while(rs.next()){
                    material=material+rs.getDouble("material");
                    bhp=bhp+rs.getDouble("bhp");
                    jmdokter=jmdokter+rs.getDouble("tarif_tindakandr");
                    kso=kso+rs.getDouble("kso");
                    menejemen=menejemen+rs.getDouble("menejemen");
                    total=total+rs.getDouble("biaya_rawat");
                    if(rs.getString("kd_pj").equals("BPJ")){
                        nosep=Sequel.cariIsi("select bridging_sep.no_sep from bridging_sep where bridging_sep.no_rawat='"+rs.getString("no_rawat")+"'");
                    }else{
                        nosep=rs.getString("no_rawat");
                    }
                    if(rs.getString("nm_poli").equals("Unit IGD")){
                        ruang="UGD";
                    }else if(rs.getString("nm_poli").equals("IGD VK")){
                        ruang="IIDB";
                    }else{
                        ruang="Poli";
                    }
                    kodedokter=Sequel.cariIsi("select kd_dokter_remun from maping_dokter_dpjpremun where kd_dokter='"+rs.getString("kd_dokter")+"'");
                    tabModeRalanDokter.addRow(new Object[]{
                        i,rs.getString("no_rkm_medis"),rs.getString("no_rawat"),nosep,rs.getString("nm_pasien"),"Tindakan Dokter",rs.getString("kd_jenis_prw"),
                        rs.getString("nm_perawatan"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("tgl_perawatan"),
                        rs.getString("jam_rawat"),rs.getString("png_jawab"),rs.getString("nm_poli"),rs.getDouble("material"),
                        rs.getDouble("bhp"),rs.getDouble("tarif_tindakandr"),rs.getDouble("kso"),rs.getDouble("menejemen"),
                        rs.getDouble("biaya_rawat"),rs.getString("nm_poli"),ruang,"Tindakan Dokter","1",rs.getDouble("biaya_rawat"),rs.getString("nm_perawatan"),kodedokter,kodedokter
                    });
                    i++;
                }
                if(total>0){
                    tabModeRalanDokter.addRow(new Object[]{
                        "","","","","","","","","","","","","","","","Jumlah Total :",material,
                        bhp,jmdokter,kso,menejemen,total,"","","","","","","",""
                    });
                }
            } catch (Exception e) {
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
    
    public void tampilralandokterpetugas(){     
//        Valid.tabelKosong(tabModeRalanDokter);
        try{
            if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPetugasRalanDokterParamedis.getText().equals("")&&NmPetugasRalanDokterParamedis.getText().equals("")&&KdPoliRalanDokter.getText().equals("")&&NmPoliRalanDokter.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                ps=koneksi.prepareStatement(
                       "select rawat_jl_drpr.no_rawat,reg_periksa.no_rkm_medis,"+
                       "pasien.nm_pasien,rawat_jl_drpr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                       "rawat_jl_drpr.kd_dokter,dokter.nm_dokter,rawat_jl_drpr.nip,petugas.nama,rawat_jl_drpr.tgl_perawatan,"+
                       "rawat_jl_drpr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                       "rawat_jl_drpr.material,rawat_jl_drpr.bhp,rawat_jl_drpr.tarif_tindakandr,rawat_jl_drpr.tarif_tindakanpr,"+
                       "rawat_jl_drpr.kso,rawat_jl_drpr.menejemen,rawat_jl_drpr.biaya_rawat,penjab.kd_pj  "+
                       "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                       "inner join rawat_jl_drpr on rawat_jl_drpr.no_rawat=reg_periksa.no_rawat "+
                       "inner join jns_perawatan on rawat_jl_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                       "inner join dokter on rawat_jl_drpr.kd_dokter=dokter.kd_dokter "+
                       "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                       "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                       "inner join petugas on rawat_jl_drpr.nip=petugas.nip "+
                       "where concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? order by rawat_jl_drpr.no_rawat desc");
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    ps=koneksi.prepareStatement(
                           "select rawat_jl_drpr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_jl_drpr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                           "rawat_jl_drpr.kd_dokter,dokter.nm_dokter,rawat_jl_drpr.nip,petugas.nama,rawat_jl_drpr.tgl_perawatan,"+
                           "rawat_jl_drpr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                           "rawat_jl_drpr.material,rawat_jl_drpr.bhp,rawat_jl_drpr.tarif_tindakandr,rawat_jl_drpr.tarif_tindakanpr,"+
                           "rawat_jl_drpr.kso,rawat_jl_drpr.menejemen,rawat_jl_drpr.biaya_rawat,penjab.kd_pj  "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_jl_drpr on rawat_jl_drpr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan on rawat_jl_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                           "inner join dokter on rawat_jl_drpr.kd_dokter=dokter.kd_dokter "+
                           "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join petugas on rawat_jl_drpr.nip=petugas.nip "+
                           "where concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.no_rawat like ? or "+
                           "concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan.nm_perawatan like ? or "+
                           "concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.kd_dokter like ? or "+
                           "concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.nip like ? or "+
                           "concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                           "concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and poliklinik.nm_poli like ? "+
                           " order by rawat_jl_drpr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
                    ps=koneksi.prepareStatement(
                           "select rawat_jl_drpr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_jl_drpr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                           "rawat_jl_drpr.kd_dokter,dokter.nm_dokter,rawat_jl_drpr.nip,petugas.nama,rawat_jl_drpr.tgl_perawatan,"+
                           "rawat_jl_drpr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                           "rawat_jl_drpr.material,rawat_jl_drpr.bhp,rawat_jl_drpr.tarif_tindakandr,rawat_jl_drpr.tarif_tindakanpr,"+
                           "rawat_jl_drpr.kso,rawat_jl_drpr.menejemen,rawat_jl_drpr.biaya_rawat,penjab.kd_pj  "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_jl_drpr on rawat_jl_drpr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan on rawat_jl_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                           "inner join dokter on rawat_jl_drpr.kd_dokter=dokter.kd_dokter "+
                           "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join petugas on rawat_jl_drpr.nip=petugas.nip "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and poliklinik.nm_poli like ? "+
                           " order by rawat_jl_drpr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
                    ps=koneksi.prepareStatement(
                           "select rawat_jl_drpr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_jl_drpr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                           "rawat_jl_drpr.kd_dokter,dokter.nm_dokter,rawat_jl_drpr.nip,petugas.nama,rawat_jl_drpr.tgl_perawatan,"+
                           "rawat_jl_drpr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                           "rawat_jl_drpr.material,rawat_jl_drpr.bhp,rawat_jl_drpr.tarif_tindakandr,rawat_jl_drpr.tarif_tindakanpr,"+
                           "rawat_jl_drpr.kso,rawat_jl_drpr.menejemen,rawat_jl_drpr.biaya_rawat,penjab.kd_pj  "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_jl_drpr on rawat_jl_drpr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan on rawat_jl_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                           "inner join dokter on rawat_jl_drpr.kd_dokter=dokter.kd_dokter "+
                           "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join petugas on rawat_jl_drpr.nip=petugas.nip "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and poliklinik.nm_poli like ? "+
                           " order by rawat_jl_drpr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
                    ps=koneksi.prepareStatement(
                           "select rawat_jl_drpr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_jl_drpr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                           "rawat_jl_drpr.kd_dokter,dokter.nm_dokter,rawat_jl_drpr.nip,petugas.nama,rawat_jl_drpr.tgl_perawatan,"+
                           "rawat_jl_drpr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                           "rawat_jl_drpr.material,rawat_jl_drpr.bhp,rawat_jl_drpr.tarif_tindakandr,rawat_jl_drpr.tarif_tindakanpr,"+
                           "rawat_jl_drpr.kso,rawat_jl_drpr.menejemen,rawat_jl_drpr.biaya_rawat,penjab.kd_pj  "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_jl_drpr on rawat_jl_drpr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan on rawat_jl_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                           "inner join dokter on rawat_jl_drpr.kd_dokter=dokter.kd_dokter "+
                           "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join petugas on rawat_jl_drpr.nip=petugas.nip "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and poliklinik.nm_poli like ? "+
                           " order by rawat_jl_drpr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
                    ps=koneksi.prepareStatement(
                           "select rawat_jl_drpr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_jl_drpr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                           "rawat_jl_drpr.kd_dokter,dokter.nm_dokter,rawat_jl_drpr.nip,petugas.nama,rawat_jl_drpr.tgl_perawatan,"+
                           "rawat_jl_drpr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                           "rawat_jl_drpr.material,rawat_jl_drpr.bhp,rawat_jl_drpr.tarif_tindakandr,rawat_jl_drpr.tarif_tindakanpr,"+
                           "rawat_jl_drpr.kso,rawat_jl_drpr.menejemen,rawat_jl_drpr.biaya_rawat,penjab.kd_pj  "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_jl_drpr on rawat_jl_drpr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan on rawat_jl_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                           "inner join dokter on rawat_jl_drpr.kd_dokter=dokter.kd_dokter "+
                           "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join petugas on rawat_jl_drpr.nip=petugas.nip "+
                           "where reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.nip like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_drpr.tgl_perawatan,' ',rawat_jl_drpr.jam_rawat) between ? and ? and concat(rawat_jl_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like ? and concat(rawat_jl_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and poliklinik.nm_poli like ? "+
                           " order by rawat_jl_drpr.no_rawat desc");
                }
                    
            }
                
            try {
                if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPetugasRalanDokterParamedis.getText().equals("")&&NmPetugasRalanDokterParamedis.getText().equals("")&&KdPoliRalanDokter.getText().equals("")&&NmPoliRalanDokter.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                }else{
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(3,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(4,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(5,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(6,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(7,"%"+TCari.getText().trim()+"%");
                    ps.setString(8,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(9,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(10,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(11,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(12,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(13,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(14,"%"+TCari.getText().trim()+"%");
                    ps.setString(15,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(16,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(17,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(18,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(19,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(20,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(21,"%"+TCari.getText().trim()+"%");
                    ps.setString(22,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(23,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(24,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(25,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(26,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(27,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(28,"%"+TCari.getText().trim()+"%");
                    ps.setString(29,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(30,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(31,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(32,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(33,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(34,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(35,"%"+TCari.getText().trim()+"%");
                    ps.setString(36,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(37,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(38,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(39,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(40,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(41,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(42,"%"+TCari.getText().trim()+"%");
                    ps.setString(43,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(44,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(45,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(46,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(47,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(48,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(49,"%"+TCari.getText().trim()+"%");
                    ps.setString(50,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(51,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(52,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(53,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(54,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(55,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(56,"%"+TCari.getText().trim()+"%");
                    ps.setString(57,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(58,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(59,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(60,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(61,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(62,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(63,"%"+TCari.getText().trim()+"%");
                    ps.setString(64,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(65,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(66,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(67,"%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%");
                    ps.setString(68,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(69,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(70,"%"+TCari.getText().trim()+"%");
                }
                    
                rs=ps.executeQuery();
                i=1;
                material=0;bhp=0;jmdokter=0;jmpetugas=0;kso=0;menejemen=0;total=0;
                while(rs.next()){
                    material=material+rs.getDouble("material");
                    bhp=bhp+rs.getDouble("bhp");
                    jmdokter=jmdokter+rs.getDouble("tarif_tindakandr");
                    jmpetugas=jmpetugas+rs.getDouble("tarif_tindakanpr");
                    kso=kso+rs.getDouble("kso");
                    menejemen=menejemen+rs.getDouble("menejemen");
                    total=total+rs.getDouble("biaya_rawat");
                    if(rs.getString("kd_pj").equals("BPJ")){
                        nosep=Sequel.cariIsi("select bridging_sep.no_sep from bridging_sep where bridging_sep.no_rawat='"+rs.getString("no_rawat")+"'");
                    }else{
                        nosep=rs.getString("no_rawat");
                    }
                    if(rs.getString("nm_poli").equals("Unit IGD")){
                        ruang="UGD";
                    }else if(rs.getString("nm_poli").equals("IGD VK")){
                        ruang="IIDB";
                    }else{
                        ruang="Poli";
                    }
                    kodedokter=Sequel.cariIsi("select kd_dokter_remun from maping_dokter_dpjpremun where kd_dokter='"+rs.getString("kd_dokter")+"'");
                    if(rs.getString("nm_perawatan").trim().contains("usg")||rs.getString("nm_perawatan").trim().contains("konsultasi")||rs.getString("nm_perawatan").trim().contains("visite")||rs.getString("nm_perawatan").trim().contains("ekg")){
                        jenis="Tindakan Dokter";
                        kodepetugas=kodedokter;
                    }else{
                        jenis="Tindakan Petugas";
                        kodepetugas="0";
                    }
                    tabModeRalanDokter.addRow(new Object[]{
                        i,rs.getString("no_rkm_medis"),rs.getString("no_rawat"),nosep,rs.getString("nm_pasien"),"Tindakan Petugas",rs.getString("kd_jenis_prw"),
                        rs.getString("nm_perawatan"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("nip"),rs.getString("nama"),rs.getString("tgl_perawatan"),
                        rs.getString("jam_rawat"),rs.getString("png_jawab"),rs.getString("nm_poli"),rs.getDouble("material"),
                        rs.getDouble("bhp"),rs.getDouble("tarif_tindakandr"),rs.getDouble("kso"),rs.getDouble("menejemen"),
                        rs.getDouble("biaya_rawat"),rs.getString("nm_poli"),ruang,jenis,"1",rs.getDouble("biaya_rawat"),rs.getString("nm_perawatan"),kodedokter,kodepetugas
                    });
                    i++;
                }
                if(total>0){
                    tabModeRalanDokter.addRow(new Object[]{
                        "","","","","","","","","","","","","","","","Jumlah Total :",material,
                        bhp,jmdokter,kso,menejemen,total,"","","","","","","",""
                    });
                }
            } catch (Exception e) {
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
    
    public void tampiltindakanok(){     
//        Valid.tabelKosong(tabModeRalanDokter);
        try{
            if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPetugasRalanDokterParamedis.getText().equals("")&&NmPetugasRalanDokterParamedis.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                ps=koneksi.prepareStatement(
                       "select operasi.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                       "operasi.kode_paket,paket_operasi.nm_perawatan,operasi.tgl_operasi, "+
                       "penjab.png_jawab,if(operasi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                       "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                       "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=operasi.no_rawat limit 1 )) as ruangan,"+
                       "operator1.nm_dokter as operator1,operasi.biayaoperator1, "+
                       "operator2.nm_dokter as operator2,operasi.biayaoperator2, "+
                       "operator3.nm_dokter as operator3,operasi.biayaoperator3,"+
                       "asisten_operator1.nama as asisten_operator1,operasi.biayaasisten_operator1, "+
                       "asisten_operator2.nama as asisten_operator2,operasi.biayaasisten_operator2, "+
                       "asisten_operator3.nama as asisten_operator3,operasi.biayaasisten_operator3, "+
                       "instrumen.nama as instrumen,operasi.biayainstrumen, "+
                       "dokter_anak.nm_dokter as dokter_anak,operasi.biayadokter_anak, "+
                       "perawaat_resusitas.nama as perawaat_resusitas,operasi.biayaperawaat_resusitas, "+
                       "dokter_anestesi.nm_dokter as dokter_anestesi,operasi.biayadokter_anestesi, "+
                       "asisten_anestesi.nama as asisten_anestesi,operasi.biayaasisten_anestesi, "+
                       "(select nama from petugas where petugas.nip=operasi.asisten_anestesi2) as asisten_anestesi2,operasi.biayaasisten_anestesi2, "+
                       "bidan.nama as bidan,operasi.biayabidan, "+
                       "(select nama from petugas where petugas.nip=operasi.bidan2) as bidan2,operasi.biayabidan2, "+
                       "(select nama from petugas where petugas.nip=operasi.bidan3) as bidan3,operasi.biayabidan3, "+
                       "(select nama from petugas where petugas.nip=operasi.perawat_luar) as perawat_luar,operasi.biayaperawat_luar, "+
                       "(select nama from petugas where petugas.nip=operasi.omloop) as omloop,operasi.biaya_omloop, "+
                       "(select nama from petugas where petugas.nip=operasi.omloop2) as omloop2,operasi.biaya_omloop2, "+
                       "(select nama from petugas where petugas.nip=operasi.omloop3) as omloop3,operasi.biaya_omloop3, "+
                       "(select nama from petugas where petugas.nip=operasi.omloop4) as omloop4,operasi.biaya_omloop4, "+
                       "(select nama from petugas where petugas.nip=operasi.omloop5) as omloop5,operasi.biaya_omloop5, "+
                       "(select nm_dokter from dokter where dokter.kd_dokter=operasi.dokter_pjanak) as dokter_pjanak,operasi.biaya_dokter_pjanak, "+
                       "(select nm_dokter from dokter where dokter.kd_dokter=operasi.dokter_umum) as dokter_umum,operasi.biaya_dokter_umum, "+
                       "operasi.biayaalat,operasi.biayasewaok,operasi.akomodasi,operasi.bagian_rs,operasi.biayasarpras,penjab.kd_pj,operasi.operator1 as kdoperator1,operasi.asisten_operator1 as kdasisten_operator1  "+
                       "from operasi inner join reg_periksa on operasi.no_rawat=reg_periksa.no_rawat "+
                       "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                       "inner join paket_operasi on operasi.kode_paket=paket_operasi.kode_paket "+
                       "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                       "inner join dokter as operator1 on operator1.kd_dokter=operasi.operator1 "+
                       "inner join dokter as operator2 on operator2.kd_dokter=operasi.operator2 "+
                       "inner join dokter as operator3 on operator3.kd_dokter=operasi.operator3 "+
                       "inner join dokter as dokter_anak on dokter_anak.kd_dokter=operasi.dokter_anak "+
                       "inner join dokter as dokter_anestesi on dokter_anestesi.kd_dokter=operasi.dokter_anestesi "+
                       "inner join petugas as asisten_operator1 on asisten_operator1.nip=operasi.asisten_operator1 "+
                       "inner join petugas as asisten_operator2 on asisten_operator2.nip=operasi.asisten_operator2 "+
                       "inner join petugas as asisten_operator3 on asisten_operator3.nip=operasi.asisten_operator3 "+
                       "inner join petugas as asisten_anestesi on asisten_anestesi.nip=operasi.asisten_anestesi "+
                       "inner join petugas as bidan on bidan.nip=operasi.bidan "+
                       "inner join petugas as instrumen on instrumen.nip=operasi.instrumen "+
                       "inner join petugas as perawaat_resusitas on perawaat_resusitas.nip=operasi.perawaat_resusitas "+
                       "where operasi.tgl_operasi between ? and ? order by operasi.no_rawat desc");
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    ps=koneksi.prepareStatement(
                           "select operasi.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "operasi.kode_paket,paket_operasi.nm_perawatan,operasi.tgl_operasi, "+
                           "penjab.png_jawab,if(operasi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=operasi.no_rawat limit 1 )) as ruangan,"+
                           "operator1.nm_dokter as operator1,operasi.biayaoperator1, "+
                           "operator2.nm_dokter as operator2,operasi.biayaoperator2, "+
                           "operator3.nm_dokter as operator3,operasi.biayaoperator3,"+
                           "asisten_operator1.nama as asisten_operator1,operasi.biayaasisten_operator1, "+
                           "asisten_operator2.nama as asisten_operator2,operasi.biayaasisten_operator2, "+
                           "asisten_operator3.nama as asisten_operator3,operasi.biayaasisten_operator3, "+
                           "instrumen.nama as instrumen,operasi.biayainstrumen, "+
                           "dokter_anak.nm_dokter as dokter_anak,operasi.biayadokter_anak, "+
                           "perawaat_resusitas.nama as perawaat_resusitas,operasi.biayaperawaat_resusitas, "+
                           "dokter_anestesi.nm_dokter as dokter_anestesi,operasi.biayadokter_anestesi, "+
                           "asisten_anestesi.nama as asisten_anestesi,operasi.biayaasisten_anestesi, "+
                           "(select nama from petugas where petugas.nip=operasi.asisten_anestesi2) as asisten_anestesi2,operasi.biayaasisten_anestesi2, "+
                           "bidan.nama as bidan,operasi.biayabidan, "+
                           "(select nama from petugas where petugas.nip=operasi.bidan2) as bidan2,operasi.biayabidan2, "+
                           "(select nama from petugas where petugas.nip=operasi.bidan3) as bidan3,operasi.biayabidan3, "+
                           "(select nama from petugas where petugas.nip=operasi.perawat_luar) as perawat_luar,operasi.biayaperawat_luar, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop) as omloop,operasi.biaya_omloop, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop2) as omloop2,operasi.biaya_omloop2, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop3) as omloop3,operasi.biaya_omloop3, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop4) as omloop4,operasi.biaya_omloop4, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop5) as omloop5,operasi.biaya_omloop5, "+
                           "(select nm_dokter from dokter where dokter.kd_dokter=operasi.dokter_pjanak) as dokter_pjanak,operasi.biaya_dokter_pjanak, "+
                           "(select nm_dokter from dokter where dokter.kd_dokter=operasi.dokter_umum) as dokter_umum,operasi.biaya_dokter_umum, "+
                           "operasi.biayaalat,operasi.biayasewaok,operasi.akomodasi,operasi.bagian_rs,operasi.biayasarpras,penjab.kd_pj,operasi.operator1 as kdoperator1,operasi.asisten_operator1 as kdasisten_operator1  "+
                           "from operasi inner join reg_periksa on operasi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join paket_operasi on operasi.kode_paket=paket_operasi.kode_paket "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join dokter as operator1 on operator1.kd_dokter=operasi.operator1 "+
                           "inner join dokter as operator2 on operator2.kd_dokter=operasi.operator2 "+
                           "inner join dokter as operator3 on operator3.kd_dokter=operasi.operator3 "+
                           "inner join dokter as dokter_anak on dokter_anak.kd_dokter=operasi.dokter_anak "+
                           "inner join dokter as dokter_anestesi on dokter_anestesi.kd_dokter=operasi.dokter_anestesi "+
                           "inner join petugas as asisten_operator1 on asisten_operator1.nip=operasi.asisten_operator1 "+
                           "inner join petugas as asisten_operator2 on asisten_operator2.nip=operasi.asisten_operator2 "+
                           "inner join petugas as asisten_operator3 on asisten_operator3.nip=operasi.asisten_operator3 "+
                           "inner join petugas as asisten_anestesi on asisten_anestesi.nip=operasi.asisten_anestesi "+
                           "inner join petugas as bidan on bidan.nip=operasi.bidan "+
                           "inner join petugas as instrumen on instrumen.nip=operasi.instrumen "+
                           "inner join petugas as perawaat_resusitas on perawaat_resusitas.nip=operasi.perawaat_resusitas "+
                           "where operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.no_rawat like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.kode_paket like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and paket_operasi.nm_perawatan like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator1.nm_dokter like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator2.nm_dokter like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator3.nm_dokter like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter_anak.nm_dokter like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator1.nama like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator2.nama like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator3.nama like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and instrumen.nama like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perawaat_resusitas.nama like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_anestesi.nama like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and bidan.nama like ? or "+
                           "operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter_anestesi.nm_dokter like ? "+
                           "order by operasi.no_rawat desc");    
                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
                    ps=koneksi.prepareStatement(
                           "select operasi.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "operasi.kode_paket,paket_operasi.nm_perawatan,operasi.tgl_operasi, "+
                           "penjab.png_jawab,if(operasi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=operasi.no_rawat limit 1 )) as ruangan,"+
                           "operator1.nm_dokter as operator1,operasi.biayaoperator1, "+
                           "operator2.nm_dokter as operator2,operasi.biayaoperator2, "+
                           "operator3.nm_dokter as operator3,operasi.biayaoperator3,"+
                           "asisten_operator1.nama as asisten_operator1,operasi.biayaasisten_operator1, "+
                           "asisten_operator2.nama as asisten_operator2,operasi.biayaasisten_operator2, "+
                           "asisten_operator3.nama as asisten_operator3,operasi.biayaasisten_operator3, "+
                           "instrumen.nama as instrumen,operasi.biayainstrumen, "+
                           "dokter_anak.nm_dokter as dokter_anak,operasi.biayadokter_anak, "+
                           "perawaat_resusitas.nama as perawaat_resusitas,operasi.biayaperawaat_resusitas, "+
                           "dokter_anestesi.nm_dokter as dokter_anestesi,operasi.biayadokter_anestesi, "+
                           "asisten_anestesi.nama as asisten_anestesi,operasi.biayaasisten_anestesi, "+
                           "(select nama from petugas where petugas.nip=operasi.asisten_anestesi2) as asisten_anestesi2,operasi.biayaasisten_anestesi2, "+
                           "bidan.nama as bidan,operasi.biayabidan, "+
                           "(select nama from petugas where petugas.nip=operasi.bidan2) as bidan2,operasi.biayabidan2, "+
                           "(select nama from petugas where petugas.nip=operasi.bidan3) as bidan3,operasi.biayabidan3, "+
                           "(select nama from petugas where petugas.nip=operasi.perawat_luar) as perawat_luar,operasi.biayaperawat_luar, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop) as omloop,operasi.biaya_omloop, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop2) as omloop2,operasi.biaya_omloop2, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop3) as omloop3,operasi.biaya_omloop3, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop4) as omloop4,operasi.biaya_omloop4, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop5) as omloop5,operasi.biaya_omloop5, "+
                           "(select nm_dokter from dokter where dokter.kd_dokter=operasi.dokter_pjanak) as dokter_pjanak,operasi.biaya_dokter_pjanak, "+
                           "(select nm_dokter from dokter where dokter.kd_dokter=operasi.dokter_umum) as dokter_umum,operasi.biaya_dokter_umum, "+
                           "operasi.biayaalat,operasi.biayasewaok,operasi.akomodasi,operasi.bagian_rs,operasi.biayasarpras,penjab.kd_pj,operasi.operator1 as kdoperator1,operasi.asisten_operator1 as kdasisten_operator1 "+
                           "from operasi inner join reg_periksa on operasi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join paket_operasi on operasi.kode_paket=paket_operasi.kode_paket "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join dokter as operator1 on operator1.kd_dokter=operasi.operator1 "+
                           "inner join dokter as operator2 on operator2.kd_dokter=operasi.operator2 "+
                           "inner join dokter as operator3 on operator3.kd_dokter=operasi.operator3 "+
                           "inner join dokter as dokter_anak on dokter_anak.kd_dokter=operasi.dokter_anak "+
                           "inner join dokter as dokter_anestesi on dokter_anestesi.kd_dokter=operasi.dokter_anestesi "+
                           "inner join petugas as asisten_operator1 on asisten_operator1.nip=operasi.asisten_operator1 "+
                           "inner join petugas as asisten_operator2 on asisten_operator2.nip=operasi.asisten_operator2 "+
                           "inner join petugas as asisten_operator3 on asisten_operator3.nip=operasi.asisten_operator3 "+
                           "inner join petugas as asisten_anestesi on asisten_anestesi.nip=operasi.asisten_anestesi "+
                           "inner join petugas as bidan on bidan.nip=operasi.bidan "+
                           "inner join petugas as instrumen on instrumen.nip=operasi.instrumen "+
                           "inner join petugas as perawaat_resusitas on perawaat_resusitas.nip=operasi.perawaat_resusitas "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.kode_paket like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and paket_operasi.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator1.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator2.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator3.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter_anak.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator1.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator2.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator3.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and instrumen.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perawaat_resusitas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_anestesi.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and bidan.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter_anestesi.nm_dokter like ? "+
                           "order by operasi.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
                    ps=koneksi.prepareStatement(
                           "select operasi.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "operasi.kode_paket,paket_operasi.nm_perawatan,operasi.tgl_operasi, "+
                           "penjab.png_jawab,if(operasi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=operasi.no_rawat limit 1 )) as ruangan,"+
                           "operator1.nm_dokter as operator1,operasi.biayaoperator1, "+
                           "operator2.nm_dokter as operator2,operasi.biayaoperator2, "+
                           "operator3.nm_dokter as operator3,operasi.biayaoperator3,"+
                           "asisten_operator1.nama as asisten_operator1,operasi.biayaasisten_operator1, "+
                           "asisten_operator2.nama as asisten_operator2,operasi.biayaasisten_operator2, "+
                           "asisten_operator3.nama as asisten_operator3,operasi.biayaasisten_operator3, "+
                           "instrumen.nama as instrumen,operasi.biayainstrumen, "+
                           "dokter_anak.nm_dokter as dokter_anak,operasi.biayadokter_anak, "+
                           "perawaat_resusitas.nama as perawaat_resusitas,operasi.biayaperawaat_resusitas, "+
                           "dokter_anestesi.nm_dokter as dokter_anestesi,operasi.biayadokter_anestesi, "+
                           "asisten_anestesi.nama as asisten_anestesi,operasi.biayaasisten_anestesi, "+
                           "(select nama from petugas where petugas.nip=operasi.asisten_anestesi2) as asisten_anestesi2,operasi.biayaasisten_anestesi2, "+
                           "bidan.nama as bidan,operasi.biayabidan, "+
                           "(select nama from petugas where petugas.nip=operasi.bidan2) as bidan2,operasi.biayabidan2, "+
                           "(select nama from petugas where petugas.nip=operasi.bidan3) as bidan3,operasi.biayabidan3, "+
                           "(select nama from petugas where petugas.nip=operasi.perawat_luar) as perawat_luar,operasi.biayaperawat_luar, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop) as omloop,operasi.biaya_omloop, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop2) as omloop2,operasi.biaya_omloop2, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop3) as omloop3,operasi.biaya_omloop3, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop4) as omloop4,operasi.biaya_omloop4, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop5) as omloop5,operasi.biaya_omloop5, "+
                           "(select nm_dokter from dokter where dokter.kd_dokter=operasi.dokter_pjanak) as dokter_pjanak,operasi.biaya_dokter_pjanak, "+
                           "(select nm_dokter from dokter where dokter.kd_dokter=operasi.dokter_umum) as dokter_umum,operasi.biaya_dokter_umum, "+
                           "operasi.biayaalat,operasi.biayasewaok,operasi.akomodasi,operasi.bagian_rs,operasi.biayasarpras,penjab.kd_pj,operasi.operator1 as kdoperator1,operasi.asisten_operator1 as kdasisten_operator1  "+
                           "from operasi inner join reg_periksa on operasi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join paket_operasi on operasi.kode_paket=paket_operasi.kode_paket "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join dokter as operator1 on operator1.kd_dokter=operasi.operator1 "+
                           "inner join dokter as operator2 on operator2.kd_dokter=operasi.operator2 "+
                           "inner join dokter as operator3 on operator3.kd_dokter=operasi.operator3 "+
                           "inner join dokter as dokter_anak on dokter_anak.kd_dokter=operasi.dokter_anak "+
                           "inner join dokter as dokter_anestesi on dokter_anestesi.kd_dokter=operasi.dokter_anestesi "+
                           "inner join petugas as asisten_operator1 on asisten_operator1.nip=operasi.asisten_operator1 "+
                           "inner join petugas as asisten_operator2 on asisten_operator2.nip=operasi.asisten_operator2 "+
                           "inner join petugas as asisten_operator3 on asisten_operator3.nip=operasi.asisten_operator3 "+
                           "inner join petugas as asisten_anestesi on asisten_anestesi.nip=operasi.asisten_anestesi "+
                           "inner join petugas as bidan on bidan.nip=operasi.bidan "+
                           "inner join petugas as instrumen on instrumen.nip=operasi.instrumen "+
                           "inner join petugas as perawaat_resusitas on perawaat_resusitas.nip=operasi.perawaat_resusitas "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.kode_paket like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and paket_operasi.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator1.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator2.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator3.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter_anak.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator1.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator2.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator3.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and instrumen.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perawaat_resusitas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_anestesi.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and bidan.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter_anestesi.nm_dokter like ? "+
                           "order by operasi.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
                    ps=koneksi.prepareStatement(
                           "select operasi.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "operasi.kode_paket,paket_operasi.nm_perawatan,operasi.tgl_operasi, "+
                           "penjab.png_jawab,if(operasi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=operasi.no_rawat limit 1 )) as ruangan,"+
                           "operator1.nm_dokter as operator1,operasi.biayaoperator1, "+
                           "operator2.nm_dokter as operator2,operasi.biayaoperator2, "+
                           "operator3.nm_dokter as operator3,operasi.biayaoperator3,"+
                           "asisten_operator1.nama as asisten_operator1,operasi.biayaasisten_operator1, "+
                           "asisten_operator2.nama as asisten_operator2,operasi.biayaasisten_operator2, "+
                           "asisten_operator3.nama as asisten_operator3,operasi.biayaasisten_operator3, "+
                           "instrumen.nama as instrumen,operasi.biayainstrumen, "+
                           "dokter_anak.nm_dokter as dokter_anak,operasi.biayadokter_anak, "+
                           "perawaat_resusitas.nama as perawaat_resusitas,operasi.biayaperawaat_resusitas, "+
                           "dokter_anestesi.nm_dokter as dokter_anestesi,operasi.biayadokter_anestesi, "+
                           "asisten_anestesi.nama as asisten_anestesi,operasi.biayaasisten_anestesi, "+
                           "(select nama from petugas where petugas.nip=operasi.asisten_anestesi2) as asisten_anestesi2,operasi.biayaasisten_anestesi2, "+
                           "bidan.nama as bidan,operasi.biayabidan, "+
                           "(select nama from petugas where petugas.nip=operasi.bidan2) as bidan2,operasi.biayabidan2, "+
                           "(select nama from petugas where petugas.nip=operasi.bidan3) as bidan3,operasi.biayabidan3, "+
                           "(select nama from petugas where petugas.nip=operasi.perawat_luar) as perawat_luar,operasi.biayaperawat_luar, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop) as omloop,operasi.biaya_omloop, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop2) as omloop2,operasi.biaya_omloop2, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop3) as omloop3,operasi.biaya_omloop3, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop4) as omloop4,operasi.biaya_omloop4, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop5) as omloop5,operasi.biaya_omloop5, "+
                           "(select nm_dokter from dokter where dokter.kd_dokter=operasi.dokter_pjanak) as dokter_pjanak,operasi.biaya_dokter_pjanak, "+
                           "(select nm_dokter from dokter where dokter.kd_dokter=operasi.dokter_umum) as dokter_umum,operasi.biaya_dokter_umum, "+
                           "operasi.biayaalat,operasi.biayasewaok,operasi.akomodasi,operasi.bagian_rs,operasi.biayasarpras,penjab.kd_pj,operasi.operator1 as kdoperator1,operasi.asisten_operator1 as kdasisten_operator1  "+
                           "from operasi inner join reg_periksa on operasi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join paket_operasi on operasi.kode_paket=paket_operasi.kode_paket "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join dokter as operator1 on operator1.kd_dokter=operasi.operator1 "+
                           "inner join dokter as operator2 on operator2.kd_dokter=operasi.operator2 "+
                           "inner join dokter as operator3 on operator3.kd_dokter=operasi.operator3 "+
                           "inner join dokter as dokter_anak on dokter_anak.kd_dokter=operasi.dokter_anak "+
                           "inner join dokter as dokter_anestesi on dokter_anestesi.kd_dokter=operasi.dokter_anestesi "+
                           "inner join petugas as asisten_operator1 on asisten_operator1.nip=operasi.asisten_operator1 "+
                           "inner join petugas as asisten_operator2 on asisten_operator2.nip=operasi.asisten_operator2 "+
                           "inner join petugas as asisten_operator3 on asisten_operator3.nip=operasi.asisten_operator3 "+
                           "inner join petugas as asisten_anestesi on asisten_anestesi.nip=operasi.asisten_anestesi "+
                           "inner join petugas as bidan on bidan.nip=operasi.bidan "+
                           "inner join petugas as instrumen on instrumen.nip=operasi.instrumen "+
                           "inner join petugas as perawaat_resusitas on perawaat_resusitas.nip=operasi.perawaat_resusitas "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.kode_paket like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and paket_operasi.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator1.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator2.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator3.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter_anak.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator1.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator2.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator3.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and instrumen.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perawaat_resusitas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_anestesi.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and bidan.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter_anestesi.nm_dokter like ? "+
                           "order by operasi.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
                    ps=koneksi.prepareStatement(
                           "select operasi.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "operasi.kode_paket,paket_operasi.nm_perawatan,operasi.tgl_operasi, "+
                           "penjab.png_jawab,if(operasi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=operasi.no_rawat limit 1 )) as ruangan,"+
                           "operator1.nm_dokter as operator1,operasi.biayaoperator1, "+
                           "operator2.nm_dokter as operator2,operasi.biayaoperator2, "+
                           "operator3.nm_dokter as operator3,operasi.biayaoperator3,"+
                           "asisten_operator1.nama as asisten_operator1,operasi.biayaasisten_operator1, "+
                           "asisten_operator2.nama as asisten_operator2,operasi.biayaasisten_operator2, "+
                           "asisten_operator3.nama as asisten_operator3,operasi.biayaasisten_operator3, "+
                           "instrumen.nama as instrumen,operasi.biayainstrumen, "+
                           "dokter_anak.nm_dokter as dokter_anak,operasi.biayadokter_anak, "+
                           "perawaat_resusitas.nama as perawaat_resusitas,operasi.biayaperawaat_resusitas, "+
                           "dokter_anestesi.nm_dokter as dokter_anestesi,operasi.biayadokter_anestesi, "+
                           "asisten_anestesi.nama as asisten_anestesi,operasi.biayaasisten_anestesi, "+
                           "(select nama from petugas where petugas.nip=operasi.asisten_anestesi2) as asisten_anestesi2,operasi.biayaasisten_anestesi2, "+
                           "bidan.nama as bidan,operasi.biayabidan, "+
                           "(select nama from petugas where petugas.nip=operasi.bidan2) as bidan2,operasi.biayabidan2, "+
                           "(select nama from petugas where petugas.nip=operasi.bidan3) as bidan3,operasi.biayabidan3, "+
                           "(select nama from petugas where petugas.nip=operasi.perawat_luar) as perawat_luar,operasi.biayaperawat_luar, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop) as omloop,operasi.biaya_omloop, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop2) as omloop2,operasi.biaya_omloop2, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop3) as omloop3,operasi.biaya_omloop3, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop4) as omloop4,operasi.biaya_omloop4, "+
                           "(select nama from petugas where petugas.nip=operasi.omloop5) as omloop5,operasi.biaya_omloop5, "+
                           "(select nm_dokter from dokter where dokter.kd_dokter=operasi.dokter_pjanak) as dokter_pjanak,operasi.biaya_dokter_pjanak, "+
                           "(select nm_dokter from dokter where dokter.kd_dokter=operasi.dokter_umum) as dokter_umum,operasi.biaya_dokter_umum, "+
                           "operasi.biayaalat,operasi.biayasewaok,operasi.akomodasi,operasi.bagian_rs,operasi.biayasarpras,penjab.kd_pj,operasi.operator1 as kdoperator1,operasi.asisten_operator1 as kdasisten_operator1 "+
                           "from operasi inner join reg_periksa on operasi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join paket_operasi on operasi.kode_paket=paket_operasi.kode_paket "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join dokter as operator1 on operator1.kd_dokter=operasi.operator1 "+
                           "inner join dokter as operator2 on operator2.kd_dokter=operasi.operator2 "+
                           "inner join dokter as operator3 on operator3.kd_dokter=operasi.operator3 "+
                           "inner join dokter as dokter_anak on dokter_anak.kd_dokter=operasi.dokter_anak "+
                           "inner join dokter as dokter_anestesi on dokter_anestesi.kd_dokter=operasi.dokter_anestesi "+
                           "inner join petugas as asisten_operator1 on asisten_operator1.nip=operasi.asisten_operator1 "+
                           "inner join petugas as asisten_operator2 on asisten_operator2.nip=operasi.asisten_operator2 "+
                           "inner join petugas as asisten_operator3 on asisten_operator3.nip=operasi.asisten_operator3 "+
                           "inner join petugas as asisten_anestesi on asisten_anestesi.nip=operasi.asisten_anestesi "+
                           "inner join petugas as bidan on bidan.nip=operasi.bidan "+
                           "inner join petugas as instrumen on instrumen.nip=operasi.instrumen "+
                           "inner join petugas as perawaat_resusitas on perawaat_resusitas.nip=operasi.perawaat_resusitas "+
                           "where reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.kode_paket like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and paket_operasi.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator1.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator2.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operator3.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter_anak.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator1.nama like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator2.nama like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_operator3.nama like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and instrumen.nama like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perawaat_resusitas.nama like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and asisten_anestesi.nama like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and bidan.nama like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and operasi.tgl_operasi between ? and ? and concat(operasi.operator1,operator1.nm_dokter) like ? and concat(operasi.asisten_operator1,asisten_operator1.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter_anestesi.nm_dokter like ? "+
                           "order by operasi.no_rawat desc");
                }   
            }
                
            try {
                if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPetugasRalanDokterParamedis.getText().equals("")&&NmPetugasRalanDokterParamedis.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                }else{
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(3,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(4,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(5,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(8,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(9,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(10,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(11,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(12,"%"+TCari.getText().trim()+"%");
                    ps.setString(13,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(14,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(15,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(16,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(17,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(18,"%"+TCari.getText().trim()+"%");
                    ps.setString(19,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(20,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(21,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(22,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(23,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(24,"%"+TCari.getText().trim()+"%");
                    ps.setString(25,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(26,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(27,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(28,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(29,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(30,"%"+TCari.getText().trim()+"%");
                    ps.setString(31,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(32,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(33,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(34,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(35,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(36,"%"+TCari.getText().trim()+"%");
                    ps.setString(37,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(38,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(39,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(40,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(41,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(42,"%"+TCari.getText().trim()+"%");
                    ps.setString(43,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(44,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(45,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(46,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(47,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(48,"%"+TCari.getText().trim()+"%");
                    ps.setString(49,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(50,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(51,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(52,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(53,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(54,"%"+TCari.getText().trim()+"%");
                    ps.setString(55,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(56,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(57,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(58,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(59,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(60,"%"+TCari.getText().trim()+"%");
                    ps.setString(61,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(62,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(63,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(64,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(65,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(66,"%"+TCari.getText().trim()+"%");
                    ps.setString(67,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(68,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(69,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(70,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(71,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(72,"%"+TCari.getText().trim()+"%");
                    ps.setString(73,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(74,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(75,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(76,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(77,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(78,"%"+TCari.getText().trim()+"%");
                    ps.setString(79,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(80,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(81,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(82,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(83,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(84,"%"+TCari.getText().trim()+"%");
                    ps.setString(85,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(86,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(87,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(88,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(89,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(90,"%"+TCari.getText().trim()+"%");
                    ps.setString(91,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(92,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(93,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(94,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(95,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(96,"%"+TCari.getText().trim()+"%");
                    ps.setString(97,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(98,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(99,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(100,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(101,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(102,"%"+TCari.getText().trim()+"%");
                    ps.setString(103,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(104,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(105,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(106,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(107,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(108,"%"+TCari.getText().trim()+"%");
                }
                    
                rs=ps.executeQuery();
                i=1;
                total=0;biayaoperator1=0;biayaoperator2=0; 
                biayaoperator3=0;biayaasisten_operator1=0;biayaasisten_operator2=0;
                biayaasisten_operator3=0;biayainstrumen=0;biayadokter_anak=0;
                biayaperawaat_resusitas=0;biayadokter_anestesi=0;biayaasisten_anestesi=0;
                biayaasisten_anestesi2=0;biayabidan=0;biayabidan2=0;biayabidan3=0;
                biayaperawat_luar=0;biayaalat=0;biayasewaok=0;akomodasi=0;
                bagian_rs=0;biaya_omloop=0;biaya_omloop2=0;biaya_omloop3=0;
                biaya_omloop4=0;biaya_omloop5=0;biayasarpras=0;biaya_dokter_pjanak=0;
                biaya_dokter_umum=0;
                while(rs.next()){  
                    biayaoperator1=biayaoperator1+rs.getDouble("biayaoperator1");
                    biayaoperator2=biayaoperator2+rs.getDouble("biayaoperator2");
                    biayaoperator3=biayaoperator3+rs.getDouble("biayaoperator3");
                    biayaasisten_operator1=biayaasisten_operator1+rs.getDouble("biayaasisten_operator1");
                    biayaasisten_operator2=biayaasisten_operator2+rs.getDouble("biayaasisten_operator2");
                    biayaasisten_operator3=biayaasisten_operator3+rs.getDouble("biayaasisten_operator3");
                    biayainstrumen=biayainstrumen+rs.getDouble("biayainstrumen");
                    biayadokter_anak=biayadokter_anak+rs.getDouble("biayadokter_anak");
                    biayaperawaat_resusitas=biayaperawaat_resusitas+rs.getDouble("biayaperawaat_resusitas");
                    biayadokter_anestesi=biayadokter_anestesi+rs.getDouble("biayadokter_anestesi");
                    biayaasisten_anestesi=biayaasisten_anestesi+rs.getDouble("biayaasisten_anestesi");
                    biayaasisten_anestesi2=biayaasisten_anestesi2+rs.getDouble("biayaasisten_anestesi2");
                    biayabidan=biayabidan+rs.getDouble("biayabidan");
                    biayabidan2=biayabidan2+rs.getDouble("biayabidan2");
                    biayabidan3=biayabidan3+rs.getDouble("biayabidan3");
                    biayaperawat_luar=biayaperawat_luar+rs.getDouble("biayaperawat_luar");
                    biaya_omloop=biaya_omloop+rs.getDouble("biaya_omloop");
                    biaya_omloop2=biaya_omloop2+rs.getDouble("biaya_omloop2");
                    biaya_omloop3=biaya_omloop3+rs.getDouble("biaya_omloop3");
                    biaya_omloop4=biaya_omloop4+rs.getDouble("biaya_omloop4");
                    biaya_omloop5=biaya_omloop5+rs.getDouble("biaya_omloop5");
                    biaya_dokter_pjanak=biaya_dokter_pjanak+rs.getDouble("biaya_dokter_pjanak");
                    biaya_dokter_umum=biaya_dokter_umum+rs.getDouble("biaya_dokter_umum");
                    biayaalat=biayaalat+rs.getDouble("biayaalat");
                    biayasewaok=biayasewaok+rs.getDouble("biayasewaok");
                    akomodasi=akomodasi+rs.getDouble("akomodasi");
                    bagian_rs=bagian_rs+rs.getDouble("bagian_rs");
                    biayasarpras=biayasarpras+rs.getDouble("biayasarpras");
                    total=total+rs.getDouble("biayaoperator1")+rs.getDouble("biayaoperator2")+rs.getDouble("biayaoperator3")+
                          rs.getDouble("biayaasisten_operator1")+rs.getDouble("biayaasisten_operator2")+rs.getDouble("biayaasisten_operator3")+
                          rs.getDouble("biayainstrumen")+rs.getDouble("biayadokter_anak")+rs.getDouble("biayaperawaat_resusitas")+
                          rs.getDouble("biayadokter_anestesi")+rs.getDouble("biayaasisten_anestesi")+rs.getDouble("biayaasisten_anestesi2")+
                          rs.getDouble("biayabidan")+rs.getDouble("biayabidan2")+rs.getDouble("biayabidan3")+
                          rs.getDouble("biayaperawat_luar")+rs.getDouble("biaya_omloop")+rs.getDouble("biaya_omloop2")+
                          rs.getDouble("biaya_omloop3")+rs.getDouble("biaya_omloop4")+rs.getDouble("biaya_omloop5")+
                          rs.getDouble("biaya_dokter_pjanak")+rs.getDouble("biaya_dokter_umum")+rs.getDouble("biayaalat")+
                          rs.getDouble("biayasewaok")+rs.getDouble("akomodasi")+rs.getDouble("bagian_rs")+rs.getDouble("biayasarpras");
                    if(rs.getString("kd_pj").equals("BPJ")){
                        nosep=Sequel.cariIsi("select bridging_sep.no_sep from bridging_sep where bridging_sep.no_rawat='"+rs.getString("no_rawat")+"'");
                    }else{
                        nosep=rs.getString("no_rawat");
                    }
                    if(rs.getString("dokter_anestesi").equals("-")&&rs.getString("dokter_anak").equals("-")){
                        jenistindakanok="Bedah Lokal";
                    }else if(!rs.getString("dokter_anestesi").equals("-")&&rs.getString("dokter_anak").equals("-")){
                        jenistindakanok="Tindakan Bedah";
                    }else if(!rs.getString("dokter_anestesi").equals("-")&&!rs.getString("dokter_anak").equals("-")){
                        jenistindakanok="Tindakan Pendamping";
                    }
                    tabModeRalanDokter.addRow(new Object[]{
                        
                        i,rs.getString("no_rkm_medis"),rs.getString("no_rawat"),nosep,rs.getString("nm_pasien"),jenistindakanok,rs.getString("kode_paket"),
                        rs.getString("nm_perawatan"),rs.getString("kdoperator1"),rs.getString("operator1"),rs.getString("kdasisten_operator1"),rs.getString("asisten_operator1"),rs.getString("tgl_operasi"),
                        rs.getString("tgl_operasi"),rs.getString("png_jawab"),"IBS",0,
                        0,0,0,0,
                        (rs.getDouble("biayaoperator1")+rs.getDouble("biayaoperator2")+rs.getDouble("biayaoperator3")+
                        rs.getDouble("biayaasisten_operator1")+rs.getDouble("biayaasisten_operator2")+rs.getDouble("biayaasisten_operator3")+
                        rs.getDouble("biayainstrumen")+rs.getDouble("biayadokter_anak")+rs.getDouble("biayaperawaat_resusitas")+
                        rs.getDouble("biayadokter_anestesi")+rs.getDouble("biayaasisten_anestesi")+rs.getDouble("biayaasisten_anestesi2")+
                        rs.getDouble("biayabidan")+rs.getDouble("biayabidan2")+rs.getDouble("biayabidan3")+
                        rs.getDouble("biayaperawat_luar")+rs.getDouble("biaya_omloop")+rs.getDouble("biaya_omloop2")+
                        rs.getDouble("biaya_omloop3")+rs.getDouble("biaya_omloop4")+rs.getDouble("biaya_omloop5")+
                        rs.getDouble("biaya_dokter_pjanak")+rs.getDouble("biaya_dokter_umum")+rs.getDouble("biayaalat")+
                        rs.getDouble("biayasewaok")+rs.getDouble("akomodasi")+rs.getDouble("bagian_rs")+rs.getDouble("biayasarpras")),
                        rs.getString("ruangan"),"IBS",jenistindakanok,"1",total,rs.getString("nm_perawatan"),kodedokter,kodedokter
                    });
                    i++;
                }
                if(total>0){
                    tabModeRalanDokter.addRow(new Object[]{
                        "","","","","","","","","","","","","","","","Jumlah Total :",0,
                        0,0,0,0,total,"","","","","","","",""
                    });
                }
            } catch (Exception e) {
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
    
    public void tampildokterranap(){     
//        Valid.tabelKosong(tabModeRalanDokter);
        try{
            if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                ps=koneksi.prepareStatement(
                       "select rawat_inap_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                       "pasien.nm_pasien,rawat_inap_dr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
                       "rawat_inap_dr.kd_dokter,dokter.nm_dokter,rawat_inap_dr.tgl_perawatan,"+
                       "rawat_inap_dr.jam_rawat,penjab.png_jawab,"+
                       "ifnull((select bangsal.nm_bangsal from kamar_inap "+
                       "inner join kamar inner join bangsal on "+
                       "kamar_inap.kd_kamar=kamar.kd_kamar "+
                       "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_dr.no_rawat limit 1),'Ruang Terhapus' ) as ruang , " +
                       "rawat_inap_dr.material,rawat_inap_dr.bhp,rawat_inap_dr.tarif_tindakandr,"+
                       "rawat_inap_dr.kso,rawat_inap_dr.menejemen,rawat_inap_dr.biaya_rawat,penjab.kd_pj "+
                       "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                       "inner join rawat_inap_dr on rawat_inap_dr.no_rawat=reg_periksa.no_rawat "+
                       "inner join jns_perawatan_inap on rawat_inap_dr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
                       "inner join dokter on rawat_inap_dr.kd_dokter=dokter.kd_dokter "+
                       "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                       "where concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? order by rawat_inap_dr.no_rawat desc");
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    ps=koneksi.prepareStatement(
                           "select rawat_inap_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_inap_dr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
                           "rawat_inap_dr.kd_dokter,dokter.nm_dokter,rawat_inap_dr.tgl_perawatan,"+
                           "rawat_inap_dr.jam_rawat,penjab.png_jawab,"+
                           "ifnull((select bangsal.nm_bangsal from kamar_inap "+
                           "inner join kamar inner join bangsal on "+
                           "kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_dr.no_rawat limit 1),'Ruang Terhapus' ) as ruang , " +
                           "rawat_inap_dr.material,rawat_inap_dr.bhp,rawat_inap_dr.tarif_tindakandr,"+
                           "rawat_inap_dr.kso,rawat_inap_dr.menejemen,rawat_inap_dr.biaya_rawat,penjab.kd_pj "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_inap_dr on rawat_inap_dr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan_inap on rawat_inap_dr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
                           "inner join dokter on rawat_inap_dr.kd_dokter=dokter.kd_dokter "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "where concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_dr.no_rawat like ? or "+
                           "concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
                           "concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_dr.kd_dokter like ? or "+
                           "concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by rawat_inap_dr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
                    ps=koneksi.prepareStatement(
                           "select rawat_inap_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_inap_dr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
                           "rawat_inap_dr.kd_dokter,dokter.nm_dokter,rawat_inap_dr.tgl_perawatan,"+
                           "rawat_inap_dr.jam_rawat,penjab.png_jawab,"+
                           "ifnull((select bangsal.nm_bangsal from kamar_inap "+
                           "inner join kamar inner join bangsal on "+
                           "kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_dr.no_rawat limit 1),'Ruang Terhapus' ) as ruang , " +
                           "rawat_inap_dr.material,rawat_inap_dr.bhp,rawat_inap_dr.tarif_tindakandr,"+
                           "rawat_inap_dr.kso,rawat_inap_dr.menejemen,rawat_inap_dr.biaya_rawat,penjab.kd_pj "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_inap_dr on rawat_inap_dr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan_inap on rawat_inap_dr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
                           "inner join dokter on rawat_inap_dr.kd_dokter=dokter.kd_dokter "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_dr.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_dr.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by rawat_inap_dr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
                    ps=koneksi.prepareStatement(
                           "select rawat_inap_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_inap_dr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
                           "rawat_inap_dr.kd_dokter,dokter.nm_dokter,rawat_inap_dr.tgl_perawatan,"+
                           "rawat_inap_dr.jam_rawat,penjab.png_jawab,"+
                           "ifnull((select bangsal.nm_bangsal from kamar_inap "+
                           "inner join kamar inner join bangsal on "+
                           "kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_dr.no_rawat limit 1),'Ruang Terhapus' ) as ruang , " +
                           "rawat_inap_dr.material,rawat_inap_dr.bhp,rawat_inap_dr.tarif_tindakandr,"+
                           "rawat_inap_dr.kso,rawat_inap_dr.menejemen,rawat_inap_dr.biaya_rawat,penjab.kd_pj "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_inap_dr on rawat_inap_dr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan_inap on rawat_inap_dr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
                           "inner join dokter on rawat_inap_dr.kd_dokter=dokter.kd_dokter "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_dr.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_dr.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by rawat_inap_dr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
                    ps=koneksi.prepareStatement(
                           "select rawat_inap_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_inap_dr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
                           "rawat_inap_dr.kd_dokter,dokter.nm_dokter,rawat_inap_dr.tgl_perawatan,"+
                           "rawat_inap_dr.jam_rawat,penjab.png_jawab,"+
                           "ifnull((select bangsal.nm_bangsal from kamar_inap "+
                           "inner join kamar inner join bangsal on "+
                           "kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_dr.no_rawat limit 1),'Ruang Terhapus' ) as ruang , " +
                           "rawat_inap_dr.material,rawat_inap_dr.bhp,rawat_inap_dr.tarif_tindakandr,"+
                           "rawat_inap_dr.kso,rawat_inap_dr.menejemen,rawat_inap_dr.biaya_rawat,penjab.kd_pj "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_inap_dr on rawat_inap_dr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan_inap on rawat_inap_dr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
                           "inner join dokter on rawat_inap_dr.kd_dokter=dokter.kd_dokter "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_dr.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_dr.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by rawat_inap_dr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
                    ps=koneksi.prepareStatement(
                           "select rawat_inap_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_inap_dr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
                           "rawat_inap_dr.kd_dokter,dokter.nm_dokter,rawat_inap_dr.tgl_perawatan,"+
                           "rawat_inap_dr.jam_rawat,penjab.png_jawab,"+
                           "ifnull((select bangsal.nm_bangsal from kamar_inap "+
                           "inner join kamar inner join bangsal on "+
                           "kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_dr.no_rawat limit 1),'Ruang Terhapus' ) as ruang , " +
                           "rawat_inap_dr.material,rawat_inap_dr.bhp,rawat_inap_dr.tarif_tindakandr,"+
                           "rawat_inap_dr.kso,rawat_inap_dr.menejemen,rawat_inap_dr.biaya_rawat,penjab.kd_pj "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_inap_dr on rawat_inap_dr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan_inap on rawat_inap_dr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
                           "inner join dokter on rawat_inap_dr.kd_dokter=dokter.kd_dokter "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "where reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_dr.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_dr.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_dr.tgl_perawatan,' ',rawat_inap_dr.jam_rawat) between ? and ? and concat(rawat_inap_dr.kd_dokter,dokter.nm_dokter) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by rawat_inap_dr.no_rawat desc");
                }
            }
                
            try {
                if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                }else{
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(3,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(4,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(5,"%"+TCari.getText().trim()+"%");
                    ps.setString(6,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(7,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(8,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(9,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(10,"%"+TCari.getText().trim()+"%");
                    ps.setString(11,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(12,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(13,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(14,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(15,"%"+TCari.getText().trim()+"%");
                    ps.setString(16,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(17,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(18,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(19,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(20,"%"+TCari.getText().trim()+"%");
                    ps.setString(21,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(22,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(23,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(24,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(25,"%"+TCari.getText().trim()+"%");
                    ps.setString(26,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(27,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(28,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(29,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(30,"%"+TCari.getText().trim()+"%");
                    ps.setString(31,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(32,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(33,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(34,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(35,"%"+TCari.getText().trim()+"%");
                }
                    
                rs=ps.executeQuery();
                i=1;
                material=0;bhp=0;jmdokter=0;kso=0;menejemen=0;total=0;
                while(rs.next()){
                    material=material+rs.getDouble("material");
                    bhp=bhp+rs.getDouble("bhp");
                    jmdokter=jmdokter+rs.getDouble("tarif_tindakandr");
                    kso=kso+rs.getDouble("kso");
                    menejemen=menejemen+rs.getDouble("menejemen");
                    total=total+rs.getDouble("biaya_rawat");
                    if(rs.getString("kd_pj").equals("BPJ")){
                        nosep=Sequel.cariIsi("select bridging_sep.no_sep from bridging_sep where bridging_sep.no_rawat='"+rs.getString("no_rawat")+"'");
                    }else{
                        nosep=rs.getString("no_rawat");
                    }
                    if(Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'").equals("")){
                        rawatgabung=rs.getString("no_rawat");
                    }else{
                        rawatgabung=Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'");
                    }
                    
                    if(rs.getString("ruang").equals("Unit IGD")){
                        ruang="UGD";
                    }else if(rs.getString("ruang").equals("IGD VK")){
                        ruang="IIDB";
                    }else if(rs.getString("ruang").trim().contains("Kembar")){
                        ruang="IRNA II";
                    }else if(rs.getString("ruang").trim().contains("Duyung")){
                        ruang="IIDB";
                    }else if(rs.getString("ruang").trim().contains("isolasi")){
                        ruang="ISOLASI";
                    }else if(rs.getString("ruang").trim().contains("Anggrek")){
                        ruang="ISOLASI";
                    }else if(rs.getString("ruang").trim().contains("Dahlia")){
                        ruang="ISOLASI";
                    }else if(rs.getString("ruang").trim().contains("Melati")){
                        ruang="ISOLASI";
                    }else if(rs.getString("ruang").trim().contains("Beleq")||rs.getString("ruang").trim().contains("Wangi")){
                        ruang="IRNA III";
                    }else if(rs.getString("ruang").equals("ICU")){
                        ruang="ICU";
                    }else if(rs.getString("ruang").equals("NICU")){
                        ruang="NICU";
                    }else if(rs.getString("ruang").equals("HCU")){
                        ruang="ICU";
                    }else{
                        ruang="Poli";
                    }
                    kodedokter=Sequel.cariIsi("select kd_dokter_remun from maping_dokter_dpjpremun where kd_dokter='"+rs.getString("kd_dokter")+"'");
                    if(rs.getString("nm_perawatan").trim().contains("usg")||rs.getString("nm_perawatan").trim().contains("Ultrasonografi")||rs.getString("nm_perawatan").trim().contains("Konsultasi Dokter")||
                            rs.getString("nm_perawatan").trim().contains("Konsultasi Spesialis")||rs.getString("nm_perawatan").trim().contains("konsultasi")||rs.getString("nm_perawatan").trim().contains("visite")||rs.getString("nm_perawatan").trim().contains("ekg")){
                        jenis="Tindakan Dokter";
                        kodepetugas=kodedokter;
                    }else{
                        jenis="Tindakan Petugas";
                        kodepetugas="0";
                    }
                    kodedokter=Sequel.cariIsi("select kd_dokter_remun from maping_dokter_dpjpremun where kd_dokter='"+rs.getString("kd_dokter")+"'");
//                    if(rs.getString("nm_perawatan").trim().contains("usg")||rs.getString("nm_perawatan").trim().contains("konsultasi")||rs.getString("nm_perawatan").trim().contains("visite")||rs.getString("nm_perawatan").trim().contains("ekg")){
//                        jenis="Tindakan Dokter";
//                        kodepetugas=kodedokter;
//                    }else{
//                        jenis="Tindakan Petugas";
//                        kodepetugas="0";
//                    }
                    tabModeRalanDokter.addRow(new Object[]{
                        i,rs.getString("no_rkm_medis"),rawatgabung,nosep,rs.getString("nm_pasien"),"Tindakan Dokter",rs.getString("kd_jenis_prw"),
                        rs.getString("nm_perawatan"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("tgl_perawatan"),
                        rs.getString("jam_rawat"),rs.getString("png_jawab"),rs.getString("ruang"),rs.getDouble("material"),
                        rs.getDouble("bhp"),rs.getDouble("tarif_tindakandr"),rs.getDouble("kso"),rs.getDouble("menejemen"),
                        rs.getDouble("biaya_rawat"),rs.getString("ruang"),ruang,"Tindakan Dokter","1",rs.getDouble("biaya_rawat"),rs.getString("nm_perawatan"),kodedokter,kodedokter
                    });
                    i++;
                }
                if(total>0){
                    tabModeRalanDokter.addRow(new Object[]{
                        "","","","","","","","","","","","","","","","Jumlah Total :",material,
                        bhp,jmdokter,kso,menejemen,total,"","","","","","","",""
                    });
                }
            } catch (Exception e) {
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
//    
//    public void tampil6(){     
//        Valid.tabelKosong(tabModeRanapParamedis);
//        try{
//            if(KdPetugasRanapParamedis.getText().equals("")&&NmPetugasRanapParamedis.getText().equals("")&&KdCaraBayarRanapParamedis.getText().equals("")&&NmCaraBayarRanapParamedis.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
//                ps=koneksi.prepareStatement(
//                       "select rawat_inap_pr.no_rawat,reg_periksa.no_rkm_medis,"+
//                       "pasien.nm_pasien,rawat_inap_pr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
//                       "rawat_inap_pr.nip,petugas.nama,rawat_inap_pr.tgl_perawatan,"+
//                       "rawat_inap_pr.jam_rawat,penjab.png_jawab,"+
//                       "ifnull((select bangsal.nm_bangsal from kamar_inap "+
//                       "inner join kamar inner join bangsal on "+
//                       "kamar_inap.kd_kamar=kamar.kd_kamar "+
//                       "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_pr.no_rawat limit 1 ),'Ruang Terhapus' ) as ruang , " +
//                       "rawat_inap_pr.material,rawat_inap_pr.bhp,rawat_inap_pr.tarif_tindakanpr,"+
//                       "rawat_inap_pr.kso,rawat_inap_pr.menejemen,rawat_inap_pr.biaya_rawat "+
//                       "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
//                       "inner join rawat_inap_pr on rawat_inap_pr.no_rawat=reg_periksa.no_rawat "+
//                       "inner join jns_perawatan_inap on rawat_inap_pr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
//                       "inner join petugas on rawat_inap_pr.nip=petugas.nip "+
//                       "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
//                       "where concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? order by rawat_inap_pr.no_rawat desc");
//            }else{
//                if(cmbStatus.getSelectedItem().equals("Semua")){
//                    ps=koneksi.prepareStatement(
//                           "select rawat_inap_pr.no_rawat,reg_periksa.no_rkm_medis,"+
//                           "pasien.nm_pasien,rawat_inap_pr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
//                           "rawat_inap_pr.nip,petugas.nama,rawat_inap_pr.tgl_perawatan,"+
//                           "rawat_inap_pr.jam_rawat,penjab.png_jawab,"+
//                           "ifnull((select bangsal.nm_bangsal from kamar_inap "+
//                           "inner join kamar inner join bangsal on "+
//                           "kamar_inap.kd_kamar=kamar.kd_kamar "+
//                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_pr.no_rawat limit 1 ),'Ruang Terhapus' ) as ruang , " +
//                           "rawat_inap_pr.material,rawat_inap_pr.bhp,rawat_inap_pr.tarif_tindakanpr,"+
//                           "rawat_inap_pr.kso,rawat_inap_pr.menejemen,rawat_inap_pr.biaya_rawat "+
//                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
//                           "inner join rawat_inap_pr on rawat_inap_pr.no_rawat=reg_periksa.no_rawat "+
//                           "inner join jns_perawatan_inap on rawat_inap_pr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
//                           "inner join petugas on rawat_inap_pr.nip=petugas.nip "+
//                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
//                           "where concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_pr.no_rawat like ? or "+
//                           "concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
//                           "concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
//                           "concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
//                           "concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_pr.nip like ? or "+
//                           "concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
//                           "concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
//                           " order by rawat_inap_pr.no_rawat desc");
//                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
//                    ps=koneksi.prepareStatement(
//                           "select rawat_inap_pr.no_rawat,reg_periksa.no_rkm_medis,"+
//                           "pasien.nm_pasien,rawat_inap_pr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
//                           "rawat_inap_pr.nip,petugas.nama,rawat_inap_pr.tgl_perawatan,"+
//                           "rawat_inap_pr.jam_rawat,penjab.png_jawab,"+
//                           "ifnull((select bangsal.nm_bangsal from kamar_inap "+
//                           "inner join kamar inner join bangsal on "+
//                           "kamar_inap.kd_kamar=kamar.kd_kamar "+
//                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_pr.no_rawat limit 1 ),'Ruang Terhapus' ) as ruang , " +
//                           "rawat_inap_pr.material,rawat_inap_pr.bhp,rawat_inap_pr.tarif_tindakanpr,"+
//                           "rawat_inap_pr.kso,rawat_inap_pr.menejemen,rawat_inap_pr.biaya_rawat "+
//                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
//                           "inner join rawat_inap_pr on rawat_inap_pr.no_rawat=reg_periksa.no_rawat "+
//                           "inner join jns_perawatan_inap on rawat_inap_pr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
//                           "inner join petugas on rawat_inap_pr.nip=petugas.nip "+
//                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
//                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
//                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_pr.no_rawat like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_pr.nip like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
//                           " order by rawat_inap_pr.no_rawat desc");
//                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
//                    ps=koneksi.prepareStatement(
//                           "select rawat_inap_pr.no_rawat,reg_periksa.no_rkm_medis,"+
//                           "pasien.nm_pasien,rawat_inap_pr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
//                           "rawat_inap_pr.nip,petugas.nama,rawat_inap_pr.tgl_perawatan,"+
//                           "rawat_inap_pr.jam_rawat,penjab.png_jawab,"+
//                           "ifnull((select bangsal.nm_bangsal from kamar_inap "+
//                           "inner join kamar inner join bangsal on "+
//                           "kamar_inap.kd_kamar=kamar.kd_kamar "+
//                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_pr.no_rawat limit 1 ),'Ruang Terhapus' ) as ruang , " +
//                           "rawat_inap_pr.material,rawat_inap_pr.bhp,rawat_inap_pr.tarif_tindakanpr,"+
//                           "rawat_inap_pr.kso,rawat_inap_pr.menejemen,rawat_inap_pr.biaya_rawat "+
//                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
//                           "inner join rawat_inap_pr on rawat_inap_pr.no_rawat=reg_periksa.no_rawat "+
//                           "inner join jns_perawatan_inap on rawat_inap_pr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
//                           "inner join petugas on rawat_inap_pr.nip=petugas.nip "+
//                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
//                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
//                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_pr.no_rawat like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_pr.nip like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
//                           " order by rawat_inap_pr.no_rawat desc");
//                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
//                    ps=koneksi.prepareStatement(
//                           "select rawat_inap_pr.no_rawat,reg_periksa.no_rkm_medis,"+
//                           "pasien.nm_pasien,rawat_inap_pr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
//                           "rawat_inap_pr.nip,petugas.nama,rawat_inap_pr.tgl_perawatan,"+
//                           "rawat_inap_pr.jam_rawat,penjab.png_jawab,"+
//                           "ifnull((select bangsal.nm_bangsal from kamar_inap "+
//                           "inner join kamar inner join bangsal on "+
//                           "kamar_inap.kd_kamar=kamar.kd_kamar "+
//                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_pr.no_rawat limit 1 ),'Ruang Terhapus' ) as ruang , " +
//                           "rawat_inap_pr.material,rawat_inap_pr.bhp,rawat_inap_pr.tarif_tindakanpr,"+
//                           "rawat_inap_pr.kso,rawat_inap_pr.menejemen,rawat_inap_pr.biaya_rawat "+
//                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
//                           "inner join rawat_inap_pr on rawat_inap_pr.no_rawat=reg_periksa.no_rawat "+
//                           "inner join jns_perawatan_inap on rawat_inap_pr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
//                           "inner join petugas on rawat_inap_pr.nip=petugas.nip "+
//                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
//                           "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_pr.no_rawat like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_pr.nip like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
//                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
//                           " order by rawat_inap_pr.no_rawat desc");
//                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
//                    ps=koneksi.prepareStatement(
//                           "select rawat_inap_pr.no_rawat,reg_periksa.no_rkm_medis,"+
//                           "pasien.nm_pasien,rawat_inap_pr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
//                           "rawat_inap_pr.nip,petugas.nama,rawat_inap_pr.tgl_perawatan,"+
//                           "rawat_inap_pr.jam_rawat,penjab.png_jawab,"+
//                           "ifnull((select bangsal.nm_bangsal from kamar_inap "+
//                           "inner join kamar inner join bangsal on "+
//                           "kamar_inap.kd_kamar=kamar.kd_kamar "+
//                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_pr.no_rawat limit 1 ),'Ruang Terhapus' ) as ruang , " +
//                           "rawat_inap_pr.material,rawat_inap_pr.bhp,rawat_inap_pr.tarif_tindakanpr,"+
//                           "rawat_inap_pr.kso,rawat_inap_pr.menejemen,rawat_inap_pr.biaya_rawat "+
//                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
//                           "inner join rawat_inap_pr on rawat_inap_pr.no_rawat=reg_periksa.no_rawat "+
//                           "inner join jns_perawatan_inap on rawat_inap_pr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
//                           "inner join petugas on rawat_inap_pr.nip=petugas.nip "+
//                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
//                           "where reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_pr.no_rawat like ? or "+
//                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
//                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
//                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
//                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_pr.nip like ? or "+
//                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
//                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_pr.tgl_perawatan,' ',rawat_inap_pr.jam_rawat) between ? and ? and concat(rawat_inap_pr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
//                           " order by rawat_inap_pr.no_rawat desc");
//                }     
//            }
//                
//            try {
//                if(KdPetugasRanapParamedis.getText().equals("")&&NmPetugasRanapParamedis.getText().equals("")&&KdCaraBayarRanapParamedis.getText().equals("")&&NmCaraBayarRanapParamedis.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
//                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
//                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
//                }else{
//                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
//                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
//                    ps.setString(3,"%"+KdPetugasRanapParamedis.getText()+NmPetugasRanapParamedis.getText()+"%");
//                    ps.setString(4,"%"+KdCaraBayarRanapParamedis.getText()+NmCaraBayarRanapParamedis.getText()+"%");
//                    ps.setString(5,"%"+TCari.getText().trim()+"%");
//                    ps.setString(6,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
//                    ps.setString(7,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
//                    ps.setString(8,"%"+KdPetugasRanapParamedis.getText()+NmPetugasRanapParamedis.getText()+"%");
//                    ps.setString(9,"%"+KdCaraBayarRanapParamedis.getText()+NmCaraBayarRanapParamedis.getText()+"%");
//                    ps.setString(10,"%"+TCari.getText().trim()+"%");
//                    ps.setString(11,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
//                    ps.setString(12,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
//                    ps.setString(13,"%"+KdPetugasRanapParamedis.getText()+NmPetugasRanapParamedis.getText()+"%");
//                    ps.setString(14,"%"+KdCaraBayarRanapParamedis.getText()+NmCaraBayarRanapParamedis.getText()+"%");
//                    ps.setString(15,"%"+TCari.getText().trim()+"%");
//                    ps.setString(16,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
//                    ps.setString(17,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
//                    ps.setString(18,"%"+KdPetugasRanapParamedis.getText()+NmPetugasRanapParamedis.getText()+"%");
//                    ps.setString(19,"%"+KdCaraBayarRanapParamedis.getText()+NmCaraBayarRanapParamedis.getText()+"%");
//                    ps.setString(20,"%"+TCari.getText().trim()+"%");
//                    ps.setString(21,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
//                    ps.setString(22,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
//                    ps.setString(23,"%"+KdPetugasRanapParamedis.getText()+NmPetugasRanapParamedis.getText()+"%");
//                    ps.setString(24,"%"+KdCaraBayarRanapParamedis.getText()+NmCaraBayarRanapParamedis.getText()+"%");
//                    ps.setString(25,"%"+TCari.getText().trim()+"%");
//                    ps.setString(26,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
//                    ps.setString(27,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
//                    ps.setString(28,"%"+KdPetugasRanapParamedis.getText()+NmPetugasRanapParamedis.getText()+"%");
//                    ps.setString(29,"%"+KdCaraBayarRanapParamedis.getText()+NmCaraBayarRanapParamedis.getText()+"%");
//                    ps.setString(30,"%"+TCari.getText().trim()+"%");
//                    ps.setString(31,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
//                    ps.setString(32,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
//                    ps.setString(33,"%"+KdPetugasRanapParamedis.getText()+NmPetugasRanapParamedis.getText()+"%");
//                    ps.setString(34,"%"+KdCaraBayarRanapParamedis.getText()+NmCaraBayarRanapParamedis.getText()+"%");
//                    ps.setString(35,"%"+TCari.getText().trim()+"%");
//                }
//                    
//                rs=ps.executeQuery();
//                i=1;
//                material=0;bhp=0;jmpetugas=0;kso=0;menejemen=0;total=0;
//                while(rs.next()){
//                    material=material+rs.getDouble("material");
//                    bhp=bhp+rs.getDouble("bhp");
//                    jmpetugas=jmpetugas+rs.getDouble("tarif_tindakanpr");
//                    kso=kso+rs.getDouble("kso");
//                    menejemen=menejemen+rs.getDouble("menejemen");
//                    total=total+rs.getDouble("biaya_rawat");
//                    tabModeRanapParamedis.addRow(new Object[]{
//                        i,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),
//                        rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),
//                        rs.getString(9),rs.getString(10),rs.getString(11),rs.getDouble(12),
//                        rs.getDouble(13),rs.getDouble(14),rs.getDouble(15),rs.getDouble(16),
//                        rs.getDouble(17),Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'")
//                    });
//                    i++;
//                }
//                if(total>0){
//                    tabModeRanapParamedis.addRow(new Object[]{
//                        "","","","","","","","","","","","Jumlah Total :",material,
//                        bhp,jmpetugas,kso,menejemen,total
//                    });
//                }
//            } catch (Exception e) {
//                System.out.println("Notifikasi : "+e);
//            } finally{
//                if(rs!=null){
//                    rs.close();
//                }
//                if(ps!=null){
//                    ps.close();
//                }
//            }           
//        }catch(Exception e){
//            System.out.println("Notifikasi : "+e);
//        }
//    }
    
    public void tampildokterpetugasranap(){     
//        Valid.tabelKosong(tabModeRalanDokter);
        try{
            if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                ps=koneksi.prepareStatement(
                       "select rawat_inap_drpr.no_rawat,reg_periksa.no_rkm_medis,"+
                       "pasien.nm_pasien,rawat_inap_drpr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
                       "rawat_inap_drpr.kd_dokter,dokter.nm_dokter,rawat_inap_drpr.nip,petugas.nama,rawat_inap_drpr.tgl_perawatan,"+
                       "rawat_inap_drpr.jam_rawat,penjab.png_jawab,ifnull((select bangsal.nm_bangsal from kamar_inap "+
                       "inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                       "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_drpr.no_rawat limit 1 ),'Ruang Terhapus' ) as ruang , " +
                       "rawat_inap_drpr.material,rawat_inap_drpr.bhp,rawat_inap_drpr.tarif_tindakandr,rawat_inap_drpr.tarif_tindakanpr,"+
                       "rawat_inap_drpr.kso,rawat_inap_drpr.menejemen,rawat_inap_drpr.biaya_rawat,penjab.kd_pj "+
                       "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                       "inner join rawat_inap_drpr on rawat_inap_drpr.no_rawat=reg_periksa.no_rawat "+
                       "inner join jns_perawatan_inap on rawat_inap_drpr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
                       "inner join dokter on rawat_inap_drpr.kd_dokter=dokter.kd_dokter "+
                       "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                       "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                       "inner join petugas on rawat_inap_drpr.nip=petugas.nip  "+
                       "where concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? order by rawat_inap_drpr.no_rawat desc");
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    ps=koneksi.prepareStatement(
                           "select rawat_inap_drpr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_inap_drpr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
                           "rawat_inap_drpr.kd_dokter,dokter.nm_dokter,rawat_inap_drpr.nip,petugas.nama,rawat_inap_drpr.tgl_perawatan,"+
                           "rawat_inap_drpr.jam_rawat,penjab.png_jawab,ifnull((select bangsal.nm_bangsal from kamar_inap "+
                           "inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_drpr.no_rawat limit 1 ),'Ruang Terhapus' ) as ruang , " +
                           "rawat_inap_drpr.material,rawat_inap_drpr.bhp,rawat_inap_drpr.tarif_tindakandr,rawat_inap_drpr.tarif_tindakanpr,"+
                           "rawat_inap_drpr.kso,rawat_inap_drpr.menejemen,rawat_inap_drpr.biaya_rawat,penjab.kd_pj "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_inap_drpr on rawat_inap_drpr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan_inap on rawat_inap_drpr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
                           "inner join dokter on rawat_inap_drpr.kd_dokter=dokter.kd_dokter "+
                           "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join petugas on rawat_inap_drpr.nip=petugas.nip  "+
                           "where concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.no_rawat like ? or "+
                           "concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
                           "concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.kd_dokter like ? or "+
                           "concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.nip like ? or "+
                           "concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ?  "+
                           " order by rawat_inap_drpr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
                    ps=koneksi.prepareStatement(
                           "select rawat_inap_drpr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_inap_drpr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
                           "rawat_inap_drpr.kd_dokter,dokter.nm_dokter,rawat_inap_drpr.nip,petugas.nama,rawat_inap_drpr.tgl_perawatan,"+
                           "rawat_inap_drpr.jam_rawat,penjab.png_jawab,ifnull((select bangsal.nm_bangsal from kamar_inap "+
                           "inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_drpr.no_rawat limit 1 ),'Ruang Terhapus' ) as ruang , " +
                           "rawat_inap_drpr.material,rawat_inap_drpr.bhp,rawat_inap_drpr.tarif_tindakandr,rawat_inap_drpr.tarif_tindakanpr,"+
                           "rawat_inap_drpr.kso,rawat_inap_drpr.menejemen,rawat_inap_drpr.biaya_rawat,penjab.kd_pj "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_inap_drpr on rawat_inap_drpr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan_inap on rawat_inap_drpr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
                           "inner join dokter on rawat_inap_drpr.kd_dokter=dokter.kd_dokter "+
                           "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join petugas on rawat_inap_drpr.nip=petugas.nip  "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ?  "+
                           " order by rawat_inap_drpr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
                    ps=koneksi.prepareStatement(
                           "select rawat_inap_drpr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_inap_drpr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
                           "rawat_inap_drpr.kd_dokter,dokter.nm_dokter,rawat_inap_drpr.nip,petugas.nama,rawat_inap_drpr.tgl_perawatan,"+
                           "rawat_inap_drpr.jam_rawat,penjab.png_jawab,ifnull((select bangsal.nm_bangsal from kamar_inap "+
                           "inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_drpr.no_rawat limit 1 ),'Ruang Terhapus' ) as ruang , " +
                           "rawat_inap_drpr.material,rawat_inap_drpr.bhp,rawat_inap_drpr.tarif_tindakandr,rawat_inap_drpr.tarif_tindakanpr,"+
                           "rawat_inap_drpr.kso,rawat_inap_drpr.menejemen,rawat_inap_drpr.biaya_rawat,penjab.kd_pj "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_inap_drpr on rawat_inap_drpr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan_inap on rawat_inap_drpr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
                           "inner join dokter on rawat_inap_drpr.kd_dokter=dokter.kd_dokter "+
                           "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join petugas on rawat_inap_drpr.nip=petugas.nip  "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ?  "+
                           " order by rawat_inap_drpr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
                    ps=koneksi.prepareStatement(
                           "select rawat_inap_drpr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_inap_drpr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
                           "rawat_inap_drpr.kd_dokter,dokter.nm_dokter,rawat_inap_drpr.nip,petugas.nama,rawat_inap_drpr.tgl_perawatan,"+
                           "rawat_inap_drpr.jam_rawat,penjab.png_jawab,ifnull((select bangsal.nm_bangsal from kamar_inap "+
                           "inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_drpr.no_rawat limit 1 ),'Ruang Terhapus' ) as ruang , " +
                           "rawat_inap_drpr.material,rawat_inap_drpr.bhp,rawat_inap_drpr.tarif_tindakandr,rawat_inap_drpr.tarif_tindakanpr,"+
                           "rawat_inap_drpr.kso,rawat_inap_drpr.menejemen,rawat_inap_drpr.biaya_rawat,penjab.kd_pj "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_inap_drpr on rawat_inap_drpr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan_inap on rawat_inap_drpr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
                           "inner join dokter on rawat_inap_drpr.kd_dokter=dokter.kd_dokter "+
                           "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join petugas on rawat_inap_drpr.nip=petugas.nip  "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ?  "+
                           " order by rawat_inap_drpr.no_rawat desc");
                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
                    ps=koneksi.prepareStatement(
                           "select rawat_inap_drpr.no_rawat,reg_periksa.no_rkm_medis,"+
                           "pasien.nm_pasien,rawat_inap_drpr.kd_jenis_prw,jns_perawatan_inap.nm_perawatan,"+
                           "rawat_inap_drpr.kd_dokter,dokter.nm_dokter,rawat_inap_drpr.nip,petugas.nama,rawat_inap_drpr.tgl_perawatan,"+
                           "rawat_inap_drpr.jam_rawat,penjab.png_jawab,ifnull((select bangsal.nm_bangsal from kamar_inap "+
                           "inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=rawat_inap_drpr.no_rawat limit 1 ),'Ruang Terhapus' ) as ruang , " +
                           "rawat_inap_drpr.material,rawat_inap_drpr.bhp,rawat_inap_drpr.tarif_tindakandr,rawat_inap_drpr.tarif_tindakanpr,"+
                           "rawat_inap_drpr.kso,rawat_inap_drpr.menejemen,rawat_inap_drpr.biaya_rawat,penjab.kd_pj "+
                           "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join rawat_inap_drpr on rawat_inap_drpr.no_rawat=reg_periksa.no_rawat "+
                           "inner join jns_perawatan_inap on rawat_inap_drpr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw "+
                           "inner join dokter on rawat_inap_drpr.kd_dokter=dokter.kd_dokter "+
                           "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join petugas on rawat_inap_drpr.nip=petugas.nip  "+
                           "where reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_inap.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.nip like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_inap_drpr.tgl_perawatan,' ',rawat_inap_drpr.jam_rawat) between ? and ? and concat(rawat_inap_drpr.kd_dokter,dokter.nm_dokter) like ? and concat(rawat_inap_drpr.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ?  "+
                           " order by rawat_inap_drpr.no_rawat desc");
                }    
            }
                
            try {
                if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                }else{
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(3,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(4,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(5,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(8,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(9,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(10,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(11,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(12,"%"+TCari.getText().trim()+"%");
                    ps.setString(13,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(14,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(15,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(16,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(17,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(18,"%"+TCari.getText().trim()+"%");
                    ps.setString(19,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(20,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(21,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(22,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(23,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(24,"%"+TCari.getText().trim()+"%");
                    ps.setString(25,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(26,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(27,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(28,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(29,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(30,"%"+TCari.getText().trim()+"%");
                    ps.setString(31,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(32,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(33,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(34,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(35,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(36,"%"+TCari.getText().trim()+"%");
                    ps.setString(37,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(38,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(39,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(40,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(41,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(42,"%"+TCari.getText().trim()+"%");
                    ps.setString(43,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(44,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(45,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(46,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(47,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(48,"%"+TCari.getText().trim()+"%");
                    ps.setString(49,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(50,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(51,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(52,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(53,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(54,"%"+TCari.getText().trim()+"%");
                }
                    
                rs=ps.executeQuery();
                i=1;
                material=0;bhp=0;jmdokter=0;jmpetugas=0;kso=0;menejemen=0;total=0;
                while(rs.next()){
                    material=material+rs.getDouble("material");
                    bhp=bhp+rs.getDouble("bhp");
                    jmdokter=jmdokter+rs.getDouble("tarif_tindakandr");
                    jmpetugas=jmpetugas+rs.getDouble("tarif_tindakanpr");
                    kso=kso+rs.getDouble("kso");
                    menejemen=menejemen+rs.getDouble("menejemen");
                    total=total+rs.getDouble("biaya_rawat");
                    if(rs.getString("kd_pj").equals("BPJ")){
                        nosep=Sequel.cariIsi("select bridging_sep.no_sep from bridging_sep where bridging_sep.no_rawat='"+rs.getString("no_rawat")+"'");
                    }else{
                        nosep=rs.getString("no_rawat");
                    }
                    
                    if(Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'").equals("")){
                        rawatgabung=rs.getString("no_rawat");
                    }else{
                        rawatgabung=Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'");
                    }
                    if(rs.getString("ruang").equals("Unit IGD")){
                        ruang="UGD";
                    }else if(rs.getString("ruang").equals("IGD VK")){
                        ruang="IIDB";
                    }else if(rs.getString("ruang").trim().contains("Kembar")){
                        ruang="IRNA II";
                    }else if(rs.getString("ruang").trim().contains("Duyung")){
                        ruang="IIDB";
                    }else if(rs.getString("ruang").trim().contains("isolasi")){
                        ruang="ISOLASI";
                    }else if(rs.getString("ruang").trim().contains("Anggrek")){
                        ruang="ISOLASI";
                    }else if(rs.getString("ruang").trim().contains("Dahlia")){
                        ruang="ISOLASI";
                    }else if(rs.getString("ruang").trim().contains("Melati")){
                        ruang="ISOLASI";
                    }else if(rs.getString("ruang").trim().contains("Beleq")||rs.getString("ruang").trim().contains("Wangi")){
                        ruang="IRNA III";
                    }else if(rs.getString("ruang").equals("ICU")){
                        ruang="ICU";
                    }else if(rs.getString("ruang").equals("NICU")){
                        ruang="NICU";
                    }else if(rs.getString("ruang").equals("HCU")){
                        ruang="ICU";
                    }else{
                        ruang="Poli";
                    }
                    kodedokter=Sequel.cariIsi("select kd_dokter_remun from maping_dokter_dpjpremun where kd_dokter='"+rs.getString("kd_dokter")+"'");
                    if(rs.getString("nm_perawatan").trim().contains("usg")||rs.getString("nm_perawatan").trim().contains("Ultrasonografi")||rs.getString("nm_perawatan").trim().contains("Konsultasi Dokter")||
                            rs.getString("nm_perawatan").trim().contains("Konsultasi Spesialis")||rs.getString("nm_perawatan").trim().contains("konsultasi")||rs.getString("nm_perawatan").trim().contains("visite")||rs.getString("nm_perawatan").trim().contains("ekg")){
                        jenis="Tindakan Dokter";
                        kodepetugas=kodedokter;
                    }else{
                        jenis="Tindakan Petugas";
                        kodepetugas="0";
                    }
                    
                    tabModeRalanDokter.addRow(new Object[]{
                        i,rs.getString("no_rkm_medis"),rawatgabung,nosep,rs.getString("nm_pasien"),"Tindakan Petugas",rs.getString("kd_jenis_prw"),
                        rs.getString("nm_perawatan"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("nip"),rs.getString("nama"),rs.getString("tgl_perawatan"),
                        rs.getString("jam_rawat"),rs.getString("png_jawab"),rs.getString("ruang"),rs.getDouble("material"),
                        rs.getDouble("bhp"),rs.getDouble("tarif_tindakandr"),rs.getDouble("kso"),rs.getDouble("menejemen"),
                        rs.getDouble("biaya_rawat"),rs.getString("ruang"),ruang,jenis,"1",rs.getDouble("biaya_rawat"),rs.getString("nm_perawatan"),kodedokter,kodepetugas
                        
//                        i,rs.getString("no_rkm_medis"),rs.getString(2),rs.getString(3),rs.getString(4),
//                        rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),
//                        rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),
//                        rs.getString(13),rs.getDouble(14),rs.getDouble(15),rs.getDouble(16),
//                        rs.getDouble(17),rs.getDouble(18),rs.getDouble(19),rs.getDouble(20),Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'")
                    });
                    i++;
                }
//                if(total>0){
//                    tabModeRalanDokter.addRow(new Object[]{
//                        "","","","","","","","","","","","","","Jumlah Total :",material,
//                        bhp,jmdokter,jmpetugas,kso,menejemen,total
//                    });
//                }
                if(total>0){
                    tabModeRalanDokter.addRow(new Object[]{
                        "","","","","","","","","","","","","","","","Jumlah Total :",material,
                        bhp,jmdokter,kso,menejemen,total,"","","","","","","",""
                    });
                }
            } catch (Exception e) {
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

    
    public void tampilperiksaradiologi(){     
//        Valid.tabelKosong(tabModeRalanDokter);
        try{
            if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPetugasRalanDokterParamedis.getText().equals("")&&NmPetugasRalanDokterParamedis.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                ps=koneksi.prepareStatement(
                       "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                       "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                       "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                       "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                       "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                       "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                       "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                       "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                       "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                       "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                       "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                       "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                       "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                       "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                       "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                       "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                       "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                       "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                       "where concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? order by periksa_radiologi.tgl_periksa");
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    ps=koneksi.prepareStatement(
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "where concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.no_rawat like ? or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.kd_jenis_prw like ? or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_radiologi.nm_perawatan like ? or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.kd_dokter like ? or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.nip like ? or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.dokter_perujuk like ? or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_radiologi.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
                    ps=koneksi.prepareStatement(
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.kd_jenis_prw like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_radiologi.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.dokter_perujuk like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_radiologi.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
                    ps=koneksi.prepareStatement(
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.kd_jenis_prw like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_radiologi.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.dokter_perujuk like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_radiologi.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
                    ps=koneksi.prepareStatement(
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.kd_jenis_prw like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_radiologi.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.dokter_perujuk like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_radiologi.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
                    ps=koneksi.prepareStatement(
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.kd_jenis_prw like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_radiologi.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.nip like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.dokter_perujuk like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_radiologi.tgl_periksa");
                }
                    
            }    
                
            try {
                if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPetugasRalanDokterParamedis.getText().equals("")&&NmPetugasRalanDokterParamedis.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                }else{
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(3,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(4,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(5,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(8,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(9,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(10,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(11,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(12,"%"+TCari.getText().trim()+"%");
                    ps.setString(13,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(14,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(15,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(16,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(17,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(18,"%"+TCari.getText().trim()+"%");
                    ps.setString(19,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(20,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(21,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(22,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(23,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(24,"%"+TCari.getText().trim()+"%");
                    ps.setString(25,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(26,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(27,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(28,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(29,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(30,"%"+TCari.getText().trim()+"%");
                    ps.setString(31,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(32,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(33,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(34,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(35,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(36,"%"+TCari.getText().trim()+"%");
                    ps.setString(37,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(38,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(39,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(40,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(41,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(42,"%"+TCari.getText().trim()+"%");
                    ps.setString(43,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(44,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(45,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(46,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(47,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(48,"%"+TCari.getText().trim()+"%");
                    ps.setString(49,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(50,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(51,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(52,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(53,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(54,"%"+TCari.getText().trim()+"%");
                    ps.setString(55,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(56,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(57,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(58,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(59,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(60,"%"+TCari.getText().trim()+"%");
                    ps.setString(61,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(62,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(63,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(64,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(65,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(66,"%"+TCari.getText().trim()+"%");
                    ps.setString(67,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(68,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(69,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(70,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(71,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(72,"%"+TCari.getText().trim()+"%");
                }   
                    
                rs=ps.executeQuery();
                i=1;
                material=0;bhp=0;jmdokter=0;jmpetugas=0;jmperujuk=0;
                kso=0;menejemen=0;total=0;
                while(rs.next()){
                    material=material+rs.getDouble("bagian_rs");
                    bhp=bhp+rs.getDouble("bhp");
                    jmdokter=jmdokter+rs.getDouble("tarif_tindakan_dokter");
                    jmpetugas=jmpetugas+rs.getDouble("tarif_tindakan_petugas");
                    jmperujuk=jmperujuk+rs.getDouble("tarif_perujuk");
                    kso=kso+rs.getDouble("kso");
                    menejemen=menejemen+rs.getDouble("menejemen");
                    total=total+rs.getDouble("biaya");
                    if(rs.getString("kd_pj").equals("BPJ")){
                        nosep=Sequel.cariIsi("select bridging_sep.no_sep from bridging_sep where bridging_sep.no_rawat='"+rs.getString("no_rawat")+"'");
                    }else{
                        nosep=rs.getString("no_rawat");
                    }
                    if(Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'").equals("")){
                        rawatgabung=rs.getString("no_rawat");
                    }else{
                        rawatgabung=Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'");
                    }
                    kodedokter=Sequel.cariIsi("select kd_dokter_remun from maping_dokter_dpjpremun where kd_dokter='"+rs.getString("kd_dokter")+"'");
                    if(rs.getString("nm_perawatan").contains("usg")||rs.getString("nm_perawatan").contains("ultrasonografi")||rs.getString("nm_perawatan").contains("konsultasi dokter")||rs.getString("nm_perawatan").contains("visite dokter")||rs.getString("nm_perawatan").contains("ekg")){
                        jenis="Tindakan Dokter";
                        kodepetugas=kodedokter;
                    }else{
                        jenis="Tindakan Petugas";
                        kodepetugas="0";
                    }
                    tabModeRalanDokter.addRow(new Object[]{
                        i,rs.getString("no_rkm_medis"),rawatgabung,nosep,rs.getString("nm_pasien"),"Radiologi",rs.getString("kd_jenis_prw"),
                        rs.getString("nm_perawatan"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("nip"),rs.getString("nama"),rs.getString("tgl_periksa"),
                        rs.getString("jam"),rs.getString("png_jawab"),rs.getString("ruangan"),rs.getDouble("bagian_rs"),
                        rs.getDouble("bhp"),rs.getDouble("tarif_tindakan_dokter"),rs.getDouble("kso"),rs.getDouble("menejemen"),
                        rs.getDouble("biaya"),rs.getString("ruangan"),"radiologi","radiologi","1",rs.getDouble("biaya"),rs.getString("nm_perawatan"),kodedokter,kodepetugas
                        
//                        i,rs.getString("no_rawat"),rs.getString("no_rkm_medis"),
//                        rs.getString("nm_pasien"),rs.getString("kd_jenis_prw"),
//                        rs.getString("nm_perawatan"),rs.getString("kd_dokter"),
//                        rs.getString("nm_dokter"),rs.getString("nip"),rs.getString("nama"),
//                        rs.getString("dokter_perujuk"),rs.getString("perujuk"),
//                        rs.getString("tgl_periksa"),rs.getString("jam"),
//                        rs.getString("png_jawab"),rs.getString("ruangan"),rs.getDouble("bagian_rs"),
//                        rs.getDouble("bhp"),rs.getDouble("tarif_tindakan_dokter"),
//                        rs.getDouble("tarif_tindakan_petugas"),rs.getDouble("tarif_perujuk"),
//                        rs.getDouble("kso"),rs.getDouble("menejemen"),rs.getDouble("biaya")
                    });
                    i++;
                }
//                if(total>0){
//                    tabModeRalanDokter.addRow(new Object[]{
//                        "","","","","","","","","","","","","","","","Jumlah Total :",
//                        material,bhp,jmdokter,jmpetugas,jmperujuk,kso,menejemen,total
//                    });
//                }
                if(total>0){
                    tabModeRalanDokter.addRow(new Object[]{
                        "","","","","","","","","","","","","","","","Jumlah Total :",material,
                        bhp,jmdokter,kso,menejemen,total,"","","","","","","",""
                    });
                }
            } catch (Exception e) {
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
    
    public void tampilperiksalaboratorium(){     
//        Valid.tabelKosong(tabModeRalanDokter);
        try{
            if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPetugasRalanDokterParamedis.getText().equals("")&&NmPetugasRalanDokterParamedis.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                ps=koneksi.prepareStatement(
                       "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                       "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                       "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                       "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                       "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                       "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                       "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                       "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                       "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                       "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                       "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                       "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                       "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                       "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                       "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                       "inner join petugas on periksa_lab.nip=petugas.nip "+
                       "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                       "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                       "where concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? order by periksa_lab.tgl_periksa");
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    ps=koneksi.prepareStatement(
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "where concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.no_rawat like ? or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_jenis_prw like ? or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_lab.nm_perawatan like ? or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_dokter like ? or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.nip like ? or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.dokter_perujuk like ? or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_lab.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
                    ps=koneksi.prepareStatement(
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_jenis_prw like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_lab.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.dokter_perujuk like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_lab.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
                    ps=koneksi.prepareStatement(
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_jenis_prw like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_lab.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.dokter_perujuk like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_lab.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
                    ps=koneksi.prepareStatement(
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_jenis_prw like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_lab.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.dokter_perujuk like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_lab.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
                    ps=koneksi.prepareStatement(
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_jenis_prw like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_lab.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.nip like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.dokter_perujuk like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_lab.tgl_periksa");
                }    
            }
                
            try {
                if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPetugasRalanDokterParamedis.getText().equals("")&&NmPetugasRalanDokterParamedis.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                }else{
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(3,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(4,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(5,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(8,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(9,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(10,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(11,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(12,"%"+TCari.getText().trim()+"%");
                    ps.setString(13,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(14,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(15,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(16,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(17,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(18,"%"+TCari.getText().trim()+"%");
                    ps.setString(19,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(20,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(21,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(22,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(23,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(24,"%"+TCari.getText().trim()+"%");
                    ps.setString(25,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(26,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(27,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(28,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(29,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(30,"%"+TCari.getText().trim()+"%");
                    ps.setString(31,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(32,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(33,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(34,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(35,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(36,"%"+TCari.getText().trim()+"%");
                    ps.setString(37,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(38,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(39,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(40,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(41,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(42,"%"+TCari.getText().trim()+"%");
                    ps.setString(43,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(44,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(45,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(46,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(47,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(48,"%"+TCari.getText().trim()+"%");
                    ps.setString(49,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(50,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(51,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(52,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(53,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(54,"%"+TCari.getText().trim()+"%");
                    ps.setString(55,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(56,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(57,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(58,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(59,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(60,"%"+TCari.getText().trim()+"%");
                    ps.setString(61,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(62,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(63,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(64,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(65,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(66,"%"+TCari.getText().trim()+"%");
                    ps.setString(67,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(68,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(69,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(70,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(71,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(72,"%"+TCari.getText().trim()+"%");
                }
                    
                rs=ps.executeQuery();
                i=1;
                material=0;bhp=0;jmdokter=0;jmpetugas=0;jmperujuk=0;
                kso=0;menejemen=0;total=0;
                while(rs.next()){
                    material=material+rs.getDouble("bagian_rs");
                    bhp=bhp+rs.getDouble("bhp");
                    jmdokter=jmdokter+rs.getDouble("tarif_tindakan_dokter");
                    jmpetugas=jmpetugas+rs.getDouble("tarif_tindakan_petugas");
                    jmperujuk=jmperujuk+rs.getDouble("tarif_perujuk");
                    kso=kso+rs.getDouble("kso");
                    menejemen=menejemen+rs.getDouble("menejemen");
                    total=total+rs.getDouble("biaya");
                    if(rs.getString("kd_pj").equals("BPJ")){
                        nosep=Sequel.cariIsi("select bridging_sep.no_sep from bridging_sep where bridging_sep.no_rawat='"+rs.getString("no_rawat")+"'");
                    }else{
                        nosep=rs.getString("no_rawat");
                    }
                    if(Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'").equals("")){
                        rawatgabung=rs.getString("no_rawat");
                    }else{
                        rawatgabung=Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'");
                    }
                    kodedokter=Sequel.cariIsi("select kd_dokter_remun from maping_dokter_dpjpremun where kd_dokter='"+rs.getString("kd_dokter")+"'");
//                    if(rs.getString("nm_perawatan").contains("usg")||rs.getString("nm_perawatan").contains("konsultasi")||rs.getString("nm_perawatan").contains("visite")||rs.getString("nm_perawatan").contains("ekg")){
//                        jenis="Tindakan Dokter";
//                        kodepetugas=kodedokter;
//                    }else{
//                        jenis="Tindakan Petugas";
//                        kodepetugas="0";
//                    }
                    tabModeRalanDokter.addRow(new Object[]{
                        i,rs.getString("no_rkm_medis"),rawatgabung,nosep,rs.getString("nm_pasien"),"Laboratorium",rs.getString("kd_jenis_prw"),
                        rs.getString("nm_perawatan"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("nip"),rs.getString("nama"),rs.getString("tgl_periksa"),
                        rs.getString("jam"),rs.getString("png_jawab"),rs.getString("ruangan"),rs.getDouble("bagian_rs"),
                        rs.getDouble("bhp"),rs.getDouble("tarif_tindakan_dokter"),rs.getDouble("kso"),rs.getDouble("menejemen"),
                        rs.getDouble("biaya"),rs.getString("ruangan"),"Laboratorium","Laboratorium","1",rs.getDouble("biaya"),rs.getString("nm_perawatan"),kodedokter,"0"
                        
//                        i,rs.getString("no_rawat"),rs.getString("no_rkm_medis"),
//                        rs.getString("nm_pasien"),rs.getString("kd_jenis_prw"),
//                        rs.getString("nm_perawatan"),rs.getString("kd_dokter"),
//                        rs.getString("nm_dokter"),rs.getString("nip"),rs.getString("nama"),
//                        rs.getString("dokter_perujuk"),rs.getString("perujuk"),
//                        rs.getString("tgl_periksa"),rs.getString("jam"),
//                        rs.getString("png_jawab"),rs.getString("ruangan"),rs.getDouble("bagian_rs"),
//                        rs.getDouble("bhp"),rs.getDouble("tarif_tindakan_dokter"),
//                        rs.getDouble("tarif_tindakan_petugas"),rs.getDouble("tarif_perujuk"),
//                        rs.getDouble("kso"),rs.getDouble("menejemen"),rs.getDouble("biaya")
                    });
                    i++;
                }
//                if(total>0){
//                    tabModeRalanDokter.addRow(new Object[]{
//                        "","","","","","","","","","","","","","","","Jumlah Total :",
//                        material,bhp,jmdokter,jmpetugas,jmperujuk,kso,menejemen,total
//                    });
//                }
                if(total>0){
                    tabModeRalanDokter.addRow(new Object[]{
                        "","","","","","","","","","","","","","","","Jumlah Total :",material,
                        bhp,jmdokter,kso,menejemen,total,"","","","","","","",""
                    });
                }
            } catch (Exception e) {
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
    
    public void tampildetailperiksalaboratorium(){     
//        Valid.tabelKosong(tabModeRalanDokter);
        try {
            if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPetugasRalanDokterParamedis.getText().equals("")&&NmPetugasRalanDokterParamedis.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                ps=koneksi.prepareStatement(
                        "select detail_periksa_lab.no_rawat,reg_periksa.no_rkm_medis,"+
                        "pasien.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                        "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                        "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                        "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                        "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                        "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                        "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                        "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                        "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                        "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                        "detail_periksa_lab.biaya_item,penjab.kd_pj from detail_periksa_lab "+
                        "inner join reg_periksa inner join pasien inner join template_laboratorium "+
                        "inner join jns_perawatan_lab inner join periksa_lab "+
                        "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                        "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                        "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                        "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                        "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                        "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                        "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                        "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw "+
                        "where concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? order by detail_periksa_lab.tgl_periksa");
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    ps=koneksi.prepareStatement(
                            "select detail_periksa_lab.no_rawat,reg_periksa.no_rkm_medis,"+
                            "pasien.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item,penjab.kd_pj from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw "+
                            "where concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.no_rawat like ? or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.kd_jenis_prw like ? or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_lab.nm_perawatan like ? or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_dokter like ? or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.nip like ? or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.dokter_perujuk like ? or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                            " order by detail_periksa_lab.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
                    ps=koneksi.prepareStatement(
                            "select detail_periksa_lab.no_rawat,reg_periksa.no_rkm_medis,"+
                            "pasien.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item,penjab.kd_pj from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab inner join piutang_pasien "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                            "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.no_rawat like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.kd_jenis_prw like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_lab.nm_perawatan like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_dokter like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.nip like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.dokter_perujuk like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                            " order by detail_periksa_lab.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
                    ps=koneksi.prepareStatement(
                            "select detail_periksa_lab.no_rawat,reg_periksa.no_rkm_medis,"+
                            "pasien.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item,penjab.kd_pj from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab inner join piutang_pasien "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                            "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.no_rawat like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.kd_jenis_prw like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_lab.nm_perawatan like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_dokter like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.nip like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.dokter_perujuk like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                            " order by detail_periksa_lab.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
                    ps=koneksi.prepareStatement(
                            "select detail_periksa_lab.no_rawat,reg_periksa.no_rkm_medis,"+
                            "pasien.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item,penjab.kd_pj from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw "+
                            "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.no_rawat like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.kd_jenis_prw like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_lab.nm_perawatan like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_dokter like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.nip like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.dokter_perujuk like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                            " order by detail_periksa_lab.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
                    ps=koneksi.prepareStatement(
                            "select detail_periksa_lab.no_rawat,reg_periksa.no_rkm_medis,"+
                            "pasien.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item,penjab.kd_pj from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw "+
                            "where reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.no_rawat like ? or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.kd_jenis_prw like ? or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_lab.nm_perawatan like ? or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.kd_dokter like ? or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.nip like ? or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.dokter_perujuk like ? or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                            " order by detail_periksa_lab.tgl_periksa");
                }    
            }
                
            try {
                if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPetugasRalanDokterParamedis.getText().equals("")&&NmPetugasRalanDokterParamedis.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                }else{
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(3,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(4,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(5,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(8,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(9,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(10,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(11,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(12,"%"+TCari.getText().trim()+"%");
                    ps.setString(13,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(14,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(15,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(16,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(17,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(18,"%"+TCari.getText().trim()+"%");
                    ps.setString(19,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(20,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(21,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(22,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(23,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(24,"%"+TCari.getText().trim()+"%");
                    ps.setString(25,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(26,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(27,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(28,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(29,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(30,"%"+TCari.getText().trim()+"%");
                    ps.setString(31,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(32,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(33,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(34,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(35,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(36,"%"+TCari.getText().trim()+"%");
                    ps.setString(37,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(38,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(39,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(40,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(41,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(42,"%"+TCari.getText().trim()+"%");
                    ps.setString(43,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(44,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(45,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(46,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(47,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(48,"%"+TCari.getText().trim()+"%");
                    ps.setString(49,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(50,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(51,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(52,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(53,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(54,"%"+TCari.getText().trim()+"%");
                    ps.setString(55,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(56,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(57,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(58,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(59,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(60,"%"+TCari.getText().trim()+"%");
                    ps.setString(61,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(62,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(63,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(64,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(65,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(66,"%"+TCari.getText().trim()+"%");
                    ps.setString(67,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(68,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(69,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(70,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(71,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(72,"%"+TCari.getText().trim()+"%");
                }
                    
                rs=ps.executeQuery();
                i=1;
                material=0;bhp=0;jmdokter=0;jmpetugas=0;jmperujuk=0;
                kso=0;menejemen=0;total=0;
                while(rs.next()){
                    material=material+rs.getDouble("bagian_rs");
                    bhp=bhp+rs.getDouble("bhp");
                    jmdokter=jmdokter+rs.getDouble("bagian_dokter");
                    jmpetugas=jmpetugas+rs.getDouble("bagian_laborat");
                    jmperujuk=jmperujuk+rs.getDouble("bagian_perujuk");
                    kso=kso+rs.getDouble("kso");
                    menejemen=menejemen+rs.getDouble("menejemen");
                    total=total+rs.getDouble("biaya_item");
                    if(rs.getString("kd_pj").equals("BPJ")){
                        nosep=Sequel.cariIsi("select bridging_sep.no_sep from bridging_sep where bridging_sep.no_rawat='"+rs.getString("no_rawat")+"'");
                    }else{
                        nosep=rs.getString("no_rawat");
                    }
                    if(Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'").equals("")){
                        rawatgabung=rs.getString("no_rawat");
                    }else{
                        rawatgabung=Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'");
                    }
                    kodedokter=Sequel.cariIsi("select kd_dokter_remun from maping_dokter_dpjpremun where kd_dokter='"+rs.getString("kd_dokter")+"'");
//                    if(rs.getString("nm_perawatan").contains("usg")||rs.getString("nm_perawatan").contains("konsultasi")||rs.getString("nm_perawatan").contains("visite")||rs.getString("nm_perawatan").contains("ekg")){
//                        jenis="Tindakan Dokter";
//                        kodepetugas=kodedokter;
//                    }else{
//                        jenis="Tindakan Petugas";
//                        kodepetugas="0";
//                    }
                    tabModeRalanDokter.addRow(new Object[]{
                        i,rs.getString("no_rkm_medis"),rawatgabung,nosep,rs.getString("nm_pasien"),"Laboratorium",rs.getString("id_template"),
                        rs.getString("Pemeriksaan"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("nip"),rs.getString("nama"),rs.getString("tgl_periksa"),
                        rs.getString("jam"),rs.getString("png_jawab"),rs.getString("ruangan"),rs.getDouble("bagian_rs"),
                        rs.getDouble("bhp"),rs.getDouble("bagian_dokter"),rs.getDouble("kso"),rs.getDouble("menejemen"),
                        rs.getDouble("biaya_item"),rs.getString("ruangan"),ruang,jenis,"1",rs.getDouble("biaya_rawat"),rs.getString("Pemeriksaan"),kodedokter,"0"
                        
//                        i,rs.getString("no_rawat"),rs.getString("no_rkm_medis"),
//                        rs.getString("nm_pasien"),rs.getString("id_template"),
//                        rs.getString("Pemeriksaan"),rs.getString("kd_dokter"),
//                        rs.getString("nm_dokter"),rs.getString("nip"),rs.getString("nama"),
//                        rs.getString("dokter_perujuk"),rs.getString("perujuk"),
//                        rs.getString("tgl_periksa"),rs.getString("jam"),
//                        rs.getString("png_jawab"),rs.getString("ruangan"),rs.getDouble("bagian_rs"),
//                        rs.getDouble("bhp"),rs.getDouble("bagian_dokter"),
//                        rs.getDouble("bagian_laborat"),rs.getDouble("bagian_perujuk"),
//                        rs.getDouble("kso"),rs.getDouble("menejemen"),rs.getDouble("biaya_item")
                    });
                    i++;
                }
//                if(tabModeRalanDokter.getRowCount()>0){
//                    tabModeRalanDokter.addRow(new Object[]{
//                        "","","","","","","","","","","","","","","","Jumlah Total :",
//                        material,bhp,jmdokter,jmpetugas,jmperujuk,kso,menejemen,total
//                    });
//                }
                if(total>0){
                    tabModeRalanDokter.addRow(new Object[]{
                        "","","","","","","","","","","","","","","","Jumlah Total :",material,
                        bhp,jmdokter,kso,menejemen,total,"","","","","","","",""
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }  
        } catch (Exception e) {
            System.out.println("keuangan.DlgDetailTindakan.tampil10() : "+e);
        }            
    }
    //MULAI SINI DETAIL TINDAKAN BDRS
    public void tampiltindakanbdrs(){
             
//        Valid.tabelKosong(tabModeRalanDokter);
        try{
            if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPetugasRalanDokterParamedis.getText().equals("")&&NmPetugasRalanDokterParamedis.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                ps=koneksi.prepareStatement(
                       "select periksa_utd.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                       "periksa_utd.kd_jenis_prw,jns_perawatan_utd.nm_perawatan, "+
                       "periksa_utd.kd_dokter,dokter.nm_dokter,periksa_utd.nip,"+
                       "petugas.nama,periksa_utd.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                       "periksa_utd.tgl_periksa,periksa_utd.jam,penjab.png_jawab,"+
                       "periksa_utd.bagian_rs,periksa_utd.bhp,periksa_utd.tarif_perujuk,"+
                       "periksa_utd.tarif_tindakan_dokter,periksa_utd.tarif_tindakan_petugas,"+
                       "periksa_utd.kso,periksa_utd.menejemen,periksa_utd.biaya,"+
                       "if(periksa_utd.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                       "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                       "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_utd.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                       "from periksa_utd inner join reg_periksa on periksa_utd.no_rawat=reg_periksa.no_rawat "+
                       "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                       "inner join dokter on periksa_utd.kd_dokter=dokter.kd_dokter "+
                       "inner join dokter as perujuk on periksa_utd.dokter_perujuk=perujuk.kd_dokter "+
                       "inner join petugas on periksa_utd.nip=petugas.nip "+
                       "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                       "inner join jns_perawatan_utd on periksa_utd.kd_jenis_prw=jns_perawatan_utd.kd_jenis_prw "+
                       "where concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? order by periksa_utd.tgl_periksa");
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    ps=koneksi.prepareStatement(
                           "select periksa_utd.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_utd.kd_jenis_prw,jns_perawatan_utd.nm_perawatan, "+
                           "periksa_utd.kd_dokter,dokter.nm_dokter,periksa_utd.nip,"+
                           "petugas.nama,periksa_utd.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_utd.tgl_periksa,periksa_utd.jam,penjab.png_jawab,"+
                           "periksa_utd.bagian_rs,periksa_utd.bhp,periksa_utd.tarif_perujuk,"+
                           "periksa_utd.tarif_tindakan_dokter,periksa_utd.tarif_tindakan_petugas,"+
                           "periksa_utd.kso,periksa_utd.menejemen,periksa_utd.biaya,"+
                           "if(periksa_utd.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_utd.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_utd inner join reg_periksa on periksa_utd.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_utd.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_utd.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_utd.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_utd on periksa_utd.kd_jenis_prw=jns_perawatan_utd.kd_jenis_prw "+
                           "where concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.no_rawat like ? or "+
                           "concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.kd_jenis_prw like ? or "+
                           "concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_utd.nm_perawatan like ? or "+
                           "concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.kd_dokter like ? or "+
                           "concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.nip like ? or "+
                           "concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.dokter_perujuk like ? or "+
                           "concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_utd.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
                    ps=koneksi.prepareStatement(
                           "select periksa_utd.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_utd.kd_jenis_prw,jns_perawatan_utd.nm_perawatan, "+
                           "periksa_utd.kd_dokter,dokter.nm_dokter,periksa_utd.nip,"+
                           "petugas.nama,periksa_utd.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_utd.tgl_periksa,periksa_utd.jam,penjab.png_jawab,"+
                           "periksa_utd.bagian_rs,periksa_utd.bhp,periksa_utd.tarif_perujuk,"+
                           "periksa_utd.tarif_tindakan_dokter,periksa_utd.tarif_tindakan_petugas,"+
                           "periksa_utd.kso,periksa_utd.menejemen,periksa_utd.biaya,"+
                           "if(periksa_utd.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_utd.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_utd inner join reg_periksa on periksa_utd.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_utd.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_utd.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_utd.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_utd on periksa_utd.kd_jenis_prw=jns_perawatan_utd.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.kd_jenis_prw like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_utd.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.dokter_perujuk like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_utd.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
                    ps=koneksi.prepareStatement(
                           "select periksa_utd.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_utd.kd_jenis_prw,jns_perawatan_utd.nm_perawatan, "+
                           "periksa_utd.kd_dokter,dokter.nm_dokter,periksa_utd.nip,"+
                           "petugas.nama,periksa_utd.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_utd.tgl_periksa,periksa_utd.jam,penjab.png_jawab,"+
                           "periksa_utd.bagian_rs,periksa_utd.bhp,periksa_utd.tarif_perujuk,"+
                           "periksa_utd.tarif_tindakan_dokter,periksa_utd.tarif_tindakan_petugas,"+
                           "periksa_utd.kso,periksa_utd.menejemen,periksa_utd.biaya,"+
                           "if(periksa_utd.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_utd.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_utd inner join reg_periksa on periksa_utd.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_utd.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_utd.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_utd.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_utd on periksa_utd.kd_jenis_prw=jns_perawatan_utd.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.kd_jenis_prw like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_utd.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.dokter_perujuk like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_utd.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
                    ps=koneksi.prepareStatement(
                           "select periksa_utd.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_utd.kd_jenis_prw,jns_perawatan_utd.nm_perawatan, "+
                           "periksa_utd.kd_dokter,dokter.nm_dokter,periksa_utd.nip,"+
                           "petugas.nama,periksa_utd.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_utd.tgl_periksa,periksa_utd.jam,penjab.png_jawab,"+
                           "periksa_utd.bagian_rs,periksa_utd.bhp,periksa_utd.tarif_perujuk,"+
                           "periksa_utd.tarif_tindakan_dokter,periksa_utd.tarif_tindakan_petugas,"+
                           "periksa_utd.kso,periksa_utd.menejemen,periksa_utd.biaya,"+
                           "if(periksa_utd.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_utd.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_utd inner join reg_periksa on periksa_utd.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_utd.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_utd.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_utd.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_utd on periksa_utd.kd_jenis_prw=jns_perawatan_utd.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.kd_jenis_prw like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_utd.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.nip like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.dokter_perujuk like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_utd.tgl_periksa");
                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
                    ps=koneksi.prepareStatement(
                           "select periksa_utd.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                           "periksa_utd.kd_jenis_prw,jns_perawatan_utd.nm_perawatan, "+
                           "periksa_utd.kd_dokter,dokter.nm_dokter,periksa_utd.nip,"+
                           "petugas.nama,periksa_utd.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_utd.tgl_periksa,periksa_utd.jam,penjab.png_jawab,"+
                           "periksa_utd.bagian_rs,periksa_utd.bhp,periksa_utd.tarif_perujuk,"+
                           "periksa_utd.tarif_tindakan_dokter,periksa_utd.tarif_tindakan_petugas,"+
                           "periksa_utd.kso,periksa_utd.menejemen,periksa_utd.biaya,"+
                           "if(periksa_utd.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_utd.no_rawat limit 1 )) as ruangan,penjab.kd_pj "+
                           "from periksa_utd inner join reg_periksa on periksa_utd.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join dokter on periksa_utd.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_utd.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_utd.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_utd on periksa_utd.kd_jenis_prw=jns_perawatan_utd.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Belum Bayar' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien.nm_pasien like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.kd_jenis_prw like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and jns_perawatan_utd.nm_perawatan like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.kd_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and dokter.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.nip like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and petugas.nama like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_utd.dokter_perujuk like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and perujuk.nm_dokter like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_utd.tgl_periksa,' ',periksa_utd.jam) between ? and ? and concat(periksa_utd.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_utd.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and penjab.png_jawab like ? "+
                           " order by periksa_utd.tgl_periksa");
                }
                    
            }    
                
            try {
                if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPetugasRalanDokterParamedis.getText().equals("")&&NmPetugasRalanDokterParamedis.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                }else{
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(3,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(4,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(5,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(8,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(9,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(10,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(11,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(12,"%"+TCari.getText().trim()+"%");
                    ps.setString(13,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(14,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(15,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(16,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(17,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(18,"%"+TCari.getText().trim()+"%");
                    ps.setString(19,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(20,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(21,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(22,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(23,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(24,"%"+TCari.getText().trim()+"%");
                    ps.setString(25,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(26,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(27,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(28,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(29,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(30,"%"+TCari.getText().trim()+"%");
                    ps.setString(31,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(32,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(33,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(34,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(35,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(36,"%"+TCari.getText().trim()+"%");
                    ps.setString(37,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(38,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(39,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(40,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(41,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(42,"%"+TCari.getText().trim()+"%");
                    ps.setString(43,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(44,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(45,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(46,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(47,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(48,"%"+TCari.getText().trim()+"%");
                    ps.setString(49,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(50,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(51,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(52,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(53,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(54,"%"+TCari.getText().trim()+"%");
                    ps.setString(55,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(56,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(57,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(58,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(59,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(60,"%"+TCari.getText().trim()+"%");
                    ps.setString(61,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(62,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(63,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(64,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(65,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(66,"%"+TCari.getText().trim()+"%");
                    ps.setString(67,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(68,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(69,"%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%");
                    ps.setString(70,"%"+KdPetugasRalanDokterParamedis.getText()+NmPetugasRalanDokterParamedis.getText()+"%");
                    ps.setString(71,"%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%");
                    ps.setString(72,"%"+TCari.getText().trim()+"%");
                }   
                    
                rs=ps.executeQuery();
                i=1;
                material=0;bhp=0;jmdokter=0;jmpetugas=0;jmperujuk=0;
                kso=0;menejemen=0;total=0;
                while(rs.next()){
                    material=material+rs.getDouble("bagian_rs");
                    bhp=bhp+rs.getDouble("bhp");
                    jmdokter=jmdokter+rs.getDouble("tarif_tindakan_dokter");
                    jmpetugas=jmpetugas+rs.getDouble("tarif_tindakan_petugas");
                    jmperujuk=jmperujuk+rs.getDouble("tarif_perujuk");
                    kso=kso+rs.getDouble("kso");
                    menejemen=menejemen+rs.getDouble("menejemen");
                    total=total+rs.getDouble("biaya");
                    if(rs.getString("kd_pj").equals("BPJ")){
                        nosep=Sequel.cariIsi("select bridging_sep.no_sep from bridging_sep where bridging_sep.no_rawat='"+rs.getString("no_rawat")+"'");
                    }else{
                        nosep=rs.getString("no_rawat");
                    }
                    if(Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'").equals("")){
                        rawatgabung=rs.getString("no_rawat");
                    }else{
                        rawatgabung=Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2='"+rs.getString(1)+"'");
                    }
                    kodedokter=Sequel.cariIsi("select kd_dokter_remun from maping_dokter_dpjpremun where kd_dokter='"+rs.getString("kd_dokter")+"'");
//                    if(rs.getString("nm_perawatan").contains("usg")||rs.getString("nm_perawatan").contains("konsultasi")||rs.getString("nm_perawatan").contains("visite")||rs.getString("nm_perawatan").contains("ekg")){
//                        jenis="Tindakan Dokter";
//                        kodepetugas=kodedokter;
//                    }else{
//                        jenis="Tindakan Petugas";
//                        kodepetugas="0";
//                    }
                    tabModeRalanDokter.addRow(new Object[]{
                        i,rs.getString("no_rkm_medis"),rawatgabung,nosep,rs.getString("nm_pasien"),"BDRS",rs.getString("kd_jenis_prw"),
                        rs.getString("nm_perawatan"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("nip"),rs.getString("nama"),rs.getString("tgl_periksa"),
                        rs.getString("jam"),rs.getString("png_jawab"),rs.getString("ruangan"),rs.getDouble("bagian_rs"),
                        rs.getDouble("bhp"),rs.getDouble("tarif_tindakan_dokter"),rs.getDouble("kso"),rs.getDouble("menejemen"),
                        rs.getDouble("biaya"),rs.getString("ruangan"),"BDRS","BDRS","1",rs.getDouble("biaya"),rs.getString("nm_perawatan"),kodedokter,kodepetugas
                        
//                        i,rs.getString("no_rawat"),rs.getString("no_rkm_medis"),
//                        rs.getString("nm_pasien"),rs.getString("kd_jenis_prw"),
//                        rs.getString("nm_perawatan"),rs.getString("kd_dokter"),
//                        rs.getString("nm_dokter"),rs.getString("nip"),rs.getString("nama"),
//                        rs.getString("dokter_perujuk"),rs.getString("perujuk"),
//                        rs.getString("tgl_periksa"),rs.getString("jam"),
//                        rs.getString("png_jawab"),rs.getString("ruangan"),rs.getDouble("bagian_rs"),
//                        rs.getDouble("bhp"),rs.getDouble("tarif_tindakan_dokter"),
//                        rs.getDouble("tarif_tindakan_petugas"),rs.getDouble("tarif_perujuk"),
//                        rs.getDouble("kso"),rs.getDouble("menejemen"),rs.getDouble("biaya")
                    });
                    i++;
                }
//                if(total>0){
//                    tabModeRalanDokter.addRow(new Object[]{
//                        "","","","","","","","","","","","","","","","Jumlah Total :",
//                        material,bhp,jmdokter,jmpetugas,jmperujuk,kso,menejemen,total
//                    });
//                }
                if(total>0){
                    tabModeRalanDokter.addRow(new Object[]{
                        "","","","","","","","","","","","","","","","Jumlah Total :",material,
                        bhp,jmdokter,kso,menejemen,total,"","","","","","","",""
                    });
                }
            } catch (Exception e) {
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
    //SAMPAI SINI DETAIL TINDAKAN BDRS
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,65));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }

    private void CetakRalanDokter() {
        if(tabModeRalanDokter.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            TCari.requestFocus();
        }else if(tabModeRalanDokter.getRowCount()!=0){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());                  
            param.put("logo",Sequel.cariGambar("select setting.logo from setting"));
            if(KdDokterRalanDokter.getText().equals("")&&NmDokterRalanDokter.getText().equals("")&&KdPoliRalanDokter.getText().equals("")&&NmPoliRalanDokter.getText().equals("")&&KdCaraBayarRalanDokter.getText().equals("")&&NmCaraBayarRalanDokter.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                Valid.MyReportqry("rptDetailTindakanRalanDokter.jasper","report","::[ Detail Tindakan Ralan Yang Ditangani Dokter ]::",
                   "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                   "pasien.nm_pasien,rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                   "rawat_jl_dr.kd_dokter,dokter.nm_dokter,rawat_jl_dr.tgl_perawatan,"+
                   "rawat_jl_dr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                   "rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.tarif_tindakandr,"+
                   "rawat_jl_dr.kso,rawat_jl_dr.menejemen,rawat_jl_dr.biaya_rawat "+
                   "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                   "inner join rawat_jl_dr on reg_periksa.no_rawat=rawat_jl_dr.no_rawat "+
                   "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                   "inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                   "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                   "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                   "where concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' order by rawat_jl_dr.no_rawat desc",param);
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    Valid.MyReportqry("rptDetailTindakanRalanDokter.jasper","report","::[ Detail Tindakan Ralan Yang Ditangani Dokter ]::",
                        "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                        "pasien.nm_pasien,rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                        "rawat_jl_dr.kd_dokter,dokter.nm_dokter,rawat_jl_dr.tgl_perawatan,"+
                        "rawat_jl_dr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                        "rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.tarif_tindakandr,"+
                        "rawat_jl_dr.kso,rawat_jl_dr.menejemen,rawat_jl_dr.biaya_rawat "+
                        "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join rawat_jl_dr on reg_periksa.no_rawat=rawat_jl_dr.no_rawat "+
                        "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                        "inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                        "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                        "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                        "where concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and rawat_jl_dr.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and jns_perawatan.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and rawat_jl_dr.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' or "+
                        "concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and poliklinik.nm_poli like '%"+TCari.getText().trim()+"%' "+
                       " order by rawat_jl_dr.no_rawat desc",param);
                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
                    Valid.MyReportqry("rptDetailTindakanRalanDokter.jasper","report","::[ Detail Tindakan Ralan Yang Ditangani Dokter ]::",
                        "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                        "pasien.nm_pasien,rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                        "rawat_jl_dr.kd_dokter,dokter.nm_dokter,rawat_jl_dr.tgl_perawatan,"+
                        "rawat_jl_dr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                        "rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.tarif_tindakandr,"+
                        "rawat_jl_dr.kso,rawat_jl_dr.menejemen,rawat_jl_dr.biaya_rawat "+
                        "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join rawat_jl_dr on reg_periksa.no_rawat=rawat_jl_dr.no_rawat "+
                        "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                        "inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                        "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                        "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                        "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                        "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and rawat_jl_dr.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and jns_perawatan.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and rawat_jl_dr.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and poliklinik.nm_poli like '%"+TCari.getText().trim()+"%' "+
                       " order by rawat_jl_dr.no_rawat desc",param);
                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
                    Valid.MyReportqry("rptDetailTindakanRalanDokter.jasper","report","::[ Detail Tindakan Ralan Yang Ditangani Dokter ]::",
                        "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                        "pasien.nm_pasien,rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                        "rawat_jl_dr.kd_dokter,dokter.nm_dokter,rawat_jl_dr.tgl_perawatan,"+
                        "rawat_jl_dr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                        "rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.tarif_tindakandr,"+
                        "rawat_jl_dr.kso,rawat_jl_dr.menejemen,rawat_jl_dr.biaya_rawat "+
                        "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join rawat_jl_dr on reg_periksa.no_rawat=rawat_jl_dr.no_rawat "+
                        "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                        "inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                        "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                        "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                        "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                        "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and rawat_jl_dr.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and jns_perawatan.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and rawat_jl_dr.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and poliklinik.nm_poli like '%"+TCari.getText().trim()+"%' "+
                       " order by rawat_jl_dr.no_rawat desc",param);
                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
                    Valid.MyReportqry("rptDetailTindakanRalanDokter.jasper","report","::[ Detail Tindakan Ralan Yang Ditangani Dokter ]::",
                        "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                        "pasien.nm_pasien,rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                        "rawat_jl_dr.kd_dokter,dokter.nm_dokter,rawat_jl_dr.tgl_perawatan,"+
                        "rawat_jl_dr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                        "rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.tarif_tindakandr,"+
                        "rawat_jl_dr.kso,rawat_jl_dr.menejemen,rawat_jl_dr.biaya_rawat "+
                        "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join rawat_jl_dr on reg_periksa.no_rawat=rawat_jl_dr.no_rawat "+
                        "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                        "inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                        "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                        "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                        "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and rawat_jl_dr.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and jns_perawatan.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and rawat_jl_dr.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and poliklinik.nm_poli like '%"+TCari.getText().trim()+"%' "+
                       " order by rawat_jl_dr.no_rawat desc",param);
                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
                    Valid.MyReportqry("rptDetailTindakanRalanDokter.jasper","report","::[ Detail Tindakan Ralan Yang Ditangani Dokter ]::",
                        "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,"+
                        "pasien.nm_pasien,rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,"+
                        "rawat_jl_dr.kd_dokter,dokter.nm_dokter,rawat_jl_dr.tgl_perawatan,"+
                        "rawat_jl_dr.jam_rawat,penjab.png_jawab,poliklinik.nm_poli, " +
                        "rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.tarif_tindakandr,"+
                        "rawat_jl_dr.kso,rawat_jl_dr.menejemen,rawat_jl_dr.biaya_rawat "+
                        "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join rawat_jl_dr on reg_periksa.no_rawat=rawat_jl_dr.no_rawat "+
                        "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                        "inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                        "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                        "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                        "where reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and rawat_jl_dr.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and jns_perawatan.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and rawat_jl_dr.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' or "+
                        "reg_periksa.status_bayar='Belum Bayar' and concat(rawat_jl_dr.tgl_perawatan,' ',rawat_jl_dr.jam_rawat) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(reg_periksa.kd_poli,poliklinik.nm_poli) like '%"+KdPoliRalanDokter.getText()+NmPoliRalanDokter.getText()+"%' and concat(rawat_jl_dr.kd_dokter,dokter.nm_dokter) like '%"+KdDokterRalanDokter.getText()+NmDokterRalanDokter.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRalanDokter.getText()+NmCaraBayarRalanDokter.getText()+"%' and poliklinik.nm_poli like '%"+TCari.getText().trim()+"%' "+
                       " order by rawat_jl_dr.no_rawat desc",param);
                }
            }
        }
    }
    
    private void tampilKamarInap() {
//        Valid.tabelKosong(tabMode);
        try{
            ps=koneksi.prepareStatement(
               "select kamar_inap.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) as alamat,reg_periksa.p_jawab,reg_periksa.hubunganpj,"+
               "penjab.png_jawab,kamar_inap.kd_kamar,bangsal.nm_bangsal,concat(kamar_inap.kd_kamar,' ',bangsal.nm_bangsal) as kamar,kamar_inap.trf_kamar,kamar_inap.diagnosa_awal,kamar_inap.diagnosa_akhir," +
               "kamar_inap.tgl_masuk,kamar_inap.jam_masuk,if(kamar_inap.tgl_keluar='0000-00-00','',kamar_inap.tgl_keluar) as tgl_keluar,if(kamar_inap.jam_keluar='00:00:00','',kamar_inap.jam_keluar) as jam_keluar,"+
               "kamar_inap.ttl_biaya,kamar_inap.stts_pulang,kamar_inap.lama,reg_periksa.kd_dokter,dokter.nm_dokter,kamar_inap.kd_kamar,reg_periksa.kd_pj,concat(reg_periksa.umurdaftar,' ',reg_periksa.sttsumur)as umur,reg_periksa.status_bayar, "+
               "pasien.agama,pasien.tgl_lahir,pasien.no_ktp from kamar_inap inner join reg_periksa on kamar_inap.no_rawat=reg_periksa.no_rawat inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
               "inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "+
               "inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "+
               "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
               "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj where kamar_inap.tgl_keluar between '"+Valid.SetTgl(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(Tgl2.getSelectedItem()+"")+"' and reg_periksa.status_bayar like '%"+cmbStatus.getSelectedItem().toString().replaceAll("Semua","")+"%' ");
            try {
                rs=ps.executeQuery();
                while(rs.next()){
                    kodedokter=Sequel.cariIsi("select kd_dokter_remun from maping_dokter_dpjpremun where kd_dokter='"+rs.getString("kd_dokter")+"'");
                    Integer ceksep=Sequel.cariInteger("select count(bridging_sep.no_sep) from bridging_sep where bridging_sep.no_rawat='"+rs.getString("no_rawat")+"'");
                    nosep=Sequel.cariIsi("select bridging_sep.no_sep from bridging_sep where bridging_sep.no_rawat='"+rs.getString("no_rawat")+"' and bridging_sep.jnspelayanan='1'");
                    tabModeRalanDokter.addRow(new Object[]{
                        i,rs.getString("no_rkm_medis"),rs.getString("no_rawat"),nosep,rs.getString("nm_pasien"),"Kamar",rs.getString("nm_bangsal"),
                        "Rawat Inap Kelas III (Kamar) @hari","",rs.getString("nm_dokter"),"",rs.getString("nm_dokter"),rs.getString("tgl_masuk"),
                        rs.getString("jam_masuk"),rs.getString("png_jawab"),rs.getString("nm_bangsal"),0,0,0,0,0,
                        rs.getDouble("ttl_biaya"),rs.getString("nm_bangsal"),"Irna III","Kamar","1",rs.getDouble("ttl_biaya"),"Rawat Inap Kelas III (Kamar) @hari",kodedokter,"0"
                    });
                       
                    psanak=koneksi.prepareStatement(
                        "select pasien.no_rkm_medis,pasien.nm_pasien,ranap_gabung.no_rawat2,concat(reg_periksa.umurdaftar,' ',reg_periksa.sttsumur)as umur,pasien.no_peserta, "+
                        "concat(pasien.alamatpj,', ',pasien.kelurahanpj,', ',pasien.kecamatanpj,', ',pasien.kabupatenpj) as alamat, "+
                        "pasien.tgl_lahir,pasien.no_ktp from reg_periksa inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis "+
                        "inner join ranap_gabung on ranap_gabung.no_rawat2=reg_periksa.no_rawat where ranap_gabung.no_rawat=?");            
                    try {
                        psanak.setString(1,rs.getString(1));
                        rs2=psanak.executeQuery();
                        if(rs2.next()){
                            tabModeRalanDokter.addRow(new Object[]{
                                i,rs2.getString("no_rkm_medis"),rs.getString("no_rawat"),nosep,rs2.getString("nm_pasien"),"Kamar",rs.getString("nm_bangsal"),
                                "Rawat Inap Kelas III (Kamar) @hari","",rs.getString("nm_dokter"),"",rs.getString("nm_dokter"),rs.getString("tgl_masuk"),
                                rs.getString("jam_masuk"),rs.getString("png_jawab"),rs.getString("nm_bangsal"),0,0,0,0,0,
                                rs.getDouble("ttl_biaya")/2,rs.getString("nm_bangsal"),"Irna III","Kamar","1",rs.getDouble("ttl_biaya")/2,"Rawat Inap Kelas III (Kamar) @hari",kodedokter,"0"
                            });
                        }
                    }catch(Exception ex){
                        System.out.println("Notifikasi : "+ex);
                    }finally{
                          if(rs2 != null){
                              rs2.close();
                          }
                          if(psanak != null){
                              psanak.close();
                          }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }
}
