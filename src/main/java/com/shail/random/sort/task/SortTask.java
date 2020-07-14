package com.shail.random.sort.task;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.shail.random.util.IOUtils;

/**
 * Recursive task to merge sort which will split files
 * until it is less then threshold & small enough to be loaded in memory for sorting.
 * while coming out of recursion merge the sorted files into final output.
 * @author shail
 *
 */
public class SortTask extends RecursiveTask<String>{
	
	private static Logger LOG = LoggerFactory.getLogger(SortTask.class);

	private static final long serialVersionUID = 3695240139858903004L;
	
	/**
	 * Size of file which can be considered safe to be loaded into memory for in memory sorting
	 */
	private static final long THRESHOLD=5000000; //~5mb

	private final String inputPath;
	private final String workingDir;

	public SortTask(String inputPath,String workingDir) {
		this.inputPath=inputPath;
		this.workingDir=workingDir;
	}
	
	/**
	 * Method to execute logic to sort file
	 * @return path of sorted file
	 */
	@Override
	protected String compute() {
		try {
			//Check if file size greater then threshold
			if(IOUtils.getFileLenght(inputPath) > THRESHOLD) {
				String path=getUniqueFilePath();
				String path1=path+"_1";
				String path2=path+"_2";
				//Split the files into two files
				IOUtils.splitFile(inputPath,path1,path2);

				//Submit tasks
				ForkJoinTask<String> ft1=new SortTask(path1,this.workingDir).fork();
				ForkJoinTask<String> ft2=new SortTask(path2,this.workingDir).fork();
				path1=ft1.join();
				path2=ft2.join();

				//Merged the sorted files
				IOUtils.mergeSortedFiles(path1,path2,path);
				return path;
			}else {
				IOUtils.sortFileInMemory(inputPath);
				return inputPath;
			}
		}catch(IOException io) {
			LOG.error("Exception in sorting.",io);
			throw new RuntimeException(io);
		}
	}

	private String getUniqueFilePath() {
		return workingDir+Calendar.getInstance().getTimeInMillis()+"-"+Thread.currentThread().getId();
	}


}
