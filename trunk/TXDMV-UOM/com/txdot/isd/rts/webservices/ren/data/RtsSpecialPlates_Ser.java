/**
 * RtsSpecialPlates_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.ren.data;

public class RtsSpecialPlates_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public RtsSpecialPlates_Ser(
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
        RtsSpecialPlates bean = (RtsSpecialPlates) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_3_55;
          propValue = new Integer(bean.getNewPltExpMo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_56;
          propValue = new Integer(bean.getNewPltExpYr());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_57;
          propValue = new Integer(bean.getPltExpMo());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_58;
          propValue = new Integer(bean.getPltExpYr());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_59;
          propValue = bean.getOrgNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            context.serialize(propQName, null, 
              propValue, 
              QName_1_5,
              true,null);
          }
          propQName = QName_3_60;
          propValue = new Integer(bean.getAddlSetIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
          propQName = QName_3_61;
          propValue = new Integer(bean.getPltValidityTerm());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_3_60 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "addlSetIndi");
    private final static javax.xml.namespace.QName QName_3_57 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "pltExpMo");
    private final static javax.xml.namespace.QName QName_3_58 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "pltExpYr");
    private final static javax.xml.namespace.QName QName_3_59 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "orgNo");
    private final static javax.xml.namespace.QName QName_3_61 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "pltValidityTerm");
    private final static javax.xml.namespace.QName QName_3_55 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "newPltExpMo");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_1_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_3_56 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.ren.webservices.rts.isd.txdot.com",
                  "newPltExpYr");
}
