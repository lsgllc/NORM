/**
 * RtsWebAppsResponse_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.webapps.data;

public class RtsWebAppsResponse_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsWebAppsResponse.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("epayRcveTimestmp");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "epayRcveTimestmp"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("epaySendTimestmp");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "epaySendTimestmp"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("initReqTimestmp");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "initReqTimestmp"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pymntAmt");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pymntAmt"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://util.services.rts.isd.txdot.com", "Dollar"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("updtTimestmp");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "updtTimestmp"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("addlSetIndi");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "addlSetIndi"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("isaIndi");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "isaIndi"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("itrntPymntStatusCd");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "itrntPymntStatusCd"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("mqUpdtIndi");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "mqUpdtIndi"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltTerm");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pltTerm"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltExpMo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pltExpMo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltExpYr");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pltExpYr"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("regPltNoReqId");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "regPltNoReqId"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("resComptCntyNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "resComptCntyNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("reserveIndi");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "reserveIndi"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("auditTrailTransId");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "auditTrailTransId"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("itrntTranceNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "itrntTranceNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("mfgPltNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "mfgPltNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("orgNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "orgNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltOwnrCity");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pltOwnrCity"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltOwnrEmail");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pltOwnrEmail"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltOwnrName2");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pltOwnrName2"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltOwnrName1");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pltOwnrName1"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltOwnrPhone");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pltOwnrPhone"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltOwnrSt1");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pltOwnrSt1"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltOwnrSt2");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pltOwnrSt2"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltOwnrState");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pltOwnrState"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltOwnrZpCd");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pltOwnrZpCd"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltOwnrZpCd4");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pltOwnrZpCd4"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pymntOrderId");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "pymntOrderId"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("regPltCd");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "regPltCd"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("regPltNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "regPltNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("reqIpAddr");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "reqIpAddr"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("transEmpId");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "transEmpId"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("transStatusCd");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "transStatusCd"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("action");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.webapps.webservices.rts.isd.txdot.com", "action"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
    };

    /**
     * Return type metadata object
     */
    public static com.ibm.ws.webservices.engine.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static com.ibm.ws.webservices.engine.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class javaType,  
           javax.xml.namespace.QName xmlType) {
        return 
          new RtsWebAppsResponse_Ser(
            javaType, xmlType, typeDesc);
    };

    /**
     * Get Custom Deserializer
     */
    public static com.ibm.ws.webservices.engine.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class javaType,  
           javax.xml.namespace.QName xmlType) {
        return 
          new RtsWebAppsResponse_Deser(
            javaType, xmlType, typeDesc);
    };

}
