package Test.Time4WorkLogic;

import org.junit.Test;

import Time4WorkLogic.FeedbackMessage;
import Time4WorkLogic.Logic;
import Time4WorkStorage.FloatingTask;
import Time4WorkStorage.Tasks;

import static org.junit.Assert.*;
public class UpdateCommandTest {
	private static final String MESSAGE_UPDATED_ = "Task updated %1$s !";
	private static final String MESSAGE_UNDO_ = "Undo %1$s!";
	Logic logic = new Logic();
	
	 /*This is the test for update task*/
	@Test
	public void deleteOneTest() throws Exception{
		
		logic.executeClear();
		Tasks task = new FloatingTask("reading");
		logic.executeAdd(task);
		
		task.setDescription("sleeping");
		FeedbackMessage feedbackMessage = logic.executeUpdate(task);
		assertEquals(String.format(MESSAGE_UPDATED_, "successfully"), feedbackMessage.getFeedback());
		
		assertEquals("sleeping", logic.getMyTaskList().get(0).getDescription());
		
		//test undo
		feedbackMessage = logic.executeUndo();
		assertEquals(String.format(MESSAGE_UNDO_, "successfully!"), feedbackMessage.getFeedback());
		assertEquals("reading", logic.getMyTaskList().get(0).getDescription());
	}
	
}