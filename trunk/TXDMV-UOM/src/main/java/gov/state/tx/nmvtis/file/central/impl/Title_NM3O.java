package gov.state.tx.nmvtis.file.central.impl;

import gov.state.tx.nmvtis.file.central.ITitle_NM3O;
import gov.state.tx.nmvtis.file.impl.AssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 11:22 AM
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
public class Title_NM3O extends AssembleNMVTISRecord implements ITitle_NM3O {

    @Column(name = "VVHIDN",length = 30)
    Byte[]  vvhidn;
    @Column(name = "VTIJUR",length = 2)
    Byte[]  vtijur;
    @Column(name = "VTINUM",length = 17)
    Byte[]  vtinum;
    @Column(name = "VTIDTE",length = 8)
    Byte[]  vtidte;
    @Column(name = "VNMTTP",length = 2)
    Byte[]  vnmttp;
    @Column(name = "BJUDAV",length = 1)
    Byte  bjudav;
    @Column(name = "VVHMAK",length = 4)
    Byte[]  vvhmak;
    @Column(name = "VVHMYE",length = 4)
    Byte[]  vvhmye;
    @Column(name = "VTIIDA",length = 8)
    Byte[]  vtiida;
    @Column(name = "VSKYTI",length = 30)
    Byte[]  vskyti;
    @Column(name = "GVCSOT",length = 1)
    Byte  gvcsot;
    @Column(name = "VODMTR",length = 9)
    Byte[]  vodmtr;
    @Column(name = "VODUME",length = 1)
    Byte  vodume;

    @Override
    public Byte[] getVVHIDN() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVTIJUR() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVTINUM() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVTIDTE() {
        return new Byte[0];
    }

    @Override
    public Byte getBJUDAV() {
        return null;
    }

    @Override
    public Byte[] getVVHMAK() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHMYE() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVTIIDA() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVSKYTI() {
        return new Byte[0];
    }

    @Override
    public Byte getGVCSOT() {
        return null;
    }

    @Override
    public Byte[] getVODMTR() {
        return new Byte[0];
    }

    @Override
    public Byte getVODUME() {
        return null;
    }

    @Override
    public void setVVHIDN(Byte[] vvhidn) {

    }

    @Override
    public void setVTIJUR(Byte[] vtijur) {

    }

    @Override
    public void setVTINUM(Byte[] vtinum) {

    }

    @Override
    public void setVTIDTE(Byte[] vtidte) {

    }

    @Override
    public void setBJUDAV(Byte bjudav) {

    }

    @Override
    public void setVVHMAK(Byte[] vvhmak) {

    }

    @Override
    public void setVVHMYE(Byte[] vvhmye) {

    }

    @Override
    public void setVTIIDA(Byte[] vtiida) {

    }

    @Override
    public void setGVCSOT(Byte gvcsot) {

    }

    @Override
    public void setVODMTR(Byte[] vodmtr) {

    }
}
