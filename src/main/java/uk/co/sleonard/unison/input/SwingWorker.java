package uk.co.sleonard.unison.input;

import java.util.Observable;

import javax.swing.SwingUtilities;

/**
 * from http://java.sun.com/products/jfc/tsc/articles/threads/threads2.html
 *
 * This is the 3rd version of SwingWorker (also known as SwingWorker 3), an abstract class that you
 * subclass to perform GUI-related work in a dedicated thread. For instructions on using this class,
 * see:
 *
 * http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
 *
 * Note that the API changed slightly in the 3rd version: You must now invoke start() on the
 * SwingWorker after creating it.
 * 
 * @author
 * @since
 * 
 */
public abstract class SwingWorker extends Observable implements Runnable {
	/** The thread var. */
	protected final ThreadVar	threadVar;

	/** The value. */
	private Object				value;				 // see getValue(), setValue()

	/**
	 * Class to maintain reference to current worker thread under separate synchronization control.
	 */
	private static class ThreadVar {

		/** The thread. */
		private Thread thread;

		/**
		 * Instantiates a new thread var.
		 *
		 * @param t
		 *            the t
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

	/**
	 * Start a thread that will call the <code>construct</code> method and then exit.
	 *
	 * @param name
	 *            the name
	 */
	public SwingWorker(final String name) {
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
	}

	/**
	 * Return the value created by the <code>construct</code> method. Returns null if either the
	 * constructing thread or the current thread was interrupted before a value was produced.
	 *
	 * @return the value created by the <code>construct</code> method
	 */
	public Object get() {
		while (true) {
			final Thread t = this.threadVar.get();
			if (t == null) {
				return this.getValue();
			}
			try {
				t.join();
			}
			catch (final InterruptedException e) {
				Thread.currentThread().interrupt(); // propagate
				return null;
			}
		}
	}

	/**
	 * Get the value produced by the worker thread, or null if it hasn't been constructed yet.
	 *
	 * @return the value
	 */
	protected synchronized Object getValue() {
		return this.value;
	}

	/**
	 * A new method that interrupts the worker thread. Call this method to force the worker to stop
	 * what it's doing.
	 */
	public void interrupt() {
		final Thread t = this.threadVar.get();
		if (t != null) {
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
		try {
			this.setValue(this.construct());
		}
		finally {
			this.threadVar.clear();
		}
		final Runnable doFinished = () -> SwingWorker.this.finished();

		SwingUtilities.invokeLater(doFinished);
	}

	/**
	 * Set the value produced by worker thread.
	 *
	 * @param x
	 *            the new value
	 */
	private synchronized void setValue(final Object x) {
		this.value = x;
	}

	/**
	 * Start the worker thread.
	 */
	public void start() {
		final Thread t = this.threadVar.get();
		if (t != null) {
			t.start();
		}
	}
}
