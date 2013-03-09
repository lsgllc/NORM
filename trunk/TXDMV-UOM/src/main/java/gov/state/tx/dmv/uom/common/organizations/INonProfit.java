package gov.state.tx.dmv.uom.common.organizations;

/**
 * Created By: sameloyiv
 * Date: 9/19/12
 * Time: 1:47 PM
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
public interface INonProfit<T extends ORGANIZATION_TYPES> extends IOrganization<T> {
    String getNonProfitType();
}
