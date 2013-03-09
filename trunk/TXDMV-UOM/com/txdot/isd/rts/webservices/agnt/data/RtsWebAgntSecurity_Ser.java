/**
 * RtsWebAgntSecurity_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.webservices.agnt.data;

public class RtsWebAgntSecurity_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public RtsWebAgntSecurity_Ser(
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
        RtsWebAgntSecurity bean = (RtsWebAgntSecurity) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_2_6;
          propValue = new Integer(bean.getAgncyIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_13;
          propValue = new Integer(bean.getAgntIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_25;
          propValue = new Integer(bean.getAgntSecrtyIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_15;
          propValue = bean.getChngTimeStmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_23,
              true,null);
          propQName = QName_2_26;
          propValue = new Boolean(bean.isAgncyAuthAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_24,
              true,null);
          propQName = QName_2_27;
          propValue = new Boolean(bean.isAgncyInfoAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_24,
              true,null);
          propQName = QName_2_28;
          propValue = new Boolean(bean.isAgntAuthAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_24,
              true,null);
          propQName = QName_2_29;
          propValue = new Boolean(bean.isAprvBatchAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_24,
              true,null);
          propQName = QName_2_30;
          propValue = new Boolean(bean.isBatchAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_24,
              true,null);
          propQName = QName_2_31;
          propValue = new Boolean(bean.isDashAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_24,
              true,null);
          propQName = QName_2_32;
          propValue = new Boolean(bean.isRenwlAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_24,
              true,null);
          propQName = QName_2_33;
          propValue = new Boolean(bean.isRePrntAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_24,
              true,null);
          propQName = QName_2_34;
          propValue = new Boolean(bean.isRptAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_24,
              true,null);
          propQName = QName_2_35;
          propValue = new Boolean(bean.isSubmitBatchAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_24,
              true,null);
          propQName = QName_2_36;
          propValue = new Boolean(bean.isVoidAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_24,
              true,null);
        }
    }
        public final static javax.xml.namespace.QName QName_2_25 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "agntSecrtyIdntyNo");
        public final static javax.xml.namespace.QName QName_2_27 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "agncyInfoAccs");
        public final static javax.xml.namespace.QName QName_2_34 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "rptAccs");
        public final static javax.xml.namespace.QName QName_2_6 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "agncyIdntyNo");
        public final static javax.xml.namespace.QName QName_2_28 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "agntAuthAccs");
        public final static javax.xml.namespace.QName QName_2_26 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "agncyAuthAccs");
        public final static javax.xml.namespace.QName QName_2_36 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "voidAccs");
        public final static javax.xml.namespace.QName QName_2_31 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "dashAccs");
        public final static javax.xml.namespace.QName QName_2_13 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "agntIdntyNo");
        public final static javax.xml.namespace.QName QName_2_29 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "aprvBatchAccs");
        public final static javax.xml.namespace.QName QName_1_4 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "int");
        public final static javax.xml.namespace.QName QName_1_23 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "dateTime");
        public final static javax.xml.namespace.QName QName_1_24 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "boolean");
        public final static javax.xml.namespace.QName QName_2_30 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "batchAccs");
        public final static javax.xml.namespace.QName QName_2_32 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "renwlAccs");
        public final static javax.xml.namespace.QName QName_2_15 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "chngTimeStmp");
        public final static javax.xml.namespace.QName QName_2_35 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "submitBatchAccs");
        public final static javax.xml.namespace.QName QName_2_33 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "rePrntAccs");
}
