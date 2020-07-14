package com.shail.random.service;

import java.io.IOException;

public interface RandomNumbersService {

	void generateNumbers(final String path) throws IOException;
	void sort(String input,String output) throws IOException;
}
