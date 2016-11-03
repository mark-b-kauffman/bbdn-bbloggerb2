package com.blackboard.bblogger2.web.domain;

import blackboard.data.AbstractIdentifiable;
import blackboard.persist.DataType;
import blackboard.persist.impl.mapping.annotation.Column;
import blackboard.persist.impl.mapping.annotation.Table;

@Table("ilp_dcbb_config_ip")
public class DcbbConfigIP
  extends AbstractIdentifiable
{
  public static final DataType DATA_TYPE = new DataType(DcbbConfigIP.class);
  @Column({"sharedtoken"})
  private String sharedtoken;
  @Column({"numlinesLog"})
  private String numlinesLog;
  @Column({"logLevelType"})
  private String logLevelType;
  @Column({"ipaddress"})
  private String ipaddress;
  @Column({"gradeBookAppType"})
  private String gradeBookAppType;
//  @Column({"userIdType"})
//  private String userIdType;
//  @Column({"classMaxDays"})
//  private int classMaxDays = 365;
  @Column({"needAlertLinks"})
  private boolean needAlertLinks = true;
  @Column({"needAttendanceLinks"})
  private boolean needAttendanceLinks = true;
  @Column({"needAttendanceLastDates"})
  private boolean needAttendanceLastDates = false;
  @Column({"needGradesLocking"})
  private boolean needGradesLocking = false;
  @Column({"dateFormat"})
  private String dateFormat;
  @Column({"noOfGradebookFields"})
  private int noOfGradebookFields;
  @Column({"gradeLetters"})
  private String gradeLetters;
  @Column({"ilpURL"})
  private String ilpURL;
  @Column({"retentionProcessID"})
  private String retentionProcessID;
  @Column({"attendanceProcessID"})
  private String attendanceProcessID;
  @Column({"gradebookProcessID"})
  private String gradebookProcessID;
//  @Column({"showIncompleteGrade"})
//  private boolean showIncompleteGrade = false;
//  @Column({"ilpApiURL"})
//  private String ilpApiURL;
//  @Column({"ilpApiConnectionId"})
//  private String ilpApiConnectionId;
//  @Column({"ilpApiConnectionPwd"})
//  private String ilpApiConnectionPwd;
//  @Column({"validateGradesLocally"})
//  private boolean validateGradesLocally = true;
//  @Column({"showNeverAttended"})
//  private boolean showNeverAttended = true;
//  @Column({"expireLabel"})
//  private String expireLabel;
  @Column({"createdby"})
  private String createdBy;

  
  public String getSharedtoken()
  {
    return this.sharedtoken;
  }
  
  public void setSharedtoken(String sharedtoken)
  {
    this.sharedtoken = sharedtoken;
  }
  
  public String getNumlinesLog()
  {
    return this.numlinesLog;
  }
  
  public void setNumlinesLog(String numlinesLog)
  {
    this.numlinesLog = numlinesLog;
  }
  
  public String getLogLevelType()
  {
    return this.logLevelType;
  }
  
  public void setLogLevelType(String logLevelType)
  {
    this.logLevelType = logLevelType;
  }
  
  public String getIpaddress()
  {
    return this.ipaddress;
  }
  
  public void setIpaddress(String ipaddress)
  {
    this.ipaddress = ipaddress;
  }
  
  public String getGradeBookAppType()
  {
    return this.gradeBookAppType;
  }
  
  public void setGradeBookAppType(String gradeBookAppType)
  {
    this.gradeBookAppType = gradeBookAppType;
  }
  /*
  public int getClassMaxDays()
  {
    return this.classMaxDays;
  }
  
  public void setClassMaxDays(int maxDays)
  {
    this.classMaxDays = maxDays;
  }
  
  public String getUserIdType()
  {
    return this.userIdType;
  }
  
  public void setUserIdType(String userIdType)
  {
    this.userIdType = userIdType;
  }
  */
  public String getDateFormat()
  {
    return this.dateFormat;
  }
  
  public void setDateFormat(String dateFormat)
  {
    this.dateFormat = dateFormat;
  }
  
  public int getNoOfGradebookFields()
  {
    return this.noOfGradebookFields;
  }
  
  public void setNoOfGradebookFields(int noOfGradebookFields)
  {
    this.noOfGradebookFields = noOfGradebookFields;
  }
  
  public String getGradeLetters()
  {
    return this.gradeLetters;
  }
  
  public void setGradeLetters(String gradeLetters)
  {
    this.gradeLetters = gradeLetters;
  }
  
  public String getIlpURL()
  {
    return this.ilpURL;
  }
  
  public void setIlpURL(String ilpURL)
  {
    this.ilpURL = ilpURL;
  }
  
  public String getRetentionProcessID()
  {
    return this.retentionProcessID;
  }
  
  public void setRetentionProcessID(String retentionProcessID)
  {
    this.retentionProcessID = retentionProcessID;
  }
  
  public String getAttendanceProcessID()
  {
    return this.attendanceProcessID;
  }
  
  public void setAttendanceProcessID(String attendanceProcessID)
  {
    this.attendanceProcessID = attendanceProcessID;
  }
  
  public String getGradebookProcessID()
  {
    return this.gradebookProcessID;
  }
  
  public void setGradebookProcessID(String gradebookProcessID)
  {
    this.gradebookProcessID = gradebookProcessID;
  }
  
  public String getCreatedBy()
  {
    return this.createdBy;
  }
  
  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }
  
  public boolean isNeedAlertLinks()
  {
    return this.needAlertLinks;
  }
  
  public void setNeedAlertLinks(boolean needAlertLinks)
  {
    this.needAlertLinks = needAlertLinks;
  }
  
  public boolean isNeedAttendanceLinks()
  {
    return this.needAttendanceLinks;
  }
  
  public void setNeedAttendanceLinks(boolean needAttendanceLinks)
  {
    this.needAttendanceLinks = needAttendanceLinks;
  }
  
  public boolean isNeedAttendanceLastDates()
  {
    return this.needAttendanceLastDates;
  }
  
  public void setNeedAttendanceLastDates(boolean needAttendanceLastDates)
  {
    this.needAttendanceLastDates = needAttendanceLastDates;
  }
  
  public boolean isNeedGradesLocking()
  {
    return this.needGradesLocking;
  }
  
  public void setNeedGradesLocking(boolean needGradesLocking)
  {
    this.needGradesLocking = needGradesLocking;
  }
  /*
  public boolean isShowIncompleteGrade()
  {
    return this.showIncompleteGrade;
  }
  
  public void setShowIncompleteGrade(boolean showIncompleteGrade)
  {
    this.showIncompleteGrade = showIncompleteGrade;
  }
  
  public String getIlpApiURL()
  {
    return this.ilpApiURL;
  }
  
  public void setIlpApiURL(String ilpApiURL)
  {
    this.ilpApiURL = ilpApiURL;
  }
  
  public String getIlpApiConnectionId()
  {
    return this.ilpApiConnectionId;
  }
  
  public void setIlpApiConnectionId(String ilpApiConnectionId)
  {
    this.ilpApiConnectionId = ilpApiConnectionId;
  }
  
  public String getIlpApiConnectionPwd()
  {
    return this.ilpApiConnectionPwd;
  }
  
  public void setIlpApiConnectionPwd(String ilpApiConnectionPwd)
  {
    this.ilpApiConnectionPwd = ilpApiConnectionPwd;
  }
  
  public boolean isValidateGradesLocally()
  {
    return this.validateGradesLocally;
  }
  
  public void setValidateGradesLocally(boolean validateGradesLocally)
  {
    this.validateGradesLocally = validateGradesLocally;
  }
  
  public boolean isShowNeverAttended()
  {
    return this.showNeverAttended;
  }
  
  public void setShowNeverAttended(boolean showNeverAttended)
  {
    this.showNeverAttended = showNeverAttended;
  }
  
  public String getExpireLabel()
  {
    return this.expireLabel;
  }
  
  public void setExpireLabel(String expireLabel)
  {
    this.expireLabel = expireLabel;
  }
  */
  public String toString()
  {
//    return "DcbbConfigIP [sharedtoken=" + this.sharedtoken + ", ipaddress=" + this.ipaddress + ", gradeBookAppType=" + this.gradeBookAppType + ", needAlertLinks=" + this.needAlertLinks + ", userIdType=" + this.userIdType + ", needAttendanceLinks=" + this.needAttendanceLinks + ", needAttendanceLastDates=" + this.needAttendanceLastDates + ", needGradesLocking=" + this.needGradesLocking + ", dateFormat=" + this.dateFormat + ", noOfGradebookFields=" + this.noOfGradebookFields + ", gradeLetters=" + this.gradeLetters + ", ilpURL=" + this.ilpURL + ", retentionProcessID=" + this.retentionProcessID + ", attendanceProcessID=" + this.attendanceProcessID + ", gradebookProcessID=" + this.gradebookProcessID + ", showIncompleteGrade=" + this.showIncompleteGrade + ", ilpApiURL=" + this.ilpApiURL + ", ilpApiConnectionId=" + this.ilpApiConnectionId + ", ilpApiConnectionPwd=" + this.ilpApiConnectionPwd + ", validateGradesLocally=" + this.validateGradesLocally + ", showNeverAttended=" + this.showNeverAttended + ", expireLabel=" + this.expireLabel + ", createdBy=" + this.createdBy + "]";
    return "DcbbConfigIP [sharedtoken=" + this.sharedtoken + ", ipaddress=" + this.ipaddress + ", gradeBookAppType=" + this.gradeBookAppType + ", needAlertLinks=" + this.needAlertLinks + ", userIdType=" + ", needAttendanceLinks=" + this.needAttendanceLinks + ", needAttendanceLastDates=" + this.needAttendanceLastDates + ", needGradesLocking=" + this.needGradesLocking + ", dateFormat=" + this.dateFormat + ", noOfGradebookFields=" + this.noOfGradebookFields + ", gradeLetters=" + this.gradeLetters + ", ilpURL=" + this.ilpURL + ", retentionProcessID=" + this.retentionProcessID + ", attendanceProcessID=" + this.attendanceProcessID + ", gradebookProcessID=" + this.gradebookProcessID + ", createdBy=" + this.createdBy + "]";
 
  }
}
