/** * RtsRenewalRcptResponse_Ser.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf150720.02 v7507160841 */package com.txdot.isd.rts.webservices.ren.data;/* &RtsRenewalRcptResponse_Ser& */public class RtsRenewalRcptResponse_Ser extends com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse_Ser {    /**     * Constructor     *//* &RtsRenewalRcptResponse_Ser.RtsRenewalRcptResponse_Ser& */    public RtsRenewalRcptResponse_Ser(           java.lang.Class _javaType,             javax.xml.namespace.QName _xmlType,            com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {        super(_javaType, _xmlType, _typeDesc);    }/* &RtsRenewalRcptResponse_Ser.serialize& */    public void serialize(        javax.xml.namespace.QName name,        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        context.startElement(name, addAttributes(attributes,value,context));        addElements(value,context);        context.endElement();    }/* &RtsRenewalRcptResponse_Ser.addAttributes& */    protected org.xml.sax.Attributes addAttributes(        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        attributes = super.addAttributes(attributes,value,context);        return attributes;    }/* &RtsRenewalRcptResponse_Ser.addElements& */    protected void addElements(        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        super.addElements(value,context);        RtsRenewalRcptResponse bean = (RtsRenewalRcptResponse) value;        java.lang.Object propValue;        javax.xml.namespace.QName propQName;        {          propQName = QName_6_180;          propValue = bean.getReceiptUrl();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }        }    }/* &RtsRenewalRcptResponse_Ser'QName_6_180& */    private final static javax.xml.namespace.QName QName_6_180 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.ren.webservices.rts.isd.txdot.com",                  "receiptUrl");/* &RtsRenewalRcptResponse_Ser'QName_1_5& */    private final static javax.xml.namespace.QName QName_1_5 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://www.w3.org/2001/XMLSchema",                  "string");}/* #RtsRenewalRcptResponse_Ser# */