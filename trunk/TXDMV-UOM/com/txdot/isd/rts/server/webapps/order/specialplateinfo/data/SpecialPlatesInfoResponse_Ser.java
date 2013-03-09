/**
 * SpecialPlatesInfoResponse_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.webapps.order.specialplateinfo.data;

public class SpecialPlatesInfoResponse_Ser extends com.txdot.isd.rts.server.webapps.order.common.data.AbstractResponse_Ser {
    /**
     * Constructor
     */
    public SpecialPlatesInfoResponse_Ser(
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
        SpecialPlatesInfoResponse bean = (SpecialPlatesInfoResponse) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_15_252;
          propValue = bean.getAddlSetPlpRegPltCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_15_253;
          propValue = bean.getAddlSetRegPltCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_15_254;
          {
            propValue = bean.getFees();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_12_274,
                    true,null);
              }
            }
          }
          propQName = QName_15_255;
          propValue = bean.getGrpDesc();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_15_245;
          propValue = new Integer(bean.getGrpId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_15_256;
          propValue = bean.getGrpName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_15_98;
          propValue = bean.getOrgNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_15_257;
          propValue = bean.getPlateName();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_15_258;
          propValue = bean.getPlpRegPltCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_15_259;
          propValue = bean.getPltDesc();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_15_246;
          propValue = bean.getPltDesign();
          context.serialize(propQName, null, 
              propValue, 
              QName_15_275,
              true,null);
          propQName = QName_15_260;
          propValue = bean.getPltFaxForm();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_15_261;
          propValue = bean.getPltFormId();
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
          propQName = QName_15_68;
          propValue = bean.getRegPltCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_15_262;
          propValue = bean.getSampleManufacturingPltNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_15_263;
          propValue = bean.getSpclPltAcctitmcd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_15_85;
          propValue = new Boolean(bean.isAddlSetIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_15_264;
          propValue = new Boolean(bean.isAllowAddSetOnline());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_15_265;
          propValue = new Boolean(bean.isAllowISAOnline());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_15_266;
          propValue = new Boolean(bean.isIsaAllowIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_15_267;
          propValue = new Boolean(bean.isOrderableAtCnty());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_15_268;
          propValue = new Boolean(bean.isOrderOnline());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_15_269;
          propValue = new Boolean(bean.isShowImageOnNonPLP());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_15_248;
          propValue = new Boolean(bean.isSpanish());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_15_270;
          propValue = new Boolean(bean.isUserPltNoAllowedIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_15,
              true,null);
          propQName = QName_15_271;
          propValue = bean.getPlateImageObject();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_276,
              true,null);
          propQName = QName_15_272;
          propValue = new Integer(bean.getRecordCount());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_15_273;
          propValue = new Integer(bean.getCrossoverIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_15_273 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "crossoverIndi");
    private final static javax.xml.namespace.QName QName_15_270 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "userPltNoAllowedIndi");
    private final static javax.xml.namespace.QName QName_15_267 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "orderableAtCnty");
    private final static javax.xml.namespace.QName QName_12_274 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.common.order.webapps.server.rts.isd.txdot.com",
                  "Fees");
    private final static javax.xml.namespace.QName QName_15_248 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "spanish");
    private final static javax.xml.namespace.QName QName_15_258 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "plpRegPltCd");
    private final static javax.xml.namespace.QName QName_15_262 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "sampleManufacturingPltNo");
    private final static javax.xml.namespace.QName QName_15_269 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "showImageOnNonPLP");
    private final static javax.xml.namespace.QName QName_1_276 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "anyType");
    private final static javax.xml.namespace.QName QName_15_275 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "SpecialPlateDesign");
    private final static javax.xml.namespace.QName QName_15_253 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "addlSetRegPltCd");
    private final static javax.xml.namespace.QName QName_15_245 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "grpId");
    private final static javax.xml.namespace.QName QName_15_264 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "allowAddSetOnline");
    private final static javax.xml.namespace.QName QName_15_261 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltFormId");
    private final static javax.xml.namespace.QName QName_15_85 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "addlSetIndi");
    private final static javax.xml.namespace.QName QName_15_246 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltDesign");
    private final static javax.xml.namespace.QName QName_15_266 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "isaAllowIndi");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_15_256 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "grpName");
    private final static javax.xml.namespace.QName QName_15_260 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltFaxForm");
    private final static javax.xml.namespace.QName QName_15_257 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "plateName");
    private final static javax.xml.namespace.QName QName_15_263 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "spclPltAcctitmcd");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_15_271 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "plateImageObject");
    private final static javax.xml.namespace.QName QName_15_268 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "orderOnline");
    private final static javax.xml.namespace.QName QName_15_252 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "addlSetPlpRegPltCd");
    private final static javax.xml.namespace.QName QName_1_15 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "boolean");
    private final static javax.xml.namespace.QName QName_15_265 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "allowISAOnline");
    private final static javax.xml.namespace.QName QName_15_98 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "orgNo");
    private final static javax.xml.namespace.QName QName_15_254 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "fees");
    private final static javax.xml.namespace.QName QName_15_247 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltId");
    private final static javax.xml.namespace.QName QName_15_68 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "regPltCd");
    private final static javax.xml.namespace.QName QName_15_249 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltImage");
    private final static javax.xml.namespace.QName QName_15_272 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "recordCount");
    private final static javax.xml.namespace.QName QName_15_259 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "pltDesc");
    private final static javax.xml.namespace.QName QName_15_255 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com",
                  "grpDesc");
}
