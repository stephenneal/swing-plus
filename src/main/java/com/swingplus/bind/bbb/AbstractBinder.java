package com.swingplus.bind.bbb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingListener;

/**
 * Encapsulate common functionality for a component that performs binding, i.e. managing bindings.
 * <p>
 * This class does not provide any operations that for the public binding API, that is up to implementing classes.
 * </p>
 * <h2>Managing bindings</h2>
 * <p>
 * This class ensures bindings are correctly managed. The bindings must be managed so that they can be disposed of when
 * the view is disposed. Not disposing the bindings can result in a memory leak.
 * </p>
 * <p>
 * Under the covers is BetterBeansBinding, {@link http ://kenai.com/projects/betterbeansbinding/pages/Home).
 * BetterBeansBinding is an implementation born from JSR-295 - Beans Binding, {@link http
 * ://jcp.org/en/jsr/detail?id=295}.
 * </p>
 * 
 * @author Stephen Neal
 * 
 * @param B bean (aka model) type
 */
abstract class AbstractBinder<B> {

    private B bean;

    private List<Binding<?, ?, ?, ?>> bindings = Collections.synchronizedList(new ArrayList<Binding<?, ?, ?, ?>>(50));
    private boolean released = false;
    private final Object lock = new Object();

    /**
     * Constructor requiring a bean (model).
     * 
     * @param bean bean, cannot be {@code null}
     */
    AbstractBinder(B bean) {
        super();
        if (bean == null) {
            throw new IllegalArgumentException("bean is null");
        }
        this.bean = bean;
    }

    /**
     * Get the underlying bean.
     * 
     * @return the underlying bean
     */
    public B getBean() {
        return this.bean;
    }

    /**
     * Release listeners, bindings etc for this instance. Invoke this when the instance is no longer required.
     */
    public void release() {
        // Synchronise to prevent binding during or after release
        synchronized (this.lock) {
            if (this.released) {
                return;
            }
            for (Binding<?, ?, ?, ?> b : this.bindings) {
                b.unbind();
                if (b.getBindingListeners() != null) {
                    for (BindingListener l : b.getBindingListeners()) {
                        b.removeBindingListener(l);
                    }
                }
            }
            this.bindings.clear();
            this.released = true;
            this.bean = null;
        }
        this.bindings = null;
    }

    // Private
    // -----------------------------------------------------------------------------------------------------------------

    protected final void bindAndManage(final Binding<?, ?, ?, ?> binding) {
        // Synchronise to prevent binding during or after release
        synchronized (this.lock) {
            if (this.released) {
                throw new IllegalStateException("binding during or after release is illegal");
            }
            this.bindings.add(binding);
        }
        binding.bind();
    }
}
