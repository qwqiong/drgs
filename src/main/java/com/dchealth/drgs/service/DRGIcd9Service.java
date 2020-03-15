package com.dchealth.drgs.service;

import com.dchealth.drgs.db.entity.DrgIcd9Info;
import com.dchealth.drgs.db.repository.DrgIcd9Repository;
import com.dchealth.drgs.db.repository.DrgInfoRepository;
import com.dchealth.drgs.utils.PDFUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DRGIcd9Service {
    public static Set<String> drgInfoSet = new HashSet<>();
    private static ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private DrgIcd9Repository drgIcd9Repository;
    @Autowired
    private DrgInfoRepository drgInfoRepository;

    @PostConstruct
    private void initDrgInfoSet(){
        drgInfoSet = drgInfoRepository.findAll().stream().map(ele->ele.getDrgCode().trim()+ele.getDrgNameCn().trim()).collect(Collectors.toSet());
        drgInfoSet.forEach(log::info);
    }

    public  void extractDrgIcd9Info(String filePath,int startPage, int endPage) {
        String drgCode = "   ";
        String mdcCode = "   ";
        String relation = "";
        for (int page = startPage; page < endPage; page++) {
            try {
                log.info("page:{}",page);
                List<DrgIcd9Info> drgIcd9Infos = new LinkedList<>();
                String line = "";
                List<String> lineList = PDFUtil.getLineList(filePath, page);
                lineLoop:
                for (int i = 0; i < lineList.size(); i++) {
                    line = lineList.get(i);
                    try {
                        if (isMatchMDC1(line)) {
                            mdcCode = line.split("：")[1];
                            drgCode = "   ";
                            log.info(mdcCode);
                            continue lineLoop;
                        }
                        String rule = getRule(line);{
                            if(Objects.nonNull(rule)){
                                relation = rule;
                                continue lineLoop;
                            }
                        }
                        if(isMatchDRGLine(line)){
                            drgCode = line.split("\\s+")[0];
                            continue lineLoop;
                        }
                        if (isMatchMdcIcd9(line)) {
                            String[] wordArr = line.split("\\s+");
                            if (wordArr.length != 4) {
                                log.info("==============" + line);
                            }
                            DrgIcd9Info drgIcd9Info = DrgIcd9Info.builder()
                                    .mdcCode(mdcCode)
                                    .drgCode(drgCode)
                                    .icdCode1(wordArr[0])
                                    .icdNameCn1(wordArr[1])
                                    .icdCode2(wordArr[2])
                                    .icdNameCn2(wordArr[3])
                                    .relation(relation)
                                    .build();
                            String nextLine="";
                            if (i < lineList.size() - 1) {
                                nextLine = lineList.get(i + 1);
                            }
                            else if(i == lineList.size() - 1){
                                nextLine = PDFUtil.getLineList(filePath,page+1).get(0);
                            }
                            if (!isMatchMdcIcd9(nextLine)&&!isMatchHalfMDCIcd9Line(nextLine)) {
                                if (isEnd1(nextLine)) {
                                    drgIcd9Info.setIcdNameCn1(drgIcd9Info.getIcdNameCn1()+nextLine.split("\\s+")[0]);
                                } else if (isEnd2(nextLine)) {
                                    drgIcd9Info.setIcdNameCn2(drgIcd9Info.getIcdNameCn2()+nextLine.split("\\s+")[2]);
                                } else if (isEnd3(nextLine)) {
                                    drgIcd9Info.setIcdNameCn1(drgIcd9Info.getIcdNameCn1()+nextLine.split("\\s+")[0]);
                                    drgIcd9Info.setIcdNameCn2(drgIcd9Info.getIcdNameCn2()+nextLine.split("\\s+")[1]);
                                }
                            }
                            drgIcd9Infos.add(drgIcd9Info);
                        }
                        if(isMatchHalfMDCIcd9Line(line)){
                            String[] wordArr = line.split("\\s+");
                            DrgIcd9Info drgIcd9Info = DrgIcd9Info.builder()
                                    .mdcCode(mdcCode)
                                    .drgCode(drgCode)
                                    .icdCode1(wordArr[0])
                                    .icdNameCn1(wordArr[1])
                                    .relation(relation)
                                    .build();
                            drgIcd9Infos.add(drgIcd9Info);
                        }
                    }catch (Exception e){
                        log.error("识别错误",e);
                    }
                }
                drgIcd9Repository.saveAll(drgIcd9Infos);
            }catch (Exception e){
                log.error("-------------------------导入错误 page:"+page,e);
            }

        }
    }

    /**
     * 测试两者关系
     * @param line
     * @return
     */
    public static String  getRule(String line){
        if(line.contains("包含以下主要手术或操作")){
            return "in";
        }
        if(line.contains("同时包含手术1和手术2")){
            return "and";
        }
        else return null;
    }


    public static void main(String... args) throws JsonProcessingException {
//        extractMDCInfo("C:\\Users\\qwqiong\\test.pdf",34,150);
    }

    private static boolean isMatchMdcIcd9(String line) {
        return PDFUtil.isMatch(line, "^\\d{2,3}\\.[0-9x]+\\s+[\\u4E00-\\u9FA5].+\\s\\d{2,3}\\.[0-9x]+\\s+[\\u4E00-\\u9FA5].+");
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
        return PDFUtil.isMatch(nextLine, "^\\D.+\\s+\\d{2,3}\\.[0-9x]+\\s+[\\u4E00-\\u9FA5].+");
    }

    /**
     * 第一中形式的后缀
     *
     * @param nextLine
     * @return
     */
    private static boolean isEnd2(String nextLine) {
        return PDFUtil.isMatch(nextLine, "\\d{2,3}\\.[0-9x]+\\s+[\\u4E00-\\u9FA5].+\\s+\\D+");
    }

    /**
     * 第三中形式的后缀
     *
     * @param nextLine
     * @return
     */
    private static boolean isEnd3(String nextLine) {
        return nextLine.split("\\s+").length==2;
    }

    /**
     *判断是否是半个MDCICD10行
     */
    private static boolean isMatchHalfMDCIcd9Line(String line) {
        return PDFUtil.isMatch(line, "^\\d{2,3}\\.[0-9x]+\\s+[\\u4E00-\\u9FA5].+") && line.split("\\s+").length == 2;
    }
}
