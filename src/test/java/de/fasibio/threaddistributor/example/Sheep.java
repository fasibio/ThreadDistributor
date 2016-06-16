package de.fasibio.threaddistributor.example;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fasibio.threaddistributor.*;


public class Sheep implements ExecuteAble{
	Logger log = LoggerFactory.getLogger(Sheep.class);
	public String name;
	private int step;
	
	public Sheep(String name){
		this.name = name;
	}
	
	public void walk(){
		Random r = new Random();
		int steps = r.nextInt(30);
//		log.info("Sheep "+name+" walk aktuelly "+steps+" steps");
		for (int i = 0 ; i <steps; i++){
			step = step +1 ;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		}
//		log.info("Sheep "+name+" has  walked "+steps+" steps");
	}
	
	public String toString(){
		return "Sheep "+name+" walk "+step+" Steps";
	}

	
	public void execute(Worker sender) {
		walk();
		
	}

}
