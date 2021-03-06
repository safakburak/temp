package com.ekinoksyazilim.etkk.prototype.right;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import com.ekinoksyazilim.etkk.prototype.dummy.DummyWork;

public class BottomRight implements IBottomRight{

	@Override
	public CompletableFuture<Integer> sum(Integer[] inputs) {

		System.out.println("BottomRight called from thread: " + Thread.currentThread().getId());
		
		return CompletableFuture.supplyAsync(() -> {
			
			System.out.println("BottomRight running at thread: " + Thread.currentThread().getId());
			
			DummyWork.doDummy();
			
			return Arrays.asList(inputs).stream().mapToInt(i -> i).sum();
		});
	}
}
