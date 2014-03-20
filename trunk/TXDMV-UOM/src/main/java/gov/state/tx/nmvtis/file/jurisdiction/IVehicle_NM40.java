package gov.state.tx.nmvtis.file.jurisdiction;

import gov.state.tx.nmvtis.file.IAssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 9:59 AM
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
public interface IVehicle_NM40 extends IAssembleNMVTISRecord {
    @Column
    Byte [] getVVHIDN();                  //    1 30 VVHIDN VEHICLE/HULL ID NO (VIN/HIN)
    @Column
    Byte [] getVVHVIJ();                  //    31 2 VVHVIJ VIN/HIN JURISDICTION
    @Column
    Byte [] getVVHBST();                  //    33 3 VVHBST VEHICLE/VESSEL BODY TYPE
    @Column
    Byte [] getVVHCOL();                  //    36 3 VVHCOL VEHICLE/VESSEL MAJOR COLOR
    @Column
    Byte [] getVVHCOM();                  //    39 3 VVHCOM VEHICLE/VESSEL MINOR COLOR
    @Column
    Byte [] getVVHGVW();                  //    42 9 VVHGVW GROSS VEHICLE WEIGHT
    @Column
    Byte [] getVVHMAK();                  //    51 4 VVHMAK VEHICLE MAKE
    @Column
    Byte [] getVVHMNA();                  //    55 6 VVHMNA VEHICLE/VESSEL MODEL NAME
    @Column
    Byte [] getVVHMNU();                  //    61 6 VVHMNU VEHICLE MODEL
    @Column
    Byte [] getVVHMYE();                  //    67 4 VVHMYE VEHICLE MODEL YEAR
    @Column
    Byte [] getVVHNCY();                  //    71 2 VVHNCY VEHICLE NUMBER OF CYLINDERS
    @Column
    Byte [] getVVHSMO();                  //    73 3 VVHSMO VEHICLE/VESSEL SERIES MODEL
    @Column
    Byte [] getVVHVWR();                  //    76 9 VVHVWR GROSS VEHICLE WEIGHT RATING
    @Column
    Byte    getVVHFTY();                  //    85 1 VVHFTY VEHICLE FUEL TYPE
    @Column
    Byte [] getVVHUCC();                  //    86 2 VVHUCC VEHICLE USE CLASS CODE
    @Column
    Byte    getVVHNDO();                  //    88 1 VVHNDO VEHICLE NUMBER OF DOORS
    @Column
    Byte [] getVVHNAX();                  //    89 2 VVHNAX VEHICLE NUMBER OF AXLES
    @Column
    Byte [] getVVHUL2();                  //    91 9 VVHUL2 VEHICLE UNLADEN WEIGHT
    @Column
    Byte [] getVODMTR();                  //    100 9 VODMTR ODOMETER READING
    @Column
    Byte    getVODUME();                  //    109 1 VODUME ODOMETER UNIT OF MEASUREMENT
    @Column
    Byte [] getVODDTE();                  //    110 8 VODDTE ODOMETER DATE
    @Column
    Byte    getVVHNUI();                  //    118 1 VVHNUI VEHICLE/VESSEL NEW/USED INDICATOR
    @Column
    Byte    getVVHLEI();                  //    119 1 VVHLEI VEHICLE LEASE INDICATOR
    @Column
    Byte    getVVHRTI();                  //    120 1 VVHRTI VEHICLE RENTAL INDICATOR
    @Column
    Byte [] getVVHTYP();                  //    121 2 VVHTYP VEHICLE TYPE
    @Column
    Byte [] getVSKYVE();                  //    123 40 VSKYVE STATE KEY FOR VEHICLE

    /** REQUIRED **/
    void setVVHIDN(Byte[] vvhidn);                  //    1 30 VVHIDN VEHICLE/HULL ID NO (VIN/HIN)
    void setVVHMAK(Byte[] vvhmak);                  //    51 4 VVHMAK VEHICLE MAKE
    void setVVHMYE(Byte[] vvhmye);                  //    67 4 VVHMYE VEHICLE MODEL YEAR

}
