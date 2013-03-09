package com.txdot.isd.rts.server.v21.admin;

public interface AdminIndiStopCodesList_SEI extends java.rmi.Remote
{
 public com.txdot.isd.rts.server.v21.admin.data.WsIndiStopCodesRes getIndiStopCodesListVerA(int aiV21UniqueId);
 public com.txdot.isd.rts.server.v21.admin.data.WsIndiStopCodesData[] getIndiStopCodesList();
}