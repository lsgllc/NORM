/** * TransactionAccessResponse_Ser.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf150720.02 v7507160841 */package com.txdot.isd.rts.server.webapps.order;/* &TransactionAccessResponse_Ser& */public class TransactionAccessResponse_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {    /**     * Constructor     *//* &TransactionAccessResponse_Ser.TransactionAccessResponse_Ser& */    public TransactionAccessResponse_Ser(           java.lang.Class _javaType,             javax.xml.namespace.QName _xmlType,            com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {        super(_javaType, _xmlType, _typeDesc);    }/* &TransactionAccessResponse_Ser.serialize& */    public void serialize(        javax.xml.namespace.QName name,        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        context.startElement(name, addAttributes(attributes,value,context));        addElements(value,context);        context.endElement();    }/* &TransactionAccessResponse_Ser.addAttributes& */    protected org.xml.sax.Attributes addAttributes(        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        return attributes;    }/* &TransactionAccessResponse_Ser.addElements& */    protected void addElements(        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        TransactionAccessResponse bean = (TransactionAccessResponse) value;        java.lang.Object propValue;        javax.xml.namespace.QName propQName;        {          propQName = QName_14_305;          {            propValue = bean.getTransactionAccessReturn();            if (propValue != null) {              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {                context.serialize(propQName, null,                     java.lang.reflect.Array.get(propValue, i),                     QName_12_306,                    true,null);              }            }          }        }    }/* &TransactionAccessResponse_Ser'QName_12_306& */    private final static javax.xml.namespace.QName QName_12_306 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.common.order.webapps.server.rts.isd.txdot.com",                  "DefaultResponse");/* &TransactionAccessResponse_Ser'QName_14_305& */    private final static javax.xml.namespace.QName QName_14_305 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://order.webapps.server.rts.isd.txdot.com",                  "transactionAccessReturn");}/* #TransactionAccessResponse_Ser# */