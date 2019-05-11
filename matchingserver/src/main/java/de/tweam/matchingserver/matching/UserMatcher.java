package de.tweam.matchingserver.matching;

import de.tweam.matchingserver.data.User;
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

    public Map<User, List<User>> calculateMatches(List<User> usersToMatch) {
        Map<User, Map<User, Double>> matching = calculatePerUserMatchings(usersToMatch);
        Map<User, List<User>> rankedMatches = new HashMap<>();
        for (User user : matching.keySet()) {
            List<User> sortedRanking = matching.get(user).entrySet().stream().sorted(
                    Collections.reverseOrder(Comparator.comparingDouble(Map.Entry::getValue))
            ).map(Map.Entry::getKey).collect(Collectors.toList());
            rankedMatches.put(user, sortedRanking);
        }
        return rankedMatches;
    }

    private Map<User, Map<User, Double>> calculatePerUserMatchings(List<User> usersToMatch) {
        Map<User, Map<User, Double>> matching = new HashMap<>();
        for (User user : usersToMatch) {
            Map<User, Double> userMatching = matching.getOrDefault(user, new HashMap<>());
            for (User otherUser : usersToMatch) {
                if (userMatching.containsKey(otherUser)) {
                    continue;
                }

                Map<User, Double> otherUserMatching = matching.getOrDefault(user, new HashMap<>());
                if (otherUserMatching.containsKey(user)) {
                    userMatching.put(otherUser, otherUserMatching.get(user));
                    matching.put(user, userMatching);
                    continue;
                }

                double score;
                try {
                    score = userScorer.getUserScore(user, otherUser);
                } catch (TwitterException e) {
                    System.err.println("Error matching users!");
                    e.printStackTrace();
                    score = 0;
                }

                userMatching.put(otherUser, score);
                otherUserMatching.put(user, score);
                matching.put(user, userMatching);
                matching.put(otherUser, otherUserMatching);
            }
        }

        return matching;
    }
}
