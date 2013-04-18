/**
 * 
 */
package com.swingplus.busy;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;

import org.apache.commons.lang.Validate;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;

import com.swingplus.busy.painter.DimPainter;
import com.swingplus.busy.painter.Painter;
import com.swingplus.busy.painter.SpinningWheelPainter;

/**
 * Extension of {@link LockableUI} that allows animated painting over the wrapped component. The default paints a dim layer and spinning wheel.
 * <p>
 * Do not use this class directly, use it through {@link BusyLayerService} which provides convenience and ensures proper synchronisation.
 * </p>
 * <p>
 * Implemented as an extension of the JXLayer {@link LockableUI} so that it should be compatible with WaitLayerUI that will be available in Java 7.
 * </p>
 * <p>
 * NB. This layer counts the number of invocations to lock and it must be unlocked as many times for the locked effect to be removed, i.e. if there are 2 requests to lock it there
 * must be 2 requests to unlock it for the locked effect to disappear. Any time a request is made to lock there should be a matching request to unlock when processing is complete.
 * </p>
 */
public class AnimatedUI extends BusyUI {
    private static final long serialVersionUID = 1L;

    private int fadeCount;
    private boolean fadeOut;
    private int fadeLimit = 5;
    private Timer timer;
    private Painter[] painters;
    private final Object lock = new Object();

    /**
     * Default constructor, uses the {@link DimPainter} and {@link SpinningWheelPainter}.
     */
    public AnimatedUI() {
        this(new DimPainter(), new SpinningWheelPainter());
    }

    /**
     * Constructor to specify custom painters
     */
    public AnimatedUI(Painter... painters) {
        super();
        this.painters = painters;
        int interval = 40; // time (in ms between repaint requests)
        this.timer = new Timer(interval, newActionListener());
    }

    Painter[] setPainters(final Painter... painters) {
        Validate.notEmpty(painters, "painters null or empty");
        if (timer.isRunning()) {
            // If fading in or out, let it finish first
            // NB. it would seem sufficient to have the while statement without the curly braces and stuff in between (logging) but there was an intermittent bug where this would
            // get stuck when it was just a while statement.
            while (fadeCount > 0 && fadeCount < fadeLimit) {
                LOGGER.debug("setPainters(Painter...): wait for fade; fadeCount=" + fadeCount + "; fadeLimit=" + fadeLimit);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
            }
        }
        synchronized (lock) {
            LOGGER.debug("setPainters(Painter...): change painters");
            // Check timer again, if we waited for fade out it might now have stopped
            boolean running = timer.isRunning();
            if (running) {
                timer.stop();
            }
            Painter[] oldPainters = AnimatedUI.this.painters;
            AnimatedUI.this.painters = painters;
            if (running) {
                timer.start();
            }
            return oldPainters;
        }
    }

    /** This should only ever be invoked by the superclass. */
    @Override
    protected void postLock() {
        synchronized (lock) {
            LOGGER.debug("postLock()");
            super.postLock();
            if (timer.isRunning()) {
                LOGGER.debug("postLock(): already started");
                // Already running, if fade out has been initiated stop it and continue running.
                // If not fading ignore the call (continue running).
                if (fadeOut) {
                    LOGGER.debug("postLock(): cancelling fade");
                    fadeOut = false;
                }
            } else {
                LOGGER.debug("postLock(): starting animation");
                // Initiate painting.
                this.fadeOut = false;
                this.fadeCount = 0;
                this.timer.start();
            }
        }
    }

    /** This should only ever be invoked by the superclass. */
    @Override
    protected boolean preUnlock() {
        synchronized (lock) {
            super.preUnlock();
            LOGGER.debug("preUnlock(): start fade out");
            // Initiate fade out.
            fadeOut = true;
            // Tell the super class that unlocking will be handled here (once the timer stops and fade out is complete)
            return false;
        }
    }

    /** This should only ever be invoked by the superclass. */
    @Override
    protected void paintLayer(Graphics2D g2, JXLayer<? extends JComponent> c) {
        // Paint the view.
        super.paintLayer(g2, c);

        if (painters != null) {
            float fade = (float) fadeCount / (float) fadeLimit;
            for (Painter p : painters) {
                p.paintLayer(g2, c, fade);
            }
        }

        g2.dispose();
    }

    /** {@link ActionListener} invoked internally by the {@link Timer} */
    private ActionListener newActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (lock) {
                    if (timer.isRunning()) {
                        if (fadeOut) {
                            if (fadeCount == 0) {
                                timer.stop();
                                LOGGER.debug("preUnlock(): fade out complete and timer stopped, unlocking...");
                                AnimatedUI.super.setLocked(false);
                                LOGGER.debug("preUnlock(): unlocked");
                            } else {
                                --fadeCount;
                            }

                        } else if (fadeCount < fadeLimit) {
                            fadeCount++;
                        }
                        setDirty(true);
                    }
                }
            }
        };
    }

}
