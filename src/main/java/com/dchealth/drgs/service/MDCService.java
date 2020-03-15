package com.dchealth.drgs.service;

import com.dchealth.drgs.db.entity.MdcInfo;
import com.dchealth.drgs.db.repository.DrgInfoRepository;
import com.dchealth.drgs.db.repository.MDCInfoRepository;
import com.dchealth.drgs.utils.PDFUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MDCService {
    public static Set<String> drgInfoSet = new HashSet<>();
    @Autowired
    private MDCInfoRepository mdcInfoRepository;
    @Autowired
    private DrgInfoRepository drgInfoRepository;

    @PostConstruct
    private void initDrgInfoSet(){
        drgInfoSet = drgInfoRepository.findAll().stream().map(ele->ele.getDrgCode().trim()+ele.getDrgNameCn().trim()).collect(Collectors.toSet());
        drgInfoSet.forEach(log::info);
    }

    public  void extractMDCInfo(String filePath,int startPage, int endPage) {
        String drgCode = "   ";
        String mdcCode = "   ";
        for (int page = startPage; page < endPage; page++) {
            log.info("page:{}",page);
            List<MdcInfo> mdcInfoList = new LinkedList<>();
            String line = "";
            List<String> lineList = PDFUtil.getLineList(filePath, page);
            for (int i = 0; i < lineList.size(); i++) {
                line = lineList.get(i);
                try {
                    if (isMatchMDC1(line)) {
                        mdcCode = line.split("：")[1];
                        drgCode = "   ";
                        log.info(mdcCode);
                    }
                    if(isMatchDRGLine(line)){
                        drgCode = line.split("\\s+")[0];
                    }
                    if (isMatchMdcIcd10(line)) {
                        String[] wordArr = line.split("\\s+");
                        if (wordArr.length != 4) {
                            log.info("==============" + line);
                        }
                        MdcInfo mdcInfo1 = MdcInfo.builder().mdcCode(mdcCode).drgCode(drgCode).icdCode(wordArr[0]).icdNameCn(wordArr[1]).build();
                        MdcInfo mdcInfo2 = MdcInfo.builder().mdcCode(mdcCode).drgCode(drgCode).icdCode(wordArr[2]).icdNameCn(wordArr[3]).build();
                        String nextLine="";
                        if (i < lineList.size() - 1) {
                            nextLine = lineList.get(i + 1);
                        }
                        else if(i == lineList.size() - 1){
                            nextLine = PDFUtil.getLineList(filePath,page+1).get(0);
                        }
                        if (!isMatchMdcIcd10(nextLine)&&!isMatchHalfMDCIcd10Line(nextLine)) {
                            if (isEnd1(nextLine)) {
                                mdcInfo1.setIcdNameCn(mdcInfo1.getIcdNameCn() + nextLine.split("\\s+")[0]);
                            } else if (isEnd2(nextLine)) {
                                mdcInfo2.setIcdNameCn(mdcInfo2.getIcdNameCn() + nextLine.split("\\s+")[2]);
                            } else if (isEnd3(nextLine)) {
                                mdcInfo1.setIcdNameCn(mdcInfo1.getIcdNameCn() + nextLine.split("\\s+")[0]);
                                mdcInfo2.setIcdNameCn(mdcInfo2.getIcdNameCn() + nextLine.split("\\s+")[1]);
                            }
                        }
                        mdcInfoList.add(mdcInfo1);
                        mdcInfoList.add(mdcInfo2);
                    }
                    if(isMatchHalfMDCIcd10Line(line)){
                        String[] wordArr = line.split("\\s+");
                        MdcInfo mdcInfo = MdcInfo.builder().mdcCode(mdcCode).drgCode(drgCode).icdCode(wordArr[0]).icdNameCn(wordArr[1]).build();
                        mdcInfoList.add(mdcInfo);
                    }
                }catch (Exception e){
                    log.error("识别错误",e);
                }
            }
            mdcInfoRepository.saveAll(mdcInfoList);
        }
    }


    public static void main(String... args) throws JsonProcessingException {
//        extractMDCInfo("C:\\Users\\qwqiong\\test.pdf",34,150);
    }

    private static boolean isMatchMdcIcd10(String line) {
        return PDFUtil.isMatch(line, "^[A-Z]\\d{2}\\..+\\s+[A-Z]\\d{2}\\..+\\s.+");
    }

    /**
     * 匹配MDC信息
     *
     * @param line
     * @return
     */

    private static boolean isMatchMDC1(String line) {
        return PDFUtil.isMatch(line, "MDC[A-Z]\\s+主诊表，主诊编码：[A-Z]\\d\\d$");
    }
    /**
     * 匹配DRG码
     */
    private static boolean isMatchDRGLine(String line){
        return drgInfoSet.contains(line.replaceAll("\\s+",""));
    }

    /**
     * 第二种形式的后缀
     *
     * @param nextLine
     * @return
     */
    private static boolean isEnd1(String nextLine) {
        return PDFUtil.isMatch(nextLine, "^[^A-Z].+\\s+[A-Z]\\d{2}\\.\\d{3}.+\\s+.+");
    }

    /**
     * 第一中形式的后缀
     *
     * @param nextLine
     * @return
     */
    private static boolean isEnd2(String nextLine) {
        return PDFUtil.isMatch(nextLine, "^[A-Z]\\d{2}\\..+[^A-Z]\\s+[^A-Z]+");
    }

    /**
     * 第三中形式的后缀
     *
     * @param nextLine
     * @return
     */
    private static boolean isEnd3(String nextLine) {
        return PDFUtil.isMatch(nextLine, "^[^A-Z].+\\s+[^A-Z].+");
    }

    /**
     *判断是否是半个MDCICD10行
     */
    private static boolean isMatchHalfMDCIcd10Line(String line) {
        return PDFUtil.isMatch(line, "^[A-Z]\\d{2}\\..+\\s+[^A-Z].+") && line.split("\\s+").length == 2;
    }

    /**
     * 叶第一行是否有前后缀
     */
    private static void testFirstLine(String filePath) {
        for (int i = 33; i <= 100; i++) {
            List<String> strList = PDFUtil.getLineList(filePath, i);
            String line = strList.get(0);
            if (!isMatchMdcIcd10(line)) {
                if (isEnd1(line) || isEnd2(line) || isEnd3(line)) {
                    log.info(line);
                }
            }
        }

    }
}
