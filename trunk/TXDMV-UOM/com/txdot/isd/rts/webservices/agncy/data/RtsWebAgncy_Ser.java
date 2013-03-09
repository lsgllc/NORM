/**
 * RtsWebAgncy_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.agncy.data;

public class RtsWebAgncy_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public RtsWebAgncy_Ser(
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
        RtsWebAgncy bean = (RtsWebAgncy) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_2_8;
          propValue = bean.getAgncyNameAddress();
          context.serialize(propQName, null, 
              propValue, 
              QName_0_29,
              true,null);
          propQName = QName_2_9;
          {
            propValue = bean.getAgncyAuthCfgs();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_2_30,
                    true,null);
              }
            }
          }
          propQName = QName_2_10;
          propValue = new Integer(bean.getAgncyIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_11;
          propValue = bean.getAgncyTypeCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_12;
          propValue = bean.getChngTimeStmpAgncy();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_31,
              true,null);
          propQName = QName_2_13;
          propValue = bean.getCity();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_14;
          propValue = bean.getCntctName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_15;
          propValue = bean.getEMail();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_16;
          propValue = new Integer(bean.getInitOfcNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_17;
          propValue = bean.getName1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_18;
          propValue = bean.getName2();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_19;
          propValue = bean.getPhone();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_20;
          propValue = bean.getSt1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_21;
          propValue = bean.getSt2();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_22;
          propValue = bean.getState();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_23;
          propValue = bean.getUpdtngUserName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_24;
          propValue = bean.getZpCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_25;
          propValue = bean.getZpCdP4();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_26;
          propValue = new Boolean(bean.isChngAgncy());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_2_27;
          propValue = new Boolean(bean.isChngAgncyCfg());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_2_28;
          propValue = new Boolean(bean.isDeleteIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_2_16 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "initOfcNo");
    private final static javax.xml.namespace.QName QName_2_10 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "agncyIdntyNo");
    private final static javax.xml.namespace.QName QName_2_9 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "agncyAuthCfgs");
    private final static javax.xml.namespace.QName QName_2_14 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "cntctName");
    private final static javax.xml.namespace.QName QName_2_27 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "chngAgncyCfg");
    private final static javax.xml.namespace.QName QName_2_24 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "zpCd");
    private final static javax.xml.namespace.QName QName_2_25 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "zpCdP4");
    private final static javax.xml.namespace.QName QName_2_13 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "city");
    private final static javax.xml.namespace.QName QName_2_19 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "phone");
    private final static javax.xml.namespace.QName QName_2_11 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "agncyTypeCd");
    private final static javax.xml.namespace.QName QName_2_23 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "updtngUserName");
    private final static javax.xml.namespace.QName QName_2_28 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "deleteIndi");
    private final static javax.xml.namespace.QName QName_2_26 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "chngAgncy");
    private final static javax.xml.namespace.QName QName_2_22 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "state");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_2_17 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "name1");
    private final static javax.xml.namespace.QName QName_2_18 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "name2");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_2_30 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "RtsWebAgncyAuthCfg");
    private final static javax.xml.namespace.QName QName_2_12 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "chngTimeStmpAgncy");
    private final static javax.xml.namespace.QName QName_1_31 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "dateTime");
    private final static javax.xml.namespace.QName QName_1_32 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "boolean");
    private final static javax.xml.namespace.QName QName_2_8 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "agncyNameAddress");
    private final static javax.xml.namespace.QName QName_0_29 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.webservices.rts.isd.txdot.com",
                  "RtsNameAddress");
    private final static javax.xml.namespace.QName QName_2_20 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "st1");
    private final static javax.xml.namespace.QName QName_2_21 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "st2");
    private final static javax.xml.namespace.QName QName_2_15 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "EMail");
}
