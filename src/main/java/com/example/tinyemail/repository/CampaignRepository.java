package com.example.tinyemail.repository;


import com.example.tinyemail.model.Campaign;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CampaignRepository extends MongoRepository<Campaign, String> {

    List<Campaign> findByUserId(String userId);
}

