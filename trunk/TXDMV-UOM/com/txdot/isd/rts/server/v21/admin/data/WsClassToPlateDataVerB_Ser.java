/**
 * WsClassToPlateDataVerB_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.v21.admin.data;

public class WsClassToPlateDataVerB_Ser extends com.txdot.isd.rts.server.v21.admin.data.WsClassToPlateData_Ser {
    /**
     * Constructor
     */
    public WsClassToPlateDataVerB_Ser(
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
        WsClassToPlateDataVerB bean = (WsClassToPlateDataVerB) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_10_475;
          propValue = new Integer(bean.getVTPEligibleIndi());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_10_475 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.admin.v21.server.rts.isd.txdot.com",
                  "VTPEligibleIndi");
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
}
