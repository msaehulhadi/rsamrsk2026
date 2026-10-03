/*
  Dilarang keras menggandakan/mengcopy/menyebarkan/membajak/mendecompile 
  Software ini dalam bentuk apapun tanpa seijin pembuat software
  (Khanza.Soft Media). Bagi yang sengaja membajak softaware ini ta
  npa ijin, kami sumpahi sial 1000 turunan, miskin sampai 500 turu
  nan. Selalu mendapat kecelakaan sampai 400 turunan. Anak pertama
  nya cacat tidak punya kaki sampai 300 turunan. Susah cari jodoh
  sampai umur 50 tahun sampai 200 turunan. Ya Alloh maafkan kami 
  karena telah berdoa buruk, semua ini kami lakukan karena kami ti
  dak pernah rela karya kami dibajak tanpa ijin.
 */

package simrskhanza;

import surat.SuratKontrol;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;
import inventory.DlgPemberianObat;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import inventory.DlgCariObat;
import inventory.DlgCopyResep;
import inventory.DlgPeresepanDokter;
import inventory.InventoryResepLuar;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariPegawai;
import keuangan.DlgJnsPerawatanRalan;
import keuangan.Jurnal;
import keuangan.Jurnal_rsk;
import laporan.DlgBerkasRawat;
import permintaan.DlgBookingOperasi;
import rekammedis.RMDataResumePasien;
import permintaan.DlgPermintaanLaboratorium;
import permintaan.DlgPermintaanPelayananInformasiObat;
import permintaan.DlgPermintaanRadiologi;
import rekammedis.RMCari5SOAPTerakhir;
import rekammedis.RMChecklistPostOperasi;
import rekammedis.RMChecklistPreOperasi;
import rekammedis.RMDataAsuhanGizi;
import rekammedis.RMDataCatatanCekGDS;
import rekammedis.RMDataCatatanObservasiIGD;
import rekammedis.RMDataMonitoringAsuhanGizi;
import rekammedis.RMDataMonitoringReaksiTranfusi;
import rekammedis.RMDataSkriningGiziLanjut;
import rekammedis.RMEdukasiPasienKeluargaRawatJalan;
import rekammedis.RMHasilPemeriksaanUSG;
import rekammedis.RMKonselingFarmasi;
import rekammedis.RMMCU;
import rekammedis.RMPemantauanPEWS;
import rekammedis.RMPemantauanPEWSD;
import rekammedis.RMPenilaianAwalKeperawatanBayiAnak;
import rekammedis.RMPenilaianAwalKeperawatanGigi;
import rekammedis.RMPenilaianAwalKeperawatanIGD;
import rekammedis.RMPenilaianAwalKeperawatanKebidanan;
import rekammedis.RMPenilaianAwalKeperawatanRalan;
import rekammedis.RMPenilaianAwalKeperawatanRalanPsikiatri;
import rekammedis.RMPenilaianAwalMedisIGD;
import rekammedis.RMPenilaianAwalMedisRalanAnak;
import rekammedis.RMPenilaianAwalMedisRalanBedah;
import rekammedis.RMPenilaianAwalMedisRalanDewasa;
import rekammedis.RMPenilaianAwalMedisRalanGeriatri;
import rekammedis.RMPenilaianAwalMedisRalanKandungan;
import rekammedis.RMPenilaianAwalMedisRalanMata;
import rekammedis.RMPenilaianAwalMedisRalanNeurologi;
import rekammedis.RMPenilaianAwalMedisRalanOrthopedi;
import rekammedis.RMPenilaianAwalMedisRalanPenyakitDalam;
import rekammedis.RMPenilaianAwalMedisRalanPsikiatrik;
import rekammedis.RMPenilaianAwalMedisRalanTHT;
import rekammedis.RMPenilaianFisioterapi;
import rekammedis.RMPenilaianKorbanKekerasan;
import rekammedis.RMPenilaianLanjutanRisikoJatuhAnak;
import rekammedis.RMPenilaianLanjutanRisikoJatuhDewasa;
import rekammedis.RMPenilaianLanjutanRisikoJatuhLansia;
import rekammedis.RMPenilaianPasienPenyakitMenular;
import rekammedis.RMPenilaianPasienTerminal;
import rekammedis.RMPenilaianPreAnastesi;
import rekammedis.RMPenilaianPreOperasi;
import rekammedis.RMPenilaianPsikologi;
import rekammedis.RMPenilaianTambahanGeriatri;
import rekammedis.RMRekonsiliasiObat;
import rekammedis.RMRiwayatPerawatan;
import rekammedis.RMSignInSebelumAnastesi;
import rekammedis.RMSignOutSebelumMenutupLuka;
import rekammedis.RMSkriningNutrisiAnak;
import rekammedis.RMSkriningNutrisiDewasa;
import rekammedis.RMSkriningNutrisiLansia;
import rekammedis.RMTimeOutSebelumInsisi;
import rekammedis.RMTransferPasienAntarRuang;
import rekammedis.RMTriaseIGD;
import rekammedis.RMUjiFungsiKFR;

/**
 *
 * @author dosen
 */
public final class DlgRawatJalanAPS extends javax.swing.JDialog {
    private final DefaultTableModel tabModeDr,tabModePr,tabModeDrPr,
            tabModePemeriksaan,tabModeObstetri,tabModeGinekologi,
            TabModeTindakan,TabModeTindakan2,TabModeTindakan3,TabModeCatatan;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private DlgCariPasien pasien=new DlgCariPasien(null,false);
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    public  DlgCariPetugas petugas=new DlgCariPetugas(null,false);    
    public  DlgCariPegawai pegawai=new DlgCariPegawai(null,false);   
    private RMCari5SOAPTerakhir soapterakhir=new RMCari5SOAPTerakhir(null,false);       
    private PreparedStatement ps,ps2,ps3,ps4,ps5,ps6,pstindakan,psset_tarif,psrekening,psrekening_rsk;
    private ResultSet rs,rstindakan,rsset_tarif,rsrekening,rsrekening_rsk;
    private int i=0,jmlparsial=0,jml=0,index=0,tinggi=0;
    private String aktifkanparsial="no",kode_poli="",kd_pj="",poli_ralan="No",cara_bayar_ralan="No",
            Suspen_Piutang_Tindakan_Ralan="",Tindakan_Ralan="",Beban_Jasa_Medik_Dokter_Tindakan_Ralan="",Utang_Jasa_Medik_Dokter_Tindakan_Ralan="",
            Beban_Jasa_Medik_Paramedis_Tindakan_Ralan="",Utang_Jasa_Medik_Paramedis_Tindakan_Ralan="",Beban_KSO_Tindakan_Ralan="",Utang_KSO_Tindakan_Ralan="",
            Beban_Jasa_Sarana_Tindakan_Ralan="",Utang_Jasa_Sarana_Tindakan_Ralan="",HPP_BHP_Tindakan_Ralan="",Persediaan_BHP_Tindakan_Ralan="",
            Beban_Jasa_Menejemen_Tindakan_Ralan="",Utang_Jasa_Menejemen_Tindakan_Ralan="",kd_jenis_prw="",pendapatan_tindakan="",beban_jasa_dokter="",utang_jasa_dokter="",beban_jasa_paramedis="",utang_jasa_paramedis="",beban_kso="",utang_kso="",hpp_persediaan="",persediaan_bhp="",beban_jasa_sarana="",utang_jasa_sarana="",beban_menejemen="",utang_menejemen="";
    private final Properties prop = new Properties();
    private boolean[] pilih; 
    private String[] kode,nama,kategori;
    private double[] totaltnd,bagianrs,bhp,jmdokter,jmperawat,kso,menejemen;
    private boolean sukses=false;
    private double ttljmdokter=0,ttljmperawat=0,ttlkso=0,ttljasasarana=0,ttlbhp=0,ttlmenejemen=0,ttlpendapatan=0;
    private Jurnal jur=new Jurnal();
    private Jurnal_rsk jurnal_rsk=new Jurnal_rsk();

