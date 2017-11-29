package com.ekinoksyazilim.etkk.prototype.comm.test;

import com.ekinoksyazilim.etkk.prototype.comm.common.IMessageCodec;
import com.ekinoksyazilim.etkk.prototype.comm.tcp.TcpServer;

public class TestServer extends TcpServer<Object> {

	public TestServer(int port, int numberOfWorkers) {
		
		super(port, numberOfWorkers);
	}

	@Override
	protected IMessageCodec<Object> getCodec() {
		
		return new TestCodec();
	}
}
