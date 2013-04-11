/**
 * Copyright (C) 2011 Stephen Neal
 */
package com.swingplus;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a component in which {@link Releaseable} components can be registered to be released when an application is
 * exiting.
 * 
 * @author Stephen Neal
 * @since 11/04/2013
 */
public class ReleaseManager implements Releaseable {

    // FIXME implement singleton properly
    private static final ReleaseManager INSTANCE = new ReleaseManager();

    /**
     * Get a single instance, normally this should suffice for an application.
     * 
     * @return a new instance
     */
    public static ReleaseManager getSingleInstance() {
        return INSTANCE;
    }

    /**
     * Create a new instance, this does not change single instance returned by {@link #getSingleInstance()}.
     * 
     * @return a new instance
     */
    public static ReleaseManager newInstance() {
        return new ReleaseManager();
    }

    private final Set<Releaseable> entries;

    /**
     * Default constructor is private, use {@link #getSingleInstance()} or {@link #newInstance()}.
     */
    private ReleaseManager() {
        super();
        this.entries = new HashSet<Releaseable>();
    }

    public void add(Releaseable r) {
        if (r == null) {
            return;
        }
        this.entries.add(r);
    }

    /**
     * Releases all entries and removes them.
     */
    @Override
    public void release() {
        Iterator<Releaseable> itr = this.entries.iterator();
        Releaseable r = null;
        while (itr.hasNext()) {
            r = itr.next();
            r.release();
            itr.remove();
        }
    }

}
