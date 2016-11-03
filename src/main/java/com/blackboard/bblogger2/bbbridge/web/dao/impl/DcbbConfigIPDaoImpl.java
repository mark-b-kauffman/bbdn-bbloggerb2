package com.blackboard.bblogger2.bbbridge.web.dao.impl;

import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceRuntimeException;
import blackboard.persist.dao.impl.SimpleDAO;
import com.blackboard.bblogger2.bbbridge.web.dao.IDcbbConfigIPDao;
import com.blackboard.bblogger2.web.domain.DcbbConfigIP;
import dcbb.common.DcbbLOGGER;
import java.util.List;

public class DcbbConfigIPDaoImpl
  extends SimpleDAO<DcbbConfigIP>
  implements IDcbbConfigIPDao
{
  public DcbbConfigIPDaoImpl()
  {
    super(DcbbConfigIP.class, DcbbConfigIP.class.getName());
  }
  
  public void save(DcbbConfigIP configIP)
  {
    if ((configIP != null) && (configIP.getLogLevelType() == null)) {
      configIP.setLogLevelType("DEBUG");
    } else {
      DcbbLOGGER.setLoglevel(configIP.getLogLevelType());
    }
    try
    {
      super.persist(configIP);
    }
    catch (PersistenceRuntimeException e) {}
  }
  
  public List<DcbbConfigIP> getAll()
  {
    List<DcbbConfigIP> dcbbConfigIPList = null;
    try
    {
      dcbbConfigIPList = super.loadAll();
    }
    catch (PersistenceRuntimeException e) {}
    return dcbbConfigIPList;
  }
  
  public DcbbConfigIP get(Id id)
  {
    DcbbConfigIP dcbbConfigIP = null;
    try
    {
      dcbbConfigIP = (DcbbConfigIP)super.loadById(id);
    }
    catch (KeyNotFoundException e) {}catch (PersistenceRuntimeException e) {}
    return dcbbConfigIP;
  }
}
