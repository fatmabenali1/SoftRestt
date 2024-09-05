package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Token;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token,Integer> {
}
