/** * RtsVehicleData_Ser.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf150720.02 v7507160841 */package com.txdot.isd.rts.webservices.ren.data;/* &RtsVehicleData_Ser& */public class RtsVehicleData_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {    /**     * Constructor     *//* &RtsVehicleData_Ser.RtsVehicleData_Ser& */    public RtsVehicleData_Ser(           java.lang.Class _javaType,             javax.xml.namespace.QName _xmlType,            com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {        super(_javaType, _xmlType, _typeDesc);    }/* &RtsVehicleData_Ser.serialize& */    public void serialize(        javax.xml.namespace.QName name,        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        context.startElement(name, addAttributes(attributes,value,context));        addElements(value,context);        context.endElement();    }/* &RtsVehicleData_Ser.addAttributes& */    protected org.xml.sax.Attributes addAttributes(        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        return attributes;    }/* &RtsVehicleData_Ser.addElements& */    protected void addElements(        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        RtsVehicleData bean = (RtsVehicleData) value;        java.lang.Object propValue;        javax.xml.namespace.QName propQName;        {          propQName = QName_3_80;          propValue = bean.getVehMk();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_3_81;          propValue = bean.getVehModl();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_3_82;          propValue = new Integer(bean.getVehModlYr());          context.serialize(propQName, null,               propValue,               QName_1_4,              true,null);          propQName = QName_3_83;          propValue = bean.getVIN();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }        }    }/* &RtsVehicleData_Ser'QName_3_83& */    private final static javax.xml.namespace.QName QName_3_83 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.ren.webservices.rts.isd.txdot.com",                  "VIN");/* &RtsVehicleData_Ser'QName_3_81& */    private final static javax.xml.namespace.QName QName_3_81 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.ren.webservices.rts.isd.txdot.com",                  "vehModl");/* &RtsVehicleData_Ser'QName_1_4& */    private final static javax.xml.namespace.QName QName_1_4 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://www.w3.org/2001/XMLSchema",                  "int");/* &RtsVehicleData_Ser'QName_1_5& */    private final static javax.xml.namespace.QName QName_1_5 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://www.w3.org/2001/XMLSchema",                  "string");/* &RtsVehicleData_Ser'QName_3_80& */    private final static javax.xml.namespace.QName QName_3_80 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.ren.webservices.rts.isd.txdot.com",                  "vehMk");/* &RtsVehicleData_Ser'QName_3_82& */    private final static javax.xml.namespace.QName QName_3_82 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.ren.webservices.rts.isd.txdot.com",                  "vehModlYr");}/* #RtsVehicleData_Ser# */