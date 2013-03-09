/**
 * WsVehicleInfoV21DataRes_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.v21.vehicleinfo.data;

public class WsVehicleInfoV21DataRes_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public WsVehicleInfoV21DataRes_Ser(
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
        WsVehicleInfoV21DataRes bean = (WsVehicleInfoV21DataRes) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_9_159;
          propValue = bean.getBodyStyle();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_9_160;
          propValue = bean.getDocumentNumber();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_9_161;
          propValue = bean.getMake();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_9_162;
          propValue = bean.getModel();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_9_163;
          propValue = new Integer(bean.getModelYear());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_9_164;
          propValue = bean.getNameOnTitleLine1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_9_165;
          propValue = bean.getNameOnTitleLine2();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_9_166;
          propValue = new Integer(bean.getPlateCreatedDate());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_9_167;
          propValue = new Integer(bean.getRegExpMonth());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_9_168;
          propValue = new Integer(bean.getRegExpYear());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_9_169;
          propValue = bean.getPlateNumber();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_9_170;
          propValue = bean.getPlateStatus();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_9_171;
          propValue = bean.getPlateType();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_9_172;
          propValue = new Integer(bean.getRegClassCode());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_9_156;
          propValue = bean.getResult();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_9_173;
          propValue = new Long(bean.getSpecialRegId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_124,
              true,null);
          propQName = QName_9_174;
          propValue = bean.getVIN();
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
    private final static javax.xml.namespace.QName QName_9_156 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "result");
    private final static javax.xml.namespace.QName QName_9_162 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "model");
    private final static javax.xml.namespace.QName QName_9_166 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "plateCreatedDate");
    private final static javax.xml.namespace.QName QName_9_171 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "plateType");
    private final static javax.xml.namespace.QName QName_9_169 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "plateNumber");
    private final static javax.xml.namespace.QName QName_1_124 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "long");
    private final static javax.xml.namespace.QName QName_9_168 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "regExpYear");
    private final static javax.xml.namespace.QName QName_9_173 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "specialRegId");
    private final static javax.xml.namespace.QName QName_9_174 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "VIN");
    private final static javax.xml.namespace.QName QName_9_170 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "plateStatus");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_9_160 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "documentNumber");
    private final static javax.xml.namespace.QName QName_9_172 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "regClassCode");
    private final static javax.xml.namespace.QName QName_9_164 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "nameOnTitleLine1");
    private final static javax.xml.namespace.QName QName_9_163 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "modelYear");
    private final static javax.xml.namespace.QName QName_9_161 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "make");
    private final static javax.xml.namespace.QName QName_9_167 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "regExpMonth");
    private final static javax.xml.namespace.QName QName_9_165 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "nameOnTitleLine2");
    private final static javax.xml.namespace.QName QName_9_159 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "bodyStyle");
}
