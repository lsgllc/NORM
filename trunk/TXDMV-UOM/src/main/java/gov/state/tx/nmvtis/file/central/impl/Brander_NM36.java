package gov.state.tx.nmvtis.file.central.impl;

import gov.state.tx.nmvtis.file.central.IBrander_NM36;
import gov.state.tx.nmvtis.file.impl.AssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 10:57 AM
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
public class Brander_NM36 extends AssembleNMVTISRecord implements IBrander_NM36 {

    @Column(name="VBRDCD", length = 7)
    Byte[]  vbrdcd;
    @Column(name="VBRDTP", length = 1)
    Byte    vbrdtp;
    @Column(name="VBRNAM", length = 30)
    Byte[]  vbrnam;
    @Column(name="VBRRAD", length = 8)
    Byte[]  vbrrad;
    @Column(name="BRANDER", length = 1)
    Byte    brander;

    @Override
    public Byte[] getVBRDCD() {
        return new Byte[0];
    }

    @Override
    public Byte getVBRDTP() {
        return null;
    }

    @Override
    public Byte[] getVBRNAM() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVBRRAD() {
        return new Byte[0];
    }

    @Override
    public Byte getBRANDER() {
        return null;
    }

    @Override
    public void setVBRDCD(Byte[] vbrdcd) {

    }

    @Override
    public void setVBRDTP(Byte vbrdtp) {

    }

    @Override
    public void setVBRNAM(Byte[] vbrnam) {

    }

    @Override
    public void setVBRRAD(Byte[] vbrrad) {

    }

    @Override
    public void setBRANDER(Byte vbrsta) {

    }
}
