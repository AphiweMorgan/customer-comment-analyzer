package com.ikhokha.techcheck;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		Map<String, Integer> totalResults = new HashMap<>();
				
		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));
		//BlockingQueue<Map<String, Integer>> queue = new ArrayBlockingQueue<Map<String, Integer>>(1);
		//queue.put(totalResults);
		for (File commentFile : commentFiles) {
			new Thread(new ResultConsolidator(commentFile, totalResults)).start();
//			CommentAnalyzer commentAnalyzer = new CommentAnalyzer(commentFile);
//			Map<String, Integer> fileResults = commentAnalyzer.analyze();
//			addReportResults(fileResults, totalResults);
						
		}
		
		System.out.println("RESULTS\n=======");
		totalResults.forEach((k,v) -> System.out.println(k + " : " + v));
	}
	


}

class ResultConsolidator implements Runnable {
	Map<String, Integer> totalResults;
	File commentFile;
	public ResultConsolidator(File commentFile, Map<String, Integer> totalResults){
		this.totalResults = totalResults;
		this.commentFile = commentFile;
	}
	@Override
	public void run() {
		CommentAnalyzer commentAnalyzer = new CommentAnalyzer(commentFile);
		Map<String, Integer> fileResults = commentAnalyzer.analyze();
		synchronized(this) {
//			System.out.println(commentFile.getName());
//			fileResults.forEach((k,v) -> System.out.println(k + " : " + v));
			addReportResults(fileResults, totalResults);
			try {
				Thread.currentThread().join(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * This method adds the result counts from a source map to the target map
	 * @param source the source map
	 * @param target the target map
	 */
	private static void addReportResults(Map<String, Integer> source, Map<String, Integer> target) {
		System.out.println("here");
		for (Map.Entry<String, Integer> entry : source.entrySet()) {
			Integer count = null;
			if((count = target.get(entry.getKey())) != null)
				target.put(entry.getKey(), count + entry.getValue());
			else
				target.put(entry.getKey(), entry.getValue());
		}
		target.forEach((k,v) -> System.out.println(k + " : " + v));

	}
}
