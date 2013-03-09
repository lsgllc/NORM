/**
 * TransactionRequest_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.webapps.order.transaction.data;

public class TransactionRequest_Ser extends com.txdot.isd.rts.server.webapps.order.common.data.AbstractRequest_Ser {
    /**
     * Constructor
     */
    public TransactionRequest_Ser(
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
        TransactionRequest bean = (TransactionRequest) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_17_85;
          propValue = new Integer(bean.getAddlSetIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_17_295;
          propValue = bean.getAddress();
          context.serialize(propQName, null, 
              propValue, 
              QName_12_304,
              true,null);
          propQName = QName_17_296;
          propValue = new Integer(bean.getCreatePOSTransIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_17_297;
          propValue = bean.getEpayRcveTimeStamp();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_17_298;
          propValue = bean.getEpaySendTimeStamp();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_17_254;
          {
            propValue = bean.getFees();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_12_274,
                    true,null);
              }
            }
          }
          propQName = QName_17_86;
          propValue = new Integer(bean.getIsaIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_17_87;
          propValue = new Integer(bean.getItrntPymntStatusCd());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_17_77;
          propValue = bean.getItrntTraceNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_17_97;
          propValue = bean.getMfgPltNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_17_98;
          propValue = bean.getOrgNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_17_299;
          propValue = bean.getPhoneNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_17_300;
          propValue = new Integer(bean.getPlpIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_17_100;
          propValue = bean.getPltOwnrEmail();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_17_102;
          propValue = bean.getPltOwnrName1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_17_101;
          propValue = bean.getPltOwnrName2();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_17_83;
          propValue = new Double(bean.getPymntAmt());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_129,
              true,null);
          propQName = QName_17_301;
          propValue = bean.getPymntOrderID();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_17_68;
          propValue = bean.getRegPltCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_17_10;
          propValue = bean.getRegPltNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_17_302;
          propValue = bean.getReqIPAddr();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_17_93;
          propValue = new Integer(bean.getResComptCntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_17_303;
          propValue = bean.getReqSessionID();
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
    private final static javax.xml.namespace.QName QName_17_101 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "pltOwnrName2");
    private final static javax.xml.namespace.QName QName_17_102 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "pltOwnrName1");
    private final static javax.xml.namespace.QName QName_17_97 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "mfgPltNo");
    private final static javax.xml.namespace.QName QName_1_129 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "double");
    private final static javax.xml.namespace.QName QName_17_93 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "resComptCntyNo");
    private final static javax.xml.namespace.QName QName_17_297 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "epayRcveTimeStamp");
    private final static javax.xml.namespace.QName QName_17_296 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "createPOSTransIndi");
    private final static javax.xml.namespace.QName QName_17_254 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "fees");
    private final static javax.xml.namespace.QName QName_17_295 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "address");
    private final static javax.xml.namespace.QName QName_17_303 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "reqSessionID");
    private final static javax.xml.namespace.QName QName_17_10 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "regPltNo");
    private final static javax.xml.namespace.QName QName_17_302 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "reqIPAddr");
    private final static javax.xml.namespace.QName QName_17_68 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "regPltCd");
    private final static javax.xml.namespace.QName QName_17_100 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "pltOwnrEmail");
    private final static javax.xml.namespace.QName QName_17_298 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "epaySendTimeStamp");
    private final static javax.xml.namespace.QName QName_17_299 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "phoneNo");
    private final static javax.xml.namespace.QName QName_17_83 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "pymntAmt");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_17_300 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "plpIndi");
    private final static javax.xml.namespace.QName QName_17_77 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "itrntTraceNo");
    private final static javax.xml.namespace.QName QName_17_85 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "addlSetIndi");
    private final static javax.xml.namespace.QName QName_17_87 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "itrntPymntStatusCd");
    private final static javax.xml.namespace.QName QName_17_86 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "isaIndi");
    private final static javax.xml.namespace.QName QName_12_304 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "Address");
    private final static javax.xml.namespace.QName QName_17_301 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "pymntOrderID");
    private final static javax.xml.namespace.QName QName_12_274 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "Fees");
    private final static javax.xml.namespace.QName QName_17_98 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "orgNo");
}
