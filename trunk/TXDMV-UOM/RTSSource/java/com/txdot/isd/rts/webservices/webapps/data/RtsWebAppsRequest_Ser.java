/** * RtsWebAppsRequest_Ser.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf150720.02 v7507160841 */package com.txdot.isd.rts.webservices.webapps.data;/* &RtsWebAppsRequest_Ser& */public class RtsWebAppsRequest_Ser extends com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest_Ser {    /**     * Constructor     *//* &RtsWebAppsRequest_Ser.RtsWebAppsRequest_Ser& */    public RtsWebAppsRequest_Ser(           java.lang.Class _javaType,             javax.xml.namespace.QName _xmlType,            com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {        super(_javaType, _xmlType, _typeDesc);    }/* &RtsWebAppsRequest_Ser.serialize& */    public void serialize(        javax.xml.namespace.QName name,        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        context.startElement(name, addAttributes(attributes,value,context));        addElements(value,context);        context.endElement();    }/* &RtsWebAppsRequest_Ser.addAttributes& */    protected org.xml.sax.Attributes addAttributes(        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        attributes = super.addAttributes(attributes,value,context);        return attributes;    }/* &RtsWebAppsRequest_Ser.addElements& */    protected void addElements(        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        super.addElements(value,context);        RtsWebAppsRequest bean = (RtsWebAppsRequest) value;        java.lang.Object propValue;        javax.xml.namespace.QName propQName;        {          propQName = QName_5_77;          propValue = bean.getItrntTraceNo();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_5_78;          propValue = bean.getPymntOrderId();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_5_10;          propValue = bean.getRegPltNo();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_5_79;          propValue = bean.getReqIpAddr();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }        }    }/* &RtsWebAppsRequest_Ser'QName_5_79& */    private final static javax.xml.namespace.QName QName_5_79 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.webapps.webservices.rts.isd.txdot.com",                  "reqIpAddr");/* &RtsWebAppsRequest_Ser'QName_1_5& */    private final static javax.xml.namespace.QName QName_1_5 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://www.w3.org/2001/XMLSchema",                  "string");/* &RtsWebAppsRequest_Ser'QName_5_10& */    private final static javax.xml.namespace.QName QName_5_10 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.webapps.webservices.rts.isd.txdot.com",                  "regPltNo");/* &RtsWebAppsRequest_Ser'QName_5_77& */    private final static javax.xml.namespace.QName QName_5_77 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.webapps.webservices.rts.isd.txdot.com",                  "itrntTraceNo");/* &RtsWebAppsRequest_Ser'QName_5_78& */    private final static javax.xml.namespace.QName QName_5_78 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.webapps.webservices.rts.isd.txdot.com",                  "pymntOrderId");}/* #RtsWebAppsRequest_Ser# */