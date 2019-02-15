package com.example.demo.Listners;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ChunkListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.BatchPojo.ControlTableRepo;

public class ChunkExecutionListener extends ChunkListenerSupport{
	
	
	private static final Logger logger = LoggerFactory.getLogger(ChunkExecutionListener.class);

	@Override
	public void afterChunk(ChunkContext context) {
//		logger.info("After chunk");
//		super.afterChunk(context);
//		System.out.println("After chunk "+Thread.currentThread().getName());
	}

	@Override
	public void beforeChunk(ChunkContext context) {
//		context.attributeNames();
//		logger.info("Before chunk");

//		System.out.println("chunk started: "+refid);

		
		


	}



}
