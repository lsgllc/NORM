package gov.state.tx.nmvtis.file.central;

import gov.state.tx.nmvtis.file.IAssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/16/12
 * Time: 2:31 PM
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
public interface ITitle_NM3O extends IAssembleNMVTISRecord {
    @Column
    Byte[] getVVHIDN();                 //    1 30 VVHIDN VEHICLE/HULL ID NO (VIN/HIN)
    @Column
    Byte[] getVTIJUR();                 //    31 2 VTIJUR TITLING JURISDICTION
    @Column
    Byte[] getVTINUM();                 //    33 17 VTINUM TITLE NUMBER
    @Column
    Byte[] getVTIDTE();                 //    50 8 VTIDTE TRANSACTION DATE
    @Column
    Byte   getBJUDAV();                 //    60 1 BJUDAV JURISDICTION DATA AVAILABLE IND.
    @Column
    Byte[] getVVHMAK();                 //    61 4 VVHMAK VEHICLE MAKE
    @Column
    Byte[] getVVHMYE();                 //    65 4 VVHMYE VEHICLE MODEL YEAR
    @Column
    Byte[] getVTIIDA();                 //    69 8 VTIIDA TITLE ISSUE DATE
    @Column
    Byte[] getVSKYTI();                 //    77 30 VSKYTI STATE TITLE KEY
    @Column
    Byte   getGVCSOT();                 //    107 1 GVCSOT CHANGE STATE OF TITLE IN PROGRESS
    @Column
    Byte[] getVODMTR();                 //    108 9 VODMTR ODOMETER READING
    @Column
    Byte   getVODUME();                 //    117 1 VODUME ODOMETER UNIT OF MEASUREMENT
    /**  REQUIRED **/
    void setVVHIDN(Byte[] vvhidn);      //    1 30 VVHIDN VEHICLE/HULL ID NO (VIN/HIN)
    void setVTIJUR(Byte[] vtijur);      //    31 2 VTIJUR TITLING JURISDICTION
    void setVTINUM(Byte[] vtinum);      //    33 17 VTINUM TITLE NUMBER
    void setVTIDTE(Byte[] vtidte);      //    50 8 VTIDTE TRANSACTION DATE
    void setBJUDAV(Byte bjudav);        //    60 1 BJUDAV JURISDICTION DATA AVAILABLE IND.
    void setVVHMAK(Byte[] vvhmak);      //    61 4 VVHMAK VEHICLE MAKE
    void setVVHMYE(Byte[] vvhmye);      //    65 4 VVHMYE VEHICLE MODEL YEAR
    void setVTIIDA(Byte[] vtiida);      //    69 8 VTIIDA TITLE ISSUE DATE
    void setGVCSOT(Byte gvcsot);        //    107 1 GVCSOT CHANGE STATE OF TITLE IN PROGRESS
    void setVODMTR(Byte[] vodmtr);      //    108 9 VODMTR ODOMETER READING
}
