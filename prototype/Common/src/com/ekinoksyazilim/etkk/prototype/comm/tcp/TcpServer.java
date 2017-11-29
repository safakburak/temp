package com.ekinoksyazilim.etkk.prototype.comm.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.ekinoksyazilim.etkk.prototype.comm.common.CommServer;
import com.ekinoksyazilim.etkk.prototype.comm.common.ICodecForStream;
import com.ekinoksyazilim.etkk.prototype.comm.common.RemoteEndPointKey;
import com.ekinoksyazilim.etkk.prototype.tools.SilentSleeper;

public abstract class TcpServer<T> extends CommServer<T> {

	private ServerSocket serverSocket;
	
	public TcpServer(int port, int numberOfWorkers) {

		super(port, numberOfWorkers);
	}

	@Override
	protected void doAccept() throws IOException {

		if(serverSocket == null) {
			
			SilentSleeper.sleep(10);
			
		} else {

			try {
				
				serverSocket.setSoTimeout(1000);
				Socket socket = serverSocket.accept();

				InetSocketAddress from = (InetSocketAddress) socket.getRemoteSocketAddress();
				RemoteEndPointKey key = new RemoteEndPointKey(from.getHostName(), from.getPort());

				TcpEndPoint<T> endPoint = (TcpEndPoint<T>) endPointMap.get(key);
				
				if(endPoint == null) {
					
					endPoint = new TcpEndPoint<>();
					endPoint.setCodec(getCodec());
					
					workers[nextWorker].assign(endPoint);
					nextWorker = (nextWorker + 1) % workers.length;
					
					fireEndPointCreated(endPoint);
					
					endPointMap.put(key, endPoint);
				}
				
				endPoint.setSocket(socket);
				
			} catch (SocketTimeoutException e) {
				
				//nothing to do
			}
		}
	}
	
	@Override
	protected void doStart() throws IOException {

		serverSocket = new ServerSocket(port);
	}
	
	protected abstract ICodecForStream<T> getCodec(); 
}
