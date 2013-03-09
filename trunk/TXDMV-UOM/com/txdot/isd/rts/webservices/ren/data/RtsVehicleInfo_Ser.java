/**
 * RtsVehicleInfo_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.ren.data;

public class RtsVehicleInfo_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public RtsVehicleInfo_Ser(
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
        RtsVehicleInfo bean = (RtsVehicleInfo) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_3_18;
          {
            propValue = bean.getFees();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_3_27,
                    true,null);
              }
            }
          }
          propQName = QName_3_19;
          propValue = bean.getRegistrationData();
          context.serialize(propQName, null, 
              propValue, 
              QName_3_28,
              true,null);
          propQName = QName_3_20;
          propValue = bean.getSpecialPlts();
          context.serialize(propQName, null, 
              propValue, 
              QName_3_29,
              true,null);
          propQName = QName_3_21;
          propValue = bean.getTitleData();
          context.serialize(propQName, null, 
              propValue, 
              QName_3_30,
              true,null);
          propQName = QName_3_22;
          propValue = new Double(bean.getTotalFees());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_31,
              true,null);
          propQName = QName_3_23;
          propValue = bean.getTransData();
          context.serialize(propQName, null, 
              propValue, 
              QName_3_32,
              true,null);
          propQName = QName_3_24;
          propValue = bean.getVehicleData();
          context.serialize(propQName, null, 
              propValue, 
              QName_3_33,
              true,null);
          propQName = QName_3_25;
          propValue = new Boolean(bean.isReprtAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_34,
              true,null);
          propQName = QName_3_26;
          propValue = new Boolean(bean.isVoidAccs());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_34,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_3_27 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "RTSFees");
    private final static javax.xml.namespace.QName QName_3_24 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "vehicleData");
    private final static javax.xml.namespace.QName QName_3_21 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "titleData");
    private final static javax.xml.namespace.QName QName_3_33 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "RtsVehicleData");
    private final static javax.xml.namespace.QName QName_3_26 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "voidAccs");
    private final static javax.xml.namespace.QName QName_3_25 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "reprtAccs");
    private final static javax.xml.namespace.QName QName_3_23 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "transData");
    private final static javax.xml.namespace.QName QName_1_34 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "boolean");
    private final static javax.xml.namespace.QName QName_3_28 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "RtsRegistrationData");
    private final static javax.xml.namespace.QName QName_3_18 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "fees");
    private final static javax.xml.namespace.QName QName_3_32 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "RtsTransactionData");
    private final static javax.xml.namespace.QName QName_3_29 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "RtsSpecialPlates");
    private final static javax.xml.namespace.QName QName_3_20 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "specialPlts");
    private final static javax.xml.namespace.QName QName_3_19 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "registrationData");
    private final static javax.xml.namespace.QName QName_3_22 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "totalFees");
    private final static javax.xml.namespace.QName QName_1_31 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "double");
    private final static javax.xml.namespace.QName QName_3_30 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "RtsTitleData");
}
