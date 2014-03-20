package gov.state.tx.nmvtis.file.jurisdiction;

import gov.state.tx.nmvtis.file.IAssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 10:21 AM
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
public interface IOwner_NM43 extends IAssembleNMVTISRecord {
    @Column
    Byte[] getVSKYTI();                //    1 30 VSKYTI STATE TITLE KEY
    @Column
    Byte[] getVOWLNU();                //    31 2 VOWLNU TITLE OWNER LINE NUMBER
    @Column
    Byte[] getVOWNAM();                //    33 35 VOWNAM OWNER NAME           \

    /** REQUIRED **/
    void setVOWNAM(Byte[] vownam);
}
