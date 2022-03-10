package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.ImportHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportRepository extends JpaRepository<ImportHistory, Integer> {
}
