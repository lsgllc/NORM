/** * WsVehicleInfoV21Indicator_Helper.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf150720.02 v7507160841 */package com.txdot.isd.rts.server.v21.vehicleinfo.data;/* &WsVehicleInfoV21Indicator_Helper& */public class WsVehicleInfoV21Indicator_Helper {    // Type metadata/* &WsVehicleInfoV21Indicator_Helper'typeDesc& */    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =        new com.ibm.ws.webservices.engine.description.TypeDesc(WsVehicleInfoV21Indicator.class);    static {        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();        field.setFieldName("indiName");        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "indiName"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));        typeDesc.addFieldDesc(field);/* &WsVehicleInfoV21Indicator_Helper'field& */        field = new com.ibm.ws.webservices.engine.description.ElementDesc();        field.setFieldName("indiValue");/* &WsVehicleInfoV21Indicator_Helper'& *//* &WsVehicleInfoV21Indicator_Helper'utils.QNameTable.createQName& */        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "indiValue"));        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));        typeDesc.addFieldDesc(field);    };    /**     * Return type metadata object     *//* &WsVehicleInfoV21Indicator_Helper.getTypeDesc& */    public static com.ibm.ws.webservices.engine.description.TypeDesc getTypeDesc() {        return typeDesc;    }    /**     * Get Custom Serializer     *//* &WsVehicleInfoV21Indicator_Helper.getSerializer& */    public static com.ibm.ws.webservices.engine.encoding.Serializer getSerializer(           java.lang.String mechType,            java.lang.Class javaType,             javax.xml.namespace.QName xmlType) {        return           new WsVehicleInfoV21Indicator_Ser(            javaType, xmlType, typeDesc);    };    /**     * Get Custom Deserializer     *//* &WsVehicleInfoV21Indicator_Helper.getDeserializer& */    public static com.ibm.ws.webservices.engine.encoding.Deserializer getDeserializer(           java.lang.String mechType,            java.lang.Class javaType,             javax.xml.namespace.QName xmlType) {        return           new WsVehicleInfoV21Indicator_Deser(            javaType, xmlType, typeDesc);    };}/* #WsVehicleInfoV21Indicator_Helper# */