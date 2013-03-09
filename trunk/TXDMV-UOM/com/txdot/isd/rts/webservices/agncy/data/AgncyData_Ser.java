/**
 * AgncyData_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.webservices.agncy.data;

public class AgncyData_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public AgncyData_Ser(
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
        AgncyData bean = (AgncyData) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_2_8;
          propValue = bean.getCity();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_9;
          propValue = bean.getCntctName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_10;
          propValue = bean.getEMail();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_11;
          propValue = new Integer(bean.getAgencyIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_12;
          propValue = bean.getName1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_13;
          propValue = bean.getName2();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_14;
          propValue = bean.getPhone();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_15;
          propValue = bean.getSt1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_16;
          propValue = bean.getSt2();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_17;
          propValue = bean.getState();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_18;
          propValue = bean.getAgencyTypeCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_19;
          propValue = bean.getZpCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_20;
          propValue = bean.getZpCdP4();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_21;
          propValue = bean.getChngTimestmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_25,
              true,null);
          propQName = QName_2_22;
          propValue = new Integer(bean.getInitOfcNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_23;
          propValue = new Integer(bean.getUpdtngAgentIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_24;
          propValue = bean.getUpdtngAgentUserName();
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
        public final static javax.xml.namespace.QName QName_2_22 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "initOfcNo");
        public final static javax.xml.namespace.QName QName_2_9 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "cntctName");
        public final static javax.xml.namespace.QName QName_2_19 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "zpCd");
        public final static javax.xml.namespace.QName QName_2_20 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "zpCdP4");
        public final static javax.xml.namespace.QName QName_2_8 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "city");
        public final static javax.xml.namespace.QName QName_2_14 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "phone");
        public final static javax.xml.namespace.QName QName_2_17 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "state");
        public final static javax.xml.namespace.QName QName_1_4 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "int");
        public final static javax.xml.namespace.QName QName_2_12 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "name1");
        public final static javax.xml.namespace.QName QName_2_13 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "name2");
        public final static javax.xml.namespace.QName QName_1_5 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "string");
        public final static javax.xml.namespace.QName QName_2_23 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "updtngAgentIdntyNo");
        public final static javax.xml.namespace.QName QName_1_25 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "dateTime");
        public final static javax.xml.namespace.QName QName_2_24 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "updtngAgentUserName");
        public final static javax.xml.namespace.QName QName_2_21 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "chngTimestmp");
        public final static javax.xml.namespace.QName QName_2_15 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "st1");
        public final static javax.xml.namespace.QName QName_2_11 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "agencyIdntyNo");
        public final static javax.xml.namespace.QName QName_2_16 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "st2");
        public final static javax.xml.namespace.QName QName_2_18 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "agencyTypeCd");
        public final static javax.xml.namespace.QName QName_2_10 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "EMail");
}
