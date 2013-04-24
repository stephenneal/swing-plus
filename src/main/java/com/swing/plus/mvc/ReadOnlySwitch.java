/**
 * Copyright (C) 2011 Stephen Neal
 */
package com.swing.plus.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define a property as a read only participant, the annotation must be on the setter. This pertains to
 * the {@link DualModePresentationModel}.
 * 
 * @author steve
 * @since 20/04/2013
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ReadOnlySwitch {

    boolean negate() default false;
}
