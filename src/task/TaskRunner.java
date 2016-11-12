package task;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Runs a submitted task <code>times</code> number of times
 * and supports a sleep interval of <code>sleepMillis</code> between
 * each run and returns a Future result.
 * 
 * This method returns immediately with a Future object 
 * which can be used to obtain the result of the task 
 * when the task completes. 
 * 
 * @param task
 * @param times
 * @param sleepMillis
 */

public class TaskRunner {


	@SuppressWarnings("unchecked")
	public <V> Future<V> runTaskAsync(final ITask<V> task, int times, long sleepMillis) {

		int numThreads = 2;
		
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);

		Future future = executor.submit(new Callable<Object>() {
			int counter = 0;
			@Override
			public Object call() throws Exception {
				try {
					while(!task.isComplete() && counter < times){
						counter++;
						//System.out.println("Thread-"+ Thread.currentThread().getName() +" going to sleep...");
						TimeUnit.MILLISECONDS.sleep(sleepMillis);
					} 
					return task.call();
				} catch (InterruptedException e) {
					throw new IllegalStateException("task interrupted", e);
				}
			}

		});
		return future;
	}
}
