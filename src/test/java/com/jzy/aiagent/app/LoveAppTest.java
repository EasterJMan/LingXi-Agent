package com.jzy.aiagent.app;

import com.jzy.aiagent.entity.LoveReport;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;


    @Test
    void doChat() {
        String uuid = UUID.randomUUID().toString();
        String answer = loveApp.doChat("你好，我是程序员鱼皮", uuid);
        Assertions.assertNotNull(answer);
        String answer1 = loveApp.doChat("如何和女生聊天", uuid);
        Assertions.assertNotNull(answer1);
        String answer2 = loveApp.doChat("我是谁？", uuid);
        Assertions.assertNotNull(answer2);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员鱼皮，我想让另一半（编程导航）更爱我，但我不知道该怎么做";
        LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

}