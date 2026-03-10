package com.jzy.imagesearchmcpserver.tools;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImageSearchTools {

    @Value("${pexels.api-key:}")
    private String apiKey;

    @Value("${pexels.api-url:https://api.pexels.com/v1/search}")
    private String apiUrl;

    @Value("${pexels.per-page:10}")
    private int perPage;

    @Tool(description = "search image from web via Pexels")
    public String searchImage(@ToolParam(description = "search query keyword") String query) {
        String err = validateRequest(query);
        if (err != null) return err;

        try {
            JSONArray photos = callPexelsSearch(query.trim());
            if (photos == null || photos.isEmpty()) return "未找到与 \"" + query + "\" 相关的图片。";
            return formatPhotos(photos, query);
        } catch (Exception e) {
            return "调用 Pexels 异常: " + e.getMessage();
        }
    }

    /** 调用 Pexels 搜索接口，返回 photos 数组；请求失败时抛出异常。 */
    private JSONArray callPexelsSearch(String query) {
        String url = apiUrl + "?query=" + URLUtil.encode(query) + "&per_page=" + Math.min(Math.max(perPage, 1), 80);
        HttpResponse response = HttpRequest.get(url).header("Authorization", apiKey).timeout(10000).execute();
        if (!response.isOk()) throw new RuntimeException("HTTP " + response.getStatus() + " - " + response.body());
        return JSONUtil.parseObj(response.body()).getJSONArray("photos");
    }

    private String validateRequest(String query) {
        if (StrUtil.isBlank(apiKey)) return "错误：未配置 Pexels API Key，请设置 pexels.api-key 或环境变量 PEXELS_API_KEY";
        if (StrUtil.isBlank(query)) return "错误：搜索关键词不能为空";
        return null;
    }

    private String formatPhotos(JSONArray photos, String query) {
        StringBuilder sb = new StringBuilder().append("共找到 ").append(photos.size()).append(" 张图片（关键词: ").append(query).append("）：\n\n");
        for (int i = 0; i < photos.size(); i++) {
            JSONObject photo = photos.getJSONObject(i);
            JSONObject src = photo.getJSONObject("src");
            String photographer = photo.getStr("photographer", "未知");
            sb.append(i + 1).append(". Photo by ").append(photographer).append(" on Pexels\n")
                    .append("   原图: ").append(src != null ? src.getStr("original", "") : "").append("\n")
                    .append("   中图: ").append(src != null ? src.getStr("medium", "") : "").append("\n\n");
        }
        return sb.toString();
    }
}
