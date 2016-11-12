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
      /** Only checking with 3 different ports */
      ServerSocket s = create(new int[] { 3843, 4584, 4843 });
      availablePort = s == null ? null : s.getLocalPort();
      s.close();
    } catch (IOException ex) {
    }
    return availablePort == null ? (T) "No Available Port Found" : (T) availablePort;
  }

  public ServerSocket create(int[] ports) {
    for (int port : ports) {
      try {
        return new ServerSocket(port);
      } catch (IOException ex) {

      }
    }
    return null;
  }

}
