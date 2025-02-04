package org.myapp.tests;

import org.junit.jupiter.api.Test;
import org.myapp.config.MyConfig;
import org.myapp.steps.RestStepsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MyConfig.class)
@ActiveProfiles("dev")
public class SmokeTest {

    @Autowired
    private RestStepsService restStepsService;

    @Test
    void firstSmokeTest() {

        restStepsService.callRestService();
    }
}
