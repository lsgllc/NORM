package gov.state.tx.nmvtis.file.central.impl;

import gov.state.tx.nmvtis.file.central.IBrandCode_NM35;
import gov.state.tx.nmvtis.file.coding.BRAND_CODES;
import gov.state.tx.nmvtis.file.impl.AssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 10:50 AM
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
public class BrandCode_NM35 extends AssembleNMVTISRecord implements IBrandCode_NM35 {
    @Column(name= "vbrcod", length = 2)
    BRAND_CODES vbrcod;
    @Column(name= "vnmbcs", length = 1)
    Byte vnmbcs;
    @Column(name= "vbrscd", length = 8)
    Byte[]  vbrscd;
    @Override
    public BRAND_CODES getVBRCOD() {
        return null;
    }

    @Override
    public Byte getVNMBCS() {
        return null;
    }

    @Override
    public Byte[] getVBRSCD() {
        return new Byte[0];
    }

    @Override
    public void setVBRCOD(BRAND_CODES vbrcod) {

    }

    @Override
    public void setVNMBCS(Byte vnmbcs) {

    }

    @Override
    public void setVBRSCD(Byte[] vbrscd) {

    }
}
