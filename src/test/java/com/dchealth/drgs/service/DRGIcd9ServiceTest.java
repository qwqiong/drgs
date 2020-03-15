package com.dchealth.drgs.service;

import com.dchealth.drgs.DrgsApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DrgsApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DRGIcd9ServiceTest {

    @Autowired
    private DRGIcd9Service drgIcd9Service;

    @Test
    void extractDrgIcd9Info() {
        drgIcd9Service.extractDrgIcd9Info("C:\\Users\\qwqiong\\test.pdf",617,618);
    }
}