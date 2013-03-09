package com.txdot.isd.rts.server.v21.transaction;

public interface TransactionV21PLD_SEI extends java.rmi.Remote
{
 public com.txdot.isd.rts.server.v21.transaction.data.WsPLDV21ResDataVerB[] createPlateDispositionVerB(java.lang.String asPlateNumber,java.lang.String asVTNSource,int aiPlateDisposition,int aiRegExpYear,int aiRegExpMonth,java.lang.String asDocumentNumber,long alSpecialRegID,int aiV21UniqueId);
 public com.txdot.isd.rts.server.v21.transaction.data.WsPLDV21ResData[] createPlateDispositionVerA(java.lang.String asPlateNumber,java.lang.String asVTNSource,int aiPlateDisposition,int aiRegExpYear,int aiRegExpMonth,java.lang.String asDocumentNumber,long alSpecialRegID,int aiV21UniqueId);
 public com.txdot.isd.rts.server.v21.transaction.data.WsPLDV21ResData[] createPlateDisposition(java.lang.String asPlateNumber,java.lang.String asVTNSource,int aiPlateDisposition,int aiRegExpYear,int aiRegExpMonth,java.lang.String asDocumentNumber,long alSpecialRegID);
}