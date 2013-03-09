/**
 * RtsTransRequest_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.trans.data;

public class RtsTransRequest_Ser extends com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest_Ser {
    /**
     * Constructor
     */
    public RtsTransRequest_Ser(
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
        RtsTransRequest bean = (RtsTransRequest) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_6_85;
          propValue = new Integer(bean.getAddlSetIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_6_125;
          propValue = bean.getEpayRcveTimeStmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_111,
              true,null);
          propQName = QName_6_126;
          propValue = bean.getEpaySendTimeStmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_111,
              true,null);
          propQName = QName_6_127;
          propValue = bean.getInitReqTimeStmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_111,
              true,null);
          propQName = QName_6_87;
          propValue = new Integer(bean.getItrntPymntStatusCd());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_6_77;
          propValue = bean.getItrntTraceNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_97;
          propValue = bean.getMfgPltNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_98;
          propValue = bean.getOrgNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_112;
          propValue = bean.getPltCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_90;
          propValue = new Integer(bean.getPltExpMo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_6_91;
          propValue = new Integer(bean.getPltExpYr());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_6_113;
          propValue = bean.getPltNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_114;
          propValue = bean.getPltOwnrAddr();
          context.serialize(propQName, null, 
              propValue, 
              QName_0_36,
              true,null);
          propQName = QName_6_115;
          propValue = bean.getPltOwnrEmailAddr();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_102;
          propValue = bean.getPltOwnrName1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_101;
          propValue = bean.getPltOwnrName2();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_103;
          propValue = bean.getPltOwnrPhone();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_116;
          propValue = new Integer(bean.getPltValidityTerm());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_6_83;
          propValue = new Double(bean.getPymntAmt());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_129,
              true,null);
          propQName = QName_6_78;
          propValue = bean.getPymntOrderId();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_93;
          propValue = new Integer(bean.getResComptCntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_6_128;
          propValue = new Boolean(bean.isFromReserve());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_6_120;
          propValue = new Boolean(bean.isIsa());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_6_122;
          propValue = new Boolean(bean.isPlp());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_6_77 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "itrntTraceNo");
    private final static javax.xml.namespace.QName QName_6_115 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "pltOwnrEmailAddr");
    private final static javax.xml.namespace.QName QName_6_127 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "initReqTimeStmp");
    private final static javax.xml.namespace.QName QName_1_129 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "double");
    private final static javax.xml.namespace.QName QName_6_91 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "pltExpYr");
    private final static javax.xml.namespace.QName QName_6_87 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "itrntPymntStatusCd");
    private final static javax.xml.namespace.QName QName_6_112 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "pltCd");
    private final static javax.xml.namespace.QName QName_6_128 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "fromReserve");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_6_85 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "addlSetIndi");
    private final static javax.xml.namespace.QName QName_6_120 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "isa");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_6_78 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "pymntOrderId");
    private final static javax.xml.namespace.QName QName_1_111 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "dateTime");
    private final static javax.xml.namespace.QName QName_6_122 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "plp");
    private final static javax.xml.namespace.QName QName_1_15 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "boolean");
    private final static javax.xml.namespace.QName QName_6_98 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "orgNo");
    private final static javax.xml.namespace.QName QName_0_36 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.webservices.rts.isd.txdot.com",
                  "RtsAddress");
    private final static javax.xml.namespace.QName QName_6_125 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "epayRcveTimeStmp");
    private final static javax.xml.namespace.QName QName_6_90 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "pltExpMo");
    private final static javax.xml.namespace.QName QName_6_126 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "epaySendTimeStmp");
    private final static javax.xml.namespace.QName QName_6_114 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "pltOwnrAddr");
    private final static javax.xml.namespace.QName QName_6_103 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "pltOwnrPhone");
    private final static javax.xml.namespace.QName QName_6_116 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "pltValidityTerm");
    private final static javax.xml.namespace.QName QName_6_113 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "pltNo");
    private final static javax.xml.namespace.QName QName_6_102 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "pltOwnrName1");
    private final static javax.xml.namespace.QName QName_6_101 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "pltOwnrName2");
    private final static javax.xml.namespace.QName QName_6_93 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "resComptCntyNo");
    private final static javax.xml.namespace.QName QName_6_83 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "pymntAmt");
    private final static javax.xml.namespace.QName QName_6_97 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.trans.webservices.rts.isd.txdot.com",
                  "mfgPltNo");
}
