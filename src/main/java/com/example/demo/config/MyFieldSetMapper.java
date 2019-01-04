package com.example.demo.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class MyFieldSetMapper implements FieldSetMapper<Map<String,Object>>, RowMapper<Map<String,Object>> {
    
	@Override
	public Map<String, Object> mapFieldSet(FieldSet fieldSet) throws BindException {
		
		HashMap<String,Object> s = new HashMap<String,Object>();
	
		String[] columnNames = fieldSet.getNames();
		Arrays.stream(columnNames).forEach(column->{
			s.put(column, fieldSet.readString(column));
		});
		
		return s;
	}

	@Override
	public Map<String, Object> mapRow(RowSet rs) throws Exception {
		HashMap<String,Object> s = new HashMap<String,Object>();
		
		String[] columnNames = rs.getMetaData().getColumnNames();
		String[] columnValues = rs.getCurrentRow();
		if(columnNames.length != columnValues.length) {
			throw new Exception("Columns Values length and column names length not matching");
		}
		
		for(int i=0;i<columnNames.length;i++) {
			s.put(columnNames[i], columnValues[i]);
//			System.out.println(columnNames[i]+columnValues[i]);
		}
		
		return s;

	}
}