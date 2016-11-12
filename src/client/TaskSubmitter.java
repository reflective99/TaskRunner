package client;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import task.TaskRunner;
import util.tasks.FileCheckerTask;
import util.tasks.PortAvailableTask;

/**
 * Main program for creating and submitting tasks to the Task Runner.
 * @author ranj4711
 *
 */
public class TaskSubmitter {

	/**
	 * Creates two tasks and submits them to the TaskRunner.
	 * @param args
	 * @throws Exception
	 */
	
	private String fileName;
	private Integer availPort;
	private int tast1TTL;
	private int task2TTL;
	private int task1Iterations;
	private int tast2Iterations;
	private static ExecutorService EXECUTOR;
	
	public Integer getPort(){
		return this.availPort;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	private void startSubmission() throws InterruptedException, ExecutionException {
		
		int numThreads = 2;
		EXECUTOR = Executors.newFixedThreadPool(numThreads);
		TaskRunner taskRunner = new TaskRunner();

		FutureTask<Object> futureTask1 = new FutureTask<Object>(new Callable<Object>(){
			@Override
			public Object call() throws Exception {
				return taskRunner.runTaskAsync(new FileCheckerTask<Object>(fileName), getTask1Iterations(), getTast1TTL()).get();
			}

		});

		FutureTask<Object> futureTask2 = new FutureTask<Object>(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				return taskRunner.runTaskAsync(new PortAvailableTask<Object>(), getTask1Iterations(), getTask2TTL()).get();
			}
		});

		EXECUTOR.execute(futureTask1);
		EXECUTOR.execute(futureTask2);


		
		//executor.awaitTermination(10, TimeUnit.SECONDS);
		System.out.println("Future returned: " + futureTask1.get());
		System.out.println("Future returned: " + futureTask2.get());
		this.availPort = (Integer) futureTask2.get();
		this.fileName = (String) futureTask1.get();
		System.out.println(this.availPort + " print");
		
	}
	
	public void submitTasks() throws InterruptedException, ExecutionException{
		startSubmission();
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	public void shutDown() {
		EXECUTOR.shutdown();
	}

	public static void main(String[] args) throws Exception {    
		TaskSubmitter ts = new TaskSubmitter();
		ts.setFileName("src");
		ts.submitTasks();
	}

	public int getTast1TTL() {
		return tast1TTL;
	}

	public void setTast1TTL(int tast1ttl) {
		tast1TTL = tast1ttl;
	}

	public int getTask2TTL() {
		return task2TTL;
	}

	public void setTask2TTL(int task2ttl) {
		task2TTL = task2ttl;
	}

	public int getTask1Iterations() {
		return task1Iterations;
	}

	public void setTask1Iterations(int task1Iterations) {
		this.task1Iterations = task1Iterations;
	}

	public int getTast2Iterations() {
		return tast2Iterations;
	}

	public void setTast2Iterations(int tast2Iterations) {
		this.tast2Iterations = tast2Iterations;
	}
}
