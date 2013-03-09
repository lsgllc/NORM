package com.txdot.isd.rts.server.v21.admin;

public interface AdminVehMakesList_SEI extends java.rmi.Remote
{
 public com.txdot.isd.rts.server.v21.admin.data.WsVehicleMakesRes getVehMakeListVerA(int aiV21UniqueId);
 public com.txdot.isd.rts.server.v21.admin.data.WsVehicleMakesData[] getVehMakeList();
}