package Time4WorkStorage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import Time4WorkStorage.Tasks.TaskType;

public class StorageLogic {
			
	private static final int DeadlineType = TaskType.DeadlineType.getTaskType();
	private static final int DurationType = TaskType.DurationType.getTaskType();
	private static final int FloatingType = TaskType.FloatingType.getTaskType();
	
	private static final String MSG_FILECREATION_FAILED = "File already exists or directory not made";
	private static final String MSG_DIRCREATION_FAILED = "Unable to create directories";
	private static final String MSG_WRITEROPEN_FAILED = "Unable to create writer for file";
	private static final String MSG_READEROPEN_FAILED = "Unable to create reader for file";
	private static final String MSG_WRITERCLOSE_FAILED = "Unable to close writer";
	private static final String MSG_READERCLOSE_FAILED = "Unable to close reader";
	private static final String MSG_FILEDELETION_FAILED = "Unable to delete file";
	
	private static final String defPath = "myTasks.txt";
	private static final String TYPE_SPLITTER = "\"type\":";
	private static final String BACKSLASH = "\\";
	private static final String FORWARDSLASH = "/";
	
	private File myFile;
	private FileWriter fw;
	private FileReader fr; 
	private BufferedWriter bw;
	private BufferedReader br;
	
	private String currentPath = "";	
	Gson gson = new Gson(); 
	
	//creates file at default directory, <local directory>/myTasks.txt
	public void createDefaultFile() throws IOException {
		
		currentPath = defPath;
		try {
			createFile(currentPath);
		} catch (IOException e) {
			throw e;
		}
	}	
	
	//custom path and filename, path has to have escape characters
	//eg. C:\\user\\Desktop\\myTasks.txt
	//returns if default file name is appended
	public String createCustomFile(String path, boolean transfer) throws IOException {
		
		ArrayList<Tasks> tempList = new ArrayList<Tasks>();
		
		if(transfer) {
			tempList = getAllTasks();
			try {
				deleteDataFile();
			} catch (IOException e) {
				throw e;
			}
		}
		
		currentPath = path;		
		try {
			createFile(currentPath);
		} catch (IOException e) {
			throw e;
		}
		
		if(myFile.isDirectory()) {
			String lastChar = currentPath.substring(currentPath.length() - 1);
			if(lastChar.equals(BACKSLASH) || lastChar.equals(FORWARDSLASH)) {
				currentPath = currentPath + defPath;
			}
			else {
				currentPath = currentPath + BACKSLASH+ defPath;
			}
			try {
				createFile(currentPath);
			} catch (IOException e) {
				throw e;
			}
		}
		
		if(transfer) {
			for(int i=0; i<tempList.size(); i++) {
				addNewTask(tempList.get(i));
			}
		}
		
		return currentPath;	
	}
	
	//if file doesn't exist, creates file and returns null
	//if file exists, read and return contents
	private ArrayList<Tasks> createFile(String currentPath) throws IOException {
		
		ArrayList<Tasks> myTaskList = new ArrayList<Tasks>();
		
		myFile = new File(currentPath);
		
		try {
			CreateDirectories(myFile);
		}
		catch (IOException e) {
			throw e;
		}
		
		if(myFile.exists() && myFile.isFile()) {
			myTaskList = getAllTasks();
		} else {			
			try {
				myFile.createNewFile();
			} catch (IOException e) {
				throw new IOException(MSG_FILECREATION_FAILED);
			}
		}
		
		return myTaskList;
	}
	
	//checks if file contains path or just fileName.
	//Creates whole path if it's a path. Returns true on success file+directory creation
	private void CreateDirectories(File theFile) throws IOException {
		
		File parentFile = theFile.getParentFile();
		

		if(parentFile != null) {
			if(!parentFile.mkdirs()) {
				if(!parentFile.exists()) {
					throw new IOException(MSG_DIRCREATION_FAILED);
				}
			}
		}
	}
	
