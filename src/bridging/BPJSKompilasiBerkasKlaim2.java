package bridging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import fungsi.akses;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import static javafx.concurrent.Worker.State.FAILED;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import laporan.DlgBerkasRawat;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.HtmlUtils;
import rekammedis.RMRiwayatPerawatan;
import simrskhanza.DlgCariCaraBayar;
import simrskhanza.DlgCariPeriksaLab;
import simrskhanza.DlgCariPeriksaRadiologi;
import simrskhanza.DlgKompilasiBerkasKlaimBPJS;
import static simrskhanza.DlgKompilasiBerkasKlaimBPJS.deleteFilesInFolder;
import static simrskhanza.DlgKompilasiBerkasKlaimBPJS.mergePDFsFromFolder;
import surat.SuratKontrol;
import org.json.JSONArray;
import org.json.JSONObject;

public class BPJSKompilasiBerkasKlaim2 extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private final Connection koneksi = koneksiDB.condb();
    private final sekuel Sequel = new sekuel();
    private final validasi Valid = new validasi();
    private final JFXPanel jfxPanelicare = new JFXPanel();
    private final JFXPanel jfxinvoices = new JFXPanel();
    private PreparedStatement ps, ps2,ps3, psambiltindakan, psambillab, psambilrad;
    private ResultSet rs, rs2,rs3, rsambiltindakan, rsambillab, rsambilrad;
    private String masalahkeperawatan = "",tanggal="",datatriase="",pilihan="";
    private int i = 0,cekberkasdigital=0;
    private final Properties prop = new Properties();
    private final DlgCariCaraBayar penjab = new DlgCariCaraBayar(null, false);
    private RMRiwayatPerawatan resume = null;
    private WebEngine engine;
    private final String KOMPILASIBERKASGUNAKANRIWAYATPASIEN = koneksiDB.KOMPILASIBERKASGUNAKANRIWAYATPASIEN(),
                         KODEPPKBPJS = Sequel.cariIsiSmc("select setting.kode_ppk from setting limit 1") + "%";
    private String finger = "", tanggalExport = "",
                   KOMPILASIBERKASGUNAKANTANGGALEXPORT = koneksiDB.KOMPILASIBERKASGUNAKANTANGGALEXPORT(),
                   KOMPILASIBERKASAPLIKASIPDF = koneksiDB.KOMPILASIBERKASAPLIKASIPDF();
    private boolean exportSukses = true;
    private long KOMPILASIBERKASMAXMEMORY = koneksiDB.KOMPILASIBERKASMAXMEMORY();
    public  ApiINACBG inacbg = new ApiINACBG();

    public BPJSKompilasiBerkasKlaim2(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        tabMode = new DefaultTableModel(null, new Object[] {
            "No. Rawat", "No. SEP", "No. RM", "Nama Pasien", "Status",
            "Tgl. SEP", "Tgl. Pulang", "Stts. Pulang", "Ruangan", "DPJP",
            "Diagnosa", "Status INACBG", "KirimINACBG"
        }) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return java.lang.String.class;
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        
        tbKompilasiKompi1.setModel(tabMode);
        tbKompilasiKompi1.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbKompilasiKompi1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbKompilasiKompi1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tbKompilasiKompi1.getColumnModel().getColumn(0).setPreferredWidth(10);
        tbKompilasiKompi1.getColumnModel().getColumn(1).setPreferredWidth(126);
        tbKompilasiKompi1.getColumnModel().getColumn(2).setPreferredWidth(46);
        tbKompilasiKompi1.getColumnModel().getColumn(3).setPreferredWidth(200);
        tbKompilasiKompi1.getColumnModel().getColumn(4).setPreferredWidth(50);
        tbKompilasiKompi1.getColumnModel().getColumn(5).setPreferredWidth(80);
        tbKompilasiKompi1.getColumnModel().getColumn(6).setPreferredWidth(70);
        tbKompilasiKompi1.getColumnModel().getColumn(7).setPreferredWidth(90);
        tbKompilasiKompi1.getColumnModel().getColumn(8).setPreferredWidth(180);
        tbKompilasiKompi1.getColumnModel().getColumn(9).setPreferredWidth(140);
        tbKompilasiKompi1.getColumnModel().getColumn(10).setPreferredWidth(70);
        tbKompilasiKompi1.getColumnModel().getColumn(11).setPreferredWidth(100);
        tbKompilasiKompi1.getColumnModel().getColumn(12).setMinWidth(0);
        tbKompilasiKompi1.getColumnModel().getColumn(12).setMaxWidth(0);
        
        tbKompilasiKompi1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row % 2 == 1) {
                    component.setBackground(new Color(255, 244, 244));
                    component.setForeground(new Color(50, 50, 50));
                } else {
                    component.setBackground(new Color(255, 255, 255));
                    component.setForeground(new Color(50, 50, 50));
                }
                if (table.getValueAt(row, 12).toString().equals("1")) {
                    component.setBackground(new Color(50, 50, 50));
                    component.setForeground(new Color(255, 255, 255));
                }
                return component;
            }
        });
        
        

        if (koneksiDB.CARICEPAT().equals("aktif")) {
            TCariKompi1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (TCariKompi1.getText().length() > 2) {
                        tampil();
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (TCariKompi1.getText().length() > 2) {
                        tampil();
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (TCariKompi1.getText().length() > 2) {
                        tampil();
                    }
                }
            });
        }
        
        panelDiagnosaSmcKompi1.getTabbedPane().addChangeListener((ChangeEvent e) -> {
            if (((JTabbedPane) e.getSource()).getSelectedIndex() == 0) {
                BtnSimpanDiagnosaKompi1.setEnabled(true);
                BtnHapusDiagnosaKompi1.setEnabled(false);
            } else {
                BtnSimpanDiagnosaKompi1.setEnabled(false);
                BtnHapusDiagnosaKompi1.setEnabled(true);
            }
        });
        
        penjab.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (penjab.getTable().getSelectedRow() != -1) {
                    KdPjKompi1.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(), 1).toString());
                    NamaPjKompi1.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(), 2).toString());
                    tampil();
                }
                KdPjKompi1.requestFocus();
            }
        });

        penjab.getTable().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    penjab.dispose();
                }
            }
        });
        
        TMaxMemory.setDocument(new batasInput(4).getOnlyAngka(TMaxMemory));

        cekPengaturanKompilasi();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        ppBerkasDigital = new javax.swing.JMenuItem();
        ppRiwayatPerawatan = new javax.swing.JMenuItem();
        ppUpdateTanggalPulangSEP = new javax.swing.JMenuItem();
        WindowUpdatePulang = new javax.swing.JDialog();
        internalFrame11 = new widget.InternalFrame();
        BtnCloseIn8 = new widget.Button();
        BtnSimpan8 = new widget.Button();
        jLabel44 = new widget.Label();
        TanggalPulang = new widget.Tanggal();
        jLabel46 = new widget.Label();
        StatusPulang = new widget.ComboBox();
        jLabel47 = new widget.Label();
        NoSuratKematian = new widget.TextBox();
        jLabel48 = new widget.Label();
        TanggalKematian = new widget.Tanggal();
        jLabel49 = new widget.Label();
        NoLPManual = new widget.TextBox();
        jLabel9 = new widget.Label();
        TNoRwPulang = new widget.TextBox();
        TNoRMPulang = new widget.TextBox();
        TPasienPulang = new widget.TextBox();
        jLabel41 = new widget.Label();
        TNoSEPRanapPulang = new widget.TextBox();
        jLabel10 = new widget.Label();
        WindowPengaturan = new javax.swing.JDialog();
        internalFrame12 = new widget.InternalFrame();
        BtnTutupPengaturan = new widget.Button();
        BtnBukaFolderExport = new widget.Button();
        CmbPilihanAplikasiPDF = new widget.ComboBox();
        jLabel12 = new widget.Label();
        TPathAplikasiPDF = new widget.TextBox();
        jLabel42 = new widget.Label();
        CmbPilihanTanggalExport = new widget.ComboBox();
        BtnSimpanPengaturan = new widget.Button();
        BtnPilihAplikasiPDF = new widget.Button();
        BtnResetPengaturan = new widget.Button();
        jLabel13 = new widget.Label();
        TMaxMemory = new widget.TextBox();
        jLabel16 = new widget.Label();
        jLabel22 = new widget.Label();
        fc = new javax.swing.JFileChooser();
        kirimonline = new widget.Button();
        internalFrame1Kompi1 = new widget.InternalFrame();
        jPanel3Kompi1 = new javax.swing.JPanel();
        panelGlass8Kompi1 = new widget.panelisi();
        label19Kompi1 = new widget.Label();
        KdPjKompi1 = new widget.TextBox();
        NamaPjKompi1 = new widget.TextBox();
        BtnPenjaminKompi1 = new widget.Button();
        jLabel6Kompi1 = new widget.Label();
        TCariKompi1 = new widget.TextBox();
        BtnCariKompi1 = new widget.Button();
        BtnAllKompi1 = new widget.Button();
        jLabel7Kompi1 = new widget.Label();
        LCountKompi1 = new widget.Label();
        BtnPengaturanKompi1 = new widget.Button();
        BtnKeluarKompi1 = new widget.Button();
        hapusKlaimKompi1 = new widget.Button();
        panelGlass10Kompi1 = new widget.panelisi();
        jLabel19Kompi1 = new widget.Label();
        CmbTanggalSEPKompi1 = new widget.ComboBox();
        DTPCari1Kompi1 = new widget.Tanggal();
        jLabel21Kompi1 = new widget.Label();
        DTPCari2Kompi1 = new widget.Tanggal();
        jLabel8Kompi1 = new widget.Label();
        CmbStatusRawatKompi1 = new widget.ComboBox();
        jLabel11Kompi1 = new widget.Label();
        CmbStatusKirimKompi1 = new widget.ComboBox();
        lblCoderNIKKompi1 = new widget.Label();
        jPanel1Kompi1 = new javax.swing.JPanel();
        jPanel4Kompi1 = new javax.swing.JPanel();
        panelGlass11Kompi1 = new widget.panelisi();
        ScrollKompi1 = new widget.ScrollPane();
        tbKompilasiKompi1 = new widget.Table();
        jPanel2Kompi1 = new javax.swing.JPanel();
        scrollPane1Kompi1 = new widget.ScrollPane();
        panelBiasa1Kompi1 = new widget.PanelBiasa();
        jLabel14Kompi1 = new widget.Label();
        lblNamaPasienKompi1 = new widget.Label();
        jLabel15Kompi1 = new widget.Label();
        lblNoRawatKompi1 = new widget.Label();
        lblNoRMKompi1 = new widget.Label();
        jLabel17Kompi1 = new widget.Label();
        lblNoSEPKompi1 = new widget.Label();
        jLabel18Kompi1 = new widget.Label();
        btnSEPKompi1 = new widget.Button();
        jLabel25Kompi1 = new widget.Label();
        btnResumeRanapKompi1 = new widget.Button();
        jLabel26Kompi1 = new widget.Label();
        btnInvoiceKompi1 = new widget.Button();
        jLabel27Kompi1 = new widget.Label();
        btnAwalMedisIGDKompi1 = new widget.Button();
        jLabel28Kompi1 = new widget.Label();
        btnHasilLabKompi1 = new widget.Button();
        jLabel29Kompi1 = new widget.Label();
        btnHasilRadKompi1 = new widget.Button();
        jLabel30Kompi1 = new widget.Label();
        btnSurkonKompi1 = new widget.Button();
        jLabel31Kompi1 = new widget.Label();
        btnSPRIKompi1 = new widget.Button();
        jLabel20Kompi1 = new widget.Label();
        lblStatusRawatKompi1 = new widget.Label();
        BtnSimpanDiagnosaKompi1 = new widget.Button();
        BtnHapusDiagnosaKompi1 = new widget.Button();
        jLabel32Kompi1 = new widget.Label();
        btnRiwayatPasienKompi1 = new widget.Button();
        panelDiagnosaSmcKompi1 = new laporan.PanelDiagnosaSmc();
        jLabel33Kompi1 = new widget.Label();
        btnPDFKlaimINACBGKompi1 = new widget.Button();
        jLabel34Kompi1 = new widget.Label();
        btnTriaseIGDKompi1 = new widget.Button();
        lblTglSEPKompi1 = new widget.Label();
        lblTglSEP1Kompi1 = new widget.Label();
        btnBerkasDigitalKompi1 = new widget.Button();
        jLabel35Kompi1 = new widget.Label();
        panelBiasa2Kompi1 = new widget.PanelBiasa();
        BtnValidasiQRKompi1 = new widget.Button();
        jPanel5Kompi1 = new javax.swing.JPanel();
        tabPane1Kompi1 = new widget.TabPane();
        panelInvoicesKompi1 = new widget.panelisi();
        PanelContentINACBGKompi1 = new widget.panelisi();

        jPopupMenu1.setForeground(new java.awt.Color(50, 50, 50));
        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        ppBerkasDigital.setBackground(new java.awt.Color(255, 255, 254));
        ppBerkasDigital.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBerkasDigital.setForeground(new java.awt.Color(50, 50, 50));
        ppBerkasDigital.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppBerkasDigital.setText("Berkas Perawatan Digital");
        ppBerkasDigital.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBerkasDigital.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBerkasDigital.setName("ppBerkasDigital"); // NOI18N
        ppBerkasDigital.setPreferredSize(new java.awt.Dimension(200, 26));
        ppBerkasDigital.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBerkasDigitalActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppBerkasDigital);

        ppRiwayatPerawatan.setBackground(new java.awt.Color(255, 255, 254));
        ppRiwayatPerawatan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppRiwayatPerawatan.setForeground(new java.awt.Color(50, 50, 50));
        ppRiwayatPerawatan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppRiwayatPerawatan.setText("Riwayat Perawatan");
        ppRiwayatPerawatan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppRiwayatPerawatan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppRiwayatPerawatan.setName("ppRiwayatPerawatan"); // NOI18N
        ppRiwayatPerawatan.setPreferredSize(new java.awt.Dimension(200, 26));
        ppRiwayatPerawatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppRiwayatPerawatanActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppRiwayatPerawatan);

        ppUpdateTanggalPulangSEP.setBackground(new java.awt.Color(255, 255, 254));
        ppUpdateTanggalPulangSEP.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppUpdateTanggalPulangSEP.setForeground(new java.awt.Color(50, 50, 50));
        ppUpdateTanggalPulangSEP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppUpdateTanggalPulangSEP.setText("Update Tanggal Pulang SEP Ranap");
        ppUpdateTanggalPulangSEP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppUpdateTanggalPulangSEP.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppUpdateTanggalPulangSEP.setName("ppUpdateTanggalPulangSEP"); // NOI18N
        ppUpdateTanggalPulangSEP.setPreferredSize(new java.awt.Dimension(200, 26));
        ppUpdateTanggalPulangSEP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppUpdateTanggalPulangSEPActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppUpdateTanggalPulangSEP);

        WindowUpdatePulang.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowUpdatePulang.setName("WindowUpdatePulang"); // NOI18N
        WindowUpdatePulang.setUndecorated(true);
        WindowUpdatePulang.setResizable(false);

        internalFrame11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Update Tanggal Pulang ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame11.setName("internalFrame11"); // NOI18N
        internalFrame11.setLayout(null);

        BtnCloseIn8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnCloseIn8.setMnemonic('U');
        BtnCloseIn8.setText("Tutup");
        BtnCloseIn8.setToolTipText("Alt+U");
        BtnCloseIn8.setName("BtnCloseIn8"); // NOI18N
        BtnCloseIn8.setPreferredSize(new java.awt.Dimension(86, 30));
        BtnCloseIn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseIn8ActionPerformed(evt);
            }
        });
        internalFrame11.add(BtnCloseIn8);
        BtnCloseIn8.setBounds(489, 182, 86, 30);

        BtnSimpan8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan8.setMnemonic('S');
        BtnSimpan8.setText("Simpan");
        BtnSimpan8.setToolTipText("Alt+S");
        BtnSimpan8.setName("BtnSimpan8"); // NOI18N
        BtnSimpan8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpan8ActionPerformed(evt);
            }
        });
        internalFrame11.add(BtnSimpan8);
        BtnSimpan8.setBounds(10, 182, 86, 30);

        jLabel44.setText("Tgl. Pulang :");
        jLabel44.setName("jLabel44"); // NOI18N
        internalFrame11.add(jLabel44);
        jLabel44.setBounds(0, 92, 78, 23);

        TanggalPulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "25-04-2025 11:05:10" }));
        TanggalPulang.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        TanggalPulang.setName("TanggalPulang"); // NOI18N
        TanggalPulang.setOpaque(false);
        TanggalPulang.setPreferredSize(new java.awt.Dimension(95, 23));
        internalFrame11.add(TanggalPulang);
        TanggalPulang.setBounds(81, 92, 130, 23);

        jLabel46.setText("Status Pulang :");
        jLabel46.setName("jLabel46"); // NOI18N
        internalFrame11.add(jLabel46);
        jLabel46.setBounds(301, 92, 81, 23);

        StatusPulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. Atas Persetujuan Dokter", "3. Atas Permintaan Sendiri", "4. Meninggal", "5. Lain-lain" }));
        StatusPulang.setName("StatusPulang"); // NOI18N
        StatusPulang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                StatusPulangItemStateChanged(evt);
            }
        });
        internalFrame11.add(StatusPulang);
        StatusPulang.setBounds(385, 92, 190, 23);

        jLabel47.setText("No. Surat Kematian :");
        jLabel47.setName("jLabel47"); // NOI18N
        jLabel47.setPreferredSize(new java.awt.Dimension(68, 23));
        internalFrame11.add(jLabel47);
        jLabel47.setBounds(0, 122, 120, 23);

        NoSuratKematian.setEditable(false);
        NoSuratKematian.setHighlighter(null);
        NoSuratKematian.setName("NoSuratKematian"); // NOI18N
        NoSuratKematian.setPreferredSize(new java.awt.Dimension(130, 23));
        internalFrame11.add(NoSuratKematian);
        NoSuratKematian.setBounds(123, 122, 160, 23);

        jLabel48.setText("Tanggal Kematian :");
        jLabel48.setName("jLabel48"); // NOI18N
        internalFrame11.add(jLabel48);
        jLabel48.setBounds(300, 122, 100, 23);

        TanggalKematian.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "25-04-2025" }));
        TanggalKematian.setDisplayFormat("dd-MM-yyyy");
        TanggalKematian.setEnabled(false);
        TanggalKematian.setName("TanggalKematian"); // NOI18N
        TanggalKematian.setOpaque(false);
        TanggalKematian.setPreferredSize(new java.awt.Dimension(95, 23));
        internalFrame11.add(TanggalKematian);
        TanggalKematian.setBounds(404, 122, 171, 23);

        jLabel49.setText("No. LP Manual :");
        jLabel49.setName("jLabel49"); // NOI18N
        jLabel49.setPreferredSize(new java.awt.Dimension(68, 23));
        internalFrame11.add(jLabel49);
        jLabel49.setBounds(0, 152, 120, 23);

        NoLPManual.setHighlighter(null);
        NoLPManual.setName("NoLPManual"); // NOI18N
        NoLPManual.setPreferredSize(new java.awt.Dimension(130, 23));
        internalFrame11.add(NoLPManual);
        NoLPManual.setBounds(123, 152, 160, 23);

        jLabel9.setText("No. Rawat :");
        jLabel9.setName("jLabel9"); // NOI18N
        internalFrame11.add(jLabel9);
        jLabel9.setBounds(0, 32, 78, 23);

        TNoRwPulang.setEditable(false);
        TNoRwPulang.setHighlighter(null);
        TNoRwPulang.setName("TNoRwPulang"); // NOI18N
        internalFrame11.add(TNoRwPulang);
        TNoRwPulang.setBounds(81, 32, 180, 23);

        TNoRMPulang.setEditable(false);
        TNoRMPulang.setHighlighter(null);
        TNoRMPulang.setName("TNoRMPulang"); // NOI18N
        internalFrame11.add(TNoRMPulang);
        TNoRMPulang.setBounds(81, 62, 130, 23);

        TPasienPulang.setEditable(false);
        TPasienPulang.setHighlighter(null);
        TPasienPulang.setName("TPasienPulang"); // NOI18N
        internalFrame11.add(TPasienPulang);
        TPasienPulang.setBounds(214, 62, 361, 23);

        jLabel41.setText("Pasien :");
        jLabel41.setName("jLabel41"); // NOI18N
        internalFrame11.add(jLabel41);
        jLabel41.setBounds(0, 62, 78, 23);

        TNoSEPRanapPulang.setEditable(false);
        TNoSEPRanapPulang.setHighlighter(null);
        TNoSEPRanapPulang.setName("TNoSEPRanapPulang"); // NOI18N
        internalFrame11.add(TNoSEPRanapPulang);
        TNoSEPRanapPulang.setBounds(395, 32, 180, 23);

        jLabel10.setText("No. SEP Ranap :");
        jLabel10.setName("jLabel10"); // NOI18N
        internalFrame11.add(jLabel10);
        jLabel10.setBounds(310, 32, 82, 23);

        WindowUpdatePulang.getContentPane().add(internalFrame11, java.awt.BorderLayout.CENTER);

        WindowPengaturan.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowPengaturan.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        WindowPengaturan.setName("WindowPengaturan"); // NOI18N
        WindowPengaturan.setUndecorated(true);
        WindowPengaturan.setResizable(false);

        internalFrame12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)), "::[ Pengaturan Kompilasi Berkas ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame12.setName("internalFrame12"); // NOI18N
        internalFrame12.setPreferredSize(new java.awt.Dimension(610, 182));
        internalFrame12.setLayout(null);

        BtnTutupPengaturan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnTutupPengaturan.setMnemonic('U');
        BtnTutupPengaturan.setText("Tutup");
        BtnTutupPengaturan.setToolTipText("Alt+U");
        BtnTutupPengaturan.setName("BtnTutupPengaturan"); // NOI18N
        BtnTutupPengaturan.setPreferredSize(new java.awt.Dimension(86, 30));
        BtnTutupPengaturan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTutupPengaturanActionPerformed(evt);
            }
        });
        internalFrame12.add(BtnTutupPengaturan);
        BtnTutupPengaturan.setBounds(514, 132, 86, 30);

        BtnBukaFolderExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnBukaFolderExport.setMnemonic('S');
        BtnBukaFolderExport.setText("Buka Folder Export");
        BtnBukaFolderExport.setToolTipText("Alt+S");
        BtnBukaFolderExport.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        BtnBukaFolderExport.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BtnBukaFolderExport.setName("BtnBukaFolderExport"); // NOI18N
        BtnBukaFolderExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBukaFolderExportActionPerformed(evt);
            }
        });
        internalFrame12.add(BtnBukaFolderExport);
        BtnBukaFolderExport.setBounds(324, 62, 136, 23);

        CmbPilihanAplikasiPDF.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "(Aplikasi Default)", "Google Chrome", "Mozilla Firefox", "Microsoft Edge", "Pilih Aplikasi...", "Jangan Buka PDF" }));
        CmbPilihanAplikasiPDF.setName("CmbPilihanAplikasiPDF"); // NOI18N
        CmbPilihanAplikasiPDF.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CmbPilihanAplikasiPDFItemStateChanged(evt);
            }
        });
        internalFrame12.add(CmbPilihanAplikasiPDF);
        CmbPilihanAplikasiPDF.setBounds(140, 32, 130, 23);

        jLabel12.setText("Buka PDF dengan :");
        jLabel12.setName("jLabel12"); // NOI18N
        internalFrame12.add(jLabel12);
        jLabel12.setBounds(0, 32, 136, 23);

        TPathAplikasiPDF.setEditable(false);
        TPathAplikasiPDF.setHighlighter(null);
        TPathAplikasiPDF.setName("TPathAplikasiPDF"); // NOI18N
        internalFrame12.add(TPathAplikasiPDF);
        TPathAplikasiPDF.setBounds(274, 32, 294, 23);

        jLabel42.setText("Gunakan Tanggal Export :");
        jLabel42.setName("jLabel42"); // NOI18N
        internalFrame12.add(jLabel42);
        jLabel42.setBounds(0, 62, 136, 23);

        CmbPilihanTanggalExport.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tanggal Kompilasi (Default)", "Tanggal SEP" }));
        CmbPilihanTanggalExport.setName("CmbPilihanTanggalExport"); // NOI18N
        CmbPilihanTanggalExport.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CmbPilihanTanggalExportItemStateChanged(evt);
            }
        });
        internalFrame12.add(CmbPilihanTanggalExport);
        CmbPilihanTanggalExport.setBounds(140, 62, 180, 23);

        BtnSimpanPengaturan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpanPengaturan.setMnemonic('S');
        BtnSimpanPengaturan.setText("Simpan");
        BtnSimpanPengaturan.setToolTipText("Alt+S");
        BtnSimpanPengaturan.setName("BtnSimpanPengaturan"); // NOI18N
        BtnSimpanPengaturan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanPengaturanActionPerformed(evt);
            }
        });
        internalFrame12.add(BtnSimpanPengaturan);
        BtnSimpanPengaturan.setBounds(10, 132, 86, 30);

        BtnPilihAplikasiPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPilihAplikasiPDF.setMnemonic('S');
        BtnPilihAplikasiPDF.setToolTipText("Alt+S");
        BtnPilihAplikasiPDF.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        BtnPilihAplikasiPDF.setName("BtnPilihAplikasiPDF"); // NOI18N
        BtnPilihAplikasiPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPilihAplikasiPDFActionPerformed(evt);
            }
        });
        internalFrame12.add(BtnPilihAplikasiPDF);
        BtnPilihAplikasiPDF.setBounds(572, 32, 28, 23);

        BtnResetPengaturan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/refresh.png"))); // NOI18N
        BtnResetPengaturan.setMnemonic('S');
        BtnResetPengaturan.setText("Reset");
        BtnResetPengaturan.setToolTipText("Alt+S");
        BtnResetPengaturan.setName("BtnResetPengaturan"); // NOI18N
        BtnResetPengaturan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnResetPengaturanActionPerformed(evt);
            }
        });
        internalFrame12.add(BtnResetPengaturan);
        BtnResetPengaturan.setBounds(150, 132, 86, 30);

        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("_____________________________________________________________________________________________________");
        jLabel13.setName("jLabel13"); // NOI18N
        internalFrame12.add(jLabel13);
        jLabel13.setBounds(0, 116, 610, 12);

        TMaxMemory.setHighlighter(null);
        TMaxMemory.setName("TMaxMemory"); // NOI18N
        internalFrame12.add(TMaxMemory);
        TMaxMemory.setBounds(140, 92, 80, 23);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jLabel16.setText("MB");
        jLabel16.setName("jLabel16"); // NOI18N
        internalFrame12.add(jLabel16);
        jLabel16.setBounds(228, 92, 14, 23);

        jLabel22.setText("Max Memory Kompilasi :");
        jLabel22.setName("jLabel22"); // NOI18N
        internalFrame12.add(jLabel22);
        jLabel22.setBounds(0, 92, 136, 23);

        WindowPengaturan.getContentPane().add(internalFrame12, java.awt.BorderLayout.CENTER);

        fc.setCurrentDirectory(null);
        fc.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
        fc.setName("fc"); // NOI18N

        kirimonline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/2.png"))); // NOI18N
        kirimonline.setText("Kirim Online");
        kirimonline.setToolTipText("Alt+S");
        kirimonline.setGlassColor(new java.awt.Color(0, 255, 153));
        kirimonline.setName("kirimonline"); // NOI18N
        kirimonline.setPreferredSize(new java.awt.Dimension(120, 30));
        kirimonline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kirimonlineActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1Kompi1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Kompilasi Berkas Klaim BPJS ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1Kompi1.setName("internalFrame1Kompi1"); // NOI18N
        internalFrame1Kompi1.setLayout(new java.awt.BorderLayout(1, 1));

        jPanel3Kompi1.setName("jPanel3Kompi1"); // NOI18N
        jPanel3Kompi1.setOpaque(false);
        jPanel3Kompi1.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3Kompi1.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8Kompi1.setName("panelGlass8Kompi1"); // NOI18N
        panelGlass8Kompi1.setPreferredSize(new java.awt.Dimension(55, 55));
        panelGlass8Kompi1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        label19Kompi1.setText("Jenis Bayar :");
        label19Kompi1.setName("label19Kompi1"); // NOI18N
        label19Kompi1.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass8Kompi1.add(label19Kompi1);

        KdPjKompi1.setEditable(false);
        KdPjKompi1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        KdPjKompi1.setName("KdPjKompi1"); // NOI18N
        KdPjKompi1.setPreferredSize(new java.awt.Dimension(41, 23));
        panelGlass8Kompi1.add(KdPjKompi1);

        NamaPjKompi1.setEditable(false);
        NamaPjKompi1.setName("NamaPjKompi1"); // NOI18N
        NamaPjKompi1.setPreferredSize(new java.awt.Dimension(170, 23));
        panelGlass8Kompi1.add(NamaPjKompi1);

        BtnPenjaminKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPenjaminKompi1.setMnemonic('3');
        BtnPenjaminKompi1.setToolTipText("Alt+3");
        BtnPenjaminKompi1.setName("BtnPenjaminKompi1"); // NOI18N
        BtnPenjaminKompi1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPenjaminKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPenjaminKompi1ActionPerformed(evt);
            }
        });
        panelGlass8Kompi1.add(BtnPenjaminKompi1);

        jLabel6Kompi1.setText("Key Word :");
        jLabel6Kompi1.setName("jLabel6Kompi1"); // NOI18N
        jLabel6Kompi1.setPreferredSize(new java.awt.Dimension(62, 23));
        panelGlass8Kompi1.add(jLabel6Kompi1);

        TCariKompi1.setName("TCariKompi1"); // NOI18N
        TCariKompi1.setPreferredSize(new java.awt.Dimension(180, 23));
        TCariKompi1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKompi1KeyPressed(evt);
            }
        });
        panelGlass8Kompi1.add(TCariKompi1);

        BtnCariKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariKompi1.setMnemonic('2');
        BtnCariKompi1.setName("BtnCariKompi1"); // NOI18N
        BtnCariKompi1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariKompi1ActionPerformed(evt);
            }
        });
        BtnCariKompi1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKompi1KeyPressed(evt);
            }
        });
        panelGlass8Kompi1.add(BtnCariKompi1);

        BtnAllKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllKompi1.setMnemonic('M');
        BtnAllKompi1.setToolTipText("Alt+M");
        BtnAllKompi1.setName("BtnAllKompi1"); // NOI18N
        BtnAllKompi1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAllKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllKompi1ActionPerformed(evt);
            }
        });
        BtnAllKompi1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKompi1KeyPressed(evt);
            }
        });
        panelGlass8Kompi1.add(BtnAllKompi1);

        jLabel7Kompi1.setText("Record :");
        jLabel7Kompi1.setName("jLabel7Kompi1"); // NOI18N
        jLabel7Kompi1.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass8Kompi1.add(jLabel7Kompi1);

        LCountKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCountKompi1.setText("0");
        LCountKompi1.setName("LCountKompi1"); // NOI18N
        LCountKompi1.setPreferredSize(new java.awt.Dimension(50, 23));
        panelGlass8Kompi1.add(LCountKompi1);

        BtnPengaturanKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/EDIT2.png"))); // NOI18N
        BtnPengaturanKompi1.setMnemonic('T');
        BtnPengaturanKompi1.setText("Pengaturan");
        BtnPengaturanKompi1.setToolTipText("Alt+T");
        BtnPengaturanKompi1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BtnPengaturanKompi1.setMaximumSize(new java.awt.Dimension(100, 30));
        BtnPengaturanKompi1.setMinimumSize(new java.awt.Dimension(100, 30));
        BtnPengaturanKompi1.setName("BtnPengaturanKompi1"); // NOI18N
        BtnPengaturanKompi1.setPreferredSize(new java.awt.Dimension(120, 28));
        BtnPengaturanKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPengaturanKompi1ActionPerformed(evt);
            }
        });
        panelGlass8Kompi1.add(BtnPengaturanKompi1);

        BtnKeluarKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluarKompi1.setMnemonic('K');
        BtnKeluarKompi1.setText("Keluar");
        BtnKeluarKompi1.setToolTipText("Alt+K");
        BtnKeluarKompi1.setName("BtnKeluarKompi1"); // NOI18N
        BtnKeluarKompi1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluarKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarKompi1ActionPerformed(evt);
            }
        });
        BtnKeluarKompi1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKompi1KeyPressed(evt);
            }
        });
        panelGlass8Kompi1.add(BtnKeluarKompi1);

        hapusKlaimKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/delete_folder.png"))); // NOI18N
        hapusKlaimKompi1.setMnemonic('K');
        hapusKlaimKompi1.setText("Hapus Claim");
        hapusKlaimKompi1.setToolTipText("Alt+K");
        hapusKlaimKompi1.setGlassColor(new java.awt.Color(255, 102, 102));
        hapusKlaimKompi1.setName("hapusKlaimKompi1"); // NOI18N
        hapusKlaimKompi1.setPreferredSize(new java.awt.Dimension(130, 30));
        hapusKlaimKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusKlaimKompi1ActionPerformed(evt);
            }
        });
        hapusKlaimKompi1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hapusKlaimKompi1KeyPressed(evt);
            }
        });
        panelGlass8Kompi1.add(hapusKlaimKompi1);

        jPanel3Kompi1.add(panelGlass8Kompi1, java.awt.BorderLayout.PAGE_END);

        panelGlass10Kompi1.setName("panelGlass10Kompi1"); // NOI18N
        panelGlass10Kompi1.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass10Kompi1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        jLabel19Kompi1.setText("Tgl. SEP Berdasarkan :");
        jLabel19Kompi1.setName("jLabel19Kompi1"); // NOI18N
        jLabel19Kompi1.setPreferredSize(new java.awt.Dimension(120, 23));
        panelGlass10Kompi1.add(jLabel19Kompi1);

        CmbTanggalSEPKompi1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tanggal Masuk", "Tanggal Pulang" }));
        CmbTanggalSEPKompi1.setLightWeightPopupEnabled(false);
        CmbTanggalSEPKompi1.setMinimumSize(new java.awt.Dimension(75, 21));
        CmbTanggalSEPKompi1.setName("CmbTanggalSEPKompi1"); // NOI18N
        CmbTanggalSEPKompi1.setPreferredSize(new java.awt.Dimension(110, 23));
        panelGlass10Kompi1.add(CmbTanggalSEPKompi1);

        DTPCari1Kompi1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1Kompi1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "25-04-2025" }));
        DTPCari1Kompi1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1Kompi1.setName("DTPCari1Kompi1"); // NOI18N
        DTPCari1Kompi1.setOpaque(false);
        DTPCari1Kompi1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass10Kompi1.add(DTPCari1Kompi1);

        jLabel21Kompi1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21Kompi1.setText("s.d.");
        jLabel21Kompi1.setName("jLabel21Kompi1"); // NOI18N
        jLabel21Kompi1.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass10Kompi1.add(jLabel21Kompi1);

        DTPCari2Kompi1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2Kompi1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "25-04-2025" }));
        DTPCari2Kompi1.setDisplayFormat("dd-MM-yyyy");
        DTPCari2Kompi1.setName("DTPCari2Kompi1"); // NOI18N
        DTPCari2Kompi1.setOpaque(false);
        DTPCari2Kompi1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass10Kompi1.add(DTPCari2Kompi1);

        jLabel8Kompi1.setText("Status Rawat :");
        jLabel8Kompi1.setName("jLabel8Kompi1"); // NOI18N
        jLabel8Kompi1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass10Kompi1.add(jLabel8Kompi1);

        CmbStatusRawatKompi1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Ralan", "Ranap" }));
        CmbStatusRawatKompi1.setLightWeightPopupEnabled(false);
        CmbStatusRawatKompi1.setMinimumSize(new java.awt.Dimension(75, 21));
        CmbStatusRawatKompi1.setName("CmbStatusRawatKompi1"); // NOI18N
        CmbStatusRawatKompi1.setPreferredSize(new java.awt.Dimension(76, 23));
        panelGlass10Kompi1.add(CmbStatusRawatKompi1);

        jLabel11Kompi1.setText("Status Kirim :");
        jLabel11Kompi1.setName("jLabel11Kompi1"); // NOI18N
        jLabel11Kompi1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass10Kompi1.add(jLabel11Kompi1);

        CmbStatusKirimKompi1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Terkirim", "Belum Terkirim" }));
        CmbStatusKirimKompi1.setLightWeightPopupEnabled(false);
        CmbStatusKirimKompi1.setMinimumSize(new java.awt.Dimension(75, 21));
        CmbStatusKirimKompi1.setName("CmbStatusKirimKompi1"); // NOI18N
        CmbStatusKirimKompi1.setPreferredSize(new java.awt.Dimension(113, 23));
        panelGlass10Kompi1.add(CmbStatusKirimKompi1);

        lblCoderNIKKompi1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCoderNIKKompi1.setName("lblCoderNIKKompi1"); // NOI18N
        lblCoderNIKKompi1.setPreferredSize(new java.awt.Dimension(105, 23));
        panelGlass10Kompi1.add(lblCoderNIKKompi1);

        jPanel3Kompi1.add(panelGlass10Kompi1, java.awt.BorderLayout.CENTER);

        internalFrame1Kompi1.add(jPanel3Kompi1, java.awt.BorderLayout.PAGE_END);

        jPanel1Kompi1.setName("jPanel1Kompi1"); // NOI18N
        jPanel1Kompi1.setOpaque(false);
        jPanel1Kompi1.setPreferredSize(new java.awt.Dimension(816, 102));
        jPanel1Kompi1.setLayout(new java.awt.GridLayout(1, 2));

        jPanel4Kompi1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)), "Data Pasien", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        jPanel4Kompi1.setName("jPanel4Kompi1"); // NOI18N
        jPanel4Kompi1.setOpaque(false);

        panelGlass11Kompi1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        panelGlass11Kompi1.setName("panelGlass11Kompi1"); // NOI18N
        panelGlass11Kompi1.setLayout(new java.awt.BorderLayout());

        ScrollKompi1.setName("ScrollKompi1"); // NOI18N
        ScrollKompi1.setOpaque(true);

        tbKompilasiKompi1.setAutoCreateRowSorter(true);
        tbKompilasiKompi1.setComponentPopupMenu(jPopupMenu1);
        tbKompilasiKompi1.setFillsViewportHeight(true);
        tbKompilasiKompi1.setMaximumSize(new java.awt.Dimension(32767, 32767));
        tbKompilasiKompi1.setName("tbKompilasiKompi1"); // NOI18N
        tbKompilasiKompi1.setPreferredScrollableViewportSize(null);
        tbKompilasiKompi1.setPreferredSize(null);
        tbKompilasiKompi1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbKompilasiKompi1MouseClicked(evt);
            }
        });
        tbKompilasiKompi1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbKompilasiKompi1KeyReleased(evt);
            }
        });
        ScrollKompi1.setViewportView(tbKompilasiKompi1);

        panelGlass11Kompi1.add(ScrollKompi1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel4Kompi1Layout = new javax.swing.GroupLayout(jPanel4Kompi1);
        jPanel4Kompi1.setLayout(jPanel4Kompi1Layout);
        jPanel4Kompi1Layout.setHorizontalGroup(
            jPanel4Kompi1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGlass11Kompi1, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
        );
        jPanel4Kompi1Layout.setVerticalGroup(
            jPanel4Kompi1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGlass11Kompi1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1Kompi1.add(jPanel4Kompi1);

        jPanel2Kompi1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)), "Kelengkapan Data", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        jPanel2Kompi1.setName("jPanel2Kompi1"); // NOI18N
        jPanel2Kompi1.setOpaque(false);
        jPanel2Kompi1.setPreferredSize(new java.awt.Dimension(800, 820));
        jPanel2Kompi1.setLayout(new java.awt.BorderLayout());

        scrollPane1Kompi1.setName("scrollPane1Kompi1"); // NOI18N
        scrollPane1Kompi1.setPreferredSize(new java.awt.Dimension(800, 820));

        panelBiasa1Kompi1.setName("panelBiasa1Kompi1"); // NOI18N
        panelBiasa1Kompi1.setPreferredSize(new java.awt.Dimension(800, 790));
        panelBiasa1Kompi1.setLayout(null);

        jLabel14Kompi1.setText("Data Pasien : ");
        jLabel14Kompi1.setName("jLabel14Kompi1"); // NOI18N
        jLabel14Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel14Kompi1);
        jLabel14Kompi1.setBounds(0, 10, 120, 14);

        lblNamaPasienKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNamaPasienKompi1.setName("lblNamaPasienKompi1"); // NOI18N
        lblNamaPasienKompi1.setPreferredSize(new java.awt.Dimension(300, 14));
        panelBiasa1Kompi1.add(lblNamaPasienKompi1);
        lblNamaPasienKompi1.setBounds(160, 10, 200, 14);

        jLabel15Kompi1.setText("No. Rawat : ");
        jLabel15Kompi1.setName("jLabel15Kompi1"); // NOI18N
        jLabel15Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel15Kompi1);
        jLabel15Kompi1.setBounds(0, 30, 120, 14);

        lblNoRawatKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNoRawatKompi1.setName("lblNoRawatKompi1"); // NOI18N
        lblNoRawatKompi1.setPreferredSize(new java.awt.Dimension(300, 14));
        panelBiasa1Kompi1.add(lblNoRawatKompi1);
        lblNoRawatKompi1.setBounds(120, 30, 240, 14);

        lblNoRMKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNoRMKompi1.setName("lblNoRMKompi1"); // NOI18N
        lblNoRMKompi1.setPreferredSize(new java.awt.Dimension(300, 14));
        panelBiasa1Kompi1.add(lblNoRMKompi1);
        lblNoRMKompi1.setBounds(120, 10, 36, 14);

        jLabel17Kompi1.setText("No. SEP : ");
        jLabel17Kompi1.setName("jLabel17Kompi1"); // NOI18N
        jLabel17Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel17Kompi1);
        jLabel17Kompi1.setBounds(0, 70, 120, 14);

        lblNoSEPKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNoSEPKompi1.setToolTipText("");
        lblNoSEPKompi1.setName("lblNoSEPKompi1"); // NOI18N
        lblNoSEPKompi1.setPreferredSize(new java.awt.Dimension(300, 14));
        panelBiasa1Kompi1.add(lblNoSEPKompi1);
        lblNoSEPKompi1.setBounds(120, 70, 124, 14);

        jLabel18Kompi1.setText("Data SEP : ");
        jLabel18Kompi1.setName("jLabel18Kompi1"); // NOI18N
        jLabel18Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel18Kompi1);
        jLabel18Kompi1.setBounds(0, 110, 120, 14);

        btnSEPKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnSEPKompi1.setMnemonic('1');
        btnSEPKompi1.setText("Lihat");
        btnSEPKompi1.setToolTipText("ALt+1");
        btnSEPKompi1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnSEPKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSEPKompi1.setName("btnSEPKompi1"); // NOI18N
        btnSEPKompi1.setPreferredSize(new java.awt.Dimension(100, 14));
        btnSEPKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSEPKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(btnSEPKompi1);
        btnSEPKompi1.setBounds(120, 110, 100, 14);

        jLabel25Kompi1.setText("Resume Ranap : ");
        jLabel25Kompi1.setName("jLabel25Kompi1"); // NOI18N
        jLabel25Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel25Kompi1);
        jLabel25Kompi1.setBounds(0, 190, 120, 14);

        btnResumeRanapKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnResumeRanapKompi1.setMnemonic('1');
        btnResumeRanapKompi1.setText("Lihat");
        btnResumeRanapKompi1.setToolTipText("ALt+1");
        btnResumeRanapKompi1.setEnabled(false);
        btnResumeRanapKompi1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnResumeRanapKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnResumeRanapKompi1.setName("btnResumeRanapKompi1"); // NOI18N
        btnResumeRanapKompi1.setPreferredSize(new java.awt.Dimension(100, 14));
        btnResumeRanapKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResumeRanapKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(btnResumeRanapKompi1);
        btnResumeRanapKompi1.setBounds(120, 190, 100, 14);

        jLabel26Kompi1.setText("Billing : ");
        jLabel26Kompi1.setName("jLabel26Kompi1"); // NOI18N
        jLabel26Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel26Kompi1);
        jLabel26Kompi1.setBounds(0, 130, 120, 14);

        btnInvoiceKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnInvoiceKompi1.setMnemonic('1');
        btnInvoiceKompi1.setText("Lihat");
        btnInvoiceKompi1.setToolTipText("ALt+1");
        btnInvoiceKompi1.setEnabled(false);
        btnInvoiceKompi1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnInvoiceKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnInvoiceKompi1.setName("btnInvoiceKompi1"); // NOI18N
        btnInvoiceKompi1.setPreferredSize(new java.awt.Dimension(100, 14));
        btnInvoiceKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvoiceKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(btnInvoiceKompi1);
        btnInvoiceKompi1.setBounds(120, 130, 100, 14);

        jLabel27Kompi1.setText("Awal Medis IGD : ");
        jLabel27Kompi1.setName("jLabel27Kompi1"); // NOI18N
        jLabel27Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel27Kompi1);
        jLabel27Kompi1.setBounds(0, 170, 120, 14);

        btnAwalMedisIGDKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnAwalMedisIGDKompi1.setMnemonic('1');
        btnAwalMedisIGDKompi1.setText("Lihat");
        btnAwalMedisIGDKompi1.setToolTipText("ALt+1");
        btnAwalMedisIGDKompi1.setEnabled(false);
        btnAwalMedisIGDKompi1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnAwalMedisIGDKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAwalMedisIGDKompi1.setName("btnAwalMedisIGDKompi1"); // NOI18N
        btnAwalMedisIGDKompi1.setPreferredSize(new java.awt.Dimension(100, 14));
        btnAwalMedisIGDKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAwalMedisIGDKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(btnAwalMedisIGDKompi1);
        btnAwalMedisIGDKompi1.setBounds(120, 170, 100, 14);

        jLabel28Kompi1.setText("Hasil Lab : ");
        jLabel28Kompi1.setName("jLabel28Kompi1"); // NOI18N
        jLabel28Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel28Kompi1);
        jLabel28Kompi1.setBounds(0, 210, 120, 14);

        btnHasilLabKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnHasilLabKompi1.setMnemonic('1');
        btnHasilLabKompi1.setText("Lihat");
        btnHasilLabKompi1.setToolTipText("ALt+1");
        btnHasilLabKompi1.setEnabled(false);
        btnHasilLabKompi1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnHasilLabKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHasilLabKompi1.setName("btnHasilLabKompi1"); // NOI18N
        btnHasilLabKompi1.setPreferredSize(new java.awt.Dimension(100, 14));
        btnHasilLabKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHasilLabKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(btnHasilLabKompi1);
        btnHasilLabKompi1.setBounds(120, 210, 100, 14);

        jLabel29Kompi1.setText("Hasil Radiologi : ");
        jLabel29Kompi1.setName("jLabel29Kompi1"); // NOI18N
        jLabel29Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel29Kompi1);
        jLabel29Kompi1.setBounds(0, 230, 120, 14);

        btnHasilRadKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnHasilRadKompi1.setMnemonic('1');
        btnHasilRadKompi1.setText("Lihat");
        btnHasilRadKompi1.setToolTipText("ALt+1");
        btnHasilRadKompi1.setEnabled(false);
        btnHasilRadKompi1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnHasilRadKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHasilRadKompi1.setName("btnHasilRadKompi1"); // NOI18N
        btnHasilRadKompi1.setPreferredSize(new java.awt.Dimension(100, 14));
        btnHasilRadKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHasilRadKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(btnHasilRadKompi1);
        btnHasilRadKompi1.setBounds(120, 230, 100, 14);

        jLabel30Kompi1.setText("Surat Kontrol : ");
        jLabel30Kompi1.setName("jLabel30Kompi1"); // NOI18N
        jLabel30Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel30Kompi1);
        jLabel30Kompi1.setBounds(0, 290, 120, 14);

        btnSurkonKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnSurkonKompi1.setMnemonic('1');
        btnSurkonKompi1.setText("Lihat");
        btnSurkonKompi1.setToolTipText("ALt+1");
        btnSurkonKompi1.setEnabled(false);
        btnSurkonKompi1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnSurkonKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSurkonKompi1.setName("btnSurkonKompi1"); // NOI18N
        btnSurkonKompi1.setPreferredSize(new java.awt.Dimension(100, 14));
        btnSurkonKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSurkonKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(btnSurkonKompi1);
        btnSurkonKompi1.setBounds(120, 290, 100, 14);

        jLabel31Kompi1.setText("SPRI : ");
        jLabel31Kompi1.setName("jLabel31Kompi1"); // NOI18N
        jLabel31Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel31Kompi1);
        jLabel31Kompi1.setBounds(0, 270, 120, 14);

        btnSPRIKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnSPRIKompi1.setMnemonic('1');
        btnSPRIKompi1.setText("Lihat");
        btnSPRIKompi1.setToolTipText("ALt+1");
        btnSPRIKompi1.setEnabled(false);
        btnSPRIKompi1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnSPRIKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSPRIKompi1.setName("btnSPRIKompi1"); // NOI18N
        btnSPRIKompi1.setPreferredSize(new java.awt.Dimension(100, 14));
        btnSPRIKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSPRIKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(btnSPRIKompi1);
        btnSPRIKompi1.setBounds(120, 270, 100, 14);

        jLabel20Kompi1.setText("Status Rawat : ");
        jLabel20Kompi1.setName("jLabel20Kompi1"); // NOI18N
        jLabel20Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel20Kompi1);
        jLabel20Kompi1.setBounds(0, 50, 120, 14);

        lblStatusRawatKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblStatusRawatKompi1.setName("lblStatusRawatKompi1"); // NOI18N
        lblStatusRawatKompi1.setPreferredSize(new java.awt.Dimension(300, 14));
        panelBiasa1Kompi1.add(lblStatusRawatKompi1);
        lblStatusRawatKompi1.setBounds(120, 50, 240, 14);

        BtnSimpanDiagnosaKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpanDiagnosaKompi1.setMnemonic('S');
        BtnSimpanDiagnosaKompi1.setText("Simpan");
        BtnSimpanDiagnosaKompi1.setToolTipText("Alt+S");
        BtnSimpanDiagnosaKompi1.setAlignmentY(0.0F);
        BtnSimpanDiagnosaKompi1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BtnSimpanDiagnosaKompi1.setName("BtnSimpanDiagnosaKompi1"); // NOI18N
        BtnSimpanDiagnosaKompi1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpanDiagnosaKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanDiagnosaKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(BtnSimpanDiagnosaKompi1);
        BtnSimpanDiagnosaKompi1.setBounds(0, 750, 100, 30);

        BtnHapusDiagnosaKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapusDiagnosaKompi1.setMnemonic('H');
        BtnHapusDiagnosaKompi1.setText("Hapus");
        BtnHapusDiagnosaKompi1.setToolTipText("Alt+H");
        BtnHapusDiagnosaKompi1.setAlignmentY(0.0F);
        BtnHapusDiagnosaKompi1.setEnabled(false);
        BtnHapusDiagnosaKompi1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BtnHapusDiagnosaKompi1.setMaximumSize(new java.awt.Dimension(76, 26));
        BtnHapusDiagnosaKompi1.setMinimumSize(new java.awt.Dimension(76, 26));
        BtnHapusDiagnosaKompi1.setName("BtnHapusDiagnosaKompi1"); // NOI18N
        BtnHapusDiagnosaKompi1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapusDiagnosaKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusDiagnosaKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(BtnHapusDiagnosaKompi1);
        BtnHapusDiagnosaKompi1.setBounds(530, 750, 100, 30);

        jLabel32Kompi1.setText("Riwayat : ");
        jLabel32Kompi1.setName("jLabel32Kompi1"); // NOI18N
        jLabel32Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel32Kompi1);
        jLabel32Kompi1.setBounds(0, 250, 120, 14);

        btnRiwayatPasienKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnRiwayatPasienKompi1.setMnemonic('1');
        btnRiwayatPasienKompi1.setText("Lihat");
        btnRiwayatPasienKompi1.setToolTipText("ALt+1");
        btnRiwayatPasienKompi1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnRiwayatPasienKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRiwayatPasienKompi1.setName("btnRiwayatPasienKompi1"); // NOI18N
        btnRiwayatPasienKompi1.setPreferredSize(new java.awt.Dimension(100, 14));
        btnRiwayatPasienKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRiwayatPasienKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(btnRiwayatPasienKompi1);
        btnRiwayatPasienKompi1.setBounds(120, 250, 100, 14);

        panelDiagnosaSmcKompi1.setName("panelDiagnosaSmcKompi1"); // NOI18N
        panelDiagnosaSmcKompi1.setPreferredSize(new java.awt.Dimension(800, 432));
        panelBiasa1Kompi1.add(panelDiagnosaSmcKompi1);
        panelDiagnosaSmcKompi1.setBounds(0, 310, 800, 432);

        jLabel33Kompi1.setText("Hasil Klaim INACBG : ");
        jLabel33Kompi1.setName("jLabel33Kompi1"); // NOI18N
        jLabel33Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel33Kompi1);
        jLabel33Kompi1.setBounds(0, 90, 120, 14);

        btnPDFKlaimINACBGKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPDFKlaimINACBGKompi1.setMnemonic('1');
        btnPDFKlaimINACBGKompi1.setText("Lihat");
        btnPDFKlaimINACBGKompi1.setToolTipText("ALt+1");
        btnPDFKlaimINACBGKompi1.setEnabled(false);
        btnPDFKlaimINACBGKompi1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnPDFKlaimINACBGKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPDFKlaimINACBGKompi1.setName("btnPDFKlaimINACBGKompi1"); // NOI18N
        btnPDFKlaimINACBGKompi1.setPreferredSize(new java.awt.Dimension(100, 14));
        btnPDFKlaimINACBGKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFKlaimINACBGKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(btnPDFKlaimINACBGKompi1);
        btnPDFKlaimINACBGKompi1.setBounds(120, 90, 100, 14);

        jLabel34Kompi1.setText("Triase IGD : ");
        jLabel34Kompi1.setName("jLabel34Kompi1"); // NOI18N
        jLabel34Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel34Kompi1);
        jLabel34Kompi1.setBounds(0, 150, 120, 14);

        btnTriaseIGDKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnTriaseIGDKompi1.setMnemonic('1');
        btnTriaseIGDKompi1.setText("Lihat");
        btnTriaseIGDKompi1.setToolTipText("ALt+1");
        btnTriaseIGDKompi1.setEnabled(false);
        btnTriaseIGDKompi1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnTriaseIGDKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnTriaseIGDKompi1.setName("btnTriaseIGDKompi1"); // NOI18N
        btnTriaseIGDKompi1.setPreferredSize(new java.awt.Dimension(100, 14));
        btnTriaseIGDKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTriaseIGDKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(btnTriaseIGDKompi1);
        btnTriaseIGDKompi1.setBounds(120, 150, 100, 14);

        lblTglSEPKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTglSEPKompi1.setToolTipText("");
        lblTglSEPKompi1.setName("lblTglSEPKompi1"); // NOI18N
        lblTglSEPKompi1.setPreferredSize(new java.awt.Dimension(300, 14));
        panelBiasa1Kompi1.add(lblTglSEPKompi1);
        lblTglSEPKompi1.setBounds(248, 70, 70, 14);

        lblTglSEP1Kompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTglSEP1Kompi1.setToolTipText("");
        lblTglSEP1Kompi1.setName("lblTglSEP1Kompi1"); // NOI18N
        lblTglSEP1Kompi1.setPreferredSize(new java.awt.Dimension(300, 14));
        panelBiasa1Kompi1.add(lblTglSEP1Kompi1);
        lblTglSEP1Kompi1.setBounds(368, 70, 60, 14);

        btnBerkasDigitalKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnBerkasDigitalKompi1.setMnemonic('1');
        btnBerkasDigitalKompi1.setText("Lihat");
        btnBerkasDigitalKompi1.setToolTipText("ALt+1");
        btnBerkasDigitalKompi1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnBerkasDigitalKompi1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBerkasDigitalKompi1.setName("btnBerkasDigitalKompi1"); // NOI18N
        btnBerkasDigitalKompi1.setPreferredSize(new java.awt.Dimension(100, 14));
        btnBerkasDigitalKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBerkasDigitalKompi1ActionPerformed(evt);
            }
        });
        panelBiasa1Kompi1.add(btnBerkasDigitalKompi1);
        btnBerkasDigitalKompi1.setBounds(340, 270, 80, 20);

        jLabel35Kompi1.setText("Berkas Digital :");
        jLabel35Kompi1.setName("jLabel35Kompi1"); // NOI18N
        jLabel35Kompi1.setPreferredSize(new java.awt.Dimension(120, 14));
        panelBiasa1Kompi1.add(jLabel35Kompi1);
        jLabel35Kompi1.setBounds(260, 270, 80, 14);

        scrollPane1Kompi1.setViewportView(panelBiasa1Kompi1);

        jPanel2Kompi1.add(scrollPane1Kompi1, java.awt.BorderLayout.CENTER);

        panelBiasa2Kompi1.setName("panelBiasa2Kompi1"); // NOI18N
        panelBiasa2Kompi1.setLayout(new java.awt.BorderLayout());

        BtnValidasiQRKompi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/2.png"))); // NOI18N
        BtnValidasiQRKompi1.setMnemonic('T');
        BtnValidasiQRKompi1.setText("Kompilasi");
        BtnValidasiQRKompi1.setToolTipText("Alt+T");
        BtnValidasiQRKompi1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BtnValidasiQRKompi1.setMaximumSize(new java.awt.Dimension(100, 30));
        BtnValidasiQRKompi1.setMinimumSize(new java.awt.Dimension(100, 30));
        BtnValidasiQRKompi1.setName("BtnValidasiQRKompi1"); // NOI18N
        BtnValidasiQRKompi1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnValidasiQRKompi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnValidasiQRKompi1ActionPerformed(evt);
            }
        });
        panelBiasa2Kompi1.add(BtnValidasiQRKompi1, java.awt.BorderLayout.CENTER);

        jPanel2Kompi1.add(panelBiasa2Kompi1, java.awt.BorderLayout.PAGE_END);

        jPanel1Kompi1.add(jPanel2Kompi1);

        jPanel5Kompi1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)), "Detail billing Pembayaran", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        jPanel5Kompi1.setName("jPanel5Kompi1"); // NOI18N
        jPanel5Kompi1.setOpaque(false);
        jPanel5Kompi1.setPreferredSize(new java.awt.Dimension(350, 102));
        jPanel5Kompi1.setLayout(new javax.swing.BoxLayout(jPanel5Kompi1, javax.swing.BoxLayout.LINE_AXIS));

        tabPane1Kompi1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        tabPane1Kompi1.setName("tabPane1Kompi1"); // NOI18N

        panelInvoicesKompi1.setName("panelInvoicesKompi1"); // NOI18N
        panelInvoicesKompi1.setPreferredSize(new java.awt.Dimension(55, 100));
        panelInvoicesKompi1.setLayout(new java.awt.BorderLayout());
        tabPane1Kompi1.addTab("Billing", panelInvoicesKompi1);
        panelInvoicesKompi1.getAccessibleContext().setAccessibleName("");

        PanelContentINACBGKompi1.setName("PanelContentINACBGKompi1"); // NOI18N
        PanelContentINACBGKompi1.setPreferredSize(new java.awt.Dimension(55, 55));
        PanelContentINACBGKompi1.setLayout(new java.awt.BorderLayout());
        tabPane1Kompi1.addTab("Klaim INACBG", PanelContentINACBGKompi1);

        jPanel5Kompi1.add(tabPane1Kompi1);

        jPanel1Kompi1.add(jPanel5Kompi1);

        internalFrame1Kompi1.add(jPanel1Kompi1, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1Kompi1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKeluarKompi1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKompi1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            dispose();
        }
    }//GEN-LAST:event_BtnKeluarKompi1KeyPressed

    private void BtnKeluarKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarKompi1ActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarKompi1ActionPerformed

    private void TCariKompi1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKompi1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            BtnCariKompi1ActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            BtnCariKompi1.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            BtnKeluarKompi1.requestFocus();
        }
    }//GEN-LAST:event_TCariKompi1KeyPressed

    private void BtnCariKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariKompi1ActionPerformed
        if(CmbTanggalSEPKompi1.getSelectedIndex()==0){
            tampil();
        }else{
            tampil2();
        }
//        tampil();
    }//GEN-LAST:event_BtnCariKompi1ActionPerformed

    private void BtnCariKompi1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKompi1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnCariKompi1ActionPerformed(null);
        } else {
            Valid.pindah(evt, TCariKompi1, BtnAllKompi1);
        }
    }//GEN-LAST:event_BtnCariKompi1KeyPressed

    private void BtnAllKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllKompi1ActionPerformed
        KdPjKompi1.setText("BPJ");
        NamaPjKompi1.setText("BPJS KESEHATAN");
        CmbStatusRawatKompi1.setSelectedIndex(0);
        CmbStatusKirimKompi1.setSelectedIndex(0);
        emptTeks();
        tampil();
    }//GEN-LAST:event_BtnAllKompi1ActionPerformed

    private void BtnAllKompi1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKompi1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            TCariKompi1.setText("");
            tampil();
        }
    }//GEN-LAST:event_BtnAllKompi1KeyPressed

    private void tbKompilasiKompi1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbKompilasiKompi1MouseClicked
        if (evt.getClickCount() >= 2) {
            evt.consume();
        } else {
            if (tabMode.getRowCount() != 0) {
                try {
                    getData();
                    tabPane1Kompi1.setSelectedIndex(0);
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbKompilasiKompi1MouseClicked

    private void BtnValidasiQRKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnValidasiQRKompi1ActionPerformed
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {
//            if(btnPDFKlaimINACBG.getText().equals("Ada")){
            if((Sequel.cariInteger("select count(inacbg_cetak_klaim.no_sep) from inacbg_cetak_klaim where inacbg_cetak_klaim.no_sep='"+lblNoSEPKompi1.getText()+"'")>0) && (Sequel.cariInteger("select count(inacbg_cetak_klaim_status.no_sep) from inacbg_cetak_klaim_status where inacbg_cetak_klaim_status.no_sep='"+lblNoSEPKompi1.getText()+"'")==0)){
                kirimonlineActionPerformed(evt);
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                getData();
                gabung();
                this.setCursor(Cursor.getDefaultCursor());
            }else{
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                getData();
                gabung();
                this.setCursor(Cursor.getDefaultCursor());
            }
        }
    }//GEN-LAST:event_BtnValidasiQRKompi1ActionPerformed

    private void btnSEPKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSEPKompi1ActionPerformed
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {
            Map<String, Object> param = new HashMap<>();
            param.put("namars", akses.getnamars());
            param.put("alamatrs", akses.getalamatrs());
            param.put("kotars", akses.getkabupatenrs());
            param.put("propinsirs", akses.getpropinsirs());
            param.put("kontakrs", akses.getkontakrs());
            param.put("norawat", lblNoRawatKompi1.getText());
            param.put("prb", Sequel.cariIsiSmc("select bpjs_prb.prb from bpjs_prb where bpjs_prb.no_sep = ?", lblNoSEPKompi1.getText()));
            param.put("noreg", Sequel.cariIsiSmc("select reg_periksa.no_reg from reg_periksa where reg_periksa.no_rawat = ?", lblNoRawatKompi1.getText()));
            param.put("logo", Sequel.cariGambar("select gambar.bpjs from gambar"));
            param.put("parameter", lblNoSEPKompi1.getText());
            param.put("cetakanke", 2);
            
            String pilihan = (String) JOptionPane.showInputDialog(
                null, "Silahkan pilih model SEP yang mau dilihat", "Pilih Model SEP", JOptionPane.INFORMATION_MESSAGE, null,
                new String[] {"Model 1 (Lembar SEP)", "Model 2 (IGDTL)", "Model 3 (Lembar SEP Alternatif)", "Model 4 (RJTL)"}, "Model 1 (Lembar SEP)"
            );
            
            if (pilihan == null) return;
            
            switch (pilihan) {
                case "Model 1 (Lembar SEP)":
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    if (lblStatusRawatKompi1.getText().contains("Ranap")) {
                        Valid.MyReport("rptBridgingSEP.jasper", "report", "::[ Cetak SEP ]::", param);
                    } else {
                        Valid.MyReport("rptBridgingSEP2.jasper", "report", "::[ Cetak SEP ]::", param);
                    }
                    this.setCursor(Cursor.getDefaultCursor());
                    break;
                case "Model 2 (IGDTL)":
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    if (lblStatusRawatKompi1.getText().contains("Ranap")) {
                        Valid.MyReport("rptBridgingSEP3.jasper", "report", "::[ Cetak SEP ]::", param);
                    } else {
                        Valid.MyReport("rptBridgingSEP4.jasper", "report", "::[ Cetak SEP ]::", param);
                    }
                    this.setCursor(Cursor.getDefaultCursor());
                    break;
                case "Model 3 (Lembar SEP Alternatif)":
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    if (lblStatusRawatKompi1.getText().contains("Ranap")) {
                        Valid.MyReport("rptBridgingSEP5.jasper", "report", "::[ Cetak SEP ]::", param);
                    } else {
                        Valid.MyReport("rptBridgingSEP6.jasper", "report", "::[ Cetak SEP ]::", param);
                    }
                    this.setCursor(Cursor.getDefaultCursor());
                    break;
                case "Model 4 (RJTL)":
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    if (lblStatusRawatKompi1.getText().contains("Ranap")) {
                        Valid.MyReport("rptBridgingSEP7.jasper", "report", "::[ Cetak SEP ]::", param);
                    } else {
                        Valid.MyReport("rptBridgingSEP8.jasper", "report", "::[ Cetak SEP ]::", param);
                    }
                    this.setCursor(Cursor.getDefaultCursor());
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_btnSEPKompi1ActionPerformed

    private void btnResumeRanapKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResumeRanapKompi1ActionPerformed
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu...!!!");
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Map<String, Object> param = new HashMap<>();
            param.put("namars", akses.getnamars());
            param.put("alamatrs", akses.getalamatrs());
            param.put("kotars", akses.getkabupatenrs());
            param.put("propinsirs", akses.getpropinsirs());
            param.put("kontakrs", akses.getkontakrs());
            param.put("emailrs", akses.getemailrs());
            param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
            param.put("norawat", lblNoRawatKompi1.getText());
            String waktuKeluar = "", tglKeluar = "", jamKeluar = "";
            waktuKeluar = Sequel.cariIsiSmc("select concat(tgl_keluar, ' ', jam_keluar) from kamar_inap where no_rawat = ? and stts_pulang != 'Pindah Kamar' order by concat(tgl_keluar, ' ', jam_keluar) limit 1", lblNoRawatKompi1.getText());
            if (!waktuKeluar.equals("")) {
                tglKeluar = waktuKeluar.substring(0, 10);
                jamKeluar = waktuKeluar.substring(11, 19);
            }
            String kodeDokter = Sequel.cariIsiSmc("select kd_dokter from resume_pasien_ranap where no_rawat = ?", lblNoRawatKompi1.getText());
            String namaDokter = Sequel.cariIsiSmc("select nm_dokter from dokter where kd_dokter = ?", kodeDokter);
            finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik = ?", kodeDokter);
            param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + namaDokter + "\nID " + (finger.equals("") ? kodeDokter : finger) + "\n" + Valid.SetTgl3(tglKeluar));
            param.put("ruang", Sequel.cariIsiSmc(
                "select concat(kamar_inap.kd_kamar, ' ', bangsal.nm_bangsal) from kamar_inap join kamar on kamar_inap.kd_kamar = kamar.kd_kamar "
                + "join bangsal on kamar.kd_bangsal = bangsal.kd_bangsal where kamar_inap.no_rawat = ? and kamar_inap.tgl_keluar = ? and kamar_inap.jam_keluar = ?",
                lblNoRawatKompi1.getText(), tglKeluar, jamKeluar)
            );
            param.put("tanggalkeluar", Valid.SetTgl3(tglKeluar));
            param.put("jamkeluar", jamKeluar);
            try (PreparedStatement ps = koneksi.prepareStatement("select dpjp_ranap.kd_dokter, dokter.nm_dokter from dpjp_ranap join dokter on dpjp_ranap.kd_dokter = dokter.kd_dokter where dpjp_ranap.no_rawat = ? and dpjp_ranap.kd_dokter != ?")) {
                ps.setString(1, lblNoRawatKompi1.getText());
                ps.setString(2, kodeDokter);
                try (ResultSet rs = ps.executeQuery()) {
                    int i = 2;
                    while (rs.next()) {
                        if (i == 2) {
                            finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs.getString("kd_dokter"));
                            param.put("finger2", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs.getString("nm_dokter") + "\nID " + (finger.equals("") ? rs.getString("kd_dokter") : finger) + "\n" + Valid.SetTgl3(tglKeluar));
                            param.put("namadokter2", rs.getString("nm_dokter"));
                        }
                        if (i == 3) {
                            finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs.getString("kd_dokter"));
                            param.put("finger3", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs.getString("nm_dokter") + "\nID " + (finger.equals("") ? rs.getString("kd_dokter") : finger) + "\n" + Valid.SetTgl3(tglKeluar));
                            param.put("namadokter3", rs.getString("nm_dokter"));
                        }
                        i++;
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }
            Valid.reportSmc("rptLaporanResumeRanapKompilasi.jasper", "report", "::[ Laporan Resume Pasien ]::", param);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_btnResumeRanapKompi1ActionPerformed

    private void btnAwalMedisIGDKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAwalMedisIGDKompi1ActionPerformed
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            String kodeDokter = Sequel.cariIsiSmc("select kd_dokter from penilaian_medis_igd where no_rawat = ?", lblNoRawatKompi1.getText());
            String namaDokter = Sequel.cariIsiSmc("select nm_dokter from dokter where kd_dokter = ?", kodeDokter);
            String tgl = Sequel.cariIsiSmc("select date_format(tanggal, '%d-%m-%Y') from penilaian_medis_igd where no_rawat = ?", lblNoRawatKompi1.getText());
            Map<String, Object> param = new HashMap<>();
            param.put("namars", akses.getnamars());
            param.put("alamatrs", akses.getalamatrs());
            param.put("kotars", akses.getkabupatenrs());
            param.put("propinsirs", akses.getpropinsirs());
            param.put("kontakrs", akses.getkontakrs());
            param.put("emailrs", akses.getemailrs());
            param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
            try {
                param.put("lokalis", getClass().getResource("/picture/semua.png").openStream());
            } catch (Exception e) {
            }
            finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", kodeDokter);
            param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + namaDokter + "\nID " + (finger.equals("") ? kodeDokter : finger) + "\n" + tgl);
            Valid.reportSmc("rptCetakPenilaianAwalMedisIGD.jasper", "report", "::[ Laporan Penilaian Awal Medis IGD ]::", param,
                "select reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, if (pasien.jk = 'L', 'Laki-Laki', 'Perempuan') as jk, pasien.tgl_lahir, penilaian_medis_igd.tanggal, penilaian_medis_igd.kd_dokter, " +
                "penilaian_medis_igd.anamnesis, penilaian_medis_igd.hubungan, concat_ws(', ', penilaian_medis_igd.anamnesis, nullif(penilaian_medis_igd.hubungan, '')) as hubungan_anemnesis, penilaian_medis_igd.keluhan_utama, " +
                "penilaian_medis_igd.rps, penilaian_medis_igd.rpk, penilaian_medis_igd.rpd, penilaian_medis_igd.rpo, penilaian_medis_igd.alergi, penilaian_medis_igd.keadaan, penilaian_medis_igd.gcs, penilaian_medis_igd.kesadaran, " +
                "penilaian_medis_igd.td, penilaian_medis_igd.nadi, penilaian_medis_igd.rr, penilaian_medis_igd.suhu, penilaian_medis_igd.spo, penilaian_medis_igd.bb, penilaian_medis_igd.tb, penilaian_medis_igd.kepala, penilaian_medis_igd.mata, " +
                "penilaian_medis_igd.gigi, penilaian_medis_igd.leher, penilaian_medis_igd.thoraks, penilaian_medis_igd.abdomen, penilaian_medis_igd.ekstremitas, penilaian_medis_igd.genital, penilaian_medis_igd.ket_fisik, penilaian_medis_igd.ket_lokalis, " +
                "penilaian_medis_igd.ekg, penilaian_medis_igd.rad, penilaian_medis_igd.lab, penilaian_medis_igd.diagnosis, penilaian_medis_igd.tata, dokter.nm_dokter from reg_periksa join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis " +
                "join penilaian_medis_igd on reg_periksa.no_rawat = penilaian_medis_igd.no_rawat join dokter on penilaian_medis_igd.kd_dokter = dokter.kd_dokter where penilaian_medis_igd.no_rawat = ?", lblNoRawatKompi1.getText()
            );
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_btnAwalMedisIGDKompi1ActionPerformed

    private void btnHasilLabKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHasilLabKompi1ActionPerformed
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            DlgCariPeriksaLab hasillab = new DlgCariPeriksaLab(null, false);
            hasillab.HasilLabPDFKlaimBPJS(lblNoRawatKompi1.getText(), lblNoRMKompi1.getText());
            this.setCursor(Cursor.getDefaultCursor());
        }
//        if (lblNoRawat.getText().equals("")) {
//            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
//            return;
//        }
//        
//        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//        String kamar = "", namaKamar = "";
//        int i = 0;
//        Map<String, Object> param = new HashMap<>();
//        try {
//            try (PreparedStatement ps = koneksi.prepareStatement(
//                "select pasien.jk, pasien.umur, pasien.tgl_lahir, concat_ws(', ', pasien.alamat, kelurahan.nm_kel, kecamatan.nm_kec, kabupaten.nm_kab) as alamat, " +
//                "pasien.pekerjaan, pasien.no_ktp from reg_periksa join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join kelurahan on pasien.kd_kel = kelurahan.kd_kel " +
//                "join kecamatan on pasien.kd_kec = kecamatan.kd_kec join kabupaten on pasien.kd_kab = kabupaten.kd_kab join propinsi on pasien.kd_prop = propinsi.kd_prop where reg_periksa.no_rawat = ?"
//            )) {
//                ps.setString(1, lblNoRawat.getText());
//                try (ResultSet rs = ps.executeQuery()) {
//                    if (rs.next()) {
//                        param.put("noperiksa", lblNoRawat.getText());
//                        param.put("norm", lblNoRM.getText());
//                        param.put("namapasien", lblNamaPasien.getText());
//                        param.put("jkel", rs.getString("jk"));
//                        param.put("umur", rs.getString("umur"));
//                        param.put("lahir", rs.getString("tgl_lahir"));
//                        param.put("alamat", rs.getString("alamat"));
//                        param.put("diagnosa", tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 9).toString());
//                        param.put("pekerjaan", rs.getString("pekerjaan"));
//                        param.put("noktp", rs.getString("no_ktp"));
//                        param.put("namars", akses.getnamars());
//                        param.put("alamatrs", akses.getalamatrs());
//                        param.put("kotars", akses.getkabupatenrs());
//                        param.put("propinsirs", akses.getpropinsirs());
//                        param.put("kontakrs", akses.getkontakrs());
//                        param.put("emailrs", akses.getemailrs());
//                        param.put("userid", akses.getkode());
//                        param.put("ipaddress", akses.getalamatip());
//                    }
//                }
//            }
//
//            try (PreparedStatement ps = koneksi.prepareStatement(
//                "select periksa_lab.no_rawat, periksa_lab.tgl_periksa, periksa_lab.jam, periksa_lab.status, periksa_lab.kategori, periksa_lab.kd_dokter, " +
//                "dokter.nm_dokter, periksa_lab.dokter_perujuk, perujuk.nm_dokter nm_perujuk, periksa_lab.nip, petugas.nama from periksa_lab join dokter " +
//                "on periksa_lab.kd_dokter = dokter.kd_dokter join dokter perujuk on periksa_lab.dokter_perujuk = perujuk.kd_dokter join petugas on " +
//                "periksa_lab.nip = petugas.nip where periksa_lab.no_rawat = ? group by periksa_lab.no_rawat, periksa_lab.tgl_periksa, periksa_lab.jam, " +
//                "periksa_lab.status, periksa_lab.kategori"
//            )) {
//                ps.setString(1, lblNoRawat.getText());
//                try (ResultSet rs = ps.executeQuery()) {
//                    while (rs.next()) {
//                        Sequel.deleteTemporaryLab();
//                        i = 0;
//                        if (rs.getString("status").equalsIgnoreCase("ralan")) {
//                            kamar = "Poli";
//                            namaKamar = Sequel.cariIsiSmc("select poliklinik.nm_poli from poliklinik join reg_periksa on poliklinik.kd_poli = reg_periksa.kd_poli where reg_periksa.no_rawat = ?", lblNoRawat.getText());
//                        } else {
//                            kamar = "Kamar";
//                            namaKamar = tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 8).toString();
//                        }
//                        param.put("kamar", kamar);
//                        param.put("namakamar", namaKamar);
//                        param.put("pengirim", rs.getString("nm_perujuk"));
//                        param.put("tanggal", rs.getString("tgl_periksa"));
//                        param.put("jam", rs.getString("jam"));
//                        param.put("penjab", rs.getString("nm_dokter"));
//                        param.put("petugas", rs.getString("nama"));
//                        param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
//                        finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs.getString("kd_dokter"));
//                        param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs.getString("nm_dokter") + "\nID " + (finger.equals("") ? rs.getString("kd_dokter") : finger) + "\n" + rs.getString("tgl_periksa"));
//                        finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs.getString("nip"));
//                        param.put("finger2", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs.getString("nama") + "\nID " + (finger.equals("") ? rs.getString("nip") : finger) + "\n" + rs.getString("tgl_periksa"));
//                        param.put("ttd", Sequel.cariGambarSmc("select dokter_ttdbasah.gambar_ttd from dokter_ttdbasah where dokter_ttdbasah.kd_dokter = ?", rs.getString("kd_dokter")));
//                        if (rs.getString("kategori").equals("PK")) {
//                            try (PreparedStatement ps2 = koneksi.prepareStatement(
//                                "select periksa_lab.kd_jenis_prw, jns_perawatan_lab.nm_perawatan from periksa_lab join jns_perawatan_lab " +
//                                "on periksa_lab.kd_jenis_prw = jns_perawatan_lab.kd_jenis_prw where periksa_lab.no_rawat = ? " +
//                                "and periksa_lab.tgl_periksa = ? and periksa_lab.jam = ? and periksa_lab.status = ? and periksa_lab.kategori = ?"
//                            )) {
//                                ps2.setString(1, lblNoRawat.getText());
//                                ps2.setString(2, rs.getString("tgl_periksa"));
//                                ps2.setString(3, rs.getString("jam"));
//                                ps2.setString(4, rs.getString("status"));
//                                ps2.setString(5, rs.getString("kategori"));
//                                try (ResultSet rs2 = ps2.executeQuery()) {
//                                    while (rs2.next()) {
//                                        Sequel.temporaryLab(String.valueOf(++i), rs2.getString("nm_perawatan"));
//                                        try (PreparedStatement ps3 = koneksi.prepareStatement(
//                                            "select template_laboratorium.Pemeriksaan, detail_periksa_lab.nilai, template_laboratorium.satuan, " +
//                                            "detail_periksa_lab.nilai_rujukan, detail_periksa_lab.biaya_item, detail_periksa_lab.keterangan, " +
//                                            "detail_periksa_lab.kd_jenis_prw from detail_periksa_lab join template_laboratorium on " +
//                                            "detail_periksa_lab.id_template = template_laboratorium.id_template where detail_periksa_lab.no_rawat = ? " +
//                                            "and detail_periksa_lab.kd_jenis_prw = ? and detail_periksa_lab.tgl_periksa = ? and detail_periksa_lab.jam = ? " +
//                                            "order by template_laboratorium.urut"
//                                        )) {
//                                            ps3.setString(1, lblNoRawat.getText());
//                                            ps3.setString(2, rs2.getString("kd_jenis_prw"));
//                                            ps3.setString(3, rs.getString("tgl_periksa"));
//                                            ps3.setString(4, rs.getString("jam"));
//                                            try (ResultSet rs3 = ps3.executeQuery()) {
//                                                while (rs3.next()) {
//                                                    Sequel.temporaryLab(
//                                                        String.valueOf(++i), "  " + rs3.getString("Pemeriksaan"), rs3.getString("nilai"),
//                                                        rs3.getString("satuan"), rs3.getString("nilai_rujukan"), rs3.getString("keterangan")
//                                                    );
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            try (PreparedStatement ps2 = koneksi.prepareStatement(
//                                "select noorder, tgl_permintaan, jam_permintaan " +
//                                "from permintaan_lab where no_rawat = ? and tgl_hasil = ? and jam_hasil = ?"
//                            )) {
//                                ps2.setString(1, lblNoRawat.getText());
//                                ps2.setString(2, rs.getString("tgl_periksa"));
//                                ps2.setString(3, rs.getString("jam"));
//                                try (ResultSet rs2 = ps2.executeQuery()) {
//                                    if (rs2.next()) {
//                                        param.put("nopermintaan", rs2.getString("noorder"));
//                                        param.put("tanggalpermintaan", rs2.getString("tgl_permintaan"));
//                                        param.put("jampermintaan", rs2.getString("jam_permintaan"));
//                                        Valid.reportSmc("rptPeriksaLab4PermintaanKompilasi.jasper", "report", "::[ Pemeriksaan Laboratorium ]::", param);
//                                    } else {
//                                        Valid.reportSmc("rptPeriksaLab4Kompilasi.jasper", "report", "::[ Pemeriksaan Laboratorium ]::", param);
//                                    }
//                                }
//                            }
//                        } else if (rs.getString("kategori").equals("PA")) {
//                            try (PreparedStatement ps2 = koneksi.prepareStatement(
//                                "select jns_perawatan_lab.nm_perawatan, detail_periksa_labpa.diagnosa_klinik, detail_periksa_labpa.makroskopik, detail_periksa_labpa.mikroskopik, detail_periksa_labpa.kesimpulan, detail_periksa_labpa.kesan " +
//                                "from detail_periksa_labpa join jns_perawatan_lab on detail_periksa_labpa.kd_jenis_prw = jns_perawatan_lab.kd_jenis_prw where no_rawat = ? and tgl_periksa = ? and jam = ?"
//                            )) {
//                                ps2.setString(1, lblNoRawat.getText());
//                                ps2.setString(2, rs.getString("tgl_periksa"));
//                                ps2.setString(3, rs.getString("jam"));
//                                try (ResultSet rs2 = ps2.executeQuery()) {
//                                    while (rs2.next()) {
//                                        Sequel.temporaryLab(String.valueOf(++i), rs2.getString("nm_perawatan"), rs2.getString(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5));
//                                    }
//                                }
//                            }
//                            try (PreparedStatement ps2 = koneksi.prepareStatement(
//                                "select noorder, tgl_permintaan, jam_permintaan " +
//                                "from permintaan_labpa where no_rawat = ? and tgl_hasil = ? and jam_hasil = ?"
//                            )) {
//                                ps2.setString(1, lblNoRawat.getText());
//                                ps2.setString(2, rs.getString("tgl_periksa"));
//                                ps2.setString(3, rs.getString("jam"));
//                                try (ResultSet rs2 = ps2.executeQuery()) {
//                                    param.put("ttd", Sequel.cariGambarSmc("select dokter_ttdbasah.gambar_ttd from dokter_ttdbasah where dokter_ttdbasah.kd_dokter = ?", rs.getString("kd_dokter")));
//                                    if (rs2.next()) {
//                                        param.put("nopermintaan", rs2.getString("noorder"));
//                                        param.put("tanggalpermintaan", rs2.getString("tgl_permintaan"));
//                                        param.put("jampermintaan", rs2.getString("jam_permintaan"));
//                                        Valid.reportSmc("rptPeriksaLabPermintaanPAKompilasi.jasper", "report", "::[ Pemeriksaan Laboratorium ]::", param);
//                                    } else {
//                                        Valid.reportSmc("rptPeriksaLabPAKompilasi.jasper", "report", "::[ Pemeriksaan Laboratorium ]::", param);
//                                    }
//                                }
//                            }
//                        } else if (rs.getString("kategori").equals("MB")) {
//                            JOptionPane.showMessageDialog(null, "Maaf, bagian ini belum disupport..!!\nSilahkan hubungi administrator");
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Notif : " + e);
//            JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada saat mencari hasil pemeriksaan lab!");
//        }
//        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnHasilLabKompi1ActionPerformed

    private void btnHasilRadKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHasilRadKompi1ActionPerformed
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            DlgCariPeriksaRadiologi hasilrad = new DlgCariPeriksaRadiologi(null, false);
            hasilrad.RadPdfKlaimBPJS(lblNoRawatKompi1.getText(), lblNoRMKompi1.getText());
            this.setCursor(Cursor.getDefaultCursor());
        }
//        if (lblNoRawat.getText().equals("")) {
//            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
//        } else {
//            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//            try (PreparedStatement ps = koneksi.prepareStatement(
//                "select pasien.jk, date_format(pasien.tgl_lahir, '%d-%m-%Y') as tgllahir, concat(reg_periksa.umurdaftar, ' ', reg_periksa.sttsumur) as umur, concat_ws(', ', pasien.alamat, kelurahan.nm_kel, kecamatan.nm_kec, kabupaten.nm_kab) as alamat, periksa_radiologi.dokter_perujuk, "
//                + "dokter_perujuk.nm_dokter nm_dokter_perujuk, periksa_radiologi.tgl_periksa, periksa_radiologi.jam, periksa_radiologi.kd_dokter, dokter.nm_dokter, periksa_radiologi.nip, petugas.nama nama_petugas, jns_perawatan_radiologi.nm_perawatan, "
//                + "periksa_radiologi.status, periksa_radiologi.proyeksi, periksa_radiologi.kV, periksa_radiologi.mAS, periksa_radiologi.FFD, periksa_radiologi.BSF, periksa_radiologi.inak, periksa_radiologi.jml_penyinaran, periksa_radiologi.dosis "
//                + "from periksa_radiologi join reg_periksa on periksa_radiologi.no_rawat = reg_periksa.no_rawat join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join dokter dokter_perujuk on periksa_radiologi.dokter_perujuk = dokter_perujuk.kd_dokter "
//                + "join dokter on periksa_radiologi.kd_dokter = dokter.kd_dokter join petugas on periksa_radiologi.nip = petugas.nip join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw = jns_perawatan_radiologi.kd_jenis_prw "
//                + "left join kelurahan on pasien.kd_kel = kelurahan.kd_kel left join kecamatan on pasien.kd_kec = kecamatan.kd_kec left join kabupaten on pasien.kd_kab = kabupaten.kd_kab where periksa_radiologi.no_rawat = ?"
//            )) {
//                ps.setString(1, lblNoRawat.getText());
//                try (ResultSet rs = ps.executeQuery()) {
//                    while (rs.next()) {
//                        String pemeriksaan = rs.getString("nm_perawatan")
//                            + (rs.getString("proyeksi") == null || rs.getString("proyeksi").equals("") ? "" : " dengan Proyeksi : " + rs.getString("proyeksi"))
//                            + (rs.getString("kV") == null || rs.getString("kV").equals("") ? "" : ", kV : " + rs.getString("kV"))
//                            + (rs.getString("mAS") == null || rs.getString("mAS").equals("") ? "" : ", mAS : " + rs.getString("mAS"))
//                            + (rs.getString("FFD") == null || rs.getString("FFD").equals("") ? "" : ", FFD : " + rs.getString("FFD"))
//                            + (rs.getString("BSF") == null || rs.getString("BSF").equals("") ? "" : ", BSF : " + rs.getString("BSF"))
//                            + (rs.getString("Inak") == null || rs.getString("Inak").equals("") ? "" : ", Inak : " + rs.getString("Inak"))
//                            + (rs.getString("jml_penyinaran") == null || rs.getString("jml_penyinaran").equals("") ? "" : ", Jumlah penyinaran : " + rs.getString("jml_penyinaran"))
//                            + (rs.getString("dosis") == null || rs.getString("dosis").equals("") ? "" : ", Dosis Radiasi : " + rs.getString("dosis"));
//                        Map<String, Object> param = new HashMap<>();
//                        param.put("noperiksa", lblNoRawat.getText());
//                        param.put("norm", lblNoRM.getText());
//                        param.put("namapasien", lblNamaPasien.getText());
//                        param.put("jkel", rs.getString("jk"));
//                        param.put("umur", rs.getString("umur"));
//                        param.put("lahir", rs.getString("tgllahir"));
//                        param.put("pengirim", rs.getString("nm_dokter_perujuk"));
//                        param.put("tanggal", rs.getString("tgl_periksa"));
//                        param.put("penjab", rs.getString("nm_dokter"));
//                        param.put("petugas", rs.getString("nama_petugas"));
//                        param.put("alamat", rs.getString("alamat"));
//                        String kamar = "", kelas = "", namaKamar = "", noRawatIbu = "";
//                        if (lblStatusRawat.getText().contains("Ranap")) {
//                            noRawatIbu = Sequel.cariIsiSmc("select no_rawat from ranap_gabung where no_rawat2 = ?", lblNoRawat.getText());
//                            if (!noRawatIbu.equals("")) {
//                                kamar = Sequel.cariIsiSmc("select ifnull(kd_kamar, '') from kamar_inap where no_rawat = ? order by tgl_masuk desc, jam_masuk desc limit 1", noRawatIbu);
//                                kelas = Sequel.cariIsiSmc("select kamar.kelas from kamar inner join kamar_inap on kamar.kd_kamar = kamar_inap.kd_kamar where no_rawat = ? order by str_to_date(concat(kamar_inap.tgl_masuk, ' ', kamar_inap.jam_masuk), '%Y-%m-%d %H:%i:%s') desc limit 1", noRawatIbu);
//                            } else {
//                                kamar = Sequel.cariIsiSmc("select ifnull(kd_kamar, '') from kamar_inap where no_rawat = ? order by tgl_masuk desc limit 1", lblNoRawat.getText());
//                                kelas = Sequel.cariIsiSmc("select kamar.kelas from kamar inner join kamar_inap on kamar.kd_kamar = kamar_inap.kd_kamar where no_rawat = ? order by str_to_date(concat(kamar_inap.tgl_masuk, ' ', kamar_inap.jam_masuk), '%Y-%m-%d %H:%i:%s') desc limit 1", lblNoRawat.getText());
//                            }
//                            namaKamar = kamar + ", " + Sequel.cariIsiSmc("select bangsal.nm_bangsal from bangsal inner join kamar on bangsal.kd_bangsal = kamar.kd_bangsal where kamar.kd_kamar = ?", kamar);
//                            kamar = "Kamar";
//                        } else {
//                            kelas = "Rawat Jalan";
//                            kamar = "Poli";
//                            namaKamar = Sequel.cariIsiSmc("select poliklinik.nm_poli from poliklinik inner join reg_periksa on poliklinik.kd_poli = reg_periksa.kd_poli where reg_periksa.no_rawat = ?", lblNoRawat.getText());
//                        }
//                        param.put("kamar", kamar);
//                        param.put("namakamar", namaKamar);
//                        param.put("pemeriksaan", pemeriksaan);
//                        param.put("jam", rs.getString("jam"));
//                        param.put("namars", akses.getnamars());
//                        param.put("alamatrs", akses.getalamatrs());
//                        param.put("kotars", akses.getkabupatenrs());
//                        param.put("propinsirs", akses.getpropinsirs());
//                        param.put("kontakrs", akses.getkontakrs());
//                        param.put("emailrs", akses.getemailrs());
//                        param.put("hasil", Sequel.cariIsiSmc("select hasil from hasil_radiologi where no_rawat = ? and tgl_periksa = ? and jam = ?", lblNoRawat.getText(), rs.getString("tgl_periksa"), rs.getString("jam")));
//                        param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
//                        finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs.getString("kd_dokter"));
//                        param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs.getString("nm_dokter") + "\nID " + (finger.equals("") ? rs.getString("kd_dokter") : finger) + "\n" + new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("tgl_periksa")));
//                        finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs.getString("nip"));
//                        param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs.getString("nama_petugas") + "\nID " + (finger.equals("") ? rs.getString("nip") : finger) + "\n" + new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("tgl_periksa")));
//                        param.put("ttd", Sequel.cariGambarSmc("select dokter_ttdbasah.gambar_ttd from dokter_ttdbasah where dokter_ttdbasah.kd_dokter = ?", rs.getString("kd_dokter")));
//                        Valid.reportSmc("rptPeriksaRadiologiKompilasi.jasper", "report", "::[ Pemeriksaan Radiologi ]::", param);
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("Notif : " + e);
//                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada saat mencari hasil pemeriksaan Radiologi!");
//            }
//            this.setCursor(Cursor.getDefaultCursor());
//        }
    }//GEN-LAST:event_btnHasilRadKompi1ActionPerformed

    private void btnSurkonKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSurkonKompi1ActionPerformed
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Map<String, Object> param = new HashMap<>();
            param.put("namars", akses.getnamars());
            param.put("alamatrs", akses.getalamatrs());
            param.put("kotars", akses.getkabupatenrs());
            param.put("propinsirs", akses.getpropinsirs());
            param.put("kontakrs", akses.getkontakrs());
            param.put("logo", Sequel.cariGambar("select gambar.bpjs from gambar"));
            String noSurat = Sequel.cariIsiSmc("select noskdp from bridging_sep where no_sep = ?", lblNoSEPKompi1.getText());
            String tglSurat = Sequel.cariIsiSmc("select date_format(tgl_surat, '%d-%m-%Y') from bridging_surat_kontrol_bpjs where no_surat = ?", noSurat);
            String kodeDokter = Sequel.cariIsiSmc("select kd_dokter from maping_dokter_dpjpvclaim where maping_dokter_dpjpvclaim.kd_dokter_bpjs = (select bridging_surat_kontrol_bpjs.kd_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat = ?)", noSurat);
            String namaDokter = Sequel.cariIsiSmc("select nm_dokter from dokter where kd_dokter = ?", kodeDokter);
            param.put("parameter", Sequel.cariIsiSmc("select noskdp from bridging_sep where no_sep = ?", lblNoSEPKompi1.getText()));
            param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + namaDokter + "\nID " + kodeDokter + "\n" + tglSurat);
            Valid.reportSmc(
                "rptBridgingSuratKontrol2.jasper", "report", "::[ Data Surat Kontrol VClaim ]::", param,
                "select bridging_sep.no_rawat, bridging_sep.no_sep, bridging_sep.no_kartu, bridging_sep.nomr, bridging_sep.nama_pasien, bridging_sep.tanggal_lahir, bridging_sep.jkel, bridging_sep.diagawal, bridging_sep.nmdiagnosaawal, bridging_surat_kontrol_bpjs.tgl_surat, "
                + "bridging_surat_kontrol_bpjs.no_surat, bridging_surat_kontrol_bpjs.tgl_rencana, bridging_surat_kontrol_bpjs.kd_dokter_bpjs, bridging_surat_kontrol_bpjs.nm_dokter_bpjs, bridging_surat_kontrol_bpjs.kd_poli_bpjs, bridging_surat_kontrol_bpjs.nm_poli_bpjs "
                + "from bridging_sep join bridging_surat_kontrol_bpjs on bridging_surat_kontrol_bpjs.no_sep = bridging_sep.no_sep where bridging_surat_kontrol_bpjs.no_surat = ?", Sequel.cariIsiSmc("select noskdp from bridging_sep where no_sep = ?", lblNoSEPKompi1.getText())
            );
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_btnSurkonKompi1ActionPerformed

    private void btnSPRIKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSPRIKompi1ActionPerformed
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih data pasien terlebih dahulu!");
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Map<String, Object> param = new HashMap<>();
            param.put("namars", akses.getnamars());
            param.put("alamatrs", akses.getalamatrs());
            param.put("kotars", akses.getkabupatenrs());
            param.put("propinsirs", akses.getpropinsirs());
            param.put("kontakrs", akses.getkontakrs());
            param.put("logo", Sequel.cariGambar("select gambar.bpjs from gambar"));
            param.put("parameter", lblNoRawatKompi1.getText());
            String noSPRI = Sequel.cariIsiSmc("select no_surat from bridging_surat_pri_bpjs where no_rawat = ? order by no_surat desc", lblNoRawatKompi1.getText());
            String kodeDokter = Sequel.cariIsiSmc("Select kd_dokter_bpjs from bridging_surat_pri_bpjs where no_surat = ?", noSPRI);
            String namaDokter = Sequel.cariIsiSmc("select nm_dokter_bpjs from maping_dokter_dpjpvclaim where kd_dokter_bpjs = ?", kodeDokter);
            String tglSPRI = Sequel.cariIsiSmc("select date_format(tgl_rencana, '%d-%m-%Y') from bridging_surat_pri_bpjs where no_surat = ?", noSPRI);
            param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + namaDokter + "\nID " + kodeDokter + "\n" + tglSPRI);
            Valid.reportSmc("rptBridgingSuratPRI2.jasper", "report", "::[ Data Surat PRI VClaim ]::", param,
                "select bridging_surat_pri_bpjs.*, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, pasien.jk "
                + "from reg_periksa join bridging_surat_pri_bpjs on bridging_surat_pri_bpjs.no_rawat = reg_periksa.no_rawat "
                + "join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis where bridging_surat_pri_bpjs.no_surat = ?", noSPRI
            );
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_btnSPRIKompi1ActionPerformed

    private void BtnSimpanDiagnosaKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanDiagnosaKompi1ActionPerformed
        if (!lblNoRawatKompi1.getText().equals("")) {
            panelDiagnosaSmcKompi1.simpan();
            tampilINACBG();
        }
    }//GEN-LAST:event_BtnSimpanDiagnosaKompi1ActionPerformed

    private void BtnHapusDiagnosaKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusDiagnosaKompi1ActionPerformed
        if (!lblNoRawatKompi1.getText().equals("")) {
            panelDiagnosaSmcKompi1.hapus();
            tampilINACBG();
        }
    }//GEN-LAST:event_BtnHapusDiagnosaKompi1ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        revalidate();
        Dimension newD = new Dimension(jPanel2Kompi1.getWidth() - 32, panelBiasa1Kompi1.getPreferredSize().height);
        panelDiagnosaSmcKompi1.setPreferredSize(new Dimension(newD.width, panelDiagnosaSmcKompi1.getPreferredSize().height));
        panelDiagnosaSmcKompi1.setSize(new Dimension(newD.width, panelDiagnosaSmcKompi1.getPreferredSize().height));
        panelDiagnosaSmcKompi1.revalidate(newD.width);
        panelBiasa1Kompi1.setPreferredSize(newD);
        panelBiasa1Kompi1.setSize(newD);
        scrollPane1Kompi1.setPreferredSize(newD);
        scrollPane1Kompi1.setSize(newD);
        BtnHapusDiagnosaKompi1.setLocation(panelBiasa1Kompi1.getWidth() - BtnHapusDiagnosaKompi1.getWidth() - 4, BtnHapusDiagnosaKompi1.getY());
        revalidate();
    }//GEN-LAST:event_formWindowOpened

    private void ppUpdateTanggalPulangSEPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppUpdateTanggalPulangSEPActionPerformed
        if (tbKompilasiKompi1.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(rootPane, "Silahkan pilih data SEP ranap pasien terlebih dahulu!");
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try (PreparedStatement pspulang = koneksi.prepareStatement(
                "select bridging_sep.no_rawat, bridging_sep.no_sep, pasien.no_rkm_medis, pasien.nm_pasien, if (bridging_sep.tglpulang is null or bridging_sep.tglpulang = '0000-00-00 00:00:00', " +
                "(select if (max(concat(kamar_inap.tgl_keluar, ' ',kamar_inap.jam_keluar)) = '0000-00-00 00:00:00' or max(concat(kamar_inap.tgl_keluar, ' ', kamar_inap.jam_keluar)) is null, now(), max(concat(kamar_inap.tgl_keluar, ' ', kamar_inap.jam_keluar))) " +
                "from kamar_inap where kamar_inap.no_rawat = bridging_sep.no_rawat), bridging_sep.tglpulang) as tglpulang from bridging_sep join reg_periksa on bridging_sep.no_rawat = reg_periksa.no_rawat " +
                "join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis where bridging_sep.jnspelayanan = '1' and bridging_sep.no_sep = ? order by bridging_sep.tglsep desc limit 1"
            )) {
                pspulang.setString(1, tbKompilasiKompi1.getValueAt(tbKompilasiKompi1.getSelectedRow(), 1).toString());
                try (ResultSet rspulang = pspulang.executeQuery()) {
                    if (rspulang.next()) {
                        WindowUpdatePulang.setSize(608, 264);
                        WindowUpdatePulang.setLocationRelativeTo(internalFrame1Kompi1);
                        TNoRwPulang.setText(rspulang.getString("no_rawat"));
                        TNoSEPRanapPulang.setText(rspulang.getString("no_sep"));
                        TNoRMPulang.setText(rspulang.getString("no_rkm_medis"));
                        TPasienPulang.setText(rspulang.getString("nm_pasien"));
                        TanggalPulang.setDate(rspulang.getTimestamp("tglpulang"));
                        WindowUpdatePulang.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Pasien tersebut belum terbit SEP, silahkan hubungi bagian terkait..!!");
                        TCariKompi1.requestFocus();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_ppUpdateTanggalPulangSEPActionPerformed

    private void BtnCloseIn8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseIn8ActionPerformed
        WindowUpdatePulang.dispose();
    }//GEN-LAST:event_BtnCloseIn8ActionPerformed

    private void BtnSimpan8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpan8ActionPerformed
        if (TNoRwPulang.getText().equals("") || TNoSEPRanapPulang.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Silahkan pilih data pasiennya terlebih dahulu..!!");
            return;
        }
        try {
            ApiBPJS api = new ApiBPJS();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
            String utc = String.valueOf(api.GetUTCdatetimeAsString());
            headers.add("X-Timestamp", utc);
            headers.add("X-Signature", api.getHmac(utc));
            headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
            String URL = koneksiDB.URLAPIBPJS() + "/SEP/2.0/updtglplg";
            String json = "{"
            + "\"request\": {"
            + "\"t_sep\": {"
            + "\"noSep\": \"" + TNoSEPRanapPulang.getText() + "\","
            + "\"statusPulang\": \"" + StatusPulang.getSelectedItem().toString().substring(0, 1) + "\","
            + "\"noSuratMeninggal\": \"" + NoSuratKematian.getText().trim() + "\","
            + "\"tglMeninggal\": \"" + (NoSuratKematian.getText().trim().equals("") ? "" : Valid.SetTgl(TanggalKematian.getSelectedItem().toString())) + "\","
            + "\"tglPulang\": \"" + Valid.SetTgl(TanggalPulang.getSelectedItem().toString()) + "\","
            + "\"noLPManual\": \"" + NoLPManual.getText().trim() + "\","
            + "\"user\": \"" + akses.getkode().trim().substring(0, 9) + "\""
            + "}"
            + "}"
            + "}";
            System.out.println("JSON : " + json);
            HttpEntity entity = new HttpEntity(json, headers);
            JsonNode root, nameNode;
            ObjectMapper mapper = new ObjectMapper();
            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.PUT, entity, String.class).getBody());
            nameNode = root.path("metaData");
            System.out.println("code : " + nameNode.path("code").asText());
            System.out.println("message : " + nameNode.path("message").asText());
            if (nameNode.path("code").asText().equals("200")) {
                Sequel.mengupdateSmc("bridging_sep", "tglpulang = ?", "no_sep = ?",
                    Valid.SetTgl(TanggalPulang.getSelectedItem().toString()) + " " + TanggalPulang.getSelectedItem().toString().substring(11, 19),
                    TNoSEPRanapPulang.getText());
                JOptionPane.showMessageDialog(rootPane, "Proses update pulang di BPJS selesai!");
                WindowUpdatePulang.dispose();
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            if (e.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(rootPane, "Koneksi ke server BPJS terputus..!!");
            }
        }
    }//GEN-LAST:event_BtnSimpan8ActionPerformed

    private void StatusPulangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_StatusPulangItemStateChanged
        if (StatusPulang.getSelectedIndex() == 2) {
            NoSuratKematian.setEditable(true);
            TanggalKematian.setEnabled(true);
        } else {
            NoSuratKematian.setEditable(false);
            TanggalKematian.setEnabled(false);
        }
    }//GEN-LAST:event_StatusPulangItemStateChanged

    private void ppBerkasDigitalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBerkasDigitalActionPerformed
        if (tbKompilasiKompi1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Maaf, table masih kosong...!!!!");
            TCariKompi1.requestFocus();
        } else {
            if (tbKompilasiKompi1.getSelectedRow() > -1) {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                DlgBerkasRawat berkas = new DlgBerkasRawat(null, true);
                berkas.setJudul("::[ Berkas Digital Perawatan ]::", "berkasrawat/pages");
                try {
                    if (akses.gethapus_berkas_digital_perawatan() == true) {
                        berkas.loadURL("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/" + "berkasrawat/login2.php?act=login&usere=" + koneksiDB.USERHYBRIDWEB() + "&passwordte=" + koneksiDB.PASHYBRIDWEB() + "&no_rawat=" + lblNoRawatKompi1.getText());
                    } else {
                        berkas.loadURL("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/" + "berkasrawat/login2nonhapus.php?act=login&usere=" + koneksiDB.USERHYBRIDWEB() + "&passwordte=" + koneksiDB.PASHYBRIDWEB() + "&no_rawat=" + lblNoRawatKompi1.getText());
                    }
                } catch (Exception ex) {
                    System.out.println("Notifikasi : " + ex);
                }
                berkas.setSize(internalFrame1Kompi1.getWidth() - 20, internalFrame1Kompi1.getHeight() - 20);
                berkas.setLocationRelativeTo(internalFrame1Kompi1);
                berkas.setVisible(true);
                this.setCursor(Cursor.getDefaultCursor());
            }
        }
    }//GEN-LAST:event_ppBerkasDigitalActionPerformed

    private void ppRiwayatPerawatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppRiwayatPerawatanActionPerformed
        if (tbKompilasiKompi1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Maaf, data pasien sudah habis...!!!!");
            TCariKompi1.requestFocus();
        } else if (tbKompilasiKompi1.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Maaf, Silahkan anda pilih dulu data kamar inap pada table...!!!");
            TCariKompi1.requestFocus();
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (resume == null) {
                resume = new RMRiwayatPerawatan(null, false);
            }
            resume.setNoRMKompilasi(tbKompilasiKompi1.getValueAt(tbKompilasiKompi1.getSelectedRow(), 0).toString(), tbKompilasiKompi1.getValueAt(tbKompilasiKompi1.getSelectedRow(), 2).toString());
            resume.setSize(internalFrame1Kompi1.getWidth() - 20, internalFrame1Kompi1.getHeight() - 20);
            resume.setLocationRelativeTo(internalFrame1Kompi1);
            resume.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_ppRiwayatPerawatanActionPerformed

    private void btnRiwayatPasienKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRiwayatPasienKompi1ActionPerformed
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu..!!");
            return;
        }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (resume == null) {
            resume = new RMRiwayatPerawatan(null, false);
        }
        resume.setNoRMKompilasi(lblNoRawatKompi1.getText(), lblNoRMKompi1.getText());
        resume.setSize(internalFrame1Kompi1.getWidth() - 20, internalFrame1Kompi1.getHeight() - 20);
        resume.setLocationRelativeTo(internalFrame1Kompi1);
        resume.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnRiwayatPasienKompi1ActionPerformed

    private void btnInvoiceKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvoiceKompi1ActionPerformed
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {
            Valid.panggilUrl("berkasrawat/loginlihatbilling.php?act=login&norawat=" + lblNoRawatKompi1.getText() + "&usere=" + koneksiDB.USERHYBRIDWEB() + "&passwordte=" + koneksiDB.PASHYBRIDWEB());
        }
    }//GEN-LAST:event_btnInvoiceKompi1ActionPerformed

    private void btnPDFKlaimINACBGKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPDFKlaimINACBGKompi1ActionPerformed

        if (lblNoSEPKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu!");
        } else {
            String file = "inacbg/" + Sequel.cariIsiSmc("select path from inacbg_cetak_klaim where no_sep = ?", lblNoSEPKompi1.getText());
            file = file + "?hash=" + DigestUtils.sha256Hex(lblNoSEPKompi1.getText() + Instant.now().toString());
            Valid.panggilUrl(file);
        }
    }//GEN-LAST:event_btnPDFKlaimINACBGKompi1ActionPerformed

    private void btnTriaseIGDKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTriaseIGDKompi1ActionPerformed
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih pasien terlebih dahulu!");
            return;
        }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        String detailTriase = "";
        int i = 0;
        Map<String, Object> param = new HashMap<>();
        param.put("namars", akses.getnamars());
        param.put("alamatrs", akses.getalamatrs());
        param.put("kotars", akses.getkabupatenrs());
        param.put("propinsirs", akses.getpropinsirs());
        param.put("kontakrs", akses.getkontakrs());
        param.put("emailrs", akses.getemailrs());
        param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
        Sequel.deleteTemporary();
        try (PreparedStatement ps = koneksi.prepareStatement(
            "select t.no_rawat, " +
            "(select count(*) from data_triase_igddetail_skala1 s1 where s1.no_rawat = t.no_rawat) as cs1, " +
            "(select count(*) from data_triase_igddetail_skala2 s2 where s2.no_rawat = t.no_rawat) as cs2, " +
            "(select count(*) from data_triase_igddetail_skala3 s3 where s3.no_rawat = t.no_rawat) as cs3, " +
            "(select count(*) from data_triase_igddetail_skala4 s4 where s4.no_rawat = t.no_rawat) as cs4, " +
            "(select count(*) from data_triase_igddetail_skala5 s5 where s5.no_rawat = t.no_rawat) as cs5 " +
            "from data_triase_igd t where t.no_rawat = ?"
        )) {
            ps.setString(1, lblNoRawatKompi1.getText());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (rs.getInt("cs1") > 0) {
                        try (PreparedStatement ps1 = koneksi.prepareStatement(
                            "select data_triase_igdprimer.keluhan_utama, data_triase_igdprimer.kebutuhan_khusus, data_triase_igdprimer.catatan, data_triase_igdprimer.plan, data_triase_igdprimer.tanggaltriase, " +
                            "data_triase_igdprimer.nik, data_triase_igd.tekanan_darah, data_triase_igd.nadi, data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, data_triase_igd.nyeri, " +
                            "data_triase_igd.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir, pegawai.nama, data_triase_igd.tgl_kunjungan, data_triase_igd.cara_masuk, master_triase_macam_kasus.macam_kasus " +
                            "from data_triase_igdprimer join data_triase_igd on data_triase_igdprimer.no_rawat = data_triase_igd.no_rawat join master_triase_macam_kasus on data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus " +
                            "join reg_periksa on data_triase_igdprimer.no_rawat = reg_periksa.no_rawat join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join pegawai on data_triase_igdprimer.nik = pegawai.nik where data_triase_igd.no_rawat = ?"
                        )) {
                            ps1.setString(1, lblNoRawatKompi1.getText());
                            try (ResultSet rs1 = ps1.executeQuery()) {
                                if (rs1.next()) {
                                    param.put("norawat", rs1.getString("no_rawat"));
                                    param.put("norm", rs1.getString("no_rkm_medis"));
                                    param.put("namapasien", rs1.getString("nm_pasien"));
                                    param.put("tanggallahir", rs1.getDate("tgl_lahir"));
                                    param.put("jk", rs1.getString("jk").replaceAll("L", "Laki-Laki").replaceAll("P", "Perempuan"));
                                    param.put("tanggalkunjungan", rs1.getDate("tgl_kunjungan"));
                                    param.put("jamkunjungan", rs1.getString("tgl_kunjungan").substring(11, 19));
                                    param.put("caradatang", rs1.getString("cara_masuk"));
                                    param.put("macamkasus", rs1.getString("macam_kasus"));
                                    param.put("keluhanutama", rs1.getString("keluhan_utama"));
                                    param.put("kebutuhankhusus", rs1.getString("kebutuhan_khusus"));
                                    param.put("plan", rs1.getString("plan"));
                                    param.put("tanggaltriase", rs1.getDate("tanggaltriase"));
                                    param.put("jamtriase", rs1.getString("tanggaltriase").substring(11, 19));
                                    param.put("pegawai", rs1.getString("nama"));
                                    param.put("catatan", rs1.getString("catatan"));
                                    param.put("tandavital", "Suhu (C) : " + rs1.getString("suhu") + ", Nyeri : " + rs1.getString("nyeri") + ", Tensi : " + rs1.getString("tekanan_darah") + ", Nadi(/menit) : " + rs1.getString("nadi") + ", Saturasi O²(%) : " + rs1.getString("saturasi_o2") + ", Respirasi(/menit) : " + rs1.getString("pernapasan"));
                                    finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs1.getString("nik"));
                                    param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs1.getString("nama") + "\nID " + (finger.equals("") ? rs1.getString("nik") : finger) + "\n" + Valid.SetTgl3(rs1.getString("tanggaltriase")));
                                    try (PreparedStatement ps2 = koneksi.prepareStatement(
                                        "select master_triase_pemeriksaan.kode_pemeriksaan, master_triase_pemeriksaan.nama_pemeriksaan from master_triase_pemeriksaan " +
                                        "join master_triase_skala1 on master_triase_pemeriksaan.kode_pemeriksaan = master_triase_skala1.kode_pemeriksaan " +
                                        "join data_triase_igddetail_skala1 on master_triase_skala1.kode_skala1 = data_triase_igddetail_skala1.kode_skala1 " +
                                        "where data_triase_igddetail_skala1.no_rawat = ? group by master_triase_pemeriksaan.kode_pemeriksaan " +
                                        "order by master_triase_pemeriksaan.kode_pemeriksaan"
                                    )) {
                                        ps2.setString(1, lblNoRawatKompi1.getText());
                                        try (ResultSet rs2 = ps2.executeQuery()) {
                                            while (rs2.next()) {
                                                detailTriase = "";
                                                try (PreparedStatement ps3 = koneksi.prepareStatement(
                                                    "select master_triase_skala1.pengkajian_skala1 from master_triase_skala1 " +
                                                    "join data_triase_igddetail_skala1 on master_triase_skala1.kode_skala1 = data_triase_igddetail_skala1.kode_skala1 " +
                                                    "where master_triase_skala1.kode_pemeriksaan = ? and data_triase_igddetail_skala1.no_rawat = ? " +
                                                    "order by data_triase_igddetail_skala1.kode_skala1"
                                                )) {
                                                    ps3.setString(1, rs2.getString(1));
                                                    ps3.setString(2, lblNoRawatKompi1.getText());
                                                    try (ResultSet rs3 = ps3.executeQuery()) {
                                                        while (rs3.next()) {
                                                            detailTriase = rs3.getString(1) + ", " + detailTriase;
                                                        }
                                                    }
                                                }
                                                detailTriase = detailTriase.substring(0, detailTriase.length() - 2);
                                                Sequel.temporary(String.valueOf(++i), rs2.getString("nama_pemeriksaan"), detailTriase);
                                            }
                                        }
                                    }
                                    Valid.reportTempSmc("rptLembarTriaseSkala1.jasper", "report", "::[ Triase Skala 1 ]::", param);
                                }
                            }
                        }
                    } else if (rs.getInt("cs2") > 0) {
                        try (PreparedStatement ps1 = koneksi.prepareStatement(
                            "select data_triase_igdprimer.keluhan_utama, data_triase_igdprimer.kebutuhan_khusus, data_triase_igdprimer.catatan, data_triase_igdprimer.plan, data_triase_igdprimer.tanggaltriase, " +
                            "data_triase_igdprimer.nik, data_triase_igd.tekanan_darah, data_triase_igd.nadi, data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, data_triase_igd.nyeri, " +
                            "data_triase_igd.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir, pegawai.nama, data_triase_igd.tgl_kunjungan, data_triase_igd.cara_masuk, master_triase_macam_kasus.macam_kasus " +
                            "from data_triase_igdprimer join data_triase_igd on data_triase_igdprimer.no_rawat = data_triase_igd.no_rawat join master_triase_macam_kasus on data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus " +
                            "join reg_periksa on data_triase_igdprimer.no_rawat = reg_periksa.no_rawat join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join pegawai on data_triase_igdprimer.nik = pegawai.nik where data_triase_igd.no_rawat = ?"
                        )) {
                            ps1.setString(1, lblNoRawatKompi1.getText());
                            try (ResultSet rs1 = ps1.executeQuery()) {
                                if (rs1.next()) {
                                    param.put("norawat", rs1.getString("no_rawat"));
                                    param.put("norm", rs1.getString("no_rkm_medis"));
                                    param.put("namapasien", rs1.getString("nm_pasien"));
                                    param.put("tanggallahir", rs1.getDate("tgl_lahir"));
                                    param.put("jk", rs1.getString("jk").replaceAll("L", "Laki-Laki").replaceAll("P", "Perempuan"));
                                    param.put("tanggalkunjungan", rs1.getDate("tgl_kunjungan"));
                                    param.put("jamkunjungan", rs1.getString("tgl_kunjungan").substring(11, 19));
                                    param.put("caradatang", rs1.getString("cara_masuk"));
                                    param.put("macamkasus", rs1.getString("macam_kasus"));
                                    param.put("keluhanutama", rs1.getString("keluhan_utama"));
                                    param.put("kebutuhankhusus", rs1.getString("kebutuhan_khusus"));
                                    param.put("plan", rs1.getString("plan"));
                                    param.put("tanggaltriase", rs1.getDate("tanggaltriase"));
                                    param.put("jamtriase", rs1.getString("tanggaltriase").substring(11, 19));
                                    param.put("pegawai", rs1.getString("nama"));
                                    param.put("catatan", rs1.getString("catatan"));
                                    param.put("tandavital", "Suhu (C) : " + rs1.getString("suhu") + ", Nyeri : " + rs1.getString("nyeri") + ", Tensi : " + rs1.getString("tekanan_darah") + ", Nadi(/menit) : " + rs1.getString("nadi") + ", Saturasi O²(%) : " + rs1.getString("saturasi_o2") + ", Respirasi(/menit) : " + rs1.getString("pernapasan"));
                                    finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs1.getString("nik"));
                                    param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs1.getString("nama") + "\nID " + (finger.equals("") ? rs1.getString("nik") : finger) + "\n" + Valid.SetTgl3(rs1.getString("tanggaltriase")));
                                    try (PreparedStatement ps2 = koneksi.prepareStatement(
                                        "select master_triase_pemeriksaan.kode_pemeriksaan, master_triase_pemeriksaan.nama_pemeriksaan from master_triase_pemeriksaan " +
                                        "join master_triase_skala2 on master_triase_pemeriksaan.kode_pemeriksaan = master_triase_skala2.kode_pemeriksaan " +
                                        "join data_triase_igddetail_skala2 on master_triase_skala2.kode_skala2 = data_triase_igddetail_skala2.kode_skala2 " +
                                        "where data_triase_igddetail_skala2.no_rawat = ? group by master_triase_pemeriksaan.kode_pemeriksaan " +
                                        "order by master_triase_pemeriksaan.kode_pemeriksaan"
                                    )) {
                                        ps2.setString(1, lblNoRawatKompi1.getText());
                                        try (ResultSet rs2 = ps2.executeQuery()) {
                                            while (rs2.next()) {
                                                detailTriase = "";
                                                try (PreparedStatement ps3 = koneksi.prepareStatement(
                                                    "select master_triase_skala2.pengkajian_skala2 from master_triase_skala2 " +
                                                    "join data_triase_igddetail_skala2 on master_triase_skala2.kode_skala2 = data_triase_igddetail_skala2.kode_skala2 " +
                                                    "where master_triase_skala2.kode_pemeriksaan = ? and data_triase_igddetail_skala2.no_rawat = ? " +
                                                    "order by data_triase_igddetail_skala2.kode_skala2"
                                                )) {
                                                    ps3.setString(1, rs2.getString(1));
                                                    ps3.setString(2, lblNoRawatKompi1.getText());
                                                    try (ResultSet rs3 = ps3.executeQuery()) {
                                                        while (rs3.next()) {
                                                            detailTriase = rs3.getString(1) + ", " + detailTriase;
                                                        }
                                                    }
                                                }
                                                detailTriase = detailTriase.substring(0, detailTriase.length() - 2);
                                                Sequel.temporary(String.valueOf(++i), rs2.getString("nama_pemeriksaan"), detailTriase);
                                            }
                                        }
                                    }
                                    Valid.reportTempSmc("rptLembarTriaseSkala2.jasper", "report", "::[ Triase Skala 2 ]::", param);
                                }
                            }
                        }
                    } else if (rs.getInt("cs3") > 0) {
                        try (PreparedStatement ps1 = koneksi.prepareStatement(
                            "select data_triase_igdsekunder.anamnesa_singkat, data_triase_igdsekunder.catatan, data_triase_igdsekunder.plan, data_triase_igdsekunder.tanggaltriase, data_triase_igdsekunder.nik, " +
                            "data_triase_igd.tekanan_darah, data_triase_igd.nadi, data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, data_triase_igd.nyeri, data_triase_igd.no_rawat, " +
                            "pasien.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir, pegawai.nama, data_triase_igd.tgl_kunjungan, data_triase_igd.cara_masuk, master_triase_macam_kasus.macam_kasus " +
                            "from data_triase_igdsekunder join data_triase_igd on data_triase_igdsekunder.no_rawat = data_triase_igd.no_rawat join master_triase_macam_kasus on data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus " +
                            "join reg_periksa on data_triase_igd.no_rawat = reg_periksa.no_rawat join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join pegawai on data_triase_igdsekunder.nik = pegawai.nik where data_triase_igdsekunder.no_rawat = ?"
                        )) {
                            ps1.setString(1, lblNoRawatKompi1.getText());
                            try (ResultSet rs1 = ps1.executeQuery()) {
                                if (rs1.next()) {
                                    param.put("norawat", rs1.getString("no_rawat"));
                                    param.put("norm", rs1.getString("no_rkm_medis"));
                                    param.put("namapasien", rs1.getString("nm_pasien"));
                                    param.put("tanggallahir", rs1.getDate("tgl_lahir"));
                                    param.put("jk", rs1.getString("jk").replaceAll("L", "Laki-Laki").replaceAll("P", "Perempuan"));
                                    param.put("tanggalkunjungan", rs1.getDate("tgl_kunjungan"));
                                    param.put("jamkunjungan", rs1.getString("tgl_kunjungan").substring(11, 19));
                                    param.put("caradatang", rs1.getString("cara_masuk"));
                                    param.put("macamkasus", rs1.getString("macam_kasus"));
                                    param.put("keluhanutama", rs1.getString("anamnesa_singkat"));
                                    param.put("plan", rs1.getString("plan"));
                                    param.put("tanggaltriase", rs1.getDate("tanggaltriase"));
                                    param.put("tandavital", "Suhu (C) : " + rs1.getString("suhu") + ", Nyeri : " + rs1.getString("nyeri") + ", Tensi : " + rs1.getString("tekanan_darah") + ", Nadi(/menit) : " + rs1.getString("nadi") + ", Saturasi O²(%) : " + rs1.getString("saturasi_o2") + ", Respirasi(/menit) : " + rs1.getString("pernapasan"));
                                    param.put("jamtriase", rs1.getString("tanggaltriase").substring(11, 19));
                                    param.put("pegawai", rs1.getString("nama"));
                                    param.put("catatan", rs1.getString("catatan"));
                                    finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs1.getString("nik"));
                                    param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs1.getString("nama") + "\nID " + (finger.equals("") ? rs1.getString("nik") : finger) + "\n" + Valid.SetTgl3(rs1.getString("tanggaltriase")));
                                    try (PreparedStatement ps2 = koneksi.prepareStatement(
                                        "select master_triase_pemeriksaan.kode_pemeriksaan, master_triase_pemeriksaan.nama_pemeriksaan from master_triase_pemeriksaan " +
                                        "join master_triase_skala3 on master_triase_pemeriksaan.kode_pemeriksaan = master_triase_skala3.kode_pemeriksaan " +
                                        "join data_triase_igddetail_skala3 on master_triase_skala3.kode_skala3 = data_triase_igddetail_skala3.kode_skala3 " +
                                        "where data_triase_igddetail_skala3.no_rawat = ? group by master_triase_pemeriksaan.kode_pemeriksaan " +
                                        "order by master_triase_pemeriksaan.kode_pemeriksaan"
                                    )) {
                                        ps2.setString(1, lblNoRawatKompi1.getText());
                                        try (ResultSet rs2 = ps2.executeQuery()) {
                                            while (rs2.next()) {
                                                detailTriase = "";
                                                try (PreparedStatement ps3 = koneksi.prepareStatement(
                                                    "select master_triase_skala3.pengkajian_skala3 from master_triase_skala3 join data_triase_igddetail_skala3 " +
                                                    "on master_triase_skala3.kode_skala3 = data_triase_igddetail_skala3.kode_skala3 where master_triase_skala3.kode_pemeriksaan = ? " +
                                                    "and data_triase_igddetail_skala3.no_rawat = ? order by data_triase_igddetail_skala3.kode_skala3"
                                                )) {
                                                    ps3.setString(1, rs2.getString(1));
                                                    ps3.setString(2, lblNoRawatKompi1.getText());
                                                    try (ResultSet rs3 = ps3.executeQuery()) {
                                                        while (rs3.next()) {
                                                            detailTriase = rs3.getString(1) + ", " + detailTriase;
                                                        }
                                                    }
                                                }
                                                detailTriase = detailTriase.substring(0, detailTriase.length() - 2);
                                                Sequel.temporary(String.valueOf(++i), rs2.getString("nama_pemeriksaan"), detailTriase);
                                            }
                                        }
                                    }
                                    Valid.reportTempSmc("rptLembarTriaseSkala3.jasper", "report", "::[ Triase Skala 3 ]::", param);
                                }
                            }
                        }
                    } else if (rs.getInt("cs4") > 0) {
                        try (PreparedStatement ps1 = koneksi.prepareStatement(
                            "select data_triase_igdsekunder.anamnesa_singkat, data_triase_igdsekunder.catatan, data_triase_igdsekunder.plan, data_triase_igdsekunder.tanggaltriase, data_triase_igdsekunder.nik, " +
                            "data_triase_igd.tekanan_darah, data_triase_igd.nadi, data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, data_triase_igd.nyeri, data_triase_igd.no_rawat, " +
                            "pasien.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir, pegawai.nama, data_triase_igd.tgl_kunjungan, data_triase_igd.cara_masuk, master_triase_macam_kasus.macam_kasus " +
                            "from data_triase_igdsekunder join data_triase_igd on data_triase_igdsekunder.no_rawat = data_triase_igd.no_rawat join master_triase_macam_kasus on data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus " +
                            "join reg_periksa on data_triase_igdsekunder.no_rawat = reg_periksa.no_rawat join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join pegawai on data_triase_igdsekunder.nik = pegawai.nik where data_triase_igd.no_rawat = ?"
                        )) {
                            ps1.setString(1, lblNoRawatKompi1.getText());
                            try (ResultSet rs1 = ps1.executeQuery()) {
                                if (rs1.next()) {
                                    param.put("norawat", rs1.getString("no_rawat"));
                                    param.put("norm", rs1.getString("no_rkm_medis"));
                                    param.put("namapasien", rs1.getString("nm_pasien"));
                                    param.put("tanggallahir", rs1.getDate("tgl_lahir"));
                                    param.put("jk", rs1.getString("jk").replaceAll("L", "Laki-Laki").replaceAll("P", "Perempuan"));
                                    param.put("tanggalkunjungan", rs1.getDate("tgl_kunjungan"));
                                    param.put("jamkunjungan", rs1.getString("tgl_kunjungan").substring(11, 19));
                                    param.put("caradatang", rs1.getString("cara_masuk"));
                                    param.put("macamkasus", rs1.getString("macam_kasus"));
                                    param.put("keluhanutama", rs1.getString("anamnesa_singkat"));
                                    param.put("plan", rs1.getString("plan"));
                                    param.put("tanggaltriase", rs1.getDate("tanggaltriase"));
                                    param.put("tandavital", "Suhu (C) : " + rs1.getString("suhu") + ", Nyeri : " + rs1.getString("nyeri") + ", Tensi : " + rs1.getString("tekanan_darah") + ", Nadi(/menit) : " + rs1.getString("nadi") + ", Saturasi O²(%) : " + rs1.getString("saturasi_o2") + ", Respirasi(/menit) : " + rs1.getString("pernapasan"));
                                    param.put("jamtriase", rs1.getString("tanggaltriase").substring(11, 19));
                                    param.put("pegawai", rs1.getString("nama"));
                                    param.put("catatan", rs1.getString("catatan"));
                                    finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs1.getString("nik"));
                                    param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs1.getString("nama") + "\nID " + (finger.equals("") ? rs1.getString("nik") : finger) + "\n" + Valid.SetTgl3(rs1.getString("tanggaltriase")));
                                    try (PreparedStatement ps2 = koneksi.prepareStatement(
                                        "select master_triase_pemeriksaan.kode_pemeriksaan, master_triase_pemeriksaan.nama_pemeriksaan from master_triase_pemeriksaan " +
                                        "join master_triase_skala4 on master_triase_pemeriksaan.kode_pemeriksaan = master_triase_skala4.kode_pemeriksaan " +
                                        "join data_triase_igddetail_skala4 on master_triase_skala4.kode_skala4 = data_triase_igddetail_skala4.kode_skala4 " +
                                        "where data_triase_igddetail_skala4.no_rawat = ? group by master_triase_pemeriksaan.kode_pemeriksaan " +
                                        "order by master_triase_pemeriksaan.kode_pemeriksaan"
                                    )) {
                                        ps2.setString(1, lblNoRawatKompi1.getText());
                                        try (ResultSet rs2 = ps2.executeQuery()) {
                                            while (rs2.next()) {
                                                detailTriase = "";
                                                try (PreparedStatement ps3 = koneksi.prepareStatement(
                                                    "select master_triase_skala4.pengkajian_skala4 from master_triase_skala4 join data_triase_igddetail_skala4 " +
                                                    "on master_triase_skala4.kode_skala4 = data_triase_igddetail_skala4.kode_skala4 where master_triase_skala4.kode_pemeriksaan = ? " +
                                                    "and data_triase_igddetail_skala4.no_rawat = ? order by data_triase_igddetail_skala4.kode_skala4"
                                                )) {
                                                    ps3.setString(1, rs2.getString(1));
                                                    ps3.setString(2, lblNoRawatKompi1.getText());
                                                    try (ResultSet rs3 = ps3.executeQuery()) {
                                                        while (rs3.next()) {
                                                            detailTriase = rs3.getString(1) + ", " + detailTriase;
                                                        }
                                                    }
                                                }
                                                detailTriase = detailTriase.substring(0, detailTriase.length() - 2);
                                                Sequel.temporary(String.valueOf(++i), rs2.getString("nama_pemeriksaan"), detailTriase);
                                            }
                                        }
                                    }
                                    Valid.reportTempSmc("rptLembarTriaseSkala4.jasper", "report", "::[ Triase Skala 4 ]::", param);
                                }
                            }
                        }
                    } else if (rs.getInt("cs5") > 0) {
                        try (PreparedStatement ps1 = koneksi.prepareStatement(
                            "select data_triase_igdsekunder.anamnesa_singkat, data_triase_igdsekunder.catatan, data_triase_igdsekunder.plan, data_triase_igdsekunder.tanggaltriase, data_triase_igdsekunder.nik, " +
                            "data_triase_igd.tekanan_darah, data_triase_igd.nadi, data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, data_triase_igd.nyeri, data_triase_igd.no_rawat, " +
                            "pasien.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir, pegawai.nama, data_triase_igd.tgl_kunjungan, data_triase_igd.cara_masuk, master_triase_macam_kasus.macam_kasus " +
                            "from data_triase_igdsekunder join data_triase_igd on data_triase_igdsekunder.no_rawat = data_triase_igd.no_rawat join master_triase_macam_kasus on data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus " +
                            "join reg_periksa on data_triase_igdsekunder.no_rawat = reg_periksa.no_rawat join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join pegawai on data_triase_igdsekunder.nik = pegawai.nik where data_triase_igd.no_rawat = ?"
                        )) {
                            ps1.setString(1, lblNoRawatKompi1.getText());
                            try (ResultSet rs1 = ps1.executeQuery()) {
                                if (rs1.next()) {
                                    param.put("norawat", rs1.getString("no_rawat"));
                                    param.put("norm", rs1.getString("no_rkm_medis"));
                                    param.put("namapasien", rs1.getString("nm_pasien"));
                                    param.put("tanggallahir", rs1.getDate("tgl_lahir"));
                                    param.put("jk", rs1.getString("jk").replaceAll("L", "Laki-Laki").replaceAll("P", "Perempuan"));
                                    param.put("tanggalkunjungan", rs1.getDate("tgl_kunjungan"));
                                    param.put("jamkunjungan", rs1.getString("tgl_kunjungan").substring(11, 19));
                                    param.put("caradatang", rs1.getString("cara_masuk"));
                                    param.put("macamkasus", rs1.getString("macam_kasus"));
                                    param.put("keluhanutama", rs1.getString("anamnesa_singkat"));
                                    param.put("plan", rs1.getString("plan"));
                                    param.put("tanggaltriase", rs1.getDate("tanggaltriase"));
                                    param.put("tandavital", "Suhu (C) : " + rs1.getString("suhu") + ", Nyeri : " + rs1.getString("nyeri") + ", Tensi : " + rs1.getString("tekanan_darah") + ", Nadi(/menit) : " + rs1.getString("nadi") + ", Saturasi O²(%) : " + rs1.getString("saturasi_o2") + ", Respirasi(/menit) : " + rs1.getString("pernapasan"));
                                    param.put("jamtriase", rs1.getString("tanggaltriase").substring(11, 19));
                                    param.put("pegawai", rs1.getString("nama"));
                                    param.put("catatan", rs1.getString("catatan"));
                                    finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs1.getString("nik"));
                                    param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs1.getString("nama") + "\nID " + (finger.equals("") ? rs1.getString("nik") : finger) + "\n" + Valid.SetTgl3(rs1.getString("tanggaltriase")));
                                    try (PreparedStatement ps2 = koneksi.prepareStatement(
                                        "select master_triase_pemeriksaan.kode_pemeriksaan, master_triase_pemeriksaan.nama_pemeriksaan from master_triase_pemeriksaan " +
                                        "join master_triase_skala5 on master_triase_pemeriksaan.kode_pemeriksaan = master_triase_skala5.kode_pemeriksaan " +
                                        "join data_triase_igddetail_skala5 on master_triase_skala5.kode_skala5 = data_triase_igddetail_skala5.kode_skala5 " +
                                        "where data_triase_igddetail_skala5.no_rawat = ? group by master_triase_pemeriksaan.kode_pemeriksaan " +
                                        "order by master_triase_pemeriksaan.kode_pemeriksaan"
                                    )) {
                                        ps2.setString(1, lblNoRawatKompi1.getText());
                                        try (ResultSet rs2 = ps2.executeQuery()) {
                                            while (rs2.next()) {
                                                detailTriase = "";
                                                try (PreparedStatement ps3 = koneksi.prepareStatement(
                                                    "select master_triase_skala5.pengkajian_skala5 from master_triase_skala5 join data_triase_igddetail_skala5 " +
                                                    "on master_triase_skala5.kode_skala5 = data_triase_igddetail_skala5.kode_skala5 where master_triase_skala5.kode_pemeriksaan = ? " +
                                                    "and data_triase_igddetail_skala5.no_rawat = ? order by data_triase_igddetail_skala5.kode_skala5"
                                                )) {
                                                    ps3.setString(1, rs2.getString(1));
                                                    ps3.setString(2, lblNoRawatKompi1.getText());
                                                    try (ResultSet rs3 = ps3.executeQuery()) {
                                                        while (rs3.next()) {
                                                            detailTriase = rs3.getString(1) + ", " + detailTriase;
                                                        }
                                                    }
                                                }
                                                detailTriase = detailTriase.substring(0, detailTriase.length() - 2);
                                                Sequel.temporary(String.valueOf(++i), rs2.getString("nama_pemeriksaan"), detailTriase);
                                            }
                                        }
                                    }
                                    Valid.reportTempSmc("rptLembarTriaseSkala5.jasper", "report", "::[ Triase Skala 5 ]::", param);
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Triase tidak memiliki skala yang benar!");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnTriaseIGDKompi1ActionPerformed

    private void BtnPenjaminKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPenjaminKompi1ActionPerformed
        penjab.isCek();
        penjab.setSize(internalFrame1Kompi1.getWidth() - 20, internalFrame1Kompi1.getHeight() - 20);
        penjab.setLocationRelativeTo(internalFrame1Kompi1);
        penjab.setAlwaysOnTop(false);
        penjab.setVisible(true);
    }//GEN-LAST:event_BtnPenjaminKompi1ActionPerformed

    private void BtnPengaturanKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPengaturanKompi1ActionPerformed
        cekPengaturanKompilasi();
        WindowPengaturan.setSize(610, 172);
        WindowPengaturan.setLocationRelativeTo(internalFrame1Kompi1);
        WindowPengaturan.setVisible(true);
    }//GEN-LAST:event_BtnPengaturanKompi1ActionPerformed

    private void BtnTutupPengaturanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTutupPengaturanActionPerformed
        WindowPengaturan.dispose();
    }//GEN-LAST:event_BtnTutupPengaturanActionPerformed

    private void BtnBukaFolderExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBukaFolderExportActionPerformed
        try {
            Desktop.getDesktop().open(new File("./berkaspdf"));
        } catch (IOException e) {
            System.out.println("Notif : " + e);
        }
    }//GEN-LAST:event_BtnBukaFolderExportActionPerformed

    private void CmbPilihanTanggalExportItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CmbPilihanTanggalExportItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_CmbPilihanTanggalExportItemStateChanged

    private void BtnSimpanPengaturanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanPengaturanActionPerformed
        try {
            String aplikasipdf = "", tanggalexport = "registrasi", maxmemory = TMaxMemory.getText().trim();
            switch (CmbPilihanAplikasiPDF.getSelectedIndex()) {
                case 1:
                    aplikasipdf = "chrome";
                    break;
                case 2:
                    aplikasipdf = "firefox";
                    break;
                case 3:
                    aplikasipdf = "msedge";
                    break;
                case 4:
                    aplikasipdf = TPathAplikasiPDF.getText().trim();
                    break;
                case 5:
                    aplikasipdf = "disable";
                    break;
                default:
                    aplikasipdf = "";
                    break;
            }
            
            if (CmbPilihanTanggalExport.getSelectedIndex() == 1) {
                tanggalexport = "sep";
            } else {
                tanggalexport = "kompilasi";
            }
            
            File iyem = new File("./cache/pengaturankompilasi.iyem");
            iyem.createNewFile();
            try (FileWriter fw = new FileWriter(iyem)) {
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    // Special behaviour for windows because of backslash directory separator
                    fw.write("{\"pengaturan\":{\"aplikasipdf\":\"" + aplikasipdf.replace(File.separator, File.separator + File.separator) + "\",\"tanggalexport\":\"" + tanggalexport + "\",\"maxmemory\":" + maxmemory + "}}");
                } else {
                    fw.write("{\"pengaturan\":{\"aplikasipdf\":\"" + aplikasipdf + "\",\"tanggalexport\":\"" + tanggalexport + "\",\"maxmemory\":" + maxmemory + "}}");
                }
                fw.flush();
                KOMPILASIBERKASAPLIKASIPDF = aplikasipdf;
                KOMPILASIBERKASGUNAKANTANGGALEXPORT = tanggalexport;
                KOMPILASIBERKASMAXMEMORY = Integer.parseInt(maxmemory);
                JOptionPane.showMessageDialog(null, "Pengaturan kompilasi berhasil disimpan..!!");
                WindowPengaturan.dispose();
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }//GEN-LAST:event_BtnSimpanPengaturanActionPerformed

    private void BtnPilihAplikasiPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPilihAplikasiPDFActionPerformed
        if (!TPathAplikasiPDF.getText().equals("")) {
            fc.setCurrentDirectory(new File(TPathAplikasiPDF.getText().substring(0, TPathAplikasiPDF.getText().lastIndexOf(File.separator))));
        }
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            TPathAplikasiPDF.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_BtnPilihAplikasiPDFActionPerformed

    private void BtnResetPengaturanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnResetPengaturanActionPerformed
        try {
            File iyem = new File("./cache/pengaturankompilasi.iyem");
            if (JOptionPane.showConfirmDialog(null, "Eeeiiittsss... Yakin mau reset pengaturan kompilasi?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION && !iyem.isDirectory()) {
                iyem.delete();
                cekPengaturanKompilasi();
                JOptionPane.showMessageDialog(null, "Pengaturan kompilasi berhasil direset..!!");
                WindowPengaturan.dispose();
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada saat mereset pengaturan kompilasi,\nSilahkan coba lagi.");
            System.out.println("Notif : " + e);
        }
    }//GEN-LAST:event_BtnResetPengaturanActionPerformed

    private void CmbPilihanAplikasiPDFItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CmbPilihanAplikasiPDFItemStateChanged
        if (CmbPilihanAplikasiPDF.getSelectedIndex() == 4) {
            TPathAplikasiPDF.setEditable(true);
            BtnPilihAplikasiPDF.setEnabled(true);
        } else {
            TPathAplikasiPDF.setText("");
            TPathAplikasiPDF.setEditable(false);
            BtnPilihAplikasiPDF.setEnabled(false);
        }
    }//GEN-LAST:event_CmbPilihanAplikasiPDFItemStateChanged

    private void tbKompilasiKompi1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbKompilasiKompi1KeyReleased
        if (tabMode.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    getData();
                    tabPane1Kompi1.setSelectedIndex(0);
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbKompilasiKompi1KeyReleased

    private void kirimonlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kirimonlineActionPerformed
        try {
            String requestJson ="{\n" +
            "\"metadata\": {\n" +
            "\"method\":\"send_claim_individual\"\n" +
            "},\n" +
            "\"data\": {\n" +
            "\"nomor_sep\":\""+lblNoSEPKompi1.getText().trim()+"\"\n" +
            "}\n" +
            "}";

            // Use the JSON string in your request
            String request = requestJson;
            System.out.println("request JSON : "+requestJson);
            JSONObject response = inacbg.request(request);
            JSONObject code = response.getJSONObject("metadata");
            System.out.println("response kirim online : "+response);

            if(response.has("metadata")){
                if(code.getInt("code")==200){
                    JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                }else{
                    JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                }
            } else {
                System.out.println("response : "+response);
                System.err.println("\n The 'data' field is missing in the response.");
            }
            Sequel.menyimpan("inacbg_cetak_klaim_status", "'"+lblNoSEPKompi1.getText()+"','Terkirim'");
        } catch (Exception e) {
            System.err.println("Error in sitb: " + e.getMessage());
            e.printStackTrace();
        }
//        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU KIRIM ONLINE PASIEN "+lblNamaPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
//        if (reply == JOptionPane.YES_OPTION) {
//            try {
//                if (lblNoRM.getText().trim().equals("") || lblNoSEP.getText().trim().equals("")) {
//                    JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP !!!!!");
//                } else {
//                    try {
//                        // Create a human-readable JSON string
//                        String requestJson ="{\n" +
//                        "\"metadata\": {\n" +
//                        "\"method\":\"send_claim_individual\"\n" +
//                        "},\n" +
//                        "\"data\": {\n" +
//                        "\"nomor_sep\":\""+lblNoSEP.getText().trim()+"\"\n" +
//                        "}\n" +
//                        "}";
//
//                        // Use the JSON string in your request
//                        String request = requestJson;
//                        System.out.println("request JSON : "+requestJson);
//                        JSONObject response = inacbg.request(request);
//                        JSONObject code = response.getJSONObject("metadata");
//                        System.out.println("response kirim online : "+response);
//
//                        if(response.has("metadata")){
//                            if(code.getInt("code")==200){
//                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
//                            }else{
//                                JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
//                            }
//                        } else {
//                            System.out.println("response : "+response);
//                            System.err.println("\n The 'data' field is missing in the response.");
//                        }
//
//                    } catch (Exception e) {
//                        System.err.println("Error in sitb: " + e.getMessage());
//                        e.printStackTrace();
//                    }
//                }
//            }catch (Exception ex) {
//                System.out.println("Notifikasi Bridging Hapus : "+ex);
//            }
//        }
    }//GEN-LAST:event_kirimonlineActionPerformed

    private void hapusKlaimKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusKlaimKompi1ActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU HAPUS KLAIM PASIEN "+lblNamaPasienKompi1.getText()+" (KLAIM INI AKAN TERHAPUS !!!!!!) ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (lblNoRMKompi1.getText().trim().equals("") || lblNoSEPKompi1.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP !!!!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                    "\"metadata\": {\n" +
                    "\"method\":\"delete_claim\"\n" +
                    "},\n" +
                    "\"data\": {\n" +
                    "\"nomor_sep\":\""+lblNoSEPKompi1.getText().trim()+"\",\n" +
                    "\"coder_nik\": \""+lblCoderNIKKompi1.getText()+"\"\n" +
                    "}\n" +
                    "}";

                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);
                    JSONObject response = inacbg.request(request);
                    JSONObject code = response.getJSONObject("metadata");
                    System.out.println("response hapus : "+response);

                    if(response.has("metadata")){
                        if(code.getInt("code")==200){
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                            Sequel.meghapus("inacbg_cetak_klaim", "no_sep", lblNoSEPKompi1.getText());
                            Sequel.meghapus("inacbg_cetak_klaim_status", "no_sep", lblNoSEPKompi1.getText());
                        }else{
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        }
                    } else {
                        System.out.println("response : "+response);
                        System.err.println("\n The 'data' field is missing in the response.");
                    }

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_hapusKlaimKompi1ActionPerformed

    private void hapusKlaimKompi1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hapusKlaimKompi1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_hapusKlaimKompi1KeyPressed

    private void btnBerkasDigitalKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBerkasDigitalKompi1ActionPerformed
        //        cekberkasdigital=Sequel.cariInteger("select count(berkas_digital_perawatan.no_rawat) from berkas_digital_perawatan where berkas_digital_perawatan.no_rawat='"+lblNoRawat.getText()+"'");
        //        if(cekberkasdigital>0){
            //            Map<String, Object> param = new HashMap<>();
            //            param.put("berkas1","http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + prop.getProperty("PORTWEB") + "/"+ prop.getProperty("HYBRIDWEB") + "/berkasrawat/");
            //            Valid.MyReportqry("rptBerkasDigital.jasper","report","::[ Laporan Berkas Digital ]::",
                //                    "select berkas_digital_perawatan.lokasi_file from berkas_digital_perawatan where berkas_digital_perawatan.no_rawat='"+lblNoRawat.getText()+"'",param);
            //        }
        //        Base64ToPDFBerkasDigital();
    }//GEN-LAST:event_btnBerkasDigitalKompi1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            BPJSKompilasiBerkasKlaim2 dialog = new BPJSKompilasiBerkasKlaim2(new javax.swing.JFrame(), true);
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
    private widget.Button BtnAllKompi1;
    private widget.Button BtnBukaFolderExport;
    private widget.Button BtnCariKompi1;
    private widget.Button BtnCloseIn8;
    private widget.Button BtnHapusDiagnosaKompi1;
    private widget.Button BtnKeluarKompi1;
    private widget.Button BtnPengaturanKompi1;
    private widget.Button BtnPenjaminKompi1;
    private widget.Button BtnPilihAplikasiPDF;
    private widget.Button BtnResetPengaturan;
    private widget.Button BtnSimpan8;
    private widget.Button BtnSimpanDiagnosaKompi1;
    private widget.Button BtnSimpanPengaturan;
    private widget.Button BtnTutupPengaturan;
    private widget.Button BtnValidasiQRKompi1;
    private widget.ComboBox CmbPilihanAplikasiPDF;
    private widget.ComboBox CmbPilihanTanggalExport;
    private widget.ComboBox CmbStatusKirimKompi1;
    private widget.ComboBox CmbStatusRawatKompi1;
    private widget.ComboBox CmbTanggalSEPKompi1;
    private widget.Tanggal DTPCari1Kompi1;
    private widget.Tanggal DTPCari2Kompi1;
    private widget.TextBox KdPjKompi1;
    private widget.Label LCountKompi1;
    private widget.TextBox NamaPjKompi1;
    private widget.TextBox NoLPManual;
    private widget.TextBox NoSuratKematian;
    private widget.panelisi PanelContentINACBGKompi1;
    private widget.ScrollPane ScrollKompi1;
    private widget.ComboBox StatusPulang;
    private widget.TextBox TCariKompi1;
    private widget.TextBox TMaxMemory;
    private widget.TextBox TNoRMPulang;
    private widget.TextBox TNoRwPulang;
    private widget.TextBox TNoSEPRanapPulang;
    private widget.TextBox TPasienPulang;
    private widget.TextBox TPathAplikasiPDF;
    private widget.Tanggal TanggalKematian;
    private widget.Tanggal TanggalPulang;
    private javax.swing.JDialog WindowPengaturan;
    private javax.swing.JDialog WindowUpdatePulang;
    private widget.Button btnAwalMedisIGDKompi1;
    private widget.Button btnBerkasDigitalKompi1;
    private widget.Button btnHasilLabKompi1;
    private widget.Button btnHasilRadKompi1;
    private widget.Button btnInvoiceKompi1;
    private widget.Button btnPDFKlaimINACBGKompi1;
    private widget.Button btnResumeRanapKompi1;
    private widget.Button btnRiwayatPasienKompi1;
    private widget.Button btnSEPKompi1;
    private widget.Button btnSPRIKompi1;
    private widget.Button btnSurkonKompi1;
    private widget.Button btnTriaseIGDKompi1;
    private javax.swing.JFileChooser fc;
    private widget.Button hapusKlaimKompi1;
    private widget.InternalFrame internalFrame11;
    private widget.InternalFrame internalFrame12;
    private widget.InternalFrame internalFrame1Kompi1;
    private widget.Label jLabel10;
    private widget.Label jLabel11Kompi1;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14Kompi1;
    private widget.Label jLabel15Kompi1;
    private widget.Label jLabel16;
    private widget.Label jLabel17Kompi1;
    private widget.Label jLabel18Kompi1;
    private widget.Label jLabel19Kompi1;
    private widget.Label jLabel20Kompi1;
    private widget.Label jLabel21Kompi1;
    private widget.Label jLabel22;
    private widget.Label jLabel25Kompi1;
    private widget.Label jLabel26Kompi1;
    private widget.Label jLabel27Kompi1;
    private widget.Label jLabel28Kompi1;
    private widget.Label jLabel29Kompi1;
    private widget.Label jLabel30Kompi1;
    private widget.Label jLabel31Kompi1;
    private widget.Label jLabel32Kompi1;
    private widget.Label jLabel33Kompi1;
    private widget.Label jLabel34Kompi1;
    private widget.Label jLabel35Kompi1;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel44;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel48;
    private widget.Label jLabel49;
    private widget.Label jLabel6Kompi1;
    private widget.Label jLabel7Kompi1;
    private widget.Label jLabel8Kompi1;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel1Kompi1;
    private javax.swing.JPanel jPanel2Kompi1;
    private javax.swing.JPanel jPanel3Kompi1;
    private javax.swing.JPanel jPanel4Kompi1;
    private javax.swing.JPanel jPanel5Kompi1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.Button kirimonline;
    private widget.Label label19Kompi1;
    private widget.Label lblCoderNIKKompi1;
    private widget.Label lblNamaPasienKompi1;
    private widget.Label lblNoRMKompi1;
    private widget.Label lblNoRawatKompi1;
    private widget.Label lblNoSEPKompi1;
    private widget.Label lblStatusRawatKompi1;
    private widget.Label lblTglSEP1Kompi1;
    private widget.Label lblTglSEPKompi1;
    private widget.PanelBiasa panelBiasa1Kompi1;
    private widget.PanelBiasa panelBiasa2Kompi1;
    private laporan.PanelDiagnosaSmc panelDiagnosaSmcKompi1;
    private widget.panelisi panelGlass10Kompi1;
    private widget.panelisi panelGlass11Kompi1;
    private widget.panelisi panelGlass8Kompi1;
    private widget.panelisi panelInvoicesKompi1;
    private javax.swing.JMenuItem ppBerkasDigital;
    private javax.swing.JMenuItem ppRiwayatPerawatan;
    private javax.swing.JMenuItem ppUpdateTanggalPulangSEP;
    private widget.ScrollPane scrollPane1Kompi1;
    private widget.TabPane tabPane1Kompi1;
    private widget.Table tbKompilasiKompi1;
    // End of variables declaration//GEN-END:variables

    public void tampil() {
        Valid.tabelKosong(tabMode);
        String statusKirim = "";
        if (CmbStatusKirimKompi1.getSelectedItem().toString().equals("Terkirim")) {
            statusKirim = "and inacbg_cetak_klaim.no_sep is not null ";
        } else if (CmbStatusKirimKompi1.getSelectedItem().toString().equals("Belum Terkirim")) {
            statusKirim = "and inacbg_cetak_klaim.no_sep is null ";
        }
        
        try (PreparedStatement ps = koneksi.prepareStatement("select bridging_sep.no_rawat, bridging_sep.no_sep, reg_periksa.no_rkm_medis, pasien.nm_pasien, reg_periksa.status_lanjut, bridging_sep.tglsep, date(bridging_sep.tglpulang) " +
            "as tglpulang, kamar_inap.stts_pulang, case when reg_periksa.status_lanjut = 'Ranap' then concat(kamar_inap.kd_kamar, ' ', bangsal.nm_bangsal) when reg_periksa.status_lanjut " +
            "= 'Ralan' then poliklinik.nm_poli end as ruangan, dokter.nm_dokter, diagnosa_pasien.kd_penyakit, (inacbg_cetak_klaim.no_sep is not null) as inacbg_terkirim from bridging_sep " +
            "join reg_periksa on bridging_sep.no_rawat = reg_periksa.no_rawat and (if(bridging_sep.jnspelayanan = '1', 'Ranap', 'Ralan')) = reg_periksa.status_lanjut join pasien on " +
            "reg_periksa.no_rkm_medis = pasien.no_rkm_medis join poliklinik on reg_periksa.kd_poli = poliklinik.kd_poli left join maping_dokter_dpjpvclaim on bridging_sep.kddpjp = " +
            "maping_dokter_dpjpvclaim.kd_dokter_bpjs left join dokter on maping_dokter_dpjpvclaim.kd_dokter = dokter.kd_dokter left join diagnosa_pasien on reg_periksa.no_rawat = " +
            "diagnosa_pasien.no_rawat and reg_periksa.status_lanjut = diagnosa_pasien.status and diagnosa_pasien.prioritas = '1' left join inacbg_cetak_klaim on bridging_sep.no_sep " +
            "= inacbg_cetak_klaim.no_sep left join kamar_inap on reg_periksa.no_rawat = kamar_inap.no_rawat and kamar_inap.stts_pulang != 'Pindah Kamar' left join kamar on kamar_inap.kd_kamar " +
            "= kamar.kd_kamar left join bangsal on kamar.kd_bangsal = bangsal.kd_bangsal where bridging_sep.no_sep like ? and bridging_sep.jnspelayanan like ? and bridging_sep.tglsep between " +
            "? and ? and length(bridging_sep.no_sep) = 19 " + statusKirim + " and reg_periksa.status_bayar = 'Sudah Bayar' and reg_periksa.kd_pj like ? " + (TCariKompi1.getText().equals("") ? "" : "and ( " +
            "reg_periksa.no_rawat like ? or bridging_sep.no_sep like ? or reg_periksa.no_rkm_medis like ? or pasien.nm_pasien like ? or poliklinik.nm_poli like ? or concat( " +
            "kamar_inap.kd_kamar, ' ', bangsal.nm_bangsal) like ? or dokter.nm_dokter like ?) ") + "group by bridging_sep.no_sep, bridging_sep.no_rawat, reg_periksa.no_rkm_medis, " +
            "reg_periksa.status_lanjut order by bridging_sep.jnspelayanan desc, bridging_sep.no_sep"
        )) {
            ps.setString(1, KODEPPKBPJS);
            if (CmbStatusRawatKompi1.getSelectedIndex() == 0) {
                ps.setString(2, "%");
            } else if (CmbStatusRawatKompi1.getSelectedIndex() == 1) {
                ps.setString(2, "2");
            } else if (CmbStatusRawatKompi1.getSelectedIndex() == 2) {
                ps.setString(2, "1");
            }
            ps.setString(3, Valid.getTglSmc(DTPCari1Kompi1));
            ps.setString(4, Valid.getTglSmc(DTPCari2Kompi1));
            if (! KdPjKompi1.getText().equals("")) {
                ps.setString(5, KdPjKompi1.getText());
            } else {
                ps.setString(5, "%");
            }
            if (!TCariKompi1.getText().equals("")) {
                ps.setString(6, "%" + TCariKompi1.getText() + "%");
                ps.setString(7, "%" + TCariKompi1.getText() + "%");
                ps.setString(8, "%" + TCariKompi1.getText() + "%");
                ps.setString(9, "%" + TCariKompi1.getText() + "%");
                ps.setString(10, "%" + TCariKompi1.getText() + "%");
                ps.setString(11, "%" + TCariKompi1.getText() + "%");
                ps.setString(12, "%" + TCariKompi1.getText() + "%");
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tabMode.addRow(new Object[] {
                        rs.getString("no_rawat"),
                        rs.getString("no_sep"),
                        rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),
                        rs.getString("status_lanjut"),
                        rs.getString("tglsep"),
                        rs.getString("tglpulang"),
                        rs.getString("stts_pulang"),
                        rs.getString("ruangan"),
                        rs.getString("nm_dokter"),
                        rs.getString("kd_penyakit"),
                        (rs.getBoolean("inacbg_terkirim") ? "Terkirim" : "Belum Terkirim"),
                        rs.getString("inacbg_terkirim")
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        LCountKompi1.setText(String.valueOf(tabMode.getRowCount()));
    }
    
    public void tampil2() {
        Valid.tabelKosong(tabMode);
        String statusKirim = "";
        if (CmbStatusKirimKompi1.getSelectedItem().toString().equals("Terkirim")) {
            statusKirim = "and inacbg_cetak_klaim.no_sep is not null ";
        } else if (CmbStatusKirimKompi1.getSelectedItem().toString().equals("Belum Terkirim")) {
            statusKirim = "and inacbg_cetak_klaim.no_sep is null ";
        }
        
        try (PreparedStatement ps = koneksi.prepareStatement("select bridging_sep.no_rawat, bridging_sep.no_sep, reg_periksa.no_rkm_medis, pasien.nm_pasien, reg_periksa.status_lanjut, bridging_sep.tglsep, date(bridging_sep.tglpulang) " +
            "as tglpulang, kamar_inap.stts_pulang, case when reg_periksa.status_lanjut = 'Ranap' then concat(kamar_inap.kd_kamar, ' ', bangsal.nm_bangsal) when reg_periksa.status_lanjut " +
            "= 'Ralan' then poliklinik.nm_poli end as ruangan, dokter.nm_dokter, diagnosa_pasien.kd_penyakit, (inacbg_cetak_klaim.no_sep is not null) as inacbg_terkirim from bridging_sep " +
            "join reg_periksa on bridging_sep.no_rawat = reg_periksa.no_rawat and (if(bridging_sep.jnspelayanan = '1', 'Ranap', 'Ralan')) = reg_periksa.status_lanjut join pasien on " +
            "reg_periksa.no_rkm_medis = pasien.no_rkm_medis join poliklinik on reg_periksa.kd_poli = poliklinik.kd_poli left join maping_dokter_dpjpvclaim on bridging_sep.kddpjp = " +
            "maping_dokter_dpjpvclaim.kd_dokter_bpjs left join dokter on maping_dokter_dpjpvclaim.kd_dokter = dokter.kd_dokter left join diagnosa_pasien on reg_periksa.no_rawat = " +
            "diagnosa_pasien.no_rawat and reg_periksa.status_lanjut = diagnosa_pasien.status and diagnosa_pasien.prioritas = '1' left join inacbg_cetak_klaim on bridging_sep.no_sep " +
            "= inacbg_cetak_klaim.no_sep left join kamar_inap on reg_periksa.no_rawat = kamar_inap.no_rawat and kamar_inap.stts_pulang != 'Pindah Kamar' left join kamar on kamar_inap.kd_kamar " +
            "= kamar.kd_kamar left join bangsal on kamar.kd_bangsal = bangsal.kd_bangsal where bridging_sep.no_sep like ? and bridging_sep.jnspelayanan like ? and date(bridging_sep.tglpulang) between " +
            "? and ? and length(bridging_sep.no_sep) = 19 " + statusKirim + " and reg_periksa.status_bayar = 'Sudah Bayar' and reg_periksa.kd_pj like ? " + (TCariKompi1.getText().equals("") ? "" : "and ( " +
            "reg_periksa.no_rawat like ? or bridging_sep.no_sep like ? or reg_periksa.no_rkm_medis like ? or pasien.nm_pasien like ? or poliklinik.nm_poli like ? or concat( " +
            "kamar_inap.kd_kamar, ' ', bangsal.nm_bangsal) like ? or dokter.nm_dokter like ?) ") + "group by bridging_sep.no_sep, bridging_sep.no_rawat, reg_periksa.no_rkm_medis, " +
            "reg_periksa.status_lanjut order by bridging_sep.jnspelayanan desc, bridging_sep.no_sep"
        )) {
            ps.setString(1, KODEPPKBPJS);
            if (CmbStatusRawatKompi1.getSelectedIndex() == 0) {
                ps.setString(2, "%");
            } else if (CmbStatusRawatKompi1.getSelectedIndex() == 1) {
                ps.setString(2, "2");
            } else if (CmbStatusRawatKompi1.getSelectedIndex() == 2) {
                ps.setString(2, "1");
            }
            ps.setString(3, Valid.getTglSmc(DTPCari1Kompi1));
            ps.setString(4, Valid.getTglSmc(DTPCari2Kompi1));
            if (! KdPjKompi1.getText().equals("")) {
                ps.setString(5, KdPjKompi1.getText());
            } else {
                ps.setString(5, "%");
            }
            if (!TCariKompi1.getText().equals("")) {
                ps.setString(6, "%" + TCariKompi1.getText() + "%");
                ps.setString(7, "%" + TCariKompi1.getText() + "%");
                ps.setString(8, "%" + TCariKompi1.getText() + "%");
                ps.setString(9, "%" + TCariKompi1.getText() + "%");
                ps.setString(10, "%" + TCariKompi1.getText() + "%");
                ps.setString(11, "%" + TCariKompi1.getText() + "%");
                ps.setString(12, "%" + TCariKompi1.getText() + "%");
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tabMode.addRow(new Object[] {
                        rs.getString("no_rawat"),
                        rs.getString("no_sep"),
                        rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),
                        rs.getString("status_lanjut"),
                        rs.getString("tglsep"),
                        rs.getString("tglpulang"),
                        rs.getString("stts_pulang"),
                        rs.getString("ruangan"),
                        rs.getString("nm_dokter"),
                        rs.getString("kd_penyakit"),
                        (rs.getBoolean("inacbg_terkirim") ? "Terkirim" : "Belum Terkirim"),
                        rs.getString("inacbg_terkirim")
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        LCountKompi1.setText(String.valueOf(tabMode.getRowCount()));
    }

    public void emptTeks() {
        TCariKompi1.setText("");
        lblNamaPasienKompi1.setText("");
        lblNoRawatKompi1.setText("");
        lblNoRMKompi1.setText("");
        lblStatusRawatKompi1.setText("");
        lblNoSEPKompi1.setText("");
        lblTglSEPKompi1.setText("");
        btnSEPKompi1.setText("Tidak Ada");
        btnSEPKompi1.setEnabled(false);
        btnResumeRanapKompi1.setText("Tidak Ada");
        btnResumeRanapKompi1.setEnabled(false);
        btnInvoiceKompi1.setText("Tidak Ada");
        btnInvoiceKompi1.setEnabled(false);
        btnAwalMedisIGDKompi1.setText("Tidak Ada");
        btnAwalMedisIGDKompi1.setEnabled(false);
        btnHasilLabKompi1.setText("Tidak Ada");
        btnHasilLabKompi1.setEnabled(false);
        btnHasilRadKompi1.setText("Tidak Ada");
        btnHasilRadKompi1.setEnabled(false);
        btnSurkonKompi1.setText("Tidak Ada");
        btnSurkonKompi1.setEnabled(false);
        btnSPRIKompi1.setText("Tidak Ada");
        btnSPRIKompi1.setEnabled(false);
        btnPDFKlaimINACBGKompi1.setText("Tidak Ada");
        btnPDFKlaimINACBGKompi1.setEnabled(false);
        tbKompilasiKompi1.clearSelection();
    }
    
    private void flipStatus(JButton button, boolean status) {
        if (status) {
            button.setText("Ada");
            button.setEnabled(true);
        } else {
            button.setText("Tidak Ada");
            button.setEnabled(false);
        }
    }

    private void getData() {
        if (tbKompilasiKompi1.getSelectedRow() != -1) {
            lblNamaPasienKompi1.setText(tbKompilasiKompi1.getValueAt(tbKompilasiKompi1.getSelectedRow(), 3).toString());
            lblNoRawatKompi1.setText(tbKompilasiKompi1.getValueAt(tbKompilasiKompi1.getSelectedRow(), 0).toString());
            lblNoRMKompi1.setText(tbKompilasiKompi1.getValueAt(tbKompilasiKompi1.getSelectedRow(), 2).toString());
            lblStatusRawatKompi1.setText(tbKompilasiKompi1.getValueAt(tbKompilasiKompi1.getSelectedRow(), 4).toString());
            lblNoSEPKompi1.setText(tbKompilasiKompi1.getValueAt(tbKompilasiKompi1.getSelectedRow(), 1).toString());
            if(lblStatusRawatKompi1.getText().equals("Ralan")){
                lblTglSEPKompi1.setText(tbKompilasiKompi1.getValueAt(tbKompilasiKompi1.getSelectedRow(), 5).toString());
            }else{
                lblTglSEPKompi1.setText(tbKompilasiKompi1.getValueAt(tbKompilasiKompi1.getSelectedRow(), 6).toString());
//                lblTglSEP.setText(Sequel.cariIsi("select bridging_sep.tglpulang from bridging_sep where bridging_sep.no_rawat='"+lblNoRawat.getText()+"'").toString().substring(0, 10));
            }
//            lblTglSEP.setText(tbKompilasi.getValueAt(tbKompilasi.getSelectedRow(), 5).toString());
//            lblTglSEP.setText(Sequel.cariIsi("select bridging_sep.tglpulang from bridging_sep where bridging_sep.no_rawat='"+lblNoRawat.getText()+"'"));
            String noSuratKontrol = Sequel.cariIsiSmc("select noskdp from bridging_sep where no_sep = ?", lblNoSEPKompi1.getText());
            if (noSuratKontrol.equals("")) {
                noSuratKontrol = Sequel.cariIsiSmc("select noskdp from bridging_sep where no_rawat = ? and noskdp != ''", lblNoRawatKompi1.getText());
            }
            btnSEPKompi1.setText("Ada");
            btnSEPKompi1.setEnabled(true);
            try (PreparedStatement ps = koneksi.prepareStatement(
                "select exists(select * from data_triase_igd where data_triase_igd.no_rawat = bridging_sep.no_rawat) as ada_triase, " +
                "exists(select * from resume_pasien_ranap where resume_pasien_ranap.no_rawat = bridging_sep.no_rawat) as ada_resume_ranap, " +
                "exists(select * from inacbg_cetak_klaim where inacbg_cetak_klaim.no_sep = bridging_sep.no_sep) as ada_cetak_klaim, " +
                "exists(select * from penilaian_medis_igd where penilaian_medis_igd.no_rawat = bridging_sep.no_rawat) as ada_awal_medis_igd, " +
                "exists(select * from periksa_lab where periksa_lab.no_rawat = bridging_sep.no_rawat) as ada_periksa_lab, " +
                "exists(select * from periksa_radiologi where periksa_radiologi.no_rawat = bridging_sep.no_rawat) as ada_periksa_rad, " +
                "exists(select * from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat = ?) as ada_skdp, " +
                "exists(select * from bridging_surat_pri_bpjs where bridging_surat_pri_bpjs.no_rawat = bridging_sep.no_rawat) as ada_spri, " +
                "exists(select * from billing where billing.no_rawat = bridging_sep.no_rawat) as ada_billing from bridging_sep where bridging_sep.no_sep = ?"
            )) {
                ps.setString(1, noSuratKontrol);
                ps.setString(2, lblNoSEPKompi1.getText());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        flipStatus(btnPDFKlaimINACBGKompi1, rs.getBoolean("ada_cetak_klaim"));
                        flipStatus(btnInvoiceKompi1, rs.getBoolean("ada_billing"));
                        flipStatus(btnTriaseIGDKompi1, rs.getBoolean("ada_triase"));
                        flipStatus(btnAwalMedisIGDKompi1, rs.getBoolean("ada_awal_medis_igd"));
                        flipStatus(btnResumeRanapKompi1, rs.getBoolean("ada_resume_ranap"));
                        flipStatus(btnHasilLabKompi1, rs.getBoolean("ada_periksa_lab"));
                        flipStatus(btnHasilRadKompi1, rs.getBoolean("ada_periksa_rad"));
                        flipStatus(btnSPRIKompi1, rs.getBoolean("ada_spri"));
                        flipStatus(btnSurkonKompi1, rs.getBoolean("ada_skdp"));
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
                flipStatus(btnPDFKlaimINACBGKompi1, false);
                flipStatus(btnInvoiceKompi1, false);
                flipStatus(btnTriaseIGDKompi1, false);
                flipStatus(btnAwalMedisIGDKompi1, false);
                flipStatus(btnResumeRanapKompi1, false);
                flipStatus(btnHasilLabKompi1, false);
                flipStatus(btnHasilRadKompi1, false);
                flipStatus(btnSPRIKompi1, false);
                flipStatus(btnSurkonKompi1, false);
            }
            panelDiagnosaSmcKompi1.setRM(lblNoRawatKompi1.getText(), lblNoRMKompi1.getText(), Valid.getTglSmc(DTPCari1Kompi1), Valid.getTglSmc(DTPCari2Kompi1), lblStatusRawatKompi1.getText());
            panelDiagnosaSmcKompi1.batal();
            panelDiagnosaSmcKompi1.pilihTab(0);
            lblTglSEP1Kompi1.setText(lblTglSEPKompi1.getText().substring(5, 7));
            tampilINACBG();
            tampilBilling();
        }
    }

    public void isCek() {
        lblCoderNIKKompi1.setText(Sequel.cariIsiSmc("select no_ik from inacbg_coder_nik where nik = ?", akses.getkode()));
        KdPjKompi1.setText("BPJ");
        NamaPjKompi1.setText("BPJS KESEHATAN");
    }

    public void isCek(String nik) {
        lblCoderNIKKompi1.setText(nik);
        KdPjKompi1.setText("BPJ");
        NamaPjKompi1.setText("BPJS KESEHATAN");
    }

    public void tampilINACBG() {
        String corona = "BukanCorona";
        String aksi = "";
        
        if (Sequel.cariExistsSmc("select * from perawatan_corona where perawatan_corona.no_rawat = ?", lblNoRawatKompi1.getText())) {
            corona = "PasienCorona";
        }
        
        if (btnPDFKlaimINACBGKompi1.isEnabled()) {
            aksi = "&sukses=true&action=selesai";
        }
        
        String url = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() 
            + "/inacbg/login.php?act=login&usere=" + koneksiDB.USERHYBRIDWEB() + "&passwordte=" + koneksiDB.PASHYBRIDWEB()
            + "&page=DetailKirimSmc&nosep=" + lblNoSEPKompi1.getText() + "&codernik=" + lblCoderNIKKompi1.getText() + "&corona=" + corona + aksi;
        Platform.runLater(() -> {
            WebView view = new WebView();
            engine = view.getEngine();
            engine.setJavaScriptEnabled(true);
            engine.setCreatePopupHandler((PopupFeatures p) -> view.getEngine());
            engine.getLoadWorker().exceptionProperty().addListener((ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) -> {
                if (engine.getLoadWorker().getState() == FAILED) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(PanelContentINACBGKompi1,
                            engine.getLocation() + "\n" + (value != null ? value.getMessage() : "Unexpected error!"),
                            "Loading Catatan...", JOptionPane.ERROR_MESSAGE);
                    });
                }
            });
            
            jfxPanelicare.setScene(new Scene(view));
            try {
                engine.load(url);
            } catch (Exception exception) {
                engine.load(url);
            }
        });
        PanelContentINACBGKompi1.add(jfxPanelicare, BorderLayout.CENTER);
    }

    public void tampilBilling() {
        String norawat = lblNoRawatKompi1.getText();
        try {
            norawat = URLEncoder.encode(norawat, "UTF-8");
        } catch (Exception e) {
            norawat = lblNoRawatKompi1.getText();
        }

        String url = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() 
            + "/berkasrawat/loginlihatbilling.php?act=login&norawat=" + norawat + "&usere=" + koneksiDB.USERHYBRIDWEB() + "&passwordte=" + koneksiDB.PASHYBRIDWEB();
        
        Platform.runLater(() -> {
            WebView view = new WebView();
            engine = view.getEngine();
            engine.setJavaScriptEnabled(true);
            engine.setCreatePopupHandler((PopupFeatures p) -> view.getEngine());
            engine.getLoadWorker().exceptionProperty()
                .addListener((ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) -> {
                    if (engine.getLoadWorker().getState() == FAILED) {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(panelInvoicesKompi1,
                                engine.getLocation() + "\n" + (value != null ? value.getMessage() : "Unexpected error!"),
                                "Loading Catatan...", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                });
            jfxinvoices.setScene(new Scene(view));
            try {
                engine.load(url);
            } catch (Exception exception) {
                engine.load(url);
            }
        });
        panelInvoicesKompi1.add(jfxinvoices, BorderLayout.CENTER);
    }

    private void exportPDF(String reportName, String savedFileName, Map reportParams) {
        try {
            File dir = new File("./berkaspdf/" + tanggalExport);
            if (!dir.isDirectory() && !dir.mkdirs()) {
                Files.createDirectory(dir.toPath());
            }
            JasperPrint jp = JasperFillManager.fillReport("./report/" + reportName, reportParams, koneksi);
            JasperExportManager.exportReportToPdfFile(jp, "./berkaspdf/" + tanggalExport + "/" + lblNoSEPKompi1.getText() + "_" + savedFileName.replaceAll(".pdf", "") + ".pdf");
        } catch (Exception e) {
            exportSukses = false;
            System.out.println("Notif : " + e);
        }
    }

    private void exportPDF(String reportName, String savedFileName, Map reportParams, String sql, String... values) {
        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            File dir = new File("./berkaspdf/" + tanggalExport);
            if (!dir.isDirectory() && !dir.mkdirs()) {
                Files.createDirectory(dir.toPath());
            }
            JasperExportManager.exportReportToPdfFile(JasperFillManager.fillReport("./report/" + reportName, reportParams, new JRResultSetDataSource(ps.executeQuery())),
                "./berkaspdf/" + tanggalExport + "/" + lblNoSEPKompi1.getText() + "_" + savedFileName.replaceAll(".pdf", "") + ".pdf"
            );
        } catch (Exception e) {
            exportSukses = false;
            System.out.println("Notif : " + e);
        }
    }

    private void exportSEP(String urutan) {
        if (lblNoSEPKompi1.getText().equals("Tidak Ada")) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("namars", akses.getnamars());
        param.put("alamatrs", akses.getalamatrs());
        param.put("kotars", akses.getkabupatenrs());
        param.put("propinsirs", akses.getpropinsirs());
        param.put("kontakrs", akses.getkontakrs());
        param.put("norawat", lblNoRawatKompi1.getText());
        param.put("prb", Sequel.cariIsiSmc("select bpjs_prb.prb from bpjs_prb where bpjs_prb.no_sep = ?", lblNoSEPKompi1.getText()));
        param.put("noreg", Sequel.cariIsiSmc("select no_reg from reg_periksa where no_rawat = ?", lblNoRawatKompi1.getText()));
        param.put("logo", Sequel.cariGambar("select gambar.bpjs from gambar"));
        param.put("parameter", lblNoSEPKompi1.getText());
        param.put("cetakanke", 2);
        if (lblStatusRawatKompi1.getText().contains("Ranap")) {
            exportPDF("rptBridgingSEP.jasper", urutan + "_SEP", param);
        } else {
            exportPDF("rptBridgingSEP2.jasper", urutan + "_SEP", param);
        }
    }
    
    private void exportKlaimINACBG(String urutan) {
        if (! btnPDFKlaimINACBGKompi1.isEnabled()) {
            return;
        }
        
        String filename = Sequel.cariIsiSmc("select path from inacbg_cetak_klaim where no_sep = ?", lblNoSEPKompi1.getText());
        if (filename.equals("")) {
            return;
        }
        
        try {
            File dir = new File("./berkaspdf/" + tanggalExport);
            if (!dir.isDirectory() && !dir.mkdirs()) {
                Files.createDirectory(dir.toPath());
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        
        HttpURLConnection http;
        String url = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/inacbg/" + filename;
        String exportPath = "./berkaspdf/" + tanggalExport + "/" + lblNoSEPKompi1.getText() + "_" + urutan + "_KlaimINACBG.pdf";
        if (filename.endsWith(".pdf")) {
            try (FileOutputStream os = new FileOutputStream(exportPath); FileChannel fileChannel = os.getChannel()) {
                URL fileUrl = new URL(url);
                http = (HttpURLConnection) fileUrl.openConnection();
                if (http.getResponseCode() == 200) {
                    fileChannel.transferFrom(Channels.newChannel(fileUrl.openStream()), 0, Long.MAX_VALUE);
                    http.disconnect();
                } else if (http.getResponseCode() / 100 == 4) {
                    throw new Exception("Terjadi kesalahan pada saat mengakses file klaim INACBG..!! Silahkan hubungi administrator.\nFilename : " + filename);
                } else {
                    throw new Exception ("Sambungan ke server terputus..!!");
                }
            } catch (Exception e) {
                exportSukses = false;
                System.out.println("Notif : " + e);
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    
    private void exportKlaimINACBGs() {
        if (! btnPDFKlaimINACBGKompi1.isEnabled()) {
            return;
        }
        
        String filename = Sequel.cariIsiSmc("select path from inacbg_cetak_klaim where no_sep = ?", lblNoSEPKompi1.getText());
        if (filename.equals("")) {
            return;
        }
        
        try {
            File dir = new File("./hasilkompilasiklaim/" + tanggalExport);
            if (!dir.isDirectory() && !dir.mkdirs()) {
                Files.createDirectory(dir.toPath());
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        
        HttpURLConnection http;
        String url = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/inacbg/" + filename;
        String exportPath = "./hasilkompilasiklaim/00"+lblNoRawatKompi1.getText().replaceAll("/", "")+ "PDFNCC.pdf";
        if (filename.endsWith(".pdf")) {
            try (FileOutputStream os = new FileOutputStream(exportPath); FileChannel fileChannel = os.getChannel()) {
                URL fileUrl = new URL(url);
                http = (HttpURLConnection) fileUrl.openConnection();
                if (http.getResponseCode() == 200) {
                    fileChannel.transferFrom(Channels.newChannel(fileUrl.openStream()), 0, Long.MAX_VALUE);
                    http.disconnect();
                } else if (http.getResponseCode() / 100 == 4) {
                    throw new Exception("Terjadi kesalahan pada saat mengakses file klaim INACBG..!! Silahkan hubungi administrator.\nFilename : " + filename);
                } else {
                    throw new Exception ("Sambungan ke server terputus..!!");
                }
            } catch (Exception e) {
                exportSukses = false;
                System.out.println("Notif : " + e);
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private void exportResumeRanap(String urutan) {
        if (!btnResumeRanapKompi1.isEnabled()) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("namars", akses.getnamars());
        param.put("alamatrs", akses.getalamatrs());
        param.put("kotars", akses.getkabupatenrs());
        param.put("propinsirs", akses.getpropinsirs());
        param.put("kontakrs", akses.getkontakrs());
        param.put("emailrs", akses.getemailrs());
        param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
        param.put("norawat", lblNoRawatKompi1.getText());
        String waktuKeluar = "", tglKeluar = "", jamKeluar = "";
        waktuKeluar = Sequel.cariIsiSmc("select concat(tgl_keluar, ' ', jam_keluar) from kamar_inap where no_rawat = ? and stts_pulang != 'Pindah Kamar' order by concat(tgl_keluar, ' ', jam_keluar) limit 1", lblNoRawatKompi1.getText());
        if (!waktuKeluar.equals("")) {
            tglKeluar = waktuKeluar.substring(0, 10);
            jamKeluar = waktuKeluar.substring(11, 19);
        }
        String kodeDokter = Sequel.cariIsiSmc("select kd_dokter from resume_pasien_ranap where no_rawat = ?", lblNoRawatKompi1.getText());
        String namaDokter = Sequel.cariIsiSmc("select nm_dokter from dokter where kd_dokter = ?", kodeDokter);
        finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik = ?", kodeDokter);
        param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + namaDokter + "\nID " + (finger.equals("") ? kodeDokter : finger) + "\n" + Valid.SetTgl3(tglKeluar));
        param.put("ruang", Sequel.cariIsiSmc("select concat(kamar_inap.kd_kamar, ' ', bangsal.nm_bangsal) from kamar_inap join kamar on kamar_inap.kd_kamar = kamar.kd_kamar "
            + "join bangsal on kamar.kd_bangsal = bangsal.kd_bangsal where kamar_inap.no_rawat = ? and kamar_inap.tgl_keluar = ? and kamar_inap.jam_keluar = ?",
            lblNoRawatKompi1.getText(), tglKeluar, jamKeluar)
        );
        param.put("tanggalkeluar", Valid.SetTgl3(tglKeluar));
        param.put("jamkeluar", jamKeluar);
        try (PreparedStatement ps = koneksi.prepareStatement("select dpjp_ranap.kd_dokter, dokter.nm_dokter from dpjp_ranap join dokter on dpjp_ranap.kd_dokter = dokter.kd_dokter where dpjp_ranap.no_rawat = ? and dpjp_ranap.kd_dokter != ?")) {
            ps.setString(1, lblNoRawatKompi1.getText());
            ps.setString(2, kodeDokter);
            try (ResultSet rs = ps.executeQuery()) {
                for (int i = 2; rs.next(); i++) {
                    if (i == 2) {
                        finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs.getString("kd_dokter"));
                        param.put("finger2", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs.getString("nm_dokter") + "\nID " + (finger.equals("") ? rs.getString("kd_dokter") : finger) + "\n" + Valid.SetTgl3(tglKeluar));
                        param.put("namadokter2", rs.getString("nm_dokter"));
                    }
                    if (i == 3) {
                        finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs.getString("kd_dokter"));
                        param.put("finger3", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs.getString("nm_dokter") + "\nID " + (finger.equals("") ? rs.getString("kd_dokter") : finger) + "\n" + Valid.SetTgl3(tglKeluar));
                        param.put("namadokter3", rs.getString("nm_dokter"));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        exportPDF("rptLaporanResumeRanapKompilasi.jasper", urutan + "_ResumePasien", param);
    }

    private void exportBilling(String urutan) {
        if (! btnInvoiceKompi1.isEnabled()) return;
        
        String norawat = lblNoRawatKompi1.getText();
        try {
            norawat = URLEncoder.encode(norawat, "UTF-8");
        } catch (Exception e) {
            norawat = lblNoRawatKompi1.getText();
        }
        String link = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() 
            + "/berkasrawat/loginlihatbilling.php?act=login&norawat=" + norawat + "&usere=" + koneksiDB.USERHYBRIDWEB() + "&passwordte=" + koneksiDB.PASHYBRIDWEB();
        try (FileOutputStream os = new FileOutputStream("./berkaspdf/" + tanggalExport + "/" + lblNoSEPKompi1.getText() + "_" + urutan + "_Billing.pdf")) {
            URL url = new URL(link);
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(url, 30000);
            jsoupDoc.head().appendElement("style").appendText("body { font-family: Arial, sans-serif }");
            org.w3c.dom.Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withW3cDocument(w3cDoc, link);
            builder.toStream(os);
            builder.run();
        } catch (Exception e) {
            exportSukses = false;
            System.out.println("Notif : " + e);
            cleanupSinglePDF(lblNoSEPKompi1.getText() + "_" + urutan + "_Billing.pdf");
        }
    }
    
    private void exportTriaseIGD(String urutan) {
        if (! btnTriaseIGDKompi1.isEnabled()) {
            return;
        }
        
        String detailTriase = "";
        int i = 0;
        Map<String, Object> param = new HashMap<>();
        param.put("namars", akses.getnamars());
        param.put("alamatrs", akses.getalamatrs());
        param.put("kotars", akses.getkabupatenrs());
        param.put("propinsirs", akses.getpropinsirs());
        param.put("kontakrs", akses.getkontakrs());
        param.put("emailrs", akses.getemailrs());
        param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
        Sequel.deleteTemporary();
        try (PreparedStatement ps = koneksi.prepareStatement(
            "select t.no_rawat, " +
            "(select count(*) from data_triase_igddetail_skala1 s1 where s1.no_rawat = t.no_rawat) as cs1, " +
            "(select count(*) from data_triase_igddetail_skala2 s2 where s2.no_rawat = t.no_rawat) as cs2, " +
            "(select count(*) from data_triase_igddetail_skala3 s3 where s3.no_rawat = t.no_rawat) as cs3, " +
            "(select count(*) from data_triase_igddetail_skala4 s4 where s4.no_rawat = t.no_rawat) as cs4, " +
            "(select count(*) from data_triase_igddetail_skala5 s5 where s5.no_rawat = t.no_rawat) as cs5 " +
            "from data_triase_igd t where t.no_rawat = ?"
        )) {
            ps.setString(1, lblNoRawatKompi1.getText());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (rs.getInt("cs1") > 0) {
                        try (PreparedStatement ps1 = koneksi.prepareStatement(
                            "select data_triase_igdprimer.keluhan_utama, data_triase_igdprimer.kebutuhan_khusus, data_triase_igdprimer.catatan, data_triase_igdprimer.plan, data_triase_igdprimer.tanggaltriase, " +
                            "data_triase_igdprimer.nik, data_triase_igd.tekanan_darah, data_triase_igd.nadi, data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, data_triase_igd.nyeri, " +
                            "data_triase_igd.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir, pegawai.nama, data_triase_igd.tgl_kunjungan, data_triase_igd.cara_masuk, master_triase_macam_kasus.macam_kasus " +
                            "from data_triase_igdprimer join data_triase_igd on data_triase_igdprimer.no_rawat = data_triase_igd.no_rawat join master_triase_macam_kasus on data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus " +
                            "join reg_periksa on data_triase_igdprimer.no_rawat = reg_periksa.no_rawat join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join pegawai on data_triase_igdprimer.nik = pegawai.nik where data_triase_igd.no_rawat = ?"
                        )) {
                            ps1.setString(1, lblNoRawatKompi1.getText());
                            try (ResultSet rs1 = ps1.executeQuery()) {
                                if (rs1.next()) {
                                    param.put("norawat", rs1.getString("no_rawat"));
                                    param.put("norm", rs1.getString("no_rkm_medis"));
                                    param.put("namapasien", rs1.getString("nm_pasien"));
                                    param.put("tanggallahir", rs1.getDate("tgl_lahir"));
                                    param.put("jk", rs1.getString("jk").replaceAll("L", "Laki-Laki").replaceAll("P", "Perempuan"));
                                    param.put("tanggalkunjungan", rs1.getDate("tgl_kunjungan"));
                                    param.put("jamkunjungan", rs1.getString("tgl_kunjungan").substring(11, 19));
                                    param.put("caradatang", rs1.getString("cara_masuk"));
                                    param.put("macamkasus", rs1.getString("macam_kasus"));
                                    param.put("keluhanutama", rs1.getString("keluhan_utama"));
                                    param.put("kebutuhankhusus", rs1.getString("kebutuhan_khusus"));
                                    param.put("plan", rs1.getString("plan"));
                                    param.put("tanggaltriase", rs1.getDate("tanggaltriase"));
                                    param.put("jamtriase", rs1.getString("tanggaltriase").substring(11, 19));
                                    param.put("pegawai", rs1.getString("nama"));
                                    param.put("catatan", rs1.getString("catatan"));
                                    param.put("tandavital", "Suhu (C) : " + rs1.getString("suhu") + ", Nyeri : " + rs1.getString("nyeri") + ", Tensi : " + rs1.getString("tekanan_darah") + ", Nadi(/menit) : " + rs1.getString("nadi") + ", Saturasi O²(%) : " + rs1.getString("saturasi_o2") + ", Respirasi(/menit) : " + rs1.getString("pernapasan"));
                                    finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs1.getString("nik"));
                                    param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs1.getString("nama") + "\nID " + (finger.equals("") ? rs1.getString("nik") : finger) + "\n" + Valid.SetTgl3(rs1.getString("tanggaltriase")));
                                    try (PreparedStatement ps2 = koneksi.prepareStatement(
                                        "select master_triase_pemeriksaan.kode_pemeriksaan, master_triase_pemeriksaan.nama_pemeriksaan from master_triase_pemeriksaan " +
                                        "join master_triase_skala1 on master_triase_pemeriksaan.kode_pemeriksaan = master_triase_skala1.kode_pemeriksaan " +
                                        "join data_triase_igddetail_skala1 on master_triase_skala1.kode_skala1 = data_triase_igddetail_skala1.kode_skala1 " +
                                        "where data_triase_igddetail_skala1.no_rawat = ? group by master_triase_pemeriksaan.kode_pemeriksaan " +
                                        "order by master_triase_pemeriksaan.kode_pemeriksaan"
                                    )) {
                                        ps2.setString(1, lblNoRawatKompi1.getText());
                                        try (ResultSet rs2 = ps2.executeQuery()) {
                                            while (rs2.next()) {
                                                detailTriase = "";
                                                try (PreparedStatement ps3 = koneksi.prepareStatement(
                                                    "select master_triase_skala1.pengkajian_skala1 from master_triase_skala1 " +
                                                    "join data_triase_igddetail_skala1 on master_triase_skala1.kode_skala1 = data_triase_igddetail_skala1.kode_skala1 " +
                                                    "where master_triase_skala1.kode_pemeriksaan = ? and data_triase_igddetail_skala1.no_rawat = ? " +
                                                    "order by data_triase_igddetail_skala1.kode_skala1"
                                                )) {
                                                    ps3.setString(1, rs2.getString(1));
                                                    ps3.setString(2, lblNoRawatKompi1.getText());
                                                    try (ResultSet rs3 = ps3.executeQuery()) {
                                                        while (rs3.next()) {
                                                            detailTriase = rs3.getString(1) + ", " + detailTriase;
                                                        }
                                                    }
                                                }
                                                detailTriase = detailTriase.substring(0, detailTriase.length() - 2);
                                                Sequel.temporary(String.valueOf(++i), rs2.getString("nama_pemeriksaan"), detailTriase);
                                            }
                                        }
                                    }
                                    exportPDF("rptLembarTriaseSkala1.jasper", urutan + "_TriaseSkala1", param, "select * from temporary where temp37 = ?", akses.getalamatip());
                                }
                            }
                        }
                    } else if (rs.getInt("cs2") > 0) {
                        try (PreparedStatement ps1 = koneksi.prepareStatement(
                            "select data_triase_igdprimer.keluhan_utama, data_triase_igdprimer.kebutuhan_khusus, data_triase_igdprimer.catatan, data_triase_igdprimer.plan, data_triase_igdprimer.tanggaltriase, " +
                            "data_triase_igdprimer.nik, data_triase_igd.tekanan_darah, data_triase_igd.nadi, data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, data_triase_igd.nyeri, " +
                            "data_triase_igd.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir, pegawai.nama, data_triase_igd.tgl_kunjungan, data_triase_igd.cara_masuk, master_triase_macam_kasus.macam_kasus " +
                            "from data_triase_igdprimer join data_triase_igd on data_triase_igdprimer.no_rawat = data_triase_igd.no_rawat join master_triase_macam_kasus on data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus " +
                            "join reg_periksa on data_triase_igdprimer.no_rawat = reg_periksa.no_rawat join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join pegawai on data_triase_igdprimer.nik = pegawai.nik where data_triase_igd.no_rawat = ?"
                        )) {
                            ps1.setString(1, lblNoRawatKompi1.getText());
                            try (ResultSet rs1 = ps1.executeQuery()) {
                                if (rs1.next()) {
                                    param.put("norawat", rs1.getString("no_rawat"));
                                    param.put("norm", rs1.getString("no_rkm_medis"));
                                    param.put("namapasien", rs1.getString("nm_pasien"));
                                    param.put("tanggallahir", rs1.getDate("tgl_lahir"));
                                    param.put("jk", rs1.getString("jk").replaceAll("L", "Laki-Laki").replaceAll("P", "Perempuan"));
                                    param.put("tanggalkunjungan", rs1.getDate("tgl_kunjungan"));
                                    param.put("jamkunjungan", rs1.getString("tgl_kunjungan").substring(11, 19));
                                    param.put("caradatang", rs1.getString("cara_masuk"));
                                    param.put("macamkasus", rs1.getString("macam_kasus"));
                                    param.put("keluhanutama", rs1.getString("keluhan_utama"));
                                    param.put("kebutuhankhusus", rs1.getString("kebutuhan_khusus"));
                                    param.put("plan", rs1.getString("plan"));
                                    param.put("tanggaltriase", rs1.getDate("tanggaltriase"));
                                    param.put("jamtriase", rs1.getString("tanggaltriase").substring(11, 19));
                                    param.put("pegawai", rs1.getString("nama"));
                                    param.put("catatan", rs1.getString("catatan"));
                                    param.put("tandavital", "Suhu (C) : " + rs1.getString("suhu") + ", Nyeri : " + rs1.getString("nyeri") + ", Tensi : " + rs1.getString("tekanan_darah") + ", Nadi(/menit) : " + rs1.getString("nadi") + ", Saturasi O²(%) : " + rs1.getString("saturasi_o2") + ", Respirasi(/menit) : " + rs1.getString("pernapasan"));
                                    finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs1.getString("nik"));
                                    param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs1.getString("nama") + "\nID " + (finger.equals("") ? rs1.getString("nik") : finger) + "\n" + Valid.SetTgl3(rs1.getString("tanggaltriase")));
                                    try (PreparedStatement ps2 = koneksi.prepareStatement(
                                        "select master_triase_pemeriksaan.kode_pemeriksaan, master_triase_pemeriksaan.nama_pemeriksaan from master_triase_pemeriksaan " +
                                        "join master_triase_skala2 on master_triase_pemeriksaan.kode_pemeriksaan = master_triase_skala2.kode_pemeriksaan " +
                                        "join data_triase_igddetail_skala2 on master_triase_skala2.kode_skala2 = data_triase_igddetail_skala2.kode_skala2 " +
                                        "where data_triase_igddetail_skala2.no_rawat = ? group by master_triase_pemeriksaan.kode_pemeriksaan " +
                                        "order by master_triase_pemeriksaan.kode_pemeriksaan"
                                    )) {
                                        ps2.setString(1, lblNoRawatKompi1.getText());
                                        try (ResultSet rs2 = ps2.executeQuery()) {
                                            while (rs2.next()) {
                                                detailTriase = "";
                                                try (PreparedStatement ps3 = koneksi.prepareStatement(
                                                    "select master_triase_skala2.pengkajian_skala2 from master_triase_skala2 " +
                                                    "join data_triase_igddetail_skala2 on master_triase_skala2.kode_skala2 = data_triase_igddetail_skala2.kode_skala2 " +
                                                    "where master_triase_skala2.kode_pemeriksaan = ? and data_triase_igddetail_skala2.no_rawat = ? " +
                                                    "order by data_triase_igddetail_skala2.kode_skala2"
                                                )) {
                                                    ps3.setString(1, rs2.getString(1));
                                                    ps3.setString(2, lblNoRawatKompi1.getText());
                                                    try (ResultSet rs3 = ps3.executeQuery()) {
                                                        while (rs3.next()) {
                                                            detailTriase = rs3.getString(1) + ", " + detailTriase;
                                                        }
                                                    }
                                                }
                                                detailTriase = detailTriase.substring(0, detailTriase.length() - 2);
                                                Sequel.temporary(String.valueOf(++i), rs2.getString("nama_pemeriksaan"), detailTriase);
                                            }
                                        }
                                    }
                                    exportPDF("rptLembarTriaseSkala2.jasper", urutan + "_TriaseSkala2", param, "select * from temporary where temp37 = ?", akses.getalamatip());
                                }
                            }
                        }
                    } else if (rs.getInt("cs3") > 0) {
                        try (PreparedStatement ps1 = koneksi.prepareStatement(
                            "select data_triase_igdsekunder.anamnesa_singkat, data_triase_igdsekunder.catatan, data_triase_igdsekunder.plan, data_triase_igdsekunder.tanggaltriase, data_triase_igdsekunder.nik, " +
                            "data_triase_igd.tekanan_darah, data_triase_igd.nadi, data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, data_triase_igd.nyeri, data_triase_igd.no_rawat, " +
                            "pasien.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir, pegawai.nama, data_triase_igd.tgl_kunjungan, data_triase_igd.cara_masuk, master_triase_macam_kasus.macam_kasus " +
                            "from data_triase_igdsekunder join data_triase_igd on data_triase_igdsekunder.no_rawat = data_triase_igd.no_rawat join master_triase_macam_kasus on data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus " +
                            "join reg_periksa on data_triase_igdsekunder.no_rawat = reg_periksa.no_rawat join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join pegawai on data_triase_igdsekunder.nik = pegawai.nik where data_triase_igd.no_rawat = ?"
                        )) {
                            ps1.setString(1, lblNoRawatKompi1.getText());
                            try (ResultSet rs1 = ps1.executeQuery()) {
                                if (rs1.next()) {
                                    param.put("norawat", rs1.getString("no_rawat"));
                                    param.put("norm", rs1.getString("no_rkm_medis"));
                                    param.put("namapasien", rs1.getString("nm_pasien"));
                                    param.put("tanggallahir", rs1.getDate("tgl_lahir"));
                                    param.put("jk", rs1.getString("jk").replaceAll("L", "Laki-Laki").replaceAll("P", "Perempuan"));
                                    param.put("tanggalkunjungan", rs1.getDate("tgl_kunjungan"));
                                    param.put("jamkunjungan", rs1.getString("tgl_kunjungan").substring(11, 19));
                                    param.put("caradatang", rs1.getString("cara_masuk"));
                                    param.put("macamkasus", rs1.getString("macam_kasus"));
                                    param.put("keluhanutama", rs1.getString("anamnesa_singkat"));
                                    param.put("plan", rs1.getString("plan"));
                                    param.put("tanggaltriase", rs1.getDate("tanggaltriase"));
                                    param.put("tandavital", "Suhu (C) : " + rs1.getString("suhu") + ", Nyeri : " + rs1.getString("nyeri") + ", Tensi : " + rs1.getString("tekanan_darah") + ", Nadi(/menit) : " + rs1.getString("nadi") + ", Saturasi O²(%) : " + rs1.getString("saturasi_o2") + ", Respirasi(/menit) : " + rs1.getString("pernapasan"));
                                    param.put("jamtriase", rs1.getString("tanggaltriase").substring(11, 19));
                                    param.put("pegawai", rs1.getString("nama"));
                                    param.put("catatan", rs1.getString("catatan"));
                                    finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs1.getString("nik"));
                                    param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs1.getString("nama") + "\nID " + (finger.equals("") ? rs1.getString("nik") : finger) + "\n" + Valid.SetTgl3(rs1.getString("tanggaltriase")));
                                    try (PreparedStatement ps2 = koneksi.prepareStatement(
                                        "select master_triase_pemeriksaan.kode_pemeriksaan, master_triase_pemeriksaan.nama_pemeriksaan from master_triase_pemeriksaan " +
                                        "join master_triase_skala3 on master_triase_pemeriksaan.kode_pemeriksaan = master_triase_skala3.kode_pemeriksaan " +
                                        "join data_triase_igddetail_skala3 on master_triase_skala3.kode_skala3 = data_triase_igddetail_skala3.kode_skala3 " +
                                        "where data_triase_igddetail_skala3.no_rawat = ? group by master_triase_pemeriksaan.kode_pemeriksaan " +
                                        "order by master_triase_pemeriksaan.kode_pemeriksaan"
                                    )) {
                                        ps2.setString(1, lblNoRawatKompi1.getText());
                                        try (ResultSet rs2 = ps2.executeQuery()) {
                                            while (rs2.next()) {
                                                detailTriase = "";
                                                try (PreparedStatement ps3 = koneksi.prepareStatement(
                                                    "select master_triase_skala3.pengkajian_skala3 from master_triase_skala3 join data_triase_igddetail_skala3 " +
                                                    "on master_triase_skala3.kode_skala3 = data_triase_igddetail_skala3.kode_skala3 where master_triase_skala3.kode_pemeriksaan = ? " +
                                                    "and data_triase_igddetail_skala3.no_rawat = ? order by data_triase_igddetail_skala3.kode_skala3"
                                                )) {
                                                    ps3.setString(1, rs2.getString("kode_pemeriksaan"));
                                                    ps3.setString(2, lblNoRawatKompi1.getText());
                                                    try (ResultSet rs3 = ps3.executeQuery()) {
                                                        while (rs3.next()) {
                                                            detailTriase = rs3.getString(1) + ", " + detailTriase;
                                                        }
                                                    }
                                                }
                                                detailTriase = detailTriase.substring(0, detailTriase.length() - 2);
                                                Sequel.temporary(String.valueOf(++i), rs2.getString("nama_pemeriksaan"), detailTriase);
                                            }
                                        }
                                    }
                                    exportPDF("rptLembarTriaseSkala3.jasper", urutan + "_TriaseSkala3", param, "select * from temporary where temp37 = ?", akses.getalamatip());
                                }
                            }
                        }
                    } else if (rs.getInt("cs4") > 0) {
                        try (PreparedStatement ps1 = koneksi.prepareStatement(
                            "select data_triase_igdsekunder.anamnesa_singkat, data_triase_igdsekunder.catatan, data_triase_igdsekunder.plan, data_triase_igdsekunder.tanggaltriase, data_triase_igdsekunder.nik, " +
                            "data_triase_igd.tekanan_darah, data_triase_igd.nadi, data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, data_triase_igd.nyeri, data_triase_igd.no_rawat, " +
                            "pasien.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir, pegawai.nama, data_triase_igd.tgl_kunjungan, data_triase_igd.cara_masuk, master_triase_macam_kasus.macam_kasus " +
                            "from data_triase_igdsekunder join data_triase_igd on data_triase_igdsekunder.no_rawat = data_triase_igd.no_rawat join master_triase_macam_kasus on data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus " +
                            "join reg_periksa on data_triase_igdsekunder.no_rawat = reg_periksa.no_rawat join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join pegawai on data_triase_igdsekunder.nik = pegawai.nik where data_triase_igd.no_rawat = ?"
                        )) {
                            ps1.setString(1, lblNoRawatKompi1.getText());
                            try (ResultSet rs1 = ps1.executeQuery()) {
                                if (rs1.next()) {
                                    param.put("norawat", rs1.getString("no_rawat"));
                                    param.put("norm", rs1.getString("no_rkm_medis"));
                                    param.put("namapasien", rs1.getString("nm_pasien"));
                                    param.put("tanggallahir", rs1.getDate("tgl_lahir"));
                                    param.put("jk", rs1.getString("jk").replaceAll("L", "Laki-Laki").replaceAll("P", "Perempuan"));
                                    param.put("tanggalkunjungan", rs1.getDate("tgl_kunjungan"));
                                    param.put("jamkunjungan", rs1.getString("tgl_kunjungan").substring(11, 19));
                                    param.put("caradatang", rs1.getString("cara_masuk"));
                                    param.put("macamkasus", rs1.getString("macam_kasus"));
                                    param.put("keluhanutama", rs1.getString("anamnesa_singkat"));
                                    param.put("plan", rs1.getString("plan"));
                                    param.put("tanggaltriase", rs1.getDate("tanggaltriase"));
                                    param.put("tandavital", "Suhu (C) : " + rs1.getString("suhu") + ", Nyeri : " + rs1.getString("nyeri") + ", Tensi : " + rs1.getString("tekanan_darah") + ", Nadi(/menit) : " + rs1.getString("nadi") + ", Saturasi O²(%) : " + rs1.getString("saturasi_o2") + ", Respirasi(/menit) : " + rs1.getString("pernapasan"));
                                    param.put("jamtriase", rs1.getString("tanggaltriase").substring(11, 19));
                                    param.put("pegawai", rs1.getString("nama"));
                                    param.put("catatan", rs1.getString("catatan"));
                                    finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs1.getString("nik"));
                                    param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs1.getString("nama") + "\nID " + (finger.equals("") ? rs1.getString("nik") : finger) + "\n" + Valid.SetTgl3(rs1.getString("tanggaltriase")));
                                    try (PreparedStatement ps2 = koneksi.prepareStatement(
                                        "select master_triase_pemeriksaan.kode_pemeriksaan, master_triase_pemeriksaan.nama_pemeriksaan from master_triase_pemeriksaan " +
                                        "join master_triase_skala4 on master_triase_pemeriksaan.kode_pemeriksaan = master_triase_skala4.kode_pemeriksaan " +
                                        "join data_triase_igddetail_skala4 on master_triase_skala4.kode_skala4 = data_triase_igddetail_skala4.kode_skala4 " +
                                        "where data_triase_igddetail_skala4.no_rawat = ? group by master_triase_pemeriksaan.kode_pemeriksaan " +
                                        "order by master_triase_pemeriksaan.kode_pemeriksaan"
                                    )) {
                                        ps2.setString(1, lblNoRawatKompi1.getText());
                                        try (ResultSet rs2 = ps2.executeQuery()) {
                                            while (rs2.next()) {
                                                detailTriase = "";
                                                try (PreparedStatement ps3 = koneksi.prepareStatement(
                                                    "select master_triase_skala4.pengkajian_skala4 from master_triase_skala4 join data_triase_igddetail_skala4 " +
                                                    "on master_triase_skala4.kode_skala4 = data_triase_igddetail_skala4.kode_skala4 where master_triase_skala4.kode_pemeriksaan = ? " +
                                                    "and data_triase_igddetail_skala4.no_rawat = ? order by data_triase_igddetail_skala4.kode_skala4"
                                                )) {
                                                    ps3.setString(1, rs2.getString(1));
                                                    ps3.setString(2, lblNoRawatKompi1.getText());
                                                    try (ResultSet rs3 = ps3.executeQuery()) {
                                                        while (rs3.next()) {
                                                            detailTriase = rs3.getString(1) + ", " + detailTriase;
                                                        }
                                                    }
                                                }
                                                detailTriase = detailTriase.substring(0, detailTriase.length() - 2);
                                                Sequel.temporary(String.valueOf(++i), rs2.getString("nama_pemeriksaan"), detailTriase);
                                            }
                                        }
                                    }
                                    exportPDF("rptLembarTriaseSkala4.jasper", urutan + "_TriaseSkala4", param, "select * from temporary where temp37 = ?", akses.getalamatip());
                                }
                            }
                        }
                    } else if (rs.getInt("cs5") > 0) {
                        try (PreparedStatement ps1 = koneksi.prepareStatement(
                            "select data_triase_igdsekunder.anamnesa_singkat, data_triase_igdsekunder.catatan, data_triase_igdsekunder.plan, data_triase_igdsekunder.tanggaltriase, data_triase_igdsekunder.nik, " +
                            "data_triase_igd.tekanan_darah, data_triase_igd.nadi, data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, data_triase_igd.nyeri, data_triase_igd.no_rawat, " +
                            "pasien.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir, pegawai.nama, data_triase_igd.tgl_kunjungan, data_triase_igd.cara_masuk, master_triase_macam_kasus.macam_kasus " +
                            "from data_triase_igdsekunder join data_triase_igd on data_triase_igdsekunder.no_rawat = data_triase_igd.no_rawat join master_triase_macam_kasus on data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus " +
                            "join reg_periksa on data_triase_igdsekunder.no_rawat = reg_periksa.no_rawat join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join pegawai on data_triase_igdsekunder.nik = pegawai.nik where data_triase_igd.no_rawat = ?"
                        )) {
                            ps1.setString(1, lblNoRawatKompi1.getText());
                            try (ResultSet rs1 = ps1.executeQuery()) {
                                if (rs1.next()) {
                                    param.put("norawat", rs1.getString("no_rawat"));
                                    param.put("norm", rs1.getString("no_rkm_medis"));
                                    param.put("namapasien", rs1.getString("nm_pasien"));
                                    param.put("tanggallahir", rs1.getDate("tgl_lahir"));
                                    param.put("jk", rs1.getString("jk").replaceAll("L", "Laki-Laki").replaceAll("P", "Perempuan"));
                                    param.put("tanggalkunjungan", rs1.getDate("tgl_kunjungan"));
                                    param.put("jamkunjungan", rs1.getString("tgl_kunjungan").substring(11, 19));
                                    param.put("caradatang", rs1.getString("cara_masuk"));
                                    param.put("macamkasus", rs1.getString("macam_kasus"));
                                    param.put("keluhanutama", rs1.getString("anamnesa_singkat"));
                                    param.put("plan", rs1.getString("plan"));
                                    param.put("tanggaltriase", rs1.getDate("tanggaltriase"));
                                    param.put("tandavital", "Suhu (C) : " + rs1.getString("suhu") + ", Nyeri : " + rs1.getString("nyeri") + ", Tensi : " + rs1.getString("tekanan_darah") + ", Nadi(/menit) : " + rs1.getString("nadi") + ", Saturasi O²(%) : " + rs1.getString("saturasi_o2") + ", Respirasi(/menit) : " + rs1.getString("pernapasan"));
                                    param.put("jamtriase", rs1.getString("tanggaltriase").substring(11, 19));
                                    param.put("pegawai", rs1.getString("nama"));
                                    param.put("catatan", rs1.getString("catatan"));
                                    finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs1.getString("nik"));
                                    param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs1.getString("nama") + "\nID " + (finger.equals("") ? rs1.getString("nik") : finger) + "\n" + Valid.SetTgl3(rs1.getString("tanggaltriase")));
                                    try (PreparedStatement ps2 = koneksi.prepareStatement(
                                        "select master_triase_pemeriksaan.kode_pemeriksaan, master_triase_pemeriksaan.nama_pemeriksaan from master_triase_pemeriksaan " +
                                        "join master_triase_skala5 on master_triase_pemeriksaan.kode_pemeriksaan = master_triase_skala5.kode_pemeriksaan " +
                                        "join data_triase_igddetail_skala5 on master_triase_skala5.kode_skala5 = data_triase_igddetail_skala5.kode_skala5 " +
                                        "where data_triase_igddetail_skala5.no_rawat = ? group by master_triase_pemeriksaan.kode_pemeriksaan " +
                                        "order by master_triase_pemeriksaan.kode_pemeriksaan"
                                    )) {
                                        ps2.setString(1, lblNoRawatKompi1.getText());
                                        try (ResultSet rs2 = ps2.executeQuery()) {
                                            while (rs2.next()) {
                                                detailTriase = "";
                                                try (PreparedStatement ps3 = koneksi.prepareStatement(
                                                    "select master_triase_skala5.pengkajian_skala5 from master_triase_skala5 join data_triase_igddetail_skala5 " +
                                                    "on master_triase_skala5.kode_skala5 = data_triase_igddetail_skala5.kode_skala5 where master_triase_skala5.kode_pemeriksaan = ? " +
                                                    "and data_triase_igddetail_skala5.no_rawat = ? order by data_triase_igddetail_skala5.kode_skala5"
                                                )) {
                                                    ps3.setString(1, rs2.getString(1));
                                                    ps3.setString(2, lblNoRawatKompi1.getText());
                                                    try (ResultSet rs3 = ps3.executeQuery()) {
                                                        while (rs3.next()) {
                                                            detailTriase = rs3.getString(1) + ", " + detailTriase;
                                                        }
                                                    }
                                                }
                                                detailTriase = detailTriase.substring(0, detailTriase.length() - 2);
                                                Sequel.temporary(String.valueOf(++i), rs2.getString("nama_pemeriksaan"), detailTriase);
                                            }
                                        }
                                    }
                                    exportPDF("rptLembarTriaseSkala5.jasper", urutan + "_TriaseSkala5", param, "select * from temporary where temp37 = ?", akses.getalamatip());
                                }
                            }
                        }
                    } else {
                        exportSukses = false;
                    }
                }
            }
        } catch (Exception e) {
            exportSukses = false;
            System.out.println("Notif : " + e);
        }
    }
    
    private void exportSOAP(String urutan) {
        if (Sequel.cariExistsSmc("select * from reg_periksa where no_rawat = ? and (kd_poli = 'IGDK' or status_lanjut = 'Ranap')", lblNoRawatKompi1.getText())) {
            return;
        }
        
        try {
            StringBuilder htmlContent = new StringBuilder();
            htmlContent
                .append("<html>")
                .append("<head>")
                .append("<style type=\"text/css\">")
                .append(".isi td{border-right: 1px solid #e2e7dd;border-bottom: 1px solid #e2e7dd;font-family: Tahoma;font-size: 8.5px;height: 12px;background-color: #ffffff;color: #323232} .isi a{text-decoration: none;color: #8b9b95;padding: 0 0 0 0px;font-family: Tahoma;font-size: 8.5px;border-color: white}")
                .append("</style>")
                .append("</head>")
                .append("<body>");

            try (PreparedStatement ps = koneksi.prepareStatement(
                "select pasien.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tmp_lahir, pasien.tgl_lahir, pasien.agama, bahasa_pasien.nama_bahasa, cacat_fisik.nama_cacat, pasien.gol_darah, " +
                "pasien.nm_ibu, pasien.stts_nikah, pasien.pnd, concat_ws(', ', pasien.alamat, kelurahan.nm_kel, kecamatan.nm_kec, kabupaten.nm_kab) as alamat, pasien.pekerjaan from pasien " +
                "join bahasa_pasien on bahasa_pasien.id = pasien.bahasa_pasien join cacat_fisik on cacat_fisik.id = pasien.cacat_fisik join kelurahan on pasien.kd_kel = kelurahan.kd_kel " +
                "join kecamatan on pasien.kd_kec = kecamatan.kd_kec join kabupaten on pasien.kd_kab = kabupaten.kd_kab where pasien.no_rkm_medis = ?"
            )) {
                ps.setString(1, lblNoRMKompi1.getText());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        htmlContent
                            .append("<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"3px\" cellspacing=\"0\" class=\"tbl_form\">")
                            .append("<tbody>")
                            .append("<tr class=\"isi\">")
                            .append("<td valign=\"top\" width=\"20%\">No.RM</td>")
                            .append("<td valign=\"top\" width=\"1%\" align=\"center\">:</td>")
                            .append("<td valign=\"top\" width=\"79%\">" + rs.getString("no_rkm_medis") + "</td>")
                            .append("</tr>")
                            .append("<tr class=\"isi\">")
                            .append("<td valign=\"top\" width=\"20%\">Nama Pasien</td>")
                            .append("<td valign=\"top\" width=\"1%\" align=\"center\">:</td>")
                            .append("<td valign=\"top\" width=\"79%\">" + rs.getString("nm_pasien") + "</td>")
                            .append("</tr>")
                            .append("<tr class=\"isi\">")
                            .append("<td valign=\"top\" width=\"20%\">Alamat</td>")
                            .append("<td valign=\"top\" width=\"1%\" align=\"center\">:</td>")
                            .append("<td valign=\"top\" width=\"79%\">" + rs.getString("alamat") + "</td>")
                            .append("</tr>")
                            .append("<tr class=\"isi\">")
                            .append("<td valign=\"top\" width=\"20%\">Jenis Kelamin</td>")
                            .append("<td valign=\"top\" width=\"1%\" align=\"center\">:</td>")
                            .append("<td valign=\"top\" width=\"79%\">" + rs.getString("jk").replaceAll("L", "Laki-Laki").replaceAll("P", "Perempuan") + "</td>")
                            .append("</tr>")
                            .append("<tr class=\"isi\">")
                            .append("<td valign=\"top\" width=\"20%\">Tempat &amp; Tanggal Lahir</td>")
                            .append("<td valign=\"top\" width=\"1%\" align=\"center\">:</td>")
                            .append("<td valign=\"top\" width=\"79%\">" + rs.getString("tmp_lahir") + ", " + new SimpleDateFormat("dd MMMM yyyy", new Locale("id")).format((Date) rs.getDate("tgl_lahir")) + "</td>")
                            .append("</tr>")
                            .append("<tr class=\"isi\">")
                            .append("<td valign=\"top\" width=\"20%\">Ibu Kandung</td>")
                            .append("<td valign=\"top\" width=\"1%\" align=\"center\">:</td>")
                            .append("<td valign=\"top\" width=\"79%\">" + rs.getString("nm_ibu") + "</td>")
                            .append("</tr>")
                            .append("<tr class=\"isi\">")
                            .append("<td valign=\"top\" width=\"20%\">Golongan Darah</td>")
                            .append("<td valign=\"top\" width=\"1%\" align=\"center\">:</td>")
                            .append("<td valign=\"top\" width=\"79%\">" + rs.getString("gol_darah") + "</td>")
                            .append("</tr>")
                            .append("<tr class=\"isi\">")
                            .append("<td valign=\"top\" width=\"20%\">Status Nikah</td>")
                            .append("<td valign=\"top\" width=\"1%\" align=\"center\">:</td>")
                            .append("<td valign=\"top\" width=\"79%\">" + rs.getString("stts_nikah") + "</td>")
                            .append("</tr>")
                            .append("<tr class=\"isi\">")
                            .append("<td valign=\"top\" width=\"20%\">Agama</td>")
                            .append("<td valign=\"top\" width=\"1%\" align=\"center\">:</td>")
                            .append("<td valign=\"top\" width=\"79%\">" + rs.getString("agama") + "</td>")
                            .append("</tr>")
                            .append("<tr class=\"isi\">")
                            .append("<td valign=\"top\" width=\"20%\">Pendidikan Terakhir</td>")
                            .append("<td valign=\"top\" width=\"1%\" align=\"center\">:</td>")
                            .append("<td valign=\"top\" width=\"79%\">" + rs.getString("pnd") + "</td>")
                            .append("</tr>")
                            .append("<tr class=\"isi\">")
                            .append("<td valign=\"top\" width=\"20%\">Bahasa Dipakai</td>")
                            .append("<td valign=\"top\" width=\"1%\" align=\"center\">:</td>")
                            .append("<td valign=\"top\" width=\"79%\">" + rs.getString("nama_bahasa") + "</td>")
                            .append("</tr>")
                            .append("<tr class=\"isi\">")
                            .append("<td valign=\"top\" width=\"20%\">Cacat Fisik</td>")
                            .append("<td valign=\"top\" width=\"1%\" align=\"center\">:</td>")
                            .append("<td valign=\"top\" width=\"79%\">" + rs.getString("nama_cacat") + "</td>")
                            .append("</tr>")
                            .append("</tbody>")
                            .append("</table>");
                    }
                }
            }

            htmlContent
                .append("<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"3px\" cellspacing=\"0\" class=\"tbl_form\">")
                .append("<tbody>")
                .append("<tr class=\"isi\">")
                .append("<td valign=\"middle\" bgcolor=\"#FFFAF8\" align=\"center\" width=\"5%\">Tgl. Reg</td>")
                .append("<td valign=\"middle\" bgcolor=\"#FFFAF8\" align=\"center\" width=\"8%\">No. Rawat</td>")
                .append("<td valign=\"middle\" bgcolor=\"#FFFAF8\" align=\"center\" width=\"3%\">Status</td>")
                .append("<td valign=\"middle\" bgcolor=\"#FFFAF8\" align=\"center\" width=\"84%\">S.O.A.P.I.E</td>")
                .append("</tr>")
                .append("<tr class=\"isi\">")
                .append("<td valign=\"top\" align=\"center\">" + Sequel.cariIsiSmc("select tgl_registrasi from reg_periksa where no_rawat = ?", lblNoRawatKompi1.getText()) + "</td>")
                .append("<td valign=\"top\" align=\"center\">" + lblNoRawatKompi1.getText() + "</td>")
                .append("<td valign=\"top\" align=\"center\">Ralan</td>")
                .append("<td valign=\"top\" align=\"center\">")
                .append("<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"2px\" cellspacing=\"0\">")
                .append("<tbody>")
                .append("<tr class=\"isi\">")
                .append("<td valign=\"middle\" bgcolor=\"#FFFFF8\" align=\"center\" width=\"7%\">Tanggal</td>")
                .append("<td valign=\"middle\" bgcolor=\"#FFFFF8\" align=\"center\" width=\"13%\">Dokter/Paramedis</td>")
                .append("<td valign=\"middle\" bgcolor=\"#FFFFF8\" align=\"center\" width=\"14%\">Subjek</td>")
                .append("<td valign=\"middle\" bgcolor=\"#FFFFF8\" align=\"center\" width=\"13%\">Objek</td>")
                .append("<td valign=\"middle\" bgcolor=\"#FFFFF8\" align=\"center\" width=\"13%\">Asesmen</td>")
                .append("<td valign=\"middle\" bgcolor=\"#FFFFF8\" align=\"center\" width=\"14%\">Plan</td>")
                .append("<td valign=\"middle\" bgcolor=\"#FFFFF8\" align=\"center\" width=\"14%\">Instruksi</td>")
                .append("<td valign=\"middle\" bgcolor=\"#FFFFF8\" align=\"center\" width=\"14%\">Evaluasi</td>")
                .append("</tr>");

            try (PreparedStatement ps = koneksi.prepareStatement(
                "select pemeriksaan_ralan.*, pegawai.nama, pegawai.jbtn from pemeriksaan_ralan join pegawai on pemeriksaan_ralan.nip = pegawai.nik " +
                "where pemeriksaan_ralan.no_rawat = ? order by concat(pemeriksaan_ralan.tgl_perawatan, ' ', pemeriksaan_ralan.jam_rawat) desc"
            )) {
                ps.setString(1, lblNoRawatKompi1.getText());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        htmlContent
                            .append("<tr class=\"isi\">")
                            .append("<td align=\"center\">" + rs.getString("tgl_perawatan") + "<br>" + rs.getString("jam_rawat") + "</td>")
                            .append("<td align=\"center\">" + rs.getString("nip") + "<br>" + rs.getString("nama") + "</td>")
                            .append("<td align=\"left\">" + HtmlUtils.htmlEscape(rs.getString("keluhan")).replaceAll("\\R", "<br>") + "</td>")
                            .append("<td align=\"left\">" + HtmlUtils.htmlEscape(rs.getString("pemeriksaan")).replaceAll("\\R", "<br>"))
                            .append((rs.getString("alergi") == null || rs.getString("alergi").equals("") ? "" : "<br>Alergi : " + rs.getString("alergi")))
                            .append((rs.getString("suhu_tubuh") == null || rs.getString("suhu_tubuh").equals("") ? "" : "<br>Suhu(C) : " + rs.getString("suhu_tubuh")))
                            .append((rs.getString("tensi") == null || rs.getString("tensi").equals("") ? "" : "<br>Tensi : " + rs.getString("tensi")))
                            .append((rs.getString("nadi") == null || rs.getString("nadi").equals("") ? "" : "<br>Nadi(/menit) : " + rs.getString("nadi")))
                            .append((rs.getString("respirasi") == null || rs.getString("respirasi").equals("") ? "" : "<br>Respirasi(/menit) : " + rs.getString("respirasi")))
                            .append((rs.getString("tinggi") == null || rs.getString("tinggi").equals("") ? "" : "<br>Tinggi(Cm) : " + rs.getString("tinggi")))
                            .append((rs.getString("berat") == null || rs.getString("berat").equals("") ? "" : "<br>Berat(Kg) : " + rs.getString("berat")))
                            .append((rs.getString("lingkar_perut") == null || rs.getString("lingkar_perut").equals("") ? "" : "<br>Lingkar Perut(Cm) : " + rs.getString("lingkar_perut")))
                            .append((rs.getString("spo2") == null || rs.getString("spo2").equals("") ? "" : "<br>SpO2(%) : " + rs.getString("spo2")))
                            .append((rs.getString("gcs") == null || rs.getString("gcs").equals("") ? "" : "<br>GCS(E,V,M) : " + rs.getString("gcs")))
                            .append((rs.getString("kesadaran") == null || rs.getString("kesadaran").equals("") ? "" : "<br>Kesadaran : " + rs.getString("kesadaran")))
                            .append("</td>")
                            .append("<td align=\"left\">" + HtmlUtils.htmlEscape(rs.getString("penilaian")).replaceAll("\\R", "<br>") + "</td>")
                            .append("<td align=\"left\">" + HtmlUtils.htmlEscape(rs.getString("rtl")).replaceAll("\\R", "<br>") + "</td>")
                            .append("<td align=\"left\">" + HtmlUtils.htmlEscape(rs.getString("instruksi")).replaceAll("\\R", "<br>") + "</td>")
                            .append("<td align=\"left\">" + HtmlUtils.htmlEscape(rs.getString("evaluasi")).replaceAll("\\R", "<br>") + "</td>")
                            .append("</tr>");
                    }
                }
            }
            
            GetMethod get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + Sequel.cariIsiSmc("select reg_periksa.kd_dokter from reg_periksa where reg_periksa.no_rawat = ?", lblNoRawatKompi1.getText()).replace(" ", "_"));
            HttpClient http = new HttpClient();
            http.executeMethod(get);

            htmlContent
                .append("</tbody>")
                .append("</table>")
                .append("</td>")
                .append("</tr>")
                .append("<tr class=\"isi\">")
                .append("<td valign=\"top\" width=\"2%\"></td>")
                .append("<td valign=\"middle\" width=\"18%\"> Tanda Tangan/Verifikasi </td>")
                .append("<td valign=\"middle\" width=\"1%\" align=\"center\"> : </td>")
                .append("<td valign=\"middle\" width=\"79%\" align=\"center\">")
                .append("Dokter Poli")
                .append("<br><img width=\"90\" height=\"90\" src=\"" + "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/temp/" + Sequel.cariIsiSmc("select reg_periksa.kd_dokter from reg_periksa where reg_periksa.no_rawat = ?", lblNoRawatKompi1.getText()).replace(" ", "_") + ".png\"><br>")
                .append(Sequel.cariIsiSmc("select dokter.nm_dokter from reg_periksa join dokter on reg_periksa.kd_dokter = dokter.kd_dokter where reg_periksa.no_rawat = ?", lblNoRawatKompi1.getText()))
                .append("</tr>")
                .append("<tr class=\"isi\"><td></td><td colspan=\"3\" align=\"right\">&#160;</td></tr>")
                .append("</body>")
                .append("</html>");

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("soap_ralan.html")))) {
                String html = htmlContent.toString().replaceAll(getClass().getResource("/picture/").toString(), "./gambar/");
                bw.write(html);
            }

            try (FileOutputStream os = new FileOutputStream("./berkaspdf/" + tanggalExport + "/" + lblNoSEPKompi1.getText() + "_" + urutan + "_SOAP.pdf")) {
                org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(new File("soap_ralan.html"));
                org.w3c.dom.Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.withW3cDocument(w3cDoc, null);
                builder.toStream(os);
                builder.run();
            }
        } catch (Exception e) {
            exportSukses = false;
            System.out.println("Notif : " + e);
        }
    }

    private void exportAwalMedisIGD(String urutan) {
        if (Sequel.cariExistsSmc("select * from reg_periksa where no_rawat = ? and kd_poli != 'IGDK'", lblNoRawatKompi1.getText())) return;
        
        if (!btnAwalMedisIGDKompi1.isEnabled()) return;
        
        String kodeDokter = Sequel.cariIsiSmc("select kd_dokter from penilaian_medis_igd where no_rawat = ?", lblNoRawatKompi1.getText());
        String namaDokter = Sequel.cariIsiSmc("select nm_dokter from dokter where kd_dokter = ?", kodeDokter);
        String tgl = Sequel.cariIsiSmc("select date_format(tanggal, '%d-%m-%Y') from penilaian_medis_igd where no_rawat = ?", lblNoRawatKompi1.getText());
        Map<String, Object> param = new HashMap<>();
        param.put("namars", akses.getnamars());
        param.put("alamatrs", akses.getalamatrs());
        param.put("kotars", akses.getkabupatenrs());
        param.put("propinsirs", akses.getpropinsirs());
        param.put("kontakrs", akses.getkontakrs());
        param.put("emailrs", akses.getemailrs());
        param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
        try {
            param.put("lokalis", getClass().getResource("/picture/semua.png").openStream());
        } catch (Exception e) {
        }
        finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", kodeDokter);
        param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + namaDokter + "\nID " + (finger.equals("") ? kodeDokter : finger) + "\n" + tgl);
        exportPDF("rptCetakPenilaianAwalMedisIGD.jasper", urutan + "_AwalMedisIGD", param,
            "select reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, if (pasien.jk = 'L', 'Laki-Laki', 'Perempuan') as jk, pasien.tgl_lahir, penilaian_medis_igd.tanggal, penilaian_medis_igd.kd_dokter, "
            + "penilaian_medis_igd.anamnesis, penilaian_medis_igd.hubungan, concat_ws(', ', penilaian_medis_igd.anamnesis, nullif(penilaian_medis_igd.hubungan, '')) as hubungan_anemnesis, penilaian_medis_igd.keluhan_utama, "
            + "penilaian_medis_igd.rps, penilaian_medis_igd.rpk, penilaian_medis_igd.rpd, penilaian_medis_igd.rpo, penilaian_medis_igd.alergi, penilaian_medis_igd.keadaan, penilaian_medis_igd.gcs, penilaian_medis_igd.kesadaran, "
            + "penilaian_medis_igd.td, penilaian_medis_igd.nadi, penilaian_medis_igd.rr, penilaian_medis_igd.suhu, penilaian_medis_igd.spo, penilaian_medis_igd.bb, penilaian_medis_igd.tb, penilaian_medis_igd.kepala, penilaian_medis_igd.mata, "
            + "penilaian_medis_igd.gigi, penilaian_medis_igd.leher, penilaian_medis_igd.thoraks, penilaian_medis_igd.abdomen, penilaian_medis_igd.ekstremitas, penilaian_medis_igd.genital, penilaian_medis_igd.ket_fisik, penilaian_medis_igd.ket_lokalis, "
            + "penilaian_medis_igd.ekg, penilaian_medis_igd.rad, penilaian_medis_igd.lab, penilaian_medis_igd.diagnosis, penilaian_medis_igd.tata, dokter.nm_dokter from reg_periksa join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
            + "join penilaian_medis_igd on reg_periksa.no_rawat = penilaian_medis_igd.no_rawat join dokter on penilaian_medis_igd.kd_dokter = dokter.kd_dokter where penilaian_medis_igd.no_rawat = ?", lblNoRawatKompi1.getText()
        );
    }

    private void exportHasilLab(String urutan) {
        if (!btnHasilLabKompi1.isEnabled()) return;
        String kamar = "", namaKamar = "";
        int i = 0;
        Map<String, Object> param = new HashMap<>();
        try {
            try (PreparedStatement ps = koneksi.prepareStatement(
                "select pasien.jk, pasien.umur, pasien.tgl_lahir, concat_ws(', ', pasien.alamat, kelurahan.nm_kel, kecamatan.nm_kec, kabupaten.nm_kab) as alamat, " +
                "pasien.pekerjaan, pasien.no_ktp from reg_periksa join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join kelurahan on pasien.kd_kel = kelurahan.kd_kel " +
                "join kecamatan on pasien.kd_kec = kecamatan.kd_kec join kabupaten on pasien.kd_kab = kabupaten.kd_kab join propinsi on pasien.kd_prop = propinsi.kd_prop where reg_periksa.no_rawat = ?"
            )) {
                ps.setString(1, lblNoRawatKompi1.getText());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        param.put("noperiksa", lblNoRawatKompi1.getText());
                        param.put("norm", lblNoRMKompi1.getText());
                        param.put("namapasien", lblNamaPasienKompi1.getText());
                        param.put("jkel", rs.getString("jk"));
                        param.put("umur", rs.getString("umur"));
                        param.put("lahir", rs.getString("tgl_lahir"));
                        param.put("alamat", rs.getString("alamat"));
                        param.put("diagnosa", tbKompilasiKompi1.getValueAt(tbKompilasiKompi1.getSelectedRow(), 9).toString());
                        param.put("pekerjaan", rs.getString("pekerjaan"));
                        param.put("noktp", rs.getString("no_ktp"));
                        param.put("namars", akses.getnamars());
                        param.put("alamatrs", akses.getalamatrs());
                        param.put("kotars", akses.getkabupatenrs());
                        param.put("propinsirs", akses.getpropinsirs());
                        param.put("kontakrs", akses.getkontakrs());
                        param.put("emailrs", akses.getemailrs());
                        param.put("userid", akses.getkode());
                        param.put("ipaddress", akses.getalamatip());
                    }
                }
            }
            
            try (PreparedStatement ps = koneksi.prepareStatement(
                "select periksa_lab.no_rawat, periksa_lab.tgl_periksa, periksa_lab.jam, periksa_lab.status, periksa_lab.kategori, periksa_lab.kd_dokter, " +
                "dokter.nm_dokter, periksa_lab.dokter_perujuk, perujuk.nm_dokter nm_perujuk, periksa_lab.nip, petugas.nama from periksa_lab join dokter " +
                "on periksa_lab.kd_dokter = dokter.kd_dokter join dokter perujuk on periksa_lab.dokter_perujuk = perujuk.kd_dokter join petugas on " +
                "periksa_lab.nip = petugas.nip where periksa_lab.no_rawat = ? group by periksa_lab.no_rawat, periksa_lab.tgl_periksa, periksa_lab.jam, " +
                "periksa_lab.status, periksa_lab.kategori"
            )) {
                ps.setString(1, lblNoRawatKompi1.getText());
                try (ResultSet rs = ps.executeQuery()) {
                    for (int j = 1; rs.next(); j++) {
                        Sequel.deleteTemporaryLab();
                        i = 0;
                        if (rs.getString("status").equalsIgnoreCase("ralan")) {
                            kamar = "Poli";
                            namaKamar = Sequel.cariIsiSmc("select poliklinik.nm_poli from poliklinik join reg_periksa on poliklinik.kd_poli = reg_periksa.kd_poli where reg_periksa.no_rawat = ?", lblNoRawatKompi1.getText());
                        } else {
                            kamar = "Kamar";
                            namaKamar = tbKompilasiKompi1.getValueAt(tbKompilasiKompi1.getSelectedRow(), 8).toString();
                        }
                        param.put("kamar", kamar);
                        param.put("namakamar", namaKamar);
                        param.put("pengirim", rs.getString("nm_perujuk"));
                        param.put("tanggal", rs.getString("tgl_periksa"));
                        param.put("jam", rs.getString("jam"));
                        param.put("penjab", rs.getString("nm_dokter"));
                        param.put("petugas", rs.getString("nama"));
                        param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
                        finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs.getString("kd_dokter"));
                        param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs.getString("nm_dokter") + "\nID " + (finger.equals("") ? rs.getString("kd_dokter") : finger) + "\n" + rs.getString("tgl_periksa"));
                        finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs.getString("nip"));
                        param.put("finger2", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs.getString("nama") + "\nID " + (finger.equals("") ? rs.getString("nip") : finger) + "\n" + rs.getString("tgl_periksa"));
                        param.put("ttd", Sequel.cariGambarSmc("select dokter_ttdbasah.gambar_ttd from dokter_ttdbasah where dokter_ttdbasah.kd_dokter = ?", rs.getString("kd_dokter")));
                        if (rs.getString("kategori").equals("PK")) {
                            try (PreparedStatement ps2 = koneksi.prepareStatement(
                                "select periksa_lab.kd_jenis_prw, jns_perawatan_lab.nm_perawatan from periksa_lab join jns_perawatan_lab " +
                                "on periksa_lab.kd_jenis_prw = jns_perawatan_lab.kd_jenis_prw where periksa_lab.no_rawat = ? " +
                                "and periksa_lab.tgl_periksa = ? and periksa_lab.jam = ? and periksa_lab.status = ? and periksa_lab.kategori = ?"
                            )) {
                                ps2.setString(1, lblNoRawatKompi1.getText());
                                ps2.setString(2, rs.getString("tgl_periksa"));
                                ps2.setString(3, rs.getString("jam"));
                                ps2.setString(4, rs.getString("status"));
                                ps2.setString(5, rs.getString("kategori"));
                                try (ResultSet rs2 = ps2.executeQuery()) {
                                    while (rs2.next()) {
                                        Sequel.temporaryLab(String.valueOf(++i), rs2.getString("nm_perawatan"));
                                        try (PreparedStatement ps3 = koneksi.prepareStatement(
                                            "select template_laboratorium.Pemeriksaan, detail_periksa_lab.nilai, template_laboratorium.satuan, " +
                                            "detail_periksa_lab.nilai_rujukan, detail_periksa_lab.biaya_item, detail_periksa_lab.keterangan, " +
                                            "detail_periksa_lab.kd_jenis_prw from detail_periksa_lab join template_laboratorium on " +
                                            "detail_periksa_lab.id_template = template_laboratorium.id_template where detail_periksa_lab.no_rawat = ? " +
                                            "and detail_periksa_lab.kd_jenis_prw = ? and detail_periksa_lab.tgl_periksa = ? and detail_periksa_lab.jam = ? " +
                                            "order by template_laboratorium.urut"
                                        )) {
                                            ps3.setString(1, lblNoRawatKompi1.getText());
                                            ps3.setString(2, rs2.getString("kd_jenis_prw"));
                                            ps3.setString(3, rs.getString("tgl_periksa"));
                                            ps3.setString(4, rs.getString("jam"));
                                            try (ResultSet rs3 = ps3.executeQuery()) {
                                                while (rs3.next()) {
                                                    Sequel.temporaryLab(
                                                        String.valueOf(++i), "  " + rs3.getString("Pemeriksaan"), rs3.getString("nilai"),
                                                        rs3.getString("satuan"), rs3.getString("nilai_rujukan"), rs3.getString("keterangan")
                                                    );
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            try (PreparedStatement ps2 = koneksi.prepareStatement(
                                "select noorder, tgl_permintaan, jam_permintaan " +
                                "from permintaan_lab where no_rawat = ? and tgl_hasil = ? and jam_hasil = ?"
                            )) {
                                ps2.setString(1, lblNoRawatKompi1.getText());
                                ps2.setString(2, rs.getString("tgl_periksa"));
                                ps2.setString(3, rs.getString("jam"));
                                try (ResultSet rs2 = ps2.executeQuery()) {
                                    if (rs2.next()) {
                                        param.put("nopermintaan", rs2.getString("noorder"));
                                        param.put("tanggalpermintaan", rs2.getString("tgl_permintaan"));
                                        param.put("jampermintaan", rs2.getString("jam_permintaan"));
                                        exportPDF("rptPeriksaLab4PermintaanKompilasi.jasper", urutan + "_HasilLab" + String.valueOf(j), param);
                                    } else {
                                        exportPDF("rptPeriksaLab4Kompilasi.jasper", urutan + "_HasilLab" + String.valueOf(j), param);
                                    }
                                }
                            }
                        } else if (rs.getString("kategori").equals("PA")) {
                            try (PreparedStatement ps2 = koneksi.prepareStatement(
                                "select jns_perawatan_lab.nm_perawatan, detail_periksa_labpa.diagnosa_klinik, detail_periksa_labpa.makroskopik, detail_periksa_labpa.mikroskopik, detail_periksa_labpa.kesimpulan, detail_periksa_labpa.kesan " +
                                "from detail_periksa_labpa join jns_perawatan_lab on detail_periksa_labpa.kd_jenis_prw = jns_perawatan_lab.kd_jenis_prw where no_rawat = ? and tgl_periksa = ? and jam = ?"
                            )) {
                                ps2.setString(1, lblNoRawatKompi1.getText());
                                ps2.setString(2, rs.getString("tgl_periksa"));
                                ps2.setString(3, rs.getString("jam"));
                                try (ResultSet rs2 = ps2.executeQuery()) {
                                    while (rs2.next()) {
                                        Sequel.temporaryLab(String.valueOf(++i), rs2.getString("nm_perawatan"), rs2.getString(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5));
                                    }
                                }
                            }
                            try (PreparedStatement ps2 = koneksi.prepareStatement(
                                "select noorder, tgl_permintaan, jam_permintaan " +
                                "from permintaan_labpa where no_rawat = ? and tgl_hasil = ? and jam_hasil = ?"
                            )) {
                                ps2.setString(1, lblNoRawatKompi1.getText());
                                ps2.setString(2, rs.getString("tgl_periksa"));
                                ps2.setString(3, rs.getString("jam"));
                                try (ResultSet rs2 = ps2.executeQuery()) {
                                    param.put("ttd", Sequel.cariGambarSmc("select dokter_ttdbasah.gambar_ttd from dokter_ttdbasah where dokter_ttdbasah.kd_dokter = ?", rs.getString("kd_dokter")));
                                    if (rs2.next()) {
                                        param.put("nopermintaan", rs2.getString("noorder"));
                                        param.put("tanggalpermintaan", rs2.getString("tgl_permintaan"));
                                        param.put("jampermintaan", rs2.getString("jam_permintaan"));
                                        exportPDF("rptPeriksaLabPermintaanPAKompilasi.jasper", urutan + "_HasilLab" + String.valueOf(j), param);
                                    } else {
                                        exportPDF("rptPeriksaLabPAKompilasi.jasper", urutan + "_HasilLab" + String.valueOf(j), param);
                                    }
                                }
                            }
                        } else if (rs.getString("kategori").equals("MB")) {
                            System.out.println("Maaf, bagian ini belum disupport..!!\nSilahkan hubungi administrator");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada saat mencari hasil pemeriksaan lab!");
        }
    }

    private void exportHasilRadiologi(String urutan) {
        if (!btnHasilRadKompi1.isEnabled()) {
            return;
        }
        int j = 1;
        try (PreparedStatement ps = koneksi.prepareStatement(
            "select pasien.jk, date_format(pasien.tgl_lahir, '%d-%m-%Y') as tgllahir, concat(reg_periksa.umurdaftar, ' ', reg_periksa.sttsumur) as umur, concat_ws(', ', pasien.alamat, kelurahan.nm_kel, kecamatan.nm_kec, kabupaten.nm_kab) as alamat, periksa_radiologi.dokter_perujuk, "
            + "dokter_perujuk.nm_dokter nm_dokter_perujuk, periksa_radiologi.tgl_periksa, periksa_radiologi.jam, periksa_radiologi.kd_dokter, dokter.nm_dokter, periksa_radiologi.nip, petugas.nama nama_petugas, jns_perawatan_radiologi.nm_perawatan, "
            + "periksa_radiologi.status, periksa_radiologi.proyeksi, periksa_radiologi.kV, periksa_radiologi.mAS, periksa_radiologi.FFD, periksa_radiologi.BSF, periksa_radiologi.inak, periksa_radiologi.jml_penyinaran, periksa_radiologi.dosis "
            + "from periksa_radiologi join reg_periksa on periksa_radiologi.no_rawat = reg_periksa.no_rawat join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis join dokter dokter_perujuk on periksa_radiologi.dokter_perujuk = dokter_perujuk.kd_dokter "
            + "join dokter on periksa_radiologi.kd_dokter = dokter.kd_dokter join petugas on periksa_radiologi.nip = petugas.nip join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw = jns_perawatan_radiologi.kd_jenis_prw "
            + "left join kelurahan on pasien.kd_kel = kelurahan.kd_kel left join kecamatan on pasien.kd_kec = kecamatan.kd_kec left join kabupaten on pasien.kd_kab = kabupaten.kd_kab where periksa_radiologi.no_rawat = ?"
        )) {
            ps.setString(1, lblNoRawatKompi1.getText());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String pemeriksaan = rs.getString("nm_perawatan")
                        + (rs.getString("proyeksi") == null || rs.getString("proyeksi").equals("") ? "" : " dengan Proyeksi : " + rs.getString("proyeksi"))
                        + (rs.getString("kV") == null || rs.getString("kV").equals("") ? "" : ", kV : " + rs.getString("kV"))
                        + (rs.getString("mAS") == null || rs.getString("mAS").equals("") ? "" : ", mAS : " + rs.getString("mAS"))
                        + (rs.getString("FFD") == null || rs.getString("FFD").equals("") ? "" : ", FFD : " + rs.getString("FFD"))
                        + (rs.getString("BSF") == null || rs.getString("BSF").equals("") ? "" : ", BSF : " + rs.getString("BSF"))
                        + (rs.getString("Inak") == null || rs.getString("Inak").equals("") ? "" : ", Inak : " + rs.getString("Inak"))
                        + (rs.getString("jml_penyinaran") == null || rs.getString("jml_penyinaran").equals("") ? "" : ", Jumlah penyinaran : " + rs.getString("jml_penyinaran"))
                        + (rs.getString("dosis") == null || rs.getString("dosis").equals("") ? "" : ", Dosis Radiasi : " + rs.getString("dosis"));
                    Map<String, Object> param = new HashMap<>();
                    param.put("noperiksa", lblNoRawatKompi1.getText());
                    param.put("norm", lblNoRMKompi1.getText());
                    param.put("namapasien", lblNamaPasienKompi1.getText());
                    param.put("jkel", rs.getString("jk"));
                    param.put("umur", rs.getString("umur"));
                    param.put("lahir", rs.getString("tgllahir"));
                    param.put("pengirim", rs.getString("nm_dokter_perujuk"));
                    param.put("tanggal", rs.getString("tgl_periksa"));
                    param.put("penjab", rs.getString("nm_dokter"));
                    param.put("petugas", rs.getString("nama_petugas"));
                    param.put("alamat", rs.getString("alamat"));
                    String kamar = "", kelas = "", namaKamar = "", noRawatIbu = "";
                    if (lblStatusRawatKompi1.getText().contains("Ranap")) {
                        noRawatIbu = Sequel.cariIsiSmc("select no_rawat from ranap_gabung where no_rawat2 = ?", lblNoRawatKompi1.getText());
                        if (!noRawatIbu.equals("")) {
                            kamar = Sequel.cariIsiSmc("select ifnull(kd_kamar, '') from kamar_inap where no_rawat = ? order by tgl_masuk desc limit 1", noRawatIbu);
                            kelas = Sequel.cariIsiSmc("select kamar.kelas from kamar inner join kamar_inap on kamar.kd_kamar = kamar_inap.kd_kamar where no_rawat = ? order by str_to_date(concat(kamar_inap.tgl_masuk, ' ', kamar_inap.jam_masuk), '%Y-%m-%d %H:%i:%s') desc limit 1", noRawatIbu);
                        } else {
                            kamar = Sequel.cariIsiSmc("select ifnull(kd_kamar, '') from kamar_inap where no_rawat = ? order by tgl_masuk desc limit 1", lblNoRawatKompi1.getText());
                            kelas = Sequel.cariIsiSmc("select kamar.kelas from kamar inner join kamar_inap on kamar.kd_kamar = kamar_inap.kd_kamar where no_rawat = ? order by str_to_date(concat(kamar_inap.tgl_masuk, ' ', kamar_inap.jam_masuk), '%Y-%m-%d %H:%i:%s') desc limit 1", lblNoRawatKompi1.getText());
                        }
                        namaKamar = kamar + ", " + Sequel.cariIsiSmc("select bangsal.nm_bangsal from bangsal inner join kamar on bangsal.kd_bangsal = kamar.kd_bangsal where kamar.kd_kamar = ?", kamar);
                        kamar = "Kamar";
                    } else {
                        kelas = "Rawat Jalan";
                        kamar = "Poli";
                        namaKamar = Sequel.cariIsiSmc("select poliklinik.nm_poli from poliklinik inner join reg_periksa on poliklinik.kd_poli = reg_periksa.kd_poli where reg_periksa.no_rawat = ?", lblNoRawatKompi1.getText());
                    }
                    param.put("kamar", kamar);
                    param.put("namakamar", namaKamar);
                    param.put("pemeriksaan", pemeriksaan);
                    param.put("jam", rs.getString("jam"));
                    param.put("namars", akses.getnamars());
                    param.put("alamatrs", akses.getalamatrs());
                    param.put("kotars", akses.getkabupatenrs());
                    param.put("propinsirs", akses.getpropinsirs());
                    param.put("kontakrs", akses.getkontakrs());
                    param.put("emailrs", akses.getemailrs());
                    param.put("hasil", Sequel.cariIsiSmc("select hasil from hasil_radiologi where no_rawat = ? and tgl_periksa = ? and jam = ?", lblNoRawatKompi1.getText(), rs.getString("tgl_periksa"), rs.getString("jam")));
                    param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
                    finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs.getString("kd_dokter"));
                    param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs.getString("nm_dokter") + "\nID " + (finger.equals("") ? rs.getString("kd_dokter") : finger) + "\n" + new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("tgl_periksa")));
                    finger = Sequel.cariIsiSmc("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id = sidikjari.id where pegawai.nik = ?", rs.getString("nip"));
                    param.put("finger2", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + rs.getString("nama_petugas") + "\nID " + (finger.equals("") ? rs.getString("nip") : finger) + "\n" + new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("tgl_periksa")));
                    param.put("ttd", Sequel.cariGambarSmc("select dokter_ttdbasah.gambar_ttd from dokter_ttdbasah where dokter_ttdbasah.kd_dokter = ?", rs.getString("kd_dokter")));
                    exportPDF("rptPeriksaRadiologiKompilasi.jasper", urutan + "_PeriksaRadiologi" + String.valueOf(j++), param);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }

    private void exportSKDP(String urutan) {
        if (!btnSurkonKompi1.isEnabled()) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("namars", akses.getnamars());
        param.put("alamatrs", akses.getalamatrs());
        param.put("kotars", akses.getkabupatenrs());
        param.put("propinsirs", akses.getpropinsirs());
        param.put("kontakrs", akses.getkontakrs());
        param.put("logo", Sequel.cariGambar("select gambar.bpjs from gambar"));
        String noSurat = Sequel.cariIsiSmc("select noskdp from bridging_sep where no_sep = ?", lblNoSEPKompi1.getText());
        String tglSurat = Sequel.cariIsiSmc("select date_format(tgl_surat, '%d-%m-%Y') from bridging_surat_kontrol_bpjs where no_surat = ?", noSurat);
        String kodeDokter = Sequel.cariIsiSmc("select kd_dokter from maping_dokter_dpjpvclaim where maping_dokter_dpjpvclaim.kd_dokter_bpjs = (select bridging_surat_kontrol_bpjs.kd_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat = ?)", noSurat);
        String namaDokter = Sequel.cariIsiSmc("select nm_dokter from dokter where kd_dokter = ?", kodeDokter);
        param.put("parameter", Sequel.cariIsiSmc("select noskdp from bridging_sep where no_sep = ?", lblNoSEPKompi1.getText()));
        param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + namaDokter + "\nID " + kodeDokter + "\n" + tglSurat);
        exportPDF("rptBridgingSuratKontrol2.jasper", urutan + "_SuratKontrol", param,
            "select bridging_sep.no_rawat, bridging_sep.no_sep, bridging_sep.no_kartu, bridging_sep.nomr, bridging_sep.nama_pasien, bridging_sep.tanggal_lahir, bridging_sep.jkel, bridging_sep.diagawal, bridging_sep.nmdiagnosaawal, bridging_surat_kontrol_bpjs.tgl_surat, "
            + "bridging_surat_kontrol_bpjs.no_surat, bridging_surat_kontrol_bpjs.tgl_rencana, bridging_surat_kontrol_bpjs.kd_dokter_bpjs, bridging_surat_kontrol_bpjs.nm_dokter_bpjs, bridging_surat_kontrol_bpjs.kd_poli_bpjs, bridging_surat_kontrol_bpjs.nm_poli_bpjs "
            + "from bridging_sep join bridging_surat_kontrol_bpjs on bridging_surat_kontrol_bpjs.no_sep = bridging_sep.no_sep where bridging_surat_kontrol_bpjs.no_surat = ?", Sequel.cariIsiSmc("select noskdp from bridging_sep where no_sep = ?", lblNoSEPKompi1.getText())
        );
    }

    private void exportSPRI(String urutan) {
        if (!btnSPRIKompi1.isEnabled()) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("namars", akses.getnamars());
        param.put("alamatrs", akses.getalamatrs());
        param.put("kotars", akses.getkabupatenrs());
        param.put("propinsirs", akses.getpropinsirs());
        param.put("kontakrs", akses.getkontakrs());
        param.put("logo", Sequel.cariGambar("select gambar.bpjs from gambar"));
        param.put("parameter", lblNoRawatKompi1.getText());
        String noSPRI = Sequel.cariIsiSmc("select no_surat from bridging_surat_pri_bpjs where no_rawat = ? order by no_surat desc", lblNoRawatKompi1.getText());
        String kodeDokter = Sequel.cariIsiSmc("Select kd_dokter_bpjs from bridging_surat_pri_bpjs where no_surat = ?", noSPRI);
        String namaDokter = Sequel.cariIsiSmc("select nm_dokter_bpjs from maping_dokter_dpjpvclaim where kd_dokter_bpjs = ?", kodeDokter);
        String tglSPRI = Sequel.cariIsiSmc("select date_format(tgl_rencana, '%d-%m-%Y') from bridging_surat_pri_bpjs where no_surat = ?", noSPRI);
        param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + namaDokter + "\nID " + kodeDokter + "\n" + tglSPRI);
        exportPDF("rptBridgingSuratPRI2.jasper", urutan + "_SPRI", param,
            "select bridging_surat_pri_bpjs.*, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, pasien.jk "
            + "from reg_periksa join bridging_surat_pri_bpjs on bridging_surat_pri_bpjs.no_rawat = reg_periksa.no_rawat "
            + "join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis where bridging_surat_pri_bpjs.no_surat = ?", noSPRI
        );
    }
    
    private void exportRiwayatPasien(String urutan) {
        try {
            if (resume == null) {
                resume = new RMRiwayatPerawatan(null, false);
            }
            resume.kompilasiDariRiwayat(lblNoRawatKompi1.getText(), lblNoRMKompi1.getText(), tanggalExport, lblNoSEPKompi1.getText(), urutan);
        } catch (Exception e) {
            exportSukses = false;
            System.out.println("Notif : " + e);
            cleanupSinglePDF(lblNoSEPKompi1.getText() + "_" + urutan + "_Riwayat");
        }
    }
    
    private void exportBerkasDigitalPerawatan(String urutan) {
        if (!Sequel.cariExistsSmc("select * from berkas_digital_perawatan where berkas_digital_perawatan.no_rawat = ?", lblNoRawatKompi1.getText())) return;
        
        String filename = "", exportPath = "";
        URL fileUrl;
        HttpURLConnection http;
        String url = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/berkasrawat/";
        try (PreparedStatement ps = koneksi.prepareStatement(
            "select berkas_digital_perawatan.lokasi_file, master_berkas_digital.nama from berkas_digital_perawatan join master_berkas_digital on berkas_digital_perawatan.kode = master_berkas_digital.kode " +
            "where berkas_digital_perawatan.no_rawat = ? and lokasi_file like '%.pdf' and master_berkas_digital.include_kompilasi_berkas = 1"
        )) {
            ps.setString(1, lblNoRawatKompi1.getText());
            try (ResultSet rs = ps.executeQuery()) {
                for (int i = 1; rs.next(); i++) {
                    filename = rs.getString("lokasi_file");
                    exportPath = "./berkaspdf/" + tanggalExport + "/" + lblNoSEPKompi1.getText() + "_" + urutan + "_BerkasDigital" + String.valueOf(i) + ".pdf";
                    if (filename.endsWith(".pdf")) {
                        try (FileOutputStream os = new FileOutputStream(exportPath); FileChannel fileChannel = os.getChannel()) {
                            fileUrl = new URL(url + rs.getString("lokasi_file"));
                            http = (HttpURLConnection) fileUrl.openConnection();
                            if (http.getResponseCode() == 200) {
                                fileChannel.transferFrom(Channels.newChannel(fileUrl.openStream()), 0, Long.MAX_VALUE);
                                http.disconnect();
                            } else {
                                fileChannel.close();
                                os.close();
                                cleanupSinglePDF(lblNoSEPKompi1.getText() + "_" + urutan + "_BerkasDigital" + String.valueOf(i));
                                System.out.println("File tidak ditemukan : " + url + rs.getString("lokasi_file"));
                                if (JOptionPane.showConfirmDialog(null, "Berkas " + rs.getString("nama") + " \"" + rs.getString("lokasi_file").substring(rs.getString("lokasi_file").lastIndexOf("/") + 1) + "\" tidak ditemukan, lewati?", "Lewati Berkas", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                                    throw new Exception("Terdapat berkas digital yang tidak bisa ditemukan..!!");
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            exportSukses = false;
            System.out.println("Notif : " + e);
            cleanupSinglePDF(lblNoSEPKompi1.getText() + "_" + urutan + "_BerkasDigital");
        }
    }

    private void gabung() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Properties systemProp = System.getProperties();
            String currentDir = systemProp.getProperty("user.dir");
            tanggalExport = lblTglSEPKompi1.getText();
            String bulanExport=lblTglSEP1Kompi1.getText(),tahunExport=lblTglSEPKompi1.getText().substring(0,4);
            File dir = new File("./hasilgabungkompilasiklaim/" +tahunExport+"/"+bulanExport+"/"+ tanggalExport);
            if (!dir.isDirectory() && !dir.mkdirs()) {
                try {
                    Files.createDirectory(dir.toPath());
                } catch (IOException ex) {
                    Logger.getLogger(BPJSKompilasiBerkasKlaim2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String folderPath = currentDir + "/hasilkompilasiklaim/"; // Replace this with the actual folder path
            String outputPath = currentDir + "/hasilgabungkompilasiklaim/" +tahunExport+"/"+bulanExport+"/"+ tanggalExport + "/" + lblNoSEPKompi1.getText().replaceAll("/", "")+ ".pdf"; // Replace
                                                                                                                        // this
                                                                                                                        // with
                                                                                                                        // the
                                                                                                                        // desired
                                                                                                                        // output
                                                                                                                        // path
            String nosuratkontrol = Sequel.cariIsi("select bridging_sep.noskdp from bridging_sep where bridging_sep.no_sep='" + lblNoSEPKompi1.getText() + "'");
            // bersihkan dulu foldernya
            deleteFilesInFolder(folderPath);
            if (Sequel.cariInteger("select count(inacbg_data_printclaim.no_sep) from inacbg_data_printclaim where inacbg_data_printclaim.no_sep='"
                            + lblNoSEPKompi1.getText() + "'") > 0) {

                Base64ToPDFForGabung(lblNoSEPKompi1.getText(), lblNoRawatKompi1.getText());
            }
            if (Sequel.cariInteger("select count(inacbg_data_printinvoice.no_sep) from inacbg_data_printinvoice where inacbg_data_printinvoice.no_sep='"
                            + lblNoSEPKompi1.getText() + "'") > 0) {

                Base64ToPDFForGabungInVoice(lblNoSEPKompi1.getText(), lblNoRawatKompi1.getText());
            }
            
            exportKlaimINACBGs();

            if (Sequel.cariInteger("select count(no_rawat) from bridging_sep where no_rawat='" + lblNoRawatKompi1.getText() + "'") > 0) {
                btnSEPResumePDF();
            }

            if (Sequel.cariInteger("select count(no_rawat) from resume_pasien where no_rawat='" + lblNoRawatKompi1.getText() + "'") > 0) {
                btnResumeRalanPdf();
            }

            if (Sequel.cariInteger("select count(no_rawat) from resume_pasien_ranap where no_rawat='" + lblNoRawatKompi1.getText() + "'") > 0) {
                btnresumeranappdf();
            }

            if (Sequel.cariInteger("select count(no_rawat) from penilaian_medis_igd where no_rawat='" + lblNoRawatKompi1.getText() + "'") > 0) {
                btnAwalMedisIGDPDF();
            }

            if (Sequel.cariInteger("select count(no_rawat) from periksa_lab where no_rawat='" + lblNoRawatKompi1.getText() + "'") > 0) {
                DlgCariPeriksaLab hasillab = new DlgCariPeriksaLab(null, false);
                hasillab.HasilLabPDFKlaimBPJSKompilasi(lblNoRawatKompi1.getText(), lblNoRMKompi1.getText());
            }
            if (Sequel.cariInteger("select count(no_rawat) from periksa_radiologi where no_rawat='" + lblNoRawatKompi1.getText() + "'") > 0) {
                DlgCariPeriksaRadiologi hasilrad = new DlgCariPeriksaRadiologi(null, false);
                hasilrad.RadPdfKlaimBPJSKompilasi(lblNoRawatKompi1.getText(), lblNoRMKompi1.getText());
            }

        if (Sequel.cariInteger("select count(no_rawat) from bridging_surat_pri_bpjs where no_rawat='"
                    + lblNoRawatKompi1.getText() + "'") > 0) {
                BPJSSPRI spri = new BPJSSPRI(null, false);
                spri.CetakSPRIKlaimPDF(lblNoSEPKompi1.getText(), lblNoRawatKompi1.getText());
            }

            if (Sequel.cariInteger("select count(no_rawat) from billing where no_rawat='"
                    + lblNoRawatKompi1.getText() + "'") > 0) {
                billingjasperpdf();
            }

            if (Sequel.cariInteger("select count(berkas_digital_perawatan.no_rawat) from berkas_digital_perawatan where berkas_digital_perawatan.no_rawat='"
                    + lblNoRawatKompi1.getText() + "'") > 0) {
                berkasdigitalperawatanPdf();
            }

            if (Sequel.cariInteger("select count(berkas_digital_perawatan.no_rawat) from berkas_digital_perawatan where berkas_digital_perawatan.no_rawat='"
                    + lblNoRawatKompi1.getText() + "'") > 0) {
                Base64ToPDFBerkasDigital();
            }

            if (Sequel.cariInteger("select count(data_triase_igd.no_rawat) from data_triase_igd where data_triase_igd.no_rawat='"
                    + lblNoRawatKompi1.getText() + "'") > 0) {
                btntriasepdf();
            }

            String kodedokter = Sequel.cariIsi("select reg_periksa.kd_dokter from reg_periksa where reg_periksa.no_rawat='"
                    + lblNoRawatKompi1.getText() + "'");
            String kodepoli = Sequel.cariIsi("select reg_periksa.kd_poli from reg_periksa where reg_periksa.no_rawat='"
                    + lblNoRawatKompi1.getText() + "'");
            String norawatpilih = Sequel
                    .cariIsi("select reg_periksa.no_rawat from reg_periksa where reg_periksa.kd_dokter='" + kodedokter
                            + "' and reg_periksa.kd_poli='" + kodepoli + "' and reg_periksa.no_rkm_medis='"
                            + lblNoRMKompi1.getText() + "' and reg_periksa.no_rawat != '" + lblNoRawatKompi1.getText()
                            + "' order by reg_periksa.no_rawat desc limit 1");

                if (Sequel.cariInteger("select count(no_surat) from bridging_surat_kontrol_bpjs where no_surat='"
                        + nosuratkontrol + "'") > 0) {
                    BPJSSuratKontrol suratkontrol = new BPJSSuratKontrol(null, false);
                    suratkontrol.SuratKontrolKlaimPDF(lblNoRawatKompi1.getText(), lblNoSEPKompi1.getText(), lblNoRMKompi1.getText());
                } else if (Sequel.cariInteger(
                        "select count(no_rawat) from skdp_bpjs where no_rawat='" + norawatpilih + "'") > 0) {
                    // source asli "select count(no_rawat) from skdp_bpjs_new where no_rawat='" + norawatpilih + "'") > 0) {
                    SuratKontrol suratkontrol = new SuratKontrol(null, false);
                    suratkontrol.SuratKontrolKlaimPDF(norawatpilih, lblNoRMKompi1.getText());
                }
            mergePDFsFromFolder(folderPath, outputPath, lblNoRawatKompi1.getText().replaceAll("/", ""));
            Sequel.queryu("truncate table inacbg_data_printclaim");
            Sequel.queryu("truncate table inacbg_data_printinvoice");
            // emptTeks();
            this.setCursor(Cursor.getDefaultCursor());
    }

    private boolean mergePDF() {
        boolean sukses = true;
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        File folder = new File("./berkaspdf/" + tanggalExport);
        File[] files = folder.listFiles();
        if (files != null) {
            try {
                Arrays.sort(files, (file1, file2) -> file1.getName().compareTo(file2.getName()));
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".pdf") && file.getName().startsWith(lblNoSEPKompi1.getText() + "_")) {
                        try {
                            pdfMerger.addSource(file);
                        } catch (Exception e) {
                            System.err.println("Error adding file: " + file.getName());
                            sukses = false;
                            throw e;
                        }
                    }
                }
                pdfMerger.setDestinationFileName("./berkaspdf/" + tanggalExport + "/" + lblNoSEPKompi1.getText() + ".pdf");
                pdfMerger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly(KOMPILASIBERKASMAXMEMORY * 1_000_000));
                System.out.println("PDFs berhasil digabung!");
                if (KOMPILASIBERKASAPLIKASIPDF == null || !KOMPILASIBERKASAPLIKASIPDF.equalsIgnoreCase("disable")) {
                    Valid.panggilUrlSmc(KOMPILASIBERKASAPLIKASIPDF, new File("./berkaspdf/" + tanggalExport + "/" + lblNoSEPKompi1.getText() + ".pdf").getAbsolutePath());
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
                sukses = false;
            }
        } else {
            System.out.println("No PDF files found in the folder: ./berkaspdf/" + tanggalExport);
        }
        return sukses;
    }
    
    private void cleanupSinglePDF(String containsName) {
        File folder = new File("./berkaspdf/" + tanggalExport);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith(containsName)) {
                    file.delete();
                }
            }
        }
    }

    private void cleanTemporaryPDF() {
        File folder = new File("./berkaspdf/" + tanggalExport);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith(lblNoSEPKompi1.getText() + "_")) {
                    if (file.delete()) {
                        System.out.println("Notif : Menghapus file sementara " + file.getName());
                    } else {
                        System.out.println("Notif : Gagal menghapus file sementara " + file.getName());
                    }
                }
            }
        } else {
            System.out.println("Notif : Tidak ada file sementara ditemukan dalam " + folder.toString());
        }
    }
    
    private void cekPengaturanKompilasi() {
        if (new File("./cache/pengaturankompilasi.iyem").isFile()) {
            try (FileReader fr = new FileReader("./cache/pengaturankompilasi.iyem")) {
                JsonNode root = new ObjectMapper().readTree(fr).path("pengaturan");
                if (root.hasNonNull("aplikasipdf")) {
                    KOMPILASIBERKASAPLIKASIPDF = root.path("aplikasipdf").asText();
                }
                
                if (root.hasNonNull("tanggalexport")) {
                    KOMPILASIBERKASGUNAKANTANGGALEXPORT = root.path("tanggalexport").asText();
                }
                
                if (root.hasNonNull("maxmemory")) {
                    KOMPILASIBERKASMAXMEMORY = root.path("maxmemory").asLong();
                }
            } catch (Exception e) {
                System.out.println("Notif : Tidak bisa membaca pengaturan kompilasi! Menggunakan pengaturan default...");
                System.out.println("Notif : " + e);
                KOMPILASIBERKASAPLIKASIPDF = koneksiDB.KOMPILASIBERKASAPLIKASIPDF();
                KOMPILASIBERKASGUNAKANTANGGALEXPORT = koneksiDB.KOMPILASIBERKASGUNAKANTANGGALEXPORT();
                KOMPILASIBERKASMAXMEMORY = koneksiDB.KOMPILASIBERKASMAXMEMORY();
            }
        } else {
            KOMPILASIBERKASAPLIKASIPDF = koneksiDB.KOMPILASIBERKASAPLIKASIPDF();
            KOMPILASIBERKASGUNAKANTANGGALEXPORT = koneksiDB.KOMPILASIBERKASGUNAKANTANGGALEXPORT();
            KOMPILASIBERKASMAXMEMORY = koneksiDB.KOMPILASIBERKASMAXMEMORY();
        }
        
        switch (KOMPILASIBERKASAPLIKASIPDF) {
            case "":
                TPathAplikasiPDF.setText("");
                TPathAplikasiPDF.setEditable(false);
                BtnPilihAplikasiPDF.setEnabled(false);
                CmbPilihanAplikasiPDF.setSelectedIndex(0);
                break;
            case "chrome":
                TPathAplikasiPDF.setText("");
                TPathAplikasiPDF.setEditable(false);
                BtnPilihAplikasiPDF.setEnabled(false);
                CmbPilihanAplikasiPDF.setSelectedIndex(1);
                break;
            case "firefox":
                TPathAplikasiPDF.setText("");
                TPathAplikasiPDF.setEditable(false);
                BtnPilihAplikasiPDF.setEnabled(false);
                CmbPilihanAplikasiPDF.setSelectedIndex(2);
                break;
            case "msedge":
                TPathAplikasiPDF.setText("");
                TPathAplikasiPDF.setEditable(false);
                BtnPilihAplikasiPDF.setEnabled(false);
                CmbPilihanAplikasiPDF.setSelectedIndex(3);
                break;
            case "disable":
                TPathAplikasiPDF.setText("");
                TPathAplikasiPDF.setEditable(false);
                BtnPilihAplikasiPDF.setEnabled(false);
                CmbPilihanAplikasiPDF.setSelectedIndex(5);
                break;
            default:
                TPathAplikasiPDF.setText(KOMPILASIBERKASAPLIKASIPDF);
                TPathAplikasiPDF.setEditable(true);
                BtnPilihAplikasiPDF.setEnabled(true);
                CmbPilihanAplikasiPDF.setSelectedIndex(4);
                break;
        }
        
        if (KOMPILASIBERKASGUNAKANTANGGALEXPORT.equalsIgnoreCase("sep")) {
            CmbPilihanTanggalExport.setSelectedIndex(1);
        } else {
            CmbPilihanTanggalExport.setSelectedIndex(0);
        }
        
        TMaxMemory.setText(String.valueOf(KOMPILASIBERKASMAXMEMORY));
    }
    
    private void btnSEPResumePDF() {
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {
            if (lblStatusRawatKompi1.getText().equals("Ralan")) {
                String nomorsep = Sequel
                        .cariIsi("select bridging_sep.no_sep from bridging_sep where bridging_sep.no_rawat='"
                                + lblNoRawatKompi1.getText() + "' and bridging_sep.jnspelayanan='2'");
                String jnspelayanan = Sequel
                        .cariIsi("select bridging_sep.jnspelayanan from bridging_sep where bridging_sep.no_rawat='"
                                + nomorsep + "'");
                String kodedokterregis = Sequel
                        .cariIsi("select reg_periksa.kd_dokter from reg_periksa where reg_periksa.no_rawat='"
                                + lblNoRawatKompi1.getText() + "'");
                String namadokterregis = Sequel.cariIsi(
                        "select dokter.nm_dokter from dokter where dokter.kd_dokter='" + kodedokterregis + "'");
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                Map<String, Object> param = new HashMap<>();
                param.put("namars", akses.getnamars());
                param.put("alamatrs", akses.getalamatrs());
                param.put("kotars", akses.getkabupatenrs());
                param.put("propinsirs", akses.getpropinsirs());
                param.put("kontakrs", akses.getkontakrs());
                param.put("logo", Sequel.cariGambar("select gambar.bpjs from gambar"));
                param.put("prb", Sequel.cariIsi("select bpjs_prb.prb from bpjs_prb where bpjs_prb.no_sep=?", nomorsep));
                if (Sequel.cariInteger("select count(pemeriksaan_ralan.no_rawat) from pemeriksaan_ralan where pemeriksaan_ralan.no_rawat='"
                                + lblNoRawatKompi1.getText() + "' and pemeriksaan_ralan.nip='" + kodedokterregis + "'") > 0) {
                    param.put("diagnosautama", Sequel.cariIsi("select pemeriksaan_ralan.penilaian from pemeriksaan_ralan where pemeriksaan_ralan.nip='"
                                    + kodedokterregis + "' and pemeriksaan_ralan.no_rawat='" + lblNoRawatKompi1.getText()
                                    + "'"));
                    param.put("uraian", Sequel.cariIsi("select pemeriksaan_ralan.instruksi from pemeriksaan_ralan where pemeriksaan_ralan.nip='"
                                    + kodedokterregis + "' and pemeriksaan_ralan.no_rawat='" + lblNoRawatKompi1.getText()
                                    + "'"));
                } else if (Sequel.cariInteger("select count(penilaian_medis_ralan.no_rawat) from penilaian_medis_ralan where penilaian_medis_ralan.no_rawat='"
                                + lblNoRawatKompi1.getText() + "' and penilaian_medis_ralan.kd_dokter='" + kodedokterregis
                                + "'") > 0) {
                    param.put("diagnosautama", Sequel.cariIsi("select penilaian_medis_ralan.diagnosis from penilaian_medis_ralan where penilaian_medis_ralan.no_rawat='"
                                    + lblNoRawatKompi1.getText() + "' and penilaian_medis_ralan.kd_dokter='" + kodedokterregis
                                    + "'"));
                    param.put("uraian", Sequel.cariIsi("select penilaian_medis_ralan.konsulrujuk from penilaian_medis_ralan where penilaian_medis_ralan.no_rawat='"
                                    + lblNoRawatKompi1.getText() + "' and penilaian_medis_ralan.kd_dokter='" + kodedokterregis
                                    + "'"));
                } else if (Sequel.cariInteger("select count(penilaian_medis_ralan_tht.no_rawat) from penilaian_medis_ralan_tht where penilaian_medis_ralan_tht.no_rawat='"
                                + lblNoRawatKompi1.getText() + "' and penilaian_medis_ralan_tht.kd_dokter='" + kodedokterregis
                                + "'") > 0) {
                    param.put("diagnosautama", Sequel.cariIsi("select penilaian_medis_ralan_tht.diagnosis from penilaian_medis_ralan_tht where penilaian_medis_ralan_tht.no_rawat='"
                                    + lblNoRawatKompi1.getText() + "' and penilaian_medis_ralan_tht.kd_dokter='"
                                    + kodedokterregis + "'"));
                    param.put("uraian", Sequel.cariIsi("select penilaian_medis_ralan_tht.konsulrujuk from penilaian_medis_ralan_tht where penilaian_medis_ralan_tht.no_rawat='"
                                    + lblNoRawatKompi1.getText() + "' and penilaian_medis_ralan_tht.kd_dokter='"
                                    + kodedokterregis + "'"));
                } else if (Sequel.cariInteger("select count(penilaian_medis_ralan_psikiatrik.no_rawat) from penilaian_medis_ralan_psikiatrik where penilaian_medis_ralan_psikiatrik.no_rawat='"
                                + lblNoRawatKompi1.getText() + "' and penilaian_medis_ralan_psikiatrik.kd_dokter='"
                                + kodedokterregis + "'") > 0) {
                    param.put("diagnosautama", Sequel.cariIsi("select penilaian_medis_ralan_psikiatrik.diagnosis1 from penilaian_medis_ralan_psikiatrik where penilaian_medis_ralan_psikiatrik.no_rawat='"
                                    + lblNoRawatKompi1.getText() + "' and penilaian_medis_ralan_psikiatrik.kd_dokter='"
                                    + kodedokterregis + "'"));
                    param.put("uraian", Sequel.cariIsi("select penilaian_medis_ralan_psikiatrik.tata from penilaian_medis_ralan_psikiatrik where penilaian_medis_ralan_psikiatrik.no_rawat='"
                                    + lblNoRawatKompi1.getText() + "' and penilaian_medis_ralan_psikiatrik.kd_dokter='"
                                    + kodedokterregis + "'"));
                } else if (Sequel.cariInteger("select count(pemeriksaan_ralan.no_rawat) from pemeriksaan_ralan inner join reg_periksa where pemeriksaan_ralan.no_rawat='"
                                + lblNoRawatKompi1.getText() + "' and reg_periksa.kd_poli = 'U0009'") > 0) {
                    param.put("diagnosautama", Sequel.cariIsi("select pemeriksaan_ralan.penilaian from pemeriksaan_ralan where  pemeriksaan_ralan.no_rawat='"
                                    + lblNoRawatKompi1.getText() + "'"));
                    param.put("uraian", Sequel.cariIsi("select pemeriksaan_ralan.instruksi from pemeriksaan_ralan where pemeriksaan_ralan.no_rawat='"
                                    + lblNoRawatKompi1.getText() + "'"));
                }

                // param.put("prosedurutama", Sequel.cariIsi("select pemeriksaan_ralan.rtl from
                // pemeriksaan_ralan where pemeriksaan_ralan.nip='" + kodedokterregis + "' and
                // pemeriksaan_ralan.no_rawat='" + lblNoRawat.getText() + "'"));
                param.put("finger",
                        "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs()
                                + "\nDitandatangani secara elektronik oleh " + namadokterregis + "\nID "
                                + kodedokterregis + "\n");
                String tindakandr = "";
                try {
                    psambiltindakan = koneksi.prepareStatement(
                            "SELECT\n"
                                    + "jns_perawatan.nm_perawatan\n"
                                    + "FROM\n"
                                    + "	rawat_jl_dr\n"
                                    + "	INNER JOIN jns_perawatan ON rawat_jl_dr.kd_jenis_prw = jns_perawatan.kd_jenis_prw \n"
                                    + "WHERE\n"
                                    + "	rawat_jl_dr.no_rawat=? order by rawat_jl_dr.tgl_perawatan,rawat_jl_dr.jam_rawat ");
                    try {
                        psambiltindakan.setString(1, lblNoRawatKompi1.getText());
                        rsambiltindakan = psambiltindakan.executeQuery();
                        while (rsambiltindakan.next()) {
                            tindakandr = tindakandr + rsambiltindakan.getString(1) + "\n";
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : " + e);
                    } finally {
                        if (rsambiltindakan != null) {
                            rsambiltindakan.close();
                        }
                        if (psambiltindakan != null) {
                            psambiltindakan.close();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                }

                try {
                    psambiltindakan = koneksi.prepareStatement(
                            "SELECT\n"
                                    + "jns_perawatan.nm_perawatan\n"
                                    + "FROM\n"
                                    + "	rawat_jl_drpr\n"
                                    + "	INNER JOIN jns_perawatan ON rawat_jl_drpr.kd_jenis_prw = jns_perawatan.kd_jenis_prw \n"
                                    + "WHERE\n"
                                    + "	rawat_jl_drpr.no_rawat=? order by rawat_jl_drpr.tgl_perawatan,rawat_jl_drpr.jam_rawat ");
                    try {
                        psambiltindakan.setString(1, lblNoRawatKompi1.getText());
                        rsambiltindakan = psambiltindakan.executeQuery();
                        while (rsambiltindakan.next()) {
                            tindakandr = tindakandr + rsambiltindakan.getString(1) + "\n";
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : " + e);
                    } finally {
                        if (rsambiltindakan != null) {
                            rsambiltindakan.close();
                        }
                        if (psambiltindakan != null) {
                            psambiltindakan.close();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                }

                try {
                    psambiltindakan = koneksi.prepareStatement(
                            "SELECT\n"
                                    + "jns_perawatan.nm_perawatan\n"
                                    + "FROM\n"
                                    + "	rawat_jl_pr\n"
                                    + "	INNER JOIN jns_perawatan ON rawat_jl_pr.kd_jenis_prw = jns_perawatan.kd_jenis_prw \n"
                                    + "WHERE\n"
                                    + "	rawat_jl_pr.no_rawat=? order by rawat_jl_pr.tgl_perawatan,rawat_jl_pr.jam_rawat ");
                    try {
                        psambiltindakan.setString(1, lblNoRawatKompi1.getText());
                        rsambiltindakan = psambiltindakan.executeQuery();
                        while (rsambiltindakan.next()) {
                            tindakandr = tindakandr + rsambiltindakan.getString(1) + "\n";
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : " + e);
                    } finally {
                        if (rsambiltindakan != null) {
                            rsambiltindakan.close();
                        }
                        if (psambiltindakan != null) {
                            psambiltindakan.close();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                }

                try {
                    psambillab = koneksi.prepareStatement(
                            "select jns_perawatan_lab.nm_perawatan "
                                    + " from periksa_lab inner join jns_perawatan_lab on jns_perawatan_lab.kd_jenis_prw=periksa_lab.kd_jenis_prw where "
                                    + " periksa_lab.no_rawat=? ");
                    try {
                        psambillab.setString(1, lblNoRawatKompi1.getText());
                        rsambillab = psambillab.executeQuery();
                        while (rsambillab.next()) {
                            tindakandr = tindakandr + rsambillab.getString(1) + "\n";
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : " + e);
                    } finally {
                        if (rsambillab != null) {
                            rsambillab.close();
                        }
                        if (psambillab != null) {
                            psambillab.close();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                }

                try {
                    psambilrad = koneksi.prepareStatement(
                            "select jns_perawatan_radiologi.nm_perawatan "
                                    + " from periksa_radiologi inner join jns_perawatan_radiologi on jns_perawatan_radiologi.kd_jenis_prw=periksa_radiologi.kd_jenis_prw where "
                                    + " periksa_radiologi.no_rawat=?");
                    try {
                        psambilrad.setString(1, lblNoRawatKompi1.getText());
                        rsambilrad = psambilrad.executeQuery();
                        while (rsambilrad.next()) {
                            tindakandr = tindakandr + rsambilrad.getString(1) + "\n";
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : " + e);
                    } finally {
                        if (rsambilrad != null) {
                            rsambilrad.close();
                        }
                        if (psambilrad != null) {
                            psambilrad.close();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                }

                param.put("prosedurutama", tindakandr);
                param.put("dokterregis", namadokterregis);
                param.put("parameter", nomorsep);
                Valid.MyReportPDFKlaim("rptBridgingSEP4Resume.jasper", "report", "1SEP", param, "hasilkompilasiklaim",
                        lblNoRawatKompi1.getText().replaceAll("/", ""));
                this.setCursor(Cursor.getDefaultCursor());
            } else {
                String nomorsep = Sequel.cariIsi("select bridging_sep.no_sep from bridging_sep where bridging_sep.jnspelayanan='1' and bridging_sep.no_rawat='"
                                + lblNoRawatKompi1.getText() + "'");
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                Map<String, Object> param = new HashMap<>();
                param.put("namars", akses.getnamars());
                param.put("alamatrs", akses.getalamatrs());
                param.put("kotars", akses.getkabupatenrs());
                param.put("propinsirs", akses.getpropinsirs());
                param.put("kontakrs", akses.getkontakrs());
                param.put("prb", Sequel.cariIsi("select bpjs_prb.prb from bpjs_prb where bpjs_prb.no_sep=?", nomorsep));
                param.put("logo", Sequel.cariGambar("select gambar.bpjs from gambar"));
                param.put("parameter", nomorsep);
                Valid.MyReportPDFKlaim("rptBridgingSEP6.jasper", "report", "1SEP", param, "hasilkompilasiklaim",
                //Valid.MyReportPDFKlaim("rptBridgingSEP.jasper", "report", "1SEP", param, "hasilkompilasiklaim",
                        lblNoRawatKompi1.getText().replaceAll("/", ""));
                this.setCursor(Cursor.getDefaultCursor());
            }

        }
    }

    private void btnAwalMedisIGDPDF() {
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {

            String kodedokterregis = Sequel
                    .cariIsi("select reg_periksa.kd_dokter from reg_periksa where reg_periksa.no_rawat='"
                            + lblNoRawatKompi1.getText() + "'");
            String namadokterregis = Sequel
                    .cariIsi("select dokter.nm_dokter from dokter where dokter.kd_dokter='" + kodedokterregis + "'");
            Map<String, Object> param = new HashMap<>();
            param.put("namars", akses.getnamars());
            param.put("alamatrs", akses.getalamatrs());
            param.put("kotars", akses.getkabupatenrs());
            param.put("propinsirs", akses.getpropinsirs());
            param.put("kontakrs", akses.getkontakrs());
            param.put("emailrs", akses.getemailrs());
            param.put("logo", Sequel.cariGambar("select logo from setting"));
            param.put("lokalis", Sequel.cariGambar("select lokalis from gambar"));
            finger = Sequel.cariIsi(
                    "select sha1(sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",
                    kodedokterregis);
            param.put("finger",
                    "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs()
                            + "\nDitandatangani secara elektronik oleh " + namadokterregis + "\nID "
                            + (finger.equals("") ? kodedokterregis : finger) + "\n");

            Valid.MyReportqrypdfKlaim("rptCetakPenilaianAwalMedisIGD.jasper", "report", "9AWMEDIGD",
                    /*"select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,penilaian_medis_igd.tanggal,"
                            + "penilaian_medis_igd.kd_dokter,penilaian_medis_igd.anamnesis,penilaian_medis_igd.hubungan,penilaian_medis_igd.keluhan_utama,penilaian_medis_igd.rps,penilaian_medis_igd.rpk,penilaian_medis_igd.rpd,penilaian_medis_igd.rpo,penilaian_medis_igd.alergi,"
                            + "penilaian_medis_igd.keadaan,penilaian_medis_igd.gcs,penilaian_medis_igd.kesadaran,penilaian_medis_igd.td,penilaian_medis_igd.nadi,penilaian_medis_igd.rr,penilaian_medis_igd.suhu,penilaian_medis_igd.spo,penilaian_medis_igd.bb,penilaian_medis_igd.tb,"
                            + "penilaian_medis_igd.kepala,penilaian_medis_igd.mata,penilaian_medis_igd.mulut,penilaian_medis_igd.leher,penilaian_medis_igd.thoraks,penilaian_medis_igd.abdomen,penilaian_medis_igd.ekstremitas,penilaian_medis_igd.genital,penilaian_medis_igd.ket_fisik,"
                            + "penilaian_medis_igd.ket_lokalis,penilaian_medis_igd.ekg,penilaian_medis_igd.rad,penilaian_medis_igd.lab,penilaian_medis_igd.diagnosis,penilaian_medis_igd.tata,dokter.nm_dokter,penilaian_medis_igd.keadaan1,penilaian_medis_igd.kulit,penilaian_medis_igd.dada,penilaian_medis_igd.hidung,penilaian_medis_igd.telinga,penilaian_medis_igd.rencanaranap,penilaian_medis_igd.indikasiranap, penilaian_medis_igd.nyeri,penilaian_medis_igd.lasupdate "
                            + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                            + "inner join penilaian_medis_igd on reg_periksa.no_rawat=penilaian_medis_igd.no_rawat "
                            + "inner join dokter on penilaian_medis_igd.kd_dokter=dokter.kd_dokter where penilaian_medis_igd.no_rawat='"*/
                                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,penilaian_medis_igd.tanggal,"+
                "penilaian_medis_igd.kd_dokter,penilaian_medis_igd.anamnesis,penilaian_medis_igd.hubungan,penilaian_medis_igd.keluhan_utama,penilaian_medis_igd.rps,penilaian_medis_igd.rpk,penilaian_medis_igd.rpd,penilaian_medis_igd.rpo,penilaian_medis_igd.alergi,"+
                "penilaian_medis_igd.keadaan,penilaian_medis_igd.gcs,penilaian_medis_igd.kesadaran,penilaian_medis_igd.td,penilaian_medis_igd.nadi,penilaian_medis_igd.rr,penilaian_medis_igd.suhu,penilaian_medis_igd.spo,penilaian_medis_igd.bb,penilaian_medis_igd.tb,"+
                "penilaian_medis_igd.kepala,penilaian_medis_igd.mata,penilaian_medis_igd.gigi,penilaian_medis_igd.leher,penilaian_medis_igd.thoraks,penilaian_medis_igd.abdomen,penilaian_medis_igd.ekstremitas,penilaian_medis_igd.genital,penilaian_medis_igd.ket_fisik,"+
                "penilaian_medis_igd.ket_lokalis,penilaian_medis_igd.ekg,penilaian_medis_igd.rad,penilaian_medis_igd.lab,penilaian_medis_igd.diagnosis,penilaian_medis_igd.tata,dokter.nm_dokter "+
                "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                "inner join penilaian_medis_igd on reg_periksa.no_rawat=penilaian_medis_igd.no_rawat "+
                "inner join dokter on penilaian_medis_igd.kd_dokter=dokter.kd_dokter where penilaian_medis_igd.no_rawat='"
                            + lblNoRawatKompi1.getText() + "'",
                    param, "hasilkompilasiklaim", lblNoRawatKompi1.getText().replaceAll("/", ""));
        }
    }
    
    private void btnResumeRalanPdf(){
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            param.put("norawat",lblNoRawatKompi1.getText());
//            param.put("kd_dokter1",NamaDokter1.getText());
            tanggal="";
                param.put("ruang",Sequel.cariIsi("select poliklinik.nm_poli from poliklinik inner join reg_periksa on reg_periksa.kd_poli=poliklinik.kd_poli where reg_periksa.no_rawat='"+lblNoRawatKompi1.getText()+"'"));
                tanggal=Sequel.cariIsi("select DATE_FORMAT(tgl_registrasi, '%d-%m-%Y') from reg_periksa where no_rawat='"+lblNoRawatKompi1.getText()+"'");
                param.put("tanggalkeluar",tanggal);
            
            finger="tes";
//            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+lblNoRawatKompi1.getText()+"\nID "+(finger.equals("")?lblNoRawatKompi1.getText():finger)+"\n"+tanggal); 
            
            
//            Valid.MyReport("rptLaporanResume.jasper","report","::[ Laporan Resume Pasien ]::",param);
            
            Valid.MyReportqrypdfKlaim("rptLaporanResumeRalan.jasper","report","1TResumeRalan",
                "select reg_periksa.tgl_registrasi,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir," +
                "reg_periksa.umurdaftar,reg_periksa.sttsumur,pasien.pekerjaan,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab,', ',propinsi.nm_prop) as alamat, " +
                "resume_pasien.kd_dokter,dokter.nm_dokter,resume_pasien.kondisi_pulang,resume_pasien.keluhan_utama,resume_pasien.jalannya_penyakit, " +
                "resume_pasien.pemeriksaan_penunjang,resume_pasien.hasil_laborat,resume_pasien.diagnosa_utama,resume_pasien.kd_diagnosa_utama, " +
                "resume_pasien.diagnosa_sekunder,resume_pasien.kd_diagnosa_sekunder,resume_pasien.diagnosa_sekunder2,resume_pasien.kd_diagnosa_sekunder2, " +
                "resume_pasien.diagnosa_sekunder3,resume_pasien.kd_diagnosa_sekunder3,resume_pasien.diagnosa_sekunder4,resume_pasien.kd_diagnosa_sekunder4, " +
                "resume_pasien.prosedur_utama,resume_pasien.kd_prosedur_utama,resume_pasien.prosedur_sekunder,resume_pasien.kd_prosedur_sekunder, " +
                "resume_pasien.prosedur_sekunder2,resume_pasien.kd_prosedur_sekunder2,resume_pasien.prosedur_sekunder3,resume_pasien.kd_prosedur_sekunder3, " +
                "resume_pasien.obat_pulang from resume_pasien inner join reg_periksa on resume_pasien.no_rawat=reg_periksa.no_rawat  " +
                "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join dokter on resume_pasien.kd_dokter=dokter.kd_dokter " +
                "inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec " +
                "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab inner join propinsi on pasien.kd_prop=propinsi.kd_prop " +
                "where resume_pasien.no_rawat='"+lblNoRawatKompi1.getText()+"'",param,"hasilkompilasiklaim", lblNoRawatKompi1.getText().replaceAll("/", ""));
        }
    }
    
    private void btnresumeranappdf(){
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {
            Map<String, Object> param = new HashMap<>();    
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            param.put("norawat",lblNoRawatKompi1.getText());
//            param.put("nmdpjp1",Sequel.cariIsi("select dokter.nm_dokter from dokter where dokter.kd_dokter='"+KodeDokter1.getText()+"'"));
//            param.put("nmdpjp2",Sequel.cariIsi("select dokter.nm_dokter from dokter where dokter.kd_dokter='"+KodeDokter2.getText()+"'"));
//            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            finger="";
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+akses.getkode()+"\nID "+akses.getkode()); 
            try {
                ps=koneksi.prepareStatement("select dpjp_ranap.kd_dokter,dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat=?");
                try {
                    ps.setString(1,lblNoRawatKompi1.getText());
                    rs=ps.executeQuery();
                    i=2;
                    while(rs.next()){
                       if(i==2){
                           finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("kd_dokter"));
                           param.put("finger2","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nm_dokter")+"\nID "+(finger.equals("")?rs.getString("kd_dokter"):finger)+"\n"+"");
                           param.put("namadokter2",rs.getString("nm_dokter")); 
                       }
                       if(i==3){
                           finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("kd_dokter"));
                           param.put("finger3","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nm_dokter")+"\nID "+(finger.equals("")?rs.getString("kd_dokter"):finger)+"\n"+"");
                           param.put("namadokter3",rs.getString("nm_dokter")); 
                       }
                       i++;
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
            param.put("ruang","-");
            param.put("tanggalkeluar","-");
            Valid.MyReportqrypdfKlaim("rptLaporanResumeRanap.jasper","report","1UResumeRanap",
                "select reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,resume_pasien_ranap.kd_dokter,dokter.nm_dokter,reg_periksa.kd_dokter as kodepengirim,pengirim.nm_dokter as pengirim," +
                " reg_periksa.tgl_registrasi,reg_periksa.jam_reg,resume_pasien_ranap.diagnosa_awal,resume_pasien_ranap.alasan,resume_pasien_ranap.keluhan_utama,resume_pasien_ranap.pemeriksaan_fisik," +
                " resume_pasien_ranap.jalannya_penyakit,resume_pasien_ranap.pemeriksaan_penunjang,resume_pasien_ranap.hasil_laborat,resume_pasien_ranap.tindakan_dan_operasi,resume_pasien_ranap.obat_di_rs," +
                " resume_pasien_ranap.diagnosa_utama,resume_pasien_ranap.kd_diagnosa_utama,resume_pasien_ranap.diagnosa_sekunder,resume_pasien_ranap.kd_diagnosa_sekunder,resume_pasien_ranap.diagnosa_sekunder2," +
                " resume_pasien_ranap.kd_diagnosa_sekunder2,resume_pasien_ranap.diagnosa_sekunder3,resume_pasien_ranap.kd_diagnosa_sekunder3,resume_pasien_ranap.diagnosa_sekunder4," +
                " resume_pasien_ranap.kd_diagnosa_sekunder4,resume_pasien_ranap.prosedur_utama,resume_pasien_ranap.kd_prosedur_utama,resume_pasien_ranap.prosedur_sekunder,resume_pasien_ranap.kd_prosedur_sekunder," +
                " resume_pasien_ranap.prosedur_sekunder2,resume_pasien_ranap.kd_prosedur_sekunder2,resume_pasien_ranap.prosedur_sekunder3,resume_pasien_ranap.kd_prosedur_sekunder3,resume_pasien_ranap.alergi," +
                " resume_pasien_ranap.diet,resume_pasien_ranap.lab_belum,resume_pasien_ranap.edukasi,resume_pasien_ranap.cara_keluar,resume_pasien_ranap.ket_keluar,resume_pasien_ranap.keadaan," +
                " resume_pasien_ranap.ket_keadaan,resume_pasien_ranap.dilanjutkan,resume_pasien_ranap.ket_dilanjutkan,resume_pasien_ranap.kontrol,resume_pasien_ranap.obat_pulang,reg_periksa.kd_pj,penjab.png_jawab," +
                " reg_periksa.umurdaftar,reg_periksa.sttsumur,pasien.pekerjaan,pasien.jk,pasien.tgl_lahir,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) as alamat" +
                " from resume_pasien_ranap inner join reg_periksa on resume_pasien_ranap.no_rawat=reg_periksa.no_rawat inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                " inner join dokter on resume_pasien_ranap.kd_dokter=dokter.kd_dokter inner join dokter as pengirim on reg_periksa.kd_dokter=pengirim.kd_dokter " +
                " inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab " +
                " inner join penjab on penjab.kd_pj=reg_periksa.kd_pj" +
                " where resume_pasien_ranap.no_rawat='"+lblNoRawatKompi1.getText()+"'",param,"hasilkompilasiklaim", lblNoRawatKompi1.getText().replaceAll("/", ""));
        }
    }

    private void Base64ToPDF(String nosep) {

        String base64EncodedData = Sequel.cariIsi(
                "select inacbg_data_printclaim.filepdf from inacbg_data_printclaim where inacbg_data_printclaim.no_sep='"
                        + nosep + "'");
        Properties systemProp = System.getProperties();
        String currentDir = systemProp.getProperty("user.dir");
        String folderPath = currentDir + "/hasilkompilasiklaim/"; // Replace this with the actual folder path
        String outputFilePath = currentDir + "/hasilkompilasiklaim/0PDFNCC" + nosep.replaceAll("/", "") + ".pdf"; // Replace
                                                                                                                  // this
                                                                                                                  // with
                                                                                                                  // the
                                                                                                                  // desired
                                                                                                                  // output
                                                                                                                  // path
        try {
            // Decode the Base64 data
            byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedData);

            // Create a FileOutputStream to write the decoded data to the PDF file
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                fos.write(decodedBytes);
            }
            File f = new File(outputFilePath);
            Desktop.getDesktop().open(f);

            System.out.println("PDF file has been successfully decoded and saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Base64ToPDFInvoice(String nosep) {

        String base64EncodedData = Sequel.cariIsi(
                "select inacbg_data_printinvoice.filepdf from inacbg_data_printinvoice where inacbg_data_printinvoice.no_sep='"
                        + nosep + "'");
        Properties systemProp = System.getProperties();
        String currentDir = systemProp.getProperty("user.dir");
        String folderPath = currentDir + "/hasilkompilasiklaim/"; // Replace this with the actual folder path
        String outputFilePath = currentDir + "/hasilkompilasiklaim/9INVOICE" + nosep.replaceAll("/", "") + ".pdf"; // Replace
                                                                                                                   // this
                                                                                                                   // with
                                                                                                                   // the
                                                                                                                   // desired
                                                                                                                   // output
                                                                                                                   // path
        try {
            // Decode the Base64 data
            byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedData);

            // Create a FileOutputStream to write the decoded data to the PDF file
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                fos.write(decodedBytes);
            }
            File f = new File(outputFilePath);
            Desktop.getDesktop().open(f);

            System.out.println("PDF file has been successfully decoded and saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Base64ToPDFForGabung(String nosep, String norawat) {

        String base64EncodedData = Sequel.cariIsi(
                "select inacbg_data_printclaim.filepdf from inacbg_data_printclaim where inacbg_data_printclaim.no_sep='"
                        + nosep + "'");
        Properties systemProp = System.getProperties();
        String currentDir = systemProp.getProperty("user.dir");
        String folderPath = currentDir + "/hasilkompilasiklaim/"; // Replace this with the actual folder path
        String outputFilePath = currentDir + "/hasilkompilasiklaim/0PDFNCC" + norawat.replaceAll("/", "") + ".pdf"; // Replace
                                                                                                                    // this
                                                                                                                    // with
                                                                                                                    // the
                                                                                                                    // desired
                                                                                                                    // output
                                                                                                                    // path
        try {
            // Decode the Base64 data
            byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedData);

            // Create a FileOutputStream to write the decoded data to the PDF file
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                fos.write(decodedBytes);
            }
            // File f = new File(outputFilePath);
            // Desktop.getDesktop().open(f);

            System.out.println("PDF file has been successfully decoded and saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Base64ToPDFForGabungInVoice(String nosep, String norawat) {

        String base64EncodedData = Sequel.cariIsi(
                "select inacbg_data_printinvoice.filepdf from inacbg_data_printinvoice where inacbg_data_printinvoice.no_sep='"
                        + nosep + "'");
        Properties systemProp = System.getProperties();
        String currentDir = systemProp.getProperty("user.dir");
        String folderPath = currentDir + "/hasilkompilasiklaim/"; // Replace this with the actual folder path
        String outputFilePath = currentDir + "/hasilkompilasiklaim/9INVOICE" + norawat.replaceAll("/", "") + ".pdf"; // Replace
                                                                                                                     // this
                                                                                                                     // with
                                                                                                                     // the
                                                                                                                     // desired
                                                                                                                     // output
                                                                                                                     // path
        try {
            // Decode the Base64 data
            byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedData);

            // Create a FileOutputStream to write the decoded data to the PDF file
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                fos.write(decodedBytes);
            }
            // File f = new File(outputFilePath);
            // Desktop.getDesktop().open(f);

            System.out.println("PDF file has been successfully decoded and saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void billingjasper(){
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            param.put("norawat",lblNoRawatKompi1.getText());
            param.put("totalbiaya", "Rp. "+Valid.SetAngka(Sequel.cariIsiAngka("select sum(billing.totalbiaya) from billing where billing.no_rawat='"+lblNoRawatKompi1.getText()+"'")));
            param.put("tanggalbayar",Valid.SetTgl2(Sequel.cariIsi("select billing.tgl_byr from billing where billing.no_rawat='"+lblNoRawatKompi1.getText()+"'")));
//            param.put("kd_dokter1",NamaDokter1.getText());
            
            finger=akses.getkode();
//            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+lblNoRawatKompi1.getText()+"\nID "+(finger.equals("")?lblNoRawatKompi1.getText():finger)+"\n"+""); 
            String Petugas;
//            if(Sequel.cariIsi("select petugas.nama from petugas inner join tagihan_sadewa on tagihan_sadewa.petugas=petugas.nip where tagihan_sadewa.no_nota='"+lblNoRawat.getText()+"'"));
            if(Sequel.cariInteger("select count(tagihan_sadewa.no_nota) from tagihan_sadewa where tagihan_sadewa.no_nota='"+lblNoRawatKompi1.getText()+"'")==1){
                Petugas=Sequel.cariIsi("select petugas.nama from petugas inner join tagihan_sadewa on tagihan_sadewa.petugas=petugas.nip where tagihan_sadewa.no_nota='"+lblNoRawatKompi1.getText()+"'");
            }else if(Sequel.cariInteger("select count(tagihan_sadewa.no_nota) from tagihan_sadewa where tagihan_sadewa.no_nota='"+lblNoRawatKompi1.getText()+"'")==1){
                
            }
            param.put("petugas", Sequel.cariIsi("select petugas.nama from petugas where petugas.nip='"+akses.getkode()+"'"));
            
//            Valid.MyReport("rptLaporanResume.jasper","report","::[ Laporan Resume Pasien ]::",param);
            
            Valid.MyReportqry("rptBillingRalan.jasper","report","::[ Laporan Billing Rawat Jalan ]::",
                "select billing.no, billing.nm_perawatan,billing.jumlah,billing.totalbiaya from billing where billing.no_rawat='"+lblNoRawatKompi1.getText()+"'",param);
        }
    }
    
    private void billingjasperpdf(){
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            param.put("norawat",lblNoRawatKompi1.getText());
            param.put("totalbiaya", Sequel.cariIsiAngka("select sum(billing.totalbiaya) from billing where billing.no_rawat='"+lblNoRawatKompi1.getText()+"'"));
            param.put("tanggalbayar",Valid.SetTgl2(Sequel.cariIsi("select billing.tgl_byr from billing where billing.no_rawat='"+lblNoRawatKompi1.getText()+"'")));
//            param.put("kd_dokter1",NamaDokter1.getText());
            
            finger=akses.getkode();
//            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+lblNoRawatKompi1.getText()+"\nID "+(finger.equals("")?lblNoRawatKompi1.getText():finger)+"\n"+""); 
            param.put("petugas", Sequel.cariIsi("select petugas.nama from petugas where petugas.nip='"+akses.getkode()+"'"));
            
//            Valid.MyReport("rptLaporanResume.jasper","report","::[ Laporan Resume Pasien ]::",param);
            
            Valid.MyReportqrypdfKlaim("rptBillingRalan.jasper","report","5BillingRalan",
                "select billing.no, billing.nm_perawatan,billing.jumlah,billing.totalbiaya from billing where billing.no_rawat='"+lblNoRawatKompi1.getText()+"'",param,"hasilkompilasiklaim", lblNoRawatKompi1.getText().replaceAll("/", ""));
        }
    }
    
    private void berkasdigitalperawatanPdf(){
        Map<String, Object> param = new HashMap<>();
        param.put("berkas1","http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/berkasrawat/");
        Valid.MyReportqrypdfKlaim("rptBerkasDigital.jasper","report","9BerkasDigital",
                "select berkas_digital_perawatan.lokasi_file from berkas_digital_perawatan where berkas_digital_perawatan.lokasi_file not like '%.pdf%' and berkas_digital_perawatan.no_rawat='"+lblNoRawatKompi1.getText()+"'",param,"hasilkompilasiklaim", lblNoRawatKompi1.getText().replaceAll("/", ""));
    }
    
    private void Base64ToPDFBerkasDigital() {
        Properties systemProp = System.getProperties();
        String currentDir = systemProp.getProperty("user.dir");
        String folderPath = currentDir + "/hasilkompilasiklaim/"; // Replace this with the actual folder path
        //        String outputFilePath = currentDir + "/hasilkompilasiklaim/91BerkasDigital" + lblNoRawat.getText().replaceAll("/", "") + ".pdf";
        Integer cekpdfberkasdigital=Sequel.cariInteger("select count(berkas_digital_perawatan.no_rawat) from berkas_digital_perawatan where berkas_digital_perawatan.lokasi_file like '%.pdf%' and berkas_digital_perawatan.no_rawat='"+lblNoRawatKompi1.getText()+"'");
        /*if(cekpdfberkasdigital==1){
        String outputFilePath = currentDir + "/hasilkompilasiklaim/91BerkasDigital" + lblNoRawat.getText().replaceAll("/", "") + ".pdf";
        URL website = new URL("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + prop.getProperty("PORTWEB") + "/"+ prop.getProperty("HYBRIDWEB") + "/berkasrawat/"+Sequel.cariIsi("select berkas_digital_perawatan.lokasi_file from berkas_digital_perawatan where berkas_digital_perawatan.lokasi_file like '%.pdf%' and berkas_digital_perawatan.no_rawat='"+lblNoRawat.getText()+"'"));
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(outputFilePath);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }else if(cekpdfberkasdigital==2){
        String outputFilePath = currentDir + "/hasilkompilasiklaim/91BerkasDigital" + lblNoRawat.getText().replaceAll("/", "") + ".pdf";
        String outputFilePath1 = currentDir + "/hasilkompilasiklaim/92BerkasDigital" + lblNoRawat.getText().replaceAll("/", "") + ".pdf";
        URL website = new URL("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + prop.getProperty("PORTWEB") + "/"+ prop.getProperty("HYBRIDWEB") + "/berkasrawat/"+Sequel.cariIsi("select berkas_digital_perawatan.lokasi_file from berkas_digital_perawatan where berkas_digital_perawatan.lokasi_file like '%.pdf%' and berkas_digital_perawatan.no_rawat='"+lblNoRawat.getText()+"'"));
        URL website1 = new URL("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + prop.getProperty("PORTWEB") + "/"+ prop.getProperty("HYBRIDWEB") + "/berkasrawat/"+Sequel.cariIsi("select berkas_digital_perawatan.lokasi_file from berkas_digital_perawatan where berkas_digital_perawatan.lokasi_file like '%.pdf%' and berkas_digital_perawatan.no_rawat='"+lblNoRawat.getText()+"' order by berkas_digital_perawatan.lokasi_file desc"));
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        ReadableByteChannel rbc1 = Channels.newChannel(website1.openStream());
        FileOutputStream fos = new FileOutputStream(outputFilePath);
        FileOutputStream fos1 = new FileOutputStream(outputFilePath1);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos1.getChannel().transferFrom(rbc1, 0, Long.MAX_VALUE);
        }else */if(cekpdfberkasdigital>0){
            try {
                ps=koneksi.prepareStatement("select berkas_digital_perawatan.lokasi_file from berkas_digital_perawatan where (berkas_digital_perawatan.lokasi_file not like '%.jpg%' and berkas_digital_perawatan.lokasi_file not like '%.jpeg%' and berkas_digital_perawatan.lokasi_file not like '%.png%') and berkas_digital_perawatan.no_rawat='"+lblNoRawatKompi1.getText()+"'");
//                ps=koneksi.prepareStatement("select berkas_digital_perawatan.lokasi_file from berkas_digital_perawatan where berkas_digital_perawatan.lokasi_file '<>.jpg' or berkas_digital_perawatan.lokasi_file '<>.jpeg' or berkas_digital_perawatan.lokasi_file '<>.png' and berkas_digital_perawatan.no_rawat='"+lblNoRawat.getText()+"'");
try{
    rs=ps.executeQuery();
    i=1;
    while(rs.next()){
        String outputFilePath = currentDir + "/hasilkompilasiklaim/91BerkasDigital" + lblNoRawatKompi1.getText().replaceAll("/", "") + i+++".pdf";
        URL website = new URL("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/berkasrawat/"+rs.getString("lokasi_file"));
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(outputFilePath);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
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
            } catch (SQLException ex) {
                Logger.getLogger(DlgKompilasiBerkasKlaimBPJS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(cekpdfberkasdigital==0){
            
        }
        
    }
    
    private void btntriasepdf(){
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            i=0;
            if(Sequel.cariInteger("select count(data_triase_igddetail_skala1.no_rawat) from data_triase_igddetail_skala1 where data_triase_igddetail_skala1.no_rawat='"+lblNoRawatKompi1.getText()+"'")>0){
                Map<String, Object> param = new HashMap<>(); 
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());   
                param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                try {
                    ps=koneksi.prepareStatement(
                        "select data_triase_igdprimer.keluhan_utama,data_triase_igdprimer.kebutuhan_khusus,data_triase_igdprimer.catatan,"+
                        "data_triase_igdprimer.plan,data_triase_igdprimer.tanggaltriase,data_triase_igdprimer.nik,data_triase_igd.tekanan_darah,"+
                        "data_triase_igd.nadi,data_triase_igd.pernapasan,data_triase_igd.suhu,data_triase_igd.saturasi_o2,data_triase_igd.nyeri,"+
                        "data_triase_igd.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,pegawai.nama,data_triase_igd.tgl_kunjungan, "+
                        "data_triase_igd.cara_masuk,master_triase_macam_kasus.macam_kasus from data_triase_igdprimer inner join data_triase_igd "+
                        "inner join pasien inner join pegawai inner join reg_periksa inner join master_triase_macam_kasus on "+
                        "data_triase_igd.no_rawat=data_triase_igdprimer.no_rawat and reg_periksa.no_rawat=data_triase_igd.no_rawat "+
                        "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis and pegawai.nik=data_triase_igdprimer.nik "+
                        "and master_triase_macam_kasus.kode_kasus=data_triase_igd.kode_kasus where data_triase_igd.no_rawat=?");
                    try {
                        ps.setString(1,lblNoRawatKompi1.getText());
                        rs=ps.executeQuery();
                        if(rs.next()){
                            param.put("norawat",rs.getString("no_rawat"));
                            param.put("norm",rs.getString("no_rkm_medis"));
                            param.put("namapasien",rs.getString("nm_pasien"));
                            param.put("tanggallahir",rs.getDate("tgl_lahir"));
                            param.put("jk",rs.getString("jk").replaceAll("L","Laki-Laki").replaceAll("P","Perempuan"));
                            param.put("tanggalkunjungan",rs.getDate("tgl_kunjungan"));
                            param.put("jamkunjungan",rs.getString("tgl_kunjungan").toString().substring(11,19));
                            param.put("caradatang",rs.getString("cara_masuk"));
                            param.put("macamkasus",rs.getString("macam_kasus"));
                            param.put("keluhanutama",rs.getString("keluhan_utama"));
                            param.put("kebutuhankhusus",rs.getString("kebutuhan_khusus"));
                            param.put("plan",rs.getString("plan"));
                            param.put("tanggaltriase",rs.getDate("tanggaltriase"));
                            param.put("jamtriase",rs.getString("tanggaltriase").toString().substring(11,19));
                            param.put("pegawai",rs.getString("nama"));
                            param.put("catatan",rs.getString("catatan"));
                            param.put("tandavital","Suhu (C) : "+rs.getString("suhu")+", Nyeri : "+rs.getString("nyeri")+", Tensi : "+rs.getString("tekanan_darah")+", Nadi(/menit) : "+rs.getString("nadi")+", Saturasi O²(%) : "+rs.getString("saturasi_o2")+", Respirasi(/menit) : "+rs.getString("pernapasan"));
                            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("nik"));
                            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nama")+"\nID "+(finger.equals("")?rs.getString("nik"):finger)+"\n"+Valid.SetTgl3(rs.getString("tanggaltriase"))); 
                            ps2=koneksi.prepareStatement(
                                "select master_triase_pemeriksaan.kode_pemeriksaan,master_triase_pemeriksaan.nama_pemeriksaan "+
                                "from master_triase_pemeriksaan inner join master_triase_skala1 inner join data_triase_igddetail_skala1 "+
                                "on master_triase_pemeriksaan.kode_pemeriksaan=master_triase_skala1.kode_pemeriksaan and "+
                                "master_triase_skala1.kode_skala1=data_triase_igddetail_skala1.kode_skala1 where data_triase_igddetail_skala1.no_rawat=? "+
                                "group by master_triase_pemeriksaan.kode_pemeriksaan order by master_triase_pemeriksaan.kode_pemeriksaan");
                            try {
                                Sequel.queryu("delete from temporary where temp37='"+akses.getalamatip()+"'");
                                ps2.setString(1,rs.getString("no_rawat"));
                                rs2=ps2.executeQuery();
                                while(rs2.next()){
                                    datatriase="";
                                    ps3=koneksi.prepareStatement(
                                        "select master_triase_skala1.pengkajian_skala1 from master_triase_skala1 inner join data_triase_igddetail_skala1 "+
                                        "on master_triase_skala1.kode_skala1=data_triase_igddetail_skala1.kode_skala1 where "+
                                        "master_triase_skala1.kode_pemeriksaan=? and data_triase_igddetail_skala1.no_rawat=? "+
                                        "order by data_triase_igddetail_skala1.kode_skala1");
                                    try {
                                        ps3.setString(1,rs2.getString("kode_pemeriksaan"));
                                        ps3.setString(2,rs.getString("no_rawat"));
                                        rs3=ps3.executeQuery();
                                        while(rs3.next()){
                                            datatriase=rs3.getString("pengkajian_skala1")+", "+datatriase;
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Notif : "+e);
                                    } finally{
                                        if(rs3!=null){
                                            rs3.close();
                                        }
                                        if(ps3!=null){
                                            ps3.close();
                                        }
                                    }

                                    if(datatriase.endsWith(", ")){
                                        datatriase = datatriase.substring(0,datatriase.length() - 2);
                                    }
                                    Sequel.menyimpan2("temporary","'"+i+"','"+rs2.getString("nama_pemeriksaan")+"','"+datatriase+"','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','"+akses.getalamatip()+"'","Transaksi");
                                    i++;
                                }
                            } catch (Exception e) {
                                System.out.println("Notif : "+e);
                            } finally{
                                if(rs2!=null){
                                    rs2.close();
                                }
                                if(ps2!=null){
                                    ps2.close();
                                }
                            }
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

//                pilihan = (String)JOptionPane.showInputDialog(null,"Silahkan pilih Lembar/PDF Triase Skala 1..!","Pilihan",JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Lembar Triase Skala 1","PDF Triase Skala 1"},"Lambar Triase Skala 1");
                Valid.MyReportqrypdfKlaim("rptLembarTriaseSkala1.jasper","report","99","select * from temporary where temporary.temp37='"+akses.getalamatip()+"' order by temporary.no",param,"hasilkompilasiklaim", lblNoRawatKompi1.getText().replaceAll("/", ""));
//                Valid.MyReportqry("rptLembarTriaseSkala1.jasper","report","99","select * from temporary where temporary.temp37='"+akses.getalamatip()+"' order by temporary.no",param);
            }else if(Sequel.cariInteger("select count(data_triase_igddetail_skala2.no_rawat) from data_triase_igddetail_skala2 where data_triase_igddetail_skala2.no_rawat='"+lblNoRawatKompi1.getText()+"'")>0){
                Map<String, Object> param = new HashMap<>(); 
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());   
                param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                try {
                    ps=koneksi.prepareStatement(
                        "select data_triase_igdprimer.keluhan_utama,data_triase_igdprimer.kebutuhan_khusus,data_triase_igdprimer.catatan,"+
                        "data_triase_igdprimer.plan,data_triase_igdprimer.tanggaltriase,data_triase_igdprimer.nik,data_triase_igd.tekanan_darah,"+
                        "data_triase_igd.nadi,data_triase_igd.pernapasan,data_triase_igd.suhu,data_triase_igd.saturasi_o2,data_triase_igd.nyeri,"+
                        "data_triase_igd.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,pegawai.nama,data_triase_igd.tgl_kunjungan, "+
                        "data_triase_igd.cara_masuk,master_triase_macam_kasus.macam_kasus from data_triase_igdprimer inner join data_triase_igd "+
                        "inner join pasien inner join pegawai inner join reg_periksa inner join master_triase_macam_kasus on "+
                        "data_triase_igd.no_rawat=data_triase_igdprimer.no_rawat and reg_periksa.no_rawat=data_triase_igd.no_rawat "+
                        "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis and pegawai.nik=data_triase_igdprimer.nik "+
                        "and master_triase_macam_kasus.kode_kasus=data_triase_igd.kode_kasus where data_triase_igd.no_rawat=?");
                    try {
                        ps.setString(1,lblNoRawatKompi1.getText());
                        rs=ps.executeQuery();
                        if(rs.next()){
                            param.put("norawat",rs.getString("no_rawat"));
                            param.put("norm",rs.getString("no_rkm_medis"));
                            param.put("namapasien",rs.getString("nm_pasien"));
                            param.put("tanggallahir",rs.getDate("tgl_lahir"));
                            param.put("jk",rs.getString("jk").replaceAll("L","Laki-Laki").replaceAll("P","Perempuan"));
                            param.put("tanggalkunjungan",rs.getDate("tgl_kunjungan"));
                            param.put("jamkunjungan",rs.getString("tgl_kunjungan").toString().substring(11,19));
                            param.put("caradatang",rs.getString("cara_masuk"));
                            param.put("macamkasus",rs.getString("macam_kasus"));
                            param.put("keluhanutama",rs.getString("keluhan_utama"));
                            param.put("kebutuhankhusus",rs.getString("kebutuhan_khusus"));
                            param.put("plan",rs.getString("plan"));
                            param.put("tanggaltriase",rs.getDate("tanggaltriase"));
                            param.put("tandavital","Suhu (C) : "+rs.getString("suhu")+", Nyeri : "+rs.getString("nyeri")+", Tensi : "+rs.getString("tekanan_darah")+", Nadi(/menit) : "+rs.getString("nadi")+", Saturasi O²(%) : "+rs.getString("saturasi_o2")+", Respirasi(/menit) : "+rs.getString("pernapasan"));
                            param.put("jamtriase",rs.getString("tanggaltriase").toString().substring(11,19));
                            param.put("pegawai",rs.getString("nama"));
                            param.put("catatan",rs.getString("catatan"));
                            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("nik"));
                            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nama")+"\nID "+(finger.equals("")?rs.getString("nik"):finger)+"\n"+Valid.SetTgl3(rs.getString("tanggaltriase"))); 
                            ps2=koneksi.prepareStatement(
                                "select master_triase_pemeriksaan.kode_pemeriksaan,master_triase_pemeriksaan.nama_pemeriksaan "+
                                "from master_triase_pemeriksaan inner join master_triase_skala2 inner join data_triase_igddetail_skala2 "+
                                "on master_triase_pemeriksaan.kode_pemeriksaan=master_triase_skala2.kode_pemeriksaan and "+
                                "master_triase_skala2.kode_skala2=data_triase_igddetail_skala2.kode_skala2 where data_triase_igddetail_skala2.no_rawat=? "+
                                "group by master_triase_pemeriksaan.kode_pemeriksaan order by master_triase_pemeriksaan.kode_pemeriksaan");
                            try {
                                Sequel.queryu("delete from temporary where temp37='"+akses.getalamatip()+"'");
                                ps2.setString(1,rs.getString("no_rawat"));
                                rs2=ps2.executeQuery();
                                while(rs2.next()){
                                    datatriase="";
                                    ps3=koneksi.prepareStatement(
                                        "select master_triase_skala2.pengkajian_skala2 from master_triase_skala2 inner join data_triase_igddetail_skala2 "+
                                        "on master_triase_skala2.kode_skala2=data_triase_igddetail_skala2.kode_skala2 where "+
                                        "master_triase_skala2.kode_pemeriksaan=? and data_triase_igddetail_skala2.no_rawat=? "+
                                        "order by data_triase_igddetail_skala2.kode_skala2");
                                    try {
                                        ps3.setString(1,rs2.getString("kode_pemeriksaan"));
                                        ps3.setString(2,rs.getString("no_rawat"));
                                        rs3=ps3.executeQuery();
                                        while(rs3.next()){
                                            datatriase=rs3.getString("pengkajian_skala2")+", "+datatriase;
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Notif : "+e);
                                    } finally{
                                        if(rs3!=null){
                                            rs3.close();
                                        }
                                        if(ps3!=null){
                                            ps3.close();
                                        }
                                    }

                                    if(datatriase.endsWith(", ")){
                                        datatriase = datatriase.substring(0,datatriase.length() - 2);
                                    }
                                    Sequel.menyimpan2("temporary","'"+i+"','"+rs2.getString("nama_pemeriksaan")+"','"+datatriase+"','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','"+akses.getalamatip()+"'","Transaksi");
                                    i++;
                                }
                            } catch (Exception e) {
                                System.out.println("Notif : "+e);
                            } finally{
                                if(rs2!=null){
                                    rs2.close();
                                }
                                if(ps2!=null){
                                    ps2.close();
                                }
                            }
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

//                pilihan = (String)JOptionPane.showInputDialog(null,"Silahkan pilih Lembar/PDF Triase Skala 2..!","Pilihan",JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Lembar Triase Skala 2","PDF Triase Skala 2"},"Lambar Triase Skala 2");
                Valid.MyReportqrypdfKlaim("rptLembarTriaseSkala2.jasper","report","99","select * from temporary where temporary.temp37='"+akses.getalamatip()+"' order by temporary.no",param,"hasilkompilasiklaim", lblNoRawatKompi1.getText().replaceAll("/", ""));
//                Valid.MyReportqry("rptLembarTriaseSkala2.jasper","report","99","select * from temporary where temporary.temp37='"+akses.getalamatip()+"' order by temporary.no",param);
            }else if(Sequel.cariInteger("select count(data_triase_igddetail_skala3.no_rawat) from data_triase_igddetail_skala3 where data_triase_igddetail_skala3.no_rawat='"+lblNoRawatKompi1.getText()+"'")>0){
                Map<String, Object> param = new HashMap<>(); 
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());   
                param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                try {
                    ps=koneksi.prepareStatement(
                        "select data_triase_igdsekunder.anamnesa_singkat,data_triase_igdsekunder.catatan,"+
                        "data_triase_igdsekunder.plan,data_triase_igdsekunder.tanggaltriase,data_triase_igdsekunder.nik,data_triase_igd.tekanan_darah,"+
                        "data_triase_igd.nadi,data_triase_igd.pernapasan,data_triase_igd.suhu,data_triase_igd.saturasi_o2,data_triase_igd.nyeri,"+
                        "data_triase_igd.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,pegawai.nama,data_triase_igd.tgl_kunjungan, "+
                        "data_triase_igd.cara_masuk,master_triase_macam_kasus.macam_kasus from data_triase_igdsekunder inner join data_triase_igd "+
                        "inner join pasien inner join pegawai inner join reg_periksa inner join master_triase_macam_kasus on "+
                        "data_triase_igd.no_rawat=data_triase_igdsekunder.no_rawat and reg_periksa.no_rawat=data_triase_igd.no_rawat "+
                        "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis and pegawai.nik=data_triase_igdsekunder.nik "+
                        "and master_triase_macam_kasus.kode_kasus=data_triase_igd.kode_kasus where data_triase_igd.no_rawat=?");
                    try {
                        ps.setString(1,lblNoRawatKompi1.getText());
                        rs=ps.executeQuery();
                        if(rs.next()){
                            param.put("norawat",rs.getString("no_rawat"));
                            param.put("norm",rs.getString("no_rkm_medis"));
                            param.put("namapasien",rs.getString("nm_pasien"));
                            param.put("tanggallahir",rs.getDate("tgl_lahir"));
                            param.put("jk",rs.getString("jk").replaceAll("L","Laki-Laki").replaceAll("P","Perempuan"));
                            param.put("tanggalkunjungan",rs.getDate("tgl_kunjungan"));
                            param.put("jamkunjungan",rs.getString("tgl_kunjungan").toString().substring(11,19));
                            param.put("caradatang",rs.getString("cara_masuk"));
                            param.put("macamkasus",rs.getString("macam_kasus"));
                            param.put("keluhanutama",rs.getString("anamnesa_singkat"));
                            param.put("plan",rs.getString("plan"));
                            param.put("tanggaltriase",rs.getDate("tanggaltriase"));
                            param.put("tandavital","Suhu (C) : "+rs.getString("suhu")+", Nyeri : "+rs.getString("nyeri")+", Tensi : "+rs.getString("tekanan_darah")+", Nadi(/menit) : "+rs.getString("nadi")+", Saturasi O²(%) : "+rs.getString("saturasi_o2")+", Respirasi(/menit) : "+rs.getString("pernapasan"));
                            param.put("jamtriase",rs.getString("tanggaltriase").toString().substring(11,19));
                            param.put("pegawai",rs.getString("nama"));
                            param.put("catatan",rs.getString("catatan"));
                            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("nik"));
                            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nama")+"\nID "+(finger.equals("")?rs.getString("nik"):finger)+"\n"+Valid.SetTgl3(rs.getString("tanggaltriase"))); 
                            ps2=koneksi.prepareStatement(
                                "select master_triase_pemeriksaan.kode_pemeriksaan,master_triase_pemeriksaan.nama_pemeriksaan "+
                                "from master_triase_pemeriksaan inner join master_triase_skala3 inner join data_triase_igddetail_skala3 "+
                                "on master_triase_pemeriksaan.kode_pemeriksaan=master_triase_skala3.kode_pemeriksaan and "+
                                "master_triase_skala3.kode_skala3=data_triase_igddetail_skala3.kode_skala3 where data_triase_igddetail_skala3.no_rawat=? "+
                                "group by master_triase_pemeriksaan.kode_pemeriksaan order by master_triase_pemeriksaan.kode_pemeriksaan");
                            try {
                                Sequel.queryu("delete from temporary where temp37='"+akses.getalamatip()+"'");
                                ps2.setString(1,rs.getString("no_rawat"));
                                rs2=ps2.executeQuery();
                                while(rs2.next()){
                                    datatriase="";
                                    ps3=koneksi.prepareStatement(
                                        "select master_triase_skala3.pengkajian_skala3 from master_triase_skala3 inner join data_triase_igddetail_skala3 "+
                                        "on master_triase_skala3.kode_skala3=data_triase_igddetail_skala3.kode_skala3 where "+
                                        "master_triase_skala3.kode_pemeriksaan=? and data_triase_igddetail_skala3.no_rawat=? "+
                                        "order by data_triase_igddetail_skala3.kode_skala3");
                                    try {
                                        ps3.setString(1,rs2.getString("kode_pemeriksaan"));
                                        ps3.setString(2,rs.getString("no_rawat"));
                                        rs3=ps3.executeQuery();
                                        while(rs3.next()){
                                            datatriase=rs3.getString("pengkajian_skala3")+", "+datatriase;
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Notif : "+e);
                                    } finally{
                                        if(rs3!=null){
                                            rs3.close();
                                        }
                                        if(ps3!=null){
                                            ps3.close();
                                        }
                                    }

                                    if(datatriase.endsWith(", ")){
                                        datatriase = datatriase.substring(0,datatriase.length() - 2);
                                    }
                                    Sequel.menyimpan2("temporary","'"+i+"','"+rs2.getString("nama_pemeriksaan")+"','"+datatriase+"','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','"+akses.getalamatip()+"'","Transaksi");
                                    i++;
                                }
                            } catch (Exception e) {
                                System.out.println("Notif : "+e);
                            } finally{
                                if(rs2!=null){
                                    rs2.close();
                                }
                                if(ps2!=null){
                                    ps2.close();
                                }
                            }
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

//                pilihan = (String)JOptionPane.showInputDialog(null,"Silahkan pilih Lembar/PDF Triase Skala 3..!","Pilihan",JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Lembar Triase Skala 3","PDF Triase Skala 3"},"Lembar Triase Skala 3");
                Valid.MyReportqrypdfKlaim("rptLembarTriaseSkala3.jasper","report","99","select * from temporary where temporary.temp37='"+akses.getalamatip()+"' order by temporary.no",param,"hasilkompilasiklaim", lblNoRawatKompi1.getText().replaceAll("/", ""));
//                Valid.MyReportqry("rptLembarTriaseSkala3.jasper","report","99","select * from temporary where temporary.temp37='"+akses.getalamatip()+"' order by temporary.no",param);
            }else if(Sequel.cariInteger("select count(data_triase_igddetail_skala4.no_rawat) from data_triase_igddetail_skala4 where data_triase_igddetail_skala4.no_rawat='"+lblNoRawatKompi1.getText()+"'")>0){
                Map<String, Object> param = new HashMap<>(); 
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());   
                param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                try {
                    ps=koneksi.prepareStatement(
                        "select data_triase_igdsekunder.anamnesa_singkat,data_triase_igdsekunder.catatan,"+
                        "data_triase_igdsekunder.plan,data_triase_igdsekunder.tanggaltriase,data_triase_igdsekunder.nik,data_triase_igd.tekanan_darah,"+
                        "data_triase_igd.nadi,data_triase_igd.pernapasan,data_triase_igd.suhu,data_triase_igd.saturasi_o2,data_triase_igd.nyeri,"+
                        "data_triase_igd.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,pegawai.nama,data_triase_igd.tgl_kunjungan, "+
                        "data_triase_igd.cara_masuk,master_triase_macam_kasus.macam_kasus from data_triase_igdsekunder inner join data_triase_igd "+
                        "inner join pasien inner join pegawai inner join reg_periksa inner join master_triase_macam_kasus on "+
                        "data_triase_igd.no_rawat=data_triase_igdsekunder.no_rawat and reg_periksa.no_rawat=data_triase_igd.no_rawat "+
                        "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis and pegawai.nik=data_triase_igdsekunder.nik "+
                        "and master_triase_macam_kasus.kode_kasus=data_triase_igd.kode_kasus where data_triase_igd.no_rawat=?");
                    try {
                        ps.setString(1,lblNoRawatKompi1.getText());
                        rs=ps.executeQuery();
                        if(rs.next()){
                            param.put("norawat",rs.getString("no_rawat"));
                            param.put("norm",rs.getString("no_rkm_medis"));
                            param.put("namapasien",rs.getString("nm_pasien"));
                            param.put("tanggallahir",rs.getDate("tgl_lahir"));
                            param.put("jk",rs.getString("jk").replaceAll("L","Laki-Laki").replaceAll("P","Perempuan"));
                            param.put("tanggalkunjungan",rs.getDate("tgl_kunjungan"));
                            param.put("jamkunjungan",rs.getString("tgl_kunjungan").toString().substring(11,19));
                            param.put("caradatang",rs.getString("cara_masuk"));
                            param.put("macamkasus",rs.getString("macam_kasus"));
                            param.put("keluhanutama",rs.getString("anamnesa_singkat"));
                            param.put("plan",rs.getString("plan"));
                            param.put("tanggaltriase",rs.getDate("tanggaltriase"));
                            param.put("tandavital","Suhu (C) : "+rs.getString("suhu")+", Nyeri : "+rs.getString("nyeri")+", Tensi : "+rs.getString("tekanan_darah")+", Nadi(/menit) : "+rs.getString("nadi")+", Saturasi O²(%) : "+rs.getString("saturasi_o2")+", Respirasi(/menit) : "+rs.getString("pernapasan"));
                            param.put("jamtriase",rs.getString("tanggaltriase").toString().substring(11,19));
                            param.put("pegawai",rs.getString("nama"));
                            param.put("catatan",rs.getString("catatan"));
                            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("nik"));
                            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nama")+"\nID "+(finger.equals("")?rs.getString("nik"):finger)+"\n"+Valid.SetTgl3(rs.getString("tanggaltriase"))); 
                            ps2=koneksi.prepareStatement(
                                "select master_triase_pemeriksaan.kode_pemeriksaan,master_triase_pemeriksaan.nama_pemeriksaan "+
                                "from master_triase_pemeriksaan inner join master_triase_skala4 inner join data_triase_igddetail_skala4 "+
                                "on master_triase_pemeriksaan.kode_pemeriksaan=master_triase_skala4.kode_pemeriksaan and "+
                                "master_triase_skala4.kode_skala4=data_triase_igddetail_skala4.kode_skala4 where data_triase_igddetail_skala4.no_rawat=? "+
                                "group by master_triase_pemeriksaan.kode_pemeriksaan order by master_triase_pemeriksaan.kode_pemeriksaan");
                            try {
                                Sequel.queryu("delete from temporary where temp37='"+akses.getalamatip()+"'");
                                ps2.setString(1,rs.getString("no_rawat"));
                                rs2=ps2.executeQuery();
                                while(rs2.next()){
                                    datatriase="";
                                    ps3=koneksi.prepareStatement(
                                        "select master_triase_skala4.pengkajian_skala4 from master_triase_skala4 inner join data_triase_igddetail_skala4 "+
                                        "on master_triase_skala4.kode_skala4=data_triase_igddetail_skala4.kode_skala4 where "+
                                        "master_triase_skala4.kode_pemeriksaan=? and data_triase_igddetail_skala4.no_rawat=? "+
                                        "order by data_triase_igddetail_skala4.kode_skala4");
                                    try {
                                        ps3.setString(1,rs2.getString("kode_pemeriksaan"));
                                        ps3.setString(2,rs.getString("no_rawat"));
                                        rs3=ps3.executeQuery();
                                        while(rs3.next()){
                                            datatriase=rs3.getString("pengkajian_skala4")+", "+datatriase;
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Notif : "+e);
                                    } finally{
                                        if(rs3!=null){
                                            rs3.close();
                                        }
                                        if(ps3!=null){
                                            ps3.close();
                                        }
                                    }

                                    if(datatriase.endsWith(", ")){
                                        datatriase = datatriase.substring(0,datatriase.length() - 2);
                                    }
                                    Sequel.menyimpan2("temporary","'"+i+"','"+rs2.getString("nama_pemeriksaan")+"','"+datatriase+"','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','"+akses.getalamatip()+"'","Transaksi");
                                    i++;
                                }
                            } catch (Exception e) {
                                System.out.println("Notif : "+e);
                            } finally{
                                if(rs2!=null){
                                    rs2.close();
                                }
                                if(ps2!=null){
                                    ps2.close();
                                }
                            }
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

//                pilihan = (String)JOptionPane.showInputDialog(null,"Silahkan pilih Lembar/PDF Triase Skala 4..!","Pilihan",JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Lembar Triase Skala 4","PDF Triase Skala 4"},"Lembar Triase Skala 4");
                Valid.MyReportqrypdfKlaim("rptLembarTriaseSkala4.jasper","report","99","select * from temporary where temporary.temp37='"+akses.getalamatip()+"' order by temporary.no",param,"hasilkompilasiklaim", lblNoRawatKompi1.getText().replaceAll("/", ""));
//                Valid.MyReportqry("rptLembarTriaseSkala4.jasper","report","99","select * from temporary where temporary.temp37='"+akses.getalamatip()+"' order by temporary.no",param);
            }else if(Sequel.cariInteger("select count(data_triase_igddetail_skala5.no_rawat) from data_triase_igddetail_skala5 where data_triase_igddetail_skala5.no_rawat='"+lblNoRawatKompi1.getText()+"'")>0){
                Map<String, Object> param = new HashMap<>(); 
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());   
                param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                try {
                    ps=koneksi.prepareStatement(
                        "select data_triase_igdsekunder.anamnesa_singkat,data_triase_igdsekunder.catatan,"+
                        "data_triase_igdsekunder.plan,data_triase_igdsekunder.tanggaltriase,data_triase_igdsekunder.nik,data_triase_igd.tekanan_darah,"+
                        "data_triase_igd.nadi,data_triase_igd.pernapasan,data_triase_igd.suhu,data_triase_igd.saturasi_o2,data_triase_igd.nyeri,"+
                        "data_triase_igd.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,pegawai.nama,data_triase_igd.tgl_kunjungan, "+
                        "data_triase_igd.cara_masuk,master_triase_macam_kasus.macam_kasus from data_triase_igdsekunder inner join data_triase_igd "+
                        "inner join pasien inner join pegawai inner join reg_periksa inner join master_triase_macam_kasus on "+
                        "data_triase_igd.no_rawat=data_triase_igdsekunder.no_rawat and reg_periksa.no_rawat=data_triase_igd.no_rawat "+
                        "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis and pegawai.nik=data_triase_igdsekunder.nik "+
                        "and master_triase_macam_kasus.kode_kasus=data_triase_igd.kode_kasus where data_triase_igd.no_rawat=?");
                    try {
                        ps.setString(1,lblNoRawatKompi1.getText());
                        rs=ps.executeQuery();
                        if(rs.next()){
                            param.put("norawat",rs.getString("no_rawat"));
                            param.put("norm",rs.getString("no_rkm_medis"));
                            param.put("namapasien",rs.getString("nm_pasien"));
                            param.put("tanggallahir",rs.getDate("tgl_lahir"));
                            param.put("jk",rs.getString("jk").replaceAll("L","Laki-Laki").replaceAll("P","Perempuan"));
                            param.put("tanggalkunjungan",rs.getDate("tgl_kunjungan"));
                            param.put("jamkunjungan",rs.getString("tgl_kunjungan").toString().substring(11,19));
                            param.put("caradatang",rs.getString("cara_masuk"));
                            param.put("macamkasus",rs.getString("macam_kasus"));
                            param.put("keluhanutama",rs.getString("anamnesa_singkat"));
                            param.put("plan",rs.getString("plan"));
                            param.put("tanggaltriase",rs.getDate("tanggaltriase"));
                            param.put("tandavital","Suhu (C) : "+rs.getString("suhu")+", Nyeri : "+rs.getString("nyeri")+", Tensi : "+rs.getString("tekanan_darah")+", Nadi(/menit) : "+rs.getString("nadi")+", Saturasi O²(%) : "+rs.getString("saturasi_o2")+", Respirasi(/menit) : "+rs.getString("pernapasan"));
                            param.put("jamtriase",rs.getString("tanggaltriase").toString().substring(11,19));
                            param.put("pegawai",rs.getString("nama"));
                            param.put("catatan",rs.getString("catatan"));
                            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("nik"));
                            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nama")+"\nID "+(finger.equals("")?rs.getString("nik"):finger)+"\n"+Valid.SetTgl3(rs.getString("tanggaltriase"))); 
                            ps2=koneksi.prepareStatement(
                                "select master_triase_pemeriksaan.kode_pemeriksaan,master_triase_pemeriksaan.nama_pemeriksaan "+
                                "from master_triase_pemeriksaan inner join master_triase_skala5 inner join data_triase_igddetail_skala5 "+
                                "on master_triase_pemeriksaan.kode_pemeriksaan=master_triase_skala5.kode_pemeriksaan and "+
                                "master_triase_skala5.kode_skala5=data_triase_igddetail_skala5.kode_skala5 where data_triase_igddetail_skala5.no_rawat=? "+
                                "group by master_triase_pemeriksaan.kode_pemeriksaan order by master_triase_pemeriksaan.kode_pemeriksaan");
                            try {
                                Sequel.queryu("delete from temporary where temp37='"+akses.getalamatip()+"'");
                                ps2.setString(1,rs.getString("no_rawat"));
                                rs2=ps2.executeQuery();
                                while(rs2.next()){
                                    datatriase="";
                                    ps3=koneksi.prepareStatement(
                                        "select master_triase_skala5.pengkajian_skala5 from master_triase_skala5 inner join data_triase_igddetail_skala5 "+
                                        "on master_triase_skala5.kode_skala5=data_triase_igddetail_skala5.kode_skala5 where "+
                                        "master_triase_skala5.kode_pemeriksaan=? and data_triase_igddetail_skala5.no_rawat=? "+
                                        "order by data_triase_igddetail_skala5.kode_skala5");
                                    try {
                                        ps3.setString(1,rs2.getString("kode_pemeriksaan"));
                                        ps3.setString(2,rs.getString("no_rawat"));
                                        rs3=ps3.executeQuery();
                                        while(rs3.next()){
                                            datatriase=rs3.getString("pengkajian_skala5")+", "+datatriase;
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Notif : "+e);
                                    } finally{
                                        if(rs3!=null){
                                            rs3.close();
                                        }
                                        if(ps3!=null){
                                            ps3.close();
                                        }
                                    }

                                    if(datatriase.endsWith(", ")){
                                        datatriase = datatriase.substring(0,datatriase.length() - 2);
                                    }
                                    Sequel.menyimpan2("temporary","'"+i+"','"+rs2.getString("nama_pemeriksaan")+"','"+datatriase+"','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','"+akses.getalamatip()+"'","Transaksi");
                                    i++;
                                }
                            } catch (Exception e) {
                                System.out.println("Notif : "+e);
                            } finally{
                                if(rs2!=null){
                                    rs2.close();
                                }
                                if(ps2!=null){
                                    ps2.close();
                                }
                            }
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

//                pilihan = (String)JOptionPane.showInputDialog(null,"Silahkan pilih Lembar/PDF Triase Skala 5..!","Pilihan",JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Lembar Triase Skala 5","PDF Triase Skala 5"},"Lembar Triase Skala 5");
                Valid.MyReportqrypdfKlaim("rptLembarTriaseSkala5.jasper","report","99","select * from temporary where temporary.temp37='"+akses.getalamatip()+"' order by temporary.no",param,"hasilkompilasiklaim", lblNoRawatKompi1.getText().replaceAll("/", ""));
//                Valid.MyReportqry("rptLembarTriaseSkala5.jasper","report","99","select * from temporary where temporary.temp37='"+akses.getalamatip()+"' order by temporary.no",param);
            }
            this.setCursor(Cursor.getDefaultCursor());
    }

    private void updatediagnosaresumeralan(){
        try {
            ps=koneksi.prepareStatement(
                    "select diagnosa_pasien.kd_penyakit,penyakit.nm_penyakit,diagnosa_pasien.prioritas "+
                    "from diagnosa_pasien inner join penyakit on diagnosa_pasien.kd_penyakit=penyakit.kd_penyakit "+
                    "where diagnosa_pasien.no_rawat=? order by diagnosa_pasien.prioritas ");
            try {
                ps.setString(1,lblNoRawatKompi1.getText());
                rs=ps.executeQuery();
                while(rs.next()){
                    if(rs.getInt("prioritas")==1){
//                        KodeDiagnosaUtama.setText(rs.getString("kd_penyakit"));
//                        DiagnosaUtama.setText(rs.getString("nm_penyakit"));
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_utama='"+rs.getString("nm_penyakit")+"',kd_diagnosa_utama='"+rs.getString("kd_penyakit")+"'");
                    }
                    
                    if(rs.getInt("prioritas")==2){
//                        KodeDiagnosaSekunder1.setText(rs.getString("kd_penyakit"));
//                        DiagnosaSekunder1.setText(rs.getString("nm_penyakit"));
//                        Sequel.mengedit("resume_pasien","no_rawat=?","diagnosa_sekunder=?,kd_diagnosa_sekunder=?",3,new String[]{lblNoRawat.getText(),rs.getString("nm_penyakit"),rs.getString("kd_penyakit")});
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder='"+rs.getString("nm_penyakit")+"',kd_diagnosa_sekunder='"+rs.getString("kd_penyakit")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder='',kd_diagnosa_sekunder=''");
                    }
                    
                    if(rs.getInt("prioritas")==3){
//                        KodeDiagnosaSekunder2.setText(rs.getString("kd_penyakit"));
//                        DiagnosaSekunder2.setText(rs.getString("nm_penyakit"));
//                        Sequel.mengedit("resume_pasien","no_rawat=?","diagnosa_sekunder2=?,kd_diagnosa_sekunder2=?",3,new String[]{lblNoRawat.getText(),rs.getString("nm_penyakit"),rs.getString("kd_penyakit")});
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder2='"+rs.getString("nm_penyakit")+"',kd_diagnosa_sekunder2='"+rs.getString("kd_penyakit")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder2='',kd_diagnosa_sekunder2=''");
                    }
                    
                    if(rs.getInt("prioritas")==4){
//                        KodeDiagnosaSekunder3.setText(rs.getString("kd_penyakit"));
//                        DiagnosaSekunder3.setText(rs.getString("nm_penyakit"));
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder3='"+rs.getString("nm_penyakit")+"',kd_diagnosa_sekunder2='"+rs.getString("kd_penyakit")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder3='',kd_diagnosa_sekunder3=''");
                    }
                    
                    if(rs.getInt("prioritas")==5){
//                        KodeDiagnosaSekunder4.setText(rs.getString("kd_penyakit"));
//                        DiagnosaSekunder4.setText(rs.getString("nm_penyakit"));
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","kd_diagnosa_sekunder4='"+rs.getString("nm_penyakit")+"',kd_diagnosa_sekunder4='"+rs.getString("kd_penyakit")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder4='',kd_diagnosa_sekunder4=''");
                    }
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
        
        try {
            ps=koneksi.prepareStatement(
                    "select prosedur_pasien.kode,icd9.deskripsi_panjang, prosedur_pasien.prioritas "+
                    "from prosedur_pasien inner join icd9 on prosedur_pasien.kode=icd9.kode "+
                    "where prosedur_pasien.no_rawat=? order by prosedur_pasien.prioritas ");
            try {
                ps.setString(1,lblNoRawatKompi1.getText());
                rs=ps.executeQuery();
                while(rs.next()){
                    if(rs.getInt("prioritas")==1){
//                        KodeProsedurUtama.setText(rs.getString("kode"));
//                        ProsedurUtama.setText(rs.getString("deskripsi_panjang"));
//                        Sequel.mengedit("resume_pasien","no_rawat=?","prosedur_utama=?,kd_prosedur_utama=?",3,new String[]{lblNoRawat.getText(),rs.getString("deskripsi_panjang"),rs.getString("kode")});
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_utama='"+rs.getString("deskripsi_panjang")+"',kd_prosedur_utama='"+rs.getString("kode")+"'");
                    }
                    
                    if(rs.getInt("prioritas")==2){
//                        KodeProsedurSekunder1.setText(rs.getString("kode"));
//                        ProsedurSekunder1.setText(rs.getString("deskripsi_panjang"));
//                        Sequel.mengedit("resume_pasien","no_rawat=?","prosedur_sekunder=?,kd_prosedur_sekunder=?",3,new String[]{lblNoRawat.getText(),rs.getString("deskripsi_panjang"),rs.getString("kode")});
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_sekunder='"+rs.getString("deskripsi_panjang")+"',kd_prosedur_sekunder='"+rs.getString("kode")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_sekunder='',kd_prosedur_sekunder=''");
                    }
                    
                    if(rs.getInt("prioritas")==3){
//                        KodeProsedurSekunder2.setText(rs.getString("kode"));
//                        ProsedurSekunder2.setText(rs.getString("deskripsi_panjang"));
//                        Sequel.mengedit("resume_pasien","no_rawat=?","prosedur_sekunder2=?,kd_prosedur_sekunder2=?",3,new String[]{lblNoRawat.getText(),rs.getString("deskripsi_panjang"),rs.getString("kode")});
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_sekunder2='"+rs.getString("deskripsi_panjang")+"',kd_prosedur_sekunder2='"+rs.getString("kode")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_sekunder2='',kd_prosedur_sekunder2=''");
                    }
                    
                    if(rs.getInt("prioritas")==4){
//                        KodeProsedurSekunder3.setText(rs.getString("kode"));
//                        ProsedurSekunder3.setText(rs.getString("deskripsi_panjang"));
//                        Sequel.mengedit("resume_pasien","no_rawat=?","prosedur_sekunder3=?,kd_prosedur_sekunder3=?",3,new String[]{lblNoRawat.getText(),rs.getString("deskripsi_panjang"),rs.getString("kode")});
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_sekunder3='"+rs.getString("deskripsi_panjang")+"',kd_prosedur_sekunder3='"+rs.getString("kode")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_sekunder3='',kd_prosedur_sekunder3=''");
                    }
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
    
    private void updatediagnosaresumeranap(){
        try {
            ps=koneksi.prepareStatement(
                    "select diagnosa_pasien.kd_penyakit,penyakit.nm_penyakit,diagnosa_pasien.prioritas "+
                    "from diagnosa_pasien inner join penyakit on diagnosa_pasien.kd_penyakit=penyakit.kd_penyakit "+
                    "where diagnosa_pasien.no_rawat=? order by diagnosa_pasien.prioritas ");
            try {
                ps.setString(1,lblNoRawatKompi1.getText());
                rs=ps.executeQuery();
                while(rs.next()){
                    if(rs.getInt("prioritas")==1){
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_utama='"+rs.getString("nm_penyakit")+"',kd_diagnosa_utama='"+rs.getString("kd_penyakit")+"'");
                    }
                    
                    if(rs.getInt("prioritas")==2){
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder='"+rs.getString("nm_penyakit")+"',kd_diagnosa_sekunder='"+rs.getString("kd_penyakit")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder='',kd_diagnosa_sekunder=''");
                    }
                    
                    if(rs.getInt("prioritas")==3){
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder2='"+rs.getString("nm_penyakit")+"',kd_diagnosa_sekunder2='"+rs.getString("kd_penyakit")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder2='',kd_diagnosa_sekunder2=''");
                    }
                    
                    if(rs.getInt("prioritas")==4){
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder3='"+rs.getString("nm_penyakit")+"',kd_diagnosa_sekunder2='"+rs.getString("kd_penyakit")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder3='',kd_diagnosa_sekunder3=''");
                    }
                    
                    if(rs.getInt("prioritas")==5){
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","kd_diagnosa_sekunder4='"+rs.getString("nm_penyakit")+"',kd_diagnosa_sekunder4='"+rs.getString("kd_penyakit")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","diagnosa_sekunder4='',kd_diagnosa_sekunder4=''");
                    }
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
        
        try {
            ps=koneksi.prepareStatement(
                    "select prosedur_pasien.kode,icd9.deskripsi_panjang, prosedur_pasien.prioritas "+
                    "from prosedur_pasien inner join icd9 on prosedur_pasien.kode=icd9.kode "+
                    "where prosedur_pasien.no_rawat=? order by prosedur_pasien.prioritas ");
            try {
                ps.setString(1,lblNoRawatKompi1.getText());
                rs=ps.executeQuery();
                while(rs.next()){
                    if(rs.getInt("prioritas")==1){
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_utama='"+rs.getString("deskripsi_panjang")+"',kd_prosedur_utama='"+rs.getString("kode")+"'");
                    }
                    
                    if(rs.getInt("prioritas")==2){
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_sekunder='"+rs.getString("deskripsi_panjang")+"',kd_prosedur_sekunder='"+rs.getString("kode")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_sekunder='',kd_prosedur_sekunder=''");
                    }
                    
                    if(rs.getInt("prioritas")==3){
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_sekunder2='"+rs.getString("deskripsi_panjang")+"',kd_prosedur_sekunder2='"+rs.getString("kode")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_sekunder2='',kd_prosedur_sekunder2=''");
                    }
                    
                    if(rs.getInt("prioritas")==4){
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_sekunder3='"+rs.getString("deskripsi_panjang")+"',kd_prosedur_sekunder3='"+rs.getString("kode")+"'");
                    }else{
                        Sequel.mengedit("resume_pasien_ranap","no_rawat='"+lblNoRawatKompi1.getText()+"'","prosedur_sekunder3='',kd_prosedur_sekunder3=''");
                    }
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
}
