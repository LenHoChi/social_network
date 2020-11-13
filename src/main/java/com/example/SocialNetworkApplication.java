package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;

@SpringBootApplication
public class SocialNetworkApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SocialNetworkApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Xin cao moi nguoi na!");
    }
}
