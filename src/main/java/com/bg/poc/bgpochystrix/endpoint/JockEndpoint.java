package com.bg.poc.bgpochystrix.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class JockEndpoint {

    private static final String URL = "http://api.icndb.com/jokes/random?firstName={firstName}&lastName={lastName}";

    private AtomicLong counter = new AtomicLong();

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(path = "/jock")
    public ResponseEntity<String> jock(@RequestParam(value = "firstName", defaultValue = "Jon") String firstName, @RequestParam(value = "lastName", defaultValue = "Do") String lastName) {
        long currentCounter = counter.incrementAndGet();

        if (currentCounter > 2 && currentCounter < 5) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return ResponseEntity.ok(restTemplate.getForObject(URL, String.class, firstName, lastName));
    }
}
