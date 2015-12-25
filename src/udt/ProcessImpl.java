/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package udt;

import com.Report.IReport;
import com.Report.ReportData;
import com.Report.ReportHTML;
import com.compressing.FactoryUnCompressing;
import com.compressing.IUnCompressing;
import com.convert.FactoryConvert;
import com.convert.IConvert;
import com.dbwork.FactoryConnection;
import com.dbwork.IConnection;
import com.download.FactoryDownload;
import com.download.IDownload;
import com.task.ITask;

/**
 *
 * @author Alexey
 */
public class ProcessImpl implements Runnable, IProcess{

    private ITask task;
    private UDTLogger MyLogger;
    private ReportData rpData;
    
    public ProcessImpl() {
    }
    
    public ProcessImpl(ITask task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        MyLogger = new UDTLogger();
        MyLogger.setTask(task);
        MyLogger.Init();
        
        rpData = new ReportData();
        rpData.setFileName(task.getName());
        
        /*скачаивание файла*/
        //System.out.println("инициализация скачивания "+task.getName());
        IDownload download = FactoryDownload.getInstance().getDownload(task,MyLogger);
        // не смог получить экземпляр класса, следовательно ничего делать немогу
        if (download==null){
            return;
        }
        // если у задачи стоит флаг - скачать
        if (task.isDownload()){
            if (!download.run()){
                return ;
            }
        }
        
        /*распаковка файла*/
        IUnCompressing uncompr = FactoryUnCompressing.getInstance().getUnCompressing(task,MyLogger);
        if (uncompr!=null){
            if (!uncompr.Run()){
                // распаковалось не удачно, выйду
                return;
            }
        }
      
        /*
               Получу ссылку на экземпляр класса. Если объект создан
               то значит надо конвертировать файл, иначе эту процедуру пропускаем.
               Если буду добавлены таблицы, которые надо обновлять, то надо добавить новый класс в
               com.convert и добавить код в FactoryConvert
        */
        IConvert convert =FactoryConvert.getInstance().getConvert(task, MyLogger,rpData);
        if (convert != null){
            if (!convert.Run()){
            // Конвертация завершилась не успешно, выйду
            return;
            }
        }
        
        /*
          Получу ссылку на экземпляр класса. Если объект создан
          то значит надо конвертировать файл, иначе эту процедуру пропускаем.
          Если буду добавлены таблицы, которые надо обновлять, то надо добавить новый класс в
          com.dbwork и добавить код в FactoryConnection
        */
        IConnection connection = FactoryConnection.getInstance().getConnection(task, MyLogger, rpData);
        if (connection != null){ 
            if (connection.Run()){
                MyLogger.LogSend("Импорт данных успешно завершен!");
                
                // сохраняю отчет
                IReport report = ReportHTML.getInstance();
                report.SaveReport(rpData);
            } else {
                MyLogger.LogSend("Во время импорта данных произошла ошибка!");
            }
        }
        
        
    }

    @Override
    public void setTask(ITask task) {
        this.task = task;
    }
    
}
