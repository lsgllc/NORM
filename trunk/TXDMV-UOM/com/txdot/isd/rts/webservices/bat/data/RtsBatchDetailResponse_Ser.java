/**
 * RtsBatchDetailResponse_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.bat.data;

public class RtsBatchDetailResponse_Ser extends com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse_Ser {
    /**
     * Constructor
     */
    public RtsBatchDetailResponse_Ser(
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
        RtsBatchDetailResponse bean = (RtsBatchDetailResponse) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_2_14;
          {
            propValue = bean.getBatchDetailLines();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_3_17,
                    true,null);
              }
            }
          }
          propQName = QName_2_6;
          propValue = new Integer(bean.getAgencyBatchIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_7;
          propValue = new Integer(bean.getAgencyIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_15;
          propValue = new Integer(bean.getDetailCount());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_11;
          propValue = new Integer(bean.getOfcIssuanceNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_2_16;
          propValue = bean.getOfcIssuanceName();
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
    private final static javax.xml.namespace.QName QName_3_17 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "RtsVehicleInfo");
    private final static javax.xml.namespace.QName QName_2_7 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "agencyIdntyNo");
    private final static javax.xml.namespace.QName QName_2_15 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "detailCount");
    private final static javax.xml.namespace.QName QName_2_16 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "ofcIssuanceName");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_2_11 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "ofcIssuanceNo");
    private final static javax.xml.namespace.QName QName_2_6 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "agencyBatchIdntyNo");
    private final static javax.xml.namespace.QName QName_2_14 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "batchDetailLines");
}
