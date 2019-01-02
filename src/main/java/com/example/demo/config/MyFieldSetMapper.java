package com.example.demo.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class MyFieldSetMapper implements FieldSetMapper<Map<String,Object>> {
    
	@Override
	public Map<String, Object> mapFieldSet(FieldSet fieldSet) throws BindException {
		
		HashMap<String,Object> s = new HashMap<String,Object>();
	
		String[] columnNames = fieldSet.getNames();
		Arrays.stream(columnNames).forEach(column->{
			s.put(column, fieldSet.readString(column));
		});
		
		return s;
	}
}