package de.fasibio.threaddistributor;

/**
 * Impliziert einen einfachen {@link Worker}
 * @author simofa
 *
 */
public class EasyWorker extends Worker {

	@Override
	protected void runTask(ExecuteAble obj) {
		obj.execute(this);
	}

}
