package com.shail.random;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;
import com.shail.random.service.RandomNumbersService;

@SpringBootApplication
public class RandomNumbersApplication implements ApplicationRunner {
	
	private static Logger LOG = LoggerFactory.getLogger(RandomNumbersApplication.class);

	private enum Action{
		SORT,GENERATE
	}
	
	@Autowired
	private RandomNumbersService service;

	public static void main(String[] args) {
		SpringApplication.run(RandomNumbersApplication.class, args);
	}

	private void validateArgs(String args[]) {
		LOG.info("Application arguments - "+Arrays.asList(args).toString());
		Assert.isTrue(args.length > 1, "Incorrect number of parameters.");
		Assert.isTrue(Action.SORT.toString().equals(args[0]) || Action.GENERATE.toString().equals(args[0]),"First parameter should be either SORT or GENERATE");
		Action action=Action.valueOf(args[0]);
		if (action.equals(Action.SORT))
			Assert.isTrue(args.length == 3,"Incorrect number of parameters for SORT action.");
		if (action.equals(Action.GENERATE))
			Assert.isTrue(args.length == 2,"Incorrect number of parameters for GENERATE action.");
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		String[] params= args.getSourceArgs();
		validateArgs(params);
		Action action=Action.valueOf(params[0]);
		if (action.equals(Action.SORT))
			service.sort(params[1], params[2]);
		else
			service.generateNumbers(params[1]);
		
	}

}
