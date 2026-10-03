

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
public final class DlgDetailTindakanAPS extends javax.swing.JDialog {
    private final Connection koneksi=koneksiDB.condb();
    private final sekuel Sequel=new sekuel();
    private DefaultTableModel tabModeRalanDokter,tabModeRalanParamedis,
            tabModeRalanDokterParamedis,tabModeRanapDokter,tabModeRanapParamedis,
            tabModeRanapDokterParamedis,tabModeRadiologi,tabModeLaborat,tabModeBDRS,
            tabModeDetailLaborat,tabModeOperasi;
    private validasi Valid=new validasi();
    private ResultSet rs;
    private PreparedStatement ps;
    private int i=0;
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
            biaya_dokter_umum=0;
    private DateFormat format=new SimpleDateFormat("yyyy-MM-dd");

    /** Creates new form DlgLhtBiaya
     * @param parent
     * @param modal */
    public DlgDetailTindakanAPS(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(885,674);
        
        tabModeRadiologi=new DefaultTableModel(null,new Object[]{
            "No.","No.Rawat","No.R.M.","Nama Pasien","Kd.Prk","Pemeriksaan","Kode P.J.",
            "Dokter P.J.Rad","NIP","Petugas Rad","Kode Perujuk","Dokter Perujuk","Tanggal","Jam","Cara Bayar","Ruangan",
            "Jasa Sarana","Paket BHP","JM P.J.Rad","JM Petugas","JM Perujuk","KSO","Menejemen","Total"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
             Class[] types = new Class[] {
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Double.class,java.lang.Double.class,
                 java.lang.Double.class,java.lang.Double.class,java.lang.Double.class,
                 java.lang.Double.class,java.lang.Double.class,java.lang.Double.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRadiologi.setModel(tabModeRadiologi);
        tbRadiologi.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRadiologi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 24; i++) {
            TableColumn column = tbRadiologi.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(30);
            }else if(i==1){
                column.setPreferredWidth(105);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(150);
            }else if(i==4){
                column.setPreferredWidth(75);
            }else if(i==5){
                column.setPreferredWidth(200);
            }else if(i==6){
                column.setPreferredWidth(75);
            }else if(i==7){
                column.setPreferredWidth(150);
            }else if(i==8){
                column.setPreferredWidth(75);
            }else if(i==9){
                column.setPreferredWidth(150);
            }else if(i==10){
                column.setPreferredWidth(75);
            }else if(i==11){
                column.setPreferredWidth(150);
            }else if(i==12){
                column.setPreferredWidth(70);
            }else if(i==13){
                column.setPreferredWidth(55);
            }else if(i==14){
                column.setPreferredWidth(110);
            }else if(i==15){
                column.setPreferredWidth(130);
            }else if(i==16){
                column.setPreferredWidth(75);
            }else if(i==17){
                column.setPreferredWidth(75);
            }else if(i==18){
                column.setPreferredWidth(75);
            }else if(i==19){
                column.setPreferredWidth(75);
            }else if(i==20){
                column.setPreferredWidth(75);
            }else if(i==21){
                column.setPreferredWidth(75);
            }else if(i==22){
                column.setPreferredWidth(75);
            }else if(i==23){
                column.setPreferredWidth(80);
            }
        }
        tbRadiologi.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeLaborat=new DefaultTableModel(null,new Object[]{
            "No.","No.Rawat","No.R.M.","Nama Pasien","Kd.Prk","Pemeriksaan","Kode P.J.",
            "Dokter P.J.Lab","NIP","Petugas Lab","Kode Perujuk","Dokter Perujuk","Tanggal","Jam","Cara Bayar","Ruangan",
            "Jasa Sarana","Paket BHP","JM P.J.Lab","JM Petugas","JM Perujuk","KSO","Menejemen","Total"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
             Class[] types = new Class[] {
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Double.class,java.lang.Double.class,
                 java.lang.Double.class,java.lang.Double.class,java.lang.Double.class,
                 java.lang.Double.class,java.lang.Double.class,java.lang.Double.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbLaborat.setModel(tabModeLaborat);
        tbLaborat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbLaborat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 24; i++) {
            TableColumn column = tbLaborat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(30);
            }else if(i==1){
                column.setPreferredWidth(105);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(150);
            }else if(i==4){
                column.setPreferredWidth(75);
            }else if(i==5){
                column.setPreferredWidth(200);
            }else if(i==6){
                column.setPreferredWidth(75);
            }else if(i==7){
                column.setPreferredWidth(150);
            }else if(i==8){
                column.setPreferredWidth(75);
            }else if(i==9){
                column.setPreferredWidth(150);
            }else if(i==10){
                column.setPreferredWidth(75);
            }else if(i==11){
                column.setPreferredWidth(150);
            }else if(i==12){
                column.setPreferredWidth(70);
            }else if(i==13){
                column.setPreferredWidth(55);
            }else if(i==14){
                column.setPreferredWidth(110);
            }else if(i==15){
                column.setPreferredWidth(130);
            }else if(i==16){
                column.setPreferredWidth(75);
            }else if(i==17){
                column.setPreferredWidth(75);
            }else if(i==18){
                column.setPreferredWidth(75);
            }else if(i==19){
                column.setPreferredWidth(75);
            }else if(i==20){
                column.setPreferredWidth(75);
            }else if(i==21){
                column.setPreferredWidth(75);
            }else if(i==22){
                column.setPreferredWidth(75);
            }else if(i==23){
                column.setPreferredWidth(80);
            }
        }
        tbLaborat.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeDetailLaborat=new DefaultTableModel(null,new Object[]{
            "No.","No.Rawat","No.R.M.","Nama Pasien","Id","Pemeriksaan","Kode P.J.",
            "Dokter P.J.Lab","NIP","Petugas Lab","Kode Perujuk","Dokter Perujuk","Tanggal","Jam","Cara Bayar","Ruangan",
            "Jasa Sarana","Paket BHP","JM P.J.Lab","JM Petugas","JM Perujuk","KSO","Menejemen","Total"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
             Class[] types = new Class[] {
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Double.class,java.lang.Double.class,
                 java.lang.Double.class,java.lang.Double.class,java.lang.Double.class,
                 java.lang.Double.class,java.lang.Double.class,java.lang.Double.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbDetailLaborat.setModel(tabModeDetailLaborat);
        tbDetailLaborat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbDetailLaborat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 24; i++) {
            TableColumn column = tbDetailLaborat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(30);
            }else if(i==1){
                column.setPreferredWidth(105);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(150);
            }else if(i==4){
                column.setPreferredWidth(75);
            }else if(i==5){
                column.setPreferredWidth(200);
            }else if(i==6){
                column.setPreferredWidth(75);
            }else if(i==7){
                column.setPreferredWidth(150);
            }else if(i==8){
                column.setPreferredWidth(75);
            }else if(i==9){
                column.setPreferredWidth(150);
            }else if(i==10){
                column.setPreferredWidth(75);
            }else if(i==11){
                column.setPreferredWidth(150);
            }else if(i==12){
                column.setPreferredWidth(70);
            }else if(i==13){
                column.setPreferredWidth(55);
            }else if(i==14){
                column.setPreferredWidth(110);
            }else if(i==15){
                column.setPreferredWidth(130);
            }else if(i==16){
                column.setPreferredWidth(75);
            }else if(i==17){
                column.setPreferredWidth(75);
            }else if(i==18){
                column.setPreferredWidth(75);
            }else if(i==19){
                column.setPreferredWidth(75);
            }else if(i==20){
                column.setPreferredWidth(75);
            }else if(i==21){
                column.setPreferredWidth(75);
            }else if(i==22){
                column.setPreferredWidth(75);
            }else if(i==23){
                column.setPreferredWidth(80);
            }
        }
        tbDetailLaborat.setDefaultRenderer(Object.class, new WarnaTable());
        
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){
                    switch (TabRawat.getSelectedIndex()) {
                        case 0:
                            KdDokterPerujukRad.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                            NmDokterPerujukRad.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                            BtnDokterPerujukRad.requestFocus();
                            break;
                        case 1:
                            KdDokterPerujukLab.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                            NmDokterPerujukLab.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                            BtnDokterPerujukLab.requestFocus();
                            break;
                        case 2:
                            KdDokterPerujukDetailLab.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                            NmDokterPerujukDetailLab.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                            BtnDokterPerujukDetailLab.requestFocus();
                            break;
                        default:
                            break;
                    }
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
        
        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(petugas.getTable().getSelectedRow()!= -1){
                    switch (TabRawat.getSelectedIndex()) {
                        case 1:
                            KdPetugasRad.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                            NmPetugasRad.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                            BtnPetugasRad.requestFocus();
                            break;
                        case 2:
                            KdPetugasLab.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                            NmPetugasLab.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                            BtnPetugasLab.requestFocus();
                            break;
                        case 3:
                            KdPetugasDetailLab.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                            NmPetugasDetailLab.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                            BtnPetugasDetailLab.requestFocus();
                            break;
                        default:
                            break;
                    }
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
               /* if(poli.getTable().getSelectedRow()!= -1){
                    switch (TabRawat.getSelectedIndex()) {
                        case 0:
                            KdPoliRalanDokter.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),0).toString());
                            NmPoliRalanDokter.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),1).toString());
                            BtnPoliRalanDokter.requestFocus();
                            break;
                        case 1:
                            KdPoliRalanParamedis.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),0).toString());
                            NmPoliRalanParamedis.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),1).toString());
                            BtnPoliRalanParamedis.requestFocus();
                            break;
                        case 2:
                            KdPoliRalanDokterParamedis.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),0).toString());
                            NmPoliRalanDokterParamedis.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),1).toString());
                            BtnPoliRalanDokterParamedis.requestFocus();
                            break;
                        default:
                            break;
                    }
                }*/      
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
                    switch (TabRawat.getSelectedIndex()) {
                        case 0:
                            KdCaraBayarRad.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(),1).toString());
                            NmCaraBayarRad.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(),2).toString());
                            BtnCaraBayarRad.requestFocus();
                            break;
                        case 1:
                            KdCaraBayarLab.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(),1).toString());
                            NmCaraBayarLab.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(),2).toString());
                            BtnCaraBayarLab.requestFocus();
                            break;
                        case 2:
                            KdCaraBayarDetailLab.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(),1).toString());
                            NmCaraBayarDetailLab.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(),2).toString());
                            BtnCaraBayarDetailLab.requestFocus();
                            break;
                        default:
                            break;
                    }
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
        
       
        ChkInput7.setSelected(false);
        isForm7();
        ChkInput8.setSelected(false);
        isForm8();
        ChkInput9.setSelected(false);
        isForm9();
        
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
        TabRawat = new javax.swing.JTabbedPane();
        internalFrame9 = new widget.InternalFrame();
        Scroll7 = new widget.ScrollPane();
        tbRadiologi = new widget.Table();
        PanelInput7 = new javax.swing.JPanel();
        ChkInput7 = new widget.CekBox();
        FormInput7 = new widget.panelisi();
        label39 = new widget.Label();
        KdPetugasRad = new widget.TextBox();
        NmPetugasRad = new widget.TextBox();
        BtnPetugasRad = new widget.Button();
        label40 = new widget.Label();
        KdCaraBayarRad = new widget.TextBox();
        NmCaraBayarRad = new widget.TextBox();
        BtnCaraBayarRad = new widget.Button();
        label41 = new widget.Label();
        KdDokterPerujukRad = new widget.TextBox();
        NmDokterPerujukRad = new widget.TextBox();
        BtnDokterPerujukRad = new widget.Button();
        internalFrame10 = new widget.InternalFrame();
        Scroll8 = new widget.ScrollPane();
        tbLaborat = new widget.Table();
        PanelInput8 = new javax.swing.JPanel();
        ChkInput8 = new widget.CekBox();
        FormInput8 = new widget.panelisi();
        label42 = new widget.Label();
        KdPetugasLab = new widget.TextBox();
        NmPetugasLab = new widget.TextBox();
        BtnPetugasLab = new widget.Button();
        label43 = new widget.Label();
        KdCaraBayarLab = new widget.TextBox();
        NmCaraBayarLab = new widget.TextBox();
        BtnCaraBayarLab = new widget.Button();
        label44 = new widget.Label();
        KdDokterPerujukLab = new widget.TextBox();
        NmDokterPerujukLab = new widget.TextBox();
        BtnDokterPerujukLab = new widget.Button();
        internalFrame11 = new widget.InternalFrame();
        Scroll9 = new widget.ScrollPane();
        tbDetailLaborat = new widget.Table();
        PanelInput9 = new javax.swing.JPanel();
        ChkInput9 = new widget.CekBox();
        FormInput9 = new widget.panelisi();
        label45 = new widget.Label();
        KdPetugasDetailLab = new widget.TextBox();
        NmPetugasDetailLab = new widget.TextBox();
        BtnPetugasDetailLab = new widget.Button();
        label46 = new widget.Label();
        KdCaraBayarDetailLab = new widget.TextBox();
        NmCaraBayarDetailLab = new widget.TextBox();
        BtnCaraBayarDetailLab = new widget.Button();
        label47 = new widget.Label();
        KdDokterPerujukDetailLab = new widget.TextBox();
        NmDokterPerujukDetailLab = new widget.TextBox();
        BtnDokterPerujukDetailLab = new widget.Button();
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

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Detail Tindakan ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        TabRawat.setBackground(new java.awt.Color(255, 255, 253));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        internalFrame9.setBackground(new java.awt.Color(235, 255, 235));
        internalFrame9.setBorder(null);
        internalFrame9.setName("internalFrame9"); // NOI18N
        internalFrame9.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll7.setName("Scroll7"); // NOI18N
        Scroll7.setOpaque(true);

        tbRadiologi.setName("tbRadiologi"); // NOI18N
        Scroll7.setViewportView(tbRadiologi);

        internalFrame9.add(Scroll7, java.awt.BorderLayout.CENTER);

        PanelInput7.setBackground(new java.awt.Color(255, 255, 255));
        PanelInput7.setName("PanelInput7"); // NOI18N
        PanelInput7.setOpaque(false);
        PanelInput7.setPreferredSize(new java.awt.Dimension(192, 65));
        PanelInput7.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput7.setMnemonic('M');
        ChkInput7.setText(".: Filter Data");
        ChkInput7.setBorderPainted(true);
        ChkInput7.setBorderPaintedFlat(true);
        ChkInput7.setFocusable(false);
        ChkInput7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput7.setName("ChkInput7"); // NOI18N
        ChkInput7.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput7.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput7.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput7.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput7ActionPerformed(evt);
            }
        });
        PanelInput7.add(ChkInput7, java.awt.BorderLayout.PAGE_END);

        FormInput7.setName("FormInput7"); // NOI18N
        FormInput7.setPreferredSize(new java.awt.Dimension(100, 74));
        FormInput7.setLayout(null);

        label39.setText("Petugas :");
        label39.setName("label39"); // NOI18N
        label39.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput7.add(label39);
        label39.setBounds(315, 10, 60, 23);

        KdPetugasRad.setEditable(false);
        KdPetugasRad.setName("KdPetugasRad"); // NOI18N
        KdPetugasRad.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput7.add(KdPetugasRad);
        KdPetugasRad.setBounds(380, 10, 65, 23);

        NmPetugasRad.setEditable(false);
        NmPetugasRad.setName("NmPetugasRad"); // NOI18N
        NmPetugasRad.setPreferredSize(new java.awt.Dimension(150, 23));
        FormInput7.add(NmPetugasRad);
        NmPetugasRad.setBounds(447, 10, 150, 23);

        BtnPetugasRad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPetugasRad.setMnemonic('3');
        BtnPetugasRad.setToolTipText("Alt+3");
        BtnPetugasRad.setName("BtnPetugasRad"); // NOI18N
        BtnPetugasRad.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPetugasRad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPetugasRadActionPerformed(evt);
            }
        });
        FormInput7.add(BtnPetugasRad);
        BtnPetugasRad.setBounds(600, 10, 28, 23);

        label40.setText("Cara Bayar :");
        label40.setName("label40"); // NOI18N
        label40.setPreferredSize(new java.awt.Dimension(75, 23));
        FormInput7.add(label40);
        label40.setBounds(640, 10, 70, 23);

        KdCaraBayarRad.setEditable(false);
        KdCaraBayarRad.setName("KdCaraBayarRad"); // NOI18N
        KdCaraBayarRad.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput7.add(KdCaraBayarRad);
        KdCaraBayarRad.setBounds(713, 10, 65, 23);

        NmCaraBayarRad.setEditable(false);
        NmCaraBayarRad.setName("NmCaraBayarRad"); // NOI18N
        NmCaraBayarRad.setPreferredSize(new java.awt.Dimension(150, 23));
        FormInput7.add(NmCaraBayarRad);
        NmCaraBayarRad.setBounds(780, 10, 150, 23);

        BtnCaraBayarRad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnCaraBayarRad.setMnemonic('3');
        BtnCaraBayarRad.setToolTipText("Alt+3");
        BtnCaraBayarRad.setName("BtnCaraBayarRad"); // NOI18N
        BtnCaraBayarRad.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCaraBayarRad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCaraBayarRadActionPerformed(evt);
            }
        });
        FormInput7.add(BtnCaraBayarRad);
        BtnCaraBayarRad.setBounds(933, 10, 28, 23);

        label41.setText("Perujuk :");
        label41.setName("label41"); // NOI18N
        label41.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput7.add(label41);
        label41.setBounds(0, 10, 60, 23);

        KdDokterPerujukRad.setEditable(false);
        KdDokterPerujukRad.setName("KdDokterPerujukRad"); // NOI18N
        KdDokterPerujukRad.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput7.add(KdDokterPerujukRad);
        KdDokterPerujukRad.setBounds(63, 10, 65, 23);

        NmDokterPerujukRad.setEditable(false);
        NmDokterPerujukRad.setName("NmDokterPerujukRad"); // NOI18N
        NmDokterPerujukRad.setPreferredSize(new java.awt.Dimension(150, 23));
        FormInput7.add(NmDokterPerujukRad);
        NmDokterPerujukRad.setBounds(130, 10, 150, 23);

        BtnDokterPerujukRad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokterPerujukRad.setMnemonic('3');
        BtnDokterPerujukRad.setToolTipText("Alt+3");
        BtnDokterPerujukRad.setName("BtnDokterPerujukRad"); // NOI18N
        BtnDokterPerujukRad.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDokterPerujukRad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDokterPerujukRadActionPerformed(evt);
            }
        });
        FormInput7.add(BtnDokterPerujukRad);
        BtnDokterPerujukRad.setBounds(283, 10, 28, 23);

        PanelInput7.add(FormInput7, java.awt.BorderLayout.CENTER);

        internalFrame9.add(PanelInput7, java.awt.BorderLayout.PAGE_START);

        TabRawat.addTab("Pemeriksaan Radiologi", internalFrame9);

        internalFrame10.setBackground(new java.awt.Color(235, 255, 235));
        internalFrame10.setBorder(null);
        internalFrame10.setName("internalFrame10"); // NOI18N
        internalFrame10.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll8.setName("Scroll8"); // NOI18N
        Scroll8.setOpaque(true);

        tbLaborat.setName("tbLaborat"); // NOI18N
        Scroll8.setViewportView(tbLaborat);

        internalFrame10.add(Scroll8, java.awt.BorderLayout.CENTER);

        PanelInput8.setBackground(new java.awt.Color(255, 255, 255));
        PanelInput8.setName("PanelInput8"); // NOI18N
        PanelInput8.setOpaque(false);
        PanelInput8.setPreferredSize(new java.awt.Dimension(192, 65));
        PanelInput8.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput8.setMnemonic('M');
        ChkInput8.setText(".: Filter Data");
        ChkInput8.setBorderPainted(true);
        ChkInput8.setBorderPaintedFlat(true);
        ChkInput8.setFocusable(false);
        ChkInput8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput8.setName("ChkInput8"); // NOI18N
        ChkInput8.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput8.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput8.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput8.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput8ActionPerformed(evt);
            }
        });
        PanelInput8.add(ChkInput8, java.awt.BorderLayout.PAGE_END);

        FormInput8.setName("FormInput8"); // NOI18N
        FormInput8.setPreferredSize(new java.awt.Dimension(100, 74));
        FormInput8.setLayout(null);

        label42.setText("Petugas :");
        label42.setName("label42"); // NOI18N
        label42.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput8.add(label42);
        label42.setBounds(315, 10, 60, 23);

        KdPetugasLab.setEditable(false);
        KdPetugasLab.setName("KdPetugasLab"); // NOI18N
        KdPetugasLab.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput8.add(KdPetugasLab);
        KdPetugasLab.setBounds(380, 10, 65, 23);

        NmPetugasLab.setEditable(false);
        NmPetugasLab.setName("NmPetugasLab"); // NOI18N
        NmPetugasLab.setPreferredSize(new java.awt.Dimension(150, 23));
        FormInput8.add(NmPetugasLab);
        NmPetugasLab.setBounds(447, 10, 150, 23);

        BtnPetugasLab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPetugasLab.setMnemonic('3');
        BtnPetugasLab.setToolTipText("Alt+3");
        BtnPetugasLab.setName("BtnPetugasLab"); // NOI18N
        BtnPetugasLab.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPetugasLab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPetugasLabActionPerformed(evt);
            }
        });
        FormInput8.add(BtnPetugasLab);
        BtnPetugasLab.setBounds(600, 10, 28, 23);

        label43.setText("Cara Bayar :");
        label43.setName("label43"); // NOI18N
        label43.setPreferredSize(new java.awt.Dimension(75, 23));
        FormInput8.add(label43);
        label43.setBounds(640, 10, 70, 23);

        KdCaraBayarLab.setEditable(false);
        KdCaraBayarLab.setName("KdCaraBayarLab"); // NOI18N
        KdCaraBayarLab.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput8.add(KdCaraBayarLab);
        KdCaraBayarLab.setBounds(713, 10, 65, 23);

        NmCaraBayarLab.setEditable(false);
        NmCaraBayarLab.setName("NmCaraBayarLab"); // NOI18N
        NmCaraBayarLab.setPreferredSize(new java.awt.Dimension(150, 23));
        FormInput8.add(NmCaraBayarLab);
        NmCaraBayarLab.setBounds(780, 10, 150, 23);

        BtnCaraBayarLab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnCaraBayarLab.setMnemonic('3');
        BtnCaraBayarLab.setToolTipText("Alt+3");
        BtnCaraBayarLab.setName("BtnCaraBayarLab"); // NOI18N
        BtnCaraBayarLab.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCaraBayarLab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCaraBayarLabActionPerformed(evt);
            }
        });
        FormInput8.add(BtnCaraBayarLab);
        BtnCaraBayarLab.setBounds(933, 10, 28, 23);

        label44.setText("Perujuk :");
        label44.setName("label44"); // NOI18N
        label44.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput8.add(label44);
        label44.setBounds(0, 10, 60, 23);

        KdDokterPerujukLab.setEditable(false);
        KdDokterPerujukLab.setName("KdDokterPerujukLab"); // NOI18N
        KdDokterPerujukLab.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput8.add(KdDokterPerujukLab);
        KdDokterPerujukLab.setBounds(63, 10, 65, 23);

        NmDokterPerujukLab.setEditable(false);
        NmDokterPerujukLab.setName("NmDokterPerujukLab"); // NOI18N
        NmDokterPerujukLab.setPreferredSize(new java.awt.Dimension(150, 23));
        FormInput8.add(NmDokterPerujukLab);
        NmDokterPerujukLab.setBounds(130, 10, 150, 23);

        BtnDokterPerujukLab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokterPerujukLab.setMnemonic('3');
        BtnDokterPerujukLab.setToolTipText("Alt+3");
        BtnDokterPerujukLab.setName("BtnDokterPerujukLab"); // NOI18N
        BtnDokterPerujukLab.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDokterPerujukLab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDokterPerujukLabActionPerformed(evt);
            }
        });
        FormInput8.add(BtnDokterPerujukLab);
        BtnDokterPerujukLab.setBounds(283, 10, 28, 23);

        PanelInput8.add(FormInput8, java.awt.BorderLayout.CENTER);

        internalFrame10.add(PanelInput8, java.awt.BorderLayout.PAGE_START);

        TabRawat.addTab("Pemeriksaan Laboratorium", internalFrame10);

        internalFrame11.setBackground(new java.awt.Color(235, 255, 235));
        internalFrame11.setBorder(null);
        internalFrame11.setName("internalFrame11"); // NOI18N
        internalFrame11.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll9.setName("Scroll9"); // NOI18N
        Scroll9.setOpaque(true);

        tbDetailLaborat.setName("tbDetailLaborat"); // NOI18N
        Scroll9.setViewportView(tbDetailLaborat);

        internalFrame11.add(Scroll9, java.awt.BorderLayout.CENTER);

        PanelInput9.setBackground(new java.awt.Color(255, 255, 255));
        PanelInput9.setName("PanelInput9"); // NOI18N
        PanelInput9.setOpaque(false);
        PanelInput9.setPreferredSize(new java.awt.Dimension(192, 65));
        PanelInput9.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput9.setMnemonic('M');
        ChkInput9.setText(".: Filter Data");
        ChkInput9.setBorderPainted(true);
        ChkInput9.setBorderPaintedFlat(true);
        ChkInput9.setFocusable(false);
        ChkInput9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput9.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput9.setName("ChkInput9"); // NOI18N
        ChkInput9.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput9.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput9.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput9.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput9ActionPerformed(evt);
            }
        });
        PanelInput9.add(ChkInput9, java.awt.BorderLayout.PAGE_END);

        FormInput9.setName("FormInput9"); // NOI18N
        FormInput9.setPreferredSize(new java.awt.Dimension(100, 74));
        FormInput9.setLayout(null);

        label45.setText("Petugas :");
        label45.setName("label45"); // NOI18N
        label45.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput9.add(label45);
        label45.setBounds(317, 10, 60, 23);

        KdPetugasDetailLab.setEditable(false);
        KdPetugasDetailLab.setName("KdPetugasDetailLab"); // NOI18N
        KdPetugasDetailLab.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput9.add(KdPetugasDetailLab);
        KdPetugasDetailLab.setBounds(380, 10, 65, 23);

        NmPetugasDetailLab.setEditable(false);
        NmPetugasDetailLab.setName("NmPetugasDetailLab"); // NOI18N
        NmPetugasDetailLab.setPreferredSize(new java.awt.Dimension(150, 23));
        FormInput9.add(NmPetugasDetailLab);
        NmPetugasDetailLab.setBounds(447, 10, 150, 23);

        BtnPetugasDetailLab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPetugasDetailLab.setMnemonic('3');
        BtnPetugasDetailLab.setToolTipText("Alt+3");
        BtnPetugasDetailLab.setName("BtnPetugasDetailLab"); // NOI18N
        BtnPetugasDetailLab.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPetugasDetailLab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPetugasDetailLabActionPerformed(evt);
            }
        });
        FormInput9.add(BtnPetugasDetailLab);
        BtnPetugasDetailLab.setBounds(600, 10, 28, 23);

        label46.setText("Cara Bayar :");
        label46.setName("label46"); // NOI18N
        label46.setPreferredSize(new java.awt.Dimension(75, 23));
        FormInput9.add(label46);
        label46.setBounds(640, 10, 70, 23);

        KdCaraBayarDetailLab.setEditable(false);
        KdCaraBayarDetailLab.setName("KdCaraBayarDetailLab"); // NOI18N
        KdCaraBayarDetailLab.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput9.add(KdCaraBayarDetailLab);
        KdCaraBayarDetailLab.setBounds(713, 10, 65, 23);

        NmCaraBayarDetailLab.setEditable(false);
        NmCaraBayarDetailLab.setName("NmCaraBayarDetailLab"); // NOI18N
        NmCaraBayarDetailLab.setPreferredSize(new java.awt.Dimension(150, 23));
        FormInput9.add(NmCaraBayarDetailLab);
        NmCaraBayarDetailLab.setBounds(780, 10, 150, 23);

        BtnCaraBayarDetailLab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnCaraBayarDetailLab.setMnemonic('3');
        BtnCaraBayarDetailLab.setToolTipText("Alt+3");
        BtnCaraBayarDetailLab.setName("BtnCaraBayarDetailLab"); // NOI18N
        BtnCaraBayarDetailLab.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCaraBayarDetailLab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCaraBayarDetailLabActionPerformed(evt);
            }
        });
        FormInput9.add(BtnCaraBayarDetailLab);
        BtnCaraBayarDetailLab.setBounds(933, 10, 28, 23);

        label47.setText("Perujuk :");
        label47.setName("label47"); // NOI18N
        label47.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput9.add(label47);
        label47.setBounds(0, 10, 60, 23);

        KdDokterPerujukDetailLab.setEditable(false);
        KdDokterPerujukDetailLab.setName("KdDokterPerujukDetailLab"); // NOI18N
        KdDokterPerujukDetailLab.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput9.add(KdDokterPerujukDetailLab);
        KdDokterPerujukDetailLab.setBounds(63, 10, 65, 23);

        NmDokterPerujukDetailLab.setEditable(false);
        NmDokterPerujukDetailLab.setName("NmDokterPerujukDetailLab"); // NOI18N
        NmDokterPerujukDetailLab.setPreferredSize(new java.awt.Dimension(150, 23));
        FormInput9.add(NmDokterPerujukDetailLab);
        NmDokterPerujukDetailLab.setBounds(130, 10, 150, 23);

        BtnDokterPerujukDetailLab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokterPerujukDetailLab.setMnemonic('3');
        BtnDokterPerujukDetailLab.setToolTipText("Alt+3");
        BtnDokterPerujukDetailLab.setName("BtnDokterPerujukDetailLab"); // NOI18N
        BtnDokterPerujukDetailLab.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDokterPerujukDetailLab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDokterPerujukDetailLabActionPerformed(evt);
            }
        });
        FormInput9.add(BtnDokterPerujukDetailLab);
        BtnDokterPerujukDetailLab.setBounds(283, 10, 28, 23);

        PanelInput9.add(FormInput9, java.awt.BorderLayout.CENTER);

        internalFrame11.add(PanelInput9, java.awt.BorderLayout.PAGE_START);

        TabRawat.addTab("Detail Pemeriksaan Laboratorium", internalFrame11);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

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
        BtnCari.setToolTipText("Alt+1");
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
        panelGlass5.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('2');
        BtnAll.setToolTipText("2Alt+2");
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
        panelGlass5.add(BtnAll);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnPrint);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(28, 23));
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
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                CetakRadiologi();
                break;
            case 1:
                CetakLab();
                break;
            case 2:
                CetakDetailLab();
                break;
            default:
                    break;
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void Tgl1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl1KeyPressed
        Valid.pindah(evt, BtnKeluar, Tgl2);
    }//GEN-LAST:event_Tgl1KeyPressed

    private void Tgl2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl2KeyPressed
        
    }//GEN-LAST:event_Tgl2KeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            TabRawatMouseClicked(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        TabRawatMouseClicked(null);
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
        switch (TabRawat.getSelectedIndex()) {
           case 0:
                KdPetugasRad.setText("");
                NmPetugasRad.setText("");
                KdDokterPerujukRad.setText("");
                NmDokterPerujukRad.setText("");
                KdCaraBayarRad.setText("");
                NmCaraBayarRad.setText("");
                break;
            case 1:
                KdPetugasLab.setText("");
                NmPetugasLab.setText("");
                KdDokterPerujukLab.setText("");
                NmDokterPerujukLab.setText("");
                KdCaraBayarLab.setText("");
                NmCaraBayarLab.setText("");
                break;
            case 2:
                KdPetugasDetailLab.setText("");
                NmPetugasDetailLab.setText("");
                KdDokterPerujukDetailLab.setText("");
                NmDokterPerujukDetailLab.setText("");
                KdCaraBayarDetailLab.setText("");
                NmCaraBayarDetailLab.setText("");
                break;
            default:
                break;
        }
        TabRawatMouseClicked(null);
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCari, TCari);
        }
    }//GEN-LAST:event_BtnAllKeyPressed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        switch (TabRawat.getSelectedIndex()) {
            case 0:
            tampil8();
            break;
            case 1:
            tampil9();
            break;
            case 2:
            tampil10();
            break;
            default:
            break;
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void BtnDokterPerujukDetailLabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterPerujukDetailLabActionPerformed
      //  BtnDokterRalanDokterActionPerformed(null);
    }//GEN-LAST:event_BtnDokterPerujukDetailLabActionPerformed

    private void BtnCaraBayarDetailLabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCaraBayarDetailLabActionPerformed
      //  BtnCaraBayarRalanDokterActionPerformed(null);
    }//GEN-LAST:event_BtnCaraBayarDetailLabActionPerformed

    private void BtnPetugasDetailLabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPetugasDetailLabActionPerformed
       // BtnPetugasRalanParamedisActionPerformed(null);
    }//GEN-LAST:event_BtnPetugasDetailLabActionPerformed

    private void ChkInput9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput9ActionPerformed
        isForm9();
    }//GEN-LAST:event_ChkInput9ActionPerformed

    private void BtnDokterPerujukLabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterPerujukLabActionPerformed
      //  BtnDokterRalanDokterActionPerformed(null);
    }//GEN-LAST:event_BtnDokterPerujukLabActionPerformed

    private void BtnCaraBayarLabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCaraBayarLabActionPerformed
       // BtnCaraBayarRalanDokterActionPerformed(null);
    }//GEN-LAST:event_BtnCaraBayarLabActionPerformed

    private void BtnPetugasLabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPetugasLabActionPerformed
       // BtnPetugasRalanParamedisActionPerformed(null);
    }//GEN-LAST:event_BtnPetugasLabActionPerformed

    private void ChkInput8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput8ActionPerformed
        isForm8();
    }//GEN-LAST:event_ChkInput8ActionPerformed

    private void BtnDokterPerujukRadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterPerujukRadActionPerformed
      //  BtnDokterRalanDokterActionPerformed(null);
    }//GEN-LAST:event_BtnDokterPerujukRadActionPerformed

    private void BtnCaraBayarRadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCaraBayarRadActionPerformed
      //  BtnCaraBayarRalanDokterActionPerformed(null);
    }//GEN-LAST:event_BtnCaraBayarRadActionPerformed

    private void BtnPetugasRadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPetugasRadActionPerformed

    }//GEN-LAST:event_BtnPetugasRadActionPerformed

    private void ChkInput7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput7ActionPerformed
        isForm7();
    }//GEN-LAST:event_ChkInput7ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgDetailTindakanAPS dialog = new DlgDetailTindakanAPS(new javax.swing.JFrame(), true);
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
    private widget.Button BtnCaraBayarDetailLab;
    private widget.Button BtnCaraBayarLab;
    private widget.Button BtnCaraBayarRad;
    private widget.Button BtnCari;
    private widget.Button BtnDokterPerujukDetailLab;
    private widget.Button BtnDokterPerujukLab;
    private widget.Button BtnDokterPerujukRad;
    private widget.Button BtnKeluar;
    private widget.Button BtnPetugasDetailLab;
    private widget.Button BtnPetugasLab;
    private widget.Button BtnPetugasRad;
    private widget.Button BtnPrint;
    private widget.CekBox ChkInput7;
    private widget.CekBox ChkInput8;
    private widget.CekBox ChkInput9;
    private widget.panelisi FormInput7;
    private widget.panelisi FormInput8;
    private widget.panelisi FormInput9;
    private widget.TextBox KdCaraBayarDetailLab;
    private widget.TextBox KdCaraBayarLab;
    private widget.TextBox KdCaraBayarRad;
    private widget.TextBox KdDokterPerujukDetailLab;
    private widget.TextBox KdDokterPerujukLab;
    private widget.TextBox KdDokterPerujukRad;
    private widget.TextBox KdPetugasDetailLab;
    private widget.TextBox KdPetugasLab;
    private widget.TextBox KdPetugasRad;
    private widget.TextBox NmCaraBayarDetailLab;
    private widget.TextBox NmCaraBayarLab;
    private widget.TextBox NmCaraBayarRad;
    private widget.TextBox NmDokterPerujukDetailLab;
    private widget.TextBox NmDokterPerujukLab;
    private widget.TextBox NmDokterPerujukRad;
    private widget.TextBox NmPetugasDetailLab;
    private widget.TextBox NmPetugasLab;
    private widget.TextBox NmPetugasRad;
    private javax.swing.JPanel PanelInput7;
    private javax.swing.JPanel PanelInput8;
    private javax.swing.JPanel PanelInput9;
    private widget.ScrollPane Scroll7;
    private widget.ScrollPane Scroll8;
    private widget.ScrollPane Scroll9;
    private widget.TextBox TCari;
    private javax.swing.JTabbedPane TabRawat;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private widget.ComboBox cmbStatus;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame10;
    private widget.InternalFrame internalFrame11;
    private widget.InternalFrame internalFrame9;
    private widget.Label jLabel12;
    private widget.Label label10;
    private widget.Label label18;
    private widget.Label label39;
    private widget.Label label40;
    private widget.Label label41;
    private widget.Label label42;
    private widget.Label label43;
    private widget.Label label44;
    private widget.Label label45;
    private widget.Label label46;
    private widget.Label label47;
    private widget.Label label9;
    private widget.panelisi panelGlass5;
    private widget.Table tbDetailLaborat;
    private widget.Table tbLaborat;
    private widget.Table tbRadiologi;
    // End of variables declaration//GEN-END:variables

    
    public void tampil8(){     
        Valid.tabelKosong(tabModeRadiologi);
        try{
            if(KdDokterPerujukRad.getText().equals("")&&NmDokterPerujukRad.getText().equals("")&&KdPetugasRad.getText().equals("")&&NmPetugasRad.getText().equals("")&&KdCaraBayarRad.getText().equals("")&&NmCaraBayarRad.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                ps=koneksi.prepareStatement(
                       "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                       "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                       "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                       "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                       "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                       "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                       "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                       "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                       "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                       "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                       "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan "+
                       "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                       "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                       "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                       "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                       "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                       "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                       "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                       "where concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? order by periksa_radiologi.tgl_periksa");
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    ps=koneksi.prepareStatement(
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "where concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.no_rawat like ? or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between ? and ? and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_radiologi.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                if(KdDokterPerujukRad.getText().equals("")&&NmDokterPerujukRad.getText().equals("")&&KdPetugasRad.getText().equals("")&&NmPetugasRad.getText().equals("")&&KdCaraBayarRad.getText().equals("")&&NmCaraBayarRad.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                }else{
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(3,"%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%");
                    ps.setString(4,"%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%");
                    ps.setString(5,"%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(8,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(9,"%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%");
                    ps.setString(10,"%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%");
                    ps.setString(11,"%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%");
                    ps.setString(12,"%"+TCari.getText().trim()+"%");
                    ps.setString(13,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(14,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(15,"%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%");
                    ps.setString(16,"%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%");
                    ps.setString(17,"%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%");
                    ps.setString(18,"%"+TCari.getText().trim()+"%");
                    ps.setString(19,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(20,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(21,"%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%");
                    ps.setString(22,"%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%");
                    ps.setString(23,"%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%");
                    ps.setString(24,"%"+TCari.getText().trim()+"%");
                    ps.setString(25,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(26,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(27,"%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%");
                    ps.setString(28,"%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%");
                    ps.setString(29,"%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%");
                    ps.setString(30,"%"+TCari.getText().trim()+"%");
                    ps.setString(31,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(32,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(33,"%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%");
                    ps.setString(34,"%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%");
                    ps.setString(35,"%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%");
                    ps.setString(36,"%"+TCari.getText().trim()+"%");
                    ps.setString(37,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(38,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(39,"%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%");
                    ps.setString(40,"%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%");
                    ps.setString(41,"%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%");
                    ps.setString(42,"%"+TCari.getText().trim()+"%");
                    ps.setString(43,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(44,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(45,"%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%");
                    ps.setString(46,"%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%");
                    ps.setString(47,"%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%");
                    ps.setString(48,"%"+TCari.getText().trim()+"%");
                    ps.setString(49,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(50,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(51,"%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%");
                    ps.setString(52,"%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%");
                    ps.setString(53,"%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%");
                    ps.setString(54,"%"+TCari.getText().trim()+"%");
                    ps.setString(55,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(56,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(57,"%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%");
                    ps.setString(58,"%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%");
                    ps.setString(59,"%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%");
                    ps.setString(60,"%"+TCari.getText().trim()+"%");
                    ps.setString(61,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(62,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(63,"%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%");
                    ps.setString(64,"%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%");
                    ps.setString(65,"%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%");
                    ps.setString(66,"%"+TCari.getText().trim()+"%");
                    ps.setString(67,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(68,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(69,"%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%");
                    ps.setString(70,"%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%");
                    ps.setString(71,"%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%");
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
                    tabModeRadiologi.addRow(new Object[]{
                        i,rs.getString("no_rawat"),rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),rs.getString("kd_jenis_prw"),
                        rs.getString("nm_perawatan"),rs.getString("kd_dokter"),
                        rs.getString("nm_dokter"),rs.getString("nip"),rs.getString("nama"),
                        rs.getString("dokter_perujuk"),rs.getString("perujuk"),
                        rs.getString("tgl_periksa"),rs.getString("jam"),
                        rs.getString("png_jawab"),rs.getString("ruangan"),rs.getDouble("bagian_rs"),
                        rs.getDouble("bhp"),rs.getDouble("tarif_tindakan_dokter"),
                        rs.getDouble("tarif_tindakan_petugas"),rs.getDouble("tarif_perujuk"),
                        rs.getDouble("kso"),rs.getDouble("menejemen"),rs.getDouble("biaya")
                    });
                    i++;
                }
                if(total>0){
                    tabModeRadiologi.addRow(new Object[]{
                        "","","","","","","","","","","","","","","","Jumlah Total :",
                        material,bhp,jmdokter,jmpetugas,jmperujuk,kso,menejemen,total
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
    
    public void tampil9(){     
        Valid.tabelKosong(tabModeLaborat);
        try{
            if(KdDokterPerujukLab.getText().equals("")&&NmDokterPerujukLab.getText().equals("")&&KdPetugasLab.getText().equals("")&&NmPetugasLab.getText().equals("")&&KdCaraBayarLab.getText().equals("")&&NmCaraBayarLab.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                ps=koneksi.prepareStatement(
                       "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                       "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                       "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                       "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                       "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                       "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                       "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                       "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                       "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                       "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                       "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan "+
                       "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                       "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                       "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                       "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                       "inner join petugas on periksa_lab.nip=petugas.nip "+
                       "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                       "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                       "where concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? order by periksa_lab.tgl_periksa");
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    ps=koneksi.prepareStatement(
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "where concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.no_rawat like ? or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.no_rawat like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                if(KdDokterPerujukLab.getText().equals("")&&NmDokterPerujukLab.getText().equals("")&&KdPetugasLab.getText().equals("")&&NmPetugasLab.getText().equals("")&&KdCaraBayarLab.getText().equals("")&&NmCaraBayarLab.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                }else{
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(3,"%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%");
                    ps.setString(4,"%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%");
                    ps.setString(5,"%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(8,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(9,"%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%");
                    ps.setString(10,"%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%");
                    ps.setString(11,"%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%");
                    ps.setString(12,"%"+TCari.getText().trim()+"%");
                    ps.setString(13,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(14,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(15,"%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%");
                    ps.setString(16,"%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%");
                    ps.setString(17,"%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%");
                    ps.setString(18,"%"+TCari.getText().trim()+"%");
                    ps.setString(19,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(20,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(21,"%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%");
                    ps.setString(22,"%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%");
                    ps.setString(23,"%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%");
                    ps.setString(24,"%"+TCari.getText().trim()+"%");
                    ps.setString(25,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(26,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(27,"%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%");
                    ps.setString(28,"%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%");
                    ps.setString(29,"%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%");
                    ps.setString(30,"%"+TCari.getText().trim()+"%");
                    ps.setString(31,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(32,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(33,"%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%");
                    ps.setString(34,"%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%");
                    ps.setString(35,"%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%");
                    ps.setString(36,"%"+TCari.getText().trim()+"%");
                    ps.setString(37,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(38,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(39,"%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%");
                    ps.setString(40,"%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%");
                    ps.setString(41,"%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%");
                    ps.setString(42,"%"+TCari.getText().trim()+"%");
                    ps.setString(43,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(44,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(45,"%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%");
                    ps.setString(46,"%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%");
                    ps.setString(47,"%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%");
                    ps.setString(48,"%"+TCari.getText().trim()+"%");
                    ps.setString(49,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(50,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(51,"%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%");
                    ps.setString(52,"%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%");
                    ps.setString(53,"%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%");
                    ps.setString(54,"%"+TCari.getText().trim()+"%");
                    ps.setString(55,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(56,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(57,"%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%");
                    ps.setString(58,"%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%");
                    ps.setString(59,"%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%");
                    ps.setString(60,"%"+TCari.getText().trim()+"%");
                    ps.setString(61,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(62,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(63,"%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%");
                    ps.setString(64,"%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%");
                    ps.setString(65,"%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%");
                    ps.setString(66,"%"+TCari.getText().trim()+"%");
                    ps.setString(67,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(68,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(69,"%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%");
                    ps.setString(70,"%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%");
                    ps.setString(71,"%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%");
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
                    tabModeLaborat.addRow(new Object[]{
                        i,rs.getString("no_rawat"),rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),rs.getString("kd_jenis_prw"),
                        rs.getString("nm_perawatan"),rs.getString("kd_dokter"),
                        rs.getString("nm_dokter"),rs.getString("nip"),rs.getString("nama"),
                        rs.getString("dokter_perujuk"),rs.getString("perujuk"),
                        rs.getString("tgl_periksa"),rs.getString("jam"),
                        rs.getString("png_jawab"),rs.getString("ruangan"),rs.getDouble("bagian_rs"),
                        rs.getDouble("bhp"),rs.getDouble("tarif_tindakan_dokter"),
                        rs.getDouble("tarif_tindakan_petugas"),rs.getDouble("tarif_perujuk"),
                        rs.getDouble("kso"),rs.getDouble("menejemen"),rs.getDouble("biaya")
                    });
                    i++;
                }
                if(total>0){
                    tabModeLaborat.addRow(new Object[]{
                        "","","","","","","","","","","","","","","","Jumlah Total :",
                        material,bhp,jmdokter,jmpetugas,jmperujuk,kso,menejemen,total
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
    
    public void tampil10(){     
        Valid.tabelKosong(tabModeDetailLaborat);
        try {
            if(KdDokterPerujukDetailLab.getText().equals("")&&NmDokterPerujukDetailLab.getText().equals("")&&KdPetugasDetailLab.getText().equals("")&&NmPetugasDetailLab.getText().equals("")&&KdCaraBayarDetailLab.getText().equals("")&&NmCaraBayarDetailLab.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                ps=koneksi.prepareStatement(
                        "select detail_periksa_lab.no_rawat,reg_periksa.no_rkm_medis,"+
                        "pasien_aps.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                        "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                        "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                        "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                        "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                        "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                        "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                        "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                        "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                        "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                        "detail_periksa_lab.biaya_item from detail_periksa_lab "+
                        "inner join reg_periksa inner join pasien_aps inner join template_laboratorium "+
                        "inner join jns_perawatan_lab inner join periksa_lab "+
                        "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                        "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                        "and reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
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
                            "pasien_aps.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien_aps inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw "+
                            "where concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.no_rawat like ? or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                            "pasien_aps.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien_aps inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab inner join piutang_pasien "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                            "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.no_rawat like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                            "pasien_aps.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien_aps inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab inner join piutang_pasien "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                            "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.no_rawat like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                            "pasien_aps.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien_aps inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw "+
                            "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.no_rawat like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                            "pasien_aps.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien_aps inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw "+
                            "where reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and detail_periksa_lab.no_rawat like ? or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and reg_periksa.no_rkm_medis like ? or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between ? and ? and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like ? and concat(periksa_lab.nip,petugas.nama) like ? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and pasien_aps.nm_pasien like ? or "+
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
                if(KdDokterPerujukDetailLab.getText().equals("")&&NmDokterPerujukDetailLab.getText().equals("")&&KdPetugasDetailLab.getText().equals("")&&NmPetugasDetailLab.getText().equals("")&&KdCaraBayarDetailLab.getText().equals("")&&NmCaraBayarDetailLab.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                }else{
                    ps.setString(1,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(2,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(3,"%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%");
                    ps.setString(4,"%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%");
                    ps.setString(5,"%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(8,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(9,"%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%");
                    ps.setString(10,"%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%");
                    ps.setString(11,"%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%");
                    ps.setString(12,"%"+TCari.getText().trim()+"%");
                    ps.setString(13,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(14,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(15,"%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%");
                    ps.setString(16,"%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%");
                    ps.setString(17,"%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%");
                    ps.setString(18,"%"+TCari.getText().trim()+"%");
                    ps.setString(19,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(20,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(21,"%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%");
                    ps.setString(22,"%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%");
                    ps.setString(23,"%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%");
                    ps.setString(24,"%"+TCari.getText().trim()+"%");
                    ps.setString(25,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(26,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(27,"%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%");
                    ps.setString(28,"%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%");
                    ps.setString(29,"%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%");
                    ps.setString(30,"%"+TCari.getText().trim()+"%");
                    ps.setString(31,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(32,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(33,"%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%");
                    ps.setString(34,"%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%");
                    ps.setString(35,"%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%");
                    ps.setString(36,"%"+TCari.getText().trim()+"%");
                    ps.setString(37,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(38,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(39,"%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%");
                    ps.setString(40,"%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%");
                    ps.setString(41,"%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%");
                    ps.setString(42,"%"+TCari.getText().trim()+"%");
                    ps.setString(43,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(44,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(45,"%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%");
                    ps.setString(46,"%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%");
                    ps.setString(47,"%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%");
                    ps.setString(48,"%"+TCari.getText().trim()+"%");
                    ps.setString(49,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(50,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(51,"%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%");
                    ps.setString(52,"%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%");
                    ps.setString(53,"%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%");
                    ps.setString(54,"%"+TCari.getText().trim()+"%");
                    ps.setString(55,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(56,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(57,"%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%");
                    ps.setString(58,"%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%");
                    ps.setString(59,"%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%");
                    ps.setString(60,"%"+TCari.getText().trim()+"%");
                    ps.setString(61,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(62,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(63,"%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%");
                    ps.setString(64,"%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%");
                    ps.setString(65,"%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%");
                    ps.setString(66,"%"+TCari.getText().trim()+"%");
                    ps.setString(67,Valid.SetTglJam(Tgl1.getSelectedItem()+""));
                    ps.setString(68,Valid.SetTglJam(Tgl2.getSelectedItem()+""));
                    ps.setString(69,"%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%");
                    ps.setString(70,"%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%");
                    ps.setString(71,"%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%");
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
                    tabModeDetailLaborat.addRow(new Object[]{
                        i,rs.getString("no_rawat"),rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),rs.getString("id_template"),
                        rs.getString("Pemeriksaan"),rs.getString("kd_dokter"),
                        rs.getString("nm_dokter"),rs.getString("nip"),rs.getString("nama"),
                        rs.getString("dokter_perujuk"),rs.getString("perujuk"),
                        rs.getString("tgl_periksa"),rs.getString("jam"),
                        rs.getString("png_jawab"),rs.getString("ruangan"),rs.getDouble("bagian_rs"),
                        rs.getDouble("bhp"),rs.getDouble("bagian_dokter"),
                        rs.getDouble("bagian_laborat"),rs.getDouble("bagian_perujuk"),
                        rs.getDouble("kso"),rs.getDouble("menejemen"),rs.getDouble("biaya_item")
                    });
                    i++;
                }
                if(tabModeDetailLaborat.getRowCount()>0){
                    tabModeDetailLaborat.addRow(new Object[]{
                        "","","","","","","","","","","","","","","","Jumlah Total :",
                        material,bhp,jmdokter,jmpetugas,jmperujuk,kso,menejemen,total
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
 
 
    
    private void isForm7(){
        if(ChkInput7.isSelected()==true){
            ChkInput7.setVisible(false);
            PanelInput7.setPreferredSize(new Dimension(WIDTH,65));
            FormInput7.setVisible(true);      
            ChkInput7.setVisible(true);
        }else if(ChkInput7.isSelected()==false){           
            ChkInput7.setVisible(false);            
            PanelInput7.setPreferredSize(new Dimension(WIDTH,20));
            FormInput7.setVisible(false);      
            ChkInput7.setVisible(true);
        }
    }
    
    private void isForm8(){
        if(ChkInput8.isSelected()==true){
            ChkInput8.setVisible(false);
            PanelInput8.setPreferredSize(new Dimension(WIDTH,65));
            FormInput8.setVisible(true);      
            ChkInput8.setVisible(true);
        }else if(ChkInput8.isSelected()==false){           
            ChkInput8.setVisible(false);            
            PanelInput8.setPreferredSize(new Dimension(WIDTH,20));
            FormInput8.setVisible(false);      
            ChkInput8.setVisible(true);
        }
    }
    
    private void isForm9(){
        if(ChkInput9.isSelected()==true){
            ChkInput9.setVisible(false);
            PanelInput9.setPreferredSize(new Dimension(WIDTH,65));
            FormInput9.setVisible(true);      
            ChkInput9.setVisible(true);
        }else if(ChkInput9.isSelected()==false){           
            ChkInput9.setVisible(false);            
            PanelInput9.setPreferredSize(new Dimension(WIDTH,20));
            FormInput9.setVisible(false);      
            ChkInput9.setVisible(true);
        }
    }
    
  
    private void CetakRadiologi() {
        if(tabModeRadiologi.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            TCari.requestFocus();
        }else if(tabModeRadiologi.getRowCount()!=0){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());
            param.put("tanggal1",Valid.SetTglJam(Tgl1.getSelectedItem()+""));
            param.put("tanggal2",Valid.SetTglJam(Tgl2.getSelectedItem()+""));
            param.put("cari","%"+TCari.getText().trim()+"%");                    
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            if(KdDokterPerujukRad.getText().equals("")&&NmDokterPerujukRad.getText().equals("")&&KdPetugasRad.getText().equals("")&&NmPetugasRad.getText().equals("")&&KdCaraBayarRad.getText().equals("")&&NmCaraBayarRad.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                Valid.MyReportqry("rptDetailRadiologi.jasper","report","::[ Detail Pemeriksaan Radiologi ]::",
                       "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                       "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                       "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                       "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                       "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                       "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                       "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                       "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                       "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                       "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                       "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan "+
                       "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                       "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                       "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                       "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                       "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                       "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                       "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                       "where concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' order by periksa_radiologi.tgl_periksa",param);
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    Valid.MyReportqry("rptDetailRadiologi.jasper","report","::[ Detail Pemeriksaan Radiologi ]::",
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "where concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and jns_perawatan_radiologi.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.nip like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                           " order by periksa_radiologi.tgl_periksa",param);
                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
                    Valid.MyReportqry("rptDetailRadiologi.jasper","report","::[ Detail Pemeriksaan Radiologi ]::",
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and jns_perawatan_radiologi.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.nip like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                           " order by periksa_radiologi.tgl_periksa",param);
                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
                    Valid.MyReportqry("rptDetailRadiologi.jasper","report","::[ Detail Pemeriksaan Radiologi ]::",
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and jns_perawatan_radiologi.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.nip like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                           " order by periksa_radiologi.tgl_periksa",param);
                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
                    Valid.MyReportqry("rptDetailRadiologi.jasper","report","::[ Detail Pemeriksaan Radiologi ]::",
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and jns_perawatan_radiologi.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.nip like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                           " order by periksa_radiologi.tgl_periksa",param);
                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
                    Valid.MyReportqry("rptDetailRadiologi.jasper","report","::[ Detail Pemeriksaan Radiologi ]::",
                           "select periksa_radiologi.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_radiologi.kd_jenis_prw,jns_perawatan_radiologi.nm_perawatan, "+
                           "periksa_radiologi.kd_dokter,dokter.nm_dokter,periksa_radiologi.nip,"+
                           "petugas.nama,periksa_radiologi.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_radiologi.tgl_periksa,periksa_radiologi.jam,penjab.png_jawab,"+
                           "periksa_radiologi.bagian_rs,periksa_radiologi.bhp,periksa_radiologi.tarif_perujuk,"+
                           "periksa_radiologi.tarif_tindakan_dokter,periksa_radiologi.tarif_tindakan_petugas,"+
                           "periksa_radiologi.kso,periksa_radiologi.menejemen,periksa_radiologi.biaya,"+
                           "if(periksa_radiologi.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_radiologi.no_rawat limit 1 )) as ruangan "+
                           "from periksa_radiologi inner join reg_periksa on periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_radiologi.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_radiologi.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and jns_perawatan_radiologi.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.nip like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and periksa_radiologi.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_radiologi.tgl_periksa,' ',periksa_radiologi.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_radiologi.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukRad.getText()+NmDokterPerujukRad.getText()+"%' and concat(periksa_radiologi.nip,petugas.nama) like '%"+KdPetugasRad.getText()+NmPetugasRad.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarRad.getText()+NmCaraBayarRad.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                           " order by periksa_radiologi.tgl_periksa",param);
                }

            }  
        }   
    }

    private void CetakLab() {
        if(tabModeLaborat.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            TCari.requestFocus();
        }else if(tabModeLaborat.getRowCount()!=0){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());
            param.put("tanggal1",Valid.SetTglJam(Tgl1.getSelectedItem()+""));
            param.put("tanggal2",Valid.SetTglJam(Tgl2.getSelectedItem()+""));
            param.put("cari","%"+TCari.getText().trim()+"%");                    
            param.put("logo",Sequel.cariGambar("select logo from setting")); 
            if(KdDokterPerujukLab.getText().equals("")&&NmDokterPerujukLab.getText().equals("")&&KdPetugasLab.getText().equals("")&&NmPetugasLab.getText().equals("")&&KdCaraBayarLab.getText().equals("")&&NmCaraBayarLab.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                Valid.MyReportqry("rptDetailLaborat.jasper","report","::[ Detail Pemeriksaan Laborat ]::",
                       "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                       "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                       "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                       "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                       "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                       "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                       "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                       "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                       "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                       "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                       "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan "+
                       "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                       "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                       "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                       "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                       "inner join petugas on periksa_lab.nip=petugas.nip "+
                       "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                       "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                       "where concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' order by periksa_lab.tgl_periksa",param);
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    Valid.MyReportqry("rptDetailLaborat.jasper","report","::[ Detail Pemeriksaan Laborat ]::",
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "where concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and jns_perawatan_lab.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.nip like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                           " order by periksa_lab.tgl_periksa",param);
                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
                    Valid.MyReportqry("rptDetailLaborat.jasper","report","::[ Detail Pemeriksaan Laborat ]::",
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and jns_perawatan_lab.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.nip like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                           " order by periksa_lab.tgl_periksa",param);
                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
                    Valid.MyReportqry("rptDetailLaborat.jasper","report","::[ Detail Pemeriksaan Laborat ]::",
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "inner join piutang_pasien on reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and jns_perawatan_lab.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.nip like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                           " order by periksa_lab.tgl_periksa",param);
                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
                    Valid.MyReportqry("rptDetailLaborat.jasper","report","::[ Detail Pemeriksaan Laborat ]::",
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and jns_perawatan_lab.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.nip like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                           " order by periksa_lab.tgl_periksa",param);
                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
                    Valid.MyReportqry("rptDetailLaborat.jasper","report","::[ Detail Pemeriksaan Laborat ]::",
                           "select periksa_lab.no_rawat,reg_periksa.no_rkm_medis,pasien_aps.nm_pasien, "+
                           "periksa_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan, "+
                           "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                           "petugas.nama,periksa_lab.dokter_perujuk,perujuk.nm_dokter as perujuk,"+
                           "periksa_lab.tgl_periksa,periksa_lab.jam,penjab.png_jawab,"+
                           "periksa_lab.bagian_rs,periksa_lab.bhp,periksa_lab.tarif_perujuk,"+
                           "periksa_lab.tarif_tindakan_dokter,periksa_lab.tarif_tindakan_petugas,"+
                           "periksa_lab.kso,periksa_lab.menejemen,periksa_lab.biaya,"+
                           "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                           "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                           "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan "+
                           "from periksa_lab inner join reg_periksa on periksa_lab.no_rawat=reg_periksa.no_rawat "+
                           "inner join pasien_aps on reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis "+
                           "inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                           "inner join dokter as perujuk on periksa_lab.dokter_perujuk=perujuk.kd_dokter "+
                           "inner join petugas on periksa_lab.nip=petugas.nip "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           "inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                           "where reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and jns_perawatan_lab.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.nip like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and periksa_lab.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                           "reg_periksa.status_bayar='Belum Bayar' and concat(periksa_lab.tgl_periksa,' ',periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukLab.getText()+NmDokterPerujukLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasLab.getText()+NmPetugasLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarLab.getText()+NmCaraBayarLab.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                           " order by periksa_lab.tgl_periksa",param);
                }    
            }
        }
    }

    private void CetakDetailLab() {
        if(tabModeDetailLaborat.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            TCari.requestFocus();
        }else if(tabModeDetailLaborat.getRowCount()!=0){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());
            param.put("tanggal1",Valid.SetTglJam(Tgl1.getSelectedItem()+""));
            param.put("tanggal2",Valid.SetTglJam(Tgl2.getSelectedItem()+""));
            param.put("cari","%"+TCari.getText().trim()+"%");                    
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            if(KdDokterPerujukDetailLab.getText().equals("")&&NmDokterPerujukDetailLab.getText().equals("")&&KdPetugasDetailLab.getText().equals("")&&NmPetugasDetailLab.getText().equals("")&&KdCaraBayarDetailLab.getText().equals("")&&NmCaraBayarDetailLab.getText().equals("")&&TCari.getText().equals("")&&cmbStatus.getSelectedItem().equals("Semua")){
                Valid.MyReportqry("rptDetailDetailLaborat.jasper","report","::[ Detail Detail Pemeriksaan Laborat ]::",
                        "select detail_periksa_lab.no_rawat,reg_periksa.no_rkm_medis,"+
                        "pasien_aps.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                        "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                        "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                        "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                        "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                        "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                        "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                        "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                        "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                        "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                        "detail_periksa_lab.biaya_item from detail_periksa_lab "+
                        "inner join reg_periksa inner join pasien_aps inner join template_laboratorium "+
                        "inner join jns_perawatan_lab inner join periksa_lab "+
                        "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                        "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                        "and reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                        "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                        "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                        "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                        "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                        "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw "+
                        "where concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' order by detail_periksa_lab.tgl_periksa",param);
            }else{
                if(cmbStatus.getSelectedItem().equals("Semua")){
                    Valid.MyReportqry("rptDetailDetailLaborat.jasper","report","::[ Detail Detail Pemeriksaan Laborat ]::",
                            "select detail_periksa_lab.no_rawat,reg_periksa.no_rkm_medis,"+
                            "pasien_aps.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien_aps inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw "+
                            "where concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and detail_periksa_lab.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and detail_periksa_lab.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and jns_perawatan_lab.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.nip like '%"+TCari.getText().trim()+"%' or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                            " order by detail_periksa_lab.tgl_periksa",param);
                }else if(cmbStatus.getSelectedItem().equals("Piutang Belum Lunas")){
                    Valid.MyReportqry("rptDetailDetailLaborat.jasper","report","::[ Detail Detail Pemeriksaan Laborat ]::",
                            "select detail_periksa_lab.no_rawat,reg_periksa.no_rkm_medis,"+
                            "pasien_aps.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien_aps inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab inner join piutang_pasien "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                            "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and detail_periksa_lab.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and detail_periksa_lab.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and jns_perawatan_lab.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.nip like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Belum Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                            " order by detail_periksa_lab.tgl_periksa",param);
                }else if(cmbStatus.getSelectedItem().equals("Piutang Sudah Lunas")){
                    Valid.MyReportqry("rptDetailDetailLaborat.jasper","report","::[ Detail Detail Pemeriksaan Laborat ]::",
                            "select detail_periksa_lab.no_rawat,reg_periksa.no_rkm_medis,"+
                            "pasien_aps.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien_aps inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab inner join piutang_pasien "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and reg_periksa.no_rawat=piutang_pasien.no_rawat "+
                            "where reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and detail_periksa_lab.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and detail_periksa_lab.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and jns_perawatan_lab.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.nip like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and piutang_pasien.status='Lunas' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                            " order by detail_periksa_lab.tgl_periksa",param);
                }else if(cmbStatus.getSelectedItem().equals("Sudah Bayar Non Piutang")){
                    Valid.MyReportqry("rptDetailDetailLaborat.jasper","report","::[ Detail Detail Pemeriksaan Laborat ]::",
                            "select detail_periksa_lab.no_rawat,reg_periksa.no_rkm_medis,"+
                            "pasien_aps.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien_aps inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw "+
                            "where reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and detail_periksa_lab.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and detail_periksa_lab.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and jns_perawatan_lab.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.nip like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Sudah Bayar' and reg_periksa.no_rawat not in (select no_rawat from piutang_pasien) and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                            " order by detail_periksa_lab.tgl_periksa",param);
                }else if(cmbStatus.getSelectedItem().equals("Belum Terclosing Kasir")){
                    Valid.MyReportqry("rptDetailDetailLaborat.jasper","report","::[ Detail Detail Pemeriksaan Laborat ]::",
                            "select detail_periksa_lab.no_rawat,reg_periksa.no_rkm_medis,"+
                            "pasien_aps.nm_pasien,detail_periksa_lab.id_template,template_laboratorium.Pemeriksaan,"+
                            "periksa_lab.kd_dokter,dokter.nm_dokter,periksa_lab.nip,"+
                            "petugas.nama,periksa_lab.dokter_perujuk,penjab.png_jawab,perujuk.nm_dokter as perujuk,"+
                            "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,"+
                            "if(periksa_lab.status='Ralan',(select nm_poli from poliklinik where poliklinik.kd_poli=reg_periksa.kd_poli),"+
                            "(select bangsal.nm_bangsal from kamar_inap inner join kamar inner join bangsal on kamar_inap.kd_kamar=kamar.kd_kamar "+
                            "and kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=periksa_lab.no_rawat limit 1 )) as ruangan,"+
                            "detail_periksa_lab.bagian_rs,detail_periksa_lab.bhp,detail_periksa_lab.bagian_perujuk,"+
                            "detail_periksa_lab.bagian_dokter,detail_periksa_lab.bagian_laborat,"+
                            "detail_periksa_lab.kso,detail_periksa_lab.menejemen,"+
                            "detail_periksa_lab.biaya_item from detail_periksa_lab "+
                            "inner join reg_periksa inner join pasien_aps inner join template_laboratorium "+
                            "inner join jns_perawatan_lab inner join periksa_lab "+
                            "inner join dokter inner join petugas inner join penjab inner join dokter as perujuk "+
                            "on detail_periksa_lab.no_rawat=reg_periksa.no_rawat "+
                            "and reg_periksa.no_rkm_medis=pasien_aps.no_rkm_medis and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                            "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw and perujuk.kd_dokter=periksa_lab.dokter_perujuk "+
                            "and reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.kd_dokter=dokter.kd_dokter "+
                            "and periksa_lab.nip=petugas.nip and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                            "and periksa_lab.jam=detail_periksa_lab.jam and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                            "and jns_perawatan_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw "+
                            "where reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and detail_periksa_lab.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and pasien_aps.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and detail_periksa_lab.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and jns_perawatan_lab.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.nip like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and periksa_lab.dokter_perujuk like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and perujuk.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                            "reg_periksa.status_bayar='Belum Bayar' and concat(detail_periksa_lab.tgl_periksa,' ',detail_periksa_lab.jam) between '"+Valid.SetTglJam(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTglJam(Tgl2.getSelectedItem()+"")+"' and concat(periksa_lab.dokter_perujuk,perujuk.nm_dokter) like '%"+KdDokterPerujukDetailLab.getText()+NmDokterPerujukDetailLab.getText()+"%' and concat(periksa_lab.nip,petugas.nama) like '%"+KdPetugasDetailLab.getText()+NmPetugasDetailLab.getText()+"%' and concat(reg_periksa.kd_pj,penjab.png_jawab) like '%"+KdCaraBayarDetailLab.getText()+NmCaraBayarDetailLab.getText()+"%' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                            " order by detail_periksa_lab.tgl_periksa",param);
                }    
            }
        }   
    }
    

}
