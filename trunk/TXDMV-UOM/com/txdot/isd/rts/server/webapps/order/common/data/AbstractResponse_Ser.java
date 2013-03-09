/**
 * AbstractResponse_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.webapps.order.common.data;

public class AbstractResponse_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public AbstractResponse_Ser(
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
        AbstractResponse bean = (AbstractResponse) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_12_226;
          propValue = bean.getAck();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_12_227;
          propValue = bean.getBuild();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_12_228;
          propValue = bean.getErrors();
          context.serialize(propQName, null, 
              propValue, 
              QName_12_230,
              true,null);
          propQName = QName_12_229;
          propValue = bean.getTimestamp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_111,
              true,null);
          propQName = QName_12_224;
          propValue = bean.getVersion();
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
    private final static javax.xml.namespace.QName QName_12_229 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "timestamp");
    private final static javax.xml.namespace.QName QName_12_230 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "Errors");
    private final static javax.xml.namespace.QName QName_12_224 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "version");
    private final static javax.xml.namespace.QName QName_1_111 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "dateTime");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_12_227 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "build");
    private final static javax.xml.namespace.QName QName_12_226 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "ack");
    private final static javax.xml.namespace.QName QName_12_228 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "errors");
}
