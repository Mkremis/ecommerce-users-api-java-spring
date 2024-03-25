package com.ecommerce.api.repositories;

import com.ecommerce.api.entities.Dashboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DashboardsRepository extends JpaRepository<Dashboard, String> {
    @Query("SELECT d FROM Dashboard d WHERE d.user.id = :userId")
    Optional<Dashboard> findByUserId( String userId);
}
