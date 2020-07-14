package com.shail.random.service.impl;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.shail.random.generator.RandomNumberGenerator;
import com.shail.random.service.RandomNumbersService;
import com.shail.random.sort.RandomNumbersFileSorter;

@Service
public class RandomNumberServiceImpl implements RandomNumbersService{

	private static Logger LOG = LoggerFactory.getLogger(RandomNumberServiceImpl.class);

	@Override
	public void generateNumbers(String path) {
		Instant start = Instant.now();
		new RandomNumberGenerator().generateNumbers(path);
		Instant end = Instant.now();
		LOG.info("Total execution time : "+Duration.between(start, end).toMillis());

	}

	@Override
	public void sort(String input,String output) throws IOException {
		Instant start = Instant.now();
		new RandomNumbersFileSorter().sort(input,output);
		Instant end = Instant.now();
		LOG.info("Total execution time : "+Duration.between(start, end).toMillis());

	}

}
