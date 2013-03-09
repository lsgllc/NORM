package gov.state.tx.dmv.uom.common.vehicle;

import gov.state.tx.dmv.uom.common.organizations.IFinancial;
import gov.state.tx.dmv.uom.common.organizations.IGovernmental;
import gov.state.tx.dmv.uom.common.lien.ILien;
import gov.state.tx.dmv.uom.common.title.ITitle;

import java.io.Serializable;
import java.util.Set;

/**
 * Created By: Sam Loy
 * Date: 09/19/2012
 * Time: 9:00AM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  ${YEAR}
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public interface IVehicle<V extends IVIN, T extends VEHICLE_TYPE> extends Serializable {
    T getType();           //    VEHBDYTYPE: CHAR(2)

    String getBodyVIN();            //    VEHBDYVIN: VARCHAR(22)

    Integer getCarryingCapacity();  //    VEHCARYNGCAP: INTEGER

    String getClassification();     //    VEHCLASSCD: CHAR(8)

    Integer getEmptyWeight();       //    VEHEMPTYWT: INTEGER

    Integer getGrossWeight();       //    VEHGROSSWT: INTEGER

    Integer getWeight();            //    VEHLNGTH: SMALLINT

    V getVIN();                   //    IVIN: VARCHAR(22),VEHMK: CHAR(4),VEHMODL: CHAR(3),VEHMODLYR: SMALLINT,VEHODMTRBRND: CHAR(1)

    String getOdometerReading();    //    VEHODMTRREADNG: CHAR(6)

    String getSalePrice();          //    VEHSALESPRICE: DECIMAL(10,2)

    String getDatePurchased();      //    VEHSOLDDATE: INTEGER

    Float getTonnage();             //    VEHTON: DECIMAL(5,2)

    Float getTradeInAllowance();    //    VEHTRADEINALLOWNCE: DECIMAL(9,2)

    String getUnitNumber();         //    VEHUNITNO: CHAR(6)

    ILicensePlate getCurrentLicensePlate();

    Set<ILicensePlate> getLicensePlates(); // returnsNull if ILicensePlate.isCustom() returns false

    ITitle getTitle();

    Set<ILien> getLiens();

    ILien getLienByLienHolder(IFinancial financial);

    IGovernmental getRegisteredCounty();

}