    /** Creates new form DlgPerawatan
     * @param parent
     * @param modal */
    public DlgRawatJalanAPS(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(8,1);
        setSize(885,674);
        tabModeDr=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M.","Nama Pasien","Perawatan/Tindakan","Kode Dokter","Dokter Yg Menangani","Tgl.Rawat","Jam Rawat","Biaya","Kode","Tarif Dokter","KSO","Jasa Sarana","BHP","Menejemen"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };


        

        tabModePr=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M.","Nama Pasien","Perawatan/Tindakan","NIP","Petugas Yg Menangani","Tgl.Rawat","Jam Rawat","Biaya","Kode","Tarif Perawat","KSO","Jasa Sarana","BHP","Menejemen"}){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };

        
        
        tabModeDrPr=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M.","Nama Pasien","Perawatan/Tindakan","Kode Dokter","Dokter Yg Menangani","NIP","Petugas Yg Menangani","Tgl.Rawat","Jam Rawat",
            "Biaya","Kode","Tarif Dokter","Tarif Petugas","KSO","Jasa Sarana","BHP","Menejemen"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class,java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRawatDrPr.setModel(tabModeDrPr);
        tbRawatDrPr.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRawatDrPr.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 19; i++) {
            TableColumn column = tbRawatDrPr.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(105);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(180);
            }else if(i==4){
                column.setPreferredWidth(180);
            }else if(i==5){
                column.setPreferredWidth(90);
            }else if(i==6){
                column.setPreferredWidth(180);
            }else if(i==7){
                column.setPreferredWidth(90);
            }else if(i==8){
                column.setPreferredWidth(180);
            }else if(i==9){
                column.setPreferredWidth(80);
            }else if(i==10){
                column.setPreferredWidth(75);
            }else if(i==11){
                column.setPreferredWidth(90);
            }else if(i==12){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==13){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==14){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==15){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==16){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==17){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==18){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbRawatDrPr.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModePemeriksaan=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M.","Nama Pasien","Tgl.Rawat","Jam","Suhu(C)","Tensi","Nadi(/menit)",
            "Respirasi(/menit)","Tinggi(Cm)","Berat(Kg)","SpO2(%)","GCS(E,V,M)","Kesadaran","Subjek","Objek","Alergi",
            "L.P.(Cm)","Plan","Asesmen","Instruksi","Evaluasi","NIP","Dokter/Paramedis","Profesi/Jabatan"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbPemeriksaan.setModel(tabModePemeriksaan);
        tbPemeriksaan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbPemeriksaan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 26; i++) {
            TableColumn column = tbPemeriksaan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(105);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(150);
            }else if(i==4){
                column.setPreferredWidth(65);
            }else if(i==5){
                column.setPreferredWidth(50);
            }else if(i==6){
                column.setPreferredWidth(45);
            }else if(i==7){
                column.setPreferredWidth(60);
            }else if(i==8){
                column.setPreferredWidth(73);
            }else if(i==9){
                column.setPreferredWidth(90);
            }else if(i==10){
                column.setPreferredWidth(63);
            }else if(i==11){
                column.setPreferredWidth(57);
            }else if(i==12){
                column.setPreferredWidth(50);
            }else if(i==13){
                column.setPreferredWidth(64);
            }else if(i==14){
                column.setPreferredWidth(90);
            }else if(i==15){
                column.setPreferredWidth(180);
            }else if(i==16){
                column.setPreferredWidth(180);
            }else if(i==17){
                column.setPreferredWidth(130);
            }else if(i==18){
                column.setPreferredWidth(50);
            }else if(i==19){
                column.setPreferredWidth(180);
            }else if(i==20){
                column.setPreferredWidth(180);
            }else if(i==21){
                column.setPreferredWidth(180);
            }else if(i==22){
                column.setPreferredWidth(180);
            }else if(i==23){
                column.setPreferredWidth(80);
            }else if(i==24){
                column.setPreferredWidth(150);
            }else if(i==25){
                column.setPreferredWidth(100);
            }
        }
        tbPemeriksaan.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeObstetri=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M","Nama Pasien","Tgl.Rawat","Jam Rawat",
            "Tinggi Fundus","Janin", "Letak","Panggul","Denyut","Kontraksi",
            "Kualitas Mnt", "Kualitas Detik","Fluksus","Albus","Vulva",
            "Portio","Dalam","Tebal","Arah","Pembukaan","Penurunan",
            "Denominator","Ketuban","Feto"}) {
             @Override public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
            }
            Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                java.lang.Object.class,java.lang.Object.class,
            };
            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        };
        
        tabModeGinekologi=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M","Nama Pasien","Tgl.Rawat","Jam Rawat",
            "Inpeksi","Inspeksi Vulva/Uretra/Vagina", "Inspekulo","Fluxus",
            "Fluor Albus", "Inspekulo Vulva/Vagina", "Inspekulo Portio", "Inspekulo Sondage",
            "Pemeriksaan Dalam Portio", "Pemeriksaan Dalam Bentuk","Pemeriksaan Dalam Cavum Uteri","Mobilitas",
            "Ukuran Cavum Uteri","Nyeri Tekan","Pemeriksaan Dalam Adnexa Kanan","Pemeriksaan Dalam Adnexa Kiri",
            "Pemeriksaan Dalam Cavum Douglas"}) {
             @Override public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class
                 
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        
        TabModeTindakan=new DefaultTableModel(null,new Object[]{
            "P","Kode","Nama Perawatan","Kategori Perawatan","Tarif/Biaya","Bagian RS","BHP","JM Dokter","JM Perawat","KSO","Menejemen"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,  
                java.lang.Object.class,java.lang.Double.class,java.lang.Double.class,
                java.lang.Double.class,java.lang.Double.class,java.lang.Double.class,
                java.lang.Double.class,java.lang.Double.class
             };
             /*Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };*/
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };

        TabModeTindakan2=new DefaultTableModel(null,new Object[]{"P","Kode","Nama Perawatan","Kategori Perawatan","Tarif/Biaya","Bagian RS","BHP","JM Dokter","JM Perawat","KSO","Menejemen"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,  
                java.lang.Object.class,java.lang.Double.class,java.lang.Double.class,
                java.lang.Double.class,java.lang.Double.class,java.lang.Double.class,
                java.lang.Double.class,java.lang.Double.class
             };
             /*Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };*/
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        
        TabModeTindakan3=new DefaultTableModel(null,new Object[]{"P","Kode","Nama Perawatan","Kategori Perawatan","Tarif/Biaya","Bagian RS","BHP","JM Dokter","JM Perawat","KSO","Menejemen"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,  
                java.lang.Object.class,java.lang.Double.class,java.lang.Double.class,
                java.lang.Double.class,java.lang.Double.class,java.lang.Double.class,
                java.lang.Double.class,java.lang.Double.class
             };
             /*Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };*/
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbTindakan3.setModel(TabModeTindakan3);
        tbTindakan3.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbTindakan3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 11; i++) {
            TableColumn column = tbTindakan3.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(90);
            }else if(i==2){
                column.setPreferredWidth(420);
            }else if(i==3){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==6){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==7){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==8){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==9){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else{
                column.setPreferredWidth(90);
            }
        }
        tbTindakan3.setDefaultRenderer(Object.class, new WarnaTable());
        
        TabModeCatatan=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M.","Nama Pasien","Tanggal","Jam","Kode Dokter","Nama Dokter","Catatan"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        
        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        kdptg2.setDocument(new batasInput((byte)20).getKata(kdptg2));
        KdDok2.setDocument(new batasInput((byte)20).getKata(KdDok2));
        KdPeg.setDocument(new batasInput((byte)20).getKata(KdPeg));
        TSuhu.setDocument(new batasInput((byte)5).getKata(TSuhu));
        TTensi.setDocument(new batasInput((byte)8).getKata(TTensi));
        TKeluhan.setDocument(new batasInput((int)2000).getKata(TKeluhan));
        TPemeriksaan.setDocument(new batasInput((int)2000).getKata(TPemeriksaan));
        TPenilaian.setDocument(new batasInput((int)2000).getKata(TPenilaian));    
        TInstruksi.setDocument(new batasInput((int)2000).getKata(TInstruksi));     
        TAlergi.setDocument(new batasInput((int)50).getKata(TAlergi));        
        TCari.setDocument(new batasInput((int)100).getKata(TCari));       
        TGCS.setDocument(new batasInput((byte)10).getKata(TGCS));
        TTinggi.setDocument(new batasInput((byte)5).getKata(TTinggi));
        LingkarPerut.setDocument(new batasInput((byte)5).getKata(LingkarPerut));
        TBerat.setDocument(new batasInput((byte)5).getKata(TBerat));
        TindakLanjut.setDocument(new batasInput((int)2000).getKata(TindakLanjut));
        TEvaluasi.setDocument(new batasInput((int)2000).getKata(TEvaluasi));
        TNadi.setDocument(new batasInput((byte)3).getOnlyAngka(TNadi));
        SpO2.setDocument(new batasInput((byte)3).getOnlyAngka(SpO2));
        TRespirasi.setDocument(new batasInput((byte)3).getOnlyAngka(TRespirasi));
        
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        TampilkanData();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        TampilkanData();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        TampilkanData();
                    }
                }
            });
        }  
        
        pasien.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(akses.getform().equals("DlgRawatJalan")){
                    if(pasien.getTable().getSelectedRow()!= -1){                   
                        TCariPasien.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),0).toString());
                    } 
                    TCariPasien.requestFocus();
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
        
        pasien.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(akses.getform().equals("DlgRawatJalan")){
                    if(e.getKeyCode()==KeyEvent.VK_SPACE){
                        pasien.dispose();
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(akses.getform().equals("DlgRawatJalan")){
                    if(dokter.getTable().getSelectedRow()!= -1){
                        if(TabRawat.getSelectedIndex()==0){
                            KdDok2.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                            TDokter2.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                            KdDok2.requestFocus();
                        }                       
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
                if(akses.getform().equals("DlgRawatJalan")){
                    if(petugas.getTable().getSelectedRow()!= -1){   
                        if(TabRawat.getSelectedIndex()==1){
                            kdptg2.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                            TPerawat2.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                            kdptg2.requestFocus();
                        }                            
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
        
        pegawai.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(akses.getform().equals("DlgRawatJalan")){
                    if(pegawai.getTable().getSelectedRow()!= -1){   
                        KdPeg.setText(pegawai.getTable().getValueAt(pegawai.getTable().getSelectedRow(),0).toString());
                        TPegawai.setText(pegawai.getTable().getValueAt(pegawai.getTable().getSelectedRow(),1).toString());
                        Jabatan.setText(pegawai.getTable().getValueAt(pegawai.getTable().getSelectedRow(),3).toString());
                        KdPeg.requestFocus();                    
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
        
        soapterakhir.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(soapterakhir.getTable().getSelectedRow()!= -1){   
                    TKeluhan.setText(soapterakhir.getTable().getValueAt(soapterakhir.getTable().getSelectedRow(),2).toString());
                    TPemeriksaan.setText(soapterakhir.getTable().getValueAt(soapterakhir.getTable().getSelectedRow(),3).toString());
                    TPenilaian.setText(soapterakhir.getTable().getValueAt(soapterakhir.getTable().getSelectedRow(),4).toString());
                    TindakLanjut.setText(soapterakhir.getTable().getValueAt(soapterakhir.getTable().getSelectedRow(),5).toString());
                    TInstruksi.setText(soapterakhir.getTable().getValueAt(soapterakhir.getTable().getSelectedRow(),6).toString());
                    TEvaluasi.setText(soapterakhir.getTable().getValueAt(soapterakhir.getTable().getSelectedRow(),7).toString());
                    TEvaluasi.requestFocus();                    
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
        isMenu(); 
        jam();
        
        try {
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            aktifkanparsial=prop.getProperty("AKTIFKANBILLINGPARSIAL");
        } catch (Exception ex) {            
            aktifkanparsial="no";
        }
        
        try {
            psrekening=koneksi.prepareStatement(
                    "select set_akun_ralan.Suspen_Piutang_Tindakan_Ralan,set_akun_ralan.Tindakan_Ralan,set_akun_ralan.Beban_Jasa_Medik_Dokter_Tindakan_Ralan,"+
                    "set_akun_ralan.Utang_Jasa_Medik_Dokter_Tindakan_Ralan,set_akun_ralan.Beban_Jasa_Medik_Paramedis_Tindakan_Ralan,"+
                    "set_akun_ralan.Utang_Jasa_Medik_Paramedis_Tindakan_Ralan,set_akun_ralan.Beban_KSO_Tindakan_Ralan,"+
                    "set_akun_ralan.Utang_KSO_Tindakan_Ralan,set_akun_ralan.Beban_Jasa_Sarana_Tindakan_Ralan,"+
                    "set_akun_ralan.Utang_Jasa_Sarana_Tindakan_Ralan,set_akun_ralan.Beban_Jasa_Menejemen_Tindakan_Ralan,"+
                    "set_akun_ralan.Utang_Jasa_Menejemen_Tindakan_Ralan,set_akun_ralan.HPP_BHP_Tindakan_Ralan,set_akun_ralan.Persediaan_BHP_Tindakan_Ralan from set_akun_ralan");
            try {
                rsrekening=psrekening.executeQuery();
                while(rsrekening.next()){
                    Suspen_Piutang_Tindakan_Ralan=rsrekening.getString("Suspen_Piutang_Tindakan_Ralan");
                    Tindakan_Ralan=rsrekening.getString("Tindakan_Ralan");
                    Beban_Jasa_Medik_Dokter_Tindakan_Ralan=rsrekening.getString("Beban_Jasa_Medik_Dokter_Tindakan_Ralan");
                    Utang_Jasa_Medik_Dokter_Tindakan_Ralan=rsrekening.getString("Utang_Jasa_Medik_Dokter_Tindakan_Ralan");
                    Beban_Jasa_Medik_Paramedis_Tindakan_Ralan=rsrekening.getString("Beban_Jasa_Medik_Paramedis_Tindakan_Ralan");
                    Utang_Jasa_Medik_Paramedis_Tindakan_Ralan=rsrekening.getString("Utang_Jasa_Medik_Paramedis_Tindakan_Ralan");
                    Beban_KSO_Tindakan_Ralan=rsrekening.getString("Beban_KSO_Tindakan_Ralan");
                    Utang_KSO_Tindakan_Ralan=rsrekening.getString("Utang_KSO_Tindakan_Ralan");
                    Beban_Jasa_Sarana_Tindakan_Ralan=rsrekening.getString("Beban_Jasa_Sarana_Tindakan_Ralan");
                    Utang_Jasa_Sarana_Tindakan_Ralan=rsrekening.getString("Utang_Jasa_Sarana_Tindakan_Ralan");
                    Beban_Jasa_Menejemen_Tindakan_Ralan=rsrekening.getString("Beban_Jasa_Menejemen_Tindakan_Ralan");
                    Utang_Jasa_Menejemen_Tindakan_Ralan=rsrekening.getString("Utang_Jasa_Menejemen_Tindakan_Ralan");
                    HPP_BHP_Tindakan_Ralan=rsrekening.getString("HPP_BHP_Tindakan_Ralan");
                    Persediaan_BHP_Tindakan_Ralan=rsrekening.getString("Persediaan_BHP_Tindakan_Ralan");
                }
            } catch (Exception e) {
                System.out.println("Notif Rekening : "+e);
            } finally{
                if(rsrekening!=null){
                    rsrekening.close();
                }
                if(psrekening!=null){
                    psrekening.close();
                }
            }            
        } catch (Exception e) {
            System.out.println(e);
        }
        
        
        //REKENING MATRIK TINDAKAN RSK_ANTONIUS AMPENAN        
        try {
            psrekening_rsk=koneksi.prepareStatement(
                    "select matrik_akun_jns_perawatan.kd_jenis_prw,matrik_akun_jns_perawatan.pendapatan_tindakan,matrik_akun_jns_perawatan.beban_jasa_dokter,matrik_akun_jns_perawatan.utang_jasa_dokter,"+
                    "matrik_akun_jns_perawatan.beban_jasa_paramedis,matrik_akun_jns_perawatan.utang_jasa_paramedis,"+
                    "matrik_akun_jns_perawatan.beban_kso,matrik_akun_jns_perawatan.utang_kso,"+
                    "matrik_akun_jns_perawatan.hpp_persediaan,matrik_akun_jns_perawatan.persediaan_bhp,"+
                    "matrik_akun_jns_perawatan.beban_jasa_sarana,matrik_akun_jns_perawatan.utang_jasa_sarana,"+
                    "matrik_akun_jns_perawatan.beban_menejemen,matrik_akun_jns_perawatan.utang_menejemen from matrik_akun_jns_perawatan");
            try {
                rsrekening_rsk=psrekening_rsk.executeQuery();
                while(rsrekening_rsk.next()){
                    kd_jenis_prw=rsrekening_rsk.getString("kd_jenis_prw");
                    pendapatan_tindakan=rsrekening_rsk.getString("pendapatan_tindakan");
                    beban_jasa_dokter=rsrekening_rsk.getString("beban_jasa_dokter");
                    utang_jasa_dokter=rsrekening_rsk.getString("utang_jasa_dokter");
                    beban_jasa_paramedis=rsrekening_rsk.getString("beban_jasa_paramedis");
                    utang_jasa_paramedis=rsrekening_rsk.getString("utang_jasa_paramedis");
                    beban_kso=rsrekening_rsk.getString("beban_kso");
                    utang_kso=rsrekening_rsk.getString("utang_kso");
                    hpp_persediaan=rsrekening_rsk.getString("hpp_persediaan");
                    persediaan_bhp=rsrekening_rsk.getString("persediaan_bhp");
                    beban_jasa_sarana=rsrekening_rsk.getString("beban_jasa_sarana");
                    utang_jasa_sarana=rsrekening_rsk.getString("utang_jasa_sarana");
                    beban_menejemen=rsrekening_rsk.getString("beban_menejemen");
                    utang_menejemen=rsrekening_rsk.getString("utang_menejemen");
                }
            } catch (Exception e) {
                System.out.println("Notif Rekening : "+e);
            } finally{
                if(rsrekening_rsk!=null){
                    rsrekening_rsk.close();
                }
                if(psrekening_rsk!=null){
                    psrekening_rsk.close();
                }
            }            
        } catch (Exception e) {
            System.out.println(e);
        }
        //SAMPAI SINI REKENING TINDAKAN RSK
        
        try {
            psset_tarif=koneksi.prepareStatement("select set_tarif.poli_ralan,set_tarif.cara_bayar_ralan from set_tarif");
            try {
                rsset_tarif=psset_tarif.executeQuery();
                if(rsset_tarif.next()){
                    poli_ralan=rsset_tarif.getString("poli_ralan");
                    cara_bayar_ralan=rsset_tarif.getString("cara_bayar_ralan");
                }else{
                    poli_ralan="Yes";
                    cara_bayar_ralan="Yes";
                }  
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rsset_tarif != null){
                    rsset_tarif.close();
                }
                if(psset_tarif != null){
                    psset_tarif.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        } 
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
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        BtnAll = new widget.Button();
        jLabel10 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel24 = new widget.Label();
        TCariPasien = new widget.TextBox();
        btnPasien = new widget.Button();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnTambahTindakan = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        internalFrame4 = new widget.InternalFrame();
        panelGlass11 = new widget.panelisi();
        jLabel14 = new widget.Label();
        kdptg2 = new widget.TextBox();
        BtnSeekPetugas2 = new widget.Button();
        TPerawat2 = new widget.TextBox();
        jLabel12 = new widget.Label();
        KdDok2 = new widget.TextBox();
        TDokter2 = new widget.TextBox();
        BtnSeekDokter2 = new widget.Button();
        TabRawatTindakanDokterPetugas = new javax.swing.JTabbedPane();
        Scroll9 = new widget.ScrollPane();
        tbTindakan3 = new widget.Table();
        Scroll10 = new widget.ScrollPane();
        tbRawatDrPr = new widget.Table();
        internalFrame5 = new widget.InternalFrame();
        Scroll3 = new widget.ScrollPane();
        tbPemeriksaan = new widget.Table();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        panelGlass12 = new widget.panelisi();
        jLabel8 = new widget.Label();
        jLabel7 = new widget.Label();
        jLabel4 = new widget.Label();
        jLabel16 = new widget.Label();
        jLabel18 = new widget.Label();
        jLabel25 = new widget.Label();
        jLabel17 = new widget.Label();
        jLabel9 = new widget.Label();
        jLabel15 = new widget.Label();
        jLabel20 = new widget.Label();
        jLabel22 = new widget.Label();
        scrollPane1 = new widget.ScrollPane();
        TKeluhan = new widget.TextArea();
        jLabel28 = new widget.Label();
        jLabel26 = new widget.Label();
        scrollPane2 = new widget.ScrollPane();
        TPemeriksaan = new widget.TextArea();
        TSuhu = new widget.TextBox();
        TTensi = new widget.TextBox();
        TTinggi = new widget.TextBox();
        TRespirasi = new widget.TextBox();
        TBerat = new widget.TextBox();
        TNadi = new widget.TextBox();
        TGCS = new widget.TextBox();
        TAlergi = new widget.TextBox();
        scrollPane3 = new widget.ScrollPane();
        TPenilaian = new widget.TextArea();
        scrollPane6 = new widget.ScrollPane();
        TindakLanjut = new widget.TextArea();
        jLabel29 = new widget.Label();
        cmbKesadaran = new widget.ComboBox();
        jLabel37 = new widget.Label();
        KdPeg = new widget.TextBox();
        TPegawai = new widget.TextBox();
        BtnSeekPegawai = new widget.Button();
        Jabatan = new widget.TextBox();
        jLabel41 = new widget.Label();
        jLabel53 = new widget.Label();
        scrollPane7 = new widget.ScrollPane();
        TInstruksi = new widget.TextArea();
        jLabel54 = new widget.Label();
        SpO2 = new widget.TextBox();
        jLabel56 = new widget.Label();
        scrollPane8 = new widget.ScrollPane();
        TEvaluasi = new widget.TextArea();
        LingkarPerut = new widget.TextBox();
        Btn5Soap = new widget.Button();
        FormInput = new widget.PanelBiasa();
        jLabel3 = new widget.Label();
        TNoRw = new widget.TextBox();
        TNoRM = new widget.TextBox();
        TPasien = new widget.TextBox();
        jLabel23 = new widget.Label();
        DTPTgl = new widget.Tanggal();
        cmbJam = new widget.ComboBox();
        cmbMnt = new widget.ComboBox();
        cmbDtk = new widget.ComboBox();
        ChkJln = new widget.CekBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Perawatan/Tindakan Rawat Jalan ]::", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

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

        jLabel10.setText("Record :");
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(95, 30));
        panelGlass8.add(jLabel10);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(87, 30));
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

        jLabel19.setText("Tgl.Rawat :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(64, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "11-05-2023" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "11-05-2023" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel24.setText("No.RM :");
        jLabel24.setName("jLabel24"); // NOI18N
        jLabel24.setPreferredSize(new java.awt.Dimension(55, 23));
        panelGlass9.add(jLabel24);

        TCariPasien.setName("TCariPasien"); // NOI18N
        TCariPasien.setPreferredSize(new java.awt.Dimension(140, 23));
        panelGlass9.add(TCariPasien);

        btnPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPasien.setMnemonic('6');
        btnPasien.setToolTipText("Alt+6");
        btnPasien.setName("btnPasien"); // NOI18N
        btnPasien.setPreferredSize(new java.awt.Dimension(28, 23));
        btnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienActionPerformed(evt);
            }
        });
        btnPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPasienKeyPressed(evt);
            }
        });
        panelGlass9.add(btnPasien);

        jSeparator5.setBackground(new java.awt.Color(220, 225, 215));
        jSeparator5.setForeground(new java.awt.Color(220, 225, 215));
        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator5.setName("jSeparator5"); // NOI18N
        jSeparator5.setOpaque(true);
        jSeparator5.setPreferredSize(new java.awt.Dimension(1, 23));
        panelGlass9.add(jSeparator5);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(240, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('6');
        BtnCari.setText("Tampilkan");
        BtnCari.setToolTipText("Alt+6");
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
        panelGlass9.add(BtnCari);

        BtnTambahTindakan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambahTindakan.setMnemonic('3');
        BtnTambahTindakan.setText("Tambah");
        BtnTambahTindakan.setToolTipText("Alt+3");
        BtnTambahTindakan.setName("BtnTambahTindakan"); // NOI18N
        BtnTambahTindakan.setPreferredSize(new java.awt.Dimension(100, 23));
        BtnTambahTindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahTindakanActionPerformed(evt);
            }
        });
        panelGlass9.add(BtnTambahTindakan);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(255, 255, 255));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        internalFrame4.setBorder(null);
        internalFrame4.setName("internalFrame4"); // NOI18N
        internalFrame4.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass11.setBorder(null);
        panelGlass11.setName("panelGlass11"); // NOI18N
        panelGlass11.setPreferredSize(new java.awt.Dimension(44, 74));
        panelGlass11.setLayout(null);

        jLabel14.setText("Petugas :");
        jLabel14.setName("jLabel14"); // NOI18N
        panelGlass11.add(jLabel14);
        jLabel14.setBounds(0, 40, 65, 23);

        kdptg2.setHighlighter(null);
        kdptg2.setName("kdptg2"); // NOI18N
        kdptg2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdptg2KeyPressed(evt);
            }
        });
        panelGlass11.add(kdptg2);
        kdptg2.setBounds(68, 40, 130, 23);

        BtnSeekPetugas2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekPetugas2.setMnemonic('5');
        BtnSeekPetugas2.setToolTipText("ALt+5");
        BtnSeekPetugas2.setName("BtnSeekPetugas2"); // NOI18N
        BtnSeekPetugas2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekPetugas2ActionPerformed(evt);
            }
        });
        panelGlass11.add(BtnSeekPetugas2);
        BtnSeekPetugas2.setBounds(749, 40, 28, 23);

        TPerawat2.setEditable(false);
        TPerawat2.setBackground(new java.awt.Color(202, 202, 202));
        TPerawat2.setHighlighter(null);
        TPerawat2.setName("TPerawat2"); // NOI18N
        panelGlass11.add(TPerawat2);
        TPerawat2.setBounds(200, 40, 546, 23);

        jLabel12.setText("Dokter :");
        jLabel12.setName("jLabel12"); // NOI18N
        panelGlass11.add(jLabel12);
        jLabel12.setBounds(0, 10, 65, 23);

        KdDok2.setHighlighter(null);
        KdDok2.setName("KdDok2"); // NOI18N
        KdDok2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdDok2KeyPressed(evt);
            }
        });
        panelGlass11.add(KdDok2);
        KdDok2.setBounds(68, 10, 130, 23);

        TDokter2.setEditable(false);
        TDokter2.setHighlighter(null);
        TDokter2.setName("TDokter2"); // NOI18N
        panelGlass11.add(TDokter2);
        TDokter2.setBounds(200, 10, 546, 23);

        BtnSeekDokter2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekDokter2.setMnemonic('4');
        BtnSeekDokter2.setToolTipText("ALt+4");
        BtnSeekDokter2.setName("BtnSeekDokter2"); // NOI18N
        BtnSeekDokter2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekDokter2ActionPerformed(evt);
            }
        });
        panelGlass11.add(BtnSeekDokter2);
        BtnSeekDokter2.setBounds(749, 10, 28, 23);

        internalFrame4.add(panelGlass11, java.awt.BorderLayout.PAGE_START);

        TabRawatTindakanDokterPetugas.setBackground(new java.awt.Color(255, 255, 255));
        TabRawatTindakanDokterPetugas.setForeground(new java.awt.Color(50, 50, 50));
        TabRawatTindakanDokterPetugas.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawatTindakanDokterPetugas.setName("TabRawatTindakanDokterPetugas"); // NOI18N
        TabRawatTindakanDokterPetugas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatTindakanDokterPetugasMouseClicked(evt);
            }
        });

        Scroll9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll9.setName("Scroll9"); // NOI18N
        Scroll9.setOpaque(true);

        tbTindakan3.setAutoCreateRowSorter(true);
        tbTindakan3.setToolTipText("");
        tbTindakan3.setName("tbTindakan3"); // NOI18N
        tbTindakan3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbTindakan3KeyPressed(evt);
            }
        });
        Scroll9.setViewportView(tbTindakan3);

        TabRawatTindakanDokterPetugas.addTab("Daftar Tindakan/Tagihan", Scroll9);

        Scroll10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll10.setName("Scroll10"); // NOI18N
        Scroll10.setOpaque(true);

        tbRawatDrPr.setAutoCreateRowSorter(true);
        tbRawatDrPr.setName("tbRawatDrPr"); // NOI18N
        tbRawatDrPr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRawatDrPrMouseClicked(evt);
            }
        });
        tbRawatDrPr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbRawatDrPrKeyReleased(evt);
            }
        });
        Scroll10.setViewportView(tbRawatDrPr);

        TabRawatTindakanDokterPetugas.addTab("Tindakan Dilakukan", Scroll10);

        internalFrame4.add(TabRawatTindakanDokterPetugas, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Penanganan Dokter & Petugas", internalFrame4);

        internalFrame5.setBackground(new java.awt.Color(235, 255, 235));
        internalFrame5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        internalFrame5.setName("internalFrame5"); // NOI18N
        internalFrame5.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll3.setName("Scroll3"); // NOI18N
        Scroll3.setOpaque(true);

        tbPemeriksaan.setAutoCreateRowSorter(true);
        tbPemeriksaan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbPemeriksaan.setName("tbPemeriksaan"); // NOI18N
        tbPemeriksaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPemeriksaanMouseClicked(evt);
            }
        });
        tbPemeriksaan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbPemeriksaanKeyReleased(evt);
            }
        });
        Scroll3.setViewportView(tbPemeriksaan);

        internalFrame5.add(Scroll3, java.awt.BorderLayout.CENTER);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 273));
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

        panelGlass12.setName("panelGlass12"); // NOI18N
        panelGlass12.setPreferredSize(new java.awt.Dimension(44, 134));
        panelGlass12.setLayout(null);

        jLabel8.setText("Subjek :");
        jLabel8.setName("jLabel8"); // NOI18N
        panelGlass12.add(jLabel8);
        jLabel8.setBounds(0, 70, 70, 23);

        jLabel7.setText("Suhu (°C) :");
        jLabel7.setName("jLabel7"); // NOI18N
        panelGlass12.add(jLabel7);
        jLabel7.setBounds(0, 160, 70, 23);

        jLabel4.setText("Tensi :");
        jLabel4.setName("jLabel4"); // NOI18N
        panelGlass12.add(jLabel4);
        jLabel4.setBounds(0, 190, 70, 23);

        jLabel16.setText("Berat (Kg) :");
        jLabel16.setName("jLabel16"); // NOI18N
        panelGlass12.add(jLabel16);
        jLabel16.setBounds(296, 160, 79, 23);

        jLabel18.setText("Nadi (/menit) :");
        jLabel18.setName("jLabel18"); // NOI18N
        panelGlass12.add(jLabel18);
        jLabel18.setBounds(296, 190, 79, 23);

        jLabel25.setText("L.P. (Cm) :");
        jLabel25.setName("jLabel25"); // NOI18N
        panelGlass12.add(jLabel25);
        jLabel25.setBounds(450, 10, 90, 23);

        jLabel17.setText("Tinggi Badan (Cm) :");
        jLabel17.setName("jLabel17"); // NOI18N
        panelGlass12.add(jLabel17);
        jLabel17.setBounds(134, 160, 100, 23);

        jLabel9.setText("Objek :");
        jLabel9.setName("jLabel9"); // NOI18N
        panelGlass12.add(jLabel9);
        jLabel9.setBounds(0, 115, 70, 23);

        jLabel15.setText("Alergi :");
        jLabel15.setName("jLabel15"); // NOI18N
        panelGlass12.add(jLabel15);
        jLabel15.setBounds(624, 10, 70, 23);

        jLabel20.setText("Respirasi (/menit) :");
        jLabel20.setName("jLabel20"); // NOI18N
        panelGlass12.add(jLabel20);
        jLabel20.setBounds(134, 190, 100, 23);

        jLabel22.setText("GCS (E,V,M) :");
        jLabel22.setName("jLabel22"); // NOI18N
        panelGlass12.add(jLabel22);
        jLabel22.setBounds(120, 220, 70, 23);

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane1.setName("scrollPane1"); // NOI18N

        TKeluhan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TKeluhan.setColumns(20);
        TKeluhan.setRows(5);
        TKeluhan.setName("TKeluhan"); // NOI18N
        TKeluhan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TKeluhanKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(TKeluhan);

        panelGlass12.add(scrollPane1);
        scrollPane1.setBounds(73, 70, 360, 38);

        jLabel28.setText("Asesmen :");
        jLabel28.setName("jLabel28"); // NOI18N
        panelGlass12.add(jLabel28);
        jLabel28.setBounds(450, 40, 90, 23);

        jLabel26.setText("Plan :");
        jLabel26.setName("jLabel26"); // NOI18N
        panelGlass12.add(jLabel26);
        jLabel26.setBounds(450, 85, 90, 23);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setName("scrollPane2"); // NOI18N

        TPemeriksaan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TPemeriksaan.setColumns(20);
        TPemeriksaan.setRows(5);
        TPemeriksaan.setName("TPemeriksaan"); // NOI18N
        TPemeriksaan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPemeriksaanKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(TPemeriksaan);

        panelGlass12.add(scrollPane2);
        scrollPane2.setBounds(73, 115, 360, 38);

        TSuhu.setFocusTraversalPolicyProvider(true);
        TSuhu.setName("TSuhu"); // NOI18N
        TSuhu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TSuhuKeyPressed(evt);
            }
        });
        panelGlass12.add(TSuhu);
        TSuhu.setBounds(73, 160, 55, 23);

        TTensi.setHighlighter(null);
        TTensi.setName("TTensi"); // NOI18N
        TTensi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TTensiKeyPressed(evt);
            }
        });
        panelGlass12.add(TTensi);
        TTensi.setBounds(73, 190, 55, 23);

        TTinggi.setFocusTraversalPolicyProvider(true);
        TTinggi.setName("TTinggi"); // NOI18N
        TTinggi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TTinggiKeyPressed(evt);
            }
        });
        panelGlass12.add(TTinggi);
        TTinggi.setBounds(237, 160, 55, 23);

        TRespirasi.setHighlighter(null);
        TRespirasi.setName("TRespirasi"); // NOI18N
        TRespirasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TRespirasiKeyPressed(evt);
            }
        });
        panelGlass12.add(TRespirasi);
        TRespirasi.setBounds(237, 190, 55, 23);

        TBerat.setHighlighter(null);
        TBerat.setName("TBerat"); // NOI18N
        TBerat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TBeratKeyPressed(evt);
            }
        });
        panelGlass12.add(TBerat);
        TBerat.setBounds(378, 160, 55, 23);

        TNadi.setFocusTraversalPolicyProvider(true);
        TNadi.setName("TNadi"); // NOI18N
        TNadi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNadiKeyPressed(evt);
            }
        });
        panelGlass12.add(TNadi);
        TNadi.setBounds(378, 190, 55, 23);

        TGCS.setFocusTraversalPolicyProvider(true);
        TGCS.setName("TGCS"); // NOI18N
        TGCS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TGCSKeyPressed(evt);
            }
        });
        panelGlass12.add(TGCS);
        TGCS.setBounds(193, 220, 42, 23);

        TAlergi.setHighlighter(null);
        TAlergi.setName("TAlergi"); // NOI18N
        TAlergi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TAlergiKeyPressed(evt);
            }
        });
        panelGlass12.add(TAlergi);
        TAlergi.setBounds(697, 10, 206, 23);

        scrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane3.setName("scrollPane3"); // NOI18N

        TPenilaian.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TPenilaian.setColumns(20);
        TPenilaian.setRows(5);
        TPenilaian.setName("TPenilaian"); // NOI18N
        TPenilaian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPenilaianKeyPressed(evt);
            }
        });
        scrollPane3.setViewportView(TPenilaian);

        panelGlass12.add(scrollPane3);
        scrollPane3.setBounds(543, 40, 360, 38);

        scrollPane6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane6.setName("scrollPane6"); // NOI18N

        TindakLanjut.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TindakLanjut.setColumns(20);
        TindakLanjut.setRows(5);
        TindakLanjut.setName("TindakLanjut"); // NOI18N
        TindakLanjut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TindakLanjutKeyPressed(evt);
            }
        });
        scrollPane6.setViewportView(TindakLanjut);

        panelGlass12.add(scrollPane6);
        scrollPane6.setBounds(543, 85, 360, 47);

        jLabel29.setText("Kesadaran :");
        jLabel29.setName("jLabel29"); // NOI18N
        panelGlass12.add(jLabel29);
        jLabel29.setBounds(234, 220, 70, 23);

        cmbKesadaran.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Compos Mentis", "Somnolence", "Sopor", "Coma" }));
        cmbKesadaran.setName("cmbKesadaran"); // NOI18N
        cmbKesadaran.setPreferredSize(new java.awt.Dimension(62, 28));
        cmbKesadaran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbKesadaranKeyPressed(evt);
            }
        });
        panelGlass12.add(cmbKesadaran);
        cmbKesadaran.setBounds(307, 220, 126, 23);

        jLabel37.setText("Dilakukan :");
        jLabel37.setName("jLabel37"); // NOI18N
        panelGlass12.add(jLabel37);
        jLabel37.setBounds(0, 10, 70, 23);

        KdPeg.setHighlighter(null);
        KdPeg.setName("KdPeg"); // NOI18N
        KdPeg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPegKeyPressed(evt);
            }
        });
        panelGlass12.add(KdPeg);
        KdPeg.setBounds(73, 10, 115, 23);

        TPegawai.setEditable(false);
        TPegawai.setHighlighter(null);
        TPegawai.setName("TPegawai"); // NOI18N
        panelGlass12.add(TPegawai);
        TPegawai.setBounds(190, 10, 212, 23);

        BtnSeekPegawai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekPegawai.setMnemonic('4');
        BtnSeekPegawai.setToolTipText("ALt+4");
        BtnSeekPegawai.setName("BtnSeekPegawai"); // NOI18N
        BtnSeekPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekPegawaiActionPerformed(evt);
            }
        });
        panelGlass12.add(BtnSeekPegawai);
        BtnSeekPegawai.setBounds(405, 10, 28, 23);

        Jabatan.setEditable(false);
        Jabatan.setHighlighter(null);
        Jabatan.setName("Jabatan"); // NOI18N
        panelGlass12.add(Jabatan);
        Jabatan.setBounds(193, 40, 209, 23);

        jLabel41.setText("Profesi / Jabatan / Departemen :");
        jLabel41.setName("jLabel41"); // NOI18N
        panelGlass12.add(jLabel41);
        jLabel41.setBounds(0, 40, 190, 23);

        jLabel53.setText("Instruksi :");
        jLabel53.setName("jLabel53"); // NOI18N
        panelGlass12.add(jLabel53);
        jLabel53.setBounds(450, 139, 90, 23);

        scrollPane7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane7.setName("scrollPane7"); // NOI18N

        TInstruksi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TInstruksi.setColumns(20);
        TInstruksi.setRows(5);
        TInstruksi.setName("TInstruksi"); // NOI18N
        TInstruksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TInstruksiKeyPressed(evt);
            }
        });
        scrollPane7.setViewportView(TInstruksi);

        panelGlass12.add(scrollPane7);
        scrollPane7.setBounds(543, 139, 360, 50);

        jLabel54.setText("SpO2 (%) :");
        jLabel54.setName("jLabel54"); // NOI18N
        panelGlass12.add(jLabel54);
        jLabel54.setBounds(0, 220, 70, 23);

        SpO2.setFocusTraversalPolicyProvider(true);
        SpO2.setName("SpO2"); // NOI18N
        SpO2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SpO2KeyPressed(evt);
            }
        });
        panelGlass12.add(SpO2);
        SpO2.setBounds(73, 220, 42, 23);

        jLabel56.setText("Evaluasi :");
        jLabel56.setName("jLabel56"); // NOI18N
        panelGlass12.add(jLabel56);
        jLabel56.setBounds(450, 196, 90, 23);

        scrollPane8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane8.setName("scrollPane8"); // NOI18N

        TEvaluasi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TEvaluasi.setColumns(20);
        TEvaluasi.setRows(5);
        TEvaluasi.setName("TEvaluasi"); // NOI18N
        TEvaluasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TEvaluasiKeyPressed(evt);
            }
        });
        scrollPane8.setViewportView(TEvaluasi);

        panelGlass12.add(scrollPane8);
        scrollPane8.setBounds(543, 196, 360, 44);

        LingkarPerut.setHighlighter(null);
        LingkarPerut.setName("LingkarPerut"); // NOI18N
        LingkarPerut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LingkarPerutKeyPressed(evt);
            }
        });
        panelGlass12.add(LingkarPerut);
        LingkarPerut.setBounds(543, 10, 55, 23);

        Btn5Soap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        Btn5Soap.setMnemonic('4');
        Btn5Soap.setToolTipText("ALt+4");
        Btn5Soap.setName("Btn5Soap"); // NOI18N
        Btn5Soap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn5SoapActionPerformed(evt);
            }
        });
        panelGlass12.add(Btn5Soap);
        Btn5Soap.setBounds(405, 40, 28, 23);

        PanelInput.add(panelGlass12, java.awt.BorderLayout.CENTER);

        internalFrame5.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        TabRawat.addTab("Pemeriksaan", internalFrame5);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(260, 43));
        FormInput.setLayout(null);

        jLabel3.setText("No.Rawat :");
        jLabel3.setName("jLabel3"); // NOI18N
        FormInput.add(jLabel3);
        jLabel3.setBounds(0, 10, 70, 23);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TNoRwMouseClicked(evt);
            }
        });
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(74, 10, 125, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        FormInput.add(TNoRM);
        TNoRM.setBounds(201, 10, 80, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        FormInput.add(TPasien);
        TPasien.setBounds(283, 10, 270, 23);

        jLabel23.setText("Tanggal :");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(554, 10, 60, 23);

        DTPTgl.setForeground(new java.awt.Color(50, 70, 50));
        DTPTgl.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "11-05-2023" }));
        DTPTgl.setDisplayFormat("dd-MM-yyyy");
        DTPTgl.setName("DTPTgl"); // NOI18N
        DTPTgl.setOpaque(false);
        DTPTgl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPTglKeyPressed(evt);
            }
        });
        FormInput.add(DTPTgl);
        DTPTgl.setBounds(617, 10, 90, 23);

        cmbJam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbJam.setName("cmbJam"); // NOI18N
        cmbJam.setPreferredSize(new java.awt.Dimension(62, 28));
        cmbJam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbJamKeyPressed(evt);
            }
        });
        FormInput.add(cmbJam);
        cmbJam.setBounds(711, 10, 62, 23);

        cmbMnt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMnt.setName("cmbMnt"); // NOI18N
        cmbMnt.setPreferredSize(new java.awt.Dimension(62, 28));
        cmbMnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMntKeyPressed(evt);
            }
        });
        FormInput.add(cmbMnt);
        cmbMnt.setBounds(776, 10, 62, 23);

        cmbDtk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbDtk.setName("cmbDtk"); // NOI18N
        cmbDtk.setPreferredSize(new java.awt.Dimension(62, 28));
        cmbDtk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbDtkKeyPressed(evt);
            }
        });
        FormInput.add(cmbDtk);
        cmbDtk.setBounds(841, 10, 62, 23);

        ChkJln.setBorder(null);
        ChkJln.setSelected(true);
        ChkJln.setBorderPainted(true);
        ChkJln.setBorderPaintedFlat(true);
        ChkJln.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkJln.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkJln.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkJln.setName("ChkJln"); // NOI18N
        ChkJln.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkJlnActionPerformed(evt);
            }
        });
        FormInput.add(ChkJln);
        ChkJln.setBounds(906, 10, 23, 23);

        internalFrame1.add(FormInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
            isPsien();
            kd_pj=Sequel.cariIsi("select reg_periksa.kd_pj from reg_periksa where reg_periksa.no_rawat=?",TNoRw.getText());
            kode_poli=Sequel.cariIsi("select reg_periksa.kd_poli from reg_periksa where reg_periksa.no_rawat=?",TNoRw.getText());
        }else{         
            if(TabRawat.getSelectedIndex()==0){
                Valid.pindah(evt,DTPTgl,KdDok2);
            }else if(TabRawat.getSelectedIndex()==1){
                Valid.pindah(evt,DTPTgl,KdPeg);
            }
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"No.Rawat");
        }else{
            switch (TabRawat.getSelectedIndex()) {
                case 0:
                    if(KdDok2.getText().trim().equals("")||TDokter2.getText().trim().equals("")){
                        Valid.textKosong(KdDok2,"Dokter");
                    }else if(kdptg2.getText().trim().equals("")||TPerawat2.getText().trim().equals("")){
                        Valid.textKosong(kdptg2,"Petugas");
                    }else{      
                        try {
                            jmlparsial=0;
                            if(aktifkanparsial.equals("yes")){
                                jmlparsial=Sequel.cariInteger("select count(set_input_parsial.kd_pj) from set_input_parsial where set_input_parsial.kd_pj=?",Sequel.cariIsi("select reg_periksa.kd_pj from reg_periksa where reg_periksa.no_rawat=?",TNoRw.getText()));
                            }
                            if(jmlparsial>0){ 
                                SimpanPenangananDokterPetugas();
                            }else {
                                if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                                    JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                                    TCari.requestFocus();
                                }else{
                                    SimpanPenangananDokterPetugas();
                                }
                            } 
                        } catch (Exception e) {
                        }                            
                    }  
                    break;
                case 1:
                    if((!TKeluhan.getText().trim().equals(""))||(!TPemeriksaan.getText().trim().equals(""))||(!TSuhu.getText().trim().equals(""))||
                            (!TTensi.getText().trim().equals(""))||(!TAlergi.getText().trim().equals(""))||(!TTinggi.getText().trim().equals(""))||
                            (!TBerat.getText().trim().equals(""))||(!TRespirasi.getText().trim().equals(""))||(!TNadi.getText().trim().equals(""))||
                            (!TGCS.getText().trim().equals(""))||(!TindakLanjut.getText().trim().equals(""))||(!TPenilaian.getText().trim().equals(""))||
                            (!TInstruksi.getText().trim().equals(""))||(!SpO2.getText().trim().equals(""))||(!TEvaluasi.getText().trim().equals(""))){
                        if(KdPeg.getText().trim().equals("")||TPegawai.getText().trim().equals("")){
                            Valid.textKosong(KdPeg,"Dokter/Paramedis masih kosong...!!");
                        }else{
                            if(akses.getkode().equals("Admin Utama")){
                                if(Sequel.menyimpantf("pemeriksaan_ralan","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",21,new String[]{
                                    TNoRw.getText(),Valid.SetTgl(DTPTgl.getSelectedItem()+""),cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                                    TSuhu.getText(),TTensi.getText(),TNadi.getText(),TRespirasi.getText(),TTinggi.getText(),TBerat.getText(),
                                    SpO2.getText(),TGCS.getText(),cmbKesadaran.getSelectedItem().toString(),TKeluhan.getText(),TPemeriksaan.getText(),TAlergi.getText(),
                                    LingkarPerut.getText(),TindakLanjut.getText(),TPenilaian.getText(),TInstruksi.getText(),TEvaluasi.getText(),KdPeg.getText()})==true){
                                        tabModePemeriksaan.addRow(new Object[]{
                                            false,TNoRw.getText(),TNoRM.getText(),TPasien.getText(),Valid.SetTgl(DTPTgl.getSelectedItem()+""),cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                                            TSuhu.getText(),TTensi.getText(),TNadi.getText(),TRespirasi.getText(),TTinggi.getText(),TBerat.getText(),SpO2.getText(),TGCS.getText(),cmbKesadaran.getSelectedItem().toString(),
                                            TKeluhan.getText(),TPemeriksaan.getText(),TAlergi.getText(),LingkarPerut.getText(),TindakLanjut.getText(),TPenilaian.getText(),TInstruksi.getText(),TEvaluasi.getText(),
                                            KdPeg.getText(),TPegawai.getText(),Jabatan.getText()
                                        });
                                        TSuhu.setText("");TTensi.setText("");TNadi.setText("");TRespirasi.setText("");
                                        TTinggi.setText("");TBerat.setText("");TGCS.setText("");TKeluhan.setText("");
                                        TPemeriksaan.setText("");TAlergi.setText("");LingkarPerut.setText("");
                                        TindakLanjut.setText("");TPenilaian.setText("");TInstruksi.setText("");SpO2.setText("");
                                        TEvaluasi.setText("");cmbKesadaran.setSelectedIndex(0);
                                        LCount.setText(""+tabModePemeriksaan.getRowCount());
                                }
                            }else{
                                if(akses.getkode().equals(KdPeg.getText())){
                                    if(Sequel.menyimpantf("pemeriksaan_ralan","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",21,new String[]{
                                        TNoRw.getText(),Valid.SetTgl(DTPTgl.getSelectedItem()+""),cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                                        TSuhu.getText(),TTensi.getText(),TNadi.getText(),TRespirasi.getText(),TTinggi.getText(),TBerat.getText(),
                                        SpO2.getText(),TGCS.getText(),cmbKesadaran.getSelectedItem().toString(),TKeluhan.getText(),TPemeriksaan.getText(),TAlergi.getText(),
                                        LingkarPerut.getText(),TindakLanjut.getText(),TPenilaian.getText(),TInstruksi.getText(),TEvaluasi.getText(),KdPeg.getText()})==true){
                                            tabModePemeriksaan.addRow(new Object[]{
                                                false,TNoRw.getText(),TNoRM.getText(),TPasien.getText(),Valid.SetTgl(DTPTgl.getSelectedItem()+""),cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                                                TSuhu.getText(),TTensi.getText(),TNadi.getText(),TRespirasi.getText(),TTinggi.getText(),TBerat.getText(),SpO2.getText(),TGCS.getText(),cmbKesadaran.getSelectedItem().toString(),
                                                TKeluhan.getText(),TPemeriksaan.getText(),TAlergi.getText(),LingkarPerut.getText(),TindakLanjut.getText(),TPenilaian.getText(),TInstruksi.getText(),TEvaluasi.getText(),
                                                KdPeg.getText(),TPegawai.getText(),Jabatan.getText()
                                            });
                                            TSuhu.setText("");TTensi.setText("");TNadi.setText("");TRespirasi.setText("");
                                            TTinggi.setText("");TBerat.setText("");TGCS.setText("");TKeluhan.setText("");
                                            TPemeriksaan.setText("");TAlergi.setText("");LingkarPerut.setText("");
                                            TindakLanjut.setText("");TPenilaian.setText("");TInstruksi.setText("");SpO2.setText("");
                                            TEvaluasi.setText("");cmbKesadaran.setSelectedIndex(0);
                                            LCount.setText(""+tabModePemeriksaan.getRowCount());
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null,"Hanya bisa disimpan oleh dokter/petugas yang bersangkutan..!!");
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }            
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            if(TabRawat.getSelectedIndex()==0){
                Valid.pindah(evt,BtnSeekPetugas2,BtnBatal);
            }else if(TabRawat.getSelectedIndex()==1){
                Valid.pindah(evt,TEvaluasi,BtnBatal);
            }
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        ChkInput.setSelected(true);
        isForm(); 
        TSuhu.setText("");    
        TTensi.setText("");
        TKeluhan.setText("");
        TInstruksi.setText("");
        TPemeriksaan.setText("");
        TPenilaian.setText("");
        TAlergi.setText("");
        TBerat.setText("");
        TTinggi.setText("");
        TNadi.setText("");
        TRespirasi.setText("");
        TGCS.setText("");
        TindakLanjut.setText("");
        SpO2.setText("");
        TEvaluasi.setText("");
        cmbKesadaran.setSelectedIndex(0);
        TNoRw.requestFocus();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnBatalActionPerformed(null);
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                if(tabModeDrPr.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                    TNoRw.requestFocus();
                }else{
                    Sequel.AutoComitFalse();
                    sukses=true;
                    ttljmdokter=0;ttljmperawat=0;ttlkso=0;ttlpendapatan=0;ttljasasarana=0;ttlbhp=0;ttlmenejemen=0;
                    for(i=0;i<tbRawatDrPr.getRowCount();i++){
                        if(tbRawatDrPr.getValueAt(i,0).toString().equals("true")){                            
                            if(Sequel.cariRegistrasi(tbRawatDrPr.getValueAt(i,1).toString())>0){
                                JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                                tbRawatDrPr.setValueAt(false,i,0);
                                TCari.requestFocus();
                            }else{
                                if(Sequel.queryutf("delete from rawat_jl_drpr where no_rawat='"+tbRawatDrPr.getValueAt(i,1).toString()+
                                        "' and kd_jenis_prw='"+tbRawatDrPr.getValueAt(i,12)+
                                        "' and kd_dokter='"+tbRawatDrPr.getValueAt(i,5).toString()+
                                        "' and nip='"+tbRawatDrPr.getValueAt(i,7).toString()+
                                        "' and tgl_perawatan='"+tbRawatDrPr.getValueAt(i,9).toString()+
                                        "' and jam_rawat='"+tbRawatDrPr.getValueAt(i,10).toString()+"' ")==true){
                                    ttljmdokter=ttljmdokter+Double.parseDouble(tbRawatDrPr.getValueAt(i,13).toString());
                                    ttljmperawat=ttljmperawat+Double.parseDouble(tbRawatDrPr.getValueAt(i,14).toString());
                                    ttlkso=ttlkso+Double.parseDouble(tbRawatDrPr.getValueAt(i,15).toString());
                                    ttlpendapatan=ttlpendapatan+Double.parseDouble(tbRawatDrPr.getValueAt(i,11).toString());
                                    ttljasasarana=ttljasasarana+Double.parseDouble(tbRawatDrPr.getValueAt(i,16).toString());
                                    ttlbhp=ttlbhp+Double.parseDouble(tbRawatDrPr.getValueAt(i,17).toString());
                                    ttlmenejemen=ttlmenejemen+Double.parseDouble(tbRawatDrPr.getValueAt(i,18).toString());
                                }else{
                                    sukses=false;
                                }
                            }
                        }                            
                    }
                    
                    if(sukses==true){
                        Sequel.queryu("delete from tampjurnal");
                        if(ttlpendapatan>0){
                            Sequel.menyimpan("tampjurnal","'"+Suspen_Piutang_Tindakan_Ralan+"','Suspen Piutang Tindakan Ralan','0','"+ttlpendapatan+"'","kredit=kredit+'"+(ttlpendapatan)+"'","kd_rek='"+Suspen_Piutang_Tindakan_Ralan+"'");   
                            Sequel.menyimpan("tampjurnal","'"+Tindakan_Ralan+"','Pendapatan Tindakan Rawat Jalan','"+ttlpendapatan+"','0'","debet=debet+'"+(ttlpendapatan)+"'","kd_rek='"+Tindakan_Ralan+"'");                             
                        }
                        if(ttljmdokter>0){
                            Sequel.menyimpan("tampjurnal","'"+Beban_Jasa_Medik_Dokter_Tindakan_Ralan+"','Beban Jasa Medik Dokter Tindakan Ralan','0','"+ttljmdokter+"'","kredit=kredit+'"+(ttljmdokter)+"'","kd_rek='"+Beban_Jasa_Medik_Dokter_Tindakan_Ralan+"'");    
                            Sequel.menyimpan("tampjurnal","'"+Utang_Jasa_Medik_Dokter_Tindakan_Ralan+"','Utang Jasa Medik Dokter Tindakan Ralan','"+ttljmdokter+"','0'","debet=debet+'"+(ttljmdokter)+"'","kd_rek='"+Utang_Jasa_Medik_Dokter_Tindakan_Ralan+"'");                              
                        }
                        if(ttljmperawat>0){
                            Sequel.menyimpan("tampjurnal","'"+Beban_Jasa_Medik_Paramedis_Tindakan_Ralan+"','Beban Jasa Medik Paramedis Tindakan Ralan','0','"+ttljmperawat+"'","kredit=kredit+'"+(ttljmperawat)+"'","kd_rek='"+Beban_Jasa_Medik_Paramedis_Tindakan_Ralan+"'");     
                            Sequel.menyimpan("tampjurnal","'"+Utang_Jasa_Medik_Paramedis_Tindakan_Ralan+"','Utang Jasa Medik Paramedis Tindakan Ralan','"+ttljmperawat+"','0'","debet=debet+'"+(ttljmperawat)+"'","kd_rek='"+Utang_Jasa_Medik_Paramedis_Tindakan_Ralan+"'");                              
                        }
                        if(ttlkso>0){
                            Sequel.menyimpan("tampjurnal","'"+Beban_KSO_Tindakan_Ralan+"','Beban KSO Tindakan Ralan','0','"+ttlkso+"'","kredit=kredit+'"+(ttlkso)+"'","kd_rek='"+Beban_KSO_Tindakan_Ralan+"'");    
                            Sequel.menyimpan("tampjurnal","'"+Utang_KSO_Tindakan_Ralan+"','Utang KSO Tindakan Ralan','"+ttlkso+"','0'","debet=debet+'"+(ttlkso)+"'","kd_rek='"+Utang_KSO_Tindakan_Ralan+"'");                               
                        }
                        if(ttlmenejemen>0){
                            Sequel.menyimpan("tampjurnal","'"+Beban_Jasa_Menejemen_Tindakan_Ralan+"','Beban Jasa Menejemen Tindakan Ralan','0','"+ttlmenejemen+"'","kredit=kredit+'"+(ttlmenejemen)+"'","kd_rek='"+Beban_Jasa_Menejemen_Tindakan_Ralan+"'");    
                            Sequel.menyimpan("tampjurnal","'"+Utang_Jasa_Menejemen_Tindakan_Ralan+"','Utang Jasa Menejemen Tindakan Ralan','"+ttlmenejemen+"','0'","debet=debet+'"+(ttlmenejemen)+"'","kd_rek='"+Utang_Jasa_Menejemen_Tindakan_Ralan+"'");                                
                        }
                        if(ttljasasarana>0){
                            Sequel.menyimpan("tampjurnal","'"+Beban_Jasa_Sarana_Tindakan_Ralan+"','Beban Jasa Sarana Tindakan Ralan','0','"+ttljasasarana+"'","kredit=kredit+'"+(ttljasasarana)+"'","kd_rek='"+Beban_Jasa_Sarana_Tindakan_Ralan+"'");    
                            Sequel.menyimpan("tampjurnal","'"+Utang_Jasa_Sarana_Tindakan_Ralan+"','Utang Jasa Sarana Tindakan Ralan','"+ttljasasarana+"','0'","debet=debet+'"+(ttljasasarana)+"'","kd_rek='"+Utang_Jasa_Sarana_Tindakan_Ralan+"'");                              
                        }
                        if(ttlbhp>0){
                            Sequel.menyimpan("tampjurnal","'"+HPP_BHP_Tindakan_Ralan+"','HPP BHP Tindakan Ralan','0','"+ttlbhp+"'","kredit=kredit+'"+(ttlbhp)+"'","kd_rek='"+HPP_BHP_Tindakan_Ralan+"'");    
                            Sequel.menyimpan("tampjurnal","'"+Persediaan_BHP_Tindakan_Ralan+"','Persediaan BHP Tindakan Ralan','"+ttlbhp+"','0'","debet=debet+'"+(ttlbhp)+"'","kd_rek='"+Persediaan_BHP_Tindakan_Ralan+"'");                            
                        }
                        sukses=jur.simpanJurnal(TNoRw.getText(),"U","PEMBATALAN TINDAKAN RAWAT JALAN PASIEN "+TNoRM.getText()+" "+TPasien.getText()+" OLEH "+akses.getkode());
                    }
                        
                    if(sukses==true){
                        Sequel.Commit();
                        for(i=0;i<tbRawatDrPr.getRowCount();i++){
                            if(tbRawatDrPr.getValueAt(i,0).toString().equals("true")){ 
                                tabModeDrPr.removeRow(i);
                                i--;
                            }
                        }
                        LCount.setText(""+tabModeDrPr.getRowCount());
                    }else{
                        sukses=false;
                        JOptionPane.showMessageDialog(null,"Terjadi kesalahan saat pemrosesan data, transaksi dibatalkan.\nPeriksa kembali data sebelum melanjutkan menyimpan..!!");
                        Sequel.RollBack();
                    }
                    Sequel.AutoComitTrue();
                }   break;
            case 1:
                if(tabModePemeriksaan.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                    TNoRw.requestFocus();
                }else{
                    for(i=0;i<tbPemeriksaan.getRowCount();i++){
                        if(tbPemeriksaan.getValueAt(i,0).toString().equals("true")){
                            if(akses.getkode().equals("Admin Utama")){
                                Sequel.queryu("delete from pemeriksaan_ralan where no_rawat='"+tbPemeriksaan.getValueAt(i,1).toString()+
                                        "' and tgl_perawatan='"+tbPemeriksaan.getValueAt(i,4).toString()+
                                        "' and jam_rawat='"+tbPemeriksaan.getValueAt(i,5).toString()+"' ");
                                tabModePemeriksaan.removeRow(i);
                                i--;
                            }else{
                                if(akses.getkode().equals(tbPemeriksaan.getValueAt(i,23).toString())){
                                    Sequel.queryu("delete from pemeriksaan_ralan where no_rawat='"+tbPemeriksaan.getValueAt(i,1).toString()+
                                            "' and tgl_perawatan='"+tbPemeriksaan.getValueAt(i,4).toString()+
                                            "' and jam_rawat='"+tbPemeriksaan.getValueAt(i,5).toString()+"' ");
                                    tabModePemeriksaan.removeRow(i);
                                    i--;
                                }else{
                                    JOptionPane.showMessageDialog(null,"Hanya bisa dihapus oleh dokter/petugas yang bersangkutan..!!");
                                }
                            }
                        }
                    }
                    LCount.setText(""+tabModePemeriksaan.getRowCount());
                }   break;
            default:
                break;
        }

        BtnBatalActionPerformed(evt);
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnPrint);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(! TCari.getText().trim().equals("")){
            BtnCariActionPerformed(evt);
        }
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                if(tabModeDr.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                }else if(tabModeDr.getRowCount()!=0){
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());
                    param.put("logo",Sequel.cariGambar("select setting.logo from setting"));
                    String pas=" and reg_periksa.no_rkm_medis like '%"+TCariPasien.getText()+"%' ";
                    
                    String tgl=" rawat_jl_dr.tgl_perawatan between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+pas;
                    Valid.MyReportqry("rptJalanDr.jasper","report","::[ Data Rawat Jalan Yang Ditangani Dokter ]::",
                            "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                                    "jns_perawatan.nm_perawatan,rawat_jl_dr.kd_dokter,dokter.nm_dokter,"+
                                    "rawat_jl_dr.tgl_perawatan,rawat_jl_dr.jam_rawat,rawat_jl_dr.biaya_rawat " +
                                    "from pasien inner join reg_periksa inner join jns_perawatan inner join "+
                                    "dokter inner join rawat_jl_dr "+
                                    "on rawat_jl_dr.no_rawat=reg_periksa.no_rawat "+
                                    "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                                    "and rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                                    "and rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                                    "where "+tgl+" and rawat_jl_dr.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and jns_perawatan.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and rawat_jl_dr.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and tgl_perawatan like '%"+TCari.getText().trim()+"%' "+
                                            " order by rawat_jl_dr.no_rawat desc",param);
                    
                }   break;
            case 1:
                if(tabModePr.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                }else if(tabModePr.getRowCount()!=0){
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());
                    param.put("logo",Sequel.cariGambar("select setting.logo from setting"));
                    String pas=" and reg_periksa.no_rkm_medis like '%"+TCariPasien.getText()+"%' ";
                    
                    String tgl=" rawat_jl_pr.tgl_perawatan between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+pas;
                    Valid.MyReportqry("rptJalanPr.jasper","report","::[ Data Rawat Jalan Yang Ditangani Perawat ]::",
                            "select rawat_jl_pr.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                                    "jns_perawatan.nm_perawatan,rawat_jl_pr.nip,petugas.nama,"+
                                    "rawat_jl_pr.tgl_perawatan,rawat_jl_pr.jam_rawat,rawat_jl_pr.biaya_rawat " +
                                    "from pasien inner join reg_periksa inner join jns_perawatan inner join "+
                                    "petugas inner join rawat_jl_pr "+
                                    "on rawat_jl_pr.no_rawat=reg_periksa.no_rawat "+
                                    "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                                    "and rawat_jl_pr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                                    "and rawat_jl_pr.nip=petugas.nip where  "+
                                    tgl+"and rawat_jl_pr.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and jns_perawatan.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and rawat_jl_pr.nip like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and rawat_jl_pr.tgl_perawatan like '%"+TCari.getText().trim()+"%'  "+
                                            "order by rawat_jl_pr.no_rawat desc",param);
                }   break;
            case 2:
                if(tabModeDrPr.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                }else if(tabModeDrPr.getRowCount()!=0){
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());
                    param.put("logo",Sequel.cariGambar("select setting.logo from setting"));
                    String pas=" and reg_periksa.no_rkm_medis like '%"+TCariPasien.getText()+"%' ";
                    
                    String tgl=" rawat_jl_drpr.tgl_perawatan between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+pas;
                    Valid.MyReportqry("rptJalanDrPr.jasper","report","::[ Data Rawat Jalan Yang Ditangani Dokter ]::",
                            "select rawat_jl_drpr.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                                    "jns_perawatan.nm_perawatan,rawat_jl_drpr.kd_dokter,dokter.nm_dokter,rawat_jl_drpr.nip,petugas.nama,"+
                                    "rawat_jl_drpr.tgl_perawatan,rawat_jl_drpr.jam_rawat,rawat_jl_drpr.biaya_rawat " +
                                    "from pasien inner join reg_periksa inner join jns_perawatan inner join "+
                                    "dokter inner join rawat_jl_drpr inner join "+
                                    "petugas on rawat_jl_drpr.no_rawat=reg_periksa.no_rawat "+
                                    "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                                    "and rawat_jl_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                                    "and rawat_jl_drpr.kd_dokter=dokter.kd_dokter "+
                                    "and rawat_jl_drpr.nip=petugas.nip "+
                                    "where "+tgl+" and rawat_jl_drpr.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and jns_perawatan.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and rawat_jl_drpr.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and rawat_jl_drpr.nip like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and tgl_perawatan like '%"+TCari.getText().trim()+"%' "+
                                            " order by rawat_jl_drpr.no_rawat desc",param);
                }   break;
            case 3:
                if(tabModePemeriksaan.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                }else if(tabModePemeriksaan.getRowCount()!=0){
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());
                    param.put("logo",Sequel.cariGambar("select setting.logo from setting"));
                    String pas=" and reg_periksa.no_rkm_medis like '%"+TCariPasien.getText()+"%' ";
                    
                    String tgl=" pemeriksaan_ralan.tgl_perawatan between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+pas;
                    Valid.MyReportqry("rptJalanPemeriksaan.jasper","report","::[ Data Pemeriksaan Rawat Jalan ]::",
                            "select pemeriksaan_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                            "pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.suhu_tubuh,pemeriksaan_ralan.tensi, " +
                            "pemeriksaan_ralan.nadi,pemeriksaan_ralan.respirasi,pemeriksaan_ralan.tinggi, " +
                            "pemeriksaan_ralan.berat,pemeriksaan_ralan.spo2,pemeriksaan_ralan.gcs,pemeriksaan_ralan.kesadaran,pemeriksaan_ralan.keluhan, " +
                            "pemeriksaan_ralan.pemeriksaan,pemeriksaan_ralan.alergi,pemeriksaan_ralan.lingkar_perut,"+
                            "pemeriksaan_ralan.rtl,pemeriksaan_ralan.penilaian,pemeriksaan_ralan.instruksi,pemeriksaan_ralan.evaluasi,pemeriksaan_ralan.nip,pegawai.nama "+
                            "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                            "inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "+
                            "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik where  "+
                            tgl+"and (pemeriksaan_ralan.no_rawat like '%"+TCari.getText().trim()+"%' or reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                            "pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or pemeriksaan_ralan.alergi like '%"+TCari.getText().trim()+"%' or "+
                            "pemeriksaan_ralan.keluhan like '%"+TCari.getText().trim()+"%' or pemeriksaan_ralan.penilaian like '%"+TCari.getText().trim()+"%' or "+
                            "pemeriksaan_ralan.pemeriksaan like '%"+TCari.getText().trim()+"%' or pegawai.nama like '%"+TCari.getText().trim()+"%') "+
                            "order by pemeriksaan_ralan.no_rawat desc",param);
                }   break;
            default:
                break;
        }

        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnAll);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        petugas.dispose();
        dokter.dispose();
        pasien.dispose();
        try {
            i=JOptionPane.showConfirmDialog(null, "Mau skalian update status pasien sudah diperiksa ????","Konfirmasi",JOptionPane.YES_NO_OPTION);
            if(i==JOptionPane.YES_OPTION){
                Sequel.mengedit("reg_periksa","no_rawat=?","stts=?",2,new String[]{"Sudah",TNoRw.getText()});
            }
        } catch (Exception e) {
        }
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnPrint,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        TCariPasien.setText("");
        TampilkanData();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnPrint, BtnKeluar);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            TampilkanData();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            switch (TabRawat.getSelectedIndex()) {
                case 0:
                    if(TabRawatTindakanDokterPetugas.getSelectedIndex()==0){
                        tbTindakan3.requestFocus();
                    }else if(TabRawatTindakanDokterPetugas.getSelectedIndex()==1){
                        tbRawatDrPr.requestFocus();
                    }
                    break;
                case 1:
                    tbPemeriksaan.requestFocus();
                    break;
                default:
                    break;
            }
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        TampilkanData();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                BtnSimpan.setEnabled(akses.gettindakan_ralan());
                BtnHapus.setEnabled(akses.gettindakan_ralan());
                BtnEdit.setEnabled(akses.gettindakan_ralan());
                BtnPrint.setEnabled(akses.gettindakan_ralan());
                BtnTambahTindakan.setVisible(true);
                TCari.setPreferredSize(new Dimension(207,23));            
                TabRawatTindakanDokterPetugasMouseClicked(null);
                break;
            case 1:
                BtnSimpan.setEnabled(akses.gettindakan_ralan());
                BtnHapus.setEnabled(akses.gettindakan_ralan());
                BtnEdit.setEnabled(akses.gettindakan_ralan());
                BtnPrint.setEnabled(akses.gettindakan_ralan());
                BtnTambahTindakan.setVisible(true); 
                TCari.setPreferredSize(new Dimension(207,23));
                tampilPemeriksaan();
                break;
            case 2:
                BtnSimpan.setEnabled(akses.gettindakan_ralan());
                BtnHapus.setEnabled(akses.gettindakan_ralan());
                BtnEdit.setEnabled(akses.gettindakan_ralan());
                BtnPrint.setEnabled(akses.gettindakan_ralan());
                BtnTambahTindakan.setVisible(true); 
                TCari.setPreferredSize(new Dimension(207,23));
                TabRawatTindakanDokterPetugasMouseClicked(null);
                break;
            default:
                break;
        }
    }//GEN-LAST:event_TabRawatMouseClicked

