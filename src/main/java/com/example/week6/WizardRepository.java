package com.example.week6;

import com.example.week6.Wizard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WizardRepository extends MongoRepository<Wizard, String> {
        @Query(value="{name:'?0'}")
    public Wizard findByName(String name);



}