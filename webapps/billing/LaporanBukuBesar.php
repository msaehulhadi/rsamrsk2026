<?php
 include '../conf/conf.php';
    $line1="ID\tProduct\tColor\tSales\t";
    $line2="1\tPrinter\tGrey\t13\t";
    $line3="2\tCD\tBlue\t15\t";
    $line4="3\tDVD\tRed\t7\t";
    $line5="4\tMonitor\tGreen\t4\t";
    $line6="5\tTelephone\tBlack\t2\t";

    $data="$line1\n$line2\n$line3\n$line4\n$line5\n$line6\n";

    header("Content-type: application/x-msdownload");
    header("Content-Disposition: attachment; filename=LaporanBukuBesar.xls");
    header("Pragma: no-cache");
    header("Expires: 0");
    print "$header\n$data";
?>
<html>
    <body>

    <?php
    reportsqlinjection();      
        $tanggal1      = $_GET['tanggal1']; 
        $tanggal2      = $_GET['tanggal2']; 
        $alamatip      = str_replace("_"," ",$_GET['alamatip']); 

        $_sql = "select temporary.temp1,temporary.temp2,temporary.temp3,temporary.temp4,temporary.temp5,temporary.temp6,temporary.temp7,temporary.temp8,temporary.temp9,temp10 from temporary where temporary.temp37='$alamatip' order by temporary.no";            
        $hasil=bukaquery($_sql);
        
        if(mysqli_num_rows($hasil)!=0) { 
          echo "<table width='100%'  border='1' align='left' cellpadding='0' cellspacing='0' class='tbl_form'>
                 <caption><br/><center><font color='000000' size='4' face='Arial'>Laporan Buku Besar</font></center><br/></caption>
                 <tr class=isi15>
                    <td width='5%' align=center>No.</td>
                    <td width='20%' align=center>Tanggal</td>
                    <td width='15%' align=center>No. Jurnal</td>
                    <td width='25%' align=center>No. Bukti</td>
                    <td width='25%' align=center>Keterangan</td>
                    <td width='10%' align=center>No. Akun</td>
                    <td width='10%' align=center>Akun Rekening</td>
                    <td width='10%' align=center>Saldo Awal</td>
                    <td width='10%' align=center>Debet</td>
                    <td width='10%' align=center>Kredit</td>
                    <td width='10%' align=center>Saldo Akhir</td>
                 </tr>";
                 $i=1;
                 while($baris = mysqli_fetch_array($hasil)) { 
					 echo "<tr>
					         <td>$i</td>
					         <td>$baris[temp1]</td>
					         <td>$baris[temp2]</td>
					         <td>$baris[temp3]</td>
					         <td>$baris[temp4]</td>
					         <td>$baris[temp5]</td>
					         <td>$baris[temp6]</td>
					         <td>$baris[temp7]</td>
					         <td>$baris[temp8]</td>
					         <td>$baris[temp9]</td>
					         <td>$baris[temp10]</td>
					       </tr>";$i++;
                 }    
                 echo "</table>";            
        } else {echo "<b>Data masih kosong !</b>";}
    ?>

    </body>
</html>
