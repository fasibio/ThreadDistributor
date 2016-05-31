package de.fasibio.threaddistributor;

import java.util.LinkedList;
import java.util.Vector;

import de.fasibio.threaddistributor.TaskListener.Status;

/**
 * Verteiler HauptKlasse 
 * Verteilt die eigentlichen Task auf eigen Threads 
 * @author simofa
 *
 */
public abstract class Distributor {

	protected Vector<Worker> workerlist = new Vector<Worker>();
	private LinkedList<Task> openTasks = new LinkedList<Task>();
	private int startWorkerNumber = 10, maxWorkerNumber = 100;
	private String workerMainName= "distributerworker-";
	/**
	 * Ini the Distributor 
	 * @param startWorkerNumber the Value of Idle Worker It have to 0 >=startWorkerNumber <= maxWorkerNumber 
	 * @param maxWorkerNumber -1 = unlimited  have to be >= startWorkerNumber
	 */
	public Distributor(int maxWorkerNumber, int startWorkerNumber){
		if (maxWorkerNumber == -1){
			setMaxWorkerNumber(Integer.MAX_VALUE);
		}else{
			setMaxWorkerNumber(maxWorkerNumber);	
		}
		
		setStartWorkerNumber(startWorkerNumber);
		for (int i = 0; i< getStartWorkerNumber(); i++){
			startWorkerWithoutTask(addNewWorker());
		}
	}
	
	protected void startWorkerWithoutTask(Worker worker){
		worker.start(null, this);
	}
	
	/**
	 * 
	 * @return die Anzahl der offenen Task Dabei wird nicht unterschieden, zwischen arbeitenden Tasks und wartenden Task.  
	 */
	public int getCountOfOpenWorker(){
		return workerlist.size();
	}
	
	/**
	 * [NICHT Aufrufen wird von den Worker selber genutzt]
	 * Ein Worker ruft diese Methode auf wenn er fertig mit seiner Arbeit ist. Dadruch wird der Thread vernichtet und offene Aufgaben einem neuen Worker hinzugefuegt. 
	 * Dabei wird beachtet das die minWorker immer offen bleiben. 
	 * @param worker
	 */
	protected synchronized void replaceWorker(Worker worker) {
		workerlist.remove(worker);
		
		if (!openTasks.isEmpty()){
			Worker result = addNewWorker();
			Task next = openTasks.removeFirst();
			handleTask(result, next);	
		}else{
			if (getCountOfOpenWorker() < getStartWorkerNumber()){
				startWorkerWithoutTask(addNewWorker());
			}
		}
	}
	
	/**
	 * Liefert die Anzahl {@link Task} die noch auf einen freien Thread warten zurueck
	 * @return
	 */
	public int getWaitListCount(){
		return openTasks.size();
	}
	
	protected void handleTask(Worker worker, Task task){
		if (worker.isAlive()){
			worker.setTask(task);
		}else{
			worker.start(task,this);
			
		}
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
	}
	
	
	/**
	 * Fuegt den Task dem naechten Freien Workerzu wenn es keinen freien Worker mehr gibt wird false zureuckkgegeben
	 * @param task
	 * @return true wenn er hinzugefuegt wurde, false wenn kein freier Worker gefunden wurde
	 */
	protected boolean addTaskToNextFreeWorker(Task task){
		boolean noFreeWorker = false;
		for (int i = 0 ; i< workerlist.size(); i++){
			Worker one = workerlist.get(i);
			if (one.isBusy()== false && one.getTask() == null){
				try{
					handleTask(one, task);
					noFreeWorker = true;
					break;
				}catch (IllegalThreadStateException e){
					e.printStackTrace();
				}
			}
			
		}
		return noFreeWorker;
	}
	
	/**
	 * fuegt einen weiteren Task dem Verteiler hinzu. 
	 * Dies startet einen neuen Thread in dem der Task verarbeitet wird. 
	 * Ist MaxWorker erreicht, wird der Task in die Warteliste aufgenommen. 
	 * Dort wartet er solange, bis er ein Worker mit seiner aktuellen Aufgabe fertig ist. 
	 * Die Warte liste nutzt dabei das FIFO Modell.
	 * @param task
	 */
	public void addTask(Task task){
		if (!addTaskToNextFreeWorker(task)){
			if (!hasMaxWorkerNumber()){
				handleTask(addNewWorker(), task);
			}else{
				openTasks.add(task);
				task.updateStatus(Status.inWaitList);
			}	
		}
		
	}
	/**
	 * Liefert zurueck ob es  Worker gibt die einen Task verarbeiten (laufen).  
	 * @return
	 */
	public boolean hasBusyWorker(){
		boolean busy = false;
		for (int i = 0 ;i < workerlist.size();i++){
			if (workerlist.get(i).isBusy() ){
				busy = true;
				break;
			}
		}
		return busy;
	}
	/**
	 *If you want Exit the Application you always have to Kill the Thread in this Distributor
	 */
	public void killAllThreads(){
		for (Worker one : workerlist){
			one.kill();
		}
	}
	
	/**
	 * Liefert die Maximale Anzahl Worker zureuck die geöffnet werden können. 
	 * @return
	 */
	public boolean hasMaxWorkerNumber(){
		return (workerlist.size() == getMaxWorkerNumber());
	}
	private int workercount = 1;
	protected Worker addNewWorker(){
		if (hasMaxWorkerNumber()){
			throw new ArrayIndexOutOfBoundsException("Max worker achieved");
		}
		Worker result = getNewWorker();
		result.setName(getWorkerMainName()+workercount);
		
		workercount++;		
		workerlist.add(result);

		return result;
	}
	
	/**
	 * Return a new Inizialised of a real Worker
	 * @return
	 */
	protected abstract Worker getNewWorker();

	/**
	 * Return the Value of Idle Worker 
	 * @return
	 */
	public int getStartWorkerNumber() {
		return startWorkerNumber;
	}

	
	private void setStartWorkerNumber(int startWorkerNumber){
		if (startWorkerNumber < 0 ){
			startWorkerNumber = 0; 
		}
		if (startWorkerNumber > getMaxWorkerNumber()){
			startWorkerNumber= getMaxWorkerNumber();
		}
		this.startWorkerNumber = startWorkerNumber;
	}

	/**
	 * 
	 * @return the max number of Worker. 
	 * If there more task then workers so the task will wait for a free worker
	 */
	public int getMaxWorkerNumber() {
		return maxWorkerNumber;
	}

	private void setMaxWorkerNumber(int maxWorkerNumber) {
		this.maxWorkerNumber = maxWorkerNumber;
	}

	public String getWorkerMainName() {
		return workerMainName;
	}

	public void setWorkerMainName(String workerMainName) {
		this.workerMainName = workerMainName;
	}
	

}
