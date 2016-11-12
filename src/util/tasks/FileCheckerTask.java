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

	public FileCheckerTask(String fileName) {
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
		File dir = new File(path);
		File[] contents = dir.listFiles();
		T fileName = null;
		for(File f : contents){
			if(f.getName().equals(this.myFileName)){
				fileName = (T) this.myFileName;
			} else {
				fileName = (T) "File Not Found";
			}
		}
		return fileName;
	}

}
