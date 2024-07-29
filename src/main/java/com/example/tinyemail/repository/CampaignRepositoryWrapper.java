package com.example.tinyemail.repository;

import com.example.tinyemail.enums.CampaignStatus;
import com.example.tinyemail.model.Campaign;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CampaignRepositoryWrapper {
    @Autowired
    private CampaignRepository repository;

    @Autowired
    MongoTemplate mongoTemplate;

    public Campaign save(Campaign campaign) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(campaign.getName()).and("isDeleted").
            is(true).and("userId").is(campaign.getUserId()));
        Campaign exCampaign = mongoTemplate.findOne(query, Campaign.class);
        campaign.setUpdated(LocalDateTime.now());
        campaign.setCreated(LocalDateTime.now());
        campaign.setStatus(CampaignStatus.DRAFT);
        if (exCampaign != null) {
            campaign.setId(exCampaign.getId());
            campaign.setDeleted(false);
        }
        return repository.save(campaign);
    }

    public Campaign update(Campaign campaign) {
        campaign.setUpdated(LocalDateTime.now());
        return repository.save(campaign);
    }

    public Campaign findCampaignByUserIdAndName(String userId, String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name).and("isDeleted").
            ne(true).and("userId").is(userId));
        return mongoTemplate.findOne(query, Campaign.class);
    }

    public Campaign findCampaignById(String campaignId) {
        ObjectId objectId = new ObjectId(campaignId);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(objectId).and("isDeleted").ne(true));
        return mongoTemplate.findOne(query, Campaign.class);
    }

    public List<Campaign> findAll(String userId, Integer pageNum, Integer pageSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId).and("isDeleted").
            ne(true));
        return mongoTemplate.find(query, Campaign.class);
    }

    public List<Campaign> findAll(String userId) {
        return this.findAll(userId, null, null);
    }

}

