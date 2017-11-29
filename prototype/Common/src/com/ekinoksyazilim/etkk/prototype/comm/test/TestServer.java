package com.ekinoksyazilim.etkk.prototype.comm.test;

import com.ekinoksyazilim.etkk.prototype.comm.CommServer;
import com.ekinoksyazilim.etkk.prototype.comm.IMessageCodec;

public class TestServer extends CommServer<Object> {

	public TestServer(int port, int numberOfWorkers) {
		
		super(port, numberOfWorkers);
	}

	@Override
	protected IMessageCodec<Object> getExtractor() {
		
		return new TestCodec();
	}
}
