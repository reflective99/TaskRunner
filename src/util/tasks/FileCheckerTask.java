package util.tasks;

import java.io.File;

import task.ITask;


/**
 * 
 * Checks if the passed in file name exists
 *
 * @param <T>
 */
public class FileCheckerTask<T> implements ITask<T>{

  private String myFileName;

  public FileCheckerTask(String fileName) throws IllegalArgumentException {
    if(fileName.isEmpty() || fileName.equals("") || fileName == null){
      throw new IllegalArgumentException();
    }
    this.myFileName = fileName;
  }

  @Override
  public boolean isComplete() {
    if(myFileName.equals(call())){
      return true;
    } else {
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public T call() {
    String path = System.getProperty("user.dir");
    //System.out.println(path);
    File dir = new File(path);
    File[] contents = dir.listFiles();
    T fileName = null;
    for(File f : contents){
      f.setExecutable(false);
      f.setWritable(false);
      f.setReadable(false);
      //System.out.println("FileName: " + f.getName() + " Looking for: " + this.myFileName);
      if(f.getName().equals(this.myFileName)){
        fileName = (T) this.myFileName;
        /** Assuming that we don't need to read in the contents of the file 
         *  we can just return the file name. Otherwise we'll have to keep 
         *  the file locked and make sure to unlock it after we are done
         *  reading it */
        f.setExecutable(true);
        f.setWritable(true);
        f.setReadable(true);
        break;
      } else {
        fileName = (T) "File Not Found";
      }
      f.setExecutable(true);
      f.setWritable(true);
      f.setReadable(true);
    }
    return fileName;
  }

}
