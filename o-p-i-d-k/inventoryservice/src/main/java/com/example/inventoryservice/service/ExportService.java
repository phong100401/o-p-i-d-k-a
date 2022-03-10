package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.ExportHistory;
import org.springframework.transaction.annotation.Transactional;

public interface ExportService {
    @Transactional
    ExportHistory creat(ExportHistory exportHistory);
}
