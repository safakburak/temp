package com.ekinoksyazilim.etkk.prototype.middle;

import java.util.concurrent.CompletableFuture;

public interface IMiddle {

	CompletableFuture<Integer> sum(Integer[] inputs);
}
