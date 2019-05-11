package de.tweam.matchingserver.matching.scores;

import de.tweam.matchingserver.data.Person;
import de.tweam.matchingserver.data.PersonRepository;
import de.tweam.matchingserver.twitter.tweets.TweetContentReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

import java.util.*;

@Component
public class TweetContentPersonScorer implements PersonScorer {
    private final TweetContentReader tweetContentReader;
    private final PersonRepository personRepository;

    @Autowired
    public TweetContentPersonScorer(TweetContentReader tweetContentReader, PersonRepository personRepository) {
        this.tweetContentReader = tweetContentReader;
        this.personRepository = personRepository;
    }

    @Override
    public double getUserScore(Person onePerson, Person otherPerson) throws TwitterException {
        maybeUpdateUserTweets(onePerson);
        maybeUpdateUserTweets(otherPerson);

        List<String> oneUserTweetContents = onePerson.getUserTweets();
        List<String> otherUserTweetContents = otherPerson.getUserTweets();

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


    private void maybeUpdateUserTweets(Person person) throws TwitterException {
        if (person.getUserTweets() == null || person.getUserTweets().isEmpty()) {
            person.setUserTweets(new ArrayList<>(tweetContentReader.readTweetContents(person.getTwitterHandle())));
            personRepository.saveAndFlush(person);
        }
    }
}
