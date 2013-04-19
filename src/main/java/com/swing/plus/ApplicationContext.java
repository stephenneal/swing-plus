package com.swing.plus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swing.binding.bbb.BindingService;
import com.swing.plus.busy.BusyLayerService;

/**
 * Component encapsulates context for an application instance and provides context operations. Most notably it contains
 * a {@link ReleaseService} where any component in the application can register itself to be released when the
 * application exits.
 * 
 * @author Stephen Neal
 * @since 11/04/2013
 */
public class ApplicationContext implements Releaseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContext.class);

    // Static
    // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /** Lazily creates a single instance in a thread safe manner. */
    private static class InstanceHolder {
        private static final ApplicationContext INSTANCE = new ApplicationContext();
    }

    /**
     * Get a single instance, normally this should suffice for an application.
     * 
     * @return a new instance
     */
    public static ApplicationContext getInstance() {
        return InstanceHolder.INSTANCE;
    }

    // Instance
    // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private ReleaseableBindingService bindingService;
    private BusyLayerService busyLayerService;
    private final ReleaseService releaseService;

    private ApplicationContext() {
        super();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        System.setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());
        this.releaseService = new ReleaseService();
    }

    /**
     * Get the instance attached to this context. Throws {@link UnsupportedOperationException} if the instance is
     * {@code null}.
     * 
     * @return the instance attached to this context
     * @throws UnsupportedOperationException if the instance is {@code null}
     */
    public BindingService getBindingService() {
        if (this.bindingService == null) {
            throw new UnsupportedOperationException("binding not supported here");
        }
        return this.bindingService;
    }

    /**
     * Set the instance for this context (can be null). If it is not {@code null} it is added the list of instances to
     * release when this context is released.
     * 
     * @param bindingService {@link BindingService} instance
     */
    public BindingService setBindingService(ReleaseableBindingService bindingService) {
        ReleaseableBindingService oldValue = this.bindingService;
        if (oldValue != null) {
            LOGGER.warn("replacing the binding service, the existing one will no longer be managed here");
            getReleaseService().remove(oldValue);
        }
        this.bindingService = bindingService;
        getReleaseService().add(bindingService);
        return oldValue;
    }

    /**
     * Get the instance attached to this context. Throws {@link UnsupportedOperationException} if the instance is
     * {@code null}.
     * 
     * @return the instance attached to this context
     * @throws UnsupportedOperationException if the instance is {@code null}
     */
    public BusyLayerService getBusyLayerService() {
        if (this.busyLayerService == null) {
            throw new UnsupportedOperationException("binding not supported here");
        }
        return this.busyLayerService;
    }

    /**
     * Set the instance for this context (can be null). If it is not {@code null} it is added the list of instances to
     * release when this context is released.
     * 
     * @param busyLayerService {@link BusyLayerService} instance
     * @return
     */
    public BusyLayerService setBusyLayerService(BusyLayerService busyLayerService) {
        BusyLayerService oldValue = this.busyLayerService;
        if (oldValue != null) {
            LOGGER.warn("replacing the busy layer service, the existing one will no longer be managed here");
            getReleaseService().remove(oldValue);
        }
        this.busyLayerService = busyLayerService;
        getReleaseService().add(busyLayerService);
        return oldValue;
    }

    /**
     * Get the instance attached to this context, it is never {@code null}.
     * 
     * @return the instance attached to this context
     */
    public ReleaseService getReleaseService() {
        return this.releaseService;
    }

    @Override
    public void release() {
        if (this.releaseService != null) {
            this.releaseService.release();
        }
    }

    public static class ExceptionHandler implements Thread.UncaughtExceptionHandler {

        public void handle(Throwable thrown) {
            // EDT exceptions
            handleException(Thread.currentThread().getName(), thrown);
        }

        @Override
        public void uncaughtException(Thread thread, Throwable thrown) {
            // Uncaught exceptions on non-EDT threads
            handleException(thread.getName(), thrown);
        }

        protected void handleException(String tname, Throwable thrown) {
            LOGGER.error("Uncaught Exception in thread '" + tname + "'", thrown);
        }
    }
}
