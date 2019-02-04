package com.example.demo.config;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.example.demo.Listners.ChunkExecutionListener;
import com.example.demo.Listners.JobCompletionNotificationListener;
import com.example.demo.Listners.StepExecutionNotificationListener;

@Configuration
@EnableBatchProcessing

public class BatchConfig {

	public final static Logger logger = LoggerFactory.getLogger(BatchConfig.class);

	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	
/*	@Value("*.csv")
	private Resource[] inputResourcescsv;*/
	

	@Bean
	public JobCompletionNotificationListener jobExecutionListener() {
		return new JobCompletionNotificationListener();
	}
	
	@Bean
	public StepExecutionNotificationListener stepExecutionListener() {
		return new StepExecutionNotificationListener();
	}
	
	@Bean
	public ChunkExecutionListener chunkListener() {
		return new ChunkExecutionListener();
	}

	@Bean
	public TaskExecutor MultiTaskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(10);
		return taskExecutor;
	}
	
	
	@Bean
	@StepScope
   public FlatFileItemReader<Map<String, Object>> csvReader(@Value("#{stepExecutionContext['fileName']}") String filename) throws MalformedURLException {
		
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
        reader.setResource( new UrlResource(filename));
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
//	            .start(step1csv())//Calling CSV reading
	            .start(partitionStepcsv())//	            .next(step2excel())
	            .next(partitionStepexcel())//Calling excel reading
	            .listener(jobExecutionListener())
//	            .next(slaveStep())
	            .build());
				
			}
			


	
	
//Masterstep Excel	
	@Bean
    public Step partitionStepexcel() throws UnexpectedInputException, ParseException, IOException, URISyntaxException {
		
		
        return stepBuilderFactory.get("partitionStepexcel")
          .partitioner("slaveStepexcel", partitionerexcel())
          .step(slaveStepexcel())
          .taskExecutor(taskExecutor())
          .build();
		
    }
	
	//Masterstep CSV
	@Bean
    public Step partitionStepcsv() throws UnexpectedInputException, ParseException, IOException, URISyntaxException {
		
		
        return stepBuilderFactory.get("partitionStepcsv")
          .partitioner("slaveStepcsv", partitionercsv())
          .step(slaveStepcsv())
          .taskExecutor(taskExecutor())
          .listener(stepExecutionListener())
          .build();
		
    }
	
   @Bean
	public Step slaveStepexcel() throws UnexpectedInputException, ParseException, IOException, URISyntaxException {
	    	
	    	
	        return stepBuilderFactory.get("slaveStepexcel")
	          .<Map<String,Object>, Map<String,Object>>chunk(100)
	          .reader(excelreaderpll(null))
	          .writer(writer())
	          .build();
	    }
	
   @Bean
  	public Step slaveStepcsv() throws UnexpectedInputException, ParseException, IOException, URISyntaxException {
  	    	
  	    	
  	        return stepBuilderFactory.get("slaveStepcsv")
  	          .<Map<String,Object>, Map<String,Object>>chunk(5000)
  	          .reader(csvReader(null))
  	          .writer(writer())
  	          .taskExecutor(MultiTaskExecutor())
  	          .listener(chunkListener())
  	          .throttleLimit(20)
  	          .build();
  	    }
	
	
	

   @Bean
    public MultiResourcePartitioner partitionerexcel() throws IOException {
    	
    	 MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
    	 
    	 PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    	 
    	 partitioner.setResources(resolver.getResources("*.xlsx"));
    	 
    	 return partitioner;

    }
   
   @Bean
   public MultiResourcePartitioner partitionercsv() throws IOException {
   	
   	 MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
   	 
   	 PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
   	 
   	 partitioner.setResources(resolver.getResources("*.csv"));
   	 
   	 return partitioner;

   }
    
	
	@Bean
    public TaskExecutor taskExecutor() {
		
		
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.afterPropertiesSet();
        taskExecutor.setThreadNamePrefix("threadPoolExecutor-");
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
	
    
    //Approach to save and rollback
    
  /*  @Bean
    public JdbcBatchItemWriter<Map<String, Object>> writertodb() {
    	
        JdbcBatchItemWriter<Map<String, Object>> itemWriter = new JdbcBatchItemWriter<Map<String, Object>>();
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Map<String, Object>>());
        //Call saving code of Validation guys
        itemWriter.setSql("INSERT INTO Issue_Intake(ID,MAPPEDJSON) VALUES (:String, :Object)");
        itemWriter.setDataSource(dataSource);
        return itemWriter;
        
    }*/
    
  /*  @Bean
    public DataSource dataSource(){
    	
    	final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    	  dataSource.setDriverClassName("org.h2.Driver");
    	  dataSource.setUrl("jdbc:h2:~/test");
    	  dataSource.setUsername("sa");
    	  dataSource.setPassword("");
    	  return dataSource;
    }*/
    
  /* @Bean
    public ItemProc<Map<String,Object>> proc()
    {
    	 return new ItemProc<Map<String,Object>>();
    }*/
    
}
	
