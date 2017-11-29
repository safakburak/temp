package com.ekinoksyazilim.etkk.prototype.comm.tcp.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.ekinoksyazilim.etkk.prototype.comm.common.ICodecForStream;

public class TestCodec implements ICodecForStream<Object> {

	@Override
	public Object decode(InputStream stream) throws IOException {
		
		ObjectInputStream ois = new ObjectInputStream(stream);
		
		try {
			
			return ois.readObject();
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void encode(OutputStream stream, Object message) throws IOException {

		ObjectOutputStream oos = new ObjectOutputStream(stream);
		
		oos.writeObject(message);
		oos.flush();
	}
}
