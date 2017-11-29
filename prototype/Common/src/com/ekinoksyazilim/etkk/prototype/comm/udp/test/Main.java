package com.ekinoksyazilim.etkk.prototype.comm.udp.test;

public class Main {

	public static void main(String[] args) {

		TestServer server = new TestServer(1111, 2);
		
		server.onCreateEndPoint((endPoint) -> {
			
			endPoint.onReceive((ep, message) -> {

				System.out.println("server: " + message);
				
				if((int)message < 0) {

					ep.send((int)message - 1);
					
				} else if((int)message > 0) {
					
					ep.send((int)message + 1);
				}
			});
		});
		
		server.start();
		
		TestClient client1 = new TestClient();
		
		client1.onReceive((ep, message) -> {
			
			System.out.println("client1: " + message);
			ep.send((int)message - 1);
		});
		
		client1.connect("127.0.0.1", 1111);
		client1.send(-1);
		
		TestClient client2 = new TestClient();
		
		client2.onReceive((ep, message) -> {
			
			System.out.println("client2: " + message);
			ep.send((int)message + 1);
		});
		
		client2.connect("localhost", 1111);
		client2.send(1);
	}
}
