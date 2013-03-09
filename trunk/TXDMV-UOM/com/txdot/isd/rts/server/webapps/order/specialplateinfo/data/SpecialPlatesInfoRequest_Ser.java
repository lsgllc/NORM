/**
 * SpecialPlatesInfoRequest_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.webapps.order.specialplateinfo.data;

public class SpecialPlatesInfoRequest_Ser extends com.txdot.isd.rts.server.webapps.order.common.data.AbstractRequest_Ser {
    /**
     * Constructor
     */
    public SpecialPlatesInfoRequest_Ser(
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
        SpecialPlatesInfoRequest bean = (SpecialPlatesInfoRequest) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_15_245;
          propValue = new Integer(bean.getGrpId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_15_246;
          propValue = bean.getPltDesign();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_15_247;
          propValue = new Integer(bean.getPltId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_15_248;
          propValue = new Boolean(bean.isSpanish());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_15_249;
          propValue = bean.getPltImage();
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
    private final static javax.xml.namespace.QName QName_15_248 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "spanish");
    private final static javax.xml.namespace.QName QName_1_15 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "boolean");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_15_246 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltDesign");
    private final static javax.xml.namespace.QName QName_15_247 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltId");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_15_249 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltImage");
    private final static javax.xml.namespace.QName QName_15_245 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "grpId");
}
