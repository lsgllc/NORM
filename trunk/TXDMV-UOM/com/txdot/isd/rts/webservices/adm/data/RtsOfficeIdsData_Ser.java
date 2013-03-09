/**
 * RtsOfficeIdsData_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.adm.data;

public class RtsOfficeIdsData_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public RtsOfficeIdsData_Ser(
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
        RtsOfficeIdsData bean = (RtsOfficeIdsData) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_3_29;
          propValue = new Integer(bean.getNumber());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_30;
          propValue = bean.getCountyName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_31;
          propValue = bean.getEmailAddress();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_32;
          propValue = bean.getPhoneNumber();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_33;
          propValue = bean.getTacName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_34;
          propValue = bean.getMailingAddress();
          context.serialize(propQName, null, 
              propValue, 
              QName_0_36,
              true,null);
          propQName = QName_3_35;
          propValue = bean.getPhysicalAddress();
          context.serialize(propQName, null, 
              propValue, 
              QName_0_36,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_3_32 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "phoneNumber");
    private final static javax.xml.namespace.QName QName_3_30 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "countyName");
    private final static javax.xml.namespace.QName QName_3_29 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "number");
    private final static javax.xml.namespace.QName QName_3_35 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "physicalAddress");
    private final static javax.xml.namespace.QName QName_3_34 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "mailingAddress");
    private final static javax.xml.namespace.QName QName_3_31 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "emailAddress");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_0_36 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.webservices.rts.isd.txdot.com",
                  "RtsAddress");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_3_33 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "tacName");
}
