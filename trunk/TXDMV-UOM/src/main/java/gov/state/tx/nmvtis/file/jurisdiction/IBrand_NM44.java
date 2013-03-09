package gov.state.tx.nmvtis.file.jurisdiction;

import gov.state.tx.nmvtis.file.IAssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 10:23 AM
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
public interface IBrand_NM44 extends IAssembleNMVTISRecord {
    @Column
    Byte[] getVSKYTI();               //    1 30 VSKYTI STATE TITLE KEY
    @Column
    Byte[] getVBRDCD();               //    31 7 VBRDCD BRANDER CODE
    @Column
    Byte[] getVBRCOD();                //    38 2 VBRCOD BRAND CODE
    @Column
    Byte[] getVBRDAO();                //    40 8 VBRDAO BRAND DATE
    @Column
    Byte[] getVBRPSA();                //    48 3 VBRPSA BRAND PERCENT OF DAMAGE
    @Column
    Byte[] getVBRTSA();                //    51 1 VBRTSA PERCENT OF DAMAGE TYPE
    
    /**  REQUIRED **/
    void setVSKYTI(Byte[] vskyti);               //    1 30 VSKYTI STATE TITLE KEY
    void setVBRDCD(Byte[] vbrdcd);               //    31 7 VBRDCD BRANDER CODE
    void setVBRCOD(Byte[] vbrcod);                //    38 2 VBRCOD BRAND CODE
    void setVBRDAO(Byte[] vbrdao);                //    40 8 VBRDAO BRAND DATE
}
