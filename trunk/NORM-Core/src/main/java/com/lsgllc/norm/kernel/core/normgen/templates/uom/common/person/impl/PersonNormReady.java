package com.lsgllc.norm.kernel.core.normgen.templates.uom.common.person.impl;

import com.lsgllc.norm.client.persistence.INormPersistable;
import com.lsgllc.norm.kernel.graph.model.instance.INormInstance;
import com.lsgllc.norm.kernel.graph.model.instance.identity.impl.ObjectInstanceId;
import com.lsgllc.norm.kernel.graph.model.instance.impl.owl.AbstractEntityInstance;
import com.lsgllc.norm.kernel.graph.model.instance.types.INSTANCE_TYPE;
import com.lsgllc.norm.util.exceptions.NormNotFoundException;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.contact.IContactInformation;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.person.IPersonNormReady;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.person.PERSON_TYPE;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.vehicle.IVehicle;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.exceptions.StrangeAndWonderfulException;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.exceptions.UOMSpecifiedException;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created By: sameloyiv
 * Date: 12/14/12
 * Time: 10:26 AM
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
public class PersonNormReady<T extends INSTANCE_TYPE, K extends ObjectInstanceId, V extends INormInstance<?,?,T>>  extends AbstractEntityInstance<K,V,T> implements IPersonNormReady<PERSON_TYPE>, INormPersistable {

    public PersonNormReady() {
        super(PersonNormReady.class.getCanonicalName(),null);
    }


    @Override
    public PERSON_TYPE getType() throws NormNotFoundException {
        return (PERSON_TYPE) this.getValue(this.getClass().getCanonicalName(), "type");
    }

    @Override
    public void setType(PERSON_TYPE personType) {
        this.setValue(personType, this.getClass().getCanonicalName(), "type");
    }
    @Override
    public Set<HashMap<IContactInformation, List<String>>> getKnownAddresses() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException {
        return (Set<HashMap<IContactInformation, List<String>>>) this.getValue(this.getClass().getCanonicalName(), "knownAddresses");
    }
    @Override
    public List<IVehicle> getAllVehicles() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException {
        return (List<IVehicle>) this.getValue(this.getClass().getCanonicalName(), "allVehicles");
    }

    @Override
    public void setKnownAddresses(Set<HashMap<IContactInformation, List<String>>> knownAddresses) throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException {
        this.setValue(knownAddresses, this.getClass().getCanonicalName(), "knownAddresses");
    }

}
