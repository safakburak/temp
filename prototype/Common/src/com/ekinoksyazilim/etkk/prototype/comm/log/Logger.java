package com.ekinoksyazilim.etkk.prototype.comm.log;

public class Logger {

	public static void log(String log) {
		
		System.out.println(log);
	}
	
	public static void log(Exception e) {
		
		e.printStackTrace();
	} 
}
