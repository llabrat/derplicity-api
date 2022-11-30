package com.derplicity.blog.api.article.services;

import com.derplicity.blog.api.article.domain.ArticleOwner;
import com.derplicity.blog.api.article.repositories.ArticleOwnerRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final ArticleOwnerRepository repository;

    @Getter
    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = {"${article.kafka.topics.user-topic.name}"})
    public void receiveUserEvent(ConsumerRecord<String, String> userEvent) {

        var articleOwner = ArticleOwner
                .builder()
                .id(UUID.fromString(userEvent.key()))
                .name(userEvent.value())
                .build();

        repository.save(articleOwner);
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

}
