package com.dchealth.drgs.utils;

import com.dchealth.drgs.DrgsApplication;
import com.dchealth.drgs.service.DRGService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DrgsApplication.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PDFUtilTest {
    @Autowired
    private DRGService drgService;

    @Test
    void extractDrgInfo() {
        drgService.extractDrgInfo();
    }
}