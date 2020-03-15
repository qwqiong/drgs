package com.dchealth.drgs.service;

import com.alibaba.druid.util.StringUtils;
import com.dchealth.drgs.db.entity.DrgInfo;
import com.dchealth.drgs.db.repository.DrgInfoRepository;
import com.dchealth.drgs.utils.PDFUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class DRGService {
    @Autowired
    private DrgInfoRepository drgInfoRepository;
    /**
     * 获取DRG信息
     *
     * @param line
     * @throws JsonProcessingException
     */
    private static String getDrgInfoFromCatalog(String line) {
        String lineWithDot = PDFUtil.getRegexStr(line,"\\s+.*\\.{3,}");
        lineWithDot = lineWithDot.replaceAll("\\.{3,}","");
        return lineWithDot;
    }

    /**
     * 获取MDC信息
     *
     * @param line
     * @return
     */
    private static String getMDCInfoFromCataLog(String line) {
        String[] mdcCodeArr = line.split("\\s+");
        String mdcCode = mdcCodeArr[1].split("：")[1];
        return mdcCode;
    }

    /**
     * 提取Drg分组信息
     */
    public  void extractDrgInfo() {
        List<String> lines = PDFUtil.getLineList("C:\\Users\\qwqiong\\test.pdf", 4, 16);
        String mdcCode = "   ";
        List<DrgInfo> drgInfoList = new LinkedList<>();
        for (String line : lines) {
            if (line.contains("MDC") && !line.contains("主诊表，主诊编码")) {
            } else if (line.contains("MDC")) {
                mdcCode = getMDCInfoFromCataLog(line);
            } else {
                String drgCode = PDFUtil.getRegexStr(line, "^[A-Z]{2}[0-9]");
                if(!StringUtils.isEmpty(drgCode)){
                    String drgName = getDrgInfoFromCatalog(line);
                    System.out.println(mdcCode+" "+drgCode+" "+drgName);
                    drgInfoList.add(DrgInfo.builder().drgCode(drgCode).drgNameCn(drgName).mdcInfo(mdcCode).build());
                }
            }
        }
        drgInfoRepository.saveAll(drgInfoList);
    }

}
