package com.ekinoksyazilim.etkk.prototype.comm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public abstract class CommServer <T> {

	private ConcurrentSkipListSet<IClientListener<T>> listeners = new ConcurrentSkipListSet<>();
	
	private ConcurrentHashMap<RemoteEndPointKey, EndPoint<T>> endPointMap = new ConcurrentHashMap<>();
	
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
	
	public void addListener(IClientListener<T> listener) {
		
		listeners.add(listener);
	}
	
	public void removeListener(IClientListener<T> listener) {
		
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
				
				InetSocketAddress from = (InetSocketAddress) socket.getRemoteSocketAddress();
				RemoteEndPointKey key = new RemoteEndPointKey(from.getHostName(), from.getPort());

				EndPoint<T> endPoint;
				
				if(endPointMap.contains(key)) {

					endPoint = endPointMap.get(key);
					endPoint.setSocket(socket);
					
				} else {
					
					endPoint = new EndPoint<>(socket, getExtractor(), getParser());
					
					endPointMap.put(key, endPoint);
					
					workers[nextWorker].assign(endPoint);
					nextWorker = (nextWorker + 1) % workers.length;
					
					fireEndPointCreated(endPoint);
				}
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}

		isClosing = false;
	}
	
	private void fireEndPointCreated(EndPoint<T> endPoint) {
		
		for(IClientListener<T> listener : listeners) {
			
			listener.endPointCreated(endPoint);
		}
	}
}
