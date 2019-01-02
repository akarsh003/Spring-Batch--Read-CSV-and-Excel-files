package com.example.demo.config;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import javax.activation.DataSource;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.RowMapper;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

//    @Autowired
//    private DataSource dataSource;

//    @Bean
//    public DataSource dataSource(){
//    	final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//    	  dataSource.setDriverClassName("org.h2.Driver");
//    	  dataSource.setUrl("jdbc:h2:~/test");
//    	  dataSource.setUsername("sa");
//    	  dataSource.setPassword("");
//    	  return dataSource;
//    }

//    @Bean
	public void readercommons() throws IOException {
		final String SAMPLE_CSV_FILE_PATH = "./Standerd.csv";

		try (Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withHeader("Attributes", "Details", "HKXRA124", "HKXRA771")
								.withIgnoreHeaderCase().withTrim());) {

// 	        	
			for (CSVRecord csvRecord : csvParser) {

				System.out.println(csvRecord.toMap());
// 	               return csvParser;

			}
		}
//			return null;

	}

//	@Bean
//	public FlatFileItemReader<Customer> reader() throws IOException {
//		FlatFileItemReader reader = new FlatFileItemReader();
//		readercommons();
////     reader.setResource(new ClassPathResource("employees.csv"));
////     reader.setLineMapper(new DefaultLineMapper<Employee>(){{
////     setLineTokenizer(new DelimitedLineTokenizer() {{
////     setNames(new String[] {"Name", "Age", "Salary", "Gender"});}});
////     setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {
////     {
////     setTargetType(Employee.class);
////     }
////     }
////     );
////     }});
////     readercommons();
////     
//		return reader;
//	}
	@Bean
    ItemReader<Map<String, Object>> excelStudentReader() {
		
        FlatFileItemReader<Map<String, Object>> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        DefaultLineMapper<Map<String, Object>> mapper = excelRowMapper(tokenizer);
        LineCallbackHandler skippedLinesCallback = new LineCallbackHandler() {
        	@Override
			public void handleLine(String line) {
        		String[] columnNames = tokenizer.tokenize(line).getValues();
        		tokenizer.setNames(columnNames);
        	}
        };
		reader.setSkippedLinesCallback(skippedLinesCallback );
        reader.setResource(new ClassPathResource("TIN MAINTAINANCE.csv"));
        //reader.
        reader.setLineMapper(mapper);
        reader.setStrict(false);
        return reader;
    }
	
	 private DefaultLineMapper<Map<String, Object>> excelRowMapper(LineTokenizer tokenizer) {
		DefaultLineMapper<Map<String,Object>> obj = new DefaultLineMapper<>();
		FieldSetMapper<Map<String, Object>> fieldSetMapper = new MyFieldSetMapper();
		obj.setLineTokenizer(tokenizer);

		obj.setFieldSetMapper(fieldSetMapper);
        return obj;
      //  return DefaultLineMapper<Map<String,Object>>();
        
    }
//    @Bean
//    public EmployeeItemProcessor processor()
//    {
//     return new EmployeeItemProcessor();
//    }

//    @Bean
//    public JdbcBatchItemWriter<Employee> writer() {
//        JdbcBatchItemWriter<Employee> itemWriter = new JdbcBatchItemWriter<Employee>();
//        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
//        itemWriter.setSql("INSERT INTO Employees(Name, Age, Salary, Gender) VALUES (:Name, :Age, :Salary, :Gender)");
//        itemWriter.setDataSource(dataSource);
//        return itemWriter;
//    }

	@Bean
	public Step step() throws IOException {
	//	stepBuilderFactory.get("").tasklet(tasklet)
		return stepBuilderFactory.get("step")
//                .partitioner(readercommons())
				.<Map<String,Object>, Map<String,Object>>chunk(3)
				.reader(excelStudentReader())
                .processor(processor())
                .writer(writer())
				.build();
	}
	
	public ItemWriter<? super Map<String, Object>> writer() {
return new NoOpItemWriter();
	
	}
	@Bean
    public CItemProcessor processor()
    {
     return new CItemProcessor();
    }
	@Bean
	public Job importUserJob() throws IOException {
		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).flow(step()).end().build();
	}

}
