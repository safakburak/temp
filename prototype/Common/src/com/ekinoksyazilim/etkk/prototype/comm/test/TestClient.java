package com.ekinoksyazilim.etkk.prototype.comm.test;

import com.ekinoksyazilim.etkk.prototype.comm.common.IMessageCodec;
import com.ekinoksyazilim.etkk.prototype.comm.tcp.TcpCommClient;

public class TestClient extends TcpCommClient<Object> {

	@Override
	protected IMessageCodec<Object> getExtractor() {
		
		return new TestCodec();
	}
}
