/*
 * Класс чтения настроек из xml файла
 * 
 */
package udt;

import com.task.ITask;
import com.task.TaskImpl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
    private Queue<ITask> TASK_QUEUE;
    
    private SettingsXML() {
        TASK_QUEUE = new ArrayDeque();
    }


    @Override
    public Queue<ITask> getTaskQue() {
        return TASK_QUEUE;
    }

    
    
    private static class SettingsXMLHolder {
        private static final SettingsXML INSTANCE = new SettingsXML();
    }
    
    public static synchronized SettingsXML getInstance() {
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
            
            ProxyHost=getStringElement(elem,"ProxyHost");
            ProxyPort=getStringElement(elem,"ProxyPort");
            Path_Log=getStringElement(elem,"Path_Log");
            Log = getBooleanElement(elem,"Log");
            BCP = getStringElement(elem,"BCP");
            
            NodeList nList = parse.getElementsByTagName("Task");
            for (int num =0; num<nList.getLength();num++){
                Node node = nList.item(num);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    Map<String,String> map = new HashMap();
                    TaskImpl task = new TaskImpl();
                    
                    for (int num_arr =0; num_arr<task.PARAMETR_ARR.length;num_arr++){
                        map.put(task.PARAMETR_ARR[num_arr],getStringElement(element,task.PARAMETR_ARR[num_arr]));
                    }
                    
                    /*установили свойства для задачи через map*/
                    task.setSettingsMap(map);
                    
                    TASK_QUEUE.add(task);
                    
                }
            }
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SettingsXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(SettingsXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SettingsXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(SettingsXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    /**
     * Возвращает строкове значение
     */
    private String getStringElement(Element elem,String param){
        if (elem.getElementsByTagName(param).getLength()<1){
            System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент "+param);
            throw new IllegalArgumentException("В файле "+SETTING_FILE_NAME+" не найден элемент "+param);
        } else {
            return elem.getElementsByTagName(param).item(0).getTextContent();
        }        
    }
    
    /**
     * Возвращает булево значение
     * 
     * 
     */
    private boolean getBooleanElement(Element elem,String param){
        String s;
        if (elem.getElementsByTagName(param).getLength()<1){
            System.out.println("В файле "+SETTING_FILE_NAME+" не найден элемент "+param);
            throw new IllegalArgumentException("В файле "+SETTING_FILE_NAME+" не найден элемент "+param);
        } else {
            s = elem.getElementsByTagName("Log").item(0).getTextContent();                
            if (s.toUpperCase().equals("TRUE")){
                return true;                    
            } else {
                return false;
            }
        }
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
