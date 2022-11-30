package com.derplicity.blog.api.article.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ArticleException extends ResponseStatusException {
    public ArticleException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
