/**
 * RtsBatchListResponse_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.bat.data;

public class RtsBatchListResponse_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsBatchListResponse.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("batchSummaryLine");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "batchSummaryLine"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "RtsBatchListSummaryLine"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("searchBatchStatusCd");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "searchBatchStatusCd"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("searchEndDate");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "searchEndDate"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("searchStartDate");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "searchStartDate"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("transWsId");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "transWsId"));
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
          new RtsBatchListResponse_Ser(
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
          new RtsBatchListResponse_Deser(
            javaType, xmlType, typeDesc);
    };

}
