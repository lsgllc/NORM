/**
 * RtsDsabldPlcrdResponse_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf150720.02 v7507160841
 */

package com.txdot.isd.rts.webservices.dsabldplcrd.data;

public class RtsDsabldPlcrdResponse_Helper {
    // Type metadata
    private static com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(RtsDsabldPlcrdResponse.class);

    static {
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("acctItmCd");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "acctItmCd"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("acctItmCdDesc");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "acctItmCdDesc"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("city");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "city"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("custId");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "custId"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("custIdTypeCd");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "custIdTypeCd"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("custIdTypeDesc");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "custIdTypeDesc"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("deleteIndi");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "deleteIndi"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("delReasnDesc");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "delReasnDesc"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("errMsgNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "errMsgNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("frstNameLastName");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "frstNameLastName"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("instIndi");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "instIndi"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("invItmNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "invItmNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("ofcName");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "ofcName"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("resComptCntyNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "resComptCntyNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("RTSEffDate");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "RTSEffDate"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("RTSExpMo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "RTSExpMo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("RTSExpYr");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "RTSExpYr"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("st1");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "st1"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("st2");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "st2"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("stateCntry");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "stateCntry"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("zpCd");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://data.dsabldplcrd.webservices.rts.isd.txdot.com", "zpCd"));
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
          new RtsDsabldPlcrdResponse_Ser(
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
          new RtsDsabldPlcrdResponse_Deser(
            javaType, xmlType, typeDesc);
    };

}
