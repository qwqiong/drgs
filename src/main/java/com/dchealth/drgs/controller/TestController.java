package com.dchealth.drgs.controller;

import com.dchealth.drgs.service.DRGIcd9Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private DRGIcd9Service drgIcd9Service;


    @GetMapping("/test")
    public String extractMDCInfo() {
        new Thread(() -> drgIcd9Service.extractDrgIcd9Info("C:\\Users\\qwqiong\\test.pdf",33,929)).start();
        return "sucess";
    }
}
