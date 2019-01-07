package com.example.demo.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
 
public class SamplePartitioner implements Partitioner{
public static List<String> listOfFiles() {
		
        List<String> results = new ArrayList<String>();
        File[] files = new File("E:\\STSWORKSPACE\\BatchCSV\\src\\main\\resources").listFiles();



        for (File file : files) {
            if (file.isFile()) {
            	
            	//Getting absolute path for the file and adding to list
            	String x=file.getAbsolutePath();
            	if(x.contains(".xlsx"))
                results.add(x);
            	
                }
            
        }
	
        return results;
}
    
 
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	List<String> fileNameList = Arrays.asList("Excel.xlsx","Another file.xlsx");
	
	
	
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		
		Map<String, ExecutionContext> partitionData = new HashMap<String, ExecutionContext>();
		
		//
		// In the tutorial: gridSize = 5 for 5 Threads to read 5 files in fileNameList:
		// {"customer-data-1.csv", "customer-data-2.csv", "customer-data-3.csv", "customer-data-4.csv", "customer-data-5.csv"}
		//
		
		for (int i = 0; i < 2/*gridSize*/; i++) {
			ExecutionContext executionContext = new ExecutionContext();
			logger.info("Starting : Thread" );
			logger.info("File Name: " + fileNameList.get(i));
			
			// give fileName for ExecutionContext
			executionContext.putString("filename", fileNameList.get(i));
			// give a thread name for ExecutionContext
			executionContext.putString("name", "Thread" );
			
			partitionData.put("partition: " , executionContext);
		}
		
		return partitionData;
	}
 
}
