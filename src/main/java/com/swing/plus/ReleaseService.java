/**
 * Copyright (C) 2011 Stephen Neal
 */
package com.swing.plus;

import java.util.Arrays;
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
public class ReleaseService implements Releaseable {

    private final Set<Releaseable> entries;

    /**
     * Default constructor.
     */
    public ReleaseService(Releaseable... releaseables) {
        super();
        this.entries = new HashSet<Releaseable>(10);
        if (releaseables != null && releaseables.length > 0) {
            this.entries.addAll(Arrays.asList(releaseables));
        }
    }

    public void add(Releaseable r) {
        if (r == null) {
            return;
        }
        this.entries.add(r);
    }

    public boolean remove(Releaseable r) {
        if (r == null) {
            return false;
        }
        return this.entries.remove(r);
    }

    public boolean release(Releaseable r) {
        if (r == null) {
            return true;
        }
        remove(r);
        r.release();
        return true;
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
