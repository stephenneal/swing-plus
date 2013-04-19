/**
 * Copyright (C) 2011 Stephen Neal
 */
package com.swing.plus;

import com.swing.binding.bbb.BindingService;

/**
 * {@link BindingService} that implements {@link Releaseable} for simple integration with {@link ApplicationContext}.
 * 
 * @author Stephen Neal
 * @since 18/04/2013
 */
public final class ReleaseableBindingService extends BindingService implements Releaseable {

    /**
     * Default constructor.
     */
    public ReleaseableBindingService() {
        super();
    }

}
