package gov.state.tx.dmv.uom.common.organizations;

import gov.state.tx.dmv.uom.common.lien.ILien;

import java.util.Set;

/**
 * Created By: sameloyiv
 * Date: 9/19/12
 * Time: 12:53 PM
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
public interface IFinancial<T extends ORGANIZATION_TYPES> extends IOrganization<T> {
    Set<ILien> getLiens();
}
