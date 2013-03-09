/**
 * RtsSessionResponse_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.ses.data;

public class RtsSessionResponse_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsSessionResponse.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("lastLoginSuccessful");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ses.webservices.rts.isd.txdot.com", "lastLoginSuccessful"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("lastLoginUnsuccessful");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ses.webservices.rts.isd.txdot.com", "lastLoginUnsuccessful"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("rtsWebAgncy");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ses.webservices.rts.isd.txdot.com", "rtsWebAgncy"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agncy.webservices.rts.isd.txdot.com", "RtsWebAgncy"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("rtsWebAgntWs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ses.webservices.rts.isd.txdot.com", "rtsWebAgntWs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "RtsWebAgntWS"));
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
          new RtsSessionResponse_Ser(
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
          new RtsSessionResponse_Deser(
            javaType, xmlType, typeDesc);
    };

}
