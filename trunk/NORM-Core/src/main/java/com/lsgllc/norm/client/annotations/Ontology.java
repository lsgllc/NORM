package com.lsgllc.norm.client.annotations;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Null Value Validate is, as the name suggests an annotation to
 * validate whether the parameter is null or not
 * @author         Y.Kamesh Rao
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface Ontology {
    String ontology();
}