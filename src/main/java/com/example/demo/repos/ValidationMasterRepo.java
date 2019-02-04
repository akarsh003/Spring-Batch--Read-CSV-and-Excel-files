package com.example.demo.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.demo.model.Validation_Master;
import com.example.demo.model.Work_Request_Type_Master;
//@RepositoryRestResource
@RepositoryRestResource(collectionResourceRel = "validationmasterrepo", path = "validationmasterrepo")
public interface ValidationMasterRepo extends JpaRepository<Validation_Master, Integer> {

	 List<Validation_Master> findAllByWorkRequestTypeId_wId(Integer workRequestTypeId);
		

}
