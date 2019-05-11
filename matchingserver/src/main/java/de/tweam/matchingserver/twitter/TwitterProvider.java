package de.tweam.matchingserver.twitter;

import org.springframework.stereotype.Component;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

@Component
public class TwitterProvider {
    public Twitter getTwitterInstance() {
        return TwitterFactory.getSingleton();
    }
}
