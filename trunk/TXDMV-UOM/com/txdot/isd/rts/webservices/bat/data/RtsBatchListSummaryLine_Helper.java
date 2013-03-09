/**
 * RtsBatchListSummaryLine_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.bat.data;

public class RtsBatchListSummaryLine_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsBatchListSummaryLine.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("agncyBatchIdntyNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "agncyBatchIdntyNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("agncyIdntyNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "agncyIdntyNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("agncyName1");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "agncyName1"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("submitAgntSecrtyIdntyNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "submitAgntSecrtyIdntyNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("agntUserName");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "agntUserName"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("batchCompltDate");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "batchCompltDate"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("batchCompltTime");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "batchCompltTime"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("batchCompltTimeStamp");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "batchCompltTimeStamp"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("batchInitDate");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "batchInitDate"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("batchInitTime");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "batchInitTime"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("batchInitTimeStamp");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "batchInitTimeStamp"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("batchStatusCd");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "batchStatusCd"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("batchStatusDesc");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "batchStatusDesc"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("ofcIssuanceNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "ofcIssuanceNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("ofcName");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "ofcName"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("renewalRequestCnt");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "renewalRequestCnt"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("reprintCnt");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "reprintCnt"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("totalDollars");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "totalDollars"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("voidCnt");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "voidCnt"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("approveAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "approveAccs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("submitAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "submitAccs"));
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
          new RtsBatchListSummaryLine_Ser(
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
          new RtsBatchListSummaryLine_Deser(
            javaType, xmlType, typeDesc);
    };

}
