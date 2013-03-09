package gov.state.tx.nmvtis.file.central.impl;

import gov.state.tx.nmvtis.file.central.ITitleHistory_NM31;
import gov.state.tx.nmvtis.file.impl.AssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 11:28 AM
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
public class TitleHistory_NM31 extends AssembleNMVTISRecord implements ITitleHistory_NM31 {
    @Column(name = "VPHVIN",length = 30)
    Byte[]  vvhidn;
    @Column(name = "VPHTJU",length = 2)
    Byte[]  vphtju;
    @Column(name = "VPHTNM",length = 17)
    Byte[]  vphtnm;
    @Column(name = "VPHDTT",length = 8)
    Byte[]  vphdtt;
    @Column(name = "VPHTTP",length = 2)
    Byte[]  vphttp;
    @Column(name = "VPHJDA",length = 1)
    Byte  vphjda;
    @Column(name = "VPHMAK",length = 4)
    Byte[]  vphmak;
    @Column(name = "VVHMYE",length = 4)
    Byte[]  VVHMYE;
    @Column(name = "VPHTID",length = 8)
    Byte[]  vphtid;
    @Column(name = "VPHCDT",length = 8)
    Byte[]  vphcdt;
    @Column(name = "VPHSTM",length = 6)
    Byte[]  vphstm;
    @Column(name = "VPHSKT",length = 30)
    Byte  vphskt;
    @Column(name = "VPHODM",length = 9)
    Byte[]  vphodm;
    @Column(name = "VPHODU",length = 1)
    Byte  vphodu;

    @Override
    public Byte[] getVPHVIN() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVPHTJU() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVPHTNM() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVPHDTT() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVPHTTP() {
        return new Byte[0];
    }

    @Override
    public Byte getVPHJDA() {
        return null;
    }

    @Override
    public Byte[] getVPHMAK() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVPHMYE() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVPHTID() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVPHCDT() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVPHSTM() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVPHSKT() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVPHODM() {
        return new Byte[0];
    }

    @Override
    public Byte getVPHODU() {
        return null;
    }

    @Override
    public void setVPHVIN(Byte[] vphvin) {

    }

    @Override
    public void setVPHTJU(Byte[] vphtju) {

    }

    @Override
    public void setVPHTNM(Byte[] vphtnm) {

    }

    @Override
    public void setVPHDTT(Byte[] vphdtt) {

    }

    @Override
    public void setVPHTTP(Byte[] vphttp) {

    }

    @Override
    public void setVPHJDA(Byte vphjda) {

    }

    @Override
    public void setVPHMAK(Byte[] vphmak) {

    }

    @Override
    public void setVPHMYE(Byte[] vphmye) {

    }

    @Override
    public void setVPHTID(Byte[] vphtid) {

    }

    @Override
    public void setVPHCDT(Byte[] vphcdt) {

    }

    @Override
    public void setVPHSTM(Byte[] vphstm) {

    }

    @Override
    public void setVPHODM(Byte[] vphodm) {

    }
}
