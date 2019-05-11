package de.tweam.matchingserver.twitter.user;

import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

@Service
public class UserReader {
    public User read(String username) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        return twitter.showUser(username);
    }
}
