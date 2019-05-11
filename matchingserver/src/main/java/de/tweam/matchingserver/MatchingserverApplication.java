package de.tweam.matchingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import twitter4j.TwitterException;

@SpringBootApplication
public class MatchingserverApplication {

    public static void main(String[] args) throws TwitterException {
        ConfigurableApplicationContext run = SpringApplication.run(MatchingserverApplication.class, args);
    }

}
