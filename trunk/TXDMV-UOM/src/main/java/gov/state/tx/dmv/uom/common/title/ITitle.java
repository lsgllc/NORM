package gov.state.tx.dmv.uom.common.title;

import gov.state.tx.dmv.uom.common.lien.ILien;
import gov.state.tx.dmv.uom.common.vehicle.IVIN;
import gov.state.tx.dmv.uom.common.vehicle.IVehicle;

import java.util.Set;

/**
 * Created By: sameloyiv
 * Date: 9/19/12
 * Time: 12:49 PM
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
public interface ITitle extends IManufacturersCertificateOfOrigin {
    Set<ILien> getLiens();

    IVehicle getVehicle();
}
