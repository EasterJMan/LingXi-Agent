package com.jzy.aiagent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class HttpAiInvoke {


    public static void main(String[] args) {
        // 从环境变量获取 API Key（确保已设置 DASHSCOPE_API_KEY 环境变量）
        String apiKey = System.getenv("DASHSCOPE_API_KEY");

        // 构建请求 JSON 数据
        JSONObject requestData = new JSONObject();
        requestData.put("model", "qwen-plus");

        JSONObject input = new JSONObject();
        JSONObject[] messages = new JSONObject[2];

        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant.");

        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", "你是谁？");

        messages[0] = systemMessage;
        messages[1] = userMessage;

        input.put("messages", messages);
        requestData.put("input", input);

        JSONObject parameters = new JSONObject();
        parameters.put("result_format", "message");
        requestData.put("parameters", parameters);

        // 发送 POST 请求
        HttpResponse response = HttpRequest.post("https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation")
                .header("Authorization", "Bearer " + "sk-6be54d34210a4eceba6f89f154eaacb4")
                .header("Content-Type", "application/json")
                .body(requestData.toString())
                .execute();

        // 输出响应结果
        System.out.println("Status: " + response.getStatus());
        System.out.println("Response: " + response.body());
    }
}
