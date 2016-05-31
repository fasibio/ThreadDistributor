package de.fasibio.threaddistributor;

public class EasyPriorityDistributor extends PriorityDistributor{

	public EasyPriorityDistributor(int maxWorkerNumber, int startWorkerNumber) {
		super(maxWorkerNumber, startWorkerNumber);
	}

	@Override
	protected Worker getNewWorker() {
		return new EasyWorker();
	}

}
