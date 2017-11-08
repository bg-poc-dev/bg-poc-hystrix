package com.bg.poc.bgpochystrix.endpoint;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class JockClientEndpoint {

    private static final String URL = "http://localhost:8080/jock?firstName={firstName}&lastName={lastName}";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Hystrix Circuit-Breaker ломается если в течении времени равного значению circuitBreaker.sleepWindowInMilliseconds
     * количество запросов как минимум равно circuitBreaker.requestVolumeThreshold и
     * процент ошибок превышает значение circuitBreaker.errorThresholdPercentag.
     * <p>
     * Значение circuitBreaker.requestVolumeThreshold - это количество вызовов которые должны быть сделаны через circuitBreaker
     * прежде чем он начнет считать процент неудачных вызовов.
     *
     * @param firstName - irrelevant
     * @param lastName  - irrelevant
     * @return - irrelevant
     */
    @GetMapping(path = "/jocker")
    @HystrixCommand(fallbackMethod = "fallback", commandProperties =
            {@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "2000")})
    public String echo(@RequestParam(value = "firstName", defaultValue = "Jon") String firstName, @RequestParam(value = "lastName", defaultValue = "Do") String lastName) {
        return restTemplate.getForObject(URL, String.class, firstName, lastName);
    }

    private String fallback(String arg1, String arg2) {
        return "oops, :)";
    }
}
