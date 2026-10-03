/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fungsi;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author me
 */
public class formatAngka {
    
    public static String ribuan(String input) {
        try {
            long angka = Long.parseLong(input);

            DecimalFormatSymbols simbol = new DecimalFormatSymbols();
            simbol.setGroupingSeparator('.');
            simbol.setDecimalSeparator(',');

            DecimalFormat formatter = new DecimalFormat("#,###", simbol);
            return formatter.format(angka);

        } catch (NumberFormatException e) {
            return "";
        }
    }
    
    public static long angkaString2Long(String angka) {
        if (angka == null || angka.trim().isEmpty()) {
            return 0;
        }
        try {
            return Long.parseLong(angka.replace(".", "").trim());
        } catch (NumberFormatException e) {
            return 0; // atau throw jika ingin error
        }
    }
    
    public static String getDateNow() {
        LocalDate tgl = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return tgl.format(formatter);
    }
    
    public static String getTanggalSekarang() {
        LocalDate tgl = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return tgl.format(formatter);
    }
    
    public static String getJamSekarang() {
        LocalTime jam = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return jam.format(formatter);
    }

}
