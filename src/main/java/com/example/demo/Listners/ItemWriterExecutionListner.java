package com.example.demo.Listners;

import java.util.List;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.BatchPojo.ControlTable;
import com.example.demo.BatchPojo.ControlTableRepo;

public class ItemWriterExecutionListner implements ItemWriteListener<Object> {

	
	private ControlTable controlTable=new ControlTable();
	private String fileName;
	
	public ItemWriterExecutionListner(String fileName) {
//		super();
		this.fileName = fileName;
	}

	@Autowired
	private ControlTableRepo controlTableRepository;
	
	

	@Override
	public void beforeWrite(List<? extends Object> items) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterWrite(List<? extends Object> items) {
		// TODO Auto-generated method stub
		
 	   controlTable=controlTableRepository.findByfilename(fileName);
 	   
 	   if(controlTable.getstatus().equals("ERROR")) {
 		   //Do not change status
 	   }else {
 		   controlTable.setstatus("Write Complete");
 		   controlTableRepository.save(controlTable);
 	   }
		
		
	}

	@Override
	public void onWriteError(Exception exception, List<? extends Object> items) {
		// TODO Auto-generated method stub
		
	}

}
