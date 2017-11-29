package com.ekinoksyazilim.etkk.prototype.comm.udp.test;

import com.ekinoksyazilim.etkk.prototype.comm.common.ICodecForByteArray;
import com.ekinoksyazilim.etkk.prototype.comm.udp.UdpServer;

public class TestServer extends UdpServer<Integer> {

	public TestServer(int port, int numberOfWorkers) {
		
		super(port, numberOfWorkers);
	}

	@Override
	protected ICodecForByteArray<Integer> getCodec() {
		
		return new TestCodec();
	}
}
