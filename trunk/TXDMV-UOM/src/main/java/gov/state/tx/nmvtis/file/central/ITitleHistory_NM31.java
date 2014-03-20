package gov.state.tx.nmvtis.file.central;

import gov.state.tx.nmvtis.file.IAssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/16/12
 * Time: 2:49 PM
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
public interface ITitleHistory_NM31 extends IAssembleNMVTISRecord {
    @Column
    Byte[] getVPHVIN();             //    1 30 VPHVIN VIN PTR HISTORY VIN
    @Column
    Byte[] getVPHTJU();             //    31 2 VPHTJU VIN PTR HISTORY TITLE JURIS
    @Column
    Byte[] getVPHTNM();             //    33 17 VPHTNM VIN PTR HISTORY TITLE NUMBER
    @Column
    Byte[] getVPHDTT();             //    50 8 VPHDTT VIN POINTER HISTORY TRANSACTION
    @Column
    Byte[] getVPHTTP();             //    58 2 VPHTTP VIN POINTER HISTORY TRANSACTION
    @Column
    Byte getVPHJDA();               //    60 1 VPHJDA VIN PTR HISTORY STATE DATA
    @Column
    Byte[] getVPHMAK();             //    61 4 VPHMAK VIN PTR HISTORY VEHICLE MAKE
    @Column
    Byte[] getVPHMYE();             //    65 4 VPHMYE VIN PTR HISTORY VEHICLE MODEL YEAR
    @Column
    Byte[] getVPHTID();             //    69 8 VPHTID VIN PTR HISTORY TITLE ISSUE DATE
    @Column
    Byte[] getVPHCDT();             //    77 8 VPHCDT VIN POINTER HISTORY CREATION DATE
    @Column
    Byte[] getVPHSTM();             //    85 6 VPHSTM VIN PTR HISTORY SYSTEM TIME
    @Column
    Byte[] getVPHSKT();             //    91 30 VPHSKT STATE KEY FOR TITLE HISTORY
    @Column
    Byte[] getVPHODM();             //    121 9 VPHODM VIN PTR HISTORY ODOMETER READING
    @Column
    Byte getVPHODU();               //    130 1 VPHODU VIN PTR HISTORY ODOMETER UNIT

    /**  REQUIRED **/
    void setVPHVIN(Byte[] vphvin);  //    1 30 VPHVIN VIN PTR HISTORY VIN
    void setVPHTJU(Byte[] vphtju);  //    31 2 VPHTJU VIN PTR HISTORY TITLE JURIS
    void setVPHTNM(Byte[] vphtnm);  //    33 17 VPHTNM VIN PTR HISTORY TITLE NUMBER
    void setVPHDTT(Byte[] vphdtt);  //    50 8 VPHDTT VIN POINTER HISTORY TRANSACTION
    void setVPHTTP(Byte[] vphttp);  //    58 2 VPHTTP VIN POINTER HISTORY TRANSACTION
    void setVPHJDA(Byte vphjda);    //    60 1 VPHJDA VIN PTR HISTORY STATE DATA
    void setVPHMAK(Byte[] vphmak);  //    61 4 VPHMAK VIN PTR HISTORY VEHICLE MAKE
    void setVPHMYE(Byte[] vphmye);  //    65 4 VPHMYE VIN PTR HISTORY VEHICLE MODEL YEAR
    void setVPHTID(Byte[] vphtid);  //    69 8 VPHTID VIN PTR HISTORY TITLE ISSUE DATE
    void setVPHCDT(Byte[] vphcdt);  //    77 8 VPHCDT VIN POINTER HISTORY CREATION DATE
    void setVPHSTM(Byte[] vphstm);  //    85 6 VPHSTM VIN PTR HISTORY SYSTEM TIME
    void setVPHODM(Byte[] vphodm);  //    121 9 VPHODM VIN PTR HISTORY ODOMETER READING

}
