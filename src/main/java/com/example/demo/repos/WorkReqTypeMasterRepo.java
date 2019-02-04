package com.example.demo.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Work_Request_Type_Master;
//@RepositoryRestResource

@Repository
public interface WorkReqTypeMasterRepo extends JpaRepository<Work_Request_Type_Master, Integer> {

//	List<Work_Request_Type_Master> findByWork_Request_Type(String value);
	Work_Request_Type_Master findByWorkRequestType(String value);

}
