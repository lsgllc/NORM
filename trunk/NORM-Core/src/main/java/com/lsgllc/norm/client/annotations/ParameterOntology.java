package com.lsgllc.norm.client.annotations;

import java.lang.annotation.*;

/**
 * Created By: sameloyiv
 * Date: 4/23/13
 * Time: 6:19 PM
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
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterOntology {
    String ontology();
}
