/**
 * 
 * udt - updating data in the table
 * 
 */
package udt;

import com.Report.ReportHTML;
import com.Report.IReport;
import com.task.ITask;
import java.util.Date;
import java.util.Queue;

/**
 *
 * @author koshkin_as
 */
public class UDT {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        SettingsXML sxml = SettingsXML.getInstance();
            
        if (!sxml.ReadSettings()){
            // Попытка чтения настроек была неуспешной, выйду.
            System.exit(0);
        }
        
        IReport rprt = ReportHTML.getInstance();
        rprt.setDateStart(new Date());
        
        Queue<ITask> taskque =sxml.getTaskQue();
        
        /*обрабатываем очередь, запускаем все в потоки, FIFO*/
        while (!taskque.isEmpty()){
            
            ITask task = taskque.remove();            
            IProcess process = new ProcessImpl(task);
            Thread myThread = new Thread((Runnable) process);
            myThread.start();
            System.out.println("запущена задача: "+task.getName());
        }
        
        
        
    }
    
}
