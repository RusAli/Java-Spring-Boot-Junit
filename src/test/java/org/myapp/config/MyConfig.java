package org.myapp.config;

import org.myapp.steps.RestStepsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class MyConfig {

    @Bean
    public RestClient buildRestClient(@Value("${base.url}") String url){
        return RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Bean
    public RestStepsService buildRestStepService(RestClient restClient){
        return new RestStepsService(restClient);
    }


}
