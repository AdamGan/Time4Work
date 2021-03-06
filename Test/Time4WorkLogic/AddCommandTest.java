package Test.Time4WorkLogic;


import org.junit.Test;

import Time4WorkLogic.FeedbackMessage;
import Time4WorkLogic.Logic;
import Time4WorkStorage.FloatingTask;
import Time4WorkStorage.Tasks;

import static org.junit.Assert.*;

public class AddCommandTest {
	 private static final String MESSAGE_ADDED_ = "Task added %1$s !";
	 private static final String MESSAGE_UNDO_ = "Undo %1$s!";
	 Logic logic = new Logic();
	
	 /*This is the test for normal adding*/
	@Test
	public void addTest() throws Exception{
		logic.executeClear();
		
		Tasks newTask = new FloatingTask("reading.");
		newTask.setTaskID(0);
		
		FeedbackMessage feedbackMessage = logic.executeAdd(newTask);
		
		assertEquals(String.format(MESSAGE_ADDED_, "successfully"), feedbackMessage.getFeedback());
		assertEquals(1, logic.getMyTaskList().size());
		assertEquals(newTask.getDescription(), feedbackMessage.getTaskList().get(0).getDescription());
		assertEquals(newTask.getTaskID(), feedbackMessage.getTaskList().get(0).getTaskID());
		

		//test undo for adding
		feedbackMessage = logic.executeUndo();
		assertEquals(String.format(MESSAGE_UNDO_, "successfully!"), feedbackMessage.getFeedback());
		assertEquals(0, logic.getMyTaskList().size());
	}
	

	
}