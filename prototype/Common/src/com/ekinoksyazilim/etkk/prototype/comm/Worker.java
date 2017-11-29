package com.ekinoksyazilim.etkk.prototype.comm;

import java.util.concurrent.ConcurrentHashMap;

import com.ekinoksyazilim.etkk.prototype.tools.SilentSleeper;

public class Worker {

	private boolean isRunning;

	private Thread readThread;
	private Thread writeThread;
	private Thread dispathThread;

	private ConcurrentHashMap<EndPoint<?>, Boolean> endPoints = new ConcurrentHashMap<>();

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

		endPoints.putIfAbsent(endPoint, true);
	}

	public void unassign(EndPoint<?> endPoint) {

		endPoints.remove(endPoint);
	}

	private void read() {

		while (isRunning) {

			boolean somethingHappened = false;

			for (EndPoint<?> endPoint : endPoints.keySet()) {

				somethingHappened &= endPoint.read();
			}

			SilentSleeper.sleep(somethingHappened, 10);
		}
	}

	private void write() {

		while (isRunning) {

			boolean somethingHappened = false;

			for (EndPoint<?> endPoint : endPoints.keySet()) {

				somethingHappened &= endPoint.write();
			}

			SilentSleeper.sleep(somethingHappened, 10);
		}
	}

	private void dispatch() {

		while (isRunning) {

			boolean somethingHappened = false;

			for (EndPoint<?> endPoint : endPoints.keySet()) {

				somethingHappened &= endPoint.dispatch();
			}

			SilentSleeper.sleep(somethingHappened, 10);
		}
	}
}
