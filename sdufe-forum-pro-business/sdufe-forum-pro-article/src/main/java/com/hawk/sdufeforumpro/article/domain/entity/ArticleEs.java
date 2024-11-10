package com.hawk.sdufeforumpro.article.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Setter
@Getter
@Document(indexName = "sdufe_forum_pro_articles")
public class ArticleEs {

    @Id
    private Long id;

    private String content;

    @Field(name = "user_id")
    private Long userId;

    @Field(name = "create_time", type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss+08:00")
    private Date createTime;
}
