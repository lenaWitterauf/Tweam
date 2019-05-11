package de.tweam.matchingserver.matching.scores;

import de.tweam.matchingserver.data.User;
import de.tweam.matchingserver.twitter.tweets.TweetContentReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TweetContentUserScorer implements UserScorer {
    private final TweetContentReader tweetContentReader;

    @Autowired
    public TweetContentUserScorer(TweetContentReader tweetContentReader) {
        this.tweetContentReader = tweetContentReader;
    }

    @Override
    public double getUserScore(User oneUser, User otherUser) throws TwitterException {
        List<String> oneUserTweetContents = tweetContentReader.readTweetContents(oneUser.getTwitterHandle());
        List<String> otherUserTweetContents = tweetContentReader.readTweetContents(otherUser.getTwitterHandle());

        if (oneUserTweetContents.isEmpty() || otherUserTweetContents.isEmpty()) {
            return -1;
        }

        Map<String, Integer> oneUserWords = countWordOccurences(oneUserTweetContents);
        Map<String, Integer> otherUserWords = countWordOccurences(otherUserTweetContents);

        int allWordCount = oneUserWords.values().stream().reduce(0, (a, b) -> a + b)
                + otherUserWords.values().stream().reduce(0, (a, b) -> a + b);
        int similarWordCount = oneUserWords.entrySet().stream().filter(
                stringIntegerEntry -> otherUserWords.containsKey(stringIntegerEntry.getKey())
        ).map(Map.Entry::getValue).reduce(0, (a, b) -> a + b) * 2;

        return (double) similarWordCount / (double) allWordCount;
    }

    private Map<String, Integer> countWordOccurences(List<String> tweetList) {
        Map<String, Integer> wordCount = new HashMap<>();
        for (String tweet : tweetList) {
            List<String> words = Arrays.asList(tweet.split("\\s+"));
            for (String word : words) {
                Integer oldCount = wordCount.getOrDefault(word, 0);
                wordCount.put(word, oldCount + 1);
            }
        }

        return wordCount;
    }
}
