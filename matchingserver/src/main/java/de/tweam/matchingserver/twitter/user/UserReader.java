package de.tweam.matchingserver.twitter.user;

import de.tweam.matchingserver.twitter.TwitterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

@Component
public class UserReader {
    private final TwitterProvider twitterProvider;

    @Autowired
    public UserReader(TwitterProvider twitterProvider) {
        this.twitterProvider = twitterProvider;
    }

    public User read(String handle) throws TwitterException {
        Twitter twitter = twitterProvider.getTwitterInstance();
        return twitter.showUser(handle);
    }
}
