package com.derplicity.blog.api.user;

import com.derplicity.blog.api.shared.ContextBase;
import com.derplicity.blog.api.user.domain.User;
import com.derplicity.blog.api.user.domain.dto.UserDto;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTests extends ContextBase {

    Consumer<String, String> consumer;

    private ConsumerRecord<String, String> getMessageFromTopic() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "true", embeddedKafkaBroker));
        consumer = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer()).createConsumer();
        consumer.subscribe(singleton(userTopicName));
        consumer.poll(Duration.ZERO);
        ConsumerRecord<String, String> received = KafkaTestUtils.getSingleRecord(consumer, userTopicName, 5000);
        consumer.close();
        return received;
    }

    @Test
    void whenGetUsers_then200AndResponse() {

        webTestClient
                .get()
                .uri("/v1/users")

                .exchange()

                .expectStatus().isOk()
                .expectBodyList(User.class);
    }

    @Test
    void whenPostUser_then201AndResponseAndMessageSent() {

        var user = UserDto.builder().name("Test User").build();

        webTestClient
                .post()
                .uri("/v1/users")
                .bodyValue(user)

                .exchange()

                .expectStatus().isCreated()
                .expectBody(User.class).consumeWith(result -> {
                            Assertions.assertNotNull(result.getResponseBody());
                            Assertions.assertEquals(user.getName(), result.getResponseBody().getName());
                            Assertions.assertInstanceOf(UUID.class, result.getResponseBody().getId());
                        }
                );

        ConsumerRecord<String, String> received = getMessageFromTopic();
        assertThat(received.value()).isEqualTo(user.getName());
    }
}