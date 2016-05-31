package de.fasibio.threaddistributor;

import java.util.HashMap;
import java.util.LinkedList;
import de.fasibio.threaddistributor.TaskListener.Status;

public abstract class PriorityDistributor extends Distributor{


	private HashMap<TaskPriority,LinkedList<Task>> openTasks = new HashMap<TaskPriority, LinkedList<Task>>();
	
	public PriorityDistributor(int maxWorkerNumber, int startWorkerNumber) {
		super(maxWorkerNumber, startWorkerNumber);
		openTasks.put(TaskPriority.low, new LinkedList<Task>());
		openTasks.put(TaskPriority.middel, new LinkedList<Task>());
		openTasks.put(TaskPriority.high, new LinkedList<Task>());
		
	}

	
	public enum TaskPriority{
		low, middel, high
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
			if (openTasks.get(TaskPriority.middel).isEmpty() ){
				if (openTasks.get(TaskPriority.low).isEmpty() ){
					return null;
				}else{
					return openTasks.get(TaskPriority.low).removeFirst();
				}	
			}else{
				return openTasks.get(TaskPriority.middel).removeFirst();
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
		return openTasks.get(TaskPriority.middel).size();
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
	 * @param task
	 * @param prio
	 * @see Distributor#addTask(Task)
	 */
	public void addTask(Task task,TaskPriority prio) {
		if (!addTaskToNextFreeWorker(task)){
			if (!hasMaxWorkerNumber()){
				handleTask(addNewWorker(), task);
			}else{
				switch (prio){
				case high:openTasks.get(TaskPriority.high).add(task);
				break;
				case middel:openTasks.get(TaskPriority.middel).add(task);
				break;
				case low: openTasks.get(TaskPriority.low).add(task);
				}
				task.updateStatus(Status.inWaitList);
			}	
		}
	}
	
	
	
}
