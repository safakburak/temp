package com.ekinoksyazilim.etkk.prototype.tools;

public class SilentSleeper {

	public static void sleep(boolean condition, long ms) {
		
		if(condition == true) {

			sleep(ms);
		}
	}
	
	public static void sleep(long ms) {
		
		try {
			
			Thread.sleep(ms);
			
		} catch (InterruptedException e) {
			
			// nothing to do
		}
	}
}
