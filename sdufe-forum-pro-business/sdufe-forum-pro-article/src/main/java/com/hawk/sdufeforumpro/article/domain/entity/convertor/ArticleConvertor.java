package com.hawk.sdufeforumpro.article.domain.entity.convertor;

import com.hawk.sdufeforumpro.api.article.response.data.ArticleInfo;
import com.hawk.sdufeforumpro.article.domain.entity.ArticleEs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ArticleConvertor {

    ArticleConvertor INSTANCE = Mappers.getMapper(ArticleConvertor.class);

    /**
     * 转换为vo
     *
     * @param article
     * @return
     */
    @Mapping(target = "articleId", source = "article.id")
    public ArticleInfo mapToVo(ArticleEs article);
}
