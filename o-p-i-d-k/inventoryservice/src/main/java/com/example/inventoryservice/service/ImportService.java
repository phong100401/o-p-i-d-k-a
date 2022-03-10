package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.ImportHistory;
import org.springframework.transaction.annotation.Transactional;

public interface ImportService {
    @Transactional
    ImportHistory save(ImportHistory importHistory);
}
