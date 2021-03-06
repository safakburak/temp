package com.ekinoksyazilim.etkk.prototype.middle;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ekinoksyazilim.etkk.prototype.left.IBottomLeft;
import com.ekinoksyazilim.etkk.prototype.right.IBottomRight;

public class Middle implements IMiddle {

	private IBottomLeft bottomLeft;
	
	private IBottomRight bottomRight;
	
	@Override
	public CompletableFuture<Integer> sum(Integer[] inputs) {

		System.out.println("Middle called from thread: " + Thread.currentThread().getId());
		
		return CompletableFuture.supplyAsync(() -> {

			System.out.println("Middle running at thread: " + Thread.currentThread().getId());
			
			Integer[] part1 = Arrays.copyOfRange(inputs, 0, inputs.length / 2);
			Integer[] part2 = Arrays.copyOfRange(inputs, inputs.length / 2, inputs.length);
			
			CompletableFuture<Integer> future1 = bottomLeft.sum(part1);
			CompletableFuture<Integer> future2 = bottomRight.sum(part2);

			Integer result = null;
			
			try {
				
				result = future1.get(5, TimeUnit.SECONDS) + future2.get(5, TimeUnit.SECONDS);
				
			} catch (InterruptedException | ExecutionException e) {
				
				System.out.println("exception!");
				
			} catch (TimeoutException e) {
				
				System.out.println("timeout!");
			}
			
			return result;
		});
	}
	
	public void setBottomLeft(IBottomLeft bottomLeft) {
		
		this.bottomLeft = bottomLeft;
	}
	
	public void setBottomRight(IBottomRight bottomRight) {
		
		this.bottomRight = bottomRight;
	}
}
