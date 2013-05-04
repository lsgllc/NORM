package com.lsgllc.norm.kernel.core.normgen.templates.uom.common.title;

import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.vehicle.IVIN;

import java.io.Serializable;

/**
 * Created By: sameloyiv
 * Date: 9/19/12
 * Time: 12:48 PM
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
public interface IManufacturersCertificateOfOrigin<V extends IVIN> extends Serializable {
    V getVIN();
}
