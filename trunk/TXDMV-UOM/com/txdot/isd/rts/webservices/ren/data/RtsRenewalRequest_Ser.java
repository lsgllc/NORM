/**
 * RtsRenewalRequest_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.ren.data;

public class RtsRenewalRequest_Ser extends com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest_Ser {
    /**
     * Constructor
     */
    public RtsRenewalRequest_Ser(
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
        RtsRenewalRequest bean = (RtsRenewalRequest) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_6_140;
          propValue = bean.getBarCdVersionNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_135;
          propValue = bean.getDocNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_141;
          propValue = bean.getKeyTypeCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_174;
          propValue = bean.getLast4OfVin();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_175;
          propValue = bean.getNewInvItmNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_122;
          propValue = bean.getRegPltNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_6_176;
          propValue = new Integer(bean.getRequestIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_6_177;
          propValue = new Boolean(bean.isManuallyVerified());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_32,
              true,null);
          propQName = QName_6_178;
          {
            propValue = bean.getOptionalFees();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_6_100,
                    true,null);
              }
            }
          }
        }
    }
    private final static javax.xml.namespace.QName QName_6_100 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "RTSFees");
    private final static javax.xml.namespace.QName QName_6_141 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "keyTypeCd");
    private final static javax.xml.namespace.QName QName_6_122 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "regPltNo");
    private final static javax.xml.namespace.QName QName_1_32 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "boolean");
    private final static javax.xml.namespace.QName QName_6_177 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "manuallyVerified");
    private final static javax.xml.namespace.QName QName_6_174 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "last4OfVin");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_6_135 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "docNo");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_6_176 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "requestIdntyNo");
    private final static javax.xml.namespace.QName QName_6_175 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "newInvItmNo");
    private final static javax.xml.namespace.QName QName_6_178 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "optionalFees");
    private final static javax.xml.namespace.QName QName_6_140 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "barCdVersionNo");
}
