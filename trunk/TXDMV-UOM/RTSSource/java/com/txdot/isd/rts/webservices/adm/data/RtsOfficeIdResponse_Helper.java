/** * RtsOfficeIdResponse_Helper.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf150720.02 v7507160841 */package com.txdot.isd.rts.webservices.adm.data;/* &RtsOfficeIdResponse_Helper& */public class RtsOfficeIdResponse_Helper {    // Type metadata/* &RtsOfficeIdResponse_Helper'typeDesc& */    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsOfficeIdResponse.class);    static {/* &RtsOfficeIdResponse_Helper'field& *//* &RtsOfficeIdResponse_Helper'& */        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();/* &RtsOfficeIdResponse_Helper'webservices.engine.utils.QNameTable.createQName& */        field.setFieldName("officeData");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.adm.webservices.rts.isd.txdot.com", "officeData"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.adm.webservices.rts.isd.txdot.com", "RtsOfficeIdsData"));        typeDesc.addFieldDesc(field);    };    /**     * Return type metadata object     *//* &RtsOfficeIdResponse_Helper.getTypeDesc& */    public static com.ibm.ws.webservices.engine.description.TypeDesc getTypeDesc() {        return typeDesc;    }    /**     * Get Custom Serializer     *//* &RtsOfficeIdResponse_Helper.getSerializer& */    public static com.ibm.ws.webservices.engine.encoding.Serializer getSerializer(           java.lang.String mechType,            java.lang.Class javaType,             javax.xml.namespace.QName xmlType) {        return           new RtsOfficeIdResponse_Ser(            javaType, xmlType, typeDesc);    };    /**     * Get Custom Deserializer     *//* &RtsOfficeIdResponse_Helper.getDeserializer& */    public static com.ibm.ws.webservices.engine.encoding.Deserializer getDeserializer(           java.lang.String mechType,            java.lang.Class javaType,             javax.xml.namespace.QName xmlType) {        return           new RtsOfficeIdResponse_Deser(            javaType, xmlType, typeDesc);    };}/* #RtsOfficeIdResponse_Helper# */