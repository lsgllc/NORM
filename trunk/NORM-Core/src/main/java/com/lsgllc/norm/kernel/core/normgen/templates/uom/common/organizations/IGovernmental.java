package com.lsgllc.norm.kernel.core.normgen.templates.uom.common.organizations;

import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.person.IPersonNormReady;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.person.PERSON_TYPE;

/**
 * Created By: sameloyiv
 * Date: 9/19/12
 * Time: 1:02 PM
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
public interface IGovernmental<P extends PERSON_TYPE> extends IOrganization<ORGANIZATION_TYPES> {
    IPersonNormReady<P> getOfficial(P official);
}
