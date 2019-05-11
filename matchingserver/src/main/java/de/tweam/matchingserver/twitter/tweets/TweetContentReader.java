package de.tweam.matchingserver.twitter.tweets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TweetContentReader {
    @Autowired
    private TweetReader tweetReader;

    public List<String> readTweetContents(String twitterScreenName) throws TwitterException {
        List<Status> tweets = tweetReader.read(twitterScreenName);
        return tweets.stream().map(Status::getText).collect(Collectors.toList());
    }

    public List<String> readTweetContents(User user) throws TwitterException {
        List<Status> tweets = tweetReader.read(user);
        return tweets.stream().map(Status::getText).collect(Collectors.toList());
    }
}
