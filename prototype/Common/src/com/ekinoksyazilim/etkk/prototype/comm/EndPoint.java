package com.ekinoksyazilim.etkk.prototype.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class EndPoint <T> {

	private ConcurrentLinkedQueue<Envelope<T>> inbox = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<Envelope<T>> outbox = new ConcurrentLinkedQueue<>();
	
	private ConcurrentSkipListSet<IEndPointListener<T>> listeners = new ConcurrentSkipListSet<>();
	
	private Socket socket;
	
	private IPackageExtractor extractor;
	
	private IParser<T> parser;
	
	public EndPoint(Socket socket, IPackageExtractor extractor, IParser<T> parser) {
		
		this.socket = socket;
		this.extractor = extractor;
		this.parser = parser;
	}
	
	public void send(T message, InetSocketAddress to) {

		outbox.add(new Envelope<T>(message, to));
	}

	public void receive(T message, InetSocketAddress from) {
		
		inbox.add(new Envelope<T>(message, from));
	}
	
	public void addListener(IEndPointListener<T> listener) {

		listeners.add(listener);
	}

	public void removeListener(IEndPointListener<T> listener) {

		listeners.remove(listener);
	}
	

	boolean read() {
		
		boolean result = false;
		
		try {

			InputStream inputStream = socket.getInputStream();
			
			byte[] data = extractor.extract(inputStream);

			if (data != null) {

				T message = parser.decode(data);

				receive(message, (InetSocketAddress) socket.getRemoteSocketAddress());

				result = true;
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return result;
	}
	
	boolean write() {
		
		boolean result = false;
		
		try {

			OutputStream outputStream = socket.getOutputStream();
			
			Envelope<T> envelope = outbox.poll();

			if (envelope != null) {

				byte[] data = parser.encode(envelope.getMessage());

				outputStream.write(data);
				outputStream.flush();
				
				for(IEndPointListener<T> listener : listeners) {
					
					listener.messageSent(envelope.getMessage(), envelope.getAddress());
				}
				
				result = true;
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return result;
	}
	
	boolean dispatch() {
	
		boolean result = false;
		
		Envelope<T> envelope = inbox.poll();
		T message = envelope.getMessage();

		for(IEndPointListener<T> listener : listeners) {
			
			listener.messageReceived(message, envelope.getAddress());
		}
		
		return result;
	}
}