package laporan;

import fungsi.WarnaTable;
import fungsi.akses;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * Dialog CRUD Khanza untuk formulir RS-Online SIRS RL 1.1-1.4 & 2.
 *
 * Metadata-driven: skema tabel sirs_rl* dibaca RUNTIME dari information_schema
 * (bukan di-hardcode). Tiga mode:
 *  - SINGLETON (RL 1.1 / 1.4 / 2) : satu baris id=1, form input scrollable.
 *  - CHECKLIST (RL 1.2)           : tabel ceklis ketersediaan pelayanan.
 *  - ROWS      (RL 1.3)           : tabel editable tempat tidur (tambah/hapus).
 *
 * Idiom widget & helper ikon() aman-null mengikuti DlgSirsRL63; pola JDBC /
 * PreparedStatement mengikuti DlgRl38. Semua query PARAMETERIZED. Koneksi
 * singleton condb() TIDAK ditutup.
 */
public final class DlgSirsEntry extends javax.swing.JDialog {

    private static final int MODE_SINGLETON = 1;
    private static final int MODE_CHECKLIST = 2;
    private static final int MODE_ROWS = 3;

    private final validasi Valid = new validasi();
    private final sekuel Sequel = new sekuel();
    private final Connection koneksi = koneksiDB.condb();

    private final String kode;
    private String tabel = "";
    private String judul = "";
    private int mode = MODE_SINGLETON;
    private boolean hasUpdatedBy = false;

    /** Deskripsi satu kolom editable (mode SINGLETON). */
    private static final class Col {
        String name;      // nama kolom di DB
        String dataType;  // enum/date/int/decimal/text/varchar/...
        String[] enumVals;
        JComponent input; // ComboBox / Tanggal / TextBox / JTextArea
        JCheckBox dateNull; // hanya untuk kolom date (centang = kosong/NULL)
        boolean isEnum, isDate, isNumeric, isText;
    }

    private final List<Col> cols = new ArrayList<>();
    private final java.util.Map<String, Col> colByName = new java.util.HashMap<>();

    // widget yang dipakai lintas-mode
    private widget.InternalFrame internalFrame1;
    private widget.ScrollPane Scroll;
    private widget.Table tbData;
    private widget.panelisi panelBawah;
    private JPanel formPanel;
    private DefaultTableModel tabMode;
    private javax.swing.JTabbedPane tab;
    private widget.ScrollPane scrollView;
    private widget.Table tbView;
    private widget.Label lblCount;

    public DlgSirsEntry(java.awt.Frame parent, boolean modal, String kode) {
        super(parent, modal);
        this.kode = kode == null ? "" : kode.trim();
        konfigurasi(this.kode);
        bacaSkema();
        initFrame();
        switch (mode) {
            case MODE_CHECKLIST: buildChecklist(); loadChecklist(); break;
            case MODE_ROWS:      buildRows();      loadRows();      break;
            default:             buildSingleton(); loadSingleton(); break;
        }
        refreshDataTab();
        // Samakan dgn web: bagian tambahan repeatable per formulir.
        if ("sirs_rl11_profil".equals(tabel)) { buildModulTab(); buildKerjasamaTab(); }
        if ("sirs_rl14_aspak_meta".equals(tabel)) { buildInventarisTab(); }
        pack();
        setSize(mode == MODE_SINGLETON ? new Dimension(780, 640)
                : new Dimension(840, 600));
        setLocationRelativeTo(null);
    }

    /** Peta kode -> mode + tabel + judul. */
    private void konfigurasi(String k) {
        switch (k) {
            case "1.1": mode = MODE_SINGLETON; tabel = "sirs_rl11_profil";     judul = "RL 1.1 Profil Rumah Sakit"; break;
            case "1.4": mode = MODE_SINGLETON; tabel = "sirs_rl14_aspak_meta"; judul = "RL 1.4 ASPAK"; break;
            case "2":   mode = MODE_SINGLETON; tabel = "sirs_rl2_meta";        judul = "RL 2 Ketenagaan (SISDMK)"; break;
            case "1.2": mode = MODE_CHECKLIST; tabel = "sirs_rl12_pelayanan";  judul = "RL 1.2 Ketersediaan Pelayanan"; break;
            case "1.3": mode = MODE_ROWS;      tabel = "sirs_rl13_tt";         judul = "RL 1.3 Ketersediaan Tempat Tidur"; break;
            default:    mode = MODE_SINGLETON; tabel = "sirs_rl11_profil";     judul = "RL " + k; break;
        }
    }

