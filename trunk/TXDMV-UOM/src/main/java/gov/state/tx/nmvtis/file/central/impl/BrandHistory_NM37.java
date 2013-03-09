package gov.state.tx.nmvtis.file.central.impl;

import gov.state.tx.nmvtis.file.central.IBrandHistory_NM37;
import gov.state.tx.nmvtis.file.central.IBrander_NM36;
import gov.state.tx.nmvtis.file.coding.BRAND_CODES;
import gov.state.tx.nmvtis.file.impl.AssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 11:02 AM
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
public class BrandHistory_NM37 extends AssembleNMVTISRecord implements IBrandHistory_NM37 {

    @Column(name = "VVHIDN", length = 30)
    Byte[]  vvhidn;
    @Column(name = "VBRDCD", length = 7)
    IBrander_NM36  vbrdcd;
    @Column(name = "VBRCOD", length = 2)
    BRAND_CODES  vbrcod;
    @Column(name = "VBRDAO", length = 8)
    Byte[]  vbrdao;
    @Column(name = "VBRMAK", length = 4)
    Byte[]  vbrmak;
    @Column(name = "VBRMYE", length = 4)
    Byte[]  vbrmye;
    @Column(name = "VBRDCR", length = 8)
    Byte[]  vbrdcr;
    @Column(name = "VBRPSA", length = 3)
    Byte    vbrpsa;
    @Column(name = "VBRTSA", length = 1)
    Byte[]  vbrtsa;
    @Column(name = "VBRTTP", length = 3)
    Byte[] vbrttp;

    @Override
    public Byte[] getVVHIDN() {
        return new Byte[0];
    }

    @Override
    public IBrander_NM36 getVBRDCD() {
        return null;
    }

    @Override
    public BRAND_CODES getVBRCOD() {
        return null;
    }

    @Override
    public Byte[] getVBRDAO() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVBRMAK() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVBRMYE() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVBRDCR() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVBRPSA() {
        return new Byte[0];
    }

    @Override
    public Byte getVBRTSA() {
        return null;
    }

    @Override
    public Byte[] getVBRTTP() {
        return new Byte[0];
    }

    @Override
    public void setVVHIDN(Byte[] vvhidn) {

    }

    @Override
    public void setVBRDCD(IBrander_NM36 vbrdcd) {

    }

    @Override
    public void setVBRCOD(BRAND_CODES vbrcod) {

    }

    @Override
    public void setVBRDAO(Byte[] vbrdao) {

    }

    @Override
    public void setVBRMAK(Byte[] vbrmak) {

    }

    @Override
    public void setVBRMYE(Byte[] vbrmye) {

    }

    @Override
    public void setVBRTTP(Byte[] vbrttp) {

    }
}
