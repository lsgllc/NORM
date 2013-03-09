package com.txdot.isd.rts.server.v21.admin;

public interface AdminVehicleBodyTypesList_SEI extends java.rmi.Remote
{
 public com.txdot.isd.rts.server.v21.admin.data.WsVehicleBodyTypesData[] getVehicleBodyTypesList();
 public com.txdot.isd.rts.server.v21.admin.data.WsVehicleBodyTypesRes getVehicleBodyTypesListVerA(int aiV21UniqueId);
}