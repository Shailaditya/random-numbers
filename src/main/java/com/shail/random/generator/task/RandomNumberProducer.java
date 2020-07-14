package com.shail.random.generator.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Task to generate random numbers using ThreadLocalRandom class
 * This class will push data in batches so as to reduce contention
 * among producers in clocking queue.
 */
public class RandomNumberProducer implements Callable<Void>{
	
	private final BlockingQueue<List<Integer>> queue;
	private final int iterations;
	private final int THRESHOLD=5000;
	
	public RandomNumberProducer(int iterations,BlockingQueue<List<Integer>> queue) {
		this.iterations=iterations;
		this.queue=queue;
	}

	@Override
	public Void call() throws Exception {
		//Don't want the ArrayList to resize so setting the size up front.
		List<Integer> list=new ArrayList<>(THRESHOLD);
		for(int i=0;i<iterations;i++) {
			list.add(ThreadLocalRandom.current().nextInt());
			if(list.size() == THRESHOLD) {
				//To avoid contention at queue level sending data in chunks.
				queue.put(list);
				list = new ArrayList<>(THRESHOLD);
			}
		}
		return null;
	}

}
