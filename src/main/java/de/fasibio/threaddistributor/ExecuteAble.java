package de.fasibio.threaddistributor;

/**
 * Diese Interface muss implementiert werden. Um es dann einem {@link Task} f�r den {@link Distributor} erstellen zu k�nnen in der Methode {@link Task#setObject(ExecuteAble)}
 * @author simofa
 *
 */
public interface ExecuteAble {
	
	/**
	 * WAs im neuen Thread ausgefuehrt werden soll. 
	 * @param sender
	 */
	public void execute(Worker sender);
}
