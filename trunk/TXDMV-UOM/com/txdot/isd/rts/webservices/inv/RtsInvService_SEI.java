package com.txdot.isd.rts.webservices.inv;

public interface RtsInvService_SEI extends java.rmi.Remote
{
 public com.txdot.isd.rts.webservices.inv.data.RtsInvResponse[] processData(com.txdot.isd.rts.webservices.inv.data.RtsInvRequest[] aarrRequest);
}