package com.example.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling

public class BatchCsvApplication {

	
	@Autowired
    JobLauncher jobLauncher;
      
    @Autowired
    Job job;
    
	public static void main(String[] args) {
		
		SpringApplication.run(BatchCsvApplication.class, args);
        }
	
	@Scheduled(cron = "0/10 * * * * ?")
    public void perform() throws Exception
    {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }
	}

       

