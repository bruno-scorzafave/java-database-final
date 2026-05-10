package com.project.code.Repo;

import java.util.List;

import com.project.code.Model.Review;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {

    public List<Review> findByStoreIdAndProductId(Long storeId, Long productId);
    
}
