package dcbb.common;

import blackboard.platform.log.Log;
import blackboard.platform.log.LogService;
import blackboard.platform.log.LogService.Verbosity;
import blackboard.platform.log.LogServiceFactory;
import blackboard.platform.plugin.PlugInUtil;
/* Not used for demonstrating the Log LogService issue.
import com.blackboard.bblogger2.bbbridge.web.dao.impl.DcbbConfigIPDaoImpl;
import com.blackboard.bblogger2.web.domain.DcbbConfigIP;
import com.blackboard.bblogger2.common.util.DcbbUtil;
*/
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DcbbLOGGER
{
  private static DcbbLOGGER DCBBLOGGER = null;
  private static LogService LOGGER = null;
  // private static DcbbConfigIPDaoImpl configIPDao = null;
  // private static DcbbConfigIP configIP = null;
  private static String loglevel;
  private String logName = "bblogger.log.txt";
  File logsDirectory = null;
  private String pluginLogPath;
  
  public static String getLoglevel() // We won't use this for this test. Just return the DEBUG log level.
  {
    /*if ((loglevel == null) || (loglevel.trim().equals("null")))
    { */
      loglevel = "DEBUG"; 
      /* configIPDao = new DcbbConfigIPDaoImpl();
      List<DcbbConfigIP> configIPs = configIPDao.getAll();
      if ((configIPs != null) && (configIPs.size() > 0))
      {
        configIP = (DcbbConfigIP)configIPs.get(0);
        if (configIP != null) {
          loglevel = configIP.getLogLevelType();
        }
      } 
    } */
    return loglevel;
  }
  
  public static void setLoglevel(String loglevel)
  {
    if (loglevel != null) {
      loglevel = loglevel;
    }
  }
  
  public DcbbLOGGER()
  {
    if (PlugInUtil.getLogDirectory("bbdn", "bblogger") != null) {
      this.pluginLogPath = PlugInUtil.getLogDirectory("bbdn", "bbloggerb2").getAbsolutePath();
    } else {
      this.pluginLogPath = "";
    }
  }
  
  public static DcbbLOGGER getDcbbLogger()
  {
    if (DCBBLOGGER == null)
    {
      DCBBLOGGER = new DcbbLOGGER();
      LOGGER = LogServiceFactory.getInstance();
    }
    return DCBBLOGGER;
  }
  
  public void logError(String logMessage)
  {
      if ((getLoglevel() == null) || ("ALL".equalsIgnoreCase(getLoglevel())) || ("ERROR".equalsIgnoreCase(getLoglevel())) || ("DEBUG".equalsIgnoreCase(getLoglevel())) || ("WARN".equalsIgnoreCase(getLoglevel())) || ("INFO".equalsIgnoreCase(getLoglevel()))) {
      logMessage(logMessage, LogService.Verbosity.ERROR);
      }
  }
  
  public void logInfo(String logMessage)
  {
      if ((getLoglevel() == null) || ("ALL".equalsIgnoreCase(getLoglevel())) || ("DEBUG".equalsIgnoreCase(getLoglevel())) || ("INFO".equalsIgnoreCase(getLoglevel()))) {
      logMessage(logMessage, LogService.Verbosity.INFORMATION);
      }
  }
  
  public void logDebug(String logMessage)
  {
      if ((getLoglevel() == null) || ("ALL".equalsIgnoreCase(getLoglevel())) || ("DEBUG".equalsIgnoreCase(getLoglevel()))) {
      logMessage(logMessage, LogService.Verbosity.DEBUG);
      }
  }
  
  private void logMessage(String logMessage, LogService.Verbosity logLevel)
  {
    Log B2LOG = null;
    B2LOG = LOGGER.getConfiguredLog(this.logName);
    String pluginLogFile = this.pluginLogPath + "\\" + this.logName;
    if ((B2LOG == null) || ((this.pluginLogPath != "") && (!B2LOG.getLogFileName().equalsIgnoreCase(pluginLogFile))))
    {
      createLogFile();
      B2LOG = LOGGER.getConfiguredLog(this.logName);
    }
    B2LOG.log(logMessage, logLevel);
  }
  
  private void createLogFile()
  {
    try
    {
      this.logsDirectory = PlugInUtil.getLogDirectory("bbdn", "bbloggerb2");
      if (!this.logsDirectory.isDirectory()) {
        this.logsDirectory.mkdir();
      } else {
        // cleanUpLogFiles();
      }
      File logsFile = new File(this.logsDirectory + "/" + this.logName);
      LOGGER.defineNewFileLog(this.logName, logsFile.toString(), LogService.Verbosity.DEBUG, false);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  /* Ignore the following. It has nothing to do with the Log/LogService rolling log issue.
  The entire cleanUpLogfiles() usage was removed by Ellucian.
  private void cleanUpLogFiles()
  {
    int maxLogLimit = 5;
    DcbbUtil dcbbUtil = new DcbbUtil();
    String ml = dcbbUtil.getResourceString("application.log.limit");
    try
    {
      maxLogLimit = Integer.parseInt(ml);
    }
    catch (NumberFormatException e) {}
    Map<Long, File> logFileMap;
    int numFiles;
    if (this.logsDirectory != null)
    {
      File[] listOfFiles = this.logsDirectory.listFiles();
      logFileMap = new TreeMap();
      
      numFiles = listOfFiles.length;
      if (listOfFiles.length <= maxLogLimit + 1) {
        return;
      }
      for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile())
        {
          String filename = listOfFiles[i].getName();
          if (!filename.contains("ILPLog"))
          {
            listOfFiles[i].delete();
            numFiles--;
          }
          else
          {
            String dateStr = "";
            try
            {
              dateStr = filename.replace("ILPLog.", "").replace(".txt", "");
            }
            catch (Exception e) {}
            if (dateStr.equals(""))
            {
              numFiles--;
            }
            else
            {
              Long logDateTime = null;Date logDate = null;
              try
              {
                logDate = DcbbUtil.parseDate(dateStr, "yyyy-MM-dd", false);
              }
              catch (Exception e) {}
              if (logDate == null)
              {
                numFiles--;
              }
              else
              {
                logDateTime = Long.valueOf(logDate.getTime());
                
                logFileMap.put(logDateTime, listOfFiles[i]);
              }
            }
          }
        }
      }
      for (Long logTime : logFileMap.keySet())
      {
        ((File)logFileMap.get(logTime)).delete();
        numFiles--;
        if (numFiles <= maxLogLimit) {
          break;
        }
      }
    }
  } */
} //public class DcbbLOGGER
