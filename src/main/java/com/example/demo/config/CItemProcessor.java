package com.example.demo.config;

import java.util.Map;

import org.springframework.batch.item.ItemProcessor;

//import com.example.demo.model.Employee;

public class CItemProcessor implements ItemProcessor<Map<String,Object>, Map<String,Object>> {

 @Override
 public Map<String,Object> process(Map<String,Object> employee) throws Exception {
  
	 System.out.println("Displaying map : " + employee);
     return employee;
 }

} 
