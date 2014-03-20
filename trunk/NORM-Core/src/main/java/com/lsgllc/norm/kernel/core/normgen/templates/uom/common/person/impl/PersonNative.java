package com.lsgllc.norm.kernel.core.normgen.templates.uom.common.person.impl;

import com.lsgllc.norm.client.persistence.INormPersistable;
import com.lsgllc.norm.kernel.graph.model.instance.INormInstance;
import com.lsgllc.norm.kernel.graph.model.instance.identity.impl.ObjectInstanceId;
import com.lsgllc.norm.kernel.graph.model.instance.impl.owl.AbstractEntityInstance;
import com.lsgllc.norm.kernel.graph.model.instance.types.INSTANCE_TYPE;
import com.lsgllc.norm.util.exceptions.NormNotFoundException;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.contact.IContactInformation;
import com.lsgllc.norm.kernel.core.normgen.templates.uom.common.person.IPersonNative;
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
public class PersonNative implements IPersonNative<PERSON_TYPE>, INormPersistable {

    public PersonNative() {
    }

    @Override

    public PERSON_TYPE getType() throws NormNotFoundException {
        return PERSON_TYPE.CUSTOMER;
    }

    @Override
    public void setType(PERSON_TYPE personType) {

    }

    @Override
    public String getFirstName() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException {

        return (String) new Object();
    }

    @Override
    public void setFirstName(String firstName) throws UOMSpecifiedException, StrangeAndWonderfulException {
    }

    @Override
    public Set<HashMap<IContactInformation, List<String>>> getKnownAddresses() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException {
        return (Set<HashMap<IContactInformation, List<String>>>) new Object();
    }

    @Override
    public void setKnownAddresses(Set<HashMap<IContactInformation, List<String>>> knownAddresses) throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException {
    }

    @Override
    public List<IVehicle> getAllVehicles() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException {
        return (List<IVehicle>) new Object();
    }

}
