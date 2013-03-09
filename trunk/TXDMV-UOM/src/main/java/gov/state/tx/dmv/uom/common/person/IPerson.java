package gov.state.tx.dmv.uom.common.person;

import com.lsgllc.norm.client.persistence.INormPersistable;
import com.lsgllc.norm.util.exceptions.NormNotFoundException;
import gov.state.tx.dmv.uom.common.contact.IContactInformation;
import gov.state.tx.dmv.uom.common.vehicle.IVehicle;
import gov.state.tx.dmv.uom.exceptions.StrangeAndWonderfulException;
import gov.state.tx.dmv.uom.exceptions.UOMSpecifiedException;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
public interface IPerson<T extends PERSON_TYPE> extends INormPersistable {

    T getType() throws NormNotFoundException;
    void setType(T personType);
//
    String getFirstName() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setFirstName(String firstName) throws UOMSpecifiedException, StrangeAndWonderfulException;
////
    String getMiddleName() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setMiddleName(String middleName)throws UOMSpecifiedException, StrangeAndWonderfulException;

    //
    String getLastName() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;

    String getSalutation() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;

    String getNameSuffix() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
////
    Set<HashMap<IContactInformation,List<String>>> getKnownAddresses() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setKnownAddresses(Set<HashMap<IContactInformation,List<String>>> knownAddresses)throws UOMSpecifiedException, StrangeAndWonderfulException;
////
    List<IVehicle> getAllVehicles() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;

}
