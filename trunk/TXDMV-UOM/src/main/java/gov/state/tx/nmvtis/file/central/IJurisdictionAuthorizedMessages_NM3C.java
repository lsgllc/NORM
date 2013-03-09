package gov.state.tx.nmvtis.file.central;

import gov.state.tx.nmvtis.file.IAssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/16/12
 * Time: 10:35 AM
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
public interface IJurisdictionAuthorizedMessages_NM3C extends IAssembleNMVTISRecord {
    @Column
    Byte[] getGMSANI(); //    1 7 GMSANI AAMVANET NETWORK ID
    @Column
    Byte[] getGMSTYP(); //    8 2 GMSTYP MESSAGE TYPE

    /**  REQUIRED **/
    void setGMSANI(Byte[] gmsani); //    1 7 GMSANI AAMVANET NETWORK ID
    void setGMSTYP(Byte[] gmstyp); //    8 2 GMSTYP MESSAGE TYPE
}
