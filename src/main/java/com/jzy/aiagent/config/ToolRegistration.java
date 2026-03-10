package com.jzy.aiagent.config;

import com.jzy.aiagent.tools.TerminateTool;
import com.jzy.aiagent.tools.WebScrapingTool;
import com.jzy.aiagent.tools.WebSearchTool;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolRegistration {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Bean
    public ToolCallback[] allTools() {
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        TerminateTool terminateTool = new TerminateTool();
        return ToolCallbacks.from(
                webSearchTool,
                webScrapingTool,
                terminateTool
        );
    }
}
