package com.lsgllc.norm.kernel.core.util.brokers;

import com.lsgllc.norm.kernel.graph.model.meta.identity.OntologyId;
import com.lsgllc.norm.kernel.graph.things.INormThing;

/*
 * $Id
 *
* The Contractor is: Loy Services Group, LLC. ***************************************************************************
 *
 * com.lsgllc.norm.util/IOntologyBroker
 * created: Apr 20, 2010 at 11:30:27 AM   1a897f10205787a6
 *
 ***************************************************************************
*/
public interface IOntologyBroker<K ,V  > extends INormBroker<K,V> {
    ClassLoader getContextClassLoader();
    OntologyId getOntology(String key);
    void removeOntology(String key);
    void updateOntology(String key);

}
