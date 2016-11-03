package com.blackboard.bblogger2.common.util;

import blackboard.data.course.Course;
import blackboard.data.course.Course.Duration;
import blackboard.data.navigation.TabTabGroup;
import blackboard.data.user.User;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.PersistenceRuntimeException;
import blackboard.persist.user.UserDbLoader;
import blackboard.persist.user.UserDbLoader.Default;
import blackboard.platform.intl.BbResourceBundle;
import blackboard.platform.intl.BundleManager;
import blackboard.platform.intl.BundleManagerFactory;
import blackboard.platform.navigation.tab.TabGroupManager;
import blackboard.platform.navigation.tab.TabGroupManagerFactory;
import blackboard.platform.plugin.PlugIn;
import blackboard.platform.plugin.PlugInManager;
import blackboard.platform.plugin.PlugInManagerFactory;
import blackboard.platform.term.Term;
import com.blackboard.bblogger2.bbbridge.web.dao.IDcbbConfigIPDao;
import com.blackboard.bblogger2.bbbridge.web.dao.impl.DcbbConfigIPDaoImpl;
import com.blackboard.bblogger2.web.domain.DcbbConfigIP;
import dcbb.common.DcbbLOGGER;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

public class DcbbUtil
{
  private PlugIn plugIn;
  private BbResourceBundle resourceBundle;
  
  public void setPlugIn(PlugIn plugIn)
  {
    this.plugIn = plugIn;
  }
  
  public void setResourceBundle(BbResourceBundle resourceBundle)
  {
    this.resourceBundle = resourceBundle;
  }
  
  public PlugIn getPlugIn()
  {
    if (this.plugIn == null) {
      this.plugIn = PlugInManagerFactory.getInstance().getPlugIn("bbdn", "bbloggerb2");
    }
    return this.plugIn;
  }
  
  public BbResourceBundle getResourceBundle()
  {
    if (this.resourceBundle == null) {
      this.resourceBundle = BundleManagerFactory.getInstance().getPluginBundle(getPlugIn().getId());
    }
    return this.resourceBundle;
  }
  
  public String getResourceString(String key)
  {
    return getResourceBundle().getString(key, true);
  }
  
  public static Map<String, String> getQueryMap(String query)
  {
    DcbbLOGGER.getDcbbLogger().logDebug("DcbbUtil.getQueryMap: start");
    
    String[] params = query.split("&");
    Map<String, String> map = new HashMap();
    for (String param : params)
    {
      String name = param.split("=")[0];
      String value = param.split("=")[1];
      map.put(name, value);
    }
    return map;
  }
  
  public static boolean isCourseSectionVisible(Course course, Term term)
  {
    boolean courseAvailability = course.getIsAvailable();
    Calendar courseStartDate = course.getStartDate();
    Calendar courseEndDate = course.getEndDate();
    Calendar termStartDate = null;
    Calendar termEndDate = null;
    if (term != null)
    {
      termStartDate = term.getStartDate();
      termEndDate = term.getEndDate();
    }
    Long courseStDateLong = courseStartDate != null ? Long.valueOf(courseStartDate.getTimeInMillis()) : null;
    Long courseEndDateLong = courseEndDate != null ? Long.valueOf(courseEndDate.getTimeInMillis()) : null;
    Long termStDateLong = termStartDate != null ? Long.valueOf(termStartDate.getTimeInMillis()) : null;
    Long termEndDateLong = termEndDate != null ? Long.valueOf(termEndDate.getTimeInMillis()) : null;
    
    boolean isTermDateInFuture = false;
    if ((termStDateLong != null) && (termStDateLong.longValue() > Calendar.getInstance().getTimeInMillis())) {
      isTermDateInFuture = true;
    }
    boolean isTermDateInPast = false;
    if ((termEndDateLong != null) && (termEndDateLong.longValue() < Calendar.getInstance().getTimeInMillis())) {
      isTermDateInPast = true;
    }
    boolean isCourseDateInFuture = false;
    if ((courseStDateLong != null) && (courseStDateLong.longValue() > Calendar.getInstance().getTimeInMillis())) {
      isCourseDateInFuture = true;
    }
    boolean isCourseDateInPast = false;
    if ((courseEndDateLong != null) && (courseEndDateLong.longValue() < Calendar.getInstance().getTimeInMillis())) {
      isCourseDateInPast = true;
    }
    if (!courseAvailability) {
      return false;
    }
    if ((isCourseDateInPast) || ((Course.Duration.USE_TERM == course.getDurationType()) && (isTermDateInPast))) {
      return false;
    }
    if ((Course.Duration.CONTINUOUS == course.getDurationType()) && (isCourseDateInFuture == true)) {
      return false;
    }
    if ((Course.Duration.USE_TERM == course.getDurationType()) && (isTermDateInFuture == true)) {
      return false;
    }
    if ((!course.getIsAvailable(true)) && (Course.Duration.CONTINUOUS == course.getDurationType())) {
      return false;
    }
    if ((course.getIsAvailable(true) == true) && (Course.Duration.DATE_RANGE == course.getDurationType()) && (isCourseDateInFuture == true)) {
      return false;
    }
    if ((course.getIsAvailable(true) == true) && (Course.Duration.USE_TERM == course.getDurationType()) && (isTermDateInFuture == true)) {
      return false;
    }
    DcbbLOGGER.getDcbbLogger().logDebug("  CourseSectionVisibility is true for course " + course.getDisplayTitle() + " - " + course.getCourseId());
    return true;
  }
  
