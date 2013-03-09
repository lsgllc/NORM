package gov.state.tx.dmv.uom.common.organizations;

import gov.state.tx.dmv.uom.common.person.IPerson;
import gov.state.tx.dmv.uom.common.person.PERSON_TYPE;

/**
 * Created By: sameloyiv
 * Date: 9/19/12
 * Time: 1:02 PM
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
public interface IGovernmental<P extends PERSON_TYPE> extends IOrganization<ORGANIZATION_TYPES> {
    IPerson<P> getOfficial(P official);
}
