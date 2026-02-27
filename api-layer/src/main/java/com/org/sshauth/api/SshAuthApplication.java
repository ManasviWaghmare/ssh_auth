package com.org.sshauth.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.org.sshauth.api", "com.org.sshauth.core" })
@EnableJpaRepositories(basePackages = "com.org.sshauth.core.repository")
@EntityScan(basePackages = "com.org.sshauth.core.model")
public class SshAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SshAuthApplication.class, args);
    }
}
