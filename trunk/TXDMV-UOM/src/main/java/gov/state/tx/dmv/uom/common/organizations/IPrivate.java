package gov.state.tx.dmv.uom.common.organizations;

import gov.state.tx.dmv.uom.common.person.IPerson;
import gov.state.tx.dmv.uom.common.person.PERSON_TYPE;

import java.util.Set;

/**
 * Created By: sameloyiv
 * Date: 10/11/12
 * Time: 4:11 PM
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
public interface IPrivate<P extends IPerson, T extends PERSON_TYPE> extends IOrganization<ORGANIZATION_TYPES> {
    Set<P> getOwners();
}
