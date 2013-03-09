package gov.state.tx.dmv.uom.common.person.impl;

import com.lsgllc.norm.client.persistence.INormPersistable;
import com.lsgllc.norm.kernel.graph.model.instance.INormValue;
import com.lsgllc.norm.kernel.graph.model.instance.types.INSTANCE_TYPE;
import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.graph.typing.INormType;
import com.lsgllc.norm.kernel.graph.things.impl.AbstractNormAttributeThing;
import com.lsgllc.norm.kernel.graph.things.impl.AbstractNormPropertyThing;
import com.lsgllc.norm.kernel.graph.model.instance.impl.NormScalarValue;
import com.lsgllc.norm.kernel.graph.model.meta.types.impl.NormEntityType;
import com.lsgllc.norm.kernel.graph.model.meta.types.impl.NormValueType;
import com.lsgllc.norm.util.exceptions.NormNotFoundException;
import gov.state.tx.dmv.uom.common.contact.IContactInformation;
import gov.state.tx.dmv.uom.common.person.IPerson;
import gov.state.tx.dmv.uom.common.person.PERSON_TYPE;
import gov.state.tx.dmv.uom.common.vehicle.IVehicle;
import gov.state.tx.dmv.uom.exceptions.StrangeAndWonderfulException;
import gov.state.tx.dmv.uom.exceptions.UOMSpecifiedException;
import org.objectweb.asm.Type;

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
public class Person<K extends String,V extends AbstractNormAttributeThing<AbstractNormPropertyThing<INormThing<INormValue<?>>>>> extends AbstractEntity<V,INormType<NormEntityType>> implements IPerson<PERSON_TYPE>, INormPersistable {


    public Person() {
        super("gov.state.tx.dmv.uom.common.person.impl.Person");
    }


    @Override
    public PERSON_TYPE getType() throws NormNotFoundException {
        AbstractAttribute attr = (AbstractAttribute) this.get("type");
        INormThing<INormValue<PERSON_TYPE>> prop = (INormThing<INormValue<PERSON_TYPE>>) attr.get("type");
        return prop.getNormInstance().getValue();
    }

    @Override
    public String getFirstName() throws NormNotFoundException {
        AbstractAttribute attr = (AbstractAttribute) this.get("firstName");
        INormThing<INormValue<String>> prop = (INormThing<INormValue<String>>) attr.get("type");
        return prop.getNormInstance().getValue();
    }


    @Override
    public String getMiddleName() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException {
        AbstractAttribute attr = (AbstractAttribute) this.get("middleName");
        INormThing<INormValue<String>> prop = (INormThing<INormValue<String>>) attr.get("type");
        return prop.getNormInstance().getValue();
    }

    @Override
    public String getLastName() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException {
        AbstractAttribute attr = (AbstractAttribute) this.get("lastName");
        INormThing<INormValue<String>> prop = (INormThing<INormValue<String>>) attr.get("type");
        return prop.getNormInstance().getValue();
    }

    @Override
    public String getSalutation() throws NormNotFoundException {
        AbstractAttribute attr = (AbstractAttribute) this.get("salutation");
        INormThing<INormValue<String>> prop = (INormThing<INormValue<String>>) attr.get("type");
        return prop.getNormInstance().getValue();
    }

    @Override
    public String getNameSuffix() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException {
        AbstractAttribute attr = (AbstractAttribute) this.get("nameSuffix");
        INormThing<INormValue<String>> prop = (INormThing<INormValue<String>>) attr.get("type");
        return prop.getNormInstance().getValue();
    }

    @Override
    public Set<HashMap<IContactInformation,List<String>>> getKnownAddresses() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException {
        AbstractAttribute attr = (AbstractAttribute) this.get("knownAddresses");
        INormThing<INormValue<Set<HashMap<IContactInformation,List<String>>>>> prop = (INormThing<INormValue<Set<HashMap<IContactInformation,List<String>>>>>) attr.get("type");
        return prop.getNormInstance().getValue();
    }

