package com.ekinoksyazilim.etkk.prototype.comm;

import java.util.concurrent.ConcurrentSkipListSet;

import com.ekinoksyazilim.etkk.prototype.tools.SilentSleeper;

public class Worker {

	private boolean isRunning;

	private Thread readThread;
	private Thread writeThread;
	private Thread dispathThread;

	private ConcurrentSkipListSet<EndPoint<?>> endPoints = new ConcurrentSkipListSet<>();

	public Worker() {

		readThread = new Thread(this::read);
		writeThread = new Thread(this::write);
		dispathThread = new Thread(this::dispatch);
	}

	public void start() {

		isRunning = true;

		readThread.start();
		writeThread.start();
		dispathThread.start();
	}

	public void stop() {

		isRunning = false;
	}

	public void assign(EndPoint<?> endPoint) {

		endPoints.add(endPoint);
	}

	public void unassign(EndPoint<?> endPoint) {

		endPoints.remove(endPoint);
	}

	private void read() {

		while (isRunning) {

			boolean somethingHappened = false;

			for (EndPoint<?> endPoint : endPoints) {

				somethingHappened &= endPoint.read();
			}

			SilentSleeper.sleep(somethingHappened, 10);
		}
	}

	private void write() {

		while (isRunning) {

			boolean somethingHappened = false;

			for (EndPoint<?> endPoint : endPoints) {

				somethingHappened &= endPoint.write();
			}

			SilentSleeper.sleep(somethingHappened, 10);
		}
	}

	private void dispatch() {

		while (isRunning) {

			boolean somethingHappened = false;

			for (EndPoint<?> endPoint : endPoints) {

				somethingHappened &= endPoint.dispatch();
			}

			SilentSleeper.sleep(somethingHappened, 10);
		}
	}
}