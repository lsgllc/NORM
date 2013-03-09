package gov.state.tx.dmv.uom.common.organizations;

import gov.state.tx.dmv.uom.common.contact.CONTACT_TYPES;
import gov.state.tx.dmv.uom.common.contact.IContactInformation;
import gov.state.tx.dmv.uom.common.title.ITitle;
import gov.state.tx.dmv.uom.common.person.IPerson;
import gov.state.tx.dmv.uom.common.person.PERSON_TYPE;

import java.io.Serializable;
import java.util.Set;

/**
 * Created By: sameloyiv
 * Date: 10/11/12
 * Time: 3:35 PM
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
public interface IOrganization<T extends ORGANIZATION_TYPES> extends Serializable {
    T getOrgType();

    void setOrgType(T orgType);

    Boolean isCommercial();

    Set<ITitle> getTitlesOwned();

    IContactInformation getPrimaryContact(T orgType);

    Set<IContactInformation<CONTACT_TYPES>> getAllContacts();
}
