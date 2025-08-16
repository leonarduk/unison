/**
 * SwingWorker
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.input;

import lombok.extern.slf4j.Slf4j;
import javax.swing.*;
import java.util.Observable;

/**
 * from http://java.sun.com/products/jfc/tsc/articles/threads/threads2.html
 * <p>
 * This is the 3rd version of SwingWorker (also known as SwingWorker 3), an abstract class that you
 * subclass to perform GUI-related work in a dedicated thread. For instructions on using this class,
 * see:
 * <p>
 * http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
 * <p>
 * Note that the API changed slightly in the 3rd version: You must now invoke start() on the
 * SwingWorker after creating it.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
@Slf4j
abstract class SwingWorker extends Observable implements Runnable {
    /**
     * The thread var.
     */
    private final ThreadVar threadVar;

    /**
     * Start a thread that will call the <code>construct</code> method and then exit.
     *
     * @param name the name
     */
    SwingWorker(final String name) {
        log.debug("Initialising SwingWorker with thread name {}", name);
        final Thread t = new Thread(this, name);
        this.threadVar = new ThreadVar(t);
    }

    /**
     * Compute the value to be returned by the <code>get</code> method.
     *
     * @return the object
     */
    public abstract Object construct();

    /**
     * Called on the event dispatching thread (not on the worker thread) after the
     * <code>construct</code> method has returned.
     */
    public void finished() {
        log.debug("SwingWorker finished");
    }

    /**
     * A new method that interrupts the worker thread. Call this method to force the worker to stop
     * what it's doing.
     */
    void interrupt() {
        final Thread t = this.threadVar.get();
        if (t != null) {
            log.debug("Interrupting thread {}", t.getName());
            t.interrupt();
        }
        this.threadVar.clear();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        log.debug("Thread {} starting", Thread.currentThread().getName());
        try {
            this.setValue(this.construct());
        } finally {
            this.threadVar.clear();
        }
        final Runnable doFinished = () -> SwingWorker.this.finished();

        SwingUtilities.invokeLater(doFinished);
    }

    /**
     * Set the value produced by worker thread.
     *
     * @param x the new value
     */
    private synchronized void setValue(final Object x) {
        //
    }

    /**
     * Start the worker thread.
     */
    public void start() {
        final Thread t = this.threadVar.get();
        if (t != null) {
            log.debug("Starting thread {}", t.getName());
            t.start();
        }
    }

    /**
     * Class to maintain reference to current worker thread under separate synchronization control.
     */
    static class ThreadVar {

        /**
         * The thread.
         */
        private Thread thread;

        /**
         * Instantiates a new thread var.
         *
         * @param t the t
         */
        ThreadVar(final Thread t) {
            this.thread = t;
        }

        /**
         * Clear.
         */
        synchronized void clear() {
            this.thread = null;
        }

        /**
         * Gets the.
         *
         * @return the thread
         */
        synchronized Thread get() {
            return this.thread;
        }
    }
}
