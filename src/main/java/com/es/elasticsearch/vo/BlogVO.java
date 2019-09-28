package com.es.elasticsearch.vo;

import lombok.Data;

@Data
public class BlogVO {
    private String id;

    private String title;

    private String summary;

    private Integer readTimes;

    private String kinds;

    private String content;

    private String author;
}
