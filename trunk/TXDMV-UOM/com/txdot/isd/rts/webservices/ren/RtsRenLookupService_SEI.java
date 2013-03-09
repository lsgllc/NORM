package com.txdot.isd.rts.webservices.ren;


public interface RtsRenLookupService_SEI extends java.rmi.Remote
{
  public com.txdot.isd.rts.webservices.ren.data.RtsRenewalResponse[] processData(com.txdot.isd.rts.webservices.ren.data.RtsRenewalRequest[] aarrRequest);
}