package com.swing.plus.busy;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.LayerUI;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;

import com.swing.plus.Releaseable;
import com.swing.plus.busy.painter.Painter;
import com.swing.plus.concurrent.Executors2;

/**
 * Provides a simple API to interact with a busy layer and synchronisation where required. Provides utilities in the
 * form of static methods and an instance which can be used to {@code run} , {@code call} and {@code submit} tasks and
 * activate a busy layer while the task is processing.
 */
public class BusyLayerService implements Releaseable {

    // Public API (static)
    // ----------------------------------------------------------------------------------------------------------------

    /**
     * Change the {@link Painter}'s for a {@link JXLayer}. It is safe to invoke this even when the layer is locked. This
     * operation is does nothing if UI attached the JXLayer is not an {@link AnimatedUI}.
     * 
     * @param jxLayer JXLayer to change
     * @param painters painters, cannot be {@code null} or empty
     * @return the {@link Painter}'s previously attached to the JXLayer
     */
    public static Painter[] changePainters(final JXLayer<JComponent> jxLayer, final Painter... painters) {
        if (painters == null || painters.length == 0) {
            throw new IllegalArgumentException("painters must not be null or empty");
        }
        if (jxLayer == null || !(jxLayer.getUI() instanceof AnimatedUI)) {
            return null;
        }
        final AnimatedUI ui = (AnimatedUI) jxLayer.getUI();
        final List<Painter> old = new ArrayList<Painter>();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Painter[] pArr = ui.setPainters(painters);
                if (pArr != null) {
                    for (Painter p : pArr) {
                        old.add(p);
                    }
                }
            }
        };
        // Cannot set the painters in the EDT (because it might have to wait for fade)
        if (SwingUtilities.isEventDispatchThread()) {
            Future<?> future = Executors.newSingleThreadExecutor().submit(runnable);
            try {
                future.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                // ExecutionException is thrown when attempting to retrieve the result of a task that aborted by
                // throwing an exception.
                // Propagate the cause.
                Throwable cause = e.getCause();
                // Cause should never be null (ExecutionException should always have a cause) but we play it safe and
                // handle it just in case.
                if (cause != null) {
                    throw new RuntimeException(cause);
                }
                throw new RuntimeException(e);
            }
        } else {
            runnable.run();
        }
        return old.isEmpty() ? null : old.toArray(new Painter[old.size()]);
    }

    /**
     * Change the {@link Cursor} that is used when a {@link JXLayer} is locked. It is safe to invoke this even when the
     * layer is already locked.
     * 
     * @param jxLayer JXLayer to change
     * @param cursor cursor to apply, cannot be {@code null}
     * @return the {@link Cursor} previously attached to the JXLayer when locked
     */
    public static Cursor changeLockedCursor(final JXLayer<JComponent> jxLayer, final Cursor cursor) {
        if (cursor == null) {
            throw new IllegalArgumentException("cursor is null");
        }
        if (jxLayer == null || !(jxLayer.getUI() instanceof LockableUI)) {
            return null;
        }
        final LockableUI ui = (LockableUI) jxLayer.getUI();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ui.setLockedCursor(cursor);
            }
        });
        return ui.getLockedCursor();
    }

    /**
     * Request a busy layer start, if the layer is already started it simply remains busy. For each request to start a
     * busy layer there must be a request to stop it for the layer to stop. If there is more than one request to start
     * but only one request to stop the layer will remain busy.
     * <p>
     * Ignored if the component is not a {@link JXLayer}.
     * </p>
     * <p>
     * After invoking start you should wrap the processing in a {@code try/finally} block and make the stop request
     * inside {@code finally} to ensure it is always invoked. If you do not the layer will not be stopped in the event
     * of a {@link RuntimeException}.
     * </p>
     * 
     * @param busyLayer {@link JXLayer} to start, invocation is ignored if it is not a {@link JXLayer}
     */
    public static void requestStart(JComponent busyLayer) {
        setBusy(busyLayer, true);
    }

    /**
     * Request a busy layer stop. For each request to start a busy layer there must be a request to stop it for the
     * layer to stop. If there were more requests to start the layer than to stop the layer will remain busy.
     * <p>
     * Ignored if the component is not a {@link JXLayer}.
     * </p>
     * <p>
     * Stop the busy layer. To be using in conjunction with {@link #requestStart(JComponent))}.
     * </p>
     * 
     * @param busyLayer busy layer to stop, ignored if it is not a {@link JXLayer}
     */
    public static void requestStop(final JComponent busyLayer) {
        setBusy(busyLayer, false);
    }

    // Instance
    // ----------------------------------------------------------------------------------------------------------------

    private final ExecutorService executorService;
    private final List<JXLayer<?>> layers;
    private boolean releaseExecutor;

    /**
     * Default constructor.
     */
    public BusyLayerService() {
        this(20);
    }

    /**
     * Constructor that allows the number of threads in the pool to be specified.
     */
    public BusyLayerService(int threads) {
        // ExecutorService created internally, shut it down when this instance is released
        this(Executors.newFixedThreadPool(threads), true);
    }

    /**
     * Constructor that allows the invoker to provide the {@link ExecutorService}. The invoker is responsible for
     * shutting down the service, it will not be performed when this instance is released.
     */
    public BusyLayerService(ExecutorService executorService) {
        // ExecutorService provided, do not shut it down when this instance is released
        this(executorService, false);
    }

    /**
     * Constructor that allows the invoker to provide the {@link ExecutorService}. The invoker is responsible for
     * shutting down the service, it will not be performed when this instance is released.
     */
    private BusyLayerService(ExecutorService executorService, boolean releaseExecutorService) {
        super();
        this.executorService = executorService;
        this.layers = new ArrayList<JXLayer<?>>();
        this.releaseExecutor = releaseExecutorService;
    }

    /**
     * Run a task synchronously and return the result, activate the busy layer while executing. Any exception thrown by
     * {@link Callable#call()} is propagated for the invoker to catch and handle appropriately.
     * <p>
     * DO NOT INVOKE THIS FROM THE EDT because it will block. Invoke from a background thread or use {@link
     * BusyLayerUtil#submit(...)}.
     * </p>
     * <p>
     * NB. executing the task synchronously means it waits for the task to complete before returning. Use {@code submit}
     * to execute asynchronously..
     * </p>
     * 
     * @param task task to run
     * @param <R> the result type of {@link Callable#call()}
     * @return the result of {@link Callable#call()}
     * @throws Exception if an exception is thrown by {@link Callable#call()}
     */
    public <R> R call(final JComponent busyLayer, final Callable<R> task) throws Exception {
        return synchronous(busyLayer, task);
    }

    /**
     * Execute one or more tasks asynchronously, activate the busy layer provided while executing.
     * 
     * @param busyLayer busy layer to activate while running the task, ignored if it is not a {@link JXLayer}
     * @param tasks task(s) to run
     */
    public void execute(final JComponent busyLayer, final Runnable... tasks) {
        asynchronous(busyLayer, tasks);
    }

    /**
     * Run one or more tasks synchronously and activate the busy layer while executing.
     * <p>
     * DO NOT INVOKE THIS FROM THE EDT because it will block. Invoke from a background thread or use {@link
     * BusyLayerUtil#submit(...)}.
     * </p>
     * <p>
     * NB. executing the tasks synchronously means it waits for the all the tasks to complete before returning. Use
     * {@code submit} to execute asynchronously.
     * </p>
     * 
     * @param busyLayer busy layer to activate while running the task, ignored if it is not a {@link JXLayer}
     * @param tasks task(s) to run
     */
    public void run(final JComponent busyLayer, final Runnable... tasks) {
        synchronous(busyLayer, tasks);
    }

    /**
     * Wrap the component with a {@link BusyUI}.
     * 
     * @param component component to wrap
     * @param ui {@link BusyUI} layer to use
     * @return the {@link JXLayer} wrapping component
     */
    public JXLayer<JComponent> wrap(JComponent component, BusyUI ui) {
        // Use JXLayer and BusyLayerUI until we upgrade to Java 7, once we are on Java 7 we can replace them with JLayer
        // and WaitLayerUI
        JXLayer<JComponent> l = new JXLayer<JComponent>(component);
        l.setUI(ui);
        this.layers.add(l);
        return l;
    }

    @Override
    public void release() {
        if (this.releaseExecutor) {
            Executors2.shutdownAndAwaitTermination(this.executorService, 5, 5, TimeUnit.SECONDS);
        }
        Iterator<JXLayer<?>> itr = this.layers.iterator();
        // JXLayer<?> l = null;
        while (itr.hasNext()) {
            // TODO release layers? i.e. unregister layer listeners etc?
            // l = itr.next();
            itr.remove();
        }
    }

    // Private
    // ----------------------------------------------------------------------------------------------------------------

    /**
     * @see #synchronous(ExecutorService, JComponent, Callable)
     */
    private <R> R synchronous(JComponent busyLayer, Callable<R> task) throws Exception {
        return synchronous(this.executorService, busyLayer, task);
    }

    /**
     * @see #synchronous(ExecutorService, JComponent, Runnable...)
     */
    private void synchronous(JComponent busyLayer, Runnable... tasks) {
        synchronous(this.executorService, busyLayer, tasks);
    }

    /**
     * @see #asynchronous(ExecutorService, JComponent, Runnable...)
     */
    private void asynchronous(JComponent busyLayer, Runnable... tasks) {
        asynchronous(this.executorService, busyLayer, tasks);
    }

    @SuppressWarnings("unchecked")
    private static void setBusy(final JComponent component, final boolean busy) {
        // Invoked in the EDT (as all Swing UI updates should be). In addition this approach has the added advantage of
        // ensuring each invocation to lock/unlock is
        // run in the same thread (to synchronise invocations).
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!(component instanceof JXLayer)) {
                    // component does not support busy
                    return;
                }
                LayerUI<? super JComponent> ui = ((JXLayer<JComponent>) component).getUI();
                if (ui instanceof BusyUI) {
                    BusyUI bl = (BusyUI) ui;
                    bl.lockRequest(busy);
                } else {
                    // NB. this handles a null UI layer even though it shouldn't be possible.
                    throw new IllegalArgumentException("unsupported UI layer: "
                                    + (ui == null ? null : ui.getClass().getName()));
                }
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }

    /**
     * Execute a {@link Callable} task and return the result or propagate any error that occurs. Activates the busy
     * layer provided while executing.
     * 
     * @param busyLayer busy layer to activate while running the task, ignored if it is not a {@link JXLayer}
     * @param task task to run
     * @param R return type of the {@link Callable} task
     * 
     * @return the result of {@link Callable#call()}
     * @throws Exception any {@link Exception} thrown invoking {@link Callable#call()} or a {@link RuntimeException} if
     *             an unexpected error occurs
     */
    private static <R> R
                    synchronous(ExecutorService executorService, final JComponent busyLayer, final Callable<R> task)
                                    throws Exception {
        if (task == null) {
            return null;
        }
        // Submit the task. Wrap the task to turn the busy layer on/off while running.
        Future<R> f = executorService.submit(new Callable<R>() {
            @Override
            public R call() throws Exception {
                setBusy(busyLayer, true);
                try {
                    return task.call();
                } finally {
                    setBusy(busyLayer, false);
                }
            }
        });
        // Wait for the task and return the result or propagate the exception of one occurred.
        try {
            return f.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            // ExecutionException is thrown when attempting to retrieve the result of a task that aborted by throwing an
            // exception.
            // Propagate the real exception (the cause).
            Throwable cause = e.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            // This shouldn't happen but we play it safe and handle it just in case.
            // Why are we using throwing something that is not an Exception?
            throw new RuntimeException(cause);
        }
    }

    /**
     * Execute a {@link Runnable} task and wait for it to complete, propagate any error that occurs. Activates the busy
     * layer provided while executing.
     * 
     * @param busyLayer busy layer to activate while running the task, ignored if it is not a {@link JXLayer}
     * @param tasks tasks to run
     */
    private static void synchronous(ExecutorService executorService, final JComponent busyLayer,
                    final Runnable... tasks) {
        if (tasks == null || tasks.length == 0) {
            return;
        }
        // Execute the task and wait for completion. Propagate any exceptions to the invoker. NB. the invoker passed a
        // Runnable task and therefore does not expect any checked
        // exceptions to occur during execution (as per Runnable#run()). Any checked exceptions that occur as a result
        // of the internal implementation here are an unexpected
        // situation and are wrapped in a RuntimeException and thrown.
        // Wrap the task to turn the busy layer on/off while running.
        Future<?> f = executorService.submit(new Runnable() {
            @Override
            public void run() {
                setBusy(busyLayer, true);
                try {
                    for (Runnable task : tasks) {
                        task.run();
                    }
                } finally {
                    setBusy(busyLayer, false);
                }
            }
        });
        try {
            f.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            // ExecutionException is thrown when attempting to retrieve the result of a task that aborted by throwing an
            // exception.
            // Propagate the cause. Since the tasks are Runnable they cannot throw a checked exception, the cause must
            // be a RuntimeException.
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            // This shouldn't happen but we play it safe and handle it just in case.
            // Since it isn't a RuntimeException it likely isn't an exception at all, why are we getting something other
            // than an Exception?
            throw new RuntimeException(cause);
        }
    }

    /**
     * Submit a {@link Runnable} task for asynchronous execution and activate the busy layer provided while executing.
     * Internally handles any errors that occur since the invoker is not in a position to do so (because the task is
     * executing asynchronously).
     * 
     * @param executorService2
     * 
     * @param task task to run
     * @param busyLayer busy layer to activate while running the task, ignored if it is not a {@link JXLayer}
     */
    private static void asynchronous(ExecutorService executorService, final JComponent busyLayer,
                    final Runnable... tasks) {
        if (tasks == null || tasks.length == 0) {
            return;
        }
        // Execute (not submit) so any exceptions get caught by the UncaughtExceptionHandler registered at start up.
        // Wrap the task to turn the busy layer on/off while running.
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                setBusy(busyLayer, true);
                try {
                    for (Runnable task : tasks) {
                        task.run();
                    }
                } finally {
                    setBusy(busyLayer, false);
                }
            }
        });
    }
}
