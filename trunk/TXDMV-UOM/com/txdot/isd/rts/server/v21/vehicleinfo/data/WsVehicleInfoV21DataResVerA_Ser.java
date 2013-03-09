/**
 * WsVehicleInfoV21DataResVerA_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.v21.vehicleinfo.data;

public class WsVehicleInfoV21DataResVerA_Ser extends com.txdot.isd.rts.server.v21.vehicleinfo.data.WsVehicleInfoV21DataRes_Ser {
    /**
     * Constructor
     */
    public WsVehicleInfoV21DataResVerA_Ser(
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
        WsVehicleInfoV21DataResVerA bean = (WsVehicleInfoV21DataResVerA) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_9_175;
          {
            propValue = bean.getIndicators();
            if (propValue != null) {
              for (int i=0; i<java.lang.reflect.Array.getLength(propValue); i++) {
                context.serialize(propQName, null, 
                    java.lang.reflect.Array.get(propValue, i), 
                    QName_9_177,
                    true,null);
              }
            }
          }
          propQName = QName_9_176;
          propValue = new Integer(bean.getTtlIssueDate());
          context.serialize(propQName, null, 
              propValue, 
              QName_1_4,
              true,null);
        }
    }
    private final static javax.xml.namespace.QName QName_1_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_9_177 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "WsVehicleInfoV21Indicator");
    private final static javax.xml.namespace.QName QName_9_175 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "indicators");
    private final static javax.xml.namespace.QName QName_9_176 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://data.vehicleinfo.v21.server.rts.isd.txdot.com",
                  "ttlIssueDate");
}
