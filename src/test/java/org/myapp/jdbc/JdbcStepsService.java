package org.myapp.jdbc;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

@AllArgsConstructor
public class JdbcStepsService {

    private JdbcClient jdbcClient;

    @Attachment
    @Step("Select BD")
    public String callDb(){
        return jdbcClient.sql("SELECT 1").query(String.class).single();
    }
}
