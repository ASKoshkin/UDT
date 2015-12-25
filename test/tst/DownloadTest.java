/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tst;

import com.download.DownloadHTTP;
import com.download.DownloadHTTPS;
import com.download.FactoryDownload;
import com.download.IDownload;
import com.task.ITask;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.itadaki.bzip2.BZip2InputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import udt.ISettings;
import udt.SettingsXML;

/**
 *
 * @author Alexey
 */
public class DownloadTest {
    
    public DownloadTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
     public void helloTest() {
         
         ISettings settings = SettingsXML.getInstance();
         settings.ReadSettings();
         
         for( ITask task:settings.getTaskQue()){
             System.out.println(task.getName());
         }
     }
     
     //@Test
     public void SecondTest(){
         ISettings settings = SettingsXML.getInstance();
         settings.ReadSettings();
         
         IDownload dwnld = DownloadHTTPS.getInstance();
         
         for( ITask task:settings.getTaskQue()){
             System.out.println(task.getName());
             System.out.println(task.getPathConvert()+task.getFileDownload());
             dwnld.setTask(task);
             dwnld.run();
         }
         
     }
     
     //@Test
     public void FactoryDownloadTest(){
         ISettings settings = SettingsXML.getInstance();
         settings.ReadSettings();
         
         IDownload dwnld;
         
         FactoryDownload fdwnld = FactoryDownload.getInstance();
         for( ITask task:settings.getTaskQue()){
            
             //dwnld = fdwnld.getDownload(task);
             //boolean b = dwnld instanceof DownloadHTTPS;
             
           //  System.out.println("Сравнение: "+b);
         }
         
     }
     
     @Test
     public void CompressingTest(){
        
         FileInputStream in;
         int bytesRead;
         byte[] decoded = new byte [524288];
         FileOutputStream out = null;
         BZip2InputStream bzIn = null;
            
         try {
             in = new FileInputStream("C:\\import\\1.bz2");
             out = new FileOutputStream("C:\\import\\2.txt");
             bzIn = new BZip2InputStream(in, false);
                
             while ((bytesRead = bzIn.read (decoded)) != -1) {
                 out.write (decoded, 0, bytesRead) ;        
             }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DownloadTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DownloadTest.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
      
         
     }
     
}