  public static boolean isValidDate(String dateStr, String format, boolean lenient)
  {
    Date date = parseDate(dateStr, format, lenient);
    if (date == null) {
      return false;
    }
    return true;
  }
  
  public static Date parseDate(String dateStr, String format, boolean lenient)
  {
    Date date = null;
    
    String reFormat = Pattern.compile("d+|M+").matcher(Matcher.quoteReplacement(format)).replaceAll("\\\\d{1,2}");
    reFormat = Pattern.compile("y+").matcher(reFormat).replaceAll("\\\\d{4}");
    if (Pattern.compile(reFormat).matcher(dateStr).matches())
    {
      SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getDateInstance();
      sdf.applyPattern(format);
      sdf.setLenient(lenient);
      try
      {
        date = sdf.parse(dateStr);
      }
      catch (ParseException e)
      {
        e.printStackTrace();
      }
    }
    return date;
  }
  
  public static String getStrippedKey(String pkStr)
  {
    return pkStr.substring(pkStr.indexOf("_") + 1, pkStr.lastIndexOf("_"));
  }
  
  public static String getTabTabGroup(String refName)
  {
    TabGroupManager tgm = TabGroupManagerFactory.getInstance();
    TabTabGroup ttg = null;
    try
    {
      ttg = tgm.loadByTabReferenceName(refName);
    }
    catch (KeyNotFoundException e)
    {
      DcbbLOGGER.getDcbbLogger().logInfo(String.format("Tab group not found for %s", new Object[] { refName }));
    }
    catch (PersistenceRuntimeException e)
    {
      DcbbLOGGER.getDcbbLogger().logInfo(String.format("Failed to get tab group : %s", new Object[] { e.getMessage() }));
    }
    if (ttg != null) {
      return ttg.getTabId().getExternalString();
    }
    return null;
  }
  
  public static String getStackTraceString(Exception e)
  {
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }
  
  public static final String encode(byte[] d)
  {
    if (d == null) {
      return null;
    }
    byte[] data = new byte[d.length + 2];
    System.arraycopy(d, 0, data, 0, d.length);
    byte[] dest = new byte[data.length / 3 * 4];
    
    int sidx = 0;
    for (int didx = 0; sidx < d.length; didx += 4)
    {
      dest[didx] = ((byte)(data[sidx] >>> 2 & 0x3F));
      dest[(didx + 1)] = ((byte)(data[(sidx + 1)] >>> 4 & 0xF | data[sidx] << 4 & 0x3F));
      
      dest[(didx + 2)] = ((byte)(data[(sidx + 2)] >>> 6 & 0x3 | data[(sidx + 1)] << 2 & 0x3F));
      
      dest[(didx + 3)] = ((byte)(data[(sidx + 2)] & 0x3F));sidx += 3;
    }
    for (int idx = 0; idx < dest.length; idx++) {
      if (dest[idx] < 26) {
        dest[idx] = ((byte)(dest[idx] + 65));
      } else if (dest[idx] < 52) {
        dest[idx] = ((byte)(dest[idx] + 97 - 26));
      } else if (dest[idx] < 62) {
        dest[idx] = ((byte)(dest[idx] + 48 - 52));
      } else if (dest[idx] < 63) {
        dest[idx] = 43;
      } else {
        dest[idx] = 47;
      }
    }
    for (int idx = dest.length - 1; idx > d.length * 4 / 3; idx--) {
      dest[idx] = 61;
    }
    return new String(dest);
  }
  
  public static boolean isMatch(String text, String pattern)
  {
    try
    {
      Pattern patt = Pattern.compile(pattern);
      Matcher matcher = patt.matcher(text);
      return matcher.matches();
    }
    catch (RuntimeException e) {}
    return false;
  }
  
