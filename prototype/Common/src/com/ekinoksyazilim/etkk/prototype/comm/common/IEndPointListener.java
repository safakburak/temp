package com.ekinoksyazilim.etkk.prototype.comm.common;

public interface IEndPointListener <T> {

	default void received(EndPoint<T> endPoint, T message) {
		
	}
	
	default void sent(EndPoint<T> endPoint, T message) {
		
	}
	
	default void connected(EndPoint<T> endPoint) {
		
	}
	
	default void disconnected(EndPoint<T> endPoint) {
		
	}
}
