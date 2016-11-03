package com.blackboard.bblogger2.bbbridge.web.dao;

import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceRuntimeException;
import com.blackboard.bblogger2.web.domain.DcbbConfigIP;
import java.util.List;

public abstract interface IDcbbConfigIPDao
{
  public abstract void save(DcbbConfigIP paramDcbbConfigIP)
    throws PersistenceRuntimeException;
  
  public abstract List<DcbbConfigIP> getAll()
    throws PersistenceRuntimeException;
  
  public abstract DcbbConfigIP get(Id paramId)
    throws KeyNotFoundException;
}
