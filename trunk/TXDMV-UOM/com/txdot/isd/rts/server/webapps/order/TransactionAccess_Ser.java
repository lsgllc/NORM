/**
 * TransactionAccess_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.webapps.order;

public class TransactionAccess_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public TransactionAccess_Ser(
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
        TransactionAccess bean = (TransactionAccess) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_14_293;
          {
            propValue = bean.getAaTransRequestObj();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_17_294,
                    true,null);
              }
            }
          }
        }
    }
    private final static javax.xml.namespace.QName QName_17_294 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.transaction.order.webapps.server.rts.isd.txdot.com",
                  "TransactionRequest");
    private final static javax.xml.namespace.QName QName_14_293 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://order.webapps.server.rts.isd.txdot.com",
                  "aaTransRequestObj");
}
