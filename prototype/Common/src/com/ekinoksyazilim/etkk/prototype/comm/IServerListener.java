package com.ekinoksyazilim.etkk.prototype.comm;

public interface IServerListener<T> {

	void endPointCreated(EndPoint<T> endPoint);
}
