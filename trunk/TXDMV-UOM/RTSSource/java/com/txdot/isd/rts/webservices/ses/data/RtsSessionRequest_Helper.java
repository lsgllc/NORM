/** * RtsSessionRequest_Helper.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf150720.02 v7507160841 */package com.txdot.isd.rts.webservices.ses.data;/* &RtsSessionRequest_Helper& */public class RtsSessionRequest_Helper {    // Type metadata/* &RtsSessionRequest_Helper'typeDesc& */    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsSessionRequest.class);    static {        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();        field.setFieldName("agntSecrtyIdntyNo");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ses.webservices.rts.isd.txdot.com", "agntSecrtyIdntyNo"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));        typeDesc.addFieldDesc(field);        field = new com.ibm.ws.webservices.engine.description.ElementDesc();        field.setFieldName("pswd");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ses.webservices.rts.isd.txdot.com", "pswd"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));        typeDesc.addFieldDesc(field);        field = new com.ibm.ws.webservices.engine.description.ElementDesc();        field.setFieldName("requestorIpAddr");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ses.webservices.rts.isd.txdot.com", "requestorIpAddr"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));        typeDesc.addFieldDesc(field);/* &RtsSessionRequest_Helper'field& */        field = new com.ibm.ws.webservices.engine.description.ElementDesc();/* &RtsSessionRequest_Helper'ame& */        field.setFieldName("dmvUserIndi");/* &RtsSessionRequest_Helper'ibm.ws.webservices.engine.utils.QNameTable.createQName& */        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ses.webservices.rts.isd.txdot.com", "dmvUserIndi"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));        typeDesc.addFieldDesc(field);    };    /**     * Return type metadata object     *//* &RtsSessionRequest_Helper.getTypeDesc& */    public static com.ibm.ws.webservices.engine.description.TypeDesc getTypeDesc() {        return typeDesc;    }    /**     * Get Custom Serializer     *//* &RtsSessionRequest_Helper.getSerializer& */    public static com.ibm.ws.webservices.engine.encoding.Serializer getSerializer(           java.lang.String mechType,            java.lang.Class javaType,             javax.xml.namespace.QName xmlType) {        return           new RtsSessionRequest_Ser(            javaType, xmlType, typeDesc);    };    /**     * Get Custom Deserializer     *//* &RtsSessionRequest_Helper.getDeserializer& */    public static com.ibm.ws.webservices.engine.encoding.Deserializer getDeserializer(           java.lang.String mechType,            java.lang.Class javaType,             javax.xml.namespace.QName xmlType) {        return           new RtsSessionRequest_Deser(            javaType, xmlType, typeDesc);    };}/* #RtsSessionRequest_Helper# */