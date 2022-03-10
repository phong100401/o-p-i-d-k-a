package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.ExportHistory;
import com.example.inventoryservice.response.RESTResponse;
import com.example.inventoryservice.service.ExportServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/export/history")
public class ExportController {
    @Autowired
    ExportServiceImp exportServiceImp;

    @RequestMapping(path = "create", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody ExportHistory exportHistory){
        return new ResponseEntity(new RESTResponse.Success()
                .addData(exportServiceImp.creat(exportHistory))
                .build(), HttpStatus.OK);
    }
}
