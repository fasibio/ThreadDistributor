package de.fasibio.threaddistributor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.fasibio.threaddistributor.TaskListener.Status;

public abstract class PriorityDistributor extends Distributor{


	private HashMap<TaskPriority,LinkedList<Task>> openTasks = new HashMap<TaskPriority, LinkedList<Task>>();
	
	public PriorityDistributor(int maxWorkerNumber, int startWorkerNumber) {
		super(maxWorkerNumber, startWorkerNumber);
		openTasks.put(TaskPriority.low, new LinkedList<Task>());
		openTasks.put(TaskPriority.middle, new LinkedList<Task>());
		openTasks.put(TaskPriority.high, new LinkedList<Task>());
		
	}

	
	public enum TaskPriority{
		low, middle, high
	}


	@Override
	protected void replaceWorker(Worker worker) {
		workerlist.remove(worker);
		Task next = getNextTaskByPriority();
		if (next != null){
			Worker result = addNewWorker();
			handleTask(result, next);
		}else{
			if (getCountOfOpenWorker() < getStartWorkerNumber()){
				startWorkerWithoutTask(addNewWorker());
			}
		}
	}


	private Task getNextTaskByPriority(){
		if (openTasks.get(TaskPriority.high).isEmpty() ){
			if (openTasks.get(TaskPriority.middle).isEmpty() ){
				if (openTasks.get(TaskPriority.low).isEmpty() ){
					return null;
				}else{
					return openTasks.get(TaskPriority.low).removeFirst();
				}	
			}else{
				return openTasks.get(TaskPriority.middle).removeFirst();
			}
		}else{
			return openTasks.get(TaskPriority.high).removeFirst();
		}
	}
	
	@Override
	public int getWaitListCount() {
		return getWaitListCountHighPriority()+getWaitListCountLowPriority()+getWaitListCountMiddlePriority();
	}

	/**
	 * @return Liefert die Anzahl der Task in der Waitliste zurueck die eine Hohe Prioritaet haben
	 */
	public int getWaitListCountHighPriority(){
		return openTasks.get(TaskPriority.high).size();
	}
	
	/**
	 * @return Liefert die Anzahl der Task in der Waitliste zurueck die eine Mittlere Prioritaet haben
	 */
	public int getWaitListCountMiddlePriority(){
		return openTasks.get(TaskPriority.middle).size();
	}
	
	/**
	 * @return Liefert die Anzahl der Task in der Waitliste zurueck die eine geringe Prioritaet haben
	 */
	public int getWaitListCountLowPriority(){
		return openTasks.get(TaskPriority.low).size();
	}
	/**
	 * Added a Task the prioritaet is low
	 */
	@Override
	public void addTask(Task task) {
		addTask(task,TaskPriority.low);
	}
	
	/**
	 * Add a Task with Priority
	 * @param task der hinzuzufuegende Task
	 * @param prio die zuverwendende prio
	 * @see Distributor#addTask(Task)
	 */
	public void addTask(Task task,TaskPriority prio) {
		if (!isDistributorstartet()){
			addTaskToWaitList(task,prio);
			return;
		}
		if (!addTaskToNextFreeWorker(task)){
			if (!hasMaxWorkerNumber()){
				handleTask(addNewWorker(), task);
			}else{
				addTaskToWaitList(task,prio);
			}	
		}
	}
	
	protected void addTaskToWaitList(Task task,TaskPriority prio) {
		switch (prio){
		case high:openTasks.get(TaskPriority.high).add(task);
		break;
		case middle:openTasks.get(TaskPriority.middle).add(task);
		break;
		case low: openTasks.get(TaskPriority.low).add(task);
		}
		task.updateStatus(Status.inWaitList);
	}




	@Override
	public void startDistribution() {
		rundistributor = true;
		List<Task> high = openTasks.get(TaskPriority.high).subList(0, openTasks.get(TaskPriority.high).size());
		List<Task> middle = openTasks.get(TaskPriority.middle).subList(0, openTasks.get(TaskPriority.middle).size());
		List<Task> low = openTasks.get(TaskPriority.low).subList(0, openTasks.get(TaskPriority.low).size());
		for (Task one : high){
			addTask(one, TaskPriority.high);
		}
		for (Task one : middle){
			addTask(one, TaskPriority.middle);
		}
		for (Task one : low){
			addTask(one, TaskPriority.low);
		}
	}
	
	
	
}
