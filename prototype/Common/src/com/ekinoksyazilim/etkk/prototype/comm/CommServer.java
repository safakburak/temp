package com.ekinoksyazilim.etkk.prototype.comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentSkipListSet;

public abstract class CommServer <T> {

	private ConcurrentSkipListSet<ICommServerListener> listeners = new ConcurrentSkipListSet<>();
	
	private Worker[] workers;
	
	private int nextWorker = 0;
	
	private ServerSocket serverSocket;
	
	private int port;
	
	private boolean isClosing = false;
	private boolean isRunning = false;
	
	public CommServer(int port) {
		
		this(port, 1);
	}
	
	public CommServer(int port, int numberOfWorkers) {
		
		this.port = port;
		
		workers = new Worker[numberOfWorkers];
		
		for(int i = 0; i < workers.length; i++) {
		
			workers[i] = new Worker();
		}
	}
	
	public void addListener(ICommServerListener listener) {
		
		listeners.add(listener);
	}
	
	public void removeListener(ICommServerListener listener) {
		
		listeners.remove(listener);
	}
	
	public void start() {
		
		if(isClosing == false) {
			
			try {
				
				serverSocket = new ServerSocket(port);
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			isRunning = true;
			accept();
		}
	}
	
	public void stop() {
		
		isClosing = true;
		isRunning = false;
	}
	
	protected abstract IParser<T> getParser();
	protected abstract IPackageExtractor getExtractor(); 
	
	private void accept() {
		
		while(isRunning) {
			
			try {
				
				Socket socket = serverSocket.accept();
				EndPoint<T> endPoint = new EndPoint<>(socket, getExtractor(), getParser());
				
				workers[nextWorker].assign(endPoint);
				
				nextWorker = (nextWorker + 1) % workers.length;
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}

		isClosing = false;
	}
}
