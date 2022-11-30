package com.derplicity.blog.api.shared.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${user.kafka.topics.user-topic.name}")
    private String userTopicName;

    @Value("${user.kafka.topics.user-topic.replicas}")
    private Integer userTopicReplicas;

    @Value("${user.kafka.topics.user-topic.partitions}")
    private Integer userTopicPartitions;

    @Bean
    public NewTopic userTopic() {
        return TopicBuilder
                .name(userTopicName)
                .replicas(userTopicReplicas)
                .partitions(userTopicPartitions)
                .build();
    }
}
