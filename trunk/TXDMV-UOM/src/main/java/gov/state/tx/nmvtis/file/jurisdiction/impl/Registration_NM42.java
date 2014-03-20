package gov.state.tx.nmvtis.file.jurisdiction.impl;

import gov.state.tx.nmvtis.file.impl.AssembleNMVTISRecord;
import gov.state.tx.nmvtis.file.jurisdiction.IRegistration_NM42;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 11:44 AM
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
public class Registration_NM42 extends AssembleNMVTISRecord implements IRegistration_NM42 {

    @Column(name = "VVHIDN",length = 30)
    protected Byte[] vvhidn;
    @Column(name = "VRGPLN",length = 10)
    protected Byte[] vrgpln;
    @Column(name = "VSKYRG",length = 30)
    protected Byte[] vskyrg;
    @Column(name = "VRGDEF",length = 8)
    protected Byte[] vrgdef;
    @Column(name = "VRGSTA",length = 2)
    protected Byte[] vrgsta;
    @Column(name = "VRGSDT",length = 8)
    protected Byte[] vrgsdt;

    @Override
    public Byte[] getVVHIDN() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVRGPLN() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVSKYRG() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVRGDEF() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVRGSTA() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVRGSDT() {
        return new Byte[0];
    }

    @Override
    public void setVVHIDN(Byte[] vvhidn) {

    }

    @Override
    public void setVRGPLN(Byte[] vrgpln) {

    }

    @Override
    public void setVRGDEF(Byte[] vrgdef) {

    }

    @Override
    public void setVRGSTA(Byte[] vrgsta) {

    }

    @Override
    public void setVRGSDT(Byte[] vrgsdt) {

    }
}
