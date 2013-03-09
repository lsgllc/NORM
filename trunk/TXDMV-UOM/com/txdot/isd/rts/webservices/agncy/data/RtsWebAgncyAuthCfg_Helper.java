/**
 * RtsWebAgncyAuthCfg_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.agncy.data;

public class RtsWebAgncyAuthCfg_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsWebAgncyAuthCfg.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("agncyAuthIdntyNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "agncyAuthIdntyNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("agncyIdntyNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "agncyIdntyNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("chngTimeStmp");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "chngTimeStmp"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("expPrcsngCd");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "expPrcsngCd"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("expPrcsngMos");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "expPrcsngMos"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("keyEntryCode");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "keyEntryCode"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("maxSubmitCount");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "maxSubmitCount"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("maxSubmitDays");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "maxSubmitDays"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("ofcIssuanceNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "ofcIssuanceNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("ofcName");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "ofcName"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("subconId");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "subconId"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("updtngAgntIdntyNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "updtngAgntIdntyNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("issueInvIndi");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "issueInvIndi"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pilotIndi");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "pilotIndi"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
          new RtsWebAgncyAuthCfg_Ser(
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
          new RtsWebAgncyAuthCfg_Deser(
            javaType, xmlType, typeDesc);
    };

}
