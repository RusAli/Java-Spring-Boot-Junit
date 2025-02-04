package org.myapp.rest;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.springframework.web.client.RestClient;

public class RestStepsService {

    private final RestClient restClient;

    public RestStepsService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Attachment
    @Step("GET /api/users/2")
    public String callRestService() {

        return restClient.get()
                .uri("/api/users/2")
                .retrieve()
                .body(String.class);
    }

}
