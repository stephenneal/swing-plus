package com.swing.plus.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods in addition to {@link Executors}.
 */
public class Executors2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(Executors2.class);

    private Executors2() {
    }

    /**
     * Performs a shutdown. NB. this differs from {@link ExecutorService#shutdown()} in that it attempts to shutdown cleanly and if that fails it attempts to force shutdown.
     * <p>
     * The implementation is that provided by the Java documentation of {@link ExecutorService}:
     * 
     * <pre>
     * void shutdownAndAwaitTermination(ExecutorService pool) {
     *     pool.shutdown(); // Disable new tasks from being submitted
     *     try {
     *         // Wait a while for existing tasks to terminate
     *         if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
     *             pool.shutdownNow(); // Cancel currently executing tasks
     *             // Wait a while for tasks to respond to being cancelled
     *             if (!pool.awaitTermination(60, TimeUnit.SECONDS))
     *                 System.err.println(&quot;Pool did not terminate&quot;);
     *         }
     *     } catch (InterruptedException ie) {
     *         // (Re-)Cancel if current thread also interrupted
     *         pool.shutdownNow();
     *         // Preserve interrupt status
     *         Thread.currentThread().interrupt();
     *     }
     * }
     * </pre>
     * 
     * </p>
     */
    public static void shutdownAndAwaitTermination(ExecutorService service, long firstTimeout, long secondTimeout, TimeUnit timeUnit) {
        boolean shutdown = false;
        // Try to shutdown gracefully
        service.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            LOGGER.debug("shutdown requested, wait for service to terminate");
            shutdown = service.awaitTermination(firstTimeout, timeUnit);
            if (shutdown) {
                LOGGER.debug("service terminated");
            } else {
                LOGGER.error("service did not shut down cleanly, force it");
                // Did not stop in time. Attempt shutdown cancelling any executing tasks.
                service.shutdownNow();
                // Wait a while for tasks to respond to being cancelled
                LOGGER.debug("forced shutdown requested, wait for service to terminate");
                shutdown = service.awaitTermination(secondTimeout, timeUnit);
                if (shutdown) {
                    LOGGER.info("service terminated (forced)");
                } else {
                    LOGGER.error("service did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            LOGGER.error("service shutdown interrupted, forcing shutdown");
            // (Re-) Cancel if current thread interrupted
            service.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

}
