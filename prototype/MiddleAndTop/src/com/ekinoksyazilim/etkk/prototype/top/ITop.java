package com.ekinoksyazilim.etkk.prototype.top;

import java.util.concurrent.CompletableFuture;

import com.ekinoksyazilim.etkk.prototype.service.IServiceCallback;

public interface ITop {

	CompletableFuture<Integer> sum(Integer[] inputs);
	
	void sum(Integer[] inputs, IServiceCallback<Integer> callback);
}
