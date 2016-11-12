package util.tasks;

import java.io.IOException;
import java.net.ServerSocket;

import task.ITask;


public class PortAvailableTask<T> implements ITask<T>{
	
	@Override
	public boolean isComplete() {
		if(call() != null){
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T call() {
		Integer availablePort = null;
		try {
		    ServerSocket s = create(new int[] { 3843, 4584, 4843 });
		    availablePort = s.getLocalPort();
		    s.close();
		} catch (IOException ex) {
		}
		return (T) availablePort;
	}
	
	public ServerSocket create(int[] ports) throws IOException {
	    for (int port : ports) {
	        try {
	            return new ServerSocket(port);
	        } catch (IOException ex) {
	            continue; 
	        }
	    }
	    throw new IOException("no free port found");
	}

}
