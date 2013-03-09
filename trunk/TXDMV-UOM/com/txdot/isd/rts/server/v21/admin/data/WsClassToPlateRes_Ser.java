/**
 * WsClassToPlateRes_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.v21.admin.data;

public class WsClassToPlateRes_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public WsClassToPlateRes_Ser(
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
        WsClassToPlateRes bean = (WsClassToPlateRes) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_10_468;
          {
            propValue = bean.getClassToPlate();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_10_469,
                    true,null);
              }
            }
          }
          propQName = QName_10_157;
          propValue = new Integer(bean.getV21ReqId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_10_158;
          propValue = new Integer(bean.getV21UniqueId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_10_468 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.admin.v21.server.rts.isd.txdot.com",
                  "classToPlate");
    private final static javax.xml.namespace.QName QName_10_157 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.admin.v21.server.rts.isd.txdot.com",
                  "v21ReqId");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_10_158 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.admin.v21.server.rts.isd.txdot.com",
                  "v21UniqueId");
    private final static javax.xml.namespace.QName QName_10_469 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.admin.v21.server.rts.isd.txdot.com",
                  "WsClassToPlateData");
}
