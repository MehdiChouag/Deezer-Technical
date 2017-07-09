package com.mehdi.deezertechnicaltest.data.executor;

import android.os.Process;
import android.support.annotation.NonNull;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * {@link ThreadPoolExecutor} that schedule task using priority.
 */
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

  private final static int CORE_POOL_SIZE = 3;

  public PriorityThreadPoolExecutor() {
    super(CORE_POOL_SIZE, Runtime.getRuntime().availableProcessors(), 60L, TimeUnit.SECONDS,
        new PriorityBlockingQueue<Runnable>(),
        new PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND));
  }

  @NonNull
  @Override
  public Future<?> submit(Runnable task) {
    PriorityFutureTask priorityFutureTask = new PriorityFutureTask((PriorityRunnable) task);
    execute(priorityFutureTask);
    return priorityFutureTask;
  }

  /**
   * Implementation of {@link Future} and {@link Comparable} that are comparable using priority.
   */
  private static final class PriorityFutureTask extends FutureTask<PriorityRunnable>
      implements Comparable<PriorityFutureTask> {
    private final PriorityRunnable priorityRunnable;

    PriorityFutureTask(PriorityRunnable priorityRunnable) {
      super(priorityRunnable, null);
      this.priorityRunnable = priorityRunnable;
    }

    @Override
    public int compareTo(@NonNull PriorityFutureTask other) {
      Priority p1 = priorityRunnable.getPriority();
      Priority p2 = other.priorityRunnable.getPriority();
      return p2.ordinal() - p1.ordinal();
    }
  }
}
