package de.fasibio.threaddistributor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fasibio.threaddistributor.PriorityDistributor.TaskPriority;

public class EasyPriorityDistributorTester {

	private final Logger log = LoggerFactory.getLogger(EasyPriorityDistributorTester.class);
	
	private PriorityDistributor distributor ;
	@Before
	public void setUp(){
		distributor = new EasyPriorityDistributor(10, 0);
		assertNotNull(distributor);
	}
	
	@Test
	public void testAddTaskWithoutListener() {
		distributor.addTask(getDefaultTask(TaskPriority.high));
	}
	
	@Test
	public void testStopStartDistributor(){
		distributor.stopDistribution();
		distributor.addTask(getDefaultTask(TaskPriority.high));
		assertEquals(1 , distributor.getWaitListCount() );
		distributor.startDistribution();
		assertEquals(0 , distributor.getWaitListCount() );
	}
	
	@Test
	public void testAddManyTasksInShortTime(){
		for (int i = 0; i < 100;i++){
			if (i % 2 == 0){
				distributor.addTask(getDefaultTask(TaskPriority.low));	
			}else{
				distributor.addTask(getDefaultTask(TaskPriority.high),TaskPriority.high);
			}
			
		}
		while (distributor.getWaitListCount()>0){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Task getDefaultTask(TaskPriority prio){
		Task t = new Task();
		t.setObject(new ExecuteTest(prio));
		return t;
	}

	
	private class ExecuteTest implements ExecuteAble{

		private TaskPriority prio;
		
		public ExecuteTest(TaskPriority prio){
			this.prio = prio;
		}
		
		public void execute(Worker sender) {
			assertNotNull(sender.getTask());
			if (this.prio == TaskPriority.high){
				log.info("execute Task with prio ");
			}else{
				log.info("execute Task");	
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
