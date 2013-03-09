package com.txdot.isd.rts.webservices.adm;

public interface RtsAdmService_SEI extends java.rmi.Remote
{
 public com.txdot.isd.rts.webservices.adm.data.RtsOfficeIdResponse[] getCountyInfo(com.txdot.isd.rts.webservices.common.data.RtsDefaultRequest[] aarrRequest);
 public com.txdot.isd.rts.webservices.adm.data.RtsPlateTypeResponse[] getPltTypes(com.txdot.isd.rts.webservices.common.data.RtsDefaultRequest[] aarrRequest);
}