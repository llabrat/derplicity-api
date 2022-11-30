package com.derplicity.blog.api.article;


import com.derplicity.blog.api.article.domain.Article;
import com.derplicity.blog.api.article.domain.dto.ArticleDto;
import com.derplicity.blog.api.article.mappers.ArticleMapper;
import com.derplicity.blog.api.article.repositories.ArticleRepository;
import com.derplicity.blog.api.shared.ContextBase;
import com.derplicity.blog.api.shared.domain.ErrorResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;


class ArticleControllerTests extends ContextBase {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ArticleMapper mapper;

    @Autowired
    ArticleRepository articleRepository;

    @AfterEach
    void cleanDb() {
        articleRepository.deleteAll();
    }

    @Test
    void whenGetArticles_then200AndResponse() {

        webTestClient
                .get()
                .uri("/v1/articles")

                .exchange()

                .expectStatus().isOk()
                .expectBodyList(Article.class);
    }

    @Test
    void whenPostArticle_Then201AndResponse() {
        var request = ArticleDto
                .builder()
                .content("This is just a test")
                .build();

        webTestClient
                .post()
                .uri("/v1/articles")
                .bodyValue(request)

                .exchange()

                .expectStatus().isCreated()
                .expectBody(Article.class).consumeWith(result -> {
                            Assertions.assertNotNull(result.getResponseBody());
                            Assertions.assertEquals(request.getContent(), result.getResponseBody().getContent());
                        }
                );
    }

    @Test
    void whenPutArticle_idFound_Then200AndResponse() {
        var request = ArticleDto
                .builder()
                .content("This is just a test")
                .build();

        EntityExchangeResult<Article> response = webTestClient
                .post()
                .uri("/v1/articles")
                .bodyValue(request)

                .exchange()

                .expectStatus().isCreated()
                .expectBody(Article.class).returnResult();

        Assertions.assertNotNull(response.getResponseBody());

        var updatedRequest = mapper.articleToArticleDto(response.getResponseBody());
        updatedRequest.setContent("Updated content");

        webTestClient
                .put()
                .uri("/v1/articles/" + response.getResponseBody().getId())
                .bodyValue(updatedRequest)

                .exchange()

                .expectStatus().isOk()
                .expectBody(Article.class).consumeWith(result -> {
                            Assertions.assertNotNull(result.getResponseBody());
                            Assertions.assertEquals(updatedRequest.getContent(), result.getResponseBody().getContent());
                        }
                );
    }

    @Test
    void whenPutArticle_idNotFound_Then404AndResponse() {
        var request = ArticleDto
                .builder()
                .content("This is just a test")
                .build();

        var invalidId = UUID.randomUUID();

        webTestClient
                .put()
                .uri("/v1/articles/" + invalidId)
                .bodyValue(request)

                .exchange()

                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class).consumeWith(result -> {
                            Assertions.assertNotNull(result.getResponseBody());
                            Assertions.assertEquals("Article not found.", result.getResponseBody().getMessage());
                        }
                );
    }
}
