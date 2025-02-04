package org.myapp.config;

import org.myapp.jdbc.JdbcStepsService;
import org.myapp.rest.RestStepsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.client.RestClient;

@Configuration
public class MyConfig {

    @Bean
    public RestClient buildRestClient(@Value("${base.url}") String url) {
        return RestClient.builder()
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


}
