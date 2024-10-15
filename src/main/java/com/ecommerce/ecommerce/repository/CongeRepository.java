package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Conge;
import com.ecommerce.ecommerce.entity.Role;
import com.ecommerce.ecommerce.entity.Utilisateur;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface CongeRepository extends MongoRepository<Conge, String> {
    List<Conge> findByStatus(String status);
    @Query("{ 'utilisateur.role': ?0 }")
    List<Conge> findByRole(Role role);
    List<Conge> findByUtilisateurIdU(String idU);
    @Query("{ 'utilisateur.username': ?0 }")
    List<Conge> findByUsername(String username);
    @Query("{ 'dateDebut' : { $gte: ?0 }, 'dateFin' : { $lte: ?1 } }")
    List<Conge> findByDateRange(Date startDate, Date endDate);
    @Query("{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'status': { $regex: ?0, $options: 'i' } } ] }")
    List<Conge> searchByKeyword(String keyword);

}
