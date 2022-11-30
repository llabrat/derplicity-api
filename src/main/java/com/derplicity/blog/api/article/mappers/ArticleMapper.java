package com.derplicity.blog.api.article.mappers;


import com.derplicity.blog.api.article.domain.Article;
import com.derplicity.blog.api.article.domain.dto.ArticleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleDto articleToArticleDto(Article article);

    @Mapping(target = "id", ignore = true)
    Article postArticleDtoToArticle(ArticleDto articlePostDto);

    @Mapping(target = "id", source = "id")
    Article putArticleDtoToArticle(UUID id, ArticleDto articlePostDto);
}
