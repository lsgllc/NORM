package gov.state.tx.dmv.uom.common.vehicle;

import java.io.Serializable;

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
public interface IVIN extends Serializable {
    boolean isValid();

    String getManufacturer();

    String getModelYear();

    String getPlantCode();

    String getVehicleAttributes();

    String getSequentialNumber();
}
