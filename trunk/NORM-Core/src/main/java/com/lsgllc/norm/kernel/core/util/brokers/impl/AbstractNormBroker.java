package com.lsgllc.norm.kernel.core.util.brokers.impl;

import com.lsgllc.norm.kernel.core.util.brokers.INormBroker;
import com.lsgllc.norm.kernel.core.util.identity.INormId;
import org.apache.camel.*;

import java.util.concurrent.*;

/**
 * Created By: sameloyiv
 * Date: 3/11/13
 * Time: 5:14 PM
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
public class AbstractNormBroker<K, V> extends ConcurrentSkipListMap<K,V> implements INormBroker<K,V> {

    @Override
    public INormId<K> getId(String key) {
        return null;
    }

    @Override
    public ClassLoader getContextClassLoader() {
        return null;
    }

    @Override
    public V get(String key) {
        return null;
    }

    @Override
    public void remove(String key) {

    }

    @Override
    public void update(String key) {

    }

    @Override
    public K add(K key, V value) {
        return null;
    }


    @Override
    public Endpoint createEndpoint(String uri) throws Exception {
        return null;
    }

    @Override
    public boolean useRawUri() {
        return false;
    }

    @Override
    public EndpointConfiguration createConfiguration(String uri) throws Exception {
        return null;
    }

    @Override
    public ComponentConfiguration createComponentConfiguration() {
        return null;
    }

    @Override
    public void setCamelContext(CamelContext camelContext) {

    }

    @Override
    public CamelContext getCamelContext() {
        return null;
    }
}
