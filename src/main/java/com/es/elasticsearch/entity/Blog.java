package com.es.elasticsearch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.GeneratedValue;
import java.util.Date;

/**
 * @author songning
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "simple_blog", type = "es_blog")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Blog {
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    private String title;

    private String summary;

    private Integer readTimes;

    private String kinds;

    private String author;

    private String content;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
