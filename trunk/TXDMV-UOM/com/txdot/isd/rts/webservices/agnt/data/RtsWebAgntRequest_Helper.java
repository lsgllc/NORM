/**
 * RtsWebAgntRequest_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.agnt.data;

public class RtsWebAgntRequest_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsWebAgntRequest.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("agnt");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "agnt"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "RtsWebAgntWS"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("profileCount");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "profileCount"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
    };

    /**
     * Return type metadata object
     */
    public static com.ibm.ws.webservices.engine.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static com.ibm.ws.webservices.engine.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class javaType,  
           javax.xml.namespace.QName xmlType) {
        return 
          new RtsWebAgntRequest_Ser(
            javaType, xmlType, typeDesc);
    };

    /**
     * Get Custom Deserializer
     */
    public static com.ibm.ws.webservices.engine.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class javaType,  
           javax.xml.namespace.QName xmlType) {
        return 
          new RtsWebAgntRequest_Deser(
            javaType, xmlType, typeDesc);
    };

}
