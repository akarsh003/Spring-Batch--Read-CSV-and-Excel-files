package com.example.demo.config;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Value("*.xlsx")
	private Resource[] inputResourcesexcel;

	
	@Value("*.csv")
	private Resource[] inputResourcescsv;
	
	
	
//CSV file reading
	@Bean
    FlatFileItemReader<Map<String, Object>> csvReader() {
		
        FlatFileItemReader<Map<String, Object>> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        LineCallbackHandler skippedLinesCallback = new LineCallbackHandler() {
        	@Override
			public void handleLine(String line) {
        		String[] columnNames = tokenizer.tokenize(line).getValues();
        		for(String nam:columnNames) {
        			System.out.println(nam);
        		}
        		tokenizer.setNames(columnNames);
        	}
        };
		reader.setSkippedLinesCallback(skippedLinesCallback );
//        reader.setResource(new ClassPathResource(inputResourcescsvstring));
        DefaultLineMapper<Map<String, Object>> mapper = csvRowMapper(tokenizer);
        reader.setLineMapper(mapper);
        reader.setStrict(true);
        return reader;
    }
	

	@Bean
	public Job readCSVFilesJob() throws IOException, URISyntaxException {
		
			return (  jobBuilderFactory
	            .get("readCSVFilesJob")
	            .incrementer(new RunIdIncrementer())
	            .start(step1csv())
//	            .next(step2excel())
	            .next(partitionStep())
	            .build());
				
			}
			

	@Bean
	public Step step1csv() {
		
	    return stepBuilderFactory.get("step1csv").<Map<String,Object>, Map<String,Object>>chunk(5)
	            .reader(multiResourceItemReader())
	            .writer(writer())
	            .build();
	}
	
	
	
	@Bean
    public Step partitionStep() throws UnexpectedInputException, ParseException, IOException, URISyntaxException {
		
		
        return stepBuilderFactory.get("partitionStep")
          .partitioner("slaveStep", partitioner())
          .step(slaveStep())
          .taskExecutor(taskExecutor())
          .build();
		
    }
	
	  @Bean
	    public Step slaveStep() throws UnexpectedInputException, ParseException, IOException, URISyntaxException {
	    	
	    	
	        return stepBuilderFactory.get("slaveStep")
	          .<Map<String,Object>, Map<String,Object>>chunk(1)
	          .reader(excelreaderpll(null))
	          .writer(writer())
	          .build();
	    }
	

	
	@Bean
	public MultiResourceItemReader<Map<String,Object>> multiResourceItemReader()
	{
	    MultiResourceItemReader<Map<String,Object>> resourceItemReadercsv= new MultiResourceItemReader<Map<String,Object>>();
	    resourceItemReadercsv.setResources(inputResourcescsv);

	    resourceItemReadercsv.setDelegate(csvReader());

	    return resourceItemReadercsv;
	}
	

    @Bean
    public MultiResourcePartitioner partitioner() throws IOException {
    	
    	 MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
    	 
    	 PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    	 
    	 partitioner.setResources(resolver.getResources("*.xlsx"));
    	 
    	 return partitioner;

    }
    
	
	@Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setQueueCapacity(5);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

	@Bean
    public ConsoleItemWriter<Map<String,Object>> writer()
    {
        return new ConsoleItemWriter<Map<String,Object>>();
    }
	
	
	private org.springframework.batch.item.excel.RowMapper<Map<String, Object>> excelRowMapper() {
		
		 MyFieldSetMapper obj = new MyFieldSetMapper();
		 return obj;
	}

	private DefaultLineMapper<Map<String, Object>> csvRowMapper(LineTokenizer tokenizer) {
		
		DefaultLineMapper<Map<String,Object>> obj = new DefaultLineMapper<>();
		FieldSetMapper<Map<String, Object>> fieldSetMapper = new MyFieldSetMapper();
		obj.setLineTokenizer(tokenizer);
		obj.setFieldSetMapper(fieldSetMapper);
       return obj;
       
   }
	

 //Excel Reading part   
    @Bean
    @StepScope
    public PoiItemReader<Map<String, Object>> excelreaderpll(@Value("#{stepExecutionContext['fileName']}") String filename) throws IOException, URISyntaxException{
    	 	
		PoiItemReader<Map<String, Object>> reader = new PoiItemReader<>();
		reader.setLinesToSkip(1);	
         reader.setResource(new UrlResource(filename));
		org.springframework.batch.item.excel.RowMapper<Map<String, Object>> mapper = excelRowMapper();
        reader.setRowMapper(mapper);
        reader.setStrict(false);
        return reader;
        
    }
	
    
  
}
	
