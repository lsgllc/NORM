/** * RtsRenewalRcptResponse_Helper.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf150720.02 v7507160841 */package com.txdot.isd.rts.webservices.ren.data;/* &RtsRenewalRcptResponse_Helper& */public class RtsRenewalRcptResponse_Helper {    // Type metadata/* &RtsRenewalRcptResponse_Helper'typeDesc& */    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsRenewalRcptResponse.class);    static {/* &RtsRenewalRcptResponse_Helper'field& *//* &RtsRenewalRcptResponse_Helper'ptUrl& */        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();/* &RtsRenewalRcptResponse_Helper'es.engine.utils.QNameTable.createQName& */        field.setFieldName("receiptUrl");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "receiptUrl"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));        typeDesc.addFieldDesc(field);    };    /**     * Return type metadata object     *//* &RtsRenewalRcptResponse_Helper.getTypeDesc& */    public static com.ibm.ws.webservices.engine.description.TypeDesc getTypeDesc() {        return typeDesc;    }    /**     * Get Custom Serializer     *//* &RtsRenewalRcptResponse_Helper.getSerializer& */    public static com.ibm.ws.webservices.engine.encoding.Serializer getSerializer(           java.lang.String mechType,            java.lang.Class javaType,             javax.xml.namespace.QName xmlType) {        return           new RtsRenewalRcptResponse_Ser(            javaType, xmlType, typeDesc);    };    /**     * Get Custom Deserializer     *//* &RtsRenewalRcptResponse_Helper.getDeserializer& */    public static com.ibm.ws.webservices.engine.encoding.Deserializer getDeserializer(           java.lang.String mechType,            java.lang.Class javaType,             javax.xml.namespace.QName xmlType) {        return           new RtsRenewalRcptResponse_Deser(            javaType, xmlType, typeDesc);    };}/* #RtsRenewalRcptResponse_Helper# */