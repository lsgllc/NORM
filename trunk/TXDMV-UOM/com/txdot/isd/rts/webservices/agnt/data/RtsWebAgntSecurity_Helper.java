/**
 * RtsWebAgntSecurity_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf20411.06 v32504192757
 */

package com.txdot.isd.rts.webservices.agnt.data;

public class RtsWebAgntSecurity_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsWebAgntSecurity.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("agncyIdntyNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "agncyIdntyNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("agntIdntyNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "agntIdntyNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("agntSecrtyIdntyNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "agntSecrtyIdntyNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("chngTimeStmp");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "chngTimeStmp"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("agncyAuthAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "agncyAuthAccs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("agncyInfoAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "agncyInfoAccs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("agntAuthAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "agntAuthAccs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("aprvBatchAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "aprvBatchAccs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("batchAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "batchAccs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("dashAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "dashAccs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("renwlAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "renwlAccs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("rePrntAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "rePrntAccs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("rptAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "rptAccs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("submitBatchAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "submitBatchAccs"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("voidAccs");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.agnt.webservices.rts.isd.txdot.com", "voidAccs"));
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
          new RtsWebAgntSecurity_Ser(
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
          new RtsWebAgntSecurity_Deser(
            javaType, xmlType, typeDesc);
    };

}
