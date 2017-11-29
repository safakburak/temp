package com.ekinoksyazilim.etkk.prototype.comm.common;

public interface ICodecForByteArray <T> {

	T decode(byte[] data); 
	
	byte[] encode(T message); 
}
