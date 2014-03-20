package com.lsgllc.norm.kernel.core.normgen.templates.uom.common.organizations;

import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.contact.CONTACT_TYPES;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.contact.IContactInformation;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.title.ITitle;

import java.io.Serializable;
import java.util.Set;

/**
 * Created By: sameloyiv
 * Date: 10/11/12
 * Time: 3:35 PM
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
public interface IOrganization<T extends ORGANIZATION_TYPES> extends Serializable {
    T getOrgType();

    void setOrgType(T orgType);

    Boolean isCommercial();

    Set<ITitle> getTitlesOwned();

    IContactInformation getPrimaryContact(T orgType);

    Set<IContactInformation<CONTACT_TYPES>> getAllContacts();
}
