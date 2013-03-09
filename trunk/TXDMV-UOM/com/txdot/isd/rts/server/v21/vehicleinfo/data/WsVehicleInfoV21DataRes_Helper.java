/**
 * WsVehicleInfoV21DataRes_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.server.v21.vehicleinfo.data;

public class WsVehicleInfoV21DataRes_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(WsVehicleInfoV21DataRes.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("bodyStyle");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "bodyStyle"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("documentNumber");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "documentNumber"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("make");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "make"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("model");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "model"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("modelYear");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "modelYear"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("nameOnTitleLine1");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "nameOnTitleLine1"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("nameOnTitleLine2");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "nameOnTitleLine2"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("plateCreatedDate");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "plateCreatedDate"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("regExpMonth");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "regExpMonth"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("regExpYear");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "regExpYear"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("plateNumber");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "plateNumber"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("plateStatus");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "plateStatus"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("plateType");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "plateType"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("regClassCode");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "regClassCode"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("result");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "result"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("specialRegId");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "specialRegId"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "long"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("VIN");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.vehicleinfo.v21.server.rts.isd.txdot.com", "VIN"));
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
          new WsVehicleInfoV21DataRes_Ser(
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
          new WsVehicleInfoV21DataRes_Deser(
            javaType, xmlType, typeDesc);
    };

}
