package com.es.elasticsearch.vo;

import lombok.Data;

@Data
public class CommonVO<T> {
    /**
     * 指定第一页开始记录号
     */
    private Integer recordStartNo;

    /**
     * 每页记录数
     */
    private Integer pageRecordNum;

    private T condition;
}
