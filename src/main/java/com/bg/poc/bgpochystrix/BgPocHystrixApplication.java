package com.bg.poc.bgpochystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.stream.IntStream;

@SpringBootApplication
@EnableHystrix
public class BgPocHystrixApplication {

    private static final String URL = "http://localhost:8080/jocker?firstName={firstName}&lastName={lastName}";

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (s) -> {
            IntStream.range(1, 20).forEach(i -> {
                String answer = restTemplate.getForObject(URL, String.class, "Bob", "The " + i + "th");
                System.out.println(answer);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        };
    }

    public static void main(String[] args) {
        SpringApplication.run(BgPocHystrixApplication.class, args);
    }
}
