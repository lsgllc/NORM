package gov.state.tx.dmv.uom.rts;

import gov.state.tx.dmv.uom.common.vehicle.IVehicle;

import java.io.Serializable;

/**
 * Created By: sameloyiv
 * Date: 9/19/12
 * Time: 1:49 PM
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
public interface IRegistration extends Serializable {
    IVehicle getVehicle();
}
