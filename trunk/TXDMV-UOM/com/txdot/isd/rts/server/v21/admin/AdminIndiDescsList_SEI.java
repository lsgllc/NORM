package com.txdot.isd.rts.server.v21.admin;

public interface AdminIndiDescsList_SEI extends java.rmi.Remote
{
 public com.txdot.isd.rts.server.v21.admin.data.WsIndiDescsRes getIndiDescsListVerA(int aiV21UniqueId);
 public com.txdot.isd.rts.server.v21.admin.data.WsIndiDescsData[] getIndiDescsList();
}