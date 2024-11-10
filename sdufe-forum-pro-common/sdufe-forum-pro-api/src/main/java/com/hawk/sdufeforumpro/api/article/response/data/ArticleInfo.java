package com.hawk.sdufeforumpro.api.article.response.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ArticleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long articleId;

    private String content;

    private Long userId;

    private Long likeCount;

    private Date createTime;
}
