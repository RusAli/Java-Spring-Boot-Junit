package org.myapp.config;

import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.pem.util.PemUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.myapp.jdbc.JdbcStepsService;
import org.myapp.kafka.KafkaService;
import org.myapp.rest.RestStepsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestClient;

import javax.net.ssl.SSLContext;
import java.nio.file.Path;
import java.util.List;

@Configuration
public class MyConfig {

    @Bean
    public RestClient buildRestClient(@Value("${base.url}") String url) {

        SSLFactory sslFactory = SSLFactory.builder()
//                .withUnsafeHostnameVerifier() //https site
//                .withUnsafeTrustMaterial()  //don't need pem
                .withTrustMaterial(PemUtils.loadTrustMaterial(
                        Path.of("src/test/resources/ca.pem")))
                .withIdentityMaterial(PemUtils.loadIdentityMaterial(
                        Path.of("src/test/resources/tls.crt"),
                        Path.of("src/test/resources/tls.key")))
                .build();
        SSLContext sslContext = sslFactory.getSslContext();

        JdkClientHttpRequestFactory jdkClientHttpRequestFactory =
                new org.springframework.http.client.JdkClientHttpRequestFactory(
                        java.net.http.HttpClient.newBuilder()
                                .sslContext(sslContext)
                                .sslParameters(sslFactory.getSslParameters())
                                .build()
                );

        return RestClient.builder()
                .requestFactory(jdkClientHttpRequestFactory)
                .baseUrl(url)
                .build();
    }

    @Bean
    public RestStepsService buildRestStepService(RestClient restClient) {
        return new RestStepsService(restClient);
    }

    @Bean
    public JdbcStepsService buildJdbcService(@Autowired JdbcClient jdbcClient) {
        return new JdbcStepsService(jdbcClient);
    }

    @Bean
    public KafkaService buildKafkaService(
            @Autowired KafkaTemplate<String, String> kafkaTemplate,
            @Autowired List<ConsumerRecord<String, String>> consumerRecords) {

        return new KafkaService(kafkaTemplate, consumerRecords);
    }


}
