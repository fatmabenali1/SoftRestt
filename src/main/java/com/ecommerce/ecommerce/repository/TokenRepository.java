package com.ecommerce.ecommerce.repository;

import ch.qos.logback.core.subst.Token;
import lombok.Data;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, Integer> {

    @Query("{ 'user.id': ?0, $or: [ { 'expired': false }, { 'revoked': false } ] }")
    List<Token> findAllValidTokenByUser(Integer id);

    @Query("{ 'token': ?0 }")
    Optional<Token> findByToken(String token);
}
