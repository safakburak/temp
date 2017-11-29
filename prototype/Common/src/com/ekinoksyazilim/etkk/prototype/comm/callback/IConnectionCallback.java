package com.ekinoksyazilim.etkk.prototype.comm.callback;

import com.ekinoksyazilim.etkk.prototype.comm.EndPoint;

@FunctionalInterface
public interface IConnectionCallback<T> {

	void callback(EndPoint<T> endPoint); 
}
