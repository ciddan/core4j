package org.core4j;

/**
 * Encapsulates a method that has a single parameter and does not return a value.
 */
public interface Action<T> {
  public abstract void run(T target);
}
