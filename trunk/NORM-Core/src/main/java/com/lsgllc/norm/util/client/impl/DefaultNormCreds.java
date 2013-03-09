package com.lsgllc.norm.util.client.impl;

import com.lsgllc.norm.kernel.graph.identity.INormId;
import com.lsgllc.norm.kernel.graph.identity.impl.AbstractNormId;
import com.lsgllc.norm.util.client.INormCreds;

import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 1/3/13
 * Time: 2:19 PM
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
public class DefaultNormCreds<V extends INormId<UUID>> implements INormCreds<V> {
    private V id;
    public DefaultNormCreds() {
        this.id = (V) new AbstractNormId(UUID.randomUUID());
    }

    @Override
    public V getId() {
        return this.id;
    }

    @Override
    public void setId(V id) {

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultNormCreds)) return false;

        DefaultNormCreds that = (DefaultNormCreds) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
