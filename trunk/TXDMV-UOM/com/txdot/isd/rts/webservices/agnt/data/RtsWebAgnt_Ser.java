/**
 * RtsWebAgnt_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.webservices.agnt.data;

public class RtsWebAgnt_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public RtsWebAgnt_Ser(
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
        RtsWebAgnt bean = (RtsWebAgnt) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_2_12;
          propValue = bean.getEMail();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_13;
          propValue = new Integer(bean.getAgntIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_14;
          propValue = bean.getPhone();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_15;
          propValue = bean.getChngTimeStmp();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_23,
              true,null);
          propQName = QName_2_16;
          propValue = bean.getChngTimeStmpDate();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_23,
              true,null);
          propQName = QName_2_17;
          propValue = bean.getFstName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_18;
          propValue = new Integer(bean.getInitOfcNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_19;
          propValue = bean.getLstName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_20;
          propValue = bean.getMiName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_21;
          propValue = bean.getUserName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_2_22;
          propValue = new Boolean(bean.isDmvUserIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_24,
              true,null);
        }
    }
        public final static javax.xml.namespace.QName QName_2_12 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "EMail");
        public final static javax.xml.namespace.QName QName_2_21 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "userName");
        public final static javax.xml.namespace.QName QName_2_19 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "lstName");
        public final static javax.xml.namespace.QName QName_2_20 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "miName");
        public final static javax.xml.namespace.QName QName_2_13 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "agntIdntyNo");
        public final static javax.xml.namespace.QName QName_2_22 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "dmvUserIndi");
        public final static javax.xml.namespace.QName QName_1_24 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "boolean");
        public final static javax.xml.namespace.QName QName_2_16 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "chngTimeStmpDate");
        public final static javax.xml.namespace.QName QName_1_4 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "int");
        public final static javax.xml.namespace.QName QName_1_5 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "string");
        public final static javax.xml.namespace.QName QName_2_15 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "chngTimeStmp");
        public final static javax.xml.namespace.QName QName_2_14 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "phone");
        public final static javax.xml.namespace.QName QName_1_23 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "dateTime");
        public final static javax.xml.namespace.QName QName_2_17 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "fstName");
        public final static javax.xml.namespace.QName QName_2_18 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "initOfcNo");
}
