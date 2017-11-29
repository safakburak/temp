package com.ekinoksyazilim.etkk.prototype.comm.callback;

import java.net.InetSocketAddress;

import com.ekinoksyazilim.etkk.prototype.comm.EndPoint;

@FunctionalInterface
public interface IMessageCallback<T> {

	void callback(EndPoint<T> endPoint, T message, InetSocketAddress from);
}
