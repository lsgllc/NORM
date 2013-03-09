package gov.state.tx.nmvtis.file.jurisdiction;

import gov.state.tx.nmvtis.file.IAssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 10:15 AM
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
public interface IRegistration_NM42 extends IAssembleNMVTISRecord {
    @Column
    Byte[] getVVHIDN();                 //    1 30 VVHIDN VEHICLE/HULL ID NO (VIN/HIN)
    @Column
    Byte[] getVRGPLN();                 //    31 10 VRGPLN PLATE NUMBER
    @Column
    Byte[] getVSKYRG();                 //    41 30 VSKYRG STATE REGISTRATION KEY
    @Column
    Byte[] getVRGDEF();                 //    71 8 VRGDEF REGISTRATION EFFECTIVE DATE
    @Column
    Byte[] getVRGSTA();                 //    79 2 VRGSTA REGISTRATION STATUS
    @Column
    Byte[] getVRGSDT();                //    81 8 VRGSDT REGISTRATION STATUS DATE
    
    /** REQUIRED **/
    void setVVHIDN(Byte[] vvhidn);                 //    1 30 VVHIDN VEHICLE/HULL ID NO (VIN/HIN)
    void setVRGPLN(Byte[] vrgpln);                 //    31 10 VRGPLN PLATE NUMBER
    void setVRGDEF(Byte[] vrgdef);                 //    71 8 VRGDEF REGISTRATION EFFECTIVE DATE
    void setVRGSTA(Byte[] vrgsta);                 //    79 2 VRGSTA REGISTRATION STATUS
    void setVRGSDT(Byte[] vrgsdt);                //    81 8 VRGSDT REGISTRATION STATUS DATE

}
