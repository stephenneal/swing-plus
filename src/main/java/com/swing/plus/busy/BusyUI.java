/**
 * 
 */
package com.swing.plus.busy;


import org.jdesktop.jxlayer.plaf.ext.LockableUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UI to lock the underlying component. Extends {@link LockableUI} to counts the number of invocations to lock and it ensure it is unlocked as many times for the locked effect to
 * be removed, i.e. if there are 2 requests to lock it there must be 2 requests to unlock it for the locked effect to disappear. Any time a request is made to lock there should be
 * a matching request to unlock when processing is complete.
 * <p>
 * NB. The API to lock/unlock is {@link #lockRequest(boolean)}, normally for a Unlike {@link LockableUI} it would be {@link #setLocked(boolean)}.
 * </p>
 * <p>
 * Lock the UI by locking the cursor. This can serve as a direct replacement for the Trumps {@link CursorManager}.
 * </p>
 * <p>
 * Do not use this class directly, use it through {@link BusyLayerService} which provides convenience and ensures proper synchronisation.
 * </p>
 * <p>
 * Implemented as an extension of the JXLayer {@link LockableUI} so that it should be compatible with WaitLayerUI that will be available in Java 7.
 * </p>
 */
class BusyUI extends LockableUI {
    private static final long serialVersionUID = 1L;
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Object lockObject = new Object();

    private int startCount = 0;

    /**
     * Override to provide custom behaviour when the layer is locked.
     */
    protected void postLock() {
    }

    /**
     * Override to provide custom behaviour immediately before the layer is unlocked.
     */
    protected boolean preUnlock() {
        return true;
    }

    /**
     * Do not invoke this directly, do it through {@link BusyLayerService} to ensure proper synchronisation.
     * <p>
     * NB. This layer tracks the number of requests to lock and requires the same number of requests to unlock for the locked effect to be removed, i.e. if there are 2 requests to
     * lock it there must be 2 requests to unlock it for the locked effect to disappear. Any time a request is made to lock there should be a matching request to unlock when
     * processing is complete.
     * </p>
     * 
     * @see org.jdesktop.jxlayer.plaf.ext.LockableUI#setLocked(boolean)
     */
    final void lockRequest(boolean lock) {
        // Synchronise (process one request at a time)
        synchronized (lockObject) {
            LOGGER.debug("START lockRequest(boolean): " + lock + "; startCount=" + startCount);
            if (lock) {
                this.startCount++;
                if (this.startCount == 1) {
                    // First request to lock so lock it!
                    super.setLocked(true);
                    postLock();
                }
            } else {
                if (this.startCount > 0) {
                    this.startCount--;
                }
                if (this.startCount == 0 && isLocked()) {
                    boolean unlock = preUnlock();
                    // Do not unlock if told not to by the subclass (assume it will unlock when appropriate)
                    if (unlock) {
                        super.setLocked(false);
                    }
                }
            }
            LOGGER.debug("END lockRequest(boolean): " + lock + "; startCount=" + startCount);
        }
    }

}
