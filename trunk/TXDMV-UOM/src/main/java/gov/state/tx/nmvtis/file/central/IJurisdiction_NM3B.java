package gov.state.tx.nmvtis.file.central;

import gov.state.tx.nmvtis.file.IAssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/16/12
 * Time: 10:11 AM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2012
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
public interface IJurisdiction_NM3B extends IAssembleNMVTISRecord {
    @Column
    Byte[] getBJUCDE();                 //    1 2 BJUCDE JURISDICTION CODE
    @Column
    Byte[] getGMSANI();                 //    3 7 GMSANI AAMVANET NETWORK ID
    @Column
    Byte[] getBJUNAM();                  //    10 25 BJUNAM JURISDICTION NAME
    @Column
    Byte getVSKYTU();                  //    35 1 VSKYTU STATE TITLE KEY USED INDICATOR
    @Column
    Byte getVNMPRT();                 //    36 1 VNMPRT VIN POINTER PARTICIPATION MODE
    @Column
    Byte getVNMPRB();                  //    37 1 VNMPRB BRAND PARTICIPATION MODE
    @Column
    Byte[] getVNMDCL();                  //    38 9 VNMDCL NUMBER OF DUPLICATES CREATED AT
    @Column
    Byte[] getBJUMBM();                   //    47 9 BJUMBM MAXIMUM NUMBER OF RECEIVED BATCH
    @Column
    Byte getVNMMST();                  //    56 4 VNMMST BATCH MESSAGE SEND TIME

    /**  REQUIRED **/
    void setVNMPRB(Byte vnmprb);                  //    37 1 VNMPRB BRAND PARTICIPATION MODE
    void setVNMPRT(Byte vnmprt);                 //    36 1 VNMPRT VIN POINTER PARTICIPATION MODE
    void setVSKYTU(Byte vskytu);                  //    35 1 VSKYTU STATE TITLE KEY USED INDICATOR
    void setBJUNAM(Byte[] bjunam);                  //    10 25 BJUNAM JURISDICTION NAME
    void setBJUCDE(Byte[] bjucde);                 //    1 2 BJUCDE JURISDICTION CODE
    void setGMSANI(Byte[] gmsani);                 //    3 7 GMSANI AAMVANET NETWORK ID

}
