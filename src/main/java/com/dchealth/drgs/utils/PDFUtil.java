package com.dchealth.drgs.utils;

import com.alibaba.druid.util.StringUtils;
import com.dchealth.drgs.db.entity.DrgIcdR;
import com.dchealth.drgs.db.entity.DrgInfo;
import com.dchealth.drgs.db.repository.DrgInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class PDFUtil {
    @Autowired
    private DrgInfoRepository drgInfoRepository;
    /**
     * 获取指定页码的字符串列表
     *
     * @param filePath
     * @param startPage
     * @param endPage
     * @return
     */
    private static List<String> getLineList(String filePath, int startPage, int endPage) {
        //创建PdfDocument实例
        PdfDocument doc = new PdfDocument();
        //加载PDF文件
        doc.loadFromFile(filePath);
        //创建StringBuilder实例
        StringBuilder sb = new StringBuilder();
        //遍历PDF页面，获取每个页面的文本并添加到StringBuilder对象
        for (int i = startPage; i < endPage; i++) {
            PdfPageBase page = doc.getPages().get(i);
            sb.append(page.extractText());
        }
        List<String> lineList = Arrays.stream(sb.toString().split("\n|\r")).map(String::trim).collect(Collectors.toList());
        lineList = filterLine(lineList);
        return lineList;
    }

    /**
     * 过滤无用行
     *
     * @param lineList
     * @return
     */
    private static List<String> filterLine(List<String> lineList) {
        lineList = lineList.stream()
                .filter(line -> !(Pattern.matches("^[0-9]*$", line)
                        || Pattern.matches("^-\\s[0-9]*\\s-$", line)
                        || line.contains("Evaluation Warning")))
                .collect(Collectors.toList());
        return lineList;
    }

    /**
     * 获取DRG信息
     *
     * @param line
     * @throws JsonProcessingException
     */
    private static String getDrgInfoFromCatalog(String line) {
        String lineWithDot = getRegexStr(line,"\\s+.*\\.{3,}");
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
        List<String> lines = getLineList("C:\\Users\\qwqiong\\test.pdf", 4, 16);
        String mdcCode = "   ";
        List<DrgInfo> drgInfoList = new LinkedList<>();
        for (String line : lines) {
            if (line.contains("MDC") && !line.contains("主诊表，主诊编码")) {
            } else if (line.contains("MDC")) {
                mdcCode = getMDCInfoFromCataLog(line);
            } else {
                String drgCode = getRegexStr(line, "^[A-Z]{2}[0-9]");
                if(!StringUtils.isEmpty(drgCode)){
                    String drgName = getDrgInfoFromCatalog(line);
                    System.out.println(mdcCode+" "+drgCode+" "+drgName);
                    drgInfoList.add(DrgInfo.builder().drgCode(drgCode).drgNameCn(drgName).mdcInfo(mdcCode).build());
                }
            }
        }
        drgInfoRepository.saveAll(drgInfoList);
    }

    /**
     * 正则匹配提取内容
     *
     * @param text
     * @param regex
     * @return
     */
    public static String getRegexStr(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(text);
        String str = "";
        if (m.find()) {
            str = m.group(0);
        }
        return str;
    }

    public static void main(String... args) throws JsonProcessingException {
        System.out.println("11111111");
    }


}
