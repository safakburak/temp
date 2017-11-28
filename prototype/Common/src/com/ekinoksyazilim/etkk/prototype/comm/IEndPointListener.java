package com.ekinoksyazilim.etkk.prototype.comm;

import java.net.InetSocketAddress;

public interface IEndPointListener <T> {

	void received(EndPoint<T> endPoint, T message, InetSocketAddress from);
	
	void sent(EndPoint<T> endPoint, T message, InetSocketAddress to);
	
	void connected(EndPoint<T> endPoint);
	
	void disconnected(EndPoint<T> endPoint);
}
