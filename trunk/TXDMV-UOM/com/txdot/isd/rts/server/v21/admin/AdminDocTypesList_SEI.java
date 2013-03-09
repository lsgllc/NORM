package com.txdot.isd.rts.server.v21.admin;

public interface AdminDocTypesList_SEI extends java.rmi.Remote
{
 public com.txdot.isd.rts.server.v21.admin.data.WsDocTypesRes getDocTypesListVerA(int aiV21UniqueId);
 public com.txdot.isd.rts.server.v21.admin.data.WsDocTypesData[] getDocTypesList();
}