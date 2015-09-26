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
		Tasks tempTask = new Tasks(1, "I'm a floating task!");
		myList = myStorage.appendTask(tempTask);
		
		//duration task
		DeadLines tempDeadLine = new DeadLines("300915", "1800" , "300915", "2000");
		tempTask = new Tasks(2, "I'm a duration task!", tempDeadLine );
		myList = myStorage.appendTask(tempTask);
		
		//deadLine task
		tempDeadLine = new DeadLines("310915", "1200");
		tempTask = new Tasks(3, "I'm a deadLine task!", tempDeadLine );
		myList = myStorage.appendTask(tempTask);
		
		//myList = myStorage.readFile();
		String myItem;
		
		System.out.println("displaying after 3 adds");
		for(int i=0; i<myList.size(); i++) {
			myItem = gson.toJson(myList.get(i)); 
			System.out.println("Item "+ (i+1) + " = " + myItem);
		}
		
		try {
			myList = myStorage.deleteTask(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("after deleting 1 task");
		for(int i=0; i<myList.size(); i++) {
			myItem = gson.toJson(myList.get(i)); 
			System.out.println("Item "+ (i+1) + " = " + myItem);
		}
	}
}
