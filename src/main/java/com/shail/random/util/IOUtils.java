package com.shail.random.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class IOUtils {

	public static void splitFile(String inputPath,String outputPath1,String outputPath2) throws IOException {
		int cnt=1;
		try(
				BufferedReader br = new BufferedReader(new FileReader(inputPath));
				PrintWriter pw1 = new PrintWriter(new BufferedWriter(new FileWriter(outputPath1)));
				PrintWriter pw2 = new PrintWriter(new BufferedWriter(new FileWriter(outputPath2)));

				){
			String line;
			while ((line = br.readLine()) != null) {
				switch(cnt++ % 2) {
				case 0:pw1.println(line); break;
				case 1:pw2.println(line); break;	
				}
			}

		}
		new File(inputPath).delete();
	}

	public static long getFileLenght(String fileName) {
		File file=new File(fileName);
		return file.length();
	}

	public static void sortFileInMemory(String fileName) throws IOException {
		Path path=Paths.get(fileName);
		List<Integer> lines=Files.lines(Paths.get(fileName)).map(Integer::parseInt).collect(Collectors.toList());
		Collections.sort(lines);
		List<String> sortedLines=lines.stream().map(i->i.toString()).collect(Collectors.toList());
		Files.write(path, sortedLines);
	}

	public static void mergeSortedFiles(String fileName1,String fileName2,String newFile) throws IOException {
		try(BufferedReader br1 = new BufferedReader(new FileReader(fileName1));
				BufferedReader br2 = new BufferedReader(new FileReader(fileName2));
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(newFile)));){

			String line1=br1.readLine();
			String line2=br2.readLine();
			do {
				int val1=Integer.parseInt(line1);
				int val2=Integer.parseInt(line2);
				if(val1 < val2) {
					pw.println(line1); 
					line1=br1.readLine();
				}
				else {
					pw.println(line2); 
					line2=br2.readLine();
				}
			}while((line1 != null && line2 != null));

			while(line1 != null) {
				pw.println(line1);
				line1 = br1.readLine();
			}

			while(line2 != null) {
				pw.println(line2);
				line2 = br2.readLine();
			}
		}
		new File(fileName1).delete();
		new File(fileName2).delete();
	}

	public static void move(String from,String to) throws IOException {
		Files.move(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
	}

	public static void copy(String from,String to) throws IOException {
		Files.copy(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
	}

	public static boolean isSorted(String fileName) throws IOException {
		int prev=Integer.MIN_VALUE;
		boolean isSorted=true;
		try(BufferedReader br = new BufferedReader(new FileReader(fileName));){
			String line;
			while((line = br.readLine()) != null) {
				int current=Integer.parseInt(line);
				if(prev > current) {
					isSorted=false;
					break;
				}
				prev=current;
			}
		}
		return isSorted;
	}

	public static boolean isEqual(String file1,String file2) throws IOException {
		List<String> lines1=Files.readAllLines(Paths.get(file1));
		List<String> lines2=Files.readAllLines(Paths.get(file2));
		Collections.sort(lines1);
		Collections.sort(lines2);
		return lines1.equals(lines2);
	}

}
