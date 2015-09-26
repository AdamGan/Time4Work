import java.io.*;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Storage {
	
	private File myFile;
	private String defPath = "myTasks.txt";
	private String currentPath = "";
	private ArrayList<Tasks> myTaskList = new ArrayList<Tasks>();
	private FileWriter fw;
	private FileReader fr; 
	private BufferedWriter bw;
	private BufferedReader br;
	
	Gson gson = new Gson(); 
	
	//default path
	public Storage() {
		setCurrentPath(defPath);
		try {
			createFile(currentPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//specified path without fileName
	public Storage(String path) {
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
		
		//set up file
		myFile = new File(currentPath);
		
		//file not found, create file, intermediate directory? and return null
		if(!myFile.exists()) {
			//myFile.mkdirs();
			myFile.createNewFile();
			setMyTaskList(new ArrayList<Tasks>());
		}
		//file exists, read file and return contents
		else {
			setMyTaskList(readFile());
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
	public ArrayList<Tasks> readFile() {
		
		try {
			openWriterReader();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		myTaskList = new ArrayList<Tasks>();
		String tempLine = "";
		Tasks tempTask;
		
		//read contents of file
		try {
			while ((tempLine = br.readLine()) != null) {
				tempTask = gson.fromJson(tempLine, Tasks.class);
				myTaskList.add(tempTask);
			}
		}
		catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		//close reader
		try {
			br.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		   
		return myTaskList;
	}

	public ArrayList<Tasks> appendTask(Tasks newTask) {
		
		try {
			openWriterReader();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		myTaskList = readFile();
		myTaskList.add(newTask);
		
		String tempLine = gson.toJson(newTask); 
		
		//write new Tasks into file
		try {
			bw.write(tempLine);
			bw.newLine();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		//close writer
		try {
			bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return myTaskList;
	}
	
	public ArrayList<Tasks> deleteTask(int taskID) throws IOException{
		
		try {
			openWriterReader();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		boolean needDelete = false;
		myTaskList = readFile();
		
		//find if taskID to be deleted is found
		for(int i=0; i<myTaskList.size(); i++) {
			if(myTaskList.get(i).getTaskID() == taskID) {
				needDelete = true;
				myTaskList.remove(i);
				break;
			}
		}
		
		//taskID to be deleted is found
		if(needDelete) {
			//create temporary file without deleted entry
			File tempFile = new File(myFile.getAbsolutePath() + ".tmp");
			FileWriter TempFw = new FileWriter(tempFile.getAbsoluteFile(), true);
			BufferedWriter TempBw = new BufferedWriter(TempFw);
			
			for(int i=0; i<myTaskList.size(); i++) {
				String tempLine = gson.toJson(myTaskList.get(i)); 
			
				//write new Tasks into file
				try {
					TempBw.write(tempLine);
					TempBw.newLine();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			//close all writers and readers
			try {
				TempBw.close();
				bw.close();
				br.close();
				System.gc();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			//wait for garbage collector
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//delete original file
			if (!myFile.delete()) {
		        System.out.println("Could not delete file");
		      } 
		      
		    //Rename the new file to the filename the original file had.
		    if (!tempFile.renameTo(myFile)) {
		    	System.out.println("Could not rename file");
		    }
		}	
		return myTaskList;
	}
	
	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	public ArrayList<Tasks> getMyTaskList() {
		return myTaskList;
	}

	public void setMyTaskList(ArrayList<Tasks> myTaskList) {
		this.myTaskList = myTaskList;
	}
}