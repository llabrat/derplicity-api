package com.derplicity.blog.api.article.config;

import com.derplicity.blog.api.article.exceptions.ArticleException;
import com.derplicity.blog.api.shared.domain.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ArticleControllerAdvice {

    @ExceptionHandler(ArticleException.class)
    ResponseEntity<ErrorResponse> handleArticleException(ArticleException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ErrorResponse.builder().code(String.valueOf(ex.getStatus().value())).message(ex.getReason()).build());
    }
}
