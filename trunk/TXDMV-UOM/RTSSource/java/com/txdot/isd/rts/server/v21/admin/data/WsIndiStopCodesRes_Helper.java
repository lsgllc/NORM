/** * WsIndiStopCodesRes_Helper.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf150720.02 v7507160841 */package com.txdot.isd.rts.server.v21.admin.data;/* &WsIndiStopCodesRes_Helper& */public class WsIndiStopCodesRes_Helper {    // Type metadata/* &WsIndiStopCodesRes_Helper'typeDesc& */    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =        new com.ibm.ws.webservices.engine.description.TypeDesc(WsIndiStopCodesRes.class);    static {        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();        field.setFieldName("indiStopCodes");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.admin.v21.server.rts.isd.txdot.com", "indiStopCodes"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.admin.v21.server.rts.isd.txdot.com", "WsIndiStopCodesData"));        typeDesc.addFieldDesc(field);        field = new com.ibm.ws.webservices.engine.description.ElementDesc();        field.setFieldName("v21ReqId");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.admin.v21.server.rts.isd.txdot.com", "v21ReqId"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));        typeDesc.addFieldDesc(field);/* &WsIndiStopCodesRes_Helper'field& */        field = new com.ibm.ws.webservices.engine.description.ElementDesc();/* &WsIndiStopCodesRes_Helper'e& */        field.setFieldName("v21UniqueId");/* &WsIndiStopCodesRes_Helper'.ws.webservices.engine.utils.QNameTable.createQName& */        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.admin.v21.server.rts.isd.txdot.com", "v21UniqueId"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));        typeDesc.addFieldDesc(field);    };    /**     * Return type metadata object     *//* &WsIndiStopCodesRes_Helper.getTypeDesc& */    public static com.ibm.ws.webservices.engine.description.TypeDesc getTypeDesc() {        return typeDesc;    }    /**     * Get Custom Serializer     *//* &WsIndiStopCodesRes_Helper.getSerializer& */    public static com.ibm.ws.webservices.engine.encoding.Serializer getSerializer(           java.lang.String mechType,            java.lang.Class javaType,             javax.xml.namespace.QName xmlType) {        return           new WsIndiStopCodesRes_Ser(            javaType, xmlType, typeDesc);    };    /**     * Get Custom Deserializer     *//* &WsIndiStopCodesRes_Helper.getDeserializer& */    public static com.ibm.ws.webservices.engine.encoding.Deserializer getDeserializer(           java.lang.String mechType,            java.lang.Class javaType,             javax.xml.namespace.QName xmlType) {        return           new WsIndiStopCodesRes_Deser(            javaType, xmlType, typeDesc);    };}/* #WsIndiStopCodesRes_Helper# */