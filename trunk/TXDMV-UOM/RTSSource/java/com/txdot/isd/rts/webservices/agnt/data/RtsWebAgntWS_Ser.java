/** * RtsWebAgntWS_Ser.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf150720.02 v7507160841 */package com.txdot.isd.rts.webservices.agnt.data;/* &RtsWebAgntWS_Ser& */public class RtsWebAgntWS_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {    /**     * Constructor     *//* &RtsWebAgntWS_Ser.RtsWebAgntWS_Ser& */    public RtsWebAgntWS_Ser(           java.lang.Class _javaType,             javax.xml.namespace.QName _xmlType,            com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {        super(_javaType, _xmlType, _typeDesc);    }/* &RtsWebAgntWS_Ser.serialize& */    public void serialize(        javax.xml.namespace.QName name,        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        context.startElement(name, addAttributes(attributes,value,context));        addElements(value,context);        context.endElement();    }/* &RtsWebAgntWS_Ser.addAttributes& */    protected org.xml.sax.Attributes addAttributes(        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        return attributes;    }/* &RtsWebAgntWS_Ser.addElements& */    protected void addElements(        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        RtsWebAgntWS bean = (RtsWebAgntWS) value;        java.lang.Object propValue;        javax.xml.namespace.QName propQName;        {          propQName = QName_4_64;          propValue = new Integer(bean.getAgntIdntyNo());          context.serialize(propQName, null,               propValue,               QName_1_4,              true,null);          propQName = QName_4_55;          propValue = new Integer(bean.getAgntSecrtyIdntyNo());          context.serialize(propQName, null,               propValue,               QName_1_4,              true,null);          propQName = QName_4_65;          {            propValue = bean.getAgntSecurity();            if (propValue != null) {              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {                context.serialize(propQName, null,                     java.lang.reflect.Array.get(propValue, i),                     QName_4_83,                    true,null);              }            }          }          propQName = QName_4_36;          propValue = bean.getChngTimeStmp();          context.serialize(propQName, null,               propValue,               QName_1_31,              true,null);          propQName = QName_4_66;          propValue = bean.getChngTimeStmpDate();          context.serialize(propQName, null,               propValue,               QName_1_31,              true,null);          propQName = QName_4_15;          propValue = bean.getEMail();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_4_67;          propValue = bean.getFstName();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_4_16;          propValue = new Integer(bean.getInitOfcNo());          context.serialize(propQName, null,               propValue,               QName_1_4,              true,null);          propQName = QName_4_68;          propValue = bean.getLstName();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_4_69;          propValue = bean.getMiName();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_4_19;          propValue = bean.getPhone();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_4_70;          propValue = bean.getUserName();          if (propValue != null && !context.shouldSendXSIType()) {            context.simpleElement(propQName, null, propValue.toString());           } else {            context.serialize(propQName, null,               propValue,               QName_1_5,              true,null);          }          propQName = QName_4_71;          propValue = new Boolean(bean.isAgncyAuthAccs());          context.serialize(propQName, null,               propValue,               QName_1_32,              true,null);          propQName = QName_4_72;          propValue = new Boolean(bean.isAgncyInfoAccs());          context.serialize(propQName, null,               propValue,               QName_1_32,              true,null);          propQName = QName_4_73;          propValue = new Boolean(bean.isAgntAuthAccs());          context.serialize(propQName, null,               propValue,               QName_1_32,              true,null);          propQName = QName_4_74;          propValue = new Boolean(bean.isAprvBatchAccs());          context.serialize(propQName, null,               propValue,               QName_1_32,              true,null);          propQName = QName_4_75;          propValue = new Boolean(bean.isBatchAccs());          context.serialize(propQName, null,               propValue,               QName_1_32,              true,null);          propQName = QName_4_76;          propValue = new Boolean(bean.isCheckDMVIndi());          context.serialize(propQName, null,               propValue,               QName_1_32,              true,null);          propQName = QName_4_77;          propValue = new Boolean(bean.isDataChanged());          context.serialize(propQName, null,               propValue,               QName_1_32,              true,null);          propQName = QName_4_58;          propValue = new Boolean(bean.isDmvUserIndi());          context.serialize(propQName, null,               propValue,               QName_1_32,              true,null);          propQName = QName_4_78;          propValue = new Boolean(bean.isRenwlAccs());          context.serialize(propQName, null,               propValue,               QName_1_32,              true,null);          propQName = QName_4_79;          propValue = new Boolean(bean.isRePrntAccs());          context.serialize(propQName, null,               propValue,               QName_1_32,              true,null);          propQName = QName_4_80;          propValue = new Boolean(bean.isRptAccs());          context.serialize(propQName, null,               propValue,               QName_1_32,              true,null);          propQName = QName_4_81;          propValue = new Boolean(bean.isSubmitBatchAccs());          context.serialize(propQName, null,               propValue,               QName_1_32,              true,null);          propQName = QName_4_82;          propValue = new Boolean(bean.isVoidAccs());          context.serialize(propQName, null,               propValue,               QName_1_32,              true,null);          propQName = QName_4_45;          propValue = new Integer(bean.getUpdtngAgntIdntyNo());          context.serialize(propQName, null,               propValue,               QName_1_4,              true,null);        }    }/* &RtsWebAgntWS_Ser'QName_4_45& */    private final static javax.xml.namespace.QName QName_4_45 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "updtngAgntIdntyNo");/* &RtsWebAgntWS_Ser'QName_4_83& */    private final static javax.xml.namespace.QName QName_4_83 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "RtsWebAgntSecurityWS");/* &RtsWebAgntWS_Ser'QName_4_55& */    private final static javax.xml.namespace.QName QName_4_55 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "agntSecrtyIdntyNo");/* &RtsWebAgntWS_Ser'QName_4_72& */    private final static javax.xml.namespace.QName QName_4_72 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "agncyInfoAccs");/* &RtsWebAgntWS_Ser'QName_4_80& */    private final static javax.xml.namespace.QName QName_4_80 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "rptAccs");/* &RtsWebAgntWS_Ser'QName_4_19& */    private final static javax.xml.namespace.QName QName_4_19 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "phone");/* &RtsWebAgntWS_Ser'QName_4_77& */    private final static javax.xml.namespace.QName QName_4_77 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "dataChanged");/* &RtsWebAgntWS_Ser'QName_4_66& */    private final static javax.xml.namespace.QName QName_4_66 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "chngTimeStmpDate");/* &RtsWebAgntWS_Ser'QName_4_70& */    private final static javax.xml.namespace.QName QName_4_70 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "userName");/* &RtsWebAgntWS_Ser'QName_4_73& */    private final static javax.xml.namespace.QName QName_4_73 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "agntAuthAccs");/* &RtsWebAgntWS_Ser'QName_4_71& */    private final static javax.xml.namespace.QName QName_4_71 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "agncyAuthAccs");/* &RtsWebAgntWS_Ser'QName_4_82& */    private final static javax.xml.namespace.QName QName_4_82 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "voidAccs");/* &RtsWebAgntWS_Ser'QName_4_64& */    private final static javax.xml.namespace.QName QName_4_64 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "agntIdntyNo");/* &RtsWebAgntWS_Ser'QName_4_76& */    private final static javax.xml.namespace.QName QName_4_76 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "checkDMVIndi");/* &RtsWebAgntWS_Ser'QName_4_74& */    private final static javax.xml.namespace.QName QName_4_74 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "aprvBatchAccs");/* &RtsWebAgntWS_Ser'QName_1_4& */    private final static javax.xml.namespace.QName QName_1_4 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://www.w3.org/2001/XMLSchema",                  "int");/* &RtsWebAgntWS_Ser'QName_4_58& */    private final static javax.xml.namespace.QName QName_4_58 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "dmvUserIndi");/* &RtsWebAgntWS_Ser'QName_1_5& */    private final static javax.xml.namespace.QName QName_1_5 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://www.w3.org/2001/XMLSchema",                  "string");/* &RtsWebAgntWS_Ser'QName_4_16& */    private final static javax.xml.namespace.QName QName_4_16 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "initOfcNo");/* &RtsWebAgntWS_Ser'QName_4_68& */    private final static javax.xml.namespace.QName QName_4_68 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "lstName");/* &RtsWebAgntWS_Ser'QName_1_31& */    private final static javax.xml.namespace.QName QName_1_31 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://www.w3.org/2001/XMLSchema",                  "dateTime");/* &RtsWebAgntWS_Ser'QName_1_32& */    private final static javax.xml.namespace.QName QName_1_32 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://www.w3.org/2001/XMLSchema",                  "boolean");/* &RtsWebAgntWS_Ser'QName_4_78& */    private final static javax.xml.namespace.QName QName_4_78 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "renwlAccs");/* &RtsWebAgntWS_Ser'QName_4_75& */    private final static javax.xml.namespace.QName QName_4_75 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "batchAccs");/* &RtsWebAgntWS_Ser'QName_4_65& */    private final static javax.xml.namespace.QName QName_4_65 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "agntSecurity");/* &RtsWebAgntWS_Ser'QName_4_36& */    private final static javax.xml.namespace.QName QName_4_36 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "chngTimeStmp");/* &RtsWebAgntWS_Ser'QName_4_69& */    private final static javax.xml.namespace.QName QName_4_69 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "miName");/* &RtsWebAgntWS_Ser'QName_4_81& */    private final static javax.xml.namespace.QName QName_4_81 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "submitBatchAccs");/* &RtsWebAgntWS_Ser'QName_4_15& */    private final static javax.xml.namespace.QName QName_4_15 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "EMail");/* &RtsWebAgntWS_Ser'QName_4_67& */    private final static javax.xml.namespace.QName QName_4_67 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "fstName");/* &RtsWebAgntWS_Ser'QName_4_79& */    private final static javax.xml.namespace.QName QName_4_79 =            com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                  "http://data.agnt.webservices.rts.isd.txdot.com",                  "rePrntAccs");}/* #RtsWebAgntWS_Ser# */