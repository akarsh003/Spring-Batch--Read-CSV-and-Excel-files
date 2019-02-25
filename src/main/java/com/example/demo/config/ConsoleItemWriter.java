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
	ControlTableRepo controlTableRepository;
	
	private ControlTable controlTable=new ControlTable();

	
	@Autowired
	ControllerClass controllerClass;
	
	private int numberOfErrors=0;
	
	private String fileName;
	
	List<HashMap<String,Object>> list=new ArrayList<HashMap<String, Object>>();
	
	@Autowired
	private IssueIntakeRepo issueIntakeRepository;
	

	public ConsoleItemWriter() {
		// TODO Auto-generated constructor stub
		
	}

	public ConsoleItemWriter(String pathToDFile) {
		// TODO Auto-generated constructor stub
		fileName=pathToDFile;
	}

	@Override	
    public synchronized void write(List<? extends T> items) throws Exception {
    	 
//		System.out.println(filename);

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
        
		ControlTable File_Details_Object=controlTableRepository.findByfilename(fileName);

        List<com.example.demo.controller.IssueIntakeResponse>  validationResponse=controllerClass.processIssueIntakeListPersist(list,File_Details_Object.getUUID());
        
        for(com.example.demo.controller.IssueIntakeResponse iterator:validationResponse) {
        	
           com.example.demo.controller.Errors[] errorArray=iterator.getErrors();
//        List<com.example.demo.service.intake.IssueIntakeResponse> e= intakeservice.processIssueIntakeListPersist(list);
        
//        for(com.example.demo.service.intake.IssueIntakeResponse s:e) {
//        com.example.demo.service.intake.Errors[] tt=s.getErrors();
		

        
          if(errorArray==null) {
			
        	   continue;
			
           }else {
			
        	   controlTable=controlTableRepository.findByfilename(fileName);
        	   controlTable.setstatus("ERROR");
				controlTableRepository.save(controlTable);
				numberOfErrors++;
//				throw new InvalidOrderItemException("50 Errors");
//				 ct=Control_Table_Repository.findByfilename(filename);
				System.out.println(fileName);
				System.out.println(controlTable.getstatus());
				
				if(controlTable.getstatus().equals("ERROR")) {
				
					System.out.println("Records for this file will be deleted: "+fileName);

					issueIntakeRepository.deleteByUuid(controlTable.getUUID());
				
				}
			
				System.out.println("Error count "+numberOfErrors);
				
				if(numberOfErrors==50) {
					
					
					throw new InvalidOrderItemException("50 Errors Found and stop the batch");	
				
				}
			}
        }
        
	}
}


	


