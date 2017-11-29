package com.ekinoksyazilim.etkk.prototype.comm.test;

import com.ekinoksyazilim.etkk.prototype.comm.CommClient;
import com.ekinoksyazilim.etkk.prototype.comm.IMessageCodec;

public class TestClient extends CommClient<Object> {

	@Override
	protected IMessageCodec<Object> getExtractor() {
		
		return new TestCodec();
	}
}
