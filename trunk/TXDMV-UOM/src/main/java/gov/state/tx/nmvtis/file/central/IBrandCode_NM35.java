package gov.state.tx.nmvtis.file.central;

import gov.state.tx.nmvtis.file.coding.BRAND_CODES;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/16/12
 * Time: 3:33 PM
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
public interface IBrandCode_NM35 {
    @Column
    BRAND_CODES getVBRCOD();        //    1 2 VBRCOD BRAND CODE
    @Column
    Byte getVNMBCS();               //    3 1 VNMBCS BRAND FILE CODE STATUS
    @Column
    Byte[] getVBRSCD();             //    4 8 VBRSCD BRAND CODE STATUS DATE

    /**  REQUIRED **/
    void setVBRCOD(BRAND_CODES vbrcod);        //    1 2 VBRCOD BRAND CODE
    void setVNMBCS(Byte vnmbcs);               //    3 1 VNMBCS BRAND FILE CODE STATUS
    void setVBRSCD(Byte[] vbrscd);             //    4 8 VBRSCD BRAND CODE STATUS DATE
}
