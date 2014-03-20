package gov.state.tx.nmvtis.file.jurisdiction;

import gov.state.tx.nmvtis.file.IAssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 10:07 AM
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
public interface ITitle_NM41 extends IAssembleNMVTISRecord {
    @Column
    Byte[] getVSKYTI();                    //    1 30 VSKYTI STATE TITLE KEY
    @Column
    Byte[] getVVHIDN();                //    31 30 VVHIDN VEHICLE/HULL ID NO (VIN/HIN)
    @Column
    Byte[] getVTINUM();                //    61 17 VTINUM TITLE NUMBER
    @Column
    Byte[] getVTISTA();                //    78 2 VTISTA TITLE STATUS
    @Column
    Byte[] getVTIIDA();                //    88 8 VTIIDA TITLE ISSUE DATE
    @Column
    Byte   getVTITYP();                //    96 1 VTITYP TITLE TYPE
    @Column
    Byte[] getVVHENU();                //    97 10 VVHENU VEHICLE EQUIPMENT NUMBER
    @Column
    Byte[] getVTIPNU();                //    107 17 VTIPNU PREVIOUS TITLE NUMBER
    @Column
    Byte[] getVTIPJU();                //    124 2 VTIPJU PREVIOUS TITLING JURISDICTION
    
    /** REQUIRED **/
    void setVVHIDN(Byte[] vvhidn);                //    31 30 VVHIDN VEHICLE/HULL ID NO (VIN/HIN)
    void setVTINUM(Byte[] vtinum);                //    61 17 VTINUM TITLE NUMBER
    void setVTISTA(Byte[] vtista);                //    78 2 VTISTA TITLE STATUS
    void setVTIIDA(Byte[] vtiida);                //    88 8 VTIIDA TITLE ISSUE DATE
    void setVTITYP(Byte vtityp);                //    96 1 VTITYP TITLE TYPE

}
