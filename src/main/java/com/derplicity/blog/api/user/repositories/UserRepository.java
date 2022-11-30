package com.derplicity.blog.api.user.repositories;

import com.derplicity.blog.api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
