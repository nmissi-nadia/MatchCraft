package com.matchcraft.config;

import com.matchcraft.service.GithubSyncService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(GithubSyncService githubSyncService) {
        return args -> {
            System.out.println("Executing DataSeeder on startup...");
            githubSyncService.syncProjectsFromGithub();
            System.out.println("DataSeeder execution completed.");
        };
    }
}
