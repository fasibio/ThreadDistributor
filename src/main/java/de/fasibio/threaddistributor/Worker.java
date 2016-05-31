package de.fasibio.threaddistributor;

import javax.management.RuntimeErrorException;

import de.fasibio.threaddistributor.TaskListener.Status;
/**
 * Ein Worker fuehrt die eigentlichen {@link Task} aus. 
 * 
 * @author simofa
 *
 */
public abstract class Worker extends Thread {

	private boolean busy = false, destroy= false;
	private Task task =null;
	private Distributor distributor = null;
	private boolean kill = false;
	public Task getTask(){
		return task;
	}
	
	public void kill(){
		kill = true;
	}
	
	public void setTask(Task task){
		if (this.task == null){
			this.task = task;
		}else{
			throw new RuntimeException("Kann not set Task to worker with have allready a Task");
		}
	}
	/**
	 * 
	 * @return ob der Worker zurzeit einen {@link Task } bearbeitet.
	 */
	public boolean isBusy() {
		return busy;
	}

	private void setBusy(boolean busy) {
		this.busy = busy;
	}
	
	/**
	 * 
	 * @return ob der worker seine arbeit Beendet hat und zerstoert werden kann. 
	 */
	public boolean canDestroy() {
		return destroy;
	}

	private void setDestroy(boolean destroy) {
		this.destroy = destroy;
	}


	private void beforRun(){
		setBusy(true);
		task.updateStatus(Status.start);
	}
	
	private void afterRun(){
		setDestroy(true);
		task.updateStatus(Status.end);
		distributor.replaceWorker(this);
	}
	
	@Override
	public void run() {
		super.run();
		while (task == null){
			try {
				Thread.sleep(500);
				if (kill){
					setTask(new KillTask());
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (!(getTask() instanceof KillTask)){
			beforRun();
			runTask(task.getObject());
			afterRun();	
		}
		
	}
	
	/**
	 * [WIRD NUR VOM {@link Distributor} AUSGEFUEHRT]
	 * @param task der {@link Task} der hier verarbeitet werden soll
	 * @param mydist der {@link Distributor} der diesen Task verarbeiten moechte und dem dieser Worker zugeordnet ist. 
	 */
	public synchronized void start(Task task,Distributor mydist) {
		this.task = task;
		this.distributor = mydist;
		super.start();
	}

	/**
	 * Wir aufgerufen und bestimmt das eigentliche Verarbeiten eines {@link Task}
	 * @param obj
	 */
	protected abstract void runTask(ExecuteAble obj);


}
