package com.lsgllc.norm.kernel.core.normgen.templates.uom.common.contact;

import com.lsgllc.norm.client.persistence.INormPersistable;
import geo.google.datamodel.GeoAddress;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.organizations.IOrganization;

import java.util.Set;

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
public interface IContactInformation<T extends CONTACT_TYPES> extends INormPersistable {
    IAddressSet<IAddress> getAddresses();

    IAddress getAddressByType(T type);

    Set<IPhoneNumber> getPhoneNumbers();

    IPhoneNumber getPhoneByType(T type);

    Set<IEmailAddress> getEmailAddresses();

    IEmailAddress getEmailByType(T type);

    IAddressSet<IAddress> getKnownAddresses();

    IAddress getLastKnownAddress();

    GeoAddress getLastKnownAddressStandardized();

    IOrganization getOrganization();
}
