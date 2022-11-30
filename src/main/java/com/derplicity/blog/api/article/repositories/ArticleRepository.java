package com.derplicity.blog.api.article.repositories;

import com.derplicity.blog.api.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface ArticleRepository extends JpaRepository<Article, UUID> {
}
