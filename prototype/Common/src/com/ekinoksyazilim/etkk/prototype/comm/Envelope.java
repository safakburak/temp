package com.ekinoksyazilim.etkk.prototype.comm;

import java.net.InetSocketAddress;

public class Envelope <T> {

	private T message;
	private InetSocketAddress address;
	
	public Envelope(T message, InetSocketAddress address) {
		
		this.message = message;
		this.address = address;
	}
	
	public T getMessage() {
		
		return message;
	}

	public InetSocketAddress getAddress() {
		
		return address;
	}
}
