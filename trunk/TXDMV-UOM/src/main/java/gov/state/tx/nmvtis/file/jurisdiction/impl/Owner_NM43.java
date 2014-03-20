package gov.state.tx.nmvtis.file.jurisdiction.impl;

import gov.state.tx.nmvtis.file.impl.AssembleNMVTISRecord;
import gov.state.tx.nmvtis.file.jurisdiction.IOwner_NM43;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 11:41 AM
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
public class Owner_NM43 extends AssembleNMVTISRecord implements IOwner_NM43 {

    @Column(name = "VSKYTI",length = 30)
    protected Byte[] vskyti;
    @Column(name = "VOWLNU",length = 2)
    protected Byte[] vowlnu;
    @Column(name = "VOWNAM",length = 35)
    protected Byte[] vownam;


    @Override
    public Byte[] getVSKYTI() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVOWLNU() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVOWNAM() {
        return new Byte[0];
    }

    @Override
    public void setVOWNAM(Byte[] vownam) {

    }
}
