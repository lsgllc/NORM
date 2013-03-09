/**
 * WsIndiStopCodesData_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.v21.admin.data;

public class WsIndiStopCodesData_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public WsIndiStopCodesData_Ser(
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
        WsIndiStopCodesData bean = (WsIndiStopCodesData) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_10_188;
          propValue = bean.getIndiFieldValue();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_10_178;
          propValue = bean.getIndiName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_10_192;
          propValue = bean.getIndiStopCode();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_10_193;
          propValue = bean.getIndiTransCode();
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
    private final static javax.xml.namespace.QName QName_10_192 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.admin.v21.server.rts.isd.txdot.com",
                  "indiStopCode");
    private final static javax.xml.namespace.QName QName_10_178 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.admin.v21.server.rts.isd.txdot.com",
                  "indiName");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_10_193 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.admin.v21.server.rts.isd.txdot.com",
                  "indiTransCode");
    private final static javax.xml.namespace.QName QName_10_188 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.admin.v21.server.rts.isd.txdot.com",
                  "indiFieldValue");
}
