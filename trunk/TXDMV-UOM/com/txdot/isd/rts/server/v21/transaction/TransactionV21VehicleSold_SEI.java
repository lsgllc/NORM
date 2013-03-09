package com.txdot.isd.rts.server.v21.transaction;

public interface TransactionV21VehicleSold_SEI extends java.rmi.Remote
{
 public com.txdot.isd.rts.server.v21.transaction.data.WsVehicleSoldV21ResData[] createVehicleTransferNotification(java.lang.String asPlateNumber,java.lang.String asVTNSource,int aiRegExpYear,int aiRegExpMonth,java.lang.String asDocumentNumber,int aiSoldDate,long alSpecialRegID);
 public com.txdot.isd.rts.server.v21.transaction.data.WsVehicleSoldV21ResDataVerB[] createVehicleTransferNotificationVerB(java.lang.String asPlateNumber,java.lang.String asVTNSource,int aiRegExpYear,int aiRegExpMonth,java.lang.String asDocumentNumber,int aiSoldDate,long alSpecialRegID,int aiV21UniqueId);
 public com.txdot.isd.rts.server.v21.transaction.data.WsVehicleSoldV21ResData[] createVehicleTransferNotificationVerA(java.lang.String asPlateNumber,java.lang.String asVTNSource,int aiRegExpYear,int aiRegExpMonth,java.lang.String asDocumentNumber,int aiSoldDate,long alSpecialRegID,int aiV21UniqueId);
}