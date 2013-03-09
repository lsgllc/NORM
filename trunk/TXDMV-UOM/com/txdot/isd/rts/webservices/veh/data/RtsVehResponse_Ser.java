/**
 * RtsVehResponse_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.veh.data;

public class RtsVehResponse_Ser extends com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse_Ser {
    /**
     * Constructor
     */
    public RtsVehResponse_Ser(
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
        RtsVehResponse bean = (RtsVehResponse) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_7_85;
          propValue = new Integer(bean.getAddlSetIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_7_86;
          propValue = new Integer(bean.getIsaIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_7_131;
          propValue = new Integer(bean.getManufacturingDate());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_7_132;
          propValue = new Integer(bean.getPlateBirthDate());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_7_133;
          propValue = new Integer(bean.getPlateEffectiveDate());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_7_134;
          propValue = new Integer(bean.getPlateExpirationMonth());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_7_135;
          propValue = new Integer(bean.getPlateExpirationYear());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_7_136;
          propValue = new Integer(bean.getPltSetNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_7_137;
          propValue = new Integer(bean.getResCompCntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_7_138;
          propValue = new Long(bean.getSRId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_124,
              true,null);
          propQName = QName_7_139;
          propValue = bean.getAuditTrailTrans();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_140;
          propValue = bean.getDocNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_9;
          propValue = bean.getManufacturingPltNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_141;
          propValue = bean.getManufacturingStatusCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_98;
          propValue = bean.getOrgNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_142;
          propValue = bean.getPlateCode();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_130;
          propValue = bean.getPlateNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_143;
          propValue = bean.getPlateOwnerCity();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_144;
          propValue = bean.getPlateOwnerCountry();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_145;
          propValue = bean.getPlateOwnerDlrGdn();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_146;
          propValue = bean.getPlateOwnerEMail();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_147;
          propValue = bean.getPlateOwnerId();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_148;
          propValue = bean.getPlateOwnerNameLine1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_149;
          propValue = bean.getPlateOwnerNameLine2();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_150;
          propValue = bean.getPlateOwnerPhone();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_151;
          propValue = bean.getPlateOwnerState();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_152;
          propValue = bean.getPlateOwnerStreetLine1();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_153;
          propValue = bean.getPlateOwnerStreetLine2();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_154;
          propValue = bean.getPlateOwnerZipCode();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_155;
          propValue = bean.getPlateOwnerZipCodeP4();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_7_116;
          propValue = new Integer(bean.getPltValidityTerm());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_7_156;
          propValue = bean.getResult();
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
    private final static javax.xml.namespace.QName QName_7_137 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "resCompCntyNo");
    private final static javax.xml.namespace.QName QName_7_130 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateNo");
    private final static javax.xml.namespace.QName QName_7_142 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateCode");
    private final static javax.xml.namespace.QName QName_7_135 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateExpirationYear");
    private final static javax.xml.namespace.QName QName_7_154 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateOwnerZipCode");
    private final static javax.xml.namespace.QName QName_7_136 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "pltSetNo");
    private final static javax.xml.namespace.QName QName_7_155 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateOwnerZipCodeP4");
    private final static javax.xml.namespace.QName QName_7_132 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateBirthDate");
    private final static javax.xml.namespace.QName QName_7_151 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateOwnerState");
    private final static javax.xml.namespace.QName QName_7_144 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateOwnerCountry");
    private final static javax.xml.namespace.QName QName_1_124 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "long");
    private final static javax.xml.namespace.QName QName_7_134 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateExpirationMonth");
    private final static javax.xml.namespace.QName QName_7_9 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "manufacturingPltNo");
    private final static javax.xml.namespace.QName QName_7_133 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateEffectiveDate");
    private final static javax.xml.namespace.QName QName_7_140 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "docNo");
    private final static javax.xml.namespace.QName QName_7_147 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateOwnerId");
    private final static javax.xml.namespace.QName QName_7_86 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "isaIndi");
    private final static javax.xml.namespace.QName QName_7_131 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "manufacturingDate");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_7_150 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateOwnerPhone");
    private final static javax.xml.namespace.QName QName_7_141 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "manufacturingStatusCd");
    private final static javax.xml.namespace.QName QName_7_146 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateOwnerEMail");
    private final static javax.xml.namespace.QName QName_7_138 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "SRId");
    private final static javax.xml.namespace.QName QName_7_145 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateOwnerDlrGdn");
    private final static javax.xml.namespace.QName QName_7_149 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateOwnerNameLine2");
    private final static javax.xml.namespace.QName QName_7_156 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "result");
    private final static javax.xml.namespace.QName QName_7_148 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateOwnerNameLine1");
    private final static javax.xml.namespace.QName QName_7_139 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "auditTrailTrans");
    private final static javax.xml.namespace.QName QName_7_98 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "orgNo");
    private final static javax.xml.namespace.QName QName_7_116 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "pltValidityTerm");
    private final static javax.xml.namespace.QName QName_7_85 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "addlSetIndi");
    private final static javax.xml.namespace.QName QName_7_153 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateOwnerStreetLine2");
    private final static javax.xml.namespace.QName QName_7_152 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateOwnerStreetLine1");
    private final static javax.xml.namespace.QName QName_7_143 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.veh.webservices.rts.isd.txdot.com",
                  "plateOwnerCity");
}
