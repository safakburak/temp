package com.ekinoksyazilim.etkk.prototype.dummy;

public class DummyWork {

	public static void doDummy() {
		
		try {
			
			long waitTime = (long) (Math.random() * 7000);
			
			System.out.println("Thread " + Thread.currentThread().getId() + " waiting for " + waitTime + " ms");
			
			Thread.sleep(waitTime);
			
		} catch (InterruptedException e) {
			
			// nothing to do
		}
	}
}
