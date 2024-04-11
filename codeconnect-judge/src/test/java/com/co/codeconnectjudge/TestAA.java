package com.co.codeconnectjudge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-10 23:09
 */
@SpringBootTest
public class TestAA {
    @Test
    public void test() {
        String json = "[[\'4 7\\n8\',\'7\\n45\']]";

        // 匹配双引号中的内容的正则表达式
        String regex = "\"([^\"]*)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);

        while (matcher.find()) {
            // 获取双引号中的内容
            String content = matcher.group(1);

            // 判断内容是否为空
            if (!content.trim().isEmpty()) {
                System.out.println("双引号中的内容为: " + content);
            } else {
                System.out.println("双引号中的内容为空");
            }
        }
    }
}
