package com.txdot.isd.rts.webservices.trans;

public interface RtsTransService_SEI extends java.rmi.Remote
{
 public com.txdot.isd.rts.webservices.trans.data.RtsTransResponse[] processDataV1(com.txdot.isd.rts.webservices.trans.data.RtsTransRequestV1[] aarrRequest);
 public com.txdot.isd.rts.webservices.trans.data.RtsTransResponse[] processData(com.txdot.isd.rts.webservices.trans.data.RtsTransRequest[] aarrRequest);
}