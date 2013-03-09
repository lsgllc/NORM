package com.txdot.isd.rts.server.v21.admin;


public interface AdminClassToPlateList_SEI extends java.rmi.Remote
{
  public com.txdot.isd.rts.server.v21.admin.data.WsClassToPlateRes getClassToPlateListVerA(int aiV21UniqueId);
  public com.txdot.isd.rts.server.v21.admin.data.WsClassToPlateResVerB getClassToPlateListVerB(int aiV21UniqueId);
}