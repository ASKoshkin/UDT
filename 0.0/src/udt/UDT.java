/**
 * 
 * udt - updating data in the table
 * 
 */
package udt;

import com.compressing.UnCompressingRAR;
import com.convert.FactoryConvert;
import com.convert.IConvert;
import com.dbwork.FactoryConnection;
import com.dbwork.IConnection;
import com.download.DownloadHTTP;
import java.util.Date;
import udt.ISettings.TableEnum;

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
                        
        if (!MatchArgs(args)){
            // Аргументы переданы не правильно
            System.exit(0);
        }            
        
        if (!sxml.ReadSettings()){
            // Попытка чтения настроек была неуспешной, выйду.
            System.exit(0);
        }
        
        ReportHTML rprt = ReportHTML.getInstance();
        rprt.setDateStart(new Date());
        UDTLogger logger = UDTLogger.getInstance();
        logger.Init();
        
        if (sxml.isDownload()){
            if (!DownloadHTTP.getInstance().run()){
                // Процесс скачивания завершился не успешно, выйду.
                System.exit(0);
            }
            
            // Если обновляем паспорта то надо распаковать и изменить кодировку файла.
            if (sxml.getTableUpdate() == TableEnum.Passport){
                if (!UnCompressingRAR.getInstance().Run()){
                    // Процесс распаковки завершился с ошибкой, выйду
                    System.exit(0);
                }
                
            }
            
            /*
               Получу ссылку на экземпляр класса. Если объект создан
               то значит надо конвертировать файл, иначе эту процедуру пропускаем.
               Если буду добавлены таблицы, которые надо обновлять, то надо добавить новый класс в
               com.convert и добавить код в FactoryConvert
            */
            IConvert convert =FactoryConvert.getInstance().getConvert();
            if (convert != null){
                if (!convert.Run()){
                // Конвертация завершилась не успешно, выйду
                System.exit(0);
            
                }
            }
            
        } // end if (sxml.isDownload())
        
        
        /*
          Получу ссылку на экземпляр класса. Если объект создан
          то значит надо конвертировать файл, иначе эту процедуру пропускаем.
          Если буду добавлены таблицы, которые надо обновлять, то надо добавить новый класс в
          com.dbwork и добавить код в FactoryConnection
        */
        IConnection connection = FactoryConnection.getInstance().getConnection();
        if (connection != null){            
            if (connection.Run()){
                System.out.println("Импорт данных успешно завершен!");
            }
        }
        
    }
    
    
    
    
    
    /** 
     * Разбор аргументов полученных в приложение через командную строку.
     * Пример: 
     * @param args - параметры передаваемые в программу
     * Пример: -t Passport or -t Ofac
     * @return 
     * Возвращает true если параметры переданы правильно иначе false
     */
    private static boolean MatchArgs(String[] args){
        
        boolean Res = false;
        SettingsXML sxml = SettingsXML.getInstance();                
        
        if (args[0].equals("-t") && (args[1].equals(TableEnum.Ofac.toString() ) || args[1].equals(TableEnum.Passport.toString()))){
            
            if (args[1].equals(TableEnum.Ofac.toString())){
                sxml.setTableUpdate(TableEnum.Ofac);
            }
            
            if (args[1].equals(TableEnum.Passport.toString())){
                sxml.setTableUpdate(TableEnum.Passport);
            }
            Res = true;
        } else {            
            System.out.println("Not found parameter, example: -t Passport or -t Ofac ");
            
        }                
        
        return Res;        
    }
    
}
