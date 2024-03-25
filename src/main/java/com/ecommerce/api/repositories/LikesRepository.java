package com.ecommerce.api.repositories;


import com.ecommerce.api.entities.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    List<Likes> findAllByUserId(String userId);
    Optional<Likes> findByProdIdAndUserId(String prodId, String userId);
}
