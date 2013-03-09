package com.txdot.isd.rts.server.v21.vehicleinfo;

public interface VehicleInfoV21_SEI extends java.rmi.Remote
{
 public com.txdot.isd.rts.server.v21.vehicleinfo.data.WsVehicleInfoV21DataRes[] getVehicleV21(java.lang.String asPlateNumber,java.lang.String asLastFourOfVIN,java.lang.String asDocumentNumber,java.lang.String asVIN);
 public com.txdot.isd.rts.server.v21.vehicleinfo.data.WsVehicleInfoV21DataResVerB[] getVehicleV21VerB(java.lang.String asPlateNumber,java.lang.String asLastFourOfVIN,java.lang.String asDocumentNumber,java.lang.String asVIN,boolean abVinaLookupNeeded,int aiV21UniqueId);
 public com.txdot.isd.rts.server.v21.vehicleinfo.data.WsVehicleInfoV21DataResVerA[] getVehicleV21VerA(java.lang.String asPlateNumber,java.lang.String asLastFourOfVIN,java.lang.String asDocumentNumber,java.lang.String asVIN,boolean abVinaLookupNeeded);
}