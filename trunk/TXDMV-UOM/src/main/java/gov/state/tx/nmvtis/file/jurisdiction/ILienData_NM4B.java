package gov.state.tx.nmvtis.file.jurisdiction;

import gov.state.tx.nmvtis.file.IAssembleNMVTISRecord;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created By: sameloyiv
 * Date: 10/17/12
 * Time: 10:27 AM
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
public interface ILienData_NM4B extends IAssembleNMVTISRecord {
    @Column
    Byte[] getVSKYTI();               //    1 30 VSKYTI STATE TITLE KEY
    @Column
    Byte[] getVLHNAM();               //    31 35 VLHNAM LIENHOLDER NAME
    @Column
    Byte[] getVLHADD();                //    66 108 VLHADD LIENHOLDER ADDRESS
    @Column
    Byte[] getVLNDAT();                //    174 8 VLNDAT LIEN DATE
    @Column
    Byte[] getVLNAMO();                //    182 6 VLNAMO LIEN AMOUNT
}
