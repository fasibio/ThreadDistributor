package de.fasibio.threaddistributor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyDistributorTester {

	private final Logger log = LoggerFactory.getLogger(EasyDistributorTester.class);
	
	private Distributor distributor ;
	@Before
	public void setUp(){
		distributor = new EasyDistributor(10, 0);
		assertNotNull(distributor);
	}
	
	@Test
	public void testAddTaskWithoutListener() {
		distributor.addTask(getDefaultTask());
	}
	
	@Test
	public void testStopStartDistributor(){
		distributor.stopDistribution();
		distributor.addTask(getDefaultTask());
		assertEquals(1 , distributor.getWaitListCount() );
		distributor.startDistribution();
		assertEquals(0 , distributor.getWaitListCount() );
	}
	
	@Test
	public void testAddManyTasksInShortTime(){
		for (int i = 0; i < 100;i++){
			distributor.addTask(getDefaultTask());
		}
	}
	
	private Task getDefaultTask(){
		Task t = new Task();
		t.setObject(new ExecuteAble() {
			
			public void execute(Worker sender) {
				assertNotNull(sender.getTask());
				log.info("execute Task");
			}
		});
		return t;
	}

}
