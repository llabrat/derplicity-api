package com.derplicity.blog.api.article;

import com.derplicity.blog.api.article.domain.ArticleOwner;
import com.derplicity.blog.api.article.repositories.ArticleOwnerRepository;
import com.derplicity.blog.api.article.services.UserEventListener;
import com.derplicity.blog.api.shared.ContextBase;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles({"test"})
class ArticleOwnerServiceTests extends ContextBase {

    @Value("${article.kafka.topics.user-topic.name}")
    public String userTopicName;

    private KafkaTemplate<String, String> producer;

    @Autowired
    ArticleOwnerRepository repository;

    @Autowired
    UserEventListener userEventListener;

    @BeforeEach
    void setUp() {
        this.producer = buildKafkaTemplate();
        this.producer.setDefaultTopic(userTopicName);
    }

    KafkaTemplate<String, String> buildKafkaTemplate() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(producerProps, new StringSerializer(), new StringSerializer());
        return new KafkaTemplate<>(pf);
    }

    @Test
    void whenNewUserOnTopic_userAdded() throws InterruptedException {
        UUID userId = UUID.randomUUID();

        producer.sendDefault(userId.toString(), "Test User");

        boolean messageConsumed = userEventListener.getLatch().await(10, TimeUnit.SECONDS);
        assertTrue(messageConsumed);

        Optional<ArticleOwner> owner = repository.findById(userId);
        assertThat(owner).isPresent();
    }
}
