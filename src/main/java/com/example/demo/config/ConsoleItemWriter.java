package com.example.demo.config;

import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import javax.transaction.Transactional;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.controller.ControllerClass;
import com.example.demo.controller.IssueIntakeResponse;

@Service
//@Transactional
public class ConsoleItemWriter<T> implements ItemWriter<T> {
	
	RestTemplate restTemplate;
	
	List<HashMap<String,Object>> list=new ArrayList<HashMap<String, Object>>();
	
	int i=0;
	int counterrors=0;

	@Autowired
	private ControllerClass controllerClass;
	

	@Override
//	@Transactional(rollbackFor = InvalidOrderItemException.class)
    public void write(List<? extends T> items) throws Exception {
    	 
 
    	 
        for (T item : items) {
        	
        	//API call 
//        	restTemplate = new RestTemplate();
//        	System.out.println("Displaying Map:"+ item);
//        	list.add((HashMap<String, Object>) item);
//        	System.out.println("Displaying Thread Name:"+ Thread.currentThread().getName());

        	HashMap<String,Object> item1=(HashMap<String, Object>)item;
        	item1.put("WorkRequestType","TinDuplication");
        	list.add((HashMap<String,Object>)item1);
 //        	System.out.println("Begin /POST request!");
//            // replace http://localhost:8080 by your restful ces
//            String postUrl = "http://localhost:8080/process";
////            Customer customer = new Customer(123, "Jack", 23);
//            ResponseEntity<String> postResponse = restTemplate.postForEntity(postUrl, item, String.class);
//            System.out.println("Response for Post Request: " + postResponse.getBody());
            
        }
        System.out.println("Displaying Thread Name:"+ Thread.currentThread().getName());

//		System.out.println("CSV reader started time:"+System.currentTimeMillis());

//    	System.out.println("Displaying full File");
//    	for(Map<String, Object> x:list) {
//    		
//        	System.out.println(x);
//
//    	}
//    	Calling Validation API for List of hashmaps
 
//    	restTemplate = new RestTemplate();
//    	String postUrl = "http://localhost:8080/process";
//    	ResponseEntity<String> postResponse = restTemplate.postForEntity(postUrl, list, String.class);
        
    	System.out.println("Calling Validation in consoleitem writer"+i);
    	i++;
//    	restTemplate = new RestTemplate();
//    	String postUrl = "http://localhost:8080/processList";
//    	ResponseEntity<String> postResponse = restTemplate.postForEntity(postUrl, list, String.class);
//    	ControllerClass c=new ControllerClass();
    	controllerClass.processIssueIntakeList(list);
//        List<IssueIntakeResponse> e= controllerClass.processIssueIntakeList(list);
    	/*for(IssueIntakeResponse s:e) {
    		
    	if(s.getErrors()[0].equals(null))
    	{
    		System.out.println("no error");
    		
    	}else
    	{
    		System.out.println("error "+counterrors++);;
    	}
    	
    	if(counterrors>=50) {
    		//Throw exception if 50 errors is reached
    		System.out.println("50 Erros found");
    		counterrors=0;
    		throw new InvalidOrderItemException("50 Errors");
    	}
    	
    	
    	}*/
//    	System.out.println("Response for Post Request: " + postResponse.getBody());
        
	}
}
