import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.core.Is.*;
import org.junit.Test;

import client.TaskSubmitter;

public class Tests {
	
	@Test 
	public void shouldFindAnAvailablePort() throws InterruptedException, ExecutionException {
		
		TaskSubmitter ts = new TaskSubmitter();
		ts.setFileName("src");
		ts.setTask1Iterations(10);
		ts.setTast1TTL(10);
		ts.setTast2Iterations(10);
		ts.setTask2TTL(10);
		
		ts.submitTasks();

		assertThat(ts.getPort(), is((Integer) 3843));

		ts.shutDown();

	}

	@Test
	public void shouldFindExistingFile() throws InterruptedException, ExecutionException {
		
		TaskSubmitter ts = new TaskSubmitter();
		ts.setFileName("App.js");
		ts.setTask1Iterations(10);
		ts.setTast1TTL(1000);
		ts.setTast2Iterations(10);
		ts.setTask2TTL(1000);
		
		ts.submitTasks();
		
		assertThat(ts.getFileName(), is("App.js"));
		
		ts.shutDown();

	
	}
	
	@Test 
	public void shouldReturnFileNotFound() throws InterruptedException, ExecutionException {
		
		TaskSubmitter ts = new TaskSubmitter();
		ts.setFileName("abc");
		ts.setTask1Iterations(10);
		ts.setTast1TTL(1000);
		ts.setTast2Iterations(10);
		ts.setTask2TTL(1000);
		
		ts.submitTasks();
		

		assertThat(ts.getFileName(), is("File Not Found"));
		
		ts.shutDown();
		
	}
	
	@Test 
	public void shouldReturnFileFoundAfterSeveralTries() throws InterruptedException, ExecutionException {
		
		TaskSubmitter ts = new TaskSubmitter();
		ts.setFileName("xyz");
		ts.setTask1Iterations(10);
		ts.setTast1TTL(1000);
		ts.setTast2Iterations(10);
		ts.setTask2TTL(1000);
		
		ts.submitTasks();
		
		File file = new File(System.getProperty("user.dir")+"/xyz");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertThat(ts.getFileName(), is("xyz"));
		
		ts.shutDown();
		
	}
	
	@Test 
	public void shouldReturnPortNotFoundForUnavailablePort() throws InterruptedException, ExecutionException {
		
		TaskSubmitter ts = new TaskSubmitter();
		ts.setFileName("src");
		ts.setTask1Iterations(10);
		ts.setTast1TTL(10);
		ts.setTast2Iterations(10);
		ts.setTask2TTL(10);
		
		ts.submitTasks();

		assertNotEquals(ts.getPort(), is((Integer) 8000));

		ts.shutDown();

	}

}
