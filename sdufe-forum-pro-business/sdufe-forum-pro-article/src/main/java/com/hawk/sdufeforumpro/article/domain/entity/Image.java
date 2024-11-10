package com.hawk.sdufeforumpro.article.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hawk.sdufeforumpro.api.article.constant.ImageState;
import com.hawk.sdufeforumpro.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 图片
 */
@Setter
@Getter
@TableName("images")
public class Image extends BaseEntity {

    private String url;

    private Long articleId;

    private ImageState imageState;

    public Image addImage(String url, Long articleId, ImageState imageState) {
        this.url = url;
        this.articleId = articleId;
        this.imageState = imageState;
        return this;
    }
}
