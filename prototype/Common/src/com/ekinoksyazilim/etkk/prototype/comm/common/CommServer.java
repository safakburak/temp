package com.ekinoksyazilim.etkk.prototype.comm.common;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.ekinoksyazilim.etkk.prototype.comm.callback.IEndPointCreationCallback;

public abstract class CommServer <T> {

	protected ConcurrentHashMap<RemoteEndPointKey, EndPoint<T>> endPointMap = new ConcurrentHashMap<>();
	
	protected Worker[] workers;
	protected int nextWorker = 0;
	
	protected int port;
	
	private ConcurrentHashMap<IServerListener<T>, Boolean> listeners = new ConcurrentHashMap<>();
	
	private boolean isClosing = false;
	private boolean isRunning = false;
	
	private Thread acceptingThread;
	
	/*
	 * Constructors
	 */
	
	public CommServer(int port) {
		
		this(port, 1);
	}
	
	/*
	 * Public methods
	 */
	
	public CommServer(int port, int numberOfWorkers) {
		
		this.port = port;
		
		workers = new Worker[numberOfWorkers];
		
		for(int i = 0; i < workers.length; i++) {
		
			Worker worker = new Worker();
			worker.start();
			workers[i] = worker;
		}
		
		acceptingThread = new Thread(this::accept);
	}
	
	public synchronized void start() {
		
		if(isClosing == false && isRunning == false) {
			
			try {
				
				doStart();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			isRunning = true;
			
			acceptingThread.start();
		}
	}
	
	public synchronized void stop() {
		
		if(isClosing == false && isRunning == true) {
			
			isClosing = true;
			isRunning = false;
			
			try {
				
				acceptingThread.join(2000);
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
				
				//nothing to do
			}
		}
	}
	
	public void addListener(IServerListener<T> listener) {
		
		listeners.putIfAbsent(listener, true);
	}
	
	public void removeListener(IServerListener<T> listener) {
		
		listeners.remove(listener);
	}
	
	public IServerListener<T> onCreateEndPoint(IEndPointCreationCallback<T> callback) {
	
		IServerListener<T> result = new IServerListener<T>() {

			@Override
			public void endPointCreated(EndPoint<T> endPoint) {
				
				callback.callback(endPoint);
			}
		};
		
		addListener(result);
		
		return result;
	}
	
	/*
	 * Protected methods
	 */
	
	
	protected abstract void doAccept() throws IOException;
	
	protected abstract void doStart() throws IOException;
	
	protected void fireEndPointCreated(EndPoint<T> endPoint) {
		
		for(IServerListener<T> listener : listeners.keySet()) {
			
			listener.endPointCreated(endPoint);
		}
	}
	
	/*
	 * Private methods
	 */
	
	private void accept() {
		
		while(isRunning) {
			
			try {
				
				doAccept();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			} 
		}

		isClosing = false;
	}
}
