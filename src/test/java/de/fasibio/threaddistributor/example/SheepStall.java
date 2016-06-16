package de.fasibio.threaddistributor.example;


import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fasibio.threaddistributor.*;

public class SheepStall implements TaskListener {
	private static final Logger log = LoggerFactory.getLogger(SheepStall.class);
	private PriorityDistributor dist;
	private Vector<Sheep> walkingsheeps = new Vector<Sheep>();
	public SheepStall(PriorityDistributor dist){
		this.dist = dist;
	}
	
	
	public void updateStatus(Status status, Task task) {
		if (task.getObject() instanceof Sheep){
			Sheep tmp = (Sheep)task.getObject();
			switch (status){
			case end: {
//				log.info("SHEEP ENDING WALIKING: "+tmp.toString());
//				log.info("OPEN HIGH WORKER:"+dist.getWaitListCountHighPriority()+" OPEN MIDDLE WORKER: "+dist.getWaitListCountMiddlePriority()+" OPEN LOW WORKER: "+dist.getWaitListCountLowPriority()+ "total worker "+dist.getCountOfOpenWorker());
				walkingsheeps.add((Sheep)task.getObject());
			}
				break;
			case inWaitList:log.info("Sheep "+tmp.name+" is in Waitlist");
				break;
			case start://log.info("Sheep "+ tmp.name+" beginn Walking");
			}	
		}
		
		
		
	}

	public int getCountofWalkingSheeps(){
		return walkingsheeps.size();
	}
}
