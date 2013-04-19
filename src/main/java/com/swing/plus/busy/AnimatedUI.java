/**
 * 
 */
package com.swing.plus.busy;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;

import com.swing.plus.busy.painter.DimPainter;
import com.swing.plus.busy.painter.Painter;
import com.swing.plus.busy.painter.SpinningWheelPainter;

/**
 * Extension of {@link LockableUI} that allows animated painting over the wrapped component. The default paints a dim
 * layer and spinning wheel.
 * <p>
 * Do not use this class directly, use it through {@link BusyLayerService} which provides convenience and ensures proper
 * synchronisation.
 * </p>
 * <p>
 * Implemented as an extension of the JXLayer {@link LockableUI} so that it should be compatible with WaitLayerUI that
 * will be available in Java 7.
 * </p>
 * <p>
 * NB. This layer counts the number of invocations to lock and it must be unlocked as many times for the locked effect
 * to be removed, i.e. if there are 2 requests to lock it there must be 2 requests to unlock it for the locked effect to
 * disappear. Any time a request is made to lock there should be a matching request to unlock when processing is
 * complete.
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
        if (painters == null || painters.length == 0) {
            throw new IllegalArgumentException("painters null or empty");
        }
        if (this.timer.isRunning()) {
            // If fading in or out, let it finish first
            // NB. it would seem sufficient to have the while statement without the curly braces and stuff in between
            // (logging) but there was an intermittent bug where this would
            // get stuck when it was just a while statement.
            while (this.fadeCount > 0 && this.fadeCount < this.fadeLimit) {
                this.LOGGER.debug("setPainters(Painter...): wait for fade; fadeCount=" + this.fadeCount
                                + "; fadeLimit=" + this.fadeLimit);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
            }
        }
        synchronized (this.lock) {
            this.LOGGER.debug("setPainters(Painter...): change painters");
            // Check timer again, if we waited for fade out it might now have stopped
            boolean running = this.timer.isRunning();
            if (running) {
                this.timer.stop();
            }
            Painter[] oldPainters = AnimatedUI.this.painters;
            AnimatedUI.this.painters = painters;
            if (running) {
                this.timer.start();
            }
            return oldPainters;
        }
    }

    /** This should only ever be invoked by the superclass. */
    @Override
    protected void postLock() {
        synchronized (this.lock) {
            this.LOGGER.debug("postLock()");
            super.postLock();
            if (this.timer.isRunning()) {
                this.LOGGER.debug("postLock(): already started");
                // Already running, if fade out has been initiated stop it and continue running.
                // If not fading ignore the call (continue running).
                if (this.fadeOut) {
                    this.LOGGER.debug("postLock(): cancelling fade");
                    this.fadeOut = false;
                }
            } else {
                this.LOGGER.debug("postLock(): starting animation");
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
        synchronized (this.lock) {
            super.preUnlock();
            this.LOGGER.debug("preUnlock(): start fade out");
            // Initiate fade out.
            this.fadeOut = true;
            // Tell the super class that unlocking will be handled here (once the timer stops and fade out is complete)
            return false;
        }
    }

    /** This should only ever be invoked by the superclass. */
    @Override
    protected void paintLayer(Graphics2D g2, JXLayer<? extends JComponent> c) {
        // Paint the view.
        super.paintLayer(g2, c);

        if (this.painters != null) {
            float fade = (float) this.fadeCount / (float) this.fadeLimit;
            for (Painter p : this.painters) {
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
                synchronized (AnimatedUI.this.lock) {
                    if (AnimatedUI.this.timer.isRunning()) {
                        if (AnimatedUI.this.fadeOut) {
                            if (AnimatedUI.this.fadeCount == 0) {
                                AnimatedUI.this.timer.stop();
                                AnimatedUI.this.LOGGER
                                                .debug("preUnlock(): fade out complete and timer stopped, unlocking...");
                                AnimatedUI.super.setLocked(false);
                                AnimatedUI.this.LOGGER.debug("preUnlock(): unlocked");
                            } else {
                                --AnimatedUI.this.fadeCount;
                            }

                        } else if (AnimatedUI.this.fadeCount < AnimatedUI.this.fadeLimit) {
                            AnimatedUI.this.fadeCount++;
                        }
                        setDirty(true);
                    }
                }
            }
        };
    }

}
