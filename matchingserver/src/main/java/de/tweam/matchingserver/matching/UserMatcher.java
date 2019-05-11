package de.tweam.matchingserver.matching;

import de.tweam.matchingserver.data.Person;
import de.tweam.matchingserver.matching.scores.UserScorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserMatcher {
    private final UserScorer userScorer;

    @Autowired
    public UserMatcher(@Qualifier("tweetContentUserScorer") UserScorer userScorer) {
        this.userScorer = userScorer;
    }

    public Map<Person, List<Person>> calculateMatches(List<Person> usersToMatches) {
        Map<Person, Map<Person, Double>> matching = calculatePerUserMatchings(usersToMatches);
        Map<Person, List<Person>> rankedMatches = new HashMap<>();
        for (Person person : matching.keySet()) {
            List<Person> sortedRanking = matching.get(person).entrySet().stream().sorted(
                    Collections.reverseOrder(Comparator.comparingDouble(Map.Entry::getValue))
            ).map(Map.Entry::getKey).collect(Collectors.toList());
            rankedMatches.put(person, sortedRanking);
        }
        return rankedMatches;
    }

    private Map<Person, Map<Person, Double>> calculatePerUserMatchings(List<Person> usersToMatches) {
        Map<Person, Map<Person, Double>> matching = new HashMap<>();
        for (Person person : usersToMatches) {
            Map<Person, Double> userMatching = matching.getOrDefault(person, new HashMap<>());
            for (Person otherPerson : usersToMatches) {
                if (userMatching.containsKey(otherPerson)) {
                    continue;
                }

                Map<Person, Double> otherUserMatching = matching.getOrDefault(person, new HashMap<>());
                if (otherUserMatching.containsKey(person)) {
                    userMatching.put(otherPerson, otherUserMatching.get(person));
                    matching.put(person, userMatching);
                    continue;
                }

                double score;
                try {
                    score = userScorer.getUserScore(person, otherPerson);
                } catch (TwitterException e) {
                    System.err.println("Error matching users!");
                    e.printStackTrace();
                    score = 0;
                }

                userMatching.put(otherPerson, score);
                otherUserMatching.put(person, score);
                matching.put(person, userMatching);
                matching.put(otherPerson, otherUserMatching);
            }
        }

        return matching;
    }
}
