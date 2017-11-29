package com.ekinoksyazilim.etkk.prototype.comm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentHashMap;

import com.ekinoksyazilim.etkk.prototype.comm.callback.IEndPointCreationCallback;

public abstract class CommServer <T> {

	private ConcurrentHashMap<IServerListener<T>, Boolean> listeners = new ConcurrentHashMap<>();
	
	private ConcurrentHashMap<RemoteEndPointKey, EndPoint<T>> endPointMap = new ConcurrentHashMap<>();
	
	private Worker[] workers;
	
	private int nextWorker = 0;
	
	private ServerSocket serverSocket;
	
	private int port;
	
	private boolean isClosing = false;
	private boolean isRunning = false;
	
	private Thread acceptingThread;
	
	public CommServer(int port) {
		
		this(port, 1);
	}
	
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
	
	public void start() {
		
		if(isClosing == false) {
			
			try {
				
				serverSocket = new ServerSocket(port);
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			isRunning = true;
			acceptingThread.start();
		}
	}
	
	public void stop() {
		
		isClosing = true;
		isRunning = false;
		
		try {
			
			acceptingThread.join();
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
			
			//nothing to do
		}
	}
	
	protected abstract IMessageCodec<T> getExtractor(); 
	
	private void accept() {
		
		while(isRunning) {
			
			try {
				
				if(serverSocket == null) {
					
					Thread.sleep(10);
					
				} else {
					
					serverSocket.setSoTimeout(1000);
					Socket socket = serverSocket.accept();
					
					InetSocketAddress from = (InetSocketAddress) socket.getRemoteSocketAddress();
					RemoteEndPointKey key = new RemoteEndPointKey(from.getHostName(), from.getPort());
					
					EndPoint<T> endPoint;
					
					if(endPointMap.contains(key)) {
						
						endPoint = endPointMap.get(key);
						endPoint.setSocket(socket);
						
					} else {
						
						endPoint = new EndPoint<>(socket, getExtractor());
						
						endPointMap.put(key, endPoint);
						
						workers[nextWorker].assign(endPoint);
						nextWorker = (nextWorker + 1) % workers.length;
						
						fireEndPointCreated(endPoint);
					}
				}
				
			} catch(SocketTimeoutException e) {
				
				//nothing to do
				
			} catch (IOException | InterruptedException e) {
				
				e.printStackTrace();
				
			} 
		}

		isClosing = false;
	}
	
	private void fireEndPointCreated(EndPoint<T> endPoint) {
		
		for(IServerListener<T> listener : listeners.keySet()) {
			
			listener.endPointCreated(endPoint);
		}
	}
}
