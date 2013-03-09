/**
 * RtsBatchListRequest_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.bat.data;

public class RtsBatchListRequest_Ser extends com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest_Ser {
    /**
     * Constructor
     */
    public RtsBatchListRequest_Ser(
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
        RtsBatchListRequest bean = (RtsBatchListRequest) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_5_84;
          propValue = new Integer(bean.getAgencyBatchIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_85;
          propValue = new Integer(bean.getAgencyIdntyNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_42;
          propValue = new Integer(bean.getOfcIssuanceNo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_5_152;
          propValue = bean.getSearchBatchStatusCd();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_5_153;
          propValue = bean.getSearchEndDate();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_31,
              true,null);
          propQName = QName_5_154;
          propValue = bean.getSearchStartDate();
          context.serialize(propQName, null, 
              propValue, 
              QName_1_31,
              true,null);
          propQName = QName_5_87;
          propValue = new Integer(bean.getTransWsId());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_5_153 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "searchEndDate");
    private final static javax.xml.namespace.QName QName_5_152 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "searchBatchStatusCd");
    private final static javax.xml.namespace.QName QName_5_85 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "agencyIdntyNo");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_5_42 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "ofcIssuanceNo");
    private final static javax.xml.namespace.QName QName_5_154 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "searchStartDate");
    private final static javax.xml.namespace.QName QName_5_84 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "agencyBatchIdntyNo");
    private final static javax.xml.namespace.QName QName_1_31 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "dateTime");
    private final static javax.xml.namespace.QName QName_5_87 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.bat.webservices.rts.isd.txdot.com",
                  "transWsId");
}
