package gov.state.tx.nmvtis.file.jurisdiction.impl;

import gov.state.tx.nmvtis.file.impl.AssembleNMVTISRecord;
import gov.state.tx.nmvtis.file.jurisdiction.ITitle_NM41;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 11:47 AM
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
public class Title_NM41 extends AssembleNMVTISRecord implements ITitle_NM41 {

    @Column(name = "VSKYTI",length = 30)
    protected Byte[] VSKYTI;
    @Column(name = "VVHIDN",length = 30)
    protected Byte[] VVHIDN;
    @Column(name = "VTINUM",length = 17)
    protected Byte[] VTINUM;
    @Column(name = "VTISTA",length = 2)
    protected Byte[] VTISTA;
    @Column(name = "VTISTD",length = 8)
    protected Byte[] VTISTD;
    @Column(name = "VTIIDA",length = 8)
    protected Byte[] VTIIDA;
    @Column(name = "VTITYP",length = 1)
    protected Byte VTITYP;
    @Column(name = "VVHENU",length = 10)
    protected Byte[] VVHENU;
    @Column(name = "VTIPNU",length = 17)
    protected Byte[] VTIPNU;
    @Column(name = "VTIPJU",length = 2)
    protected Byte[] VTIPJU;

    @Override
    public Byte[] getVSKYTI() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHIDN() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVTINUM() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVTISTA() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVTIIDA() {
        return new Byte[0];
    }

    @Override
    public Byte getVTITYP() {
        return null;
    }

    @Override
    public Byte[] getVVHENU() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVTIPNU() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVTIPJU() {
        return new Byte[0];
    }

    @Override
    public void setVVHIDN(Byte[] vvhidn) {

    }

    @Override
    public void setVTINUM(Byte[] vtinum) {

    }

    @Override
    public void setVTISTA(Byte[] vtista) {

    }

    @Override
    public void setVTIIDA(Byte[] vtiida) {

    }

    @Override
    public void setVTITYP(Byte vtityp) {

    }
}
