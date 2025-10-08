package com.jzy.aiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

public class LangChain4jInvoke {
    public static void main(String[] args) {
        ChatLanguageModel chatModel = QwenChatModel.builder()
                .modelName("qwen-max")
                .apiKey(TestApiKey.API_KEY).build();
        String chat = chatModel.chat("我是一名java工作5年的开发员工，我应该会哪些技术，技术的深度掌握程度又该是什么样");
        System.out.printf(chat);
    }
}
