/**
 * ProcessDataResponse_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.webservices.agnt;

public class ProcessDataResponse_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public ProcessDataResponse_Ser(
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
        ProcessDataResponse bean = (ProcessDataResponse) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_7_73;
          {
            propValue = bean.getProcessDataReturn();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_6_101,
                    true,null);
              }
            }
          }
        }
    }
        public final static javax.xml.namespace.QName QName_7_73 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://agnt.webservices.rts.isd.txdot.com",
                      "processDataReturn");
        public final static javax.xml.namespace.QName QName_6_101 = 
               com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                      "http://data.agnt.webservices.rts.isd.txdot.com",
                      "RtsWebAgntResponse");
}
