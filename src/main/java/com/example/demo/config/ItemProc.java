package com.example.demo.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.controller.ControllerClass;
import com.example.demo.controller.IssueIntakeResponse;

//import com.example.demo.model.Employee;
@Transactional
public class ItemProc<T> implements ItemProcessor<T,T> {
	
	int i=0;
	int counterrors=0;
	
	List<HashMap<String,Object>> list=new ArrayList<HashMap<String, Object>>();
	

	@Autowired
	private ControllerClass controllerClass;
	



	@Override
	@Transactional(rollbackFor = InvalidOrderItemException.class)
	public T process(T item) throws Exception {
		// TODO Auto-generated method stub
	
	        	
	        	//API call 

	        	HashMap<String,Object> item1=(HashMap<String, Object>)item;
	        	item1.put("WorkRequestType","TinDuplication");
	        	list.add((HashMap<String,Object>)item1);
	        

//	    	Calling Validation API for List of hashmaps

	    	System.out.println("Calling Validation in processor "+i);
	    	i++;
//	    	restTemplate = new RestTemplate();
//	    	String postUrl = "http://localhost:8080/processList";
//	    	ResponseEntity<String> postResponse = restTemplate.postForEntity(postUrl, list, String.class);
//	    	ControllerClass c=new ControllerClass();
//	    	controllerClass.processIssueIntakeList(list);
	    	IssueIntakeResponse e= controllerClass.processIssueIntake(item1);
//	    	IssueIntakeResponse er=e.get(0);
//	    	System.out.println(e.get(0).getErrors()[0].getAttributeName());
//	    	System.out.println(e.size());
	    	

	    	//for(IssueIntakeResponse s:e) {
	    		
	    		if(e.getErrors()[0].equals(null))
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
	    	
//	System.out.println("Response for Post Request: " + postResponse.getBody());
		return item;
	}

	
	
} 
