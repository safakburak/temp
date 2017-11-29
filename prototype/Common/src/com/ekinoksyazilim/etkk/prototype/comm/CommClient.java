package com.ekinoksyazilim.etkk.prototype.comm;

import java.io.IOException;
import java.net.Socket;

public abstract class CommClient<T> extends EndPoint<T> {

	private Worker worker;
	
	public CommClient() {
	
		setCodec(getExtractor());
		
		worker = new Worker();
		worker.start();
		worker.assign(this);
	}
	
	public boolean connect(String host, int port) {
		
		disconnect();
		
		try {
			
			Socket socket = new Socket(host, port);
			setSocket(socket);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
			return false;
		}

		return true;
	}

	protected abstract IMessageCodec<T> getExtractor();
}
