/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgPasienMati.java
 *
 * Created on Aug 30, 2010, 7:46:01 AM
 */

package bridging;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
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
import kepegawaian.DlgCariDokter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.web.util.HtmlUtils;
import rekammedis.RMRiwayatPerawatan;
import simrskhanza.DlgCariCaraBayar;
import simrskhanza.DlgCariPeriksaLab;
import simrskhanza.DlgCariPeriksaRadiologi;
import simrskhanza.DlgKompilasiBerkasKlaimBPJS;
import static simrskhanza.DlgKompilasiBerkasKlaimBPJS.deleteFilesInFolder;
import static simrskhanza.DlgKompilasiBerkasKlaimBPJS.mergePDFsFromFolder;
import simrskhanza.DlgPasien;
import surat.SuratKontrol;

/**
 *
 * @author dosen3
 */
public class INACBGData extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private DlgPasien pasien=new DlgPasien(null,false);
    private PreparedStatement ps,pskoders,ps2,ps3, psambiltindakan, psambillab, psambilrad;
    private ResultSet rs,rskode,rs2,rs3,rsdiagnosa, rsprosedur,rsambiltindakan, rsambillab, rsambilrad;
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private String tgl, finger="", lama="",payor_id="",payor_cd="",coder="",dokterkonsul="",tgl_masuk_ranap="",
            tgl_keluar_ranap="",dpjp_ranap="",inacbg_diagnosa="",prosedur_inacbg="",tanggal="",datatriase="",pilihan="";
    private String[] part;
    public  ApiINACBG inacbg = new ApiINACBG();
    private RMRiwayatPerawatan resume = null;
    private WebEngine engine;
    private final DlgCariCaraBayar penjab = new DlgCariCaraBayar(null, false);
    private int total_biaya = 0,i=0,j=0;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter_tanggal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final JFXPanel jfxPanelicare = new JFXPanel();
    private final JFXPanel jfxinvoices = new JFXPanel();
    private final String KOMPILASIBERKASGUNAKANRIWAYATPASIEN = koneksiDB.KOMPILASIBERKASGUNAKANRIWAYATPASIEN(),
                         KODEPPKBPJS = Sequel.cariIsiSmc("select setting.kode_ppk from setting limit 1") + "%";
    private String tanggalExport = "",
                   KOMPILASIBERKASGUNAKANTANGGALEXPORT = koneksiDB.KOMPILASIBERKASGUNAKANTANGGALEXPORT(),
                   KOMPILASIBERKASAPLIKASIPDF = koneksiDB.KOMPILASIBERKASAPLIKASIPDF();
    private boolean exportSukses = true;
    private long KOMPILASIBERKASMAXMEMORY = koneksiDB.KOMPILASIBERKASMAXMEMORY();


    
    /** Creates new form DlgPasienMati
     * @param parent
     * @param modal */
    public INACBGData(java.awt.Frame parent, boolean modal) {
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
    
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        no_rawat = new widget.TextBox();
        icd10 = new widget.TextBox();
        icd9 = new widget.TextBox();
        WindowPengaturan = new javax.swing.JDialog();
        internalFrame12 = new widget.InternalFrame();
        BtnTutupPengaturan = new widget.Button();
        BtnBukaFolderExport = new widget.Button();
        CmbPilihanAplikasiPDF = new widget.ComboBox();
        jLabel14 = new widget.Label();
        TPathAplikasiPDF = new widget.TextBox();
        jLabel56 = new widget.Label();
        CmbPilihanTanggalExport = new widget.ComboBox();
        BtnSimpanPengaturan = new widget.Button();
        BtnPilihAplikasiPDF = new widget.Button();
        BtnResetPengaturan = new widget.Button();
        jLabel16 = new widget.Label();
        TMaxMemory = new widget.TextBox();
        jLabel17 = new widget.Label();
        jLabel24 = new widget.Label();
        fc = new javax.swing.JFileChooser();
        kirimonline1 = new widget.Button();
        TabRawat = new widget.TabPane();
        internalFrame1 = new widget.InternalFrame();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.panelisi();
        TPasien = new widget.TextBox();
        tgl_masuk = new widget.Tanggal();
        TNoRM = new widget.TextBox();
        jLabel12 = new widget.Label();
        jLabel15 = new widget.Label();
        pnb = new widget.TextBox();
        ahli = new widget.TextBox();
        no_kartu = new widget.TextBox();
        cara_masuk = new widget.ComboBox();
        sistole = new widget.TextBox();
        jLabel22 = new widget.Label();
        jenis_rawat = new widget.ComboBox();
        cob = new widget.ComboBox();
        jaminan = new widget.ComboBox();
        jLabel6 = new widget.Label();
        jLabel19 = new widget.Label();
        jLabel20 = new widget.Label();
        jLabel21 = new widget.Label();
        jLabel23 = new widget.Label();
        hak_kelas = new widget.ComboBox();
        rehab = new widget.TextBox();
        rad = new widget.TextBox();
        alkes = new widget.TextBox();
        obat = new widget.TextBox();
        pb = new widget.TextBox();
        kpr = new widget.TextBox();
        akomodasi = new widget.TextBox();
        lab = new widget.TextBox();
        bmhp = new widget.TextBox();
        kronis = new widget.TextBox();
        ksl = new widget.TextBox();
        pnj = new widget.TextBox();
        ri = new widget.TextBox();
        pl_darah = new widget.TextBox();
        sewa_alat = new widget.TextBox();
        obat_kemo = new widget.TextBox();
        jLabel26 = new widget.Label();
        jLabel27 = new widget.Label();
        jLabel28 = new widget.Label();
        jLabel29 = new widget.Label();
        jLabel30 = new widget.Label();
        jLabel31 = new widget.Label();
        jLabel32 = new widget.Label();
        jLabel33 = new widget.Label();
        jLabel34 = new widget.Label();
        jLabel35 = new widget.Label();
        jLabel36 = new widget.Label();
        jLabel37 = new widget.Label();
        jLabel38 = new widget.Label();
        jLabel39 = new widget.Label();
        jLabel40 = new widget.Label();
        jLabel41 = new widget.Label();
        jLabel42 = new widget.Label();
        jLabel43 = new widget.Label();
        jam_masuk = new widget.ComboBox();
        menit_masuk = new widget.ComboBox();
        tgl_keluar = new widget.Tanggal();
        jLabel44 = new widget.Label();
        jam_keluar = new widget.ComboBox();
        menit_keluar = new widget.ComboBox();
        rawat_intensif = new javax.swing.JCheckBox();
        jLabel45 = new widget.Label();
        ventilator = new javax.swing.JCheckBox();
        pasien_tb = new widget.TextBox();
        jLabel46 = new widget.Label();
        naik_turun_kelas = new javax.swing.JCheckBox();
        naik_kelas_pelayanan = new widget.ComboBox();
        jLabel47 = new widget.Label();
        lama_naik_kelas = new widget.TextBox();
        jLabel48 = new widget.Label();
        jLabel51 = new widget.Label();
        jLabel52 = new widget.Label();
        chronic = new widget.TextBox();
        sub_acute = new widget.TextBox();
        jLabel54 = new widget.Label();
        berat_bayi = new widget.TextBox();
        jLabel55 = new widget.Label();
        dpjp = new widget.TextBox();
        jLabel57 = new widget.Label();
        validasi_tb = new widget.Button();
        validasi_cvd19 = new widget.Button();
        co_covid_19 = new widget.TextBox();
        jLabel58 = new widget.Label();
        status_pulang = new widget.ComboBox();
        jLabel59 = new widget.Label();
        kelas_eksekutif = new javax.swing.JCheckBox();
        tgl_intubasi = new widget.Tanggal();
        jam_intubasi = new widget.ComboBox();
        menit_intubasi = new widget.ComboBox();
        total_rupiah = new widget.Label();
        menit_ekstubasi = new widget.ComboBox();
        jam_ekstubasi = new widget.ComboBox();
        tgl_ekstubasi = new widget.Tanggal();
        jLabel62 = new widget.Label();
        hari_intensif = new widget.TextBox();
        jLabel63 = new widget.Label();
        jLabel66 = new widget.Label();
        jLabel67 = new widget.Label();
        distole = new widget.TextBox();
        tarif_eksekutif = new widget.TextBox();
        jLabel13 = new widget.Label();
        pnb1 = new widget.TextBox();
        tgl_lahir = new widget.TextBox();
        jLabel60 = new widget.Label();
        jenis_kelamin = new widget.TextBox();
        jLabel64 = new widget.Label();
        jam_ventilator = new widget.TextBox();
        jLabel65 = new widget.Label();
        jLabel68 = new widget.Label();
        jLabel69 = new widget.Label();
        nm_diag_1 = new widget.TextBox();
        jLabel70 = new widget.Label();
        jLabel71 = new widget.Label();
        kd_diag_1 = new widget.TextBox();
        nm_diag_2 = new widget.TextBox();
        kd_diag_2 = new widget.TextBox();
        nm_diag_3 = new widget.TextBox();
        kd_diag_3 = new widget.TextBox();
        nm_diag_4 = new widget.TextBox();
        kd_diag_4 = new widget.TextBox();
        nm_diag_5 = new widget.TextBox();
        kd_diag_5 = new widget.TextBox();
        nm_psdr_1 = new widget.TextBox();
        kd_psdr_1 = new widget.TextBox();
        nm_psdr_2 = new widget.TextBox();
        kd_psdr_2 = new widget.TextBox();
        nm_psdr_3 = new widget.TextBox();
        kd_psdr_3 = new widget.TextBox();
        nm_psdr_4 = new widget.TextBox();
        kd_psdr_4 = new widget.TextBox();
        nm_psdr_5 = new widget.TextBox();
        kd_psdr_5 = new widget.TextBox();
        chk_diag_5 = new javax.swing.JCheckBox();
        chk_diag_1 = new javax.swing.JCheckBox();
        chk_diag_2 = new javax.swing.JCheckBox();
        chk_diag_3 = new javax.swing.JCheckBox();
        chk_diag_4 = new javax.swing.JCheckBox();
        jLabel72 = new widget.Label();
        jLabel73 = new widget.Label();
        jLabel74 = new widget.Label();
        total_rupiah1 = new widget.Label();
        jLabel75 = new widget.Label();
        jLabel76 = new widget.Label();
        jLabel77 = new widget.Label();
        ap_5 = new widget.TextBox();
        jLabel78 = new widget.Label();
        ap_1 = new widget.TextBox();
        ap_2 = new widget.TextBox();
        ap_3 = new widget.TextBox();
        ap_4 = new widget.TextBox();
        ap_10 = new widget.TextBox();
        ap_6 = new widget.TextBox();
        ap_7 = new widget.TextBox();
        ap_8 = new widget.TextBox();
        ap_9 = new widget.TextBox();
        stage_2 = new widget.ComboBox();
        jLabel49 = new widget.Label();
        Tsep = new widget.TextBox();
        jLabel50 = new widget.Label();
        jLabel53 = new widget.Label();
        panelGlass8 = new widget.panelisi();
        BtnGrouper = new widget.Button();
        finalklaim = new widget.Button();
        editKlaim = new widget.Button();
        kirimonline = new widget.Button();
        hapusKlaim = new widget.Button();
        jLabel25 = new widget.Label();
        BtnKeluar = new widget.Button();
        kirimonline2 = new widget.Button();
        kirimonline3 = new widget.Button();
        kirimonline4 = new widget.Button();
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

        no_rawat.setHighlighter(null);
        no_rawat.setName("no_rawat"); // NOI18N

        icd10.setHighlighter(null);
        icd10.setName("icd10"); // NOI18N

        icd9.setHighlighter(null);
        icd9.setName("icd9"); // NOI18N

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

        jLabel14.setText("Buka PDF dengan :");
        jLabel14.setName("jLabel14"); // NOI18N
        internalFrame12.add(jLabel14);
        jLabel14.setBounds(0, 32, 136, 23);

        TPathAplikasiPDF.setEditable(false);
        TPathAplikasiPDF.setHighlighter(null);
        TPathAplikasiPDF.setName("TPathAplikasiPDF"); // NOI18N
        internalFrame12.add(TPathAplikasiPDF);
        TPathAplikasiPDF.setBounds(274, 32, 294, 23);

        jLabel56.setText("Gunakan Tanggal Export :");
        jLabel56.setName("jLabel56"); // NOI18N
        internalFrame12.add(jLabel56);
        jLabel56.setBounds(0, 62, 136, 23);

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

        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("_____________________________________________________________________________________________________");
        jLabel16.setName("jLabel16"); // NOI18N
        internalFrame12.add(jLabel16);
        jLabel16.setBounds(0, 116, 610, 12);

        TMaxMemory.setHighlighter(null);
        TMaxMemory.setName("TMaxMemory"); // NOI18N
        internalFrame12.add(TMaxMemory);
        TMaxMemory.setBounds(140, 92, 80, 23);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jLabel17.setText("MB");
        jLabel17.setName("jLabel17"); // NOI18N
        internalFrame12.add(jLabel17);
        jLabel17.setBounds(228, 92, 14, 23);

        jLabel24.setText("Max Memory Kompilasi :");
        jLabel24.setName("jLabel24"); // NOI18N
        internalFrame12.add(jLabel24);
        jLabel24.setBounds(0, 92, 136, 23);

        WindowPengaturan.getContentPane().add(internalFrame12, java.awt.BorderLayout.CENTER);

        fc.setCurrentDirectory(null);
        fc.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
        fc.setName("fc"); // NOI18N

        kirimonline1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/2.png"))); // NOI18N
        kirimonline1.setText("Kirim Online");
        kirimonline1.setToolTipText("Alt+S");
        kirimonline1.setGlassColor(new java.awt.Color(0, 255, 153));
        kirimonline1.setName("kirimonline1"); // NOI18N
        kirimonline1.setPreferredSize(new java.awt.Dimension(120, 30));
        kirimonline1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kirimonline1ActionPerformed(evt);
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

        TabRawat.setName("TabRawat"); // NOI18N

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ BRIDGING E-KLAIM ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Fira Sans", 0, 13), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout());

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(1300, 650));

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(1100, 660));
        FormInput.setLayout(null);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        FormInput.add(TPasien);
        TPasien.setBounds(225, 10, 230, 23);

        tgl_masuk.setEditable(false);
        tgl_masuk.setForeground(new java.awt.Color(50, 70, 50));
        tgl_masuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2025-04-25" }));
        tgl_masuk.setDisplayFormat("yyyy-MM-dd");
        tgl_masuk.setName("tgl_masuk"); // NOI18N
        tgl_masuk.setOpaque(false);
        FormInput.add(tgl_masuk);
        tgl_masuk.setBounds(130, 100, 100, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        FormInput.add(TNoRM);
        TNoRM.setBounds(130, 10, 90, 23);

        jLabel12.setText("Distole : ");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(710, 70, 60, 23);

        jLabel15.setText("Intubasi :");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput.add(jLabel15);
        jLabel15.setBounds(580, 190, 70, 23);

        pnb.setHighlighter(null);
        pnb.setName("pnb"); // NOI18N
        pnb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pnbKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pnbKeyTyped(evt);
            }
        });
        FormInput.add(pnb);
        pnb.setBounds(130, 250, 154, 23);

        ahli.setHighlighter(null);
        ahli.setName("ahli"); // NOI18N
        ahli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ahliKeyReleased(evt);
            }
        });
        FormInput.add(ahli);
        ahli.setBounds(130, 280, 154, 23);

        no_kartu.setEditable(false);
        no_kartu.setHighlighter(null);
        no_kartu.setName("no_kartu"); // NOI18N
        FormInput.add(no_kartu);
        no_kartu.setBounds(500, 10, 120, 23);

        cara_masuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "gp : Rujukan FKTP", "hosp-trans : Rujukan FKRTL", "mp : Rujukan Spesialis", "outp : Dari Rawat Jalan", "inp : Dari Rawat Inap", "emd : Dari Rawat Darurat", "born : Lahir di RS", "nursing : Rujukan Panti Jompo", "psych : Rujukan dari RS Jiwa", "rehab : Rujukan Fasilitas Rehab", "other : Lain-lain" }));
        cara_masuk.setName("cara_masuk"); // NOI18N
        FormInput.add(cara_masuk);
        cara_masuk.setBounds(750, 40, 270, 23);

        sistole.setHighlighter(null);
        sistole.setName("sistole"); // NOI18N
        FormInput.add(sistole);
        sistole.setBounds(670, 70, 50, 23);

        jLabel22.setText("Pasien TB : ");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(60, 190, 70, 23);

        jenis_rawat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1 : RAWAT INAP", "2 : RAWAT JALAN", "3 : IGD" }));
        jenis_rawat.setSelectedIndex(1);
        jenis_rawat.setName("jenis_rawat"); // NOI18N
        jenis_rawat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jenis_rawatItemStateChanged(evt);
            }
        });
        FormInput.add(jenis_rawat);
        jenis_rawat.setBounds(130, 70, 150, 23);

        cob.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "# : TIDAK COB", "0001 : MANDIRI INHEALTH", "0005 : ASURANSI SINAR MAS", "0006 : ASURANSI TUGU MANDIRI", "0007 : ASURANSI MITRA MAPARYA", "0008 : ASURANSI AXA MANDIRI FINANSIAL SERVICE", "0009 : ASURANSI AXA FINANSIAL INDONESIA", "0010 : LIPPO GENERAL INSURANCE", "0011 : ARTHAGRAHA GENERAL INSURANSE", "0012 : TUGU PRATAMA INDONESIA", "0013 : ASURANSI BINA DANA ARTA", "0014 : ASURANSI JIWA SINAR MAS MSIG", "0015 : AVRIST ASSURANCE", "0016 : ASURANSI JIWA SRAYA", "0017 : ASURANSI JIWA CENTRAL ASIA RAYA", "0018 : ASURANSI TAKAFUL KELUARGA", "0019 : ASURANSI JIWA GENERALI INDONESIA", "0020 : ASURANSI ASTRA BUANA", "0021 : ASURANSI UMUM MEGA", "0022 : ASURANSI MULTI ARTHA GUNA", "0023 : ASURANSI AIA INDONESIA", "0024 : ASURANSI JIWA EQUITY LIFE INDONESIA", "0025 : ASURANSI JIWA RECAPITAL", "0026 : GREAT EASTERN LIFE INDONESIA", "0027 : ASURANSI ADISARANA WANAARTHA", "0028 : ASURANSI JIWA BRINGIN JIWA SEJAHTERA", "0029 : BOSOWA ASURANSI", "0030 : MNC LIFE ASSURANCE", "0031 : ASURANSI AVIVA INDONESIA", "0032 : ASURANSI CENTRAL ASIA RAYA", "0033 : ASURANSI ALLIANZ LIFE INDONESIA", "0034 : ASURANSI BINTANG", "0035 : TOKIO MARINE LIFE INSURANCE INDONESIA", "0036 : MALACCA TRUST WUWUNGAN", "0037 : ASURANSI JASA INDONESIA", "0038 : ASURANSI JIWA MANULIFE INDONESIA", "0039 : ASURANSI BANGUN ASKRIDA", "0040 : ASURANSI JIWA SEQUIS FINANCIAL", "0041 : ASURANSI AXA INDONESIA", "0042 : BNI LIFE", "0043 : ACE LIFE INSURANCE", "0044 : CITRA INTERNATIONAL UNDERWRITERS", "0045 : ASURANSI RELIANCE INDONESIA", "0046 : HANWHA LIFE INSURANCE INDONESIA", "0047 : ASURANSI DAYIN MITRA", "0048 : ASURANSI ADIRA DINAMIKA", "0049 : PAN PASIFIC INSURANCE", "0050 : ASURANSI SAMSUNG TUGU", "0051 : ASURANSI UMUM BUMI PUTERA MUDA 1967", "0052 : ASURANSI JIWA KRESNA", "0053 : ASURANSI RAMAYANA", "0054 : VICTORIA INSURANCE", "0055 : ASURANSI JIWA BERSAMA BUMIPUTERA 1912", "0056 : FWD LIFE INDONESIA", "0057 : ASURANSI TAKAFUL KELUARGA", "0058 : ASURANSI TUGU KRESNA PRATAMA", "0059 : SOMPO INSURANCE" }));
        cob.setName("cob"); // NOI18N
        FormInput.add(cob);
        cob.setBounds(750, 10, 270, 23);

        jaminan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3 : JKN ", "71 : JAMINAN COVID-19", "72 : JAMINAN KIPI", "73 : JAMINAN BAYI BARU LAHIR", "74 : JAMINAN PERPANJANGAN MASA RAWAT", "75 : JAMINAN CO-INSIDENSE", "76 : JAMPERSAL", "77 : JAMINAN PEMULIHAN KESEHATAN PRIORITAS" }));
        jaminan.setName("jaminan"); // NOI18N
        FormInput.add(jaminan);
        jaminan.setBounds(130, 40, 200, 23);

        jLabel6.setText("No.Rekam Medik :");
        jLabel6.setName("jLabel6"); // NOI18N
        FormInput.add(jLabel6);
        jLabel6.setBounds(10, 10, 115, 23);

        jLabel19.setText("Jaminan :");
        jLabel19.setName("jLabel19"); // NOI18N
        FormInput.add(jLabel19);
        jLabel19.setBounds(10, 40, 115, 23);

        jLabel20.setText("COB :");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(710, 10, 30, 23);

        jLabel21.setText("Kartu : ");
        jLabel21.setName("jLabel21"); // NOI18N
        FormInput.add(jLabel21);
        jLabel21.setBounds(410, 10, 90, 23);

        jLabel23.setText("Tgl Masuk :");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(10, 100, 115, 23);

        hak_kelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Kelas 1", "Kelas 2", "Kelas 3" }));
        hak_kelas.setName("hak_kelas"); // NOI18N
        FormInput.add(hak_kelas);
        hak_kelas.setBounds(890, 70, 130, 23);

        rehab.setHighlighter(null);
        rehab.setName("rehab"); // NOI18N
        rehab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rehabKeyReleased(evt);
            }
        });
        FormInput.add(rehab);
        rehab.setBounds(130, 340, 154, 23);

        rad.setHighlighter(null);
        rad.setName("rad"); // NOI18N
        rad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                radKeyReleased(evt);
            }
        });
        FormInput.add(rad);
        rad.setBounds(130, 310, 154, 23);

        alkes.setHighlighter(null);
        alkes.setName("alkes"); // NOI18N
        alkes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                alkesKeyReleased(evt);
            }
        });
        FormInput.add(alkes);
        alkes.setBounds(130, 400, 154, 23);

        obat.setHighlighter(null);
        obat.setName("obat"); // NOI18N
        obat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                obatKeyReleased(evt);
            }
        });
        FormInput.add(obat);
        obat.setBounds(130, 370, 154, 23);

        pb.setHighlighter(null);
        pb.setName("pb"); // NOI18N
        pb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pbKeyPressed(evt);
            }
        });
        FormInput.add(pb);
        pb.setBounds(400, 250, 154, 23);

        kpr.setHighlighter(null);
        kpr.setName("kpr"); // NOI18N
        kpr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kprKeyPressed(evt);
            }
        });
        FormInput.add(kpr);
        kpr.setBounds(400, 280, 154, 23);

        akomodasi.setHighlighter(null);
        akomodasi.setName("akomodasi"); // NOI18N
        akomodasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                akomodasiKeyPressed(evt);
            }
        });
        FormInput.add(akomodasi);
        akomodasi.setBounds(400, 340, 154, 23);

        lab.setHighlighter(null);
        lab.setName("lab"); // NOI18N
        lab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                labKeyPressed(evt);
            }
        });
        FormInput.add(lab);
        lab.setBounds(400, 310, 154, 23);

        bmhp.setHighlighter(null);
        bmhp.setName("bmhp"); // NOI18N
        bmhp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bmhpKeyPressed(evt);
            }
        });
        FormInput.add(bmhp);
        bmhp.setBounds(400, 400, 154, 23);

        kronis.setHighlighter(null);
        kronis.setName("kronis"); // NOI18N
        kronis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kronisKeyPressed(evt);
            }
        });
        FormInput.add(kronis);
        kronis.setBounds(400, 370, 154, 23);

        ksl.setHighlighter(null);
        ksl.setName("ksl"); // NOI18N
        ksl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kslKeyPressed(evt);
            }
        });
        FormInput.add(ksl);
        ksl.setBounds(660, 250, 154, 23);

        pnj.setHighlighter(null);
        pnj.setName("pnj"); // NOI18N
        pnj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pnjKeyPressed(evt);
            }
        });
        FormInput.add(pnj);
        pnj.setBounds(660, 280, 154, 23);

        ri.setHighlighter(null);
        ri.setName("ri"); // NOI18N
        ri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                riKeyPressed(evt);
            }
        });
        FormInput.add(ri);
        ri.setBounds(660, 340, 154, 23);

        pl_darah.setHighlighter(null);
        pl_darah.setName("pl_darah"); // NOI18N
        pl_darah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pl_darahKeyPressed(evt);
            }
        });
        FormInput.add(pl_darah);
        pl_darah.setBounds(660, 310, 154, 23);

        sewa_alat.setHighlighter(null);
        sewa_alat.setName("sewa_alat"); // NOI18N
        sewa_alat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sewa_alatKeyPressed(evt);
            }
        });
        FormInput.add(sewa_alat);
        sewa_alat.setBounds(660, 400, 154, 23);

        obat_kemo.setHighlighter(null);
        obat_kemo.setName("obat_kemo"); // NOI18N
        obat_kemo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                obat_kemoKeyPressed(evt);
            }
        });
        FormInput.add(obat_kemo);
        obat_kemo.setBounds(660, 370, 154, 23);

        jLabel26.setText("Hak Kelas :");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(815, 70, 70, 23);

        jLabel27.setText("BMHP :");
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput.add(jLabel27);
        jLabel27.setBounds(280, 400, 115, 23);

        jLabel28.setText("Prosedur Non Bedah :");
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput.add(jLabel28);
        jLabel28.setBounds(10, 250, 115, 23);

        jLabel29.setText("Tenaga Ahli :");
        jLabel29.setName("jLabel29"); // NOI18N
        FormInput.add(jLabel29);
        jLabel29.setBounds(10, 280, 115, 23);

        jLabel30.setText("Radiologi :");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(10, 310, 115, 23);

        jLabel31.setText("Rehabilitasi :");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(10, 340, 115, 23);

        jLabel32.setText("Obat :");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(10, 370, 115, 23);

        jLabel33.setText("Prosedur Bedah :");
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(280, 250, 115, 23);

        jLabel34.setText("Keperawatan :");
        jLabel34.setName("jLabel34"); // NOI18N
        FormInput.add(jLabel34);
        jLabel34.setBounds(280, 280, 115, 23);

        jLabel35.setText("Labotatorium :");
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput.add(jLabel35);
        jLabel35.setBounds(280, 310, 115, 23);

        jLabel36.setText("Kamar / Akomodasi :");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(280, 340, 115, 23);

        jLabel37.setText("Obat Kronis :");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(280, 370, 115, 23);

        jLabel38.setText("Konsultasi :");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(540, 250, 115, 23);

        jLabel39.setText("Penunjang :");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput.add(jLabel39);
        jLabel39.setBounds(540, 280, 115, 23);

        jLabel40.setText("Pelayanan Darah :");
        jLabel40.setName("jLabel40"); // NOI18N
        FormInput.add(jLabel40);
        jLabel40.setBounds(540, 310, 115, 23);

        jLabel41.setText("Rawat Intensif :");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(540, 340, 115, 23);

        jLabel42.setText("Obat Kemoterapi :");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(540, 370, 115, 23);

        jLabel43.setText("Jenis Rawat :");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput.add(jLabel43);
        jLabel43.setBounds(10, 70, 115, 23);

        jam_masuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jam_masuk.setName("jam_masuk"); // NOI18N
        FormInput.add(jam_masuk);
        jam_masuk.setBounds(240, 100, 62, 23);

        menit_masuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        menit_masuk.setName("menit_masuk"); // NOI18N
        FormInput.add(menit_masuk);
        menit_masuk.setBounds(300, 100, 62, 23);

        tgl_keluar.setEditable(false);
        tgl_keluar.setForeground(new java.awt.Color(50, 70, 50));
        tgl_keluar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2025-04-25" }));
        tgl_keluar.setDisplayFormat("yyyy-MM-dd");
        tgl_keluar.setName("tgl_keluar"); // NOI18N
        tgl_keluar.setOpaque(false);
        FormInput.add(tgl_keluar);
        tgl_keluar.setBounds(430, 100, 100, 23);

        jLabel44.setText("Tgl Keluar :");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(310, 100, 115, 23);

        jam_keluar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jam_keluar.setName("jam_keluar"); // NOI18N
        FormInput.add(jam_keluar);
        jam_keluar.setBounds(540, 100, 62, 23);

        menit_keluar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        menit_keluar.setName("menit_keluar"); // NOI18N
        FormInput.add(menit_keluar);
        menit_keluar.setBounds(600, 100, 62, 23);

        rawat_intensif.setBackground(new java.awt.Color(255, 255, 255));
        rawat_intensif.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        rawat_intensif.setText("Ada Rawat Intensif");
        rawat_intensif.setName("rawat_intensif"); // NOI18N
        rawat_intensif.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rawat_intensifItemStateChanged(evt);
            }
        });
        FormInput.add(rawat_intensif);
        rawat_intensif.setBounds(470, 70, 140, 22);

        jLabel45.setText("ADL Score :");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput.add(jLabel45);
        jLabel45.setBounds(10, 160, 115, 23);

        ventilator.setBackground(new java.awt.Color(255, 255, 255));
        ventilator.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        ventilator.setText("Ventilator");
        ventilator.setName("ventilator"); // NOI18N
        ventilator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ventilatorItemStateChanged(evt);
            }
        });
        FormInput.add(ventilator);
        ventilator.setBounds(410, 190, 90, 22);

        pasien_tb.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        pasien_tb.setEnabled(false);
        pasien_tb.setHighlighter(null);
        pasien_tb.setName("pasien_tb"); // NOI18N
        FormInput.add(pasien_tb);
        pasien_tb.setBounds(130, 190, 190, 23);

        jLabel46.setText("SEP : ");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput.add(jLabel46);
        jLabel46.setBounds(300, 40, 70, 23);

        naik_turun_kelas.setBackground(new java.awt.Color(255, 255, 255));
        naik_turun_kelas.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        naik_turun_kelas.setText("Naik / Turun Kelas");
        naik_turun_kelas.setName("naik_turun_kelas"); // NOI18N
        naik_turun_kelas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                naik_turun_kelasItemStateChanged(evt);
            }
        });
        FormInput.add(naik_turun_kelas);
        naik_turun_kelas.setBounds(320, 70, 140, 22);

        naik_kelas_pelayanan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "kelas_3 : Kelas 3", "kelas_2 : Kelas 2", "kelas_1 : Kelas 1", "vip : Diatas Kelas 1" }));
        naik_kelas_pelayanan.setName("naik_kelas_pelayanan"); // NOI18N
        FormInput.add(naik_kelas_pelayanan);
        naik_kelas_pelayanan.setBounds(890, 100, 130, 23);

        jLabel47.setText("Kelas Pelayanan : ");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(790, 100, 100, 23);

        lama_naik_kelas.setEditable(false);
        lama_naik_kelas.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        lama_naik_kelas.setEnabled(false);
        lama_naik_kelas.setHighlighter(null);
        lama_naik_kelas.setName("lama_naik_kelas"); // NOI18N
        FormInput.add(lama_naik_kelas);
        lama_naik_kelas.setBounds(740, 100, 50, 23);

        jLabel48.setText("Lama (hari) : ");
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput.add(jLabel48);
        jLabel48.setBounds(670, 100, 70, 23);

        jLabel51.setText("Chronic : ");
        jLabel51.setName("jLabel51"); // NOI18N
        FormInput.add(jLabel51);
        jLabel51.setBounds(240, 160, 90, 23);

        jLabel52.setText("Gram");
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput.add(jLabel52);
        jLabel52.setBounds(220, 130, 30, 23);

        chronic.setText("-");
        chronic.setHighlighter(null);
        chronic.setName("chronic"); // NOI18N
        FormInput.add(chronic);
        chronic.setBounds(330, 160, 60, 23);

        sub_acute.setText("-");
        sub_acute.setHighlighter(null);
        sub_acute.setName("sub_acute"); // NOI18N
        FormInput.add(sub_acute);
        sub_acute.setBounds(200, 160, 60, 23);

        jLabel54.setText("Sub Acute : ");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(110, 160, 90, 23);

        berat_bayi.setHighlighter(null);
        berat_bayi.setName("berat_bayi"); // NOI18N
        FormInput.add(berat_bayi);
        berat_bayi.setBounds(130, 130, 90, 23);

        jLabel55.setText("Berat Lahir Bayi : ");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(40, 130, 90, 23);

        dpjp.setHighlighter(null);
        dpjp.setName("dpjp"); // NOI18N
        FormInput.add(dpjp);
        dpjp.setBounds(300, 130, 310, 23);

        jLabel57.setText("DPJP : ");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(190, 130, 110, 23);

        validasi_tb.setText("Validasi");
        validasi_tb.setToolTipText("");
        validasi_tb.setGlassColor(new java.awt.Color(0, 255, 153));
        validasi_tb.setName("validasi_tb"); // NOI18N
        validasi_tb.setPreferredSize(new java.awt.Dimension(100, 30));
        validasi_tb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validasi_tbActionPerformed(evt);
            }
        });
        FormInput.add(validasi_tb);
        validasi_tb.setBounds(320, 190, 70, 22);

        validasi_cvd19.setText("Validasi");
        validasi_cvd19.setToolTipText("");
        validasi_cvd19.setEnabled(false);
        validasi_cvd19.setGlassColor(new java.awt.Color(0, 255, 153));
        validasi_cvd19.setName("validasi_cvd19"); // NOI18N
        validasi_cvd19.setPreferredSize(new java.awt.Dimension(100, 30));
        validasi_cvd19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validasi_cvd19ActionPerformed(evt);
            }
        });
        FormInput.add(validasi_cvd19);
        validasi_cvd19.setBounds(320, 220, 70, 22);

        co_covid_19.setEditable(false);
        co_covid_19.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        co_covid_19.setEnabled(false);
        co_covid_19.setHighlighter(null);
        co_covid_19.setName("co_covid_19"); // NOI18N
        FormInput.add(co_covid_19);
        co_covid_19.setBounds(130, 220, 190, 23);

        jLabel58.setText("Co-Insidense COVID-19 : ");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(0, 220, 130, 23);

        status_pulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1 : Atas persetujuan dokter", "2 : Dirujuk", "3 : Atas permintaan sendiri", "4 : Meninggal", "5 : Lain-lain" }));
        status_pulang.setName("status_pulang"); // NOI18N
        FormInput.add(status_pulang);
        status_pulang.setBounds(450, 160, 170, 23);

        jLabel59.setText("Pulang : ");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput.add(jLabel59);
        jLabel59.setBounds(380, 160, 70, 23);

        kelas_eksekutif.setBackground(new java.awt.Color(255, 255, 255));
        kelas_eksekutif.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        kelas_eksekutif.setText("Kelas Eksekutif");
        kelas_eksekutif.setName("kelas_eksekutif"); // NOI18N
        kelas_eksekutif.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                kelas_eksekutifItemStateChanged(evt);
            }
        });
        FormInput.add(kelas_eksekutif);
        kelas_eksekutif.setBounds(320, 70, 140, 22);

        tgl_intubasi.setEditable(false);
        tgl_intubasi.setForeground(new java.awt.Color(50, 70, 50));
        tgl_intubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2025-04-25" }));
        tgl_intubasi.setDisplayFormat("yyyy-MM-dd");
        tgl_intubasi.setName("tgl_intubasi"); // NOI18N
        tgl_intubasi.setOpaque(false);
        FormInput.add(tgl_intubasi);
        tgl_intubasi.setBounds(660, 190, 100, 23);

        jam_intubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jam_intubasi.setName("jam_intubasi"); // NOI18N
        FormInput.add(jam_intubasi);
        jam_intubasi.setBounds(770, 190, 62, 23);

        menit_intubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        menit_intubasi.setName("menit_intubasi"); // NOI18N
        FormInput.add(menit_intubasi);
        menit_intubasi.setBounds(830, 190, 62, 23);

        total_rupiah.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        total_rupiah.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        total_rupiah.setName("total_rupiah"); // NOI18N
        FormInput.add(total_rupiah);
        total_rupiah.setBounds(870, 260, 360, 50);

        menit_ekstubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        menit_ekstubasi.setName("menit_ekstubasi"); // NOI18N
        FormInput.add(menit_ekstubasi);
        menit_ekstubasi.setBounds(830, 220, 62, 23);

        jam_ekstubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jam_ekstubasi.setName("jam_ekstubasi"); // NOI18N
        FormInput.add(jam_ekstubasi);
        jam_ekstubasi.setBounds(770, 220, 62, 23);

        tgl_ekstubasi.setEditable(false);
        tgl_ekstubasi.setForeground(new java.awt.Color(50, 70, 50));
        tgl_ekstubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2025-04-25" }));
        tgl_ekstubasi.setDisplayFormat("yyyy-MM-dd");
        tgl_ekstubasi.setName("tgl_ekstubasi"); // NOI18N
        tgl_ekstubasi.setOpaque(false);
        FormInput.add(tgl_ekstubasi);
        tgl_ekstubasi.setBounds(660, 220, 100, 23);

        jLabel62.setText("Ekstubasi :");
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput.add(jLabel62);
        jLabel62.setBounds(580, 220, 70, 23);

        hari_intensif.setEditable(false);
        hari_intensif.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        hari_intensif.setEnabled(false);
        hari_intensif.setHighlighter(null);
        hari_intensif.setName("hari_intensif"); // NOI18N
        FormInput.add(hari_intensif);
        hari_intensif.setBounds(520, 220, 50, 23);

        jLabel63.setText(" Rawat Intensif (hari) : ");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput.add(jLabel63);
        jLabel63.setBounds(400, 220, 120, 23);

        jLabel66.setText("Check Diagnosa Utama");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(270, 440, 115, 23);

        jLabel67.setText("Sistole : ");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput.add(jLabel67);
        jLabel67.setBounds(620, 70, 50, 23);

        distole.setHighlighter(null);
        distole.setName("distole"); // NOI18N
        FormInput.add(distole);
        distole.setBounds(770, 70, 50, 23);

        tarif_eksekutif.setEditable(false);
        tarif_eksekutif.setHighlighter(null);
        tarif_eksekutif.setName("tarif_eksekutif"); // NOI18N
        FormInput.add(tarif_eksekutif);
        tarif_eksekutif.setBounds(700, 130, 70, 23);

        jLabel13.setText("Tarif Eksekutif : ");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(610, 130, 90, 23);

        pnb1.setHighlighter(null);
        pnb1.setName("pnb1"); // NOI18N
        FormInput.add(pnb1);
        pnb1.setBounds(130, 250, 154, 23);

        tgl_lahir.setEditable(false);
        tgl_lahir.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        tgl_lahir.setEnabled(false);
        tgl_lahir.setHighlighter(null);
        tgl_lahir.setName("tgl_lahir"); // NOI18N
        FormInput.add(tgl_lahir);
        tgl_lahir.setBounds(625, 10, 80, 23);

        jLabel60.setText("Gender : ");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(530, 40, 70, 23);

        jenis_kelamin.setEditable(false);
        jenis_kelamin.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jenis_kelamin.setEnabled(false);
        jenis_kelamin.setHighlighter(null);
        jenis_kelamin.setName("jenis_kelamin"); // NOI18N
        FormInput.add(jenis_kelamin);
        jenis_kelamin.setBounds(600, 40, 50, 23);

        jLabel64.setText("Cara Masuk :");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput.add(jLabel64);
        jLabel64.setBounds(670, 40, 70, 23);

        jam_ventilator.setEditable(false);
        jam_ventilator.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jam_ventilator.setEnabled(false);
        jam_ventilator.setHighlighter(null);
        jam_ventilator.setName("jam_ventilator"); // NOI18N
        FormInput.add(jam_ventilator);
        jam_ventilator.setBounds(700, 160, 50, 23);

        jLabel65.setText("Berapa Jam  : ");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput.add(jLabel65);
        jLabel65.setBounds(620, 160, 80, 23);

        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel68.setText(" TARIF RUMAH SAKIT : ");
        jLabel68.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput.add(jLabel68);
        jLabel68.setBounds(820, 250, 130, 23);

        jLabel69.setText("RP .");
        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput.add(jLabel69);
        jLabel69.setBounds(780, 260, 80, 50);

        nm_diag_1.setEditable(false);
        nm_diag_1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_1.setEnabled(false);
        nm_diag_1.setHighlighter(null);
        nm_diag_1.setName("nm_diag_1"); // NOI18N
        FormInput.add(nm_diag_1);
        nm_diag_1.setBounds(110, 460, 190, 23);

        jLabel70.setText("Alkes :");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput.add(jLabel70);
        jLabel70.setBounds(10, 400, 115, 23);

        jLabel71.setText("DIAGNOSA PASIEN");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput.add(jLabel71);
        jLabel71.setBounds(20, 440, 115, 23);

        kd_diag_1.setEditable(false);
        kd_diag_1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_1.setEnabled(false);
        kd_diag_1.setHighlighter(null);
        kd_diag_1.setName("kd_diag_1"); // NOI18N
        FormInput.add(kd_diag_1);
        kd_diag_1.setBounds(40, 460, 70, 23);

        nm_diag_2.setEditable(false);
        nm_diag_2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_2.setEnabled(false);
        nm_diag_2.setHighlighter(null);
        nm_diag_2.setName("nm_diag_2"); // NOI18N
        FormInput.add(nm_diag_2);
        nm_diag_2.setBounds(110, 490, 190, 23);

        kd_diag_2.setEditable(false);
        kd_diag_2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_2.setEnabled(false);
        kd_diag_2.setHighlighter(null);
        kd_diag_2.setName("kd_diag_2"); // NOI18N
        FormInput.add(kd_diag_2);
        kd_diag_2.setBounds(40, 490, 70, 23);

        nm_diag_3.setEditable(false);
        nm_diag_3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_3.setEnabled(false);
        nm_diag_3.setHighlighter(null);
        nm_diag_3.setName("nm_diag_3"); // NOI18N
        FormInput.add(nm_diag_3);
        nm_diag_3.setBounds(110, 520, 190, 23);

        kd_diag_3.setEditable(false);
        kd_diag_3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_3.setEnabled(false);
        kd_diag_3.setHighlighter(null);
        kd_diag_3.setName("kd_diag_3"); // NOI18N
        FormInput.add(kd_diag_3);
        kd_diag_3.setBounds(40, 520, 70, 23);

        nm_diag_4.setEditable(false);
        nm_diag_4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_4.setEnabled(false);
        nm_diag_4.setHighlighter(null);
        nm_diag_4.setName("nm_diag_4"); // NOI18N
        FormInput.add(nm_diag_4);
        nm_diag_4.setBounds(110, 550, 190, 23);

        kd_diag_4.setEditable(false);
        kd_diag_4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_4.setEnabled(false);
        kd_diag_4.setHighlighter(null);
        kd_diag_4.setName("kd_diag_4"); // NOI18N
        FormInput.add(kd_diag_4);
        kd_diag_4.setBounds(40, 550, 70, 23);

        nm_diag_5.setEditable(false);
        nm_diag_5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_diag_5.setEnabled(false);
        nm_diag_5.setHighlighter(null);
        nm_diag_5.setName("nm_diag_5"); // NOI18N
        FormInput.add(nm_diag_5);
        nm_diag_5.setBounds(110, 580, 190, 23);

        kd_diag_5.setEditable(false);
        kd_diag_5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_diag_5.setEnabled(false);
        kd_diag_5.setHighlighter(null);
        kd_diag_5.setName("kd_diag_5"); // NOI18N
        FormInput.add(kd_diag_5);
        kd_diag_5.setBounds(40, 580, 70, 23);

        nm_psdr_1.setEditable(false);
        nm_psdr_1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_1.setEnabled(false);
        nm_psdr_1.setHighlighter(null);
        nm_psdr_1.setName("nm_psdr_1"); // NOI18N
        FormInput.add(nm_psdr_1);
        nm_psdr_1.setBounds(470, 460, 190, 23);

        kd_psdr_1.setEditable(false);
        kd_psdr_1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_1.setEnabled(false);
        kd_psdr_1.setHighlighter(null);
        kd_psdr_1.setName("kd_psdr_1"); // NOI18N
        FormInput.add(kd_psdr_1);
        kd_psdr_1.setBounds(400, 460, 70, 23);

        nm_psdr_2.setEditable(false);
        nm_psdr_2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_2.setEnabled(false);
        nm_psdr_2.setHighlighter(null);
        nm_psdr_2.setName("nm_psdr_2"); // NOI18N
        FormInput.add(nm_psdr_2);
        nm_psdr_2.setBounds(470, 490, 190, 23);

        kd_psdr_2.setEditable(false);
        kd_psdr_2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_2.setEnabled(false);
        kd_psdr_2.setHighlighter(null);
        kd_psdr_2.setName("kd_psdr_2"); // NOI18N
        FormInput.add(kd_psdr_2);
        kd_psdr_2.setBounds(400, 490, 70, 23);

        nm_psdr_3.setEditable(false);
        nm_psdr_3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_3.setEnabled(false);
        nm_psdr_3.setHighlighter(null);
        nm_psdr_3.setName("nm_psdr_3"); // NOI18N
        FormInput.add(nm_psdr_3);
        nm_psdr_3.setBounds(470, 520, 190, 23);

        kd_psdr_3.setEditable(false);
        kd_psdr_3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_3.setEnabled(false);
        kd_psdr_3.setHighlighter(null);
        kd_psdr_3.setName("kd_psdr_3"); // NOI18N
        FormInput.add(kd_psdr_3);
        kd_psdr_3.setBounds(400, 520, 70, 23);

        nm_psdr_4.setEditable(false);
        nm_psdr_4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_4.setEnabled(false);
        nm_psdr_4.setHighlighter(null);
        nm_psdr_4.setName("nm_psdr_4"); // NOI18N
        FormInput.add(nm_psdr_4);
        nm_psdr_4.setBounds(470, 550, 190, 23);

        kd_psdr_4.setEditable(false);
        kd_psdr_4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_4.setEnabled(false);
        kd_psdr_4.setHighlighter(null);
        kd_psdr_4.setName("kd_psdr_4"); // NOI18N
        FormInput.add(kd_psdr_4);
        kd_psdr_4.setBounds(400, 550, 70, 23);

        nm_psdr_5.setEditable(false);
        nm_psdr_5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        nm_psdr_5.setEnabled(false);
        nm_psdr_5.setHighlighter(null);
        nm_psdr_5.setName("nm_psdr_5"); // NOI18N
        FormInput.add(nm_psdr_5);
        nm_psdr_5.setBounds(470, 580, 190, 23);

        kd_psdr_5.setEditable(false);
        kd_psdr_5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        kd_psdr_5.setEnabled(false);
        kd_psdr_5.setHighlighter(null);
        kd_psdr_5.setName("kd_psdr_5"); // NOI18N
        FormInput.add(kd_psdr_5);
        kd_psdr_5.setBounds(400, 580, 70, 23);

        chk_diag_5.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_5.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_5.setName("chk_diag_5"); // NOI18N
        chk_diag_5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_5ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_5);
        chk_diag_5.setBounds(310, 580, 30, 22);

        chk_diag_1.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_1.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_1.setName("chk_diag_1"); // NOI18N
        chk_diag_1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_1ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_1);
        chk_diag_1.setBounds(310, 460, 30, 22);

        chk_diag_2.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_2.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_2.setName("chk_diag_2"); // NOI18N
        chk_diag_2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_2ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_2);
        chk_diag_2.setBounds(310, 490, 30, 22);

        chk_diag_3.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_3.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_3.setName("chk_diag_3"); // NOI18N
        chk_diag_3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_3ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_3);
        chk_diag_3.setBounds(310, 520, 30, 22);

        chk_diag_4.setBackground(new java.awt.Color(255, 255, 255));
        chk_diag_4.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        chk_diag_4.setName("chk_diag_4"); // NOI18N
        chk_diag_4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_diag_4ItemStateChanged(evt);
            }
        });
        FormInput.add(chk_diag_4);
        chk_diag_4.setBounds(310, 550, 30, 22);

        jLabel72.setText("5 ");
        jLabel72.setName("jLabel72"); // NOI18N
        FormInput.add(jLabel72);
        jLabel72.setBounds(710, 460, 40, 23);

        jLabel73.setText("PROSEDUR PASIEN");
        jLabel73.setName("jLabel73"); // NOI18N
        FormInput.add(jLabel73);
        jLabel73.setBounds(380, 440, 115, 23);

        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel74.setText(" TARIF INACBG : ");
        jLabel74.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel74.setName("jLabel74"); // NOI18N
        FormInput.add(jLabel74);
        jLabel74.setBounds(820, 320, 130, 23);

        total_rupiah1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        total_rupiah1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        total_rupiah1.setName("total_rupiah1"); // NOI18N
        FormInput.add(total_rupiah1);
        total_rupiah1.setBounds(870, 330, 360, 50);

        jLabel75.setText("RP .");
        jLabel75.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel75.setName("jLabel75"); // NOI18N
        FormInput.add(jLabel75);
        jLabel75.setBounds(780, 330, 80, 50);

        jLabel76.setText("PROSEDUR PASIEN");
        jLabel76.setName("jLabel76"); // NOI18N
        FormInput.add(jLabel76);
        jLabel76.setBounds(380, 440, 115, 23);

        jLabel77.setText("APGAR SCORE BAYI");
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput.add(jLabel77);
        jLabel77.setBounds(670, 440, 115, 23);

        ap_5.setHighlighter(null);
        ap_5.setName("ap_5"); // NOI18N
        ap_5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ap_5KeyPressed(evt);
            }
        });
        FormInput.add(ap_5);
        ap_5.setBounds(690, 580, 40, 23);

        jLabel78.setText("1 ");
        jLabel78.setName("jLabel78"); // NOI18N
        FormInput.add(jLabel78);
        jLabel78.setBounds(650, 460, 40, 23);

        ap_1.setHighlighter(null);
        ap_1.setName("ap_1"); // NOI18N
        FormInput.add(ap_1);
        ap_1.setBounds(690, 460, 40, 23);

        ap_2.setHighlighter(null);
        ap_2.setName("ap_2"); // NOI18N
        ap_2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ap_2KeyPressed(evt);
            }
        });
        FormInput.add(ap_2);
        ap_2.setBounds(690, 490, 40, 23);

        ap_3.setHighlighter(null);
        ap_3.setName("ap_3"); // NOI18N
        ap_3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ap_3KeyPressed(evt);
            }
        });
        FormInput.add(ap_3);
        ap_3.setBounds(690, 520, 40, 23);

        ap_4.setHighlighter(null);
        ap_4.setName("ap_4"); // NOI18N
        ap_4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ap_4KeyPressed(evt);
            }
        });
        FormInput.add(ap_4);
        ap_4.setBounds(690, 550, 40, 23);

        ap_10.setHighlighter(null);
        ap_10.setName("ap_10"); // NOI18N
        ap_10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ap_10KeyPressed(evt);
            }
        });
        FormInput.add(ap_10);
        ap_10.setBounds(750, 580, 40, 23);

        ap_6.setHighlighter(null);
        ap_6.setName("ap_6"); // NOI18N
        ap_6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ap_6KeyPressed(evt);
            }
        });
        FormInput.add(ap_6);
        ap_6.setBounds(750, 460, 40, 23);

        ap_7.setHighlighter(null);
        ap_7.setName("ap_7"); // NOI18N
        ap_7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ap_7KeyPressed(evt);
            }
        });
        FormInput.add(ap_7);
        ap_7.setBounds(750, 490, 40, 23);

        ap_8.setHighlighter(null);
        ap_8.setName("ap_8"); // NOI18N
        ap_8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ap_8KeyPressed(evt);
            }
        });
        FormInput.add(ap_8);
        ap_8.setBounds(750, 520, 40, 23);

        ap_9.setHighlighter(null);
        ap_9.setName("ap_9"); // NOI18N
        ap_9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ap_9KeyPressed(evt);
            }
        });
        FormInput.add(ap_9);
        ap_9.setBounds(750, 550, 40, 23);

        stage_2.setBackground(new java.awt.Color(0, 255, 204));
        stage_2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        stage_2.setName("stage_2"); // NOI18N
        FormInput.add(stage_2);
        stage_2.setBounds(810, 470, 250, 30);

        jLabel49.setForeground(new java.awt.Color(255, 0, 0));
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("JANGAN LUPA KLIK GROUPER LAGI");
        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput.add(jLabel49);
        jLabel49.setBounds(810, 500, 250, 23);

        Tsep.setEditable(false);
        Tsep.setHighlighter(null);
        Tsep.setName("Tsep"); // NOI18N
        Tsep.setOpaque(true);
        FormInput.add(Tsep);
        Tsep.setBounds(370, 40, 180, 23);

        jLabel50.setText("Sewa Alat :");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(540, 400, 115, 23);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setText("GROUPER STAGE 2");
        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(810, 450, 250, 23);

        scrollInput.setViewportView(FormInput);

        internalFrame1.add(scrollInput, java.awt.BorderLayout.CENTER);

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 54));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnGrouper.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/users.png"))); // NOI18N
        BtnGrouper.setMnemonic('B');
        BtnGrouper.setText("Grouper");
        BtnGrouper.setToolTipText("Alt+B");
        BtnGrouper.setGlassColor(new java.awt.Color(0, 255, 153));
        BtnGrouper.setName("BtnGrouper"); // NOI18N
        BtnGrouper.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnGrouper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGrouperActionPerformed(evt);
            }
        });
        BtnGrouper.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnGrouperKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnGrouper);

        finalklaim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        finalklaim.setText("Final Klaim");
        finalklaim.setToolTipText("Alt+S");
        finalklaim.setGlassColor(new java.awt.Color(0, 255, 153));
        finalklaim.setName("finalklaim"); // NOI18N
        finalklaim.setPreferredSize(new java.awt.Dimension(120, 30));
        finalklaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalklaimActionPerformed(evt);
            }
        });
        panelGlass8.add(finalklaim);

        editKlaim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/EDIT2.png"))); // NOI18N
        editKlaim.setText("Edit Klaim");
        editKlaim.setToolTipText("Alt+S");
        editKlaim.setGlassColor(new java.awt.Color(0, 255, 153));
        editKlaim.setName("editKlaim"); // NOI18N
        editKlaim.setPreferredSize(new java.awt.Dimension(120, 30));
        editKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editKlaimActionPerformed(evt);
            }
        });
        panelGlass8.add(editKlaim);

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
        panelGlass8.add(kirimonline);

        hapusKlaim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/delete_folder.png"))); // NOI18N
        hapusKlaim.setMnemonic('K');
        hapusKlaim.setText("Hapus Claim");
        hapusKlaim.setToolTipText("Alt+K");
        hapusKlaim.setGlassColor(new java.awt.Color(255, 102, 102));
        hapusKlaim.setName("hapusKlaim"); // NOI18N
        hapusKlaim.setPreferredSize(new java.awt.Dimension(130, 30));
        hapusKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusKlaimActionPerformed(evt);
            }
        });
        hapusKlaim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hapusKlaimKeyPressed(evt);
            }
        });
        panelGlass8.add(hapusKlaim);

        jLabel25.setName("jLabel25"); // NOI18N
        jLabel25.setPreferredSize(new java.awt.Dimension(150, 14));
        panelGlass8.add(jLabel25);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setGlassColor(new java.awt.Color(255, 102, 102));
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

        kirimonline2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/2.png"))); // NOI18N
        kirimonline2.setText("Tes Auto");
        kirimonline2.setToolTipText("Alt+S");
        kirimonline2.setGlassColor(new java.awt.Color(0, 255, 153));
        kirimonline2.setName("kirimonline2"); // NOI18N
        kirimonline2.setPreferredSize(new java.awt.Dimension(120, 30));
        kirimonline2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kirimonline2ActionPerformed(evt);
            }
        });
        panelGlass8.add(kirimonline2);

        kirimonline3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/2.png"))); // NOI18N
        kirimonline3.setText("Tarik Berkas Klaim");
        kirimonline3.setToolTipText("Alt+S");
        kirimonline3.setGlassColor(new java.awt.Color(0, 255, 153));
        kirimonline3.setName("kirimonline3"); // NOI18N
        kirimonline3.setPreferredSize(new java.awt.Dimension(120, 30));
        kirimonline3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kirimonline3ActionPerformed(evt);
            }
        });
        panelGlass8.add(kirimonline3);

        kirimonline4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/2.png"))); // NOI18N
        kirimonline4.setText("KOMPILASI");
        kirimonline4.setToolTipText("Alt+S");
        kirimonline4.setGlassColor(new java.awt.Color(0, 255, 153));
        kirimonline4.setName("kirimonline4"); // NOI18N
        kirimonline4.setPreferredSize(new java.awt.Dimension(120, 30));
        kirimonline4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kirimonline4ActionPerformed(evt);
            }
        });
        panelGlass8.add(kirimonline4);

        internalFrame1.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        TabRawat.addTab("E-KLAIM", internalFrame1);

        internalFrame1Kompi1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Kompilasi Berkas Klaim BPJS ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1Kompi1.setName("internalFrame1Kompi1"); // NOI18N
        internalFrame1Kompi1.setLayout(new java.awt.BorderLayout());

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
            .addComponent(panelGlass11Kompi1, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
        );
        jPanel4Kompi1Layout.setVerticalGroup(
            jPanel4Kompi1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGlass11Kompi1, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
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

        PanelContentINACBGKompi1.setName("PanelContentINACBGKompi1"); // NOI18N
        PanelContentINACBGKompi1.setPreferredSize(new java.awt.Dimension(55, 55));
        PanelContentINACBGKompi1.setLayout(new java.awt.BorderLayout());
        tabPane1Kompi1.addTab("Klaim INACBG", PanelContentINACBGKompi1);

        jPanel5Kompi1.add(tabPane1Kompi1);

        jPanel1Kompi1.add(jPanel5Kompi1);

        internalFrame1Kompi1.add(jPanel1Kompi1, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("KOMPILASI", internalFrame1Kompi1);

        getContentPane().add(TabRawat, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnGrouperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGrouperActionPerformed
        if (stage_2.getSelectedItem() == null) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            klaimbaru();
            setdataklaim();
            grouper1();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); 
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            grouper2();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
}//GEN-LAST:event_BtnGrouperActionPerformed

    private void BtnGrouperKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnGrouperKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            emptTeks();
        }
}//GEN-LAST:event_BtnGrouperKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
       
    }//GEN-LAST:event_formWindowOpened

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select pasien.nm_pasien from pasien where pasien.no_rkm_medis=? ",TPasien,TNoRM.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
//            BtnSeekActionPerformed(null);
        }else{
            Valid.pindah(evt,cara_masuk,pnb);
        }
    }//GEN-LAST:event_TNoRMKeyPressed

    private void klaimbaru(){
        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson = "{\n"
                                   + " \"metadata\": {\n"
                                   + " \"method\": \"new_claim\"\n"
                                   + "},\n"
                                   + " \"data\": {\n"+
                                                    "\"nomor_kartu\": \""+no_kartu.getText().trim()+"\",\n" +
                                                    "\"nomor_sep\": \""+Tsep.getText().trim()+"\",\n" +
                                                    "\"nomor_rm\": \""+TNoRM.getText().trim()+"\",\n" +
                                                    "\"nama_pasien\": \""+TPasien.getText().trim()+"\",\n" +
                                                    "\"tgl_lahir\": \""+tgl_lahir.getText().trim()+" 00:00:00\",\n" +
                                                    "\"gender\": \""+(jenis_kelamin.equals("L") ? 1 : 2)+"\""
                                            + " }\n"
                                   + "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);

                JSONObject response = inacbg.request(request);
                
                System.out.println("respon klaimbaru : "+response);

                if(response.has("metadata")){
                    JSONObject code = response.getJSONObject("metadata");
                    if(code.getInt("code")==200){
                        JSONObject data = response.getJSONObject("response");
                        if (data.has("patient_id")) {
                            if(Sequel.menyimpantf2("inacbg_klaim_baru","?,?,?,?","EKLAIM",4,new String[]{
                               Tsep.getText(),data.getInt("patient_id")+"",data.getInt("admission_id")+"",data.getInt("hospital_admission_id")+""
                            })==false){
                                Sequel.mengedit("inacbg_klaim_baru", "no_sep='"+Tsep.getText()+"'", "patient_id='"+data.getInt("patient_id")+"',admission_id='"+data.getInt("admission_id")+"',hospital_admission_id='"+data.getInt("hospital_admission_id")+"'");
                            }
                        }
                        System.out.println("RESPON : "+code.getString("message"));
                    }else{
                        if (!code.getString("message").contains("Duplikasi")) {
                            JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        }
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
    
    private void grouper1(){
        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"grouper\",\n" +
                                            "\"stage\":\"1\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"nomor_sep\":\""+Tsep.getText().trim()+"\"\n" +
                                        "}\n" +
                                    "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                System.out.println("response grouper1 : "+response);
                
                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        JSONObject data = response.getJSONObject("response");
                        JSONObject trf = data.getJSONObject("cbg");
                        if (trf.has("tariff")) {
                            if(Sequel.menyimpantf2("inacbg_grouping_stage1","?,?,?,?","GROUPER EKLAIM",4,new String[]{
                               Tsep.getText(),trf.getString("code"),trf.getString("description"),trf.getDouble("tariff")+""
                            })==false){
                                Sequel.mengedit("inacbg_grouping_stage1", "no_sep='"+Tsep.getText()+"'", "code_cbg='"+trf.getString("code")+"',deskripsi='"+trf.getString("description")+"',tarif='"+trf.getDouble("tariff")+"'");
                            }
                            total_rupiah1.setText(Valid.SetAngka(trf.getDouble("tariff")));
                        }else{
                            JOptionPane.showMessageDialog(null,"PASIEN UNGROUPABLE (GAGAL GROUPER)");
                        }
                        
                        if(response.has("special_cmg_option")){
                            System.out.println("spesial : "+response.getJSONArray("special_cmg_option"));
                            JSONArray spesial = response.getJSONArray("special_cmg_option");
                            jLabel49.setVisible(true);
                            stage_2.setVisible(true);
                            jLabel53.setVisible(true);
                            
                            for (int i = 0; i < spesial.length(); i++) {
                                JSONObject cmg = spesial.getJSONObject(i);
                                stage_2.addItem(cmg.getString("code")+" : "+cmg.getString("description"));
                            }
                        }
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
    
    private void grouper2(){
        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson ="{\n" +
                                        "\"metadata\": {\n" +
                                            "\"method\":\"grouper\",\n" +
                                            "\"stage\":\"2\"\n" +
                                        "},\n" +
                                        "\"data\": {\n" +
                                            "\"nomor_sep\":\""+Tsep.getText().trim()+"\",\n" +
                                            "\"special_cmg\": \""+stage_2.getSelectedItem().toString().split(":")[0].trim()+"\"\n" +
                                        "}\n" +
                                    "}";


                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);
                JSONObject response = inacbg.request(request);
                JSONObject code = response.getJSONObject("metadata");
                System.out.println("response grouper2 : "+response);
                
                if(response.has("metadata")){
                    if(code.getInt("code")==200){
                        JSONObject data = response.getJSONObject("response");
                        JSONObject trf = data.getJSONObject("cbg");
//                        System.out.println("resdata : "+trf);
                        if (trf.has("tariff")) {
                            if(Sequel.menyimpantf2("inacbg_grouping_stage2","?,?,?,?","GROUPER EKLAIM",4,new String[]{
                               Tsep.getText(),trf.getString("code"),trf.getString("description"),trf.getDouble("tariff")+""
                            })==false){
                                Sequel.mengedit("inacbg_grouping_stage2", "no_sep='"+Tsep.getText()+"'", "code_cbg='"+trf.getString("code")+"',deskripsi='"+trf.getString("description")+"',tarif='"+trf.getDouble("tariff")+"'");
                            }
                            total_rupiah1.setText(Valid.SetAngka(trf.getDouble("tariff")));
                        }
                    }else{
                        System.out.println("response : "+response);
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
    
    private void setdataklaim(){
        if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
        } else {            
            switch (jaminan.getSelectedIndex()) {
                case 0:
                    payor_id = 3+"";
                    payor_cd = "JKN";
                    break;
                case 1:
                    payor_id = 71+"";
                    payor_cd = "COVID-19";
                    break;
                case 2:
                    payor_id = 72+"";
                    payor_cd = "KIPI";
                    break;
                case 3:
                    payor_id = 73+"";
                    payor_cd = "BBL";
                    break;
                case 4:
                    payor_id = 74+"";
                    payor_cd = "PMR";
                    break;
                case 5:
                    payor_id = 75+"";
                    payor_cd = "CO-INS";
                    break;
                case 6:
                    payor_id = 76+"";
                    payor_cd = "JPS";
                    break;
                case 7:
                    payor_id = 77+"";
                    payor_cd = "JPKP";
                    break;
                default:
                    payor_id = 3+"";
                    payor_cd = "JKN";
                    break;
            }
            try {
                // Create a human-readable JSON string
                String requestJson =   "{\n"+
                                        "  \"metadata\": {\n" +
                                        "        \"method\": \"set_claim_data\",\n" +
                                        "        \"nomor_sep\": \""+Tsep.getText()+"\"\n" +
                                        "    },\n" +
                                        "    \"data\": {\n" +
                                        "        \"nomor_sep\": \""+Tsep.getText()+"\",\n" +
                                        "        \"nomor_kartu\": \""+no_kartu.getText()+"\",\n" +
                                        "        \"tgl_masuk\": \""+tgl_masuk.getSelectedItem()+" "+jam_masuk.getSelectedItem()+":"+menit_masuk.getSelectedItem()+":00\",\n" +
                                        "        \"tgl_pulang\": \""+tgl_keluar.getSelectedItem()+" "+jam_keluar.getSelectedItem()+":"+menit_keluar.getSelectedItem()+":00\",\n" +
                                        "        \"cara_masuk\": \""+cara_masuk.getSelectedItem().toString().split(":")[0].trim()+"\",\n" +
                                        "        \"jenis_rawat\": \""+jenis_rawat.getSelectedItem().toString().split(":")[0].trim()+"\",\n" +
                                        "        \"kelas_rawat\": \""+hak_kelas.getSelectedItem().toString().split(":")[0].trim()+"\",\n" +
                                        "        \"adl_sub_acute\": \""+sub_acute.getText()+"\",\n" +
                                        "        \"adl_chronic\": \""+chronic.getText()+"\",\n" +
                                        "        \"icu_indikator\": \""+(rawat_intensif.isSelected() == true ? 1 : 0)+"\",\n" +
                                        "        \"icu_los\": \""+hari_intensif.getText()+"\",\n" +
                                        "        \"ventilator_hour\": \""+jam_ventilator.getText()+"\",\n" +
                                        "        \"ventilator\": {\n" +
                                        "            \"use_ind\": \""+(ventilator.isSelected()==true ? 1 : 0)+"\",\n" +
                                        "            \"start_dttm\": \""+tgl_intubasi.getSelectedItem()+" "+jam_intubasi.getSelectedItem()+":"+menit_intubasi.getSelectedItem()+":00\",\n" +
                                        "            \"stop_dttm\": \""+tgl_ekstubasi.getSelectedItem()+" "+jam_ekstubasi.getSelectedItem()+":"+menit_ekstubasi.getSelectedItem()+":00\"\n" +
                                        "        },\n" +
                                        "        \"upgrade_class_ind\": \""+(naik_turun_kelas.isSelected()==true ? 1 : 0)+"\",\n" +
                                        "        \"upgrade_class_class\": \""+naik_kelas_pelayanan.getSelectedItem().toString().split(":")[0].trim()+"\",\n" + 
                                        "        \"upgrade_class_los\": \"5\",\n" +
                                        "        \"upgrade_class_payor\": \"peserta\",\n" +
                                        "        \"add_payment_pct\": \"35\",\n" +
                                        "        \"birth_weight\": \""+berat_bayi.getText()+"\",\n" +
                                        "        \"sistole\": \""+sistole.getText()+"\",\n" +
                                        "        \"diastole\": \""+distole.getText()+"\",\n" +
                                        "        \"discharge_status\": \""+status_pulang.getSelectedItem().toString().split(":")[0].trim()+"\",\n" +
                                        "        \"diagnosa\": \""+icd10.getText()+"\",\n" +
                                        "        \"procedure\": \""+icd9.getText()+"\",\n" +
                                        "        \"diagnosa_inagrouper\": \""+icd10.getText()+"\",\n" +
                                        "        \"procedure_inagrouper\": \""+icd9.getText()+"\",\n" +
                                        "        \"tarif_rs\": {\n" +
                                        "            \"prosedur_non_bedah\": \""+pnb.getText()+"\",\n" +
                                        "            \"prosedur_bedah\": \""+pb.getText()+"\",\n" +
                                        "            \"konsultasi\": \""+ksl.getText()+"\",\n" +
                                        "            \"tenaga_ahli\": \""+ahli.getText()+"\",\n" +
                                        "            \"keperawatan\": \""+kpr.getText()+"\",\n" +
                                        "            \"penunjang\": \""+pnj.getText()+"\",\n" +
                                        "            \"radiologi\": \""+rad.getText()+"\",\n" +
                                        "            \"laboratorium\": \""+lab.getText()+"\",\n" +
                                        "            \"pelayanan_darah\": \""+pl_darah.getText()+"\",\n" +
                                        "            \"rehabilitasi\": \""+rehab.getText()+"\",\n" +
                                        "            \"kamar\": \""+akomodasi.getText()+"\",\n" +
                                        "            \"rawat_intensif\": \""+ri.getText()+"\",\n" +
                                        "            \"obat\": \""+obat.getText()+"\",\n" +
                                        "            \"obat_kronis\": \""+kronis.getText()+"\",\n" +
                                        "            \"obat_kemoterapi\": \""+obat_kemo.getText()+"\",\n" +
                                        "            \"alkes\": \""+alkes.getText()+"\",\n" +
                                        "            \"bmhp\": \""+bmhp.getText()+"\",\n" +
                                        "            \"sewa_alat\": \""+sewa_alat.getText()+"\"\n" +
                                        "        },\n" +
//                                        "        \"pemulasaraan_jenazah\": \"1\",\n" +
//                                        "        \"kantong_jenazah\": \"1\",\n" +
//                                        "        \"peti_jenazah\": \"1\",\n" +
//                                        "        \"plastik_erat\": \"1\",\n" +
//                                        "        \"desinfektan_jenazah\": \"1\",\n" +
//                                        "        \"mobil_jenazah\": \"0\",\n" +
//                                        "        \"desinfektan_mobil_jenazah\": \"0\",\n" +
//                                        "        \"covid19_status_cd\": \"1\",\n" +
//                                        "        \"nomor_kartu_t\": \"nik\",\n" +
//                                        "        \"episodes\": \"1;12#2;3#6;5\",\n" +
//                                        "        \"covid19_cc_ind\": \"1\",\n" +
//                                        "        \"covid19_rs_darurat_ind\": \"1\",\n" +
//                                        "        \"covid19_co_insidense_ind\": \"1\",\n" +
//                                        "        \"covid19_penunjang_pengurang\": {\n" +
//                                        "            \"lab_asam_laktat\": \"1\",\n" +
//                                        "            \"lab_procalcitonin\": \"1\",\n" +
//                                        "            \"lab_crp\": \"1\",\n" +
//                                        "            \"lab_kultur\": \"1\",\n" +
//                                        "            \"lab_d_dimer\": \"1\",\n" +
//                                        "            \"lab_pt\": \"1\",\n" +
//                                        "            \"lab_aptt\": \"1\",\n" +
//                                        "            \"lab_waktu_pendarahan\": \"1\",\n" +
//                                        "            \"lab_anti_hiv\": \"1\",\n" +
//                                        "            \"lab_analisa_gas\": \"1\",\n" +
//                                        "            \"lab_albumin\": \"1\",\n" +
//                                        "            \"rad_thorax_ap_pa\": \"0\"\n" +
//                                        "        },\n" +
//                                        "        \"terapi_konvalesen\": \"1000000\",\n" +
//                                        "        \"akses_naat\": \"C\",\n" +
//                                        "        \"isoman_ind\": \"0\",\n" +
//                                        "        \"bayi_lahir_status_cd\": 1,\n" +
//                                        "        \"dializer_single_use\": 0,\n" +
//                                        "        \"kantong_darah\": 1,\n" +
//                                        "        \"alteplase_ind\": 0,\n" +
//                                        "        \"apgar\": {\n" +
//                                        "            \"menit_1\": {\n" +
//                                        "                \"appearance\": "+ap_1.getText()+",\n" +
//                                        "                \"pulse\": "+ap_2.getText()+",\n" +
//                                        "                \"grimace\": "+ap_3.getText()+",\n" +
//                                        "                \"activity\": "+ap_4.getText()+",\n" +
//                                        "                \"respiration\": "+ap_5.getText()+"\n" +
//                                        "            },\n" +
//                                        "            \"menit_5\": {\n" +
//                                        "                \"appearance\": "+ap_6.getText()+",\n" +
//                                        "                \"pulse\": "+ap_7.getText()+",\n" +
//                                        "                \"grimace\": "+ap_8.getText()+",\n" +
//                                        "                \"activity\": "+ap_9.getText()+",\n" +
//                                        "                \"respiration\": "+ap_10.getText()+"\n" +
//                                        "            }\n" +
//                                        "        },\n" +
//                                        "        \"persalinan\": {\n" +
//                                        "            \"usia_kehamilan\": \"22\",\n" +
//                                        "            \"gravida\": \"2\",\n" +
//                                        "            \"partus\": \"4\",\n" +
//                                        "            \"abortus\": \"2\",\n" +
//                                        "            \"onset_kontraksi\": \"induksi\",\n" +
//                                        "            \"delivery\": [\n" +
//                                        "                {\n" +
//                                        "                    \"delivery_sequence\": \"1\",\n" +
//                                        "                    \"delivery_method\": \"vaginal\",\n" +
//                                        "                    \"delivery_dttm\": \"2023-01-21 17:01:33\",\n" +
//                                        "                    \"letak_janin\": \"kepala\",\n" +
//                                        "                    \"kondisi\": \"livebirth\",\n" +
//                                        "                    \"use_manual\": \"1\",\n" +
//                                        "                    \"use_forcep\": \"0\",\n" +
//                                        "                    \"use_vacuum\": \"1\",\n" +
//                                        "                    \"shk_spesimen_ambil\": \"ya\",\n" +
//                                        "                    \"shk_lokasi\": \"tumit\",\n" +
//                                        "                    \"shk_spesimen_dttm\": \"2023-01-21 18:11:33\"\n" +
//                                        "                },\n" +
//                                        "                {\n" +
//                                        "                    \"delivery_sequence\": \"2\",\n" +
//                                        "                    \"delivery_method\": \"vaginal\",\n" +
//                                        "                    \"delivery_dttm\": \"2023-01-21 17:03:49\",\n" +
//                                        "                    \"letak_janin\": \"lintang\",\n" +
//                                        "                    \"kondisi\": \"livebirth\",\n" +
//                                        "                    \"use_manual\": \"1\",\n" +
//                                        "                    \"use_forcep\": \"0\",\n" +
//                                        "                    \"use_vacuum\": \"0\",\n" +
//                                        "                    \"shk_spesimen_ambil\": \"tidak\",\n" +
//                                        "                    \"shk_alasan\": \"akses-sulit\"\n" +
//                                        "                }\n" +
//                                        "            ]\n" +
//                                        "        },\n" +
                                        "        \"tarif_poli_eks\": \""+(tarif_eksekutif.getText().equals(0) ? 0 : tarif_eksekutif.getText())+"\",\n" +
                                        "        \"nama_dokter\": \""+dpjp.getText()+"\",\n" +
                                        "        \"kode_tarif\": \""+koneksiDB.KELASRSEKLAIM()+"\",\n" +
                                        "        \"payor_id\": \""+payor_id+"\",\n" +
                                        "        \"payor_cd\": \""+payor_cd+"\",\n" +
                                        "        \"cob_cd\": \""+cob.getSelectedItem().toString().split(":")[0].trim()+"\",\n" +
                                        "        \"coder_nik\": \""+coder+"\"\n" +
                                        "    \n}"
                                    + " }\n";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);

                JSONObject response = inacbg.request(request);
                System.out.println("response data klaim : "+response); 
                
                if(response.has("metadata")){
                    JSONObject code = response.getJSONObject("metadata");
                    if(code.getInt("code")==200){
                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
                        if(Sequel.menyimpantf2("inacbg_data_terkirim","?,?","DATA EKLAIM",2,new String[]{
                               Tsep.getText(),coder
                        })==false){
                            Sequel.mengedit("inacbg_data_terkirim", "no_sep='"+Tsep.getText()+"'", "nik='"+coder+"'");
                        }
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
    
    
    private void validasi_tbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validasi_tbActionPerformed
        // TODO add your handling code here:
        if (pasien_tb.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Silahkan isi nomor sitb pasien terlebih dahulu..!!");
        } else {
            try {
                // Create a human-readable JSON string
                String requestJson = "{\n"
                                   + " \"metadata\": {\n"
                                   + " \"method\": \"sitb_validate\"\n"
                                   + "},\n"
                                   + " \"data\": {\n"
                                   + " \"nomor_sep\": \""+Tsep.getText()+"\",\n"
                                   + " \"nomor_register_sitb\": \"" + pasien_tb.getText().trim() + "\"\n"
                                   + " }\n"
                                   + "}";

                // Use the JSON string in your request
                String request = requestJson;
                System.out.println("request JSON : "+requestJson);

                JSONObject response = inacbg.request(request);
                System.out.println("response data klaim : "+response); 
                
                if(response.has("metadata")){
                    JSONObject code = response.getJSONObject("metadata");
                    if(code.getInt("code")==200){
                        JOptionPane.showMessageDialog(null,"RESPON : "+code.getString("message"));
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
    }//GEN-LAST:event_validasi_tbActionPerformed

    private void validasi_cvd19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validasi_cvd19ActionPerformed
        
    }//GEN-LAST:event_validasi_cvd19ActionPerformed

    private void jenis_rawatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jenis_rawatItemStateChanged
        // TODO add your handling code here:
        if (jenis_rawat.getSelectedIndex() == 0) {
            naik_turun_kelas.setVisible(true);
            rawat_intensif.setVisible(true);
            kelas_eksekutif.setVisible(false);
            jLabel58.setVisible(true);
            jLabel22.setVisible(true);
            validasi_cvd19.setVisible(true);
            validasi_tb.setVisible(true);
            pasien_tb.setVisible(true);
            co_covid_19.setVisible(true);
            kelas_eksekutif.setSelected(false);
            jLabel47.setVisible(false);
            naik_kelas_pelayanan.setVisible(false);
        } else {
            naik_turun_kelas.setVisible(false);
            rawat_intensif.setVisible(false);
            kelas_eksekutif.setVisible(true);
            jLabel58.setVisible(false);
            jLabel22.setVisible(true);
            validasi_cvd19.setVisible(false);
            validasi_tb.setVisible(true);
            pasien_tb.setVisible(true);
            co_covid_19.setVisible(false);
            kelas_eksekutif.setSelected(false);
            jLabel47.setVisible(true);
            naik_kelas_pelayanan.setVisible(true);
        }
    }//GEN-LAST:event_jenis_rawatItemStateChanged

    private void rawat_intensifItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rawat_intensifItemStateChanged
        // TODO add your handling code here:
        if (rawat_intensif.isSelected() == true) {
            ventilator.setVisible(true);
            jLabel63.setVisible(true);
            hari_intensif.setVisible(true);
            ventilatorItemStateChanged(null);
        } else {
            ventilator.setVisible(false);
            jLabel63.setVisible(false);
            hari_intensif.setVisible(false);
            ventilatorItemStateChanged(null);
        }
    }//GEN-LAST:event_rawat_intensifItemStateChanged

    private void naik_turun_kelasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_naik_turun_kelasItemStateChanged
        // TODO add your handling code here:
        if (naik_turun_kelas.isSelected() == true) {
           jLabel47.setVisible(true);
           naik_kelas_pelayanan.setVisible(true);
           jLabel48.setVisible(true);
           lama_naik_kelas.setVisible(true);
        } else {
           jLabel47.setVisible(false);
           naik_kelas_pelayanan.setVisible(false);
           jLabel48.setVisible(false);
           lama_naik_kelas.setVisible(false); 
        }
    }//GEN-LAST:event_naik_turun_kelasItemStateChanged

    private void kelas_eksekutifItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_kelas_eksekutifItemStateChanged
        // TODO add your handling code here:
        if (kelas_eksekutif.isSelected() == true) {
           jLabel13.setVisible(true);
           tarif_eksekutif.setVisible(true);
        } else {
           jLabel13.setVisible(false);
           tarif_eksekutif.setVisible(false);
        }
    }//GEN-LAST:event_kelas_eksekutifItemStateChanged

    private void ventilatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ventilatorItemStateChanged
        // TODO add your handling code here:
        if (ventilator.isSelected() == true) {
            tgl_intubasi.setVisible(true);
            jam_intubasi.setVisible(true);
            menit_intubasi.setVisible(true);
            tgl_ekstubasi.setVisible(true);
            jam_ekstubasi.setVisible(true);
            menit_ekstubasi.setVisible(true);
            jLabel15.setVisible(true);
            jLabel62.setVisible(true);
            jLabel65.setVisible(true);
            jam_ventilator.setVisible(true);
        } else {
            tgl_intubasi.setVisible(false);
            jam_intubasi.setVisible(false);
            menit_intubasi.setVisible(false);
            tgl_ekstubasi.setVisible(false);
            jam_ekstubasi.setVisible(false);
            menit_ekstubasi.setVisible(false);
            jLabel15.setVisible(false);
            jLabel62.setVisible(false);
            jLabel65.setVisible(false);
            jam_ventilator.setVisible(false);
        }
    }//GEN-LAST:event_ventilatorItemStateChanged

    private void pbKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pbKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_pbKeyPressed

    private void kprKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kprKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_kprKeyPressed

    private void labKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_labKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_labKeyPressed

    private void akomodasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_akomodasiKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_akomodasiKeyPressed

    private void kronisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kronisKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_kronisKeyPressed

    private void bmhpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bmhpKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_bmhpKeyPressed

    private void kslKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kslKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_kslKeyPressed

    private void pnjKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnjKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_pnjKeyPressed

    private void pl_darahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pl_darahKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_pl_darahKeyPressed

    private void riKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_riKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_riKeyPressed

    private void obat_kemoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_obat_kemoKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_obat_kemoKeyPressed

    private void sewa_alatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sewa_alatKeyPressed
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_sewa_alatKeyPressed

    private void pnbKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnbKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_pnbKeyReleased

    private void ahliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ahliKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_ahliKeyReleased

    private void radKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_radKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_radKeyReleased

    private void rehabKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rehabKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_rehabKeyReleased

    private void obatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_obatKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_obatKeyReleased

    private void alkesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alkesKeyReleased
        // TODO add your handling code here:
        hitungtotalbiaya();
    }//GEN-LAST:event_alkesKeyReleased

    private void pnbKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnbKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_pnbKeyTyped

    private void kirimonlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kirimonlineActionPerformed
        // TODO add your handling code here:
//        Sequel.menyimpan("inacbg_cetak_klaim", "'"+Tsep.getText()+"','pages/pdf/"+Tsep.getText()+".pdf'");
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU KIRIM ONLINE PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            try {
                if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
                } else {
                    try {
                        // Create a human-readable JSON string
                        String requestJson ="{\n" +
                                                "\"metadata\": {\n" +
                                                    "\"method\":\"send_claim_individual\"\n" +
                                                "},\n" +
                                                "\"data\": {\n" +
                                                    "\"nomor_sep\":\""+Tsep.getText().trim()+"\"\n" +
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

                    } catch (Exception e) {
                        System.err.println("Error in sitb: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }catch (Exception ex) {
                System.out.println("Notifikasi Bridging Hapus : "+ex);
            }
        }
    }//GEN-LAST:event_kirimonlineActionPerformed

    private void chk_diag_5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_5ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_5.isSelected()==true && !kd_diag_5.getText().trim().isEmpty()) {
            chk_diag_4.setSelected(false);
            chk_diag_3.setSelected(false);
            chk_diag_2.setSelected(false);
            chk_diag_1.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_5.getText().trim()+(kd_diag_1.getText().trim().equals("") ? "" : "#"+kd_diag_1.getText().trim())+
                    (kd_diag_2.getText().trim().equals("") ? "" : "#"+kd_diag_2.getText().trim())+(kd_diag_3.getText().trim().equals("") ? "" : "#"+kd_diag_3.getText().trim())+
                    (kd_diag_4.getText().trim().equals("") ? "" : "#"+kd_diag_4.getText().trim()));
        }else{
            chk_diag_5.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_5ItemStateChanged

    private void chk_diag_1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_1ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_1.isSelected()==true && !kd_diag_1.getText().trim().isEmpty()) {
            chk_diag_2.setSelected(false);
            chk_diag_3.setSelected(false);
            chk_diag_4.setSelected(false);
            chk_diag_5.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_1.getText().trim()+(kd_diag_2.getText().trim().equals("") ? "" : "#"+kd_diag_2.getText().trim())+
                    (kd_diag_3.getText().trim().equals("") ? "" : "#"+kd_diag_3.getText().trim())+(kd_diag_4.getText().trim().equals("") ? "" : "#"+kd_diag_4.getText().trim())+
                    (kd_diag_5.getText().trim().equals("") ? "" : "#"+kd_diag_5.getText().trim()));
        }else{
            chk_diag_1.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_1ItemStateChanged

    private void chk_diag_2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_2ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_2.isSelected()==true && !kd_diag_2.getText().trim().isEmpty()) {
            chk_diag_1.setSelected(false);
            chk_diag_3.setSelected(false);
            chk_diag_4.setSelected(false);
            chk_diag_5.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_2.getText().trim()+(kd_diag_1.getText().trim().equals("") ? "" : "#"+kd_diag_1.getText().trim())+
                    (kd_diag_3.getText().trim().equals("") ? "" : "#"+kd_diag_3.getText().trim())+(kd_diag_4.getText().trim().equals("") ? "" : "#"+kd_diag_4.getText().trim())+
                    (kd_diag_5.getText().trim().equals("") ? "" : "#"+kd_diag_5.getText().trim()));
        }else{
            chk_diag_2.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_2ItemStateChanged

    private void chk_diag_3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_3ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_3.isSelected()==true && !kd_diag_3.getText().trim().isEmpty()) {
            chk_diag_1.setSelected(false);
            chk_diag_2.setSelected(false);
            chk_diag_4.setSelected(false);
            chk_diag_5.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_3.getText().trim()+(kd_diag_1.getText().trim().equals("") ? "" : "#"+kd_diag_1.getText().trim())+
                    (kd_diag_2.getText().trim().equals("") ? "" : "#"+kd_diag_2.getText().trim())+(kd_diag_4.getText().trim().equals("") ? "" : "#"+kd_diag_4.getText().trim())+
                    (kd_diag_5.getText().trim().equals("") ? "" : "#"+kd_diag_5.getText().trim()));
        }else{
            chk_diag_3.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_3ItemStateChanged

    private void chk_diag_4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_diag_4ItemStateChanged
        // TODO add your handling code here:
        if (chk_diag_4.isSelected()==true && !kd_diag_4.getText().trim().isEmpty()) {
            chk_diag_2.setSelected(false);
            chk_diag_3.setSelected(false);
            chk_diag_1.setSelected(false);
            chk_diag_5.setSelected(false);
            
            icd10.setText("");
            icd10.setText(kd_diag_4.getText().trim()+(kd_diag_1.getText().trim().equals("") ? "" : "#"+kd_diag_1.getText().trim())+
                    (kd_diag_2.getText().trim().equals("") ? "" : "#"+kd_diag_2.getText().trim())+(kd_diag_3.getText().trim().equals("") ? "" : "#"+kd_diag_3.getText().trim())+
                    (kd_diag_5.getText().trim().equals("") ? "" : "#"+kd_diag_5.getText().trim()));
        }else{
            chk_diag_4.setSelected(false);
        }
    }//GEN-LAST:event_chk_diag_4ItemStateChanged

    private void ap_5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ap_5KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ap_5KeyPressed

    private void ap_2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ap_2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ap_2KeyPressed

    private void ap_3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ap_3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ap_3KeyPressed

    private void ap_4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ap_4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ap_4KeyPressed

    private void ap_10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ap_10KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ap_10KeyPressed

    private void ap_6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ap_6KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ap_6KeyPressed

    private void ap_7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ap_7KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ap_7KeyPressed

    private void ap_8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ap_8KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ap_8KeyPressed

    private void ap_9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ap_9KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ap_9KeyPressed

    private void finalklaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalklaimActionPerformed
        // TODO add your handling code here:
        Sequel.menyimpan("inacbg_cetak_klaim", "'"+Tsep.getText()+"','pages/pdf/"+Tsep.getText()+".pdf'");
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU FINAL KLAIM PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                                            "\"metadata\": {\n" +
                                                "\"method\":\"claim_final\"\n" +
                                            "},\n" +
                                            "\"data\": {\n" +
                                                "\"nomor_sep\":\""+Tsep.getText().trim()+"\",\n" +
                                                "\"coder_nik\": \""+coder+"\"\n" +
                                            "}\n" +
                                        "}";


                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);
                    JSONObject response = inacbg.request(request);
                    JSONObject code = response.getJSONObject("metadata");
                    System.out.println("response final : "+response);

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

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_finalklaimActionPerformed

    private void hapusKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusKlaimActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU HAPUS KLAIM PASIEN "+TPasien.getText()+" (KLAIM INI AKAN TERHAPUS !!!!!!) ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                                            "\"metadata\": {\n" +
                                                "\"method\":\"delete_claim\"\n" +
                                            "},\n" +
                                            "\"data\": {\n" +
                                                "\"nomor_sep\":\""+Tsep.getText().trim()+"\",\n" +
                                                "\"coder_nik\": \""+coder+"\"\n" +
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
                            Sequel.meghapus("inacbg_data_terkirim", "no_sep", Tsep.getText());
                            Sequel.meghapus("inacbg_grouping_stage1", "no_sep", Tsep.getText());
                            Sequel.meghapus("inacbg_grouping_stage2", "no_sep", Tsep.getText());
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
    }//GEN-LAST:event_hapusKlaimActionPerformed

    private void hapusKlaimKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hapusKlaimKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_hapusKlaimKeyPressed

    private void editKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editKlaimActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,"SUDAH YAKIN MAU EDIT KLAIM PASIEN "+TPasien.getText()+" ?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (TNoRM.getText().trim().equals("") || Tsep.getText().trim().equals("") || no_kartu.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"DATA NOMOR REKAM MEDIS / NOMOR SEP / NOMOR KARTU TIDAK TERISI !!!!!");
            } else {
                try {
                    // Create a human-readable JSON string
                    String requestJson ="{\n" +
                                            "\"metadata\": {\n" +
                                                "\"method\":\"reedit_claim\"\n" +
                                            "},\n" +
                                            "\"data\": {\n" +
                                                "\"nomor_sep\":\""+Tsep.getText().trim()+"\"\n" +
                                            "}\n" +
                                        "}";


                    // Use the JSON string in your request
                    String request = requestJson;
                    System.out.println("request JSON : "+requestJson);
                    JSONObject response = inacbg.request(request);
                    JSONObject code = response.getJSONObject("metadata");
                    System.out.println("response edit : "+response);

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

                } catch (Exception e) {
                    System.err.println("Error in sitb: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_editKlaimActionPerformed

    private void BtnPenjaminKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPenjaminKompi1ActionPerformed
        penjab.isCek();
        penjab.setSize(internalFrame1Kompi1.getWidth() - 20, internalFrame1Kompi1.getHeight() - 20);
        penjab.setLocationRelativeTo(internalFrame1Kompi1);
        penjab.setAlwaysOnTop(false);
        penjab.setVisible(true);
    }//GEN-LAST:event_BtnPenjaminKompi1ActionPerformed

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

    private void BtnPengaturanKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPengaturanKompi1ActionPerformed
        cekPengaturanKompilasi();
        WindowPengaturan.setSize(610, 172);
        WindowPengaturan.setLocationRelativeTo(internalFrame1Kompi1);
        WindowPengaturan.setVisible(true);
    }//GEN-LAST:event_BtnPengaturanKompi1ActionPerformed

    private void BtnKeluarKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarKompi1ActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarKompi1ActionPerformed

    private void BtnKeluarKompi1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKompi1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            dispose();
        }
    }//GEN-LAST:event_BtnKeluarKompi1KeyPressed

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

    private void tbKompilasiKompi1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbKompilasiKompi1MouseClicked
        if (evt.getClickCount() >= 2) {
            evt.consume();
        } else {
            if (tabMode.getRowCount() != 0) {
                try {
                    getData();
                    setDiagnosa(lblNoRawatKompi1.getText(), lblNoRMKompi1.getText(), lblNamaPasienKompi1.getText());
                    tabPane1Kompi1.setSelectedIndex(0);
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbKompilasiKompi1MouseClicked

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

    private void btnInvoiceKompi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvoiceKompi1ActionPerformed
        if (lblNoRawatKompi1.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih pasien terlebih dahulu");
        } else {
            Valid.panggilUrl("berkasrawat/loginlihatbilling.php?act=login&norawat=" + lblNoRawatKompi1.getText() + "&usere=" + koneksiDB.USERHYBRIDWEB() + "&passwordte=" + koneksiDB.PASHYBRIDWEB());
        }
    }//GEN-LAST:event_btnInvoiceKompi1ActionPerformed

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

    private void kirimonline1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kirimonline1ActionPerformed
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
    }//GEN-LAST:event_kirimonline1ActionPerformed

    private void kirimonline2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kirimonline2ActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            BtnGrouperActionPerformed(evt);
            BtnGrouperActionPerformed(evt);
            finalklaimActionPerformed(evt);
            kirimonline1ActionPerformed(evt);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_kirimonline2ActionPerformed

    private void kirimonline3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kirimonline3ActionPerformed
        String aksi = "";
        
        if (btnPDFKlaimINACBGKompi1.isEnabled()) {
            aksi = "&sukses=true&action=selesai";
        }
        
        String url = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() 
            + "/inacbg/index.php?act=DetailKirimSmc&codernik="+lblCoderNIKKompi1.getText()+"&nosep=" + lblNoSEPKompi1.getText() + "&corona=BukanCorona&action=cetak";
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
    }//GEN-LAST:event_kirimonline3ActionPerformed

    private void kirimonline4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kirimonline4ActionPerformed
        BtnValidasiQRKompi1ActionPerformed(evt);
    }//GEN-LAST:event_kirimonline4ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            INACBGData dialog = new INACBGData(new javax.swing.JFrame(), true);
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
    private widget.Button BtnGrouper;
    private widget.Button BtnHapusDiagnosaKompi1;
    private widget.Button BtnKeluar;
    private widget.Button BtnKeluarKompi1;
    private widget.Button BtnPengaturanKompi1;
    private widget.Button BtnPenjaminKompi1;
    private widget.Button BtnPilihAplikasiPDF;
    private widget.Button BtnResetPengaturan;
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
    private widget.panelisi FormInput;
    private widget.TextBox KdPjKompi1;
    private widget.Label LCountKompi1;
    private widget.TextBox NamaPjKompi1;
    private widget.panelisi PanelContentINACBGKompi1;
    private widget.ScrollPane ScrollKompi1;
    private widget.TextBox TCariKompi1;
    private widget.TextBox TMaxMemory;
    private widget.TextBox TNoRM;
    private widget.TextBox TPasien;
    private widget.TextBox TPathAplikasiPDF;
    private widget.TabPane TabRawat;
    private widget.TextBox Tsep;
    private javax.swing.JDialog WindowPengaturan;
    private widget.TextBox ahli;
    private widget.TextBox akomodasi;
    private widget.TextBox alkes;
    private widget.TextBox ap_1;
    private widget.TextBox ap_10;
    private widget.TextBox ap_2;
    private widget.TextBox ap_3;
    private widget.TextBox ap_4;
    private widget.TextBox ap_5;
    private widget.TextBox ap_6;
    private widget.TextBox ap_7;
    private widget.TextBox ap_8;
    private widget.TextBox ap_9;
    private widget.TextBox berat_bayi;
    private widget.TextBox bmhp;
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
    private widget.ComboBox cara_masuk;
    private javax.swing.JCheckBox chk_diag_1;
    private javax.swing.JCheckBox chk_diag_2;
    private javax.swing.JCheckBox chk_diag_3;
    private javax.swing.JCheckBox chk_diag_4;
    private javax.swing.JCheckBox chk_diag_5;
    private widget.TextBox chronic;
    private widget.TextBox co_covid_19;
    private widget.ComboBox cob;
    private widget.TextBox distole;
    private widget.TextBox dpjp;
    private widget.Button editKlaim;
    private javax.swing.JFileChooser fc;
    private widget.Button finalklaim;
    private widget.ComboBox hak_kelas;
    private widget.Button hapusKlaim;
    private widget.Button hapusKlaimKompi1;
    private widget.TextBox hari_intensif;
    private widget.TextBox icd10;
    private widget.TextBox icd9;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame12;
    private widget.InternalFrame internalFrame1Kompi1;
    private widget.Label jLabel11Kompi1;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel14Kompi1;
    private widget.Label jLabel15;
    private widget.Label jLabel15Kompi1;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel17Kompi1;
    private widget.Label jLabel18Kompi1;
    private widget.Label jLabel19;
    private widget.Label jLabel19Kompi1;
    private widget.Label jLabel20;
    private widget.Label jLabel20Kompi1;
    private widget.Label jLabel21;
    private widget.Label jLabel21Kompi1;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel25Kompi1;
    private widget.Label jLabel26;
    private widget.Label jLabel26Kompi1;
    private widget.Label jLabel27;
    private widget.Label jLabel27Kompi1;
    private widget.Label jLabel28;
    private widget.Label jLabel28Kompi1;
    private widget.Label jLabel29;
    private widget.Label jLabel29Kompi1;
    private widget.Label jLabel30;
    private widget.Label jLabel30Kompi1;
    private widget.Label jLabel31;
    private widget.Label jLabel31Kompi1;
    private widget.Label jLabel32;
    private widget.Label jLabel32Kompi1;
    private widget.Label jLabel33;
    private widget.Label jLabel33Kompi1;
    private widget.Label jLabel34;
    private widget.Label jLabel34Kompi1;
    private widget.Label jLabel35;
    private widget.Label jLabel35Kompi1;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel39;
    private widget.Label jLabel40;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel48;
    private widget.Label jLabel49;
    private widget.Label jLabel50;
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
    private widget.Label jLabel62;
    private widget.Label jLabel63;
    private widget.Label jLabel64;
    private widget.Label jLabel65;
    private widget.Label jLabel66;
    private widget.Label jLabel67;
    private widget.Label jLabel68;
    private widget.Label jLabel69;
    private widget.Label jLabel6Kompi1;
    private widget.Label jLabel70;
    private widget.Label jLabel71;
    private widget.Label jLabel72;
    private widget.Label jLabel73;
    private widget.Label jLabel74;
    private widget.Label jLabel75;
    private widget.Label jLabel76;
    private widget.Label jLabel77;
    private widget.Label jLabel78;
    private widget.Label jLabel7Kompi1;
    private widget.Label jLabel8Kompi1;
    private javax.swing.JPanel jPanel1Kompi1;
    private javax.swing.JPanel jPanel2Kompi1;
    private javax.swing.JPanel jPanel3Kompi1;
    private javax.swing.JPanel jPanel4Kompi1;
    private javax.swing.JPanel jPanel5Kompi1;
    private widget.ComboBox jam_ekstubasi;
    private widget.ComboBox jam_intubasi;
    private widget.ComboBox jam_keluar;
    private widget.ComboBox jam_masuk;
    private widget.TextBox jam_ventilator;
    private widget.ComboBox jaminan;
    private widget.TextBox jenis_kelamin;
    private widget.ComboBox jenis_rawat;
    private widget.TextBox kd_diag_1;
    private widget.TextBox kd_diag_2;
    private widget.TextBox kd_diag_3;
    private widget.TextBox kd_diag_4;
    private widget.TextBox kd_diag_5;
    private widget.TextBox kd_psdr_1;
    private widget.TextBox kd_psdr_2;
    private widget.TextBox kd_psdr_3;
    private widget.TextBox kd_psdr_4;
    private widget.TextBox kd_psdr_5;
    private javax.swing.JCheckBox kelas_eksekutif;
    private widget.Button kirimonline;
    private widget.Button kirimonline1;
    private widget.Button kirimonline2;
    private widget.Button kirimonline3;
    private widget.Button kirimonline4;
    private widget.TextBox kpr;
    private widget.TextBox kronis;
    private widget.TextBox ksl;
    private widget.TextBox lab;
    private widget.Label label19Kompi1;
    private widget.TextBox lama_naik_kelas;
    private widget.Label lblCoderNIKKompi1;
    private widget.Label lblNamaPasienKompi1;
    private widget.Label lblNoRMKompi1;
    private widget.Label lblNoRawatKompi1;
    private widget.Label lblNoSEPKompi1;
    private widget.Label lblStatusRawatKompi1;
    private widget.Label lblTglSEP1Kompi1;
    private widget.Label lblTglSEPKompi1;
    private widget.ComboBox menit_ekstubasi;
    private widget.ComboBox menit_intubasi;
    private widget.ComboBox menit_keluar;
    private widget.ComboBox menit_masuk;
    private widget.ComboBox naik_kelas_pelayanan;
    private javax.swing.JCheckBox naik_turun_kelas;
    private widget.TextBox nm_diag_1;
    private widget.TextBox nm_diag_2;
    private widget.TextBox nm_diag_3;
    private widget.TextBox nm_diag_4;
    private widget.TextBox nm_diag_5;
    private widget.TextBox nm_psdr_1;
    private widget.TextBox nm_psdr_2;
    private widget.TextBox nm_psdr_3;
    private widget.TextBox nm_psdr_4;
    private widget.TextBox nm_psdr_5;
    private widget.TextBox no_kartu;
    private widget.TextBox no_rawat;
    private widget.TextBox obat;
    private widget.TextBox obat_kemo;
    private widget.PanelBiasa panelBiasa1Kompi1;
    private widget.PanelBiasa panelBiasa2Kompi1;
    private laporan.PanelDiagnosaSmc panelDiagnosaSmcKompi1;
    private widget.panelisi panelGlass10Kompi1;
    private widget.panelisi panelGlass11Kompi1;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass8Kompi1;
    private widget.panelisi panelInvoicesKompi1;
    private widget.TextBox pasien_tb;
    private widget.TextBox pb;
    private widget.TextBox pl_darah;
    private widget.TextBox pnb;
    private widget.TextBox pnb1;
    private widget.TextBox pnj;
    private widget.TextBox rad;
    private javax.swing.JCheckBox rawat_intensif;
    private widget.TextBox rehab;
    private widget.TextBox ri;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane1Kompi1;
    private widget.TextBox sewa_alat;
    private widget.TextBox sistole;
    private widget.ComboBox stage_2;
    private widget.ComboBox status_pulang;
    private widget.TextBox sub_acute;
    private widget.TabPane tabPane1Kompi1;
    private widget.TextBox tarif_eksekutif;
    private widget.Table tbKompilasiKompi1;
    private widget.Tanggal tgl_ekstubasi;
    private widget.Tanggal tgl_intubasi;
    private widget.Tanggal tgl_keluar;
    private widget.TextBox tgl_lahir;
    private widget.Tanggal tgl_masuk;
    private widget.Label total_rupiah;
    private widget.Label total_rupiah1;
    private widget.Button validasi_cvd19;
    private widget.Button validasi_tb;
    private javax.swing.JCheckBox ventilator;
    // End of variables declaration//GEN-END:variables

    public void emptTeks() {
        TNoRM.setText("");
        TPasien.setText("");
        Tsep.setText("");
        tgl_masuk.setDate(new Date());
        tgl_masuk.requestFocus();
    }
    
    public void setDiagnosa(String norw, String norm, String pasien) {
        //cek koder
        try {

            rs = koneksi.prepareStatement("select * from bridging_sep where no_rawat='"+norw+"'").executeQuery();
            while (rs.next()) {

                dokterkonsul = Sequel.cariIsi("select concat(', ',dokter.nm_dokter) from reg_periksa inner "
                                    + "join konsultasi_medik on konsultasi_medik.no_rawat=reg_periksa.no_rawat inner join dokter on konsultasi_medik.kd_dokter_dikonsuli=dokter.kd_dokter where "
                                    + "reg_periksa.no_rawat='"+no_rawat.getText()+"'");
                coder = Sequel.cariIsi("select no_ik from inacbg_coder_nik where nik='"+akses.getkode()+"'");
                if (akses.getkode().equals("Admin Utama")){
                   coder=Sequel.cariIsi("select no_ik from inacbg_coder_nik LIMIT 1");
                } else if (!coder.equals("")){
                   coder=coder;
                } else {
                    JOptionPane.showMessageDialog(null,"ANDA BUKAN CODER KLAIM, JADINYA TIDAK BISA !!!!!");
                    BtnKeluarActionPerformed(null);
                }

                no_rawat.setText(norw);
                no_kartu.setText(rs.getString("no_kartu"));  
                Tsep.setText(rs.getString("no_sep"));  
                
                TPasien.setText(pasien);   
                jenis_kelamin.setText(rs.getString("jkel"));
                tgl_lahir.setText(rs.getString("tanggal_lahir"));
                TNoRM.setText(norm);
                
                if (rs.getString("jnspelayanan").equals("2")) {
                    hak_kelas.setSelectedIndex(3);
                } else {
                    switch (rs.getString("klsrawat")) {
                        case "1":
                            hak_kelas.setSelectedIndex(1);
                            break;
                        case "2":
                            hak_kelas.setSelectedIndex(2);
                            break;
                        case "3":
                            hak_kelas.setSelectedIndex(3);
                            break;
                        default:
                            throw new AssertionError();
                    }
                }
                
                
//                pnb.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(PNB)%' and no_rawat='"+no_rawat.getText()+"'"));
                pnb.setText("0");
                pb.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Operasi'"));
                ksl.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where (status='Ranap Dokter' or status='Ralan Dokter') and no_rawat='"+no_rawat.getText()+"'"));
//                ksl.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(KSL)%' and no_rawat='"+no_rawat.getText()+"'"));
                ahli.setText("0");
//                kpr.setText(Sequel.cariIsi("select (select ifnull(sum(totalbiaya),0) from billing where (status='Ranap Dokter Paramedis' or status='Ralan Dokter Paramedis') and no_rawat='"+no_rawat.getText()+"') + (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(PSB)%' and no_rawat='"+no_rawat.getText()+"')"));
                kpr.setText(Sequel.cariIsi("select (select ifnull(sum(totalbiaya),0) from billing where (status='Ranap Dokter Paramedis' or status='Ralan Dokter Paramedis') and no_rawat='"+no_rawat.getText()+"')"));
                rad.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Radiologi'"));
                lab.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Laborat'"));
//                alkes.setText((Sequel.cariIsi("select (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(ALKES)%' and no_rawat='"+no_rawat.getText()+"') + (select ifnull(sum(totalbiaya),0) from billing where (nm_perawatan like '%Lensa%' OR nm_perawatan like '%LENSA%' OR nm_perawatan like '%lensa%') and no_rawat='"+no_rawat.getText()+"')")));
//                kronis.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(kronis)%' and no_rawat='"+no_rawat.getText()+"' and status='Obat'"));
//                obat_kemo.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(kemo)%' and no_rawat='"+no_rawat.getText()+"' and status='Obat'"));
                alkes.setText("0");
                kronis.setText("0");
                obat_kemo.setText("0");
//                obat.setText(Sequel.cariIsi("select (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan not like '%(ALKES)%' and no_rawat='"+no_rawat.getText()+"' and status='Obat') + (select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Retur Obat') + (select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Resep Pulang') - (select (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%kronis%' and no_rawat='"+no_rawat.getText()+"' and status='Obat') - (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%kemo%' and no_rawat='"+no_rawat.getText()+"' and status='Obat'))"));
                obat.setText(Sequel.cariIsi("select (select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Obat')"));
//                pnj.setText(Sequel.cariIsi("select (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan not like '%selisih%' and no_rawat='"+no_rawat.getText()+"' and status='Tambahan') + (select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(PNG)%' and no_rawat='"+no_rawat.getText()+"')"));
//                pl_darah.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(DRH)%' and no_rawat='"+no_rawat.getText()+"'"));
//                bmhp.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(BMHP)%' and no_rawat='"+no_rawat.getText()+"'"));
//                sewa_alat.setText(Sequel.cariIsi("select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(ALM)%' and no_rawat='"+no_rawat.getText()+"'"));
                pnj.setText("0");
                pl_darah.setText("0");
                bmhp.setText("0");
                sewa_alat.setText("0");
                akomodasi.setText(Sequel.cariIsi("SELECT (select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Kamar')"));
//                akomodasi.setText(Sequel.cariIsi("SELECT (select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Kamar')+(select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Harian')+(select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Service')+(select ifnull(sum(totalbiaya),0) from billing where no_rawat='"+no_rawat.getText()+"' and status='Registrasi')+(select ifnull(sum(totalbiaya),0) from billing where nm_perawatan like '%(HRP)%' and no_rawat='"+no_rawat.getText()+"')"));
                rehab.setText("0");
                ri.setText("0");
                sistole.setText(Sequel.cariIsi("select tensi from pemeriksaan_ranap where no_rawat='"+no_rawat.getText()+"'"));

                hitungtotalbiaya();

                try {
                    rs = koneksi.prepareStatement("select reg_periksa.tgl_registrasi, reg_periksa.jam_reg, reg_periksa.status_lanjut, reg_periksa.status_bayar, dokter.nm_dokter from reg_periksa inner join "
                            + "dokter on reg_periksa.kd_dokter=dokter.kd_dokter where reg_periksa.no_rawat='"+no_rawat.getText()+"'").executeQuery();
                    if (rs.next()) {
                        if (rs.getString("status_lanjut").equals("Ralan")) {
                            tgl_masuk.setDate(rs.getDate("tgl_registrasi"));
                            tgl_keluar.setDate(rs.getDate("tgl_registrasi"));
                            jam_masuk.setSelectedItem(rs.getString("jam_reg").substring(0,2));
                            jam_keluar.setSelectedItem(rs.getString("jam_reg").substring(0,2));
                            menit_masuk.setSelectedItem(rs.getString("jam_reg").substring(3,5));
                            menit_keluar.setSelectedItem(rs.getString("jam_reg").substring(3,5));
                            dpjp.setText(rs.getString("nm_dokter")+dokterkonsul);
                        } else {
                            jenis_rawat.setSelectedIndex(0);

                            tgl_masuk_ranap = Sequel.cariIsi("SELECT DISTINCT(concat(tgl_masuk,' ',jam_masuk)) as jam_masuk FROM kamar_inap WHERE no_rawat='"+no_rawat.getText()+"' ORDER BY tgl_masuk ASC LIMIT 1");
                            tgl_keluar_ranap = Sequel.cariIsi("SELECT DISTINCT(concat(tgl_keluar,' ',jam_keluar)) as jam_keluar FROM kamar_inap WHERE no_rawat='"+no_rawat.getText()+"' AND tgl_keluar<>'0000-00-00' ORDER BY tgl_masuk DESC LIMIT 1");

                            LocalDateTime tanggalWaktu1 = LocalDateTime.parse(tgl_masuk_ranap, formatter);
                            LocalDateTime tanggalWaktu2 = LocalDateTime.parse(tgl_keluar_ranap, formatter);

                            // Menghitung selisih hari
                            long selisihHari = ChronoUnit.DAYS.between(tanggalWaktu1, tanggalWaktu2);
                            long selisihJam = ChronoUnit.HOURS.between(tanggalWaktu1, tanggalWaktu2);

                            try{
                                rs3=koneksi.prepareStatement(
                                    "select dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat='"+no_rawat.getText()+"'").executeQuery();

                                while (rs3.next()) {
                                    dpjp_ranap = dpjp_ranap+rs3.getString("nm_dokter")+", ";
                                }
                            } catch (Exception e) {
                                System.out.println("Status Lanjut : "+e);
                            }

                            Valid.SetTgl(tgl_masuk,tgl_masuk_ranap.substring(0,10));
                            Valid.SetTgl(tgl_keluar,tgl_keluar_ranap.substring(0,10));
                            jam_masuk.setSelectedItem(tgl_masuk_ranap.substring(11,13));
                            jam_keluar.setSelectedItem(tgl_keluar_ranap.substring(11,13));
                            menit_masuk.setSelectedItem(tgl_masuk_ranap.substring(14,16));
                            menit_keluar.setSelectedItem(tgl_keluar_ranap.substring(14,16));
                            dpjp.setText(dpjp_ranap);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("error eklaim : "+e);
                }
                
//                diagnosa
                try {
                    rsdiagnosa = koneksi.prepareStatement("select penyakit.kd_penyakit, penyakit.nm_penyakit from diagnosa_pasien inner join penyakit on diagnosa_pasien.kd_penyakit=penyakit.kd_penyakit where diagnosa_pasien.no_rawat='"+norw+"' "
                            + "order by diagnosa_pasien.prioritas asc").executeQuery();
                    i=1;
                    while (rsdiagnosa.next()) {
//                        System.out.println("data "+rsdiagnosa.getString("kd_penyakit"));
                        
                        if (i == 1) {
                            kd_diag_1.setText(rsdiagnosa.getString("kd_penyakit"));
                            nm_diag_1.setText(rsdiagnosa.getString("nm_penyakit"));
                        }
                        if (i == 2) {
                            kd_diag_2.setText(rsdiagnosa.getString("kd_penyakit"));
                            nm_diag_2.setText(rsdiagnosa.getString("nm_penyakit"));
                        }
                        if (i == 3) {
                            kd_diag_3.setText(rsdiagnosa.getString("kd_penyakit"));
                            nm_diag_3.setText(rsdiagnosa.getString("nm_penyakit"));
                        }
                        if (i == 4) {
                            kd_diag_4.setText(rsdiagnosa.getString("kd_penyakit"));
                            nm_diag_4.setText(rsdiagnosa.getString("nm_penyakit"));
                        }
                        if (i == 5) {
                            kd_diag_5.setText(rsdiagnosa.getString("kd_penyakit"));
                            nm_diag_5.setText(rsdiagnosa.getString("nm_penyakit"));
                        }
                        i++;
                    }
                } catch (Exception e) {
                    System.out.println("ICD 10 : "+e);
                }
                
//                prosedur
                try {
                    rsprosedur = koneksi.prepareStatement("select icd9.kode, icd9.deskripsi_panjang from prosedur_pasien inner join icd9 on prosedur_pasien.kode=icd9.kode where prosedur_pasien.no_rawat='"+norw+"' "
                            + "order by prosedur_pasien.prioritas asc").executeQuery();
                    j=1;
                    while (rsprosedur.next()) {
//                        System.out.println("data "+rsprosedur.getString("kode"));
                        
                        if (j == 1) {
                            kd_psdr_1.setText(rsprosedur.getString("kode"));
                            nm_psdr_1.setText(rsprosedur.getString("deskripsi_panjang"));
                        }
                        if (j == 2) {
                            kd_psdr_2.setText(rsprosedur.getString("kode"));
                            nm_psdr_2.setText(rsprosedur.getString("deskripsi_panjang"));
                        }
                        if (j == 3) {
                            kd_psdr_3.setText(rsprosedur.getString("kode"));
                            nm_psdr_3.setText(rsprosedur.getString("deskripsi_panjang"));
                        }
                        if (j == 4) {
                            kd_psdr_4.setText(rsprosedur.getString("kode"));
                            nm_psdr_4.setText(rsprosedur.getString("deskripsi_panjang"));
                        }
                        if (j == 5) {
                            kd_psdr_5.setText(rsprosedur.getString("kode"));
                            nm_psdr_5.setText(rsprosedur.getString("deskripsi_panjang"));
                        }
                        j++;
                    }
                    
                    icd9.setText("");
                    icd9.setText(kd_psdr_1.getText().trim()+(kd_psdr_2.getText().trim().equals("") ? "" : "#"+kd_psdr_2.getText())
                                    +(kd_psdr_3.getText().trim().equals("") ? "" : "#"+kd_psdr_3.getText())+(kd_psdr_4.getText().trim().equals("") ? "" : "#"+kd_psdr_4.getText())
                                    +(kd_psdr_5.getText().trim().equals("") ? "" : "#"+kd_psdr_5.getText()));
                } catch (Exception e) {
                    System.out.println("ICD 9 : "+e);
                }
                
               
                if (!kd_diag_1.getText().trim().equals("")) {
                   chk_diag_1.setSelected(true);
                }
                
                jLabel49.setVisible(false);                
                jLabel53.setVisible(false);
                stage_2.setVisible(false);

                jenis_rawatItemStateChanged(null);
                rawat_intensifItemStateChanged(null);
                kelas_eksekutifItemStateChanged(null);
                naik_turun_kelasItemStateChanged(null);
            }
                
        } catch (Exception e) {
            System.out.println("error : "+e);
        }
    }
    
    public void hitungtotalbiaya(){
        if (pnb.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pnb.requestFocus();
        }
        else if (pb.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pb.requestFocus();
        }
        else if (ahli.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            ahli.requestFocus();
        }
        else if (rad.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            rad.requestFocus();
        }
        else if (rehab.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            rehab.requestFocus();
        }
        else if (obat.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            obat.requestFocus();
        }
        else if (alkes.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            alkes.requestFocus();
        }
        else if (kpr.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            kpr.requestFocus();
        }
        else if (lab.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            lab.requestFocus();
        }
        else if (akomodasi.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            akomodasi.requestFocus();
        }
        else if (kronis.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            kronis.requestFocus();
        }
        else if (bmhp.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            bmhp.requestFocus();
        }
        else if (ksl.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            ksl.requestFocus();
        }
        else if (pnb.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pnb.requestFocus();
        }
        else if (pnj.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pnj.requestFocus();
        }
        else if (pl_darah.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            pl_darah.requestFocus();
        }
        else if (ri.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            ri.requestFocus();
        }
        else if (obat_kemo.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            obat_kemo.requestFocus();
        }
        else if (sewa_alat.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"DATA INI TIDAK BOLEH KOSONG DAN HANYA BOLEH ANGKA !!!!");
            sewa_alat.requestFocus();
        } else {
            total_biaya = (Integer.parseInt(pnb.getText()) + Integer.parseInt(pb.getText()) + Integer.parseInt(ksl.getText()) + Integer.parseInt(ahli.getText()) + Integer.parseInt(kpr.getText()) +  Integer.parseInt(rad.getText()) + Integer.parseInt(lab.getText()) + Integer.parseInt(alkes.getText()) + Integer.parseInt(kronis.getText()) + Integer.parseInt(obat_kemo.getText()) + Integer.parseInt(obat.getText()) + Integer.parseInt(pnj.getText()) + Integer.parseInt(pl_darah.getText()) + Integer.parseInt(bmhp.getText()) + Integer.parseInt(sewa_alat.getText()) + Integer.parseInt(rehab.getText()) + Integer.parseInt(ri.getText()) + Integer.parseInt(akomodasi.getText()) );
            total_rupiah.setText(String.valueOf(Valid.SetAngka(total_biaya)));
        }
    }
    
    
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
    

    public void emptTeksKompi1() {
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
