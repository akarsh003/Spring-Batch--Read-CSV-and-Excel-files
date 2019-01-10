package com.example.demo.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ConsoleItemWriter<T> implements ItemWriter<T> {
	
	RestTemplate restTemplate;
	
	List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
	
	
	


	@Override
    public void write(List<? extends T> items) throws Exception {
    	 
 
    	 
        for (T item : items) {
        	
        	//API call 
//        	restTemplate = new RestTemplate();
        	System.out.println("Displaying Map:"+ item);
        	list.add((Map<String, Object>) item);
//        	System.out.println("Begin /POST request!");
//            // replace http://localhost:8080 by your restful services
//            String postUrl = "http://localhost:8080/process";
////            Customer customer = new Customer(123, "Jack", 23);
//            ResponseEntity<String> postResponse = restTemplate.postForEntity(postUrl, item, String.class);
//            System.out.println("Response for Post Request: " + postResponse.getBody());
            
        }
    	System.out.println("Displaying full File");
    	for(Map<String, Object> x:list) {
        	System.out.println(x);

    	}
    	//Calling Validation API for List of hashmaps
    	restTemplate = new RestTemplate();
    	String postUrl = "http://localhost:8080/process";
    	ResponseEntity<String> postResponse = restTemplate.postForEntity(postUrl, list, String.class);
    	System.out.println("Response for Post Request: " + postResponse.getBody());
        
    }
}
