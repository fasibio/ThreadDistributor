package de.fasibio.threaddistributor;

/**
 * Dieses Interface dient als Schnittstelle um den aktuellen Status eines {@link Task} zu erhalten. 
 * Dabei kann das implemntierte Object in der Methode {@link Task#addTaskListener(TaskListener)} dem {@link Task} hinzugefügt werden.  
 * @author simofa
 *
 */
public interface TaskListener {

	/**
	 * Den Status des aktuellen {@link Task}  
	 * @see TaskListener
	 * @see Task
	 * 
	 * @author simofa
	 *
	 */
	public enum Status{
		inWaitList, start, end
	}
	
	/**
	 * Wird von den {@link Worker} aufgerufen wenn sich der Status eines Task sich veraendert
	 * @param status den nun gueltigen {@link Status}
	 * @param task der {@link Task} um den es sich hansdelt
	 */
	public  void updateStatus(Status status,Task task);
	
	
	
}
