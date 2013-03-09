/**
 * RtsWebAgntWS_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.agnt.data;

public class RtsWebAgntWS_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public RtsWebAgntWS_Ser(
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
        RtsWebAgntWS bean = (RtsWebAgntWS) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_4_64;
          propValue = new Integer(bean.getAgntIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_4_55;
          propValue = new Integer(bean.getAgntSecrtyIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_4_65;
          {
            propValue = bean.getAgntSecurity();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_4_83,
                    true,null);
              }
            }
          }
          propQName = QName_4_36;
          propValue = bean.getChngTimeStmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_31,
              true,null);
          propQName = QName_4_66;
          propValue = bean.getChngTimeStmpDate();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_31,
              true,null);
          propQName = QName_4_15;
          propValue = bean.getEMail();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_4_67;
          propValue = bean.getFstName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_4_16;
          propValue = new Integer(bean.getInitOfcNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_4_68;
          propValue = bean.getLstName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_4_69;
          propValue = bean.getMiName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_4_19;
          propValue = bean.getPhone();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_4_70;
          propValue = bean.getUserName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_4_71;
          propValue = new Boolean(bean.isAgncyAuthAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_4_72;
          propValue = new Boolean(bean.isAgncyInfoAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_4_73;
          propValue = new Boolean(bean.isAgntAuthAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_4_74;
          propValue = new Boolean(bean.isAprvBatchAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_4_75;
          propValue = new Boolean(bean.isBatchAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_4_76;
          propValue = new Boolean(bean.isCheckDMVIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_4_77;
          propValue = new Boolean(bean.isDataChanged());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_4_58;
          propValue = new Boolean(bean.isDmvUserIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_4_78;
          propValue = new Boolean(bean.isRenwlAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_4_79;
          propValue = new Boolean(bean.isRePrntAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_4_80;
          propValue = new Boolean(bean.isRptAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_4_81;
          propValue = new Boolean(bean.isSubmitBatchAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_4_82;
          propValue = new Boolean(bean.isVoidAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_4_45;
          propValue = new Integer(bean.getUpdtngAgntIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_4_45 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "updtngAgntIdntyNo");
    private final static javax.xml.namespace.QName QName_4_83 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "RtsWebAgntSecurityWS");
    private final static javax.xml.namespace.QName QName_4_55 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "agntSecrtyIdntyNo");
    private final static javax.xml.namespace.QName QName_4_72 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "agncyInfoAccs");
    private final static javax.xml.namespace.QName QName_4_80 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "rptAccs");
    private final static javax.xml.namespace.QName QName_4_19 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "phone");
    private final static javax.xml.namespace.QName QName_4_77 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "dataChanged");
    private final static javax.xml.namespace.QName QName_4_66 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "chngTimeStmpDate");
    private final static javax.xml.namespace.QName QName_4_70 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "userName");
    private final static javax.xml.namespace.QName QName_4_73 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "agntAuthAccs");
    private final static javax.xml.namespace.QName QName_4_71 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "agncyAuthAccs");
    private final static javax.xml.namespace.QName QName_4_82 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "voidAccs");
    private final static javax.xml.namespace.QName QName_4_64 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "agntIdntyNo");
    private final static javax.xml.namespace.QName QName_4_76 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "checkDMVIndi");
    private final static javax.xml.namespace.QName QName_4_74 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "aprvBatchAccs");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_4_58 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "dmvUserIndi");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_4_16 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "initOfcNo");
    private final static javax.xml.namespace.QName QName_4_68 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "lstName");
    private final static javax.xml.namespace.QName QName_1_31 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "dateTime");
    private final static javax.xml.namespace.QName QName_1_32 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "boolean");
    private final static javax.xml.namespace.QName QName_4_78 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "renwlAccs");
    private final static javax.xml.namespace.QName QName_4_75 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "batchAccs");
    private final static javax.xml.namespace.QName QName_4_65 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "agntSecurity");
    private final static javax.xml.namespace.QName QName_4_36 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "chngTimeStmp");
    private final static javax.xml.namespace.QName QName_4_69 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "miName");
    private final static javax.xml.namespace.QName QName_4_81 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "submitBatchAccs");
    private final static javax.xml.namespace.QName QName_4_15 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "EMail");
    private final static javax.xml.namespace.QName QName_4_67 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "fstName");
    private final static javax.xml.namespace.QName QName_4_79 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.agnt.webservices.rts.isd.txdot.com",
                  "rePrntAccs");
}
