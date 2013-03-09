/** * RtsInvRequest_Ser.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf150720.02 v7507160841 */package com.txdot.isd.rts.webservices.inv.data;/* &RtsInvRequest_Ser& */public class RtsInvRequest_Ser extends com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest_Ser {    /**     * Constructor     *//* &RtsInvRequest_Ser.RtsInvRequest_Ser& */    public RtsInvRequest_Ser(           java.lang.Class _javaType,             javax.xml.namespace.QName _xmlType,            com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {        super(_javaType, _xmlType, _typeDesc);    }/* &RtsInvRequest_Ser.serialize& */    public void serialize(        javax.xml.namespace.QName name,        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        context.startElement(name, addAttributes(attributes,value,context));        addElements(value,context);        context.endElement();    }/* &RtsInvRequest_Ser.addAttributes& */    protected org.xml.sax.Attributes addAttributes(        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        attributes = super.addAttributes(attributes,value,context);        return attributes;    }/* &RtsInvRequest_Ser.addElements& */    protected void addElements(        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        super.addElements(value,context);        RtsInvRequest bean = (RtsInvRequest) value;        java.lang.Object propValue;        javax.xml.namespace.QName propQName;        {          propQName = QName_2_6;          propValue = new Integer(bean.getItmYr());          context.serialize(propQName, null,               propValue,               QName_1_4,              true,null);          propQName = QName_2_7;          propValue = bean.getItmCd();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_2_8;          propValue = bean.getItmNo();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_2_9;          propValue = bean.getManufacturingPltNo();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_2_10;          propValue = bean.getRegPltNo();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_2_11;          propValue = new Integer(bean.getRequestingOfcIssuanceNo());          context.serialize(propQName, null,               propValue,               QName_1_4,              true,null);          propQName = QName_2_12;          propValue = new Boolean(bean.isFromReserveFlag());          context.serialize(propQName, null,               propValue,               QName_1_15,              true,null);          propQName = QName_2_13;          propValue = new Boolean(bean.isIsaFlg());          context.serialize(propQName, null,               propValue,               QName_1_15,              true,null);          propQName = QName_2_14;          propValue = new Boolean(bean.isPlpFlag());          context.serialize(propQName, null,               propValue,               QName_1_15,              true,null);        }    }/* &RtsInvRequest_Ser'QName_2_13& */    private final static javax.xml.namespace.QName QName_2_13 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.inv.webservices.rts.isd.txdot.com",                  "isaFlg");/* &RtsInvRequest_Ser'QName_2_11& */    private final static javax.xml.namespace.QName QName_2_11 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.inv.webservices.rts.isd.txdot.com",                  "requestingOfcIssuanceNo");/* &RtsInvRequest_Ser'QName_2_10& */    private final static javax.xml.namespace.QName QName_2_10 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.inv.webservices.rts.isd.txdot.com",                  "regPltNo");/* &RtsInvRequest_Ser'QName_2_12& */    private final static javax.xml.namespace.QName QName_2_12 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.inv.webservices.rts.isd.txdot.com",                  "fromReserveFlag");/* &RtsInvRequest_Ser'QName_1_15& */    private final static javax.xml.namespace.QName QName_1_15 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://www.w3.org/2001/XMLSchema",                  "boolean");/* &RtsInvRequest_Ser'QName_2_9& */    private final static javax.xml.namespace.QName QName_2_9 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.inv.webservices.rts.isd.txdot.com",                  "manufacturingPltNo");/* &RtsInvRequest_Ser'QName_1_4& */    private final static javax.xml.namespace.QName QName_1_4 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://www.w3.org/2001/XMLSchema",                  "int");/* &RtsInvRequest_Ser'QName_2_6& */    private final static javax.xml.namespace.QName QName_2_6 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.inv.webservices.rts.isd.txdot.com",                  "itmYr");/* &RtsInvRequest_Ser'QName_1_5& */    private final static javax.xml.namespace.QName QName_1_5 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://www.w3.org/2001/XMLSchema",                  "string");/* &RtsInvRequest_Ser'QName_2_8& */    private final static javax.xml.namespace.QName QName_2_8 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.inv.webservices.rts.isd.txdot.com",                  "itmNo");/* &RtsInvRequest_Ser'QName_2_14& */    private final static javax.xml.namespace.QName QName_2_14 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.inv.webservices.rts.isd.txdot.com",                  "plpFlag");/* &RtsInvRequest_Ser'QName_2_7& */    private final static javax.xml.namespace.QName QName_2_7 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.inv.webservices.rts.isd.txdot.com",                  "itmCd");}/* #RtsInvRequest_Ser# */