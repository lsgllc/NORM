/**
 * RtsPendingBatchStatus_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.webservices.bat.data;

public class RtsPendingBatchStatus_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public RtsPendingBatchStatus_Ser(
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
        RtsPendingBatchStatus bean = (RtsPendingBatchStatus) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_3_20;
          propValue = new Integer(bean.getAgncyBatchIndtyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_21;
          propValue = new Integer(bean.getAgncyIndntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_22;
          propValue = new Integer(bean.getNumberOfDays());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_23;
          propValue = new Integer(bean.getNumberOfRequests());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_24;
          propValue = new Integer(bean.getOfcIssuanceNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_25;
          propValue = bean.getOfcName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_26;
          propValue = new Boolean(bean.isBatchMaxDaysMet());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_8,
              true,null);
          propQName = QName_3_27;
          propValue = new Boolean(bean.isBatchMaxRequestsMet());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_8,
              true,null);
        }
    }
        public final static javax.xml.namespace.QName QName_3_25 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.bat.webservices.rts.isd.txdot.com",
                      "ofcName");
        public final static javax.xml.namespace.QName QName_3_23 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.bat.webservices.rts.isd.txdot.com",
                      "numberOfRequests");
        public final static javax.xml.namespace.QName QName_3_26 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.bat.webservices.rts.isd.txdot.com",
                      "batchMaxDaysMet");
        public final static javax.xml.namespace.QName QName_3_27 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.bat.webservices.rts.isd.txdot.com",
                      "batchMaxRequestsMet");
        public final static javax.xml.namespace.QName QName_3_20 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.bat.webservices.rts.isd.txdot.com",
                      "agncyBatchIndtyNo");
        public final static javax.xml.namespace.QName QName_1_8 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "boolean");
        public final static javax.xml.namespace.QName QName_3_22 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.bat.webservices.rts.isd.txdot.com",
                      "numberOfDays");
        public final static javax.xml.namespace.QName QName_1_4 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "int");
        public final static javax.xml.namespace.QName QName_3_21 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.bat.webservices.rts.isd.txdot.com",
                      "agncyIndntyNo");
        public final static javax.xml.namespace.QName QName_1_5 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://www.w3.org/2001/XMLSchema",
                      "string");
        public final static javax.xml.namespace.QName QName_3_24 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.bat.webservices.rts.isd.txdot.com",
                      "ofcIssuanceNo");
}
