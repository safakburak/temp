package com.ekinoksyazilim.etkk.prototype.comm.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import com.ekinoksyazilim.etkk.prototype.comm.common.ICodecForByteArray;
import com.ekinoksyazilim.etkk.prototype.comm.common.Worker;
import com.ekinoksyazilim.etkk.prototype.comm.log.Logger;

public abstract class UdpClient<T> extends UdpEndPoint<T> {

	private Thread readingThread;
	
	private Worker worker;
	
	private byte[] buffer = new byte[4096];

	public UdpClient() {

		setCodec(getCodec());

		worker = new Worker();
		worker.start();
		worker.assign(this);
		
		readingThread = new Thread(this::accept);
		readingThread.start();
	}

	public void connect(String host, int port) {

		disconnect();

		try {

			socket = new DatagramSocket();
			setSocket(socket, host, port);

		} catch (IOException e) {

			Logger.log(e);
		}
	}

	protected abstract ICodecForByteArray<T> getCodec();
	
	private void accept() {
		
		while(true) {
			
			if(socket != null) {
				
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				
				try {
					
					socket.receive(packet);
					
					if(packet.getLength() > 0) {
						
						receive(Arrays.copyOf(packet.getData(), packet.getLength()));
					}
					
				} catch (IOException e) {
					
					// nothing to do
				}
			}
		}
	}
}
