/**
 * WsClassToPlateData_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.v21.admin.data;

public class WsClassToPlateData_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public WsClassToPlateData_Ser(
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
        WsClassToPlateData bean = (WsClassToPlateData) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_10_471;
          propValue = new Integer(bean.getPTOEligibleIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_10_472;
          propValue = new Integer(bean.getRegistrationClassCode());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_10_473;
          propValue = bean.getRegistrationPlateCode();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_10_474;
          propValue = bean.getReplacementPlateCode();
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
    private final static javax.xml.namespace.QName QName_10_474 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.admin.v21.server.rts.isd.txdot.com",
                  "replacementPlateCode");
    private final static javax.xml.namespace.QName QName_10_472 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.admin.v21.server.rts.isd.txdot.com",
                  "registrationClassCode");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_10_473 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.admin.v21.server.rts.isd.txdot.com",
                  "registrationPlateCode");
    private final static javax.xml.namespace.QName QName_10_471 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.admin.v21.server.rts.isd.txdot.com",
                  "PTOEligibleIndi");
}
