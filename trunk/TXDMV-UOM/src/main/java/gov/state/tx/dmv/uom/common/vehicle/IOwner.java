package gov.state.tx.dmv.uom.common.vehicle;

import gov.state.tx.dmv.uom.common.organizations.IOrganization;

import java.io.Serializable;
import java.util.Set;

/**
 * Created By: sameloyiv
 * Date: 10/11/12
 * Time: 4:22 PM
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
public interface IOwner<O extends IOrganization, V extends IVehicle> extends Serializable {
    Set<O> getOwners();

    Set<V> getVehicles();

    V getVehicleByVIN(IVIN vin);

    V getVehicleByOwner(O owner);

}
