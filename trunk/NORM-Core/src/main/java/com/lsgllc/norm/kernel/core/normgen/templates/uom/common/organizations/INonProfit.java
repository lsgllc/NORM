package com.lsgllc.norm.kernel.core.normgen.templates.uom.common.organizations;

/**
 * Created By: sameloyiv
 * Date: 9/19/12
 * Time: 1:47 PM
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
public interface INonProfit<T extends ORGANIZATION_TYPES> extends IOrganization<T> {
    String getNonProfitType();
}
