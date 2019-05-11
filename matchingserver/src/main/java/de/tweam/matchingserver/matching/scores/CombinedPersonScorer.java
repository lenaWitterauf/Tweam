package de.tweam.matchingserver.matching.scores;

import de.tweam.matchingserver.data.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

@Component
public class CombinedPersonScorer implements PersonScorer {
    private final FollowerPersonScorer followerPersonScorer;
    private final TweetContentPersonScorer tweetContentPersonScorer;

    @Autowired
    public CombinedPersonScorer(FollowerPersonScorer followerPersonScorer, TweetContentPersonScorer tweetContentPersonScorer) {
        this.followerPersonScorer = followerPersonScorer;
        this.tweetContentPersonScorer = tweetContentPersonScorer;
    }

    @Override
    public double getUserScore(Person onePerson, Person otherPerson) throws TwitterException {
        double tweetContentScore = tweetContentPersonScorer.getUserScore(onePerson, otherPerson);
        double followerPersonScore = followerPersonScorer.getUserScore(onePerson, otherPerson);
        if (tweetContentScore == -1 && followerPersonScore == -1) {
            System.err.println("No information for both users :-(");
            return 0;
        }

        if (tweetContentScore == -1) {
            return followerPersonScore;
        }

        if (followerPersonScore == -1) {
            return tweetContentScore;
        }

        return (followerPersonScore + tweetContentScore) / 2;
    }
}
