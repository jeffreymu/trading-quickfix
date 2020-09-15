package com.algoTrader.service.fix.utils;

import java.io.InterruptedIOException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.FileLockInterruptionException;

import javax.naming.InterruptedNamingException;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * General-purpose utilities.
 * 
 */

public final class ExceptUtils {
	private static Logger logger = LoggerFactory.getLogger(ExceptUtils.class);

	// CLASS METHODS.

	/**
	 * Checks whether the calling thread has been interrupted, and, if so,
	 * throws an {@link InterruptedException} with the default interruption
	 * message and no underlying cause. The interrupted status of the thread is
	 * cleared.
	 * 
	 * @throws InterruptedException
	 *             Thrown if the calling thread was interrupted.
	 */

	public static void checkInterruption() throws InterruptedException {
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException("Thread execution was interrupted");
		}
	}

	/**
	 * Checks whether the calling thread has been interrupted, and, if so,
	 * throws an {@link InterruptedException} with the given interruption
	 * message, but without an underlying cause. The interrupted status of the
	 * thread is cleared.
	 * 
	 * @param message
	 *            The message.
	 * 
	 * @throws InterruptedException
	 *             Thrown if the calling thread was interrupted.
	 */

	public static void checkInterruption(String message) throws InterruptedException {
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException(message);
		}
	}

	/**
	 * Checks whether the calling thread has been interrupted, and, if so,
	 * throws an {@link InterruptedException} with the default interruption
	 * message and the given underlying cause. The given underlying cause is set
	 * on the thrown exception. The interrupted status of the thread is cleared.
	 * 
	 * @param cause
	 *            The cause.
	 * 
	 * @throws InterruptedException
	 *             Thrown if the calling thread was interrupted.
	 */

	public static void checkInterruption(Throwable cause) throws InterruptedException {
		if (Thread.currentThread().isInterrupted()) {
			InterruptedException ex = new InterruptedException("Thread execution was interrupted");
			ex.initCause(cause);
			throw ex;
		}
	}

	/**
	 * Checks whether the calling thread has been interrupted, and, if so,
	 * throws an {@link InterruptedException} with the given interruption
	 * message and the given underlying cause. The given underlying cause is set
	 * on the thrown exception. The interrupted status of the thread is cleared.
	 * 
	 * @param cause
	 *            The cause.
	 * @param message
	 *            The message.
	 * 
	 * @throws InterruptedException
	 *             Thrown if the calling thread was interrupted.
	 */

	public static void checkInterruption(Throwable cause, String message) throws InterruptedException {
		if (Thread.currentThread().isInterrupted()) {
			InterruptedException ex = new InterruptedException(message);
			ex.initCause(cause);
			throw ex;
		}
	}

	public static boolean isInterruptException(Throwable throwable) {
		return ((throwable instanceof InterruptedException) || (throwable instanceof InterruptedIOException)
				|| (throwable instanceof ClosedByInterruptException)
				|| (throwable instanceof FileLockInterruptionException)
				|| (throwable instanceof InterruptedNamingException) || (throwable instanceof InterruptedException));
	}

	/**
	 * If the given throwable is an interruption exception per
	 * {@link #isInterruptException(Throwable)}, then the calling thread is
	 * interrupted. Otherwise, this is a no-op.
	 * 
	 * @param throwable
	 *            The throwable.
	 * 
	 * @return True if the calling thread was interrupted.
	 */

	public static boolean interrupt(Throwable throwable) {
		if (isInterruptException(throwable)) {
			Thread.currentThread().interrupt();
			return true;
		}
		return false;
	}

	/**
	 * Swallows the given throwable. It logs the given parameterized message and
	 * throwable under the given logging category at the warning level. Also, if
	 * the given throwable is an interruption exception per
	 * {@link #isInterruptException(Throwable)}, then the calling thread is
	 * interrupted.
	 * 
	 * @param throwable
	 *            The throwable.
	 * @param category
	 *            The category.
	 * @param message
	 *            The message.
	 * 
	 * @return True if the calling thread was interrupted.
	 */

	public static boolean swallow(Throwable throwable, Object category, String message) {
		logger.warn(message + " " + category);
		return interrupt(throwable);
	}

	/**
	 * Swallows the given throwable. It logs a default message alongside the
	 * throwable at the warning level. Also, if the given throwable is an
	 * interruption exception per {@link #isInterruptException(Throwable)}, then
	 * the calling thread is interrupted.
	 * 
	 * @param throwable
	 *            The throwable.
	 * 
	 * @return True if the calling thread was interrupted.
	 */

	public static boolean swallow(Throwable throwable) {
		return swallow(throwable, ExceptUtils.class, "Caught throwable was not propagated");
	}

	public static Exception wrap(Throwable throwable, String message) {
		if (isInterruptException(throwable)) {
			Thread.currentThread().interrupt();
			return new InterruptedException(message);
		}
		return new Exception(message, throwable);
	}

	public static Exception wrap(Throwable throwable) {
		if (isInterruptException(throwable)) {
			Thread.currentThread().interrupt();
			return new InterruptedException(throwable.getMessage());
		}
		return new Exception(throwable);
	}

	public static RuntimeException wrapRuntime(Throwable throwable, String message) {
		if (isInterruptException(throwable)) {
			Thread.currentThread().interrupt();
			return new RuntimeException(message);
		}
		return new RuntimeException(message, throwable);
	}

	public static RuntimeException wrapRuntime(Throwable throwable) {
		if (isInterruptException(throwable)) {
			Thread.currentThread().interrupt();
			return new RuntimeException(throwable);
		}
		return new RuntimeException(throwable);
	}

	/**
	 * Returns the hash code of the given throwable. The result matches the
	 * equality definition of {@link #areEqual(Throwable,Object)}.
	 * 
	 * @param t
	 *            The throwable.
	 * 
	 * @return The hash code.
	 */

	public static int getHashCode(Throwable t) {
		if (t == null) {
			return 0;
		}
		int code = ObjectUtils.hashCode(t.getClass());
		code += ObjectUtils.hashCode(t.getMessage());
		code += getHashCode(t.getCause());
		return code;
	}

	/**
	 * Checks whether the given throwable is equal to the given object. Equality
	 * requires identical classes, equal messages, and equal causes.
	 * 
	 * @param t
	 *            The throwable. It may be null.
	 * @param o
	 *            The object. It may be null.
	 * 
	 * @return True if so.
	 */

	public static boolean areEqual(Throwable t, Object o) {
		if (t == o) {
			return true;
		}
		if ((o == null) || (t == null) || !t.getClass().equals(o.getClass())) {
			return false;
		}
		Throwable to = (Throwable) o;
		if (o instanceof Throwable) {
			if (!ObjectUtils.equals(((Throwable) t).getMessage(), ((Throwable) o).getMessage())) {
				return false;
			}
		} else {
			if (!ObjectUtils.equals(t.getMessage(), to.getMessage())) {
				return false;
			}
		}
		return areEqual(t.getCause(), to.getCause());
	}

	// CONSTRUCTORS.

	/**
	 * Constructor. It is private so that no instances can be created.
	 */

	private ExceptUtils() {
	}
}
