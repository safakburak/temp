package com.ekinoksyazilim.etkk.prototype.comm.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.ekinoksyazilim.etkk.prototype.comm.common.IMessageCodec;

public class TestCodec implements IMessageCodec<Object> {

	@Override
	public Object decode(InputStream inputStream) {

		try {
			
			ObjectInputStream ois = new ObjectInputStream(inputStream);
			
			return ois.readObject();
			
		} catch (IOException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void encode(OutputStream outputStream, Object message) {

		try {
			
			ObjectOutputStream oos = new ObjectOutputStream(outputStream);
			
			oos.writeObject(message);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
