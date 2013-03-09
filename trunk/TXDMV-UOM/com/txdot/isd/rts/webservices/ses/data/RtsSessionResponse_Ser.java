/**
 * RtsSessionResponse_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.ses.data;

public class RtsSessionResponse_Ser extends com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse_Ser {
    /**
     * Constructor
     */
    public RtsSessionResponse_Ser(
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
        attributes = super.addAttributes(attributes,value,context);
        return attributes;
    }
    protected void addElements(
        java.lang.Object value,
        com.ibm.ws.webservices.engine.encoding.SerializationContext context)
        throws java.io.IOException
    {
        super.addElements(value,context);
        RtsSessionResponse bean = (RtsSessionResponse) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_3_59;
          propValue = bean.getLastLoginSuccessful();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_31,
              true,null);
          propQName = QName_3_60;
          propValue = bean.getLastLoginUnsuccessful();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_31,
              true,null);
          propQName = QName_3_61;
          {
            propValue = bean.getRtsWebAgncy();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_2_7,
                    true,null);
              }
            }
          }
          propQName = QName_3_62;
          {
            propValue = bean.getRtsWebAgntWs();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_4_63,
                    true,null);
              }
            }
          }
        }
    }
    private final static javax.xml.namespace.QName QName_3_62 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ses.webservices.rts.isd.txdot.com",
                  "rtsWebAgntWs");
    private final static javax.xml.namespace.QName QName_3_60 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ses.webservices.rts.isd.txdot.com",
                  "lastLoginUnsuccessful");
    private final static javax.xml.namespace.QName QName_4_63 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "RtsWebAgntWS");
    private final static javax.xml.namespace.QName QName_1_31 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "dateTime");
    private final static javax.xml.namespace.QName QName_2_7 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "RtsWebAgncy");
    private final static javax.xml.namespace.QName QName_3_59 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ses.webservices.rts.isd.txdot.com",
                  "lastLoginSuccessful");
    private final static javax.xml.namespace.QName QName_3_61 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ses.webservices.rts.isd.txdot.com",
                  "rtsWebAgncy");
}