private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
  isForm();                
}//GEN-LAST:event_ChkInputActionPerformed

private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"No.Rawat");
        }else{
            switch (TabRawat.getSelectedIndex()) {
                case 3:
                    if(KdPeg.getText().trim().equals("")||TPegawai.getText().trim().equals("")){
                            Valid.textKosong(KdPeg,"Dokter/Paramedis masih kosong...!!");
                    }else if((!TKeluhan.getText().trim().equals(""))||(!TPemeriksaan.getText().trim().equals(""))||(!TSuhu.getText().trim().equals(""))||
                            (!TTensi.getText().trim().equals(""))||(!TAlergi.getText().trim().equals(""))||(!TTinggi.getText().trim().equals(""))||
                            (!TBerat.getText().trim().equals(""))||(!TRespirasi.getText().trim().equals(""))||(!TNadi.getText().trim().equals(""))||
                            (!TGCS.getText().trim().equals(""))||(!TindakLanjut.getText().trim().equals(""))||(!TPenilaian.getText().trim().equals(""))||
                            (!KdPeg.getText().trim().equals(""))||(!TPegawai.getText().trim().equals(""))||(!TInstruksi.getText().trim().equals(""))||
                            (!SpO2.getText().trim().equals(""))||(!TEvaluasi.getText().trim().equals(""))){
                        if(tbPemeriksaan.getSelectedRow()>-1){
                            if(akses.getkode().equals("Admin Utama")){
                                if(Sequel.mengedittf("pemeriksaan_ralan","no_rawat='"+tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),1)+
                                    "' and tgl_perawatan='"+tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),4)+
                                    "' and jam_rawat='"+tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),5)+"'",
                                    "no_rawat='"+TNoRw.getText()+"',suhu_tubuh='"+TSuhu.getText()+"',tensi='"+TTensi.getText()+"',"+
                                    "keluhan='"+TKeluhan.getText()+"',pemeriksaan='"+TPemeriksaan.getText()+"',"+
                                    "nadi='"+TNadi.getText()+"',respirasi='"+TRespirasi.getText()+"',"+
                                    "tinggi='"+TTinggi.getText()+"',berat='"+TBerat.getText()+"',spo2='"+SpO2.getText()+"',"+
                                    "gcs='"+TGCS.getText()+"',kesadaran='"+cmbKesadaran.getSelectedItem()+"',"+
                                    "alergi='"+TAlergi.getText()+"',lingkar_perut='"+LingkarPerut.getText()+"',"+
                                    "tgl_perawatan='"+Valid.SetTgl(DTPTgl.getSelectedItem()+"")+"',"+
                                    "jam_rawat='"+cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem()+"',"+
                                    "rtl='"+TindakLanjut.getText()+"',penilaian='"+TPenilaian.getText()+"',"+
                                    "instruksi='"+TInstruksi.getText()+"',evaluasi='"+TEvaluasi.getText()+"',nip='"+KdPeg.getText()+"'")==true){
                                        tbPemeriksaan.setValueAt(TNoRw.getText(),tbPemeriksaan.getSelectedRow(), 1);
                                        tbPemeriksaan.setValueAt(TNoRM.getText(),tbPemeriksaan.getSelectedRow(), 2);
                                        tbPemeriksaan.setValueAt(TPasien.getText(),tbPemeriksaan.getSelectedRow(), 3);
                                        tbPemeriksaan.setValueAt(Valid.SetTgl(DTPTgl.getSelectedItem()+""),tbPemeriksaan.getSelectedRow(), 4);
                                        tbPemeriksaan.setValueAt(cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),tbPemeriksaan.getSelectedRow(), 5);
                                        tbPemeriksaan.setValueAt(TSuhu.getText(),tbPemeriksaan.getSelectedRow(), 6);
                                        tbPemeriksaan.setValueAt(TTensi.getText(),tbPemeriksaan.getSelectedRow(), 7);
                                        tbPemeriksaan.setValueAt(TNadi.getText(),tbPemeriksaan.getSelectedRow(), 8);
                                        tbPemeriksaan.setValueAt(TRespirasi.getText(),tbPemeriksaan.getSelectedRow(), 9);
                                        tbPemeriksaan.setValueAt(TTinggi.getText(),tbPemeriksaan.getSelectedRow(), 10);
                                        tbPemeriksaan.setValueAt(TBerat.getText(),tbPemeriksaan.getSelectedRow(), 11);
                                        tbPemeriksaan.setValueAt(SpO2.getText(),tbPemeriksaan.getSelectedRow(), 12);
                                        tbPemeriksaan.setValueAt(TGCS.getText(),tbPemeriksaan.getSelectedRow(), 13);
                                        tbPemeriksaan.setValueAt(cmbKesadaran.getSelectedItem().toString(),tbPemeriksaan.getSelectedRow(), 14);
                                        tbPemeriksaan.setValueAt(TKeluhan.getText(),tbPemeriksaan.getSelectedRow(), 15);
                                        tbPemeriksaan.setValueAt(TPemeriksaan.getText(),tbPemeriksaan.getSelectedRow(), 16);
                                        tbPemeriksaan.setValueAt(TAlergi.getText(),tbPemeriksaan.getSelectedRow(), 17);
                                        tbPemeriksaan.setValueAt(LingkarPerut.getText(),tbPemeriksaan.getSelectedRow(), 18);
                                        tbPemeriksaan.setValueAt(TindakLanjut.getText(),tbPemeriksaan.getSelectedRow(), 19);
                                        tbPemeriksaan.setValueAt(TPenilaian.getText(),tbPemeriksaan.getSelectedRow(), 20);
                                        tbPemeriksaan.setValueAt(TInstruksi.getText(),tbPemeriksaan.getSelectedRow(), 21);
                                        tbPemeriksaan.setValueAt(TEvaluasi.getText(),tbPemeriksaan.getSelectedRow(), 22);
                                        tbPemeriksaan.setValueAt(KdPeg.getText(),tbPemeriksaan.getSelectedRow(), 23);
                                        tbPemeriksaan.setValueAt(TPegawai.getText(),tbPemeriksaan.getSelectedRow(), 24);
                                        tbPemeriksaan.setValueAt(Jabatan.getText(),tbPemeriksaan.getSelectedRow(), 25);
                                        TSuhu.setText("");TTensi.setText("");TNadi.setText("");TRespirasi.setText("");
                                        TTinggi.setText("");TBerat.setText("");TGCS.setText("");TKeluhan.setText("");
                                        TPemeriksaan.setText("");TAlergi.setText("");LingkarPerut.setText("");
                                        TindakLanjut.setText("");TPenilaian.setText("");TInstruksi.setText("");
                                        SpO2.setText("");TEvaluasi.setText("");
                                }   
                            }else{
                                if(akses.getkode().equals(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),23).toString())){
                                    if(Sequel.mengedittf("pemeriksaan_ralan","no_rawat='"+tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),1)+
                                        "' and tgl_perawatan='"+tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),4)+
                                        "' and jam_rawat='"+tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),5)+"'",
                                        "no_rawat='"+TNoRw.getText()+"',suhu_tubuh='"+TSuhu.getText()+"',tensi='"+TTensi.getText()+"',"+
                                        "keluhan='"+TKeluhan.getText()+"',pemeriksaan='"+TPemeriksaan.getText()+"',"+
                                        "nadi='"+TNadi.getText()+"',respirasi='"+TRespirasi.getText()+"',"+
                                        "tinggi='"+TTinggi.getText()+"',berat='"+TBerat.getText()+"',spo2='"+SpO2.getText()+"',"+
                                        "gcs='"+TGCS.getText()+"',kesadaran='"+cmbKesadaran.getSelectedItem()+"',"+
                                        "alergi='"+TAlergi.getText()+"',lingkar_perut='"+LingkarPerut.getText()+"',"+
                                        "tgl_perawatan='"+Valid.SetTgl(DTPTgl.getSelectedItem()+"")+"',"+
                                        "jam_rawat='"+cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem()+"',"+
                                        "rtl='"+TindakLanjut.getText()+"',penilaian='"+TPenilaian.getText()+"',"+
                                        "instruksi='"+TInstruksi.getText()+"',evaluasi='"+TEvaluasi.getText()+"'")==true){
                                            tbPemeriksaan.setValueAt(TNoRw.getText(),tbPemeriksaan.getSelectedRow(), 1);
                                            tbPemeriksaan.setValueAt(TNoRM.getText(),tbPemeriksaan.getSelectedRow(), 2);
                                            tbPemeriksaan.setValueAt(TPasien.getText(),tbPemeriksaan.getSelectedRow(), 3);
                                            tbPemeriksaan.setValueAt(Valid.SetTgl(DTPTgl.getSelectedItem()+""),tbPemeriksaan.getSelectedRow(), 4);
                                            tbPemeriksaan.setValueAt(cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),tbPemeriksaan.getSelectedRow(), 5);
                                            tbPemeriksaan.setValueAt(TSuhu.getText(),tbPemeriksaan.getSelectedRow(), 6);
                                            tbPemeriksaan.setValueAt(TTensi.getText(),tbPemeriksaan.getSelectedRow(), 7);
                                            tbPemeriksaan.setValueAt(TNadi.getText(),tbPemeriksaan.getSelectedRow(), 8);
                                            tbPemeriksaan.setValueAt(TRespirasi.getText(),tbPemeriksaan.getSelectedRow(), 9);
                                            tbPemeriksaan.setValueAt(TTinggi.getText(),tbPemeriksaan.getSelectedRow(), 10);
                                            tbPemeriksaan.setValueAt(TBerat.getText(),tbPemeriksaan.getSelectedRow(), 11);
                                            tbPemeriksaan.setValueAt(SpO2.getText(),tbPemeriksaan.getSelectedRow(), 12);
                                            tbPemeriksaan.setValueAt(TGCS.getText(),tbPemeriksaan.getSelectedRow(), 13);
                                            tbPemeriksaan.setValueAt(cmbKesadaran.getSelectedItem().toString(),tbPemeriksaan.getSelectedRow(), 14);
                                            tbPemeriksaan.setValueAt(TKeluhan.getText(),tbPemeriksaan.getSelectedRow(), 15);
                                            tbPemeriksaan.setValueAt(TPemeriksaan.getText(),tbPemeriksaan.getSelectedRow(), 16);
                                            tbPemeriksaan.setValueAt(TAlergi.getText(),tbPemeriksaan.getSelectedRow(), 17);
                                            tbPemeriksaan.setValueAt(LingkarPerut.getText(),tbPemeriksaan.getSelectedRow(), 18);
                                            tbPemeriksaan.setValueAt(TindakLanjut.getText(),tbPemeriksaan.getSelectedRow(), 19);
                                            tbPemeriksaan.setValueAt(TPenilaian.getText(),tbPemeriksaan.getSelectedRow(), 20);
                                            tbPemeriksaan.setValueAt(TInstruksi.getText(),tbPemeriksaan.getSelectedRow(), 21);
                                            tbPemeriksaan.setValueAt(TEvaluasi.getText(),tbPemeriksaan.getSelectedRow(), 22);
                                            tbPemeriksaan.setValueAt(KdPeg.getText(),tbPemeriksaan.getSelectedRow(), 23);
                                            tbPemeriksaan.setValueAt(TPegawai.getText(),tbPemeriksaan.getSelectedRow(), 24);
                                            tbPemeriksaan.setValueAt(Jabatan.getText(),tbPemeriksaan.getSelectedRow(), 25);
                                            TSuhu.setText("");TTensi.setText("");TNadi.setText("");TRespirasi.setText("");
                                            TTinggi.setText("");TBerat.setText("");TGCS.setText("");TKeluhan.setText("");
                                            TPemeriksaan.setText("");TAlergi.setText("");LingkarPerut.setText("");
                                            TindakLanjut.setText("");TPenilaian.setText("");TInstruksi.setText("");
                                            SpO2.setText("");TEvaluasi.setText("");
                                    }   
                                }else{
                                    JOptionPane.showMessageDialog(null,"Hanya bisa diganti oleh dokter/petugas yang bersangkutan..!!");
                                }
                            }                         
                        }else{
                            JOptionPane.showMessageDialog(rootPane,"Silahkan pilih data yang mau diganti..!!");
                            TCari.requestFocus();
                        }
                    }   break;
                default:                
                    break;
            }
        }
}//GEN-LAST:event_BtnEditActionPerformed