  public static boolean isValidIPV6(String ipString)
  {
    InetAddress inetAddress = null;
    try
    {
      inetAddress = InetAddress.getByName(ipString);
      if ((inetAddress instanceof Inet6Address)) {
        return true;
      }
    }
    catch (Exception e)
    {
      DcbbLOGGER.getDcbbLogger().logInfo(getStackTraceString(e));
    }
    return false;
  }
  
  public static boolean isValidIP_IPRangeV4(String ip)
  {
    StringTokenizer st = new StringTokenizer(ip, ",");
    try
    {
      while (st.hasMoreTokens())
      {
        String string = st.nextToken();
        string = string.trim();
        InetAddress inetAddress = InetAddress.getByName(string);
        if (!(inetAddress instanceof Inet4Address)) {
          return false;
        }
      }
    }
    catch (Exception e)
    {
      return false;
    }
    return true;
  }
  
  public static boolean validIP_IPRanges(String ip)
    throws Exception
  {
    DcbbLOGGER.getDcbbLogger().logDebug("DcbbUtil.validIP_IPRanges: start");
    if ((ip == null) || (ip.trim().equals(""))) {
      return true;
    }
    String ipstr = ip.trim();
    if ((ipstr.indexOf(":") > -1) || (ipstr.indexOf("::") > -1) || (ipstr.indexOf("ipv6") > -1) || (ipstr.indexOf("[") < ipstr.indexOf("]")))
    {
      String[] ipv6Arr = ipstr.split(",\\s*");
      for (String ipv6 : ipv6Arr)
      {
        ipv6 = ipv6.trim();
        InetAddress inetAddress = InetAddress.getByName(ipv6);
        if (!(inetAddress instanceof Inet6Address)) {
          return false;
        }
      }
    }
    else
    {
      String iprange_regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(([01]?\\d\\d?|2[0-4]\\d|25[0-5])|(([01]?\\d\\d?|2[0-4]\\d|25[0-5])[-/]([01]?\\d\\d?|2[0-4]\\d|25[0-5])))$";
      
      StringTokenizer st = new StringTokenizer(ip, ",");
      while (st.hasMoreTokens())
      {
        String string = st.nextToken();
        string = string.trim();
        if (!isMatch(string, iprange_regex)) {
          return false;
        }
        if ((isMatch(string, iprange_regex)) && (!checkIPRange(string))) {
          return false;
        }
      }
    }
    return true;
  }
  
  public static boolean validURL(String url)
  {
    String url_regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    return isMatch(url, url_regex);
  }
  
  private static boolean checkIPRange(String text)
    throws Exception
  {
    DcbbLOGGER.getDcbbLogger().logDebug("DcbbUtil.checkIPRange: start");
    
    String ch = null;
    String dot = ".";
    text = text.trim();
    if (text.indexOf("-") > -1) {
      ch = "-";
    } else if (text.indexOf("/") > -1) {
      ch = "/";
    }
    if (ch != null)
    {
      String s1 = text.substring(text.indexOf(ch) + 1);
      String s2 = text.substring(text.lastIndexOf(dot) + 1, text.indexOf(ch));
      if (Integer.parseInt(s2) > Integer.parseInt(s1)) {
        return false;
      }
    }
    return true;
  }
  
  public static void testIPAndIPRanges()
    throws Exception
  {
    String ipaddress = "1.2.2.23/23-23/3";
    System.out.println("IP Address = " + ipaddress + "\nValidity = " + validIP_IPRanges(ipaddress));
  }
  
  public static void testURL()
  {
    String url = "http://www.google.com";
    System.out.println("URL = " + url + "\nValidity = " + validURL(url));
  }
  
