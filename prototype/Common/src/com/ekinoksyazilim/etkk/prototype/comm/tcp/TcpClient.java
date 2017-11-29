package com.ekinoksyazilim.etkk.prototype.comm.tcp;

import java.io.IOException;
import java.net.Socket;

import com.ekinoksyazilim.etkk.prototype.comm.common.ICodecForStream;
import com.ekinoksyazilim.etkk.prototype.comm.common.Worker;
import com.ekinoksyazilim.etkk.prototype.comm.log.Logger;

public abstract class TcpClient<T> extends TcpEndPoint<T> {

	private Worker worker;
	
	public TcpClient() {
	
		setCodec(getCodec());
		
		worker = new Worker();
		worker.start();
		worker.assign(this);
	}
	
	public void connect(String host, int port) {
		
		disconnect();
		
		try {
			
			socket = new Socket(host, port);
			setSocket(socket);
			
		} catch (IOException e) {

			Logger.log(e);
		}
	}
	
	protected abstract ICodecForStream<T> getCodec();
}
