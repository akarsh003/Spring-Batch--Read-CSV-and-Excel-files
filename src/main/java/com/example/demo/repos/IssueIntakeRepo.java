package com.example.demo.repos;

import com.example.demo.model.Issue_Intake;

import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface IssueIntakeRepo extends JpaRepository<Issue_Intake,String> {
//public String findByMappedJson(String Id);
@Cacheable("issueintake")
 Issue_Intake findAllById(String Id);

void deleteByUuid(UUID uuid);

}
