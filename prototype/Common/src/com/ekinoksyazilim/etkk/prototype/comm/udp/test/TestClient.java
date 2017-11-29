package com.ekinoksyazilim.etkk.prototype.comm.udp.test;

import com.ekinoksyazilim.etkk.prototype.comm.common.ICodecForByteArray;
import com.ekinoksyazilim.etkk.prototype.comm.udp.UdpClient;

public class TestClient extends UdpClient<Integer> {

	@Override
	protected ICodecForByteArray<Integer> getCodec() {
		
		return new TestCodec();
	}
}
