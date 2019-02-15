package com.example.demo.Listners;

import java.util.UUID;

import org.springframework.batch.core.ItemReadListener;

public class ItemReaderExecutionListener implements ItemReadListener<Object>{


	
	@Override
	public void beforeRead() {
		// TODO Auto-generated method stub
//		System.out.println("Before read "+Thread.currentThread().getName());
		
	}

	@Override
	public void afterRead(Object item) {
		// TODO Auto-generated method stub
		

	}

	@Override
	public void onReadError(Exception ex) {
		// TODO Auto-generated method stub
		
	}


}
