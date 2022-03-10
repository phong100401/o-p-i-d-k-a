package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.ImportHistory;
import com.example.inventoryservice.response.RESTResponse;
import com.example.inventoryservice.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/import/history")
public class ImportController {
    @Autowired
    ImportService importService;

    @RequestMapping(path = "create", method = RequestMethod.POST)
    public ResponseEntity save(@RequestBody ImportHistory importHistory){
        return new ResponseEntity(new RESTResponse.Success()
                .addData(importService.save(importHistory))
                .build(), HttpStatus.OK);
    }
}
