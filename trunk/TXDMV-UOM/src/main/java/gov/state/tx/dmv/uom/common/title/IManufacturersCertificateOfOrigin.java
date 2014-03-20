package gov.state.tx.dmv.uom.common.title;

import gov.state.tx.dmv.uom.common.vehicle.IVIN;

import java.io.Serializable;

/**
 * Created By: sameloyiv
 * Date: 9/19/12
 * Time: 12:48 PM
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
public interface IManufacturersCertificateOfOrigin<V extends IVIN> extends Serializable {
    V getVIN();
}
