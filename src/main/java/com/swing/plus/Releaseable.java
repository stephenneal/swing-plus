/**
 * Copyright (C) 2011 Stephen Neal
 */
package com.swing.plus;

/**
 * An interface that defines a component that can be released. This is part of a mechanism to release or clean up before
 * disposing of a component and/or exiting an application.
 * 
 * @author Stephen Neal
 * @since 11/04/2013
 */
public interface Releaseable {

    /**
     * Release resources that could otherwise lead to a memory leak.
     */
    void release();
}
