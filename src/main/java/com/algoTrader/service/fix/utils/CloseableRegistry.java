package com.algoTrader.service.fix.utils;

import java.io.Closeable;
import java.util.LinkedList;

/**
 * A registry of {@link Closeable} instances.
 * 
 */

public class CloseableRegistry implements Closeable {

	// INSTANCE DATA.

	private LinkedList<Closeable> mRegistry = new LinkedList<Closeable>();

	// INSTANCE METHODS.

	/**
	 * Registers a {@link Closeable} instance with the receiver. An instance
	 * (dependent) that depends upon another instance (parent) must be
	 * registered <i>after</i> the parent.
	 * 
	 * @param closeable
	 *            The closeable.
	 */

	public void register(Closeable closeable) {
		mRegistry.addFirst(closeable);
	}

	/**
	 * Closes all {@link Closeable} instances registered with the receiver.
	 * <i>All</i> instances are always closed, even if an error occurs while
	 * closing an instance; such errors are merely logged, but do not prevent
	 * further closures. The order of closures is the reverse of the
	 * registration order.
	 */

	@Override
	public void close() {
		for (Closeable closeable : mRegistry) {
			try {
				closeable.close();
			} catch (Throwable t) {
				ExceptUtils.swallow(t, this, "Closing failed");
			}
		}
	}
}
