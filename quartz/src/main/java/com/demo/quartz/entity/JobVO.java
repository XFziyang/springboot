package com.demo.quartz.entity;

import lombok.Data;

import java.util.Map;

@Data
public class JobVO {

    private String id;
    private String group;
    private String cron;
    private Map<String, Object> data;
}
