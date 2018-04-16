package com.blackboard.bbloggerb2;
/* Copyright (C) 2016, Blackboard Inc.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  -- Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *  -- Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *  -- Neither the name of Blackboard Inc. nor the names of its contributors
 *     may be used to endorse or promote products derived from this
 *     software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY BLACKBOARD INC ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL BLACKBOARD INC. BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

// Author: MBK
// Purpose: Demonstrate the functionality provided by blackboard.platform.log.Log and LogService and how they can be used to write
// to a directory returned by PluginUtil.getLogDirectory

// READ CAREFULLY! There are two loggers used in this class. The first is using the Blackboard Java APIs, Log and LogService.
// The second is using slf4j and Logback. The second logs to the blackboard/logs/custom directory and is used to keep track of everything
// this code does with the first. The reason for this is that is so that we can follow the logic this class uses to create and log to
// a file using the Learn Log and LogService classes.

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

import org.slf4j.Logger; // We'll use logback to log what this logging class is doing for troubleshooting.
import org.slf4j.LoggerFactory;

public class MybbLOGGER
{

  private static final Logger logback = LoggerFactory.getLogger(MybbLOGGER.class);
  // The above Logger uses the Logback logger configured in src/main/resources/logback.xml
  // We use the Logback logger to 'trace' or 'follow' what is happening with the Learn Java Log and LogService APIs.

  //The following is the Blackboard Logger we're creating with this class, MybbLOGGER. MybbLOGGER uses the Learn Java APIs Log and LogService
  //and PluginUtil.getLogDirectory, to write logs to a log file.
  private static MybbLOGGER ABBLOGGER = null;  // This will be a singelton class.
  private static LogService LOGSERVICE = null; // Changed this from LOGGER to LOGSERVICE FOR CLARITY. MBK
  private static Log B2LOG = null;  // MBK - Global so we can only create the log file when it doesn't exist.

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
      this.pluginLogPath = ""; // Something really went wrong here if this didn't run on startup...
    }
  }

  public static MybbLOGGER getBbLogger() // Define a singelton logging class.
  {
    if (ABBLOGGER == null)
    {
      ABBLOGGER = new MybbLOGGER();
      LOGSERVICE = LogServiceFactory.getInstance();
    }
    return ABBLOGGER;
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
    String pluginLogFile = this.pluginLogPath + File.separator + this.logName;


    logback.info("In logMessage. Set pluginLogFile="+pluginLogFile);

    if (B2LOG != null)
        B2LOGlogFileName = B2LOG.getLogFileName();
    else
        logback.info("B2LOG was NULL.");

    logback.info("In logMessage. B2LOG.getLogFileName():"+ B2LOGlogFileName +  " pluginLogFile:"+pluginLogFile);

    if ((B2LOG == null)) // || ((this.pluginLogPath != "") && (!B2LOG.getLogFileName().equalsIgnoreCase(pluginLogFile)))) // Unecessary code, gone in 2.0
    {
        logback.info("logMessage calling createLogFile()");
        createLogFile();
        logback.info("logMessage back from createLogFile()");
        B2LOG = LOGSERVICE.getConfiguredLog(this.logName);
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
      this.logsDirectory = PlugInUtil.getLogDirectory("bbdn", "bbloggerb2");   // this.logsDirectory.toString() == this.pluginLogPath??
      logback.info("createLogFile this.logsDirectory String Value:" + this.logsDirectory.toString());
      if (!this.logsDirectory.isDirectory()) {
        logback.info("createLogFile this.logsDirectory.isDirectory was FALSE");
        logback.info("createLogFile calling this.logsDirectory.mkdir()");
        this.logsDirectory.mkdir();
      } else {
        // Nothing here... no more cleanUpLogFiles();
      }
      // 1.5.7 used defineNewFileLog only.  1.5.8+ will use both new File( and defineNewFileLog
      logback.info("createLogFile calling() new File("+ this.logsDirectory + File.separator + this.logName); // 1.6.0+ will be using File.separator
      File logsFile = new File(this.logsDirectory + File.separator + this.logName);

      logback.info("createLogFile() calling LogService.defineNewFileLog with logName:" + this.logName + " and logFilePath:" +logsFile.toString());
      LOGSERVICE.defineNewFileLog(this.logName, logsFile.toString(), LogService.Verbosity.DEBUG, false);

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
  } // private void createLogFile

} //public class MybbLOGGER
