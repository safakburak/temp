package com.ekinoksyazilim.etkk.prototype.comm.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ICodecForStream <T> {

	T decode(InputStream stream) throws IOException; 
	
	void encode(OutputStream stream, T message) throws IOException; 
}
