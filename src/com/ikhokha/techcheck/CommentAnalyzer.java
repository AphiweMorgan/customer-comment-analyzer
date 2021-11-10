package com.ikhokha.techcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommentAnalyzer {
	
	private File file;
	private Map<String, String> metricMap = new HashMap<>();
	public CommentAnalyzer(File file) {
		this.file = file;
	}
	private void populateMetrics() throws IOException {
        File metricsFile = new File("docs/metrics/metrics.txt");
        BufferedReader reader = new BufferedReader(new FileReader(metricsFile));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] metric = line.split(":");
            metricMap.put(metric[0], metric[1]);
        }
    }
	public Map<String, Integer> analyze() {
		
		Map<String, Integer> resultsMap = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			populateMetrics();
            Set<String> metricKeys = metricMap.keySet();

			String line = null;
			while ((line = reader.readLine()) != null) {
				
				if (line.length() < 15) {
					
					incOccurrence(resultsMap, "SHORTER_THAN_15");

				}
                for(String k: metricKeys){
                    String desc = metricMap.get(k);
                    if(line.toUpperCase().contains(k.toUpperCase())){
                        incOccurrence(resultsMap, desc);
                    }
                }
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Error processing file: " + file.getAbsolutePath());
			e.printStackTrace();
		}
		
		return resultsMap;
		
	}
	
	/**
	 * This method increments a counter by 1 for a match type on the countMap. Uninitialized keys will be set to 1
	 * @param countMap the map that keeps track of counts
	 * @param key the key for the value to increment
	 */
	private void incOccurrence(Map<String, Integer> countMap, String key) {
		
		countMap.putIfAbsent(key, 0);
		countMap.put(key, countMap.get(key) + 1);
	}

}
