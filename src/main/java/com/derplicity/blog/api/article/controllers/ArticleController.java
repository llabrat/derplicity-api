package com.derplicity.blog.api.article.controllers;

import com.derplicity.blog.api.article.domain.Article;
import com.derplicity.blog.api.article.domain.dto.ArticleDto;
import com.derplicity.blog.api.article.exceptions.ArticleException;
import com.derplicity.blog.api.article.mappers.ArticleMapper;
import com.derplicity.blog.api.article.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/articles")
public class ArticleController {

    private final ArticleMapper mapper;
    private final ArticleRepository repository;

    @GetMapping()
    public List<Article> getArticles() {
        return repository.findAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Article createArticle(@RequestBody ArticleDto articlePostDto) {

        Article article = mapper.postArticleDtoToArticle(articlePostDto);
        return repository.save(article);
    }

    @PutMapping("/{id}")
    public Article updateArticle(@PathVariable UUID id, @RequestBody ArticleDto articleDto) {
        Article article = mapper.putArticleDtoToArticle(id, articleDto);

        Optional<Article> articleToUpdate = repository.findById(id);

        if (articleToUpdate.isPresent()) {
            return repository.save(article);
        } else {
            throw new ArticleException(HttpStatus.NOT_FOUND, "Article not found.");
        }
    }
}
