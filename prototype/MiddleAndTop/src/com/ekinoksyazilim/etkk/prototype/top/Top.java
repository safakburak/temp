package com.ekinoksyazilim.etkk.prototype.top;

import java.util.concurrent.CompletableFuture;

import com.ekinoksyazilim.etkk.prototype.middle.IMiddle;
import com.ekinoksyazilim.etkk.prototype.service.IServiceCallback;

public class Top implements ITop {

	private IMiddle middle;
	
	@Override
	public CompletableFuture<Integer> sum(Integer[] inputs) {
		
		return middle.sum(inputs);
	}
	
	@Override
	public void sum(Integer[] inputs, IServiceCallback<Integer> callback) {

		sum(inputs).thenAccept(result -> callback.completed(result));
	}
	
	public void setMiddle(IMiddle middle) {
		
		this.middle = middle;
	}
}
