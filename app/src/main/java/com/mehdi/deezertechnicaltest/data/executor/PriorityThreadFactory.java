package com.mehdi.deezertechnicaltest.data.executor;

import android.os.Process;
import android.support.annotation.NonNull;
import java.util.concurrent.ThreadFactory;

/**
 * Implementation of {@link ThreadFactory} that apply priority to threads
 */
class PriorityThreadFactory implements ThreadFactory {

  private final int threadPriority;

  private int threadCount;

  PriorityThreadFactory(int threadPriority) {
    this.threadPriority = threadPriority;
  }

  @Override
  public Thread newThread(@NonNull final Runnable r) {
    Runnable wrapper = new Runnable() {
      @Override
      public void run() {
        try {
          Process.setThreadPriority(threadPriority);
        } catch (Throwable ignored) {
        }
        r.run();
      }
    };
    return new Thread(wrapper, "thread_" + ++threadCount);
  }
}
