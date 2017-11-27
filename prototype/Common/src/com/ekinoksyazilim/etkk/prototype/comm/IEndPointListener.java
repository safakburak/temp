package com.ekinoksyazilim.etkk.prototype.comm;

import java.net.InetSocketAddress;

public interface IEndPointListener <T> {

	void messageReceived(T message, InetSocketAddress from);
	
	void messageSent(T message, InetSocketAddress to);
}
