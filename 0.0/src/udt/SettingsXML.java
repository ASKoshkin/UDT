/*
 * Класс чтения настроек из xml файла
 * 
 */
package udt;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author koshkin_as
 * 
 */
public class SettingsXML extends ASettings{
    
    /**
     * Название файла с настройками
     */
    protected final String SETTING_FILE_NAME ="Settings.xml";
    
    
    private SettingsXML() {
    }
    
    private static class SettingsXMLHolder {
        private static final SettingsXML INSTANCE = new SettingsXML();
    }
    
    public static SettingsXML getInstance() {
        return SettingsXMLHolder.INSTANCE;
    }

    
    @Override
    public boolean ReadSettings() {
        if (!CheckFile()){
            System.out.println("Не найден файл: "+SETTING_FILE_NAME);
            return false;
       }
        String s;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
       
        try {
            
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document parse = db.parse(SETTING_FILE_NAME);
            Element elem = parse.getDocumentElement();
            
            if (TableUpdate == TableEnum.Passport){
                if (elem.getElementsByTagName("url_passport").getLength()<1){
                    System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент url_passport");
                    return false;
                } else {
                    url=elem.getElementsByTagName("url_passport").item(0).getTextContent();
                }
            } else if (TableUpdate == TableEnum.Ofac){
                if (elem.getElementsByTagName("url_ofac").getLength()<1){
                    System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент url_ofac");
                    return false;
                } else {
                    url=elem.getElementsByTagName("url_ofac").item(0).getTextContent();
                }
            }
                
            if (TableUpdate == TableEnum.Passport){
                if (elem.getElementsByTagName("File_arh_passport").getLength()<1){
                    System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент File_arh_passport");
                    return false;
                } else {
                    FileArhive = elem.getElementsByTagName("File_arh_passport").item(0).getTextContent();
                }
            } else if (TableUpdate == TableEnum.Ofac){
                if (elem.getElementsByTagName("File_arh_ofac").getLength()<1){
                    System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент File_arh_ofac");
                    return false;
                } else {
                    FileArhive = elem.getElementsByTagName("File_arh_ofac").item(0).getTextContent();
                }
            }
            
            if (TableUpdate == TableEnum.Passport){
                if (elem.getElementsByTagName("File_passport").getLength()<1){
                    System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент File_passport");
                    return false;
                } else {
                    File = elem.getElementsByTagName("File_passport").item(0).getTextContent();
                }
            } else if (TableUpdate == TableEnum.Ofac){
                if (elem.getElementsByTagName("File_ofac").getLength()<1){
                    System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент ofac");
                    return false;
                } else {
                    File = elem.getElementsByTagName("File_ofac").item(0).getTextContent();
                }
            }
                            
            if (TableUpdate == TableEnum.Passport){
                if (elem.getElementsByTagName("File_Converted_passport").getLength()<1){
                    System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент File_Converted_passport");
                    return false;
                } else {
                    FileConverted = elem.getElementsByTagName("File_Converted_passport").item(0).getTextContent();
                }
            } else if (TableUpdate == TableEnum.Ofac){
                if (elem.getElementsByTagName("File_Converted_ofac").getLength()<1){
                    System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент File_Converted_ofac");
                    return false;
                } else {
                    FileConverted = elem.getElementsByTagName("File_Converted_ofac").item(0).getTextContent();
                }
            }            
            
            if (elem.getElementsByTagName("Download").getLength()<1){
                System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент Download");
                return false;
            } else {
                s = elem.getElementsByTagName("Download").item(0).getTextContent();
                if (s.toUpperCase().equals("TRUE")){
                    Download = true;
                } else {
                    Download = false;
                }
            }
                        
            if (elem.getElementsByTagName("AutorizSQL").getLength()<1){
                System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент AutorizSQL");
                return false;
            } else {
                s = elem.getElementsByTagName("AutorizSQL").item(0).getTextContent();
                if (s.toUpperCase().equals("TRUE")){
                    AutorizSQL = true;
                } else {
                    AutorizSQL = false;
                }
            }
            
            if (elem.getElementsByTagName("user").getLength()<1){
                System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент user");
                return false;
            } else {
                User = elem.getElementsByTagName("user").item(0).getTextContent();
            }
            
            if (elem.getElementsByTagName("pw").getLength()<1){
                System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент pw");
                return false;
            } else {
                PW = elem.getElementsByTagName("pw").item(0).getTextContent();
            }
            
            if (elem.getElementsByTagName("server").getLength()<1){
                System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент server");
                return false;
            } else {
                Server = elem.getElementsByTagName("server").item(0).getTextContent();
            }
            
            if (elem.getElementsByTagName("DataBase").getLength()<1){
                System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент DataBase");
                return false;
            } else {
                DataBase = elem.getElementsByTagName("DataBase").item(0).getTextContent();
            }
            
            if (TableUpdate == TableEnum.Passport){
                if (elem.getElementsByTagName("Table_Passport").getLength()<1){
                    System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент Table_Passport");
                    return false;
                } else {
                    Table = elem.getElementsByTagName("Table_Passport").item(0).getTextContent();
                }
            } else if    (TableUpdate == TableEnum.Ofac){
                if (elem.getElementsByTagName("Table_Ofac").getLength()<1){
                    System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент Table_Ofac");
                    return false;
                } else {
                    Table = elem.getElementsByTagName("Table_Ofac").item(0).getTextContent();
                }
            }
            
            if (elem.getElementsByTagName("Path_Converted").getLength()<1){
                System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент Path_Converted");
                return false;
            } else {
                PathConvert = elem.getElementsByTagName("Path_Converted").item(0).getTextContent();
            }

            if (elem.getElementsByTagName("ProxyHost").getLength()<1){
                System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент ProxyHost");
                return false;
            } else {
                ProxyHost = elem.getElementsByTagName("ProxyHost").item(0).getTextContent();
            }
            
            if (elem.getElementsByTagName("ProxyPort").getLength()<1){
                System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент ProxyPort");
                return false;
            } else {
                ProxyPort = elem.getElementsByTagName("ProxyPort").item(0).getTextContent();
            }
            
            if (elem.getElementsByTagName("Log").getLength()<1){
                System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент Log");
                return false;
            } else {
                s = elem.getElementsByTagName("Log").item(0).getTextContent();                
                if (s.toUpperCase().equals("TRUE")){
                    Log = true;                    
                } else {
                    Log = false;
                }
            }
            
           if (elem.getElementsByTagName("Path_Log").getLength()<1){
                System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент Path_Log");
                return false;
            } else {
                Path_Log=elem.getElementsByTagName("Path_Log").item(0).getTextContent();
            }
           
            if (elem.getElementsByTagName("ReCreateIndex").getLength()<1){
                System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент ReCreateIndex");
                return false;
            } else {
                s = elem.getElementsByTagName("ReCreateIndex").item(0).getTextContent();
                if (s.toUpperCase().equals("TRUE")){
                    ReCreateIndex = true;
                } else {
                    ReCreateIndex = false;
                }
            }
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SettingsXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(SettingsXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SettingsXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

    /**
     * Проверка существования файла с настройками
     * @return Возвращает true если файл существует, иначе false
     */
    @Override
    public boolean CheckFile() {
        
        boolean Res;      
        File f;
        f = new File(SETTING_FILE_NAME);
        Res = f.exists();
        return Res;
    }
    
    
        
}
