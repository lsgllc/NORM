/** * WsDocTypesData_Ser.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf150720.02 v7507160841 */package com.txdot.isd.rts.server.v21.admin.data;/* &WsDocTypesData_Ser& */public class WsDocTypesData_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {    /**     * Constructor     *//* &WsDocTypesData_Ser.WsDocTypesData_Ser& */    public WsDocTypesData_Ser(           java.lang.Class _javaType,             javax.xml.namespace.QName _xmlType,            com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {        super(_javaType, _xmlType, _typeDesc);    }/* &WsDocTypesData_Ser.serialize& */    public void serialize(        javax.xml.namespace.QName name,        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        context.startElement(name, addAttributes(attributes,value,context));        addElements(value,context);        context.endElement();    }/* &WsDocTypesData_Ser.addAttributes& */    protected org.xml.sax.Attributes addAttributes(        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        return attributes;    }/* &WsDocTypesData_Ser.addElements& */    protected void addElements(        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        WsDocTypesData bean = (WsDocTypesData) value;        java.lang.Object propValue;        javax.xml.namespace.QName propQName;        {          propQName = QName_10_182;          propValue = new Integer(bean.getDocTypeCd());          context.serialize(propQName, null,               propValue,               QName_1_4,              true,null);          propQName = QName_10_183;          propValue = bean.getDocTypeCdDesc();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_10_184;          propValue = new Integer(bean.getRegRecIndi());          context.serialize(propQName, null,               propValue,               QName_1_4,              true,null);        }    }/* &WsDocTypesData_Ser'QName_10_184& */    private final static javax.xml.namespace.QName QName_10_184 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.admin.v21.server.rts.isd.txdot.com",                  "regRecIndi");/* &WsDocTypesData_Ser'QName_1_4& */    private final static javax.xml.namespace.QName QName_1_4 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://www.w3.org/2001/XMLSchema",                  "int");/* &WsDocTypesData_Ser'QName_10_183& */    private final static javax.xml.namespace.QName QName_10_183 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.admin.v21.server.rts.isd.txdot.com",                  "docTypeCdDesc");/* &WsDocTypesData_Ser'QName_1_5& */    private final static javax.xml.namespace.QName QName_1_5 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://www.w3.org/2001/XMLSchema",                  "string");/* &WsDocTypesData_Ser'QName_10_182& */    private final static javax.xml.namespace.QName QName_10_182 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.admin.v21.server.rts.isd.txdot.com",                  "docTypeCd");}/* #WsDocTypesData_Ser# */