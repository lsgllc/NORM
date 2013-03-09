/**
 * SpecialPlateDesign_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.webapps.order.specialplateinfo.data;

public class SpecialPlateDesign_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public SpecialPlateDesign_Ser(
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
        SpecialPlateDesign bean = (SpecialPlateDesign) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_15_313;
          propValue = new Integer(bean.getPltCharHeight());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_15_314;
          propValue = new Integer(bean.getPltCharWidth());
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
          propQName = QName_15_315;
          propValue = new Integer(bean.getPltFillHeight());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_15_316;
          propValue = new Integer(bean.getPltFillWidth());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_15_317;
          propValue = new Integer(bean.getPltFillX());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_15_318;
          propValue = new Integer(bean.getPltFillY());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_15_319;
          propValue = new Integer(bean.getPltHeight());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_15_320;
          propValue = new Integer(bean.getPltLeftSpace());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_15_321;
          propValue = new Integer(bean.getPltPLPMaxChar());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_15_322;
          propValue = new Integer(bean.getPltTopSpace());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_15_323;
          propValue = new Integer(bean.getPltWidth());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_15_321 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltPLPMaxChar");
    private final static javax.xml.namespace.QName QName_15_323 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltWidth");
    private final static javax.xml.namespace.QName QName_15_322 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltTopSpace");
    private final static javax.xml.namespace.QName QName_15_315 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltFillHeight");
    private final static javax.xml.namespace.QName QName_15_318 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltFillY");
    private final static javax.xml.namespace.QName QName_15_313 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltCharHeight");
    private final static javax.xml.namespace.QName QName_15_317 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltFillX");
    private final static javax.xml.namespace.QName QName_15_319 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltHeight");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_15_314 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltCharWidth");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_15_316 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltFillWidth");
    private final static javax.xml.namespace.QName QName_15_246 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltDesign");
    private final static javax.xml.namespace.QName QName_15_320 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltLeftSpace");
}
