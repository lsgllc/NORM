package com.lsgllc.norm.kernel.core.normgen.templates.uom.common.organizations;

import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.person.IPersonNormReady;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.person.PERSON_TYPE;

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
public interface IPrivate<P extends IPersonNormReady, T extends PERSON_TYPE> extends IOrganization<ORGANIZATION_TYPES> {
    Set<P> getOwners();
}
