package com.blackboard.bbloggerb2;

import blackboard.data.content.Content;
import blackboard.data.course.Course;
import blackboard.persist.PersistenceException;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.course.CourseDbLoader.Default;
import blackboard.platform.cx.component.CopyControl;
import blackboard.platform.cx.component.CxComponent;
import blackboard.platform.cx.component.CxComponent.Usage;
import blackboard.platform.cx.component.CxComponentLogger;
import blackboard.platform.cx.component.ExportControl;
import blackboard.platform.cx.component.GenericPackageEntry;
import blackboard.platform.cx.component.ImportControl;
import blackboard.platform.log.LogService.Verbosity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Handles requests for the application home page.
 */
public class CourseCopyHandler implements CxComponent {
	
	private static final Logger logger = LoggerFactory.getLogger(CourseCopyHandler.class);
	
  private static final int CHECK_JOB_INTERVAL_MILLIS = 5000;
  
  public CxComponent.Usage getUsage()
  {
    logger.info("CourseCopyHandler.getUsage");
    return CxComponent.Usage.ALWAYS;
  }
  
  public void doCopy(CopyControl cc)
  {
    try
    {
      logger.info("CourseCopyHandler.doCopy");

    }
    catch (Exception ex)
    {
      logger.info( "CourseCopyHandler.doCopy", ex);
      logger.info("Course media copy failed. Course src (" + cc.getSourceCourseId() + ") dest (" + cc.getDestinationCourseId() + ")");
    }
  }
  
   
  public String getComponentHandle()
  {
    logger.info("CourseCopyHandler.getComponentHandle");
    
    return "1234";
  }
  
  public String getApplicationHandle()
  {
    logger.info("CourseCopyHandler.getApplicationHandle");
    
    return "2345";
  }
  
  public String getName()
  {
    logger.info("CourseCopyHandler.getName");
    return "bbloggerb2 - course copy";
  }
  
  public void doExport(ExportControl ec)
  {
    logger.info("CourseCopyHandler.doExport not implemented");
  }
  
  public void doImport(GenericPackageEntry gpe, ImportControl ic)
  {
    logger.info("CourseCopyHandler.doImport - " + gpe + ", " + ic + " not implemented");
  }
  
  public void afterImportContent(Content content, ImportControl ic)
  {
    logger.info("CourseCopyHandler.afterImportContent - " + content + " " + ic + " not implemented");
  }


}
