package com.fb.goldencudgel.auditDecisionSystem.dao;

import com.fb.goldencudgel.auditDecisionSystem.mapper.FileContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class FileContextDao {

    @Autowired
    private MongoTemplate mongoTemplate;


    public FileContext findById(String fileId) {
        Query query = new Query(Criteria.where("fileId").is(fileId));
        FileContext fileContext = mongoTemplate.findOne(query,FileContext.class);
        return fileContext;
    }

    public List<FileContext> findByIdList(String fileId) {
        Query query = new Query(Criteria.where("fileId").is(fileId));
        return mongoTemplate.find(query, FileContext.class);
    }

    public void deleteById(String fileId) {
        Query query = new Query(Criteria.where("fileId").is(fileId));
        mongoTemplate.remove(query);
    }

    public void insertOne(FileContext file) {
        mongoTemplate.insert(file);
    }

}
