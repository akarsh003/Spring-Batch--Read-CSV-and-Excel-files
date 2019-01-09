package com.example.demo.config;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ConsoleItemWriter<T> implements ItemWriter<T> {
	
	RestTemplate restTemplate;
	
	
//	
//    public ConsoleItemWriter(RestTemplate restTemplate) {
//    	restTemplate = new RestTemplate();
//	}



	@Override
    public void write(List<? extends T> items) throws Exception {
    	 
    	    
    	 
    	 
    	 
    	 
        for (T item : items) {
        	
        	//API call 
        	restTemplate = new RestTemplate();
        	System.out.println("Displaying Map:"+ item);
        	System.out.println("Begin /POST request!");
            // replace http://localhost:8080 by your restful services
            String postUrl = "http://localhost:8080/process";
//            Customer customer = new Customer(123, "Jack", 23);
            ResponseEntity<String> postResponse = restTemplate.postForEntity(postUrl, item, String.class);
            System.out.println("Response for Post Request: " + postResponse.getBody());
            
        }
    }
}
