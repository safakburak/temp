package com.ekinoksyazilim.etkk.prototype.comm;

import java.io.InputStream;
import java.io.OutputStream;

public interface IMessageCodec <T> {

	T decode(InputStream inputStream); 
	
	void encode(OutputStream outputStream, T message); 
}
