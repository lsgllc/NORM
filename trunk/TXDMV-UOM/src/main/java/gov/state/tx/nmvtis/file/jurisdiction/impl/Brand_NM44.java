package gov.state.tx.nmvtis.file.jurisdiction.impl;

import gov.state.tx.nmvtis.file.impl.AssembleNMVTISRecord;
import gov.state.tx.nmvtis.file.jurisdiction.IBrand_NM44;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 11:34 AM
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
public class Brand_NM44 extends AssembleNMVTISRecord implements IBrand_NM44 {

    @Column(name = "VSKYTI", length = 30)
    protected Byte[]  vskyti;
    @Column(name = "VBRDCD", length = 7)
    protected Byte[]  vbrdcd;
    @Column(name = "VBRCOD", length = 2)
    protected Byte[]  vbrcod;
    @Column(name = "VBRDAO", length = 8)
    protected Byte[]  vbrdao;
    @Column(name = "VBRPSA", length = 3)
    protected Byte[]  vbrpsa;
    @Column(name = "VBRTSA", length = 1)
    protected Byte  vbrtsa;

    @Override
    public Byte[] getVSKYTI() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVBRDCD() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVBRCOD() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVBRDAO() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVBRPSA() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVBRTSA() {
        return new Byte[0];
    }

    @Override
    public void setVSKYTI(Byte[] vskyti) {

    }

    @Override
    public void setVBRDCD(Byte[] vbrdcd) {

    }

    @Override
    public void setVBRCOD(Byte[] vbrcod) {

    }

    @Override
    public void setVBRDAO(Byte[] vbrdao) {

    }
}
