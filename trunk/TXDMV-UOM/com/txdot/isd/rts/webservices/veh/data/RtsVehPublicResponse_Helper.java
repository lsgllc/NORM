/**
 * RtsVehPublicResponse_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.veh.data;

public class RtsVehPublicResponse_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsVehPublicResponse.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("docNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.veh.webservices.rts.isd.txdot.com", "docNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("indicators");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.veh.webservices.rts.isd.txdot.com", "indicators"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.veh.webservices.rts.isd.txdot.com", "RtsVehIndicator"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("ttlIssueDate");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.veh.webservices.rts.isd.txdot.com", "ttlIssueDate"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("vehMk");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.veh.webservices.rts.isd.txdot.com", "vehMk"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("vehModl");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.veh.webservices.rts.isd.txdot.com", "vehModl"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("vehModlYr");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.veh.webservices.rts.isd.txdot.com", "vehModlYr"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("VIN");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.veh.webservices.rts.isd.txdot.com", "VIN"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
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
          new RtsVehPublicResponse_Ser(
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
          new RtsVehPublicResponse_Deser(
            javaType, xmlType, typeDesc);
    };

}
