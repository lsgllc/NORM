package com.lsgllc.norm.kernel.graph.model.meta.identity;

import com.lsgllc.norm.kernel.core.util.identity.INormId;
import com.lsgllc.norm.kernel.graph.things.INormObject;
import com.lsgllc.norm.kernel.graph.typing.IHasNormType;

/**
 * Created By: sameloyiv
 * Date: 10/10/14
 * Time: 12:51 PM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2014
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public interface IOntologyId<TT extends INormObject> extends IHasNormType<TT>,INormId<TT> {
}
