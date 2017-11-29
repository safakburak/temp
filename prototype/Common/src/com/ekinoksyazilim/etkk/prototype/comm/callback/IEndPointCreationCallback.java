package com.ekinoksyazilim.etkk.prototype.comm.callback;

import com.ekinoksyazilim.etkk.prototype.comm.EndPoint;

@FunctionalInterface
public interface IEndPointCreationCallback<T> {

	void callback(EndPoint<T> endPoint);
}
