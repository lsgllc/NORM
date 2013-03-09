/**
 * RtsDsabldPlcrdResponse_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.dsabldplcrd.data;

public class RtsDsabldPlcrdResponse_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public RtsDsabldPlcrdResponse_Ser(
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
        RtsDsabldPlcrdResponse bean = (RtsDsabldPlcrdResponse) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_2_9;
          propValue = bean.getAcctItmCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_10;
          propValue = bean.getAcctItmCdDesc();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_11;
          propValue = bean.getCity();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_12;
          propValue = bean.getCustId();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_13;
          propValue = new Integer(bean.getCustIdTypeCd());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_14;
          propValue = bean.getCustIdTypeDesc();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_15;
          propValue = new Integer(bean.getDeleteIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_16;
          propValue = bean.getDelReasnDesc();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_17;
          propValue = new Integer(bean.getErrMsgNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_18;
          propValue = bean.getFrstNameLastName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_19;
          propValue = new Integer(bean.getInstIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_6;
          propValue = bean.getInvItmNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_29;
          propValue = bean.getOfcName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_21;
          propValue = new Integer(bean.getResComptCntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_22;
          propValue = new Integer(bean.getRTSEffDate());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_30;
          propValue = new Integer(bean.getRTSExpMo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_31;
          propValue = new Integer(bean.getRTSExpYr());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_25;
          propValue = bean.getSt1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_26;
          propValue = bean.getSt2();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_27;
          propValue = bean.getStateCntry();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_32;
          propValue = bean.getZpCd();
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
    private final static javax.xml.namespace.QName QName_2_22 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "RTSEffDate");
    private final static javax.xml.namespace.QName QName_2_26 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "st2");
    private final static javax.xml.namespace.QName QName_2_25 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "st1");
    private final static javax.xml.namespace.QName QName_2_14 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "custIdTypeDesc");
    private final static javax.xml.namespace.QName QName_2_11 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "city");
    private final static javax.xml.namespace.QName QName_2_12 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "custId");
    private final static javax.xml.namespace.QName QName_2_19 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "instIndi");
    private final static javax.xml.namespace.QName QName_2_21 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "resComptCntyNo");
    private final static javax.xml.namespace.QName QName_2_27 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "stateCntry");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_2_6 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "invItmNo");
    private final static javax.xml.namespace.QName QName_2_32 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "zpCd");
    private final static javax.xml.namespace.QName QName_2_15 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "deleteIndi");
    private final static javax.xml.namespace.QName QName_2_18 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "frstNameLastName");
    private final static javax.xml.namespace.QName QName_2_30 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "RTSExpMo");
    private final static javax.xml.namespace.QName QName_2_17 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "errMsgNo");
    private final static javax.xml.namespace.QName QName_2_16 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "delReasnDesc");
    private final static javax.xml.namespace.QName QName_2_13 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "custIdTypeCd");
    private final static javax.xml.namespace.QName QName_2_10 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "acctItmCdDesc");
    private final static javax.xml.namespace.QName QName_2_29 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "ofcName");
    private final static javax.xml.namespace.QName QName_2_31 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "RTSExpYr");
    private final static javax.xml.namespace.QName QName_2_9 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.dsabldplcrd.webservices.rts.isd.txdot.com",
                  "acctItmCd");
}
