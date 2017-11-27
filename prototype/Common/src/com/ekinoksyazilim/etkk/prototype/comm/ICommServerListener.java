package com.ekinoksyazilim.etkk.prototype.comm;

public interface ICommServerListener {

	void clientConnected(EndPoint<?> endPoint);
	void clientDisconnected(EndPoint<?> endPoint);
}
