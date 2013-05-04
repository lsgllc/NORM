package com.lsgllc.norm.kernel.core.normgen.templates.uom.common.person;

import com.lsgllc.norm.client.annotations.Ontology;
import com.lsgllc.norm.client.annotations.ParameterOntology;
import com.lsgllc.norm.client.annotations.ReturnedValueOntology;
import com.lsgllc.norm.client.persistence.INormPersistable;
import com.lsgllc.norm.util.exceptions.NormNotFoundException;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.contact.IContactInformation;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.vehicle.IVehicle;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.exceptions.StrangeAndWonderfulException;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.exceptions.UOMSpecifiedException;

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
public interface IPersonNative<T extends PERSON_TYPE> extends INormPersistable {

    T getType() throws NormNotFoundException;
    void setType(T personType);

    String getFirstName() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setFirstName(String firstName) throws UOMSpecifiedException, StrangeAndWonderfulException;

    Set<HashMap<IContactInformation,List<String>>> getKnownAddresses() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setKnownAddresses(Set<HashMap<IContactInformation, List<String>>> knownAddresses)throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;

    List<IVehicle> getAllVehicles() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;

}
