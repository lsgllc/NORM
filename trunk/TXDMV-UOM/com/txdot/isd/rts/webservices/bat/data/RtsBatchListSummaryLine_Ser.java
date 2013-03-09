/**
 * RtsBatchListSummaryLine_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.bat.data;

public class RtsBatchListSummaryLine_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public RtsBatchListSummaryLine_Ser(
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
        RtsBatchListSummaryLine bean = (RtsBatchListSummaryLine) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_5_138;
          propValue = new Integer(bean.getAgncyBatchIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_10;
          propValue = new Integer(bean.getAgncyIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_157;
          propValue = bean.getAgncyName1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_158;
          propValue = new Integer(bean.getSubmitAgntSecrtyIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_159;
          propValue = bean.getAgntUserName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_160;
          propValue = bean.getBatchCompltDate();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_31,
              true,null);
          propQName = QName_5_161;
          propValue = bean.getBatchCompltTime();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_162;
          propValue = bean.getBatchCompltTimeStamp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_31,
              true,null);
          propQName = QName_5_163;
          propValue = bean.getBatchInitDate();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_31,
              true,null);
          propQName = QName_5_164;
          propValue = bean.getBatchInitTime();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_165;
          propValue = bean.getBatchInitTimeStamp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_31,
              true,null);
          propQName = QName_5_166;
          propValue = bean.getBatchStatusCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_167;
          propValue = bean.getBatchStatusDesc();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_42;
          propValue = new Integer(bean.getOfcIssuanceNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_43;
          propValue = bean.getOfcName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_168;
          propValue = new Integer(bean.getRenewalRequestCnt());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_169;
          propValue = new Integer(bean.getReprintCnt());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_170;
          propValue = new Double(bean.getTotalDollars());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_104,
              true,null);
          propQName = QName_5_171;
          propValue = new Integer(bean.getVoidCnt());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_172;
          propValue = new Boolean(bean.isApproveAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_5_173;
          propValue = new Boolean(bean.isSubmitAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_5_164 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "batchInitTime");
    private final static javax.xml.namespace.QName QName_5_166 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "batchStatusCd");
    private final static javax.xml.namespace.QName QName_1_104 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "double");
    private final static javax.xml.namespace.QName QName_5_171 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "voidCnt");
    private final static javax.xml.namespace.QName QName_5_157 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "agncyName1");
    private final static javax.xml.namespace.QName QName_5_42 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "ofcIssuanceNo");
    private final static javax.xml.namespace.QName QName_5_173 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "submitAccs");
    private final static javax.xml.namespace.QName QName_5_167 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "batchStatusDesc");
    private final static javax.xml.namespace.QName QName_5_161 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "batchCompltTime");
    private final static javax.xml.namespace.QName QName_5_170 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "totalDollars");
    private final static javax.xml.namespace.QName QName_5_158 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "submitAgntSecrtyIdntyNo");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_5_172 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "approveAccs");
    private final static javax.xml.namespace.QName QName_5_159 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "agntUserName");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_5_169 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "reprintCnt");
    private final static javax.xml.namespace.QName QName_1_31 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "dateTime");
    private final static javax.xml.namespace.QName QName_5_138 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "agncyBatchIdntyNo");
    private final static javax.xml.namespace.QName QName_1_32 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "boolean");
    private final static javax.xml.namespace.QName QName_5_168 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "renewalRequestCnt");
    private final static javax.xml.namespace.QName QName_5_165 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "batchInitTimeStamp");
    private final static javax.xml.namespace.QName QName_5_160 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "batchCompltDate");
    private final static javax.xml.namespace.QName QName_5_163 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "batchInitDate");
    private final static javax.xml.namespace.QName QName_5_43 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "ofcName");
    private final static javax.xml.namespace.QName QName_5_162 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "batchCompltTimeStamp");
    private final static javax.xml.namespace.QName QName_5_10 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "agncyIdntyNo");
}
