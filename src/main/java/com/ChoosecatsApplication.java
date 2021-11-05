package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChoosecatsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChoosecatsApplication.class, args);
    }

    /*Бин использовался для того, чтобы иметь возможность смотреть встроенную базу H2 из intellij idea.
    Без него состояние базы можно отслеживать здесь /catdb-console
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
    */
}
