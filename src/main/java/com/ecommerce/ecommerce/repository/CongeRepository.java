package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Conge;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CongeRepository extends MongoRepository<Conge, String> {
    // Custom queries if needed
}
