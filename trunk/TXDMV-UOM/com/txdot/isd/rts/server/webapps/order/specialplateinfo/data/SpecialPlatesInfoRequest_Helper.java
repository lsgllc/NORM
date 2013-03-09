/**
 * SpecialPlatesInfoRequest_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.webapps.order.specialplateinfo.data;

public class SpecialPlatesInfoRequest_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(SpecialPlatesInfoRequest.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("grpId");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com", "grpId"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltDesign");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com", "pltDesign"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltId");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com", "pltId"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("spanish");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com", "spanish"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("pltImage");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.specialplateinfo.order.webapps.server.rts.isd.txdot.com", "pltImage"));
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
          new SpecialPlatesInfoRequest_Ser(
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
          new SpecialPlatesInfoRequest_Deser(
            javaType, xmlType, typeDesc);
    };

}
