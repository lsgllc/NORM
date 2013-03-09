/**
 * RtsOfficeIdResponse_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.adm.data;

public class RtsOfficeIdResponse_Ser extends com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse_Ser {
    /**
     * Constructor
     */
    public RtsOfficeIdResponse_Ser(
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
        RtsOfficeIdResponse bean = (RtsOfficeIdResponse) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_3_25;
          {
            propValue = bean.getOfficeData();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_3_26,
                    true,null);
              }
            }
          }
        }
    }
    private final static javax.xml.namespace.QName QName_3_26 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "RtsOfficeIdsData");
    private final static javax.xml.namespace.QName QName_3_25 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.adm.webservices.rts.isd.txdot.com",
                  "officeData");
}
