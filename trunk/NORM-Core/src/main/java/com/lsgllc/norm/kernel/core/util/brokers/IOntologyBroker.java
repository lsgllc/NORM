package com.lsgllc.norm.kernel.core.util.brokers;

import com.lsgllc.norm.kernel.graph.identity.INormId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.OntologyId;
import com.lsgllc.norm.util.exceptions.NormSystemException;
import com.lsgllc.norm.util.impl.GRAPH_PROPERTY_KEYS;

import java.util.List;
import java.util.Map;

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
public interface IOntologyBroker<T extends INormId> extends INormBroker<T> {
    ClassLoader getContextClassLoader();
    OntologyId getOntology(String key);

}
