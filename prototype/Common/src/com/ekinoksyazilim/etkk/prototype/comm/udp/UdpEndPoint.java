package com.ekinoksyazilim.etkk.prototype.comm.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ekinoksyazilim.etkk.prototype.comm.common.EndPoint;
import com.ekinoksyazilim.etkk.prototype.comm.common.ICodecForByteArray;

public class UdpEndPoint<T> extends EndPoint<T> {

	protected DatagramSocket socket;

	private ICodecForByteArray<T> codec;

	private ConcurrentLinkedQueue<byte[]> readQueue = new ConcurrentLinkedQueue<>();

	/*
	 * Public methods
	 */
	
	public void receive(byte[] data) {

		readQueue.add(data);
	}

	public void setCodec(ICodecForByteArray<T> codec) {

		this.codec = codec;
	}

	/*
	 * Protected methods
	 */
	
	protected void setSocket(DatagramSocket socket, String remoteHost, int remotePort) {

		disconnect();

		this.socket = socket;
		
		setRemote(remoteHost, remotePort);

		setConnected(true);
	}

	@Override
	protected T doRead() throws IOException {

		byte[] data = readQueue.poll();

		T result = null;

		if (data != null) {

			result = codec.decode(data);
		}

		return result;
	}

	@Override
	protected void doWrite(T message) throws IOException {

		byte[] data = codec.encode(message);

		if (data != null) {

			DatagramPacket p = new DatagramPacket(data, data.length);
			p.setAddress(InetAddress.getByName(remoteHost));
			p.setPort(remotePort);
			socket.send(p);
		}
	}

	@Override
	protected void doDisconnect() throws IOException {

	}
}
