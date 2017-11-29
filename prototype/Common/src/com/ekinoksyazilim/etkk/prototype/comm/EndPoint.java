package com.ekinoksyazilim.etkk.prototype.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ekinoksyazilim.etkk.prototype.comm.callback.IConnectionCallback;
import com.ekinoksyazilim.etkk.prototype.comm.callback.IMessageCallback;

public class EndPoint <T> {

	private ConcurrentLinkedQueue<Envelope<T>> inbox = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<Envelope<T>> outbox = new ConcurrentLinkedQueue<>();
	
	private ConcurrentHashMap<IEndPointListener<T>, Boolean> listeners = new ConcurrentHashMap<>();
	
	private Socket socket;
	
	private IMessageCodec<T> codec;
	
	
	
	public EndPoint() {
		
	}
	
	public EndPoint(IMessageCodec<T> codec) {
		
		this.codec = codec;
	}
	
	public EndPoint(Socket socket, IMessageCodec<T> codec) {
		
		this(codec);
		
		setSocket(socket);
	}
	
	public void send(T message, InetSocketAddress to) {

		outbox.add(new Envelope<T>(message, to));
	}

	public void receive(T message, InetSocketAddress from) {
		
		inbox.add(new Envelope<T>(message, from));
	}
	
	public void setSocket(Socket socket) {
		
		if(this.socket != null) {
		
			disconnect();
		}
		
		this.socket = socket;
		
		fireConnected();
	}
	
	public void disconnect() {
		
		if(this.socket != null) {
			
			try {
				
				this.socket.close();
				
			} catch (IOException e) {
				
				//nothing to do
			}
			
			this.socket = null;
			
			fireDisconnected();
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
			public void received(EndPoint<T> endPoint, T message, InetSocketAddress from) {
				
				callback.callback(endPoint, message, from);
			}
		};
		
		addListener(result);
		
		return result;
	}
	
	public IEndPointListener<T> onSend(IMessageCallback<T> callback) {
		
		IEndPointListener<T> result = new IEndPointListener<T>() {
			
			@Override
			public void sent(EndPoint<T> endPoint, T message, InetSocketAddress from) {
				
				callback.callback(endPoint, message, from);
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
	
	boolean read() {
		
		boolean result = false;
		
		try {

			if(socket != null) {
				
				InputStream inputStream = socket.getInputStream();
				
				T message = codec.decode(inputStream);
				
				if (message != null) {
					
					receive(message, (InetSocketAddress) socket.getRemoteSocketAddress());
					
					result = true;
				}
			}

		} catch (IOException e) {

			disconnect();
		}
		
		return result;
	}
	
	boolean write() {
		
		boolean result = false;
		
		try {

			if(socket != null) {
				
				OutputStream outputStream = socket.getOutputStream();
				
				Envelope<T> envelope = outbox.poll();
				
				if (envelope != null) {
					
					codec.encode(outputStream, envelope.getMessage());
					
					outputStream.flush();
					
					fireSent(envelope.getMessage(), envelope.getAddress());
					
					result = true;
				}
			} 
			
		} catch (IOException e) {
			
			disconnect();
		}
		
		return result;
	}
	
	boolean dispatch() {
	
		boolean result = false;
		
		Envelope<T> envelope = inbox.poll();
		
		if(envelope != null) {
			
			fireReceived(envelope.getMessage(), envelope.getAddress());
			result = true;
		}
		
		return result;
	}
	
	protected void setCodec(IMessageCodec<T> codec) {
		
		this.codec = codec;
	}
	
	private void fireConnected() {
		
		for(IEndPointListener<T> listener : listeners.keySet()) {
			
			listener.connected(this);
		}
	}
	
	private void fireDisconnected() {
		
		for(IEndPointListener<T> listener : listeners.keySet()) {
			
			listener.disconnected(this);
		}
	}
	
	private void fireReceived(T message, InetSocketAddress from) {
		
		for(IEndPointListener<T> listener : listeners.keySet()) {
			
			listener.received(this, message, from);
		}
	}
	
	private void fireSent(T message, InetSocketAddress to) {
		
		for(IEndPointListener<T> listener : listeners.keySet()) {
			
			listener.sent(this, message, to);
		}
	}
}
