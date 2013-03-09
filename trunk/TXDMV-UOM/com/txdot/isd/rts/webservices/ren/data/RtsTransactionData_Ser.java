/**
 * RtsTransactionData_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.ren.data;

public class RtsTransactionData_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public RtsTransactionData_Ser(
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
        RtsTransactionData bean = (RtsTransactionData) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_3_63;
          propValue = bean.getAccptTimestmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_79,
              true,null);
          propQName = QName_3_64;
          propValue = bean.getAccptTimestmpDate();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_79,
              true,null);
          propQName = QName_3_65;
          propValue = new Integer(bean.getAgncyBatchIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_66;
          propValue = new Integer(bean.getAgntIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_67;
          propValue = new Integer(bean.getAgntSecrtyIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_68;
          propValue = bean.getAuditTrailTransId();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_69;
          propValue = bean.getBarCdVersionNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_70;
          propValue = bean.getKeyTypeCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_71;
          propValue = bean.getPosTransId();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_72;
          propValue = new Integer(bean.getSAVReqId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_73;
          propValue = new Integer(bean.getSubconId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_74;
          propValue = bean.getUserName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_75;
          propValue = bean.getWebSessionId();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_76;
          propValue = new Boolean(bean.isAccptVehIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_34,
              true,null);
          propQName = QName_3_77;
          propValue = new Boolean(bean.isAgncyVoidIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_34,
              true,null);
          propQName = QName_3_78;
          propValue = new Boolean(bean.isCntyVoidIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_34,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_3_65 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "agncyBatchIdntyNo");
    private final static javax.xml.namespace.QName QName_3_63 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "accptTimestmp");
    private final static javax.xml.namespace.QName QName_3_67 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "agntSecrtyIdntyNo");
    private final static javax.xml.namespace.QName QName_3_77 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "agncyVoidIndi");
    private final static javax.xml.namespace.QName QName_3_70 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "keyTypeCd");
    private final static javax.xml.namespace.QName QName_3_76 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "accptVehIndi");
    private final static javax.xml.namespace.QName QName_3_74 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "userName");
    private final static javax.xml.namespace.QName QName_3_71 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "posTransId");
    private final static javax.xml.namespace.QName QName_3_64 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "accptTimestmpDate");
    private final static javax.xml.namespace.QName QName_3_78 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "cntyVoidIndi");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_1_79 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "dateTime");
    private final static javax.xml.namespace.QName QName_3_73 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "subconId");
    private final static javax.xml.namespace.QName QName_1_34 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "boolean");
    private final static javax.xml.namespace.QName QName_3_72 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "SAVReqId");
    private final static javax.xml.namespace.QName QName_3_68 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "auditTrailTransId");
    private final static javax.xml.namespace.QName QName_3_75 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "webSessionId");
    private final static javax.xml.namespace.QName QName_3_66 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "agntIdntyNo");
    private final static javax.xml.namespace.QName QName_3_69 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "barCdVersionNo");
}
