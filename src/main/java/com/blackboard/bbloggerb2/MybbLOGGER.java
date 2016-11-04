package com.blackboard.bbloggerb2;

import blackboard.platform.log.Log;
import blackboard.platform.log.LogService;
import blackboard.platform.log.LogService.Verbosity;
import blackboard.platform.log.LogServiceFactory;
import blackboard.platform.plugin.PlugInUtil;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger; // We'll use logback to log what this logging class is doing to troubleshoot.
import org.slf4j.LoggerFactory;

public class MybbLOGGER
{
    
  private static final Logger logback = LoggerFactory.getLogger(MybbLOGGER.class);  
  
  private static MybbLOGGER DCBBLOGGER = null;
  private static LogService LOGGER = null;
  private static Log B2LOG = null;  // MBK - added so we can only create the log file when it doesn't exist.

  private static String loglevel;
  private String logName = "bblogger.log.txt";
  File logsDirectory = null;
  private String pluginLogPath;
  
  public static String getLoglevel() // We won't use this for this test. Just return the DEBUG log level.
  {

    loglevel = "DEBUG"; 

    return loglevel;
  }
  
  public static void setLoglevel(String loglevel)
  {
    if (loglevel != null) {
      loglevel = loglevel;
    }
  }
  
  public MybbLOGGER()
  {
    if (PlugInUtil.getLogDirectory("bbdn", "bbloggerb2") != null) {
      this.pluginLogPath = PlugInUtil.getLogDirectory("bbdn", "bbloggerb2").getAbsolutePath();
    } else {
      this.pluginLogPath = "";
    }
  }
  
  public static MybbLOGGER getDcbbLogger()
  {
    if (DCBBLOGGER == null)
    {
      DCBBLOGGER = new MybbLOGGER();
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
    logback.info("Enter logMessage(). this.logName:" + this.logName);
    String B2LOGlogFileName = "";
    
    String pluginLogFile = this.pluginLogPath + "/" + this.logName; // MBK was using DOS style path. \\
    logback.info("In logMessage. Set pluginLogFile="+pluginLogFile);
    
    if (B2LOG != null)
        B2LOGlogFileName = B2LOG.getLogFileName();
    
    logback.info("In logMessage. B2LOG.getLogFileName():"+ B2LOGlogFileName +  "comparing with pluginLogFile...");
   
    if ((B2LOG == null) || ((this.pluginLogPath != "") && (!B2LOGlogFileName.equalsIgnoreCase(pluginLogFile))))
    {
        logback.info("logMessage calling createLogFile()");
        createLogFile();
        logback.info("logMessage back from createLogFile()");
        B2LOG = LOGGER.getConfiguredLog(this.logName);
    }
    logback.info("logMessage calling Log.log()");
    B2LOG.log(logMessage, logLevel);
    logback.info("Exit logMessage()");
  }
  
  private void createLogFile()
  {
    logback.info("Enter createLogFile()");
    try
    {
      logback.info("createLogFile calling PlugInUtil.getLogDirectory");
      this.logsDirectory = PlugInUtil.getLogDirectory("bbdn", "bbloggerb2");
      logback.info("createLogFile this.logsDirectory String Value:" + this.logsDirectory.toString());
      if (!this.logsDirectory.isDirectory()) {
        logback.info("createLogFile this.logsDirectory.isDirectory was FALSE");
        logback.info("createLogFile calling this.logsDirectory.mkdir()"); 
        this.logsDirectory.mkdir();
      } else {
        // Nothing here... no more cleanUpLogFiles();
      }
      logback.info("createLogFile calling() new File("+ this.logsDirectory + "/" + this.logName);
      File logsFile = new File(this.logsDirectory + "/" + this.logName);
      logback.info("createLogFile() calling LogService.defineNewFileLog with logName:" + this.logName + " and logFilePath:" +logsFile.toString());
      LOGGER.defineNewFileLog(this.logName, logsFile.toString(), LogService.Verbosity.DEBUG, false);
    }
    catch (Exception e)
    {
      logback.info("createLofFile() CAUGHT EXCEPTION");
      e.printStackTrace();
      if (e.getLocalizedMessage() != null)
        logback.info(e.getLocalizedMessage());
      else
        logback.info("createMessage() e.localizedMessage was null");
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
} //public class MybbLOGGER
