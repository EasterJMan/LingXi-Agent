package com.jzy.imagesearchmcpserver;

import com.jzy.imagesearchmcpserver.tools.ImageSearchTools;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ImageSearchMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageSearchMcpServerApplication.class, args);
    }


    @Bean
    public ToolCallback imageSearchTolls(ImageSearchTools imageSearchTools) {
        return MethodToolCallback.builder().toolObject(imageSearchTools).build();
    }
}
