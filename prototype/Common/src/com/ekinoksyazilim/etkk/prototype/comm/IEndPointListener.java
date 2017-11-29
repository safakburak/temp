package com.ekinoksyazilim.etkk.prototype.comm;

import java.net.InetSocketAddress;

public interface IEndPointListener <T> {

	default void received(EndPoint<T> endPoint, T message, InetSocketAddress from) {
		
	}
	
	default void sent(EndPoint<T> endPoint, T message, InetSocketAddress to) {
		
	}
	
	default void connected(EndPoint<T> endPoint) {
		
	}
	
	default void disconnected(EndPoint<T> endPoint) {
		
	}
}
