/**
 * VirtualInvRequest_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.webapps.order.virtualinv.data;

public class VirtualInvRequest_Ser extends com.txdot.isd.rts.server.webapps.order.common.data.AbstractRequest_Ser {
    /**
     * Constructor
     */
    public VirtualInvRequest_Ser(
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
        VirtualInvRequest bean = (VirtualInvRequest) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_16_245;
          propValue = new Integer(bean.getGrpId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_16_279;
          propValue = new Integer(bean.getGrpPltId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
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
          propQName = QName_16_282;
          propValue = new Integer(bean.getItemYr());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_16_283;
          propValue = bean.getManufacturingPltNoRequest();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_16_10;
          propValue = bean.getRegPltNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_16_284;
          propValue = bean.getRequestingIP();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_16_285;
          propValue = new Boolean(bean.isInetReq());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_16_286;
          propValue = new Boolean(bean.isIsaFlag());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_16_14;
          propValue = new Boolean(bean.isPlpFlag());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_16_287;
          propValue = bean.getSessionID();
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
    private final static javax.xml.namespace.QName QName_16_284 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "requestingIP");
    private final static javax.xml.namespace.QName QName_16_286 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "isaFlag");
    private final static javax.xml.namespace.QName QName_16_281 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "itemNo");
    private final static javax.xml.namespace.QName QName_16_283 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "manufacturingPltNoRequest");
    private final static javax.xml.namespace.QName QName_16_280 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "itemCode");
    private final static javax.xml.namespace.QName QName_1_15 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "boolean");
    private final static javax.xml.namespace.QName QName_16_285 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "inetReq");
    private final static javax.xml.namespace.QName QName_16_10 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "regPltNo");
    private final static javax.xml.namespace.QName QName_16_14 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "plpFlag");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_16_282 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "itemYr");
    private final static javax.xml.namespace.QName QName_16_287 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "sessionID");
    private final static javax.xml.namespace.QName QName_16_245 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "grpId");
    private final static javax.xml.namespace.QName QName_16_279 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.virtualinv.order.webapps.server.rts.isd.txdot.com",
                  "grpPltId");
}
