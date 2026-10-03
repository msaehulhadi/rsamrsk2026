/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import AESsecurity.EnkripsiAES;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author khanzasoft
 */
public class koneksiDBAsterisk {
    private static Connection connection=null;
    private static final Properties prop = new Properties();  
    private static final MysqlDataSource dataSource=new MysqlDataSource();
    
    public koneksiDBAsterisk(){} 
    public static Connection condb(){ 
        if(connection == null){
            try{
                prop.loadFromXML(new FileInputStream("setting/database.xml"));
                dataSource.setURL("jdbc:mysql://"+EnkripsiAES.decrypt(prop.getProperty("SIP_SRV_HOST"))+":"+EnkripsiAES.decrypt(prop.getProperty("PORTASTERISK"))+"/"+EnkripsiAES.decrypt(prop.getProperty("DATABASEASTERISK"))+"?zeroDateTimeBehavior=convertToNull&amp;autoReconnect=true");
                dataSource.setUser(EnkripsiAES.decrypt(prop.getProperty("USERASTERISK")));
                dataSource.setPassword(EnkripsiAES.decrypt(prop.getProperty("PASSASTERISK")));
                connection=dataSource.getConnection();       
                System.out.println("  Koneksi Berhasil. Menyambungkan ke database asterisk...!!!");
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"Koneksi ke server local database asterisk terputus : "+e);
            }
        }
        return connection;        
    }
    
}
