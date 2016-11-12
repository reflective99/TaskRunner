package tests;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Test;


import client.TaskSubmitter;
import task.ITask;
import task.TaskRunner;
import util.tasks.FileCheckerTask;
import util.tasks.PortAvailableTask;

public class Tests {
  
  @Test (expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForInvalidFileName() {
    new FileCheckerTask<Object>("");
  }

  @Test 
  public void shouldFinishAndFindTheFileThatExists() throws InterruptedException, ExecutionException {

    TaskSubmitter taskSubmitter = new TaskSubmitter();
    taskSubmitter.setNumberOfTasks(1);	
    FileCheckerTask<Object> fileChecker = new FileCheckerTask<Object>("src");
    taskSubmitter.addFutureTask(fileChecker);
    TaskRunner taskRunner = new TaskRunner();
    taskSubmitter.initializeFutureTasks(taskRunner, 10, 1000);

    taskSubmitter.initializeExecutor();
    taskSubmitter.executeTasksViaFutureCallBacks();

    taskSubmitter.executorShutDown();

    assertThat(taskSubmitter.getFutureForTask(fileChecker).get(), is("src"));

  }

  @Test 
  public void shouldReturnFileNotFoundForAFileThatDoesntExist() throws InterruptedException, ExecutionException {

    TaskSubmitter taskSubmitter = new TaskSubmitter();
    taskSubmitter.setNumberOfTasks(1);  
    FileCheckerTask<Object> fileChecker = new FileCheckerTask<Object>("blah");
    taskSubmitter.addFutureTask(fileChecker);
    TaskRunner taskRunner = new TaskRunner();
    taskSubmitter.initializeFutureTasks(taskRunner, 10, 1000);

    taskSubmitter.initializeExecutor();
    taskSubmitter.executeTasksViaFutureCallBacks();

    taskSubmitter.executorShutDown();

    assertNotEquals(taskSubmitter.getFutureForTask(fileChecker).get(), is("src"));

  }
  
  @Test 
  public void shouldReturnFileFoundAfterSeveralTries() throws InterruptedException, ExecutionException {
    
    /** Check this manually by creating a file */
    TaskSubmitter taskSubmitter = new TaskSubmitter();
    taskSubmitter.setNumberOfTasks(1);  
    FileCheckerTask<Object> fileChecker = new FileCheckerTask<Object>("blah");
    taskSubmitter.addFutureTask(fileChecker);
    TaskRunner taskRunner = new TaskRunner();
    taskSubmitter.initializeFutureTasks(taskRunner, 15, 1000);

    taskSubmitter.initializeExecutor();

    taskSubmitter.executeTasksViaFutureCallBacks();
    
    taskSubmitter.executorShutDown();
   
    assertThat(taskSubmitter.getFutureForTask(fileChecker).get(), is("blah"));

  }
  
  @Test
  public void shouldReturnAnAvailablePort() throws InterruptedException, ExecutionException {
    
    TaskSubmitter taskSubmitter = new TaskSubmitter();
    taskSubmitter.setNumberOfTasks(1);  
    PortAvailableTask<Object> portChecker = new PortAvailableTask<Object>();
    taskSubmitter.addFutureTask(portChecker);
    TaskRunner taskRunner = new TaskRunner();
    taskSubmitter.initializeFutureTasks(taskRunner, 10, 1000);
    
    taskSubmitter.initializeExecutor();

    taskSubmitter.executeTasksViaFutureCallBacks();
    
    taskSubmitter.executorShutDown();
   
    assertThat(taskSubmitter.getFutureForTask(portChecker).get(), is(3843));
  }
  
  @Test
  public void shouldReturnNoPortAvailable() throws InterruptedException, ExecutionException, IOException {
    
    TaskSubmitter taskSubmitter = new TaskSubmitter();
    taskSubmitter.setNumberOfTasks(1);  
    PortAvailableTask<Object> portChecker = new PortAvailableTask<Object>();
    taskSubmitter.addFutureTask(portChecker);
    TaskRunner taskRunner = new TaskRunner();
    taskSubmitter.initializeFutureTasks(taskRunner, 10, 1000);
    
    taskSubmitter.initializeExecutor();
    
    taskSubmitter.executeTasksViaFutureCallBacks();
    
    taskSubmitter.executorShutDown();
   
    assertNotEquals(taskSubmitter.getFutureForTask(portChecker).get(), is("No Available Port Found"));
    
  }
  
  @Test
  public void shouldFindMultipleFiles() throws InterruptedException, ExecutionException, IOException {
    
    TaskSubmitter taskSubmitter = new TaskSubmitter();
    int fileNum = 100;
    taskSubmitter.setNumberOfTasks(fileNum);
    List<ITask<Object>> tasks = new ArrayList<ITask<Object>>();
    for(int i = 0; i < fileNum; i++){
      FileCheckerTask<Object> fileChecker = new FileCheckerTask<Object>(Integer.toString(i));
      taskSubmitter.addFutureTask(fileChecker);
      tasks.add(fileChecker);
    }
    TaskRunner taskRunner = new TaskRunner();
    taskSubmitter.initializeFutureTasks(taskRunner, 10, 1000);
    
    for(int i = 0; i < fileNum; i++){
      File f = new File(Integer.toString(i));
      f.createNewFile();
      f.deleteOnExit();
    }
    
    taskSubmitter.initializeExecutor();

    taskSubmitter.executeTasksViaFutureCallBacks();
    
    taskSubmitter.executorShutDown();
    
    for(int i = 0; i < fileNum; i++){
      assertThat(taskSubmitter.getFutureForTask(tasks.get(i)).get(), is(Integer.toString(i)));
    }
    
  }
  
}
