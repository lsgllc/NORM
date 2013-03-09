/**
 * RtsPlateTypeData_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.adm.data;

public class RtsPlateTypeData_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public RtsPlateTypeData_Ser(
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
        RtsPlateTypeData bean = (RtsPlateTypeData) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_3_37;
          propValue = bean.getAddlSetApplFee();
          context.serialize(propQName, null, 
              propValue, 
              QName_4_75,
              true,null);
          propQName = QName_3_38;
          propValue = bean.getAddlSetRenwlFee();
          context.serialize(propQName, null, 
              propValue, 
              QName_4_75,
              true,null);
          propQName = QName_3_39;
          propValue = bean.getFirstSetApplFee();
          context.serialize(propQName, null, 
              propValue, 
              QName_4_75,
              true,null);
          propQName = QName_3_40;
          propValue = bean.getFirstSetRenwlFee();
          context.serialize(propQName, null, 
              propValue, 
              QName_4_75,
              true,null);
          propQName = QName_3_41;
          propValue = bean.getPLPFee();
          context.serialize(propQName, null, 
              propValue, 
              QName_4_75,
              true,null);
          propQName = QName_3_42;
          propValue = bean.getRemakeFee();
          context.serialize(propQName, null, 
              propValue, 
              QName_4_75,
              true,null);
          propQName = QName_3_43;
          propValue = bean.getReplFee();
          context.serialize(propQName, null, 
              propValue, 
              QName_4_75,
              true,null);
          propQName = QName_3_44;
          propValue = new Integer(bean.getAnnualPltIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_45;
          propValue = new Integer(bean.getDuplsAllowdCd());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_46;
          propValue = new Integer(bean.getMandPltReplAge());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_47;
          propValue = new Integer(bean.getMaxByteCount());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_48;
          propValue = new Integer(bean.getMfgProcsCd());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_49;
          propValue = new Integer(bean.getOptPltReplAge());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_50;
          propValue = new Integer(bean.getPltSetImprtnceCd());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_51;
          propValue = new Integer(bean.getPltSurchargeIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_52;
          propValue = new Integer(bean.getRegRenwlCd());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_53;
          propValue = new Integer(bean.getRTSEffDate());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_54;
          propValue = new Integer(bean.getRTSEffEndDate());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_55;
          propValue = new Integer(bean.getSpclPrortnIncrmnt());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_56;
          propValue = new Integer(bean.getUserPltNoIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_57;
          propValue = bean.getBaseRegPltCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_58;
          propValue = bean.getDispPltGrpId();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_59;
          propValue = bean.getISAAllowdCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_60;
          propValue = bean.getLimitedPltGrpId();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_61;
          propValue = bean.getLocCntyAuthCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_62;
          propValue = bean.getLocHQAuthCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_63;
          propValue = bean.getLocInetAuthCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_64;
          propValue = bean.getLocRegionAuthCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_65;
          propValue = bean.getNeedsProgramCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_66;
          propValue = bean.getPLPAcctItmCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_67;
          propValue = bean.getPltOwnrshpCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_68;
          propValue = bean.getRegPltCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_69;
          propValue = bean.getRegPltDesign();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_70;
          propValue = bean.getRenwlRtrnAddrCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_71;
          propValue = bean.getShpngAddrCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_72;
          propValue = bean.getSpclPltType();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_73;
          propValue = bean.getTrnsfrCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_74;
          propValue = bean.getRegPltCdDesc();
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
    private final static javax.xml.namespace.QName QName_3_61 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "locCntyAuthCd");
    private final static javax.xml.namespace.QName QName_3_63 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "locInetAuthCd");
    private final static javax.xml.namespace.QName QName_3_54 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "RTSEffEndDate");
    private final static javax.xml.namespace.QName QName_3_39 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "firstSetApplFee");
    private final static javax.xml.namespace.QName QName_3_52 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "regRenwlCd");
    private final static javax.xml.namespace.QName QName_3_45 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "duplsAllowdCd");
    private final static javax.xml.namespace.QName QName_3_37 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "addlSetApplFee");
    private final static javax.xml.namespace.QName QName_3_72 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "spclPltType");
    private final static javax.xml.namespace.QName QName_3_40 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "firstSetRenwlFee");
    private final static javax.xml.namespace.QName QName_3_60 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "limitedPltGrpId");
    private final static javax.xml.namespace.QName QName_3_56 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "userPltNoIndi");
    private final static javax.xml.namespace.QName QName_3_55 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "spclPrortnIncrmnt");
    private final static javax.xml.namespace.QName QName_3_58 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "dispPltGrpId");
    private final static javax.xml.namespace.QName QName_3_43 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "replFee");
    private final static javax.xml.namespace.QName QName_3_59 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "ISAAllowdCd");
    private final static javax.xml.namespace.QName QName_3_68 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "regPltCd");
    private final static javax.xml.namespace.QName QName_3_71 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "shpngAddrCd");
    private final static javax.xml.namespace.QName QName_3_51 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "pltSurchargeIndi");
    private final static javax.xml.namespace.QName QName_3_64 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "locRegionAuthCd");
    private final static javax.xml.namespace.QName QName_3_66 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "PLPAcctItmCd");
    private final static javax.xml.namespace.QName QName_3_46 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "mandPltReplAge");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_3_44 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "annualPltIndi");
    private final static javax.xml.namespace.QName QName_3_74 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "regPltCdDesc");
    private final static javax.xml.namespace.QName QName_3_73 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "trnsfrCd");
    private final static javax.xml.namespace.QName QName_3_62 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "locHQAuthCd");
    private final static javax.xml.namespace.QName QName_3_42 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "remakeFee");
    private final static javax.xml.namespace.QName QName_3_69 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "regPltDesign");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_3_70 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "renwlRtrnAddrCd");
    private final static javax.xml.namespace.QName QName_3_48 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "mfgProcsCd");
    private final static javax.xml.namespace.QName QName_3_67 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "pltOwnrshpCd");
    private final static javax.xml.namespace.QName QName_3_47 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "maxByteCount");
    private final static javax.xml.namespace.QName QName_3_50 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "pltSetImprtnceCd");
    private final static javax.xml.namespace.QName QName_3_49 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "optPltReplAge");
    private final static javax.xml.namespace.QName QName_3_65 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "needsProgramCd");
    private final static javax.xml.namespace.QName QName_4_75 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://util.services.rts.isd.txdot.com",
                  "Dollar");
    private final static javax.xml.namespace.QName QName_3_57 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "baseRegPltCd");
    private final static javax.xml.namespace.QName QName_3_38 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "addlSetRenwlFee");
    private final static javax.xml.namespace.QName QName_3_53 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "RTSEffDate");
    private final static javax.xml.namespace.QName QName_3_41 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "PLPFee");
}
