/**
 * VirtualInvResponse_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.webapps.order.virtualinv.data;

public class VirtualInvResponse_Ser extends com.txdot.isd.rts.server.webapps.order.common.data.AbstractResponse_Ser {
    /**
     * Constructor
     */
    public VirtualInvResponse_Ser(
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
        VirtualInvResponse bean = (VirtualInvResponse) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_16_280;
          propValue = bean.getItemCode();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_16_281;
          propValue = bean.getItemNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_16_120;
          propValue = new Boolean(bean.isIsa());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_16_290;
          propValue = bean.getManufacturingPlateNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_16_291;
          propValue = bean.getRegPlateNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_16_292;
          propValue = bean.getReqIPAddress();
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
    private final static javax.xml.namespace.QName QName_16_290 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "manufacturingPlateNo");
    private final static javax.xml.namespace.QName QName_16_281 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "itemNo");
    private final static javax.xml.namespace.QName QName_1_15 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "boolean");
    private final static javax.xml.namespace.QName QName_16_291 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "regPlateNo");
    private final static javax.xml.namespace.QName QName_16_280 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "itemCode");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_16_120 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "isa");
    private final static javax.xml.namespace.QName QName_16_292 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "reqIPAddress");
}
