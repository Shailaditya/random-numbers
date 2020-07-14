package com.shail.random.sort;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shail.random.sort.task.SortTask;
import com.shail.random.util.IOUtils;

public class RandomNumbersFileSorter {
	
	private static Logger LOG = LoggerFactory.getLogger(RandomNumbersFileSorter.class);
	
	public void sort(String input,String output) throws IOException {
		LOG.info("RandomNumbersFileSorter started with param input : "+input+" & output : "+output);

		ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
		String workingDir = System.getProperty("java.io.tmpdir");
		String inputPath=workingDir+"input_file";

		//Copy the input file to working directory
		IOUtils.copy(input, inputPath);

		LOG.info("Running sort job.");
		
		Instant start = Instant.now();
		//Submit Recursive task to pool
		String sortedFile=forkJoinPool.invoke(new SortTask(inputPath,workingDir));
		Instant end = Instant.now();
		LOG.info("Sorting completed. Total time elapsed in sorting : "+Duration.between(start, end).toMillis());
		//Move & rename file to user supplied name 
		IOUtils.move(sortedFile, output);

		LOG.info("Validating results.");
		//Validate if output file is indeed sorted
		LOG.info("Is sorted : "+IOUtils.isSorted(output));
		LOG.info("Is equal : "+IOUtils.isEqual(input,output));
		boolean isInactive=forkJoinPool.awaitQuiescence(10, TimeUnit.SECONDS);
		if(!isInactive)
			LOG.info("Timeout but pool still not inactive exiting !!!");
		LOG.info("RandomNumbersFileSorter execution ended.");
		
	}

}
