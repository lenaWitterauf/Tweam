package de.tweam.matchingserver.twitter.tweets;

import de.tweam.matchingserver.twitter.TwitterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class TweetReader {
    private final TwitterProvider twitterProvider;

    @Autowired
    public TweetReader(TwitterProvider twitterProvider) {
        this.twitterProvider = twitterProvider;
    }

    public List<Status> read(User user) throws TwitterException {
        Twitter twitter = twitterProvider.getTwitterInstance();
        Query query = new Query();
        query.setQuery("from:" + user.getScreenName());

        List<Status> allTweets = new ArrayList<>();
        QueryResult result;
        do {
            result = twitter.search(query);
            allTweets.addAll(result.getTweets());
        } while ((query = result.nextQuery()) != null);

        return allTweets;
    }
}
