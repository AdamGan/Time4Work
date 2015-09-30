import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;

public class Tester {

	public static void main(String[] args) {
		
		//default path
		Storage myStorage = new Storage();
		Gson gson = new Gson(); 
		
		ArrayList<Tasks> myList = null;
		
		//floating task
		Tasks tempTask = new FloatingTask("I'm a floating task!");
		myStorage.appendTask(tempTask);
		myList = myStorage.readFile();
		
		//duration task
		Duration tempDeadLine = new Duration("300915", "1800" , "300915", "2000");
		tempTask = new DurationTask("I'm a duration task!", tempDeadLine );
		myStorage.appendTask(tempTask);
		myList = myStorage.readFile();
				
		
		//deadLine task
		tempDeadLine = new Duration("310915", "1200");
		tempTask = new DeadlineTask("I'm a deadLine task!", tempDeadLine );
		myStorage.appendTask(tempTask);
		myList = myStorage.readFile();
		
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
		} catch (IOException e) {
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
		myStorage.UpdateTask(2, tempTask);
		myList = myStorage.readFile();
		
		System.out.println("after update ID 2");
		for(int i=0; i<myList.size(); i++) {
			myItem = gson.toJson(myList.get(i)); 
			System.out.println("Item "+ (i+1) + " = " + myItem);
		}
	}
}
