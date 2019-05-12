package de.tweam.matchingserver.matching.scores;

import de.tweam.matchingserver.data.Person;
import de.tweam.matchingserver.data.PersonRepository;
import de.tweam.matchingserver.twitter.tweets.TweetContentReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public double getUserScore(Person onePerson, Person otherPerson) {
        List<String> oneUserTweetContents = onePerson.getUserTweets();
        List<String> otherUserTweetContents = otherPerson.getUserTweets();

        if (oneUserTweetContents.isEmpty() || otherUserTweetContents.isEmpty()) {
            return -1;
        }

        Map<String, Integer> oneUserWords = countWordOccurences(oneUserTweetContents);
        Map<String, Integer> otherUserWords = countWordOccurences(otherUserTweetContents);

        int allWordCount = oneUserWords.values().stream().reduce(0, (a, b) -> a + b)
                + otherUserWords.values().stream().reduce(0, (a, b) -> a + b)
                + onePerson.getUserKeywords().size()
                + otherPerson.getUserKeywords().size();
        int similarWordCount = countSimilarWords(otherPerson, oneUserWords, otherUserWords)
                + countSimilarWords(onePerson, otherUserWords, oneUserWords)
                + (int) onePerson.getUserKeywords().stream().filter(word -> otherPerson.getUserKeywords().contains(word)).count() * 2;

        return (double) similarWordCount / (double) allWordCount;
    }

    private int countSimilarWords(Person otherPerson, Map<String, Integer> oneUserWords, Map<String, Integer> otherUserWords) {
        return oneUserWords.entrySet().stream().filter(
                stringIntegerEntry -> otherUserWords.containsKey(stringIntegerEntry.getKey()) || otherPerson.getUserKeywords().contains(stringIntegerEntry.getKey())
        ).map(entry -> entry.getValue() * (otherPerson.getUserKeywords().contains(entry.getKey()) ? 2 : 1)).reduce(0, (a, b) -> a + b);
    }

    private Map<String, Integer> countWordOccurences(List<String> tweetList) {
        Map<String, Integer> wordCount = new HashMap<>();
        for (String tweet : tweetList) {
            String[] words = tweet.split("\\s+");
            for (String word : words) {
                Integer oldCount = wordCount.getOrDefault(word.toLowerCase(), 0);
                wordCount.put(word.toLowerCase(), oldCount + 1);
            }
        }

        return wordCount;
    }
}
