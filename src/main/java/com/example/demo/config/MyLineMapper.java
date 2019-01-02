package com.example.demo.config;

import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.AbstractLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;

public class MyLineMapper<T> extends DefaultLineMapper<T> {
private AbstractLineTokenizer myTokenizer;

	 @Override
		public T mapLine(String line, int lineNumber) throws Exception {
		 if(lineNumber==1) {
			FieldSet columnNames = myTokenizer.tokenize(line);
			myTokenizer.setNames(columnNames.getValues());
			return null;
		 }
		 return super.mapLine(line, lineNumber);
		}
	 @Override
	 public void setLineTokenizer(LineTokenizer tokenizer) {
			this.myTokenizer = (AbstractLineTokenizer)tokenizer;
		}
}
