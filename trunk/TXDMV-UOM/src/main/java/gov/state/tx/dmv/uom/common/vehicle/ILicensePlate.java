package gov.state.tx.dmv.uom.common.vehicle;

import java.io.Serializable;
import java.util.Date;
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
public interface ILicensePlate extends Serializable {
    Boolean isCommercial();

    Boolean isCustom();

    Date getDateIssued();

    IVIN getVIN();

    Set<IVIN> getAllVINs(); // returns null if isCustom() returns false;
}
