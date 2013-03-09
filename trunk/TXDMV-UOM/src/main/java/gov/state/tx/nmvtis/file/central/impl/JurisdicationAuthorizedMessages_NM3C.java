package gov.state.tx.nmvtis.file.central.impl;

import gov.state.tx.nmvtis.file.central.IJurisdictionAuthorizedMessages_NM3C;
import gov.state.tx.nmvtis.file.impl.AssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 11:19 AM
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
public class JurisdicationAuthorizedMessages_NM3C extends AssembleNMVTISRecord implements IJurisdictionAuthorizedMessages_NM3C{

    @Column(name = "GMSANI", length = 7)
    Byte[]  gmsani;
    @Column(name = "GMSTYP", length = 2)
    Byte[]  gmstyp;

    @Override
    public Byte[] getGMSANI() {
        return new Byte[0];
    }

    @Override
    public Byte[] getGMSTYP() {
        return new Byte[0];
    }

    @Override
    public void setGMSANI(Byte[] gmsani) {

    }

    @Override
    public void setGMSTYP(Byte[] gmstyp) {

    }
}
