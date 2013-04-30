package com.lsgllc.norm.util.client.impl;

import com.lsgllc.norm.kernel.core.util.brokers.impl.OntologyBroker;
import com.lsgllc.norm.kernel.graph.typing.ELEMENT_TYPES;
import com.lsgllc.norm.kernel.core.util.identity.INormId;
import com.lsgllc.norm.util.client.INormConnection;
import com.lsgllc.norm.util.client.INormCreds;
import com.lsgllc.norm.util.client.INormDataMart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 1/3/13
 * Time: 1:55 PM
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
@ContextConfiguration(locations = { "classpath:META-INF/spring/camel-context.xml" })
public class NormDataMart implements INormDataMart<INormId<ELEMENT_TYPES>> {
    private Map<INormId<UUID>,INormConnection> connections = null;
    @Autowired
    private OntologyBroker graphManager;

    public NormDataMart() {
        this.connections = new HashMap<INormId<UUID>, INormConnection>();
    }



    public INormConnection getConnection(INormCreds creds){
        INormConnection current = this.connections.get(creds.getId());
        if (current == null){
            current = new NormConnection(this.graphManager);
            this.connections.put((INormId<UUID>) current.getId(),current);
        }
        return current;
    }

    @Override
    public INormId<ELEMENT_TYPES> getId() {
        return null;
    }

    @Override
    public void setId(INormId<ELEMENT_TYPES> id) {

    }
}
