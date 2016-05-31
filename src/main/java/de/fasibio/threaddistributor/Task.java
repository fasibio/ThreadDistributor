package de.fasibio.threaddistributor;

import java.util.Vector;
/**
 * Spiegelt einen Aufgabe wieder die dem {@link Distributor} mitgegeben werden kann. 
 * @author simofa
 *
 */
public class Task {
	private ExecuteAble object ; 
	
	private Vector<TaskListener> listener = new Vector<TaskListener>();
	
	/**
	 * Fuegt einen weiteren {@link TaskListener} hinzu der bei Verarbeiten durch einen {@link Worker} status updates mitgeteilt werden
	 * @param listener
	 */
	public void addTaskListener(TaskListener listener){
		this.listener.add(listener);
		
	}
	
	/**
	 * @see Task#addTaskListener(TaskListener)
	 * Loescht einen Tasklistener
	 * @param listener
	 */
	public void removeTaskListener(TaskListener listener){
		this.listener.remove(listener);
	}
	
	/**
	 * Wird aufgerufen um den {@link TaskListener} einen aktuellen Status mitzuteilen
	 * @param status
	 */
	public void updateStatus(TaskListener.Status status){
		for (TaskListener one : listener){
			one.updateStatus(status, this);
		}
	}
	
	/**
	 * 
	 * @return the real Object for this Task
	 */
	public ExecuteAble getObject(){
		return object;
	}
	
	/**
	 * Um das Object zu setzen das im neuen Thread ausgefuehrt werden soll. 
	 * @param object
	 */
	public void setObject(ExecuteAble object) {
		this.object = object;
	}
	
}
