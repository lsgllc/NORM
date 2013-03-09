package gov.state.tx.nmvtis.file.central.impl;

import gov.state.tx.nmvtis.file.central.IJurisdiction_NM3B;
import gov.state.tx.nmvtis.file.impl.AssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 11:09 AM
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
public class Jurisdication_NM3B extends AssembleNMVTISRecord implements IJurisdiction_NM3B {

    @Column(name = "BJUCDE",length = 2)
    Byte[]  bjucde;
    @Column(name = "GMSANI",length = 7)
    Byte[]  gmsani;
    @Column(name = "BJUNAM",length = 25)
    Byte[]  bjunam;
    @Column(name = "VSKYTU",length = 1)
    Byte  vskytu;
    @Column(name = "VNMPRT",length = 1)
    Byte  vnmprt;
    @Column(name = "VNMPRB",length = 1)
    Byte  vnmprb;
    @Column(name = "VNMDCL",length = 9)
    Byte[]  vnmdcl;
    @Column(name = "BJUMBM",length = 9)
    Byte[]  bjumbm;
    @Column(name = "VNMMST",length = 4)
    Byte[]  vnmmst;

    @Override
    public Byte[] getBJUCDE() {
        return new Byte[0];
    }

    @Override
    public Byte[] getGMSANI() {
        return new Byte[0];
    }

    @Override
    public Byte[] getBJUNAM() {
        return new Byte[0];
    }

    @Override
    public Byte getVSKYTU() {
        return null;
    }

    @Override
    public Byte getVNMPRT() {
        return null;
    }

    @Override
    public Byte getVNMPRB() {
        return null;
    }

    @Override
    public Byte[] getVNMDCL() {
        return new Byte[0];
    }

    @Override
    public Byte[] getBJUMBM() {
        return new Byte[0];
    }

    @Override
    public Byte getVNMMST() {
        return null;
    }

    @Override
    public void setVNMPRB(Byte vnmprb) {

    }

    @Override
    public void setVNMPRT(Byte vnmprt) {

    }

    @Override
    public void setVSKYTU(Byte vskytu) {

    }

    @Override
    public void setBJUNAM(Byte[] bjunam) {

    }

    @Override
    public void setBJUCDE(Byte[] bjucde) {

    }

    @Override
    public void setGMSANI(Byte[] gmsani) {

    }
}
