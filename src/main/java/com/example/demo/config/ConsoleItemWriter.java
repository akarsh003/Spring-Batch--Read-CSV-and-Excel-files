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
	ControlTableRepo Control_Table_Repository;
	
	private ControlTable Control_Table=new ControlTable();

	
	@Autowired
	ControllerClass controllerclass;
	
	int NumberOfErrors=0;
	
	private String filename;
	
	List<HashMap<String,Object>> list=new ArrayList<HashMap<String, Object>>();
	
	@Autowired
	private IssueIntakeRepo IssueIntake_Repository;
	
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
        
		ControlTable File_Details_Object=Control_Table_Repository.findByfilename(filename);

        List<com.example.demo.controller.IssueIntakeResponse>  Validation_Response=controllerclass.processIssueIntakeListPersist(list,File_Details_Object.getUUID());
        
        for(com.example.demo.controller.IssueIntakeResponse s:Validation_Response) {
        	
           com.example.demo.controller.Errors[] Error_Array=s.getErrors();
//        List<com.example.demo.service.intake.IssueIntakeResponse> e= intakeservice.processIssueIntakeListPersist(list);
        
//        for(com.example.demo.service.intake.IssueIntakeResponse s:e) {
//        com.example.demo.service.intake.Errors[] tt=s.getErrors();
		

        
          if(Error_Array==null) {
			
        	   continue;
			
           }else {
			
        	   Control_Table=Control_Table_Repository.findByfilename(filename);
        	   Control_Table.setstatus("ERROR");
				Control_Table_Repository.save(Control_Table);
				NumberOfErrors++;
//				throw new InvalidOrderItemException("50 Errors");
//				 ct=Control_Table_Repository.findByfilename(filename);
				
				System.out.println(filename);
				System.out.println(Control_Table.getstatus());
				
				if(Control_Table.getstatus().equals("ERROR")) {
				
					System.out.println("Records for this file will be deleted: "+filename);

					IssueIntake_Repository.deleteByUuid(Control_Table.getUUID());
				
				}
			
				System.out.println("Error count "+NumberOfErrors);
				
				if(NumberOfErrors==50) {
					
					
					throw new InvalidOrderItemException("50 Errors Found and stop the batch");	
				
				}
			}
        }
        
	}
}


	