	//reads and return all tasks in file
	public ArrayList<Tasks> getAllTasks() throws IOException {
		
		if(!myFile.exists()) {
			try {
				createFile(currentPath);
			} catch (IOException e) {
				throw e;
			}
		}
		
		try {
			openWriterReader();
		} catch (IOException e) {
			throw e;
		}
		
		
		ArrayList<Tasks> myTaskList = new ArrayList<Tasks>();
		String tempLine = "";
		Tasks tempTask;
		
		//read contents of file		
		try {
			while ((tempLine = br.readLine()) != null) {
				int type = getTypeFromGsonString(tempLine);
				
				if (type == DeadlineType) {
					tempTask = gson.fromJson(tempLine, DeadlineTask.class);
				} else if (type == DurationType) {
					tempTask = gson.fromJson(tempLine, DurationTask.class);
				} else if (type == FloatingType) {
					tempTask = gson.fromJson(tempLine, FloatingTask.class);
				} else {
					tempTask = null;
				}
				
				if(tempTask != null ) {
					myTaskList.add(tempTask);
				}
			}
		} catch (JsonSyntaxException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		
		try {
			closeWriterReader();
		} catch (IOException e) {
			throw e;
		}
		   
		return myTaskList;
	}

	//retrieve type number from Gson string
	private int getTypeFromGsonString(String gsonString) {
		
		int type = 0;
		
		int startIndex = gsonString.indexOf(TYPE_SPLITTER)+TYPE_SPLITTER.length();
		type = Integer.parseInt( gsonString.substring(startIndex, startIndex+1));
		
		return type;
	}
	
	
	//adds new task at the end of file, generates taskID if not present
	public Tasks addNewTask(Tasks newTask) throws IOException {
		
		if(!myFile.exists()) {
			try {
				createFile(currentPath);
			} catch (IOException e) {
				throw e;
			}
		}
		
		try {
			openWriterReader();
		} catch (IOException e) {
			throw e;
		}
				
		//if task has no taskID, generate one!
		if(newTask.getTaskID() == 0) {
			try {
				newTask.setTaskID(GenerateTaskID());
			} catch (IOException e) {
				throw e;
			}
		}
		
		try {
			closeWriterReader();
		} catch (IOException e) {
			throw e;
		}
		
		String tempLine = gson.toJson(newTask);
		
		try {
			openWriterReader();
		} catch (IOException e) {
			throw e;
		}
		

		try {
			bw.write(tempLine);
			bw.newLine();
		} catch (IOException e) {
			throw e;
		}
		

		try {
			closeWriterReader();
		} catch (IOException e) {
			throw e;
		}
		
		return newTask;
	}
	
	//generate a valid taskID for requests
	private int GenerateTaskID() throws IOException {
		
		int largestID = 0;
		ArrayList<Tasks> myTaskList = new ArrayList<Tasks>();
		

		if(!myFile.exists()) {
			try {
				createFile(currentPath);
			} catch (IOException e) {
				throw e;
			}
		}
		
		try {
			openWriterReader();
		} catch (IOException e) {
			throw e;
		}
		
		try {
			myTaskList = getAllTasks();
		} catch (IOException e) {
			throw e;
		}
		for(int i=0; i<myTaskList.size(); i++) {
			if(myTaskList.get(i).getTaskID() > largestID) {
				largestID = myTaskList.get(i).getTaskID();
			}
		}
		
		try {
			closeWriterReader();
		} catch (IOException e) {
			throw e;
		}
		
		return largestID +1;
	}
	

	private void openWriterReader() throws IOException, FileNotFoundException{

		try {
			fw = new FileWriter(myFile.getAbsoluteFile(),true);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			throw new IOException(MSG_WRITEROPEN_FAILED);
		}
		try {
			fr = new FileReader(myFile.getAbsoluteFile());
			br = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException(MSG_READEROPEN_FAILED);
		}
	}
	

	private void closeWriterReader() throws IOException {

			try {
				bw.close();
			} catch (IOException e) {
				throw new IOException(MSG_WRITERCLOSE_FAILED);
			}
			try {
				br.close();
			} catch (IOException e) {
				throw new IOException(MSG_READERCLOSE_FAILED);
			}
	}

	//searches and deletes the indicated taskID, if not found, returns null, if deleted returns deleted task 
	public Tasks delete(int taskID) throws IOException, InterruptedException {
		Tasks deletedTask = null;
		ArrayList<Tasks> myTaskList = new ArrayList<Tasks>();
		
		if(!myFile.exists()) {
			try {
				createFile(currentPath);
			} catch (IOException e) {
				throw e;
			}
		}
		
		try {
			openWriterReader();
		} catch (IOException e) {
			throw e;
		}
		
		boolean needDelete = false;
		try {
			myTaskList = getAllTasks();
		} catch (IOException e) {
			throw e;
		}
		

		for(int i=0; i<myTaskList.size(); i++) {
			if(myTaskList.get(i).getTaskID() == taskID) {
				needDelete = true;
				deletedTask = myTaskList.get(i);
				myTaskList.remove(i);
				break;
			}
		}
		
		//taskID to be deleted is found
		if(needDelete) {
			
			try {
				closeWriterReader();
			} catch (IOException e) {
				throw e;
			}
			try {
				deleteDataFile();
			} catch (IOException e) {
				throw e;
			}
			
			try {
				createFile(currentPath);
			} catch (IOException e) {
				throw e;
			}
			
			try {
				openWriterReader();
			} catch (IOException e) {
				throw e;
			}
			
			
			
			for(int i=0; i<myTaskList.size(); i++) {
				String tempLine = gson.toJson(myTaskList.get(i)); 
			
				try {
					bw.write(tempLine);
					bw.newLine();
				}  catch (IOException e) {
					throw e;
				}
			}
		}
		
		try {
			closeWriterReader();
		} catch (IOException e) {
			throw e;
		}
			
		return deletedTask;
		
	}

	//sets task as completed, returns the target task
	public Tasks setCompleted(int taskID, boolean status) throws IOException, InterruptedException {
		Tasks editedTask = null;
		ArrayList<Tasks> myTaskList = new ArrayList<Tasks>();
		
		if(!myFile.exists()) {
			try {
				createFile(currentPath);
			} catch (IOException e) {
				throw e;
			}
		}
				
		try {
			openWriterReader();
		} catch (IOException e) {
			throw e;
		}
		
		boolean needUpdate = false;
		try {
			myTaskList = getAllTasks();
		} catch (IOException e) {
			throw e;
		}
		
		for(int i=0; i<myTaskList.size(); i++) {
			if(myTaskList.get(i).getTaskID() == taskID) {
				needUpdate = true;
				myTaskList.get(i).setCompleted(status);
				editedTask = myTaskList.get(i);				
			}
			
		}
		
		//taskID to be deleted is found
		if(needUpdate) {
			
			try {
				closeWriterReader();
			} catch (IOException e) {
				throw e;
			}
			try {
				deleteDataFile();
			} catch (IOException e) {
				throw e;
			}
			
			try {
				createFile(currentPath);
			} catch (IOException e) {
				throw e;
			}
			
			try {
				openWriterReader();
			} catch (IOException e) {
				throw e;
			}
			
			
			
			for(int i=0; i<myTaskList.size(); i++) {
				String tempLine = gson.toJson(myTaskList.get(i)); 
			
				try {
					bw.write(tempLine);
					bw.newLine();
				}  catch (IOException e) {
					throw e;
				}
			}
		}
		
		try {
			closeWriterReader();
		} catch (IOException e) {
			throw e;
		}
			
		return editedTask;
		
	}

	//deletes the file and recreates it, returning all the deleted tasks
	public ArrayList<Tasks> clear() throws IOException, InterruptedException {
		
		ArrayList<Tasks> myTaskList = new ArrayList<Tasks>();
		

		if(!myFile.exists()) {
			try {
				createFile(currentPath);
			} catch (IOException e) {
				throw e;
			}
		}
		
		try {
			openWriterReader();
		} catch (IOException e) {
			throw e;
		}
		
		try {
			myTaskList = getAllTasks();
		} catch (IOException e) {
			throw e;
		}
		
		try {
			closeWriterReader();
		} catch (IOException e) {
			throw e;
		}
		
		try {
			deleteDataFile();
		} catch (IOException e) {
			throw e;
		}
		
		try {
			createFile(currentPath);
		} catch (IOException e) {
			throw e;
		}
					
		return myTaskList;
		
	}
	
	public void deleteDataFile() throws IOException {
		
		try {
			closeWriterReader();
			fr.close();
			br.close();
		} catch (IOException e) {
			throw e;
		}
		
		System.gc();
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!myFile.delete()) {
			throw new IOException(MSG_FILEDELETION_FAILED);
	    } 
	}
	
	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

}
