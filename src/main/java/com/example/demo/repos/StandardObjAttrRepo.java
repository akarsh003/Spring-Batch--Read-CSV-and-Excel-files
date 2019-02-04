package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.demo.model.StandardObjectMappingMaster;
//@RepositoryRestResource
@RepositoryRestResource(collectionResourceRel = "standardobjattrrepo", path = "standardobjattrrepo")
public interface StandardObjAttrRepo extends JpaRepository<StandardObjectMappingMaster, Integer> {

}
