package com.txdot.isd.rts.webservices.bat;


public interface RtsBatchListService_SEI extends java.rmi.Remote
{
  public com.txdot.isd.rts.webservices.bat.data.RtsBatchListResponse[] processData(com.txdot.isd.rts.webservices.bat.data.RtsBatchListRequest[] aarrRequest);
}