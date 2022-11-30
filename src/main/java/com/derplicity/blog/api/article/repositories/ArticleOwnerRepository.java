package com.derplicity.blog.api.article.repositories;

import com.derplicity.blog.api.article.domain.ArticleOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArticleOwnerRepository extends JpaRepository<ArticleOwner, UUID> {
}
