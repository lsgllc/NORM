/** * RtsBatchListRequest_Helper.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf150720.02 v7507160841 */package com.txdot.isd.rts.webservices.bat.data;/* &RtsBatchListRequest_Helper& */public class RtsBatchListRequest_Helper {    // Type metadata/* &RtsBatchListRequest_Helper'typeDesc& */    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsBatchListRequest.class);    static {        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();        field.setFieldName("agencyBatchIdntyNo");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "agencyBatchIdntyNo"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));        typeDesc.addFieldDesc(field);        field = new com.ibm.ws.webservices.engine.description.ElementDesc();        field.setFieldName("agencyIdntyNo");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "agencyIdntyNo"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));        typeDesc.addFieldDesc(field);        field = new com.ibm.ws.webservices.engine.description.ElementDesc();        field.setFieldName("ofcIssuanceNo");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "ofcIssuanceNo"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));        typeDesc.addFieldDesc(field);        field = new com.ibm.ws.webservices.engine.description.ElementDesc();        field.setFieldName("searchBatchStatusCd");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "searchBatchStatusCd"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));        typeDesc.addFieldDesc(field);        field = new com.ibm.ws.webservices.engine.description.ElementDesc();        field.setFieldName("searchEndDate");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "searchEndDate"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));        typeDesc.addFieldDesc(field);        field = new com.ibm.ws.webservices.engine.description.ElementDesc();        field.setFieldName("searchStartDate");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "searchStartDate"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));        typeDesc.addFieldDesc(field);/* &RtsBatchListRequest_Helper'field& */        field = new com.ibm.ws.webservices.engine.description.ElementDesc();/* &RtsBatchListRequest_Helper'& */        field.setFieldName("transWsId");/* &RtsBatchListRequest_Helper'ebservices.engine.utils.QNameTable.createQName& */        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.bat.webservices.rts.isd.txdot.com", "transWsId"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));        typeDesc.addFieldDesc(field);    };    /**     * Return type metadata object     *//* &RtsBatchListRequest_Helper.getTypeDesc& */    public static com.ibm.ws.webservices.engine.description.TypeDesc getTypeDesc() {        return typeDesc;    }    /**     * Get Custom Serializer     *//* &RtsBatchListRequest_Helper.getSerializer& */    public static com.ibm.ws.webservices.engine.encoding.Serializer getSerializer(           java.lang.String mechType,            java.lang.Class javaType,             javax.xml.namespace.QName xmlType) {        return           new RtsBatchListRequest_Ser(            javaType, xmlType, typeDesc);    };    /**     * Get Custom Deserializer     *//* &RtsBatchListRequest_Helper.getDeserializer& */    public static com.ibm.ws.webservices.engine.encoding.Deserializer getDeserializer(           java.lang.String mechType,            java.lang.Class javaType,             javax.xml.namespace.QName xmlType) {        return           new RtsBatchListRequest_Deser(            javaType, xmlType, typeDesc);    };}/* #RtsBatchListRequest_Helper# */