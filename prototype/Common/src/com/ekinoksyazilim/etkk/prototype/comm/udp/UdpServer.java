package com.ekinoksyazilim.etkk.prototype.comm.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;

import com.ekinoksyazilim.etkk.prototype.comm.common.CommServer;
import com.ekinoksyazilim.etkk.prototype.comm.common.ICodecForByteArray;
import com.ekinoksyazilim.etkk.prototype.comm.common.RemoteEndPointKey;

public abstract class UdpServer<T> extends CommServer<T> {

	private DatagramSocket socket;
	
	private byte[] buffer = new byte[4096];
	
	public UdpServer(int port, int numberOfWorkers) {

		super(port, numberOfWorkers);
	}

	@Override
	protected void doAccept() throws IOException {

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		socket.receive(packet);

		if(packet.getLength() > 0) {

			RemoteEndPointKey key = new RemoteEndPointKey(packet.getAddress().getHostAddress(), packet.getPort());
			
			UdpEndPoint<T> endPoint = (UdpEndPoint<T>) endPointMap.get(key);
			
			if(endPoint == null) {
				
				endPoint = new UdpEndPoint<T>();
				endPoint.setSocket(socket, ((InetSocketAddress)packet.getSocketAddress()).getHostName(), packet.getPort());
				endPoint.setCodec(getCodec());
				
				endPointMap.put(key, endPoint);
				
				workers[nextWorker].assign(endPoint);
				nextWorker = (nextWorker + 1) % workers.length;

				fireEndPointCreated(endPoint);
			}
			
			((UdpEndPoint<T>)endPoint).receive(Arrays.copyOf(packet.getData(), packet.getLength()));
		}
	}

	@Override
	protected void doStart() throws IOException {

		socket = new DatagramSocket(port);
	}
	
	protected abstract ICodecForByteArray<T> getCodec();
}
