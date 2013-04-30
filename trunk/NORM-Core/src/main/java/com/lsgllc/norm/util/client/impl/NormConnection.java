package com.lsgllc.norm.util.client.impl;

import com.lsgllc.norm.kernel.core.normgen.NormClassLoader;
import com.lsgllc.norm.kernel.graph.typing.ELEMENT_TYPES;
import com.lsgllc.norm.kernel.core.util.identity.INormId;
import com.lsgllc.norm.kernel.core.util.identity.impl.AbstractNormId;
import com.lsgllc.norm.util.client.INormConnection;
import com.lsgllc.norm.util.client.INormCreds;
import com.lsgllc.norm.util.client.INormPersistable;
import com.lsgllc.norm.util.exceptions.NormRuntimeSystemException;
import com.lsgllc.norm.util.exceptions.NormSystemException;
import com.lsgllc.norm.kernel.core.util.brokers.impl.OntologyBroker;

import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 1/3/13
 * Time: 2:05 PM
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
public class NormConnection implements INormConnection<INormId<ELEMENT_TYPES>> {
    private INormId id;
    private INormCreds creds;
    private NormClassLoader classLoader;// TODO: Temporary declaration

    public NormConnection(OntologyBroker graghManagerId) {
        this(new DefaultNormCreds(), new AbstractNormId(UUID.randomUUID()));
        // TODO: Temporary
        this.classLoader = new NormClassLoader(graghManagerId.getContextClassLoader());
    }
    public NormConnection(INormCreds creds,INormId id) {
        this.id = id;
        this.creds = creds;
    }


    @Override
    public INormCreds getCreds(INormConnection creds) {
        return this.creds;
    }

    @Override
    public INormPersistable getNewPersistable(INormPersistable iFace) throws NormRuntimeSystemException{
        Class<?> clazz = null;
        try {
            clazz = this.classLoader.loadClass(iFace.getClass());
            return (INormPersistable) clazz.newInstance();
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
            throw new NormRuntimeSystemException(new NormSystemException(e));
        }
        return null;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NormConnection)) return false;

        NormConnection that = (NormConnection) o;

        if (!creds.equals(that.creds)) return false;
        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + creds.hashCode();
        return result;
    }

    @Override
    public INormId<ELEMENT_TYPES> getId() {
        return null;
    }

    @Override
    public void setId(INormId<ELEMENT_TYPES> id) {

    }
}
