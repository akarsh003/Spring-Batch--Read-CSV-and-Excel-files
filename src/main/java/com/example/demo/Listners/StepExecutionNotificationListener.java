package com.example.demo.Listners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.BatchPojo.ControlTable;
import com.example.demo.BatchPojo.ControlTableRepo;
import com.example.demo.model.Issue_Intake;
import com.example.demo.repos.IssueIntakeRepo;

public class StepExecutionNotificationListener extends StepExecutionListenerSupport{
	
	

	

	private static final Logger logger = LoggerFactory.getLogger(StepExecutionNotificationListener.class);

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		
		return super.afterStep(stepExecution);
		
		
		
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {

		
	}
	

	
}
