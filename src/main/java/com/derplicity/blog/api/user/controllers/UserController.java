package com.derplicity.blog.api.user.controllers;

import com.derplicity.blog.api.user.domain.User;
import com.derplicity.blog.api.user.domain.dto.UserDto;
import com.derplicity.blog.api.user.mappers.UserMapper;
import com.derplicity.blog.api.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    @Value("${user.kafka.topics.user-topic.name}")
    private String userTopicName;

    private final UserRepository repository;

    private final KafkaTemplate<String, String> template;

    private final UserMapper mapper;

    @GetMapping()
    public List<User> getUsers() {
        return repository.findAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody UserDto userDto) {
        var blogUser = mapper.userDtoToUser(userDto);
        var savedUser = repository.save(blogUser);
        log.info("Saved user: {}", blogUser);
        template.send(userTopicName, String.valueOf(savedUser.getId()), savedUser.getName());
        return savedUser;
    }
}
