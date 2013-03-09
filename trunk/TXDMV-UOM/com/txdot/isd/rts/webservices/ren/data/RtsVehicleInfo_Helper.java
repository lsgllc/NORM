/**
 * RtsVehicleInfo_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.ren.data;

public class RtsVehicleInfo_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsVehicleInfo.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("fees");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "fees"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "RTSFees"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("registrationData");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "registrationData"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "RtsRegistrationData"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("specialPlts");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "specialPlts"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "RtsSpecialPlates"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("titleData");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "titleData"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "RtsTitleData"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("totalFees");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "totalFees"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("transData");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "transData"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "RtsTransactionData"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("vehicleData");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "vehicleData"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "RtsVehicleData"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("reprtAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "reprtAccs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("voidAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.ren.webservices.rts.isd.txdot.com", "voidAccs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
          new RtsVehicleInfo_Ser(
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
          new RtsVehicleInfo_Deser(
            javaType, xmlType, typeDesc);
    };

}
