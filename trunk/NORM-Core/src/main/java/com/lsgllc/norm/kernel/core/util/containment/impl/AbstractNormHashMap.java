package com.lsgllc.norm.kernel.core.util.containment.impl;

import java.util.HashMap;

/**
 * Created By: sameloyiv
 * Date: 12/14/12
 * Time: 11:16 AM
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
public abstract class AbstractNormHashMap<K ,V> extends HashMap<K,V> {
    enum RELATIONSHIP {HAS_ONE_OR_MORE, HAS_ZERO_OR_MORE, IS_CONTAINED_BY, REFERENCED_BY}

    private V theThing = null;

}
