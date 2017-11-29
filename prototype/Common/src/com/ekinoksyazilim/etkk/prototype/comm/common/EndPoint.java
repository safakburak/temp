package com.ekinoksyazilim.etkk.prototype.comm.common;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ekinoksyazilim.etkk.prototype.comm.callback.IConnectionCallback;
import com.ekinoksyazilim.etkk.prototype.comm.callback.IMessageCallback;

public abstract class EndPoint<T> {

	protected String remoteHost;
	protected int remotePort;
	
	private ConcurrentLinkedQueue<T> inbox = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<T> outbox = new ConcurrentLinkedQueue<>();

	private ConcurrentHashMap<IEndPointListener<T>, Boolean> listeners = new ConcurrentHashMap<>();

	private boolean isConnected = false;

	/*
	 * Public methods
	 */
	
	public void send(T message) {

		outbox.add(message);
	}

	public void receive(T message) {

		inbox.add(message);
	}

	public boolean isConnected() {
		
		return isConnected;
	}
	
	public void disconnect() {

		if(isConnected == true) {
			
			try {
				
				doDisconnect();
				
			} catch(Exception e) {
				
				//nothing to do
			}
			
			setConnected(false);
		}
	}
	
	public void addListener(IEndPointListener<T> listener) {

		listeners.putIfAbsent(listener, true);
	}

	public void removeListener(IEndPointListener<T> listener) {

		listeners.remove(listener);
	}

	public IEndPointListener<T> onReceive(IMessageCallback<T> callback) {

		IEndPointListener<T> result = new IEndPointListener<T>() {

			@Override
			public void received(EndPoint<T> endPoint, T message) {

				callback.callback(endPoint, message);
			}
		};

		addListener(result);

		return result;
	}

	public IEndPointListener<T> onSend(IMessageCallback<T> callback) {

		IEndPointListener<T> result = new IEndPointListener<T>() {

			@Override
			public void sent(EndPoint<T> endPoint, T message) {

				callback.callback(endPoint, message);
			}
		};

		addListener(result);

		return result;
	}

	public IEndPointListener<T> onConnect(IConnectionCallback<T> callback) {

		IEndPointListener<T> result = new IEndPointListener<T>() {

			@Override
			public void connected(EndPoint<T> endPoint) {

				callback.callback(endPoint);
			}
		};

		addListener(result);

		return result;
	}

	public IEndPointListener<T> onDisconnect(IConnectionCallback<T> callback) {

		IEndPointListener<T> result = new IEndPointListener<T>() {

			@Override
			public void disconnected(EndPoint<T> endPoint) {

				callback.callback(endPoint);
			}
		};

		addListener(result);

		return result;
	}
	
	public void setRemote(String host, int port) {
		
		remoteHost = host;
		remotePort = port;
	}
	
	/*
	 * Package methods
	 */
	
	boolean read() {

		boolean result = false;

		try {

			T message = doRead();
			
			if(message != null) {

				receive(message);
				result = true;
			}

		} catch (IOException e) {

			disconnect();
		}

		return result;
	}
	
	boolean write() {

		boolean result = false;

		try {

			T message = outbox.poll();
			
			if(message != null) {
				
				doWrite(message);
				fireSent(message);
			}

		} catch (IOException e) {

			disconnect();
		}

		return result;
	}
	
	boolean dispatch() {

		boolean result = false;

		T message = inbox.poll();

		if (message != null) {

			fireReceived(message);
			
			result = true;
		}

		return result;
	}

	/*
	 * Protected methods
	 */
	
	protected void setConnected(boolean isConnected) {
		
		if(this.isConnected != isConnected) {
			
			this.isConnected = isConnected;
			
			if(isConnected) {
				
				fireConnected();
				
			} else {
				
				fireDisconnected();
			}
		}
	}

	/*
	 * Protected abstract methods 
	 */
	
	protected abstract T doRead() throws IOException;
	
	protected abstract void doWrite(T message) throws IOException;
	
	protected abstract void doDisconnect() throws IOException;
	
	/*
	 * Private methods
	 */
	
	private void fireConnected() {

		for (IEndPointListener<T> listener : listeners.keySet()) {

			listener.connected(this);
		}
	}

	private void fireDisconnected() {

		for (IEndPointListener<T> listener : listeners.keySet()) {

			listener.disconnected(this);
		}
	}

	private void fireReceived(T message) {

		for (IEndPointListener<T> listener : listeners.keySet()) {

			listener.received(this, message);
		}
	}

	private void fireSent(T message) {

		for (IEndPointListener<T> listener : listeners.keySet()) {

			listener.sent(this, message);
		}
	}
}
