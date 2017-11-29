package com.ekinoksyazilim.etkk.prototype.comm.tcp.test;

import com.ekinoksyazilim.etkk.prototype.comm.common.ICodecForStream;
import com.ekinoksyazilim.etkk.prototype.comm.tcp.TcpClient;

public class TestClient extends TcpClient<Object> {

	@Override
	protected ICodecForStream<Object> getCodec() {
		
		return new TestCodec();
	}
}
