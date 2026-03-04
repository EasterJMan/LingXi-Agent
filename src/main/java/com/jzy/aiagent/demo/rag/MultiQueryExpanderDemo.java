package com.jzy.aiagent.demo.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;

import java.util.List;

public class MultiQueryExpanderDemo {
    private final ChatClient.Builder chatClientBuilder;

    public MultiQueryExpanderDemo(ChatModel dashScopChatModel) {
        this.chatClientBuilder = ChatClient.builder(dashScopChatModel);
    }

    public List<Query> expander(String message){
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .build();
        List<Query> queries = queryExpander.expand(new Query("谁是程序员鱼皮啊？"));
        return queries;
    }
}
