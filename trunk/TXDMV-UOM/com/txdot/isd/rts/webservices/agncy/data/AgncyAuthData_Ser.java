/**
 * AgncyAuthData_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.webservices.agncy.data;

public class AgncyAuthData_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public AgncyAuthData_Ser(
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
        AgncyAuthData bean = (AgncyAuthData) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_4_29;
          propValue = new Integer(bean.getAgencyAuthIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_4_30;
          propValue = new Integer(bean.getAgencyIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_4_31;
          propValue = bean.getChngTimeStmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_24,
              true,null);
          propQName = QName_4_32;
          propValue = bean.getExpPrcsngCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_4_33;
          propValue = new Integer(bean.getExpPrcsngMos());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_4_34;
          propValue = bean.getKeyEntryCode();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_4_35;
          propValue = new Integer(bean.getMaxSubmitCount());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_4_36;
          propValue = new Integer(bean.getMaxSubmitDays());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_4_37;
          propValue = new Integer(bean.getOfcIssuanceNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_4_38;
          propValue = bean.getOfcName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_4_39;
          propValue = new Integer(bean.getSubconId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_4_40;
          propValue = new Boolean(bean.isIssueInvIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_9,
              true,null);
        }
    }
        public final static javax.xml.namespace.QName QName_4_38 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "ofcName");
        public final static javax.xml.namespace.QName QName_4_40 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "issueInvIndi");
        public final static javax.xml.namespace.QName QName_4_35 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "maxSubmitCount");
        public final static javax.xml.namespace.QName QName_4_36 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "maxSubmitDays");
        public final static javax.xml.namespace.QName QName_4_31 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "chngTimeStmp");
        public final static javax.xml.namespace.QName QName_4_39 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "subconId");
        public final static javax.xml.namespace.QName QName_4_37 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "ofcIssuanceNo");
        public final static javax.xml.namespace.QName QName_1_9 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "boolean");
        public final static javax.xml.namespace.QName QName_4_29 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "agencyAuthIdntyNo");
        public final static javax.xml.namespace.QName QName_4_30 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "agencyIdntyNo");
        public final static javax.xml.namespace.QName QName_1_4 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "int");
        public final static javax.xml.namespace.QName QName_1_5 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "string");
        public final static javax.xml.namespace.QName QName_4_32 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "expPrcsngCd");
        public final static javax.xml.namespace.QName QName_1_24 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "dateTime");
        public final static javax.xml.namespace.QName QName_4_34 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "keyEntryCode");
        public final static javax.xml.namespace.QName QName_4_33 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agncy.webservices.rts.isd.txdot.com",
                      "expPrcsngMos");
}
