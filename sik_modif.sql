SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE IF NOT EXISTS `adamlabs_orderlab`  (
  `noorder` varchar(20) NOT NULL,
  `no_laboratorium` varchar(30) NOT NULL,
  PRIMARY KEY (`noorder`, `no_laboratorium`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `adamlabs_request_response`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `noorder` varchar(20) NULL DEFAULT NULL,
  `url` varchar(255) NULL DEFAULT NULL,
  `method` varchar(5) NULL DEFAULT NULL,
  `request` text NULL DEFAULT NULL,
  `code` varchar(5) NULL DEFAULT NULL,
  `response` text NULL DEFAULT NULL,
  `pengirim` varchar(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `noorder`(`noorder`) USING BTREE,
  INDEX `url`(`url`) USING BTREE,
  INDEX `method`(`method`) USING BTREE,
  INDEX `code`(`code`) USING BTREE,
  INDEX `pengirim`(`pengirim`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `antriloketcetak_smc`  (
  `nomor` int(10) UNSIGNED NOT NULL,
  `tanggal` date NOT NULL,
  `jam` time NULL DEFAULT NULL,
  `jam_panggil` time NULL DEFAULT NULL,
  `no_rawat` varchar(17) NULL DEFAULT NULL,
  `no_rkm_medis` varchar(15) NULL DEFAULT NULL,
  PRIMARY KEY (`tanggal`, `nomor`) USING BTREE,
  INDEX `antriloketcetak_smc_jam_IDX`(`jam`) USING BTREE,
  INDEX `antriloketcetak_smc_tanggal_IDX`(`tanggal`) USING BTREE,
  INDEX `antriloketcetak_smc_no_rawat_IDX`(`no_rawat`) USING BTREE,
  INDEX `antriloketcetak_smc_no_rkm_medis_IDX`(`no_rkm_medis`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

ALTER TABLE `booking_operasi` ADD COLUMN IF NOT EXISTS `catatan` varchar(500) NULL DEFAULT NULL AFTER `kd_ruang_ok`;

ALTER TABLE `booking_operasi` ADD INDEX IF NOT EXISTS `booking_operasi_catatan_IDX`(`catatan`) USING BTREE;

ALTER TABLE `catatan_keseimbangan_cairan` MODIFY COLUMN IF EXISTS `infus` double NOT NULL DEFAULT 0 AFTER `jam_rawat`;

ALTER TABLE `catatan_keseimbangan_cairan` MODIFY COLUMN IF EXISTS `tranfusi` double NOT NULL DEFAULT 0 AFTER `infus`;

ALTER TABLE `catatan_keseimbangan_cairan` MODIFY COLUMN IF EXISTS `minum` double NOT NULL DEFAULT 0 AFTER `tranfusi`;

ALTER TABLE `catatan_keseimbangan_cairan` MODIFY COLUMN IF EXISTS `urine` double NOT NULL DEFAULT 0 AFTER `minum`;

ALTER TABLE `catatan_keseimbangan_cairan` MODIFY COLUMN IF EXISTS `drain` double NOT NULL DEFAULT 0 AFTER `urine`;

ALTER TABLE `catatan_keseimbangan_cairan` MODIFY COLUMN IF EXISTS `ngt` double NOT NULL DEFAULT 0 AFTER `drain`;

ALTER TABLE `catatan_keseimbangan_cairan` MODIFY COLUMN IF EXISTS `iwl` double NOT NULL DEFAULT 0 AFTER `ngt`;

ALTER TABLE `catatan_keseimbangan_cairan` MODIFY COLUMN IF EXISTS `keseimbangan` double NOT NULL DEFAULT 0 AFTER `iwl`;

ALTER TABLE `datasuplier` MODIFY COLUMN IF EXISTS `alamat` varchar(100) NULL DEFAULT NULL AFTER `nama_suplier`;

ALTER TABLE `datasuplier` MODIFY COLUMN IF EXISTS `kota` varchar(50) NULL DEFAULT NULL AFTER `alamat`;

ALTER TABLE `datasuplier` MODIFY COLUMN IF EXISTS `no_telp` varchar(20) NULL DEFAULT NULL AFTER `kota`;

ALTER TABLE `datasuplier` MODIFY COLUMN IF EXISTS `nama_bank` varchar(50) NULL DEFAULT NULL AFTER `no_telp`;

CREATE TABLE IF NOT EXISTS `detail_pemberian_obat_selanjutnya`  (
  `tgl_perawatan` date NOT NULL,
  `jam` time NOT NULL,
  `no_rkm_medis` varchar(17) NOT NULL,
  `kode_brng` varchar(15) NOT NULL,
  `tgl_pemberian_selanjutnya` date NULL DEFAULT NULL,
  `total_hari` varchar(100) NULL DEFAULT NULL,
  PRIMARY KEY (`tgl_perawatan`, `jam`, `no_rkm_medis`, `kode_brng`) USING BTREE,
  INDEX `detail_pemberian_obat_selanjutnya_databarang_FK`(`kode_brng`) USING BTREE,
  INDEX `detail_pemberian_obat_selanjutnya_pasien_FK`(`no_rkm_medis`) USING BTREE,
  CONSTRAINT `detail_pemberian_obat_selanjutnya_databarang_FK` FOREIGN KEY (`kode_brng`) REFERENCES `databarang` (`kode_brng`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `detail_pemberian_obat_selanjutnya_pasien_FK` FOREIGN KEY (`no_rkm_medis`) REFERENCES `pasien` (`no_rkm_medis`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

ALTER TABLE `detail_penagihan_piutang` ADD COLUMN IF NOT EXISTS `diskon` double NULL DEFAULT NULL AFTER `sisapiutang`;

ALTER TABLE `detail_periksa_lab` MODIFY COLUMN IF EXISTS `nilai` varchar(700) NOT NULL AFTER `id_template`;

ALTER TABLE `detail_periksa_lab` MODIFY COLUMN IF EXISTS `nilai_rujukan` varchar(700) NOT NULL AFTER `nilai`;

ALTER TABLE `dokter` MODIFY COLUMN IF EXISTS `nm_dokter` varchar(80) NULL DEFAULT NULL AFTER `kd_dokter`;

ALTER TABLE `dokter` MODIFY COLUMN IF EXISTS `almt_tgl` varchar(100) NULL DEFAULT NULL AFTER `agama`;

CREATE TABLE IF NOT EXISTS `dokter_ttdbasah`  (
  `kd_dokter` varchar(20) NOT NULL,
  `gambar_ttd` longblob NULL DEFAULT NULL,
  PRIMARY KEY (`kd_dokter`),
  CONSTRAINT `dokter_ttdbasah_ibfk_1` FOREIGN KEY (`kd_dokter`) REFERENCES `dokter` (`kd_dokter`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `eklaim_icd10`  (
  `code` varchar(7) NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` tinyint(3) UNSIGNED NULL DEFAULT 1,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

ALTER TABLE `emergency_index` MODIFY COLUMN IF EXISTS `nama_emergency` varchar(200) NULL DEFAULT NULL AFTER `kode_emergency`;

CREATE TABLE IF NOT EXISTS `inacbg_cetak_klaim`  (
  `no_sep` varchar(40) NOT NULL,
  `path` varchar(100) NULL DEFAULT NULL,
  PRIMARY KEY (`no_sep`) USING BTREE,
  CONSTRAINT `inacbg_cetak_klaim_bridging_sep_FK` FOREIGN KEY (`no_sep`) REFERENCES `bridging_sep` (`no_sep`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

ALTER TABLE `industrifarmasi` MODIFY COLUMN IF EXISTS `alamat` varchar(200) NULL DEFAULT NULL AFTER `nama_industri`;

ALTER TABLE `industrifarmasi` MODIFY COLUMN IF EXISTS `kota` varchar(30) NULL DEFAULT NULL AFTER `alamat`;

ALTER TABLE `ipsrssuplier` MODIFY COLUMN IF EXISTS `alamat` varchar(100) NULL DEFAULT NULL AFTER `nama_suplier`;

ALTER TABLE `ipsrssuplier` MODIFY COLUMN IF EXISTS `kota` varchar(50) NULL DEFAULT NULL AFTER `alamat`;

ALTER TABLE `ipsrssuplier` MODIFY COLUMN IF EXISTS `no_telp` varchar(20) NULL DEFAULT NULL AFTER `kota`;

ALTER TABLE `ipsrssuplier` MODIFY COLUMN IF EXISTS `nama_bank` varchar(50) NULL DEFAULT NULL AFTER `no_telp`;

ALTER TABLE `jns_perawatan_inap` MODIFY COLUMN IF EXISTS `nm_perawatan` varchar(200) NULL DEFAULT NULL AFTER `kd_jenis_prw`;

CREATE TABLE IF NOT EXISTS `mapping_pemeriksaan_labpk`  (
  `id_pemeriksaan` int(10) UNSIGNED NOT NULL,
  `id_template` int(11) NOT NULL,
  PRIMARY KEY (`id_pemeriksaan`, `id_template`) USING BTREE,
  INDEX `id_template`(`id_template`) USING BTREE,
  CONSTRAINT `mapping_pemeriksaan_labpk_ibfk_1` FOREIGN KEY (`id_pemeriksaan`) REFERENCES `pemeriksaan_labpk` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `mapping_pemeriksaan_labpk_ibfk_2` FOREIGN KEY (`id_template`) REFERENCES `template_laboratorium` (`id_template`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `mapping_user_bridginglab`  (
  `nip` varchar(20) NOT NULL,
  `username` varchar(100) NOT NULL,
  `vendor` varchar(100) NOT NULL,
  PRIMARY KEY (`nip`, `username`, `vendor`) USING BTREE,
  CONSTRAINT `mapping_user_bridginglab_petugas_fk` FOREIGN KEY (`nip`) REFERENCES `petugas` (`nip`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

ALTER TABLE `master_berkas_digital` ADD COLUMN IF NOT EXISTS `include_kompilasi_berkas` tinyint(1) NOT NULL DEFAULT 1 AFTER `nama`;

ALTER TABLE `pasien` MODIFY COLUMN IF EXISTS `nm_pasien` varchar(60) NULL DEFAULT NULL AFTER `no_rkm_medis`;

ALTER TABLE `pasien` MODIFY COLUMN IF EXISTS `tmp_lahir` varchar(30) NULL DEFAULT NULL AFTER `jk`;

ALTER TABLE `pasien` MODIFY COLUMN IF EXISTS `nm_ibu` varchar(60) NOT NULL AFTER `tgl_lahir`;

CREATE DEFINER = `root`@`%` TRIGGER IF NOT EXISTS `set_password_user` AFTER INSERT ON `pasien` FOR EACH ROW insert into personal_pasien values(
  new.no_rkm_medis,
  '',
  aes_encrypt(date_format(new.tgl_lahir, '%d%m%Y'), 'windi')
);

ALTER TABLE `pegawai` MODIFY COLUMN IF EXISTS `nama` varchar(100) NOT NULL AFTER `nik`;

ALTER TABLE `pegawai` MODIFY COLUMN IF EXISTS `alamat` varchar(150) NOT NULL AFTER `tgl_lahir`;

CREATE TABLE IF NOT EXISTS `pemeriksaan_labpk`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `kode_pemeriksaan` varchar(20) NOT NULL,
  `nama_pemeriksaan` varchar(200) NOT NULL,
  `satuan` varchar(15) NOT NULL,
  `metode` varchar(50) NULL DEFAULT NULL,
  `kategori` varchar(50) NOT NULL,
  `urut` int(10) UNSIGNED NULL DEFAULT NULL,
  `vendor` varchar(15) NOT NULL DEFAULT '',
  `kode_compound` varchar(255) GENERATED ALWAYS AS (concat_ws('-',`kode_pemeriksaan`,`kategori`,`vendor`)) VIRTUAL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `kode_pemeriksaan`(`kode_pemeriksaan`) USING BTREE,
  INDEX `nama_pemeriksaan`(`nama_pemeriksaan`) USING BTREE,
  INDEX `urut`(`urut`) USING BTREE,
  INDEX `vendor`(`vendor`) USING BTREE,
  INDEX `pemeriksaan_labpk_pemeriksaan_labpk_kategori_FK`(`kategori`) USING BTREE,
  INDEX `kode_compund`(`kode_compound`) USING BTREE,
  CONSTRAINT `pemeriksaan_labpk_pemeriksaan_labpk_kategori_FK` FOREIGN KEY (`kategori`) REFERENCES `pemeriksaan_labpk_kategori` (`nama`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `pemeriksaan_labpk_kategori`  (
  `nama` varchar(50) NOT NULL,
  `urut` int(10) UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`nama`) USING BTREE,
  INDEX `urut_idx`(`urut`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

ALTER TABLE `pengeluaran_harian` MODIFY COLUMN IF EXISTS `keterangan` varchar(250) NOT NULL DEFAULT '' AFTER `nip`;

ALTER TABLE `penilaian_awal_keperawatan_ranap` MODIFY COLUMN IF EXISTS `rpd` varchar(300) NOT NULL AFTER `rps`;

ALTER TABLE `penilaian_awal_keperawatan_ranap_neonatus` MODIFY COLUMN IF EXISTS `gd_ibu` enum('A +','A -','B +','B -','AB +','AB -','O +','O -','-') NOT NULL AFTER `gd_bayi`;

ALTER TABLE `penilaian_awal_keperawatan_ranap_neonatus` MODIFY COLUMN IF EXISTS `gd_ayah` enum('A +','A -','B +','B -','AB +','AB -','O +','O -','-') NOT NULL AFTER `gd_ibu`;

ALTER TABLE `penjab` MODIFY COLUMN IF EXISTS `png_jawab` varchar(50) NOT NULL AFTER `kd_pj`;

ALTER TABLE `penjab` MODIFY COLUMN IF EXISTS `nama_perusahaan` varchar(100) NOT NULL AFTER `png_jawab`;

CREATE TABLE IF NOT EXISTS `referensi_mobilejkn_bpjs_taskid_response`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `no_rawat` varchar(17) NULL DEFAULT NULL,
  `jenispasien` enum('JKN','NON JKN') NULL DEFAULT NULL,
  `taskid` char(2) NULL DEFAULT NULL,
  `code` varchar(5) NULL DEFAULT NULL,
  `message` varchar(200) NULL DEFAULT NULL,
  `waktu` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `referensi_mobilejkn_bpjs_taskid_response_no_rawat_IDX`(`no_rawat`) USING BTREE,
  INDEX `referensi_mobilejkn_bpjs_taskid_response_waktu_IDX`(`waktu`) USING BTREE,
  INDEX `referensi_mobilejkn_bpjs_taskid_response_taskid_IDX`(`taskid`) USING BTREE,
  INDEX `referensi_mobilejkn_bpjs_taskid_response_code_IDX`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `referensi_mobilejkn_bpjs_taskid_response2`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `no_rawat` varchar(17) NOT NULL,
  `kodebooking` varchar(17) NULL DEFAULT NULL,
  `jenispasien` enum('MobileJKN','Onsite') NULL DEFAULT NULL,
  `taskid` enum('addantrean','batalantrean','addantreanfarmasi','1','2','3','4','5','6','7','99') NULL DEFAULT NULL,
  `request` varchar(5000) NULL DEFAULT NULL,
  `code` varchar(5) NULL DEFAULT NULL,
  `message` varchar(200) NULL DEFAULT NULL,
  `response` varchar(5000) NULL DEFAULT NULL,
  `waktu` datetime NULL DEFAULT NULL,
  `waktu_rs` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `referensi_mobilejkn_bpjs_taskid_response_no_rawat_IDX`(`no_rawat`) USING BTREE,
  INDEX `referensi_mobilejkn_bpjs_taskid_response_waktu_IDX`(`waktu`) USING BTREE,
  INDEX `referensi_mobilejkn_bpjs_taskid_response_kodebooking_IDX`(`kodebooking`) USING BTREE,
  INDEX `referensi_mobilejkn_bpjs_taskid_response_jenispasien_IDX`(`jenispasien`) USING BTREE,
  INDEX `referensi_mobilejkn_bpjs_taskid_response_taskid_IDX`(`taskid`) USING BTREE,
  INDEX `referensi_mobilejkn_bpjs_taskid_response_code_IDX`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

ALTER TABLE `resep_obat` ADD COLUMN IF NOT EXISTS `nama_template` varchar(100) NULL DEFAULT NULL AFTER `jam_penyerahan`;

ALTER TABLE `resiko_kerja` MODIFY COLUMN IF EXISTS `nama_resiko` varchar(200) NULL DEFAULT NULL AFTER `kode_resiko`;

ALTER TABLE `resume_pasien` MODIFY COLUMN IF EXISTS `diagnosa_utama` varchar(200) NOT NULL AFTER `hasil_laborat`;

ALTER TABLE `resume_pasien` MODIFY COLUMN IF EXISTS `diagnosa_sekunder` varchar(200) NOT NULL AFTER `kd_diagnosa_utama`;

ALTER TABLE `resume_pasien` MODIFY COLUMN IF EXISTS `diagnosa_sekunder2` varchar(200) NOT NULL AFTER `kd_diagnosa_sekunder`;

ALTER TABLE `resume_pasien` MODIFY COLUMN IF EXISTS `diagnosa_sekunder3` varchar(200) NOT NULL AFTER `kd_diagnosa_sekunder2`;

ALTER TABLE `resume_pasien` MODIFY COLUMN IF EXISTS `diagnosa_sekunder4` varchar(200) NOT NULL AFTER `kd_diagnosa_sekunder3`;

ALTER TABLE `resume_pasien` MODIFY COLUMN IF EXISTS `prosedur_utama` varchar(200) NOT NULL AFTER `kd_diagnosa_sekunder4`;

ALTER TABLE `resume_pasien` MODIFY COLUMN IF EXISTS `prosedur_sekunder` varchar(200) NOT NULL AFTER `kd_prosedur_utama`;

ALTER TABLE `resume_pasien` MODIFY COLUMN IF EXISTS `prosedur_sekunder2` varchar(200) NOT NULL AFTER `kd_prosedur_sekunder`;

ALTER TABLE `resume_pasien` MODIFY COLUMN IF EXISTS `prosedur_sekunder3` varchar(200) NOT NULL AFTER `kd_prosedur_sekunder2`;

ALTER TABLE `resume_pasien_ranap` MODIFY COLUMN IF EXISTS `diagnosa_utama` varchar(200) NOT NULL AFTER `obat_di_rs`;

ALTER TABLE `resume_pasien_ranap` MODIFY COLUMN IF EXISTS `diagnosa_sekunder` varchar(200) NOT NULL AFTER `kd_diagnosa_utama`;

ALTER TABLE `resume_pasien_ranap` MODIFY COLUMN IF EXISTS `diagnosa_sekunder2` varchar(200) NOT NULL AFTER `kd_diagnosa_sekunder`;

ALTER TABLE `resume_pasien_ranap` MODIFY COLUMN IF EXISTS `diagnosa_sekunder3` varchar(200) NOT NULL AFTER `kd_diagnosa_sekunder2`;

ALTER TABLE `resume_pasien_ranap` MODIFY COLUMN IF EXISTS `diagnosa_sekunder4` varchar(200) NOT NULL AFTER `kd_diagnosa_sekunder3`;

ALTER TABLE `resume_pasien_ranap` MODIFY COLUMN IF EXISTS `prosedur_utama` varchar(200) NOT NULL AFTER `kd_diagnosa_sekunder4`;

ALTER TABLE `resume_pasien_ranap` MODIFY COLUMN IF EXISTS `prosedur_sekunder` varchar(200) NOT NULL AFTER `kd_prosedur_utama`;

ALTER TABLE `resume_pasien_ranap` MODIFY COLUMN IF EXISTS `prosedur_sekunder2` varchar(200) NOT NULL AFTER `kd_prosedur_sekunder`;

ALTER TABLE `resume_pasien_ranap` MODIFY COLUMN IF EXISTS `prosedur_sekunder3` varchar(200) NOT NULL AFTER `kd_prosedur_sekunder2`;

ALTER TABLE `saran_kesan_lab` MODIFY COLUMN IF EXISTS `saran` varchar(1000) NULL DEFAULT NULL AFTER `jam`;

ALTER TABLE `saran_kesan_lab` MODIFY COLUMN IF EXISTS `kesan` varchar(1000) NULL DEFAULT NULL AFTER `saran`;

ALTER TABLE `satu_sehat_mapping_obat` MODIFY COLUMN IF EXISTS `obat_display` varchar(500) NULL DEFAULT NULL AFTER `obat_system`;

CREATE TABLE IF NOT EXISTS `satu_sehat_referensi_denominator`  (
  `code` varchar(30) NOT NULL,
  `display` varchar(100) NULL DEFAULT NULL,
  `definition` varchar(400) NULL DEFAULT NULL,
  `status` varchar(10) NULL DEFAULT NULL,
  `system` varchar(100) NOT NULL,
  PRIMARY KEY (`code`, `system`) USING BTREE,
  INDEX `satu_sehat_referensi_denominator_obat_display_ibfk_1`(`display`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `satu_sehat_referensi_lab_loinc`  (
  `code` varchar(30) NOT NULL,
  `system` varchar(100) NOT NULL,
  `display` varchar(300) NULL DEFAULT NULL,
  `display_ind` varchar(300) NULL DEFAULT NULL,
  PRIMARY KEY (`code`, `system`) USING BTREE,
  INDEX `display`(`display`) USING BTREE,
  INDEX `display_ind`(`display_ind`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `satu_sehat_referensi_lab_snomed`  (
  `code` varchar(30) NOT NULL,
  `system` varchar(100) NOT NULL,
  `display` varchar(300) NULL DEFAULT NULL,
  `display_ind` varchar(300) NULL DEFAULT NULL,
  PRIMARY KEY (`code`, `system`) USING BTREE,
  INDEX `display`(`display`) USING BTREE,
  INDEX `display_ind`(`display_ind`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `satu_sehat_referensi_numerator`  (
  `code` varchar(30) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `display` varchar(200) NULL DEFAULT NULL,
  `system` varchar(100) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`code`, `system`) USING BTREE,
  INDEX `satu_sehat_referensi_numerator_obat_display_ibfk_1`(`display`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `satu_sehat_referensi_patient`  (
  `no_ktp` varchar(20) NOT NULL,
  `patient_ihs_number` varchar(20) NOT NULL,
  PRIMARY KEY (`no_ktp`, `patient_ihs_number`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `satu_sehat_referensi_practitioneer`  (
  `no_ktp` varchar(20) NOT NULL,
  `practition_his_number` varchar(20) NOT NULL,
  PRIMARY KEY (`no_ktp`, `practition_his_number`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `satu_sehat_referensi_radiologi_loinc`  (
  `code` varchar(30) NOT NULL,
  `system` varchar(100) NOT NULL,
  `display` varchar(300) NULL DEFAULT NULL,
  `display_ind` varchar(300) NULL DEFAULT NULL,
  PRIMARY KEY (`code`, `system`) USING BTREE,
  INDEX `display`(`display`) USING BTREE,
  INDEX `display_ind`(`display_ind`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `satu_sehat_referensi_radiologi_snomed`  (
  `code` varchar(30) NOT NULL,
  `system` varchar(100) NOT NULL,
  `display` varchar(300) NULL DEFAULT NULL,
  `display_ind` varchar(300) NULL DEFAULT NULL,
  PRIMARY KEY (`code`, `system`) USING BTREE,
  INDEX `display`(`display`) USING BTREE,
  INDEX `display_ind`(`display_ind`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `satu_sehat_referensi_route`  (
  `code` varchar(30) NOT NULL,
  `display` varchar(100) NULL DEFAULT NULL,
  `keterangan` text NULL DEFAULT NULL,
  `system` varchar(100) NOT NULL,
  PRIMARY KEY (`code`, `system`) USING BTREE,
  INDEX `satu_sehat_referensi_route_display_ibfk_1`(`display`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

ALTER TABLE `set_validasi_registrasi` MODIFY COLUMN IF EXISTS `wajib_closing_kasir` enum('Yes','Peringatan di hari yang sama','No') NULL DEFAULT NULL FIRST;

CREATE TABLE IF NOT EXISTS `set_akses_edit_sementara`  (
  `id_user` varchar(700) NOT NULL,
  `tgl_selesai` datetime NOT NULL,
  PRIMARY KEY (`id_user`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

ALTER TABLE `setting` ADD COLUMN IF NOT EXISTS `pemberlakuan_2x24_jam` enum('Yes','No') NULL DEFAULT NULL AFTER `logo`;

ALTER TABLE `surat_keterangan_rawat_inap` ADD COLUMN IF NOT EXISTS `kd_dokter` varchar(20) NOT NULL AFTER `tanggalakhir`;

ALTER TABLE `surat_keterangan_rawat_inap` ADD COLUMN IF NOT EXISTS `lamasakit` varchar(20) NULL DEFAULT NULL AFTER `kd_dokter`;

ALTER TABLE `surat_keterangan_rawat_inap` ADD CONSTRAINT `surat_keterangan_rawat_inap_dokter_FK` FOREIGN KEY IF NOT EXISTS (`kd_dokter`) REFERENCES `dokter` (`kd_dokter`) ON DELETE NO ACTION ON UPDATE CASCADE;

ALTER TABLE `surat_keterangan_rawat_inap` ADD INDEX IF NOT EXISTS `surat_keterangan_rawat_inap_dokter_FK`(`kd_dokter`) USING BTREE;

ALTER TABLE `suratsakitpihak2` ADD PRIMARY KEY IF NOT EXISTS (`no_surat`) USING BTREE;

ALTER TABLE `suratsakitpihak2` MODIFY COLUMN IF EXISTS `no_surat` varchar(20) NOT NULL FIRST;

ALTER TABLE `suratsakitpihak2` MODIFY COLUMN IF EXISTS `hubungan` enum('Suami','Istri','Anak','Ayah','Ibu','Saudara','Keponakan') NOT NULL AFTER `alamat`;

CREATE TABLE IF NOT EXISTS `tampjurnal_rvpbpjs`  (
  `kd_rek` char(15) NOT NULL,
  `nm_rek` varchar(100) NULL DEFAULT NULL,
  `debet` double NOT NULL,
  `kredit` double NOT NULL,
  PRIMARY KEY (`kd_rek`) USING HASH,
  INDEX `nm_rek`(`nm_rek`) USING HASH,
  INDEX `debet`(`debet`) USING HASH,
  INDEX `kredit`(`kredit`) USING HASH
) ENGINE = MEMORY CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Fixed;

CREATE TABLE IF NOT EXISTS `tampjurnal_smc`  (
  `kd_rek` char(15) NOT NULL,
  `nm_rek` varchar(100) NULL DEFAULT NULL,
  `debet` double NOT NULL,
  `kredit` double NOT NULL,
  `user_id` varchar(20) NOT NULL,
  `ip` varchar(25) NOT NULL,
  PRIMARY KEY (`kd_rek`, `user_id`, `ip`) USING HASH
) ENGINE = MEMORY CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Fixed;

CREATE TABLE IF NOT EXISTS `tempinacbg`  (
  `coder_nik` varchar(20) NOT NULL,
  `cmg_code` varchar(10) NOT NULL,
  `cmg_description` varchar(100) NULL DEFAULT NULL,
  `cmg_type` varchar(50) NULL DEFAULT NULL,
  PRIMARY KEY (`coder_nik`, `cmg_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

ALTER TABLE `transfer_pasien_antar_ruang` MODIFY COLUMN IF EXISTS `diagnosa_utama` varchar(100) NULL DEFAULT NULL AFTER `ruang_selanjutnya`;

SET FOREIGN_KEY_CHECKS=1;
