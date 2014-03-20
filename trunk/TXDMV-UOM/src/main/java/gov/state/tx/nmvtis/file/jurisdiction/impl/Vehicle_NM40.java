package gov.state.tx.nmvtis.file.jurisdiction.impl;

import gov.state.tx.nmvtis.file.impl.AssembleNMVTISRecord;
import gov.state.tx.nmvtis.file.jurisdiction.IVehicle_NM40;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 11:51 AM
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
public class Vehicle_NM40 extends AssembleNMVTISRecord implements IVehicle_NM40 {

    @Column(name = "VVHIDN",length = 30)
    protected Byte[] vvhidn;
    @Column(name = "VVHVIJ",length = 2)
    protected Byte[] vvhvij;
    @Column(name = "VVHBST",length = 3)
    protected Byte[] vvhbst;
    @Column(name = "VVHCOL",length = 3)
    protected Byte[] vvhcol;
    @Column(name = "VVHCOM",length = 3)
    protected Byte[] vvhcom;
    @Column(name = "VVHGVW",length = 9)
    protected Byte[] vvhgvw;
    @Column(name = "VVHMAK",length = 4)
    protected Byte[] vvhmak;
    @Column(name = "VVHMNA",length = 6)
    protected Byte[] vvhmna;
    @Column(name = "VVHMNU",length = 6)
    protected Byte[] vvhmnu;
    @Column(name = "VVHMYE",length = 4)
    protected Byte[] vvhmye;
    @Column(name = "VVHNCY",length = 2)
    protected Byte[] vvhncy;
    @Column(name = "VVHSMO",length = 3)
    protected Byte[] vvhsmo;
    @Column(name = "VVHVWR",length = 9)
    protected Byte[] vvhvwr;
    @Column(name = "VVHFTY",length = 1)
    protected Byte vvhfty;
    @Column(name = "VVHUCC",length = 2)
    protected Byte[] vvhucc;
    @Column(name = "VVHNDO",length = 1)
    protected Byte[] vvhndo;
    @Column(name = "VVHNAX",length = 2)
    protected Byte[] vvhnax;
    @Column(name = "VVHUL2",length = 9)
    protected Byte vvhul2;
    @Column(name = "VODMTR",length = 9)
    protected Byte[] vodmtr;
    @Column(name = "VODUME",length = 1)
    protected Byte vodume;
    @Column(name = "VODDTE",length = 8)
    protected Byte voddte;
    @Column(name = "VVHNUI",length = 1)
    protected Byte vvhnui;
    @Column(name = "VVHLEI",length = 1)
    protected Byte[] vvhlei;
    @Column(name = "VVHRTI",length = 1)
    protected Byte[] vvhrti;
    @Column(name = "VVHTYP",length = 2)
    protected Byte[] vvhtyp;
    @Column(name = "VSKYVE",length = 40)
    protected Byte[] vskyve;

    @Override
    public Byte[] getVVHIDN() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHVIJ() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHBST() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHCOL() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHCOM() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHGVW() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHMAK() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHMNA() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHMNU() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHMYE() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHNCY() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHSMO() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHVWR() {
        return new Byte[0];
    }

    @Override
    public Byte getVVHFTY() {
        return null;
    }

    @Override
    public Byte[] getVVHUCC() {
        return new Byte[0];
    }

    @Override
    public Byte getVVHNDO() {
        return null;
    }

    @Override
    public Byte[] getVVHNAX() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVVHUL2() {
        return new Byte[0];
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
    public Byte[] getVODDTE() {
        return new Byte[0];
    }

    @Override
    public Byte getVVHNUI() {
        return null;
    }

    @Override
    public Byte getVVHLEI() {
        return null;
    }

    @Override
    public Byte getVVHRTI() {
        return null;
    }

    @Override
    public Byte[] getVVHTYP() {
        return new Byte[0];
    }

    @Override
    public Byte[] getVSKYVE() {
        return new Byte[0];
    }

    @Override
    public void setVVHIDN(Byte[] vvhidn) {

    }

    @Override
    public void setVVHMAK(Byte[] vvhmak) {

    }

    @Override
    public void setVVHMYE(Byte[] vvhmye) {

    }
}
