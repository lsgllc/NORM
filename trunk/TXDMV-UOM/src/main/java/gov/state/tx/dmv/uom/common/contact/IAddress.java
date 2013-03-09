package gov.state.tx.dmv.uom.common.contact;

import geo.google.datamodel.GeoUsAddress;
import gov.state.tx.dmv.uom.common.organizations.IOrganization;

import java.io.Serializable;
import java.util.List;

/**
 * Created By: Sam Loy
 * Date: 09/19/2012
 * Time: 9:00AM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  ${YEAR}
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public interface IAddress<T extends CONTACT_TYPES> extends Serializable {
    String getStreetAddress1();

    String getStreetAddress2();

    String getCity();

    String getStateOrRegion();

    String getCounty();

    String getPostalCode();

    String getCountry();

    String getDateRecorded();

    List<GeoUsAddress> getStandardized();

    T getType();

    IOrganization getOrganization();

}
