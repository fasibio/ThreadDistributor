package de.fasibio.threaddistributor;

/**
 * Dies ist ein einfacher Worker. 
 * Dieser kann direkt verwendet werden. 
 * Er arbeitet mit {@link EasyWorker} zusammen. 
 * @author simofa
 *
 */
public class EasyDistributor extends Distributor{

	public EasyDistributor(int maxWorkerNumber, int startWorkerNumber) {
		super(maxWorkerNumber, startWorkerNumber);
	
	}

	@Override
	protected Worker getNewWorker() {
		return new EasyWorker();
	}


}
