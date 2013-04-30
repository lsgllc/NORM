package com.lsgllc.norm.client.annotations;

import java.lang.annotation.*;

/**
 * Created By: sameloyiv
 * Date: 4/23/13
 * Time: 6:17 PM
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
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReturnedValueOntology {
    String ontology();
}
