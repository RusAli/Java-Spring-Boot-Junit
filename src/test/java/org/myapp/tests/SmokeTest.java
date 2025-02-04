package org.myapp.tests;

import org.junit.jupiter.api.Test;
import org.myapp.config.MyConfig;
import org.myapp.jdbc.JdbcStepsService;
import org.myapp.rest.RestStepsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ActiveProfiles("dev")
@EnableAutoConfiguration
@SpringBootTest(classes = MyConfig.class)
public class SmokeTest {

    @Autowired
    RestStepsService restStepsService;

    @Autowired
    JdbcStepsService jdbcStepsService;

    @Test
    void restTest() {
        String response = restStepsService.callRestService();
        assertThat(response, is(jsonPartEquals("data.id", 2)));
    }

    @Test
    void jdbcTest(){
       String result = jdbcStepsService.callDb();
       assertThat(result,is("1"));

    }
}
