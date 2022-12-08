package com.derplicity.blog.api.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.time.Instant;
import java.util.List;


@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(brokerProperties = "listeners=PLAINTEXT://localhost:9092")
public abstract class ContextBase {

    @MockBean
    JwtDecoder jwtDecoder;

    public WebTestClient webTestClient;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public EmbeddedKafkaBroker embeddedKafkaBroker;

    @Value("${user.kafka.topics.user-topic.name}")
    public String userTopicName;

    @Autowired
    void setMockMvc(MockMvc mockMvc) {
        this.webTestClient = MockMvcWebTestClient.bindTo(mockMvc)
                .build();
    }

    protected static JwtAuthenticationToken jwtAuthenticationToken() {
        return new JwtAuthenticationToken(jwt().build(), AuthorityUtils.createAuthorityList());
    }

    public static Jwt.Builder jwt() {
        return Jwt.withTokenValue("token")
                .header("alg", "none")
                .audience(List.of("https://audience.example.org"))
                .expiresAt(Instant.MAX)
                .issuedAt(Instant.MIN)
                .issuer("https://issuer.example.org")
                .jti("jti")
                .notBefore(Instant.MIN)
                .subject("mock-test-subject");
    }
}
