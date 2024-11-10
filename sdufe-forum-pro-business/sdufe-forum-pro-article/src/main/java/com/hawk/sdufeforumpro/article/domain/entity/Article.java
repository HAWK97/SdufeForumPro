package com.hawk.sdufeforumpro.article.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hawk.sdufeforumpro.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章
 */
@Setter
@Getter
@TableName("articles")
public class Article extends BaseEntity {

    private String content;

    private Long userId;

    public Article addArticle(String content, Long userId) {
        this.setContent(content);
        this.setUserId(userId);
        return this;
    }
}
