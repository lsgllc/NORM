package gov.state.tx.nmvtis.file.jurisdiction.impl;

import gov.state.tx.nmvtis.file.impl.AssembleNMVTISRecord;
import gov.state.tx.nmvtis.file.jurisdiction.ILienData_NM4B;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 11:38 AM
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
public class LienData_NM48 extends AssembleNMVTISRecord implements ILienData_NM4B {

    @Column(name = "VSKYTI", length = 30)
    protected Byte[] vskyti;
    @Column(name = "VLHNAM", length = 35)
    protected Byte[] vlhnam;
    @Column(name = "VLHADD", length = 108)
    protected Byte[] vlhadd;
    @Column(name = "VLNDAT", length = 8)
    protected Byte[] vlndat;
    @Column(name = "VLNAMO", length = 6)
    protected Byte[] vlnamo;

    @Override
    public Byte[] getVSKYTI() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVLHNAM() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVLHADD() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVLNDAT() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVLNAMO() {
        return new Byte[0];
    }
}
