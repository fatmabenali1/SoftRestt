package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Conge;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CongeRepository extends MongoRepository<Conge, String> {
}