    @Override
    public List<IVehicle> getAllVehicles() throws UOMSpecifiedException, StrangeAndWonderfulException, NormNotFoundException {
        AbstractAttribute attr = (AbstractAttribute) this.get("allVehicles");
        INormThing<INormValue<List<IVehicle>>> prop = (INormThing<INormValue<List<IVehicle>>>) attr.get("type");
        return prop.getNormInstance().getValue();
    }


    @Override
    public void setType(PERSON_TYPE type)  {
        AbstractNormAttributeThing<AbstractNormPropertyThing<INormThing<INormValue<?>>>> nat = this.get("type");
        if (nat == null){
            INormValue<PERSON_TYPE> val = new NormScalarValue<PERSON_TYPE>(type);
            AbstractNormPropertyThing<INormThing<INormValue<?>>> attr = new AbstractNormPropertyThing<INormThing<INormValue<?>>>((INormThing<INormValue<?>>) val);
            nat = new AbstractNormAttributeThing<AbstractNormPropertyThing<INormThing<INormValue<?>>>>(attr);
            NormScalarValue<String> attrTypeVal = new NormScalarValue<String>(Type.getInternalName(PERSON_TYPE.class));
            INormType<NormScalarValue<String>> attrType = new NormAsmType<NormScalarValue<String>>(attrTypeVal);
            nat.put(attr.getId(),attr);
            nat.setType(attrType);
        }  else {
            try {
                nat.getNormInstance().get(nat.getNormInstance().getId()).getThing().setValue(type);
            } catch (NormNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.put( "type", (V) nat);
    }

//    @Override
//    public String getFirstName() throws UOMSpecifiedException, StrangeAndWonderfulException {
//        return null;
//    }


//    @Override
//    public void setMiddleName(String firstName)  throws UOMSpecifiedException, StrangeAndWonderfulException {
//        AbstractAttribute attr = new AbstractAttribute("firstName",firstName);
//        this.put((K) "firstName", (V) attr);
//    }
    public void setMiddleName(String middleName) {
        V attr = this.get("middleName");
        if (attr == null){
//            INormType<INormValue> type = new NormValueType<INormValue>(new AbstractNormId());
//            NormThing typeThing = new NormThing(type);
            INormValue<Object, INSTANCE_TYPE> value = new AbstractValue<String, NormValueType<INormValue>>(middleName, thing);
            NormThing thing = new NormThing(value);
//            thing.setType(typeThing);
            attr = (V) new AbstractAttribute("middleName", middleName);
        }

        this.put((K) "middleName", (V) attr);
    }

    public void setLastName(String lastName) {
        AbstractAttribute attr = new AbstractAttribute("lastName", lastName);
        this.put((K) "lastName", (V) attr);
    }

    public void setFirstName(String firstName) {
        AbstractAttribute attr = new AbstractAttribute("firstName", firstName);
        this.put((K) "firstName", (V) attr);
    }

    public void setSalutation(String salutation) {
        AbstractAttribute attr = new AbstractAttribute("salutation", salutation);
        this.put((K) "salutation", (V) attr);
    }

    public void setNameSuffix(String nameSuffix) {
        AbstractAttribute attr = new AbstractAttribute("nameSuffix", nameSuffix);
        this.put((K) "nameSuffix", (V) attr);
    }

    @Override
    public void setKnownAddresses(Set<HashMap<IContactInformation,List<String>>> knownAddresses)  throws UOMSpecifiedException, StrangeAndWonderfulException {
        AbstractAttribute attr = new AbstractAttribute("knownAddresses", knownAddresses);
        this.put((K) "knownAddresses", (V) attr);
    }

    public void setAllVehicles(List<IVehicle> allVehicles)  throws UOMSpecifiedException, StrangeAndWonderfulException {
        AbstractAttribute attr = new AbstractAttribute("allVehicles", allVehicles);
        this.put((K) "allVehicles", (V) attr);
    }


}
