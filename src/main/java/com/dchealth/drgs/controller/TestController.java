package com.dchealth.drgs.controller;

import com.dchealth.drgs.service.MDCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private MDCService mdcService;


    @GetMapping("/test")
    public String extractMDCInfo() {
        new Thread(() -> mdcService.extractMDCInfo("C:\\Users\\qwqiong\\test.pdf",34,110)).start();
        return "sucess";
    }
}
