package com.txdot.isd.rts.webservices.dsabldplcrd;


public interface RtsDsabldPlcrdService_SEI extends java.rmi.Remote
{
  public com.txdot.isd.rts.webservices.dsabldplcrd.data.RtsDsabldPlcrdResponse[] getDsabldPlcrd(com.txdot.isd.rts.webservices.dsabldplcrd.data.RtsDsabldPlcrdRequest[] aarrRequest);
  public com.txdot.isd.rts.webservices.dsabldplcrd.data.RtsDsabldPlcrdResponse[] getDsabldPlcrdA(int aiAction,java.lang.String asCaller,java.lang.String asSessionId,int aiVersionNo,java.lang.String asInvItmNo);
}