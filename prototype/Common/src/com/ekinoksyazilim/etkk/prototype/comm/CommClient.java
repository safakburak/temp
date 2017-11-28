package com.ekinoksyazilim.etkk.prototype.comm;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentSkipListSet;

public abstract class CommClient <T> {

	private ConcurrentSkipListSet<IClientListener> listeners = new ConcurrentSkipListSet<>();
	
	private EndPoint<T> endPoint;
	private Worker worker;
	
	public CommClient() {
	
		worker = new Worker();
	}

	public void connect(String host, int port) throws UnknownHostException, IOException {
		
		Socket socket = new Socket(host, port);
		
		endPoint = new EndPoint<>(socket, getExtractor(), getParser());
		worker.assign(endPoint);
	}
	
	public void addListener(IClientListener listener) {
		
		listeners.add(listener);
	}
	
	public void removeListener(IClientListener listener) {
		
		listeners.remove(listener);
	}
	
	public EndPoint<T> getEndPoint() {
		
		return endPoint;
	}
	
	protected abstract IParser<T> getParser();
	protected abstract IPackageExtractor getExtractor(); 
}
