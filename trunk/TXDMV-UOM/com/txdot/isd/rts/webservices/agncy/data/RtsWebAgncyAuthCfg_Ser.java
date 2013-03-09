/**
 * RtsWebAgncyAuthCfg_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.agncy.data;

public class RtsWebAgncyAuthCfg_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public RtsWebAgncyAuthCfg_Ser(
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
        RtsWebAgncyAuthCfg bean = (RtsWebAgncyAuthCfg) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_2_35;
          propValue = new Integer(bean.getAgncyAuthIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_10;
          propValue = new Integer(bean.getAgncyIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_36;
          propValue = bean.getChngTimeStmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_31,
              true,null);
          propQName = QName_2_37;
          propValue = bean.getExpPrcsngCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_38;
          propValue = new Integer(bean.getExpPrcsngMos());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_39;
          propValue = bean.getKeyEntryCode();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_40;
          propValue = new Integer(bean.getMaxSubmitCount());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_41;
          propValue = new Integer(bean.getMaxSubmitDays());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_42;
          propValue = new Integer(bean.getOfcIssuanceNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_43;
          propValue = bean.getOfcName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_44;
          propValue = new Integer(bean.getSubconId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_45;
          propValue = new Integer(bean.getUpdtngAgntIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_46;
          propValue = new Boolean(bean.isIssueInvIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_2_47;
          propValue = new Boolean(bean.isPilotIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_2_47 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "pilotIndi");
    private final static javax.xml.namespace.QName QName_2_43 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "ofcName");
    private final static javax.xml.namespace.QName QName_2_42 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "ofcIssuanceNo");
    private final static javax.xml.namespace.QName QName_2_10 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "agncyIdntyNo");
    private final static javax.xml.namespace.QName QName_2_45 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "updtngAgntIdntyNo");
    private final static javax.xml.namespace.QName QName_2_39 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "keyEntryCode");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_1_31 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "dateTime");
    private final static javax.xml.namespace.QName QName_1_32 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "boolean");
    private final static javax.xml.namespace.QName QName_2_36 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "chngTimeStmp");
    private final static javax.xml.namespace.QName QName_2_46 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "issueInvIndi");
    private final static javax.xml.namespace.QName QName_2_40 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "maxSubmitCount");
    private final static javax.xml.namespace.QName QName_2_38 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "expPrcsngMos");
    private final static javax.xml.namespace.QName QName_2_37 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "expPrcsngCd");
    private final static javax.xml.namespace.QName QName_2_44 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "subconId");
    private final static javax.xml.namespace.QName QName_2_35 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "agncyAuthIdntyNo");
    private final static javax.xml.namespace.QName QName_2_41 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agncy.webservices.rts.isd.txdot.com",
                  "maxSubmitDays");
}
