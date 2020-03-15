package com.dchealth.drgs.utils;

import com.dchealth.drgs.db.repository.DrgInfoRepository;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PDFUtil {
    @Autowired
    private DrgInfoRepository drgInfoRepository;

    /**
     * 获取指定页码范围的字符串列表
     *
     * @param filePath
     * @param startPage
     * @param endPage
     * @return
     */
    public static List<String> getLineList(String filePath, int startPage, int endPage) {
        //遍历PDF页面，获取每个页面的文本并添加到StringBuilder对象
        List<String> lineList = new LinkedList<>();
        int pageSize = getPageSize(filePath);
        if(startPage<=pageSize&&endPage<=pageSize);
        for (int i = startPage - 1; i < endPage - 1; i++) {
            lineList.addAll(getLineList(filePath,i));
        }
        return lineList;
    }

    /**
     * 获取指定页码的数据
     *
     * @param filePath
     * @param pageNumber
     * @return
     */
    public static List<String> getLineList(String filePath, int pageNumber) {
        //创建PdfDocument实例
        PdfDocument doc = new PdfDocument();
        //加载PDF文件
        doc.loadFromFile(filePath);
        //创建StringBuilder实例
        StringBuilder sb = new StringBuilder();
        PdfPageBase page = doc.getPages().get(pageNumber-1);
        sb.append(page.extractText());
        List<String> lineList = Arrays.stream(sb.toString().split("\n|\r")).map(String::trim).collect(Collectors.toList());
        lineList = filterLine(lineList);
        return lineList;
    }

    /**
     * 读取页数
     *
     * @param filePath
     * @return
     */
    private static int getPageSize(String filePath) {
        //创建PdfDocument实例
        PdfDocument doc = new PdfDocument();
        //加载PDF文件
        doc.loadFromFile(filePath);
        return doc.getPages().getCount();
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

    /**
     * 正则匹配
     *
     * @param text
     * @param regex
     * @return
     */
    public static boolean isMatch(String text, String regex) {
        return Pattern.matches(regex, text);
    }

    public static void main(String... args) {
        List<String> stringList = getLineList("C:\\Users\\qwqiong\\test.pdf", 35);
        stringList.forEach(log::info);
    }
}
