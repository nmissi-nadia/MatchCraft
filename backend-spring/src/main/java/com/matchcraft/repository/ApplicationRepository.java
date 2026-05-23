package com.matchcraft.repository;

import com.matchcraft.domain.entity.Application;
import com.matchcraft.domain.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStatut(ApplicationStatus statut);
}
