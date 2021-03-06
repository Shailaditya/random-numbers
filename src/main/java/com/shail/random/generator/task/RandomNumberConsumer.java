package com.shail.random.generator.task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.BlockingQueue;

/*
 * A consumer class which will read the numbers generated by random number producers
 * and write it to disk.
 */
public class RandomNumberConsumer implements Callable<Void>{
	
	private static Logger LOG = LoggerFactory.getLogger(RandomNumberConsumer.class);
	private final String path;
	private final BlockingQueue<List<Integer>> queue;
	private final Integer totalNumbers;
	
	public RandomNumberConsumer(BlockingQueue<List<Integer>> queue,String path,Integer totalNumbers){
		this.path=path;
		this.queue=queue;
		this.totalNumbers=totalNumbers;
		
	}
	
	@Override
	public Void call() throws Exception{
		int count=0;
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path)))){
			while(count < totalNumbers) {
				List<Integer> list=queue.take();
				this.writeToFile(list,pw);
				count+=list.size();
			}
		}catch (IOException io) {
			LOG.error("Error in consumer while writing.",io);
			throw io;
		}
        return null;
    }
	
	private void writeToFile(List<Integer> list,PrintWriter pw) {
		list.forEach(item -> pw.println(item));
	}

}
