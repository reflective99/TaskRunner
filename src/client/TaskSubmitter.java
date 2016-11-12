package client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import task.ITask;
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

  /** Number of Tasks */
  private static int numTasks;

  /** A Map of Tasks To be Run and their FutureTask Objects */
  private static Map<ITask<Object>, FutureTask<Object>> myTasks =
      new HashMap<ITask<Object>, FutureTask<Object>>();

  /** Task Executor */
  private static ExecutorService EXECUTOR;

  public void initializeFutureTasks(TaskRunner taskRunner, int taskIterations, int taskDelay) {

    for(ITask<Object> task : myTasks.keySet()){

      FutureTask<Object> future = new FutureTask<Object>(new Callable<Object>() {
        @Override
        public Object call() throws Exception {
          return taskRunner.runTaskAsync(task, taskIterations, taskDelay).get();
        }
      });

      myTasks.put(task, future);

    }
  }

  public void initializeExecutor() {
    EXECUTOR = Executors.newFixedThreadPool(numTasks);
  }

  public void executeTasksViaFutureCallBacks() {

    for(ITask<Object> task : myTasks.keySet()){
      EXECUTOR.execute(myTasks.get(task));
      try {
        System.out.println(myTasks.get(task).get());
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }

  }

  public void executorShutDown(){
    EXECUTOR.shutdown();
    while(!EXECUTOR.isTerminated()){

    }
    System.out.println("All Tasks are finished!");
  }

  public void setNumberOfTasks(int num){
    numTasks = num;
  }

  public void addFutureTask(ITask<Object> task){
    myTasks.put(task, null);
  }

  public FutureTask<Object> getFutureForTask(ITask<Object> task) {
    return myTasks.get(task);
  }

  public static void main(String[] args) throws Exception {    

    TaskSubmitter taskSubmitter = new TaskSubmitter();

    numTasks = 2;

    String fileName = "xyz";
    /** Number of times we want to check for task completion */
    int taskIterations = 10;
    /** Delay time in MilliSeconds */
    int taskDelay = 1000;

    FileCheckerTask<Object> fileChecker = new FileCheckerTask<Object>(fileName);
    PortAvailableTask<Object> portChecker = new PortAvailableTask<Object>();

    taskSubmitter.addFutureTask(fileChecker);
    taskSubmitter.addFutureTask(portChecker);

    TaskRunner taskRunner = new TaskRunner();

    taskSubmitter.initializeFutureTasks(taskRunner, taskIterations, taskDelay);

    taskSubmitter.initializeExecutor();

    taskSubmitter.executeTasksViaFutureCallBacks();

    taskSubmitter.executorShutDown();

  }

}