  public static String getLogFileContents(String filename, HttpServletRequest request)
  {
    DcbbLOGGER.getDcbbLogger().logDebug("DcbbUtil.getLogFileContents: start");
    
    DcbbUtil dcbbUtil = new DcbbUtil();
    
    File file = new File(filename);
    if (!file.exists()) {
      return dcbbUtil.getResourceString("gc.constants.nologexist.label");
    }
    StringBuilder text = new StringBuilder();
    String NL = "\n";
    Scanner scanner = null;
    try
    {
      scanner = new Scanner(new FileInputStream(filename), "utf-8");
    }
    catch (FileNotFoundException e)
    {
      DcbbLOGGER.getDcbbLogger().logInfo(getStackTraceString(e));
    }
    int lineLimit = 0;
    IDcbbConfigIPDao configIPDao = new DcbbConfigIPDaoImpl();
    List<DcbbConfigIP> configIPs = configIPDao.getAll();
    DcbbConfigIP configIP = null;
    if ((configIPs != null) && (configIPs.size() > 0)) {
      configIP = (DcbbConfigIP)configIPs.get(0);
    }
    String numlines = configIP.getNumlinesLog();
    
    int totalLineCount = 0;
    try
    {
      totalLineCount = getLineCount(filename);
    }
    catch (IOException e) {}
    if ((totalLineCount != 0) && (totalLineCount > 0)) {
      try
      {
        lineLimit = Integer.parseInt(numlines);
      }
      catch (NumberFormatException e)
      {
        DcbbLOGGER.getDcbbLogger().logDebug("application.log.numlines.limit does not provide int value, defaulting to 50");
        lineLimit = 50;
      }
    }
    try
    {
      for (int i = 0; i < totalLineCount - lineLimit; i++) {
        scanner.nextLine();
      }
      for (int i = 0; i < lineLimit; i++) {
        text.append(scanner.nextLine() + NL);
      }
    }
    finally
    {
      scanner.close();
    }
    return text.toString();
  }
  
  private static int getLineCount(String filename)
    throws IOException
  {
    InputStream is = new BufferedInputStream(new FileInputStream(filename));
    try
    {
      byte[] c = new byte['Ѐ'];
      int count = 0;
      int readChars = 0;
      boolean empty = true;
      int i;
      while ((readChars = is.read(c)) != -1)
      {
        empty = false;
        for (i = 0; i < readChars; i++) {
          if (c[i] == 10) {
            count++;
          }
        }
      }
      return (count == 0) && (!empty) ? 1 : count;
    }
    finally
    {
      is.close();
    }
  }
  
  public static String getDateForLogs(int year, int month, int day)
  {
    DcbbLOGGER.getDcbbLogger().logDebug("Entered DcbbUtil :: getDateForLogs(int year, int month, int day) method");
    Calendar cal = Calendar.getInstance();
    if ((cal.get(5) == day) && (cal.get(2) + 1 == month) && (cal.get(1) == year)) {
      return "";
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    cal.set(5, day);
    cal.set(2, month - 1);
    cal.set(1, year);
    cal.add(5, 1);
    
    return "." + dateFormat.format(cal.getTime());
  }
  
  public static Date getLocalizedDate(String dateParamStr, String localeStr)
  {
    DcbbLOGGER.getDcbbLogger().logDebug("DcbbUtil.getLocalizedDate: start");
    if ((localeStr == null) || (dateParamStr == null)) {
      return null;
    }
    String language = localeStr.substring(0, 2);
    String country = localeStr.substring(3);
    DateFormat df = DateFormat.getDateInstance(3, new Locale(language, country));
    Date myDate = null;
    try
    {
      myDate = df.parse(dateParamStr);
    }
    catch (ParseException e)
    {
      DcbbLOGGER.getDcbbLogger().logInfo(getStackTraceString(e));
    }
    return myDate;
  }
  
  public static boolean isCourseSectionValidDates(Course course, int numberOfDays)
  {
    boolean valid = true;
    try
    {
      DcbbLOGGER.getDcbbLogger().logDebug("  Checking start date of course " + course.getDisplayTitle() + " to see if it should be included in this result set");
      
      Date today = new Date();
      
      long courseMilliseconds = course.getStartDate().getTimeInMillis();
      Calendar cal = Calendar.getInstance();
      cal.setTime(today);
      
      long daysStarted = (cal.getTimeInMillis() - courseMilliseconds) / 86400000L;
      if (daysStarted > numberOfDays)
      {
        DcbbLOGGER.getDcbbLogger().logInfo("  Course " + course.getDisplayTitle() + " started more than " + numberOfDays + " ago.");
        valid = false;
      }
    }
    catch (Exception ex)
    {
      DcbbLOGGER.getDcbbLogger().logError("  Could not check start date conditions for course " + course.getDisplayTitle());
    }
    return valid;
  }
  
  public static String getUsernameFromBatchUid(String batchUid)
  {
    String username = null;
    try
    {
      username = UserDbLoader.Default.getInstance().loadByBatchUid(batchUid).getUserName();
    }
    catch (KeyNotFoundException e)
    {
      DcbbLOGGER.getDcbbLogger().logInfo("Could not find user with batch uid " + batchUid + " in the Blackboard database");
      DcbbLOGGER.getDcbbLogger().logInfo(e.getMessage());
    }
    catch (PersistenceException e)
    {
      DcbbLOGGER.getDcbbLogger().logInfo("Error retrieving username for user with batch uid " + batchUid);
      DcbbLOGGER.getDcbbLogger().logInfo(e.getMessage());
    }
    return username;
  }
}
