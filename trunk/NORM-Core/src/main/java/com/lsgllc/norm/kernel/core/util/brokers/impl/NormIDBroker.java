package com.lsgllc.norm.kernel.core.util.brokers.impl;

import com.lsgllc.norm.kernel.core.util.containment.impl.NormUtilityContainer;
import com.lsgllc.norm.kernel.graph.typing.ELEMENT_TYPES;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 2/2/13
 * Time: 2:10 PM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2013
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
@ContextConfiguration(locations = { "classpath:../openjpa/META-INF/spring/camel-context.xml" })
public class NormIDBroker {
    private  Map<ELEMENT_TYPES,Set<UUID>> idsByType = null;


    private NormIDBroker() {
       this.idsByType = new NormUtilityContainer<ELEMENT_TYPES, Set<UUID>>();
    }

    public NormIDBroker(Map<ELEMENT_TYPES, Set<UUID>> idsByType) {
        if (idsByType != null){
            idsByType.putAll(this.idsByType);
        }
        this.idsByType = idsByType;
    }

    public ELEMENT_TYPES getIdType(UUID id) {
        for(Map.Entry<ELEMENT_TYPES,Set<UUID>> theSet:idsByType.entrySet()) {
            if (theSet.getValue().contains(id)){
                return theSet.getKey();
            }
        }
        return null;
    }

    public void addNormId(UUID id, ELEMENT_TYPES type) {
        if (!this.idsByType.containsKey(type)){
            this.idsByType.put(type,new HashSet<UUID>());
        } else {
            this.idsByType.get(type).add(id);
        }

    }
}
