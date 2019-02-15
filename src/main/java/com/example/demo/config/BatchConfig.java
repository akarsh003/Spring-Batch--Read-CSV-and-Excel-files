package com.example.demo.config;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.example.demo.BatchPojo.ControlTable;
import com.example.demo.BatchPojo.ControlTableRepo;
import com.example.demo.Listners.ChunkExecutionListener;
import com.example.demo.Listners.ItemReaderExecutionListener;
import com.example.demo.Listners.ItemWriterExecutionListner;
import com.example.demo.Listners.JobCompletionNotificationListener;
import com.example.demo.Listners.StepExecutionNotificationListener;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	public final static Logger logger = LoggerFactory.getLogger(BatchConfig.class);

	private java.util.UUID uuid=null;

	private ControlTable ct=new ControlTable();

	@Autowired
	private ControlTableRepo ctrepo;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;


	@Bean
	public JobCompletionNotificationListener JobExecutionListener() {
		
		return new JobCompletionNotificationListener();
		
	}
	  
	@Bean
	@StepScope
	public StepExecutionNotificationListener StepExecutionListener(@Value("#{stepExecutionContext['fileName']}") String filename) {

		
		return new StepExecutionNotificationListener(/*filename*/);
		
	}
	
	@Bean
	public ChunkExecutionListener ChunkListener() {
		return new ChunkExecutionListener();
	}
	
	@Bean
	public ItemReaderExecutionListener ItemReaderListener() {
		return new ItemReaderExecutionListener();
	}
	
	@Bean
	@StepScope
	public ItemWriterExecutionListner ItemWriterListener(@Value("#{stepExecutionContext['fileName']}") String filename) {
		
		return new ItemWriterExecutionListner(/*filename*/);
	}
	
	
	//Batch Job
	@Bean
	public Job ReadCommonFormatFilesJob() throws IOException, URISyntaxException {
		
			return (  jobBuilderFactory
	            .get("ReadCommonFormatFilesJob")
	            .incrementer(new RunIdIncrementer())
	            .start(MasterStepCsv())//CSV MasterStep call
	            .next(MasterStepExcel())//Excel MasterStep Call
	            .build());
				
	}
			

	//Master Step for  CSV
	@Bean
    public Step MasterStepCsv() throws UnexpectedInputException, ParseException, IOException, URISyntaxException {
		
		
        return stepBuilderFactory.get("MasterStepCsv")
          .partitioner("slaveStepcsv", PartitionerCsv())  
          .step(SlaveStepCsv())
          .taskExecutor(TaskExecutor())
//	          .listener(StepExecutionListener(null))
          .build();
		
    }
	
	//Slave step
	@Bean
	public Step SlaveStepCsv() throws UnexpectedInputException, ParseException, IOException, URISyntaxException {
	  	    	
	  	    	
	  	        return stepBuilderFactory.get("SlaveStepCsv")
	  	          .<Map<String,Object>, Map<String,Object>>chunk(100)
	  	          .reader(CsvReader(null))
	  	          .listener(ItemReaderListener())
//	  	          .processor(proc(null))
	  	          .writer(Writer(null))  
	  	          .listener(ItemWriterListener(null))
	  	          .taskExecutor(MultiTaskExecutor())
//	  	          .listener(ChunkListener())
	  	          .listener(StepExecutionListener(null))

	  	          .build();
	  	        
	 }
	
	
	//Master Step for Excel
	 @Bean
	 public Step MasterStepExcel() throws UnexpectedInputException, ParseException, IOException, URISyntaxException {
			
			
	        return stepBuilderFactory.get("MasterStepExcel")
	          .partitioner("slaveStepexcel", PartitionerExcel())
	          .step(SlaveStepExcel())
	          .taskExecutor(TaskExecutor())
	          .build();
			
	  }
  
	//Slave step for Excel
    @Bean
	public Step SlaveStepExcel() throws UnexpectedInputException, ParseException, IOException, URISyntaxException {
	    	
	    	
	        return stepBuilderFactory.get("SlaveStepExcel")
	          .<Map<String,Object>, Map<String,Object>>chunk(100)
	          .reader(ExcelReader(null))
	          .listener(ItemReaderListener())
//  	          .processor(proc(null))
  	          .writer(Writer(null))  
  	          .listener(ItemWriterListener(null))
  	          .taskExecutor(MultiTaskExecutor())
//  	          .listener(ChunkListener())
  	          .listener(StepExecutionListener(null))

  	          .build();
	}
   
   
   @Bean
   public MultiResourcePartitioner PartitionerCsv() throws IOException {
   	
   	 MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
   	 
   	 PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
   	 
   	 partitioner.setResources(resolver.getResources("*.csv"));
   	 
   	 return partitioner;

   }
    
   @Bean
   public MultiResourcePartitioner PartitionerExcel() throws IOException {
   	
   	 MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
   	 
   	 PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
   	 
   	 partitioner.setResources(resolver.getResources("*.xlsx"));
   	 
   	 return partitioner;

   }
   
   @Bean
	public TaskExecutor MultiTaskExecutor() {
	   
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(10);
		return taskExecutor;
		
	}
    
	@Bean
    public TaskExecutor TaskExecutor() {
		
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
        
    }

	
	//CSV Reading using FlatFile ItemReader
	@Bean
	@StepScope
   public FlatFileItemReader<Map<String, Object>> CsvReader(@Value("#{stepExecutionContext['fileName']}") String filename) throws MalformedURLException {
		

		uuid=UUID.randomUUID();
		UUID refid=uuid;
//		System.out.println("File started: "+refid+"\nThread: "+Thread.currentThread().getName()+"\n "+filename);
		
		ct.setUUID(refid);
		ct.setFilename(filename);
		ct.setstatus("Reading Started: ");
		ctrepo.save(ct);
		
        FlatFileItemReader<Map<String, Object>> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        LineCallbackHandler skippedLinesCallback = new LineCallbackHandler() {
        	@Override
			public void handleLine(String line) {
        		String[] columnNames = tokenizer.tokenize(line).getValues();
        		for(String nam:columnNames) {
        		}
        		tokenizer.setNames(columnNames);
        	}
        };
		reader.setSkippedLinesCallback(skippedLinesCallback );
        reader.setResource( new UrlResource(filename));
        DefaultLineMapper<Map<String, Object>> mapper = CsvRowMapper(tokenizer);
        reader.setLineMapper(mapper);
        reader.setStrict(true);
        
//		System.out.println("File read done: "+refid+"\nThread: "+Thread.currentThread().getName());
		ct.setstatus("Read Complete and yet to be written");
		ctrepo.save(ct);
        
        return reader;
    }
	
	//Excel reading using POI ItemReader
	@Bean
	@StepScope
	public PoiItemReader<Map<String, Object>> ExcelReader(@Value("#{stepExecutionContext['fileName']}") String filename) throws IOException, URISyntaxException{
		
			PoiItemReader<Map<String, Object>> reader = new PoiItemReader<>();
			reader.setLinesToSkip(1);	
	         reader.setResource(new UrlResource(filename));
			org.springframework.batch.item.excel.RowMapper<Map<String, Object>> mapper = ExcelRowMapper();
	        reader.setRowMapper(mapper);
	        reader.setStrict(false);
	        return reader;
	        
	}
	   
	
	@Bean
	@StepScope
    public ConsoleItemWriter<Map<String,Object>> Writer(@Value("#{stepExecutionContext['fileName']}") String pathToDFile)
    {
		
//		System.out.println("File writing started:"+filename+"\n Thread:"+Thread.currentThread().getName());
//		ControlTable ud=ctrepo.findByfilename(filename);
//		UUID udd=ud.getUUID();
//		System.out.println("File under write uuid:" +ud.getUUID() );
//
//		ud.setstatus("Started Writing");
//		ctrepo.save(ud);
//		System.out.println(pathToDFile);
		
        return new ConsoleItemWriter<Map<String,Object>>(pathToDFile);
        
        
    }

	
	/*@Bean
	@StepScope
    public ItemProc<Map<String,Object>> proc(@Value("#{stepExecutionContext['fileName']}") String filename)
    {
		
		System.out.println("File process started:"+filename+" Thread:"+Thread.currentThread().getName()+" "+uuid);
		ControlTable ud=ctrepo.findByfilename(filename);
//		UUID udd=ud.getUUID();
		System.out.println("File process uuid:" +ud.getUUID() );

		ud.setstatus("Started");
		ctrepo.save(ud);
    	return new ItemProc<Map<String,Object>>();
    	
    }*/

	
	
	private DefaultLineMapper<Map<String, Object>> CsvRowMapper(LineTokenizer tokenizer) {
		
		DefaultLineMapper<Map<String,Object>> obj = new DefaultLineMapper<>();
		FieldSetMapper<Map<String, Object>> fieldSetMapper = new MyFieldSetMapper();
		obj.setLineTokenizer(tokenizer);
		obj.setFieldSetMapper(fieldSetMapper);
       return obj;
       
   }
	
	private org.springframework.batch.item.excel.RowMapper<Map<String, Object>> ExcelRowMapper() {
			
		 MyFieldSetMapper obj = new MyFieldSetMapper();
		 return obj;
	}
	

    
}
	
