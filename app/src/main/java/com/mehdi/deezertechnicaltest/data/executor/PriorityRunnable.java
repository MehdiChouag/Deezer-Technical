package com.mehdi.deezertechnicaltest.data.executor;

/**
 * Runnable task associated to a priority.
 */
public abstract class PriorityRunnable implements Runnable {

  protected final Priority priority;

  public PriorityRunnable(Priority priority) {
    this.priority = priority;
  }

  Priority getPriority() {
    return priority;
  }
}
