import java.io.*;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Storage2 {
	
	private File myFile;
	private String defPath = "myTasks.txt";
	private String currentPath = "";
	private FileWriter fw;
	private FileReader fr; 
	private BufferedWriter bw;
	private BufferedReader br;
	
	private int type = 0;
	private final int DeadlineType = 1;
	private final int DurationType = 2;
	private final int BlockedType = 3;
	private final int FloatingType = 4;
	
	Gson gson = new Gson(); 
	
	//default path
	public Storage2() {
		setCurrentPath(defPath);
		try {
			createFile(currentPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//specified path without fileName
	public Storage2(String path) {
		currentPath = path + File.separator + "myTasks.txt";
		try {
			createFile(currentPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//if file doesn't exist, returns null
	//if file exists, return contents
	private ArrayList<Tasks> createFile(String path) throws IOException {
		
		ArrayList<Tasks> myTaskList = new ArrayList<Tasks>();
		
		//set up file
		myFile = new File(currentPath);
		
		//file not found, create file, intermediate directory? and return null
		if(!myFile.exists()) {
			//myFile.mkdirs();
			myFile.createNewFile();
		}
		//file exists, read file and return contents
		else {
			myTaskList = readFile();
		}
		return myTaskList;

	}

	private void openWriterReader() throws IOException, FileNotFoundException {
		//set up writers
		fw = new FileWriter(myFile.getAbsoluteFile(),true);
		fr = new FileReader(myFile.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		br = new BufferedReader(fr);
	}
	
	//read from file, deserialize and add to myTaskList
	public ArrayList<Tasks> readFile() throws FileNotFoundException, IOException, JsonSyntaxException {
		
		openWriterReader();

		ArrayList<Tasks> myTaskList = new ArrayList<Tasks>();
		String tempLine = "";
		Tasks tempTask;
		
		//read contents of file
		//String segments[] = path.split("\"type\":");
		String splitter = "\"type\":";
		
		

		while ((tempLine = br.readLine()) != null) {
			//extract type
			int startIndex = tempLine.indexOf(splitter)+splitter.length();
			type = Integer.parseInt( tempLine.substring(startIndex, startIndex+1));
			
			switch(type) {
				case DeadlineType: 	tempTask = gson.fromJson(tempLine, DeadlineTask.class);
									break;
				case DurationType: 	tempTask = gson.fromJson(tempLine, DurationTask.class);
									break;
				case FloatingType: 	tempTask = gson.fromJson(tempLine, FloatingTask.class);
									break;
				case BlockedType: 	tempTask = gson.fromJson(tempLine, BlockedTask.class);
									break;
				default:			tempTask = null;
									break;
			}
			//tempTask = gson.fromJson(tempLine, Tasks.class);
			myTaskList.add(tempTask);
		}

		
		//close writer and reader
		closeWriterReader();
		   
		return myTaskList;
	}

	public Tasks appendTask(Tasks newTask) throws FileNotFoundException, IOException {
		
		openWriterReader();

		
		
		//if task has no taskID, generate one!
		if(newTask.getTaskID() == 0) {
			newTask.setTaskID(GenerateTaskID());
		}
		
		closeWriterReader();

		
		String tempLine = gson.toJson(newTask);
		
		openWriterReader();

		
		//write new Tasks into file
		bw.write(tempLine);
		bw.newLine();

		
		//close writer
		closeWriterReader();
		
		return newTask;
	}

	
	
	public Tasks deleteTask(int taskID) throws FileNotFoundException, IOException, InterruptedException{
		
		Tasks deletedTask = null;
		ArrayList<Tasks> myTaskList = new ArrayList<Tasks>();
		
		openWriterReader();

		
		boolean needDelete = false;
		myTaskList = readFile();
		
		//find if taskID to be deleted is found
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
			
			closeWriterReader();
			System.gc();
			
			//wait for garbage collector
			Thread.sleep(500);

			
			//delete old file
			if (!myFile.delete()) {
		        System.out.println("Could not delete file");
		    } 
			
			createFile(currentPath);
			
			openWriterReader();
			
			for(int i=0; i<myTaskList.size(); i++) {
				String tempLine = gson.toJson(myTaskList.get(i)); 
			
				//write new Tasks into file
				bw.write(tempLine);
				bw.newLine();
			}
		}
		
		closeWriterReader();

		return deletedTask;
	}
	
	//search and returns entries with description matching search String
	public ArrayList<Tasks> SearchTask(String searchString) throws FileNotFoundException, IOException{
		
		openWriterReader();

		
		ArrayList<Tasks> tempList = readFile();
		ArrayList<Tasks> myTaskList = new ArrayList<Tasks>();
		
		for(int i=0; i<tempList.size(); i++) {
			if(tempList.get(i).getDescription().contains(searchString)) {
				myTaskList.add(tempList.get(i));
			}
		}
		
		closeWriterReader();
		
		return myTaskList;
	}
	
	//search and returns task by taskID
	public Tasks SearchTaskID(int taskID) throws FileNotFoundException, IOException{
		
		openWriterReader();

		
		ArrayList<Tasks> tempList = readFile();
		Tasks myTask = null;
		
		for(int i=0; i<tempList.size(); i++) {
			if(tempList.get(i).getTaskID() == taskID) {
				myTask = tempList.get(i);
			}
		}
		
		closeWriterReader();
		
		return myTask;
	}
	
	//replaces specified taskID with updated Tasks and returns the "old" task that was updated
	public Tasks UpdateTask(int taskID, Tasks updatedTask) throws FileNotFoundException, IOException, InterruptedException{
		
		Tasks oldTask = null;
		
		openWriterReader();
		
		oldTask = SearchTaskID(taskID);
		
		openWriterReader();
		
		deleteTask(taskID);
		appendTask(updatedTask);

		
		closeWriterReader();
		
		return oldTask;
	}
	
	private int GenerateTaskID() throws FileNotFoundException, IOException {
		
		int largestID = 0;
		ArrayList<Tasks> myTaskList = new ArrayList<Tasks>();
		
		openWriterReader();

		
		myTaskList = readFile();
		for(int i=0; i<myTaskList.size(); i++) {
			if(myTaskList.get(i).getTaskID() > largestID) {
				largestID = myTaskList.get(i).getTaskID();
			}
		}
		
		closeWriterReader();

		
		return largestID +1;
	}
	
	private void closeWriterReader() throws IOException {
		bw.close();
		br.close();
	}
	
	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}


}
