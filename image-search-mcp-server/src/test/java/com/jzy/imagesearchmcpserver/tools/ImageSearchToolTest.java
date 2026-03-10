package com.jzy.imagesearchmcpserver.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImageSearchToolTest {
    @Resource
    private ImageSearchTools imageSearchTools;

    @Test
    public void test() {
        String images = imageSearchTools.searchImage("computer");
        Assertions.assertNotNull(images);
    }
}
