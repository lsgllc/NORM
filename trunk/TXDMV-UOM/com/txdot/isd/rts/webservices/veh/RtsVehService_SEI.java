package com.txdot.isd.rts.webservices.veh;

public interface RtsVehService_SEI extends java.rmi.Remote
{
 public com.txdot.isd.rts.webservices.veh.data.RtsVehResponse[] processData(com.txdot.isd.rts.webservices.veh.data.RtsVehRequest[] aarrRequest);
}