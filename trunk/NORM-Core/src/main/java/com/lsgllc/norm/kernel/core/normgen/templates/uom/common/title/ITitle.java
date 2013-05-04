package com.lsgllc.norm.kernel.core.normgen.templates.uom.common.title;

import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.lien.ILien;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.vehicle.IVIN;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.vehicle.IVehicle;

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
