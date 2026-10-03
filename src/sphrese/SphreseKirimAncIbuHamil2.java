/*
  by Mas Elkhanza
 */

package sphrese;

import bridging.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import static io.grpc.Context.key;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import static okhttp3.Cache.key;
import static org.primefaces.behavior.confirm.ConfirmBehavior.PropertyKeys.header;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 *
 * @author dosen
 */
public final class SphreseKirimAncIbuHamil2 extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private PreparedStatement ps;
    private ResultSet rs;   
    private int i=0;
    private String link="",json="",iddokter="",idpasien="",idepisode="";
    private String key,clientid,urlauth,token;
    private ApiSphrese api=new ApiSphrese();
    private HttpHeaders headers ;
    private HttpEntity requestEntity;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode response;
    private SatuSehatCekNIK cekViaSatuSehat=new SatuSehatCekNIK();  
    private StringBuilder htmlContent;    
    private long millis;
    private SSLContext sslContext;
    private SSLSocketFactory sslFactory;
    private Scheme scheme;
    private HttpComponentsClientHttpRequestFactory factory;
    private ApiBPJSAesKeySpec mykey;
    private HttpHeaders header ;
    
    /** Creates new form DlgKamar
     * @param parent
     * @param modal */
    public SphreseKirimAncIbuHamil2(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(10,2);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new String[]{
                "P","Tanggal Registrasi","No.Rawat","No.RM","Nama Pasien","No.KTP Pasien","Kode Dokter","Nama Dokter",
                "No.KTP Dokter","Kode Poli","Nama Poli/Unit","No. HP","Stts Rawat","Stts Lanjut",
                "Tanggal Pulang","Job ID","Tanggal Lahir","Alamat","KD Prov","Kd Kab","Kd Kec","Kd Kel","Tipe Nakes"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                 java.lang.String.class, java.lang.String.class, java.lang.String.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbObat.setModel(tabMode);

        //tbKamar.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbKamar.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 23; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(150);
            }else if(i==2){
                column.setPreferredWidth(105);
            }else if(i==3){
                column.setPreferredWidth(70);
            }else if(i==4){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setPreferredWidth(110);
            }else if(i==6){
                column.setPreferredWidth(80);
            }else if(i==7){
                column.setPreferredWidth(150);
            }else if(i==8){
                column.setPreferredWidth(110);
            }else if(i==9){
                column.setPreferredWidth(80);
            }else if(i==10){
                column.setPreferredWidth(140);
            }else if(i==11){
                column.setPreferredWidth(70);
            }else if(i==12){
                column.setPreferredWidth(63);
            }else if(i==13){
                column.setPreferredWidth(63);
            }else if(i==14){
                column.setPreferredWidth(150);
            }else if(i==15){
                column.setPreferredWidth(215);
            }else{
                column.setPreferredWidth(100);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());
        
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
        
        try {
            link=koneksiDB.URLFHIRSATUSEHAT();
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }  
        
        try {
            key = koneksiDB.KEYSPHRESE();
            clientid = koneksiDB.CLIENTIDSPHRESE();
            urlauth = koneksiDB.URLAUTHSPHRESE();
        } catch (Exception ex) {
            System.out.println("Notifikasi : "+ex);
        }
        
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
        ppPilihSemua = new javax.swing.JMenuItem();
        ppBersihkan = new javax.swing.JMenuItem();
        LoadHTML = new widget.editorpane();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnAll = new widget.Button();
        BtnKirim = new widget.Button();
        BtnUpdate = new widget.Button();
        BtnPrint = new widget.Button();
        BtnKeluar = new widget.Button();
        BtnUpdate1 = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel15 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel17 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel16 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        jLabel18 = new widget.Label();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        ppPilihSemua.setBackground(new java.awt.Color(255, 255, 254));
        ppPilihSemua.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppPilihSemua.setForeground(new java.awt.Color(50, 50, 50));
        ppPilihSemua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppPilihSemua.setText("Pilih Semua");
        ppPilihSemua.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppPilihSemua.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppPilihSemua.setName("ppPilihSemua"); // NOI18N
        ppPilihSemua.setPreferredSize(new java.awt.Dimension(150, 26));
        ppPilihSemua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppPilihSemuaActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppPilihSemua);

        ppBersihkan.setBackground(new java.awt.Color(255, 255, 254));
        ppBersihkan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBersihkan.setForeground(new java.awt.Color(50, 50, 50));
        ppBersihkan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppBersihkan.setText("Hilangkan Pilihan");
        ppBersihkan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBersihkan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBersihkan.setName("ppBersihkan"); // NOI18N
        ppBersihkan.setPreferredSize(new java.awt.Dimension(150, 26));
        ppBersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBersihkanActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppBersihkan);

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(null);
        setIconImages(null);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pengiriman Data ANC Sphrese ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setComponentPopupMenu(jPopupMenu1);
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbObat.setComponentPopupMenu(jPopupMenu1);
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
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

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(53, 23));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass8.add(LCount);

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

        BtnKirim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/34.png"))); // NOI18N
        BtnKirim.setMnemonic('K');
        BtnKirim.setText("Kirim");
        BtnKirim.setToolTipText("Alt+K");
        BtnKirim.setName("BtnKirim"); // NOI18N
        BtnKirim.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKirimActionPerformed(evt);
            }
        });
        panelGlass8.add(BtnKirim);

        BtnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/edit_f2.png"))); // NOI18N
        BtnUpdate.setMnemonic('U');
        BtnUpdate.setText("Update");
        BtnUpdate.setToolTipText("Alt+U");
        BtnUpdate.setName("BtnUpdate"); // NOI18N
        BtnUpdate.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUpdateActionPerformed(evt);
            }
        });
        panelGlass8.add(BtnUpdate);

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
        panelGlass8.add(BtnPrint);

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

        BtnUpdate1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/edit_f2.png"))); // NOI18N
        BtnUpdate1.setMnemonic('U');
        BtnUpdate1.setText("cek token");
        BtnUpdate1.setToolTipText("Alt+U");
        BtnUpdate1.setName("BtnUpdate1"); // NOI18N
        BtnUpdate1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnUpdate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUpdate1ActionPerformed(evt);
            }
        });
        panelGlass8.add(BtnUpdate1);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel15.setText("Tgl.Registrasi :");
        jLabel15.setName("jLabel15"); // NOI18N
        jLabel15.setPreferredSize(new java.awt.Dimension(85, 23));
        panelGlass9.add(jLabel15);

        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "19-07-2026" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari1);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("s.d.");
        jLabel17.setName("jLabel17"); // NOI18N
        jLabel17.setPreferredSize(new java.awt.Dimension(24, 23));
        panelGlass9.add(jLabel17);

        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "19-07-2026" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel16.setText("Key Word :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel16);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(210, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('6');
        BtnCari.setToolTipText("Alt+6");
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

        jLabel18.setText("Key Word :");
        jLabel18.setName("jLabel18"); // NOI18N
        jLabel18.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel18);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnPrint,BtnKeluar);}
    }//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
            htmlContent = new StringBuilder();
            htmlContent.append(                             
                "<tr class='isi'>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Tanggal Registrasi</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>No.Rawat</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>No.RM</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Nama Pasien</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>No.KTP Pasien</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Kode Dokter</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Nama Dokter</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>No.KTP Dokter</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Kode Poli</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Nama Poli/Unit</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>ID Lokasi Unit</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Stts Rawat</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Stts Lanjut</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Tanggal Pulang</b></td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>ID Encounter</b></td>"+
                "</tr>"
            );
            for (i = 0; i < tabMode.getRowCount(); i++) {
                htmlContent.append(
                    "<tr class='isi'>"+
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
                    "</tr>");
            }
            LoadHTML.setText(
                "<html>"+
                  "<table width='100%' border='0' align='center' cellpadding='1px' cellspacing='0' class='tbl_form'>"+
                   htmlContent.toString()+
                  "</table>"+
                "</html>"
            );
            htmlContent=null;

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

            File f = new File("DataSatuSehatEncounter.html");            
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));            
            bw.write(LoadHTML.getText().replaceAll("<head>","<head>"+
                        "<link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" />"+
                        "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                            "<tr class='isi2'>"+
                                "<td valign='top' align='center'>"+
                                    "<font size='4' face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                                    akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br>"+
                                    akses.getkontakrs()+", E-mail : "+akses.getemailrs()+"<br><br>"+
                                    "<font size='2' face='Tahoma'>DATA PENGIRIMAN SATU SEHAT ENCOUNTER<br><br></font>"+        
                                "</td>"+
                           "</tr>"+
                        "</table>")
            );
            bw.close();                         
            Desktop.getDesktop().browse(f.toURI());
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        this.setCursor(Cursor.getDefaultCursor());       
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            tbObat.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        tampil();
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt,TCari,BtnPrint);
        }
    }//GEN-LAST:event_BtnCariKeyPressed

    private void BtnKirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKirimActionPerformed
