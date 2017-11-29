package com.ekinoksyazilim.etkk.prototype.comm.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.ekinoksyazilim.etkk.prototype.comm.common.EndPoint;
import com.ekinoksyazilim.etkk.prototype.comm.common.ICodecForStream;

public class TcpEndPoint<T> extends EndPoint<T> {

	private ICodecForStream<T> codec;
	
	protected Socket socket;
	
	public void setCodec(ICodecForStream<T> codec) {
		
		this.codec = codec;
	}

	void setSocket(Socket socket) {
		
		disconnect();
		
		this.socket = socket;
		
		InetSocketAddress remote = (InetSocketAddress) socket.getRemoteSocketAddress();
		setRemote(remote.getHostName(), remote.getPort());
		
		setConnected(true);
	}

	@Override
	protected T doRead() throws IOException {

		T result = null;
		
		if(socket != null) {
			
			result = codec.decode(socket.getInputStream());
		}
		
		return result;
	}

	@Override
	protected void doWrite(T message) throws IOException {
		
		if(socket != null) {
			
			OutputStream stream = socket.getOutputStream();
			
			if(stream != null) {
				
				codec.encode(stream, message);
			}
		}
	}
	
	
	@Override
	protected void doDisconnect() throws IOException {

		socket.close();
	}
}
