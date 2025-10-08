package com.jzy.aiagent.entity;

import lombok.Data;

import java.util.List;

@Data
public class LoveReport {
    private String title;
    private List<String> suggestions;
}
