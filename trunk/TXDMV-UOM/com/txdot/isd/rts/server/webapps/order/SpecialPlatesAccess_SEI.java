package com.txdot.isd.rts.server.webapps.order;

public interface SpecialPlatesAccess_SEI extends java.rmi.Remote
{
 public com.txdot.isd.rts.server.webapps.order.common.data.DefaultResponse vehicleValidation(com.txdot.isd.rts.server.webapps.order.vehiclevalidation.data.VehicleValidationRequest aaVVRequestObj);
 public com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoResponse[] specialPlateInfo(com.txdot.isd.rts.server.webapps.order.specialplateinfo.data.SpecialPlatesInfoRequest aaInfoRequestObj);
 public com.txdot.isd.rts.server.webapps.order.virtualinv.data.VirtualInvResponse[] virtualInventoryAccess(com.txdot.isd.rts.server.webapps.order.virtualinv.data.VirtualInvRequest[] aaVIRequestObj);
 public com.txdot.isd.rts.server.webapps.order.common.data.DefaultResponse[] transactionAccess(com.txdot.isd.rts.server.webapps.order.transaction.data.TransactionRequest[] aaTransRequestObj);
 public com.txdot.isd.rts.server.webapps.order.countyinfo.data.CountyInfoResponse[] countyInfo(com.txdot.isd.rts.server.webapps.order.countyinfo.data.CountyInfoRequest aaCntyInfoRequestObj);
}