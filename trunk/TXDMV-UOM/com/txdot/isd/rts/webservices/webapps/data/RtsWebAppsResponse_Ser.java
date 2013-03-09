/**
 * RtsWebAppsResponse_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.webapps.data;

public class RtsWebAppsResponse_Ser extends com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse_Ser {
    /**
     * Constructor
     */
    public RtsWebAppsResponse_Ser(
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
        RtsWebAppsResponse bean = (RtsWebAppsResponse) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_5_80;
          propValue = bean.getEpayRcveTimestmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_111,
              true,null);
          propQName = QName_5_81;
          propValue = bean.getEpaySendTimestmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_111,
              true,null);
          propQName = QName_5_82;
          propValue = bean.getInitReqTimestmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_111,
              true,null);
          propQName = QName_5_83;
          propValue = bean.getPymntAmt();
          context.serialize(propQName, null, 
              propValue, 
              QName_4_75,
              true,null);
          propQName = QName_5_84;
          propValue = bean.getUpdtTimestmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_111,
              true,null);
          propQName = QName_5_85;
          propValue = new Integer(bean.getAddlSetIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_86;
          propValue = new Integer(bean.getIsaIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_87;
          propValue = new Integer(bean.getItrntPymntStatusCd());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_88;
          propValue = new Integer(bean.getMqUpdtIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_89;
          propValue = new Integer(bean.getPltTerm());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_90;
          propValue = new Integer(bean.getPltExpMo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_91;
          propValue = new Integer(bean.getPltExpYr());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_92;
          propValue = new Integer(bean.getRegPltNoReqId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_93;
          propValue = new Integer(bean.getResComptCntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_94;
          propValue = new Integer(bean.getReserveIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_95;
          propValue = bean.getAuditTrailTransId();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_96;
          propValue = bean.getItrntTranceNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_97;
          propValue = bean.getMfgPltNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_98;
          propValue = bean.getOrgNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_99;
          propValue = bean.getPltOwnrCity();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_100;
          propValue = bean.getPltOwnrEmail();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_101;
          propValue = bean.getPltOwnrName2();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_102;
          propValue = bean.getPltOwnrName1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_103;
          propValue = bean.getPltOwnrPhone();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_104;
          propValue = bean.getPltOwnrSt1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_105;
          propValue = bean.getPltOwnrSt2();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_106;
          propValue = bean.getPltOwnrState();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_107;
          propValue = bean.getPltOwnrZpCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_108;
          propValue = bean.getPltOwnrZpCd4();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_78;
          propValue = bean.getPymntOrderId();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_68;
          propValue = bean.getRegPltCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_10;
          propValue = bean.getRegPltNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_79;
          propValue = bean.getReqIpAddr();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_109;
          propValue = bean.getTransEmpId();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_110;
          propValue = bean.getTransStatusCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_0;
          propValue = new Integer(bean.getAction());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_5_94 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "reserveIndi");
    private final static javax.xml.namespace.QName QName_5_96 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "itrntTranceNo");
    private final static javax.xml.namespace.QName QName_5_109 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "transEmpId");
    private final static javax.xml.namespace.QName QName_5_106 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pltOwnrState");
    private final static javax.xml.namespace.QName QName_5_89 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pltTerm");
    private final static javax.xml.namespace.QName QName_5_91 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pltExpYr");
    private final static javax.xml.namespace.QName QName_5_98 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "orgNo");
    private final static javax.xml.namespace.QName QName_5_108 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pltOwnrZpCd4");
    private final static javax.xml.namespace.QName QName_5_88 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "mqUpdtIndi");
    private final static javax.xml.namespace.QName QName_5_97 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "mfgPltNo");
    private final static javax.xml.namespace.QName QName_5_10 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "regPltNo");
    private final static javax.xml.namespace.QName QName_5_83 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pymntAmt");
    private final static javax.xml.namespace.QName QName_5_103 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pltOwnrPhone");
    private final static javax.xml.namespace.QName QName_5_82 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "initReqTimestmp");
    private final static javax.xml.namespace.QName QName_5_84 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "updtTimestmp");
    private final static javax.xml.namespace.QName QName_5_86 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "isaIndi");
    private final static javax.xml.namespace.QName QName_5_100 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pltOwnrEmail");
    private final static javax.xml.namespace.QName QName_5_102 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pltOwnrName1");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_5_101 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pltOwnrName2");
    private final static javax.xml.namespace.QName QName_5_93 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "resComptCntyNo");
    private final static javax.xml.namespace.QName QName_5_79 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "reqIpAddr");
    private final static javax.xml.namespace.QName QName_5_81 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "epaySendTimestmp");
    private final static javax.xml.namespace.QName QName_5_104 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pltOwnrSt1");
    private final static javax.xml.namespace.QName QName_5_105 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pltOwnrSt2");
    private final static javax.xml.namespace.QName QName_5_0 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "action");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_5_87 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "itrntPymntStatusCd");
    private final static javax.xml.namespace.QName QName_5_90 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pltExpMo");
    private final static javax.xml.namespace.QName QName_5_78 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pymntOrderId");
    private final static javax.xml.namespace.QName QName_5_95 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "auditTrailTransId");
    private final static javax.xml.namespace.QName QName_1_111 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "dateTime");
    private final static javax.xml.namespace.QName QName_5_92 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "regPltNoReqId");
    private final static javax.xml.namespace.QName QName_4_75 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://util.services.rts.isd.txdot.com",
                  "Dollar");
    private final static javax.xml.namespace.QName QName_5_80 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "epayRcveTimestmp");
    private final static javax.xml.namespace.QName QName_5_110 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "transStatusCd");
    private final static javax.xml.namespace.QName QName_5_99 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pltOwnrCity");
    private final static javax.xml.namespace.QName QName_5_68 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "regPltCd");
    private final static javax.xml.namespace.QName QName_5_107 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "pltOwnrZpCd");
    private final static javax.xml.namespace.QName QName_5_85 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.webapps.webservices.rts.isd.txdot.com",
                  "addlSetIndi");
}
