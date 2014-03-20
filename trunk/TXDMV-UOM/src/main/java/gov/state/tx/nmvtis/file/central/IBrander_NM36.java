package gov.state.tx.nmvtis.file.central;

import gov.state.tx.nmvtis.file.IAssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/16/12
 * Time: 3:30 PM
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
public interface IBrander_NM36 extends IAssembleNMVTISRecord {
    @Column
    Byte[] getVBRDCD();         //    1 7 VBRDCD BRANDER CODE
    @Column
    Byte getVBRDTP();           //    8 1 VBRDTP BRANDER TYPE CODE
    @Column
    Byte[] getVBRNAM();         //    9 30 VBRNAM BRANDER NAME
    @Column
    Byte[] getVBRRAD();         //    39 8 VBRRAD DATE BRANDER ADDED
    @Column
    Byte getBRANDER();          //    47 1 VBRSTA BRANDER PARTICIPATION STATUS
    
    /**  REQUIRED **/
    void setVBRDCD(Byte[] vbrdcd);         //    1 7 VBRDCD BRANDER CODE
    void setVBRDTP(Byte vbrdtp);           //    8 1 VBRDTP BRANDER TYPE CODE
    void setVBRNAM(Byte[] vbrnam);         //    9 30 VBRNAM BRANDER NAME
    void setVBRRAD(Byte[] vbrrad);         //    39 8 VBRRAD DATE BRANDER ADDED
    void setBRANDER(Byte vbrsta);          //    47 1 VBRSTA BRANDER PARTICIPATION STATUS
}
