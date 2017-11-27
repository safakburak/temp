package com.ekinoksyazilim.etkk.prototype.left;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import com.ekinoksyazilim.etkk.prototype.dummy.DummyWork;

public class BottomLeft implements IBottomLeft {

	@Override
	public CompletableFuture<Integer> sum(Integer[] inputs) {

		System.out.println("BottomLeft called from thread: " + Thread.currentThread().getId());
		
		return CompletableFuture.supplyAsync(() -> {
		
			System.out.println("BottomLeft running at thread: " + Thread.currentThread().getId());
			
			DummyWork.doDummy();
			
			return Arrays.asList(inputs).stream().mapToInt(i -> i).sum();
		});
	}
}
