package com.hawk.sdufeforumpro.article.infrastructure;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hawk.sdufeforumpro.article.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
