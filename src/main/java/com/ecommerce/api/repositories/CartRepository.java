package com.ecommerce.api.repositories;

import com.ecommerce.api.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByUserId(String userId);
    Optional<Cart> findByProdIdAndUserId(String prodId, String userId);

}
