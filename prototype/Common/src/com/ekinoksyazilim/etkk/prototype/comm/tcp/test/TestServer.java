package com.ekinoksyazilim.etkk.prototype.comm.tcp.test;

import com.ekinoksyazilim.etkk.prototype.comm.common.ICodecForStream;
import com.ekinoksyazilim.etkk.prototype.comm.tcp.TcpServer;

public class TestServer extends TcpServer<Object> {

	public TestServer(int port, int numberOfWorkers) {
		
		super(port, numberOfWorkers);
	}

	@Override
	protected ICodecForStream<Object> getCodec() {
		
		return new TestCodec();
	}
}
