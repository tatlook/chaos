/**
 * 
 */
package io.tatlook.chaos;

/**
 * @author Administrator
 *
 */
public @FunctionalInterface
interface SetRunnable {
	public void set(String value) throws NumberFormatException;
}
