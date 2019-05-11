package de.tweam.matchingserver.twitter.tweets;

import org.springframework.stereotype.Service;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class TweetReader {
    public List<String> read(User user) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query();
        query.setQuery("from:" + user.getScreenName());

        List<String> tweetContents = new ArrayList<>();
        QueryResult result;
        do {
            result = twitter.search(query);
            result.getTweets().forEach(tweet -> tweetContents.add(tweet.getText()));
        } while ((query = result.nextQuery()) != null);

        return tweetContents;
    }
}
