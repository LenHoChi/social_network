package com.example;

import com.example.model.Relationship;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.RelationshipService;
import com.example.service.impl.RelationshipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;

@SpringBootApplication

public class SocialNetworkApplication implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    public static void main(String[] args) {
        SpringApplication.run(SocialNetworkApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello world!");
        User user1 = new User("zoombiev1@gmail.com");
        User user2 = new User("zoombiev2@gmail.com");
        User user3 = new User("zoombiev1@gmail.com");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
}
