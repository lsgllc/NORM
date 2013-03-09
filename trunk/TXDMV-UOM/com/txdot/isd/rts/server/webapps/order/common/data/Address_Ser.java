/**
 * Address_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.webapps.order.common.data;

public class Address_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public Address_Ser(
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType, 
           com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {
        super(_javaType, _xmlType, _typeDesc);
    }
    public void serialize(
        javax.xml.namespace.QName name,
        org.xml.sax.Attributes attributes,
        java.lang.Object value,
        com.ibm.ws.webservices.engine.encoding.SerializationContext context)
        throws java.io.IOException
    {
        context.startElement(name, addAttributes(attributes,value,context));
        addElements(value,context);
        context.endElement();
    }
    protected org.xml.sax.Attributes addAttributes(
        org.xml.sax.Attributes attributes,
        java.lang.Object value,
        com.ibm.ws.webservices.engine.encoding.SerializationContext context)
        throws java.io.IOException
    {
        return attributes;
    }
    protected void addElements(
        java.lang.Object value,
        com.ibm.ws.webservices.engine.encoding.SerializationContext context)
        throws java.io.IOException
    {
        Address bean = (Address) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_12_19;
          propValue = bean.getCity();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_12_20;
          propValue = bean.getState();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_12_238;
          propValue = bean.getStreet1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_12_239;
          propValue = bean.getStreet2();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_12_240;
          propValue = bean.getZipCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_12_241;
          propValue = bean.getZipCd4();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_12_242;
          propValue = bean.getCounty();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
        }
    }
    private final static javax.xml.namespace.QName QName_12_240 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "zipCd");
    private final static javax.xml.namespace.QName QName_12_241 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "zipCd4");
    private final static javax.xml.namespace.QName QName_12_20 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "state");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_12_19 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "city");
    private final static javax.xml.namespace.QName QName_12_238 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "street1");
    private final static javax.xml.namespace.QName QName_12_239 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "street2");
    private final static javax.xml.namespace.QName QName_12_242 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "county");
}
