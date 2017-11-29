package com.ekinoksyazilim.etkk.prototype.comm.udp.test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.ekinoksyazilim.etkk.prototype.comm.common.ICodecForByteArray;

public class TestCodec implements ICodecForByteArray<Integer> {

	@Override
	public Integer decode(byte[] data) {
		
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.put(data);
		buffer.rewind();
		
		return buffer.getInt();
	}

	@Override
	public byte[] encode(Integer message) {
		
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(message);
		
		return Arrays.copyOf(buffer.array(), 4);
	}
}
