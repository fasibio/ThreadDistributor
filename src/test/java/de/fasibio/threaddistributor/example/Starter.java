package de.fasibio.threaddistributor.example;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fasibio.threaddistributor.*;
import de.fasibio.threaddistributor.PriorityDistributor.TaskPriority;

public class Starter {

private static final Logger log = LoggerFactory.getLogger(Starter.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		EasyPriorityDistributor distributor = new EasyPriorityDistributor(20, 0);
		SheepStall stall = new SheepStall(distributor);
		try {
			log.info("Warte 10s");
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		for (int i = 0 ; i <1000 ; i++){
			Task onesheep = new Task();
			onesheep.setObject(new Sheep("s"+i));
			onesheep.addTaskListener(stall);
			if (i % 2 == 0){
				distributor.addTask(onesheep,TaskPriority.low);	
			}else{
				distributor.addTask(onesheep,TaskPriority.high);
			}
			
		}
		while (distributor.hasBusyWorker()){
			try {
				Thread.sleep(2000);
				log.info("Anzahl in Waitlist:"+ distributor.getWaitListCount());
				log.info("Anzahl in Waitlist High:"+ distributor.getWaitListCountHighPriority());
				log.info("Anzahl in Waitlist Low:"+ distributor.getWaitListCountLowPriority());

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		try {
			log.info("Warte 10s");
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		log.info("Count Walking Sheeps "+ stall.getCountofWalkingSheeps());
		distributor.killAllThreads();
	}

}
