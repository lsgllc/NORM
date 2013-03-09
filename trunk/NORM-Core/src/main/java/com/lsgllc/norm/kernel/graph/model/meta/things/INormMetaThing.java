package com.lsgllc.norm.kernel.graph.model.meta.things;

import com.lsgllc.norm.kernel.graph.model.meta.owl.INormPredicate;
import com.lsgllc.norm.kernel.graph.model.meta.owl.INormTriple;

import java.util.Set;

/**
 * Created By: sameloyiv
 * Date: 2/5/13
 * Time: 5:28 PM
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
public interface INormMetaThing {
    Set<INormPredicate> getPredicates();
    Set<INormTriple>  getTriples();
    INormPredicate addPredicate(INormPredicate predicate);
    INormTriple addTriple(INormTriple triple);
}
