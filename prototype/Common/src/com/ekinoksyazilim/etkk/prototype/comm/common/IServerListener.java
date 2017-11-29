package com.ekinoksyazilim.etkk.prototype.comm.common;

public interface IServerListener<T> {

	void endPointCreated(EndPoint<T> endPoint);
}
