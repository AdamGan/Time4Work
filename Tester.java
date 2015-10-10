import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;

public class Tester {

	public static void main(String[] args) {
		
		/*
		//default path
		Storage myStorage = null;
		try {
			myStorage = new Storage();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		*/
		
		//custom path
		Storage myStorage = null;
		try {
			myStorage = new Storage("C:\\Users\\Adam\\Downloads\\test\\maybe\\myTasks.txt");
		} catch (IOException e1) {
			System.out.println("I caught exception!");
			e1.printStackTrace();
		}
		
		System.out.println("created without exception");
		
		Gson gson = new Gson(); 
		
		ArrayList<Tasks> myList = null;
		
		//floating task
		Tasks tempTask = new FloatingTask("I'm a floating task!");
		try {
			myStorage.appendTask(tempTask);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			myList = myStorage.readFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//duration task
		Duration tempDeadLine = new Duration("300915", "1800" , "300915", "2000");
		tempTask = new DurationTask("I'm a duration task!", tempDeadLine );
		try {
			myStorage.appendTask(tempTask);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			myList = myStorage.readFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		
		//deadLine task
		tempDeadLine = new Duration("310915", "1200");
		tempTask = new DeadlineTask("I'm a deadLine task!", tempDeadLine );
		try {
			myStorage.appendTask(tempTask);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			myList = myStorage.readFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//myList = myStorage.readFile();
		String myItem;
		
		System.out.println("displaying after 3 adds");
		for(int i=0; i<myList.size(); i++) {
			myItem = gson.toJson(myList.get(i)); 
			System.out.println("Item "+ (i+1) + " = " + myItem);
		}
		
		try {
			myStorage.deleteTask(1);
			myList = myStorage.readFile();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("after deleting 1 task");
		for(int i=0; i<myList.size(); i++) {
			myItem = gson.toJson(myList.get(i)); 
			System.out.println("Item "+ (i+1) + " = " + myItem);
		}
		
		tempDeadLine = new Duration("301015", "1800" , "301015", "2000");
		tempTask = new DurationTask("I'm a duration taskv2!", tempDeadLine );
		tempTask.setTaskID(2);
		try {
			myStorage.UpdateTask(2, tempTask);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			myList = myStorage.readFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("after update ID 2");
		for(int i=0; i<myList.size(); i++) {
			myItem = gson.toJson(myList.get(i)); 
			System.out.println("Item "+ (i+1) + " = " + myItem);
		}
	}
}
