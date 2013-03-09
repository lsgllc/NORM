/**
 * CountyInfoResponse_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.webapps.order.countyinfo.data;

public class CountyInfoResponse_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(CountyInfoResponse.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("countyName");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.countyinfo.order.webapps.server.rts.isd.txdot.com", "countyName"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("countyNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.countyinfo.order.webapps.server.rts.isd.txdot.com", "countyNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("emailAddress");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.countyinfo.order.webapps.server.rts.isd.txdot.com", "emailAddress"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("mailingAddress");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.countyinfo.order.webapps.server.rts.isd.txdot.com", "mailingAddress"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.common.order.webapps.server.rts.isd.txdot.com", "Address"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("phone");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.countyinfo.order.webapps.server.rts.isd.txdot.com", "phone"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("physicalAddress");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.countyinfo.order.webapps.server.rts.isd.txdot.com", "physicalAddress"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.common.order.webapps.server.rts.isd.txdot.com", "Address"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("tacName");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.countyinfo.order.webapps.server.rts.isd.txdot.com", "tacName"));
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
          new CountyInfoResponse_Ser(
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
          new CountyInfoResponse_Deser(
            javaType, xmlType, typeDesc);
    };

}
