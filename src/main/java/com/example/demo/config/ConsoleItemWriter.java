package com.example.demo.config;

import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//import javax.transaction.Transactional;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.BatchPojo.ControlTable;
import com.example.demo.BatchPojo.ControlTableRepo;
import com.example.demo.controller.ControllerClass;
import com.example.demo.repos.IssueIntakeRepo;


@Service
public class ConsoleItemWriter<T> implements ItemWriter<T> {
	
		
	@Autowired
	ControlTableRepo ctrepo;
	
	private ControlTable ct=new ControlTable();

	
	@Autowired
	ControllerClass controllerclass;
	
	int counterrors=0;
	
	private String filename;
	
	List<HashMap<String,Object>> list=new ArrayList<HashMap<String, Object>>();
	
	@Autowired
	private IssueIntakeRepo itrepo;
	
	Object object=new Object();



	public ConsoleItemWriter() {
		// TODO Auto-generated constructor stub
		
	}

	public ConsoleItemWriter(String pathToDFile) {
		// TODO Auto-generated constructor stub
		filename=pathToDFile;
	}

	@Override	
    public synchronized void write(List<? extends T> items) throws Exception {
    	 
		System.out.println(filename);

//		synchronized(object){
        for (T item : items) {
        	
        	//API call 
        	
        	System.out.println("Displaying Map:"+ item+"\n Thread:"+Thread.currentThread().getName());
//        	list.add((HashMap<String, Object>) item);
        	
        	HashMap<String,Object> item1=(HashMap<String, Object>)item;
//        	item1.put("WorkRequestType","TinDuplication");
        	list.add((HashMap<String,Object>)item1);
 
        
//            com.example.demo.controller.IssueIntakeResponse e=controllerclass.processIssueIntakePersist(item1);


        }
	//    	restTemplate = new RestTemplate();
//    	String postUrl = "http://localhost:8080/process";
//    	ResponseEntity<String> postResponse = restTemplate.postForEntity(postUrl, list, String.class);
        
		ControlTable ud=ctrepo.findByfilename(filename);

        List<com.example.demo.controller.IssueIntakeResponse> e=controllerclass.processIssueIntakeListPersist(list,ud.getUUID());
        
        for(com.example.demo.controller.IssueIntakeResponse s:e) {
        	
           com.example.demo.controller.Errors[] tt=s.getErrors();
//        List<com.example.demo.service.intake.IssueIntakeResponse> e= intakeservice.processIssueIntakeListPersist(list);
        
//        for(com.example.demo.service.intake.IssueIntakeResponse s:e) {
//        com.example.demo.service.intake.Errors[] tt=s.getErrors();
		

        
          if(tt==null) {
			
        	   continue;
			
           }else {
			
				ct=ctrepo.findByfilename(filename);
				ct.setstatus("ERROR");
				ctrepo.save(ct);
				counterrors++;
//				throw new InvalidOrderItemException("50 Errors");
//				 ct=ctrepo.findByfilename(filename);
				
				System.out.println(filename);
				System.out.println(ct.getstatus());
				
				if(ct.getstatus().equals("ERROR")) {
				
					System.out.println("Records for this file will be deleted");

					itrepo.deleteByUuid(ct.getUUID());
				
					System.out.println("Success in deletion");
				}
			
				System.out.println("Error count "+counterrors);
				
				if(counterrors==50) {
					
					
					throw new InvalidOrderItemException("50 Errors Found");	
				
				}
			}
        }
        
	}
}


	