    // =========================================================================
    // SKEMA (runtime, information_schema)
    // =========================================================================
    private void bacaSkema() {
        cols.clear();
        String sql = "SELECT column_name, data_type, column_type FROM information_schema.columns "
                + "WHERE table_schema=DATABASE() AND table_name=? ORDER BY ordinal_position";
        try (PreparedStatement pst = koneksi.prepareStatement(sql)) {
            pst.setString(1, tabel);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("column_name");
                    String dtype = rs.getString("data_type").toLowerCase();
                    String ctype = rs.getString("column_type");
                    if ("updated_by".equalsIgnoreCase(name)) { hasUpdatedBy = true; continue; }
                    if ("id".equalsIgnoreCase(name) || "updated_at".equalsIgnoreCase(name)) continue;
                    // Untuk mode SINGLETON kita bangun widget per kolom.
                    if (mode != MODE_SINGLETON) continue;
                    Col c = new Col();
                    c.name = name;
                    c.dataType = dtype;
                    if ("enum".equals(dtype)) {
                        c.isEnum = true;
                        c.enumVals = parseEnum(ctype);
                    } else if ("date".equals(dtype)) {
                        c.isDate = true;
                    } else if (dtype.matches("tinyint|smallint|mediumint|int|bigint|decimal|float|double")) {
                        c.isNumeric = true;
                    } else if (dtype.matches("text|mediumtext|longtext")) {
                        c.isText = true;
                    }
                    cols.add(c);
                }
            }
        } catch (Exception e) {
            System.out.println("DlgSirsEntry bacaSkema " + tabel + " : " + e);
        }
    }

    /** enum('a','b','c') -> {a,b,c} (memulihkan escape ''). */
    private String[] parseEnum(String ctype) {
        try {
            int a = ctype.indexOf('(');
            int b = ctype.lastIndexOf(')');
            String inner = ctype.substring(a + 1, b).trim();
            if (inner.startsWith("'")) inner = inner.substring(1);
            if (inner.endsWith("'")) inner = inner.substring(0, inner.length() - 1);
            String[] raw = inner.split("','");
            for (int i = 0; i < raw.length; i++) raw[i] = raw[i].replace("''", "'");
            return raw;
        } catch (Exception e) {
            return new String[]{};
        }
    }

    /** Kolom nama pegawai (pakai pencari DlgCariPegawai). */
    private boolean isPegawaiNama(String col) {
        return col.equals("nama_direktur") || col.equals("nama_ka_ifrs");
    }

    /** Label field mengikuti form WEB (rl11/rl14/rl2.blade); fallback: humanize per kata. */
    private static final java.util.Map<String, String> WEB_LABEL = new java.util.HashMap<>();
    static {
        String[][] m = {
            {"tahun_berdiri","Tahun Berdiri"},{"nama_direktur","Nama Direktur"},{"jenis_rs","Jenis RS"},
            {"kelas_rs","Kelas RS"},{"status_blu","Status BLU"},{"kepemilikan","Kepemilikan"},
            {"nama_penyelenggara","Nama Penyelenggara"},{"kerjasama_bpjs","Kerjasama BPJS"},
            {"luas_tanah_m2","Luas Tanah (m²)"},{"luas_bangunan_m2","Luas Bangunan (m²)"},
            {"no_call_center","No. Call Center"},{"website","Website"},
            {"no_sio","No. Surat Izin Operasional (SIO)"},{"tgl_sio","Tgl SIO"},
            {"tgl_berlaku_sio","Tgl Berlaku SIO"},{"file_sio","File SIO (path)"},
            {"pentahapan_akreditasi","Pentahapan Akreditasi"},{"tgl_penetapan_akreditasi","Tgl Penetapan Akreditasi"},
            {"tgl_berlaku_akreditasi","Tgl Berlaku Akreditasi"},{"file_akreditasi","File Akreditasi (path)"},
            {"akreditasi_internasional","Akreditasi Internasional"},{"rs_pendidikan","RS Pendidikan"},
            {"no_sk_pendidikan","No. SK Pendidikan"},{"tgl_sk_pendidikan","Tgl SK Pendidikan"},
            {"no_sk_tt","No. SK Tempat Tidur"},{"tgl_sk_tt","Tgl SK Tempat Tidur"},{"file_sk_tt","File SK TT (path)"},
            {"ipwl","IPWL"},{"no_sk_ipwl","No. SK IPWL"},{"tgl_sk_ipwl","Tgl SK IPWL"},
            {"file_tarif_layanan","File Tarif Layanan (path)"},{"layanan_unggulan","Layanan Unggulan"},
            {"simrs_kondisi","Kondisi SIMRS"},{"pelayanan_darah","Pelayanan Darah"},
            {"komite_transfusi_darah","Komite Transfusi Darah"},{"audit_pelayanan_darah","Audit Pelayanan Darah"},
            {"latitude","Latitude"},{"longitude","Longitude"},{"nama_ka_ifrs","Nama Ka. IFRS"},
            {"telp_ka_ifrs","Telp Ka. IFRS"},{"email_ka_ifrs","Email Ka. IFRS"},{"kerjasama_psef","Kerjasama PSEF"},
            {"kerjasama_fk_fkg","Kerjasama FK / FKG"},{"foto_tampak_depan","Foto Tampak Depan"},
            {"foto_ruang_ralan","Foto Ruang Ralan"},{"foto_ruang_ranap","Foto Ruang Ranap"},
            {"foto_ruang_igd","Foto Ruang IGD"},{"foto_alkes","Foto Alkes"},{"simrs_jenis_vendor","Jenis Vendor"},
            {"simrs_nama_developer","Nama Developer"},{"simrs_akses_internet","Akses Internet"},
            {"simrs_pic_nama","PIC Nama"},{"simrs_pic_nik","PIC NIK"},{"simrs_pic_email","PIC Email"},
            {"simrs_pic_telp","PIC Telp"},{"simrs_pic_jabatan","PIC Jabatan"},
            // RL 1.4 ASPAK
            {"aspak_url","URL Aplikasi ASPAK"},{"persen_pemenuhan","Persen Pemenuhan (%)"},
            {"total_alkes","Total Alkes Tercatat"},{"total_terkalibrasi","Total Terkalibrasi"},
            // RL 2 SISDMK
            {"sisdmk_url","URL Aplikasi SISDMK"},{"total_di_sisdmk","Total Tenaga Ter-input di SISDMK"},
            // pic_* (RL 1.4/2) + umum
            {"pic_nama","Nama PIC"},{"pic_nik","NIK / NIP PIC"},{"pic_email","Email PIC"},
            {"pic_telp","Telp PIC"},{"pic_jabatan","Jabatan PIC"},{"terakhir_sync","Terakhir Sync"},
            {"catatan","Catatan"},
        };
        for (String[] e : m) WEB_LABEL.put(e[0], e[1]);
    }

    /** Label field: pakai label WEB kalau ada, jika tidak humanize per kata. */
    private String humanize(String col) {
        String w = WEB_LABEL.get(col);
        if (w != null) return w;
        String[] p = col.split("_");
        StringBuilder sb = new StringBuilder();
        for (String wd : p) {
            if (wd.isEmpty()) continue;
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(wd.charAt(0)));
            if (wd.length() > 1) sb.append(wd.substring(1));
        }
        return sb.toString();
    }

    // =========================================================================
    // FRAME UMUM
    // =========================================================================
    private void initFrame() {
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        panelBawah = new widget.panelisi();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SIRS " + judul);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)),
                "::[ " + judul + " ]::",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50)));
        internalFrame1.setLayout(new BorderLayout(1, 1));

        // 2 tab: "Input" (form/tabel editable) + "Data Input" (tabel data tersimpan, read-only).
        scrollView = new widget.ScrollPane();
        tbView = new widget.Table();
        tbView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbView.setDefaultRenderer(Object.class, new WarnaTable());
        scrollView.setViewportView(tbView);
        tab = new javax.swing.JTabbedPane();
        tab.addTab("Input", Scroll);
        tab.addTab("Data Input", scrollView);
        internalFrame1.add(tab, BorderLayout.CENTER);

        panelBawah.setPreferredSize(new Dimension(55, 47));
        panelBawah.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 9));
        internalFrame1.add(panelBawah, BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, BorderLayout.CENTER);
    }

    private widget.Button tombol(String teks, char mnem, String ikonPath) {
        widget.Button b = new widget.Button();
        b.setIcon(ikon(ikonPath));
        if (mnem != 0) {
            b.setMnemonic(mnem);
            b.setToolTipText("Alt+" + mnem);
        }
        b.setText(teks);
        b.setPreferredSize(new Dimension(Math.max(100, 24 + teks.length() * 9), 30));
        return b;
    }

    /** ImageIcon aman-null (hindari NPE bila resource tak ada). */
    private javax.swing.ImageIcon ikon(String path) {
        java.net.URL u = getClass().getResource(path);
        return u != null ? new javax.swing.ImageIcon(u) : null;
    }

    // =========================================================================
    // MODE SINGLETON
    // =========================================================================
    private void buildSingleton() {
        widget.panelisi fp = new widget.panelisi();
        fp.setLayout(new GridBagLayout());
        formPanel = fp;
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.WEST;
        int row = 0;
        for (Col c : cols) {
            g.gridy = row;
            // label: rata KANAN supaya titik dua nempel ke input (idiom form Khanza)
            g.gridx = 0;
            g.weightx = 0;
            g.fill = GridBagConstraints.NONE;
            g.anchor = GridBagConstraints.EAST;
            g.insets = new Insets(3, 6, 3, 2);
            widget.Label lab = new widget.Label();
            lab.setText(humanize(c.name) + " :");
            lab.setHorizontalAlignment(SwingConstants.RIGHT);
            lab.setPreferredSize(new Dimension(200, 23));
            formPanel.add(lab, g);

            // input: langsung di sebelah kanan label
            g.gridx = 1;
            g.weightx = 0;
            g.fill = GridBagConstraints.NONE;
            g.anchor = GridBagConstraints.WEST;
            g.insets = new Insets(3, 2, 3, 6);
            formPanel.add(buildInput(c), g);

            // filler kanan: cegah label+input melar ke tepi kanan
            g.gridx = 2;
            g.weightx = 1;
            g.fill = GridBagConstraints.HORIZONTAL;
            formPanel.add(new javax.swing.JLabel(), g);
            row++;
        }
        // pengganjal supaya rapat ke atas — weightx=0 supaya kolom label TIDAK melar
        // (kalau weightx ikut 1 dari filler baris, kolom 0 melebar & label EAST kepencet ke tengah).
        g.gridy = row; g.gridx = 0; g.weightx = 0; g.weighty = 1;
        g.fill = GridBagConstraints.NONE;
        formPanel.add(new javax.swing.JLabel(" "), g);

        Scroll.setViewportView(formPanel);
        Scroll.getVerticalScrollBar().setUnitIncrement(16);

        widget.Button btnSimpan = tombol("Simpan", 'S', "/picture/accept.png");
        btnSimpan.addActionListener(evt -> simpanSingleton());
        widget.Button btnKeluar = tombol("Keluar", 'K', "/picture/exit.png");
        btnKeluar.addActionListener(evt -> dispose());
        panelBawah.add(btnSimpan);
        panelBawah.add(btnKeluar);
    }

    @SuppressWarnings("unchecked")
    private Component buildInput(Col c) {
        colByName.put(c.name, c);
        // Kode Pegawai PIC: textbox + tombol cari -> DlgCariPegawai (isi kode+nama+jabatan).
        if (c.name.endsWith("pic_nik")) {
            widget.TextBox tb = new widget.TextBox();
            tb.setPreferredSize(new Dimension(200, 23));
            tb.setHorizontalAlignment(SwingConstants.LEFT);
            c.input = tb;
            widget.Button btn = new widget.Button();
            btn.setIcon(ikon("/picture/Search-16x16.png"));
            btn.setToolTipText("Cari Pegawai");
            btn.setPreferredSize(new Dimension(28, 23));
            final Col self = c;
            btn.addActionListener(evt -> cariPegawaiPic(self));
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
            p.setOpaque(false);
            p.add(tb);
            p.add(btn);
            return p;
        }
        // Nama pegawai (Direktur / Kepala IFRS): Kode Pegawai + Nama Pegawai + tombol cari.
        if (isPegawaiNama(c.name)) {
            widget.TextBox kodeTb = new widget.TextBox();
            kodeTb.setPreferredSize(new Dimension(110, 23));
            kodeTb.setToolTipText("Kode Pegawai");
            widget.TextBox namaTb = new widget.TextBox();
            namaTb.setPreferredSize(new Dimension(240, 23));
            namaTb.setToolTipText("Nama Pegawai");
            c.input = namaTb; // yang disimpan ke DB = nama
            widget.Button btn = new widget.Button();
            btn.setIcon(ikon("/picture/Search-16x16.png"));
            btn.setToolTipText("Cari Pegawai");
            btn.setPreferredSize(new Dimension(28, 23));
            btn.addActionListener(evt -> cariPegawaiNama(kodeTb, namaTb));
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
            p.setOpaque(false);
            p.add(kodeTb);
            p.add(namaTb);
            p.add(btn);
            return p;
        }
        if (c.isEnum) {
            widget.ComboBox cb = new widget.ComboBox();
            for (String v : c.enumVals) cb.addItem(v);
            cb.setPreferredSize(new Dimension(300, 23));
            c.input = cb;
            return cb;
        }
        if (c.isDate) {
            widget.Tanggal t = new widget.Tanggal();
            t.setDisplayFormat("dd-MM-yyyy");
            t.setPreferredSize(new Dimension(110, 23));
            JCheckBox chk = new JCheckBox("kosong");
            chk.setOpaque(false);
            c.input = t;
            c.dateNull = chk;
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
            p.setOpaque(false);
            p.add(t);
            p.add(chk);
            return p;
        }
        if (c.isText) {
            JTextArea ta = new JTextArea(3, 30);
            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);
            ta.setFont(new java.awt.Font("Tahoma", 0, 11));
            JScrollPane sp = new JScrollPane(ta);
            sp.setPreferredSize(new Dimension(320, 52));
            c.input = ta;
            return sp;
        }
        widget.TextBox tb = new widget.TextBox();
        tb.setPreferredSize(new Dimension(300, 23));
        tb.setHorizontalAlignment(SwingConstants.LEFT);
        if (c.isNumeric) {
            terapkanNumerik(tb, c.dataType.equals("decimal") || c.dataType.equals("float") || c.dataType.equals("double"));
        }
        c.input = tb;
        return tb;
    }

    /** Batasi input hanya angka (dan titik desimal / minus bila diizinkan). */
    private void terapkanNumerik(final javax.swing.text.JTextComponent tc, final boolean decimal) {
        tc.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char ch = e.getKeyChar();
                if (Character.isDigit(ch) || ch == '-'
                        || (decimal && ch == '.')
                        || ch == java.awt.event.KeyEvent.VK_BACK_SPACE
                        || ch == java.awt.event.KeyEvent.VK_DELETE) {
                    return;
                }
                e.consume();
            }
        });
    }

    /** Buka DlgCariPegawai (modal), isi Kode Pegawai + Nama + Jabatan PIC. */
    private void cariPegawaiPic(Col nikCol) {
        kepegawaian.DlgCariPegawai dp = new kepegawaian.DlgCariPegawai(null, true);
        try { dp.emptTeks(); } catch (Exception ignore) { }
        dp.setSize(900, 500);
        dp.setLocationRelativeTo(this);
        dp.setVisible(true); // modal -> blok sampai ditutup
        javax.swing.JTable t = dp.getTable();
        int r = t.getSelectedRow();
        if (r < 0) return;
        String nik = str(t.getValueAt(r, 0));
        String nama = t.getColumnCount() > 1 ? str(t.getValueAt(r, 1)) : "";
        String jabatan = t.getColumnCount() > 3 ? str(t.getValueAt(r, 3)) : "";
        ((widget.TextBox) nikCol.input).setText(nik);
        String prefix = nikCol.name.substring(0, nikCol.name.length() - 3); // buang "nik"
        setColText(prefix + "nama", nama);
        if (!jabatan.isEmpty()) setColText(prefix + "jabatan", jabatan);
    }

    /** Cari pegawai untuk field nama (Direktur/Kepala IFRS): isi Kode + Nama. */
    private void cariPegawaiNama(widget.TextBox kodeTb, widget.TextBox namaTb) {
        kepegawaian.DlgCariPegawai dp = new kepegawaian.DlgCariPegawai(null, true);
        try { dp.emptTeks(); } catch (Exception ignore) { }
        dp.setSize(900, 500);
        dp.setLocationRelativeTo(this);
        dp.setVisible(true);
        javax.swing.JTable t = dp.getTable();
        int r = t.getSelectedRow();
        if (r < 0) return;
        kodeTb.setText(str(t.getValueAt(r, 0)));
        if (t.getColumnCount() > 1) namaTb.setText(str(t.getValueAt(r, 1)));
    }

    /** Isi textbox kolom bila ada & bertipe TextBox. */
    private void setColText(String colName, String val) {
        Col c = colByName.get(colName);
        if (c != null && c.input instanceof widget.TextBox) {
            ((widget.TextBox) c.input).setText(val);
        }
    }

    private void loadSingleton() {
        String sql = "SELECT * FROM " + tabel + " WHERE id=1";
        try (PreparedStatement pst = koneksi.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            boolean ada = rs.next();
            for (Col c : cols) {
                if (c.isEnum) {
                    String v = ada ? rs.getString(c.name) : null;
                    if (v != null) ((widget.ComboBox) c.input).setSelectedItem(v);
                    else if (((widget.ComboBox) c.input).getItemCount() > 0)
                        ((widget.ComboBox) c.input).setSelectedIndex(0);
                } else if (c.isDate) {
                    java.sql.Date d = ada ? rs.getDate(c.name) : null;
                    widget.Tanggal t = (widget.Tanggal) c.input;
                    if (d == null) {
                        c.dateNull.setSelected(true); // kosong
                        t.setDate(new java.util.Date());
                    } else {
                        c.dateNull.setSelected(false);
                        t.setDate(d);
                    }
                } else if (c.isText) {
                    String v = ada ? rs.getString(c.name) : null;
                    ((JTextArea) c.input).setText(v == null ? "" : v);
                } else {
                    String v = ada ? rs.getString(c.name) : null;
                    ((widget.TextBox) c.input).setText(v == null ? "" : v);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data " + tabel + " :\n" + e);
            System.out.println("DlgSirsEntry loadSingleton : " + e);
        }
    }

    private void simpanSingleton() {
        // 1) rakit nilai + validasi numerik
        List<Object> params = new ArrayList<>();
        StringBuilder colNames = new StringBuilder("id");
        StringBuilder place = new StringBuilder("1");
        StringBuilder upd = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Col c : cols) {
            Object val;
            if (c.isEnum) {
                Object sel = ((widget.ComboBox) c.input).getSelectedItem();
                val = sel == null ? null : sel.toString();
            } else if (c.isDate) {
                if (c.dateNull.isSelected()) {
                    val = null;
                } else {
                    java.util.Date d = ((widget.Tanggal) c.input).getDate();
                    val = d == null ? null : sdf.format(d);
                }
            } else if (c.isText) {
                val = ((JTextArea) c.input).getText().trim();
            } else if (c.isNumeric) {
                String s = ((widget.TextBox) c.input).getText().trim();
                if (s.isEmpty()) {
                    val = null; // angka kosong -> NULL
                } else {
                    try {
                        Double.parseDouble(s);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(this, "Nilai '" + humanize(c.name) + "' harus berupa angka.");
                        ((widget.TextBox) c.input).requestFocus();
                        return;
                    }
                    val = s;
                }
            } else {
                val = ((widget.TextBox) c.input).getText().trim();
            }
            colNames.append(",").append(c.name);
            place.append(",?");
            if (upd.length() > 0) upd.append(",");
            upd.append(c.name).append("=VALUES(").append(c.name).append(")");
            params.add(val);
        }
        if (hasUpdatedBy) {
            colNames.append(",updated_by");
            place.append(",?");
            upd.append(",updated_by=VALUES(updated_by)");
            String u = akses.getkode() == null ? "" : akses.getkode();
            if (u.length() > 20) u = u.substring(0, 20);
            params.add(u);
        }
        colNames.append(",updated_at");
        place.append(",NOW()");
        upd.append(upd.length() > 0 ? ",updated_at=NOW()" : "updated_at=NOW()");

        String sql = "INSERT INTO " + tabel + " (" + colNames + ") VALUES (" + place + ") "
                + "ON DUPLICATE KEY UPDATE " + upd;

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try (PreparedStatement pst = koneksi.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                pst.setObject(i + 1, params.get(i));
            }
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data " + judul + " berhasil disimpan.");
            loadSingleton();
            selesaiSimpan();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan :\n" + e);
            System.out.println("DlgSirsEntry simpanSingleton : " + e);
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    // =========================================================================
    // MODE CHECKLIST (RL 1.2)
    // =========================================================================
    private void buildChecklist() {
        tabMode = new DefaultTableModel(null, new String[]{"id", "No.", "Kategori", "Sub Pelayanan", "Ada"}) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 4; }
            @Override
            public Class<?> getColumnClass(int c) { return c == 4 ? Boolean.class : Object.class; }
        };
        tbData = new widget.Table();
        tbData.setModel(tabMode);
        tbData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbData.setDefaultRenderer(Object.class, new WarnaTable());
        tbData.setPreferredScrollableViewportSize(new Dimension(760, 480));
        Scroll.setViewportView(tbData);
        // sembunyikan kolom id
        TableColumn idc = tbData.getColumnModel().getColumn(0);
        tbData.getColumnModel().removeColumn(idc);
        int[] w = {40, 260, 300, 60}; // No, Kategori, Sub, Ada
        for (int i = 0; i < w.length; i++) tbData.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        widget.Button btnSimpan = tombol("Simpan", 'S', "/picture/accept.png");
        btnSimpan.addActionListener(evt -> simpanChecklist());
        widget.Button btnAll = tombol("Centang Semua", 'C', "/picture/save-16x16.png");
        btnAll.addActionListener(evt -> setSemua(true));
        widget.Button btnNone = tombol("Kosongkan", 'O', "/picture/delete-16x16.png");
        btnNone.addActionListener(evt -> setSemua(false));
        widget.Button btnKeluar = tombol("Keluar", 'K', "/picture/exit.png");
        btnKeluar.addActionListener(evt -> dispose());
        lblCount = new widget.Label();
        lblCount.setPreferredSize(new Dimension(260, 23));
        panelBawah.add(btnSimpan);
        panelBawah.add(btnAll);
        panelBawah.add(btnNone);
        panelBawah.add(btnKeluar);
        panelBawah.add(lblCount);
        // update hitungan tiap kolom "Ada" berubah
        tabMode.addTableModelListener(e -> updateCount());
    }

    private void loadChecklist() {
        tabMode.setRowCount(0);
        String sql = "SELECT id, kat_name, sub_name, ada FROM " + tabel + " ORDER BY kat_no, sub_no";
        try (PreparedStatement pst = koneksi.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            int no = 1;
            String prevKat = null;
            while (rs.next()) {
                String kat = rs.getString("kat_name");
                // tampilkan nama kategori hanya di baris pertama grup (tampilan berkelompok spt web)
                String katShow = kat != null && kat.equals(prevKat) ? "" : kat;
                prevKat = kat;
                tabMode.addRow(new Object[]{
                    rs.getInt("id"), no++, katShow,
                    rs.getString("sub_name"), rs.getInt("ada") == 1});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat ceklis :\n" + e);
            System.out.println("DlgSirsEntry loadChecklist : " + e);
        }
        updateCount();
    }

    /** Ringkasan jumlah pelayanan tersedia (seperti header form web). */
    private void updateCount() {
        if (lblCount == null || tabMode == null) return;
        int total = tabMode.getRowCount(), ada = 0;
        for (int r = 0; r < total; r++) if (Boolean.TRUE.equals(tabMode.getValueAt(r, 4))) ada++;
        lblCount.setText("  Total: " + total + "   |   Tersedia: " + ada);
    }

    private void setSemua(boolean v) {
        for (int r = 0; r < tabMode.getRowCount(); r++) {
            tabMode.setValueAt(v, r, 4);
        }
    }

    private void simpanChecklist() {
        String sql = "UPDATE " + tabel + " SET ada=?, updated_at=NOW() WHERE id=?";
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try (PreparedStatement pst = koneksi.prepareStatement(sql)) {
            for (int r = 0; r < tabMode.getRowCount(); r++) {
                Object id = tabMode.getValueAt(r, 0);
                boolean ada = Boolean.TRUE.equals(tabMode.getValueAt(r, 4));
                pst.setInt(1, ada ? 1 : 0);
                pst.setInt(2, Integer.parseInt(id.toString()));
                pst.addBatch();
            }
            pst.executeBatch();
            JOptionPane.showMessageDialog(this, "Ketersediaan pelayanan berhasil disimpan.");
            loadChecklist();
            selesaiSimpan();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan :\n" + e);
            System.out.println("DlgSirsEntry simpanChecklist : " + e);
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    // =========================================================================
    // MODE ROWS (RL 1.3)
    // =========================================================================
    private void buildRows() {
        tabMode = new DefaultTableModel(null,
                new String[]{"id", "Kelas TT", "Ruangan", "Tersedia", "Terpakai", "Catatan"}) {
            @Override
            public boolean isCellEditable(int r, int c) { return c > 0; }
        };
        tbData = new widget.Table();
        tbData.setModel(tabMode);
        tbData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbData.setDefaultRenderer(Object.class, new WarnaTable());
        tbData.setPreferredScrollableViewportSize(new Dimension(760, 440));
        Scroll.setViewportView(tbData);
        TableColumn idc = tbData.getColumnModel().getColumn(0);
        tbData.getColumnModel().removeColumn(idc);
        int[] w = {160, 220, 80, 80, 220};
        for (int i = 0; i < w.length; i++) tbData.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        widget.Button btnTambah = tombol("Tambah", 'T', "/picture/add-file-16x16.png");
        btnTambah.addActionListener(evt -> tambahRow());
        widget.Button btnHapus = tombol("Hapus", 'H', "/picture/delete-16x16.png");
        btnHapus.addActionListener(evt -> hapusRow());
        widget.Button btnSimpan = tombol("Simpan", 'S', "/picture/accept.png");
        btnSimpan.addActionListener(evt -> simpanRows());
        widget.Button btnKeluar = tombol("Keluar", 'K', "/picture/exit.png");
        btnKeluar.addActionListener(evt -> dispose());
        panelBawah.add(btnTambah);
        panelBawah.add(btnHapus);
        panelBawah.add(btnSimpan);
        panelBawah.add(btnKeluar);
    }

    private void loadRows() {
        tabMode.setRowCount(0);
        String sql = "SELECT id, kelas_tt, ruangan, jml_tersedia, jml_terpakai, catatan FROM "
                + tabel + " ORDER BY kelas_tt, ruangan";
        try (PreparedStatement pst = koneksi.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                tabMode.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("kelas_tt"), rs.getString("ruangan"),
                    rs.getInt("jml_tersedia"), rs.getInt("jml_terpakai"),
                    rs.getString("catatan") == null ? "" : rs.getString("catatan")});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat tempat tidur :\n" + e);
            System.out.println("DlgSirsEntry loadRows : " + e);
        }
    }

    private void tambahRow() {
        if (tbData.isEditing()) tbData.getCellEditor().stopCellEditing();
        tabMode.addRow(new Object[]{"", "", "", "0", "0", ""});
    }

    private void hapusRow() {
        int r = tbData.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus dulu.");
            return;
        }
        if (tbData.isEditing()) tbData.getCellEditor().stopCellEditing();
        int mr = tbData.convertRowIndexToModel(r);
        Object id = tabMode.getValueAt(mr, 0);
        if (JOptionPane.showConfirmDialog(this, "Hapus baris ini?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        if (id != null && !id.toString().trim().isEmpty()) {
            try (PreparedStatement pst = koneksi.prepareStatement("DELETE FROM " + tabel + " WHERE id=?")) {
                pst.setInt(1, Integer.parseInt(id.toString()));
                pst.executeUpdate();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus :\n" + e);
                return;
            }
        }
        tabMode.removeRow(mr);
    }

    private void simpanRows() {
        if (tbData.isEditing()) tbData.getCellEditor().stopCellEditing();
        int n = tabMode.getRowCount();
        Set<String> unik = new HashSet<>();
        // validasi
        for (int r = 0; r < n; r++) {
            String kelas = str(tabMode.getValueAt(r, 1)).trim();
            String ruang = str(tabMode.getValueAt(r, 2)).trim();
            String sTer = str(tabMode.getValueAt(r, 3)).trim();
            String sPak = str(tabMode.getValueAt(r, 4)).trim();
            if (kelas.isEmpty() || ruang.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Baris " + (r + 1) + ": Kelas TT dan Ruangan wajib diisi.");
                return;
            }
            int tersedia, terpakai;
            try {
                tersedia = sTer.isEmpty() ? 0 : Integer.parseInt(sTer);
                terpakai = sPak.isEmpty() ? 0 : Integer.parseInt(sPak);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Baris " + (r + 1) + ": Tersedia/Terpakai harus angka bulat.");
                return;
            }
            if (terpakai > tersedia) {
                JOptionPane.showMessageDialog(this, "Baris " + (r + 1) + ": Terpakai (" + terpakai
                        + ") tidak boleh melebihi Tersedia (" + tersedia + ").");
                return;
            }
            String key = (kelas + "||" + ruang).toLowerCase();
            if (!unik.add(key)) {
                JOptionPane.showMessageDialog(this, "Kombinasi Kelas TT + Ruangan harus unik. Duplikat di baris " + (r + 1) + ".");
                return;
            }
        }
        // simpan
        String u = akses.getkode() == null ? "" : akses.getkode();
        if (u.length() > 20) u = u.substring(0, 20);
        String sqlIns = "INSERT INTO " + tabel
                + " (kelas_tt, ruangan, jml_tersedia, jml_terpakai, catatan, updated_by, updated_at) "
                + "VALUES (?,?,?,?,?,?,NOW())";
        String sqlUpd = "UPDATE " + tabel + " SET kelas_tt=?, ruangan=?, jml_tersedia=?, jml_terpakai=?, "
                + "catatan=?, updated_by=?, updated_at=NOW() WHERE id=?";
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            for (int r = 0; r < n; r++) {
                Object id = tabMode.getValueAt(r, 0);
                String kelas = str(tabMode.getValueAt(r, 1)).trim();
                String ruang = str(tabMode.getValueAt(r, 2)).trim();
                int tersedia = Integer.parseInt(str(tabMode.getValueAt(r, 3)).trim().isEmpty() ? "0" : str(tabMode.getValueAt(r, 3)).trim());
                int terpakai = Integer.parseInt(str(tabMode.getValueAt(r, 4)).trim().isEmpty() ? "0" : str(tabMode.getValueAt(r, 4)).trim());
                String catatan = str(tabMode.getValueAt(r, 5)).trim();
                boolean baru = id == null || id.toString().trim().isEmpty();
                try (PreparedStatement pst = koneksi.prepareStatement(baru ? sqlIns : sqlUpd)) {
                    pst.setString(1, kelas);
                    pst.setString(2, ruang);
                    pst.setInt(3, tersedia);
                    pst.setInt(4, terpakai);
                    pst.setString(5, catatan);
                    pst.setString(6, u);
                    if (!baru) pst.setInt(7, Integer.parseInt(id.toString()));
                    pst.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "Data tempat tidur berhasil disimpan.");
            loadRows();
            selesaiSimpan();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan :\n" + e);
            System.out.println("DlgSirsEntry simpanRows : " + e);
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    private String str(Object o) { return o == null ? "" : o.toString(); }

    // =========================================================================
    // TAB "Modul SIMRS" (RL 1.1) — checklist sirs_rl11_simrs_modul (seperti web).
    // =========================================================================
    private void buildModulTab() {
        final DefaultTableModel mm = new DefaultTableModel(null,
                new String[]{"id", "Kategori", "Sub Kategori", "Modul", "Ada"}) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 4; }
            @Override
            public Class<?> getColumnClass(int c) { return c == 4 ? Boolean.class : Object.class; }
        };
        widget.Table tb = new widget.Table();
        tb.setModel(mm);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb.setDefaultRenderer(Object.class, new WarnaTable());
        // muat data
        try (PreparedStatement pst = koneksi.prepareStatement(
                "SELECT id, kategori, sub_kategori, modul, ada FROM sirs_rl11_simrs_modul ORDER BY id");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                mm.addRow(new Object[]{rs.getInt("id"), rs.getString("kategori"),
                    rs.getString("sub_kategori"), rs.getString("modul"), rs.getInt("ada") == 1});
            }
        } catch (Exception e) { System.out.println("buildModulTab load: " + e); }
        // sembunyikan kolom id
        TableColumn idc = tb.getColumnModel().getColumn(0);
        tb.getColumnModel().removeColumn(idc);
        int[] w = {140, 160, 360, 50};
        for (int i = 0; i < w.length; i++) tb.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        widget.ScrollPane sc = new widget.ScrollPane();
        sc.setViewportView(tb);

        widget.panelisi bar = new widget.panelisi();
        bar.setPreferredSize(new Dimension(55, 45));
        bar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 9));
        widget.Button bSimpan = tombol("Simpan Modul", 'M', "/picture/accept.png");
        bSimpan.addActionListener(evt -> simpanModul(mm));
        widget.Button bAll = tombol("Centang Semua", 'C', "/picture/save-16x16.png");
        bAll.addActionListener(evt -> { for (int r = 0; r < mm.getRowCount(); r++) mm.setValueAt(true, r, 4); });
        widget.Button bNone = tombol("Kosongkan", 'O', "/picture/delete-16x16.png");
        bNone.addActionListener(evt -> { for (int r = 0; r < mm.getRowCount(); r++) mm.setValueAt(false, r, 4); });
        bar.add(bSimpan); bar.add(bAll); bar.add(bNone);

        JPanel panel = new JPanel(new BorderLayout(1, 1));
        panel.add(sc, BorderLayout.CENTER);
        panel.add(bar, BorderLayout.PAGE_END);
        // sisipkan setelah tab "Input"
        tab.insertTab("Modul SIMRS", null, panel, null, 1);
    }

    // =========================================================================
    // TAB "Inventaris" (RL 1.4) — sirs_rl14_inventaris_kat (repeatable, seperti web).
    // =========================================================================
    private void buildInventarisTab() {
        final DefaultTableModel im = new DefaultTableModel(null, new String[]{
            "id", "Kategori", "Nama Item", "Jumlah", "Baik", "Rusak Ringan", "Rusak Berat", "Satuan", "Catatan"}) {
            @Override
            public boolean isCellEditable(int r, int c) { return c > 0; }
        };
        final widget.Table tb = new widget.Table();
        tb.setModel(im);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb.setDefaultRenderer(Object.class, new WarnaTable());
        muatInventaris(im);
        TableColumn idc = tb.getColumnModel().getColumn(0);
        tb.getColumnModel().removeColumn(idc);
        int[] w = {150, 200, 70, 60, 90, 90, 70, 180};
        for (int i = 0; i < w.length; i++) tb.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        widget.ScrollPane sc = new widget.ScrollPane();
        sc.setViewportView(tb);
        widget.panelisi bar = new widget.panelisi();
        bar.setPreferredSize(new Dimension(55, 45));
        bar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 9));
        widget.Button bTambah = tombol("Tambah", 'T', "/picture/add-file-16x16.png");
        bTambah.addActionListener(evt -> { if (tb.isEditing()) tb.getCellEditor().stopCellEditing();
            im.addRow(new Object[]{"", "", "", "0", "0", "0", "0", "unit", ""}); });
        widget.Button bHapus = tombol("Hapus", 'H', "/picture/delete-16x16.png");
        bHapus.addActionListener(evt -> hapusInventaris(tb, im));
        widget.Button bSimpan = tombol("Simpan", 'S', "/picture/accept.png");
        bSimpan.addActionListener(evt -> simpanInventaris(tb, im));
        bar.add(bTambah); bar.add(bHapus); bar.add(bSimpan);

        JPanel panel = new JPanel(new BorderLayout(1, 1));
        panel.add(sc, BorderLayout.CENTER);
        panel.add(bar, BorderLayout.PAGE_END);
        tab.insertTab("Inventaris", null, panel, null, 1);
    }

    private void muatInventaris(DefaultTableModel im) {
        im.setRowCount(0);
        try (PreparedStatement pst = koneksi.prepareStatement(
                "SELECT id, kategori, nama_item, jumlah, jml_baik, jml_rusak_ringan, jml_rusak_berat, satuan, catatan "
                + "FROM sirs_rl14_inventaris_kat ORDER BY kategori, nama_item");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                im.addRow(new Object[]{rs.getInt("id"), str(rs.getString("kategori")), str(rs.getString("nama_item")),
                    rs.getInt("jumlah"), rs.getInt("jml_baik"), rs.getInt("jml_rusak_ringan"),
                    rs.getInt("jml_rusak_berat"), str(rs.getString("satuan")), str(rs.getString("catatan"))});
            }
        } catch (Exception e) { System.out.println("muatInventaris: " + e); }
    }

    private void hapusInventaris(widget.Table tb, DefaultTableModel im) {
        int r = tb.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus dulu."); return; }
        if (tb.isEditing()) tb.getCellEditor().stopCellEditing();
        int mr = tb.convertRowIndexToModel(r);
        Object id = im.getValueAt(mr, 0);
        if (JOptionPane.showConfirmDialog(this, "Hapus item ini?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        if (id != null && !id.toString().trim().isEmpty()) {
            try (PreparedStatement pst = koneksi.prepareStatement("DELETE FROM sirs_rl14_inventaris_kat WHERE id=?")) {
                pst.setInt(1, Integer.parseInt(id.toString()));
                pst.executeUpdate();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Gagal menghapus :\n" + e); return; }
        }
        im.removeRow(mr);
    }

    private void simpanInventaris(widget.Table tb, DefaultTableModel im) {
        if (tb.isEditing()) tb.getCellEditor().stopCellEditing();
        for (int r = 0; r < im.getRowCount(); r++) {
            if (str(im.getValueAt(r, 1)).trim().isEmpty() || str(im.getValueAt(r, 2)).trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Baris " + (r + 1) + ": Kategori & Nama Item wajib diisi.");
                return;
            }
            try {
                int jml = pInt(im.getValueAt(r, 3)), baik = pInt(im.getValueAt(r, 4)),
                    rr = pInt(im.getValueAt(r, 5)), rb = pInt(im.getValueAt(r, 6));
                if (baik + rr + rb > jml) {
                    JOptionPane.showMessageDialog(this, "Baris " + (r + 1) + ": Baik+Rusak Ringan+Rusak Berat tidak boleh melebihi Jumlah.");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Baris " + (r + 1) + ": Jumlah/Baik/Rusak harus angka bulat.");
                return;
            }
        }
        String ins = "INSERT INTO sirs_rl14_inventaris_kat (kategori, nama_item, jumlah, jml_baik, jml_rusak_ringan, jml_rusak_berat, satuan, catatan, updated_at) VALUES (?,?,?,?,?,?,?,?,NOW())";
        String upd = "UPDATE sirs_rl14_inventaris_kat SET kategori=?, nama_item=?, jumlah=?, jml_baik=?, jml_rusak_ringan=?, jml_rusak_berat=?, satuan=?, catatan=?, updated_at=NOW() WHERE id=?";
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            for (int r = 0; r < im.getRowCount(); r++) {
                Object id = im.getValueAt(r, 0);
                boolean baru = id == null || id.toString().trim().isEmpty();
                try (PreparedStatement pst = koneksi.prepareStatement(baru ? ins : upd)) {
                    pst.setString(1, str(im.getValueAt(r, 1)).trim());
                    pst.setString(2, str(im.getValueAt(r, 2)).trim());
                    pst.setInt(3, pInt(im.getValueAt(r, 3)));
                    pst.setInt(4, pInt(im.getValueAt(r, 4)));
                    pst.setInt(5, pInt(im.getValueAt(r, 5)));
                    pst.setInt(6, pInt(im.getValueAt(r, 6)));
                    pst.setString(7, str(im.getValueAt(r, 7)).trim().isEmpty() ? "unit" : str(im.getValueAt(r, 7)).trim());
                    pst.setString(8, str(im.getValueAt(r, 8)).trim());
                    if (!baru) pst.setInt(9, Integer.parseInt(id.toString()));
                    pst.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "Data Inventaris berhasil disimpan.");
            muatInventaris(im);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan :\n" + e);
            System.out.println("simpanInventaris: " + e);
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    private int pInt(Object o) {
        String s = str(o).trim();
        return s.isEmpty() ? 0 : Integer.parseInt(s);
    }

    // =========================================================================
    // TAB "Kerjasama LN" (RL 1.1) — sirs_rl11_kerjasama_ln (repeatable, seperti web).
    // =========================================================================
    private void buildKerjasamaTab() {
        final DefaultTableModel km = new DefaultTableModel(null, new String[]{
            "id", "Nama Institusi", "Negara", "Area", "Bentuk", "Tgl Mulai", "Tgl Akhir", "Output Kerja", "Keterangan"}) {
            @Override
            public boolean isCellEditable(int r, int c) { return c > 0; }
        };
        final widget.Table tb = new widget.Table();
        tb.setModel(km);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb.setDefaultRenderer(Object.class, new WarnaTable());
        muatKerjasama(km);
        TableColumn idc = tb.getColumnModel().getColumn(0);
        tb.getColumnModel().removeColumn(idc);
        int[] w = {180, 90, 120, 120, 90, 90, 160, 160};
        for (int i = 0; i < w.length; i++) tb.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        widget.ScrollPane sc = new widget.ScrollPane();
        sc.setViewportView(tb);

        widget.panelisi bar = new widget.panelisi();
        bar.setPreferredSize(new Dimension(55, 45));
        bar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 9));
        widget.Button bTambah = tombol("Tambah", 'T', "/picture/add-file-16x16.png");
        bTambah.addActionListener(evt -> { if (tb.isEditing()) tb.getCellEditor().stopCellEditing();
            km.addRow(new Object[]{"", "", "", "", "", "", "", "", ""}); });
        widget.Button bHapus = tombol("Hapus", 'H', "/picture/delete-16x16.png");
        bHapus.addActionListener(evt -> hapusKerjasama(tb, km));
        widget.Button bSimpan = tombol("Simpan", 'S', "/picture/accept.png");
        bSimpan.addActionListener(evt -> simpanKerjasama(tb, km));
        bar.add(bTambah); bar.add(bHapus); bar.add(bSimpan);

        JPanel panel = new JPanel(new BorderLayout(1, 1));
        panel.add(sc, BorderLayout.CENTER);
        panel.add(bar, BorderLayout.PAGE_END);
        tab.insertTab("Kerjasama LN", null, panel, null, 2);
    }

    private void muatKerjasama(DefaultTableModel km) {
        km.setRowCount(0);
        try (PreparedStatement pst = koneksi.prepareStatement(
                "SELECT id, nama_institusi, negara, area, bentuk, tgl_mulai, tgl_akhir, output_kerja, keterangan "
                + "FROM sirs_rl11_kerjasama_ln ORDER BY id");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                km.addRow(new Object[]{rs.getInt("id"), str(rs.getString("nama_institusi")), str(rs.getString("negara")),
                    str(rs.getString("area")), str(rs.getString("bentuk")),
                    rs.getDate("tgl_mulai") == null ? "" : rs.getString("tgl_mulai"),
                    rs.getDate("tgl_akhir") == null ? "" : rs.getString("tgl_akhir"),
                    str(rs.getString("output_kerja")), str(rs.getString("keterangan"))});
            }
        } catch (Exception e) { System.out.println("muatKerjasama: " + e); }
    }

    private void hapusKerjasama(widget.Table tb, DefaultTableModel km) {
        int r = tb.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus dulu."); return; }
        if (tb.isEditing()) tb.getCellEditor().stopCellEditing();
        int mr = tb.convertRowIndexToModel(r);
        Object id = km.getValueAt(mr, 0);
        if (JOptionPane.showConfirmDialog(this, "Hapus kerja sama ini?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        if (id != null && !id.toString().trim().isEmpty()) {
            try (PreparedStatement pst = koneksi.prepareStatement("DELETE FROM sirs_rl11_kerjasama_ln WHERE id=?")) {
                pst.setInt(1, Integer.parseInt(id.toString()));
                pst.executeUpdate();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Gagal menghapus :\n" + e); return; }
        }
        km.removeRow(mr);
    }

    private void simpanKerjasama(widget.Table tb, DefaultTableModel km) {
        if (tb.isEditing()) tb.getCellEditor().stopCellEditing();
        for (int r = 0; r < km.getRowCount(); r++) {
            if (str(km.getValueAt(r, 1)).trim().isEmpty() || str(km.getValueAt(r, 2)).trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Baris " + (r + 1) + ": Nama Institusi & Negara wajib diisi.");
                return;
            }
        }
        String ins = "INSERT INTO sirs_rl11_kerjasama_ln (nama_institusi, negara, area, bentuk, tgl_mulai, tgl_akhir, output_kerja, keterangan, created_at, updated_at) "
                + "VALUES (?,?,?,?,?,?,?,?,NOW(),NOW())";
        String upd = "UPDATE sirs_rl11_kerjasama_ln SET nama_institusi=?, negara=?, area=?, bentuk=?, tgl_mulai=?, tgl_akhir=?, output_kerja=?, keterangan=?, updated_at=NOW() WHERE id=?";
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            for (int r = 0; r < km.getRowCount(); r++) {
                Object id = km.getValueAt(r, 0);
                boolean baru = id == null || id.toString().trim().isEmpty();
                Object tmulai = str(km.getValueAt(r, 5)).trim().isEmpty() ? null : str(km.getValueAt(r, 5)).trim();
                Object takhir = str(km.getValueAt(r, 6)).trim().isEmpty() ? null : str(km.getValueAt(r, 6)).trim();
                try (PreparedStatement pst = koneksi.prepareStatement(baru ? ins : upd)) {
                    pst.setString(1, str(km.getValueAt(r, 1)).trim());
                    pst.setString(2, str(km.getValueAt(r, 2)).trim());
                    pst.setString(3, str(km.getValueAt(r, 3)).trim());
                    pst.setString(4, str(km.getValueAt(r, 4)).trim());
                    pst.setObject(5, tmulai);
                    pst.setObject(6, takhir);
                    pst.setString(7, str(km.getValueAt(r, 7)).trim());
                    pst.setString(8, str(km.getValueAt(r, 8)).trim());
                    if (!baru) pst.setInt(9, Integer.parseInt(id.toString()));
                    pst.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "Data Kerjasama LN berhasil disimpan.");
            muatKerjasama(km);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan :\n" + e);
            System.out.println("simpanKerjasama: " + e);
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void simpanModul(DefaultTableModel mm) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try (PreparedStatement pst = koneksi.prepareStatement(
                "UPDATE sirs_rl11_simrs_modul SET ada=?, updated_at=NOW() WHERE id=?")) {
            for (int r = 0; r < mm.getRowCount(); r++) {
                pst.setInt(1, Boolean.TRUE.equals(mm.getValueAt(r, 4)) ? 1 : 0);
                pst.setInt(2, Integer.parseInt(mm.getValueAt(r, 0).toString()));
                pst.addBatch();
            }
            pst.executeBatch();
            JOptionPane.showMessageDialog(this, "Checklist Modul SIMRS berhasil disimpan.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan modul :\n" + e);
            System.out.println("simpanModul: " + e);
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    // =========================================================================
    // TAB "Data Input" — tabel read-only berisi data yang sudah tersimpan.
    // =========================================================================
    private void refreshDataTab() {
        DefaultTableModel m;
        int[] w;
        if (mode == MODE_CHECKLIST) {
            m = viewModel(new String[]{"No.", "Kategori", "Sub Pelayanan", "Ada"});
            w = new int[]{40, 280, 320, 60};
            String sql = "SELECT kat_name, sub_name, ada FROM " + tabel + " ORDER BY kat_no, sub_no";
            try (PreparedStatement pst = koneksi.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {
                int no = 1;
                while (rs.next()) {
                    m.addRow(new Object[]{no++, rs.getString("kat_name"), rs.getString("sub_name"),
                        rs.getInt("ada") == 1 ? "Ya" : "Tidak"});
                }
            } catch (Exception e) { System.out.println("refreshDataTab checklist: " + e); }
        } else if (mode == MODE_ROWS) {
            m = viewModel(new String[]{"Kelas TT", "Ruangan", "Tersedia", "Terpakai", "Catatan"});
            w = new int[]{160, 220, 80, 80, 240};
            String sql = "SELECT kelas_tt, ruangan, jml_tersedia, jml_terpakai, catatan FROM "
                    + tabel + " ORDER BY kelas_tt, ruangan";
            try (PreparedStatement pst = koneksi.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    m.addRow(new Object[]{rs.getString("kelas_tt"), rs.getString("ruangan"),
                        rs.getInt("jml_tersedia"), rs.getInt("jml_terpakai"),
                        rs.getString("catatan") == null ? "" : rs.getString("catatan")});
                }
            } catch (Exception e) { System.out.println("refreshDataTab rows: " + e); }
        } else {
            m = viewModel(new String[]{"Field", "Nilai"});
            w = new int[]{260, 460};
            String sql = "SELECT * FROM " + tabel + " WHERE id=1";
            try (PreparedStatement pst = koneksi.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {
                boolean ada = rs.next();
                for (Col c : cols) {
                    String v = ada ? rs.getString(c.name) : null;
                    m.addRow(new Object[]{humanize(c.name), v == null ? "" : v});
                }
            } catch (Exception e) { System.out.println("refreshDataTab singleton: " + e); }
        }
        tbView.setModel(m);
        tbView.setDefaultRenderer(Object.class, new WarnaTable());
        tbView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < w.length && i < tbView.getColumnCount(); i++) {
            tbView.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        }
    }

    private DefaultTableModel viewModel(String[] kol) {
        return new DefaultTableModel(null, kol) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    /** Refresh tab data + pindah ke tab "Data Input" setelah menyimpan. */
    private void selesaiSimpan() {
        refreshDataTab();
        if (tab != null) tab.setSelectedIndex(1);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgSirsEntry d = new DlgSirsEntry(new javax.swing.JFrame(), true, "1.1");
            d.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) { System.exit(0); }
            });
            d.setVisible(true);
        });
    }
}
