package com.swingplus.bind.bbb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingListener;

import com.swingplus.Releaseable;

/**
 * Manage bindings. Provides a mechanism to release all bindings that are managed by this instance. It is also possible
 * to release bindings for a single source object. Bindings should be released to guard against memory leaks.
 * 
 * @author Stephen Neal
 * @since 11/04/2013
 */
public class BindingService implements Releaseable {

    private Map<Object, List<Binding<?, ?, ?, ?>>> bindingMap;
    private boolean released;
    private final Object lock = new Object();

    /**
     * Default constructor.
     */
    public BindingService() {
        super();
        this.released = false;
        this.bindingMap = new HashMap<Object, List<Binding<?, ?, ?, ?>>>(50);
    }

    /**
     * Invokes {@link Binding#bind()} and adds the binding to the list of bindings to manage.
     * 
     * @param binding binding
     */
    public void bind(final Binding<?, ?, ?, ?> binding) {
        if (binding == null) {
            return;
        }
        // Synchronise to prevent binding during or after release
        synchronized (this.lock) {
            if (this.released) {
                throw new IllegalStateException("adding bindings during or after release is illegal");
            }
            Object key = binding.getSourceObject();
            List<Binding<?, ?, ?, ?>> value = this.bindingMap.get(key);
            if (value == null) {
                value = new ArrayList<Binding<?, ?, ?, ?>>(50);
                value.add(binding);
                this.bindingMap.put(binding.getSourceObject(), value);
            } else {
                value.add(binding);
            }
        }
        binding.bind();
    }

    /**
     * Release all bindings managed by this instance.
     */
    @Override
    public void release() {
        // Synchronise to prevent binding during or after release
        synchronized (this.lock) {
            if (this.released) {
                return;
            }
            for (Entry<Object, List<Binding<?, ?, ?, ?>>> e : this.bindingMap.entrySet()) {
                BindingService.release(e.getValue());
            }
            this.bindingMap.clear();
            this.released = true;
        }
    }

    /**
     * Release all bindings for a bean instance.
     */
    public void release(Object bean) {
        if (bean == null) {
            return;
        }
        // Synchronise to prevent binding during this release
        synchronized (this.lock) {
            if (this.released) {
                return;
            }
            BindingService.release(this.bindingMap.get(bean));
            this.bindingMap.remove(bean);
        }
    }

    /**
     * Release a list of bindings.
     */
    static void release(List<Binding<?, ?, ?, ?>> bindings) {
        if (bindings == null) {
            return;
        }
        Iterator<Binding<?, ?, ?, ?>> itr = bindings.iterator();
        Binding<?, ?, ?, ?> b = null;
        while (itr.hasNext()) {
            b = itr.next();
            BindingService.release(b);
            itr.remove();
        }
    }

    /**
     * Release a binding.
     */
    static void release(Binding<?, ?, ?, ?> b) {
        if (b == null) {
            return;
        }
        if (b.isBound()) {
            b.unbind();
        }
        if (b.getBindingListeners() != null) {
            for (BindingListener l : b.getBindingListeners()) {
                b.removeBindingListener(l);
            }
        }
    }

}
