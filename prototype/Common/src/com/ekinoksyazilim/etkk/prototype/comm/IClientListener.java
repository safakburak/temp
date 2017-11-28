package com.ekinoksyazilim.etkk.prototype.comm;

public interface IClientListener<T> {

	void endPointCreated(EndPoint<T> endPoint);
}
