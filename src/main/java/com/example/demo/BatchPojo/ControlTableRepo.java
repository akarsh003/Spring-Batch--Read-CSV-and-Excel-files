package com.example.demo.BatchPojo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;


@Repository
public interface ControlTableRepo extends JpaRepository<ControlTable, java.util.UUID> {

	ControlTable findByfilename(String filename);
	
	ControlTable findBystatus(String status);
	
}
