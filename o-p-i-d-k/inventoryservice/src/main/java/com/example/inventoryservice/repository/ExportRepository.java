package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.ExportHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportRepository extends JpaRepository<ExportHistory, Integer> {
}
