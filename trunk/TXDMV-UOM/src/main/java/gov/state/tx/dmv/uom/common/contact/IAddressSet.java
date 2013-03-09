package gov.state.tx.dmv.uom.common.contact;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;

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
public interface IAddressSet<T extends IAddress> extends Serializable {
    Set<T> getKnownAddressesByDateRecorded();
}
