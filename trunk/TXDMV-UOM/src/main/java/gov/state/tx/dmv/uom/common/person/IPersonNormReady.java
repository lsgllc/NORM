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


    @ReturnedValueOntology(ontology = "gov.state.tx.dmv.uom.common.person.PERSON_TYPE:type")
    T getType() throws NormNotFoundException;

    void setType(@ParameterOntology(ontology = "gov.state.tx.dmv.uom.common.person.PERSON_TYPE:type")T personType);
    @ReturnedValueOntology(ontology = "java.lang.String:firstName")
    String getFirstName() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setFirstName(@ParameterOntology(ontology = "java.lang.String:firstName")String firstName) throws UOMSpecifiedException, StrangeAndWonderfulException;
    @ReturnedValueOntology(ontology = "java.lang.String:middleName")
    String getMiddleName() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setMiddleName(@ParameterOntology(ontology = "java.lang.String:middleName")String middleName)throws UOMSpecifiedException, StrangeAndWonderfulException;

    //
    @ReturnedValueOntology(ontology = "java.lang.String:lastName")
    String getLastName() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setLastName(@ParameterOntology(ontology = "String:lastName")String lastName)throws UOMSpecifiedException, StrangeAndWonderfulException;

    @ReturnedValueOntology(ontology = "java.lang.String:salutation")
    String getSalutation() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setSalutation(@ParameterOntology(ontology = "String:salutation")String salutation)throws UOMSpecifiedException, StrangeAndWonderfulException;

    @ReturnedValueOntology(ontology = "java.lang.String:nameSuffix")
    String getNameSuffix() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setNameSuffix(@ParameterOntology(ontology = "java.lang.String:nameSuffix")String middleName)throws UOMSpecifiedException, StrangeAndWonderfulException;
    @ReturnedValueOntology(ontology = "java.util.Set<java.util.HashMap<gov.state.tx.dmv.uom.common.contact.IContactInformation,java.util.List<java.lang.String>>>:knownAddresses")
    Set<HashMap<IContactInformation,List<String>>> getKnownAddresses() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    void setKnownAddresses(@ParameterOntology(ontology = "java.util.Set<java.util.HashMap<gov.state.tx.dmv.uom.common.contact.IContactInformation,java.util.List<java.lang.String>>>:knownAddresses")Set<HashMap<IContactInformation, List<String>>> knownAddresses)throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;
    @ReturnedValueOntology(ontology = "java.util.List<gov.state.tx.dmv.uom.common.vehicle.IVehicle>:allVehicles")
    List<gov.state.tx.dmv.uom.common.vehicle.IVehicle> getAllVehicles() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException;

}
