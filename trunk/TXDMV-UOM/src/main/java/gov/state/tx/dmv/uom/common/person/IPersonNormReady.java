package gov.state.tx.dmv.uom.common.person;

import com.lsgllc.norm.client.annotations.Ontology;
import com.lsgllc.norm.client.annotations.ParameterOntology;
import com.lsgllc.norm.client.annotations.ReturnedValueOntology;
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
@Ontology(ontology = "class")
public interface IPersonNormReady<T extends PERSON_TYPE> extends INormPersistable {


    @ReturnedValueOntology(ontology = "T:<PERSON_TYPE>:type")
    T getType() throws NormNotFoundException;

    void setType(@ParameterOntology(ontology = "String:firstName")T personType);
    @ReturnedValueOntology(ontology = "String:firstName")
    String getFirstName() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setFirstName(@ParameterOntology(ontology = "String:firstName")String firstName) throws UOMSpecifiedException, StrangeAndWonderfulException;
    @ReturnedValueOntology(ontology = "String:middleName")
    String getMiddleName() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setMiddleName(@ParameterOntology(ontology = "String:middleName")String middleName)throws UOMSpecifiedException, StrangeAndWonderfulException;

    //
    @ReturnedValueOntology(ontology = "String:lastName")
    String getLastName() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setLastName(@ParameterOntology(ontology = "String:lastName")String lastName)throws UOMSpecifiedException, StrangeAndWonderfulException;

    @ReturnedValueOntology(ontology = "String:salutation")
    String getSalutation() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setSalutation(@ParameterOntology(ontology = "String:salutation")String salutation)throws UOMSpecifiedException, StrangeAndWonderfulException;

    @ReturnedValueOntology(ontology = "String:nameSuffix")
    String getNameSuffix() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setNameSuffix(@ParameterOntology(ontology = "String:nameSuffix")String middleName)throws UOMSpecifiedException, StrangeAndWonderfulException;
    @ReturnedValueOntology(ontology = "Set<HashMap<IContactInformation,List<String>>>:knownAddresses")
    Set<HashMap<IContactInformation,List<String>>> getKnownAddresses() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setKnownAddresses(@ParameterOntology(ontology = "Set<HashMap<IContactInformation,List<String>>>:knownAddresses")Set<HashMap<IContactInformation, List<String>>> knownAddresses)throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    @ReturnedValueOntology(ontology = "List<IVehicle>:allVehicles")
    List<IVehicle> getAllVehicles() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;

}
