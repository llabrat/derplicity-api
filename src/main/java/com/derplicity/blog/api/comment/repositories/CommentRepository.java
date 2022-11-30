package com.derplicity.blog.api.comment.repositories;

import com.derplicity.blog.api.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