//        for(i=0;i<tbObat.getRowCount();i++){
            try {
                // STEP 1: Generate Token
                URL url = new URL("https://spheres-sim-rs-api-dev-571066868581.asia-southeast2.run.app/v1/auth/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonInputString = "{"
                            + "\"client_id\":\"rsud-awet-muda-narmada-dev\","
                            + "\"client_secret\":\"6hzw#0]}22i-QdXjBvJ06iV(@2-7(P8E9[h4HM+VjB$4n07(_4\""
                            + "}";

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                br.close();

                // Anggap response JSON berisi {"access_token":"..."}
                String token = response.toString();
                System.out.println("Access Token: " + token);
                    int start = token.indexOf("\"access_token\":\"") + 16;
                    int end = token.indexOf("\"", start);
                    String accessToken = token.substring(start, end);
                System.out.println(accessToken);

                // STEP 2: Gunakan token untuk request lain
                URL url2 = new URL("https://spheres-sim-rs-api-dev-571066868581.asia-southeast2.run.app/v1/form/anc-ibu-hamil");
                HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                conn2.setRequestMethod("POST");
                conn2.setRequestProperty("Content-Type", "application/json");
                conn2.setRequestProperty("Authorization", "Bearer " + accessToken);
                conn2.setDoOutput(true);
                    String hpht=Sequel.cariIsi("SELECT hpht FROM penilaian_awal_keperawatan_kebidanan inner join reg_periksa on reg_periksa.no_rawat=penilaian_awal_keperawatan_kebidanan.no_rawat inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where pasien.no_rkm_medis='"+tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString()+"' order by hpht desc limit 1");
                    String hpl=Sequel.cariIsi("SELECT tp FROM penilaian_awal_keperawatan_kebidanan inner join reg_periksa on reg_periksa.no_rawat=penilaian_awal_keperawatan_kebidanan.no_rawat inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where pasien.no_rkm_medis='"+tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString()+"' order by tanggal desc limit 1");
                    String bb=Sequel.cariIsi("SELECT bb FROM penilaian_awal_keperawatan_kebidanan inner join reg_periksa on reg_periksa.no_rawat=penilaian_awal_keperawatan_kebidanan.no_rawat inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where pasien.no_rkm_medis='"+tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString()+"' order by tanggal asc limit 1");
                    String bb1=Sequel.cariIsi("SELECT bb FROM penilaian_awal_keperawatan_kebidanan inner join reg_periksa on reg_periksa.no_rawat=penilaian_awal_keperawatan_kebidanan.no_rawat inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where pasien.no_rkm_medis='"+tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString()+"' order by tanggal desc limit 1");
                    String tb=Sequel.cariIsi("SELECT tb FROM penilaian_awal_keperawatan_kebidanan inner join reg_periksa on reg_periksa.no_rawat=penilaian_awal_keperawatan_kebidanan.no_rawat inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where pasien.no_rkm_medis='"+tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString()+"' order by tanggal desc limit 1");
                    String diagnosa=Sequel.cariIsi("select diagnosa_pasien.kd_penyakit from diagnosa_pasien inner join reg_periksa on reg_periksa.no_rawat=diagnosa_pasien.no_rawat where diagnosa_pasien.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(), 2).toString()+"'");
                    String tatalaksana=Sequel.cariIsi("SELECT tindakan FROM penilaian_awal_keperawatan_kebidanan inner join reg_periksa on reg_periksa.no_rawat=penilaian_awal_keperawatan_kebidanan.no_rawat inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where pasien.no_rkm_medis='"+tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString()+"' order by tanggal desc limit 1");
                    String tatalaksanaobat=Sequel.cariIsi("select GROUP_CONCAT(databarang.nama_brng,' ',resep_dokter.jml,' ',resep_dokter.aturan_pakai,' - ') from databarang inner join resep_dokter on resep_dokter.kode_brng=databarang.kode_brng inner join resep_obat on resep_obat.no_resep=resep_dokter.no_resep inner join reg_periksa on reg_periksa.no_rawat=resep_obat.no_rawat where resep_obat.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(), 2).toString()+"'");
                    String komplikasi=Sequel.cariIsi("SELECT komplikasi FROM penilaian_awal_keperawatan_kebidanan inner join reg_periksa on reg_periksa.no_rawat=penilaian_awal_keperawatan_kebidanan.no_rawat inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where pasien.no_rkm_medis='"+tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString()+"' order by tanggal desc limit 1");
                    String imunisasitt=Sequel.cariIsi("SELECT imunisasi FROM penilaian_awal_keperawatan_kebidanan inner join reg_periksa on reg_periksa.no_rawat=penilaian_awal_keperawatan_kebidanan.no_rawat inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where pasien.no_rkm_medis='"+tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString()+"' order by tanggal desc limit 1");
                    String riwayat_imunisasitt=Sequel.cariIsi("SELECT ket_imunisasi FROM penilaian_awal_keperawatan_kebidanan inner join reg_periksa on reg_periksa.no_rawat=penilaian_awal_keperawatan_kebidanan.no_rawat inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where pasien.no_rkm_medis='"+tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString()+"' order by tanggal desc limit 1");
                    String sistol=Sequel.cariIsi("select left(tensi,3) from pemeriksaan_ralan where no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(), 2).toString()+"' limit 1");
                    String diasistol=Sequel.cariIsi("select right(tensi,2) from pemeriksaan_ralan where no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(), 2).toString()+"' limit 1");
                    String lila=Sequel.cariIsi("SELECT lila FROM penilaian_awal_keperawatan_kebidanan inner join reg_periksa on reg_periksa.no_rawat=penilaian_awal_keperawatan_kebidanan.no_rawat inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where pasien.no_rkm_medis='"+tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString()+"' order by tanggal desc limit 1");
                    String usia_kehamilan=Sequel.cariIsi("SELECT ifnull(usia_kehamilan,'1') FROM penilaian_awal_keperawatan_kebidanan inner join reg_periksa on reg_periksa.no_rawat=penilaian_awal_keperawatan_kebidanan.no_rawat inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where pasien.no_rkm_medis='"+tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString()+"' order by tanggal desc limit 1");
                // Contoh body ANC (disingkat)
                String ancJson="[\n" +
"  {\n" +
"    \"nama_ibu\": \""+tbObat.getValueAt(tbObat.getSelectedRow(), 4)+"\",\n" +
"    \"nik_ibu\": \""+tbObat.getValueAt(tbObat.getSelectedRow(), 5)+"\",\n" +
"    \"no_hp_ibu\": \""+tbObat.getValueAt(tbObat.getSelectedRow(), 11)+"\",\n" +
"    \"tgl_lahir_ibu\": \""+tbObat.getValueAt(tbObat.getSelectedRow(), 16)+"\",\n" +
"    \"alamat_ibu\": \""+tbObat.getValueAt(tbObat.getSelectedRow(), 17)+"\",\n" +
"    \"id_prov_ibu\": \""+Integer.parseInt(tbObat.getValueAt(tbObat.getSelectedRow(), 18).toString())+"\",\n" +
"    \"id_kab_ibu\": \""+Integer.parseInt(tbObat.getValueAt(tbObat.getSelectedRow(), 19).toString())+"\",\n" +
"    \"id_kec_ibu\": \""+Integer.parseInt(tbObat.getValueAt(tbObat.getSelectedRow(), 20).toString())+"\",\n" +
"    \"id_desa_ibu\": \""+tbObat.getValueAt(tbObat.getSelectedRow(), 21).toString()+"\",\n" +
"    \"tempat_pelayanan_anc\": \"Poli Obgyn\",\n" +
"    \"tipe_nakes\": \"DSO\",\n" +
"    \"nama_nakes\": \"dr. Ida Bagus Yuda Andika, M.Biomed, Sp.OG\",\n" +
"    \"nik_nakes\": \"1807221804840001\",\n" +
"    \"gender_nakes\": \"L\",\n" +
"    \"tgl_anc\": \"2023-10-27\",\n" +
"    \"memiliki_buku_kia\": true,\n" +
"    \"hpht\": \"2023-03-20\",\n" +
"    \"hpl\": \"2023-12-27\",\n" +
"    \"bb_sebelum_hamil\": 55,\n" +
"    \"tb\": 160,\n" +
"    \"kode_icd_diagnosa\": \"Z34.0\",\n" +
"    \"tatalaksana\": \"Pemberian suplemen zat besi\",\n" +
"    \"tatalaksana_obat\": \"Asam Folat 1x1, Tablet Fe 1x1\",\n" +
"    \"komplikasi\": \"Anemia ringan\",\n" +
"    \"imt_sebelum_hamil\": 21.48,\n" +
"    \"dilakukan_imunisasi_tt\": true,\n" +
"    \"riwayat_imunisasi_tt\": 2,\n" +
"    \"pemeriksaan_anc_pertama\": false,\n" +
"    \"usia_kehamilan\": 12,\n" +
"    \"trimester\": 1,\n" +
"    \"bb\": 57,\n" +
"    \"sistol\": 110,\n" +
"    \"diastol\": 70,\n" +
"    \"lila\": 25,\n" +
"    \"status_gizi_kek\": \"TK\",\n" +
"    \"tfu\": 10,\n" +
"    \"lab_goldar\": \"A\",\n" +
"    \"lab_rhesus\": \"P\",\n" +
"    \"bpm_janin\": 140,\n" +
"    \"presentasi_janin\": \"K\",\n" +
"    \"komplikasi_kehamilan\": \"L\",\n" +
"    \"pemeriksaan_usg\": \"DTN\",\n" +
"    \"deskripsi_pemeriksaan_usg\": \"Suspek Plasenta Previa\",\n" +
"    \"gravida\": 2,\n" +
"    \"partus\": 1,\n" +
"    \"abortus\": 0,\n" +
"    \"lab_hiv\": \"NR\",\n" +
"    \"lab_hepatitis\": \"N\",\n" +
"    \"lab_siphilis\": \"N\",\n" +
"    \"lab_protein_urine\": \"N\",\n" +
"    \"lab_hb\": 11.5,\n" +
"    \"lab_gds\": 90,\n" +
"    \"tatalaksana_anemia\": \"TTD\",\n" +
"    \"pre_eklampsia\": \"TPEE\",\n" +
"    \"tatalaksana_pre_eklampsia\": \"TD\",\n" +
"    \"tatalaksana_kek\": \"PM\",\n" +
"    \"risiko_msl_kesehatan\": \"L\",\n" +
"    \"tatalaksana_risiko_msl_kesehatan\": true,\n" +
"    \"diberikan_konseling\": true,\n" +
"    \"dilakukan_skrinning_keswa\": true,\n" +
"    \"jarak_dgn_hamil_sblmnya\": 24,\n" +
"    \"jml_janin\": 1,\n" +
"    \"ibu_dm\": false,\n" +
"    \"ibu_hiv\": false,\n" +
"    \"ibu_hep_b\": false,\n" +
"    \"nilai_hasil_pemeriksaan_ljt_hep\": 100,\n" +
"    \"hasil_pemeriksaan_ljt_hep\": \"N\",\n" +
"    \"ibu_sifilis\": false,\n" +
"    \"ibu_malaria\": false,\n" +
"    \"ibu_tbc\": false,\n" +
"    \"ibu_terapi_art\": false,\n" +
"    \"obat_malaria\": \"AR\",\n" +
"    \"obat_tbc\": \"R\",\n" +
"    \"konsumsi_obat_hep\": false,\n" +
"    \"konsumsi_obat_dm\": false\n" +
"  }\n" +
"]";

                try (OutputStream os = conn2.getOutputStream()) {
                    byte[] input = ancJson.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                BufferedReader br2 = new BufferedReader(new InputStreamReader(conn2.getInputStream(), "utf-8"));
                StringBuilder response2 = new StringBuilder();
                while ((line = br2.readLine()) != null) {
                    response2.append(line.trim());
                }
                br2.close();
                
                int mulai = response2.indexOf("\"job_id\":\"") + 10;
                int selesai = response2.indexOf("\"", start);
                String jobId = response2.substring(mulai, selesai);
                
                if(!response2.toString().equals("")){
//                    idepisode=response.asText();
                    Sequel.menyimpan("sphrese_anc","?,?","No.Rawat",2,new String[]{
                        tbObat.getValueAt(tbObat.getSelectedRow(),2).toString(),jobId
                    });
                    JOptionPane.showMessageDialog(null,"Kirim data ANC Ibu Hamil ke Sphrese Berhasil!");
                }
                System.out.println("Response ANC: " + response2.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
    }//GEN-LAST:event_BtnKirimActionPerformed

    private void ppPilihSemuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppPilihSemuaActionPerformed
        for(i=0;i<tbObat.getRowCount();i++){
            tbObat.setValueAt(true,i,0);
        }
    }//GEN-LAST:event_ppPilihSemuaActionPerformed

    private void ppBersihkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBersihkanActionPerformed
        for(i=0;i<tbObat.getRowCount();i++){
            tbObat.setValueAt(false,i,0);
        }
    }//GEN-LAST:event_ppBersihkanActionPerformed

    private void BtnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUpdateActionPerformed
        for(i=0;i<tbObat.getRowCount();i++){
            if(tbObat.getValueAt(i,0).toString().equals("true")&&(!tbObat.getValueAt(i,5).toString().equals(""))&&(!tbObat.getValueAt(i,8).toString().equals(""))&&(!tbObat.getValueAt(i,15).toString().equals(""))){
                try {
                    iddokter=cekViaSatuSehat.tampilIDParktisi(tbObat.getValueAt(i,8).toString());
                    idpasien=cekViaSatuSehat.tampilIDPasien(tbObat.getValueAt(i,5).toString());
                    try{
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("Authorization", "Bearer "+api.TokenSphrese());
                        json = "{" +
                                    "\"resourceType\": \"Encounter\"," +
                                    "\"id\": \""+tbObat.getValueAt(i,15).toString()+"\"," +
                                    "\"identifier\": [" +
                                        "{" +
                                            "\"system\": \"http://sys-ids.kemkes.go.id/encounter/"+koneksiDB.IDSATUSEHAT()+"\"," +
                                            "\"value\": \""+tbObat.getValueAt(i,2).toString()+"\"" +
                                        "}" +
                                    "]," +
                                    "\"status\": \"arrived\"," +
                                    "\"class\": {" +
                                        "\"system\": \"http://terminology.hl7.org/CodeSystem/v3-ActCode\"," +
                                        "\"code\": \""+(tbObat.getValueAt(i,11).toString().equals("Ralan")?"AMB":"IMP")+"\"," +
                                        "\"display\": \""+(tbObat.getValueAt(i,11).toString().equals("Ralan")?"ambulatory":"inpatient encounter")+"\"" +
                                    "}," +
                                    "\"subject\": {" +
                                        "\"reference\": \"Patient/"+idpasien+"\"," +
                                        "\"display\": \""+tbObat.getValueAt(i,4).toString()+"\"" +
                                    "}," +
                                    "\"participant\": [" +
                                        "{" +
                                            "\"type\": [" +
                                                "{" +
                                                    "\"coding\": [" +
                                                        "{" +
                                                            "\"system\": \"http://terminology.hl7.org/CodeSystem/v3-ParticipationType\"," +
                                                            "\"code\": \"ATND\"," +
                                                            "\"display\": \"attender\"" +
                                                        "}" +
                                                    "]" +
                                                "}" +
                                            "]," +
                                            "\"individual\": {" +
                                                "\"reference\": \"Practitioner/"+iddokter+"\"," +
                                                "\"display\": \""+tbObat.getValueAt(i,7).toString()+"\"" +
                                            "}" +
                                        "}" +
                                    "]," +
                                    "\"period\": {" +
                                        "\"start\": \""+tbObat.getValueAt(i,1).toString()+"\"" +
                                    "}," +
                                    "\"location\": [" +
                                        "{" +
                                            "\"location\": {" +
                                                "\"reference\": \"Location/"+tbObat.getValueAt(i,11).toString()+"\"," +
                                                "\"display\": \""+tbObat.getValueAt(i,10).toString()+"\"" +
                                            "}" +
                                        "}" +
                                    "]," +
                                    "\"statusHistory\": [" +
                                        "{" +
                                            "\"status\": \"arrived\"," +
                                            "\"period\": {" +
                                                "\"start\": \""+tbObat.getValueAt(i,1).toString()+"\"," +
                                                "\"end\": \""+tbObat.getValueAt(i,14).toString()+"\"" +
                                            "}" +
                                        "}" +
                                    "]," +
                                    "\"serviceProvider\": {" +
                                        "\"reference\": \"Organization/"+koneksiDB.IDSATUSEHAT()+"\"" +
                                    "}" +
                                "}";
                        System.out.println("URL : "+link+"/Encounter/"+tbObat.getValueAt(i,15).toString());
                        System.out.println("Request JSON : "+json);
                        requestEntity = new HttpEntity(json,headers);
                        json=api.getRest().exchange(link+"/Encounter/"+tbObat.getValueAt(i,15).toString(), HttpMethod.PUT, requestEntity, String.class).getBody();
                        System.out.println("Result JSON : "+json);
                        tbObat.setValueAt(false,i,0);
                    }catch(Exception e){
                        System.out.println("Notifikasi Bridging : "+e);
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                }
            }
        }
    }//GEN-LAST:event_BtnUpdateActionPerformed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            TCari.setText("");
            tampil();
        }else{
            Valid.pindah(evt, BtnPrint, BtnKeluar);
        }
    }//GEN-LAST:event_BtnAllKeyPressed

    private void BtnUpdate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUpdate1ActionPerformed
        try {
                // URL endpoint API
                URL url = new URL("https://spheres-sim-rs-api-dev-571066868581.asia-southeast2.run.app/v1/auth/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set method POST
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // JSON body sesuai dokumentasi
                String jsonInputString = "{"
                        + "\"client_id\":\"rsud-awet-muda-narmada-dev\","
                        + "\"client_secret\":\"6hzw#0]}22i-QdXjBvJ06iV(@2-7(P8E9[h4HM+VjB$4n07(_4\""
                        + "}";

                // Kirim body request
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Baca response
                int code = conn.getResponseCode();
                System.out.println("Response Code: " + code);

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response: " + response.toString());
                }
                
                JSONObject token2=new JSONObject(response);
                String accessToken = token2.getString("access_token");
                System.out.println("Access Token: " + accessToken);

            } catch (Exception e) {
                e.printStackTrace();
            }
    }//GEN-LAST:event_BtnUpdate1ActionPerformed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        jLabel18.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 21).toString());
    }//GEN-LAST:event_tbObatMouseClicked

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            SphreseKirimAncIbuHamil2 dialog = new SphreseKirimAncIbuHamil2(new javax.swing.JFrame(), true);
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
    private widget.Button BtnKirim;
    private widget.Button BtnPrint;
    private widget.Button BtnUpdate;
    private widget.Button BtnUpdate1;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.Label LCount;
    private widget.editorpane LoadHTML;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel7;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private javax.swing.JMenuItem ppBersihkan;
    private javax.swing.JMenuItem ppPilihSemua;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables
    private void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            ps=koneksi.prepareStatement(
                   "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,pasien.no_tlp,reg_periksa.no_rkm_medis,reg_periksa.kd_poli,"+
                   "reg_periksa.stts,pasien.tgl_lahir,pasien.alamat,pasien.kd_prop,pasien.kd_kab,pasien.kd_kec,pasien.kd_kel,dokter.jk,"+
                   "pegawai.nama,pegawai.no_ktp as ktpdokter,poliklinik.nm_poli,reg_periksa.kd_dokter,"+
                   "reg_periksa.status_lanjut,concat(reg_periksa.tgl_registrasi,'T',reg_periksa.jam_reg,'+07:00') as pulang,ifnull(sphrese_anc.job_id,'') as job_id "+
                   "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join pegawai on pegawai.nik=reg_periksa.kd_dokter "+
                   "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join satu_sehat_mapping_lokasi_ralan on satu_sehat_mapping_lokasi_ralan.kd_poli=poliklinik.kd_poli "+
                   "left join sphrese_anc on sphrese_anc.no_rawat=reg_periksa.no_rawat "+
                   "inner join dokter on dokter.kd_dokter=reg_periksa.kd_dokter "+
                   "where reg_periksa.kd_poli='U0001' and reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.tgl_registrasi between ? and ? "+
                   (TCari.getText().equals("")?"":"and (reg_periksa.no_rawat like ? or reg_periksa.no_rkm_medis like ? or "+
                   "pasien.nm_pasien like ? or pasien.no_ktp like ? or pegawai.nama like ? or poliklinik.nm_poli like ? or "+
                   "reg_periksa.stts like ? or reg_periksa.status_lanjut like ?)"));
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                    ps.setString(10,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new Object[]{
                        false,rs.getString("tgl_registrasi"),rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("no_ktp"),rs.getString("kd_dokter"),rs.getString("nama"),rs.getString("ktpdokter"),rs.getString("kd_poli"),rs.getString("nm_poli"),
                        rs.getString("no_tlp"),rs.getString("stts"),rs.getString("status_lanjut"),rs.getString("pulang"),rs.getString("job_id"),
                        rs.getString("tgl_lahir"),rs.getString("alamat"),rs.getString("kd_prop"),rs.getString("kd_kab"),rs.getString("kd_kec"),rs.getString("kd_kel"),"DSO"
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

    public void isCek(){
        BtnKirim.setEnabled(akses.getsatu_sehat_kirim_encounter());
        BtnUpdate.setEnabled(akses.getsatu_sehat_kirim_encounter());
        BtnPrint.setEnabled(akses.getsatu_sehat_kirim_encounter());
    }
    
    public JTable getTable(){
        return tbObat;
    }
    
    
    public RestTemplate getRest() throws NoSuchAlgorithmException, KeyManagementException {
        sslContext = SSLContext.getInstance("TLSv1.2");
        TrustManager[] trustManagers= {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {return null;}
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)throws CertificateException {}
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)throws CertificateException {}
            }
        };
        sslContext.init(null,trustManagers , new SecureRandom());
        sslFactory=new SSLSocketFactory(sslContext,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        scheme=new Scheme("https",443,sslFactory);
        factory=new HttpComponentsClientHttpRequestFactory();
        factory.getHttpClient().getConnectionManager().getSchemeRegistry().register(scheme);
        return new RestTemplate(factory);
    }
    
}