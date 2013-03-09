/** * RtsWebAgntSecurity_Ser.java * * This file was auto-generated from WSDL * by the IBM Web services WSDL2Java emitter. * cf20411.06 v32504192757 */package com.txdot.isd.rts.webservices.agnt.data;/* &RtsWebAgntSecurity_Ser& */public class RtsWebAgntSecurity_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {    /**     * Constructor     *//* &RtsWebAgntSecurity_Ser.RtsWebAgntSecurity_Ser& */    public RtsWebAgntSecurity_Ser(           java.lang.Class _javaType,             javax.xml.namespace.QName _xmlType,            com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {        super(_javaType, _xmlType, _typeDesc);    }/* &RtsWebAgntSecurity_Ser.serialize& */    public void serialize(        javax.xml.namespace.QName name,        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        context.startElement(name, addAttributes(attributes,value,context));        addElements(value,context);        context.endElement();    }/* &RtsWebAgntSecurity_Ser.addAttributes& */    protected org.xml.sax.Attributes addAttributes(        org.xml.sax.Attributes attributes,        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        return attributes;    }/* &RtsWebAgntSecurity_Ser.addElements& */    protected void addElements(        java.lang.Object value,        com.ibm.ws.webservices.engine.encoding.SerializationContext context)        throws java.io.IOException    {        RtsWebAgntSecurity bean = (RtsWebAgntSecurity) value;        java.lang.Object propValue;        javax.xml.namespace.QName propQName;        {          propQName = QName_2_6;          propValue = new Integer(bean.getAgncyIdntyNo());          context.serialize(propQName, null,               propValue,               QName_1_4,              true,null);          propQName = QName_2_13;          propValue = new Integer(bean.getAgntIdntyNo());          context.serialize(propQName, null,               propValue,               QName_1_4,              true,null);          propQName = QName_2_25;          propValue = new Integer(bean.getAgntSecrtyIdntyNo());          context.serialize(propQName, null,               propValue,               QName_1_4,              true,null);          propQName = QName_2_15;          propValue = bean.getChngTimeStmp();          context.serialize(propQName, null,               propValue,               QName_1_23,              true,null);          propQName = QName_2_26;          propValue = new Boolean(bean.isAgncyAuthAccs());          context.serialize(propQName, null,               propValue,               QName_1_24,              true,null);          propQName = QName_2_27;          propValue = new Boolean(bean.isAgncyInfoAccs());          context.serialize(propQName, null,               propValue,               QName_1_24,              true,null);          propQName = QName_2_28;          propValue = new Boolean(bean.isAgntAuthAccs());          context.serialize(propQName, null,               propValue,               QName_1_24,              true,null);          propQName = QName_2_29;          propValue = new Boolean(bean.isAprvBatchAccs());          context.serialize(propQName, null,               propValue,               QName_1_24,              true,null);          propQName = QName_2_30;          propValue = new Boolean(bean.isBatchAccs());          context.serialize(propQName, null,               propValue,               QName_1_24,              true,null);          propQName = QName_2_31;          propValue = new Boolean(bean.isDashAccs());          context.serialize(propQName, null,               propValue,               QName_1_24,              true,null);          propQName = QName_2_32;          propValue = new Boolean(bean.isRenwlAccs());          context.serialize(propQName, null,               propValue,               QName_1_24,              true,null);          propQName = QName_2_33;          propValue = new Boolean(bean.isRePrntAccs());          context.serialize(propQName, null,               propValue,               QName_1_24,              true,null);          propQName = QName_2_34;          propValue = new Boolean(bean.isRptAccs());          context.serialize(propQName, null,               propValue,               QName_1_24,              true,null);          propQName = QName_2_35;          propValue = new Boolean(bean.isSubmitBatchAccs());          context.serialize(propQName, null,               propValue,               QName_1_24,              true,null);          propQName = QName_2_36;          propValue = new Boolean(bean.isVoidAccs());          context.serialize(propQName, null,               propValue,               QName_1_24,              true,null);        }    }/* &RtsWebAgntSecurity_Ser'QName_2_25& */        public final static javax.xml.namespace.QName QName_2_25 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "agntSecrtyIdntyNo");/* &RtsWebAgntSecurity_Ser'QName_2_27& */        public final static javax.xml.namespace.QName QName_2_27 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "agncyInfoAccs");/* &RtsWebAgntSecurity_Ser'QName_2_34& */        public final static javax.xml.namespace.QName QName_2_34 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "rptAccs");/* &RtsWebAgntSecurity_Ser'QName_2_6& */        public final static javax.xml.namespace.QName QName_2_6 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "agncyIdntyNo");/* &RtsWebAgntSecurity_Ser'QName_2_28& */        public final static javax.xml.namespace.QName QName_2_28 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "agntAuthAccs");/* &RtsWebAgntSecurity_Ser'QName_2_26& */        public final static javax.xml.namespace.QName QName_2_26 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "agncyAuthAccs");/* &RtsWebAgntSecurity_Ser'QName_2_36& */        public final static javax.xml.namespace.QName QName_2_36 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "voidAccs");/* &RtsWebAgntSecurity_Ser'QName_2_31& */        public final static javax.xml.namespace.QName QName_2_31 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "dashAccs");/* &RtsWebAgntSecurity_Ser'QName_2_13& */        public final static javax.xml.namespace.QName QName_2_13 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "agntIdntyNo");/* &RtsWebAgntSecurity_Ser'QName_2_29& */        public final static javax.xml.namespace.QName QName_2_29 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "aprvBatchAccs");/* &RtsWebAgntSecurity_Ser'QName_1_4& */        public final static javax.xml.namespace.QName QName_1_4 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://www.w3.org/2001/XMLSchema",                      "int");/* &RtsWebAgntSecurity_Ser'QName_1_23& */        public final static javax.xml.namespace.QName QName_1_23 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://www.w3.org/2001/XMLSchema",                      "dateTime");/* &RtsWebAgntSecurity_Ser'QName_1_24& */        public final static javax.xml.namespace.QName QName_1_24 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://www.w3.org/2001/XMLSchema",                      "boolean");/* &RtsWebAgntSecurity_Ser'QName_2_30& */        public final static javax.xml.namespace.QName QName_2_30 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "batchAccs");/* &RtsWebAgntSecurity_Ser'QName_2_32& */        public final static javax.xml.namespace.QName QName_2_32 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "renwlAccs");/* &RtsWebAgntSecurity_Ser'QName_2_15& */        public final static javax.xml.namespace.QName QName_2_15 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "chngTimeStmp");/* &RtsWebAgntSecurity_Ser'QName_2_35& */        public final static javax.xml.namespace.QName QName_2_35 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "submitBatchAccs");/* &RtsWebAgntSecurity_Ser'QName_2_33& */        public final static javax.xml.namespace.QName QName_2_33 =                com.ibm.ws.webservices.engine.utils.QNameTable.createQName(                      "http://data.agnt.webservices.rts.isd.txdot.com",                      "rePrntAccs");}/* #RtsWebAgntSecurity_Ser# */