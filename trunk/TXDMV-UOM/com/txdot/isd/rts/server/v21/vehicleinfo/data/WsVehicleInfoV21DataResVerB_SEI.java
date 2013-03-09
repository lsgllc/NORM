package com.txdot.isd.rts.server.v21.vehicleinfo.data;

public interface WsVehicleInfoV21DataResVerB_SEI extends java.rmi.Remote
{
 public java.lang.String getMake();
 public void setTtlIssueDate(int aiTtlIssueDate);
 public void setModelYear(int i);
 public java.lang.String getVIN();
 public java.lang.String getModel();
 public int getTtlIssueDate();
 public void setMake(java.lang.String string);
 public int getRegExpYear();
 public void setPlateNumber(java.lang.String string);
 public java.lang.String getDocumentNumber();
 public void setPlateStatus(java.lang.String string);
 public int getModelYear();
 public com.txdot.isd.rts.server.v21.vehicleinfo.data.WsVehicleInfoV21Indicator[] getIndicators();
 public void setV21ReqId(int aiV21ReqId);
 public void setRegExpYear(int i);
 public void setNameOnTitleLine2(java.lang.String string);
 public void setResult(int aiErrorCode);
 public java.lang.String getPlateStatus();
 public java.lang.String getPlateType();
 public java.lang.String getBodyStyle();
 public void setSpecialRegId(long alSpecialRegId);
 public long getSpecialRegId();
 public void setPlateType(java.lang.String string);
 public int getV21ReqId();
 public int getPlateCreatedDate();
 public int getV21UniqueId();
 public void setIndicators(com.txdot.isd.rts.server.v21.vehicleinfo.data.WsVehicleInfoV21Indicator[] aarrIndicators);
 public void setRegClassCode(int i);
 public void setRegExpMonth(int i);
 public int getRegClassCode();
 public void setResult(java.lang.String asResult);
 public void setPlateCreatedDate(int aiPlateCreatedDate);
 public java.lang.String getPlateNumber();
 public void setModel(java.lang.String string);
 public void setVIN(java.lang.String string);
 public java.lang.String getResult();
 public void setNameOnTitleLine1(java.lang.String string);
 public void setV21UniqueId(int aiV21UniqueId);
 public void setBodyStyle(java.lang.String string);
 public void setDocumentNumber(java.lang.String string);
 public java.lang.String getNameOnTitleLine2();
 public int getRegExpMonth();
 public java.lang.String getNameOnTitleLine1();
}