private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void tbRawatDrPrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRawatDrPrMouseClicked
        if(tabModeDrPr.getRowCount()!=0){
            try {
                getDataDrPr();
            } catch (java.lang.NullPointerException e) {
            }            
        }
    }//GEN-LAST:event_tbRawatDrPrMouseClicked

    private void kdptg2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdptg2KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            TPerawat2.setText(petugas.tampil3(kdptg2.getText()));
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnSeekPetugas2ActionPerformed(null);
        }else{
            Valid.pindah(evt,KdDok2,BtnSeekPetugas2);
        }    
    }//GEN-LAST:event_kdptg2KeyPressed

    private void BtnSeekPetugas2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekPetugas2ActionPerformed
        akses.setform("DlgRawatJalan");
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_BtnSeekPetugas2ActionPerformed

    private void KdDok2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDok2KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select dokter.nm_dokter from dokter where dokter.kd_dokter=?",TDokter2,KdDok2.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnSeekDokter2ActionPerformed(null);
        }else{            
            Valid.pindah(evt,TNoRw,kdptg2);
        }
    }//GEN-LAST:event_KdDok2KeyPressed

    private void BtnSeekDokter2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekDokter2ActionPerformed
        akses.setform("DlgRawatJalan");
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnSeekDokter2ActionPerformed

    private void tbPemeriksaanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPemeriksaanMouseClicked
        if(tabModePemeriksaan.getRowCount()!=0){
            try {
                getDataPemeriksaan();
            } catch (java.lang.NullPointerException e) {
            }

        }
    }//GEN-LAST:event_tbPemeriksaanMouseClicked

    private void DTPTglKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPTglKeyPressed
//        Valid.pindah(evt,BtnSeekDokter,cmbJam);
    }//GEN-LAST:event_DTPTglKeyPressed

    private void cmbJamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJamKeyPressed
        Valid.pindah(evt,DTPTgl,cmbMnt);
    }//GEN-LAST:event_cmbJamKeyPressed

    private void cmbMntKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMntKeyPressed
        Valid.pindah(evt,cmbJam,cmbDtk);
    }//GEN-LAST:event_cmbMntKeyPressed

    private void cmbDtkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDtkKeyPressed
        Valid.pindah(evt,cmbMnt,TCari);
    }//GEN-LAST:event_cmbDtkKeyPressed

    private void ChkJlnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkJlnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkJlnActionPerformed

    private void btnPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienActionPerformed
        akses.setform("DlgRawatJalan");
        pasien.emptTeks();
        pasien.isCek();
        pasien.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        pasien.setLocationRelativeTo(internalFrame1);
        pasien.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_btnPasienActionPerformed

    private void btnPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPasienKeyPressed
        Valid.pindah(evt,TCariPasien,DTPCari1);
    }//GEN-LAST:event_btnPasienKeyPressed

    private void TKeluhanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKeluhanKeyPressed
        Valid.pindah2(evt,KdPeg,TPemeriksaan);
    }//GEN-LAST:event_TKeluhanKeyPressed

    private void tbRawatDrPrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRawatDrPrKeyReleased
        if(tabModeDrPr.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getDataDrPr();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbRawatDrPrKeyReleased

    private void tbPemeriksaanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPemeriksaanKeyReleased
        if(tabModePemeriksaan.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getDataPemeriksaan();
                } catch (java.lang.NullPointerException e) {
                }
            }

        }
    }//GEN-LAST:event_tbPemeriksaanKeyReleased

    private void tbTindakan3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbTindakan3KeyPressed
        if(tbTindakan3.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                try {
                    i=tbTindakan3.getSelectedColumn();
                    if(i==1){
                        if(tbTindakan3.getSelectedRow()>-1){
                            tbTindakan3.setValueAt(true,tbTindakan3.getSelectedRow(),0);
                        }
                        TCari.setText("");
                        TCari.requestFocus();
                    }
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
                TCari.setText("");
                TCari.requestFocus();
            }
        }
    }//GEN-LAST:event_tbTindakan3KeyPressed

    private void TabRawatTindakanDokterPetugasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatTindakanDokterPetugasMouseClicked
        if(TabRawatTindakanDokterPetugas.getSelectedIndex()==0){
            TCari.setText("");
        }else if(TabRawatTindakanDokterPetugas.getSelectedIndex()==1){
            TCari.setText("");
            TCariPasien.setText(TNoRM.getText());
        }
        TCari.requestFocus();
        tampilkanPenangananDokterPetugas(); 
    }//GEN-LAST:event_TabRawatTindakanDokterPetugasMouseClicked

    private void BtnTambahTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahTindakanActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DlgJnsPerawatanRalan perawatan=new DlgJnsPerawatanRalan(null,false);
        perawatan.emptTeks();
        perawatan.isCek();
        perawatan.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
        perawatan.setLocationRelativeTo(internalFrame1);
        perawatan.setAlwaysOnTop(false);
        perawatan.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahTindakanActionPerformed

    private void TPemeriksaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPemeriksaanKeyPressed
        Valid.pindah2(evt,TKeluhan,TSuhu);
    }//GEN-LAST:event_TPemeriksaanKeyPressed

    private void TSuhuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TSuhuKeyPressed
        Valid.pindah(evt,TPemeriksaan,TTinggi);
    }//GEN-LAST:event_TSuhuKeyPressed

    private void TTensiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TTensiKeyPressed
        Valid.pindah(evt,TBerat,TRespirasi);
    }//GEN-LAST:event_TTensiKeyPressed

    private void TTinggiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TTinggiKeyPressed
        Valid.pindah(evt,TSuhu,TBerat);
    }//GEN-LAST:event_TTinggiKeyPressed

    private void TRespirasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TRespirasiKeyPressed
        Valid.pindah(evt,TTensi,TNadi);
    }//GEN-LAST:event_TRespirasiKeyPressed

    private void TBeratKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TBeratKeyPressed
        Valid.pindah(evt,TTinggi,TTensi);
    }//GEN-LAST:event_TBeratKeyPressed

    private void TNadiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNadiKeyPressed
        Valid.pindah(evt,TRespirasi,SpO2);
    }//GEN-LAST:event_TNadiKeyPressed

    private void TGCSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TGCSKeyPressed
        Valid.pindah(evt,TNadi,cmbKesadaran);
    }//GEN-LAST:event_TGCSKeyPressed

    private void TAlergiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TAlergiKeyPressed
        Valid.pindah(evt,LingkarPerut,TPenilaian);
    }//GEN-LAST:event_TAlergiKeyPressed

    private void TPenilaianKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPenilaianKeyPressed
        Valid.pindah2(evt,TAlergi,TindakLanjut);
    }//GEN-LAST:event_TPenilaianKeyPressed

    private void TindakLanjutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TindakLanjutKeyPressed
        Valid.pindah2(evt,TPenilaian,TInstruksi);
    }//GEN-LAST:event_TindakLanjutKeyPressed

    private void cmbKesadaranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbKesadaranKeyPressed
        Valid.pindah(evt,TGCS,LingkarPerut);
    }//GEN-LAST:event_cmbKesadaranKeyPressed

    private void KdPegKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPegKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select pegawai.nama from pegawai where pegawai.nik=?",TPegawai,KdPeg.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnSeekPegawaiActionPerformed(null);
        }else{
            Valid.pindah(evt,TNoRw,TKeluhan);
        }
    }//GEN-LAST:event_KdPegKeyPressed

    private void BtnSeekPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekPegawaiActionPerformed
        akses.setform("DlgRawatJalan");
        pegawai.emptTeks();
        pegawai.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        pegawai.setLocationRelativeTo(internalFrame1);
        pegawai.setVisible(true);
    }//GEN-LAST:event_BtnSeekPegawaiActionPerformed

    private void TInstruksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TInstruksiKeyPressed
        Valid.pindah2(evt,TindakLanjut,TEvaluasi);
    }//GEN-LAST:event_TInstruksiKeyPressed

    private void SpO2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SpO2KeyPressed
        Valid.pindah(evt,TNadi,TGCS);
    }//GEN-LAST:event_SpO2KeyPressed

    private void TEvaluasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TEvaluasiKeyPressed
        Valid.pindah2(evt,TInstruksi,BtnSimpan);
    }//GEN-LAST:event_TEvaluasiKeyPressed

    private void LingkarPerutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LingkarPerutKeyPressed
        Valid.pindah(evt,cmbKesadaran,TAlergi); 
    }//GEN-LAST:event_LingkarPerutKeyPressed

    private void TNoRwMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TNoRwMouseClicked
        Window[] wins = Window.getWindows();
        for (Window win : wins) {
            if (win instanceof JDialog) {
                win.setLocationRelativeTo(internalFrame1);
                win.toFront();
            }
        }
    }//GEN-LAST:event_TNoRwMouseClicked

    private void Btn5SoapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn5SoapActionPerformed
        if(TPasien.getText().trim().equals("")||TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else if(TPegawai.getText().trim().equals("")||KdPeg.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu petugas/dokter pemberi asuhan...!!!");
            TCari.requestFocus();
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            soapterakhir.setNoRM(TNoRM.getText(),KdPeg.getText(),"Ralan");
            soapterakhir.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            soapterakhir.setLocationRelativeTo(internalFrame1);
            soapterakhir.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_Btn5SoapActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgRawatJalanAPS dialog = new DlgRawatJalanAPS(new javax.swing.JFrame(), true);
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
    private widget.Button Btn5Soap;
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSeekDokter2;
    private widget.Button BtnSeekPegawai;
    private widget.Button BtnSeekPetugas2;
    private widget.Button BtnSimpan;
    private widget.Button BtnTambahTindakan;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkJln;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.Tanggal DTPTgl;
    private widget.PanelBiasa FormInput;
    private widget.TextBox Jabatan;
    private widget.TextBox KdDok2;
    private widget.TextBox KdPeg;
    private widget.Label LCount;
    private widget.TextBox LingkarPerut;
    private javax.swing.JPanel PanelInput;
    private widget.ScrollPane Scroll10;
    private widget.ScrollPane Scroll3;
    private widget.ScrollPane Scroll9;
    private widget.TextBox SpO2;
    private widget.TextBox TAlergi;
    private widget.TextBox TBerat;
    private widget.TextBox TCari;
    private widget.TextBox TCariPasien;
    private widget.TextBox TDokter2;
    private widget.TextArea TEvaluasi;
    private widget.TextBox TGCS;
    private widget.TextArea TInstruksi;
    private widget.TextArea TKeluhan;
    private widget.TextBox TNadi;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TPegawai;
    private widget.TextArea TPemeriksaan;
    private widget.TextArea TPenilaian;
    private widget.TextBox TPerawat2;
    private widget.TextBox TRespirasi;
    private widget.TextBox TSuhu;
    private widget.TextBox TTensi;
    private widget.TextBox TTinggi;
    private javax.swing.JTabbedPane TabRawat;
    private javax.swing.JTabbedPane TabRawatTindakanDokterPetugas;
    private widget.TextArea TindakLanjut;
    private widget.Button btnPasien;
    private widget.ComboBox cmbDtk;
    private widget.ComboBox cmbJam;
    private widget.ComboBox cmbKesadaran;
    private widget.ComboBox cmbMnt;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame4;
    private widget.InternalFrame internalFrame5;
    private widget.Label jLabel10;
    private widget.Label jLabel12;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel28;
    private widget.Label jLabel29;
    private widget.Label jLabel3;
    private widget.Label jLabel37;
    private widget.Label jLabel4;
    private widget.Label jLabel41;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
    private widget.Label jLabel56;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator5;
    private widget.TextBox kdptg2;
    private widget.panelisi panelGlass11;
    private widget.panelisi panelGlass12;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.ScrollPane scrollPane3;
    private widget.ScrollPane scrollPane6;
    private widget.ScrollPane scrollPane7;
    private widget.ScrollPane scrollPane8;
    private widget.Table tbPemeriksaan;
    private widget.Table tbRawatDrPr;
    private widget.Table tbTindakan3;
    // End of variables declaration//GEN-END:variables

    private void tampilDr() {
        Valid.tabelKosong(tabModeDr);
        try{
            ps=koneksi.prepareStatement("select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                   "concat(rawat_jl_dr.kd_jenis_prw,' ',jns_perawatan.nm_perawatan),rawat_jl_dr.kd_dokter,dokter.nm_dokter,"+
                   "rawat_jl_dr.tgl_perawatan,rawat_jl_dr.jam_rawat,rawat_jl_dr.biaya_rawat,rawat_jl_dr.kd_jenis_prw, " +
                   "rawat_jl_dr.tarif_tindakandr,rawat_jl_dr.kso,rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.menejemen "+
                   "from pasien inner join reg_periksa inner join jns_perawatan inner join "+
                   "dokter inner join rawat_jl_dr "+
                   "on rawat_jl_dr.no_rawat=reg_periksa.no_rawat "+
                   "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                   "and rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                   "and rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                   "where rawat_jl_dr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? "+
                   (TCari.getText().trim().equals("")?"":"and (rawat_jl_dr.no_rawat like ? or reg_periksa.no_rkm_medis like ? or pasien.nm_pasien like ? or "+
                   "jns_perawatan.nm_perawatan like ? or rawat_jl_dr.kd_dokter like ? or dokter.nm_dokter like ? )")+
                   " order by rawat_jl_dr.no_rawat,rawat_jl_dr.tgl_perawatan,rawat_jl_dr.jam_rawat desc");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(3,"%"+TCariPasien.getText()+"%");
                if(!TCari.getText().trim().equals("")){
                    ps.setString(4,"%"+TCari.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText().trim()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,"%"+TCari.getText().trim()+"%");
                    ps.setString(8,"%"+TCari.getText().trim()+"%");
                    ps.setString(9,"%"+TCari.getText().trim()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeDr.addRow(new Object[]{
                        false,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                        rs.getString(8),rs.getDouble(9),rs.getString("kd_jenis_prw"),rs.getString("tarif_tindakandr"),rs.getString("kso"),
                        rs.getString("material"),rs.getString("bhp"),rs.getString("menejemen")
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
        LCount.setText(""+tabModeDr.getRowCount());
    }

    private void tampilPr() {
        Valid.tabelKosong(tabModePr);
        try{  
            ps2=koneksi.prepareStatement("select rawat_jl_pr.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                   "concat(rawat_jl_pr.kd_jenis_prw,' ',jns_perawatan.nm_perawatan),rawat_jl_pr.nip,petugas.nama,"+
                   "rawat_jl_pr.tgl_perawatan,rawat_jl_pr.jam_rawat,rawat_jl_pr.biaya_rawat,rawat_jl_pr.kd_jenis_prw, " +
                   "rawat_jl_pr.tarif_tindakanpr,rawat_jl_pr.kso,rawat_jl_pr.material,rawat_jl_pr.bhp,rawat_jl_pr.menejemen "+
                   "from pasien inner join reg_periksa inner join jns_perawatan inner join "+
                   "petugas inner join rawat_jl_pr "+
                   "on rawat_jl_pr.no_rawat=reg_periksa.no_rawat "+
                   "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                   "and rawat_jl_pr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                   "and rawat_jl_pr.nip=petugas.nip where  "+
                   "rawat_jl_pr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? "+
                   (TCari.getText().trim().equals("")?"":"and (rawat_jl_pr.no_rawat like ? or reg_periksa.no_rkm_medis like ? or pasien.nm_pasien like ? or "+
                   "jns_perawatan.nm_perawatan like ? or rawat_jl_pr.nip like ? or petugas.nama like ?) ")+
                   "order by rawat_jl_pr.no_rawat,rawat_jl_pr.tgl_perawatan,rawat_jl_pr.jam_rawat desc"); 
            try{
                ps2.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps2.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps2.setString(3,"%"+TCariPasien.getText()+"%");
                if(!TCari.getText().trim().equals("")){
                    ps2.setString(4,"%"+TCari.getText().trim()+"%");
                    ps2.setString(5,"%"+TCari.getText().trim()+"%");
                    ps2.setString(6,"%"+TCari.getText().trim()+"%");
                    ps2.setString(7,"%"+TCari.getText().trim()+"%");
                    ps2.setString(8,"%"+TCari.getText().trim()+"%");
                    ps2.setString(9,"%"+TCari.getText().trim()+"%");
                }
                rs=ps2.executeQuery();
                while(rs.next()){
                    tabModePr.addRow(new Object[]{
                        false,rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),
                        rs.getString(7),rs.getString(8),rs.getDouble(9),
                        rs.getString("kd_jenis_prw"),rs.getString("tarif_tindakanpr"),
                        rs.getString("kso"),rs.getString("material"),
                        rs.getString("bhp"),rs.getString("menejemen")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps2!=null){
                    ps2.close();
                }
            }                  
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModePr.getRowCount());
    }
    
    private void tampilDrPr() {
        Valid.tabelKosong(tabModeDrPr);
        try{
            ps3=koneksi.prepareStatement("select rawat_jl_drpr.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien,"+
                   "concat(rawat_jl_drpr.kd_jenis_prw,' ',jns_perawatan.nm_perawatan),rawat_jl_drpr.kd_dokter,dokter.nm_dokter,"+
                   "rawat_jl_drpr.nip,petugas.nama,rawat_jl_drpr.tgl_perawatan,rawat_jl_drpr.jam_rawat,rawat_jl_drpr.biaya_rawat,rawat_jl_drpr.kd_jenis_prw, " +
                   "rawat_jl_drpr.tarif_tindakandr,rawat_jl_drpr.tarif_tindakanpr,rawat_jl_drpr.kso,rawat_jl_drpr.material,rawat_jl_drpr.bhp,rawat_jl_drpr.menejemen  "+
                   "from pasien_aps inner join reg_periksa inner join jns_perawatan inner join "+
                   "dokter inner join rawat_jl_drpr inner join petugas on rawat_jl_drpr.no_rawat=reg_periksa.no_rawat "+
                   "and reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis and rawat_jl_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                   "and rawat_jl_drpr.kd_dokter=dokter.kd_dokter and rawat_jl_drpr.nip=petugas.nip "+
                   "where rawat_jl_drpr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? "+
                   (TCari.getText().trim().equals("")?"":"and (rawat_jl_drpr.no_rawat like ? or reg_periksa.no_rkm_medis like ? or pasien_aps.nm_pasien like ? or "+
                   "jns_perawatan.nm_perawatan like ? or rawat_jl_drpr.kd_dokter like ? or dokter.nm_dokter like ? or "+
                   "rawat_jl_drpr.nip like ? or petugas.nama like ?)")+
                   " order by rawat_jl_drpr.no_rawat,rawat_jl_drpr.tgl_perawatan,rawat_jl_drpr.jam_rawat desc");
            try{
                ps3.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps3.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps3.setString(3,"%"+TCariPasien.getText()+"%");
                if(!TCari.getText().trim().equals("")){
                    ps3.setString(4,"%"+TCari.getText().trim()+"%");
                    ps3.setString(5,"%"+TCari.getText().trim()+"%");
                    ps3.setString(6,"%"+TCari.getText().trim()+"%");
                    ps3.setString(7,"%"+TCari.getText().trim()+"%");
                    ps3.setString(8,"%"+TCari.getText().trim()+"%");
                    ps3.setString(9,"%"+TCari.getText().trim()+"%");
                    ps3.setString(10,"%"+TCari.getText().trim()+"%");
                    ps3.setString(11,"%"+TCari.getText().trim()+"%");
                }
                rs=ps3.executeQuery();
                while(rs.next()){
                    tabModeDrPr.addRow(new Object[]{
                        false,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                        rs.getString(8),rs.getString(9),rs.getString(10),rs.getDouble(11),rs.getString("kd_jenis_prw"),
                        rs.getString("tarif_tindakandr"),rs.getString("tarif_tindakanpr"),rs.getString("kso"),
                        rs.getString("material"),rs.getString("bhp"),rs.getString("menejemen")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps3!=null){
                    ps3.close();
                }
            }              
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModeDrPr.getRowCount());
    }
    
    private void getDataDrPr() {
        if(tbRawatDrPr.getSelectedRow()!= -1){
            TNoRw.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),1).toString());
            TNoRM.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),2).toString());
            TPasien.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),3).toString());
            KdDok2.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),5).toString());
            TDokter2.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),6).toString());
            kdptg2.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),7).toString());
            TPerawat2.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),8).toString());
            cmbJam.setSelectedItem(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),10).toString().substring(0,2));
            cmbMnt.setSelectedItem(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),10).toString().substring(3,5));
            cmbDtk.setSelectedItem(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),10).toString().substring(6,8));
            Valid.SetTgl(DTPTgl,tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),9).toString());
        }
    }
    
    private void isRawat(){
        Sequel.cariIsi("select reg_periksa.no_rkm_medis from reg_periksa where reg_periksa.no_rawat=? ",TNoRM,TNoRw.getText());
        TCariPasien.setText(TNoRM.getText());
    }

    private void isPsien(){
        Sequel.cariIsi("select concat(pasien_aps.nm_pasien,' (',pasien_aps.umur,')') from pasien_aps where pasien_aps.no_rkm_medis=? ",TPasien,TNoRM.getText());
    }
    
    public void setNoRm(String norwt,Date tgl1,Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText("");
        DTPCari1.setDate(tgl1);
        DTPCari2.setDate(tgl2);
        isRawat();
        isPsien();  
        ChkInput.setSelected(true);
        isForm();
        TabRawatMouseClicked(null);
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,273));
            panelGlass12.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            panelGlass12.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    
    private void isMenu(){
        /*if(ChkAccor.isSelected()==true){
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(205,HEIGHT));
            FormMenu.setVisible(true);  
            ChkAccor.setVisible(true);
        }else if(ChkAccor.isSelected()==false){
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(15,HEIGHT));
            FormMenu.setVisible(false);
            ChkAccor.setVisible(true);
        }*/
    }
    
    public void isCek(){
        tinggi=0;
        BtnSimpan.setEnabled(akses.gettindakan_ralan());
        BtnHapus.setEnabled(akses.gettindakan_ralan());
        BtnEdit.setEnabled(akses.gettindakan_ralan());
        BtnPrint.setEnabled(akses.gettindakan_ralan());
        BtnTambahTindakan.setEnabled(akses.gettarif_ralan());    
        
        if(akses.getjml2()>=1){
            KdPeg.setText(akses.getkode());
            TPegawai.setText(Sequel.cariIsi("select nama from pegawai where nik='"+KdPeg.getText()+"'"));
            Sequel.cariIsi("select pegawai.jbtn from pegawai where pegawai.nik=?",Jabatan,KdPeg.getText());
        }
        kdptg2.setText(akses.getkode());
        TPerawat2.setText(Sequel.cariIsi("select nama from petugas where nip='"+kdptg2.getText()+"'"));
    }

    private void tampilPemeriksaan() {
        Valid.tabelKosong(tabModePemeriksaan);
        try{  
            ps4=koneksi.prepareStatement("select pemeriksaan_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien,"+
                    "pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.suhu_tubuh,pemeriksaan_ralan.tensi, " +
                    "pemeriksaan_ralan.nadi,pemeriksaan_ralan.respirasi,pemeriksaan_ralan.tinggi, " +
                    "pemeriksaan_ralan.berat,pemeriksaan_ralan.spo2,pemeriksaan_ralan.gcs,pemeriksaan_ralan.kesadaran,pemeriksaan_ralan.keluhan, " +
                    "pemeriksaan_ralan.pemeriksaan,pemeriksaan_ralan.alergi,pemeriksaan_ralan.lingkar_perut,pemeriksaan_ralan.rtl,"+
                    "pemeriksaan_ralan.penilaian,pemeriksaan_ralan.instruksi,pemeriksaan_ralan.evaluasi,pemeriksaan_ralan.nip,pegawai.nama,pegawai.jbtn "+
                    "from pasien_aps inner join reg_periksa on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                    "inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "+
                    "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik where  "+
                    "pemeriksaan_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? "+
                    (TCari.getText().trim().equals("")?"":"and (pemeriksaan_ralan.no_rawat like ? or reg_periksa.no_rkm_medis like ? or pasien_aps.nm_pasien like ? or "+
                    "pemeriksaan_ralan.alergi like ? or pemeriksaan_ralan.keluhan like ? or pemeriksaan_ralan.penilaian like ? or "+
                    "pemeriksaan_ralan.pemeriksaan like ? or pegawai.nama like ?) ")+"order by pemeriksaan_ralan.no_rawat,pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat desc"); 
            try{
                ps4.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(3,"%"+TCariPasien.getText()+"%");
                if(!TCari.getText().trim().equals("")){
                    ps4.setString(4,"%"+TCari.getText().trim()+"%");
                    ps4.setString(5,"%"+TCari.getText().trim()+"%");
                    ps4.setString(6,"%"+TCari.getText().trim()+"%");
                    ps4.setString(7,"%"+TCari.getText().trim()+"%");
                    ps4.setString(8,"%"+TCari.getText().trim()+"%");
                    ps4.setString(9,"%"+TCari.getText().trim()+"%");
                    ps4.setString(10,"%"+TCari.getText().trim()+"%");
                    ps4.setString(11,"%"+TCari.getText().trim()+"%");
                }
                rs=ps4.executeQuery();
                while(rs.next()){
                    tabModePemeriksaan.addRow(new Object[]{
                        false,rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                        rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
                        rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),
                        rs.getString(16),rs.getString(17),rs.getString(18),rs.getString(19),
                        rs.getString(20),rs.getString(21),rs.getString(22),rs.getString(23),
                        rs.getString(24),rs.getString(25)
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps4!=null){
                    ps4.close();
                }
            }                  
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModePemeriksaan.getRowCount());
    }
    
    private void tampilCatatan() {
        Valid.tabelKosong(TabModeCatatan);
        try{  
            ps4=koneksi.prepareStatement("select catatan_perawatan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                    "catatan_perawatan.tanggal,catatan_perawatan.jam,catatan_perawatan.kd_dokter,dokter.nm_dokter,"+
                    "catatan_perawatan.catatan from pasien inner join reg_periksa inner join catatan_perawatan inner join dokter "+
                    "on catatan_perawatan.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "and catatan_perawatan.kd_dokter=dokter.kd_dokter where  "+
                    "catatan_perawatan.tanggal between ? and ? and reg_periksa.no_rkm_medis like ? "+
                    (TCari.getText().trim().equals("")?"":"and (catatan_perawatan.no_rawat like ? or reg_periksa.no_rkm_medis like ? or pasien.nm_pasien like ? or  "+
                    "catatan_perawatan.catatan like ? or catatan_perawatan.kd_dokter like ? or dokter.nm_dokter like ?) ")+
                    "order by catatan_perawatan.no_rawat,catatan_perawatan.tanggal,catatan_perawatan.jam desc"); 
            try{
                ps4.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(3,"%"+TCariPasien.getText()+"%");
                if(!TCari.getText().trim().equals("")){
                    ps4.setString(4,"%"+TCari.getText().trim()+"%");
                    ps4.setString(5,"%"+TCari.getText().trim()+"%");
                    ps4.setString(6,"%"+TCari.getText().trim()+"%");
                    ps4.setString(7,"%"+TCari.getText().trim()+"%");
                    ps4.setString(8,"%"+TCari.getText().trim()+"%");
                    ps4.setString(9,"%"+TCari.getText().trim()+"%");
                }
                rs=ps4.executeQuery();
                while(rs.next()){
                    TabModeCatatan.addRow(new Object[]{
                        false,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi Catatan : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps4!=null){
                    ps4.close();
                }
            }                  
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+TabModeCatatan.getRowCount());
    }

    private void getDataPemeriksaan() {
        if(tbPemeriksaan.getSelectedRow()!= -1){
            TNoRw.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),1).toString());
            TNoRM.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),2).toString());
            TPasien.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),3).toString());             
            TSuhu.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),6).toString()); 
            TTensi.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),7).toString()); 
            TNadi.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),8).toString()); 
            TRespirasi.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),9).toString()); 
            TTinggi.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),10).toString()); 
            TBerat.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),11).toString());  
            SpO2.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),12).toString()); 
            TGCS.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),13).toString());   
            cmbKesadaran.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),14).toString()); 
            TKeluhan.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),15).toString()); 
            TPemeriksaan.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),16).toString()); 
            TAlergi.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),17).toString()); 
            LingkarPerut.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),18).toString()); 
            TindakLanjut.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),19).toString()); 
            TPenilaian.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),20).toString()); 
            TInstruksi.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),21).toString()); 
            TEvaluasi.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),22).toString()); 
            cmbJam.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),5).toString().substring(0,2));
            cmbMnt.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),5).toString().substring(3,5));
            cmbDtk.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),5).toString().substring(6,8));
            Valid.SetTgl(DTPTgl,tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),4).toString());
        }
    }
    
      
        
    
    private void jam(){
        ActionListener taskPerformer = new ActionListener(){
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;
            @Override
            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                // Membuat Date
                //Date dt = new Date();
                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if(ChkJln.isSelected()==true){
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                }else if(ChkJln.isSelected()==false){
                    nilai_jam =cmbJam.getSelectedIndex();
                    nilai_menit =cmbMnt.getSelectedIndex();
                    nilai_detik =cmbDtk.getSelectedIndex();
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
                cmbJam.setSelectedItem(jam);
                cmbMnt.setSelectedItem(menit);
                cmbDtk.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }
    
    public void setNoRm(String norwt,Date tgl1,Date tgl2,String kodedokter, String namadokter) {
        TNoRw.setText(norwt);
        DTPCari1.setDate(tgl1);
        DTPCari2.setDate(tgl2);
        isRawat();
        isPsien();  
        ChkInput.setSelected(true);
        isForm(); 
        KdDok2.setText(kodedokter);
        TDokter2.setText(namadokter); 
    }
    
    public void SetPoli(String KodePoli){
        this.kode_poli=KodePoli;
    }
    
    public void SetPj(String KodePj){
        this.kd_pj=KodePj;
    }
    
    
    private void tampilPemeriksaanGinekologi() {
        Valid.tabelKosong(tabModeGinekologi);
        try{
            ps6=koneksi.prepareStatement("select pemeriksaan_ginekologi_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                    "pemeriksaan_ginekologi_ralan.tgl_perawatan,pemeriksaan_ginekologi_ralan.jam_rawat,pemeriksaan_ginekologi_ralan.inspeksi,pemeriksaan_ginekologi_ralan.inspeksi_vulva,pemeriksaan_ginekologi_ralan.inspekulo_gine, " +
                    "pemeriksaan_ginekologi_ralan.fluxus_gine,pemeriksaan_ginekologi_ralan.fluor_gine,pemeriksaan_ginekologi_ralan.vulva_inspekulo, " +
                    "pemeriksaan_ginekologi_ralan.portio_inspekulo,pemeriksaan_ginekologi_ralan.sondage,pemeriksaan_ginekologi_ralan.portio_dalam,pemeriksaan_ginekologi_ralan.bentuk, " +
                    "pemeriksaan_ginekologi_ralan.cavum_uteri,pemeriksaan_ginekologi_ralan.mobilitas,pemeriksaan_ginekologi_ralan.ukuran, pemeriksaan_ginekologi_ralan.nyeri_tekan, pemeriksaan_ginekologi_ralan.adnexa_kanan, pemeriksaan_ginekologi_ralan.adnexa_kiri," +
                    "pemeriksaan_ginekologi_ralan.cavum_douglas " +
                    "from pasien inner join reg_periksa inner join pemeriksaan_ginekologi_ralan "+
                    "on pemeriksaan_ginekologi_ralan.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis where  "+
                    "pemeriksaan_ginekologi_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? "+
                    (TCari.getText().trim().equals("")?"":"and (pemeriksaan_ginekologi_ralan.no_rawat like ? or reg_periksa.no_rkm_medis like ? or "+
                    "pasien.nm_pasien like ? or  pemeriksaan_ginekologi_ralan.inspeksi like ? or pemeriksaan_ginekologi_ralan.inspeksi_vulva like ? or "+
                    "pemeriksaan_ginekologi_ralan.inspekulo_gine like ?) ")+
                    "order by pemeriksaan_ginekologi_ralan.no_rawat,pemeriksaan_ginekologi_ralan.tgl_perawatan,pemeriksaan_ginekologi_ralan.jam_rawat desc");
            try {
                ps6.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps6.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps6.setString(3,"%"+TCariPasien.getText()+"%");
                if(!TCari.getText().trim().equals("")){
                    ps6.setString(4,"%"+TCari.getText().trim()+"%");
                    ps6.setString(5,"%"+TCari.getText().trim()+"%");
                    ps6.setString(6,"%"+TCari.getText().trim()+"%");
                    ps6.setString(7,"%"+TCari.getText().trim()+"%");
                    ps6.setString(8,"%"+TCari.getText().trim()+"%");
                    ps6.setString(9,"%"+TCari.getText().trim()+"%");
                }
                rs=ps6.executeQuery();
                while(rs.next()) {
                    tabModeGinekologi.addRow(new Object[] {
                        false, rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("inspeksi"),
                        rs.getString("inspeksi_vulva"),rs.getString("inspekulo_gine"),rs.getString("fluxus_gine"),
                        rs.getString("fluor_gine"),rs.getString("vulva_inspekulo"),rs.getString("portio_inspekulo"),
                        rs.getString("sondage"),rs.getString("portio_dalam"),rs.getString("bentuk"),
                        rs.getString("cavum_uteri"),rs.getString("mobilitas"),rs.getString("ukuran"),
                        rs.getString("nyeri_tekan"),rs.getString("adnexa_kanan"),rs.getString("adnexa_kiri"),
                        rs.getString("cavum_douglas")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);    
            } finally {
                if(rs!=null) {
                    rs.close();
                }
                if(ps5!=null) {
                    ps5.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi :" +e);
        }
        LCount.setText(""+tabModeGinekologi.getRowCount());
    }
    
    
    private void tampilTindakanDr() {
        try{     
            jml=0;
            for(i=0;i<TabModeTindakan.getRowCount();i++){
                if(TabModeTindakan.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml]; 
            kode=null;
            kode=new String[jml];
            nama=null;
            nama=new String[jml];
            kategori=null;
            kategori=new String[jml];
            totaltnd=null;
            totaltnd=new double[jml];  
            bagianrs=null;
            bagianrs=new double[jml];
            bhp=null;
            bhp=new double[jml];
            jmdokter=null;
            jmdokter=new double[jml];
            jmperawat=null;
            jmperawat=new double[jml];
            kso=null;
            kso=new double[jml];
            menejemen=null;
            menejemen=new double[jml];

            index=0;        
            for(i=0;i<TabModeTindakan.getRowCount();i++){
                if(TabModeTindakan.getValueAt(i,0).toString().equals("true")){
                    pilih[index]=true;
                    kode[index]=TabModeTindakan.getValueAt(i,1).toString();
                    nama[index]=TabModeTindakan.getValueAt(i,2).toString();
                    kategori[index]=TabModeTindakan.getValueAt(i,3).toString();
                    totaltnd[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,4).toString());
                    bagianrs[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,5).toString());
                    bhp[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,6).toString());
                    jmdokter[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,7).toString());
                    jmperawat[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,8).toString());  
                    kso[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,9).toString());
                    menejemen[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,10).toString());  
                    index++;
                }
            }       

            Valid.tabelKosong(TabModeTindakan);

            for(i=0;i<jml;i++){
                TabModeTindakan.addRow(new Object[] {
                    pilih[i],kode[i],nama[i],kategori[i],totaltnd[i],bagianrs[i],bhp[i],jmdokter[i],jmperawat[i],kso[i],menejemen[i]
                });
            }
            
            if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan "); 
            }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan ");        
            }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan ");     
            }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan "); 
            }
            
            try {
                if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,kode_poli.trim());
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(4,kd_pj.trim());
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(7,kd_pj.trim());
                    pstindakan.setString(8,kode_poli.trim());
                    pstindakan.setString(9,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kd_pj.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kd_pj.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,kode_poli.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kode_poli.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }
                
                while(rstindakan.next()){
                    TabModeTindakan.addRow(new Object[] {
                        false,rstindakan.getString(1),rstindakan.getString(2),rstindakan.getString(3),
                        rstindakan.getDouble("total_byrdr"),rstindakan.getDouble("material"),
                        rstindakan.getDouble("bhp"),rstindakan.getDouble("tarif_tindakandr"),
                        rstindakan.getDouble("tarif_tindakanpr"),rstindakan.getDouble("kso"),
                        rstindakan.getDouble("menejemen")
                    });    
                }                   
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rstindakan != null){
                    rstindakan.close();
                }
                if(pstindakan != null){
                    pstindakan.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+TabModeTindakan.getRowCount());
    }
    
    private void tampilTindakanPr() {
        try{     
            jml=0;
            for(i=0;i<TabModeTindakan2.getRowCount();i++){
                if(TabModeTindakan2.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml]; 
            kode=null;
            kode=new String[jml];
            nama=null;
            nama=new String[jml];
            kategori=null;
            kategori=new String[jml];
            totaltnd=null;
            totaltnd=new double[jml];  
            bagianrs=null;
            bagianrs=new double[jml];
            bhp=null;
            bhp=new double[jml];
            jmdokter=null;
            jmdokter=new double[jml];
            jmperawat=null;
            jmperawat=new double[jml];
            kso=null;
            kso=new double[jml];
            menejemen=null;
            menejemen=new double[jml];

            index=0;        
            for(i=0;i<TabModeTindakan2.getRowCount();i++){
                if(TabModeTindakan2.getValueAt(i,0).toString().equals("true")){
                    pilih[index]=true;
                    kode[index]=TabModeTindakan2.getValueAt(i,1).toString();
                    nama[index]=TabModeTindakan2.getValueAt(i,2).toString();
                    kategori[index]=TabModeTindakan2.getValueAt(i,3).toString();
                    totaltnd[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,4).toString());
                    bagianrs[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,5).toString());
                    bhp[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,6).toString());
                    jmdokter[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,7).toString());
                    jmperawat[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,8).toString());  
                    kso[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,9).toString());
                    menejemen[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,10).toString());  
                    index++;
                }
            }       

            Valid.tabelKosong(TabModeTindakan2);

            for(i=0;i<jml;i++){
                TabModeTindakan2.addRow(new Object[] {
                    pilih[i],kode[i],nama[i],kategori[i],totaltnd[i],bagianrs[i],bhp[i],jmdokter[i],jmperawat[i],kso[i],menejemen[i]
                });
            }
            
            if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan "); 
            }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan ");        
            }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan ");     
            }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan "); 
            }
            
            try {
                if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,kode_poli.trim());
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(4,kd_pj.trim());
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(7,kd_pj.trim());
                    pstindakan.setString(8,kode_poli.trim());
                    pstindakan.setString(9,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kd_pj.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kd_pj.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,kode_poli.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kode_poli.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }
                
                while(rstindakan.next()){
                    TabModeTindakan2.addRow(new Object[] {
                        false,rstindakan.getString(1),rstindakan.getString(2),rstindakan.getString(3),
                        rstindakan.getDouble("total_byrpr"),rstindakan.getDouble("material"),
                        rstindakan.getDouble("bhp"),rstindakan.getDouble("tarif_tindakandr"),
                        rstindakan.getDouble("tarif_tindakanpr"),rstindakan.getDouble("kso"),
                        rstindakan.getDouble("menejemen")
                    });        
                }                      
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rstindakan != null){
                    rstindakan.close();
                }
                if(pstindakan != null){
                    pstindakan.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+TabModeTindakan2.getRowCount());
    }
    
    private void tampilTindakanDrPr() {
        try{     
            jml=0;
            for(i=0;i<TabModeTindakan3.getRowCount();i++){
                if(TabModeTindakan3.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml]; 
            kode=null;
            kode=new String[jml];
            nama=null;
            nama=new String[jml];
            kategori=null;
            kategori=new String[jml];
            totaltnd=null;
            totaltnd=new double[jml];  
            bagianrs=null;
            bagianrs=new double[jml];
            bhp=null;
            bhp=new double[jml];
            jmdokter=null;
            jmdokter=new double[jml];
            jmperawat=null;
            jmperawat=new double[jml];
            kso=null;
            kso=new double[jml];
            menejemen=null;
            menejemen=new double[jml];

            index=0;        
            for(i=0;i<TabModeTindakan3.getRowCount();i++){
                if(TabModeTindakan3.getValueAt(i,0).toString().equals("true")){
                    pilih[index]=true;
                    kode[index]=TabModeTindakan3.getValueAt(i,1).toString();
                    nama[index]=TabModeTindakan3.getValueAt(i,2).toString();
                    kategori[index]=TabModeTindakan3.getValueAt(i,3).toString();
                    totaltnd[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,4).toString());
                    bagianrs[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,5).toString());
                    bhp[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,6).toString());
                    jmdokter[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,7).toString());
                    jmperawat[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,8).toString());  
                    kso[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,9).toString());
                    menejemen[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,10).toString());  
                    index++;
                }
            }       

            Valid.tabelKosong(TabModeTindakan3);

            for(i=0;i<jml;i++){
                TabModeTindakan3.addRow(new Object[] {
                    pilih[i],kode[i],nama[i],kategori[i],totaltnd[i],bagianrs[i],bhp[i],jmdokter[i],jmperawat[i],kso[i],menejemen[i]
                });
            }
            
            if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj='A09' or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli='KHS' or jns_perawatan.kd_poli='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj='A09' or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli='KHS' or jns_perawatan.kd_poli='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj='A09' or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli='KHS' or jns_perawatan.kd_poli='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan "); 
            }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan ");        
            }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan ");     
            }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan "); 
            }
            
            try {
                if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                //    pstindakan.setString(1,kd_pj.trim());
                //    pstindakan.setString(2,kode_poli.trim());
                    pstindakan.setString(1,"%"+TCari.getText().trim()+"%");
                //    pstindakan.setString(4,kd_pj.trim());
                //    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                //    pstindakan.setString(7,kd_pj.trim());
                //    pstindakan.setString(8,kode_poli.trim());
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kd_pj.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kd_pj.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,kode_poli.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kode_poli.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }
                
                while(rstindakan.next()){
                    TabModeTindakan3.addRow(new Object[] {
                        false,rstindakan.getString(1),rstindakan.getString(2),rstindakan.getString(3),
                        rstindakan.getDouble("total_byrdrpr"),rstindakan.getDouble("material"),
                        rstindakan.getDouble("bhp"),rstindakan.getDouble("tarif_tindakandr"),
                        rstindakan.getDouble("tarif_tindakanpr"),rstindakan.getDouble("kso"),
                        rstindakan.getDouble("menejemen")
                    });    
                }   
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rstindakan != null){
                    rstindakan.close();
                }
                if(pstindakan != null){
                    pstindakan.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+TabModeTindakan3.getRowCount());
    }
    
    private void TampilkanData(){
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                tampilkanPenangananDokterPetugas();
                break;
            case 1:
                tampilPemeriksaan();
                break;
            case 2:
                tampilkanPenangananDokterPetugas();
                break;
            default:
                break;
        }
    }
    
    private void SimpanPenangananDokterPetugas(){        
        try {
            ChkJln.setSelected(false);
            Sequel.AutoComitFalse();
            sukses=true;
            ttljmdokter=0;ttljmperawat=0;ttlkso=0;ttlpendapatan=0;ttljasasarana=0;ttlbhp=0;ttlmenejemen=0;
            for(i=0;i<tbTindakan3.getRowCount();i++){ 
                if(tbTindakan3.getValueAt(i,0).toString().equals("true")){  
                    if(Sequel.menyimpantf("rawat_jl_drpr","?,?,?,?,?,?,?,?,?,?,?,?,?,'Belum'","Tindakan",13,new String[]{
                        TNoRw.getText(),tbTindakan3.getValueAt(i,1).toString(),KdDok2.getText(),kdptg2.getText(),
                        Valid.SetTgl(DTPTgl.getSelectedItem()+""),cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                        tbTindakan3.getValueAt(i,5).toString(),tbTindakan3.getValueAt(i,6).toString(),tbTindakan3.getValueAt(i,7).toString(),
                        tbTindakan3.getValueAt(i,8).toString(),tbTindakan3.getValueAt(i,9).toString(),tbTindakan3.getValueAt(i,10).toString(),
                        tbTindakan3.getValueAt(i,4).toString()
                    })==true){
                        ttljmdokter=ttljmdokter+Double.parseDouble(tbTindakan3.getValueAt(i,7).toString());
                        ttljmperawat=ttljmperawat+Double.parseDouble(tbTindakan3.getValueAt(i,8).toString());
                        ttlkso=ttlkso+Double.parseDouble(tbTindakan3.getValueAt(i,9).toString());
                        ttlpendapatan=ttlpendapatan+Double.parseDouble(tbTindakan3.getValueAt(i,4).toString());
                        ttljasasarana=ttljasasarana+Double.parseDouble(tbTindakan3.getValueAt(i,5).toString());
                        ttlbhp=ttlbhp+Double.parseDouble(tbTindakan3.getValueAt(i,6).toString());
                        ttlmenejemen=ttlmenejemen+Double.parseDouble(tbTindakan3.getValueAt(i,10).toString());
                    }else{
                        sukses=false;
                    }
                }                           
            }
            if(sukses==true){
                Sequel.queryu("delete from tampjurnal");    
                Sequel.queryu("delete from tampjurnal_rsk");  
                if(ttlpendapatan>0){
                    Sequel.menyimpan("tampjurnal","'"+Suspen_Piutang_Tindakan_Ralan+"','Suspen Piutang Tindakan Ralan','"+ttlpendapatan+"','0'","debet=debet+'"+(ttlpendapatan)+"'","kd_rek='"+Suspen_Piutang_Tindakan_Ralan+"'");    
                    Sequel.menyimpan("tampjurnal","'"+Tindakan_Ralan+"','Pendapatan Tindakan Rawat Inap','0','"+ttlpendapatan+"'","kredit=kredit+'"+(ttlpendapatan)+"'","kd_rek='"+Tindakan_Ralan+"'");
                    Sequel.menyimpan("tampjurnal_rsk","'"+pendapatan_tindakan+"','Suspen Piutang Tindakan Ralan','"+ttlpendapatan+"','0'","debet=debet+'"+(ttlpendapatan)+"'","kd_rek='"+pendapatan_tindakan+"'");    
                    Sequel.menyimpan("tampjurnal_rsk","'"+pendapatan_tindakan+"','Pendapatan Tindakan Rawat Jalan','0','"+ttlpendapatan+"'","kredit=kredit+'"+(ttlpendapatan)+"'","kd_rek='"+pendapatan_tindakan+"'");
                }
                if(ttljmdokter>0){
                    Sequel.menyimpan("tampjurnal","'"+Beban_Jasa_Medik_Dokter_Tindakan_Ralan+"','Beban Jasa Medik Dokter Tindakan Ralan','"+ttljmdokter+"','0'","debet=debet+'"+(ttljmdokter)+"'","kd_rek='"+Beban_Jasa_Medik_Dokter_Tindakan_Ralan+"'");       
                    Sequel.menyimpan("tampjurnal","'"+Utang_Jasa_Medik_Dokter_Tindakan_Ralan+"','Utang Jasa Medik Dokter Tindakan Ralan','0','"+ttljmdokter+"'","kredit=kredit+'"+(ttljmdokter)+"'","kd_rek='"+Utang_Jasa_Medik_Dokter_Tindakan_Ralan+"'");
                    Sequel.menyimpan("tampjurnal_rsk","'"+beban_jasa_dokter+"','Beban Jasa Medik Dokter Tindakan Ralan','"+ttljmdokter+"','0'","debet=debet+'"+(ttljmdokter)+"'","kd_rek='"+beban_jasa_dokter+"'");       
                    Sequel.menyimpan("tampjurnal_rsk","'"+utang_jasa_dokter+"','Utang Jasa Medik Dokter Tindakan Ralan','0','"+ttljmdokter+"'","kredit=kredit+'"+(ttljmdokter)+"'","kd_rek='"+utang_jasa_dokter+"'");
                }
                if(ttljmperawat>0){
                    Sequel.menyimpan("tampjurnal","'"+Beban_Jasa_Medik_Paramedis_Tindakan_Ralan+"','Beban Jasa Medik Paramedis Tindakan Ralan','"+ttljmperawat+"','0'","debet=debet+'"+(ttljmperawat)+"'","kd_rek='"+Beban_Jasa_Medik_Paramedis_Tindakan_Ralan+"'");       
                    Sequel.menyimpan("tampjurnal","'"+Utang_Jasa_Medik_Paramedis_Tindakan_Ralan+"','Utang Jasa Medik Paramedis Tindakan Ralan','0','"+ttljmperawat+"'","kredit=kredit+'"+(ttljmperawat)+"'","kd_rek='"+Utang_Jasa_Medik_Paramedis_Tindakan_Ralan+"'");
                    Sequel.menyimpan("tampjurnal_rsk","'"+beban_jasa_paramedis+"','Beban Jasa Medik Paramedis Tindakan Ralan','"+ttljmperawat+"','0'","debet=debet+'"+(ttljmperawat)+"'","kd_rek='"+beban_jasa_paramedis+"'");       
                    Sequel.menyimpan("tampjurnal_rsk","'"+utang_jasa_paramedis+"','Utang Jasa Medik Paramedis Tindakan Ralan','0','"+ttljmperawat+"'","kredit=kredit+'"+(ttljmperawat)+"'","kd_rek='"+utang_jasa_paramedis+"'");
                }
                if(ttlkso>0){
                    Sequel.menyimpan("tampjurnal","'"+Beban_KSO_Tindakan_Ralan+"','Beban KSO Tindakan Ralan','"+ttlkso+"','0'","debet=debet+'"+(ttlkso)+"'","kd_rek='"+Beban_KSO_Tindakan_Ralan+"'");       
                    Sequel.menyimpan("tampjurnal","'"+Utang_KSO_Tindakan_Ralan+"','Utang KSO Tindakan Ralan','0','"+ttlkso+"'","kredit=kredit+'"+(ttlkso)+"'","kd_rek='"+Utang_KSO_Tindakan_Ralan+"'");
                    Sequel.menyimpan("tampjurnal_rsk","'"+beban_kso+"','Beban KSO Tindakan Ralan','"+ttlkso+"','0'","debet=debet+'"+(ttlkso)+"'","kd_rek='"+beban_kso+"'");       
                    Sequel.menyimpan("tampjurnal_rsk","'"+utang_kso+"','Utang KSO Tindakan Ralan','0','"+ttlkso+"'","kredit=kredit+'"+(ttlkso)+"'","kd_rek='"+utang_kso+"'");
                }
                if(ttljasasarana>0){
                    Sequel.menyimpan("tampjurnal","'"+Beban_Jasa_Sarana_Tindakan_Ralan+"','Beban Jasa Sarana Tindakan Ralan','"+ttljasasarana+"','0'","debet=debet+'"+(ttljasasarana)+"'","kd_rek='"+Beban_Jasa_Sarana_Tindakan_Ralan+"'");     
                    Sequel.menyimpan("tampjurnal","'"+Utang_Jasa_Sarana_Tindakan_Ralan+"','Utang Jasa Sarana Tindakan Ralan','0','"+ttljasasarana+"'","kredit=kredit+'"+(ttljasasarana)+"'","kd_rek='"+Utang_Jasa_Sarana_Tindakan_Ralan+"'");
                    Sequel.menyimpan("tampjurnal_rsk","'"+beban_jasa_sarana+"','Beban Jasa Sarana Tindakan Ralan','"+ttljasasarana+"','0'","debet=debet+'"+(ttljasasarana)+"'","kd_rek='"+beban_jasa_sarana+"'");     
                    Sequel.menyimpan("tampjurnal_rsk","'"+utang_jasa_sarana+"','Utang Jasa Sarana Tindakan Ralan','0','"+ttljasasarana+"'","kredit=kredit+'"+(ttljasasarana)+"'","kd_rek='"+utang_jasa_sarana+"'");
                }
                if(ttlbhp>0){
                    Sequel.menyimpan("tampjurnal","'"+HPP_BHP_Tindakan_Ralan+"','HPP BHP Tindakan Ralan','"+ttlbhp+"','0'","debet=debet+'"+(ttlbhp)+"'","kd_rek='"+HPP_BHP_Tindakan_Ralan+"'");      
                    Sequel.menyimpan("tampjurnal","'"+Persediaan_BHP_Tindakan_Ralan+"','Persediaan BHP Tindakan Ralan','0','"+ttlbhp+"'","kredit=kredit+'"+(ttlbhp)+"'","kd_rek='"+Persediaan_BHP_Tindakan_Ralan+"'");
                    Sequel.menyimpan("tampjurnal_rsk","'"+hpp_persediaan+"','HPP BHP Tindakan Ralan','"+ttlbhp+"','0'","debet=debet+'"+(ttlbhp)+"'","kd_rek='"+hpp_persediaan+"'");      
                    Sequel.menyimpan("tampjurnal_rsk","'"+persediaan_bhp+"','Persediaan BHP Tindakan Ralan','0','"+ttlbhp+"'","kredit=kredit+'"+(ttlbhp)+"'","kd_rek='"+persediaan_bhp+"'");
                }
                if(ttlmenejemen>0){
                    Sequel.menyimpan("tampjurnal","'"+Beban_Jasa_Menejemen_Tindakan_Ralan+"','Beban Jasa Menejemen Tindakan Ralan','"+ttlmenejemen+"','0'","debet=debet+'"+(ttlmenejemen)+"'","kd_rek='"+Beban_Jasa_Menejemen_Tindakan_Ralan+"'");       
                    Sequel.menyimpan("tampjurnal","'"+Utang_Jasa_Menejemen_Tindakan_Ralan+"','Utang Jasa Menejemen Tindakan Ralan','0','"+ttlmenejemen+"'","kredit=kredit+'"+(ttlmenejemen)+"'","kd_rek='"+Utang_Jasa_Menejemen_Tindakan_Ralan+"'");
                    Sequel.menyimpan("tampjurnal_rsk","'"+beban_menejemen+"','Beban Jasa Menejemen Tindakan Ralan','"+ttlmenejemen+"','0'","debet=debet+'"+(ttlmenejemen)+"'","kd_rek='"+beban_menejemen+"'");       
                    Sequel.menyimpan("tampjurnal_rsk","'"+utang_menejemen+"','Utang Jasa Menejemen Tindakan Ralan','0','"+ttlmenejemen+"'","kredit=kredit+'"+(ttlmenejemen)+"'","kd_rek='"+utang_menejemen+"'");
                }
                sukses=jur.simpanJurnal(TNoRw.getText(),"U","TINDAKAN RAWAT JALAN PASIEN "+TNoRM.getText()+" "+TPasien.getText()+", DIPOSTING OLEH "+akses.getkode());
                sukses=jurnal_rsk.simpanJurnal_rsk(TNoRw.getText(),"U","TINDAKAN RAWAT JALAN PASIEN "+TNoRM.getText()+" "+TPasien.getText()+", DIPOSTING OLEH "+akses.getkode());
            }
            
            if(sukses==true){
                Sequel.Commit();
                for(i=0;i<tbTindakan3.getRowCount();i++){ 
                    tbTindakan3.setValueAt(false,i,0);
                }
            }else{
                sukses=false;
                JOptionPane.showMessageDialog(null,"Terjadi kesalahan saat pemrosesan data, transaksi dibatalkan.\nPeriksa kembali data sebelum melanjutkan menyimpan..!!");
                Sequel.RollBack();
            }

            Sequel.AutoComitTrue();
            ChkJln.setSelected(true);
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }

    private void tampilkanPenangananDokterPetugas() {
        if(TabRawatTindakanDokterPetugas.getSelectedIndex()==0){
            tampilTindakanDrPr();
        }else if(TabRawatTindakanDokterPetugas.getSelectedIndex()==1){
            tampilDrPr();
        }
    }

    private void inputKamar() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        akses.setstatus(true);
        DlgKamarInap dlgki=new DlgKamarInap(null,false);
        dlgki.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
        dlgki.setLocationRelativeTo(internalFrame1);
        dlgki.emptTeks();
        dlgki.isCek();
        dlgki.setNoRm(TNoRw.getText(),TNoRM.getText(),TPasien.getText());  
        dlgki.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }
    
    public void emptTeks(){
        BtnBatalActionPerformed(null);
        TabRawat.setSelectedIndex(3);
    }
}
