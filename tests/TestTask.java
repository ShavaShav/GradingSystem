package tests;

import java.util.Date;

import assets.Task;
import assets.TaskType;

import junit.framework.TestCase;

public class TestTask extends TestCase {
	Task task, laterTask;
	protected void setUp() throws Exception {
		super.setUp();
		task = new Task(TaskType.ASSIGNMENT, 1);
		laterTask = new Task(TaskType.ASSIGNMENT, 2);
	}

	public void testTask() {
		assertEquals(task.getType(), TaskType.ASSIGNMENT);
		assertEquals(task.getNumber(), 1);
		assertEquals(task.getGrade(), 0.0);
		assertEquals(task.getWeight(), 0.0);
		assertNull(task.getDueDate());
	}

	public void testIsComplete() {
		assertFalse(task.isComplete());
		task.setGrade(80.00);
		assertTrue(task.isComplete());
	}

	public void testBefore() {
		task.setDueDate(new Date(0));
		laterTask.setDueDate(new Date(10000));
		assertTrue(task.before(laterTask));
	}

}
