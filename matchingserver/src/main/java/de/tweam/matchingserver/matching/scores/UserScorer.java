package de.tweam.matchingserver.matching.scores;

import de.tweam.matchingserver.data.User;
import twitter4j.TwitterException;

public interface UserScorer {
    double getUserScore(User oneUser, User otherUser) throws TwitterException;
}
