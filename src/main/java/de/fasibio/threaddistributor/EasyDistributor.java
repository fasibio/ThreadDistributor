package de.fasibio.threaddistributor;

/**
 * Dies ist ein einfacher Worker. 
 * Dieser kann direkt verwendet werden. 
 * Er arbeitet mit {@link EasyWorker} zusammen. 
 * @author simofa
 *
 */
public class EasyDistributor extends Distributor{
	/**
	 * Ini the Distributor 
	 * @param startWorkerNumber the Value of Idle Worker It have to 0 bigger/equals startWorkerNumber have to smaller/equals maxWorkerNumber 
	 * @param maxWorkerNumber -1 = unlimited  have to be bigger/equals startWorkerNumber
	 */
	public EasyDistributor(int maxWorkerNumber, int startWorkerNumber) {
		super(maxWorkerNumber, startWorkerNumber);
	
	}

	@Override
	protected Worker getNewWorker() {
		return new EasyWorker();
	}


}
