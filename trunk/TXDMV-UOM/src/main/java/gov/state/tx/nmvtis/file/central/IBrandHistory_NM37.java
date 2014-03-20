package gov.state.tx.nmvtis.file.central;

import gov.state.tx.nmvtis.file.IAssembleNMVTISRecord;
import gov.state.tx.nmvtis.file.coding.BRAND_CODES;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/16/12
 * Time: 3:42 PM
 * <p/>
 * <p/>
 * (c) Loy Services Group, LLC. 2008-2014
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
@Entity
public interface IBrandHistory_NM37 extends IAssembleNMVTISRecord {
    @Column
    Byte[] getVVHIDN();                     //    1 30 VVHIDN VEHICLE/HULL ID NO (VIN/HIN)
    @Column
    IBrander_NM36 getVBRDCD();               //    31 7 VBRDCD BRANDER CODE
    @Column
    BRAND_CODES getVBRCOD();                //    38 2 VBRCOD BRAND CODE
    @Column
    Byte[] getVBRDAO();                     //    40 8 VBRDAO BRAND DATE
    @Column
    Byte[] getVBRMAK();                     //    48 4 VBRMAK VEHICLE MAKE ON BRAND
    @Column
    Byte[] getVBRMYE();                     //    52 4 VBRMYE VEHICLE MODEL YEAR ON BRAND
    @Column
    Byte[] getVBRDCR();                     //    56 8 VBRDCR BRAND TRANSACTION DATE
    @Column
    Byte[] getVBRPSA();                     //    64 3 VBRPSA BRAND PERCENT OF DAMAGE
    @Column
    Byte getVBRTSA();                       //    67 1 VBRTSA PERCENT OF DAMAGE TYPE
    @Column
    Byte[] getVBRTTP();                     //    68 2 VBRTTP NMVTIS BRAND TRANSACTION TYPE

    /**  REQUIRED **/
    void setVVHIDN(Byte[] vvhidn);          //    1 30 VVHIDN VEHICLE/HULL ID NO (VIN/HIN)
    void setVBRDCD(IBrander_NM36 vbrdcd);    //    31 7 VBRDCD BRANDER CODE
    void setVBRCOD(BRAND_CODES vbrcod);     //    38 2 VBRCOD BRAND CODE
    void setVBRDAO(Byte[] vbrdao);          //    40 8 VBRDAO BRAND DATE
    void setVBRMAK(Byte[] vbrmak);          //    48 4 VBRMAK VEHICLE MAKE ON BRAND
    void setVBRMYE(Byte[] vbrmye);          //    52 4 VBRMYE VEHICLE MODEL YEAR ON BRAND
    void setVBRTTP(Byte[] vbrttp);          //    68 2 VBRTTP NMVTIS BRAND TRANSACTION TYPE
}
